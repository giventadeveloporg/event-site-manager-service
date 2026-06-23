package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.EventOrganizer} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.EventOrganizerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-organizers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventOrganizerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter designation;

    private StringFilter contactEmail;

    private StringFilter contactPhone;

    private BooleanFilter isPrimary;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter organizerId;

    private Boolean distinct;

    public EventOrganizerCriteria() {}

    public EventOrganizerCriteria(EventOrganizerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.designation = other.designation == null ? null : other.designation.copy();
        this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
        this.contactPhone = other.contactPhone == null ? null : other.contactPhone.copy();
        this.isPrimary = other.isPrimary == null ? null : other.isPrimary.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.organizerId = other.organizerId == null ? null : other.organizerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventOrganizerCriteria copy() {
        return new EventOrganizerCriteria(this);
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

    public StringFilter getDesignation() {
        return designation;
    }

    public StringFilter designation() {
        if (designation == null) {
            designation = new StringFilter();
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public StringFilter contactEmail() {
        if (contactEmail == null) {
            contactEmail = new StringFilter();
        }
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public StringFilter getContactPhone() {
        return contactPhone;
    }

    public StringFilter contactPhone() {
        if (contactPhone == null) {
            contactPhone = new StringFilter();
        }
        return contactPhone;
    }

    public void setContactPhone(StringFilter contactPhone) {
        this.contactPhone = contactPhone;
    }

    public BooleanFilter getIsPrimary() {
        return isPrimary;
    }

    public BooleanFilter isPrimary() {
        if (isPrimary == null) {
            isPrimary = new BooleanFilter();
        }
        return isPrimary;
    }

    public void setIsPrimary(BooleanFilter isPrimary) {
        this.isPrimary = isPrimary;
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

    public LongFilter getOrganizerId() {
        return organizerId;
    }

    public LongFilter organizerId() {
        if (organizerId == null) {
            organizerId = new LongFilter();
        }
        return organizerId;
    }

    public void setOrganizerId(LongFilter organizerId) {
        this.organizerId = organizerId;
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
        final EventOrganizerCriteria that = (EventOrganizerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(contactPhone, that.contactPhone) &&
            Objects.equals(isPrimary, that.isPrimary) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(organizerId, that.organizerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            designation,
            contactEmail,
            contactPhone,
            isPrimary,
            createdAt,
            updatedAt,
            eventId,
            organizerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventOrganizerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (designation != null ? "designation=" + designation + ", " : "") +
            (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
            (contactPhone != null ? "contactPhone=" + contactPhone + ", " : "") +
            (isPrimary != null ? "isPrimary=" + isPrimary + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (organizerId != null ? "organizerId=" + organizerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
