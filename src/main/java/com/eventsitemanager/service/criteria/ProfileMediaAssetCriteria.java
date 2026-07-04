package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileMediaAssetCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter title;
    private StringFilter fileType;
    private IntegerFilter displayOrder;
    private BooleanFilter isDownloadable;
    private BooleanFilter requiresEmail;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public ProfileMediaAssetCriteria() {}

    public ProfileMediaAssetCriteria(ProfileMediaAssetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.fileType = other.fileType == null ? null : other.fileType.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.isDownloadable = other.isDownloadable == null ? null : other.isDownloadable.copy();
        this.requiresEmail = other.requiresEmail == null ? null : other.requiresEmail.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfileMediaAssetCriteria copy() {
        return new ProfileMediaAssetCriteria(this);
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

    public StringFilter getFileType() {
        return fileType;
    }

    public void setFileType(StringFilter fileType) {
        this.fileType = fileType;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public BooleanFilter getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(BooleanFilter isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    public BooleanFilter getRequiresEmail() {
        return requiresEmail;
    }

    public void setRequiresEmail(BooleanFilter requiresEmail) {
        this.requiresEmail = requiresEmail;
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
        ProfileMediaAssetCriteria that = (ProfileMediaAssetCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
