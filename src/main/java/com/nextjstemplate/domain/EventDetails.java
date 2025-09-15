package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventDetails.
 */
@Entity
@Table(name = "event_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "caption", length = 255)
    private String caption;

    @Size(max = 1000)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "promotion_start_date", nullable = false)
    private LocalDate promotionStartDate;

    @NotNull
    @Size(max = 100)
    @Column(name = "start_time", length = 100, nullable = false)
    private String startTime;

    @NotNull
    @Size(max = 100)
    @Column(name = "end_time", length = 100, nullable = false)
    private String endTime;

    @NotNull
    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Size(max = 255)
    @Column(name = "location", length = 255)
    private String location;

    @Size(max = 600)
    @Column(name = "directions_to_venue", length = 255)
    private String directionsToVenue;

    @Column(name = "capacity")
    private Integer capacity;

    @Size(max = 50)
    @Column(name = "admission_type", length = 50)
    private String admissionType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Min(value = 0)
    @Column(name = "max_guests_per_attendee")
    private Integer maxGuestsPerAttendee;

    @Column(name = "allow_guests")
    private Boolean allowGuests;

    @Column(name = "require_guest_approval")
    private Boolean requireGuestApproval;

    @Column(name = "enable_guest_pricing")
    private Boolean enableGuestPricing;

    @Column(name = "enable_qr_code")
    private Boolean enableQrCode;

    @Column(name = "is_registration_required")
    private Boolean isRegistrationRequired;

    @Column(name = "is_sports_event")
    private Boolean isSportsEvent;

    @Column(name = "is_live")
    private Boolean isLive;

    @NotNull
    @Column(name = "is_featured_event", nullable = false)
    private Boolean isFeaturedEvent;

    @NotNull
    @Column(name = "featured_event_priority_ranking", nullable = false)
    private Integer featuredEventPriorityRanking;

    @NotNull
    @Column(name = "live_event_priority_ranking", nullable = false)
    private Integer liveEventPriorityRanking;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventTypeDetails eventType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_event_details__discount_codes", joinColumns = @JoinColumn(name = "event_details_id"), inverseJoinColumns = @JoinColumn(name = "discount_codes_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "events" }, allowSetters = true)
    private Set<DiscountCode> discountCodes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Set<EventFeaturedPerformers> eventFeaturedPerformers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Set<EventContacts> eventContacts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Set<EventEmails> eventEmails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Set<EventProgramDirectors> eventProgramDirectors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Set<EventSponsorsJoin> eventSponsorsJoins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventDetails tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return this.title;
    }

    public EventDetails title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return this.caption;
    }

    public EventDetails caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return this.description;
    }

    public EventDetails description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public EventDetails startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public EventDetails endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getPromotionStartDate() {
        return this.promotionStartDate;
    }

    public EventDetails promotionStartDate(LocalDate promotionStartDate) {
        this.setPromotionStartDate(promotionStartDate);
        return this;
    }

    public void setPromotionStartDate(LocalDate promotionStartDate) {
        this.promotionStartDate = promotionStartDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public EventDetails startTime(String startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public EventDetails endTime(String endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public EventDetails timezone(String timezone) {
        this.setTimezone(timezone);
        return this;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocation() {
        return this.location;
    }

    public EventDetails location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDirectionsToVenue() {
        return this.directionsToVenue;
    }

    public EventDetails directionsToVenue(String directionsToVenue) {
        this.setDirectionsToVenue(directionsToVenue);
        return this;
    }

    public void setDirectionsToVenue(String directionsToVenue) {
        this.directionsToVenue = directionsToVenue;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public EventDetails capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAdmissionType() {
        return this.admissionType;
    }

    public EventDetails admissionType(String admissionType) {
        this.setAdmissionType(admissionType);
        return this;
    }

    public void setAdmissionType(String admissionType) {
        this.admissionType = admissionType;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public EventDetails isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxGuestsPerAttendee() {
        return this.maxGuestsPerAttendee;
    }

    public EventDetails maxGuestsPerAttendee(Integer maxGuestsPerAttendee) {
        this.setMaxGuestsPerAttendee(maxGuestsPerAttendee);
        return this;
    }

    public void setMaxGuestsPerAttendee(Integer maxGuestsPerAttendee) {
        this.maxGuestsPerAttendee = maxGuestsPerAttendee;
    }

    public Boolean getAllowGuests() {
        return this.allowGuests;
    }

    public EventDetails allowGuests(Boolean allowGuests) {
        this.setAllowGuests(allowGuests);
        return this;
    }

    public void setAllowGuests(Boolean allowGuests) {
        this.allowGuests = allowGuests;
    }

    public Boolean getRequireGuestApproval() {
        return this.requireGuestApproval;
    }

    public EventDetails requireGuestApproval(Boolean requireGuestApproval) {
        this.setRequireGuestApproval(requireGuestApproval);
        return this;
    }

    public void setRequireGuestApproval(Boolean requireGuestApproval) {
        this.requireGuestApproval = requireGuestApproval;
    }

    public Boolean getEnableGuestPricing() {
        return this.enableGuestPricing;
    }

    public EventDetails enableGuestPricing(Boolean enableGuestPricing) {
        this.setEnableGuestPricing(enableGuestPricing);
        return this;
    }

    public void setEnableGuestPricing(Boolean enableGuestPricing) {
        this.enableGuestPricing = enableGuestPricing;
    }

    public Boolean getEnableQrCode() {
        return this.enableQrCode;
    }

    public EventDetails enableQrCode(Boolean enableQrCode) {
        this.setEnableQrCode(enableQrCode);
        return this;
    }

    public void setEnableQrCode(Boolean enableQrCode) {
        this.enableQrCode = enableQrCode;
    }

    public Boolean getIsRegistrationRequired() {
        return this.isRegistrationRequired;
    }

    public EventDetails isRegistrationRequired(Boolean isRegistrationRequired) {
        this.setIsRegistrationRequired(isRegistrationRequired);
        return this;
    }

    public void setIsRegistrationRequired(Boolean isRegistrationRequired) {
        this.isRegistrationRequired = isRegistrationRequired;
    }

    public Boolean getIsSportsEvent() {
        return this.isSportsEvent;
    }

    public EventDetails isSportsEvent(Boolean isSportsEvent) {
        this.setIsSportsEvent(isSportsEvent);
        return this;
    }

    public void setIsSportsEvent(Boolean isSportsEvent) {
        this.isSportsEvent = isSportsEvent;
    }

    public Boolean getIsLive() {
        return this.isLive;
    }

    public EventDetails isLive(Boolean isLive) {
        this.setIsLive(isLive);
        return this;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean getIsFeaturedEvent() {
        return this.isFeaturedEvent;
    }

    public EventDetails isFeaturedEvent(Boolean isFeaturedEvent) {
        this.setIsFeaturedEvent(isFeaturedEvent);
        return this;
    }

    public void setIsFeaturedEvent(Boolean isFeaturedEvent) {
        this.isFeaturedEvent = isFeaturedEvent;
    }

    public Integer getFeaturedEventPriorityRanking() {
        return this.featuredEventPriorityRanking;
    }

    public EventDetails featuredEventPriorityRanking(Integer featuredEventPriorityRanking) {
        this.setFeaturedEventPriorityRanking(featuredEventPriorityRanking);
        return this;
    }

    public void setFeaturedEventPriorityRanking(Integer featuredEventPriorityRanking) {
        this.featuredEventPriorityRanking = featuredEventPriorityRanking;
    }

    public Integer getLiveEventPriorityRanking() {
        return this.liveEventPriorityRanking;
    }

    public EventDetails liveEventPriorityRanking(Integer liveEventPriorityRanking) {
        this.setLiveEventPriorityRanking(liveEventPriorityRanking);
        return this;
    }

    public void setLiveEventPriorityRanking(Integer liveEventPriorityRanking) {
        this.liveEventPriorityRanking = liveEventPriorityRanking;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventDetails createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventDetails updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfile getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserProfile userProfile) {
        this.createdBy = userProfile;
    }

    public EventDetails createdBy(UserProfile userProfile) {
        this.setCreatedBy(userProfile);
        return this;
    }

    public EventTypeDetails getEventType() {
        return this.eventType;
    }

    public void setEventType(EventTypeDetails eventTypeDetails) {
        this.eventType = eventTypeDetails;
    }

    public EventDetails eventType(EventTypeDetails eventTypeDetails) {
        this.setEventType(eventTypeDetails);
        return this;
    }

    public Set<DiscountCode> getDiscountCodes() {
        return this.discountCodes;
    }

    public void setDiscountCodes(Set<DiscountCode> discountCodes) {
        this.discountCodes = discountCodes;
    }

    public EventDetails discountCodes(Set<DiscountCode> discountCodes) {
        this.setDiscountCodes(discountCodes);
        return this;
    }

    public EventDetails addDiscountCodes(DiscountCode discountCode) {
        this.discountCodes.add(discountCode);
        return this;
    }

    public EventDetails removeDiscountCodes(DiscountCode discountCode) {
        this.discountCodes.remove(discountCode);
        return this;
    }

    public Set<EventFeaturedPerformers> getEventFeaturedPerformers() {
        return this.eventFeaturedPerformers;
    }

    public void setEventFeaturedPerformers(Set<EventFeaturedPerformers> eventFeaturedPerformers) {
        if (this.eventFeaturedPerformers != null) {
            this.eventFeaturedPerformers.forEach(i -> i.setEvent(null));
        }
        if (eventFeaturedPerformers != null) {
            eventFeaturedPerformers.forEach(i -> i.setEvent(this));
        }
        this.eventFeaturedPerformers = eventFeaturedPerformers;
    }

    public EventDetails eventFeaturedPerformers(Set<EventFeaturedPerformers> eventFeaturedPerformers) {
        this.setEventFeaturedPerformers(eventFeaturedPerformers);
        return this;
    }

    public EventDetails addEventFeaturedPerformers(EventFeaturedPerformers eventFeaturedPerformers) {
        this.eventFeaturedPerformers.add(eventFeaturedPerformers);
        eventFeaturedPerformers.setEvent(this);
        return this;
    }

    public EventDetails removeEventFeaturedPerformers(EventFeaturedPerformers eventFeaturedPerformers) {
        this.eventFeaturedPerformers.remove(eventFeaturedPerformers);
        eventFeaturedPerformers.setEvent(null);
        return this;
    }

    public Set<EventContacts> getEventContacts() {
        return this.eventContacts;
    }

    public void setEventContacts(Set<EventContacts> eventContacts) {
        if (this.eventContacts != null) {
            this.eventContacts.forEach(i -> i.setEvent(null));
        }
        if (eventContacts != null) {
            eventContacts.forEach(i -> i.setEvent(this));
        }
        this.eventContacts = eventContacts;
    }

    public EventDetails eventContacts(Set<EventContacts> eventContacts) {
        this.setEventContacts(eventContacts);
        return this;
    }

    public EventDetails addEventContacts(EventContacts eventContacts) {
        this.eventContacts.add(eventContacts);
        eventContacts.setEvent(this);
        return this;
    }

    public EventDetails removeEventContacts(EventContacts eventContacts) {
        this.eventContacts.remove(eventContacts);
        eventContacts.setEvent(null);
        return this;
    }

    public Set<EventEmails> getEventEmails() {
        return this.eventEmails;
    }

    public void setEventEmails(Set<EventEmails> eventEmails) {
        if (this.eventEmails != null) {
            this.eventEmails.forEach(i -> i.setEvent(null));
        }
        if (eventEmails != null) {
            eventEmails.forEach(i -> i.setEvent(this));
        }
        this.eventEmails = eventEmails;
    }

    public EventDetails eventEmails(Set<EventEmails> eventEmails) {
        this.setEventEmails(eventEmails);
        return this;
    }

    public EventDetails addEventEmails(EventEmails eventEmails) {
        this.eventEmails.add(eventEmails);
        eventEmails.setEvent(this);
        return this;
    }

    public EventDetails removeEventEmails(EventEmails eventEmails) {
        this.eventEmails.remove(eventEmails);
        eventEmails.setEvent(null);
        return this;
    }

    public Set<EventProgramDirectors> getEventProgramDirectors() {
        return this.eventProgramDirectors;
    }

    public void setEventProgramDirectors(Set<EventProgramDirectors> eventProgramDirectors) {
        if (this.eventProgramDirectors != null) {
            this.eventProgramDirectors.forEach(i -> i.setEvent(null));
        }
        if (eventProgramDirectors != null) {
            eventProgramDirectors.forEach(i -> i.setEvent(this));
        }
        this.eventProgramDirectors = eventProgramDirectors;
    }

    public EventDetails eventProgramDirectors(Set<EventProgramDirectors> eventProgramDirectors) {
        this.setEventProgramDirectors(eventProgramDirectors);
        return this;
    }

    public EventDetails addEventProgramDirectors(EventProgramDirectors eventProgramDirectors) {
        this.eventProgramDirectors.add(eventProgramDirectors);
        eventProgramDirectors.setEvent(this);
        return this;
    }

    public EventDetails removeEventProgramDirectors(EventProgramDirectors eventProgramDirectors) {
        this.eventProgramDirectors.remove(eventProgramDirectors);
        eventProgramDirectors.setEvent(null);
        return this;
    }

    public Set<EventSponsorsJoin> getEventSponsorsJoins() {
        return this.eventSponsorsJoins;
    }

    public void setEventSponsorsJoins(Set<EventSponsorsJoin> eventSponsorsJoins) {
        if (this.eventSponsorsJoins != null) {
            this.eventSponsorsJoins.forEach(i -> i.setEvent(null));
        }
        if (eventSponsorsJoins != null) {
            eventSponsorsJoins.forEach(i -> i.setEvent(this));
        }
        this.eventSponsorsJoins = eventSponsorsJoins;
    }

    public EventDetails eventSponsorsJoins(Set<EventSponsorsJoin> eventSponsorsJoins) {
        this.setEventSponsorsJoins(eventSponsorsJoins);
        return this;
    }

    public EventDetails addEventSponsorsJoin(EventSponsorsJoin eventSponsorsJoin) {
        this.eventSponsorsJoins.add(eventSponsorsJoin);
        eventSponsorsJoin.setEvent(this);
        return this;
    }

    public EventDetails removeEventSponsorsJoin(EventSponsorsJoin eventSponsorsJoin) {
        this.eventSponsorsJoins.remove(eventSponsorsJoin);
        eventSponsorsJoin.setEvent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((EventDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDetails{" +
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
                "}";
    }
}
