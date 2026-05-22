package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetitionRegistration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
/**
 * Spring Data JPA repository for the EventCompetitionRegistration entity.
 */
@Repository
public interface EventCompetitionRegistrationRepository extends JpaRepository<EventCompetitionRegistration, Long>, JpaSpecificationExecutor<EventCompetitionRegistration> {
    boolean existsByCompetitionIdAndParticipantProfileIdAndIdNot(Long competitionId, Long participantProfileId, Long id);
    long countByCompetitionIdAndRegistrationStatusNotIn(Long competitionId, java.util.Collection<com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus> statuses);
    long countByGroupLeaderRegistrationId(Long groupLeaderRegistrationId);
}
