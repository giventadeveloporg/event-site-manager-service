package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.EventScoreCardDetail} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.EventScoreCardDetailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-score-card-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventScoreCardDetailCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter teamName;

    private StringFilter playerName;

    private IntegerFilter points;

    private StringFilter remarks;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter scoreCardId;

    private Boolean distinct;

    public EventScoreCardDetailCriteria() {}

    public EventScoreCardDetailCriteria(EventScoreCardDetailCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.teamName = other.teamName == null ? null : other.teamName.copy();
        this.playerName = other.playerName == null ? null : other.playerName.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.remarks = other.remarks == null ? null : other.remarks.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.scoreCardId = other.scoreCardId == null ? null : other.scoreCardId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventScoreCardDetailCriteria copy() {
        return new EventScoreCardDetailCriteria(this);
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

    public StringFilter getTeamName() {
        return teamName;
    }

    public StringFilter teamName() {
        if (teamName == null) {
            teamName = new StringFilter();
        }
        return teamName;
    }

    public void setTeamName(StringFilter teamName) {
        this.teamName = teamName;
    }

    public StringFilter getPlayerName() {
        return playerName;
    }

    public StringFilter playerName() {
        if (playerName == null) {
            playerName = new StringFilter();
        }
        return playerName;
    }

    public void setPlayerName(StringFilter playerName) {
        this.playerName = playerName;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public IntegerFilter points() {
        if (points == null) {
            points = new IntegerFilter();
        }
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
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

    public LongFilter getScoreCardId() {
        return scoreCardId;
    }

    public LongFilter scoreCardId() {
        if (scoreCardId == null) {
            scoreCardId = new LongFilter();
        }
        return scoreCardId;
    }

    public void setScoreCardId(LongFilter scoreCardId) {
        this.scoreCardId = scoreCardId;
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
        final EventScoreCardDetailCriteria that = (EventScoreCardDetailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(teamName, that.teamName) &&
            Objects.equals(playerName, that.playerName) &&
            Objects.equals(points, that.points) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(scoreCardId, that.scoreCardId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName, playerName, points, remarks, createdAt, updatedAt, scoreCardId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventScoreCardDetailCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (teamName != null ? "teamName=" + teamName + ", " : "") +
            (playerName != null ? "playerName=" + playerName + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (remarks != null ? "remarks=" + remarks + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (scoreCardId != null ? "scoreCardId=" + scoreCardId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
