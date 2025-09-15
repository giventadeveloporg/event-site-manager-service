package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventAttendee} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventAttendeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-attendees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phone;

    private BooleanFilter isMember;

    private StringFilter tenantId;

    private StringFilter registrationStatus;

    private ZonedDateTimeFilter registrationDate;

    private ZonedDateTimeFilter confirmationDate;

    private ZonedDateTimeFilter cancellationDate;

    private StringFilter cancellationReason;

    private StringFilter attendeeType;

    private StringFilter specialRequirements;

    private StringFilter emergencyContactName;

    private StringFilter emergencyContactPhone;

    private StringFilter checkInStatus;

    private ZonedDateTimeFilter checkInTime;

    private IntegerFilter totalNumberOfGuests;

    private IntegerFilter numberOfGuestsCheckedIn;
    private StringFilter notes;

    private StringFilter qrCodeData;

    private BooleanFilter qrCodeGenerated;

    private ZonedDateTimeFilter qrCodeGeneratedAt;

    private StringFilter dietaryRestrictions;

    private StringFilter accessibilityNeeds;

    private StringFilter emergencyContactRelationship;

    private ZonedDateTimeFilter checkOutTime;

    private IntegerFilter attendanceRating;

    private StringFilter feedback;

    private StringFilter registrationSource;

    private IntegerFilter waitListPosition;

    private IntegerFilter priorityScore;
    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter userId;

    private Boolean distinct;

    public EventAttendeeCriteria() {}

    public EventAttendeeCriteria(EventAttendeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.isMember = other.isMember == null ? null : other.isMember.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.registrationStatus = other.registrationStatus == null ? null : other.registrationStatus.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.confirmationDate = other.confirmationDate == null ? null : other.confirmationDate.copy();
        this.cancellationDate = other.cancellationDate == null ? null : other.cancellationDate.copy();
        this.cancellationReason = other.cancellationReason == null ? null : other.cancellationReason.copy();
        this.attendeeType = other.attendeeType == null ? null : other.attendeeType.copy();
        this.specialRequirements = other.specialRequirements == null ? null : other.specialRequirements.copy();
        this.emergencyContactName = other.emergencyContactName == null ? null : other.emergencyContactName.copy();
        this.emergencyContactPhone = other.emergencyContactPhone == null ? null : other.emergencyContactPhone.copy();
        this.checkInStatus = other.checkInStatus == null ? null : other.checkInStatus.copy();
        this.checkInTime = other.checkInTime == null ? null : other.checkInTime.copy();
        this.totalNumberOfGuests = other.totalNumberOfGuests == null ? null : other.totalNumberOfGuests.copy();
        this.numberOfGuestsCheckedIn = other.numberOfGuestsCheckedIn == null ? null : other.numberOfGuestsCheckedIn.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.qrCodeData = other.qrCodeData == null ? null : other.qrCodeData.copy();
        this.qrCodeGenerated = other.qrCodeGenerated == null ? null : other.qrCodeGenerated.copy();
        this.qrCodeGeneratedAt = other.qrCodeGeneratedAt == null ? null : other.qrCodeGeneratedAt.copy();
        this.dietaryRestrictions = other.dietaryRestrictions == null ? null : other.dietaryRestrictions.copy();
        this.accessibilityNeeds = other.accessibilityNeeds == null ? null : other.accessibilityNeeds.copy();
        this.emergencyContactRelationship = other.emergencyContactRelationship == null ? null : other.emergencyContactRelationship.copy();
        this.checkOutTime = other.checkOutTime == null ? null : other.checkOutTime.copy();
        this.attendanceRating = other.attendanceRating == null ? null : other.attendanceRating.copy();
        this.feedback = other.feedback == null ? null : other.feedback.copy();
        this.registrationSource = other.registrationSource == null ? null : other.registrationSource.copy();
        this.waitListPosition = other.waitListPosition == null ? null : other.waitListPosition.copy();
        this.priorityScore = other.priorityScore == null ? null : other.priorityScore.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventAttendeeCriteria copy() {
        return new EventAttendeeCriteria(this);
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

    public BooleanFilter getIsMember() {
        return isMember;
    }

    public BooleanFilter isMember() {
        if (isMember == null) {
            isMember = new BooleanFilter();
        }
        return isMember;
    }

    public void setIsMember(BooleanFilter isMember) {
        this.isMember = isMember;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public StringFilter getRegistrationStatus() {
        return registrationStatus;
    }

    public StringFilter registrationStatus() {
        if (registrationStatus == null) {
            registrationStatus = new StringFilter();
        }
        return registrationStatus;
    }

    public void setRegistrationStatus(StringFilter registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public ZonedDateTimeFilter getRegistrationDate() {
        return registrationDate;
    }

    public ZonedDateTimeFilter registrationDate() {
        if (registrationDate == null) {
            registrationDate = new ZonedDateTimeFilter();
        }
        return registrationDate;
    }

    public void setRegistrationDate(ZonedDateTimeFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ZonedDateTimeFilter getConfirmationDate() {
        return confirmationDate;
    }

    public ZonedDateTimeFilter confirmationDate() {
        if (confirmationDate == null) {
            confirmationDate = new ZonedDateTimeFilter();
        }
        return confirmationDate;
    }

    public void setConfirmationDate(ZonedDateTimeFilter confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public ZonedDateTimeFilter getCancellationDate() {
        return cancellationDate;
    }

    public ZonedDateTimeFilter cancellationDate() {
        if (cancellationDate == null) {
            cancellationDate = new ZonedDateTimeFilter();
        }
        return cancellationDate;
    }

    public void setCancellationDate(ZonedDateTimeFilter cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public StringFilter getCancellationReason() {
        return cancellationReason;
    }

    public StringFilter cancellationReason() {
        if (cancellationReason == null) {
            cancellationReason = new StringFilter();
        }
        return cancellationReason;
    }

    public void setCancellationReason(StringFilter cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public StringFilter getAttendeeType() {
        return attendeeType;
    }

    public StringFilter attendeeType() {
        if (attendeeType == null) {
            attendeeType = new StringFilter();
        }
        return attendeeType;
    }

    public void setAttendeeType(StringFilter attendeeType) {
        this.attendeeType = attendeeType;
    }

    public StringFilter getSpecialRequirements() {
        return specialRequirements;
    }

    public StringFilter specialRequirements() {
        if (specialRequirements == null) {
            specialRequirements = new StringFilter();
        }
        return specialRequirements;
    }

    public void setSpecialRequirements(StringFilter specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public StringFilter getEmergencyContactName() {
        return emergencyContactName;
    }

    public StringFilter emergencyContactName() {
        if (emergencyContactName == null) {
            emergencyContactName = new StringFilter();
        }
        return emergencyContactName;
    }

    public void setEmergencyContactName(StringFilter emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public StringFilter getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public StringFilter emergencyContactPhone() {
        if (emergencyContactPhone == null) {
            emergencyContactPhone = new StringFilter();
        }
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(StringFilter emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public StringFilter getCheckInStatus() {
        return checkInStatus;
    }

    public StringFilter checkInStatus() {
        if (checkInStatus == null) {
            checkInStatus = new StringFilter();
        }
        return checkInStatus;
    }

    public void setCheckInStatus(StringFilter checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public ZonedDateTimeFilter getCheckInTime() {
        return checkInTime;
    }

    public ZonedDateTimeFilter checkInTime() {
        if (checkInTime == null) {
            checkInTime = new ZonedDateTimeFilter();
        }
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTimeFilter checkInTime) {
        this.checkInTime = checkInTime;
    }

    public IntegerFilter getTotalNumberOfGuests() {
        return totalNumberOfGuests;
    }

    public IntegerFilter totalNumberOfGuests() {
        if (totalNumberOfGuests == null) {
            totalNumberOfGuests = new IntegerFilter();
        }
        return totalNumberOfGuests;
    }

    public void setTotalNumberOfGuests(IntegerFilter totalNumberOfGuests) {
        this.totalNumberOfGuests = totalNumberOfGuests;
    }

    public IntegerFilter getNumberOfGuestsCheckedIn() {
        return numberOfGuestsCheckedIn;
    }

    public IntegerFilter numberOfGuestsCheckedIn() {
        if (numberOfGuestsCheckedIn == null) {
            numberOfGuestsCheckedIn = new IntegerFilter();
        }
        return numberOfGuestsCheckedIn;
    }

    public void setNumberOfGuestsCheckedIn(IntegerFilter numberOfGuestsCheckedIn) {
        this.numberOfGuestsCheckedIn = numberOfGuestsCheckedIn;
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

    public StringFilter getQrCodeData() {
        return qrCodeData;
    }

    public StringFilter qrCodeData() {
        if (qrCodeData == null) {
            qrCodeData = new StringFilter();
        }
        return qrCodeData;
    }

    public void setQrCodeData(StringFilter qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public BooleanFilter getQrCodeGenerated() {
        return qrCodeGenerated;
    }

    public BooleanFilter qrCodeGenerated() {
        if (qrCodeGenerated == null) {
            qrCodeGenerated = new BooleanFilter();
        }
        return qrCodeGenerated;
    }

    public void setQrCodeGenerated(BooleanFilter qrCodeGenerated) {
        this.qrCodeGenerated = qrCodeGenerated;
    }

    public ZonedDateTimeFilter getQrCodeGeneratedAt() {
        return qrCodeGeneratedAt;
    }

    public ZonedDateTimeFilter qrCodeGeneratedAt() {
        if (qrCodeGeneratedAt == null) {
            qrCodeGeneratedAt = new ZonedDateTimeFilter();
        }
        return qrCodeGeneratedAt;
    }

    public void setQrCodeGeneratedAt(ZonedDateTimeFilter qrCodeGeneratedAt) {
        this.qrCodeGeneratedAt = qrCodeGeneratedAt;
    }

    public StringFilter getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public StringFilter dietaryRestrictions() {
        if (dietaryRestrictions == null) {
            dietaryRestrictions = new StringFilter();
        }
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(StringFilter dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public StringFilter getAccessibilityNeeds() {
        return accessibilityNeeds;
    }

    public StringFilter accessibilityNeeds() {
        if (accessibilityNeeds == null) {
            accessibilityNeeds = new StringFilter();
        }
        return accessibilityNeeds;
    }

    public void setAccessibilityNeeds(StringFilter accessibilityNeeds) {
        this.accessibilityNeeds = accessibilityNeeds;
    }

    public StringFilter getEmergencyContactRelationship() {
        return emergencyContactRelationship;
    }

    public StringFilter emergencyContactRelationship() {
        if (emergencyContactRelationship == null) {
            emergencyContactRelationship = new StringFilter();
        }
        return emergencyContactRelationship;
    }

    public void setEmergencyContactRelationship(StringFilter emergencyContactRelationship) {
        this.emergencyContactRelationship = emergencyContactRelationship;
    }

    public ZonedDateTimeFilter getCheckOutTime() {
        return checkOutTime;
    }

    public ZonedDateTimeFilter checkOutTime() {
        if (checkOutTime == null) {
            checkOutTime = new ZonedDateTimeFilter();
        }
        return checkOutTime;
    }

    public void setCheckOutTime(ZonedDateTimeFilter checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public IntegerFilter getAttendanceRating() {
        return attendanceRating;
    }

    public IntegerFilter attendanceRating() {
        if (attendanceRating == null) {
            attendanceRating = new IntegerFilter();
        }
        return attendanceRating;
    }

    public void setAttendanceRating(IntegerFilter attendanceRating) {
        this.attendanceRating = attendanceRating;
    }

    public StringFilter getFeedback() {
        return feedback;
    }

    public StringFilter feedback() {
        if (feedback == null) {
            feedback = new StringFilter();
        }
        return feedback;
    }

    public void setFeedback(StringFilter feedback) {
        this.feedback = feedback;
    }

    public StringFilter getRegistrationSource() {
        return registrationSource;
    }

    public StringFilter registrationSource() {
        if (registrationSource == null) {
            registrationSource = new StringFilter();
        }
        return registrationSource;
    }

    public void setRegistrationSource(StringFilter registrationSource) {
        this.registrationSource = registrationSource;
    }

    public IntegerFilter getWaitListPosition() {
        return waitListPosition;
    }

    public IntegerFilter waitListPosition() {
        if (waitListPosition == null) {
            waitListPosition = new IntegerFilter();
        }
        return waitListPosition;
    }

    public void setWaitListPosition(IntegerFilter waitListPosition) {
        this.waitListPosition = waitListPosition;
    }

    public IntegerFilter getPriorityScore() {
        return priorityScore;
    }

    public IntegerFilter priorityScore() {
        if (priorityScore == null) {
            priorityScore = new IntegerFilter();
        }
        return priorityScore;
    }

    public void setPriorityScore(IntegerFilter priorityScore) {
        this.priorityScore = priorityScore;
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
        final EventAttendeeCriteria that = (EventAttendeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(isMember, that.isMember) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(registrationStatus, that.registrationStatus) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(confirmationDate, that.confirmationDate) &&
            Objects.equals(cancellationDate, that.cancellationDate) &&
            Objects.equals(cancellationReason, that.cancellationReason) &&
            Objects.equals(attendeeType, that.attendeeType) &&
            Objects.equals(specialRequirements, that.specialRequirements) &&
            Objects.equals(emergencyContactName, that.emergencyContactName) &&
            Objects.equals(emergencyContactPhone, that.emergencyContactPhone) &&
            Objects.equals(checkInStatus, that.checkInStatus) &&
            Objects.equals(checkInTime, that.checkInTime) &&
            Objects.equals(totalNumberOfGuests, that.totalNumberOfGuests) &&
            Objects.equals(numberOfGuestsCheckedIn, that.numberOfGuestsCheckedIn) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(qrCodeData, that.qrCodeData) &&
            Objects.equals(qrCodeGenerated, that.qrCodeGenerated) &&
            Objects.equals(qrCodeGeneratedAt, that.qrCodeGeneratedAt) &&
            Objects.equals(dietaryRestrictions, that.dietaryRestrictions) &&
            Objects.equals(accessibilityNeeds, that.accessibilityNeeds) &&
            Objects.equals(emergencyContactRelationship, that.emergencyContactRelationship) &&
            Objects.equals(checkOutTime, that.checkOutTime) &&
            Objects.equals(attendanceRating, that.attendanceRating) &&
            Objects.equals(feedback, that.feedback) &&
            Objects.equals(registrationSource, that.registrationSource) &&
            Objects.equals(waitListPosition, that.waitListPosition) &&
            Objects.equals(priorityScore, that.priorityScore) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            email,
            phone,
            isMember,
            eventId,
            userId,
            tenantId,
            registrationStatus,
            registrationDate,
            confirmationDate,
            cancellationDate,
            cancellationReason,
            attendeeType,
            specialRequirements,
            emergencyContactName,
            emergencyContactPhone,
            checkInStatus,
            checkInTime,
            totalNumberOfGuests,
            numberOfGuestsCheckedIn,
            notes,
            qrCodeData,
            qrCodeGenerated,
            qrCodeGeneratedAt,
            dietaryRestrictions,
            accessibilityNeeds,
            emergencyContactRelationship,
            checkOutTime,
            attendanceRating,
            feedback,
            registrationSource,
            waitListPosition,
            priorityScore,
            createdAt,
            updatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAttendeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (isMember != null ? "isMember=" + isMember + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (registrationStatus != null ? "registrationStatus=" + registrationStatus + ", " : "") +
            (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
            (confirmationDate != null ? "confirmationDate=" + confirmationDate + ", " : "") +
            (cancellationDate != null ? "cancellationDate=" + cancellationDate + ", " : "") +
            (cancellationReason != null ? "cancellationReason=" + cancellationReason + ", " : "") +
            (attendeeType != null ? "attendeeType=" + attendeeType + ", " : "") +
            (specialRequirements != null ? "specialRequirements=" + specialRequirements + ", " : "") +
            (emergencyContactName != null ? "emergencyContactName=" + emergencyContactName + ", " : "") +
            (emergencyContactPhone != null ? "emergencyContactPhone=" + emergencyContactPhone + ", " : "") +
            (checkInStatus != null ? "checkInStatus=" + checkInStatus + ", " : "") +
            (checkInTime != null ? "checkInTime=" + checkInTime + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (qrCodeData != null ? "qrCodeData=" + qrCodeData + ", " : "") +
            (qrCodeGenerated != null ? "qrCodeGenerated=" + qrCodeGenerated + ", " : "") +
            (qrCodeGeneratedAt != null ? "qrCodeGeneratedAt=" + qrCodeGeneratedAt + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
