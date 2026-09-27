package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.LastMatch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LastMatchDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 500)
    private String homeLogoUrl;

    @NotNull
    @Size(max = 500)
    private String awayLogoUrl;

    @NotNull
    @Size(max = 64)
    private String matchDateLabel;

    @NotNull
    private Integer homeScore;

    @NotNull
    private Integer awayScore;

    @NotNull
    @Size(max = 255)
    private String leagueName;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 32)
    private String matchKind;

    private Integer priorityOrder;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getAwayLogoUrl() {
        return awayLogoUrl;
    }

    public void setAwayLogoUrl(String awayLogoUrl) {
        this.awayLogoUrl = awayLogoUrl;
    }

    public String getMatchDateLabel() {
        return matchDateLabel;
    }

    public void setMatchDateLabel(String matchDateLabel) {
        this.matchDateLabel = matchDateLabel;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatchKind() {
        return matchKind;
    }

    public void setMatchKind(String matchKind) {
        this.matchKind = matchKind;
    }

    public Integer getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(Integer priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LastMatchDTO)) {
            return false;
        }

        LastMatchDTO lastMatchDTO = (LastMatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lastMatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LastMatchDTO{" +
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
