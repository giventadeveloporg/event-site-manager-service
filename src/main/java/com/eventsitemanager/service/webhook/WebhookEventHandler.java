package com.eventsitemanager.service.webhook;

import com.eventsitemanager.service.dto.WebhookEventRequest;

/**
 * Interface for handling specific Clerk webhook events.
 */
public interface WebhookEventHandler {
    /**
     * Handle a webhook event.
     *
     * @param event the webhook event
     */
    void handle(WebhookEventRequest event);

    /**
     * Get the event type this handler supports.
     *
     * @return the event type (e.g., "user.created")
     */
    String getEventType();

    /**
     * Check if retry is supported for this handler.
     *
     * @return true if retry is supported
     */
    default boolean supportsRetry() {
        return true;
    }

    /**
     * Get maximum retry attempts.
     *
     * @return max retry attempts
     */
    default int getMaxRetries() {
        return 3;
    }
}
