package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.SendPromotionEmailDTO;
import java.util.Map;

/**
 * Service Interface for sending promotion emails.
 */
public interface PromotionEmailService {
    /**
     * Send a test email to a single recipient.
     *
     * @param templateId the template ID
     * @param recipientEmail the recipient email (optional, uses admin email if not provided)
     * @param tenantId the tenant ID
     * @param userId the user ID sending the email
     * @return Map containing success status and message ID
     */
    Map<String, Object> sendTestEmail(Long templateId, String recipientEmail, String tenantId, Long userId);

    /**
     * Send bulk emails to event registrants or specified recipients.
     *
     * @param templateId the template ID
     * @param recipientEmails optional list of recipient emails (if not provided, retrieves from event registrants)
     * @param tenantId the tenant ID
     * @param userId the user ID sending the emails
     * @return Map containing success status, sent count, and failed count
     */
    Map<String, Object> sendBulkEmail(Long templateId, java.util.List<String> recipientEmails, String tenantId, Long userId);

    /**
     * Send bulk emails to all subscribed members of the tenant (isEmailSubscribed=true).
     *
     * @param templateId the template ID
     * @param tenantId the tenant ID
     * @param userId the user ID sending the emails
     * @return Map containing success status, sent count, and failed count
     */
    Map<String, Object> sendBulkEmailToSubscribedMembers(Long templateId, String tenantId, Long userId);

    /**
     * Build final email content from template with optional overrides.
     *
     * @param templateId the template ID
     * @param subjectOverride optional subject override
     * @param bodyHtmlOverride optional body HTML override
     * @param tenantId the tenant ID
     * @return Map containing subject and bodyHtml
     */
    Map<String, String> buildEmailContent(Long templateId, String subjectOverride, String bodyHtmlOverride, String tenantId);
}
