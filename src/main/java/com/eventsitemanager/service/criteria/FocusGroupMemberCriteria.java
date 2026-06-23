package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.FocusGroupMember}
 * entity. This class is used
 * in {@link com.eventsitemanager.web.rest.FocusGroupMemberResource} to receive
 * all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /focus-group-members?id.greaterThan=5&focusGroupId.equals=123&status.equals=ACTIVE}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FocusGroupMemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter focusGroupId;

    private LongFilter userProfileId;

    private StringFilter role;

    private StringFilter status;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public FocusGroupMemberCriteria() {}

    public FocusGroupMemberCriteria(FocusGroupMemberCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.focusGroupId = other.optionalFocusGroupId().map(LongFilter::copy).orElse(null);
        this.userProfileId = other.optionalUserProfileId().map(LongFilter::copy).orElse(null);
        this.role = other.optionalRole().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FocusGroupMemberCriteria copy() {
        return new FocusGroupMemberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
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

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getFocusGroupId() {
        return focusGroupId;
    }

    public Optional<LongFilter> optionalFocusGroupId() {
        return Optional.ofNullable(focusGroupId);
    }

    public LongFilter focusGroupId() {
        if (focusGroupId == null) {
            setFocusGroupId(new LongFilter());
        }
        return focusGroupId;
    }

    public void setFocusGroupId(LongFilter focusGroupId) {
        this.focusGroupId = focusGroupId;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public Optional<LongFilter> optionalUserProfileId() {
        return Optional.ofNullable(userProfileId);
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            setUserProfileId(new LongFilter());
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public StringFilter getRole() {
        return role;
    }

    public Optional<StringFilter> optionalRole() {
        return Optional.ofNullable(role);
    }

    public StringFilter role() {
        if (role == null) {
            setRole(new StringFilter());
        }
        return role;
    }

    public void setRole(StringFilter role) {
        this.role = role;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FocusGroupMemberCriteria that = (FocusGroupMemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(focusGroupId, that.focusGroupId) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(role, that.role) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, focusGroupId, userProfileId, role, status, createdAt, updatedAt, distinct);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "FocusGroupMemberCriteria{" +
        optionalId().map(f -> "id=" + f + ", ").orElse("") +
        optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
        optionalFocusGroupId().map(f -> "focusGroupId=" + f + ", ").orElse("") +
        optionalUserProfileId().map(f -> "userProfileId=" + f + ", ").orElse("") +
        optionalRole().map(f -> "role=" + f + ", ").orElse("") +
        optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
        optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
        optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
        optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
  }
}
