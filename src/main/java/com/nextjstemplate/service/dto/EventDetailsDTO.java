package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDetailsDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String caption;

    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalDate promotionStartDate;

    @NotNull
    @Size(max = 100)
    private String startTime;

    @NotNull
    @Size(max = 100)
    private String endTime;

    @NotNull
    private String timezone;

    @Size(max = 255)
    private String location;

    @Size(max = 600)
    private String directionsToVenue;

    private Integer capacity;

    @Size(max = 50)
    private String admissionType;

    private Boolean isActive;

    @Min(value = 0)
    private Integer maxGuestsPerAttendee;

    private Boolean allowGuests;

    private Boolean requireGuestApproval;

    private Boolean enableGuestPricing;

    private Boolean enableQrCode;
    private Boolean isRegistrationRequired;

    private Boolean isSportsEvent;

    private Boolean isLive;

    @NotNull
    private Boolean isFeaturedEvent;

    @NotNull
    private Integer featuredEventPriorityRanking;

    @NotNull
    private Integer liveEventPriorityRanking;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private UserProfileDTO createdBy;

    private EventTypeDetailsDTO eventType;

    private Set<DiscountCodeDTO> discountCodes = new HashSet<>();

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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getPromotionStartDate() {
        return promotionStartDate;
    }

    public void setPromotionStartDate(LocalDate promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDirectionsToVenue() {
        return directionsToVenue;
    }

    public void setDirectionsToVenue(String directionsToVenue) {
        this.directionsToVenue = directionsToVenue;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(String admissionType) {
        this.admissionType = admissionType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxGuestsPerAttendee() {
        return maxGuestsPerAttendee;
    }

    public void setMaxGuestsPerAttendee(Integer maxGuestsPerAttendee) {
        this.maxGuestsPerAttendee = maxGuestsPerAttendee;
    }

    public Boolean getAllowGuests() {
        return allowGuests;
    }

    public void setAllowGuests(Boolean allowGuests) {
        this.allowGuests = allowGuests;
    }

    public Boolean getRequireGuestApproval() {
        return requireGuestApproval;
    }

    public void setRequireGuestApproval(Boolean requireGuestApproval) {
        this.requireGuestApproval = requireGuestApproval;
    }

    public Boolean getEnableGuestPricing() {
        return enableGuestPricing;
    }

    public void setEnableGuestPricing(Boolean enableGuestPricing) {
        this.enableGuestPricing = enableGuestPricing;
    }

    public Boolean getEnableQrCode() {
        return enableQrCode;
    }

    public void setEnableQrCode(Boolean enableQrCode) {
        this.enableQrCode = enableQrCode;
    }

    public Boolean getIsRegistrationRequired() {
        return isRegistrationRequired;
    }

    public void setIsRegistrationRequired(Boolean isRegistrationRequired) {
        this.isRegistrationRequired = isRegistrationRequired;
    }

    public Boolean getIsSportsEvent() {
        return isSportsEvent;
    }

    public void setIsSportsEvent(Boolean isSportsEvent) {
        this.isSportsEvent = isSportsEvent;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean getIsFeaturedEvent() {
        return isFeaturedEvent;
    }

    public void setIsFeaturedEvent(Boolean isFeaturedEvent) {
        this.isFeaturedEvent = isFeaturedEvent;
    }

    public Integer getFeaturedEventPriorityRanking() {
        return featuredEventPriorityRanking;
    }

    public void setFeaturedEventPriorityRanking(Integer featuredEventPriorityRanking) {
        this.featuredEventPriorityRanking = featuredEventPriorityRanking;
    }

    public Integer getLiveEventPriorityRanking() {
        return liveEventPriorityRanking;
    }

    public void setLiveEventPriorityRanking(Integer liveEventPriorityRanking) {
        this.liveEventPriorityRanking = liveEventPriorityRanking;
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

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    public EventTypeDetailsDTO getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeDetailsDTO eventType) {
        this.eventType = eventType;
    }

    public Set<DiscountCodeDTO> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(Set<DiscountCodeDTO> discountCodes) {
        this.discountCodes = discountCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDetailsDTO)) {
            return false;
        }

        EventDetailsDTO eventDetailsDTO = (EventDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDetailsDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", caption='" + getCaption() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", promotionStartDate='" + getPromotionStartDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", timezone='" + getTimezone() + "'" +
            ", location='" + getLocation() + "'" +
            ", directionsToVenue='" + getDirectionsToVenue() + "'" +
            ", capacity=" + getCapacity() +
            ", admissionType='" + getAdmissionType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", maxGuestsPerAttendee=" + getMaxGuestsPerAttendee() +
            ", allowGuests='" + getAllowGuests() + "'" +
            ", requireGuestApproval='" + getRequireGuestApproval() + "'" +
            ", enableGuestPricing='" + getEnableGuestPricing() + "'" +
            ", enableQrCode='" + getEnableQrCode() + "'" +
            ", isRegistrationRequired='" + getIsRegistrationRequired() + "'" +
            ", isSportsEvent='" + getIsSportsEvent() + "'" +
            ", isLive='" + getIsLive() + "'" +
            ", isFeaturedEvent='" + getIsFeaturedEvent() + "'" +
            ", featuredEventPriorityRanking=" + getFeaturedEventPriorityRanking() +
            ", liveEventPriorityRanking=" + getLiveEventPriorityRanking() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", eventType=" + getEventType() +
            ", discountCodes=" + getDiscountCodes() +
            "}";
    }
}
