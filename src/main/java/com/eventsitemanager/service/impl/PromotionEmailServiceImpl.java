package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventAttendee;
import com.eventsitemanager.domain.PromotionEmailSentLog;
import com.eventsitemanager.domain.PromotionEmailTemplate;
import com.eventsitemanager.domain.TenantSettings;
import com.eventsitemanager.domain.enumeration.EmailStatus;
import com.eventsitemanager.repository.EventAttendeeRepository;
import com.eventsitemanager.repository.PromotionEmailSentLogRepository;
import com.eventsitemanager.repository.PromotionEmailTemplateRepository;
import com.eventsitemanager.repository.TenantSettingsRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.BatchJobEmailService;
import com.eventsitemanager.service.EmailSenderService;
import com.eventsitemanager.service.PromotionEmailService;
import com.eventsitemanager.service.PromotionTestEmailBatchJobService;
import com.eventsitemanager.service.S3Service;
import com.eventsitemanager.service.UserProfileService;
import com.eventsitemanager.service.dto.BatchJobEmailResponse;
import com.eventsitemanager.service.dto.PromotionTestEmailJobRequest;
import com.eventsitemanager.service.dto.PromotionTestEmailJobResponse;
import com.eventsitemanager.service.dto.RecipientType;
import com.eventsitemanager.service.dto.UserProfileDTO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for sending promotion emails.
 */
@Service
@Transactional
public class PromotionEmailServiceImpl implements PromotionEmailService {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailServiceImpl.class);

    private final PromotionEmailTemplateRepository promotionEmailTemplateRepository;

    private final PromotionEmailSentLogRepository promotionEmailSentLogRepository;

    private final EmailSenderService emailSenderService;

    private final UserProfileService userProfileService;

    private final BatchJobEmailService batchJobEmailService;

    private final PromotionTestEmailBatchJobService promotionTestEmailBatchJobService;

    private final EventAttendeeRepository eventAttendeeRepository;

    private final TenantSettingsRepository tenantSettingsRepository;

    private final S3Service s3Service;

    private final UserProfileRepository userProfileRepository;

    // Cache for tenant footer HTML (per tenant, expires after 1 hour)
    private final Cache<String, String> footerHtmlCache = CacheBuilder
        .newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    public PromotionEmailServiceImpl(
        PromotionEmailTemplateRepository promotionEmailTemplateRepository,
        PromotionEmailSentLogRepository promotionEmailSentLogRepository,
        EmailSenderService emailSenderService,
        UserProfileService userProfileService,
        BatchJobEmailService batchJobEmailService,
        PromotionTestEmailBatchJobService promotionTestEmailBatchJobService,
        EventAttendeeRepository eventAttendeeRepository,
        TenantSettingsRepository tenantSettingsRepository,
        S3Service s3Service,
        UserProfileRepository userProfileRepository
    ) {
        this.promotionEmailTemplateRepository = promotionEmailTemplateRepository;
        this.promotionEmailSentLogRepository = promotionEmailSentLogRepository;
        this.emailSenderService = emailSenderService;
        this.userProfileService = userProfileService;
        this.batchJobEmailService = batchJobEmailService;
        this.promotionTestEmailBatchJobService = promotionTestEmailBatchJobService;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.s3Service = s3Service;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Map<String, Object> sendTestEmail(Long templateId, String recipientEmail, String tenantId, Long userId) {
        log.debug("Request to send test email: templateId={}, recipientEmail={}, tenantId={}", templateId, recipientEmail, tenantId);

        // Use provided email - if not provided, throw error (don't lookup from userId)
        String finalRecipientEmail = recipientEmail;
        if (finalRecipientEmail == null || finalRecipientEmail.isEmpty()) {
            throw new IllegalArgumentException("Recipient email is required for test emails");
        }

        Map<String, Object> result = new HashMap<>();
        PromotionTestEmailJobRequest request = new PromotionTestEmailJobRequest(
            tenantId,
            templateId,
            finalRecipientEmail,
            System.currentTimeMillis(),
            userId
        );

        PromotionTestEmailJobResponse response = promotionTestEmailBatchJobService.triggerPromotionTestEmailJob(request);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            result.put("success", true);
            result.put("jobExecutionId", response.getJobExecutionId());
            result.put("message", response.getMessage());
        } else {
            result.put("success", false);
            result.put("error", response != null ? response.getMessage() : "Null response from batch job service");
        }

        return result;
    }

    @Override
    public Map<String, Object> sendBulkEmail(Long templateId, List<String> recipientEmails, String tenantId, Long userId) {
        log.debug("Request to send bulk email: templateId={}, recipientEmails={}, tenantId={}", templateId, recipientEmails, tenantId);

        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(templateId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Template not found: " + templateId));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        // Log if this is a newsletter email (eventId is null)
        if (template.getEventId() == null) {
            log.debug("Template has null eventId - this is a newsletter email. Using tenant settings fallback for header/footer.");
        }

        // Build email content using the already-fetched template to avoid redundant DB query
        // Use the passed tenantId parameter for fallback to tenant settings
        // Note: buildEmailContent does not depend on eventId - it only uses tenantId for fallback
        Map<String, String> emailContent = buildEmailContent(template, null, null, tenantId);
        String subject = emailContent.get("subject");
        String bodyHtml = emailContent.get("bodyHtml");
        String fromEmail = template.getFromEmail();

        // Determine recipient type based on eventId
        // If eventId is null, it's a newsletter email sent to all subscribed members
        String recipientType;
        List<String> finalRecipientEmails = recipientEmails;

        if (template.getEventId() == null) {
            // Newsletter email - send to all subscribed members
            log.info("Template has null eventId - treating as newsletter email for subscribed members");
            recipientType = RecipientType.SUBSCRIBED_MEMBERS;
            // Don't fetch event attendees if eventId is null
            if (finalRecipientEmails == null || finalRecipientEmails.isEmpty()) {
                log.debug("No explicit recipient emails provided, batch service will fetch subscribed members");
            }
        } else {
            // Event-specific email - send to event attendees
            recipientType = RecipientType.EVENT_ATTENDEES;
            if (finalRecipientEmails == null || finalRecipientEmails.isEmpty()) {
                // Get event registrants from EventAttendee
                Long eventId = template.getEventId();
                List<EventAttendee> attendees = eventAttendeeRepository
                    .findAll()
                    .stream()
                    .filter(attendee -> eventId.equals(attendee.getEventId()))
                    .filter(attendee -> attendee.getEmail() != null && !attendee.getEmail().isEmpty())
                    .filter(attendee -> "CONFIRMED".equalsIgnoreCase(attendee.getRegistrationStatus()))
                    .collect(Collectors.toList());

                finalRecipientEmails =
                    attendees
                        .stream()
                        .map(EventAttendee::getEmail)
                        .filter(email -> email != null && !email.isEmpty())
                        .distinct()
                        .collect(Collectors.toList());

                log.debug("Retrieved {} recipient emails from event registrants for eventId: {}", finalRecipientEmails.size(), eventId);
            }
        }

        // Refactored to use batch job service instead of looping through emails
        log.info(
            "Triggering batch email job for template {} and tenant {} with {} recipients (recipientType: {})",
            templateId,
            tenantId,
            finalRecipientEmails != null ? finalRecipientEmails.size() : "null (will fetch from batch service)",
            recipientType
        );

        // Trigger batch job service - it will handle email sending asynchronously
        // Pass recipientEmails if provided, otherwise batch service will fetch them based on recipientType
        BatchJobEmailResponse response = batchJobEmailService.triggerEmailBatch(
            tenantId,
            templateId,
            50,
            10000,
            finalRecipientEmails,
            userId,
            recipientType
        );

        Map<String, Object> result = new HashMap<>();
        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            log.info(
                "Batch email job triggered successfully. JobExecutionId: {}, ProcessedCount: {}",
                response.getJobExecutionId(),
                response.getProcessedCount()
            );
            result.put("success", true);
            result.put("sentCount", response.getSuccessCount() != null ? response.getSuccessCount().intValue() : 0);
            result.put("failedCount", response.getFailedCount() != null ? response.getFailedCount().intValue() : 0);
            result.put(
                "totalCount",
                response.getProcessedCount() != null ? response.getProcessedCount().intValue() : finalRecipientEmails.size()
            );
            result.put("jobExecutionId", response.getJobExecutionId());
        } else {
            log.error("Failed to trigger batch email job. Message: {}", response != null ? response.getMessage() : "null response");
            result.put("success", false);
            result.put("sentCount", 0);
            result.put("failedCount", finalRecipientEmails.size());
            result.put("totalCount", finalRecipientEmails.size());
            result.put("error", response != null ? response.getMessage() : "Unknown error");
        }

        return result;
    }

    @Override
    public Map<String, Object> sendBulkEmailToSubscribedMembers(Long templateId, String tenantId, Long userId) {
        log.debug("Request to send bulk email to subscribed members: templateId={}, tenantId={}", templateId, tenantId);

        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(templateId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Template not found: " + templateId));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        // Log if this is a newsletter email (eventId is null)
        if (template.getEventId() == null) {
            log.debug("Template has null eventId - this is a newsletter email. Using tenant settings fallback for header/footer.");
        }

        // Build email content using the already-fetched template to avoid redundant DB query
        // Use the passed tenantId parameter for fallback to tenant settings
        Map<String, String> emailContent = buildEmailContent(template, null, null, tenantId);
        String subject = emailContent.get("subject");
        String bodyHtml = emailContent.get("bodyHtml");
        String fromEmail = template.getFromEmail();

        // Get subscribed member emails from UserProfile
        List<String> subscribedEmails = userProfileRepository.findSubscribedEmailsByTenantId(tenantId);

        if (subscribedEmails == null || subscribedEmails.isEmpty()) {
            log.warn("No subscribed members found for tenant: {}", tenantId);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("sentCount", 0);
            result.put("failedCount", 0);
            result.put("totalCount", 0);
            result.put("message", "No subscribed members found for this tenant");
            return result;
        }

        log.debug("Retrieved {} subscribed member emails for tenant: {}", subscribedEmails.size(), tenantId);

        // Refactored to use batch job service instead of looping through emails
        log.info(
            "Triggering batch email job for template {} and tenant {} with {} subscribed members",
            templateId,
            tenantId,
            subscribedEmails.size()
        );

        // Trigger batch job service - it will handle email sending asynchronously
        // Pass null for recipientEmails so batch service will fetch subscribed members
        // Set recipientType to SUBSCRIBED_MEMBERS for subscribed members
        BatchJobEmailResponse response = batchJobEmailService.triggerEmailBatch(
            tenantId,
            templateId,
            null,
            userId,
            RecipientType.SUBSCRIBED_MEMBERS
        );

        Map<String, Object> result = new HashMap<>();
        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            log.info(
                "Batch email job triggered successfully for subscribed members. JobExecutionId: {}, ProcessedCount: {}",
                response.getJobExecutionId(),
                response.getProcessedCount()
            );
            result.put("success", true);
            result.put("sentCount", response.getSuccessCount() != null ? response.getSuccessCount().intValue() : 0);
            result.put("failedCount", response.getFailedCount() != null ? response.getFailedCount().intValue() : 0);
            result.put(
                "totalCount",
                response.getProcessedCount() != null ? response.getProcessedCount().intValue() : subscribedEmails.size()
            );
            result.put("jobExecutionId", response.getJobExecutionId());
        } else {
            log.error(
                "Failed to trigger batch email job for subscribed members. Message: {}",
                response != null ? response.getMessage() : "null response"
            );
            result.put("success", false);
            result.put("sentCount", 0);
            result.put("failedCount", subscribedEmails.size());
            result.put("totalCount", subscribedEmails.size());
            result.put("error", response != null ? response.getMessage() : "Unknown error");
        }

        return result;
    }

    /**
     * Build email content from template object (internal method to avoid redundant DB queries).
     * Uses template's tenantId for fallback to tenant settings.
     */
    private Map<String, String> buildEmailContent(PromotionEmailTemplate template, String subjectOverride, String bodyHtmlOverride) {
        return buildEmailContent(template, subjectOverride, bodyHtmlOverride, template.getTenantId());
    }

    /**
     * Build email content from template object with explicit tenantId for fallback.
     * This allows batch jobs to use the tenantId from the request rather than relying on template's tenantId.
     */
    private Map<String, String> buildEmailContent(
        PromotionEmailTemplate template,
        String subjectOverride,
        String bodyHtmlOverride,
        String tenantIdForFallback
    ) {
        log.debug(
            "Building email content for template: id={}, name={}, tenantIdForFallback={}",
            template.getId(),
            template.getTemplateName(),
            tenantIdForFallback
        );

        String subject = subjectOverride != null && !subjectOverride.isEmpty() ? subjectOverride : template.getSubject();
        String bodyHtml = bodyHtmlOverride != null && !bodyHtmlOverride.isEmpty() ? bodyHtmlOverride : template.getBodyHtml();

        // Build full HTML with header and footer images if available
        StringBuilder fullHtml = new StringBuilder();
        fullHtml.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");

        // Add header image - check template first, then fall back to tenant settings
        String headerImageUrl = template.getHeaderImageUrl();
        log.debug("Template header image URL: {}", headerImageUrl);
        if (headerImageUrl == null || headerImageUrl.isEmpty()) {
            // Fall back to tenant settings header image using the provided tenantIdForFallback
            log.debug("Template has no header image, checking tenant settings for tenant: {}", tenantIdForFallback);
            headerImageUrl = getTenantEmailHeaderImageUrl(tenantIdForFallback);
            log.debug("Tenant settings header image URL: {}", headerImageUrl);
        }
        if (headerImageUrl != null && !headerImageUrl.isEmpty()) {
            log.debug("Adding header image to email: {}", headerImageUrl);
            fullHtml.append("<div style='text-align: center; margin-bottom: 20px;'>");
            fullHtml.append("<img src='").append(headerImageUrl).append("' alt='Header' style='max-width: 100%; height: auto;' />");
            fullHtml.append("</div>");
        } else {
            log.debug("No header image URL found for template or tenant");
        }

        // Add body HTML
        fullHtml.append("<div>").append(bodyHtml).append("</div>");

        // Add footer HTML if available
        if (template.getFooterHtml() != null && !template.getFooterHtml().isEmpty()) {
            fullHtml.append("<div>").append(template.getFooterHtml()).append("</div>");
        }

        // Add footer image if available (for backward compatibility)
        if (template.getFooterImageUrl() != null && !template.getFooterImageUrl().isEmpty()) {
            fullHtml.append("<div style='text-align: center; margin-top: 20px;'>");
            fullHtml
                .append("<img src='")
                .append(template.getFooterImageUrl())
                .append("' alt='Footer' style='max-width: 100%; height: auto;' />");
            fullHtml.append("</div>");
        }

        // Add tenant email footer HTML from tenant settings using the provided tenantIdForFallback
        String tenantFooterHtml = getTenantEmailFooterHtml(tenantIdForFallback);
        if (tenantFooterHtml != null && !tenantFooterHtml.isEmpty()) {
            fullHtml.append("<div>").append(tenantFooterHtml).append("</div>");
        }

        fullHtml.append("</body></html>");

        Map<String, String> result = new HashMap<>();
        result.put("subject", subject);
        result.put("bodyHtml", fullHtml.toString());

        return result;
    }

    /**
     * Get tenant email header image URL from tenant settings.
     *
     * @param tenantId the tenant ID
     * @return the header image URL, or empty string if not available
     */
    private String getTenantEmailHeaderImageUrl(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            log.debug("Tenant ID is null or empty, skipping header image");
            return "";
        }

        try {
            return tenantSettingsRepository
                .findByTenantId(tenantId)
                .map(tenantSettings -> {
                    String emailHeaderImageUrl = tenantSettings.getEmailHeaderImageUrl();
                    log.debug("Found tenant settings for tenant {}: emailHeaderImageUrl={}", tenantId, emailHeaderImageUrl);
                    return emailHeaderImageUrl != null && !emailHeaderImageUrl.isEmpty() ? emailHeaderImageUrl : "";
                })
                .orElseGet(() -> {
                    log.debug("Tenant settings not found for tenant: {}", tenantId);
                    return "";
                });
        } catch (Exception e) {
            log.error("Error getting tenant email header image URL for tenant {}: {}", tenantId, e.getMessage(), e);
            return "";
        }
    }

    /**
     * Get tenant email footer HTML from tenant settings.
     * Downloads footer HTML from S3 and replaces {{LOGO_URL}} placeholder with tenant logo URL.
     * Uses caching to avoid repeated S3 downloads.
     *
     * @param tenantId the tenant ID
     * @return the footer HTML with logo URL replaced, or empty string if not available
     */
    private String getTenantEmailFooterHtml(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            log.debug("Tenant ID is null or empty, skipping tenant footer HTML");
            return "";
        }

        try {
            // Fetch tenant settings
            return tenantSettingsRepository
                .findByTenantId(tenantId)
                .map(tenantSettings -> {
                    String emailFooterHtmlUrl = tenantSettings.getEmailFooterHtmlUrl();
                    String logoImageUrl = tenantSettings.getLogoImageUrl();

                    // If no footer HTML URL is configured, return empty string
                    if (emailFooterHtmlUrl == null || emailFooterHtmlUrl.isEmpty()) {
                        log.debug("Email footer HTML URL not configured for tenant: {}", tenantId);
                        return "";
                    }

                    // Create cache key including tenantId and logoImageUrl to ensure correct caching
                    // when logo changes, we get fresh footer HTML
                    String cacheKey = "footer:" + tenantId + "|" + (logoImageUrl != null ? logoImageUrl : "");

                    // Check cache first
                    String cachedFooterHtml = footerHtmlCache.getIfPresent(cacheKey);
                    if (cachedFooterHtml != null) {
                        log.debug("Cache hit for footer HTML for tenant: {}", tenantId);
                        return cachedFooterHtml;
                    }

                    log.debug("Cache miss for footer HTML for tenant: {}, fetching from S3", tenantId);

                    // Download footer HTML from S3
                    String footerHtml = "";
                    try {
                        footerHtml = s3Service.downloadHtmlFromUrl(emailFooterHtmlUrl);
                        if (footerHtml == null || footerHtml.isEmpty()) {
                            log.warn("Downloaded footer HTML is empty for tenant: {}", tenantId);
                            return "";
                        }
                        log.debug("Downloaded footer HTML from S3 for tenant: {}", tenantId);
                    } catch (Exception e) {
                        log.warn("Failed to download footer HTML from S3 for tenant {}: {}", tenantId, e.getMessage());
                        return "";
                    }

                    // Replace {{LOGO_URL}} placeholder with tenant logo URL if available
                    if (logoImageUrl != null && !logoImageUrl.isEmpty()) {
                        footerHtml = footerHtml.replace("{{LOGO_URL}}", logoImageUrl);
                        log.debug("Replaced {{LOGO_URL}} placeholder with logo URL for tenant: {}", tenantId);
                    } else {
                        log.debug("Logo image URL not configured for tenant: {}, leaving {{LOGO_URL}} placeholder as is", tenantId);
                    }

                    // Cache the processed footer HTML
                    footerHtmlCache.put(cacheKey, footerHtml);
                    log.debug("Cached footer HTML for tenant: {}", tenantId);

                    return footerHtml;
                })
                .orElseGet(() -> {
                    log.debug("Tenant settings not found for tenant: {}", tenantId);
                    return "";
                });
        } catch (Exception e) {
            log.error("Error getting tenant email footer HTML for tenant {}: {}", tenantId, e.getMessage(), e);
            return "";
        }
    }

    @Override
    public Map<String, String> buildEmailContent(Long templateId, String subjectOverride, String bodyHtmlOverride, String tenantId) {
        log.debug("Building email content: templateId={}, tenantId={}", templateId, tenantId);

        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(templateId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Template not found: " + templateId));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        // Use the passed tenantId parameter for fallback to tenant settings
        // This ensures batch jobs use the correct tenantId from the request
        return buildEmailContent(template, subjectOverride, bodyHtmlOverride, tenantId);
    }

    /**
     * Log email sent to database.
     * This method is non-blocking - any exceptions during logging are caught and logged as warnings
     * to ensure that email sending success/failure is not affected by database logging issues.
     */
    private void logEmailSent(
        PromotionEmailTemplate template,
        String recipientEmail,
        String subject,
        String tenantId,
        Long userId,
        Boolean isTestEmail,
        EmailStatus emailStatus,
        String errorMessage
    ) {
        try {
            PromotionEmailSentLog sentLog = new PromotionEmailSentLog();
            sentLog.setTenantId(tenantId);
            sentLog.setTemplateId(template.getId());
            // eventId can be null for newsletter emails - handle gracefully
            sentLog.setEventId(template.getEventId());
            sentLog.setRecipientEmail(recipientEmail);
            sentLog.setSubject(subject);
            sentLog.setPromotionCode(template.getPromotionCode());
            sentLog.setDiscountCodeId(template.getDiscountCodeId());
            sentLog.setSentAt(ZonedDateTime.now());
            sentLog.setIsTestEmail(isTestEmail != null ? isTestEmail : false);
            sentLog.setEmailStatus(emailStatus);
            sentLog.setErrorMessage(errorMessage);
            sentLog.setSentById(userId);

            promotionEmailSentLogRepository.save(sentLog);
            log.debug("Successfully logged email sent to database: recipient={}, status={}", recipientEmail, emailStatus);
        } catch (Exception e) {
            // Use warn level instead of error - email was already sent/failed, this is just a logging issue
            log.warn(
                "Failed to log email sent to database (email was already {}): recipient={}, error={}",
                emailStatus == EmailStatus.SENT ? "sent successfully" : "failed to send",
                recipientEmail,
                e.getMessage(),
                e
            );
            // Don't throw - logging failure shouldn't break email sending or affect the response
        }
    }
}
