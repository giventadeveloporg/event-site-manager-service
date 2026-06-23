package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventRecurrenceSeries.
 * Stores recurrence configuration for recurring events.
 * Note: weekly_days is INTEGER[] (PostgreSQL array), different from event_details.recurrence_weekly_days which is TEXT.
 */
@Entity
@Table(name = "event_recurrence_series")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventRecurrenceSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_event_id", nullable = false)
    @JsonIgnoreProperties(value = { "parentEvent", "childEvents", "createdBy", "eventType" }, allowSetters = true)
    private EventDetails parentEvent;

    @NotNull
    @Size(max = 50)
    @Column(name = "pattern", length = 50, nullable = false)
    private String pattern;

    @NotNull
    @Column(name = "interval", nullable = false)
    private Integer interval;

    @NotNull
    @Size(max = 20)
    @Column(name = "end_type", length = 20, nullable = false)
    private String endType;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "occurrences")
    private Integer occurrences;

    @Column(name = "weekly_days", columnDefinition = "integer[]")
    private Integer[] weeklyDays;

    @Column(name = "monthly_day")
    private Integer monthlyDay;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventRecurrenceSeries id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventRecurrenceSeries tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public EventDetails getParentEvent() {
        return this.parentEvent;
    }

    public EventRecurrenceSeries parentEvent(EventDetails eventDetails) {
        this.setParentEvent(eventDetails);
        return this;
    }

    public void setParentEvent(EventDetails eventDetails) {
        this.parentEvent = eventDetails;
    }

    public String getPattern() {
        return this.pattern;
    }

    public EventRecurrenceSeries pattern(String pattern) {
        this.setPattern(pattern);
        return this;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getInterval() {
        return this.interval;
    }

    public EventRecurrenceSeries interval(Integer interval) {
        this.setInterval(interval);
        return this;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getEndType() {
        return this.endType;
    }

    public EventRecurrenceSeries endType(String endType) {
        this.setEndType(endType);
        return this;
    }

    public void setEndType(String endType) {
        this.endType = endType;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public EventRecurrenceSeries endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getOccurrences() {
        return this.occurrences;
    }

    public EventRecurrenceSeries occurrences(Integer occurrences) {
        this.setOccurrences(occurrences);
        return this;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public Integer[] getWeeklyDays() {
        return this.weeklyDays;
    }

    public EventRecurrenceSeries weeklyDays(Integer[] weeklyDays) {
        this.setWeeklyDays(weeklyDays);
        return this;
    }

    public void setWeeklyDays(Integer[] weeklyDays) {
        this.weeklyDays = weeklyDays;
    }

    public Integer getMonthlyDay() {
        return this.monthlyDay;
    }

    public EventRecurrenceSeries monthlyDay(Integer monthlyDay) {
        this.setMonthlyDay(monthlyDay);
        return this;
    }

    public void setMonthlyDay(Integer monthlyDay) {
        this.monthlyDay = monthlyDay;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventRecurrenceSeries createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventRecurrenceSeries updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventRecurrenceSeries)) {
            return false;
        }
        return getId() != null && getId().equals(((EventRecurrenceSeries) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-jpa-entity-identifiers/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "EventRecurrenceSeries{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", pattern='" +
            getPattern() +
            "'" +
            ", interval=" +
            getInterval() +
            ", endType='" +
            getEndType() +
            "'" +
            ", endDate=" +
            getEndDate() +
            ", occurrences=" +
            getOccurrences() +
            ", monthlyDay=" +
            getMonthlyDay() +
            ", createdAt=" +
            getCreatedAt() +
            ", updatedAt=" +
            getUpdatedAt() +
            "}"
        );
    }
}
