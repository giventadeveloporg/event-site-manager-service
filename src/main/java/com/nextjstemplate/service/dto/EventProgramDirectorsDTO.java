package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventProgramDirectors} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventProgramDirectorsDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(max = 255)
  private String name;

  @Size(max = 1024)
  private String photoUrl;

  private String bio;

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

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
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
    if (!(o instanceof EventProgramDirectorsDTO)) {
      return false;
    }

    EventProgramDirectorsDTO eventProgramDirectorsDTO = (EventProgramDirectorsDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, eventProgramDirectorsDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventProgramDirectorsDTO{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", photoUrl='" + getPhotoUrl() + "'" +
        ", bio='" + getBio() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        ", event=" + getEvent() +
        "}";
  }
}
