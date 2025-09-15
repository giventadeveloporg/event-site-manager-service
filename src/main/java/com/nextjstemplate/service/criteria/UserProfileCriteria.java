package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.UserProfile} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter userId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zipCode;

    private StringFilter country;

    private StringFilter notes;

    private StringFilter familyName;

    private StringFilter cityTown;

    private StringFilter district;

    private StringFilter educationalInstitution;

    private StringFilter profileImageUrl;

    private BooleanFilter isEmailSubscribed;
    private StringFilter emailSubscriptionToken;

    private BooleanFilter isEmailSubscriptionTokenUsed;

    private StringFilter userStatus;

    private StringFilter userRole;

    private LocalDateFilter reviewedByAdminAt;

    private StringFilter requestId;

    private StringFilter requestReason;

    private StringFilter status;

    private StringFilter adminComments;

    private ZonedDateTimeFilter submittedAt;

    private ZonedDateTimeFilter reviewedAt;

    private ZonedDateTimeFilter approvedAt;

    private ZonedDateTimeFilter rejectedAt;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter reviewedByAdminId;

    private LongFilter userSubscriptionId;

    private Boolean distinct;

    public UserProfileCriteria() {}

    public UserProfileCriteria(UserProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zipCode = other.zipCode == null ? null : other.zipCode.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.familyName = other.familyName == null ? null : other.familyName.copy();
        this.cityTown = other.cityTown == null ? null : other.cityTown.copy();
        this.district = other.district == null ? null : other.district.copy();
        this.educationalInstitution = other.educationalInstitution == null ? null : other.educationalInstitution.copy();
        this.profileImageUrl = other.profileImageUrl == null ? null : other.profileImageUrl.copy();
        this.isEmailSubscribed = other.isEmailSubscribed == null ? null : other.isEmailSubscribed.copy();
        this.emailSubscriptionToken = other.emailSubscriptionToken == null ? null : other.emailSubscriptionToken.copy();
        this.isEmailSubscriptionTokenUsed = other.isEmailSubscriptionTokenUsed == null ? null : other.isEmailSubscriptionTokenUsed.copy();
        this.userStatus = other.userStatus == null ? null : other.userStatus.copy();
        this.userRole = other.userRole == null ? null : other.userRole.copy();
        this.reviewedByAdminAt = other.reviewedByAdminAt == null ? null : other.reviewedByAdminAt.copy();
        this.requestId = other.requestId == null ? null : other.requestId.copy();
        this.requestReason = other.requestReason == null ? null : other.requestReason.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.adminComments = other.adminComments == null ? null : other.adminComments.copy();
        this.submittedAt = other.submittedAt == null ? null : other.submittedAt.copy();
        this.reviewedAt = other.reviewedAt == null ? null : other.reviewedAt.copy();
        this.approvedAt = other.approvedAt == null ? null : other.approvedAt.copy();
        this.rejectedAt = other.rejectedAt == null ? null : other.rejectedAt.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.reviewedByAdminId = other.reviewedByAdminId == null ? null : other.reviewedByAdminId.copy();
        this.userSubscriptionId = other.userSubscriptionId == null ? null : other.userSubscriptionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
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

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public StringFilter addressLine1() {
        if (addressLine1 == null) {
            addressLine1 = new StringFilter();
        }
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public StringFilter addressLine2() {
        if (addressLine2 == null) {
            addressLine2 = new StringFilter();
        }
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public StringFilter zipCode() {
        if (zipCode == null) {
            zipCode = new StringFilter();
        }
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getFamilyName() {
        return familyName;
    }

    public StringFilter familyName() {
        if (familyName == null) {
            familyName = new StringFilter();
        }
        return familyName;
    }

    public void setFamilyName(StringFilter familyName) {
        this.familyName = familyName;
    }

    public StringFilter getCityTown() {
        return cityTown;
    }

    public StringFilter cityTown() {
        if (cityTown == null) {
            cityTown = new StringFilter();
        }
        return cityTown;
    }

    public void setCityTown(StringFilter cityTown) {
        this.cityTown = cityTown;
    }

    public StringFilter getDistrict() {
        return district;
    }

    public StringFilter district() {
        if (district == null) {
            district = new StringFilter();
        }
        return district;
    }

    public void setDistrict(StringFilter district) {
        this.district = district;
    }

    public StringFilter getEducationalInstitution() {
        return educationalInstitution;
    }

    public StringFilter educationalInstitution() {
        if (educationalInstitution == null) {
            educationalInstitution = new StringFilter();
        }
        return educationalInstitution;
    }

    public void setEducationalInstitution(StringFilter educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    public StringFilter getProfileImageUrl() {
        return profileImageUrl;
    }

    public StringFilter profileImageUrl() {
        if (profileImageUrl == null) {
            profileImageUrl = new StringFilter();
        }
        return profileImageUrl;
    }

    public void setProfileImageUrl(StringFilter profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public BooleanFilter getIsEmailSubscribed() {
        return isEmailSubscribed;
    }

    public BooleanFilter isEmailSubscribed() {
        if (isEmailSubscribed == null) {
            isEmailSubscribed = new BooleanFilter();
        }
        return isEmailSubscribed;
    }

    public void setIsEmailSubscribed(BooleanFilter isEmailSubscribed) {
        this.isEmailSubscribed = isEmailSubscribed;
    }

    public StringFilter getEmailSubscriptionToken() {
        return emailSubscriptionToken;
    }

    public StringFilter emailSubscriptionToken() {
        if (emailSubscriptionToken == null) {
            emailSubscriptionToken = new StringFilter();
        }
        return emailSubscriptionToken;
    }

    public void setEmailSubscriptionToken(StringFilter emailSubscriptionToken) {
        this.emailSubscriptionToken = emailSubscriptionToken;
    }

    public BooleanFilter getIsEmailSubscriptionTokenUsed() {
        return isEmailSubscriptionTokenUsed;
    }

    public BooleanFilter isEmailSubscriptionTokenUsed() {
        if (isEmailSubscriptionTokenUsed == null) {
            isEmailSubscriptionTokenUsed = new BooleanFilter();
        }
        return isEmailSubscriptionTokenUsed;
    }

    public void setIsEmailSubscriptionTokenUsed(BooleanFilter isEmailSubscriptionTokenUsed) {
        this.isEmailSubscriptionTokenUsed = isEmailSubscriptionTokenUsed;
    }

    public StringFilter getUserStatus() {
        return userStatus;
    }

    public StringFilter userStatus() {
        if (userStatus == null) {
            userStatus = new StringFilter();
        }
        return userStatus;
    }

    public void setUserStatus(StringFilter userStatus) {
        this.userStatus = userStatus;
    }

    public StringFilter getUserRole() {
        return userRole;
    }

    public StringFilter userRole() {
        if (userRole == null) {
            userRole = new StringFilter();
        }
        return userRole;
    }

    public void setUserRole(StringFilter userRole) {
        this.userRole = userRole;
    }

    public LocalDateFilter getReviewedByAdminAt() {
        return reviewedByAdminAt;
    }

    public LocalDateFilter reviewedByAdminAt() {
        if (reviewedByAdminAt == null) {
            reviewedByAdminAt = new LocalDateFilter();
        }
        return reviewedByAdminAt;
    }

    public void setReviewedByAdminAt(LocalDateFilter reviewedByAdminAt) {
        this.reviewedByAdminAt = reviewedByAdminAt;
    }

    public StringFilter getRequestId() {
        return requestId;
    }

    public StringFilter requestId() {
        if (requestId == null) {
            requestId = new StringFilter();
        }
        return requestId;
    }

    public void setRequestId(StringFilter requestId) {
        this.requestId = requestId;
    }

    public StringFilter getRequestReason() {
        return requestReason;
    }

    public StringFilter requestReason() {
        if (requestReason == null) {
            requestReason = new StringFilter();
        }
        return requestReason;
    }

    public void setRequestReason(StringFilter requestReason) {
        this.requestReason = requestReason;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getAdminComments() {
        return adminComments;
    }

    public StringFilter adminComments() {
        if (adminComments == null) {
            adminComments = new StringFilter();
        }
        return adminComments;
    }

    public void setAdminComments(StringFilter adminComments) {
        this.adminComments = adminComments;
    }

    public ZonedDateTimeFilter getSubmittedAt() {
        return submittedAt;
    }

    public ZonedDateTimeFilter submittedAt() {
        if (submittedAt == null) {
            submittedAt = new ZonedDateTimeFilter();
        }
        return submittedAt;
    }

    public void setSubmittedAt(ZonedDateTimeFilter submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ZonedDateTimeFilter getReviewedAt() {
        return reviewedAt;
    }

    public ZonedDateTimeFilter reviewedAt() {
        if (reviewedAt == null) {
            reviewedAt = new ZonedDateTimeFilter();
        }
        return reviewedAt;
    }

    public void setReviewedAt(ZonedDateTimeFilter reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public ZonedDateTimeFilter getApprovedAt() {
        return approvedAt;
    }

    public ZonedDateTimeFilter approvedAt() {
        if (approvedAt == null) {
            approvedAt = new ZonedDateTimeFilter();
        }
        return approvedAt;
    }

    public void setApprovedAt(ZonedDateTimeFilter approvedAt) {
        this.approvedAt = approvedAt;
    }

    public ZonedDateTimeFilter getRejectedAt() {
        return rejectedAt;
    }

    public ZonedDateTimeFilter rejectedAt() {
        if (rejectedAt == null) {
            rejectedAt = new ZonedDateTimeFilter();
        }
        return rejectedAt;
    }

    public void setRejectedAt(ZonedDateTimeFilter rejectedAt) {
        this.rejectedAt = rejectedAt;
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

    public LongFilter getReviewedByAdminId() {
        return reviewedByAdminId;
    }

    public LongFilter reviewedByAdminId() {
        if (reviewedByAdminId == null) {
            reviewedByAdminId = new LongFilter();
        }
        return reviewedByAdminId;
    }

    public void setReviewedByAdminId(LongFilter reviewedByAdminId) {
        this.reviewedByAdminId = reviewedByAdminId;
    }

    public LongFilter getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public LongFilter userSubscriptionId() {
        if (userSubscriptionId == null) {
            userSubscriptionId = new LongFilter();
        }
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(LongFilter userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
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
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(country, that.country) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(familyName, that.familyName) &&
            Objects.equals(cityTown, that.cityTown) &&
            Objects.equals(district, that.district) &&
            Objects.equals(educationalInstitution, that.educationalInstitution) &&
            Objects.equals(profileImageUrl, that.profileImageUrl) &&
            Objects.equals(isEmailSubscribed, that.isEmailSubscribed) &&
            Objects.equals(emailSubscriptionToken, that.emailSubscriptionToken) &&
            Objects.equals(isEmailSubscriptionTokenUsed, that.isEmailSubscriptionTokenUsed) &&
            Objects.equals(userStatus, that.userStatus) &&
            Objects.equals(userRole, that.userRole) &&
            Objects.equals(reviewedByAdminAt, that.reviewedByAdminAt) &&
            Objects.equals(requestId, that.requestId) &&
            Objects.equals(requestReason, that.requestReason) &&
            Objects.equals(status, that.status) &&
            Objects.equals(adminComments, that.adminComments) &&
            Objects.equals(submittedAt, that.submittedAt) &&
            Objects.equals(reviewedAt, that.reviewedAt) &&
            Objects.equals(approvedAt, that.approvedAt) &&
            Objects.equals(rejectedAt, that.rejectedAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(reviewedByAdminId, that.reviewedByAdminId) &&
            Objects.equals(userSubscriptionId, that.userSubscriptionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            userId,
            firstName,
            lastName,
            email,
            phone,
            addressLine1,
            addressLine2,
            city,
            state,
            zipCode,
            country,
            notes,
            familyName,
            cityTown,
            district,
            educationalInstitution,
            profileImageUrl,
            isEmailSubscribed,
            emailSubscriptionToken,
            isEmailSubscriptionTokenUsed,
            userStatus,
            userRole,
            reviewedByAdminAt,
            requestId,
            requestReason,
            status,
            adminComments,
            submittedAt,
            reviewedAt,
            approvedAt,
            rejectedAt,
            createdAt,
            updatedAt,
            reviewedByAdminId,
            userSubscriptionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
            (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (zipCode != null ? "zipCode=" + zipCode + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (familyName != null ? "familyName=" + familyName + ", " : "") +
            (cityTown != null ? "cityTown=" + cityTown + ", " : "") +
            (district != null ? "district=" + district + ", " : "") +
            (educationalInstitution != null ? "educationalInstitution=" + educationalInstitution + ", " : "") +
            (profileImageUrl != null ? "profileImageUrl=" + profileImageUrl + ", " : "") +
            (isEmailSubscribed != null ? "isEmailSubscribed=" + isEmailSubscribed + ", " : "") +
            (emailSubscriptionToken != null ? "emailSubscriptionToken=" + emailSubscriptionToken + ", " : "") +
            (isEmailSubscriptionTokenUsed != null ? "isEmailSubscriptionTokenUsed=" + isEmailSubscriptionTokenUsed + ", " : "") +
            (userStatus != null ? "userStatus=" + userStatus + ", " : "") +
            (userRole != null ? "userRole=" + userRole + ", " : "") +
            (reviewedByAdminAt != null ? "reviewedByAdminAt=" + reviewedByAdminAt + ", " : "") +
            (requestId != null ? "requestId=" + requestId + ", " : "") +
            (requestReason != null ? "requestReason=" + requestReason + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (adminComments != null ? "adminComments=" + adminComments + ", " : "") +
            (submittedAt != null ? "submittedAt=" + submittedAt + ", " : "") +
            (reviewedAt != null ? "reviewedAt=" + reviewedAt + ", " : "") +
            (approvedAt != null ? "approvedAt=" + approvedAt + ", " : "") +
            (rejectedAt != null ? "rejectedAt=" + rejectedAt + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (reviewedByAdminId != null ? "reviewedByAdminId=" + reviewedByAdminId + ", " : "") +
            (userSubscriptionId != null ? "userSubscriptionId=" + userSubscriptionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
