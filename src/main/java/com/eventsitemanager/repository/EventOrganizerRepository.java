package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventOrganizer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventOrganizer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long>, JpaSpecificationExecutor<EventOrganizer> {}
