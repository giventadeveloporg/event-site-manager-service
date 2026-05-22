package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import com.nextjstemplate.domain.enumeration.*;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventCompetitionParticipant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionParticipantDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private CompetitionParticipantType participantType;

    @NotNull
    @Size(max = 255)
    private String clerkUserId;

    @NotNull
    @Size(max = 100)
    private String firstName;

    @NotNull
    @Size(max = 100)
    private String lastName;

    @Size(max = 200)
    private String displayName;

    private LocalDate dateOfBirth;

    private Integer currentGrade;

    @Size(max = 255)
    private String schoolName;

    @Size(max = 50)
    private String phone;

    @Size(max = 255)
    private String email;

    @NotNull
    private Boolean isActive;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private UserProfileDTO userProfile;

    private UserProfileDTO guardianUserProfile;

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

    public CompetitionParticipantType getParticipantType() {
        return participantType;
    }

    public void setParticipantType(CompetitionParticipantType participantType) {
        this.participantType = participantType;
    }

    public String getClerkUserId() {
        return clerkUserId;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(Integer currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfileDTO getGuardianUserProfile() {
        return guardianUserProfile;
    }

    public void setGuardianUserProfile(UserProfileDTO guardianUserProfile) {
        this.guardianUserProfile = guardianUserProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionParticipantDTO)) {
            return false;
        }
        EventCompetitionParticipantDTO other = (EventCompetitionParticipantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
