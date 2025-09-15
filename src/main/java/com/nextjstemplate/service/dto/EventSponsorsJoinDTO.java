package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventSponsorsJoin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSponsorsJoinDTO implements Serializable {

  private Long id;

  @NotNull
  private ZonedDateTime createdAt;

  private EventDetailsDTO event;

  private EventSponsorsDTO sponsor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public EventDetailsDTO getEvent() {
    return event;
  }

  public void setEvent(EventDetailsDTO event) {
    this.event = event;
  }

  public EventSponsorsDTO getSponsor() {
    return sponsor;
  }

  public void setSponsor(EventSponsorsDTO sponsor) {
    this.sponsor = sponsor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventSponsorsJoinDTO)) {
      return false;
    }

    EventSponsorsJoinDTO eventSponsorsJoinDTO = (EventSponsorsJoinDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, eventSponsorsJoinDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventSponsorsJoinDTO{" +
        "id=" + getId() +
        ", createdAt='" + getCreatedAt() + "'" +
        ", event=" + getEvent() +
        ", sponsor=" + getSponsor() +
        "}";
  }
}
