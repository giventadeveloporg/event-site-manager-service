package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventCompetitionRegistration;
import java.util.Collection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionRegistration entity.
 */
@Repository
public interface EventCompetitionRegistrationRepository
    extends JpaRepository<EventCompetitionRegistration, Long>, JpaSpecificationExecutor<EventCompetitionRegistration> {
    boolean existsByCompetitionIdAndParticipantProfileIdAndIdNot(Long competitionId, Long participantProfileId, Long id);
    long countByCompetitionIdAndRegistrationStatusNotIn(
        Long competitionId,
        java.util.Collection<com.eventsitemanager.domain.enumeration.CompetitionRegistrationStatus> statuses
    );
    long countByGroupLeaderRegistrationId(Long groupLeaderRegistrationId);
}
