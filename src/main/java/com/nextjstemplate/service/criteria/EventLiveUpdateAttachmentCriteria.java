package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventLiveUpdateAttachment} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventLiveUpdateAttachmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-live-update-attachments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateAttachmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter attachmentType;

    private StringFilter attachmentUrl;

    private IntegerFilter displayOrder;

    private StringFilter metadata;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter liveUpdateId;

    private Boolean distinct;

    public EventLiveUpdateAttachmentCriteria() {}

    public EventLiveUpdateAttachmentCriteria(EventLiveUpdateAttachmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.attachmentType = other.optionalAttachmentType().map(StringFilter::copy).orElse(null);
        this.attachmentUrl = other.optionalAttachmentUrl().map(StringFilter::copy).orElse(null);
        this.displayOrder = other.optionalDisplayOrder().map(IntegerFilter::copy).orElse(null);
        this.metadata = other.optionalMetadata().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.liveUpdateId = other.optionalLiveUpdateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventLiveUpdateAttachmentCriteria copy() {
        return new EventLiveUpdateAttachmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAttachmentType() {
        return attachmentType;
    }

    public Optional<StringFilter> optionalAttachmentType() {
        return Optional.ofNullable(attachmentType);
    }

    public StringFilter attachmentType() {
        if (attachmentType == null) {
            setAttachmentType(new StringFilter());
        }
        return attachmentType;
    }

    public void setAttachmentType(StringFilter attachmentType) {
        this.attachmentType = attachmentType;
    }

    public StringFilter getAttachmentUrl() {
        return attachmentUrl;
    }

    public Optional<StringFilter> optionalAttachmentUrl() {
        return Optional.ofNullable(attachmentUrl);
    }

    public StringFilter attachmentUrl() {
        if (attachmentUrl == null) {
            setAttachmentUrl(new StringFilter());
        }
        return attachmentUrl;
    }

    public void setAttachmentUrl(StringFilter attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public Optional<IntegerFilter> optionalDisplayOrder() {
        return Optional.ofNullable(displayOrder);
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            setDisplayOrder(new IntegerFilter());
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public StringFilter getMetadata() {
        return metadata;
    }

    public Optional<StringFilter> optionalMetadata() {
        return Optional.ofNullable(metadata);
    }

    public StringFilter metadata() {
        if (metadata == null) {
            setMetadata(new StringFilter());
        }
        return metadata;
    }

    public void setMetadata(StringFilter metadata) {
        this.metadata = metadata;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getLiveUpdateId() {
        return liveUpdateId;
    }

    public Optional<LongFilter> optionalLiveUpdateId() {
        return Optional.ofNullable(liveUpdateId);
    }

    public LongFilter liveUpdateId() {
        if (liveUpdateId == null) {
            setLiveUpdateId(new LongFilter());
        }
        return liveUpdateId;
    }

    public void setLiveUpdateId(LongFilter liveUpdateId) {
        this.liveUpdateId = liveUpdateId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventLiveUpdateAttachmentCriteria that = (EventLiveUpdateAttachmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(attachmentType, that.attachmentType) &&
            Objects.equals(attachmentUrl, that.attachmentUrl) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(metadata, that.metadata) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(liveUpdateId, that.liveUpdateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attachmentType, attachmentUrl, displayOrder, metadata, createdAt, updatedAt, liveUpdateId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateAttachmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAttachmentType().map(f -> "attachmentType=" + f + ", ").orElse("") +
            optionalAttachmentUrl().map(f -> "attachmentUrl=" + f + ", ").orElse("") +
            optionalDisplayOrder().map(f -> "displayOrder=" + f + ", ").orElse("") +
            optionalMetadata().map(f -> "metadata=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalLiveUpdateId().map(f -> "liveUpdateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
