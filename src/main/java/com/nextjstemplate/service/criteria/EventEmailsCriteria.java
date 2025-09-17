package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventEmails} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventEmailsCriteria implements Serializable, Criteria {

  private static final long serialVersionUID = 1L;

  private LongFilter id;
  private StringFilter tenantId;
  private StringFilter email;
  private ZonedDateTimeFilter createdAt;
  private ZonedDateTimeFilter updatedAt;
  private LongFilter eventId;
  private Boolean distinct;

  public EventEmailsCriteria() {
  }

  public EventEmailsCriteria(EventEmailsCriteria other) {
    this.id = other.id == null ? null : other.id.copy();
    this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
    this.email = other.email == null ? null : other.email.copy();
    this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
    this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
    this.eventId = other.eventId == null ? null : other.eventId.copy();
    this.distinct = other.distinct;
  }

  @Override
  public EventEmailsCriteria copy() {
    return new EventEmailsCriteria(this);
  }

  public LongFilter getId() {
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
    EventEmailsCriteria that = (EventEmailsCriteria) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(email, that.email) &&
        Objects.equals(createdAt, that.createdAt) &&
        Objects.equals(updatedAt, that.updatedAt) &&
        Objects.equals(eventId, that.eventId) &&
        Objects.equals(distinct, that.distinct);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, createdAt, updatedAt, eventId, distinct);
  }

  @Override
  public String toString() {
    return "EventEmailsCriteria{" +
        (id != null ? "id=" + id + ", " : "") +
        (email != null ? "email=" + email + ", " : "") +
        (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
        (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
        (eventId != null ? "eventId=" + eventId + ", " : "") +
        (distinct != null ? "distinct=" + distinct + ", " : "") +
        "}";
  }
}
