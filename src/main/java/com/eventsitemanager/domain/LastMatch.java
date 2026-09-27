package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LastMatch.
 */
@Entity
@Table(name = "last_matches")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LastMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lastMatchesSeq")
    @SequenceGenerator(name = "lastMatchesSeq", sequenceName = "public.last_matches_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 500)
    @Column(name = "home_logo_url", length = 500, nullable = false)
    private String homeLogoUrl;

    @NotNull
    @Size(max = 500)
    @Column(name = "away_logo_url", length = 500, nullable = false)
    private String awayLogoUrl;

    @NotNull
    @Size(max = 64)
    @Column(name = "match_date_label", length = 64, nullable = false)
    private String matchDateLabel;

    @NotNull
    @Column(name = "home_score", nullable = false)
    private Integer homeScore;

    @NotNull
    @Column(name = "away_score", nullable = false)
    private Integer awayScore;

    @NotNull
    @Size(max = 255)
    @Column(name = "league_name", length = 255, nullable = false)
    private String leagueName;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    /**
     * PAST = completed result cards; UPCOMING = scheduled match cards.
     */
    @NotNull
    @Size(max = 32)
    @Column(name = "match_kind", length = 32, nullable = false)
    private String matchKind;

    @Column(name = "priority_order")
    private Integer priorityOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (isActive == null) {
            isActive = true;
        }
        if (matchKind == null || matchKind.isBlank()) {
            matchKind = "PAST";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LastMatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public LastMatch tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getHomeLogoUrl() {
        return this.homeLogoUrl;
    }

    public LastMatch homeLogoUrl(String homeLogoUrl) {
        this.setHomeLogoUrl(homeLogoUrl);
        return this;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getAwayLogoUrl() {
        return this.awayLogoUrl;
    }

    public LastMatch awayLogoUrl(String awayLogoUrl) {
        this.setAwayLogoUrl(awayLogoUrl);
        return this;
    }

    public void setAwayLogoUrl(String awayLogoUrl) {
        this.awayLogoUrl = awayLogoUrl;
    }

    public String getMatchDateLabel() {
        return this.matchDateLabel;
    }

    public LastMatch matchDateLabel(String matchDateLabel) {
        this.setMatchDateLabel(matchDateLabel);
        return this;
    }

    public void setMatchDateLabel(String matchDateLabel) {
        this.matchDateLabel = matchDateLabel;
    }

    public Integer getHomeScore() {
        return this.homeScore;
    }

    public LastMatch homeScore(Integer homeScore) {
        this.setHomeScore(homeScore);
        return this;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return this.awayScore;
    }

    public LastMatch awayScore(Integer awayScore) {
        this.setAwayScore(awayScore);
        return this;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public String getLeagueName() {
        return this.leagueName;
    }

    public LastMatch leagueName(String leagueName) {
        this.setLeagueName(leagueName);
        return this;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getTitle() {
        return this.title;
    }

    public LastMatch title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatchKind() {
        return this.matchKind;
    }

    public LastMatch matchKind(String matchKind) {
        this.setMatchKind(matchKind);
        return this;
    }

    public void setMatchKind(String matchKind) {
        this.matchKind = matchKind;
    }

    public Integer getPriorityOrder() {
        return this.priorityOrder;
    }

    public LastMatch priorityOrder(Integer priorityOrder) {
        this.setPriorityOrder(priorityOrder);
        return this;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public LastMatch isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public LastMatch createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public LastMatch updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LastMatch)) {
            return false;
        }
        return getId() != null && getId().equals(((LastMatch) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LastMatch{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", homeLogoUrl='" + getHomeLogoUrl() + "'" +
            ", awayLogoUrl='" + getAwayLogoUrl() + "'" +
            ", matchDateLabel='" + getMatchDateLabel() + "'" +
            ", homeScore=" + getHomeScore() +
            ", awayScore=" + getAwayScore() +
            ", leagueName='" + getLeagueName() + "'" +
            ", title='" + getTitle() + "'" +
            ", matchKind='" + getMatchKind() + "'" +
            ", priorityOrder=" + getPriorityOrder() +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
