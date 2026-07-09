package com.eventsitemanager.repository;

import com.eventsitemanager.domain.ProfileAudienceContact;
import com.eventsitemanager.domain.enumeration.ProfileAudienceContactOptInStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileAudienceContactRepository
    extends JpaRepository<ProfileAudienceContact, Long>, JpaSpecificationExecutor<ProfileAudienceContact> {
    Optional<ProfileAudienceContact> findByTenantIdAndEmail(String tenantId, String email);

    Optional<ProfileAudienceContact> findByTenantIdAndEmailAndUnsubscribeToken(String tenantId, String email, String unsubscribeToken);

    @Query(
        "SELECT DISTINCT p.email FROM ProfileAudienceContact p " +
        "WHERE p.tenantId = :tenantId " +
        "AND p.optInStatus = :optInStatus " +
        "AND p.email IS NOT NULL " +
        "AND p.email <> ''"
    )
    List<String> findEmailsByTenantIdAndOptInStatus(
        @Param("tenantId") String tenantId,
        @Param("optInStatus") ProfileAudienceContactOptInStatus optInStatus
    );
}
