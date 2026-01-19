package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.ManualPaymentConfirmationEmailJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentConfirmationEmailJobResponse;
import com.nextjstemplate.service.dto.ManualPaymentTicketEmailJobRequest;
import com.nextjstemplate.service.dto.ManualPaymentTicketEmailJobResponse;

/**
 * Client service for submitting manual payment email jobs to the batch jobs microservice.
 * Mirrors the pattern used by {@link com.nextjstemplate.service.ContactFormBatchJobService}.
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
