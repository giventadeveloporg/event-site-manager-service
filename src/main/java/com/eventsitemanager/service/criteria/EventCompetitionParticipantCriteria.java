package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.*;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.EventCompetitionParticipant} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionParticipantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private Filter<CompetitionParticipantType> participantType;

    private StringFilter clerkUserId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter displayName;

    private LocalDateFilter dateOfBirth;

    private IntegerFilter currentGrade;

    private StringFilter schoolName;

    private StringFilter phone;

    private StringFilter email;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter userProfileId;

    private LongFilter guardianUserProfileId;

    private Boolean distinct;

    public EventCompetitionParticipantCriteria() {}

    public EventCompetitionParticipantCriteria(EventCompetitionParticipantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.participantType = other.participantType == null ? null : other.participantType.copy();
        this.clerkUserId = other.clerkUserId == null ? null : other.clerkUserId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.currentGrade = other.currentGrade == null ? null : other.currentGrade.copy();
        this.schoolName = other.schoolName == null ? null : other.schoolName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
        this.guardianUserProfileId = other.guardianUserProfileId == null ? null : other.guardianUserProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionParticipantCriteria copy() {
        return new EventCompetitionParticipantCriteria(this);
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

    public Filter<CompetitionParticipantType> getParticipantType() {
        return participantType;
    }

    public Filter<CompetitionParticipantType> participantType() {
        if (participantType == null) {
            participantType = new Filter<CompetitionParticipantType>();
        }
        return participantType;
    }

    public void setParticipantType(Filter<CompetitionParticipantType> participantType) {
        this.participantType = participantType;
    }

    public StringFilter getClerkUserId() {
        return clerkUserId;
    }

    public StringFilter clerkUserId() {
        if (clerkUserId == null) {
            clerkUserId = new StringFilter();
        }
        return clerkUserId;
    }

    public void setClerkUserId(StringFilter clerkUserId) {
        this.clerkUserId = clerkUserId;
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

    public StringFilter getDisplayName() {
        return displayName;
    }

    public StringFilter displayName() {
        if (displayName == null) {
            displayName = new StringFilter();
        }
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new LocalDateFilter();
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public IntegerFilter getCurrentGrade() {
        return currentGrade;
    }

    public IntegerFilter currentGrade() {
        if (currentGrade == null) {
            currentGrade = new IntegerFilter();
        }
        return currentGrade;
    }

    public void setCurrentGrade(IntegerFilter currentGrade) {
        this.currentGrade = currentGrade;
    }

    public StringFilter getSchoolName() {
        return schoolName;
    }

    public StringFilter schoolName() {
        if (schoolName == null) {
            schoolName = new StringFilter();
        }
        return schoolName;
    }

    public void setSchoolName(StringFilter schoolName) {
        this.schoolName = schoolName;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
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

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            userProfileId = new LongFilter();
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getGuardianUserProfileId() {
        return guardianUserProfileId;
    }

    public LongFilter guardianUserProfileId() {
        if (guardianUserProfileId == null) {
            guardianUserProfileId = new LongFilter();
        }
        return guardianUserProfileId;
    }

    public void setGuardianUserProfileId(LongFilter guardianUserProfileId) {
        this.guardianUserProfileId = guardianUserProfileId;
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
        final EventCompetitionParticipantCriteria that = (EventCompetitionParticipantCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
