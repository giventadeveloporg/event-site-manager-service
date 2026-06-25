package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventScoreCard.
 */
@Entity
@Table(name = "event_score_card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventScoreCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "team_a_name", length = 255, nullable = false)
    private String teamAName;

    @NotNull
    @Size(max = 255)
    @Column(name = "team_b_name", length = 255, nullable = false)
    private String teamBName;

    @NotNull
    @Column(name = "team_a_score", nullable = false)
    private Integer teamAScore;

    @NotNull
    @Column(name = "team_b_score", nullable = false)
    private Integer teamBScore;

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
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventScoreCard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamAName() {
        return this.teamAName;
    }

    public EventScoreCard teamAName(String teamAName) {
        this.setTeamAName(teamAName);
        return this;
    }

    public void setTeamAName(String teamAName) {
        this.teamAName = teamAName;
    }

    public String getTeamBName() {
        return this.teamBName;
    }

    public EventScoreCard teamBName(String teamBName) {
        this.setTeamBName(teamBName);
        return this;
    }

    public void setTeamBName(String teamBName) {
        this.teamBName = teamBName;
    }

    public Integer getTeamAScore() {
        return this.teamAScore;
    }

    public EventScoreCard teamAScore(Integer teamAScore) {
        this.setTeamAScore(teamAScore);
        return this;
    }

    public void setTeamAScore(Integer teamAScore) {
        this.teamAScore = teamAScore;
    }

    public Integer getTeamBScore() {
        return this.teamBScore;
    }

    public EventScoreCard teamBScore(Integer teamBScore) {
        this.setTeamBScore(teamBScore);
        return this;
    }

    public void setTeamBScore(Integer teamBScore) {
        this.teamBScore = teamBScore;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public EventScoreCard remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventScoreCard createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventScoreCard updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public EventScoreCard event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventScoreCard)) {
            return false;
        }
        return getId() != null && getId().equals(((EventScoreCard) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventScoreCard{" +
            "id=" + getId() +
            ", teamAName='" + getTeamAName() + "'" +
            ", teamBName='" + getTeamBName() + "'" +
            ", teamAScore=" + getTeamAScore() +
            ", teamBScore=" + getTeamBScore() +
            ", remarks='" + getRemarks() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
