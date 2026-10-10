package com.eventsitemanager.repository;

import com.eventsitemanager.domain.TenantOnboardingRequest;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantOnboardingRequest entity.
 */
@Repository
public interface TenantOnboardingRequestRepository
    extends JpaRepository<TenantOnboardingRequest, Long>, JpaSpecificationExecutor<TenantOnboardingRequest> {
    Optional<TenantOnboardingRequest> findByRequestCode(String requestCode);

    boolean existsByRequestCode(String requestCode);

    boolean existsByStatusAndRequestedHostnameIgnoreCase(OnboardingRequestStatus status, String requestedHostname);

    boolean existsByStatusAndContactEmailIgnoreCase(OnboardingRequestStatus status, String contactEmail);

    boolean existsByAssignedTenantIdAndIdNot(String assignedTenantId, Long id);

    /** Row lock so two concurrent approve/reject calls cannot both act on a PENDING request. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from TenantOnboardingRequest r where r.id = :id")
    Optional<TenantOnboardingRequest> findByIdForUpdate(@Param("id") Long id);
}
