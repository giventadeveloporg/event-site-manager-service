package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventRecurrenceSeries;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventRecurrenceSeries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRecurrenceSeriesRepository extends JpaRepository<EventRecurrenceSeries, Long> {
    /**
     * Find recurrence series by parent event ID.
     *
     * @param parentEventId the parent event ID
     * @return optional recurrence series
     */
    Optional<EventRecurrenceSeries> findByParentEventId(Long parentEventId);

    /**
     * Delete recurrence series by parent event ID.
     *
     * @param parentEventId the parent event ID
     */
    void deleteByParentEventId(Long parentEventId);
}
