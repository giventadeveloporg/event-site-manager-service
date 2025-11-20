package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
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
     *
     * @param parentEventId the parent event ID
     * @return list of child events
     */
    List<EventDetails> findByParentEventId(Long parentEventId);

    /**
     * Find parent event (event with no parent and matching series ID).
     *
     * @param recurrenceSeriesId the recurrence series ID
     * @return optional parent event
     */
    Optional<EventDetails> findByRecurrenceSeriesIdAndParentEventIsNull(Long recurrenceSeriesId);
}
