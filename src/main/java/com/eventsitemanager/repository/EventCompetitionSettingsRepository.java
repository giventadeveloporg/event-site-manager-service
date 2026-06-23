package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventCompetitionSettings;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventCompetitionSettings entity.
 */
@Repository
public interface EventCompetitionSettingsRepository
    extends JpaRepository<EventCompetitionSettings, Long>, JpaSpecificationExecutor<EventCompetitionSettings> {
    Optional<EventCompetitionSettings> findOneByEventId(Long eventId);
}
