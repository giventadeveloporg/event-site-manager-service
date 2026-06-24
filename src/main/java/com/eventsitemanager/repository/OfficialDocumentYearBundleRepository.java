package com.eventsitemanager.repository;

import com.eventsitemanager.domain.OfficialDocumentYearBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link OfficialDocumentYearBundle}.
 */
@Repository
public interface OfficialDocumentYearBundleRepository
    extends JpaRepository<OfficialDocumentYearBundle, Long>, JpaSpecificationExecutor<OfficialDocumentYearBundle> {
    boolean existsByTenantIdAndOfficialDocumentCategoryIdAndDocumentYear(
        String tenantId,
        Long officialDocumentCategoryId,
        Integer documentYear
    );
}
