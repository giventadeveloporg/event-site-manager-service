package com.eventsitemanager.repository;

import com.eventsitemanager.domain.PublicProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicProfileRepository extends JpaRepository<PublicProfile, Long>, JpaSpecificationExecutor<PublicProfile> {
    boolean existsByTenantId(String tenantId);

    java.util.Optional<PublicProfile> findFirstByTenantId(String tenantId);
}
