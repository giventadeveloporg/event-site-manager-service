package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.ContactFormEmailJobRequest;
import com.nextjstemplate.service.dto.ContactFormEmailJobResponse;

/**
 * Client service for submitting contact form email jobs to the batch jobs microservice.
 * Mirrors the pattern used by {@link com.nextjstemplate.service.BatchJobEmailService}.
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
