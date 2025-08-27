package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventLiveUpdateAttachment.
 */
@Entity
@Table(name = "event_live_update_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 20)
    @Column(name = "attachment_type", length = 20)
    private String attachmentType;

    @Size(max = 1024)
    @Column(name = "attachment_url", length = 1024)
    private String attachmentUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Size(max = 4096)
    @Column(name = "metadata", length = 4096)
    private String metadata;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private EventLiveUpdate liveUpdate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventLiveUpdateAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentType() {
        return this.attachmentType;
    }

    public EventLiveUpdateAttachment attachmentType(String attachmentType) {
        this.setAttachmentType(attachmentType);
        return this;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentUrl() {
        return this.attachmentUrl;
    }

    public EventLiveUpdateAttachment attachmentUrl(String attachmentUrl) {
        this.setAttachmentUrl(attachmentUrl);
        return this;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public EventLiveUpdateAttachment displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public EventLiveUpdateAttachment metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventLiveUpdateAttachment createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventLiveUpdateAttachment updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventLiveUpdate getLiveUpdate() {
        return this.liveUpdate;
    }

    public void setLiveUpdate(EventLiveUpdate eventLiveUpdate) {
        this.liveUpdate = eventLiveUpdate;
    }

    public EventLiveUpdateAttachment liveUpdate(EventLiveUpdate eventLiveUpdate) {
        this.setLiveUpdate(eventLiveUpdate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLiveUpdateAttachment)) {
            return false;
        }
        return getId() != null && getId().equals(((EventLiveUpdateAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateAttachment{" +
            "id=" + getId() +
            ", attachmentType='" + getAttachmentType() + "'" +
            ", attachmentUrl='" + getAttachmentUrl() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", metadata='" + getMetadata() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
