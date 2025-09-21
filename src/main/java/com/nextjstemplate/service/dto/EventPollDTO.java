package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventPoll} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventPollDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    private Boolean isActive;

    private Boolean isAnonymous;

    private Boolean allowMultipleChoices;

    private Integer maxResponsesPerUser;

    @Size(max = 50)
    private String resultsVisibleTo;

    @NotNull
    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private UserProfileDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Boolean getAllowMultipleChoices() {
        return allowMultipleChoices;
    }

    public void setAllowMultipleChoices(Boolean allowMultipleChoices) {
        this.allowMultipleChoices = allowMultipleChoices;
    }

    public Integer getMaxResponsesPerUser() {
        return maxResponsesPerUser;
    }

    public void setMaxResponsesPerUser(Integer maxResponsesPerUser) {
        this.maxResponsesPerUser = maxResponsesPerUser;
    }

    public String getResultsVisibleTo() {
        return resultsVisibleTo;
    }

    public void setResultsVisibleTo(String resultsVisibleTo) {
        this.resultsVisibleTo = resultsVisibleTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventPollDTO)) {
            return false;
        }

        EventPollDTO eventPollDTO = (EventPollDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventPollDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPollDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isAnonymous='" + getIsAnonymous() + "'" +
            ", allowMultipleChoices='" + getAllowMultipleChoices() + "'" +
            ", maxResponsesPerUser='" + getMaxResponsesPerUser() + "'" +
            ", resultsVisibleTo='" + getResultsVisibleTo() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
