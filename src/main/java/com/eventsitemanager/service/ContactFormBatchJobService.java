package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ContactFormEmailJobRequest;
import com.eventsitemanager.service.dto.ContactFormEmailJobResponse;

/**
 * Client service for submitting contact form email jobs to the batch jobs microservice.
 * Mirrors the pattern used by {@link com.eventsitemanager.service.BatchJobEmailService}.
 */
public interface ContactFormBatchJobService {
    /**
     * Trigger a contact form email batch job.
     *
     * @param request the contact form email job request
     * @return the contact form email job response
     */
    ContactFormEmailJobResponse triggerContactFormEmailJob(ContactFormEmailJobRequest request);
}
