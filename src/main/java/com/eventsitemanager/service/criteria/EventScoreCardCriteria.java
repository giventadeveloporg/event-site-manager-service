package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.EventScoreCard} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.EventScoreCardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-score-cards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventScoreCardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter teamAName;

    private StringFilter teamBName;

    private IntegerFilter teamAScore;

    private IntegerFilter teamBScore;

    private StringFilter remarks;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventScoreCardCriteria() {}

    public EventScoreCardCriteria(EventScoreCardCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.teamAName = other.teamAName == null ? null : other.teamAName.copy();
        this.teamBName = other.teamBName == null ? null : other.teamBName.copy();
        this.teamAScore = other.teamAScore == null ? null : other.teamAScore.copy();
        this.teamBScore = other.teamBScore == null ? null : other.teamBScore.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventScoreCardCriteria copy() {
        return new EventScoreCardCriteria(this);
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

    public StringFilter getTeamAName() {
        return teamAName;
    }

    public StringFilter teamAName() {
        if (teamAName == null) {
            teamAName = new StringFilter();
        }
        return teamAName;
    }

    public void setTeamAName(StringFilter teamAName) {
        this.teamAName = teamAName;
    }

    public StringFilter getTeamBName() {
        return teamBName;
    }

    public StringFilter teamBName() {
        if (teamBName == null) {
            teamBName = new StringFilter();
        }
        return teamBName;
    }

    public void setTeamBName(StringFilter teamBName) {
        this.teamBName = teamBName;
    }

    public IntegerFilter getTeamAScore() {
        return teamAScore;
    }

    public IntegerFilter teamAScore() {
        if (teamAScore == null) {
            teamAScore = new IntegerFilter();
        }
        return teamAScore;
    }

    public void setTeamAScore(IntegerFilter teamAScore) {
        this.teamAScore = teamAScore;
    }

    public IntegerFilter getTeamBScore() {
        return teamBScore;
    }

    public IntegerFilter teamBScore() {
        if (teamBScore == null) {
            teamBScore = new IntegerFilter();
        }
        return teamBScore;
    }

    public void setTeamBScore(IntegerFilter teamBScore) {
        this.teamBScore = teamBScore;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public StringFilter remarks() {
        if (remarks == null) {
            remarks = new StringFilter();
        }
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
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
        final EventScoreCardCriteria that = (EventScoreCardCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(teamAName, that.teamAName) &&
            Objects.equals(teamBName, that.teamBName) &&
            Objects.equals(teamAScore, that.teamAScore) &&
            Objects.equals(teamBScore, that.teamBScore) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamAName, teamBName, teamAScore, teamBScore, remarks, createdAt, updatedAt, eventId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventScoreCardCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (teamAName != null ? "teamAName=" + teamAName + ", " : "") +
            (teamBName != null ? "teamBName=" + teamBName + ", " : "") +
            (teamAScore != null ? "teamAScore=" + teamAScore + ", " : "") +
            (teamBScore != null ? "teamBScore=" + teamBScore + ", " : "") +
            (remarks != null ? "remarks=" + remarks + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
