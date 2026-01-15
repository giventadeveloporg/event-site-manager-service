package com.nextjstemplate.service.dto;

import com.nextjstemplate.domain.enumeration.PaymentFlowMode;
import com.nextjstemplate.domain.enumeration.RecurrenceEndType;
import com.nextjstemplate.domain.enumeration.RecurrencePattern;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
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

    private String metadata; // DEPRECATED - kept for backward compatibility during migration

    private String donationMetadata; // For fundraiser/charity configuration only

    private String eventRecurrenceMetadata; // For recurrence configuration only

    @Size(max = 2048)
    private String emailHeaderImageUrl;

    @NotNull
    @Size(max = 255)
    private String fromEmail;

    // Recurrence fields
    private Boolean isRecurring;

    private RecurrencePattern recurrencePattern;

    private Integer recurrenceInterval;

    private RecurrenceEndType recurrenceEndType;

    private LocalDate recurrenceEndDate;

    private Integer recurrenceOccurrences;

    private List<Integer> recurrenceWeeklyDays;

    private Integer recurrenceMonthlyDay;

    private Long parentEventId;

    private Long recurrenceSeriesId;

    private PaymentFlowMode paymentFlowMode;

    private Boolean manualPaymentEnabled;

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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getDonationMetadata() {
        return donationMetadata;
    }

    public void setDonationMetadata(String donationMetadata) {
        this.donationMetadata = donationMetadata;
    }

    public String getEventRecurrenceMetadata() {
        return eventRecurrenceMetadata;
    }

    public void setEventRecurrenceMetadata(String eventRecurrenceMetadata) {
        this.eventRecurrenceMetadata = eventRecurrenceMetadata;
    }

    public String getEmailHeaderImageUrl() {
        return emailHeaderImageUrl;
    }

    public void setEmailHeaderImageUrl(String emailHeaderImageUrl) {
        this.emailHeaderImageUrl = emailHeaderImageUrl;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public RecurrencePattern getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public Integer getRecurrenceInterval() {
        return recurrenceInterval;
    }

    public void setRecurrenceInterval(Integer recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }

    public RecurrenceEndType getRecurrenceEndType() {
        return recurrenceEndType;
    }

    public void setRecurrenceEndType(RecurrenceEndType recurrenceEndType) {
        this.recurrenceEndType = recurrenceEndType;
    }

    public LocalDate getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(LocalDate recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public Integer getRecurrenceOccurrences() {
        return recurrenceOccurrences;
    }

    public void setRecurrenceOccurrences(Integer recurrenceOccurrences) {
        this.recurrenceOccurrences = recurrenceOccurrences;
    }

    public List<Integer> getRecurrenceWeeklyDays() {
        return recurrenceWeeklyDays;
    }

    public void setRecurrenceWeeklyDays(List<Integer> recurrenceWeeklyDays) {
        this.recurrenceWeeklyDays = recurrenceWeeklyDays;
    }

    public Integer getRecurrenceMonthlyDay() {
        return recurrenceMonthlyDay;
    }

    public void setRecurrenceMonthlyDay(Integer recurrenceMonthlyDay) {
        this.recurrenceMonthlyDay = recurrenceMonthlyDay;
    }

    public Long getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(Long parentEventId) {
        this.parentEventId = parentEventId;
    }

    public Long getRecurrenceSeriesId() {
        return recurrenceSeriesId;
    }

    public void setRecurrenceSeriesId(Long recurrenceSeriesId) {
        this.recurrenceSeriesId = recurrenceSeriesId;
    }

    public PaymentFlowMode getPaymentFlowMode() {
        return paymentFlowMode;
    }

    public void setPaymentFlowMode(PaymentFlowMode paymentFlowMode) {
        this.paymentFlowMode = paymentFlowMode;
    }

    public Boolean getManualPaymentEnabled() {
        return manualPaymentEnabled;
    }

    public void setManualPaymentEnabled(Boolean manualPaymentEnabled) {
        this.manualPaymentEnabled = manualPaymentEnabled;
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
