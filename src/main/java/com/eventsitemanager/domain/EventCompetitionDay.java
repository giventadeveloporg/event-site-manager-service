package com.eventsitemanager.domain;

import com.eventsitemanager.domain.EventDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionDay.
 */
@Entity
@Table(name = "event_competition_day")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id")
    private String tenantId;

    @NotNull
    @Size(max = 100)
    @Column(name = "day_label")
    private String dayLabel;

    @NotNull
    @Column(name = "event_date")
    private LocalDate eventDate;

    @NotNull
    @Size(max = 255)
    @Column(name = "venue_name")
    private String venueName;

    @Size(max = 500)
    @Column(name = "venue_address")
    private String venueAddress;

    @NotNull
    @Column(name = "sort_order")
    private Integer sortOrder;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionDay tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDayLabel() {
        return this.dayLabel;
    }

    public EventCompetitionDay dayLabel(String dayLabel) {
        this.setDayLabel(dayLabel);
        return this;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public LocalDate getEventDate() {
        return this.eventDate;
    }

    public EventCompetitionDay eventDate(LocalDate eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getVenueName() {
        return this.venueName;
    }

    public EventCompetitionDay venueName(String venueName) {
        this.setVenueName(venueName);
        return this;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return this.venueAddress;
    }

    public EventCompetitionDay venueAddress(String venueAddress) {
        this.setVenueAddress(venueAddress);
        return this;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public EventCompetitionDay sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getNotes() {
        return this.notes;
    }

    public EventCompetitionDay notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionDay createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetitionDay updatedAt(ZonedDateTime updatedAt) {
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

    public EventCompetitionDay event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionDay id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionDay)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionDay) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
