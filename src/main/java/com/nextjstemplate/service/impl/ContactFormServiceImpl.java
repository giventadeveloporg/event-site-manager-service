package com.nextjstemplate.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nextjstemplate.domain.TenantEmailAddress;
import com.nextjstemplate.domain.enumeration.TenantEmailType;
import com.nextjstemplate.repository.TenantEmailAddressRepository;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.ContactFormService;
import com.nextjstemplate.service.EmailSenderService;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.dto.ContactFormDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for sending contact form emails.
 */
@Service
@Transactional
public class ContactFormServiceImpl implements ContactFormService {

    private final Logger log = LoggerFactory.getLogger(ContactFormServiceImpl.class);

    private final EmailSenderService emailSenderService;

    private final TenantSettingsRepository tenantSettingsRepository;

    private final S3Service s3Service;

    private final TenantEmailAddressRepository tenantEmailAddressRepository;

    // Cache for tenant FROM email address for contact form (per tenant, expires after 1 hour)
    private final Cache<String, String> contactFromEmailCache = CacheBuilder
        .newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    // Cache for tenant footer HTML (per tenant, expires after 1 hour)
    private final Cache<String, String> footerHtmlCache = CacheBuilder
        .newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .build();

    public ContactFormServiceImpl(
        EmailSenderService emailSenderService,
        TenantSettingsRepository tenantSettingsRepository,
        S3Service s3Service,
        TenantEmailAddressRepository tenantEmailAddressRepository
    ) {
        this.emailSenderService = emailSenderService;
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.s3Service = s3Service;
        this.tenantEmailAddressRepository = tenantEmailAddressRepository;
    }

    @Override
    public Map<String, Object> sendContactFormEmail(ContactFormDTO contactFormDTO, String tenantId) {
        log.debug(
            "Request to send contact form email: from={}, to={}, tenantId={}",
            contactFormDTO.getFromEmail(),
            contactFormDTO.getToEmail(),
            tenantId
        );

        // Build email subject
        String subject = String.format("Contact Form Submission from %s %s", contactFormDTO.getFirstName(), contactFormDTO.getLastName());

        // Build email body HTML with tenant branding
        String bodyHtml = buildEmailBody(contactFormDTO, tenantId);

        // Resolve FROM email from tenant email addresses (type CONTACT if available)
        String fromEmail = resolveContactFromEmail(tenantId);
        String replyToEmail = contactFormDTO.getFromEmail();
        if (fromEmail == null || fromEmail.isEmpty()) {
            // Fallback: use the original behavior (user's email as from)
            log.warn(
                "No tenant CONTACT email address found for tenant {}. Falling back to using contact form sender email as FROM address.",
                tenantId
            );
            fromEmail = replyToEmail;
        }
        String toEmail = contactFormDTO.getToEmail();

        log.debug("Sending contact form email from {} to {} with subject: {} (replyTo={})", fromEmail, toEmail, subject, replyToEmail);

        Map<String, Object> result = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        if (replyToEmail != null && !replyToEmail.isEmpty()) {
            headers.put("Reply-To", replyToEmail);
        }
        try {
            emailSenderService.sendEmail(fromEmail, toEmail, subject, bodyHtml, true, headers);
            result.put("success", true);
            result.put("messageId", "contact-form-" + System.currentTimeMillis());
            log.info("Contact form email sent successfully from {} to {}", fromEmail, toEmail);
        } catch (Exception e) {
            log.error("Failed to send contact form email from {} to {}: {}", fromEmail, toEmail, e.getMessage(), e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * Resolve the FROM email address for contact form emails for a tenant.
     * Priority:
     * 1) TenantEmailAddress with emailType = CONTACT (prefer default & active)
     * 2) TenantEmailAddress default (isDefault = true and active)
     * 3) Any active TenantEmailAddress for the tenant
     * Results are cached per tenant.
     *
     * @param tenantId the tenant ID
     * @return the resolved FROM email address, or empty string if none found
     */
    private String resolveContactFromEmail(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            log.warn("Tenant ID is null or empty when resolving contact FROM email");
            return "";
        }

        String cacheKey = "contact-from:" + tenantId;
        String cached = contactFromEmailCache.getIfPresent(cacheKey);
        if (cached != null && !cached.isEmpty()) {
            log.debug("Using cached contact FROM email for tenant {}: {}", tenantId, cached);
            return cached;
        }

        try {
            // 1) CONTACT type addresses
            java.util.List<TenantEmailAddress> contactEmails = tenantEmailAddressRepository.findByTenantIdAndEmailType(
                tenantId,
                TenantEmailType.CONTACT
            );

            String fromEmail = selectBestFromTenantEmails(contactEmails);

            // 2) Default address (any type) if CONTACT not found
            if (fromEmail == null || fromEmail.isEmpty()) {
                tenantEmailAddressRepository
                    .findByTenantIdAndIsDefaultTrue(tenantId)
                    .filter(TenantEmailAddress::getIsActive)
                    .ifPresent(addr -> {
                        log.debug(
                            "Using default tenant email address as FROM for tenant {}: {} (type={})",
                            tenantId,
                            addr.getEmailAddress(),
                            addr.getEmailType()
                        );
                    });

                fromEmail =
                    tenantEmailAddressRepository
                        .findByTenantIdAndIsDefaultTrue(tenantId)
                        .filter(TenantEmailAddress::getIsActive)
                        .map(TenantEmailAddress::getEmailAddress)
                        .orElse(fromEmail);
            }

            // 3) Any active email for tenant
            if (fromEmail == null || fromEmail.isEmpty()) {
                java.util.List<TenantEmailAddress> activeEmails = tenantEmailAddressRepository.findByTenantIdAndIsActive(tenantId, true);
                fromEmail = selectBestFromTenantEmails(activeEmails);
            }

            if (fromEmail != null && !fromEmail.isEmpty()) {
                contactFromEmailCache.put(cacheKey, fromEmail);
                log.debug("Resolved contact FROM email for tenant {}: {}", tenantId, fromEmail);
                return fromEmail;
            }

            log.warn("No tenant email address found for tenant {} when resolving contact FROM email", tenantId);
            return "";
        } catch (Exception e) {
            log.error("Error resolving contact FROM email for tenant {}: {}", tenantId, e.getMessage(), e);
            return "";
        }
    }

    /**
     * Select the best email from a list of TenantEmailAddress records.
     * Preference: active + default, then active, then any.
     */
    private String selectBestFromTenantEmails(java.util.List<TenantEmailAddress> emails) {
        if (emails == null || emails.isEmpty()) {
            return "";
        }

        // Prefer active + default
        return emails
            .stream()
            .filter(addr -> Boolean.TRUE.equals(addr.getIsActive()) && Boolean.TRUE.equals(addr.getIsDefault()))
            .map(TenantEmailAddress::getEmailAddress)
            .findFirst()
            // Then any active
            .or(() ->
                emails.stream().filter(addr -> Boolean.TRUE.equals(addr.getIsActive())).map(TenantEmailAddress::getEmailAddress).findFirst()
            )
            // Then any
            .or(() -> emails.stream().map(TenantEmailAddress::getEmailAddress).findFirst())
            .orElse("");
    }

    /**
     * Build HTML email body from contact form data with tenant branding.
     *
     * @param contactFormDTO the contact form data
     * @param tenantId the tenant ID
     * @return HTML formatted email body
     */
    private String buildEmailBody(ContactFormDTO contactFormDTO, String tenantId) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background-color: #f4f4f4; padding: 20px; border-radius: 5px; margin-bottom: 20px; }");
        html.append(".content { padding: 20px; background-color: #ffffff; border: 1px solid #ddd; border-radius: 5px; }");
        html.append(".field { margin-bottom: 15px; }");
        html.append(".label { font-weight: bold; color: #555; }");
        html.append(".value { margin-top: 5px; padding: 10px; background-color: #f9f9f9; border-radius: 3px; }");
        html.append(".message { white-space: pre-wrap; }");
        html.append("</style></head><body>");

        // Add tenant header image if available
        String headerImageUrl = getTenantEmailHeaderImageUrl(tenantId);
        log.debug("Retrieved header image URL for tenant {}: {}", tenantId, headerImageUrl);
        if (headerImageUrl != null && !headerImageUrl.isEmpty()) {
            log.debug("Adding header image to email: {}", headerImageUrl);
            html.append("<div style='text-align: center; margin-bottom: 20px;'>");
            html.append("<img src='").append(headerImageUrl).append("' alt='Header' style='max-width: 100%; height: auto;' />");
            html.append("</div>");
        } else {
            log.debug("No header image URL found for tenant: {}", tenantId);
        }

        html.append("<div class='container'>");
        html.append("<div class='header'><h2>New Contact Form Submission</h2></div>");
        html.append("<div class='content'>");

        html.append("<div class='field'>");
        html.append("<div class='label'>Name:</div>");
        html
            .append("<div class='value'>")
            .append(escapeHtml(contactFormDTO.getFirstName()))
            .append(" ")
            .append(escapeHtml(contactFormDTO.getLastName()))
            .append("</div>");
        html.append("</div>");

        html.append("<div class='field'>");
        html.append("<div class='label'>Email:</div>");
        html.append("<div class='value'>").append(escapeHtml(contactFormDTO.getFromEmail())).append("</div>");
        html.append("</div>");

        html.append("<div class='field'>");
        html.append("<div class='label'>Message:</div>");
        html.append("<div class='value message'>").append(escapeHtml(contactFormDTO.getMessageBody())).append("</div>");
        html.append("</div>");

        html.append("</div></div>");

        // Add tenant email footer HTML if available
        String tenantFooterHtml = getTenantEmailFooterHtml(tenantId);
        if (tenantFooterHtml != null && !tenantFooterHtml.isEmpty()) {
            html.append("<div>").append(tenantFooterHtml).append("</div>");
        }

        html.append("</body></html>");

        return html.toString();
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

    /**
     * Escape HTML special characters to prevent XSS attacks.
     *
     * @param text the text to escape
     * @return escaped HTML text
     */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
}
