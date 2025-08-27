package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventLiveUpdate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String updateType;

    private String contentText;

    @Size(max = 1024)
    private String contentImageUrl;

    @Size(max = 1024)
    private String contentVideoUrl;

    @Size(max = 1024)
    private String contentLinkUrl;

    @Size(max = 8192)
    private String metadata;

    private Integer displayOrder;

    private Boolean isDefault;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @NotNull
    private EventDetailsDTO event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContentImageUrl() {
        return contentImageUrl;
    }

    public void setContentImageUrl(String contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public String getContentVideoUrl() {
        return contentVideoUrl;
    }

    public void setContentVideoUrl(String contentVideoUrl) {
        this.contentVideoUrl = contentVideoUrl;
    }

    public String getContentLinkUrl() {
        return contentLinkUrl;
    }

    public void setContentLinkUrl(String contentLinkUrl) {
        this.contentLinkUrl = contentLinkUrl;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLiveUpdateDTO)) {
            return false;
        }

        EventLiveUpdateDTO eventLiveUpdateDTO = (EventLiveUpdateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventLiveUpdateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateDTO{" +
            "id=" + getId() +
            ", updateType='" + getUpdateType() + "'" +
            ", contentText='" + getContentText() + "'" +
            ", contentImageUrl='" + getContentImageUrl() + "'" +
            ", contentVideoUrl='" + getContentVideoUrl() + "'" +
            ", contentLinkUrl='" + getContentLinkUrl() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isDefault='" + getIsDefault() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            "}";
    }
}
