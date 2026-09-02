package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A timed program item on an event day (Onam-style agenda).
 */
@Entity
@Table(name = "event_agenda_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAgendaItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventAgendaItemSeq")
    @SequenceGenerator(name = "eventAgendaItemSeq", sequenceName = "public.event_agenda_item_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @NotNull
    @Size(max = 100)
    @Column(name = "start_time", length = 100, nullable = false)
    private String startTime;

    @Size(max = 100)
    @Column(name = "end_time", length = 100)
    private String endTime;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 1024)
    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @NotNull
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotNull
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = Boolean.TRUE;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnoreProperties(
        value = { "eventFeaturedPerformers", "eventContacts", "eventEmails", "eventProgramDirectors", "eventAgendaItems" },
        allowSetters = true
    )
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_media_id")
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private EventMedia eventMedia;

    public Long getId() {
        return this.id;
    }

    public EventAgendaItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventAgendaItem tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDate getScheduleDate() {
        return this.scheduleDate;
    }

    public EventAgendaItem scheduleDate(LocalDate scheduleDate) {
        this.setScheduleDate(scheduleDate);
        return this;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public EventAgendaItem startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public EventAgendaItem endTime(String endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return this.title;
    }

    public EventAgendaItem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public EventAgendaItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public EventAgendaItem imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public EventAgendaItem sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsPublished() {
        return this.isPublished;
    }

    public EventAgendaItem isPublished(Boolean isPublished) {
        this.setIsPublished(isPublished);
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventAgendaItem createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventAgendaItem updatedAt(ZonedDateTime updatedAt) {
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

    public EventAgendaItem event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public EventMedia getEventMedia() {
        return this.eventMedia;
    }

    public void setEventMedia(EventMedia eventMedia) {
        this.eventMedia = eventMedia;
    }

    public EventAgendaItem eventMedia(EventMedia eventMedia) {
        this.setEventMedia(eventMedia);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAgendaItem)) {
            return false;
        }
        return getId() != null && getId().equals(((EventAgendaItem) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EventAgendaItem{" + "id=" + getId() + ", title='" + getTitle() + "'" + ", startTime='" + getStartTime() + "'" + "}";
    }
}
