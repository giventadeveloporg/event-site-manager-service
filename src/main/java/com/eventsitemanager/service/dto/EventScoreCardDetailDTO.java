package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventScoreCardDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventScoreCardDetailDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String teamName;

    @Size(max = 255)
    private String playerName;

    @NotNull
    private Integer points;

    private String remarks;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventScoreCardDTO scoreCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventScoreCardDTO getScoreCard() {
        return scoreCard;
    }

    public void setScoreCard(EventScoreCardDTO scoreCard) {
        this.scoreCard = scoreCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventScoreCardDetailDTO)) {
            return false;
        }

        EventScoreCardDetailDTO eventScoreCardDetailDTO = (EventScoreCardDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventScoreCardDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventScoreCardDetailDTO{" +
            "id=" + getId() +
            ", teamName='" + getTeamName() + "'" +
            ", playerName='" + getPlayerName() + "'" +
            ", points=" + getPoints() +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", scoreCard=" + getScoreCard() +
            "}";
    }
}
