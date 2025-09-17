package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventEmails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventEmailsDTO implements Serializable {

  private Long id;

  @Size(max = 255)
  private String tenantId;

  @NotNull
  @Size(max = 255)
  private String email;

  @NotNull
  private ZonedDateTime createdAt;

  @NotNull
  private ZonedDateTime updatedAt;

  private EventDetailsDTO event;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventEmailsDTO)) {
      return false;
    }

    EventEmailsDTO eventEmailsDTO = (EventEmailsDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, eventEmailsDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventEmailsDTO{" +
        "id=" + getId() +
        ", email='" + getEmail() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        ", event=" + getEvent() +
        "}";
  }
}
