package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventProgramDirectors.
 */
@Entity
@Table(name = "event_program_directors")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventProgramDirectors implements Serializable {

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

  @Size(max = 1024)
  @Column(name = "photo_url", length = 1024)
  private String photoUrl;

  @Column(name = "bio", columnDefinition = "text")
  private String bio;

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

  public EventProgramDirectors id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public EventProgramDirectors tenantId(String tenantId) {
    this.setTenantId(tenantId);
    return this;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getName() {
    return this.name;
  }

  public EventProgramDirectors name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhotoUrl() {
    return this.photoUrl;
  }

  public EventProgramDirectors photoUrl(String photoUrl) {
    this.setPhotoUrl(photoUrl);
    return this;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getBio() {
    return this.bio;
  }

  public EventProgramDirectors bio(String bio) {
    this.setBio(bio);
    return this;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public ZonedDateTime getCreatedAt() {
    return this.createdAt;
  }

  public EventProgramDirectors createdAt(ZonedDateTime createdAt) {
    this.setCreatedAt(createdAt);
    return this;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public EventProgramDirectors updatedAt(ZonedDateTime updatedAt) {
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

  public EventProgramDirectors event(EventDetails event) {
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
    if (!(o instanceof EventProgramDirectors)) {
      return false;
    }
    return getId() != null && getId().equals(((EventProgramDirectors) o).getId());
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
    return "EventProgramDirectors{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", photoUrl='" + getPhotoUrl() + "'" +
        ", bio='" + getBio() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
