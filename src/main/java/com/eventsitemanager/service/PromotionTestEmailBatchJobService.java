package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.PromotionTestEmailJobRequest;
import com.eventsitemanager.service.dto.PromotionTestEmailJobResponse;

/**
 * Service interface for triggering promotion test email jobs via the batch jobs microservice.
 *
 * Mirrors {@link ContactFormBatchJobService} pattern.
 */
public interface PromotionTestEmailBatchJobService {
    PromotionTestEmailJobResponse triggerPromotionTestEmailJob(PromotionTestEmailJobRequest request);
}
