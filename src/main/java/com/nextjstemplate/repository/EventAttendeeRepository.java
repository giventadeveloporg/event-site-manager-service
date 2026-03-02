package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventAttendee;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAttendee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long>, JpaSpecificationExecutor<EventAttendee> {
    /**
     * Find an attendee by event, email (case-insensitive), and tenant for registration upsert.
     */
    Optional<EventAttendee> findOneByEventIdAndEmailIgnoreCaseAndTenantId(Long eventId, String email, String tenantId);
}
