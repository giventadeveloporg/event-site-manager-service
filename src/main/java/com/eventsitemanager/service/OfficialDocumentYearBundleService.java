package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.OfficialDocumentYearBundleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for {@link com.eventsitemanager.domain.OfficialDocumentYearBundle}.
 */
public interface OfficialDocumentYearBundleService {
    /**
     * Create a bundle row (idempotent insert if missing) after an official-document upload for the same tenant/category/year.
     */
    void ensureBundleForUpload(String tenantId, Long officialDocumentCategoryId, Integer documentYear);

    OfficialDocumentYearBundleDTO save(OfficialDocumentYearBundleDTO dto);

    OfficialDocumentYearBundleDTO update(OfficialDocumentYearBundleDTO dto);

    Optional<OfficialDocumentYearBundleDTO> partialUpdate(OfficialDocumentYearBundleDTO dto);

    Page<OfficialDocumentYearBundleDTO> findAll(Pageable pageable);

    Optional<OfficialDocumentYearBundleDTO> findOne(Long id);

    void delete(Long id);
}
