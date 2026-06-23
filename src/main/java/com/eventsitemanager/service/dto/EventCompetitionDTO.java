package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.*;
import com.eventsitemanager.service.dto.EventCompetitionDayDTO;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventCompetition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private CompetitionType competitionType;

    @NotNull
    private CompetitionEligibleAudience eligibleAudience;

    @Size(max = 20)
    private String categoryCode;

    @Size(max = 100)
    private String divisionLabel;

    @Size(max = 50)
    private String track;

    @NotNull
    private BigDecimal feeAmount;

    private Integer maxParticipants;

    private Integer minGroupSize;

    private Integer maxGroupSize;

    private Integer timeLimitMinutes;

    @NotNull
    private Boolean requiresSoundtrack;

    private String judgmentCriteriaJson;

    @NotNull
    private Integer displayOrder;

    @NotNull
    private Boolean isActive;

    private CompetitionDisciplineCode disciplineCode;

    private Integer minAge;

    private Integer maxAge;

    private Integer minGrade;

    private Integer maxGrade;

    private Integer maxPlacements;

    private ZonedDateTime registrationDeadline;

    private String rulesMarkdown;

    private Boolean requiresTeamName;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private EventCompetitionDayDTO competitionDay;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public void setCompetitionType(CompetitionType competitionType) {
        this.competitionType = competitionType;
    }

    public CompetitionEligibleAudience getEligibleAudience() {
        return eligibleAudience;
    }

    public void setEligibleAudience(CompetitionEligibleAudience eligibleAudience) {
        this.eligibleAudience = eligibleAudience;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDivisionLabel() {
        return divisionLabel;
    }

    public void setDivisionLabel(String divisionLabel) {
        this.divisionLabel = divisionLabel;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getMinGroupSize() {
        return minGroupSize;
    }

    public void setMinGroupSize(Integer minGroupSize) {
        this.minGroupSize = minGroupSize;
    }

    public Integer getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Boolean getRequiresSoundtrack() {
        return requiresSoundtrack;
    }

    public void setRequiresSoundtrack(Boolean requiresSoundtrack) {
        this.requiresSoundtrack = requiresSoundtrack;
    }

    public String getJudgmentCriteriaJson() {
        return judgmentCriteriaJson;
    }

    public void setJudgmentCriteriaJson(String judgmentCriteriaJson) {
        this.judgmentCriteriaJson = judgmentCriteriaJson;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public CompetitionDisciplineCode getDisciplineCode() {
        return disciplineCode;
    }

    public void setDisciplineCode(CompetitionDisciplineCode disciplineCode) {
        this.disciplineCode = disciplineCode;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(Integer minGrade) {
        this.minGrade = minGrade;
    }

    public Integer getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(Integer maxGrade) {
        this.maxGrade = maxGrade;
    }

    public Integer getMaxPlacements() {
        return maxPlacements;
    }

    public void setMaxPlacements(Integer maxPlacements) {
        this.maxPlacements = maxPlacements;
    }

    public ZonedDateTime getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(ZonedDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getRulesMarkdown() {
        return rulesMarkdown;
    }

    public void setRulesMarkdown(String rulesMarkdown) {
        this.rulesMarkdown = rulesMarkdown;
    }

    public Boolean getRequiresTeamName() {
        return requiresTeamName;
    }

    public void setRequiresTeamName(Boolean requiresTeamName) {
        this.requiresTeamName = requiresTeamName;
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

    public EventCompetitionDayDTO getCompetitionDay() {
        return competitionDay;
    }

    public void setCompetitionDay(EventCompetitionDayDTO competitionDay) {
        this.competitionDay = competitionDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionDTO)) {
            return false;
        }
        EventCompetitionDTO other = (EventCompetitionDTO) o;
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
