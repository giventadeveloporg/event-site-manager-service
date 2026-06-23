package com.eventsitemanager.service.webhook;

import com.eventsitemanager.service.dto.WebhookEventRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler for session webhook events (created, ended, revoked).
 */
@Component
public class SessionEventHandler implements WebhookEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SessionEventHandler.class);

    private final String eventType;

    public SessionEventHandler() {
        this.eventType = "session.*"; // Handles all session events
    }

    @Override
    public void handle(WebhookEventRequest event) {
        log.info("Processing session event: {} (type: {})", event.getId(), event.getType());

        try {
            Map<String, Object> sessionData = event.getData();
            String sessionId = (String) sessionData.get("id");
            String userId = (String) sessionData.get("user_id");
            String status = (String) sessionData.get("status");

            String eventType = event.getType();

            switch (eventType) {
                case "session.created" -> handleSessionCreated(sessionId, userId, sessionData);
                case "session.ended" -> handleSessionEnded(sessionId, userId, status);
                case "session.revoked" -> handleSessionRevoked(sessionId, userId);
                default -> log.warn("Unknown session event type: {}", eventType);
            }

            log.info("Successfully processed session event: {}", event.getId());
        } catch (Exception e) {
            log.error("Error handling session event", e);
            throw new RuntimeException("Failed to handle session event", e);
        }
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    /**
     * Handle session.created event.
     */
    private void handleSessionCreated(String sessionId, String userId, Map<String, Object> sessionData) {
        log.info("Session created for user {}: {}", userId, sessionId);
        // In a full implementation:
        // 1. Save to clerk_session table
        // 2. Track active sessions count
        // 3. Send notification if needed
        log.debug("Session creation logged - session ID: {}", sessionId);
    }

    /**
     * Handle session.ended event.
     */
    private void handleSessionEnded(String sessionId, String userId, String status) {
        log.info("Session ended for user {}: {} (status: {})", userId, sessionId, status);
        // In a full implementation:
        // 1. Update clerk_session table
        // 2. Mark session as ended
        // 3. Clean up cached session data
        log.debug("Session ended logged - session ID: {}", sessionId);
    }

    /**
     * Handle session.revoked event.
     */
    private void handleSessionRevoked(String sessionId, String userId) {
        log.info("Session revoked for user {}: {}", userId, sessionId);
        // In a full implementation:
        // 1. Update clerk_session table
        // 2. Invalidate all tokens for this session
        // 3. Clear security context if active
        log.debug("Session revoked logged - session ID: {}", sessionId);
    }
}
