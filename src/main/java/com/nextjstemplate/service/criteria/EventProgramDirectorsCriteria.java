package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the
 * {@link com.nextjstemplate.domain.EventProgramDirectors} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventProgramDirectorsCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;
  private StringFilter name;
  private StringFilter photoUrl;
  private StringFilter bio;
  private ZonedDateTimeFilter createdAt;
  private ZonedDateTimeFilter updatedAt;
  private LongFilter eventId;
  private Boolean distinct;

  public EventProgramDirectorsCriteria() {
  }

  public EventProgramDirectorsCriteria(EventProgramDirectorsCriteria other) {
    this.id = other.id == null ? null : other.id.copy();
    this.name = other.name == null ? null : other.name.copy();
    this.photoUrl = other.photoUrl == null ? null : other.photoUrl.copy();
    this.bio = other.bio == null ? null : other.bio.copy();
    this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
    this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
    this.eventId = other.eventId == null ? null : other.eventId.copy();
    this.distinct = other.distinct;
  }

  @Override
  public EventProgramDirectorsCriteria copy() {
    return new EventProgramDirectorsCriteria(this);
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getName() {
    return name;
  }

  public void setName(StringFilter name) {
    this.name = name;
  }

  public StringFilter getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(StringFilter photoUrl) {
    this.photoUrl = photoUrl;
  }

  public StringFilter getBio() {
    return bio;
  }

  public void setBio(StringFilter bio) {
    this.bio = bio;
  }

  public ZonedDateTimeFilter getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTimeFilter createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTimeFilter getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LongFilter getEventId() {
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
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventProgramDirectorsCriteria that = (EventProgramDirectorsCriteria) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(photoUrl, that.photoUrl) &&
        Objects.equals(bio, that.bio) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(eventId, that.eventId) &&
        Objects.equals(distinct, that.distinct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, photoUrl, bio, createdAt, updatedAt, eventId, distinct);
  }

  @Override
  public String toString() {
    return "EventProgramDirectorsCriteria{" +
        (id != null ? "id=" + id + ", " : "") +
        (name != null ? "name=" + name + ", " : "") +
        (photoUrl != null ? "photoUrl=" + photoUrl + ", " : "") +
        (bio != null ? "bio=" + bio + ", " : "") +
        (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
        (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
        (eventId != null ? "eventId=" + eventId + ", " : "") +
        (distinct != null ? "distinct=" + distinct + ", " : "") +
        "}";
  }
}
