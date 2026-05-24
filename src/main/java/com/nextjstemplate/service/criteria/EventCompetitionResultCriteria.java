package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventCompetitionResult} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionResultCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter displayName;

    private IntegerFilter placement;

    private StringFilter placementLabel;

    private StringFilter prizeTitle;

    private StringFilter prizeDetails;

    private IntegerFilter pointsAwarded;

    private StringFilter winnerPhotoUrl;

    private StringFilter notes;

    private BooleanFilter isPublished;

    private ZonedDateTimeFilter publishedAt;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter competitionId;

    private LongFilter participantProfileId;

    private LongFilter registrationId;

    private LongFilter winnerMediaId;

    private Boolean distinct;

    public EventCompetitionResultCriteria() {}

    public EventCompetitionResultCriteria(EventCompetitionResultCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
        this.placement = other.placement == null ? null : other.placement.copy();
        this.placementLabel = other.placementLabel == null ? null : other.placementLabel.copy();
        this.prizeTitle = other.prizeTitle == null ? null : other.prizeTitle.copy();
        this.prizeDetails = other.prizeDetails == null ? null : other.prizeDetails.copy();
        this.pointsAwarded = other.pointsAwarded == null ? null : other.pointsAwarded.copy();
        this.winnerPhotoUrl = other.winnerPhotoUrl == null ? null : other.winnerPhotoUrl.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.isPublished = other.isPublished == null ? null : other.isPublished.copy();
        this.publishedAt = other.publishedAt == null ? null : other.publishedAt.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.competitionId = other.competitionId == null ? null : other.competitionId.copy();
        this.participantProfileId = other.participantProfileId == null ? null : other.participantProfileId.copy();
        this.registrationId = other.registrationId == null ? null : other.registrationId.copy();
        this.winnerMediaId = other.winnerMediaId == null ? null : other.winnerMediaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionResultCriteria copy() {
        return new EventCompetitionResultCriteria(this);
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

    public IntegerFilter getPlacement() {
        return placement;
    }

    public IntegerFilter placement() {
        if (placement == null) {
            placement = new IntegerFilter();
        }
        return placement;
    }

    public void setPlacement(IntegerFilter placement) {
        this.placement = placement;
    }

    public StringFilter getPlacementLabel() {
        return placementLabel;
    }

    public StringFilter placementLabel() {
        if (placementLabel == null) {
            placementLabel = new StringFilter();
        }
        return placementLabel;
    }

    public void setPlacementLabel(StringFilter placementLabel) {
        this.placementLabel = placementLabel;
    }

    public StringFilter getPrizeTitle() {
        return prizeTitle;
    }

    public StringFilter prizeTitle() {
        if (prizeTitle == null) {
            prizeTitle = new StringFilter();
        }
        return prizeTitle;
    }

    public void setPrizeTitle(StringFilter prizeTitle) {
        this.prizeTitle = prizeTitle;
    }

    public StringFilter getPrizeDetails() {
        return prizeDetails;
    }

    public StringFilter prizeDetails() {
        if (prizeDetails == null) {
            prizeDetails = new StringFilter();
        }
        return prizeDetails;
    }

    public void setPrizeDetails(StringFilter prizeDetails) {
        this.prizeDetails = prizeDetails;
    }

    public IntegerFilter getPointsAwarded() {
        return pointsAwarded;
    }

    public IntegerFilter pointsAwarded() {
        if (pointsAwarded == null) {
            pointsAwarded = new IntegerFilter();
        }
        return pointsAwarded;
    }

    public void setPointsAwarded(IntegerFilter pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public StringFilter getWinnerPhotoUrl() {
        return winnerPhotoUrl;
    }

    public StringFilter winnerPhotoUrl() {
        if (winnerPhotoUrl == null) {
            winnerPhotoUrl = new StringFilter();
        }
        return winnerPhotoUrl;
    }

    public void setWinnerPhotoUrl(StringFilter winnerPhotoUrl) {
        this.winnerPhotoUrl = winnerPhotoUrl;
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

    public BooleanFilter getIsPublished() {
        return isPublished;
    }

    public BooleanFilter isPublished() {
        if (isPublished == null) {
            isPublished = new BooleanFilter();
        }
        return isPublished;
    }

    public void setIsPublished(BooleanFilter isPublished) {
        this.isPublished = isPublished;
    }

    public ZonedDateTimeFilter getPublishedAt() {
        return publishedAt;
    }

    public ZonedDateTimeFilter publishedAt() {
        if (publishedAt == null) {
            publishedAt = new ZonedDateTimeFilter();
        }
        return publishedAt;
    }

    public void setPublishedAt(ZonedDateTimeFilter publishedAt) {
        this.publishedAt = publishedAt;
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

    public LongFilter getRegistrationId() {
        return registrationId;
    }

    public LongFilter registrationId() {
        if (registrationId == null) {
            registrationId = new LongFilter();
        }
        return registrationId;
    }

    public void setRegistrationId(LongFilter registrationId) {
        this.registrationId = registrationId;
    }

    public LongFilter getWinnerMediaId() {
        return winnerMediaId;
    }

    public LongFilter winnerMediaId() {
        if (winnerMediaId == null) {
            winnerMediaId = new LongFilter();
        }
        return winnerMediaId;
    }

    public void setWinnerMediaId(LongFilter winnerMediaId) {
        this.winnerMediaId = winnerMediaId;
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
        final EventCompetitionResultCriteria that = (EventCompetitionResultCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
