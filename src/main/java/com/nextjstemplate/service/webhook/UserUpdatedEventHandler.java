package com.nextjstemplate.service.webhook;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.dto.WebhookEventRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler for user.updated webhook events.
 */
@Component
public class UserUpdatedEventHandler implements WebhookEventHandler {

    private static final Logger log = LoggerFactory.getLogger(UserUpdatedEventHandler.class);

    private final UserProfileRepository userProfileRepository;

    public UserUpdatedEventHandler(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void handle(WebhookEventRequest event) {
        log.info("Processing user.updated event: {}", event.getId());

        try {
            Map<String, Object> userData = event.getData();
            String clerkUserId = (String) userData.get("id");

            List<UserProfile> profiles = userProfileRepository.findAllByUserId(clerkUserId);
            if (profiles.isEmpty()) {
                log.warn("No user profiles found for update: {}", clerkUserId);
                return;
            }

            String firstName = (String) userData.get("first_name");
            String lastName = (String) userData.get("last_name");
            String profileImageUrl = (String) userData.get("profile_image_url");

            for (UserProfile userProfile : profiles) {
                if (firstName != null) {
                    userProfile.setFirstName(firstName);
                }
                if (lastName != null) {
                    userProfile.setLastName(lastName);
                }
                if (profileImageUrl != null) {
                    userProfile.setProfileImageUrl(profileImageUrl);
                }
                userProfile.setUpdatedAt(ZonedDateTime.now());
                userProfileRepository.save(userProfile);
            }

            log.info("Successfully updated {} user profile(s) from webhook for Clerk user: {}", profiles.size(), clerkUserId);
        } catch (Exception e) {
            log.error("Error handling user.updated event", e);
            throw new RuntimeException("Failed to handle user.updated event", e);
        }
    }

    @Override
    public String getEventType() {
        return "user.updated";
    }
}
