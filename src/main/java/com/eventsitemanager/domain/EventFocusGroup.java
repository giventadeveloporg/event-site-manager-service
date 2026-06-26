package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An EventFocusGroup entity linking events with focus groups.
 */
@Entity
@Table(
    name = "event_focus_groups",
    uniqueConstraints = { @UniqueConstraint(name = "uq_event_focus_group", columnNames = { "tenant_id", "event_id", "focus_group_id" }) }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventFocusGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventFocusGroupsSeq")
    @SequenceGenerator(name = "eventFocusGroupsSeq", sequenceName = "public.focus_group_members_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "focus_group_id", nullable = false)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private FocusGroup focusGroup;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventFocusGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventFocusGroup tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public EventFocusGroup eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public EventFocusGroup createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public EventFocusGroup updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public FocusGroup getFocusGroup() {
        return this.focusGroup;
    }

    public void setFocusGroup(FocusGroup focusGroup) {
        this.focusGroup = focusGroup;
    }

    public EventFocusGroup focusGroup(FocusGroup focusGroup) {
        this.setFocusGroup(focusGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventFocusGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((EventFocusGroup) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "EventFocusGroup{" +
        "id=" + getId() +
        ", tenantId='" + getTenantId() + "'" +
        ", eventId=" + getEventId() +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
