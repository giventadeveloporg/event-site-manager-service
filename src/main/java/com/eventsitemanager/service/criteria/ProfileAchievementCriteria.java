package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.ProfileAchievementCategory;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileAchievementCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter title;
    private ProfileAchievementCategoryFilter category;
    private IntegerFilter displayOrder;
    private BooleanFilter isFeatured;
    private LocalDateFilter achievementDate;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public ProfileAchievementCriteria() {}

    public ProfileAchievementCriteria(ProfileAchievementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.isFeatured = other.isFeatured == null ? null : other.isFeatured.copy();
        this.achievementDate = other.achievementDate == null ? null : other.achievementDate.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfileAchievementCriteria copy() {
        return new ProfileAchievementCriteria(this);
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

    public ProfileAchievementCategoryFilter getCategory() {
        return category;
    }

    public void setCategory(ProfileAchievementCategoryFilter category) {
        this.category = category;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public BooleanFilter getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(BooleanFilter isFeatured) {
        this.isFeatured = isFeatured;
    }

    public LocalDateFilter getAchievementDate() {
        return achievementDate;
    }

    public void setAchievementDate(LocalDateFilter achievementDate) {
        this.achievementDate = achievementDate;
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
        ProfileAchievementCriteria that = (ProfileAchievementCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }

    public static class ProfileAchievementCategoryFilter extends Filter<ProfileAchievementCategory> {

        public ProfileAchievementCategoryFilter() {}

        public ProfileAchievementCategoryFilter(ProfileAchievementCategoryFilter filter) {
            super(filter);
        }

        @Override
        public ProfileAchievementCategoryFilter copy() {
            return new ProfileAchievementCategoryFilter(this);
        }
    }
}
