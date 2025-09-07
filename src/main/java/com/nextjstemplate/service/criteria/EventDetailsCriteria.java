package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventDetails} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter caption;

    private StringFilter description;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private StringFilter startTime;

    private StringFilter endTime;

    private StringFilter timezone;
    private StringFilter location;

    private StringFilter directionsToVenue;

    private IntegerFilter capacity;

    private StringFilter admissionType;

    private BooleanFilter isActive;

    private IntegerFilter maxGuestsPerAttendee;

    private BooleanFilter allowGuests;

    private BooleanFilter requireGuestApproval;

    private BooleanFilter enableGuestPricing;

    private BooleanFilter enableQrCode;

    private BooleanFilter isRegistrationRequired;

    private BooleanFilter isSportsEvent;

    private BooleanFilter isLive;

    private BooleanFilter isFeaturedEvent;

    private IntegerFilter featuredEventPriority;

    private IntegerFilter liveEventPriority;
    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter createdById;

    private LongFilter eventTypeId;

    private LongFilter discountCodesId;

    private Boolean distinct;

    public EventDetailsCriteria() {}

    public EventDetailsCriteria(EventDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.caption = other.caption == null ? null : other.caption.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.timezone = other.timezone == null ? null : other.timezone.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.directionsToVenue = other.directionsToVenue == null ? null : other.directionsToVenue.copy();
        this.capacity = other.capacity == null ? null : other.capacity.copy();
        this.admissionType = other.admissionType == null ? null : other.admissionType.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.maxGuestsPerAttendee = other.maxGuestsPerAttendee == null ? null : other.maxGuestsPerAttendee.copy();
        this.allowGuests = other.allowGuests == null ? null : other.allowGuests.copy();
        this.requireGuestApproval = other.requireGuestApproval == null ? null : other.requireGuestApproval.copy();
        this.enableGuestPricing = other.enableGuestPricing == null ? null : other.enableGuestPricing.copy();
        this.enableQrCode = other.enableQrCode == null ? null : other.enableQrCode.copy();
        this.isRegistrationRequired = other.isRegistrationRequired == null ? null : other.isRegistrationRequired.copy();
        this.isSportsEvent = other.isSportsEvent == null ? null : other.isSportsEvent.copy();
        this.isLive = other.isLive == null ? null : other.isLive.copy();
        this.isFeaturedEvent = other.optionalIsFeaturedEvent().map(BooleanFilter::copy).orElse(null);
        this.featuredEventPriority = other.optionalFeaturedEventPriority().map(IntegerFilter::copy).orElse(null);
        this.liveEventPriority = other.optionalLiveEventPriority().map(IntegerFilter::copy).orElse(null);
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.eventTypeId = other.eventTypeId == null ? null : other.eventTypeId.copy();
        this.discountCodesId = other.discountCodesId == null ? null : other.discountCodesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventDetailsCriteria copy() {
        return new EventDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getCaption() {
        return caption;
    }

    public StringFilter caption() {
        if (caption == null) {
            caption = new StringFilter();
        }
        return caption;
    }

    public void setCaption(StringFilter caption) {
        this.caption = caption;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getStartTime() {
        return startTime;
    }

    public StringFilter startTime() {
        if (startTime == null) {
            startTime = new StringFilter();
        }
        return startTime;
    }

    public void setStartTime(StringFilter startTime) {
        this.startTime = startTime;
    }

    public StringFilter getEndTime() {
        return endTime;
    }

    public StringFilter endTime() {
        if (endTime == null) {
            endTime = new StringFilter();
        }
        return endTime;
    }

    public void setEndTime(StringFilter endTime) {
        this.endTime = endTime;
    }

    public StringFilter getTimezone() {
        return timezone;
    }

    public StringFilter timezone() {
        if (timezone == null) {
            timezone = new StringFilter();
        }
        return timezone;
    }

    public void setTimezone(StringFilter timezone) {
        this.timezone = timezone;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getDirectionsToVenue() {
        return directionsToVenue;
    }

    public StringFilter directionsToVenue() {
        if (directionsToVenue == null) {
            directionsToVenue = new StringFilter();
        }
        return directionsToVenue;
    }

    public void setDirectionsToVenue(StringFilter directionsToVenue) {
        this.directionsToVenue = directionsToVenue;
    }

    public IntegerFilter getCapacity() {
        return capacity;
    }

    public IntegerFilter capacity() {
        if (capacity == null) {
            capacity = new IntegerFilter();
        }
        return capacity;
    }

    public void setCapacity(IntegerFilter capacity) {
        this.capacity = capacity;
    }

    public StringFilter getAdmissionType() {
        return admissionType;
    }

    public StringFilter admissionType() {
        if (admissionType == null) {
            admissionType = new StringFilter();
        }
        return admissionType;
    }

    public void setAdmissionType(StringFilter admissionType) {
        this.admissionType = admissionType;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public IntegerFilter getMaxGuestsPerAttendee() {
        return maxGuestsPerAttendee;
    }

    public IntegerFilter maxGuestsPerAttendee() {
        if (maxGuestsPerAttendee == null) {
            maxGuestsPerAttendee = new IntegerFilter();
        }
        return maxGuestsPerAttendee;
    }

    public void setMaxGuestsPerAttendee(IntegerFilter maxGuestsPerAttendee) {
        this.maxGuestsPerAttendee = maxGuestsPerAttendee;
    }

    public BooleanFilter getAllowGuests() {
        return allowGuests;
    }

    public BooleanFilter allowGuests() {
        if (allowGuests == null) {
            allowGuests = new BooleanFilter();
        }
        return allowGuests;
    }

    public void setAllowGuests(BooleanFilter allowGuests) {
        this.allowGuests = allowGuests;
    }

    public BooleanFilter getRequireGuestApproval() {
        return requireGuestApproval;
    }

    public BooleanFilter requireGuestApproval() {
        if (requireGuestApproval == null) {
            requireGuestApproval = new BooleanFilter();
        }
        return requireGuestApproval;
    }

    public void setRequireGuestApproval(BooleanFilter requireGuestApproval) {
        this.requireGuestApproval = requireGuestApproval;
    }

    public BooleanFilter getEnableGuestPricing() {
        return enableGuestPricing;
    }

    public BooleanFilter enableGuestPricing() {
        if (enableGuestPricing == null) {
            enableGuestPricing = new BooleanFilter();
        }
        return enableGuestPricing;
    }

    public void setEnableGuestPricing(BooleanFilter enableGuestPricing) {
        this.enableGuestPricing = enableGuestPricing;
    }

    public BooleanFilter getEnableQrCode() {
        return enableQrCode;
    }

    public BooleanFilter enableQrCode() {
        if (enableQrCode == null) {
            enableQrCode = new BooleanFilter();
        }
        return enableQrCode;
    }

    public void setEnableQrCode(BooleanFilter enableQrCode) {
        this.enableQrCode = enableQrCode;
    }

    public BooleanFilter getIsRegistrationRequired() {
        return isRegistrationRequired;
    }

    public BooleanFilter isRegistrationRequired() {
        if (isRegistrationRequired == null) {
            isRegistrationRequired = new BooleanFilter();
        }
        return isRegistrationRequired;
    }

    public void setIsRegistrationRequired(BooleanFilter isRegistrationRequired) {
        this.isRegistrationRequired = isRegistrationRequired;
    }

    public BooleanFilter getIsSportsEvent() {
        return isSportsEvent;
    }

    public BooleanFilter isSportsEvent() {
        if (isSportsEvent == null) {
            isSportsEvent = new BooleanFilter();
        }
        return isSportsEvent;
    }

    public void setIsSportsEvent(BooleanFilter isSportsEvent) {
        this.isSportsEvent = isSportsEvent;
    }

    public BooleanFilter getIsLive() {
        return isLive;
    }

    public BooleanFilter isLive() {
        if (isLive == null) {
            isLive = new BooleanFilter();
        }
        return isLive;
    }

    public void setIsLive(BooleanFilter isLive) {
        this.isLive = isLive;
    }

    public BooleanFilter getIsFeaturedEvent() {
        return isFeaturedEvent;
    }

    public Optional<BooleanFilter> optionalIsFeaturedEvent() {
        return Optional.ofNullable(isFeaturedEvent);
    }

    public BooleanFilter isFeaturedEvent() {
        if (isFeaturedEvent == null) {
            setIsFeaturedEvent(new BooleanFilter());
        }
        return isFeaturedEvent;
    }

    public void setIsFeaturedEvent(BooleanFilter isFeaturedEvent) {
        this.isFeaturedEvent = isFeaturedEvent;
    }

    public IntegerFilter getFeaturedEventPriority() {
        return featuredEventPriority;
    }

    public Optional<IntegerFilter> optionalFeaturedEventPriority() {
        return Optional.ofNullable(featuredEventPriority);
    }

    public IntegerFilter featuredEventPriority() {
        if (featuredEventPriority == null) {
            setFeaturedEventPriority(new IntegerFilter());
        }
        return featuredEventPriority;
    }

    public void setFeaturedEventPriority(IntegerFilter featuredEventPriority) {
        this.featuredEventPriority = featuredEventPriority;
    }

    public IntegerFilter getLiveEventPriority() {
        return liveEventPriority;
    }

    public Optional<IntegerFilter> optionalLiveEventPriority() {
        return Optional.ofNullable(liveEventPriority);
    }

    public IntegerFilter liveEventPriority() {
        if (liveEventPriority == null) {
            setLiveEventPriority(new IntegerFilter());
        }
        return liveEventPriority;
    }

    public void setLiveEventPriority(IntegerFilter liveEventPriority) {
        this.liveEventPriority = liveEventPriority;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getEventTypeId() {
        return eventTypeId;
    }

    public LongFilter eventTypeId() {
        if (eventTypeId == null) {
            eventTypeId = new LongFilter();
        }
        return eventTypeId;
    }

    public void setEventTypeId(LongFilter eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public LongFilter getDiscountCodesId() {
        return discountCodesId;
    }

    public LongFilter discountCodesId() {
        if (discountCodesId == null) {
            discountCodesId = new LongFilter();
        }
        return discountCodesId;
    }

    public void setDiscountCodesId(LongFilter discountCodesId) {
        this.discountCodesId = discountCodesId;
    }

    public Boolean getDistinct() {
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
        final EventDetailsCriteria that = (EventDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(caption, that.caption) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(timezone, that.timezone) &&
            Objects.equals(location, that.location) &&
            Objects.equals(directionsToVenue, that.directionsToVenue) &&
            Objects.equals(capacity, that.capacity) &&
            Objects.equals(admissionType, that.admissionType) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(maxGuestsPerAttendee, that.maxGuestsPerAttendee) &&
            Objects.equals(allowGuests, that.allowGuests) &&
            Objects.equals(requireGuestApproval, that.requireGuestApproval) &&
            Objects.equals(enableGuestPricing, that.enableGuestPricing) &&
            Objects.equals(enableQrCode, that.enableQrCode) &&
            Objects.equals(isRegistrationRequired, that.isRegistrationRequired) &&
            Objects.equals(isSportsEvent, that.isSportsEvent) &&
            Objects.equals(isLive, that.isLive) &&
            Objects.equals(isFeaturedEvent, that.isFeaturedEvent) &&
            Objects.equals(featuredEventPriority, that.featuredEventPriority) &&
            Objects.equals(liveEventPriority, that.liveEventPriority) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(eventTypeId, that.eventTypeId) &&
            Objects.equals(discountCodesId, that.discountCodesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            caption,
            description,
            startDate,
            endDate,
            startTime,
            endTime,
            timezone,
            location,
            directionsToVenue,
            capacity,
            admissionType,
            isActive,
            maxGuestsPerAttendee,
            allowGuests,
            requireGuestApproval,
            enableGuestPricing,
            enableQrCode,
            isRegistrationRequired,
            isSportsEvent,
            isLive,
            isFeaturedEvent,
            featuredEventPriority,
            liveEventPriority,
            createdAt,
            updatedAt,
            createdById,
            eventTypeId,
            discountCodesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (caption != null ? "caption=" + caption + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (timezone != null ? "timezone=" + timezone + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (directionsToVenue != null ? "directionsToVenue=" + directionsToVenue + ", " : "") +
            (capacity != null ? "capacity=" + capacity + ", " : "") +
            (admissionType != null ? "admissionType=" + admissionType + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (maxGuestsPerAttendee != null ? "maxGuestsPerAttendee=" + maxGuestsPerAttendee + ", " : "") +
            (allowGuests != null ? "allowGuests=" + allowGuests + ", " : "") +
            (requireGuestApproval != null ? "requireGuestApproval=" + requireGuestApproval + ", " : "") +
            (enableGuestPricing != null ? "enableGuestPricing=" + enableGuestPricing + ", " : "") +
            (enableQrCode != null ? "enableQrCode=" + enableQrCode + ", " : "") +
            (isRegistrationRequired != null ? "isRegistrationRequired=" + isRegistrationRequired + ", " : "") +
            (isSportsEvent != null ? "isSportsEvent=" + isSportsEvent + ", " : "") +
            (isLive != null ? "isLive=" + isLive + ", " : "") +
             optionalIsFeaturedEvent().map(f -> "isFeaturedEvent=" + f + ", ").orElse("") +
            optionalFeaturedEventPriority().map(f -> "featuredEventPriority=" + f + ", ").orElse("") +
            optionalLiveEventPriority().map(f -> "liveEventPriority=" + f + ", ").orElse("") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (eventTypeId != null ? "eventTypeId=" + eventTypeId + ", " : "") +
            (discountCodesId != null ? "discountCodesId=" + discountCodesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
