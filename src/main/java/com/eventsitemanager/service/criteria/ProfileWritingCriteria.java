package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.ProfileWritingStatus;
import com.eventsitemanager.domain.enumeration.ProfileWritingType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileWritingCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter title;
    private StringFilter slug;
    private ProfileWritingTypeFilter writingType;
    private ProfileWritingStatusFilter status;
    private IntegerFilter displayOrder;
    private LocalDateFilter publishedAt;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public ProfileWritingCriteria() {}

    public ProfileWritingCriteria(ProfileWritingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.writingType = other.writingType == null ? null : other.writingType.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.publishedAt = other.publishedAt == null ? null : other.publishedAt.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfileWritingCriteria copy() {
        return new ProfileWritingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public ProfileWritingTypeFilter getWritingType() {
        return writingType;
    }

    public void setWritingType(ProfileWritingTypeFilter writingType) {
        this.writingType = writingType;
    }

    public ProfileWritingStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ProfileWritingStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateFilter getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateFilter publishedAt) {
        this.publishedAt = publishedAt;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileWritingCriteria that = (ProfileWritingCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }

    public static class ProfileWritingTypeFilter extends Filter<ProfileWritingType> {

        public ProfileWritingTypeFilter() {}

        public ProfileWritingTypeFilter(ProfileWritingTypeFilter filter) {
            super(filter);
        }

        @Override
        public ProfileWritingTypeFilter copy() {
            return new ProfileWritingTypeFilter(this);
        }
    }

    public static class ProfileWritingStatusFilter extends Filter<ProfileWritingStatus> {

        public ProfileWritingStatusFilter() {}

        public ProfileWritingStatusFilter(ProfileWritingStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProfileWritingStatusFilter copy() {
            return new ProfileWritingStatusFilter(this);
        }
    }
}
