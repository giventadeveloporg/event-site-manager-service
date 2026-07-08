package com.eventsitemanager.repository;

import com.eventsitemanager.domain.GasStationUserStationAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface GasStationUserStationAssignmentRepository
    extends JpaRepository<GasStationUserStationAssignment, Long>, JpaSpecificationExecutor<GasStationUserStationAssignment> {
    List<GasStationUserStationAssignment> findByTenantIdAndUserProfileId(String tenantId, Long userProfileId);

    Optional<GasStationUserStationAssignment> findByTenantIdAndUserProfileIdAndStationId(
        String tenantId,
        Long userProfileId,
        Long stationId
    );

    boolean existsByTenantIdAndUserProfileIdAndStationId(String tenantId, Long userProfileId, Long stationId);
}
