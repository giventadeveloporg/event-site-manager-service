package com.nextjstemplate.service.criteria;

import com.nextjstemplate.domain.enumeration.*;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventCompetition} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter name;

    private StringFilter description;

    private Filter<CompetitionType> competitionType;

    private Filter<CompetitionEligibleAudience> eligibleAudience;

    private StringFilter categoryCode;

    private StringFilter divisionLabel;

    private StringFilter track;

    private RangeFilter<java.math.BigDecimal> feeAmount;

    private IntegerFilter maxParticipants;

    private IntegerFilter minGroupSize;

    private IntegerFilter maxGroupSize;

    private IntegerFilter timeLimitMinutes;

    private BooleanFilter requiresSoundtrack;

    private StringFilter judgmentCriteriaJson;

    private IntegerFilter displayOrder;

    private BooleanFilter isActive;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter competitionDayId;

    private Boolean distinct;

    public EventCompetitionCriteria() {}

    public EventCompetitionCriteria(EventCompetitionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.competitionType = other.competitionType == null ? null : other.competitionType.copy();
        this.eligibleAudience = other.eligibleAudience == null ? null : other.eligibleAudience.copy();
        this.categoryCode = other.categoryCode == null ? null : other.categoryCode.copy();
        this.divisionLabel = other.divisionLabel == null ? null : other.divisionLabel.copy();
        this.track = other.track == null ? null : other.track.copy();
        this.feeAmount = other.feeAmount == null ? null : other.feeAmount.copy();
        this.maxParticipants = other.maxParticipants == null ? null : other.maxParticipants.copy();
        this.minGroupSize = other.minGroupSize == null ? null : other.minGroupSize.copy();
        this.maxGroupSize = other.maxGroupSize == null ? null : other.maxGroupSize.copy();
        this.timeLimitMinutes = other.timeLimitMinutes == null ? null : other.timeLimitMinutes.copy();
        this.requiresSoundtrack = other.requiresSoundtrack == null ? null : other.requiresSoundtrack.copy();
        this.judgmentCriteriaJson = other.judgmentCriteriaJson == null ? null : other.judgmentCriteriaJson.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.competitionDayId = other.competitionDayId == null ? null : other.competitionDayId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionCriteria copy() {
        return new EventCompetitionCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public Filter<CompetitionType> getCompetitionType() {
        return competitionType;
    }

    public Filter<CompetitionType> competitionType() {
        if (competitionType == null) {
            competitionType = new Filter<CompetitionType>();
        }
        return competitionType;
    }

    public void setCompetitionType(Filter<CompetitionType> competitionType) {
        this.competitionType = competitionType;
    }

    public Filter<CompetitionEligibleAudience> getEligibleAudience() {
        return eligibleAudience;
    }

    public Filter<CompetitionEligibleAudience> eligibleAudience() {
        if (eligibleAudience == null) {
            eligibleAudience = new Filter<CompetitionEligibleAudience>();
        }
        return eligibleAudience;
    }

    public void setEligibleAudience(Filter<CompetitionEligibleAudience> eligibleAudience) {
        this.eligibleAudience = eligibleAudience;
    }

    public StringFilter getCategoryCode() {
        return categoryCode;
    }

    public StringFilter categoryCode() {
        if (categoryCode == null) {
            categoryCode = new StringFilter();
        }
        return categoryCode;
    }

    public void setCategoryCode(StringFilter categoryCode) {
        this.categoryCode = categoryCode;
    }

    public StringFilter getDivisionLabel() {
        return divisionLabel;
    }

    public StringFilter divisionLabel() {
        if (divisionLabel == null) {
            divisionLabel = new StringFilter();
        }
        return divisionLabel;
    }

    public void setDivisionLabel(StringFilter divisionLabel) {
        this.divisionLabel = divisionLabel;
    }

    public StringFilter getTrack() {
        return track;
    }

    public StringFilter track() {
        if (track == null) {
            track = new StringFilter();
        }
        return track;
    }

    public void setTrack(StringFilter track) {
        this.track = track;
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

    public IntegerFilter getMaxParticipants() {
        return maxParticipants;
    }

    public IntegerFilter maxParticipants() {
        if (maxParticipants == null) {
            maxParticipants = new IntegerFilter();
        }
        return maxParticipants;
    }

    public void setMaxParticipants(IntegerFilter maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public IntegerFilter getMinGroupSize() {
        return minGroupSize;
    }

    public IntegerFilter minGroupSize() {
        if (minGroupSize == null) {
            minGroupSize = new IntegerFilter();
        }
        return minGroupSize;
    }

    public void setMinGroupSize(IntegerFilter minGroupSize) {
        this.minGroupSize = minGroupSize;
    }

    public IntegerFilter getMaxGroupSize() {
        return maxGroupSize;
    }

    public IntegerFilter maxGroupSize() {
        if (maxGroupSize == null) {
            maxGroupSize = new IntegerFilter();
        }
        return maxGroupSize;
    }

    public void setMaxGroupSize(IntegerFilter maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public IntegerFilter getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public IntegerFilter timeLimitMinutes() {
        if (timeLimitMinutes == null) {
            timeLimitMinutes = new IntegerFilter();
        }
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(IntegerFilter timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public BooleanFilter getRequiresSoundtrack() {
        return requiresSoundtrack;
    }

    public BooleanFilter requiresSoundtrack() {
        if (requiresSoundtrack == null) {
            requiresSoundtrack = new BooleanFilter();
        }
        return requiresSoundtrack;
    }

    public void setRequiresSoundtrack(BooleanFilter requiresSoundtrack) {
        this.requiresSoundtrack = requiresSoundtrack;
    }

    public StringFilter getJudgmentCriteriaJson() {
        return judgmentCriteriaJson;
    }

    public StringFilter judgmentCriteriaJson() {
        if (judgmentCriteriaJson == null) {
            judgmentCriteriaJson = new StringFilter();
        }
        return judgmentCriteriaJson;
    }

    public void setJudgmentCriteriaJson(StringFilter judgmentCriteriaJson) {
        this.judgmentCriteriaJson = judgmentCriteriaJson;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            displayOrder = new IntegerFilter();
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
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

    public LongFilter getCompetitionDayId() {
        return competitionDayId;
    }

    public LongFilter competitionDayId() {
        if (competitionDayId == null) {
            competitionDayId = new LongFilter();
        }
        return competitionDayId;
    }

    public void setCompetitionDayId(LongFilter competitionDayId) {
        this.competitionDayId = competitionDayId;
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
        final EventCompetitionCriteria that = (EventCompetitionCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
