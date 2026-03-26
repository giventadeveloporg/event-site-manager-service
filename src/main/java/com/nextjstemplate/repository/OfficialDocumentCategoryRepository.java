package com.nextjstemplate.repository;

import com.nextjstemplate.domain.OfficialDocumentCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialDocumentCategoryRepository
    extends JpaRepository<OfficialDocumentCategory, Long>, JpaSpecificationExecutor<OfficialDocumentCategory> {
    Optional<OfficialDocumentCategory> findByTenantIdAndSlug(String tenantId, String slug);
}
