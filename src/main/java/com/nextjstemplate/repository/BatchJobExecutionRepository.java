package com.nextjstemplate.repository;

import com.nextjstemplate.domain.BatchJobExecution;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BatchJobExecution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, Long>, JpaSpecificationExecutor<BatchJobExecution> {
    List<BatchJobExecution> findByTenantIdOrderByStartedAtDesc(String tenantId);

    List<BatchJobExecution> findByJobNameOrderByStartedAtDesc(String jobName);

    List<BatchJobExecution> findByStatusOrderByStartedAtDesc(String status);
}

