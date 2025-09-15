package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventSponsorsJoin}
 * entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSponsorsJoinCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;
  private ZonedDateTimeFilter createdAt;
  private LongFilter eventId;
  private LongFilter sponsorId;
  private Boolean distinct;

  public EventSponsorsJoinCriteria() {
  }

  public EventSponsorsJoinCriteria(EventSponsorsJoinCriteria other) {
    this.id = other.id == null ? null : other.id.copy();
    this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
    this.eventId = other.eventId == null ? null : other.eventId.copy();
    this.sponsorId = other.sponsorId == null ? null : other.sponsorId.copy();
    this.distinct = other.distinct;
  }

  @Override
  public EventSponsorsJoinCriteria copy() {
    return new EventSponsorsJoinCriteria(this);
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public ZonedDateTimeFilter getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTimeFilter createdAt) {
    this.createdAt = createdAt;
  }

  public LongFilter getEventId() {
    return eventId;
  }

  public void setEventId(LongFilter eventId) {
    this.eventId = eventId;
  }

  public LongFilter getSponsorId() {
    return sponsorId;
  }

  public void setSponsorId(LongFilter sponsorId) {
    this.sponsorId = sponsorId;
  }

  public Boolean getDistinct() {
    return distinct;
  }

  public void setDistinct(Boolean distinct) {
    this.distinct = distinct;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventSponsorsJoinCriteria that = (EventSponsorsJoinCriteria) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(eventId, that.eventId) &&
        Objects.equals(sponsorId, that.sponsorId) &&
        Objects.equals(distinct, that.distinct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdAt, eventId, sponsorId, distinct);
  }

  @Override
  public String toString() {
    return "EventSponsorsJoinCriteria{" +
        (id != null ? "id=" + id + ", " : "") +
        (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
        (eventId != null ? "eventId=" + eventId + ", " : "") +
        (sponsorId != null ? "sponsorId=" + sponsorId + ", " : "") +
        (distinct != null ? "distinct=" + distinct + ", " : "") +
        "}";
  }
}
