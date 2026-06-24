package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ManualPaymentConfirmationEmailJobRequest;
import com.eventsitemanager.service.dto.ManualPaymentConfirmationEmailJobResponse;
import com.eventsitemanager.service.dto.ManualPaymentTicketEmailJobRequest;
import com.eventsitemanager.service.dto.ManualPaymentTicketEmailJobResponse;

/**
 * Client service for submitting manual payment email jobs to the batch jobs microservice.
 * Mirrors the pattern used by {@link com.eventsitemanager.service.ContactFormBatchJobService}.
 */
public interface ManualPaymentBatchJobService {
    /**
     * Trigger a manual payment confirmation email batch job.
     * This email is sent immediately after a manual payment request is created.
     *
     * @param request the manual payment confirmation email job request
     * @return the manual payment confirmation email job response
     */
    ManualPaymentConfirmationEmailJobResponse triggerConfirmationEmailJob(ManualPaymentConfirmationEmailJobRequest request);

    /**
     * Trigger a manual payment ticket email batch job.
     * This email is sent when an admin marks the payment as RECEIVED.
     *
     * @param request the manual payment ticket email job request
     * @return the manual payment ticket email job response
     */
    ManualPaymentTicketEmailJobResponse triggerTicketEmailJob(ManualPaymentTicketEmailJobRequest request);
}
