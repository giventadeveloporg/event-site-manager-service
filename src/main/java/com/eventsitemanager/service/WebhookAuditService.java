package com.eventsitemanager.service;

import com.eventsitemanager.domain.ClerkWebhookEvent;
import java.util.List;

/**
 * Service Interface for webhook audit logging and management.
 */
public interface WebhookAuditService {
    /**
     * Save a webhook event to the audit log.
     *
     * @param eventId     the webhook event ID
     * @param eventType   the event type
     * @param clerkUserId the Clerk user ID (optional)
     * @param payload     the event payload
     * @return the saved audit event
     */
    ClerkWebhookEvent logEvent(String eventId, String eventType, String clerkUserId, String payload);

    /**
     * Mark an event as processed.
     *
     * @param eventId the webhook event ID
     * @return the updated event
     */
    ClerkWebhookEvent markEventAsProcessed(String eventId);

    /**
     * Record an error for an event.
     *
     * @param eventId      the webhook event ID
     * @param errorMessage the error message
     * @return the updated event
     */
    ClerkWebhookEvent recordEventError(String eventId, String errorMessage);

    /**
     * Increment retry count for an event.
     *
     * @param eventId the webhook event ID
     * @return the updated event
     */
    ClerkWebhookEvent incrementRetryCount(String eventId);

    /**
     * Get all unprocessed events.
     *
     * @return list of unprocessed events
     */
    List<ClerkWebhookEvent> getUnprocessedEvents();

    /**
     * Get failed events eligible for retry.
     *
     * @return list of events to retry
     */
    List<ClerkWebhookEvent> getFailedEventsForRetry();

    /**
     * Clean up old processed events (older than specified days).
     *
     * @param daysToKeep number of days to keep processed events
     * @return number of events deleted
     */
    int cleanupOldEvents(int daysToKeep);

    /**
     * Get webhook events for a specific user.
     *
     * @param clerkUserId the Clerk user ID
     * @return list of webhook events for the user
     */
    List<ClerkWebhookEvent> getEventsForUser(String clerkUserId);

    /**
     * Get processing statistics.
     *
     * @return map with statistics (total, processed, failed, pending)
     */
    java.util.Map<String, Long> getProcessingStatistics();
}
