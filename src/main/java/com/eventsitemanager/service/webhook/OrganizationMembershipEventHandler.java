package com.eventsitemanager.service.webhook;

import com.eventsitemanager.service.OrganizationSyncService;
import com.eventsitemanager.service.dto.WebhookEventRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler for organizationMembership webhook events.
 */
@Component
public class OrganizationMembershipEventHandler implements WebhookEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrganizationMembershipEventHandler.class);

    private final OrganizationSyncService organizationSyncService;

    public OrganizationMembershipEventHandler(OrganizationSyncService organizationSyncService) {
        this.organizationSyncService = organizationSyncService;
    }

    @Override
    public void handle(WebhookEventRequest event) {
        log.info("Processing organizationMembership event: {} (type: {})", event.getId(), event.getType());

        try {
            Map<String, Object> membershipData = event.getData();
            String userId = (String) membershipData.get("user_id");

            @SuppressWarnings("unchecked")
            Map<String, Object> organization = (Map<String, Object>) membershipData.get("organization");
            String organizationId = organization != null ? (String) organization.get("id") : null;
            String role = (String) membershipData.get("role");

            String eventType = event.getType();

            switch (eventType) {
                case "organizationMembership.created" -> handleMembershipCreated(userId, organizationId, role);
                case "organizationMembership.updated" -> handleMembershipUpdated(userId, organizationId, role);
                case "organizationMembership.deleted" -> handleMembershipDeleted(userId, organizationId);
                default -> log.warn("Unknown organizationMembership event type: {}", eventType);
            }

            log.info("Successfully processed organizationMembership event: {}", event.getId());
        } catch (Exception e) {
            log.error("Error handling organizationMembership event", e);
            throw new RuntimeException("Failed to handle organizationMembership event", e);
        }
    }

    @Override
    public String getEventType() {
        return "organizationMembership.*";
    }

    /**
     * Handle organizationMembership.created event.
     */
    private void handleMembershipCreated(String userId, String organizationId, String role) {
        log.info("Organization membership created: user={}, org={}, role={}", userId, organizationId, role);

        try {
            // Sync organization memberships for user
            organizationSyncService.syncUserOrganizations(userId);
            log.info("Synced organization memberships for user: {}", userId);
        } catch (Exception e) {
            log.error("Error syncing organization membership", e);
            throw e;
        }
    }

    /**
     * Handle organizationMembership.updated event.
     */
    private void handleMembershipUpdated(String userId, String organizationId, String role) {
        log.info("Organization membership updated: user={}, org={}, role={}", userId, organizationId, role);

        try {
            // Update user role based on new organization role
            if (organizationId != null) {
                organizationSyncService.updateUserRoleFromClerkRole(userId, role, organizationId);
                log.info("Updated user role for user: {}", userId);
            }
        } catch (Exception e) {
            log.error("Error updating organization membership", e);
            throw e;
        }
    }

    /**
     * Handle organizationMembership.deleted event.
     */
    private void handleMembershipDeleted(String userId, String organizationId) {
        log.info("Organization membership deleted: user={}, org={}", userId, organizationId);

        try {
            // Resync user organizations to reflect removal
            organizationSyncService.syncUserOrganizations(userId);
            log.info("Resynced organization memberships after deletion for user: {}", userId);
        } catch (Exception e) {
            log.error("Error handling organization membership deletion", e);
            throw e;
        }
    }
}
