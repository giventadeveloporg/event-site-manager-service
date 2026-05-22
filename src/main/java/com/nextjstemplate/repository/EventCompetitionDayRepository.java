package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetitionDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionDay entity.
 */
@Repository
public interface EventCompetitionDayRepository extends JpaRepository<EventCompetitionDay, Long>, JpaSpecificationExecutor<EventCompetitionDay> {
}
