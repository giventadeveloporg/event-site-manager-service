package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import com.nextjstemplate.domain.enumeration.*;
import com.nextjstemplate.service.dto.EventDetailsDTO;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventCompetitionSettings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionSettingsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private CompetitionAudienceMode audienceMode;

    @NotNull
    private CompetitionRegistrationMode registrationMode;

    private ZonedDateTime registrationDeadline;

    @NotNull
    private Boolean registrationOpen;

    @NotNull
    private Boolean allowTicketSales;

    @NotNull
    private Integer pointsFirst;

    @NotNull
    private Integer pointsSecond;

    @NotNull
    private Integer pointsThird;

    @NotNull
    private Boolean championEnabled;

    @NotNull
    private Boolean championExcludeGroupPoints;

    private Integer championMaxCategory;

    private CompetitionResultsDisplayMode resultsDisplayMode;

    private String eligibilityText;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

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

    public CompetitionAudienceMode getAudienceMode() {
        return audienceMode;
    }

    public void setAudienceMode(CompetitionAudienceMode audienceMode) {
        this.audienceMode = audienceMode;
    }

    public CompetitionRegistrationMode getRegistrationMode() {
        return registrationMode;
    }

    public void setRegistrationMode(CompetitionRegistrationMode registrationMode) {
        this.registrationMode = registrationMode;
    }

    public ZonedDateTime getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(ZonedDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Boolean getRegistrationOpen() {
        return registrationOpen;
    }

    public void setRegistrationOpen(Boolean registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public Boolean getAllowTicketSales() {
        return allowTicketSales;
    }

    public void setAllowTicketSales(Boolean allowTicketSales) {
        this.allowTicketSales = allowTicketSales;
    }

    public Integer getPointsFirst() {
        return pointsFirst;
    }

    public void setPointsFirst(Integer pointsFirst) {
        this.pointsFirst = pointsFirst;
    }

    public Integer getPointsSecond() {
        return pointsSecond;
    }

    public void setPointsSecond(Integer pointsSecond) {
        this.pointsSecond = pointsSecond;
    }

    public Integer getPointsThird() {
        return pointsThird;
    }

    public void setPointsThird(Integer pointsThird) {
        this.pointsThird = pointsThird;
    }

    public Boolean getChampionEnabled() {
        return championEnabled;
    }

    public void setChampionEnabled(Boolean championEnabled) {
        this.championEnabled = championEnabled;
    }

    public Boolean getChampionExcludeGroupPoints() {
        return championExcludeGroupPoints;
    }

    public void setChampionExcludeGroupPoints(Boolean championExcludeGroupPoints) {
        this.championExcludeGroupPoints = championExcludeGroupPoints;
    }

    public Integer getChampionMaxCategory() {
        return championMaxCategory;
    }

    public void setChampionMaxCategory(Integer championMaxCategory) {
        this.championMaxCategory = championMaxCategory;
    }

    public CompetitionResultsDisplayMode getResultsDisplayMode() {
        return resultsDisplayMode;
    }

    public void setResultsDisplayMode(CompetitionResultsDisplayMode resultsDisplayMode) {
        this.resultsDisplayMode = resultsDisplayMode;
    }

    public String getEligibilityText() {
        return eligibilityText;
    }

    public void setEligibilityText(String eligibilityText) {
        this.eligibilityText = eligibilityText;
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

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionSettingsDTO)) {
            return false;
        }
        EventCompetitionSettingsDTO other = (EventCompetitionSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
