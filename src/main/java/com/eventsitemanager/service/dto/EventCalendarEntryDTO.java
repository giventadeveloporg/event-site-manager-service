package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventCalendarEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCalendarEntryDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String calendarProvider;

    @Size(max = 255)
    private String externalEventId;

    @NotNull
    @Size(max = 255)
    private String calendarLink;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private UserProfileDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCalendarProvider() {
        return calendarProvider;
    }

    public void setCalendarProvider(String calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    public String getExternalEventId() {
        return externalEventId;
    }

    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }

    public String getCalendarLink() {
        return calendarLink;
    }

    public void setCalendarLink(String calendarLink) {
        this.calendarLink = calendarLink;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCalendarEntryDTO)) {
            return false;
        }

        EventCalendarEntryDTO eventCalendarEntryDTO = (EventCalendarEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventCalendarEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCalendarEntryDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", calendarProvider='" + getCalendarProvider() + "'" +
            ", externalEventId='" + getExternalEventId() + "'" +
            ", calendarLink='" + getCalendarLink() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
