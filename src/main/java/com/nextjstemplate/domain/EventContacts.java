package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventContacts.
 */
@Entity
@Table(name = "event_contacts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContacts implements Serializable {

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
  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @NotNull
  @Size(max = 50)
  @Column(name = "phone", length = 50, nullable = false)
  private String phone;

  @Size(max = 255)
  @Column(name = "email", length = 255)
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

  public EventContacts id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public EventContacts tenantId(String tenantId) {
    this.setTenantId(tenantId);
    return this;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getName() {
    return this.name;
  }

  public EventContacts name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return this.phone;
  }

  public EventContacts phone(String phone) {
    this.setPhone(phone);
    return this;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return this.email;
  }

  public EventContacts email(String email) {
    this.setEmail(email);
    return this;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ZonedDateTime getCreatedAt() {
    return this.createdAt;
  }

  public EventContacts createdAt(ZonedDateTime createdAt) {
    this.setCreatedAt(createdAt);
    return this;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public EventContacts updatedAt(ZonedDateTime updatedAt) {
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

  public EventContacts event(EventDetails event) {
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
    if (!(o instanceof EventContacts)) {
      return false;
    }
    return getId() != null && getId().equals(((EventContacts) o).getId());
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
    return "EventContacts{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", phone='" + getPhone() + "'" +
        ", email='" + getEmail() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
