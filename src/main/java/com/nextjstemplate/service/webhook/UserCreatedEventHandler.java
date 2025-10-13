package com.nextjstemplate.service.webhook;

import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.dto.WebhookEventRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler for user.created webhook events.
 */
@Component
public class UserCreatedEventHandler implements WebhookEventHandler {

    private static final Logger log = LoggerFactory.getLogger(UserCreatedEventHandler.class);

    private final UserProfileRepository userProfileRepository;

    public UserCreatedEventHandler(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void handle(WebhookEventRequest event) {
        log.info("Processing user.created event: {}", event.getId());

        try {
            Map<String, Object> userData = event.getData();
            String clerkUserId = (String) userData.get("id");

            // Check if user already exists
            if (userProfileRepository.findByUserId(clerkUserId).isPresent()) {
                log.info("User already exists, skipping: {}", clerkUserId);
                return;
            }

            log.info("User created via webhook - typically handled by sign-up API. Event logged for audit trail: {}", clerkUserId);
            // In most cases, users are created via the sign-up API, not directly in Clerk
            // This handler serves as an audit trail and fallback
        } catch (Exception e) {
            log.error("Error handling user.created event", e);
            throw new RuntimeException("Failed to handle user.created event", e);
        }
    }

    @Override
    public String getEventType() {
        return "user.created";
    }
}
