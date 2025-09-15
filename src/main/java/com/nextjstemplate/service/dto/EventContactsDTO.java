package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventContacts} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContactsDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(max = 255)
  private String name;

  @NotNull
  @Size(max = 50)
  private String phone;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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
    if (!(o instanceof EventContactsDTO)) {
      return false;
    }

    EventContactsDTO eventContactsDTO = (EventContactsDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, eventContactsDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventContactsDTO{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", phone='" + getPhone() + "'" +
        ", email='" + getEmail() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        ", event=" + getEvent() +
        "}";
  }
}
