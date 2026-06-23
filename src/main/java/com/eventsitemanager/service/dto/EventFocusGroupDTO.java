package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventFocusGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventFocusGroupDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long eventId;

    @NotNull
    private Long focusGroupId;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getFocusGroupId() {
        return focusGroupId;
    }

    public void setFocusGroupId(Long focusGroupId) {
        this.focusGroupId = focusGroupId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventFocusGroupDTO)) {
            return false;
        }

        EventFocusGroupDTO eventFocusGroupDTO = (EventFocusGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventFocusGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "EventFocusGroupDTO{" +
        "id=" + getId() +
        ", tenantId='" + getTenantId() + "'" +
        ", eventId=" + getEventId() +
        ", focusGroupId=" + getFocusGroupId() +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
