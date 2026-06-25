package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventScoreCardDetail.
 */
@Entity
@Table(name = "event_score_card_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventScoreCardDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "team_name", length = 255, nullable = false)
    private String teamName;

    @Size(max = 255)
    @Column(name = "player_name", length = 255)
    private String playerName;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "remarks")
    private String remarks;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private EventScoreCard scoreCard;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventScoreCardDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public EventScoreCardDetail teamName(String teamName) {
        this.setTeamName(teamName);
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public EventScoreCardDetail playerName(String playerName) {
        this.setPlayerName(playerName);
        return this;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPoints() {
        return this.points;
    }

    public EventScoreCardDetail points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public EventScoreCardDetail remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventScoreCardDetail createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventScoreCardDetail updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventScoreCard getScoreCard() {
        return this.scoreCard;
    }

    public void setScoreCard(EventScoreCard eventScoreCard) {
        this.scoreCard = eventScoreCard;
    }

    public EventScoreCardDetail scoreCard(EventScoreCard eventScoreCard) {
        this.setScoreCard(eventScoreCard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventScoreCardDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((EventScoreCardDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventScoreCardDetail{" +
            "id=" + getId() +
            ", teamName='" + getTeamName() + "'" +
            ", playerName='" + getPlayerName() + "'" +
            ", points=" + getPoints() +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
