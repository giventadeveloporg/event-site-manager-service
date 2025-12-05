package com.nextjstemplate.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.PromotionEmailSentLog;
import com.nextjstemplate.domain.PromotionEmailTemplate;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.domain.enumeration.EmailStatus;
import com.nextjstemplate.repository.EventAttendeeRepository;
import com.nextjstemplate.repository.PromotionEmailSentLogRepository;
import com.nextjstemplate.repository.PromotionEmailTemplateRepository;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.EmailSenderService;
import com.nextjstemplate.service.PromotionEmailService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.UserProfileService;
import com.nextjstemplate.service.dto.UserProfileDTO;
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
        EventAttendeeRepository eventAttendeeRepository,
        TenantSettingsRepository tenantSettingsRepository,
        S3Service s3Service,
        UserProfileRepository userProfileRepository
    ) {
        this.promotionEmailTemplateRepository = promotionEmailTemplateRepository;
        this.promotionEmailSentLogRepository = promotionEmailSentLogRepository;
        this.emailSenderService = emailSenderService;
        this.userProfileService = userProfileService;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.s3Service = s3Service;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Map<String, Object> sendTestEmail(Long templateId, String recipientEmail, String tenantId, Long userId) {
        log.debug("Request to send test email: templateId={}, recipientEmail={}, tenantId={}", templateId, recipientEmail, tenantId);

        // Fetch template fresh from database to avoid stale data issues
        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(templateId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Template not found: " + templateId));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        // Use provided email - if not provided, throw error (don't lookup from userId)
        String finalRecipientEmail = recipientEmail;
        if (finalRecipientEmail == null || finalRecipientEmail.isEmpty()) {
            throw new IllegalArgumentException("Recipient email is required for test emails");
        }

        // Build email content using the already-fetched template to avoid redundant DB query
        Map<String, String> emailContent = buildEmailContent(template, null, null);
        String subject = emailContent.get("subject");
        String bodyHtml = emailContent.get("bodyHtml");
        String fromEmail = template.getFromEmail();

        log.debug("Sending test email from {} to {} with subject: {}", fromEmail, finalRecipientEmail, subject);

        Map<String, Object> result = new HashMap<>();
        try {
            emailSenderService.sendEmail(fromEmail, finalRecipientEmail, subject, bodyHtml, true, new HashMap<>());
            result.put("success", true);
            result.put("messageId", "test-" + System.currentTimeMillis());
            log.info("Test email sent successfully to {} for template {}", finalRecipientEmail, templateId);

            // Log email sent
            logEmailSent(template, finalRecipientEmail, subject, tenantId, userId, true, EmailStatus.SENT, null);
        } catch (Exception e) {
            log.error("Failed to send test email to {} for template {}: {}", finalRecipientEmail, templateId, e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());

            // Log email failure
            logEmailSent(template, finalRecipientEmail, subject, tenantId, userId, true, EmailStatus.FAILED, e.getMessage());
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

        // Build email content using the already-fetched template to avoid redundant DB query
        Map<String, String> emailContent = buildEmailContent(template, null, null);
        String subject = emailContent.get("subject");
        String bodyHtml = emailContent.get("bodyHtml");
        String fromEmail = template.getFromEmail();

        // Get recipient emails if not provided
        List<String> finalRecipientEmails = recipientEmails;
        if (finalRecipientEmails == null || finalRecipientEmails.isEmpty()) {
            // Get event registrants from EventAttendee
            List<EventAttendee> attendees = eventAttendeeRepository
                .findAll()
                .stream()
                .filter(attendee -> attendee.getEventId().equals(template.getEventId()))
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

            log.debug("Retrieved {} recipient emails from event registrants", finalRecipientEmails.size());
        }

        int sentCount = 0;
        int failedCount = 0;

        // Send emails in batches
        int batchSize = 50; // SES recommended batch size
        for (int i = 0; i < finalRecipientEmails.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, finalRecipientEmails.size());
            List<String> batch = finalRecipientEmails.subList(i, endIndex);

            try {
                emailSenderService.sendBatchEmails(fromEmail, batch, subject, bodyHtml, true, new HashMap<>());
                sentCount += batch.size();

                // Log successful emails
                for (String email : batch) {
                    logEmailSent(template, email, subject, tenantId, userId, false, EmailStatus.SENT, null);
                }
            } catch (Exception e) {
                log.error("Failed to send email batch: {}", e.getMessage(), e);
                failedCount += batch.size();

                // Log failed emails
                for (String email : batch) {
                    logEmailSent(template, email, subject, tenantId, userId, false, EmailStatus.FAILED, e.getMessage());
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("sentCount", sentCount);
        result.put("failedCount", failedCount);
        result.put("totalCount", finalRecipientEmails.size());

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

        // Build email content using the already-fetched template to avoid redundant DB query
        Map<String, String> emailContent = buildEmailContent(template, null, null);
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

        int sentCount = 0;
        int failedCount = 0;

        // Send emails in batches
        int batchSize = 50; // SES recommended batch size
        for (int i = 0; i < subscribedEmails.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, subscribedEmails.size());
            List<String> batch = subscribedEmails.subList(i, endIndex);

            try {
                emailSenderService.sendBatchEmails(fromEmail, batch, subject, bodyHtml, true, new HashMap<>());
                sentCount += batch.size();

                // Log successful emails
                for (String email : batch) {
                    logEmailSent(template, email, subject, tenantId, userId, false, EmailStatus.SENT, null);
                }
            } catch (Exception e) {
                log.error("Failed to send email batch to subscribed members: {}", e.getMessage(), e);
                failedCount += batch.size();

                // Log failed emails
                for (String email : batch) {
                    logEmailSent(template, email, subject, tenantId, userId, false, EmailStatus.FAILED, e.getMessage());
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("sentCount", sentCount);
        result.put("failedCount", failedCount);
        result.put("totalCount", subscribedEmails.size());

        return result;
    }

    /**
     * Build email content from template object (internal method to avoid redundant DB queries).
     */
    private Map<String, String> buildEmailContent(PromotionEmailTemplate template, String subjectOverride, String bodyHtmlOverride) {
        log.debug("Building email content for template: id={}, name={}", template.getId(), template.getTemplateName());

        String subject = subjectOverride != null && !subjectOverride.isEmpty() ? subjectOverride : template.getSubject();
        String bodyHtml = bodyHtmlOverride != null && !bodyHtmlOverride.isEmpty() ? bodyHtmlOverride : template.getBodyHtml();

        // Build full HTML with header and footer images if available
        StringBuilder fullHtml = new StringBuilder();
        fullHtml.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>");

        // Add header image if available
        if (template.getHeaderImageUrl() != null && !template.getHeaderImageUrl().isEmpty()) {
            fullHtml.append("<div style='text-align: center; margin-bottom: 20px;'>");
            fullHtml
                .append("<img src='")
                .append(template.getHeaderImageUrl())
                .append("' alt='Header' style='max-width: 100%; height: auto;' />");
            fullHtml.append("</div>");
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

        // Add tenant email footer HTML from tenant settings
        String tenantFooterHtml = getTenantEmailFooterHtml(template.getTenantId());
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
        log.debug("Building email content: templateId={}", templateId);

        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(templateId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Template not found: " + templateId));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        return buildEmailContent(template, subjectOverride, bodyHtmlOverride);
    }

    /**
     * Log email sent to database.
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
            PromotionEmailSentLog log = new PromotionEmailSentLog();
            log.setTenantId(tenantId);
            log.setTemplateId(template.getId());
            log.setEventId(template.getEventId());
            log.setRecipientEmail(recipientEmail);
            log.setSubject(subject);
            log.setPromotionCode(template.getPromotionCode());
            log.setDiscountCodeId(template.getDiscountCodeId());
            log.setSentAt(ZonedDateTime.now());
            log.setIsTestEmail(isTestEmail != null ? isTestEmail : false);
            log.setEmailStatus(emailStatus);
            log.setErrorMessage(errorMessage);
            log.setSentById(userId);

            promotionEmailSentLogRepository.save(log);
        } catch (Exception e) {
            log.error("Failed to log email sent: {}", e.getMessage(), e);
            // Don't throw - logging failure shouldn't break email sending
        }
    }
}
