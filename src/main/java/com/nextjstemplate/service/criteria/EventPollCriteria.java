package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventPoll} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventPollResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-polls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventPollCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter description;

    private BooleanFilter isActive;

    private BooleanFilter isAnonymous;

    private BooleanFilter allowMultipleChoices;

    private IntegerFilter maxResponsesPerUser;

    private StringFilter resultsVisibleTo;

    private ZonedDateTimeFilter startDate;

    private ZonedDateTimeFilter endDate;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter createdById;

    private Boolean distinct;

    public EventPollCriteria() {}

    public EventPollCriteria(EventPollCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.isAnonymous = other.isAnonymous == null ? null : other.isAnonymous.copy();
        this.allowMultipleChoices = other.allowMultipleChoices == null ? null : other.allowMultipleChoices.copy();
        this.maxResponsesPerUser = other.maxResponsesPerUser == null ? null : other.maxResponsesPerUser.copy();
        this.resultsVisibleTo = other.resultsVisibleTo == null ? null : other.resultsVisibleTo.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventPollCriteria copy() {
        return new EventPollCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter getIsAnonymous() {
        return isAnonymous;
    }

    public BooleanFilter isAnonymous() {
        if (isAnonymous == null) {
            isAnonymous = new BooleanFilter();
        }
        return isAnonymous;
    }

    public void setIsAnonymous(BooleanFilter isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public BooleanFilter getAllowMultipleChoices() {
        return allowMultipleChoices;
    }

    public BooleanFilter allowMultipleChoices() {
        if (allowMultipleChoices == null) {
            allowMultipleChoices = new BooleanFilter();
        }
        return allowMultipleChoices;
    }

    public void setAllowMultipleChoices(BooleanFilter allowMultipleChoices) {
        this.allowMultipleChoices = allowMultipleChoices;
    }

    public IntegerFilter getMaxResponsesPerUser() {
        return maxResponsesPerUser;
    }

    public IntegerFilter maxResponsesPerUser() {
        if (maxResponsesPerUser == null) {
            maxResponsesPerUser = new IntegerFilter();
        }
        return maxResponsesPerUser;
    }

    public void setMaxResponsesPerUser(IntegerFilter maxResponsesPerUser) {
        this.maxResponsesPerUser = maxResponsesPerUser;
    }

    public StringFilter getResultsVisibleTo() {
        return resultsVisibleTo;
    }

    public StringFilter resultsVisibleTo() {
        if (resultsVisibleTo == null) {
            resultsVisibleTo = new StringFilter();
        }
        return resultsVisibleTo;
    }

    public void setResultsVisibleTo(StringFilter resultsVisibleTo) {
        this.resultsVisibleTo = resultsVisibleTo;
    }

    public ZonedDateTimeFilter getStartDate() {
        return startDate;
    }

    public ZonedDateTimeFilter startDate() {
        if (startDate == null) {
            startDate = new ZonedDateTimeFilter();
        }
        return startDate;
    }

    public void setStartDate(ZonedDateTimeFilter startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTimeFilter getEndDate() {
        return endDate;
    }

    public ZonedDateTimeFilter endDate() {
        if (endDate == null) {
            endDate = new ZonedDateTimeFilter();
        }
        return endDate;
    }

    public void setEndDate(ZonedDateTimeFilter endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public Boolean getDistinct() {
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
        final EventPollCriteria that = (EventPollCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isAnonymous, that.isAnonymous) &&
            Objects.equals(allowMultipleChoices, that.allowMultipleChoices) &&
            Objects.equals(maxResponsesPerUser, that.maxResponsesPerUser) &&
            Objects.equals(resultsVisibleTo, that.resultsVisibleTo) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            description,
            isActive,
            isAnonymous,
            allowMultipleChoices,
            maxResponsesPerUser,
            resultsVisibleTo,
            startDate,
            endDate,
            createdAt,
            updatedAt,
            eventId,
            createdById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPollCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (isAnonymous != null ? "isAnonymous=" + isAnonymous + ", " : "") +
            (allowMultipleChoices != null ? "allowMultipleChoices=" + allowMultipleChoices + ", " : "") +
            (maxResponsesPerUser != null ? "maxResponsesPerUser=" + maxResponsesPerUser + ", " : "") +
            (resultsVisibleTo != null ? "resultsVisibleTo=" + resultsVisibleTo + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
