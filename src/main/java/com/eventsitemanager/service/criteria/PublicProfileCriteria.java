package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PublicProfileCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter displayName;
    private StringFilter publicSlug;
    private BooleanFilter isPublished;
    private LongFilter ownerUserProfileId;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;
    private Boolean distinct;

    public PublicProfileCriteria() {}

    public PublicProfileCriteria(PublicProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.displayName = other.optionalDisplayName().map(StringFilter::copy).orElse(null);
        this.publicSlug = other.optionalPublicSlug().map(StringFilter::copy).orElse(null);
        this.isPublished = other.optionalIsPublished().map(BooleanFilter::copy).orElse(null);
        this.ownerUserProfileId = other.optionalOwnerUserProfileId().map(LongFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PublicProfileCriteria copy() {
        return new PublicProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public Optional<StringFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public Optional<StringFilter> optionalDisplayName() {
        return Optional.ofNullable(displayName);
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getPublicSlug() {
        return publicSlug;
    }

    public Optional<StringFilter> optionalPublicSlug() {
        return Optional.ofNullable(publicSlug);
    }

    public void setPublicSlug(StringFilter publicSlug) {
        this.publicSlug = publicSlug;
    }

    public BooleanFilter getIsPublished() {
        return isPublished;
    }

    public Optional<BooleanFilter> optionalIsPublished() {
        return Optional.ofNullable(isPublished);
    }

    public void setIsPublished(BooleanFilter isPublished) {
        this.isPublished = isPublished;
    }

    public LongFilter getOwnerUserProfileId() {
        return ownerUserProfileId;
    }

    public Optional<LongFilter> optionalOwnerUserProfileId() {
        return Optional.ofNullable(ownerUserProfileId);
    }

    public void setOwnerUserProfileId(LongFilter ownerUserProfileId) {
        this.ownerUserProfileId = ownerUserProfileId;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicProfileCriteria that = (PublicProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(publicSlug, that.publicSlug) &&
            Objects.equals(isPublished, that.isPublished) &&
            Objects.equals(ownerUserProfileId, that.ownerUserProfileId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, displayName, publicSlug, isPublished, ownerUserProfileId, createdAt, updatedAt, distinct);
    }
}
