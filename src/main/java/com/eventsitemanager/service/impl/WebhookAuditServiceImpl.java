package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.ClerkWebhookEvent;
import com.eventsitemanager.repository.ClerkWebhookEventRepository;
import com.eventsitemanager.service.WebhookAuditService;
import com.eventsitemanager.service.webhook.InboundWebhookGuard;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of WebhookAuditService for webhook event audit logging.
 */
@Service
@Transactional
public class WebhookAuditServiceImpl implements WebhookAuditService {

    private static final Logger log = LoggerFactory.getLogger(WebhookAuditServiceImpl.class);
    private static final int MAX_RETRIES = 3;
    private static final int DEFAULT_CLEANUP_DAYS = 30;

    private final ClerkWebhookEventRepository webhookEventRepository;
    private final InboundWebhookGuard inboundWebhookGuard;

    public WebhookAuditServiceImpl(ClerkWebhookEventRepository webhookEventRepository, InboundWebhookGuard inboundWebhookGuard) {
        this.webhookEventRepository = webhookEventRepository;
        this.inboundWebhookGuard = inboundWebhookGuard;
    }

    @Override
    public ClerkWebhookEvent logEvent(String eventId, String eventType, String clerkUserId, String payload) {
        log.debug("Logging webhook event: {} (type: {})", eventId, eventType);

        ClerkWebhookEvent event = new ClerkWebhookEvent();
        event.setEventId(eventId);
        event.setEventType(eventType);
        event.setClerkUserId(clerkUserId);
        event.setPayload(payload);
        event.setProcessed(false);
        event.setRetryCount(0);

        ZonedDateTime now = ZonedDateTime.now();
        event.setReceivedAt(now);
        event.setCreatedAt(now);

        return webhookEventRepository.save(event);
    }

    @Override
    public ClerkWebhookEvent markEventAsProcessed(String eventId) {
        log.debug("Marking event as processed: {}", eventId);

        ClerkWebhookEvent event = webhookEventRepository
            .findByEventId(eventId)
            .orElseThrow(() -> new RuntimeException("Webhook event not found: " + eventId));

        event.setProcessed(true);
        event.setProcessedAt(ZonedDateTime.now());
        event.setErrorMessage(null);

        return webhookEventRepository.save(event);
    }

    @Override
    public ClerkWebhookEvent recordEventError(String eventId, String errorMessage) {
        log.warn("Recording error for event {}: {}", eventId, errorMessage);

        ClerkWebhookEvent event = webhookEventRepository
            .findByEventId(eventId)
            .orElseThrow(() -> new RuntimeException("Webhook event not found: " + eventId));

        event.setProcessed(false);
        event.setErrorMessage(errorMessage);

        return webhookEventRepository.save(event);
    }

    @Override
    public ClerkWebhookEvent incrementRetryCount(String eventId) {
        log.debug("Incrementing retry count for event: {}", eventId);

        ClerkWebhookEvent event = webhookEventRepository
            .findByEventId(eventId)
            .orElseThrow(() -> new RuntimeException("Webhook event not found: " + eventId));

        int currentRetries = event.getRetryCount() != null ? event.getRetryCount() : 0;
        event.setRetryCount(currentRetries + 1);

        return webhookEventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkWebhookEvent> getUnprocessedEvents() {
        log.debug("Fetching unprocessed webhook events");
        return webhookEventRepository.findByProcessedFalseOrderByReceivedAtAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkWebhookEvent> getFailedEventsForRetry() {
        log.debug("Fetching failed events for retry");
        return webhookEventRepository.findFailedEventsForRetry(MAX_RETRIES);
    }

    @Override
    public int cleanupOldEvents(int daysToKeep) {
        log.info("Starting cleanup of webhook events older than {} days", daysToKeep);

        ZonedDateTime cutoffDate = ZonedDateTime.now().minusDays(daysToKeep);
        List<ClerkWebhookEvent> oldEvents = webhookEventRepository.findProcessedEventsOlderThan(cutoffDate);

        int deletedCount = oldEvents.size();
        webhookEventRepository.deleteAll(oldEvents);

        log.info("Cleaned up {} old webhook events", deletedCount);
        return deletedCount;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClerkWebhookEvent> getEventsForUser(String clerkUserId) {
        log.debug("Fetching webhook events for user: {}", clerkUserId);
        return webhookEventRepository.findByClerkUserIdOrderByReceivedAtDesc(clerkUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getProcessingStatistics() {
        log.debug("Computing webhook processing statistics");

        List<ClerkWebhookEvent> allEvents = webhookEventRepository.findAll();

        long total = allEvents.size();
        long processed = allEvents.stream().filter(e -> Boolean.TRUE.equals(e.getProcessed())).count();
        long failed = allEvents.stream().filter(e -> Boolean.FALSE.equals(e.getProcessed()) && e.getErrorMessage() != null).count();
        long pending = allEvents.stream().filter(e -> Boolean.FALSE.equals(e.getProcessed()) && e.getErrorMessage() == null).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("processed", processed);
        stats.put("failed", failed);
        stats.put("pending", pending);

        log.debug("Statistics: total={}, processed={}, failed={}, pending={}", total, processed, failed, pending);
        return stats;
    }

    /**
     * Scheduled job to clean up old webhook events.
     * Runs daily at 2:00 AM.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledCleanup() {
        if (!inboundWebhookGuard.isClerkScheduledTasksEnabled()) {
            return;
        }

        log.info("Running scheduled webhook event cleanup");
        try {
            int deletedCount = cleanupOldEvents(DEFAULT_CLEANUP_DAYS);
            log.info("Scheduled cleanup completed: {} events deleted", deletedCount);
        } catch (Exception e) {
            log.error("Error during scheduled cleanup", e);
        }
    }

    /**
     * Scheduled job to retry failed webhook events.
     * Runs every 15 minutes.
     */
    @Scheduled(cron = "0 */15 * * * *")
    public void retryFailedEvents() {
        if (!inboundWebhookGuard.isClerkScheduledTasksEnabled()) {
            return;
        }

        log.debug("Checking for failed webhook events to retry");

        try {
            List<ClerkWebhookEvent> failedEvents = getFailedEventsForRetry();

            if (failedEvents.isEmpty()) {
                log.debug("No failed events to retry");
                return;
            }

            log.info("Found {} failed events to retry", failedEvents.size());

            for (ClerkWebhookEvent event : failedEvents) {
                try {
                    log.info("Retrying event: {} (attempt {})", event.getEventId(), event.getRetryCount() + 1);

                    // Increment retry count
                    incrementRetryCount(event.getEventId());

                    // Note: Actual retry logic would re-process the event
                    // For now, we just log it
                    log.info("Event {} queued for retry", event.getEventId());
                } catch (Exception e) {
                    log.error("Error retrying event: {}", event.getEventId(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error in retry job", e);
        }
    }
}
