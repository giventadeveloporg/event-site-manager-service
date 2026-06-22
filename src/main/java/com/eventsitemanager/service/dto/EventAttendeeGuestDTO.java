package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventAttendeeGuest} entity.
 */
@Schema(description = "Event Guest Management JDL Model\nEnhanced entities for sophisticated guest management with age-based pricing")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeGuestDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String phone;

    @Size(max = 20)
    private String ageGroup;

    @Size(max = 50)
    private String relationship;

    @Size(max = 500)
    private String specialRequirements;

    @Size(max = 50)
    private String registrationStatus;

    @Size(max = 50)
    private String checkInStatus;

    private ZonedDateTime checkInTime;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventAttendeeDTO primaryAttendee;

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

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
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

    public EventAttendeeDTO getPrimaryAttendee() {
        return primaryAttendee;
    }

    public void setPrimaryAttendee(EventAttendeeDTO primaryAttendee) {
        this.primaryAttendee = primaryAttendee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAttendeeGuestDTO)) {
            return false;
        }

        EventAttendeeGuestDTO eventAttendeeGuestDTO = (EventAttendeeGuestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventAttendeeGuestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeGuestDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", ageGroup='" + getAgeGroup() + "'" +
            ", relationship='" + getRelationship() + "'" +
            ", specialRequirements='" + getSpecialRequirements() + "'" +
            ", registrationStatus='" + getRegistrationStatus() + "'" +
            ", checkInStatus='" + getCheckInStatus() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", primaryAttendee=" + getPrimaryAttendee() +
            "}";
    }
}
