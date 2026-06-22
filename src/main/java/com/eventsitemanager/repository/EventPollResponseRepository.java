package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventPollResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventPollResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventPollResponseRepository extends JpaRepository<EventPollResponse, Long>, JpaSpecificationExecutor<EventPollResponse> {}
