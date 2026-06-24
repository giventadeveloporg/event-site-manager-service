package com.eventsitemanager.service.webhook;

import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.dto.WebhookEventRequest;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler for user.deleted webhook events.
 */
@Component
public class UserDeletedEventHandler implements WebhookEventHandler {

    private static final Logger log = LoggerFactory.getLogger(UserDeletedEventHandler.class);

    private final UserProfileRepository userProfileRepository;

    public UserDeletedEventHandler(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void handle(WebhookEventRequest event) {
        log.info("Processing user.deleted event: {}", event.getId());

        try {
            Map<String, Object> userData = event.getData();
            String clerkUserId = (String) userData.get("id");

            // Find all user profiles for this Clerk user (across all tenants)
            List<UserProfile> userProfiles = userProfileRepository
                .findAll()
                .stream()
                .filter(user -> clerkUserId.equals(user.getUserId()))
                .toList();

            if (userProfiles.isEmpty()) {
                log.warn("No user profiles found for deleted user: {}", clerkUserId);
                return;
            }

            // Soft delete by updating status
            for (UserProfile userProfile : userProfiles) {
                userProfile.setUserStatus("DELETED");
                userProfile.setUpdatedAt(java.time.ZonedDateTime.now());
                userProfileRepository.save(userProfile);
                log.info("Marked user profile as deleted: {} (tenant: {})", userProfile.getId(), userProfile.getTenantId());
            }

            log.info("Successfully processed user deletion for {} profiles", userProfiles.size());
        } catch (Exception e) {
            log.error("Error handling user.deleted event", e);
            throw new RuntimeException("Failed to handle user.deleted event", e);
        }
    }

    @Override
    public String getEventType() {
        return "user.deleted";
    }
}
