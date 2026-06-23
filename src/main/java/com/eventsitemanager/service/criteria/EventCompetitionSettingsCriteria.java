package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.*;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.EventCompetitionSettings} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionSettingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private Filter<CompetitionAudienceMode> audienceMode;

    private Filter<CompetitionRegistrationMode> registrationMode;

    private ZonedDateTimeFilter registrationDeadline;

    private BooleanFilter registrationOpen;

    private BooleanFilter allowTicketSales;

    private IntegerFilter pointsFirst;

    private IntegerFilter pointsSecond;

    private IntegerFilter pointsThird;

    private BooleanFilter championEnabled;

    private BooleanFilter championExcludeGroupPoints;

    private IntegerFilter championMaxCategory;

    private Filter<CompetitionResultsDisplayMode> resultsDisplayMode;

    private StringFilter eligibilityText;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventCompetitionSettingsCriteria() {}

    public EventCompetitionSettingsCriteria(EventCompetitionSettingsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.audienceMode = other.audienceMode == null ? null : other.audienceMode.copy();
        this.registrationMode = other.registrationMode == null ? null : other.registrationMode.copy();
        this.registrationDeadline = other.registrationDeadline == null ? null : other.registrationDeadline.copy();
        this.registrationOpen = other.registrationOpen == null ? null : other.registrationOpen.copy();
        this.allowTicketSales = other.allowTicketSales == null ? null : other.allowTicketSales.copy();
        this.pointsFirst = other.pointsFirst == null ? null : other.pointsFirst.copy();
        this.pointsSecond = other.pointsSecond == null ? null : other.pointsSecond.copy();
        this.pointsThird = other.pointsThird == null ? null : other.pointsThird.copy();
        this.championEnabled = other.championEnabled == null ? null : other.championEnabled.copy();
        this.championExcludeGroupPoints = other.championExcludeGroupPoints == null ? null : other.championExcludeGroupPoints.copy();
        this.championMaxCategory = other.championMaxCategory == null ? null : other.championMaxCategory.copy();
        this.resultsDisplayMode = other.resultsDisplayMode == null ? null : other.resultsDisplayMode.copy();
        this.eligibilityText = other.eligibilityText == null ? null : other.eligibilityText.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionSettingsCriteria copy() {
        return new EventCompetitionSettingsCriteria(this);
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

    public Filter<CompetitionAudienceMode> getAudienceMode() {
        return audienceMode;
    }

    public Filter<CompetitionAudienceMode> audienceMode() {
        if (audienceMode == null) {
            audienceMode = new Filter<CompetitionAudienceMode>();
        }
        return audienceMode;
    }

    public void setAudienceMode(Filter<CompetitionAudienceMode> audienceMode) {
        this.audienceMode = audienceMode;
    }

    public Filter<CompetitionRegistrationMode> getRegistrationMode() {
        return registrationMode;
    }

    public Filter<CompetitionRegistrationMode> registrationMode() {
        if (registrationMode == null) {
            registrationMode = new Filter<CompetitionRegistrationMode>();
        }
        return registrationMode;
    }

    public void setRegistrationMode(Filter<CompetitionRegistrationMode> registrationMode) {
        this.registrationMode = registrationMode;
    }

    public ZonedDateTimeFilter getRegistrationDeadline() {
        return registrationDeadline;
    }

    public ZonedDateTimeFilter registrationDeadline() {
        if (registrationDeadline == null) {
            registrationDeadline = new ZonedDateTimeFilter();
        }
        return registrationDeadline;
    }

    public void setRegistrationDeadline(ZonedDateTimeFilter registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public BooleanFilter getRegistrationOpen() {
        return registrationOpen;
    }

    public BooleanFilter registrationOpen() {
        if (registrationOpen == null) {
            registrationOpen = new BooleanFilter();
        }
        return registrationOpen;
    }

    public void setRegistrationOpen(BooleanFilter registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public BooleanFilter getAllowTicketSales() {
        return allowTicketSales;
    }

    public BooleanFilter allowTicketSales() {
        if (allowTicketSales == null) {
            allowTicketSales = new BooleanFilter();
        }
        return allowTicketSales;
    }

    public void setAllowTicketSales(BooleanFilter allowTicketSales) {
        this.allowTicketSales = allowTicketSales;
    }

    public IntegerFilter getPointsFirst() {
        return pointsFirst;
    }

    public IntegerFilter pointsFirst() {
        if (pointsFirst == null) {
            pointsFirst = new IntegerFilter();
        }
        return pointsFirst;
    }

    public void setPointsFirst(IntegerFilter pointsFirst) {
        this.pointsFirst = pointsFirst;
    }

    public IntegerFilter getPointsSecond() {
        return pointsSecond;
    }

    public IntegerFilter pointsSecond() {
        if (pointsSecond == null) {
            pointsSecond = new IntegerFilter();
        }
        return pointsSecond;
    }

    public void setPointsSecond(IntegerFilter pointsSecond) {
        this.pointsSecond = pointsSecond;
    }

    public IntegerFilter getPointsThird() {
        return pointsThird;
    }

    public IntegerFilter pointsThird() {
        if (pointsThird == null) {
            pointsThird = new IntegerFilter();
        }
        return pointsThird;
    }

    public void setPointsThird(IntegerFilter pointsThird) {
        this.pointsThird = pointsThird;
    }

    public BooleanFilter getChampionEnabled() {
        return championEnabled;
    }

    public BooleanFilter championEnabled() {
        if (championEnabled == null) {
            championEnabled = new BooleanFilter();
        }
        return championEnabled;
    }

    public void setChampionEnabled(BooleanFilter championEnabled) {
        this.championEnabled = championEnabled;
    }

    public BooleanFilter getChampionExcludeGroupPoints() {
        return championExcludeGroupPoints;
    }

    public BooleanFilter championExcludeGroupPoints() {
        if (championExcludeGroupPoints == null) {
            championExcludeGroupPoints = new BooleanFilter();
        }
        return championExcludeGroupPoints;
    }

    public void setChampionExcludeGroupPoints(BooleanFilter championExcludeGroupPoints) {
        this.championExcludeGroupPoints = championExcludeGroupPoints;
    }

    public IntegerFilter getChampionMaxCategory() {
        return championMaxCategory;
    }

    public IntegerFilter championMaxCategory() {
        if (championMaxCategory == null) {
            championMaxCategory = new IntegerFilter();
        }
        return championMaxCategory;
    }

    public void setChampionMaxCategory(IntegerFilter championMaxCategory) {
        this.championMaxCategory = championMaxCategory;
    }

    public Filter<CompetitionResultsDisplayMode> getResultsDisplayMode() {
        return resultsDisplayMode;
    }

    public Filter<CompetitionResultsDisplayMode> resultsDisplayMode() {
        if (resultsDisplayMode == null) {
            resultsDisplayMode = new Filter<CompetitionResultsDisplayMode>();
        }
        return resultsDisplayMode;
    }

    public void setResultsDisplayMode(Filter<CompetitionResultsDisplayMode> resultsDisplayMode) {
        this.resultsDisplayMode = resultsDisplayMode;
    }

    public StringFilter getEligibilityText() {
        return eligibilityText;
    }

    public StringFilter eligibilityText() {
        if (eligibilityText == null) {
            eligibilityText = new StringFilter();
        }
        return eligibilityText;
    }

    public void setEligibilityText(StringFilter eligibilityText) {
        this.eligibilityText = eligibilityText;
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
        final EventCompetitionSettingsCriteria that = (EventCompetitionSettingsCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
