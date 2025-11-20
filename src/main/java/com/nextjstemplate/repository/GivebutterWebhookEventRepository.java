package com.nextjstemplate.repository;

import com.nextjstemplate.domain.GivebutterWebhookEvent;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GivebutterWebhookEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GivebutterWebhookEventRepository extends JpaRepository<GivebutterWebhookEvent, Long> {
    /**
     * Find a webhook event by event ID (for idempotency check).
     *
     * @param eventId the event ID
     * @return Optional containing the event if found
     */
    Optional<GivebutterWebhookEvent> findByEventId(String eventId);

    /**
     * Check if an event has been processed.
     *
     * @param eventId the event ID
     * @return true if event exists and is processed
     */
    @Query(
        "SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM GivebutterWebhookEvent e WHERE e.eventId = :eventId AND e.processed = true"
    )
    boolean isEventProcessed(String eventId);
}
