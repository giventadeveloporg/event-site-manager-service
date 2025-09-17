package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventSponsorsJoin.
 */
@Entity
@Table(name = "event_sponsors_join")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSponsorsJoin implements Serializable {

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
  @Column(name = "created_at", nullable = false)
  private ZonedDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = { "eventFeaturedPerformers", "eventContacts", "eventEmails",
      "eventProgramDirectors" }, allowSetters = true)
  private EventDetails event;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = { "eventSponsorsJoins" }, allowSetters = true)
  private EventSponsors sponsor;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public EventSponsorsJoin id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenantId() {
    return this.tenantId;
  }

  public EventSponsorsJoin tenantId(String tenantId) {
    this.setTenantId(tenantId);
    return this;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ZonedDateTime getCreatedAt() {
    return this.createdAt;
  }

  public EventSponsorsJoin createdAt(ZonedDateTime createdAt) {
    this.setCreatedAt(createdAt);
    return this;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public EventDetails getEvent() {
    return this.event;
  }

  public void setEvent(EventDetails event) {
    this.event = event;
  }

  public EventSponsorsJoin event(EventDetails event) {
    this.setEvent(event);
    return this;
  }

  public EventSponsors getSponsor() {
    return this.sponsor;
  }

  public void setSponsor(EventSponsors sponsor) {
    this.sponsor = sponsor;
  }

  public EventSponsorsJoin sponsor(EventSponsors sponsor) {
    this.setSponsor(sponsor);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
  // setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventSponsorsJoin)) {
      return false;
    }
    return getId() != null && getId().equals(((EventSponsorsJoin) o).getId());
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
    return "EventSponsorsJoin{" +
        "id=" + getId() +
        ", createdAt='" + getCreatedAt() + "'" +
        "}";
  }
}
