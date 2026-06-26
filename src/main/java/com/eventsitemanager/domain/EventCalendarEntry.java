package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCalendarEntry.
 */
@Entity
@Table(name = "event_calendar_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCalendarEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventCalendarEntrySeq")
    @SequenceGenerator(name = "eventCalendarEntrySeq", sequenceName = "public.event_calendar_entry_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "calendar_provider", length = 255, nullable = false)
    private String calendarProvider;

    @Size(max = 255)
    @Column(name = "external_event_id", length = 255)
    private String externalEventId;

    @NotNull
    @Size(max = 255)
    @Column(name = "calendar_link", length = 255, nullable = false)
    private String calendarLink;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType", "discountCodes" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventCalendarEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCalendarEntry tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCalendarProvider() {
        return this.calendarProvider;
    }

    public EventCalendarEntry calendarProvider(String calendarProvider) {
        this.setCalendarProvider(calendarProvider);
        return this;
    }

    public void setCalendarProvider(String calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    public String getExternalEventId() {
        return this.externalEventId;
    }

    public EventCalendarEntry externalEventId(String externalEventId) {
        this.setExternalEventId(externalEventId);
        return this;
    }

    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }

    public String getCalendarLink() {
        return this.calendarLink;
    }

    public EventCalendarEntry calendarLink(String calendarLink) {
        this.setCalendarLink(calendarLink);
        return this;
    }

    public void setCalendarLink(String calendarLink) {
        this.calendarLink = calendarLink;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCalendarEntry createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCalendarEntry updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public EventCalendarEntry event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public UserProfile getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserProfile userProfile) {
        this.createdBy = userProfile;
    }

    public EventCalendarEntry createdBy(UserProfile userProfile) {
        this.setCreatedBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCalendarEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCalendarEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCalendarEntry{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", calendarProvider='" + getCalendarProvider() + "'" +
            ", externalEventId='" + getExternalEventId() + "'" +
            ", calendarLink='" + getCalendarLink() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
