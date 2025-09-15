package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventLiveUpdateAttachment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateAttachmentDTO implements Serializable {

    private Long id;

    @Size(max = 20)
    private String attachmentType;

    @Size(max = 1024)
    private String attachmentUrl;

    private Integer displayOrder;

    @Size(max = 4096)
    private String metadata;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @NotNull
    private EventLiveUpdateDTO liveUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
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

    public EventLiveUpdateDTO getLiveUpdate() {
        return liveUpdate;
    }

    public void setLiveUpdate(EventLiveUpdateDTO liveUpdate) {
        this.liveUpdate = liveUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLiveUpdateAttachmentDTO)) {
            return false;
        }

        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = (EventLiveUpdateAttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventLiveUpdateAttachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateAttachmentDTO{" +
            "id=" + getId() +
            ", attachmentType='" + getAttachmentType() + "'" +
            ", attachmentUrl='" + getAttachmentUrl() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", metadata='" + getMetadata() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", liveUpdate=" + getLiveUpdate() +
            "}";
    }
}
