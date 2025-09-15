package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventContacts}
 * entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContactsCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;
  private StringFilter name;
  private StringFilter phone;
  private StringFilter email;
  private ZonedDateTimeFilter createdAt;
  private ZonedDateTimeFilter updatedAt;
  private LongFilter eventId;
  private Boolean distinct;

  public EventContactsCriteria() {
  }

  public EventContactsCriteria(EventContactsCriteria other) {
    this.id = other.id == null ? null : other.id.copy();
    this.name = other.name == null ? null : other.name.copy();
    this.phone = other.phone == null ? null : other.phone.copy();
    this.email = other.email == null ? null : other.email.copy();
    this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
    this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
    this.eventId = other.eventId == null ? null : other.eventId.copy();
    this.distinct = other.distinct;
  }

  @Override
  public EventContactsCriteria copy() {
    return new EventContactsCriteria(this);
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

  public StringFilter getPhone() {
    return phone;
  }

  public void setPhone(StringFilter phone) {
    this.phone = phone;
  }

  public StringFilter getEmail() {
    return email;
  }

  public void setEmail(StringFilter email) {
    this.email = email;
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
    EventContactsCriteria that = (EventContactsCriteria) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(phone, that.phone) &&
        Objects.equals(email, that.email) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(eventId, that.eventId) &&
        Objects.equals(distinct, that.distinct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, phone, email, createdAt, updatedAt, eventId, distinct);
  }

  @Override
  public String toString() {
    return "EventContactsCriteria{" +
        (id != null ? "id=" + id + ", " : "") +
        (name != null ? "name=" + name + ", " : "") +
        (phone != null ? "phone=" + phone + ", " : "") +
        (email != null ? "email=" + email + ", " : "") +
        (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
        (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
        (eventId != null ? "eventId=" + eventId + ", " : "") +
        (distinct != null ? "distinct=" + distinct + ", " : "") +
        "}";
  }
}
