package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.UserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String userId;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    private String addressLine1;

    @Size(max = 255)
    private String addressLine2;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String state;

    @Size(max = 255)
    private String zipCode;

    @Size(max = 255)
    private String country;

    @Size(max = 255)
    private String notes;

    @Size(max = 255)
    private String familyName;

    @Size(max = 255)
    private String cityTown;

    @Size(max = 255)
    private String district;

    @Size(max = 255)
    private String educationalInstitution;

    @Size(max = 255)
    private String profileImageUrl;

    private Boolean isEmailSubscribed;

    @Size(max = 2048)
    private String emailSubscriptionToken;

    private Boolean isEmailSubscriptionTokenUsed;

    @Size(max = 50)
    private String userStatus;

    @Size(max = 50)
    private String userRole;

    private LocalDate reviewedByAdminAt;

    @Size(max = 255)
    private String requestId;

    @Size(max = 1000)
    private String requestReason;

    @Size(max = 50)
    private String status;

    @Size(max = 1000)
    private String adminComments;

    private ZonedDateTime submittedAt;

    private ZonedDateTime reviewedAt;

    private ZonedDateTime approvedAt;

    private ZonedDateTime rejectedAt;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private UserProfileDTO reviewedByAdmin;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCityTown() {
        return cityTown;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEducationalInstitution() {
        return educationalInstitution;
    }

    public void setEducationalInstitution(String educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Boolean getIsEmailSubscribed() {
        return isEmailSubscribed;
    }

    public void setIsEmailSubscribed(Boolean isEmailSubscribed) {
        this.isEmailSubscribed = isEmailSubscribed;
    }

    public String getEmailSubscriptionToken() {
        return emailSubscriptionToken;
    }

    public void setEmailSubscriptionToken(String emailSubscriptionToken) {
        this.emailSubscriptionToken = emailSubscriptionToken;
    }

    public Boolean getIsEmailSubscriptionTokenUsed() {
        return isEmailSubscriptionTokenUsed;
    }

    public void setIsEmailSubscriptionTokenUsed(Boolean isEmailSubscriptionTokenUsed) {
        this.isEmailSubscriptionTokenUsed = isEmailSubscriptionTokenUsed;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public LocalDate getReviewedByAdminAt() {
        return reviewedByAdminAt;
    }

    public void setReviewedByAdminAt(LocalDate reviewedByAdminAt) {
        this.reviewedByAdminAt = reviewedByAdminAt;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public ZonedDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(ZonedDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ZonedDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(ZonedDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public ZonedDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(ZonedDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
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

    public UserProfileDTO getReviewedByAdmin() {
        return reviewedByAdmin;
    }

    public void setReviewedByAdmin(UserProfileDTO reviewedByAdmin) {
        this.reviewedByAdmin = reviewedByAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            ", addressLine2='" + getAddressLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", notes='" + getNotes() + "'" +
            ", familyName='" + getFamilyName() + "'" +
            ", cityTown='" + getCityTown() + "'" +
            ", district='" + getDistrict() + "'" +
            ", educationalInstitution='" + getEducationalInstitution() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", isEmailSubscribed='" + getIsEmailSubscribed() + "'" +
            ", emailSubscriptionToken='" + getEmailSubscriptionToken() + "'" +
            ", isEmailSubscriptionTokenUsed='" + getIsEmailSubscriptionTokenUsed() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            ", userRole='" + getUserRole() + "'" +
            ", reviewedByAdminAt='" + getReviewedByAdminAt() + "'" +
            ", requestId='" + getRequestId() + "'" +
            ", requestReason='" + getRequestReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", adminComments='" + getAdminComments() + "'" +
            ", submittedAt='" + getSubmittedAt() + "'" +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", rejectedAt='" + getRejectedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", reviewedByAdmin=" + getReviewedByAdmin() +
            "}";
    }
}
