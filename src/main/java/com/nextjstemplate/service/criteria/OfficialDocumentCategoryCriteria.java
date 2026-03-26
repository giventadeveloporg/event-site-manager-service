package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria for {@link com.nextjstemplate.domain.OfficialDocumentCategory}.
 * Example: {@code /official-document-categories?tenantId.equals=x&isActive.equals=true&sort=sortOrder,asc}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OfficialDocumentCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter slug;
    private StringFilter displayName;
    private BooleanFilter isActive;
    private IntegerFilter sortOrder;
    private InstantFilter createdAt;
    private InstantFilter updatedAt;
    private Boolean distinct;

    public OfficialDocumentCategoryCriteria() {}

    public OfficialDocumentCategoryCriteria(OfficialDocumentCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OfficialDocumentCategoryCriteria copy() {
        return new OfficialDocumentCategoryCriteria(this);
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

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter displayName() {
        if (displayName == null) {
            setDisplayName(new StringFilter());
        }
        return displayName;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            setSortOrder(new IntegerFilter());
        }
        return sortOrder;
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
        OfficialDocumentCategoryCriteria that = (OfficialDocumentCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, slug, displayName, isActive, sortOrder, createdAt, updatedAt, distinct);
    }
}
