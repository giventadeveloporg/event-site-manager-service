package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventPollResponse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventPollResponseDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @Size(max = 1000)
    private String responseValue;

    private Boolean isAnonymous;

    @Size(max = 255)
    private String comment;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventPollDTO poll;

    private EventPollOptionDTO pollOption;

    private UserProfileDTO user;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public EventPollDTO getPoll() {
        return poll;
    }

    public void setPoll(EventPollDTO poll) {
        this.poll = poll;
    }

    public EventPollOptionDTO getPollOption() {
        return pollOption;
    }

    public void setPollOption(EventPollOptionDTO pollOption) {
        this.pollOption = pollOption;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    public String getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(String responseValue) {
        this.responseValue = responseValue;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventPollResponseDTO)) {
            return false;
        }

        EventPollResponseDTO eventPollResponseDTO = (EventPollResponseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventPollResponseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPollResponseDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", responseValue='" + getResponseValue() + "'" +
            ", isAnonymous='" + getIsAnonymous() + "'" +
            ", comment='" + getComment() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", poll=" + getPoll() +
            ", pollOption=" + getPollOption() +
            ", user=" + getUser() +
            "}";
    }
}
