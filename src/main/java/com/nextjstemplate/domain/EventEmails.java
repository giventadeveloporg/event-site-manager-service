package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventEmails.
 */
@Entity
@Table(name = "event_emails")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventEmails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @Size(max = 255)
  @Column(name = "tenant_id", length = 255)
  private String tenantId;

  @NotNull
  @Size(max = 255)
  @Column(name = "email", length = 255, nullable = false)
  private String email;

  @NotNull
  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @NotNull
  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = { "eventFeaturedPerformers", "eventContacts", "eventEmails",
      "eventProgramDirectors" }, allowSetters = true)
  private EventDetails event;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public EventEmails id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public EventEmails tenantId(String tenantId) {
    this.setTenantId(tenantId);
    return this;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getEmail() {
    return this.email;
  }

  public EventEmails email(String email) {
    this.setEmail(email);
    return this;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ZonedDateTime getCreatedAt() {
    return this.createdAt;
  }

  public EventEmails createdAt(ZonedDateTime createdAt) {
    this.setCreatedAt(createdAt);
    return this;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public EventEmails updatedAt(ZonedDateTime updatedAt) {
    this.setUpdatedAt(updatedAt);
    return this;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public EventDetails getEvent() {
    return this.event;
  }

  public void setEvent(EventDetails event) {
    this.event = event;
  }

  public EventEmails event(EventDetails event) {
    this.setEvent(event);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
  // setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventEmails)) {
      return false;
    }
    return getId() != null && getId().equals(((EventEmails) o).getId());
  }

  @Override
  public int hashCode() {
    // see
    // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EventEmails{" +
        "id=" + getId() +
        ", email='" + getEmail() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
