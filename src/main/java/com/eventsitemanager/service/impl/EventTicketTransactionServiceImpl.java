package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventTicketTransaction;
import com.eventsitemanager.domain.PaymentProviderConfig;
import com.eventsitemanager.domain.enumeration.PaymentProvider;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.errors.ConflictException;
import com.eventsitemanager.errors.UnprocessableEntityException;
import com.eventsitemanager.repository.EventTicketTransactionRepository;
import com.eventsitemanager.repository.PaymentProviderConfigRepository;
import com.eventsitemanager.security.TenantContext;
import com.eventsitemanager.service.EventTicketTransactionService;
import com.eventsitemanager.service.dto.CheckInAnalyticsDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionDTO;
import com.eventsitemanager.service.dto.EventTicketTransactionStatisticsDTO;
import com.eventsitemanager.service.dto.SalesAnalyticsDTO;
import com.eventsitemanager.service.mapper.EventTicketTransactionMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.eventsitemanager.domain.EventTicketTransaction}.
 */
@Service
@Transactional
public class EventTicketTransactionServiceImpl implements EventTicketTransactionService {

    private static final String ENTITY_NAME = "eventTicketTransaction";

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
    @CacheEvict(value = "eventTicketTransactions", allEntries = true)
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
    @CacheEvict(value = "eventTicketTransactions", allEntries = true)
    public EventTicketTransactionDTO update(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to update EventTicketTransaction : {}", eventTicketTransactionDTO);
        EventTicketTransaction eventTicketTransaction = eventTicketTransactionMapper.toEntity(eventTicketTransactionDTO);
        eventTicketTransaction = eventTicketTransactionRepository.save(eventTicketTransaction);
        return eventTicketTransactionMapper.toDto(eventTicketTransaction);
    }

    @Override
    @CacheEvict(value = "eventTicketTransactions", allEntries = true)
    public Optional<EventTicketTransactionDTO> partialUpdate(EventTicketTransactionDTO eventTicketTransactionDTO) {
        log.debug("Request to partially update EventTicketTransaction : {}", eventTicketTransactionDTO);

        return eventTicketTransactionRepository
            .findById(eventTicketTransactionDTO.getId())
            .map(existingEventTicketTransaction -> {
                // Validate transaction status before allowing check-in
                String currentStatus = existingEventTicketTransaction.getStatus();
                String newCheckInStatus = eventTicketTransactionDTO.getCheckInStatus();

                // If attempting to check in, validate transaction status
                if (newCheckInStatus != null && "CHECKED_IN".equals(newCheckInStatus)) {
                    // Task 7: Prevent check-in of REFUNDED or CANCELLED transactions
                    if ("REFUNDED".equals(currentStatus) || "CANCELLED".equals(currentStatus)) {
                        log.warn("Cannot check in transaction {} with status {}", existingEventTicketTransaction.getId(), currentStatus);
                        throw new UnprocessableEntityException(
                            String.format("Cannot check in transaction with status: %s", currentStatus),
                            ENTITY_NAME,
                            "invalidTransactionStatus"
                        );
                    }

                    // Task 6: Prevent duplicate check-in
                    String existingCheckInStatus = existingEventTicketTransaction.getCheckInStatus();
                    if ("CHECKED_IN".equals(existingCheckInStatus)) {
                        log.warn("Transaction {} is already checked in", existingEventTicketTransaction.getId());
                        throw new ConflictException("Transaction is already checked in", ENTITY_NAME, "duplicateCheckIn");
                    }

                    // Set checkInTime if not already set and checking in
                    if (existingEventTicketTransaction.getCheckInTime() == null && eventTicketTransactionDTO.getCheckInTime() == null) {
                        existingEventTicketTransaction.setCheckInTime(java.time.ZonedDateTime.now());
                        log.debug("Auto-set checkInTime for transaction {}", existingEventTicketTransaction.getId());
                    }
                }

                // Perform partial update
                eventTicketTransactionMapper.partialUpdate(existingEventTicketTransaction, eventTicketTransactionDTO);

                // Ensure updatedAt is set
                existingEventTicketTransaction.setUpdatedAt(java.time.ZonedDateTime.now());

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
    @Cacheable(value = "eventTicketTransactions", key = "#id", unless = "#result == null")
    public Optional<EventTicketTransactionDTO> findOne(Long id) {
        log.debug("Request to get EventTicketTransaction : {}", id);
        return eventTicketTransactionRepository.findById(id).map(eventTicketTransactionMapper::toDto);
    }

    @Override
    @CacheEvict(value = "eventTicketTransactions", allEntries = true)
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
        return getEventStatistics(eventId, null, null);
    }

    @Override
    public EventTicketTransactionStatisticsDTO getEventStatistics(Long eventId, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get statistics for event {} with date range: startDate={}, endDate={}", eventId, startDate, endDate);

        // Task 8: Get tenant ID from context for multi-tenant filtering
        String tenantId = TenantContext.getCurrentTenant();
        log.debug("Tenant ID from context: {}", tenantId);

        // Task 11: Filter by date range if provided, tenant, and exclude REFUNDED and CANCELLED transactions
        List<EventTicketTransaction> transactions = eventTicketTransactionRepository
            .findAll()
            .stream()
            .filter(t -> {
                // Filter by event ID
                if (t.getEventId() == null || !t.getEventId().equals(eventId)) {
                    return false;
                }

                // Task 8: Filter by tenant for multi-tenant security
                if (tenantId != null && !tenantId.isEmpty()) {
                    if (!tenantId.equals(t.getTenantId())) {
                        return false;
                    }
                }

                // Exclude REFUNDED and CANCELLED transactions
                String status = t.getStatus();
                if ("REFUNDED".equals(status) || "CANCELLED".equals(status)) {
                    return false;
                }

                // Filter by purchase date range if provided
                ZonedDateTime purchaseDate = t.getPurchaseDate();
                if (purchaseDate != null) {
                    LocalDate purchaseLocalDate = purchaseDate.toLocalDate();

                    if (startDate != null && purchaseLocalDate.isBefore(startDate)) {
                        return false;
                    }

                    if (endDate != null && purchaseLocalDate.isAfter(endDate)) {
                        return false;
                    }
                } else {
                    // If purchase date is null and date range is specified, exclude it
                    if (startDate != null || endDate != null) {
                        return false;
                    }
                }

                return true;
            })
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

        // Task 11: Include date range in response if provided
        if (startDate != null || endDate != null) {
            stats.setStartDate(startDate);
            stats.setEndDate(endDate);
        }

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public CheckInAnalyticsDTO getCheckInAnalytics(Long eventId, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get check-in analytics for event {} with date range: startDate={}, endDate={}", eventId, startDate, endDate);

        // Task 8: Get tenant ID from context for multi-tenant filtering
        String tenantId = TenantContext.getCurrentTenant();
        log.debug("Tenant ID from context: {}", tenantId);

        // Filter transactions by event, tenant, date range, and check-in status
        List<EventTicketTransaction> allTransactions = eventTicketTransactionRepository
            .findAll()
            .stream()
            .filter(t -> {
                if (t.getEventId() == null || !t.getEventId().equals(eventId)) {
                    return false;
                }

                // Task 8: Filter by tenant for multi-tenant security
                if (tenantId != null && !tenantId.isEmpty()) {
                    if (!tenantId.equals(t.getTenantId())) {
                        return false;
                    }
                }

                // Filter by check-in time date range if provided
                ZonedDateTime checkInTime = t.getCheckInTime();
                if (checkInTime != null) {
                    LocalDate checkInLocalDate = checkInTime.toLocalDate();

                    if (startDate != null && checkInLocalDate.isBefore(startDate)) {
                        return false;
                    }

                    if (endDate != null && checkInLocalDate.isAfter(endDate)) {
                        return false;
                    }
                }

                return true;
            })
            .collect(Collectors.toList());

        // Calculate aggregated metrics
        int totalTickets = allTransactions.stream().mapToInt(t -> t.getQuantity() != null ? t.getQuantity() : 0).sum();

        List<EventTicketTransaction> checkedInTransactions = allTransactions
            .stream()
            .filter(t -> "CHECKED_IN".equals(t.getCheckInStatus()))
            .collect(Collectors.toList());

        int checkedInCount = checkedInTransactions.size();
        int notCheckedInCount = (int) allTransactions.stream().filter(t -> "NOT_CHECKED_IN".equals(t.getCheckInStatus())).count();
        int noShowCount = (int) allTransactions.stream().filter(t -> "NO_SHOW".equals(t.getCheckInStatus())).count();

        double checkInPercentage = totalTickets > 0 ? ((double) checkedInCount / totalTickets) * 100.0 : 0.0;

        // Aggregate check-ins by hour
        Map<String, Integer> checkInsByHour = new HashMap<>();
        checkedInTransactions.forEach(t -> {
            if (t.getCheckInTime() != null) {
                LocalTime hour = t.getCheckInTime().toLocalTime();
                String hourKey = String.format("%02d:00", hour.getHour());
                checkInsByHour.put(hourKey, checkInsByHour.getOrDefault(hourKey, 0) + 1);
            }
        });

        // Aggregate check-ins by ticket type (simplified - using quantity for now)
        // TODO: Enhance with actual ticket type from EventTicketTransactionItem if needed
        Map<String, Integer> checkInsByTicketType = new HashMap<>();
        checkedInTransactions.forEach(t -> {
            String typeKey = "General"; // Default - can be enhanced with actual ticket type
            int quantity = t.getQuantity() != null ? t.getQuantity() : 0;
            checkInsByTicketType.put(typeKey, checkInsByTicketType.getOrDefault(typeKey, 0) + quantity);
        });

        // Calculate peak check-in time
        String peakCheckInTime = null;
        if (!checkInsByHour.isEmpty()) {
            Map.Entry<String, Integer> peakEntry = checkInsByHour.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
            if (peakEntry != null) {
                int peakHour = Integer.parseInt(peakEntry.getKey().substring(0, 2));
                peakCheckInTime = String.format("%02d:00-%02d:00", peakHour, peakHour + 1);
            }
        }

        // Calculate average check-in time
        String averageCheckInTime = null;
        if (!checkedInTransactions.isEmpty()) {
            List<LocalTime> checkInTimes = checkedInTransactions
                .stream()
                .filter(t -> t.getCheckInTime() != null)
                .map(t -> t.getCheckInTime().toLocalTime())
                .collect(Collectors.toList());

            if (!checkInTimes.isEmpty()) {
                long totalSeconds = checkInTimes.stream().mapToLong(LocalTime::toSecondOfDay).sum();
                long avgSeconds = totalSeconds / checkInTimes.size();
                LocalTime avgTime = LocalTime.ofSecondOfDay(avgSeconds);
                averageCheckInTime = String.format("%02d:%02d", avgTime.getHour(), avgTime.getMinute());
            }
        }

        CheckInAnalyticsDTO analytics = new CheckInAnalyticsDTO();
        analytics.setEventId(eventId);
        analytics.setTotalTickets(totalTickets);
        analytics.setCheckedInCount(checkedInCount);
        analytics.setNotCheckedInCount(notCheckedInCount);
        analytics.setNoShowCount(noShowCount);
        analytics.setCheckInPercentage(Math.round(checkInPercentage * 100.0) / 100.0); // Round to 2 decimal places
        analytics.setCheckInsByHour(checkInsByHour);
        analytics.setCheckInsByTicketType(checkInsByTicketType);
        analytics.setPeakCheckInTime(peakCheckInTime);
        analytics.setAverageCheckInTime(averageCheckInTime);

        if (startDate != null || endDate != null) {
            analytics.setStartDate(startDate);
            analytics.setEndDate(endDate);
        }

        return analytics;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesAnalyticsDTO getSalesAnalytics(Long eventId, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get sales analytics for event {} with date range: startDate={}, endDate={}", eventId, startDate, endDate);

        // Task 8: Get tenant ID from context for multi-tenant filtering
        String tenantId = TenantContext.getCurrentTenant();
        log.debug("Tenant ID from context: {}", tenantId);

        // Filter transactions by event, tenant, date range, and exclude REFUNDED/CANCELLED
        List<EventTicketTransaction> transactions = eventTicketTransactionRepository
            .findAll()
            .stream()
            .filter(t -> {
                if (t.getEventId() == null || !t.getEventId().equals(eventId)) {
                    return false;
                }

                // Task 8: Filter by tenant for multi-tenant security
                if (tenantId != null && !tenantId.isEmpty()) {
                    if (!tenantId.equals(t.getTenantId())) {
                        return false;
                    }
                }

                // Exclude REFUNDED and CANCELLED transactions
                String status = t.getStatus();
                if ("REFUNDED".equals(status) || "CANCELLED".equals(status)) {
                    return false;
                }

                // Filter by purchase date range if provided
                ZonedDateTime purchaseDate = t.getPurchaseDate();
                if (purchaseDate != null) {
                    LocalDate purchaseLocalDate = purchaseDate.toLocalDate();

                    if (startDate != null && purchaseLocalDate.isBefore(startDate)) {
                        return false;
                    }

                    if (endDate != null && purchaseLocalDate.isAfter(endDate)) {
                        return false;
                    }
                } else {
                    if (startDate != null || endDate != null) {
                        return false;
                    }
                }

                return true;
            })
            .collect(Collectors.toList());

        // Calculate aggregated metrics
        int totalSales = transactions.size();
        BigDecimal totalRevenue = transactions
            .stream()
            .map(t -> t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDiscounts = transactions
            .stream()
            .map(t -> t.getDiscountAmount() != null ? t.getDiscountAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRefunds = transactions
            .stream()
            .map(t -> t.getRefundAmount() != null ? t.getRefundAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netRevenue = totalRevenue.subtract(totalRefunds);

        // Calculate Net Revenue Before Tax
        // Formula: net_revenue_before_tax = final_amount - stripe_fee_amount
        BigDecimal netRevenueBeforeTax = transactions
            .stream()
            .map(t -> {
                BigDecimal finalAmount = t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO;
                BigDecimal stripeFee = t.getStripeFeeAmount() != null ? t.getStripeFeeAmount() : BigDecimal.ZERO;
                // Net revenue before tax = final_amount - stripe_fee_amount
                return finalAmount.subtract(stripeFee);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageTicketPrice = totalSales > 0
            ? totalRevenue.divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        // Aggregate sales by date
        List<SalesAnalyticsDTO.SalesByDateDTO> salesByDate = new ArrayList<>();
        Map<LocalDate, List<EventTicketTransaction>> transactionsByDate = transactions
            .stream()
            .filter(t -> t.getPurchaseDate() != null)
            .collect(Collectors.groupingBy(t -> t.getPurchaseDate().toLocalDate()));

        transactionsByDate.forEach((date, dateTransactions) -> {
            SalesAnalyticsDTO.SalesByDateDTO dto = new SalesAnalyticsDTO.SalesByDateDTO();
            dto.setDate(date);
            dto.setCount(dateTransactions.size());
            dto.setRevenue(
                dateTransactions
                    .stream()
                    .map(t -> t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
            salesByDate.add(dto);
        });

        // Aggregate sales by ticket type (simplified - using quantity for now)
        // TODO: Enhance with actual ticket type from EventTicketTransactionItem if needed
        Map<String, SalesAnalyticsDTO.SalesByTicketTypeDTO> salesByTicketType = new HashMap<>();
        transactions.forEach(t -> {
            String typeKey = "General"; // Default - can be enhanced with actual ticket type
            SalesAnalyticsDTO.SalesByTicketTypeDTO dto = salesByTicketType.computeIfAbsent(
                typeKey,
                k -> new SalesAnalyticsDTO.SalesByTicketTypeDTO()
            );
            dto.setCount(dto.getCount() + (t.getQuantity() != null ? t.getQuantity() : 0));
            dto.setRevenue(dto.getRevenue().add(t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO));
        });

        // Aggregate sales by payment method
        Map<String, SalesAnalyticsDTO.SalesByPaymentMethodDTO> salesByPaymentMethod = new HashMap<>();
        transactions.forEach(t -> {
            String method = t.getPaymentMethod() != null ? t.getPaymentMethod() : "UNKNOWN";
            SalesAnalyticsDTO.SalesByPaymentMethodDTO dto = salesByPaymentMethod.computeIfAbsent(
                method,
                k -> new SalesAnalyticsDTO.SalesByPaymentMethodDTO()
            );
            dto.setCount(dto.getCount() + 1);
            dto.setRevenue(dto.getRevenue().add(t.getFinalAmount() != null ? t.getFinalAmount() : BigDecimal.ZERO));
        });

        SalesAnalyticsDTO analytics = new SalesAnalyticsDTO();
        analytics.setEventId(eventId);
        analytics.setTotalSales(totalSales);
        analytics.setTotalRevenue(totalRevenue);
        analytics.setNetRevenue(netRevenue);
        analytics.setNetRevenueBeforeTax(netRevenueBeforeTax);
        analytics.setTotalDiscounts(totalDiscounts);
        analytics.setTotalRefunds(totalRefunds);
        analytics.setAverageTicketPrice(averageTicketPrice);
        analytics.setSalesByDate(salesByDate);
        analytics.setSalesByTicketType(salesByTicketType);
        analytics.setSalesByPaymentMethod(salesByPaymentMethod);

        if (startDate != null || endDate != null) {
            analytics.setStartDate(startDate);
            analytics.setEndDate(endDate);
        }

        return analytics;
    }
}
