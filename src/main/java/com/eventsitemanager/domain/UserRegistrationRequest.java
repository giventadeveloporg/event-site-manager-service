package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserRegistrationRequest.
 */
@Entity
@Table(name = "user_registration_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRegistrationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "request_id", length = 255, nullable = false, unique = true)
    private String requestId;

    @NotNull
    @Size(max = 255)
    @Column(name = "user_id", length = 255, nullable = false)
    private String userId;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "phone", length = 255)
    private String phone;

    @Size(max = 255)
    @Column(name = "address_line_1", length = 255)
    private String addressLine1;

    @Size(max = 255)
    @Column(name = "address_line_2", length = 255)
    private String addressLine2;

    @Size(max = 255)
    @Column(name = "city", length = 255)
    private String city;

    @Size(max = 255)
    @Column(name = "state", length = 255)
    private String state;

    @Size(max = 255)
    @Column(name = "zip_code", length = 255)
    private String zipCode;

    @Size(max = 255)
    @Column(name = "country", length = 255)
    private String country;

    @Size(max = 255)
    @Column(name = "family_name", length = 255)
    private String familyName;

    @Size(max = 255)
    @Column(name = "city_town", length = 255)
    private String cityTown;

    @Size(max = 255)
    @Column(name = "district", length = 255)
    private String district;

    @Size(max = 255)
    @Column(name = "educational_institution", length = 255)
    private String educationalInstitution;

    @Size(max = 255)
    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Size(max = 1000)
    @Column(name = "request_reason", length = 1000)
    private String requestReason;

    @NotNull
    @Size(max = 50)
    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Size(max = 1000)
    @Column(name = "admin_comments", length = 1000)
    private String adminComments;

    @NotNull
    @Column(name = "submitted_at", nullable = false)
    private ZonedDateTime submittedAt;

    @Column(name = "reviewed_at")
    private ZonedDateTime reviewedAt;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Column(name = "rejected_at")
    private ZonedDateTime rejectedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile reviewedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserRegistrationRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public UserRegistrationRequest tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public UserRegistrationRequest requestId(String requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return this.userId;
    }

    public UserRegistrationRequest userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserRegistrationRequest firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserRegistrationRequest lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public UserRegistrationRequest email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserRegistrationRequest phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public UserRegistrationRequest addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public UserRegistrationRequest addressLine2(String addressLine2) {
        this.setAddressLine2(addressLine2);
        return this;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return this.city;
    }

    public UserRegistrationRequest city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public UserRegistrationRequest state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public UserRegistrationRequest zipCode(String zipCode) {
        this.setZipCode(zipCode);
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public UserRegistrationRequest country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public UserRegistrationRequest familyName(String familyName) {
        this.setFamilyName(familyName);
        return this;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getCityTown() {
        return this.cityTown;
    }

    public UserRegistrationRequest cityTown(String cityTown) {
        this.setCityTown(cityTown);
        return this;
    }

    public void setCityTown(String cityTown) {
        this.cityTown = cityTown;
    }

    public String getDistrict() {
        return this.district;
    }

    public UserRegistrationRequest district(String district) {
        this.setDistrict(district);
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEducationalInstitution() {
        return this.educationalInstitution;
    }

    public UserRegistrationRequest educationalInstitution(String educationalInstitution) {
        this.setEducationalInstitution(educationalInstitution);
        return this;
    }

    public void setEducationalInstitution(String educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public UserRegistrationRequest profileImageUrl(String profileImageUrl) {
        this.setProfileImageUrl(profileImageUrl);
        return this;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRequestReason() {
        return this.requestReason;
    }

    public UserRegistrationRequest requestReason(String requestReason) {
        this.setRequestReason(requestReason);
        return this;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getStatus() {
        return this.status;
    }

    public UserRegistrationRequest status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminComments() {
        return this.adminComments;
    }

    public UserRegistrationRequest adminComments(String adminComments) {
        this.setAdminComments(adminComments);
        return this;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public ZonedDateTime getSubmittedAt() {
        return this.submittedAt;
    }

    public UserRegistrationRequest submittedAt(ZonedDateTime submittedAt) {
        this.setSubmittedAt(submittedAt);
        return this;
    }

    public void setSubmittedAt(ZonedDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ZonedDateTime getReviewedAt() {
        return this.reviewedAt;
    }

    public UserRegistrationRequest reviewedAt(ZonedDateTime reviewedAt) {
        this.setReviewedAt(reviewedAt);
        return this;
    }

    public void setReviewedAt(ZonedDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public ZonedDateTime getApprovedAt() {
        return this.approvedAt;
    }

    public UserRegistrationRequest approvedAt(ZonedDateTime approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public void setApprovedAt(ZonedDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public ZonedDateTime getRejectedAt() {
        return this.rejectedAt;
    }

    public UserRegistrationRequest rejectedAt(ZonedDateTime rejectedAt) {
        this.setRejectedAt(rejectedAt);
        return this;
    }

    public void setRejectedAt(ZonedDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public UserRegistrationRequest createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public UserRegistrationRequest updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfile getReviewedBy() {
        return this.reviewedBy;
    }

    public void setReviewedBy(UserProfile userProfile) {
        this.reviewedBy = userProfile;
    }

    public UserRegistrationRequest reviewedBy(UserProfile userProfile) {
        this.setReviewedBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRegistrationRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((UserRegistrationRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRegistrationRequest{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", requestId='" + getRequestId() + "'" +
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
            ", familyName='" + getFamilyName() + "'" +
            ", cityTown='" + getCityTown() + "'" +
            ", district='" + getDistrict() + "'" +
            ", educationalInstitution='" + getEducationalInstitution() + "'" +
            ", profileImageUrl='" + getProfileImageUrl() + "'" +
            ", requestReason='" + getRequestReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", adminComments='" + getAdminComments() + "'" +
            ", submittedAt='" + getSubmittedAt() + "'" +
            ", reviewedAt='" + getReviewedAt() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", rejectedAt='" + getRejectedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
