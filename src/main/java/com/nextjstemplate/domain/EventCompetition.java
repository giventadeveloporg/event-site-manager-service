package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nextjstemplate.domain.EventCompetitionDay;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.enumeration.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetition.
 */
@Entity
@Table(name = "event_competition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id")
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "competition_type", length = 20)
    private CompetitionType competitionType;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "eligible_audience", length = 20)
    private CompetitionEligibleAudience eligibleAudience;

    @Size(max = 20)
    @Column(name = "category_code")
    private String categoryCode;

    @Size(max = 100)
    @Column(name = "division_label")
    private String divisionLabel;

    @Size(max = 50)
    @Column(name = "track")
    private String track;

    @NotNull
    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "min_group_size")
    private Integer minGroupSize;

    @Column(name = "max_group_size")
    private Integer maxGroupSize;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @NotNull
    @Column(name = "requires_soundtrack")
    private Boolean requiresSoundtrack;

    @Lob
    @Column(name = "judgment_criteria_json")
    private String judgmentCriteriaJson;

    @NotNull
    @Column(name = "display_order")
    private Integer displayOrder;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Size(max = 32)
    @Column(name = "discipline_code", length = 32)
    private CompetitionDisciplineCode disciplineCode;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Column(name = "min_grade")
    private Integer minGrade;

    @Column(name = "max_grade")
    private Integer maxGrade;

    @Column(name = "max_placements")
    private Integer maxPlacements;

    @Column(name = "registration_deadline")
    private ZonedDateTime registrationDeadline;

    @Lob
    @Column(name = "rules_markdown")
    private String rulesMarkdown;

    @Column(name = "requires_team_name")
    private Boolean requiresTeamName;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private EventCompetitionDay competitionDay;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetition tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return this.name;
    }

    public EventCompetition name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public EventCompetition description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompetitionType getCompetitionType() {
        return this.competitionType;
    }

    public EventCompetition competitionType(CompetitionType competitionType) {
        this.setCompetitionType(competitionType);
        return this;
    }

    public void setCompetitionType(CompetitionType competitionType) {
        this.competitionType = competitionType;
    }

    public CompetitionEligibleAudience getEligibleAudience() {
        return this.eligibleAudience;
    }

    public EventCompetition eligibleAudience(CompetitionEligibleAudience eligibleAudience) {
        this.setEligibleAudience(eligibleAudience);
        return this;
    }

    public void setEligibleAudience(CompetitionEligibleAudience eligibleAudience) {
        this.eligibleAudience = eligibleAudience;
    }

    public String getCategoryCode() {
        return this.categoryCode;
    }

    public EventCompetition categoryCode(String categoryCode) {
        this.setCategoryCode(categoryCode);
        return this;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDivisionLabel() {
        return this.divisionLabel;
    }

    public EventCompetition divisionLabel(String divisionLabel) {
        this.setDivisionLabel(divisionLabel);
        return this;
    }

    public void setDivisionLabel(String divisionLabel) {
        this.divisionLabel = divisionLabel;
    }

    public String getTrack() {
        return this.track;
    }

    public EventCompetition track(String track) {
        this.setTrack(track);
        return this;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public BigDecimal getFeeAmount() {
        return this.feeAmount;
    }

    public EventCompetition feeAmount(BigDecimal feeAmount) {
        this.setFeeAmount(feeAmount);
        return this;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public Integer getMaxParticipants() {
        return this.maxParticipants;
    }

    public EventCompetition maxParticipants(Integer maxParticipants) {
        this.setMaxParticipants(maxParticipants);
        return this;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getMinGroupSize() {
        return this.minGroupSize;
    }

    public EventCompetition minGroupSize(Integer minGroupSize) {
        this.setMinGroupSize(minGroupSize);
        return this;
    }

    public void setMinGroupSize(Integer minGroupSize) {
        this.minGroupSize = minGroupSize;
    }

    public Integer getMaxGroupSize() {
        return this.maxGroupSize;
    }

    public EventCompetition maxGroupSize(Integer maxGroupSize) {
        this.setMaxGroupSize(maxGroupSize);
        return this;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public Integer getTimeLimitMinutes() {
        return this.timeLimitMinutes;
    }

    public EventCompetition timeLimitMinutes(Integer timeLimitMinutes) {
        this.setTimeLimitMinutes(timeLimitMinutes);
        return this;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Boolean getRequiresSoundtrack() {
        return this.requiresSoundtrack;
    }

    public EventCompetition requiresSoundtrack(Boolean requiresSoundtrack) {
        this.setRequiresSoundtrack(requiresSoundtrack);
        return this;
    }

    public void setRequiresSoundtrack(Boolean requiresSoundtrack) {
        this.requiresSoundtrack = requiresSoundtrack;
    }

    public String getJudgmentCriteriaJson() {
        return this.judgmentCriteriaJson;
    }

    public EventCompetition judgmentCriteriaJson(String judgmentCriteriaJson) {
        this.setJudgmentCriteriaJson(judgmentCriteriaJson);
        return this;
    }

    public void setJudgmentCriteriaJson(String judgmentCriteriaJson) {
        this.judgmentCriteriaJson = judgmentCriteriaJson;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public EventCompetition displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public EventCompetition isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public CompetitionDisciplineCode getDisciplineCode() {
        return this.disciplineCode;
    }

    public EventCompetition disciplineCode(CompetitionDisciplineCode disciplineCode) {
        this.setDisciplineCode(disciplineCode);
        return this;
    }

    public void setDisciplineCode(CompetitionDisciplineCode disciplineCode) {
        this.disciplineCode = disciplineCode;
    }

    public Integer getMinAge() {
        return this.minAge;
    }

    public EventCompetition minAge(Integer minAge) {
        this.setMinAge(minAge);
        return this;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return this.maxAge;
    }

    public EventCompetition maxAge(Integer maxAge) {
        this.setMaxAge(maxAge);
        return this;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getMinGrade() {
        return this.minGrade;
    }

    public EventCompetition minGrade(Integer minGrade) {
        this.setMinGrade(minGrade);
        return this;
    }

    public void setMinGrade(Integer minGrade) {
        this.minGrade = minGrade;
    }

    public Integer getMaxGrade() {
        return this.maxGrade;
    }

    public EventCompetition maxGrade(Integer maxGrade) {
        this.setMaxGrade(maxGrade);
        return this;
    }

    public void setMaxGrade(Integer maxGrade) {
        this.maxGrade = maxGrade;
    }

    public Integer getMaxPlacements() {
        return this.maxPlacements;
    }

    public EventCompetition maxPlacements(Integer maxPlacements) {
        this.setMaxPlacements(maxPlacements);
        return this;
    }

    public void setMaxPlacements(Integer maxPlacements) {
        this.maxPlacements = maxPlacements;
    }

    public ZonedDateTime getRegistrationDeadline() {
        return this.registrationDeadline;
    }

    public EventCompetition registrationDeadline(ZonedDateTime registrationDeadline) {
        this.setRegistrationDeadline(registrationDeadline);
        return this;
    }

    public void setRegistrationDeadline(ZonedDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getRulesMarkdown() {
        return this.rulesMarkdown;
    }

    public EventCompetition rulesMarkdown(String rulesMarkdown) {
        this.setRulesMarkdown(rulesMarkdown);
        return this;
    }

    public void setRulesMarkdown(String rulesMarkdown) {
        this.rulesMarkdown = rulesMarkdown;
    }

    public Boolean getRequiresTeamName() {
        return this.requiresTeamName;
    }

    public EventCompetition requiresTeamName(Boolean requiresTeamName) {
        this.setRequiresTeamName(requiresTeamName);
        return this;
    }

    public void setRequiresTeamName(Boolean requiresTeamName) {
        this.requiresTeamName = requiresTeamName;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetition createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetition updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails event) {
        this.event = event;
    }

    public EventCompetition event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public EventCompetitionDay getCompetitionDay() {
        return this.competitionDay;
    }

    public void setCompetitionDay(EventCompetitionDay competitionDay) {
        this.competitionDay = competitionDay;
    }

    public EventCompetition competitionDay(EventCompetitionDay competitionDay) {
        this.setCompetitionDay(competitionDay);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetition id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetition)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetition) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
