package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventFocusGroup}
 * entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventFocusGroupResource} to receive all
 * the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-focus-groups?id.greaterThan=5&eventId.equals=456&focusGroupId.equals=123}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventFocusGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter eventId;

    private LongFilter focusGroupId;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public EventFocusGroupCriteria() {}

    public EventFocusGroupCriteria(EventFocusGroupCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.eventId = other.optionalEventId().map(LongFilter::copy).orElse(null);
        this.focusGroupId = other.optionalFocusGroupId().map(LongFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventFocusGroupCriteria copy() {
        return new EventFocusGroupCriteria(this);
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

    public LongFilter getEventId() {
        return eventId;
    }

    public Optional<LongFilter> optionalEventId() {
        return Optional.ofNullable(eventId);
    }

    public LongFilter eventId() {
        if (eventId == null) {
            setEventId(new LongFilter());
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
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
        final EventFocusGroupCriteria that = (EventFocusGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(focusGroupId, that.focusGroupId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, eventId, focusGroupId, createdAt, updatedAt, distinct);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "EventFocusGroupCriteria{" +
        optionalId().map(f -> "id=" + f + ", ").orElse("") +
        optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
        optionalEventId().map(f -> "eventId=" + f + ", ").orElse("") +
        optionalFocusGroupId().map(f -> "focusGroupId=" + f + ", ").orElse("") +
        optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
        optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
        optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
  }
}
