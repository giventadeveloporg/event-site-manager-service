package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.nextjstemplate.domain.OfficialDocumentYearBundle}.
 */
public class OfficialDocumentYearBundleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long officialDocumentCategoryId;

    @NotNull
    private Integer documentYear;

    private Long coverEventMediaId;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOfficialDocumentCategoryId() {
        return officialDocumentCategoryId;
    }

    public void setOfficialDocumentCategoryId(Long officialDocumentCategoryId) {
        this.officialDocumentCategoryId = officialDocumentCategoryId;
    }

    public Integer getDocumentYear() {
        return documentYear;
    }

    public void setDocumentYear(Integer documentYear) {
        this.documentYear = documentYear;
    }

    public Long getCoverEventMediaId() {
        return coverEventMediaId;
    }

    public void setCoverEventMediaId(Long coverEventMediaId) {
        this.coverEventMediaId = coverEventMediaId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OfficialDocumentYearBundleDTO)) {
            return false;
        }
        OfficialDocumentYearBundleDTO that = (OfficialDocumentYearBundleDTO) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(officialDocumentCategoryId, that.officialDocumentCategoryId) &&
            Objects.equals(documentYear, that.documentYear) &&
            Objects.equals(coverEventMediaId, that.coverEventMediaId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, officialDocumentCategoryId, documentYear, coverEventMediaId, createdAt, updatedAt);
    }
}
