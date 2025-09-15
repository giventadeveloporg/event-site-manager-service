package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventLiveUpdate.
 */
@Entity
@Table(name = "event_live_update")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "update_type", length = 20, nullable = false)
    private String updateType;

    @Column(name = "content_text")
    private String contentText;

    @Size(max = 1024)
    @Column(name = "content_image_url", length = 1024)
    private String contentImageUrl;

    @Size(max = 1024)
    @Column(name = "content_video_url", length = 1024)
    private String contentVideoUrl;

    @Size(max = 1024)
    @Column(name = "content_link_url", length = 1024)
    private String contentLinkUrl;

    @Size(max = 8192)
    @Column(name = "metadata", length = 8192)
    private String metadata;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_default")
    private Boolean isDefault;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventLiveUpdate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUpdateType() {
        return this.updateType;
    }

    public EventLiveUpdate updateType(String updateType) {
        this.setUpdateType(updateType);
        return this;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getContentText() {
        return this.contentText;
    }

    public EventLiveUpdate contentText(String contentText) {
        this.setContentText(contentText);
        return this;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContentImageUrl() {
        return this.contentImageUrl;
    }

    public EventLiveUpdate contentImageUrl(String contentImageUrl) {
        this.setContentImageUrl(contentImageUrl);
        return this;
    }

    public void setContentImageUrl(String contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public String getContentVideoUrl() {
        return this.contentVideoUrl;
    }

    public EventLiveUpdate contentVideoUrl(String contentVideoUrl) {
        this.setContentVideoUrl(contentVideoUrl);
        return this;
    }

    public void setContentVideoUrl(String contentVideoUrl) {
        this.contentVideoUrl = contentVideoUrl;
    }

    public String getContentLinkUrl() {
        return this.contentLinkUrl;
    }

    public EventLiveUpdate contentLinkUrl(String contentLinkUrl) {
        this.setContentLinkUrl(contentLinkUrl);
        return this;
    }

    public void setContentLinkUrl(String contentLinkUrl) {
        this.contentLinkUrl = contentLinkUrl;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public EventLiveUpdate metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public EventLiveUpdate displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public EventLiveUpdate isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventLiveUpdate createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventLiveUpdate updatedAt(ZonedDateTime updatedAt) {
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

    public EventLiveUpdate event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLiveUpdate)) {
            return false;
        }
        return getId() != null && getId().equals(((EventLiveUpdate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdate{" +
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
            "}";
    }
}
