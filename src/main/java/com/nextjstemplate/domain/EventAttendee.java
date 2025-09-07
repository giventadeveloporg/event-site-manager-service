package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventAttendee.
 */
@Entity
@Table(name = "event_attendee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    @Size(max = 255)
    @Column(name = "phone", length = 255)
    private String phone;

    @Column(name = "is_member")
    private Boolean isMember;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "user_id")
    private Long userId;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    @Column(name = "registration_status", length = 50, nullable = false)
    private String registrationStatus;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private ZonedDateTime registrationDate;

    @Column(name = "confirmation_date")
    private ZonedDateTime confirmationDate;

    @Column(name = "cancellation_date")
    private ZonedDateTime cancellationDate;

    @Size(max = 500)
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Size(max = 50)
    @Column(name = "attendee_type", length = 50)
    private String attendeeType;

    @Size(max = 1000)
    @Column(name = "special_requirements", length = 1000)
    private String specialRequirements;

    @Size(max = 255)
    @Column(name = "emergency_contact_name", length = 255)
    private String emergencyContactName;

    @Size(max = 50)
    @Column(name = "emergency_contact_phone", length = 50)
    private String emergencyContactPhone;

    @Size(max = 50)
    @Column(name = "check_in_status", length = 50)
    private String checkInStatus;

    @Column(name = "check_in_time")
    private ZonedDateTime checkInTime;

    @Column(name = "total_number_of_guests")
    private Integer totalNumberOfGuests;

    @Column(name = "number_of_guests_checked_in")
    private Integer numberOfGuestsCheckedIn;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    @Size(max = 1000)
    @Column(name = "qr_code_data", length = 1000)
    private String qrCodeData;

    @Column(name = "qr_code_generated")
    private Boolean qrCodeGenerated;

    @Column(name = "qr_code_generated_at")
    private ZonedDateTime qrCodeGeneratedAt;

    @Size(max = 1000)
    @Column(name = "dietary_restrictions", length = 1000)
    private String dietaryRestrictions;

    @Size(max = 1000)
    @Column(name = "accessibility_needs", length = 1000)
    private String accessibilityNeeds;

    @Size(max = 100)
    @Column(name = "emergency_contact_relationship", length = 100)
    private String emergencyContactRelationship;

    @Column(name = "check_out_time")
    private ZonedDateTime checkOutTime;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "attendance_rating")
    private Integer attendanceRating;

    @Size(max = 1000)
    @Column(name = "feedback", length = 1000)
    private String feedback;

    @Size(max = 100)
    @Column(name = "registration_source", length = 100)
    private String registrationSource;

    @Min(value = 1)
    @Column(name = "waitlist_position")
    private Integer waitListPosition;

    @Column(name = "priority_score")
    private Integer priorityScore;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    /*@ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "createdBy", "eventType", "discountCodes" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile attendee;
*/
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public EventAttendee userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return this.id;
    }

    public EventAttendee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public EventAttendee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public EventAttendee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public EventAttendee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public EventAttendee phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsMember() {
        return this.isMember;
    }

    public EventAttendee isMember(Boolean isMember) {
        this.setIsMember(isMember);
        return this;
    }

    public void setIsMember(Boolean isMember) {
        this.isMember = isMember;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventAttendee tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRegistrationStatus() {
        return this.registrationStatus;
    }

    public EventAttendee registrationStatus(String registrationStatus) {
        this.setRegistrationStatus(registrationStatus);
        return this;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public ZonedDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public EventAttendee registrationDate(ZonedDateTime registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTime getConfirmationDate() {
        return this.confirmationDate;
    }

    public EventAttendee confirmationDate(ZonedDateTime confirmationDate) {
        this.setConfirmationDate(confirmationDate);
        return this;
    }

    public void setConfirmationDate(ZonedDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTime getCancellationDate() {
        return this.cancellationDate;
    }

    public EventAttendee cancellationDate(ZonedDateTime cancellationDate) {
        this.setCancellationDate(cancellationDate);
        return this;
    }

    public void setCancellationDate(ZonedDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public EventAttendee cancellationReason(String cancellationReason) {
        this.setCancellationReason(cancellationReason);
        return this;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getAttendeeType() {
        return this.attendeeType;
    }

    public EventAttendee attendeeType(String attendeeType) {
        this.setAttendeeType(attendeeType);
        return this;
    }

    public void setAttendeeType(String attendeeType) {
        this.attendeeType = attendeeType;
    }

    public String getSpecialRequirements() {
        return this.specialRequirements;
    }

    public EventAttendee specialRequirements(String specialRequirements) {
        this.setSpecialRequirements(specialRequirements);
        return this;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getEmergencyContactName() {
        return this.emergencyContactName;
    }

    public EventAttendee emergencyContactName(String emergencyContactName) {
        this.setEmergencyContactName(emergencyContactName);
        return this;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return this.emergencyContactPhone;
    }

    public EventAttendee emergencyContactPhone(String emergencyContactPhone) {
        this.setEmergencyContactPhone(emergencyContactPhone);
        return this;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getCheckInStatus() {
        return this.checkInStatus;
    }

    public EventAttendee checkInStatus(String checkInStatus) {
        this.setCheckInStatus(checkInStatus);
        return this;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTime getCheckInTime() {
        return this.checkInTime;
    }

    public EventAttendee checkInTime(ZonedDateTime checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Integer getTotalNumberOfGuests() {
        return this.totalNumberOfGuests;
    }

    public EventAttendee totalNumberOfGuests(Integer totalNumberOfGuests) {
        this.setTotalNumberOfGuests(totalNumberOfGuests);
        return this;
    }

    public void setTotalNumberOfGuests(Integer totalNumberOfGuests) {
        this.totalNumberOfGuests = totalNumberOfGuests;
    }

    public Integer getNumberOfGuestsCheckedIn() {
        return this.numberOfGuestsCheckedIn;
    }

    public EventAttendee numberOfGuestsCheckedIn(Integer numberOfGuestsCheckedIn) {
        this.setNumberOfGuestsCheckedIn(numberOfGuestsCheckedIn);
        return this;
    }

    public void setNumberOfGuestsCheckedIn(Integer numberOfGuestsCheckedIn) {
        this.numberOfGuestsCheckedIn = numberOfGuestsCheckedIn;
    }

    public String getNotes() {
        return this.notes;
    }

    public EventAttendee notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQrCodeData() {
        return this.qrCodeData;
    }

    public EventAttendee qrCodeData(String qrCodeData) {
        this.setQrCodeData(qrCodeData);
        return this;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public Boolean getQrCodeGenerated() {
        return this.qrCodeGenerated;
    }

    public EventAttendee qrCodeGenerated(Boolean qrCodeGenerated) {
        this.setQrCodeGenerated(qrCodeGenerated);
        return this;
    }

    public void setQrCodeGenerated(Boolean qrCodeGenerated) {
        this.qrCodeGenerated = qrCodeGenerated;
    }

    public ZonedDateTime getQrCodeGeneratedAt() {
        return this.qrCodeGeneratedAt;
    }

    public EventAttendee qrCodeGeneratedAt(ZonedDateTime qrCodeGeneratedAt) {
        this.setQrCodeGeneratedAt(qrCodeGeneratedAt);
        return this;
    }

    public void setQrCodeGeneratedAt(ZonedDateTime qrCodeGeneratedAt) {
        this.qrCodeGeneratedAt = qrCodeGeneratedAt;
    }

    public String getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    public EventAttendee dietaryRestrictions(String dietaryRestrictions) {
        this.setDietaryRestrictions(dietaryRestrictions);
        return this;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public String getAccessibilityNeeds() {
        return this.accessibilityNeeds;
    }

    public EventAttendee accessibilityNeeds(String accessibilityNeeds) {
        this.setAccessibilityNeeds(accessibilityNeeds);
        return this;
    }

    public void setAccessibilityNeeds(String accessibilityNeeds) {
        this.accessibilityNeeds = accessibilityNeeds;
    }

    public String getEmergencyContactRelationship() {
        return this.emergencyContactRelationship;
    }

    public EventAttendee emergencyContactRelationship(String emergencyContactRelationship) {
        this.setEmergencyContactRelationship(emergencyContactRelationship);
        return this;
    }

    public void setEmergencyContactRelationship(String emergencyContactRelationship) {
        this.emergencyContactRelationship = emergencyContactRelationship;
    }

    public ZonedDateTime getCheckOutTime() {
        return this.checkOutTime;
    }

    public EventAttendee checkOutTime(ZonedDateTime checkOutTime) {
        this.setCheckOutTime(checkOutTime);
        return this;
    }

    public void setCheckOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getAttendanceRating() {
        return this.attendanceRating;
    }

    public EventAttendee attendanceRating(Integer attendanceRating) {
        this.setAttendanceRating(attendanceRating);
        return this;
    }

    public void setAttendanceRating(Integer attendanceRating) {
        this.attendanceRating = attendanceRating;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public EventAttendee feedback(String feedback) {
        this.setFeedback(feedback);
        return this;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRegistrationSource() {
        return this.registrationSource;
    }

    public EventAttendee registrationSource(String registrationSource) {
        this.setRegistrationSource(registrationSource);
        return this;
    }

    public void setRegistrationSource(String registrationSource) {
        this.registrationSource = registrationSource;
    }

    public Integer getWaitListPosition() {
        return this.waitListPosition;
    }

    public EventAttendee waitListPosition(Integer waitListPosition) {
        this.setWaitListPosition(waitListPosition);
        return this;
    }

    public void setWaitListPosition(Integer waitListPosition) {
        this.waitListPosition = waitListPosition;
    }

    public Integer getPriorityScore() {
        return this.priorityScore;
    }

    public EventAttendee priorityScore(Integer priorityScore) {
        this.setPriorityScore(priorityScore);
        return this;
    }

    public void setPriorityScore(Integer priorityScore) {
        this.priorityScore = priorityScore;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventAttendee createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventAttendee updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /*  public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public EventAttendee event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public UserProfile getAttendee() {
        return this.attendee;
    }

    public void setAttendee(UserProfile userProfile) {
        this.attendee = userProfile;
    }*/
    /*
    public EventAttendee attendee(UserProfile userProfile) {
        this.setAttendee(userProfile);
        return this;
    }*/

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendee)) {
            return false;
        }
        return getId() != null && getId().equals(((EventAttendee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", isMember='" + getIsMember() + "'" +
            ", eventId=" + getEventId() +
            ", userId=" + getUserId() +
            ", tenantId='" + getTenantId() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", confirmationDate='" + getConfirmationDate() + "'" +
            ", cancellationDate='" + getCancellationDate() + "'" +
            ", cancellationReason='" + getCancellationReason() + "'" +
            ", attendeeType='" + getAttendeeType() + "'" +
            ", specialRequirements='" + getSpecialRequirements() + "'" +
            ", emergencyContactName='" + getEmergencyContactName() + "'" +
            ", emergencyContactPhone='" + getEmergencyContactPhone() + "'" +
            ", checkInStatus='" + getCheckInStatus() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", totalNumberOfGuests=" + getTotalNumberOfGuests() +
            ", numberOfGuestsCheckedIn=" + getNumberOfGuestsCheckedIn() +
            ", notes='" + getNotes() + "'" +
            ", qrCodeData='" + getQrCodeData() + "'" +
            ", qrCodeGenerated='" + getQrCodeGenerated() + "'" +
            ", qrCodeGeneratedAt='" + getQrCodeGeneratedAt() + "'" +
            ", dietaryRestrictions='" + getDietaryRestrictions() + "'" +
            ", accessibilityNeeds='" + getAccessibilityNeeds() + "'" +
            ", emergencyContactRelationship='" + getEmergencyContactRelationship() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            ", attendanceRating=" + getAttendanceRating() +
            ", feedback='" + getFeedback() + "'" +
            ", registrationSource='" + getRegistrationSource() + "'" +
            ", waitListPosition=" + getWaitListPosition() +
            ", priorityScore=" + getPriorityScore() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
