package com.eventsitemanager.repository;

import com.eventsitemanager.domain.WhatsAppLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WhatsAppLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhatsAppLogRepository extends JpaRepository<WhatsAppLog, Long>, JpaSpecificationExecutor<WhatsAppLog> {}
