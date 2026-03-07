package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.EventTicketTransactionItem;
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.domain.ManualPaymentRequest;
import com.nextjstemplate.domain.enumeration.ManualPaymentStatus;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.repository.ManualPaymentRequestRepository;
import com.nextjstemplate.service.ManualPaymentBatchJobService;
import com.nextjstemplate.service.ManualPaymentRequestService;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.dto.CartItemDTO;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.dto.ManualPaymentConfirmationEmailJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentRequestDTO;
import com.nextjstemplate.service.dto.ManualPaymentTicketEmailJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentTicketEmailJobRequest.TicketItemDTO;
import com.nextjstemplate.service.mapper.EventDetailsMapper;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
import com.nextjstemplate.service.mapper.ManualPaymentRequestMapper;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.ManualPaymentRequest}.
 */
@Service
@Transactional
public class ManualPaymentRequestServiceImpl implements ManualPaymentRequestService {

    private final Logger log = LoggerFactory.getLogger(ManualPaymentRequestServiceImpl.class);

    private final ManualPaymentRequestRepository manualPaymentRequestRepository;
    private final ManualPaymentRequestMapper manualPaymentRequestMapper;
    private final S3Service s3Service;
    private final Environment environment;
    private final EventTicketTransactionRepository eventTicketTransactionRepository;
    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;
    private final EventTicketTypeRepository eventTicketTypeRepository;
    private final EventDetailsRepository eventDetailsRepository;
    private final EventTicketTransactionMapper eventTicketTransactionMapper;
    private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;
    private final EventDetailsMapper eventDetailsMapper;
    private final QRCodeService qrCodeService;
    private final ManualPaymentBatchJobService manualPaymentBatchJobService;
    private final ObjectMapper objectMapper;

    @Value("${app.email-host-url-prefix:}")
    private String emailHostUrlPrefix;

    public ManualPaymentRequestServiceImpl(
        ManualPaymentRequestRepository manualPaymentRequestRepository,
        ManualPaymentRequestMapper manualPaymentRequestMapper,
        S3Service s3Service,
        Environment environment,
        EventTicketTransactionRepository eventTicketTransactionRepository,
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventTicketTypeRepository eventTicketTypeRepository,
        EventDetailsRepository eventDetailsRepository,
        EventTicketTransactionMapper eventTicketTransactionMapper,
        EventTicketTransactionItemMapper eventTicketTransactionItemMapper,
        EventDetailsMapper eventDetailsMapper,
        QRCodeService qrCodeService,
        ManualPaymentBatchJobService manualPaymentBatchJobService,
        ObjectMapper objectMapper
    ) {
        this.manualPaymentRequestRepository = manualPaymentRequestRepository;
        this.manualPaymentRequestMapper = manualPaymentRequestMapper;
        this.s3Service = s3Service;
        this.environment = environment;
        this.eventTicketTransactionRepository = eventTicketTransactionRepository;
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventTicketTransactionMapper = eventTicketTransactionMapper;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
        this.eventDetailsMapper = eventDetailsMapper;
        this.qrCodeService = qrCodeService;
        this.manualPaymentBatchJobService = manualPaymentBatchJobService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ManualPaymentRequestDTO save(ManualPaymentRequestDTO dto) {
        log.debug("Request to save ManualPaymentRequest : {}", dto);

        // Log cart and selectedTickets for debugging
        log.debug(
            "ManualPaymentRequestDTO details - cart: {}, selectedTickets: {}, eventId: {}, amountDue: {}",
            dto.getCart() != null ? dto.getCart().size() + " items" : "null",
            dto.getSelectedTickets() != null ? "present (" + dto.getSelectedTickets().length() + " chars)" : "null",
            dto.getEventId(),
            dto.getAmountDue()
        );

        // Validate event exists and is active
        if (dto.getEventId() == null) {
            throw new BadRequestAlertException("Event ID is required", "manualPaymentRequest", "eventIdRequired");
        }

        EventDetails event = eventDetailsRepository
            .findById(dto.getEventId())
            .orElseThrow(() -> new EntityNotFoundException("Event not found: " + dto.getEventId()));

        if (event.getTenantId() != null && dto.getTenantId() != null && !event.getTenantId().equals(dto.getTenantId())) {
            throw new BadRequestAlertException("Event does not belong to the specified tenant", "manualPaymentRequest", "tenantMismatch");
        }

        if (Boolean.FALSE.equals(event.getIsActive())) {
            throw new BadRequestAlertException("Event is not active", "manualPaymentRequest", "eventNotActive");
        }

        // Reconstruct cart from selectedTickets if cart is not provided
        List<CartItemDTO> cart = dto.getCart();
        if ((cart == null || cart.isEmpty()) && dto.getSelectedTickets() != null && !dto.getSelectedTickets().trim().isEmpty()) {
            try {
                cart = reconstructCartFromSelectedTickets(dto.getSelectedTickets(), dto.getEventId(), dto.getTenantId());
                log.info("Reconstructed cart from selectedTickets for manual payment request: {} items", cart.size());
            } catch (Exception e) {
                log.warn(
                    "Failed to reconstruct cart from selectedTickets for manual payment request: {}. " +
                    "Transaction items will not be created.",
                    e.getMessage()
                );
                // Continue without cart - transaction will be created but without items
            }
        }

        // Validate cart and calculate total if cart is provided
        if (cart != null && !cart.isEmpty()) {
            BigDecimal calculatedTotal = BigDecimal.ZERO;
            for (CartItemDTO item : cart) {
                EventTicketType ticketType = eventTicketTypeRepository
                    .findById(item.getTicketTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket type not found: " + item.getTicketTypeId()));

                // Validate tenant match
                if (ticketType.getTenantId() != null && dto.getTenantId() != null && !ticketType.getTenantId().equals(dto.getTenantId())) {
                    throw new BadRequestAlertException(
                        "Ticket type does not belong to the specified tenant",
                        "manualPaymentRequest",
                        "tenantMismatch"
                    );
                }

                // Validate inventory (check remaining quantity)
                int remaining = (ticketType.getRemainingQuantity() != null ? ticketType.getRemainingQuantity() : 0);
                if (item.getQuantity() > remaining) {
                    throw new BadRequestAlertException(
                        "Insufficient tickets available for type: " + ticketType.getName(),
                        "manualPaymentRequest",
                        "insufficientInventory"
                    );
                }

                calculatedTotal = calculatedTotal.add(ticketType.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            // Verify amount matches
            if (calculatedTotal.compareTo(dto.getAmountDue()) != 0) {
                throw new BadRequestAlertException(
                    "Amount mismatch. Calculated: " + calculatedTotal + ", Provided: " + dto.getAmountDue(),
                    "manualPaymentRequest",
                    "amountMismatch"
                );
            }
        }

        // Create ManualPaymentRequest
        ManualPaymentRequest entity = manualPaymentRequestMapper.toEntity(dto);
        ZonedDateTime now = ZonedDateTime.now();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        }
        entity.setUpdatedAt(now);

        if (entity.getStatus() == null) {
            entity.setStatus(ManualPaymentStatus.REQUESTED);
        }

        // Save payment request first (without ticket_transaction_id yet)
        entity = manualPaymentRequestRepository.save(entity);

        // Create ticket transaction and items if cart is provided
        // CRITICAL: Cart is required for manual payment requests to create ticket transactions with items
        if (cart == null || cart.isEmpty()) {
            log.warn(
                "Manual payment request {} created without cart or selectedTickets. " +
                "Creating basic transaction without items. Frontend should send either 'cart' array or 'selectedTickets' JSON string.",
                entity.getId()
            );
            log.debug(
                "DTO received - cart: {}, selectedTickets: {}. " +
                "To create transaction items, frontend must send ticket information in one of these formats: " +
                "1) cart: [{{ticketTypeId: 123, quantity: 2}}] OR " +
                "2) selectedTickets: '[{{\"ticketTypeId\": 123, \"quantity\": 2}}]'",
                dto.getCart() != null ? dto.getCart().size() + " items" : "null",
                dto.getSelectedTickets() != null ? "present" : "null"
            );

            // Create basic transaction without items (for sales analytics visibility)
            // This ensures the transaction appears in sales analytics even without item details
            try {
                EventTicketTransaction ticketTransaction = createBasicTicketTransactionForManualPayment(entity, dto);
                entity.setTicketTransactionId(ticketTransaction.getId());
                entity = manualPaymentRequestRepository.save(entity);
                dto.setTicketTransactionId(ticketTransaction.getId());

                log.info(
                    "Created basic ticket transaction {} (without items) for manual payment request {}. " +
                    "Transaction items were not created because cart/selectedTickets were not provided.",
                    ticketTransaction.getId(),
                    entity.getId()
                );
            } catch (Exception e) {
                log.error("Failed to create basic ticket transaction for manual payment request {}: {}", entity.getId(), e.getMessage(), e);
                // Don't fail the entire request - payment request is already saved
            }
        } else {
            try {
                EventTicketTransaction ticketTransaction = createTicketTransactionForManualPayment(entity, dto, cart);
                entity.setTicketTransactionId(ticketTransaction.getId());
                entity = manualPaymentRequestRepository.save(entity);

                // Set ticket transaction ID in response DTO
                dto.setTicketTransactionId(ticketTransaction.getId());

                log.info(
                    "Successfully created ticket transaction {} with {} items for manual payment request {}",
                    ticketTransaction.getId(),
                    cart.size(),
                    entity.getId()
                );
            } catch (Exception e) {
                log.error("Failed to create ticket transaction for manual payment request {}: {}", entity.getId(), e.getMessage(), e);
                // Throw exception to ensure transaction rollback if ticket creation fails
                // This ensures data consistency - if tickets can't be created, payment request shouldn't be saved
                throw new BadRequestAlertException(
                    "Failed to create ticket transaction: " + e.getMessage(),
                    "manualPaymentRequest",
                    "ticketTransactionCreationFailed"
                );
            }
        }

        ManualPaymentRequestDTO result = manualPaymentRequestMapper.toDto(entity);
        result.setTicketTransactionId(entity.getTicketTransactionId());

        return result;
    }

    @Override
    public Optional<ManualPaymentRequestDTO> partialUpdate(ManualPaymentRequestDTO dto) {
        log.debug("Request to partially update ManualPaymentRequest : {}", dto);

        return manualPaymentRequestRepository
            .findById(dto.getId())
            .map(existing -> {
                manualPaymentRequestMapper.partialUpdate(existing, dto);
                existing.setUpdatedAt(ZonedDateTime.now());
                return existing;
            })
            .map(manualPaymentRequestRepository::save)
            .map(manualPaymentRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManualPaymentRequestDTO> findAll(Pageable pageable) {
        return manualPaymentRequestRepository.findAll(pageable).map(manualPaymentRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManualPaymentRequestDTO> findOne(Long id) {
        return manualPaymentRequestRepository.findById(id).map(manualPaymentRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        manualPaymentRequestRepository.deleteById(id);
    }

    @Override
    public ManualPaymentRequestDTO uploadProofOfPayment(Long id, String tenantId, MultipartFile file) {
        ManualPaymentRequest entity = manualPaymentRequestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Manual payment request not found: " + id));

        if (tenantId != null && entity.getTenantId() != null && !tenantId.equals(entity.getTenantId())) {
            throw new IllegalArgumentException("Manual payment request does not belong to the specified tenant");
        }

        if (entity.getEventId() == null) {
            throw new IllegalArgumentException("eventId is required on manual payment request to upload proof");
        }

        String key = generateProofOfPaymentS3Key(entity.getTenantId(), entity.getEventId(), entity.getId(), file.getOriginalFilename());
        String url = s3Service.uploadFile(key, file);

        entity.setProofOfPaymentFileKey(key);
        entity.setProofOfPaymentFileUrl(url);
        entity.setProofOfPaymentUploadedAt(ZonedDateTime.now());
        entity.setUpdatedAt(ZonedDateTime.now());

        entity = manualPaymentRequestRepository.save(entity);
        return manualPaymentRequestMapper.toDto(entity);
    }

    /**
     * Called by the REST resource when status is updated (Received/Voided/Refunded).
     */
    @Override
    public ManualPaymentRequestDTO updateStatus(
        Long id,
        String tenantId,
        ManualPaymentStatus newStatus,
        String receivedBy,
        String voidReason
    ) {
        ManualPaymentRequest entity = manualPaymentRequestRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Manual payment request not found: " + id));

        if (tenantId != null && entity.getTenantId() != null && !tenantId.equals(entity.getTenantId())) {
            throw new IllegalArgumentException("Manual payment request does not belong to the specified tenant");
        }

        entity.setStatus(newStatus);
        entity.setUpdatedAt(ZonedDateTime.now());

        // Validate status transition
        if (entity.getStatus() != ManualPaymentStatus.REQUESTED && newStatus == ManualPaymentStatus.RECEIVED) {
            throw new BadRequestAlertException(
                "Cannot change status to RECEIVED from " + entity.getStatus(),
                "manualPaymentRequest",
                "invalidStatusTransition"
            );
        }

        if (newStatus == ManualPaymentStatus.RECEIVED) {
            entity.setReceivedAt(ZonedDateTime.now());
            entity.setReceivedBy(receivedBy);
            // Ticketing integration (best-effort)
            confirmTicketTransactionIfPresent(entity);
        } else if (newStatus == ManualPaymentStatus.VOIDED || newStatus == ManualPaymentStatus.REFUNDED) {
            entity.setVoidReason(voidReason);
            // Update ticket transaction to CANCELLED (but don't update inventory)
            cancelTicketTransactionIfPresent(entity);
        }

        entity = manualPaymentRequestRepository.save(entity);
        return manualPaymentRequestMapper.toDto(entity);
    }

    /**
     * Create basic ticket transaction without items when cart/selectedTickets are not provided.
     * This ensures the transaction appears in sales analytics even without item details.
     */
    private EventTicketTransaction createBasicTicketTransactionForManualPayment(
        ManualPaymentRequest paymentRequest,
        ManualPaymentRequestDTO dto
    ) {
        ZonedDateTime now = ZonedDateTime.now();

        // Create EventTicketTransaction with PENDING status (without items)
        EventTicketTransaction ticketTransaction = new EventTicketTransaction();
        ticketTransaction.setEventId(paymentRequest.getEventId());
        ticketTransaction.setEmail(paymentRequest.getRequesterEmail());
        ticketTransaction.setFirstName(paymentRequest.getRequesterFirstName());
        ticketTransaction.setLastName(paymentRequest.getRequesterLastName());
        ticketTransaction.setPhone(paymentRequest.getRequesterPhone());
        ticketTransaction.setTotalAmount(paymentRequest.getAmountDue());
        ticketTransaction.setFinalAmount(paymentRequest.getAmountDue());
        ticketTransaction.setQuantity(1); // Default to 1 when quantity unknown
        ticketTransaction.setStatus("PENDING"); // CRITICAL: PENDING, not CONFIRMED
        ticketTransaction.setTransactionReference("MANUAL-" + paymentRequest.getId());
        ticketTransaction.setPurchaseDate(now);
        ticketTransaction.setTenantId(paymentRequest.getTenantId());
        ticketTransaction.setCreatedAt(now);
        ticketTransaction.setUpdatedAt(now);
        ticketTransaction.setPricePerUnit(paymentRequest.getAmountDue()); // Use amountDue as price per unit
        // DO NOT set qrCodeImageUrl - only set after RECEIVED status
        // DO NOT set stripe fields - manual payments don't use Stripe

        ticketTransaction = eventTicketTransactionRepository.save(ticketTransaction);

        log.info(
            "Created basic ticket transaction {} (without items) for manual payment request {}",
            ticketTransaction.getId(),
            paymentRequest.getId()
        );

        return ticketTransaction;
    }

    /**
     * Create ticket transaction and items for manual payment request.
     */
    private EventTicketTransaction createTicketTransactionForManualPayment(
        ManualPaymentRequest paymentRequest,
        ManualPaymentRequestDTO dto,
        List<CartItemDTO> cart
    ) {
        ZonedDateTime now = ZonedDateTime.now();

        // Create EventTicketTransaction with PENDING status
        EventTicketTransaction ticketTransaction = new EventTicketTransaction();
        ticketTransaction.setEventId(paymentRequest.getEventId());
        ticketTransaction.setEmail(paymentRequest.getRequesterEmail());
        ticketTransaction.setFirstName(paymentRequest.getRequesterFirstName());
        ticketTransaction.setLastName(paymentRequest.getRequesterLastName());
        ticketTransaction.setPhone(paymentRequest.getRequesterPhone());
        ticketTransaction.setTotalAmount(paymentRequest.getAmountDue());
        ticketTransaction.setFinalAmount(paymentRequest.getAmountDue());
        ticketTransaction.setQuantity(cart.stream().mapToInt(CartItemDTO::getQuantity).sum());
        ticketTransaction.setStatus("PENDING"); // CRITICAL: PENDING, not CONFIRMED
        ticketTransaction.setTransactionReference("MANUAL-" + paymentRequest.getId());
        ticketTransaction.setPurchaseDate(now);
        ticketTransaction.setTenantId(paymentRequest.getTenantId());
        ticketTransaction.setCreatedAt(now);
        ticketTransaction.setUpdatedAt(now);
        // DO NOT set qrCodeImageUrl - only set after RECEIVED status
        // DO NOT set stripe fields - manual payments don't use Stripe

        // Calculate price per unit (average)
        BigDecimal totalQuantity = BigDecimal.valueOf(ticketTransaction.getQuantity());
        BigDecimal pricePerUnit = totalQuantity.compareTo(BigDecimal.ZERO) > 0
            ? paymentRequest.getAmountDue().divide(totalQuantity, 2, java.math.RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        ticketTransaction.setPricePerUnit(pricePerUnit);

        ticketTransaction = eventTicketTransactionRepository.save(ticketTransaction);

        // Create EventTicketTransactionItem records
        // CRITICAL: Check for existing items first to prevent duplicates (idempotency)
        // This prevents issues if the method is called multiple times or if there are race conditions
        List<EventTicketTransactionItem> existingItems = eventTicketTransactionItemRepository.findByTransactionIdAndTenantId(
            ticketTransaction.getId(),
            paymentRequest.getTenantId()
        );

        if (!existingItems.isEmpty()) {
            log.warn(
                "Transaction items already exist for transaction {} and tenant {}. Skipping creation. " +
                "This may indicate a duplicate call or race condition.",
                ticketTransaction.getId(),
                paymentRequest.getTenantId()
            );
            return ticketTransaction;
        }

        List<EventTicketTransactionItem> transactionItems = new ArrayList<>();
        for (CartItemDTO item : cart) {
            EventTicketType ticketType = eventTicketTypeRepository
                .findById(item.getTicketTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket type not found: " + item.getTicketTypeId()));

            // Validate tenant match for ticket type
            if (
                ticketType.getTenantId() != null &&
                paymentRequest.getTenantId() != null &&
                !ticketType.getTenantId().equals(paymentRequest.getTenantId())
            ) {
                throw new BadRequestAlertException(
                    "Ticket type does not belong to the specified tenant",
                    "manualPaymentRequest",
                    "tenantMismatch"
                );
            }

            EventTicketTransactionItem transactionItem = new EventTicketTransactionItem();
            transactionItem.setTransactionId(ticketTransaction.getId());
            transactionItem.setTicketTypeId(item.getTicketTypeId());
            transactionItem.setQuantity(item.getQuantity());
            transactionItem.setPricePerUnit(ticketType.getPrice());
            transactionItem.setTotalAmount(ticketType.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            transactionItem.setTenantId(paymentRequest.getTenantId());
            transactionItem.setCreatedAt(now);
            transactionItem.setUpdatedAt(now);
            // NOTE: Manual payments don't use paymentMethodDomainId (Stripe-specific field)
            // This keeps manual payment flow separate from Stripe flow

            transactionItems.add(transactionItem);
        }

        // Save all transaction items in a single batch operation
        // This is separate from Stripe flow which uses TicketGenerationService
        List<EventTicketTransactionItem> savedItems;
        try {
            savedItems = eventTicketTransactionItemRepository.saveAll(transactionItems);
            log.debug("Saved {} transaction items for manual payment transaction {}", savedItems.size(), ticketTransaction.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Handle race condition - another thread might have created items
            log.warn(
                "Unique constraint violation when creating transaction items for transaction {} - " +
                "another thread likely created them. Fetching existing items.",
                ticketTransaction.getId()
            );
            savedItems =
                eventTicketTransactionItemRepository.findByTransactionIdAndTenantId(
                    ticketTransaction.getId(),
                    paymentRequest.getTenantId()
                );
        }

        log.info(
            "Created ticket transaction {} with {} items for manual payment request {}",
            ticketTransaction.getId(),
            transactionItems.size(),
            paymentRequest.getId()
        );

        return ticketTransaction;
    }

    /**
     * Reconstruct cart from selectedTickets JSON string.
     * Expected format: [{"ticketTypeId": 123, "quantity": 2}, ...]
     * or: [{"id": 123, "quantity": 2}, ...] (alternative format)
     *
     * @param selectedTickets JSON string containing ticket selections
     * @param eventId Event ID for validation
     * @param tenantId Tenant ID for validation
     * @return List of CartItemDTO objects
     */
    private List<CartItemDTO> reconstructCartFromSelectedTickets(String selectedTickets, Long eventId, String tenantId) {
        try {
            // Parse JSON string to list of maps
            List<java.util.Map<String, Object>> ticketSelections = objectMapper.readValue(
                selectedTickets,
                new TypeReference<List<java.util.Map<String, Object>>>() {}
            );

            final String finalTenantId = tenantId; // Make effectively final for lambda
            List<CartItemDTO> cart = new ArrayList<>();
            for (java.util.Map<String, Object> selection : ticketSelections) {
                // Support both "ticketTypeId" and "id" keys
                Long ticketTypeIdValue = null;
                if (selection.containsKey("ticketTypeId")) {
                    ticketTypeIdValue = ((Number) selection.get("ticketTypeId")).longValue();
                } else if (selection.containsKey("id")) {
                    ticketTypeIdValue = ((Number) selection.get("id")).longValue();
                }

                Integer quantityValue = null;
                if (selection.containsKey("quantity")) {
                    quantityValue = ((Number) selection.get("quantity")).intValue();
                }

                if (ticketTypeIdValue != null && quantityValue != null && quantityValue > 0) {
                    // Make final for lambda
                    final Long ticketTypeId = ticketTypeIdValue;
                    final Integer quantity = quantityValue;

                    // Validate ticket type exists and belongs to tenant
                    EventTicketType ticketType = eventTicketTypeRepository
                        .findById(ticketTypeId)
                        .orElseThrow(() -> new EntityNotFoundException("Ticket type not found: " + ticketTypeId));

                    // Validate tenant match (event validation is done at the request level)
                    if (finalTenantId != null && ticketType.getTenantId() != null && !ticketType.getTenantId().equals(finalTenantId)) {
                        throw new BadRequestAlertException(
                            "Ticket type " + ticketTypeId + " does not belong to tenant " + finalTenantId,
                            "manualPaymentRequest",
                            "ticketTypeTenantMismatch"
                        );
                    }

                    CartItemDTO cartItem = new CartItemDTO();
                    cartItem.setTicketTypeId(ticketTypeId);
                    cartItem.setQuantity(quantity);
                    cart.add(cartItem);
                }
            }

            return cart;
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new BadRequestAlertException(
                "Invalid selectedTickets JSON format: " + e.getMessage(),
                "manualPaymentRequest",
                "invalidSelectedTicketsFormat"
            );
        }
    }

    private void confirmTicketTransactionIfPresent(ManualPaymentRequest request) {
        try {
            if (request.getTicketTransactionId() == null) {
                log.debug("No ticket transaction ID for manual payment request {}", request.getId());
                return;
            }

            EventTicketTransaction txn = eventTicketTransactionRepository.findById(request.getTicketTransactionId()).orElse(null);
            if (txn == null) {
                log.warn("No EventTicketTransaction found for ticketTransactionId={}", request.getTicketTransactionId());
                return;
            }

            if (txn.getTenantId() != null && request.getTenantId() != null && !request.getTenantId().equals(txn.getTenantId())) {
                log.warn(
                    "Tenant mismatch for manual payment receipt. manualPaymentTenant={}, ticketTransactionTenant={}",
                    request.getTenantId(),
                    txn.getTenantId()
                );
                return;
            }

            // Update ticket transaction status to CONFIRMED
            if (!"CONFIRMED".equalsIgnoreCase(txn.getStatus())) {
                txn.setStatus("CONFIRMED");
                txn.setUpdatedAt(ZonedDateTime.now());
            }

            // Update ticket inventory (increment quantity_sold)
            List<EventTicketTransactionItem> items = eventTicketTransactionItemRepository.findByTransactionId(txn.getId());
            for (EventTicketTransactionItem item : items) {
                EventTicketType ticketType = eventTicketTypeRepository
                    .findById(item.getTicketTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket type not found: " + item.getTicketTypeId()));

                int currentSold = ticketType.getSoldQuantity() != null ? ticketType.getSoldQuantity() : 0;
                ticketType.setSoldQuantity(currentSold + item.getQuantity());

                // Update remaining quantity
                int available = ticketType.getAvailableQuantity() != null ? ticketType.getAvailableQuantity() : 0;
                ticketType.setRemainingQuantity(available - ticketType.getSoldQuantity());

                eventTicketTypeRepository.save(ticketType);
                log.debug(
                    "Updated inventory for ticket type {}: sold={}, remaining={}",
                    ticketType.getId(),
                    ticketType.getSoldQuantity(),
                    ticketType.getRemainingQuantity()
                );
            }

            // Generate QR code if missing
            if (txn.getQrCodeImageUrl() == null || txn.getQrCodeImageUrl().isEmpty()) {
                if (emailHostUrlPrefix == null || emailHostUrlPrefix.isEmpty()) {
                    log.warn("emailHostUrlPrefix not configured; skipping QR generation for transaction {}", txn.getId());
                } else {
                    String qrScanUrlContent =
                        emailHostUrlPrefix + "/qrcode-scan/tickets/events/" + txn.getEventId() + "/transactions/" + txn.getId();
                    String qrUrl = qrCodeService.generateAndUploadQRCode(
                        qrScanUrlContent,
                        txn.getEventId(),
                        String.valueOf(txn.getId()),
                        txn.getTenantId()
                    );
                    txn.setQrCodeImageUrl(qrUrl);
                    txn.setUpdatedAt(ZonedDateTime.now());
                    log.info("Generated QR code for ticket transaction {}: {}", txn.getId(), qrUrl);
                }
            }

            eventTicketTransactionRepository.save(txn);

            // Trigger ticket email batch job
            triggerTicketEmailJob(request, txn);
        } catch (Exception e) {
            log.error("Failed to confirm ticket transaction for manual payment request {}: {}", request.getId(), e.getMessage(), e);
            // Don't re-throw - this is best-effort
        }
    }

    private String generateProofOfPaymentS3Key(String tenantId, Long eventId, Long paymentId, String originalFilename) {
        String profilePrefix = getActiveProfilePrefix();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String safeBase = "proof_of_payment";
        return String.format(
            "%s/events/tenantId/%s/event-id/%d/manual-payments/proof-of-payment/%d/%s_%s_%s%s",
            profilePrefix,
            tenantId,
            eventId,
            paymentId,
            safeBase,
            timestamp,
            uuid,
            ext
        );
    }

    /**
     * Cancel ticket transaction when payment request is voided/cancelled.
     */
    private void cancelTicketTransactionIfPresent(ManualPaymentRequest request) {
        try {
            if (request.getTicketTransactionId() == null) {
                return;
            }

            EventTicketTransaction txn = eventTicketTransactionRepository.findById(request.getTicketTransactionId()).orElse(null);
            if (txn == null) {
                log.warn("No EventTicketTransaction found for ticketTransactionId={}", request.getTicketTransactionId());
                return;
            }

            if (txn.getTenantId() != null && request.getTenantId() != null && !request.getTenantId().equals(txn.getTenantId())) {
                log.warn(
                    "Tenant mismatch for manual payment cancellation. manualPaymentTenant={}, ticketTransactionTenant={}",
                    request.getTenantId(),
                    txn.getTenantId()
                );
                return;
            }

            // Update ticket transaction status to CANCELLED
            // DO NOT update inventory - tickets were never confirmed
            txn.setStatus("CANCELLED");
            txn.setUpdatedAt(ZonedDateTime.now());
            eventTicketTransactionRepository.save(txn);

            log.info("Ticket transaction {} cancelled for manual payment request {}", txn.getId(), request.getId());
        } catch (Exception e) {
            log.error("Failed to cancel ticket transaction for manual payment request {}: {}", request.getId(), e.getMessage(), e);
        }
    }

    @Override
    public Optional<ManualPaymentRequestDTO> findOneWithDetails(Long id) {
        return manualPaymentRequestRepository
            .findById(id)
            .map(entity -> {
                ManualPaymentRequestDTO dto = manualPaymentRequestMapper.toDto(entity);

                // Fetch ticket transaction if present
                if (entity.getTicketTransactionId() != null) {
                    eventTicketTransactionRepository
                        .findById(entity.getTicketTransactionId())
                        .ifPresent(txn -> {
                            EventTicketTransactionDTO txnDto = eventTicketTransactionMapper.toDto(txn);

                            // Fetch transaction items
                            List<EventTicketTransactionItem> items = eventTicketTransactionItemRepository.findByTransactionId(txn.getId());
                            List<EventTicketTransactionItemDTO> itemDtos = items
                                .stream()
                                .map(eventTicketTransactionItemMapper::toDto)
                                .toList();

                            // Set transaction items in DTO
                            txnDto.setTransactionItems(itemDtos);
                            dto.setTicketTransaction(txnDto);
                        });
                }

                // Fetch event details
                if (entity.getEventId() != null) {
                    eventDetailsRepository
                        .findById(entity.getEventId())
                        .ifPresent(event -> {
                            EventDetailsDTO eventDto = eventDetailsMapper.toDto(event);
                            dto.setEvent(eventDto);
                        });
                }

                return dto;
            });
    }

    @Override
    public void triggerConfirmationEmail(Long paymentRequestId) {
        try {
            ManualPaymentRequest paymentRequest = manualPaymentRequestRepository
                .findById(paymentRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Manual payment request not found: " + paymentRequestId));

            // Fetch event details
            EventDetails event = null;
            if (paymentRequest.getEventId() != null) {
                event = eventDetailsRepository.findById(paymentRequest.getEventId()).orElse(null);
            }

            // Build confirmation email job request
            ManualPaymentConfirmationEmailJobRequest request = new ManualPaymentConfirmationEmailJobRequest();
            request.setTenantId(paymentRequest.getTenantId());
            request.setPaymentRequestId(paymentRequest.getId());
            request.setEventId(paymentRequest.getEventId());
            request.setRecipientEmail(paymentRequest.getRequesterEmail());
            String recipientName =
                ((paymentRequest.getRequesterFirstName() != null ? paymentRequest.getRequesterFirstName() : "") +
                    " " +
                    (paymentRequest.getRequesterLastName() != null ? paymentRequest.getRequesterLastName() : "")).trim();
            // Batch job requires @NotBlank validation, so provide default if name is empty
            if (recipientName.isEmpty()) {
                // Extract name from email (part before @) or use "Customer" as fallback
                String email = paymentRequest.getRequesterEmail();
                if (email != null && email.contains("@")) {
                    recipientName = email.substring(0, email.indexOf("@"));
                } else {
                    recipientName = "Customer";
                }
            }
            request.setRecipientName(recipientName);
            request.setPaymentMethod(
                paymentRequest.getPaymentMethodType() != null ? paymentRequest.getPaymentMethodType().toString() : null
            );
            request.setAmount(paymentRequest.getAmountDue());
            request.setPaymentInstructions(paymentRequest.getPaymentInstructions());

            if (event != null) {
                request.setEventTitle(event.getTitle());
                request.setEventDate(event.getStartDate() != null ? event.getStartDate().toString() : null);
                request.setEventLocation(event.getLocation());
            }

            // Trigger batch job
            manualPaymentBatchJobService.triggerConfirmationEmailJob(request);
            log.info("Confirmation email batch job triggered for manual payment request {}", paymentRequestId);
        } catch (Exception e) {
            log.error("Failed to trigger confirmation email for manual payment request {}: {}", paymentRequestId, e.getMessage(), e);
            // Don't throw - this is best-effort
        }
    }

    /**
     * Trigger ticket email batch job when payment is confirmed.
     */
    private void triggerTicketEmailJob(ManualPaymentRequest paymentRequest, EventTicketTransaction ticketTransaction) {
        try {
            // Fetch event details
            EventDetails event = null;
            if (paymentRequest.getEventId() != null) {
                event = eventDetailsRepository.findById(paymentRequest.getEventId()).orElse(null);
            }

            // Fetch transaction items
            List<EventTicketTransactionItem> items = eventTicketTransactionItemRepository.findByTransactionId(ticketTransaction.getId());

            // Build ticket items DTOs
            List<TicketItemDTO> ticketItemDtos = new ArrayList<>();
            for (EventTicketTransactionItem item : items) {
                EventTicketType ticketType = eventTicketTypeRepository.findById(item.getTicketTypeId()).orElse(null);
                if (ticketType != null) {
                    TicketItemDTO ticketItemDto = new TicketItemDTO();
                    ticketItemDto.setTicketTypeName(ticketType.getName());
                    ticketItemDto.setQuantity(item.getQuantity());
                    ticketItemDto.setPricePerUnit(item.getPricePerUnit());
                    ticketItemDto.setTotalAmount(item.getTotalAmount());
                    ticketItemDtos.add(ticketItemDto);
                }
            }

            // Build ticket email job request
            ManualPaymentTicketEmailJobRequest request = new ManualPaymentTicketEmailJobRequest();
            request.setTenantId(paymentRequest.getTenantId());
            request.setPaymentRequestId(paymentRequest.getId());
            request.setEventId(paymentRequest.getEventId());
            request.setTicketTransactionId(ticketTransaction.getId());
            request.setRecipientEmail(paymentRequest.getRequesterEmail());
            String recipientName =
                ((paymentRequest.getRequesterFirstName() != null ? paymentRequest.getRequesterFirstName() : "") +
                    " " +
                    (paymentRequest.getRequesterLastName() != null ? paymentRequest.getRequesterLastName() : "")).trim();
            // Batch job requires @NotBlank validation, so provide default if name is empty
            if (recipientName.isEmpty()) {
                // Extract name from email (part before @) or use "Customer" as fallback
                String email = paymentRequest.getRequesterEmail();
                if (email != null && email.contains("@")) {
                    recipientName = email.substring(0, email.indexOf("@"));
                } else {
                    recipientName = "Customer";
                }
            }
            request.setRecipientName(recipientName);
            request.setQrCodeImageUrl(ticketTransaction.getQrCodeImageUrl());
            request.setTicketItems(ticketItemDtos);
            request.setTotalAmount(ticketTransaction.getTotalAmount());
            request.setTransactionReference(ticketTransaction.getTransactionReference());

            if (event != null) {
                request.setEventTitle(event.getTitle());
                request.setEventDate(event.getStartDate() != null ? event.getStartDate().toString() : null);
                request.setEventLocation(event.getLocation());
            }

            // Trigger batch job
            manualPaymentBatchJobService.triggerTicketEmailJob(request);
            log.info(
                "Ticket email batch job triggered for manual payment request {} and ticket transaction {}",
                paymentRequest.getId(),
                ticketTransaction.getId()
            );
        } catch (Exception e) {
            log.error("Failed to trigger ticket email for manual payment request {}: {}", paymentRequest.getId(), e.getMessage(), e);
            // Don't throw - this is best-effort
        }
    }

    private String getActiveProfilePrefix() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0];
            if ("prod".equalsIgnoreCase(profile) || "production".equalsIgnoreCase(profile)) {
                return "prod";
            }
            return "dev";
        }
        return "dev";
    }
}
