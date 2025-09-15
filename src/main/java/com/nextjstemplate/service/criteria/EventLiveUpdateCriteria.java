package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventLiveUpdate} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventLiveUpdateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-live-updates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLiveUpdateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter updateType;

    private StringFilter contentText;

    private StringFilter contentImageUrl;

    private StringFilter contentVideoUrl;

    private StringFilter contentLinkUrl;

    private StringFilter metadata;

    private IntegerFilter displayOrder;

    private BooleanFilter isDefault;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventLiveUpdateCriteria() {}

    public EventLiveUpdateCriteria(EventLiveUpdateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.updateType = other.optionalUpdateType().map(StringFilter::copy).orElse(null);
        this.contentText = other.optionalContentText().map(StringFilter::copy).orElse(null);
        this.contentImageUrl = other.optionalContentImageUrl().map(StringFilter::copy).orElse(null);
        this.contentVideoUrl = other.optionalContentVideoUrl().map(StringFilter::copy).orElse(null);
        this.contentLinkUrl = other.optionalContentLinkUrl().map(StringFilter::copy).orElse(null);
        this.metadata = other.optionalMetadata().map(StringFilter::copy).orElse(null);
        this.displayOrder = other.optionalDisplayOrder().map(IntegerFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.eventId = other.optionalEventId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventLiveUpdateCriteria copy() {
        return new EventLiveUpdateCriteria(this);
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

    public StringFilter getUpdateType() {
        return updateType;
    }

    public Optional<StringFilter> optionalUpdateType() {
        return Optional.ofNullable(updateType);
    }

    public StringFilter updateType() {
        if (updateType == null) {
            setUpdateType(new StringFilter());
        }
        return updateType;
    }

    public void setUpdateType(StringFilter updateType) {
        this.updateType = updateType;
    }

    public StringFilter getContentText() {
        return contentText;
    }

    public Optional<StringFilter> optionalContentText() {
        return Optional.ofNullable(contentText);
    }

    public StringFilter contentText() {
        if (contentText == null) {
            setContentText(new StringFilter());
        }
        return contentText;
    }

    public void setContentText(StringFilter contentText) {
        this.contentText = contentText;
    }

    public StringFilter getContentImageUrl() {
        return contentImageUrl;
    }

    public Optional<StringFilter> optionalContentImageUrl() {
        return Optional.ofNullable(contentImageUrl);
    }

    public StringFilter contentImageUrl() {
        if (contentImageUrl == null) {
            setContentImageUrl(new StringFilter());
        }
        return contentImageUrl;
    }

    public void setContentImageUrl(StringFilter contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public StringFilter getContentVideoUrl() {
        return contentVideoUrl;
    }

    public Optional<StringFilter> optionalContentVideoUrl() {
        return Optional.ofNullable(contentVideoUrl);
    }

    public StringFilter contentVideoUrl() {
        if (contentVideoUrl == null) {
            setContentVideoUrl(new StringFilter());
        }
        return contentVideoUrl;
    }

    public void setContentVideoUrl(StringFilter contentVideoUrl) {
        this.contentVideoUrl = contentVideoUrl;
    }

    public StringFilter getContentLinkUrl() {
        return contentLinkUrl;
    }

    public Optional<StringFilter> optionalContentLinkUrl() {
        return Optional.ofNullable(contentLinkUrl);
    }

    public StringFilter contentLinkUrl() {
        if (contentLinkUrl == null) {
            setContentLinkUrl(new StringFilter());
        }
        return contentLinkUrl;
    }

    public void setContentLinkUrl(StringFilter contentLinkUrl) {
        this.contentLinkUrl = contentLinkUrl;
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

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
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

    public LongFilter getEventId() {
        return eventId;
    }

    public Optional<LongFilter> optionalEventId() {
        return Optional.ofNullable(eventId);
    }

    public LongFilter eventId() {
        if (eventId == null) {
            setEventId(new LongFilter());
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
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
        final EventLiveUpdateCriteria that = (EventLiveUpdateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(updateType, that.updateType) &&
            Objects.equals(contentText, that.contentText) &&
            Objects.equals(contentImageUrl, that.contentImageUrl) &&
            Objects.equals(contentVideoUrl, that.contentVideoUrl) &&
            Objects.equals(contentLinkUrl, that.contentLinkUrl) &&
            Objects.equals(metadata, that.metadata) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            updateType,
            contentText,
            contentImageUrl,
            contentVideoUrl,
            contentLinkUrl,
            metadata,
            displayOrder,
            isDefault,
            createdAt,
            updatedAt,
            eventId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLiveUpdateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUpdateType().map(f -> "updateType=" + f + ", ").orElse("") +
            optionalContentText().map(f -> "contentText=" + f + ", ").orElse("") +
            optionalContentImageUrl().map(f -> "contentImageUrl=" + f + ", ").orElse("") +
            optionalContentVideoUrl().map(f -> "contentVideoUrl=" + f + ", ").orElse("") +
            optionalContentLinkUrl().map(f -> "contentLinkUrl=" + f + ", ").orElse("") +
            optionalMetadata().map(f -> "metadata=" + f + ", ").orElse("") +
            optionalDisplayOrder().map(f -> "displayOrder=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalEventId().map(f -> "eventId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
