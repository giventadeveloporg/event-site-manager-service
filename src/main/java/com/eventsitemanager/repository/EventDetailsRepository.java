package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventDetails entity.
 *
 * When extending this class, extend EventDetailsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface EventDetailsRepository
    extends EventDetailsRepositoryWithBagRelationships, JpaRepository<EventDetails, Long>, JpaSpecificationExecutor<EventDetails> {
    default Optional<EventDetails> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<EventDetails> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<EventDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    /**
     * Find all events in a recurrence series by series ID.
     *
     * @param recurrenceSeriesId the recurrence series ID
     * @return list of events in the series
     */
    List<EventDetails> findByRecurrenceSeriesId(Long recurrenceSeriesId);

    /**
     * Find all child events by parent event ID.
     * Uses native SQL query to directly query the parent_event_id column,
     * avoiding any JPA lazy loading issues and ensuring accurate filtering.
     *
     * @param parentEventId the parent event ID
     * @return list of child events
     */
    @Query(value = "SELECT * FROM event_details WHERE parent_event_id = :parentEventId", nativeQuery = true)
    List<EventDetails> findByParentEventId(@Param("parentEventId") Long parentEventId);

    /**
     * Get the parent_event_id for a specific event by directly querying the database column.
     * This is used for validation to ensure we're only updating correct child events.
     *
     * @param eventId the event ID
     * @return the parent event ID, or null if the event has no parent
     */
    @Query(value = "SELECT parent_event_id FROM event_details WHERE id = :eventId", nativeQuery = true)
    Long getParentEventIdByEventId(@Param("eventId") Long eventId);

    /**
     * Find parent event (event with no parent and matching series ID).
     *
     * @param recurrenceSeriesId the recurrence series ID
     * @return optional parent event
     */
    Optional<EventDetails> findByRecurrenceSeriesIdAndParentEventIsNull(Long recurrenceSeriesId);
}
