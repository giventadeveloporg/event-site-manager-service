package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria for {@link com.nextjstemplate.domain.OfficialDocumentYearBundle}.
 * Example: {@code ?tenantId.equals=x&officialDocumentCategoryId.equals=1&documentYear.equals=2024&sort=documentYear,desc}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfficialDocumentYearBundleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter officialDocumentCategoryId;
    private IntegerFilter documentYear;
    private LongFilter coverEventMediaId;
    private InstantFilter createdAt;
    private InstantFilter updatedAt;
    private Boolean distinct;

    public OfficialDocumentYearBundleCriteria() {}

    public OfficialDocumentYearBundleCriteria(OfficialDocumentYearBundleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.officialDocumentCategoryId = other.officialDocumentCategoryId == null ? null : other.officialDocumentCategoryId.copy();
        this.documentYear = other.documentYear == null ? null : other.documentYear.copy();
        this.coverEventMediaId = other.coverEventMediaId == null ? null : other.coverEventMediaId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OfficialDocumentYearBundleCriteria copy() {
        return new OfficialDocumentYearBundleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public LongFilter getOfficialDocumentCategoryId() {
        return officialDocumentCategoryId;
    }

    public void setOfficialDocumentCategoryId(LongFilter officialDocumentCategoryId) {
        this.officialDocumentCategoryId = officialDocumentCategoryId;
    }

    public LongFilter officialDocumentCategoryId() {
        if (officialDocumentCategoryId == null) {
            setOfficialDocumentCategoryId(new LongFilter());
        }
        return officialDocumentCategoryId;
    }

    public IntegerFilter getDocumentYear() {
        return documentYear;
    }

    public void setDocumentYear(IntegerFilter documentYear) {
        this.documentYear = documentYear;
    }

    public IntegerFilter documentYear() {
        if (documentYear == null) {
            setDocumentYear(new IntegerFilter());
        }
        return documentYear;
    }

    public LongFilter getCoverEventMediaId() {
        return coverEventMediaId;
    }

    public void setCoverEventMediaId(LongFilter coverEventMediaId) {
        this.coverEventMediaId = coverEventMediaId;
    }

    public LongFilter coverEventMediaId() {
        if (coverEventMediaId == null) {
            setCoverEventMediaId(new LongFilter());
        }
        return coverEventMediaId;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfficialDocumentYearBundleCriteria that = (OfficialDocumentYearBundleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(officialDocumentCategoryId, that.officialDocumentCategoryId) &&
            Objects.equals(documentYear, that.documentYear) &&
            Objects.equals(coverEventMediaId, that.coverEventMediaId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, officialDocumentCategoryId, documentYear, coverEventMediaId, createdAt, updatedAt, distinct);
    }
}
