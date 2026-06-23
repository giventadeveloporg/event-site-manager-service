package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.BatchJobEmailResponse;
import java.util.List;

/**
 * Service interface for triggering batch email jobs via the batch jobs microservice.
 */
public interface BatchJobEmailService {
    /**
     * Trigger email batch job with all parameters.
     *
     * @param tenantId        The tenant ID to process emails for (required)
     * @param templateId      The email template ID (required)
     * @param batchSize       Number of emails to process per batch (optional, default: 50)
     * @param maxEmails       Maximum number of emails to process (optional, default: 10000)
     * @param recipientEmails Optional list of specific recipient emails. If null, batch service will fetch recipients
     * @param userId          Optional user ID who triggered the job
     * @param recipientType   Recipient type: "EVENT_ATTENDEES" or "SUBSCRIBED_MEMBERS" (required)
     * @return BatchJobEmailResponse containing job execution details
     */
    BatchJobEmailResponse triggerEmailBatch(
        String tenantId,
        Long templateId,
        Integer batchSize,
        Integer maxEmails,
        List<String> recipientEmails,
        Long userId,
        String recipientType
    );

    /**
     * Trigger email batch job with template ID, tenant ID, and recipient type.
     *
     * @param tenantId      The tenant ID to process emails for
     * @param templateId    The email template ID
     * @param userId        Optional user ID who triggered the job
     * @param recipientType Recipient type: "EVENT_ATTENDEES" or "SUBSCRIBED_MEMBERS" (required)
     * @return BatchJobEmailResponse containing job execution details
     */
    BatchJobEmailResponse triggerEmailBatch(String tenantId, Long templateId, Long userId, String recipientType);

    /**
     * Trigger email batch job with template ID, tenant ID, recipient emails, and recipient type.
     *
     * @param tenantId        The tenant ID to process emails for
     * @param templateId      The email template ID
     * @param recipientEmails List of specific recipient emails
     * @param userId          Optional user ID who triggered the job
     * @param recipientType   Recipient type: "EVENT_ATTENDEES" or "SUBSCRIBED_MEMBERS" (required)
     * @return BatchJobEmailResponse containing job execution details
     */
    BatchJobEmailResponse triggerEmailBatch(
        String tenantId,
        Long templateId,
        List<String> recipientEmails,
        Long userId,
        String recipientType
    );
}
