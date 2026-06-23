package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventAttendeeGuest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAttendeeGuest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAttendeeGuestRepository
    extends JpaRepository<EventAttendeeGuest, Long>, JpaSpecificationExecutor<EventAttendeeGuest> {}
