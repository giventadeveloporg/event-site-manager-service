package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.*;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.EventCompetitionRegistration} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionRegistrationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private Filter<CompetitionRegistrationStatus> registrationStatus;

    private RangeFilter<java.math.BigDecimal> feeAmount;

    private StringFilter effectiveCategory;

    private StringFilter stripePaymentIntentId;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter competitionId;

    private LongFilter participantProfileId;

    private LongFilter registeredByUserProfileId;

    private LongFilter groupLeaderRegistrationId;

    private Boolean distinct;

    public EventCompetitionRegistrationCriteria() {}

    public EventCompetitionRegistrationCriteria(EventCompetitionRegistrationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.registrationStatus = other.registrationStatus == null ? null : other.registrationStatus.copy();
        this.feeAmount = other.feeAmount == null ? null : other.feeAmount.copy();
        this.effectiveCategory = other.effectiveCategory == null ? null : other.effectiveCategory.copy();
        this.stripePaymentIntentId = other.stripePaymentIntentId == null ? null : other.stripePaymentIntentId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.competitionId = other.competitionId == null ? null : other.competitionId.copy();
        this.participantProfileId = other.participantProfileId == null ? null : other.participantProfileId.copy();
        this.registeredByUserProfileId = other.registeredByUserProfileId == null ? null : other.registeredByUserProfileId.copy();
        this.groupLeaderRegistrationId = other.groupLeaderRegistrationId == null ? null : other.groupLeaderRegistrationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionRegistrationCriteria copy() {
        return new EventCompetitionRegistrationCriteria(this);
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

    public Filter<CompetitionRegistrationStatus> getRegistrationStatus() {
        return registrationStatus;
    }

    public Filter<CompetitionRegistrationStatus> registrationStatus() {
        if (registrationStatus == null) {
            registrationStatus = new Filter<CompetitionRegistrationStatus>();
        }
        return registrationStatus;
    }

    public void setRegistrationStatus(Filter<CompetitionRegistrationStatus> registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public RangeFilter<java.math.BigDecimal> getFeeAmount() {
        return feeAmount;
    }

    public RangeFilter<java.math.BigDecimal> feeAmount() {
        if (feeAmount == null) {
            feeAmount = new RangeFilter<java.math.BigDecimal>();
        }
        return feeAmount;
    }

    public void setFeeAmount(RangeFilter<java.math.BigDecimal> feeAmount) {
        this.feeAmount = feeAmount;
    }

    public StringFilter getEffectiveCategory() {
        return effectiveCategory;
    }

    public StringFilter effectiveCategory() {
        if (effectiveCategory == null) {
            effectiveCategory = new StringFilter();
        }
        return effectiveCategory;
    }

    public void setEffectiveCategory(StringFilter effectiveCategory) {
        this.effectiveCategory = effectiveCategory;
    }

    public StringFilter getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public StringFilter stripePaymentIntentId() {
        if (stripePaymentIntentId == null) {
            stripePaymentIntentId = new StringFilter();
        }
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(StringFilter stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
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

    public LongFilter getCompetitionId() {
        return competitionId;
    }

    public LongFilter competitionId() {
        if (competitionId == null) {
            competitionId = new LongFilter();
        }
        return competitionId;
    }

    public void setCompetitionId(LongFilter competitionId) {
        this.competitionId = competitionId;
    }

    public LongFilter getParticipantProfileId() {
        return participantProfileId;
    }

    public LongFilter participantProfileId() {
        if (participantProfileId == null) {
            participantProfileId = new LongFilter();
        }
        return participantProfileId;
    }

    public void setParticipantProfileId(LongFilter participantProfileId) {
        this.participantProfileId = participantProfileId;
    }

    public LongFilter getRegisteredByUserProfileId() {
        return registeredByUserProfileId;
    }

    public LongFilter registeredByUserProfileId() {
        if (registeredByUserProfileId == null) {
            registeredByUserProfileId = new LongFilter();
        }
        return registeredByUserProfileId;
    }

    public void setRegisteredByUserProfileId(LongFilter registeredByUserProfileId) {
        this.registeredByUserProfileId = registeredByUserProfileId;
    }

    public LongFilter getGroupLeaderRegistrationId() {
        return groupLeaderRegistrationId;
    }

    public LongFilter groupLeaderRegistrationId() {
        if (groupLeaderRegistrationId == null) {
            groupLeaderRegistrationId = new LongFilter();
        }
        return groupLeaderRegistrationId;
    }

    public void setGroupLeaderRegistrationId(LongFilter groupLeaderRegistrationId) {
        this.groupLeaderRegistrationId = groupLeaderRegistrationId;
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
        final EventCompetitionRegistrationCriteria that = (EventCompetitionRegistrationCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
