package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventAttendee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String phone;

    private Boolean isMember;

    @NotNull
    private Long eventId;

    private Long userId;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    private String registrationStatus;

    @NotNull
    private ZonedDateTime registrationDate;

    private ZonedDateTime confirmationDate;

    private ZonedDateTime cancellationDate;

    @Size(max = 500)
    private String cancellationReason;

    @Size(max = 50)
    private String attendeeType;

    @Size(max = 1000)
    private String specialRequirements;

    @Size(max = 255)
    private String emergencyContactName;

    @Size(max = 50)
    private String emergencyContactPhone;

    @Size(max = 50)
    private String checkInStatus;

    private ZonedDateTime checkInTime;

    private Integer totalNumberOfGuests;

    private Integer numberOfGuestsCheckedIn;

    @Size(max = 1000)
    private String notes;

    @Size(max = 1000)
    private String qrCodeData;

    private Boolean qrCodeGenerated;

    private ZonedDateTime qrCodeGeneratedAt;

    @Size(max = 1000)
    private String dietaryRestrictions;

    @Size(max = 1000)
    private String accessibilityNeeds;

    @Size(max = 100)
    private String emergencyContactRelationship;

    private ZonedDateTime checkOutTime;

    @Min(value = 1)
    @Max(value = 5)
    private Integer attendanceRating;

    @Size(max = 1000)
    private String feedback;

    @Size(max = 100)
    private String registrationSource;

    @Min(value = 1)
    private Integer waitListPosition;

    private Integer priorityScore;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private UserProfileDTO attendee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(Boolean isMember) {
        this.isMember = isMember;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public ZonedDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(ZonedDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(ZonedDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(ZonedDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getAttendeeType() {
        return attendeeType;
    }

    public void setAttendeeType(String attendeeType) {
        this.attendeeType = attendeeType;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Integer getTotalNumberOfGuests() {
        return totalNumberOfGuests;
    }

    public void setTotalNumberOfGuests(Integer totalNumberOfGuests) {
        this.totalNumberOfGuests = totalNumberOfGuests;
    }

    public Integer getNumberOfGuestsCheckedIn() {
        return numberOfGuestsCheckedIn;
    }

    public void setNumberOfGuestsCheckedIn(Integer numberOfGuestsCheckedIn) {
        this.numberOfGuestsCheckedIn = numberOfGuestsCheckedIn;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public Boolean getQrCodeGenerated() {
        return qrCodeGenerated;
    }

    public void setQrCodeGenerated(Boolean qrCodeGenerated) {
        this.qrCodeGenerated = qrCodeGenerated;
    }

    public ZonedDateTime getQrCodeGeneratedAt() {
        return qrCodeGeneratedAt;
    }

    public void setQrCodeGeneratedAt(ZonedDateTime qrCodeGeneratedAt) {
        this.qrCodeGeneratedAt = qrCodeGeneratedAt;
    }

    public String getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public String getAccessibilityNeeds() {
        return accessibilityNeeds;
    }

    public void setAccessibilityNeeds(String accessibilityNeeds) {
        this.accessibilityNeeds = accessibilityNeeds;
    }

    public String getEmergencyContactRelationship() {
        return emergencyContactRelationship;
    }

    public void setEmergencyContactRelationship(String emergencyContactRelationship) {
        this.emergencyContactRelationship = emergencyContactRelationship;
    }

    public ZonedDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getAttendanceRating() {
        return attendanceRating;
    }

    public void setAttendanceRating(Integer attendanceRating) {
        this.attendanceRating = attendanceRating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRegistrationSource() {
        return registrationSource;
    }

    public void setRegistrationSource(String registrationSource) {
        this.registrationSource = registrationSource;
    }

    public Integer getWaitListPosition() {
        return waitListPosition;
    }

    public void setWaitListPosition(Integer waitListPosition) {
        this.waitListPosition = waitListPosition;
    }

    public Integer getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(Integer priorityScore) {
        this.priorityScore = priorityScore;
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

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendeeDTO)) {
            return false;
        }

        EventAttendeeDTO eventAttendeeDTO = (EventAttendeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventAttendeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeDTO{" +
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
