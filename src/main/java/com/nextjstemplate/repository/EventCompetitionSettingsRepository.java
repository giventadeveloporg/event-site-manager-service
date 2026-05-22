package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventCompetitionSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Spring Data JPA repository for the EventCompetitionSettings entity.
 */
@Repository
public interface EventCompetitionSettingsRepository extends JpaRepository<EventCompetitionSettings, Long>, JpaSpecificationExecutor<EventCompetitionSettings> {
    Optional<EventCompetitionSettings> findOneByEventId(Long eventId);
}
