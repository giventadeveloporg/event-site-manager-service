package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventScoreCardDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventScoreCardDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventScoreCardDetailRepository
    extends JpaRepository<EventScoreCardDetail, Long>, JpaSpecificationExecutor<EventScoreCardDetail> {}
