package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventCompetitionDay} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionDayCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter dayLabel;

    private LocalDateFilter eventDate;

    private StringFilter venueName;

    private StringFilter venueAddress;

    private IntegerFilter sortOrder;

    private StringFilter notes;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventCompetitionDayCriteria() {}

    public EventCompetitionDayCriteria(EventCompetitionDayCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.dayLabel = other.dayLabel == null ? null : other.dayLabel.copy();
        this.eventDate = other.eventDate == null ? null : other.eventDate.copy();
        this.venueName = other.venueName == null ? null : other.venueName.copy();
        this.venueAddress = other.venueAddress == null ? null : other.venueAddress.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionDayCriteria copy() {
        return new EventCompetitionDayCriteria(this);
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

    public StringFilter getDayLabel() {
        return dayLabel;
    }

    public StringFilter dayLabel() {
        if (dayLabel == null) {
            dayLabel = new StringFilter();
        }
        return dayLabel;
    }

    public void setDayLabel(StringFilter dayLabel) {
        this.dayLabel = dayLabel;
    }

    public LocalDateFilter getEventDate() {
        return eventDate;
    }

    public LocalDateFilter eventDate() {
        if (eventDate == null) {
            eventDate = new LocalDateFilter();
        }
        return eventDate;
    }

    public void setEventDate(LocalDateFilter eventDate) {
        this.eventDate = eventDate;
    }

    public StringFilter getVenueName() {
        return venueName;
    }

    public StringFilter venueName() {
        if (venueName == null) {
            venueName = new StringFilter();
        }
        return venueName;
    }

    public void setVenueName(StringFilter venueName) {
        this.venueName = venueName;
    }

    public StringFilter getVenueAddress() {
        return venueAddress;
    }

    public StringFilter venueAddress() {
        if (venueAddress == null) {
            venueAddress = new StringFilter();
        }
        return venueAddress;
    }

    public void setVenueAddress(StringFilter venueAddress) {
        this.venueAddress = venueAddress;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            sortOrder = new IntegerFilter();
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
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
        final EventCompetitionDayCriteria that = (EventCompetitionDayCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
