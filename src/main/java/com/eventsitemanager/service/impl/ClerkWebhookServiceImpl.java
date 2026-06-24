package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ClerkWebhookEvent;
import com.eventsitemanager.repository.ClerkWebhookEventRepository;
import com.eventsitemanager.service.ClerkWebhookService;
import com.eventsitemanager.service.ClerkWebhookSignatureValidator;
import com.eventsitemanager.service.dto.WebhookEventRequest;
import com.eventsitemanager.service.dto.WebhookEventResponse;
import com.eventsitemanager.service.webhook.WebhookEventHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for processing Clerk webhook events.
 */
@Service
@Transactional
public class ClerkWebhookServiceImpl implements ClerkWebhookService {

    private static final Logger log = LoggerFactory.getLogger(ClerkWebhookServiceImpl.class);

    private final ClerkWebhookEventRepository webhookEventRepository;
    private final ClerkWebhookSignatureValidator signatureValidator;
    private final ObjectMapper objectMapper;
    private final List<WebhookEventHandler> eventHandlers;

    public ClerkWebhookServiceImpl(
        ClerkWebhookEventRepository webhookEventRepository,
        ClerkWebhookSignatureValidator signatureValidator,
        ObjectMapper objectMapper,
        List<WebhookEventHandler> eventHandlers
    ) {
        this.webhookEventRepository = webhookEventRepository;
        this.signatureValidator = signatureValidator;
        this.objectMapper = objectMapper;
        this.eventHandlers = eventHandlers;
        log.info("Initialized ClerkWebhookService with {} event handlers", eventHandlers.size());
    }

    @Override
    public WebhookEventResponse processWebhookEvent(String payload, String signature) {
        log.info("Processing Clerk webhook event");

        WebhookEventResponse response = new WebhookEventResponse();

        try {
            // Verify signature
            if (!signatureValidator.verifySignature(payload, signature)) {
                log.error("Invalid webhook signature");
                response.setSuccess(false);
                response.setMessage("Invalid signature");
                return response;
            }

            // Parse payload
            WebhookEventRequest event = parsePayload(payload);

            // Check idempotency - have we already processed this event?
            if (isEventProcessed(event.getId())) {
                log.info("Event {} already processed, skipping", event.getId());
                response.setSuccess(true);
                response.setMessage("Event already processed");
                response.setEventId(event.getId());
                response.setEventType(event.getType());
                return response;
            }

            // Save webhook event to database (audit trail)
            ClerkWebhookEvent webhookEvent = saveWebhookEvent(event, payload);

            // Route to appropriate handler
            try {
                handleEvent(event);

                // Mark as processed
                webhookEvent.setProcessed(true);
                webhookEvent.setProcessedAt(ZonedDateTime.now());
                webhookEventRepository.save(webhookEvent);

                response.setSuccess(true);
                response.setMessage("Webhook processed successfully");
                response.setEventId(event.getId());
                response.setEventType(event.getType());

                log.info("Successfully processed webhook event: {} (type: {})", event.getId(), event.getType());
            } catch (Exception e) {
                // Log processing error
                log.error("Error processing webhook event: {}", event.getId(), e);

                webhookEvent.setErrorMessage(e.getMessage());
                webhookEvent.setRetryCount(webhookEvent.getRetryCount() != null ? webhookEvent.getRetryCount() + 1 : 1);
                webhookEventRepository.save(webhookEvent);

                response.setSuccess(false);
                response.setMessage("Error processing webhook: " + e.getMessage());
                response.setEventId(event.getId());
                response.setEventType(event.getType());
            }

            return response;
        } catch (Exception e) {
            log.error("Error handling webhook", e);
            response.setSuccess(false);
            response.setMessage("Webhook processing failed: " + e.getMessage());
            return response;
        }
    }

    @Override
    public boolean isEventProcessed(String eventId) {
        return webhookEventRepository.existsByEventId(eventId);
    }

    @Override
    public WebhookEventRequest parsePayload(String payload) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

            WebhookEventRequest event = new WebhookEventRequest();
            event.setId((String) payloadMap.get("id"));
            event.setType((String) payloadMap.get("type"));
            event.setObject((String) payloadMap.get("object"));
            event.setCreated(payloadMap.get("created") instanceof Integer ? ((Integer) payloadMap.get("created")).longValue() : null);

            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) payloadMap.get("data");
            event.setData(data);

            return event;
        } catch (Exception e) {
            log.error("Error parsing webhook payload", e);
            throw new RuntimeException("Failed to parse webhook payload", e);
        }
    }

    /**
     * Save webhook event to database for audit trail.
     */
    private ClerkWebhookEvent saveWebhookEvent(WebhookEventRequest event, String payload) {
        ClerkWebhookEvent webhookEvent = new ClerkWebhookEvent();
        webhookEvent.setEventId(event.getId());
        webhookEvent.setEventType(event.getType());
        webhookEvent.setPayload(payload);
        webhookEvent.setProcessed(false);
        webhookEvent.setRetryCount(0);

        ZonedDateTime now = ZonedDateTime.now();
        webhookEvent.setReceivedAt(now);
        webhookEvent.setCreatedAt(now);

        // Extract Clerk user ID from payload if present
        if (event.getData() != null && event.getData().get("id") != null) {
            webhookEvent.setClerkUserId(event.getData().get("id").toString());
        }

        return webhookEventRepository.save(webhookEvent);
    }

    /**
     * Route webhook event to appropriate handler.
     */
    private void handleEvent(WebhookEventRequest event) {
        String eventType = event.getType();
        log.debug("Routing event to handler: {}", eventType);

        // Find appropriate handler for this event type
        for (WebhookEventHandler handler : eventHandlers) {
            String handlerType = handler.getEventType();

            // Exact match or wildcard match (e.g., "session.*" matches "session.created")
            if (
                handlerType.equals(eventType) ||
                (handlerType.endsWith(".*") && eventType.startsWith(handlerType.substring(0, handlerType.length() - 2)))
            ) {
                try {
                    log.info("Delegating to handler: {} for event: {}", handler.getClass().getSimpleName(), eventType);
                    handler.handle(event);
                    return;
                } catch (Exception e) {
                    log.error("Handler failed to process event: {}", eventType, e);
                    throw e;
                }
            }
        }

        // No handler found
        log.warn("No handler found for event type: {}", eventType);
    }
}
