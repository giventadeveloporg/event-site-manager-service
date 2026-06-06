package com.nextjstemplate.service.impl;

import com.nextjstemplate.service.ContactFormBatchJobService;
import com.nextjstemplate.service.ContactFormService;
import com.nextjstemplate.service.dto.ContactFormDTO;
import com.nextjstemplate.service.dto.ContactFormEmailJobRequest;
import com.nextjstemplate.service.dto.ContactFormEmailJobResponse;
import java.util.Map;
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

    private static final String DEFAULT_EMAIL_TYPE = "CONTACT";

    private final Logger log = LoggerFactory.getLogger(ContactFormServiceImpl.class);

    private final ContactFormBatchJobService contactFormBatchJobService;

    public ContactFormServiceImpl(ContactFormBatchJobService contactFormBatchJobService) {
        this.contactFormBatchJobService = contactFormBatchJobService;
    }

    @Override
    public Map<String, Object> sendContactFormEmail(ContactFormDTO contactFormDTO, String tenantId) {
        String emailType = normalizeEmailType(contactFormDTO.getEmailType());

        log.debug(
            "Request to send contact form email: senderEmail={}, emailType={}, tenantId={}",
            contactFormDTO.getSenderEmail(),
            emailType,
            tenantId
        );

        ContactFormEmailJobRequest request = new ContactFormEmailJobRequest();
        request.setTenantId(tenantId);
        request.setFirstName(contactFormDTO.getFirstName());
        request.setLastName(contactFormDTO.getLastName());
        request.setMessageBody(contactFormDTO.getMessageBody());
        request.setSenderEmail(contactFormDTO.getSenderEmail());
        request.setEmailType(emailType);
        request.setSubmittedAtEpochMillis(System.currentTimeMillis());

        ContactFormEmailJobResponse response = contactFormBatchJobService.triggerContactFormEmailJob(request);

        Map<String, Object> result = new java.util.HashMap<>();
        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            log.info(
                "Contact form email batch job triggered successfully for tenant {}. JobExecutionId: {}, ProcessedCount: {}",
                tenantId,
                response.getJobExecutionId(),
                response.getProcessedCount()
            );
            result.put("success", true);
            result.put("jobExecutionId", response.getJobExecutionId());
            result.put("processedCount", response.getProcessedCount());
            result.put("successCount", response.getSuccessCount());
            result.put("failedCount", response.getFailedCount());
            result.put("message", response.getMessage());
        } else {
            String errorMessage = response != null ? response.getMessage() : "Unknown error triggering contact form batch job";
            log.error("Failed to trigger contact form email batch job for tenant {}: {}", tenantId, errorMessage);
            result.put("success", false);
            result.put("error", errorMessage);
        }

        return result;
    }

    private String normalizeEmailType(String emailType) {
        if (emailType == null || emailType.isBlank()) {
            return DEFAULT_EMAIL_TYPE;
        }
        return emailType.trim().toUpperCase();
    }
}
