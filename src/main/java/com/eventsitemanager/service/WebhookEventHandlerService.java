package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.ClerkWebhookRequest;

/**
 * Service Interface for handling Clerk webhook events.
 */
public interface WebhookEventHandlerService {
    /**
     * Handle a webhook event and route to appropriate handler.
     *
     * @param webhookRequest the webhook event
     */
    void handleEvent(ClerkWebhookRequest webhookRequest);

    /**
     * Check if an event has already been processed (idempotency).
     *
     * @param eventId the webhook event ID
     * @return true if event was already processed
     */
    boolean isEventProcessed(String eventId);

    /**
     * Handle user.created event.
     *
     * @param webhookRequest the webhook event
     */
    void handleUserCreated(ClerkWebhookRequest webhookRequest);

    /**
     * Handle user.updated event.
     *
     * @param webhookRequest the webhook event
     */
    void handleUserUpdated(ClerkWebhookRequest webhookRequest);

    /**
     * Handle user.deleted event.
     *
     * @param webhookRequest the webhook event
     */
    void handleUserDeleted(ClerkWebhookRequest webhookRequest);

    /**
     * Handle session.created event.
     *
     * @param webhookRequest the webhook event
     */
    void handleSessionCreated(ClerkWebhookRequest webhookRequest);

    /**
     * Handle session.ended event.
     *
     * @param webhookRequest the webhook event
     */
    void handleSessionEnded(ClerkWebhookRequest webhookRequest);

    /**
     * Handle session.revoked event.
     *
     * @param webhookRequest the webhook event
     */
    void handleSessionRevoked(ClerkWebhookRequest webhookRequest);

    /**
     * Handle organizationMembership.created event.
     *
     * @param webhookRequest the webhook event
     */
    void handleOrganizationMembershipCreated(ClerkWebhookRequest webhookRequest);

    /**
     * Handle organizationMembership.updated event.
     *
     * @param webhookRequest the webhook event
     */
    void handleOrganizationMembershipUpdated(ClerkWebhookRequest webhookRequest);

    /**
     * Handle organizationMembership.deleted event.
     *
     * @param webhookRequest the webhook event
     */
    void handleOrganizationMembershipDeleted(ClerkWebhookRequest webhookRequest);
}
