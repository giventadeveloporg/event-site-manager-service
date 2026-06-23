package com.eventsitemanager.repository;

import com.eventsitemanager.domain.FocusGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FocusGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FocusGroupRepository extends JpaRepository<FocusGroup, Long>, JpaSpecificationExecutor<FocusGroup> {
    Optional<FocusGroup> findByTenantIdAndSlug(String tenantId, String slug);
}
