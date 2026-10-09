package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileFamilyMemberCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private StringFilter displayName;
    private StringFilter relationship;
    private StringFilter roleTitle;
    private IntegerFilter displayOrder;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public ProfileFamilyMemberCriteria() {}

    public ProfileFamilyMemberCriteria(ProfileFamilyMemberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.relationship = other.relationship == null ? null : other.relationship.copy();
        this.roleTitle = other.roleTitle == null ? null : other.roleTitle.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfileFamilyMemberCriteria copy() {
        return new ProfileFamilyMemberCriteria(this);
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

    public StringFilter getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getRelationship() {
        return relationship;
    }

    public void setRelationship(StringFilter relationship) {
        this.relationship = relationship;
    }

    public StringFilter getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(StringFilter roleTitle) {
        this.roleTitle = roleTitle;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
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
        ProfileFamilyMemberCriteria that = (ProfileFamilyMemberCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
