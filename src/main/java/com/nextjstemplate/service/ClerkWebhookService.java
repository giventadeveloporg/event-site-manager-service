package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.WebhookEventRequest;
import com.nextjstemplate.service.dto.WebhookEventResponse;

/**
 * Service Interface for processing Clerk webhook events.
 */
public interface ClerkWebhookService {
    /**
     * Process a webhook event from Clerk.
     *
     * @param payload   the raw webhook payload
     * @param signature the webhook signature for verification
     * @return the processing response
     */
    WebhookEventResponse processWebhookEvent(String payload, String signature);

    /**
     * Check if an event has already been processed (idempotency).
     *
     * @param eventId the event ID
     * @return true if already processed
     */
    boolean isEventProcessed(String eventId);

    /**
     * Parse webhook payload to event request.
     *
     * @param payload the raw JSON payload
     * @return the parsed event request
     */
    WebhookEventRequest parsePayload(String payload);
}
