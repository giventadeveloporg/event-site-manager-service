package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetitionParticipant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionParticipant entity.
 */
@Repository
public interface EventCompetitionParticipantRepository extends JpaRepository<EventCompetitionParticipant, Long>, JpaSpecificationExecutor<EventCompetitionParticipant> {
}
