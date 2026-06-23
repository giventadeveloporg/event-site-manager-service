package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventCompetitionResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionResult entity.
 */
@Repository
public interface EventCompetitionResultRepository
    extends JpaRepository<EventCompetitionResult, Long>, JpaSpecificationExecutor<EventCompetitionResult> {
    boolean existsByCompetitionIdAndPlacementAndIsPublishedTrueAndIdNot(Long competitionId, Integer placement, Long id);
}
