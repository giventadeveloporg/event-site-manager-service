package com.eventsitemanager.repository;

import com.eventsitemanager.domain.EventAdminAuditLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventAdminAuditLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAdminAuditLogRepository
    extends JpaRepository<EventAdminAuditLog, Long>, JpaSpecificationExecutor<EventAdminAuditLog> {}
