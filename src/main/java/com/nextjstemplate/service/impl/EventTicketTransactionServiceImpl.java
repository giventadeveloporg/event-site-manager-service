package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.PaymentProviderConfig;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.PaymentProviderConfigRepository;
import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionStatisticsDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.EventTicketTransaction}.
 */
@Service
@Transactional
public class EventTicketTransactionServiceImpl implements EventTicketTransactionService {

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionServiceImpl.class);

    private final EventTicketTransactionRepository eventTicketTransactionRepository;

    private final EventTicketTransactionMapper eventTicketTransactionMapper;

    private final PaymentProviderConfigRepository paymentProviderConfigRepository;

    public EventTicketTransactionServiceImpl(
        EventTicketTransactionRepository eventTicketTransactionRepository,
        EventTicketTransactionMapper eventTicketTransactionMapper,
        PaymentProviderConfigRepository paymentProviderConfigRepository
    ) {
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionMapper = eventTicketTransactionMapper;
        this.paymentProviderConfigRepository = paymentProviderConfigRepository;
    }

    @Override
    public EventTicketTransactionDTO save(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to save EventTicketTransaction : {}", eventTicketTransactionDTO);

        // Step 1: Extract tenant ID (prioritize TenantContext over DTO)
        //        String authenticatedTenantId = TenantContext.getCurrentTenant();
        String dtoTenantId = eventTicketTransactionDTO.getTenantId();
        String tenantId = dtoTenantId;

        if (tenantId == null || tenantId.isEmpty()) {
            log.error("Tenant ID is required for transaction creation");
            throw new BadRequestAlertException("Tenant ID is required", "eventTicketTransaction", "tenantIdRequired");
        }

        // Step 2: Extract Payment Method Domain ID from DTO
        String paymentMethodDomainId = eventTicketTransactionDTO.getPaymentMethodDomainId();

        if (paymentMethodDomainId == null || paymentMethodDomainId.isEmpty()) {
            // Log warning but don't fail - some transactions might not have this (backward compatibility)
            log.warn("Payment Method Domain ID missing from transaction DTO for tenant: {}", tenantId);
        }

        // Step 3: Perform triple validation (tenantId, paymentMethodDomainId, webhookSecret)
        validateTripleCombination(tenantId, paymentMethodDomainId);

        // Step 4: Use validated tenantId for all database operations
        eventTicketTransactionDTO.setTenantId(tenantId);

        EventTicketTransaction eventTicketTransaction = eventTicketTransactionMapper.toEntity(eventTicketTransactionDTO);

        // CRITICAL: Explicitly ensure tenantId from DTO is set on entity (defense in depth)
        // This prevents any potential override from TenantContext or other sources
        if (tenantId != null && !tenantId.isEmpty()) {
            eventTicketTransaction.setTenantId(tenantId);
            log.debug("Explicitly set tenantId from validated value: {} on EventTicketTransaction entity", tenantId);
        }

        eventTicketTransaction = eventTicketTransactionRepository.save(eventTicketTransaction);

        // Verify tenantId was preserved after save
        String savedTenantId = eventTicketTransaction.getTenantId();
        if (tenantId != null && !tenantId.equals(savedTenantId)) {
            log.error(
                "CRITICAL: TenantId mismatch! Expected tenantId='{}' but saved entity has tenantId='{}'. This indicates tenantId was overridden.",
                tenantId,
                savedTenantId
            );
        }

        return eventTicketTransactionMapper.toDto(eventTicketTransaction);
    }

    /**
     * Validate triple combination (tenantId, paymentMethodDomainId, webhookSecret).
     * This ensures that the combination exists in payment_provider_config table.
     *
     * @param tenantId Tenant ID
     * @param paymentMethodDomainId Payment Method Domain ID (optional)
     */
    private void validateTripleCombination(String tenantId, String paymentMethodDomainId) {
        PaymentProviderConfig config = null;

        if (paymentMethodDomainId != null && !paymentMethodDomainId.isEmpty()) {
            // Lookup by tenantId and paymentMethodDomainId
            config = paymentProviderConfigRepository.findByTenantIdAndPaymentMethodDomainId(tenantId, paymentMethodDomainId).orElse(null);

            if (config == null) {
                log.error(
                    "Triple validation failed: No payment_provider_config found for tenantId={}, paymentMethodDomainId={}",
                    tenantId,
                    paymentMethodDomainId
                );
                throw new BadRequestAlertException(
                    String.format(
                        "Invalid tenant/payment method domain combination: tenantId=%s, paymentMethodDomainId=%s",
                        tenantId,
                        paymentMethodDomainId
                    ),
                    "eventTicketTransaction",
                    "tripleValidationFailed"
                );
            }

            // Verify the combination matches
            if (config.getPaymentMethodDomainId() == null || !config.getPaymentMethodDomainId().equals(paymentMethodDomainId)) {
                log.error(
                    "Triple validation failed: Payment Method Domain ID mismatch. Expected={}, Got={}",
                    config.getPaymentMethodDomainId(),
                    paymentMethodDomainId
                );
                throw new BadRequestAlertException(
                    "Payment Method Domain ID mismatch",
                    "eventTicketTransaction",
                    "paymentMethodDomainIdMismatch"
                );
            }

            // Verify webhook_secret_encrypted exists
            if (config.getWebhookSecretEncrypted() == null || config.getWebhookSecretEncrypted().isEmpty()) {
                log.error(
                    "Triple validation failed: webhook_secret_encrypted is NULL or EMPTY for tenantId={}, paymentMethodDomainId={}",
                    tenantId,
                    paymentMethodDomainId
                );
                throw new BadRequestAlertException("Webhook secret not configured", "eventTicketTransaction", "webhookSecretNotConfigured");
            }

            log.info(
                "Triple validation successful: tenantId={}, paymentMethodDomainId={}, webhookSecretExists=true",
                tenantId,
                paymentMethodDomainId
            );
        } else {
            // Fallback: Lookup by tenantId only (for backward compatibility)
            config = paymentProviderConfigRepository.findByTenantIdAndProviderName(tenantId, PaymentProvider.STRIPE).orElse(null);

            if (config == null) {
                log.warn("No payment_provider_config found for tenantId={}, providerName=STRIPE", tenantId);
                // Don't fail - allow backward compatibility
            } else {
                log.debug("Found payment_provider_config for tenantId={} (fallback lookup without paymentMethodDomainId)", tenantId);
            }
        }
    }

    @Override
    public EventTicketTransactionDTO update(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to update EventTicketTransaction : {}", eventTicketTransactionDTO);
        EventTicketTransaction eventTicketTransaction = eventTicketTransactionMapper.toEntity(eventTicketTransactionDTO);
        eventTicketTransaction = eventTicketTransactionRepository.save(eventTicketTransaction);
        return eventTicketTransactionMapper.toDto(eventTicketTransaction);
    }

    @Override
    public Optional<EventTicketTransactionDTO> partialUpdate(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to partially update EventTicketTransaction : {}", eventTicketTransactionDTO);

        return eventTicketTransactionRepository
            .findById(eventTicketTransactionDTO.getId())
            .map(existingEventTicketTransaction -> {
                eventTicketTransactionMapper.partialUpdate(existingEventTicketTransaction, eventTicketTransactionDTO);

                return existingEventTicketTransaction;
            })
            .map(eventTicketTransactionRepository::save)
            .map(eventTicketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTicketTransactions");
        return eventTicketTransactionRepository.findAll(pageable).map(eventTicketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionDTO> findOne(Long id) {
        log.debug("Request to get EventTicketTransaction : {}", id);
        return eventTicketTransactionRepository.findById(id).map(eventTicketTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventTicketTransaction : {}", id);
        eventTicketTransactionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionDTO> findByStripePaymentIntentId(String stripePaymentIntentId) {
        log.debug("Request to find EventTicketTransaction by stripePaymentIntentId: {}", stripePaymentIntentId);
        return eventTicketTransactionRepository.findByStripePaymentIntentId(stripePaymentIntentId).map(eventTicketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionDTO> findByStripePaymentIntentIdAndTenantId(String stripePaymentIntentId, String tenantId) {
        log.debug("Request to find EventTicketTransaction by stripePaymentIntentId: {} and tenantId: {}", stripePaymentIntentId, tenantId);
        return eventTicketTransactionRepository
            .findByStripePaymentIntentIdAndTenantId(stripePaymentIntentId, tenantId)
            .map(eventTicketTransactionMapper::toDto);
    }

    @Override
    public EventTicketTransactionStatisticsDTO getEventStatistics(Long eventId) {
        List<EventTicketTransaction> transactions = eventTicketTransactionRepository
            .findAll()
            .stream()
            .filter(t -> t.getEventId() != null && t.getEventId().equals(eventId))
            .collect(Collectors.toList());
        int totalTicketsSold = transactions.stream().mapToInt(t -> t.getQuantity() != null ? t.getQuantity() : 0).sum();
        BigDecimal totalAmount = transactions
            .stream()
            .map(t -> t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFees = transactions
            .stream()
            .map(t -> {
                BigDecimal platformFee = t.getPlatformFeeAmount() != null ? t.getPlatformFeeAmount() : BigDecimal.ZERO;
                BigDecimal stripeFee = t.getStripeFeeAmount() != null ? t.getStripeFeeAmount() : BigDecimal.ZERO;
                return platformFee.add(stripeFee);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netAmount = totalAmount.subtract(totalFees);
        Map<String, Integer> ticketsByStatus = transactions
            .stream()
            .collect(
                Collectors.groupingBy(
                    t -> t.getStatus() != null ? t.getStatus() : "UNKNOWN",
                    Collectors.summingInt(t -> t.getQuantity() != null ? t.getQuantity() : 0)
                )
            );
        Map<String, BigDecimal> amountByStatus = transactions
            .stream()
            .collect(
                Collectors.groupingBy(
                    t -> t.getStatus() != null ? t.getStatus() : "UNKNOWN",
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        t -> t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO,
                        BigDecimal::add
                    )
                )
            );
        EventTicketTransactionStatisticsDTO stats = new EventTicketTransactionStatisticsDTO();
        stats.setEventId(eventId);
        stats.setTotalTicketsSold(totalTicketsSold);
        stats.setTotalAmount(totalAmount);
        stats.setNetAmount(netAmount);
        stats.setTicketsByStatus(ticketsByStatus);
        stats.setAmountByStatus(amountByStatus);
        return stats;
    }
}
