package com.nextjstemplate.web.rest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.EmailSenderService;
import com.nextjstemplate.service.QRCodeService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.UserProfileService;
import com.nextjstemplate.service.dto.*;
import com.nextjstemplate.service.mapper.EventDetailsMapper;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
import com.nextjstemplate.service.mapper.EventTicketTypeMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QRCodeResource {

    private final EventTicketTransactionRepository transactionRepository;
    private final S3Service s3Service;
    private final QRCodeService qrCodeService;
    private final EventTicketTransactionMapper eventTicketTransactionMapper;
    private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;
    private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;
    private final EventDetailsRepository eventDetailsRepository;
    private final EventDetailsMapper eventDetailsMapper;
    private final EventTicketTypeRepository eventTicketTypeRepository;
    private final EventTicketTypeMapper eventTicketTypeMapper;
    private final EmailSenderService emailSenderService;
    private final UserProfileService userProfileService;
    private final TenantSettingsRepository tenantSettingsRepository;
    private final JwtEncoder jwtEncoder;
    private final Environment environment;
    private final MeterRegistry meterRegistry;
    private static final Logger log = LoggerFactory.getLogger(QRCodeResource.class);

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;

    @Value("${aws.s3.region}")
    private String s3Region;

    @Value("${email.tenant.rate-limit-per-second:50}")
    private double tenantRateLimitPerSecond;

    // Cache for S3 footer HTML (per tenant, expires after 1 hour)
    private final Cache<String, String> footerHtmlCache = CacheBuilder
        .newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    // Tenant-based rate limiters
    private final Map<String, RateLimiter> tenantRateLimiters = new ConcurrentHashMap<>();

    // Metrics
    private final Counter promoEmailSentCounter;
    private final Counter promoEmailBatchCounter;
    private final Counter promoEmailFailedCounter;
    private final Counter promoEmailCacheHitCounter;
    private final Counter promoEmailCacheMissCounter;

    /*
     * @Value("${email.host.url-prefix}")
     * private String emailHostUrlPrefix;
     */

    @Autowired
    private EmailHostUrlPrefixDecoder decoder;

    @Autowired
    public QRCodeResource(
        EventTicketTransactionRepository transactionRepository,
        S3Service s3Service,
        QRCodeService qrCodeService,
        EventTicketTransactionMapper eventTicketTransactionMapper,
        EventTicketTransactionItemMapper eventTicketTransactionItemMapper,
        EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
        EventDetailsRepository eventDetailsRepository,
        EventDetailsMapper eventDetailsMapper,
        EventTicketTypeRepository eventTicketTypeRepository,
        EventTicketTypeMapper eventTicketTypeMapper,
        EmailSenderService emailSenderService,
        UserProfileService userProfileService,
        TenantSettingsRepository tenantSettingsRepository,
        JwtEncoder jwtEncoder,
        Environment environment,
        MeterRegistry meterRegistry
    ) {
        this.transactionRepository = transactionRepository;
        this.s3Service = s3Service;
        this.qrCodeService = qrCodeService;
        this.eventTicketTransactionMapper = eventTicketTransactionMapper;
        this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
        this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
        this.eventDetailsRepository = eventDetailsRepository;
        this.eventDetailsMapper = eventDetailsMapper;
        this.eventTicketTypeRepository = eventTicketTypeRepository;
        this.eventTicketTypeMapper = eventTicketTypeMapper;
        this.emailSenderService = emailSenderService;
        this.userProfileService = userProfileService;
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.jwtEncoder = jwtEncoder;
        this.environment = environment;
        this.meterRegistry = meterRegistry;

        // Initialize metrics
        this.promoEmailSentCounter =
            Counter
                .builder("promo.email.sent.total")
                .description("Total number of promotional emails sent successfully")
                .register(meterRegistry);
        this.promoEmailBatchCounter =
            Counter
                .builder("promo.email.batch.total")
                .description("Total number of promotional email batches processed")
                .register(meterRegistry);
        this.promoEmailFailedCounter =
            Counter
                .builder("promo.email.failed.total")
                .description("Total number of failed promotional email sends")
                .register(meterRegistry);
        this.promoEmailCacheHitCounter =
            Counter.builder("promo.email.cache.hit.total").description("Total number of S3 footer HTML cache hits").register(meterRegistry);
        this.promoEmailCacheMissCounter =
            Counter
                .builder("promo.email.cache.miss.total")
                .description("Total number of S3 footer HTML cache misses")
                .register(meterRegistry);
    }

    /**
     * Get or create tenant-specific rate limiter
     */
    private RateLimiter getTenantRateLimiter(String tenantId) {
        return tenantRateLimiters.computeIfAbsent(
            tenantId,
            id -> {
                log.debug("Creating rate limiter for tenant {}: {} emails/second", id, tenantRateLimitPerSecond);
                return RateLimiter.create(tenantRateLimitPerSecond);
            }
        );
    }

    /**
     * Get the active Spring profile prefix for S3 paths.
     * Returns the first active profile or "default" if none is set.
     *
     * @return the active profile prefix
     */
    private String getActiveProfilePrefix() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            String profile = activeProfiles[0];
            // Map common profile names to S3 path prefixes
            if ("prod".equalsIgnoreCase(profile) || "production".equalsIgnoreCase(profile)) {
                return "prod";
            }
            // Default to "dev" for dev, local, or any other profile
            return "dev";
        }
        // Default to "dev" for local development when no profile is set
        return "dev";
    }

    /**
     * Construct the S3 base URL dynamically using configured bucket name and region.
     *
     * @return the S3 base URL (e.g., "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com")
     */
    private String getS3BaseUrl() {
        return String.format("https://%s.s3.%s.amazonaws.com", s3BucketName, s3Region);
    }

    private QrCodeUsageDTO buildQrCodeUsageDTO(Long eventId, Long transactionId) {
        Optional<EventTicketTransaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            return null;
        }
        EventTicketTransaction transaction = transactionOpt.orElseThrow();
        if (!eventId.equals(transaction.getEventId())) {
            return null;
        }
        EventTicketTransactionDTO transactionDTO = eventTicketTransactionMapper.toDto(transaction);
        List<EventTicketTransactionItemDTO> itemDTOs = eventTicketTransactionItemRepository
            .findByTransactionId(transactionId)
            .stream()
            .map(eventTicketTransactionItemMapper::toDto)
            .collect(Collectors.toList());
        Optional<EventDetailsDTO> eventDetailsDTO = eventDetailsRepository
            .findOneWithEagerRelationships(eventId)
            .map(eventDetailsMapper::toDto);
        if (eventDetailsDTO.isEmpty()) {
            return null;
        }
        List<EventTicketTypeDTO> eventTicketTypeDTOs = eventTicketTypeRepository
            .findByEvent_Id(eventId)
            .stream()
            .map(eventTicketTypeMapper::toDto)
            .collect(Collectors.toList());
        return new QrCodeUsageDTO(transactionDTO, itemDTOs, eventDetailsDTO.orElseThrow(), eventTicketTypeDTOs);
    }

    @GetMapping("/qrcode-scan/tickets/events/{eventId}/transactions/{transactionId}")
    public ResponseEntity<QrCodeUsageDTO> getQRCodeScanDetails(@PathVariable Long eventId, @PathVariable Long transactionId) {
        QrCodeUsageDTO result = buildQrCodeUsageDTO(eventId, transactionId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/events/{eventId}/transactions/{transactionId}/emailHostUrlPrefix/{emailHostUrlPrefix}/qrcode")
    public ResponseEntity<?> getQRCodeImage(
        @PathVariable Long eventId,
        @PathVariable Long transactionId,
        @PathVariable String emailHostUrlPrefix
    ) {
        log.debug("Enter: getQRCodeImage() with argument[s] = [{}, {}, {}]", eventId, transactionId, emailHostUrlPrefix);

        QrCodeUsageDTO dto = buildQrCodeUsageDTO(eventId, transactionId);
        if (dto == null) {
            log.warn("QR code data not found for eventId={}, transactionId={}", eventId, transactionId);
            return ResponseEntity.notFound().build();
        }

        String decodedEmailHostUrlPrefix = decoder.decodeEmailHostUrlPrefix(emailHostUrlPrefix);
        String qrCodeImageUrl = dto.getTransaction().getQrCodeImageUrl();

        // CRITICAL: Log the QR code image URL to diagnose production issues
        log.info("QR code image URL for eventId={}, transactionId={}: {}", eventId, transactionId, qrCodeImageUrl);

        if (qrCodeImageUrl == null || qrCodeImageUrl.isEmpty()) {
            log.error(
                "QR code image URL is NULL or EMPTY for eventId={}, transactionId={}. " +
                "This indicates QR code was not generated during ticket purchase. " +
                "Attempting to generate QR code on-demand...",
                eventId,
                transactionId
            );

            // FALLBACK: Try to generate QR code on-demand if missing
            try {
                qrCodeImageUrl =
                    generateQRCodeOnDemand(eventId, transactionId, decodedEmailHostUrlPrefix, dto.getTransaction().getTenantId());
                if (qrCodeImageUrl != null && !qrCodeImageUrl.isEmpty()) {
                    log.info(
                        "Successfully generated QR code on-demand for eventId={}, transactionId={}: {}",
                        eventId,
                        transactionId,
                        qrCodeImageUrl
                    );
                } else {
                    log.error("Failed to generate QR code on-demand for eventId={}, transactionId={}", eventId, transactionId);
                    return ResponseEntity.notFound().build();
                }
            } catch (java.io.IOException e) {
                log.error(
                    "IOException while generating QR code on-demand for eventId={}, transactionId={}: {}",
                    eventId,
                    transactionId,
                    e.getMessage(),
                    e
                );
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                log.error(
                    "Exception while generating QR code on-demand for eventId={}, transactionId={}: {}",
                    eventId,
                    transactionId,
                    e.getMessage(),
                    e
                );
                return ResponseEntity.notFound().build();
            }
        }

        log.debug(
            "Exit: getQRCodeImage() with result = <200 OK OK,[Content-Type:\"image/png\", Content-Disposition:\"inline; filename=\\\"qrcode.png\\\"\"]>"
        );
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType("image/png"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
            .body(qrCodeImageUrl);
    }

    /**
     * Generate QR code on-demand if it's missing from the transaction.
     * This is a fallback mechanism for transactions that were created before QR code generation was implemented,
     * or for cases where QR code generation failed during ticket purchase.
     *
     * @throws IOException if QR code generation or S3 upload fails
     */
    private String generateQRCodeOnDemand(Long eventId, Long transactionId, String emailHostUrlPrefix, String tenantId)
        throws java.io.IOException {
        log.info("Enter: generateQRCodeOnDemand() for eventId={}, transactionId={}, tenantId={}", eventId, transactionId, tenantId);

        try {
            // Build QR scan URL content
            String qrScanUrlContent = emailHostUrlPrefix + "/qrcode-scan/tickets/events/" + eventId + "/transactions/" + transactionId;
            log.debug("QR scan URL content: {}", qrScanUrlContent);

            // Generate and upload QR code using injected service
            String qrCodeImageUrl = qrCodeService.generateAndUploadQRCode(
                qrScanUrlContent,
                eventId,
                String.valueOf(transactionId),
                tenantId
            );

            // Update the transaction with the QR code URL
            transactionRepository
                .findById(transactionId)
                .ifPresentOrElse(
                    transaction -> {
                        transaction.setQrCodeImageUrl(qrCodeImageUrl);
                        transaction.setUpdatedAt(java.time.ZonedDateTime.now());
                        transactionRepository.save(transaction);
                        log.info("Updated transaction {} with QR code image URL: {}", transactionId, qrCodeImageUrl);
                    },
                    () -> log.warn("Transaction {} not found when trying to update QR code URL", transactionId)
                );

            return qrCodeImageUrl;
        } catch (java.io.IOException e) {
            log.error(
                "Failed to generate QR code on-demand for eventId={}, transactionId={}: {}",
                eventId,
                transactionId,
                e.getMessage(),
                e
            );
            throw e;
        } catch (Exception e) {
            log.error(
                "Unexpected error generating QR code on-demand for eventId={}, transactionId={}: {}",
                eventId,
                transactionId,
                e.getMessage(),
                e
            );
            // Wrap non-IOException exceptions in RuntimeException to avoid changing method signature
            throw new RuntimeException("Failed to generate QR code on-demand", e);
        }
    }

    @PostMapping("/events/{eventId}/transactions/{transactionId}/emailHostUrlPrefix/{emailHostUrlPrefix}/send-ticket-email")
    @Async
    public void sendTicketEmail(
        @PathVariable Long eventId,
        @PathVariable Long transactionId,
        @RequestParam(value = "to", required = false) String to,
        @PathVariable String emailHostUrlPrefix
    ) {
        QrCodeUsageDTO dto = buildQrCodeUsageDTO(eventId, transactionId);
        if (dto == null) {
            return;
        }
        String recipient = (to != null && !to.isBlank()) ? to : "test@example.com";

        // CRITICAL: Get tenantId first for multi-tenant email subscription check
        String tenantId = dto.getTransaction().getTenantId();

        // Check subscription - MUST use findByEmailAndTenantId to avoid NonUniqueResultException
        // In multi-tenant systems, the same email can exist across different tenants
        boolean isSubscribed = userProfileService
            .findByEmailAndTenantId(recipient, tenantId)
            .map(u -> Boolean.TRUE.equals(u.getIsEmailSubscribed()))
            .orElse(true); // Default to true if not found
        if (!isSubscribed) {
            return;
        }
        // Generate unsubscribe token and link
        JwtClaimsSet claims = JwtClaimsSet.builder().subject(recipient).build();
        JwsHeader jwsHeader = JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        String unsubscribeLinkPrefix = emailHostUrlPrefix + "/unsubscribe-email?email=%s&token=%s";
        String unsubscribeLink = String.format(unsubscribeLinkPrefix, recipient, token);
        // https://yourdomain.com/unsubscribe-email?email=user@example.com&token=UNIQUE_TOKEN
        String unsubscribeHtml = String.format(
            "<div style='margin:24px 0 0 0; text-align:center; color:#888; font-size:13px;'>If you no longer wish to receive these emails, <a href='%s' style='color:#6b207c;'>click here to unsubscribe</a>.</div>",
            unsubscribeLink
        );
        String profilePrefix = getActiveProfilePrefix();
        String s3BaseUrl = getS3BaseUrl();

        // Get email header image URL from event_details table, fallback to default path if not set
        String headerImageUrl = dto.getEventDetails().getEmailHeaderImageUrl();
        if (headerImageUrl == null || headerImageUrl.isEmpty()) {
            // Fallback to default path for backward compatibility
            headerImageUrl =
                String.format(
                    "%s/%s/events/tenantId/%s/event-id/%d/tickets/email-templates/email_header_image.jpeg",
                    s3BaseUrl,
                    profilePrefix,
                    tenantId,
                    eventId
                );
            log.debug("Using default email header image path for event {}: {}", eventId, headerImageUrl);
        } else {
            log.debug("Using custom email header image URL for event {}: {}", eventId, headerImageUrl);
        }

        String qrCodeImageUrl = dto.getTransaction().getQrCodeImageUrl();
        String eventName = dto.getEventDetails().getTitle();
        // Format event date and time
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);
        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
        DateTimeFormatter timeRangeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        DateTimeFormatter timeWithZoneFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
        String eventDate = "";
        String eventStart = "";
        String eventEnd = "";
        String eventTimeZoneId = dto.getEventDetails().getTimezone();
        java.time.ZoneId eventZoneId = java.time.ZoneId.systemDefault();
        String eventTimeZoneAbbr = "";
        if (eventTimeZoneId != null && !eventTimeZoneId.isBlank()) {
            try {
                eventZoneId = java.time.ZoneId.of(eventTimeZoneId);
                java.time.LocalDate endDate = dto.getEventDetails().getEndDate();
                java.time.ZonedDateTime endZdt = endDate.atStartOfDay(eventZoneId);
                eventTimeZoneAbbr = endZdt.getZone().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
            } catch (Exception e) {
                eventZoneId = java.time.ZoneId.systemDefault();
                eventTimeZoneAbbr = eventZoneId.getId();
            }
        }
        if (dto.getEventDetails().getStartDate() != null && dto.getEventDetails().getEndDate() != null) {
            java.time.LocalDate startDate = dto.getEventDetails().getStartDate();
            java.time.LocalDate endDate = dto.getEventDetails().getEndDate();
            java.time.ZonedDateTime start = startDate.atStartOfDay(eventZoneId);
            java.time.ZonedDateTime end = endDate.atStartOfDay(eventZoneId);
            eventDate = start.format(dateOnlyFormatter);
            eventStart = start.format(timeRangeFormatter);
            eventEnd = end.format(timeRangeFormatter) + " (" + eventTimeZoneAbbr + ")";
        }
        String dateOfPurchase = "";
        if (dto.getTransaction().getCreatedAt() != null) {
            dateOfPurchase = dto.getTransaction().getCreatedAt().format(dateTimeFormatter);
        }
        String checkInTime = "";
        if (dto.getTransaction().getCheckInTime() != null) {
            checkInTime = dto.getTransaction().getCheckInTime().format(dateTimeFormatter);
        }
        // Transaction details
        String name = dto.getTransaction().getFirstName();
        String email = dto.getTransaction().getEmail();
        String amountPaid = dto.getTransaction().getTotalAmount() != null
            ? String.format("$%.2f", dto.getTransaction().getFinalAmount())
            : "";

        EventTicketTransactionDTO txn = dto.getTransaction();
        String ticketNumber;
        if (txn.getTransactionReference() != null && !txn.getTransactionReference().isEmpty()) {
            ticketNumber = txn.getTransactionReference();
        } else if (txn.getId() != null) {
            ticketNumber = "TKTN" + txn.getId().toString();
        } else {
            ticketNumber = null; // or handle as needed
        }

        String checkInStatus = dto.getTransaction().getCheckInStatus();

        // Build ticket breakdown rows with centered text
        StringBuilder ticketBreakdownRows = new StringBuilder();
        for (EventTicketTransactionItemDTO item : dto.getItems()) {
            String typeName = "";
            for (EventTicketTypeDTO type : dto.getEventTicketTypes()) {
                if (type.getId().equals(item.getTicketTypeId())) {
                    typeName = type.getName();
                    break;
                }
            }
            ticketBreakdownRows.append(
                String.format(
                    "<tr>" +
                    "<td style='padding:8px; text-align:center;'>%s</td>" +
                    "<td style='padding:8px; text-align:center;'>%d</td>" +
                    "<td style='padding:8px; text-align:center;'>$%.2f</td>" +
                    "<td style='padding:8px; text-align:center;'>$%.2f</td>" +
                    "</tr>",
                    typeName,
                    item.getQuantity(),
                    item.getPricePerUnit(),
                    item.getTotalAmount()
                )
            );
        }

        // Emojis for section headings
        String moneyEmoji = "💵";
        String ticketEmoji = "🎟️";
        String calendarEmoji = "📅";
        String qrEmoji = "🎫";
        String discountEmoji = "🏷️";
        String locationEmoji = "📍";
        String discountAmount = "";
        if (dto.getTransaction().getDiscountAmount() != null && dto.getTransaction().getDiscountAmount().doubleValue() > 0) {
            discountAmount = String.format("%s $%.2f", discountEmoji, dto.getTransaction().getDiscountAmount());
        }

        // Enhanced location display with Google Maps link
        String locationAddress = dto.getEventDetails().getLocation();
        String googleMapsUrl = "";
        String locationDisplay = "";
        if (locationAddress != null && !locationAddress.trim().isEmpty()) {
            // URL encode the address for Google Maps
            try {
                googleMapsUrl =
                    "https://www.google.com/maps/search/" +
                    java.net.URLEncoder.encode(locationAddress, java.nio.charset.StandardCharsets.UTF_8);
            } catch (Exception e) {
                googleMapsUrl = "https://www.google.com/maps";
            }

            locationDisplay =
                String.format(
                    """
                    <div style="display: flex; align-items: flex-start; gap: 8px; margin-top: 8px;">
                      <span style="color: #dc2626; font-size: 16px; margin-top: 2px;">%s</span>
                      <div style="flex: 1;">
                        <div style="background: #f8f9fa; padding: 8px; border-radius: 4px; border-left: 3px solid #dc2626; margin: 4px 0;">
                          <span style="color: #374151; line-height: 1.5; font-weight: 500;">%s</span>
                        </div>
                        <div style="margin-top: 8px;">
                          <a href="%s" target="_blank"
                             style="display: inline-flex; align-items: center; gap: 4px; padding: 6px 12px;
                                    background: #f3f4f6; border-radius: 6px; text-decoration: none;
                                    color: #059669; font-size: 13px; border: 1px solid #d1d5db;">
                            <span>🗺️</span>
                            <span>View on Google Maps</span>
                          </a>
                        </div>
                      </div>
                    </div>
                    """,
                    locationEmoji,
                    locationAddress,
                    googleMapsUrl
                );
        } else {
            locationDisplay = "<span style=\"color: #6b7280;\">Location TBD</span>";
        }

        // Build additional table rows for discount and amount paid
        String additionalRows = "";

        // Add discount row if applicable
        if (!discountAmount.isEmpty()) {
            additionalRows +=
            String.format(
                "<tr style=\"border-top: 2px solid #e5e7eb;\"><td colspan=\"2\" style=\"padding:8px; text-align:right; font-weight:bold;\">%s Discount Applied:</td><td colspan=\"2\" style=\"padding:8px; text-align:center; color:#dc2626; font-weight:bold;\">-%s</td></tr>",
                discountEmoji,
                String.format("$%.2f", dto.getTransaction().getDiscountAmount())
            );
        }

        // Add amount paid row
        String finalAmount = dto.getTransaction().getFinalAmount() != null
            ? String.format("$%.2f", dto.getTransaction().getFinalAmount())
            : String.format("$%.2f", dto.getTransaction().getTotalAmount());

        additionalRows +=
        String.format(
            "<tr style=\"border-top: 2px solid #e5e7eb; background:#f0f9ff;\"><td colspan=\"2\" style=\"padding:12px; text-align:right; font-weight:bold; font-size:16px;\">%s Amount Paid:</td><td colspan=\"2\" style=\"padding:12px; text-align:center; color:#059669; font-weight:bold; font-size:16px;\">%s</td></tr>",
            moneyEmoji,
            finalAmount
        );
        String template = String.format(
            """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset=\"UTF-8\">
              <title>Your Event Ticket</title>
            </head>
            <body style=\"font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px;\">
              <div style=\"max-width: 650px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #eee; overflow: hidden;\">
                <img src=\"%s\" alt=\"Event Header\" style=\"width: 100%%; display: block; border-bottom: 1px solid #eee;\">
                <div style=\"padding: 32px;\">
                  <h2 style=\"margin-top: 0; text-align:center; color:#6b207c;\">%s Your Ticket QR Code</h2>
                  <div style=\"text-align:center; margin-bottom: 32px;\">
                    <img src=\"%s\" alt=\"QR Code\" style=\"width:180px;height:180px; border: 1px solid #eee; border-radius: 8px; background: #fafafa; padding: 12px;\">
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; color:#6b207c;\">%s Ticket # <span style='color:#2d3748;font-weight:bold;'>%s</span></h3>
                    <h3 style=\"margin-bottom: 12px; color:#6b207c;\">%s Ticket Breakdown</h3>
                    <table style=\"width:100%%; border-collapse:collapse;\">
                      <tr style=\"background:#f5f5f5;\">
                        <th style=\"padding:8px; text-align:center;\">Ticket Type</th>
                        <th style=\"padding:8px; text-align:center;\">Quantity</th>
                        <th style=\"padding:8px; text-align:center;\">Price Per Unit</th>
                        <th style=\"padding:8px; text-align:center;\">Total</th>
                      </tr>
                      %s
                      %s
                    </table>
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; color:#6b207c;\">%s Event Details</h3>
                    <p>
                      <strong>%s</strong><br>
                      <span>%s | %s - %s</span><br>
                    </p>
                    %s
                  </div>
                </div>
              </div>
            </body>
            </html>
            """,
            headerImageUrl,
            qrEmoji,
            qrCodeImageUrl,
            ticketEmoji,
            ticketNumber,
            ticketEmoji,
            ticketBreakdownRows.toString(),
            additionalRows,
            calendarEmoji,
            eventName,
            eventDate,
            eventStart,
            eventEnd,
            locationDisplay
        );

        // Prepare S3 URLs for footer and logo, replacing tenant_demo_001 with actual
        // tenantId
        String tenantIdPath = tenantId != null ? tenantId : "tenant_demo_001";
        String footerHtmlS3Url = String.format(
            "%s/%s/events/tenantId/%s/email-templates/email_footer.html",
            s3BaseUrl,
            profilePrefix,
            tenantIdPath
        );
        String logoS3Url = String.format(
            "%s/%s/events/tenantId/%s/email-templates/email_footer_logo.png",
            s3BaseUrl,
            profilePrefix,
            tenantIdPath
        );

        // Download the footer HTML from S3
        String footerHtml = "";
        try {
            footerHtml = s3Service.downloadHtmlFromUrl(footerHtmlS3Url); // Implement this method in S3Service
            // Replace the logo placeholder
            footerHtml = footerHtml.replace("{{LOGO_URL}}", logoS3Url);
        } catch (Exception e) {
            // If download fails, fallback to empty or default footer
            footerHtml = "";
        }

        // Build ticket refund policy disclaimer
        String ticketPolicyHtml =
            """
            <div style="margin: 24px 0; padding: 20px; background: #f8f9fa; border-radius: 8px; font-size: 13px; color: #555;">
              <h4 style="margin: 0 0 12px 0; color: #333; font-size: 14px;">Ticket Sales Policy</h4>
              <p style="margin: 0 0 12px 0;"><strong>All ticket sales are final and non-refundable, with the following exceptions:</strong></p>
              <ul style="margin: 0 0 12px 0; padding-left: 20px;">
                <li style="margin-bottom: 8px;"><strong>Event Cancellation:</strong> If the event is outright canceled, you will be eligible for a full refund.</li>
                <li style="margin-bottom: 8px;"><strong>Event Postponement/Rescheduling:</strong> In the event of a postponement or rescheduling, you may be offered the option of a refund or the ability to use your ticket for the new date, depending on the specific circumstances. Please refer to further communication regarding postponed or rescheduled events for details.</li>
                <li style="margin-bottom: 8px;"><strong>Refund Processing:</strong> Approved refunds will typically be processed back to the original method of payment.</li>
              </ul>
              <p style="margin: 0; font-weight: bold;"><strong>By purchasing this ticket, you acknowledge and agree to the terms of this Ticket Sales Policy.</strong></p>
            </div>
            """;

        // ... build the main email HTML as before ...
        String fullEmailHtml = template + unsubscribeHtml + footerHtml + ticketPolicyHtml;

        // Get from email with fallback chain:
        // 1. Event's fromEmail (required field)
        // 2. TenantSettings email (if event fromEmail is not set or empty)
        // 3. Default config email (handled by EmailSenderService)
        String fromEmail = dto.getEventDetails().getFromEmail();
        if (fromEmail == null || fromEmail.isEmpty()) {
            // Try tenant settings as fallback
            fromEmail =
                tenantSettingsRepository
                    .findByTenantId(tenantId)
                    .map(settings -> settings.getEmail())
                    .filter(tenantEmail -> tenantEmail != null && !tenantEmail.isEmpty())
                    .orElse(null);
        }

        // List-Unsubscribe header
        Map<String, String> headers = new HashMap<>();
        headers.put("List-Unsubscribe", EmailSenderService.buildListUnsubscribeHeader(recipient, unsubscribeLink));

        // Send email with fromEmail if available, otherwise use default
        if (fromEmail != null && !fromEmail.isEmpty()) {
            emailSenderService.sendEmail(fromEmail, recipient, "Your Event Ticket for " + eventName, fullEmailHtml, true, headers);
        } else {
            emailSenderService.sendEmail(recipient, "Your Event Ticket for " + eventName, fullEmailHtml, true, headers);
        }
    }

    @PostMapping("/send-promotion-emails")
    public ResponseEntity<Map<String, Object>> sendPromotionEmail(@RequestBody PromotionEmailRequestDTO requestDTO) {
        String tenantId = requestDTO.getTenantId();
        String recipient = requestDTO.getTo();
        String subject = requestDTO.getSubject();
        String promoCode = requestDTO.getPromoCode();
        String bodyHtml = requestDTO.getBodyHtml();
        String headerImagePath = requestDTO.getHeaderImagePath();
        String footerPath = requestDTO.getFooterPath();
        boolean isTestEmail = requestDTO.isTestEmail();
        String emailHostUrlPrefix = requestDTO.getEmailHostUrlPrefix();
        sendPromoEmails(tenantId, promoCode, footerPath, bodyHtml, recipient, subject, isTestEmail, emailHostUrlPrefix);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @Async("emailTaskExecutor")
    protected void sendPromoEmails(
        String tenantId,
        String promoCode,
        String footerPath,
        String bodyHtml,
        String recipient,
        String subject,
        boolean isTestEmail,
        String emailHostUrlPrefix
    ) {
        if (isTestEmail) {
            // For test emails, we still need to lookup the user
            userProfileService
                .findByEmailAndTenantId(recipient, tenantId)
                .ifPresent(user -> sendPromoEmailToSingleRecipient(user, tenantId, promoCode, bodyHtml, subject, emailHostUrlPrefix));
            return;
        }

        // Use batch processing for large email lists
        int batchSize = 100; // Process 100 emails at a time
        int offset = 0;

        while (true) {
            List<UserProfileDTO> userBatch = userProfileService.findSubscribedUsersByTenantIdWithPagination(tenantId, batchSize, offset);

            if (userBatch.isEmpty()) {
                break; // No more users to process
            }

            // Get tenant rate limiter
            RateLimiter tenantRateLimiter = getTenantRateLimiter(tenantId);

            // Filter subscribed users with valid tokens (already filtered by query, but double-check)
            List<UserProfileDTO> validUsers = userBatch
                .stream()
                .filter(user -> Boolean.TRUE.equals(user.getIsEmailSubscribed()))
                .filter(user -> user.getEmailSubscriptionToken() != null && !user.getEmailSubscriptionToken().isBlank())
                .collect(Collectors.toList());

            // Group users into batches for SES batch sending (50 per batch)
            int sesBatchSize = 50;
            for (int i = 0; i < validUsers.size(); i += sesBatchSize) {
                int endIndex = Math.min(i + sesBatchSize, validUsers.size());
                List<UserProfileDTO> sesBatch = validUsers.subList(i, endIndex);

                // Apply tenant rate limiting
                if (!tenantRateLimiter.tryAcquire()) {
                    log.warn(
                        "Tenant {} rate limit exceeded, skipping batch {}/{}",
                        tenantId,
                        (i / sesBatchSize + 1),
                        (int) Math.ceil((double) validUsers.size() / sesBatchSize)
                    );
                    promoEmailFailedCounter.increment(sesBatch.size());
                    continue;
                }

                try {
                    sendPromoEmailBatch(sesBatch, tenantId, promoCode, bodyHtml, subject, emailHostUrlPrefix);
                    promoEmailBatchCounter.increment();
                    promoEmailSentCounter.increment(sesBatch.size());
                } catch (Exception e) {
                    log.error("Failed to send email batch for tenant {}: {}", tenantId, e.getMessage(), e);
                    promoEmailFailedCounter.increment(sesBatch.size());
                }
            }

            offset += batchSize;

            // Add a small delay between batches to avoid overwhelming the email service
            if (!userBatch.isEmpty()) {
                try {
                    Thread.sleep(2000); // 2 second delay between batches
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Alternative method using custom thread pool for even better performance
    @Async("emailTaskExecutor")
    protected void sendPromoEmailsWithCustomExecutor(
        String tenantId,
        String promoCode,
        String footerPath,
        String bodyHtml,
        String recipient,
        String subject,
        boolean isTestEmail,
        String emailHostUrlPrefix
    ) {
        if (isTestEmail) {
            // For test emails, we still need to lookup the user
            userProfileService
                .findByEmailAndTenantId(recipient, tenantId)
                .ifPresent(user -> sendPromoEmailToSingleRecipient(user, tenantId, promoCode, bodyHtml, subject, emailHostUrlPrefix));
            return;
        }

        // Use larger batches for custom executor
        int batchSize = 200; // Process 200 emails at a time
        int offset = 0;

        while (true) {
            List<UserProfileDTO> userBatch = userProfileService.findSubscribedUsersByTenantIdWithPagination(tenantId, batchSize, offset);

            if (userBatch.isEmpty()) {
                break;
            }

            // Get tenant rate limiter
            RateLimiter tenantRateLimiter = getTenantRateLimiter(tenantId);

            // Filter subscribed users with valid tokens
            List<UserProfileDTO> validUsers = userBatch
                .stream()
                .filter(user -> Boolean.TRUE.equals(user.getIsEmailSubscribed()))
                .filter(user -> user.getEmailSubscriptionToken() != null && !user.getEmailSubscriptionToken().isBlank())
                .collect(Collectors.toList());

            // Group users into batches for SES batch sending (50 per batch)
            int sesBatchSize = 50;
            for (int i = 0; i < validUsers.size(); i += sesBatchSize) {
                int endIndex = Math.min(i + sesBatchSize, validUsers.size());
                List<UserProfileDTO> sesBatch = validUsers.subList(i, endIndex);

                // Apply tenant rate limiting
                if (!tenantRateLimiter.tryAcquire()) {
                    log.warn(
                        "Tenant {} rate limit exceeded, skipping batch {}/{}",
                        tenantId,
                        (i / sesBatchSize + 1),
                        (int) Math.ceil((double) validUsers.size() / sesBatchSize)
                    );
                    promoEmailFailedCounter.increment(sesBatch.size());
                    continue;
                }

                try {
                    sendPromoEmailBatch(sesBatch, tenantId, promoCode, bodyHtml, subject, emailHostUrlPrefix);
                    promoEmailBatchCounter.increment();
                    promoEmailSentCounter.increment(sesBatch.size());
                } catch (Exception e) {
                    log.error("Failed to send email batch for tenant {}: {}", tenantId, e.getMessage(), e);
                    promoEmailFailedCounter.increment(sesBatch.size());
                }
            }

            offset += batchSize;

            // Shorter delay with custom executor
            if (!userBatch.isEmpty()) {
                try {
                    Thread.sleep(1000); // 1 second delay between batches
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    /**
     * Send promotional email batch using SES batch sending API
     * This method uses the batch data directly without redundant database lookups
     */
    private void sendPromoEmailBatch(
        List<UserProfileDTO> users,
        String tenantId,
        String promoCode,
        String bodyHtml,
        String subject,
        String emailHostUrlPrefix
    ) {
        if (users.isEmpty()) {
            return;
        }

        // Build email HTML template once (reusable for batch)
        String baseUnsubscribeLink = emailHostUrlPrefix + "/unsubscribe-email?email=%s&token=%s";
        String cachedFooterHtml = getCachedFooterHtml(tenantId);

        // Prepare batch email data
        List<String> recipientEmails = new ArrayList<>();
        Map<String, String> emailToUnsubscribeLink = new HashMap<>();
        Map<String, String> emailToHeaders = new HashMap<>();

        for (UserProfileDTO user : users) {
            String email = user.getEmail();
            String token = user.getEmailSubscriptionToken();
            String unsubscribeLink = String.format(baseUnsubscribeLink, email, token);

            recipientEmails.add(email);
            emailToUnsubscribeLink.put(email, unsubscribeLink);

            Map<String, String> headers = new HashMap<>();
            headers.put("List-Unsubscribe", EmailSenderService.buildListUnsubscribeHeader(email, unsubscribeLink));
            emailToHeaders.put(email, headers.toString()); // Note: SES batch API doesn't support per-recipient headers
        }

        // Build email HTML with first user's unsubscribe link (SES batch limitation)
        // For true per-recipient personalization, individual sends would be needed
        String firstUnsubscribeLink = emailToUnsubscribeLink.get(recipientEmails.get(0));
        String fullEmailHtml = buildPromotionEmailHtml(subject, tenantId, promoCode, bodyHtml, firstUnsubscribeLink, cachedFooterHtml);

        // Use SES batch sending
        Map<String, String> commonHeaders = new HashMap<>();
        if (!recipientEmails.isEmpty()) {
            commonHeaders.put(
                "List-Unsubscribe",
                EmailSenderService.buildListUnsubscribeHeader(recipientEmails.get(0), firstUnsubscribeLink)
            );
        }

        emailSenderService.sendBatchEmails(recipientEmails, subject, fullEmailHtml, true, commonHeaders);
    }

    /**
     * Send promotional email to single recipient (for test emails)
     * Uses user data directly without redundant database lookup
     */
    private void sendPromoEmailToSingleRecipient(
        UserProfileDTO user,
        String tenantId,
        String promoCode,
        String bodyHtml,
        String subject,
        String emailHostUrlPrefix
    ) {
        String email = user.getEmail();
        String token = user.getEmailSubscriptionToken();
        if (token == null || token.isBlank()) {
            return;
        }

        String unsubscribeLink = String.format(emailHostUrlPrefix + "/unsubscribe-email?email=%s&token=%s", email, token);
        String cachedFooterHtml = getCachedFooterHtml(tenantId);
        String fullEmailHtml = buildPromotionEmailHtml(subject, tenantId, promoCode, bodyHtml, unsubscribeLink, cachedFooterHtml);

        Map<String, String> headers = new HashMap<>();
        headers.put("List-Unsubscribe", EmailSenderService.buildListUnsubscribeHeader(email, unsubscribeLink));
        emailSenderService.sendEmail(email, subject, fullEmailHtml, true, headers);
    }

    /**
     * Get cached footer HTML for tenant, or fetch from S3 if not cached
     */
    private String getCachedFooterHtml(String tenantId) {
        String cacheKey = "footer:" + tenantId;
        String cachedHtml = footerHtmlCache.getIfPresent(cacheKey);

        if (cachedHtml != null) {
            promoEmailCacheHitCounter.increment();
            log.debug("Cache hit for footer HTML for tenant: {}", tenantId);
            return cachedHtml;
        }

        promoEmailCacheMissCounter.increment();
        log.debug("Cache miss for footer HTML for tenant: {}, fetching from S3", tenantId);

        try {
            String profilePrefix = getActiveProfilePrefix();
            String s3BaseUrl = getS3BaseUrl();
            String tenantIdPath = tenantId != null ? tenantId : "tenant_demo_001";
            String footerHtmlS3Url = String.format(
                "%s/%s/events/tenantId/%s/email-templates/email_footer.html",
                s3BaseUrl,
                profilePrefix,
                tenantIdPath
            );

            String footerHtml = s3Service.downloadHtmlFromUrl(footerHtmlS3Url);
            if (footerHtml != null && !footerHtml.isEmpty()) {
                footerHtmlCache.put(cacheKey, footerHtml);
                log.debug("Cached footer HTML for tenant: {}", tenantId);
            }
            return footerHtml != null ? footerHtml : "";
        } catch (Exception e) {
            log.warn("Failed to fetch footer HTML from S3 for tenant {}: {}", tenantId, e.getMessage());
            return "";
        }
    }

    // Extracted reusable method for building the promotion email HTML
    private String buildPromotionEmailHtml(
        String subject,
        String tenantId,
        String promoCode,
        String bodyHtml,
        String unsubscribeLink,
        String cachedFooterHtml
    ) {
        String profilePrefix = getActiveProfilePrefix();
        String s3BaseUrl = getS3BaseUrl();
        String headerImagePath = String.format(
            "%s/%s/events/tenantId/%s/promotions/promocode/%s/email-templates/email_header_image.jpeg",
            s3BaseUrl,
            profilePrefix,
            tenantId,
            promoCode
        );
        String tenantIdPath = tenantId != null ? tenantId : "tenant_demo_001";
        String logoS3Url = String.format(
            "%s/%s/events/tenantId/%s/email-templates/email_footer_logo.png",
            s3BaseUrl,
            profilePrefix,
            tenantIdPath
        );

        // Use cached footer HTML and replace logo URL placeholder
        String footerHtml = cachedFooterHtml != null ? cachedFooterHtml : "";
        footerHtml = footerHtml.replace("{{LOGO_URL}}", logoS3Url);
        String unsubscribeHtml = String.format(
            "<div style='margin:24px 0 0 0; text-align:center; color:#888; font-size:13px;'>If you no longer wish to receive these emails, <a href='%s' style='color:#6b207c;'>click here to unsubscribe</a>.</div>",
            unsubscribeLink
        );
        return String.format(
            """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset=\"UTF-8\">
              <title>%s</title>
            </head>
            <body style=\"font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px;\">
              <div style=\"max-width: 650px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #eee; overflow: hidden;\">
                <img src=\"%s\" alt=\"Event Header\" style=\"width: 100%%; display: block; border-bottom: 1px solid #eee;\">
                <div style=\"padding: 32px;\">
                  %s
                  %s
                </div>
                %s
              </div>
            </body>
            </html>
            """,
            subject,
            headerImagePath,
            bodyHtml,
            unsubscribeHtml,
            footerHtml
        );
    }
}
