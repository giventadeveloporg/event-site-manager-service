package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ClerkWebhookEvent;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClerkWebhookEvent entity.
 */
@Repository
public interface ClerkWebhookEventRepository extends JpaRepository<ClerkWebhookEvent, Long>, JpaSpecificationExecutor<ClerkWebhookEvent> {
    /**
     * Find webhook event by event ID.
     */
    Optional<ClerkWebhookEvent> findByEventId(String eventId);

    /**
     * Check if event has been processed.
     */
    boolean existsByEventId(String eventId);

    /**
     * Find unprocessed events.
     */
    List<ClerkWebhookEvent> findByProcessedFalseOrderByReceivedAtAsc();

    /**
     * Find events older than a specific date for cleanup.
     */
    @Query("SELECT e FROM ClerkWebhookEvent e WHERE e.receivedAt < :cutoffDate AND e.processed = true")
    List<ClerkWebhookEvent> findProcessedEventsOlderThan(@Param("cutoffDate") ZonedDateTime cutoffDate);

    /**
     * Find failed events for retry.
     */
    @Query("SELECT e FROM ClerkWebhookEvent e WHERE e.processed = false AND e.retryCount < :maxRetries ORDER BY e.receivedAt ASC")
    List<ClerkWebhookEvent> findFailedEventsForRetry(@Param("maxRetries") Integer maxRetries);

    /**
     * Find events by Clerk user ID.
     */
    List<ClerkWebhookEvent> findByClerkUserIdOrderByReceivedAtDesc(String clerkUserId);
}
