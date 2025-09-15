package com.nextjstemplate.repository;

import com.nextjstemplate.domain.EventTicketType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTicketTypeRepository extends JpaRepository<EventTicketType, Long>, JpaSpecificationExecutor<EventTicketType> {
    // Custom method to find ticket types by event id
    java.util.List<EventTicketType> findByEvent_Id(Long eventId);
}
