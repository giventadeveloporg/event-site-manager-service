package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetition entity.
 */
@Repository
public interface EventCompetitionRepository extends JpaRepository<EventCompetition, Long>, JpaSpecificationExecutor<EventCompetition> {
}
