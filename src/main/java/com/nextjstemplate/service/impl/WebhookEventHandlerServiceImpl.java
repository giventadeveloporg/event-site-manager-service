package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nextjstemplate.domain.ClerkWebhookEvent;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.ClerkWebhookEventRepository;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.OrganizationSyncService;
import com.nextjstemplate.service.WebhookEventHandlerService;
import com.nextjstemplate.service.dto.ClerkWebhookRequest;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of WebhookEventHandlerService for processing Clerk webhook
 * events.
 */
@Service
@Transactional
public class WebhookEventHandlerServiceImpl implements WebhookEventHandlerService {

    private static final Logger log = LoggerFactory.getLogger(WebhookEventHandlerServiceImpl.class);

    private final ClerkWebhookEventRepository webhookEventRepository;
    private final UserProfileRepository userProfileRepository;
    private final OrganizationSyncService organizationSyncService;

    public WebhookEventHandlerServiceImpl(
        ClerkWebhookEventRepository webhookEventRepository,
        UserProfileRepository userProfileRepository,
        OrganizationSyncService organizationSyncService
    ) {
        this.webhookEventRepository = webhookEventRepository;
        this.userProfileRepository = userProfileRepository;
        this.organizationSyncService = organizationSyncService;
    }

    @Override
    public void handleEvent(ClerkWebhookRequest webhookRequest) {
        log.info("Handling webhook event: {} (ID: {})", webhookRequest.getType(), webhookRequest.getId());

        // Save to audit log
        ClerkWebhookEvent auditEvent = createAuditEvent(webhookRequest);

        try {
            // Route to appropriate handler
            switch (webhookRequest.getType()) {
                case "user.created" -> handleUserCreated(webhookRequest);
                case "user.updated" -> handleUserUpdated(webhookRequest);
                case "user.deleted" -> handleUserDeleted(webhookRequest);
                case "session.created" -> handleSessionCreated(webhookRequest);
                case "session.ended" -> handleSessionEnded(webhookRequest);
                case "session.revoked" -> handleSessionRevoked(webhookRequest);
                case "organizationMembership.created" -> handleOrganizationMembershipCreated(webhookRequest);
                case "organizationMembership.updated" -> handleOrganizationMembershipUpdated(webhookRequest);
                case "organizationMembership.deleted" -> handleOrganizationMembershipDeleted(webhookRequest);
                default -> {
                    log.warn("Unknown webhook event type: {}", webhookRequest.getType());
                    auditEvent.setErrorMessage("Unknown event type");
                }
            }

            // Mark as processed
            auditEvent.setProcessed(true);
            auditEvent.setProcessedAt(ZonedDateTime.now());
            webhookEventRepository.save(auditEvent);

            log.info("Successfully processed webhook event: {}", webhookRequest.getId());
        } catch (Exception e) {
            log.error("Error processing webhook event: {}", webhookRequest.getId(), e);

            // Update audit event with error
            auditEvent.setProcessed(false);
            auditEvent.setErrorMessage(e.getMessage());
            auditEvent.setRetryCount((auditEvent.getRetryCount() != null ? auditEvent.getRetryCount() : 0) + 1);
            webhookEventRepository.save(auditEvent);

            throw new RuntimeException("Failed to process webhook event", e);
        }
    }

    @Override
    public boolean isEventProcessed(String eventId) {
        return webhookEventRepository.existsByEventId(eventId);
    }

    @Override
    public void handleUserCreated(ClerkWebhookRequest webhookRequest) {
        log.info("Handling user.created event");

        try {
            JsonNode data = webhookRequest.getData();
            String clerkUserId = data.get("id").asText();
            String email = extractEmail(data);

            log.info("Creating user profile for Clerk user: {}", clerkUserId);

            // Note: In webhook scenario, we don't know the tenant ID yet
            // This would typically be set when user first accesses a tenant
            log.info("User created in Clerk: {} ({})", email, clerkUserId);
        } catch (Exception e) {
            log.error("Error handling user.created event", e);
            throw new RuntimeException("Failed to handle user.created event", e);
        }
    }

    @Override
    public void handleUserUpdated(ClerkWebhookRequest webhookRequest) {
        log.info("Handling user.updated event");

        try {
            JsonNode data = webhookRequest.getData();
            String clerkUserId = data.get("id").asText();

            // Find all user profiles with this Clerk ID (across tenants)
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(clerkUserId);

            if (!userProfileOpt.isPresent()) {
                log.warn("User profile not found for Clerk user: {}", clerkUserId);
                return;
            }

            UserProfile userProfile = userProfileOpt.orElseThrow();

            // Update fields
            if (data.has("first_name")) {
                userProfile.setFirstName(data.get("first_name").asText());
            }
            if (data.has("last_name")) {
                userProfile.setLastName(data.get("last_name").asText());
            }
            if (data.has("profile_image_url")) {
                userProfile.setProfileImageUrl(data.get("profile_image_url").asText());
            }

            userProfile.setUpdatedAt(ZonedDateTime.now());
            userProfileRepository.save(userProfile);

            log.info("Updated user profile for Clerk user: {}", clerkUserId);
        } catch (Exception e) {
            log.error("Error handling user.updated event", e);
            throw new RuntimeException("Failed to handle user.updated event", e);
        }
    }

    @Override
    public void handleUserDeleted(ClerkWebhookRequest webhookRequest) {
        log.info("Handling user.deleted event");

        try {
            JsonNode data = webhookRequest.getData();
            String clerkUserId = data.get("id").asText();

            // Soft delete or mark as deleted
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(clerkUserId);

            if (!userProfileOpt.isPresent()) {
                log.warn("User profile not found for Clerk user: {}", clerkUserId);
                return;
            }

            UserProfile userProfile = userProfileOpt.orElseThrow();
            userProfile.setUserStatus("DELETED");
            userProfile.setUpdatedAt(ZonedDateTime.now());
            userProfileRepository.save(userProfile);

            log.info("Marked user as deleted: {}", clerkUserId);
        } catch (Exception e) {
            log.error("Error handling user.deleted event", e);
            throw new RuntimeException("Failed to handle user.deleted event", e);
        }
    }

    @Override
    public void handleSessionCreated(ClerkWebhookRequest webhookRequest) {
        log.info("Handling session.created event");

        try {
            JsonNode data = webhookRequest.getData();
            String sessionId = data.get("id").asText();
            String clerkUserId = data.get("user_id").asText();

            log.info("Session created for user: {} (Session: {})", clerkUserId, sessionId);

            // Update last sign-in time
            Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(clerkUserId);
            if (userProfileOpt.isPresent()) {
                UserProfile userProfile = userProfileOpt.orElseThrow();
                userProfile.setUpdatedAt(ZonedDateTime.now());
                userProfileRepository.save(userProfile);
            }
        } catch (Exception e) {
            log.error("Error handling session.created event", e);
            // Non-critical, log but don't throw
        }
    }

    @Override
    public void handleSessionEnded(ClerkWebhookRequest webhookRequest) {
        log.info("Handling session.ended event");

        try {
            JsonNode data = webhookRequest.getData();
            String sessionId = data.get("id").asText();
            String clerkUserId = data.get("user_id").asText();

            log.info("Session ended for user: {} (Session: {})", clerkUserId, sessionId);
            // Session tracking would go here if needed

        } catch (Exception e) {
            log.error("Error handling session.ended event", e);
            // Non-critical, log but don't throw
        }
    }

    @Override
    public void handleSessionRevoked(ClerkWebhookRequest webhookRequest) {
        log.info("Handling session.revoked event");

        try {
            JsonNode data = webhookRequest.getData();
            String sessionId = data.get("id").asText();
            String clerkUserId = data.get("user_id").asText();

            log.info("Session revoked for user: {} (Session: {})", clerkUserId, sessionId);
            // Session revocation tracking would go here if needed

        } catch (Exception e) {
            log.error("Error handling session.revoked event", e);
            // Non-critical, log but don't throw
        }
    }

    @Override
    public void handleOrganizationMembershipCreated(ClerkWebhookRequest webhookRequest) {
        log.info("Handling organizationMembership.created event");

        try {
            JsonNode data = webhookRequest.getData();
            JsonNode organization = data.get("organization");
            JsonNode publicUserData = data.get("public_user_data");

            String organizationId = organization.get("id").asText();
            String clerkUserId = publicUserData.get("user_id").asText();
            String role = data.get("role").asText();

            log.info("User {} added to organization {} with role {}", clerkUserId, organizationId, role);

            // Sync organization membership
            organizationSyncService.syncOrganizationMembership(clerkUserId, organizationId);
            organizationSyncService.updateUserRoleFromClerkRole(clerkUserId, role, organizationId);
        } catch (Exception e) {
            log.error("Error handling organizationMembership.created event", e);
            throw new RuntimeException("Failed to handle organizationMembership.created event", e);
        }
    }

    @Override
    public void handleOrganizationMembershipUpdated(ClerkWebhookRequest webhookRequest) {
        log.info("Handling organizationMembership.updated event");

        try {
            JsonNode data = webhookRequest.getData();
            JsonNode organization = data.get("organization");
            JsonNode publicUserData = data.get("public_user_data");

            String organizationId = organization.get("id").asText();
            String clerkUserId = publicUserData.get("user_id").asText();
            String role = data.get("role").asText();

            log.info("User {} role updated in organization {} to {}", clerkUserId, organizationId, role);

            // Update role
            organizationSyncService.updateUserRoleFromClerkRole(clerkUserId, role, organizationId);
        } catch (Exception e) {
            log.error("Error handling organizationMembership.updated event", e);
            throw new RuntimeException("Failed to handle organizationMembership.updated event", e);
        }
    }

    @Override
    public void handleOrganizationMembershipDeleted(ClerkWebhookRequest webhookRequest) {
        log.info("Handling organizationMembership.deleted event");

        try {
            JsonNode data = webhookRequest.getData();
            JsonNode organization = data.get("organization");
            JsonNode publicUserData = data.get("public_user_data");

            String organizationId = organization.get("id").asText();
            String clerkUserId = publicUserData.get("user_id").asText();

            log.info("User {} removed from organization {}", clerkUserId, organizationId);
            // Handle membership removal - could update user role or remove access

        } catch (Exception e) {
            log.error("Error handling organizationMembership.deleted event", e);
            throw new RuntimeException("Failed to handle organizationMembership.deleted event", e);
        }
    }

    /**
     * Create audit event record.
     */
    private ClerkWebhookEvent createAuditEvent(ClerkWebhookRequest webhookRequest) {
        ClerkWebhookEvent auditEvent = new ClerkWebhookEvent();
        auditEvent.setEventId(webhookRequest.getId());
        auditEvent.setEventType(webhookRequest.getType());
        auditEvent.setPayload(webhookRequest.getData().toString());
        auditEvent.setProcessed(false);
        auditEvent.setRetryCount(0);

        ZonedDateTime now = ZonedDateTime.now();
        auditEvent.setReceivedAt(now);
        auditEvent.setCreatedAt(now);

        // Extract user ID if available
        if (webhookRequest.getData() != null) {
            try {
                if (webhookRequest.getData().has("id")) {
                    auditEvent.setClerkUserId(webhookRequest.getData().get("id").asText());
                } else if (webhookRequest.getData().has("user_id")) {
                    auditEvent.setClerkUserId(webhookRequest.getData().get("user_id").asText());
                }
            } catch (Exception e) {
                log.debug("Could not extract user ID from webhook data", e);
            }
        }

        return webhookEventRepository.save(auditEvent);
    }

    /**
     * Extract email from Clerk user data.
     */
    private String extractEmail(JsonNode userData) {
        if (userData.has("email_addresses")) {
            JsonNode emailAddresses = userData.get("email_addresses");
            if (emailAddresses.isArray() && emailAddresses.size() > 0) {
                JsonNode primaryEmail = emailAddresses.get(0);
                if (primaryEmail.has("email_address")) {
                    return primaryEmail.get("email_address").asText();
                }
            }
        }

        if (userData.has("email")) {
            return userData.get("email").asText();
        }

        return "";
    }
}
