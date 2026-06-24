package com.eventsitemanager.repository;

import com.eventsitemanager.domain.BulkOperationLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BulkOperationLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BulkOperationLogRepository extends JpaRepository<BulkOperationLog, Long>, JpaSpecificationExecutor<BulkOperationLog> {}
