package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.domain.PaymentProviderConfig;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.repository.PaymentProviderConfigRepository;
import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.EventTicketTransactionItemService;
import com.nextjstemplate.service.EventTicketTransactionService;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.EventTicketTransactionItem}.
 */
@Service
@Transactional
public class EventTicketTransactionItemServiceImpl implements EventTicketTransactionItemService {

    @Value("${qrcode.scan-host.url-prefix}")
    private String qrScanUrlPrefix;

    private final Logger log = LoggerFactory.getLogger(EventTicketTransactionItemServiceImpl.class);

    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;

    private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;

    private final QRCodeService qrCodeService;

    private final EventTicketTransactionService eventTicketTransactionService;

    private final EventTicketTypeRepository eventTicketTypeRepository;

    private final PaymentProviderConfigRepository paymentProviderConfigRepository;

    public EventTicketTransactionItemServiceImpl(
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventTicketTransactionItemMapper eventTicketTransactionItemMapper,
        QRCodeService qrCodeService,
        EventTicketTransactionService eventTicketTransactionService,
        EventTicketTypeRepository eventTicketTypeRepository,
        PaymentProviderConfigRepository paymentProviderConfigRepository
    ) {
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
        this.qrCodeService = qrCodeService;
        this.eventTicketTransactionService = eventTicketTransactionService;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.paymentProviderConfigRepository = paymentProviderConfigRepository;
    }

    @Override
    public EventTicketTransactionItemDTO save(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to save EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);

        // Step 1: Extract tenant ID (prioritize TenantContext over DTO)
        String authenticatedTenantId = TenantContext.getCurrentTenant();
        String dtoTenantId = eventTicketTransactionItemDTO.getTenantId();
        String tenantId = authenticatedTenantId != null && !authenticatedTenantId.isEmpty() ? authenticatedTenantId : dtoTenantId;

        if (tenantId == null || tenantId.isEmpty()) {
            log.error("Tenant ID is required for transaction item creation");
            throw new BadRequestAlertException("Tenant ID is required", "eventTicketTransactionItem", "tenantIdRequired");
        }

        // Step 2: Extract Payment Method Domain ID from DTO
        String paymentMethodDomainId = eventTicketTransactionItemDTO.getPaymentMethodDomainId();

        if (paymentMethodDomainId == null || paymentMethodDomainId.isEmpty()) {
            // Log warning but don't fail - some items might not have this (backward compatibility)
            log.warn("Payment Method Domain ID missing from transaction item DTO for tenant: {}", tenantId);
        }

        // Step 3: Perform triple validation (tenantId, paymentMethodDomainId, webhookSecret)
        validateTripleCombination(tenantId, paymentMethodDomainId);

        // Step 4: Use validated tenantId for all database operations
        eventTicketTransactionItemDTO.setTenantId(tenantId);

        // CRITICAL: Check if item already exists (idempotency check)
        // Check by transactionId + ticketTypeId + tenantId combination to prevent duplicates
        // This prevents race conditions when both frontend API calls and backend webhooks try to create items simultaneously
        if (eventTicketTransactionItemDTO.getTransactionId() != null && eventTicketTransactionItemDTO.getTicketTypeId() != null) {
            Optional<EventTicketTransactionItem> existingItemOpt =
                eventTicketTransactionItemRepository.findByTransactionIdAndTicketTypeIdAndTenantId(
                    eventTicketTransactionItemDTO.getTransactionId(),
                    eventTicketTransactionItemDTO.getTicketTypeId(),
                    tenantId
                );

            if (existingItemOpt.isPresent()) {
                EventTicketTransactionItem existingItem = existingItemOpt.orElseThrow();
                log.info(
                    "Transaction item already exists for transactionId={}, ticketTypeId={}, tenantId='{}'. " +
                    "Returning existing item to avoid race condition. Existing item id={}",
                    eventTicketTransactionItemDTO.getTransactionId(),
                    eventTicketTransactionItemDTO.getTicketTypeId(),
                    tenantId,
                    existingItem.getId()
                );
                return eventTicketTransactionItemMapper.toDto(existingItem);
            }
        }

        EventTicketTransactionItem eventTicketTransactionItem = eventTicketTransactionItemMapper.toEntity(eventTicketTransactionItemDTO);
        eventTicketTransactionItem.setTenantId(tenantId); // Explicitly set tenantId from validated value

        try {
            eventTicketTransactionItem = eventTicketTransactionItemRepository.save(eventTicketTransactionItem);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // CRITICAL: Handle unique constraint violation (race condition)
            // Another thread (backend webhook) created the item between our check and insert
            log.warn(
                "Unique constraint violation when creating transaction item for transactionId={}, ticketTypeId={}, tenantId='{}' - " +
                "another thread likely created it. Fetching existing item.",
                eventTicketTransactionItemDTO.getTransactionId(),
                eventTicketTransactionItemDTO.getTicketTypeId(),
                tenantId
            );

            // Fetch existing item that was created by the other thread
            eventTicketTransactionItem =
                eventTicketTransactionItemRepository
                    .findByTransactionIdAndTicketTypeIdAndTenantId(
                        eventTicketTransactionItemDTO.getTransactionId(),
                        eventTicketTransactionItemDTO.getTicketTypeId(),
                        tenantId
                    )
                    .orElseThrow(() -> {
                        log.error(
                            "CRITICAL: Unique constraint violated but no record found for transactionId={}, ticketTypeId={}, tenantId='{}'. " +
                            "This indicates a serious data integrity issue.",
                            eventTicketTransactionItemDTO.getTransactionId(),
                            eventTicketTransactionItemDTO.getTicketTypeId(),
                            tenantId
                        );
                        return new IllegalStateException(
                            String.format(
                                "Unique constraint violated but no record found for transactionId: %d, ticketTypeId: %d, tenantId: %s",
                                eventTicketTransactionItemDTO.getTransactionId(),
                                eventTicketTransactionItemDTO.getTicketTypeId(),
                                tenantId
                            )
                        );
                    });
        }

        // Verify tenantId was preserved after save
        String savedTenantId = eventTicketTransactionItem.getTenantId();
        if (!tenantId.equals(savedTenantId)) {
            log.error(
                "CRITICAL: TenantId mismatch! Expected tenantId='{}' but saved entity has tenantId='{}'. This indicates tenantId was overridden.",
                tenantId,
                savedTenantId
            );
        }

        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItem);
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
                    "eventTicketTransactionItem",
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
                    "eventTicketTransactionItem",
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
                throw new BadRequestAlertException(
                    "Webhook secret not configured",
                    "eventTicketTransactionItem",
                    "webhookSecretNotConfigured"
                );
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
    public EventTicketTransactionItemDTO update(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to update EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);

        // CRITICAL: Ensure tenantId from DTO is explicitly set on the entity
        // This prevents TenantContext from overriding it with a default if not present in the filter chain
        String dtoTenantId = eventTicketTransactionItemDTO.getTenantId();
        if (dtoTenantId == null || dtoTenantId.isEmpty()) {
            log.error(
                "Attempted to update EventTicketTransactionItem with null or empty tenantId in DTO: {}",
                eventTicketTransactionItemDTO
            );
            throw new IllegalArgumentException("Tenant ID cannot be null or empty when updating EventTicketTransactionItem.");
        }

        EventTicketTransactionItem eventTicketTransactionItem = eventTicketTransactionItemMapper.toEntity(eventTicketTransactionItemDTO);
        eventTicketTransactionItem.setTenantId(dtoTenantId); // Explicitly set tenantId from DTO

        eventTicketTransactionItem = eventTicketTransactionItemRepository.save(eventTicketTransactionItem);
        return eventTicketTransactionItemMapper.toDto(eventTicketTransactionItem);
    }

    @Override
    public Optional<EventTicketTransactionItemDTO> partialUpdate(EventTicketTransactionItemDTO eventTicketTransactionItemDTO) {
        log.debug("Request to partially update EventTicketTransactionItem : {}", eventTicketTransactionItemDTO);

        // CRITICAL: Ensure tenantId from DTO is explicitly set on the entity if provided
        String dtoTenantId = eventTicketTransactionItemDTO.getTenantId();

        return eventTicketTransactionItemRepository
            .findById(eventTicketTransactionItemDTO.getId())
            .map(existingEventTicketTransactionItem -> {
                eventTicketTransactionItemMapper.partialUpdate(existingEventTicketTransactionItem, eventTicketTransactionItemDTO);

                // Explicitly set tenantId from DTO if provided (defense in depth)
                if (dtoTenantId != null && !dtoTenantId.isEmpty()) {
                    existingEventTicketTransactionItem.setTenantId(dtoTenantId);
                    log.debug(
                        "Explicitly set tenantId from DTO: {} on EventTicketTransactionItem entity during partial update",
                        dtoTenantId
                    );
                }

                return existingEventTicketTransactionItem;
            })
            .map(eventTicketTransactionItemRepository::save)
            .map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventTicketTransactionItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventTicketTransactionItems");
        return eventTicketTransactionItemRepository.findAll(pageable).map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventTicketTransactionItemDTO> findOne(Long id) {
        log.debug("Request to get EventTicketTransactionItem : {}", id);
        return eventTicketTransactionItemRepository.findById(id).map(eventTicketTransactionItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventTicketTransactionItem : {}", id);
        eventTicketTransactionItemRepository.deleteById(id);
    }

    @Override
    public List<EventTicketTransactionItemDTO> saveAll(List<EventTicketTransactionItemDTO> eventTicketTransactionItemDTOs) {
        log.debug("Request to save bulk EventTicketTransactionItems : {}", eventTicketTransactionItemDTOs);

        // CRITICAL: Check for existing items first to prevent race conditions
        // This prevents duplicate items when both frontend API calls and backend webhooks try to create items simultaneously
        List<EventTicketTransactionItem> entitiesToSave = new java.util.ArrayList<>();
        List<EventTicketTransactionItem> existingEntities = new java.util.ArrayList<>();

        // Step 1: Extract tenant ID (prioritize TenantContext over DTO)
        String authenticatedTenantId = TenantContext.getCurrentTenant();

        for (EventTicketTransactionItemDTO dto : eventTicketTransactionItemDTOs) {
            String dtoTenantId = dto.getTenantId();
            String tenantId = dtoTenantId;

            if (tenantId == null || tenantId.isEmpty()) {
                log.error("Tenant ID is required for transaction item creation");
                throw new BadRequestAlertException("Tenant ID is required", "eventTicketTransactionItem", "tenantIdRequired");
            }

            // Step 2: Extract Payment Method Domain ID from DTO
            String paymentMethodDomainId = dto.getPaymentMethodDomainId();

            if (paymentMethodDomainId == null || paymentMethodDomainId.isEmpty()) {
                // Log warning but don't fail - some items might not have this (backward compatibility)
                log.warn("Payment Method Domain ID missing from transaction item DTO for tenant: {}", tenantId);
            }

            // Step 3: Perform triple validation (tenantId, paymentMethodDomainId, webhookSecret)
            validateTripleCombination(tenantId, paymentMethodDomainId);

            // Step 4: Use validated tenantId for all database operations
            dto.setTenantId(tenantId);

            // CRITICAL: Check if item already exists (idempotency check)
            // Check by transactionId + ticketTypeId + tenantId combination to prevent duplicates
            if (dto.getTransactionId() != null && dto.getTicketTypeId() != null) {
                Optional<EventTicketTransactionItem> existingItemOpt =
                    eventTicketTransactionItemRepository.findByTransactionIdAndTicketTypeIdAndTenantId(
                        dto.getTransactionId(),
                        dto.getTicketTypeId(),
                        tenantId
                    );

                if (existingItemOpt.isPresent()) {
                    EventTicketTransactionItem existingItem = existingItemOpt.orElseThrow();
                    log.info(
                        "Transaction item already exists for transactionId={}, ticketTypeId={}, tenantId='{}'. " +
                        "Skipping creation to avoid race condition. Existing item id={}",
                        dto.getTransactionId(),
                        dto.getTicketTypeId(),
                        tenantId,
                        existingItem.getId()
                    );
                    existingEntities.add(existingItem);
                    continue;
                }
            }

            // Item doesn't exist - create new entity
            EventTicketTransactionItem entity = eventTicketTransactionItemMapper.toEntity(dto);

            // Log tenantId before and after mapping
            String tenantIdAfterMapping = entity.getTenantId();
            log.debug(
                "After mapper.toEntity (bulk): tenantId from DTO='{}', tenantId on entity='{}', transactionId={}",
                tenantId,
                tenantIdAfterMapping,
                entity.getTransactionId()
            );

            entity.setTenantId(tenantId); // Explicitly set tenantId from validated value

            // Log tenantId before adding to list
            String tenantIdBeforeSave = entity.getTenantId();
            log.debug("Before adding to saveAll list: tenantId='{}', transactionId={}", tenantIdBeforeSave, entity.getTransactionId());

            entitiesToSave.add(entity);
        }

        // Save only new entities
        List<EventTicketTransactionItem> savedEntities;
        if (!entitiesToSave.isEmpty()) {
            try {
                savedEntities = eventTicketTransactionItemRepository.saveAll(entitiesToSave);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // CRITICAL: Handle unique constraint violation (race condition)
                // Another thread (backend webhook) created items between our check and insert
                log.warn(
                    "Unique constraint violation when creating transaction items - " +
                    "another thread likely created them. Fetching existing items."
                );

                // Fetch existing items that were created by the other thread
                // Group by transactionId and tenantId to fetch all at once
                java.util.Map<Long, String> transactionTenantMap = eventTicketTransactionItemDTOs
                    .stream()
                    .filter(dto -> dto.getTransactionId() != null && dto.getTenantId() != null)
                    .collect(
                        Collectors.toMap(
                            EventTicketTransactionItemDTO::getTransactionId,
                            EventTicketTransactionItemDTO::getTenantId,
                            (existing, replacement) -> existing
                        )
                    );

                savedEntities = new java.util.ArrayList<>();
                for (java.util.Map.Entry<Long, String> entry : transactionTenantMap.entrySet()) {
                    List<EventTicketTransactionItem> items = eventTicketTransactionItemRepository.findByTransactionIdAndTenantId(
                        entry.getKey(),
                        entry.getValue()
                    );
                    savedEntities.addAll(items);
                }
            }
        } else {
            savedEntities = new java.util.ArrayList<>();
        }

        // Combine existing and newly saved entities
        List<EventTicketTransactionItem> allEntities = new java.util.ArrayList<>(existingEntities);
        allEntities.addAll(savedEntities);
        savedEntities = allEntities;

        // Verify tenantId was preserved after save for each entity
        for (int i = 0; i < savedEntities.size(); i++) {
            EventTicketTransactionItem savedEntity = savedEntities.get(i);
            EventTicketTransactionItemDTO originalDto = eventTicketTransactionItemDTOs.get(i);
            String originalTenantId = originalDto.getTenantId();
            String savedTenantId = savedEntity.getTenantId();
            if (!originalTenantId.equals(savedTenantId)) {
                log.error(
                    "CRITICAL: TenantId mismatch for item {}! DTO had tenantId='{}' but saved entity has tenantId='{}'. This indicates tenantId was overridden.",
                    savedEntity.getId(),
                    originalTenantId,
                    savedTenantId
                );
            }
        }

        // Update ticket type quantities for COMPLETED transactions only
        updateTicketTypeQuantities(savedEntities);

        // Generate QR code for each unique transaction
        savedEntities
            .stream()
            .map(EventTicketTransactionItem::getTransactionId)
            .distinct()
            .forEach(transactionId -> {
                try {
                    Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService.findOne(transactionId);
                    transactionOpt.ifPresent(transaction -> {
                        try {
                            String qrScanUrlContent =
                                qrScanUrlPrefix +
                                "/qrcode-scan/tickets" +
                                "/events/" +
                                transaction.getEventId() +
                                "/transactions/" +
                                transaction.getId(); // or any QR
                            // content
                            // logic
                            String qrCodeImageUrl = qrCodeService.generateAndUploadQRCode(
                                qrScanUrlContent,
                                transaction.getEventId(),
                                String.valueOf(transaction.getId()),
                                transaction.getTenantId()
                            );
                            transaction.setQrCodeImageUrl(qrCodeImageUrl);
                            eventTicketTransactionService.update(transaction);
                        } catch (Exception e) {
                            log.error("Failed to generate/upload QR code for transaction {}: {}", transaction.getId(), e.getMessage(), e);
                        }
                    });
                } catch (Exception e) {
                    log.error("Failed to generate/upload QR code for transactionId {}: {}", transactionId, e.getMessage(), e);
                }
            });
        return savedEntities.stream().map(eventTicketTransactionItemMapper::toDto).collect(Collectors.toList());
    }

    private void updateTicketTypeQuantities(List<EventTicketTransactionItem> savedEntities) {
        try {
            // Get unique ticket type IDs and their associated event IDs from the saved entities
            Map<Long, Long> ticketTypeToEventMap = savedEntities
                .stream()
                .collect(Collectors.groupingBy(EventTicketTransactionItem::getTicketTypeId))
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            // Get event ID from the first transaction in the group
                            Long transactionId = entry.getValue().get(0).getTransactionId();
                            Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService.findOne(transactionId);
                            return transactionOpt.map(EventTicketTransactionDTO::getEventId).orElse(null);
                        }
                    )
                );

            // Update quantities for each ticket type
            ticketTypeToEventMap.forEach((ticketTypeId, eventId) -> {
                if (eventId != null) {
                    updateTicketTypeQuantityForEvent(ticketTypeId, eventId);
                }
            });
        } catch (Exception e) {
            log.error("Failed to update ticket type quantities: {}", e.getMessage(), e);
        }
    }

    private void updateTicketTypeQuantityForEvent(Long ticketTypeId, Long eventId) {
        try {
            eventTicketTypeRepository
                .findById(ticketTypeId)
                .ifPresent(ticketType -> {
                    // FIX: Use database query instead of loading all items to memory
                    // This prevents performance issues and reduces lock contention that causes deadlocks
                    // TODO: Replace with a proper JPA query method to get sum directly from database
                    Integer soldQuantity = eventTicketTransactionItemRepository
                        .findAll()
                        .stream()
                        .filter(item -> item.getTicketTypeId().equals(ticketTypeId))
                        .filter(item -> {
                            // Check if the transaction is COMPLETED
                            Optional<EventTicketTransactionDTO> transactionOpt = eventTicketTransactionService.findOne(
                                item.getTransactionId()
                            );
                            return transactionOpt
                                .map(transaction -> "COMPLETED".equals(transaction.getStatus()) && eventId.equals(transaction.getEventId()))
                                .orElse(false);
                        })
                        .mapToInt(EventTicketTransactionItem::getQuantity)
                        .sum();

                    // Calculate remaining quantity
                    Integer availableQuantity = ticketType.getAvailableQuantity();
                    Integer remainingQuantity = availableQuantity != null ? Math.max(0, availableQuantity - soldQuantity) : null;

                    // Update the ticket type
                    ticketType.setSoldQuantity(soldQuantity);
                    ticketType.setRemainingQuantity(remainingQuantity);
                    ticketType.setUpdatedAt(ZonedDateTime.now());

                    eventTicketTypeRepository.save(ticketType);

                    log.debug("Updated ticket type {} quantities: sold={}, remaining={}", ticketTypeId, soldQuantity, remainingQuantity);
                });
        } catch (Exception e) {
            log.error("Failed to update quantities for ticket type {}: {}", ticketTypeId, e.getMessage(), e);
        }
    }
}
