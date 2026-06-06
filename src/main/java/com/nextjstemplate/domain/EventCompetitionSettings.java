package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.enumeration.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionSettings.
 */
@Entity
@Table(name = "event_competition_settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionSettings implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "audience_mode", length = 20)
    private CompetitionAudienceMode audienceMode;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "registration_mode", length = 32)
    private CompetitionRegistrationMode registrationMode;

    @Column(name = "registration_deadline")
    private ZonedDateTime registrationDeadline;

    @NotNull
    @Column(name = "registration_open")
    private Boolean registrationOpen;

    @NotNull
    @Column(name = "allow_ticket_sales")
    private Boolean allowTicketSales;

    @NotNull
    @Column(name = "points_first")
    private Integer pointsFirst;

    @NotNull
    @Column(name = "points_second")
    private Integer pointsSecond;

    @NotNull
    @Column(name = "points_third")
    private Integer pointsThird;

    @Column(name = "points_fourth")
    private Integer pointsFourth;

    @Column(name = "default_max_placements")
    private Integer defaultMaxPlacements;

    @NotNull
    @Column(name = "champion_enabled")
    private Boolean championEnabled;

    @NotNull
    @Column(name = "champion_exclude_group_points")
    private Boolean championExcludeGroupPoints;

    @Column(name = "champion_max_category")
    private Integer championMaxCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "results_display_mode", length = 32)
    private CompetitionResultsDisplayMode resultsDisplayMode;

    @Lob
    @Column(name = "eligibility_text")
    private String eligibilityText;

    @Column(name = "winners_published_email_sent_at")
    private ZonedDateTime winnersPublishedEmailSentAt;

    @NotNull
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionSettings tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public CompetitionAudienceMode getAudienceMode() {
        return this.audienceMode;
    }

    public EventCompetitionSettings audienceMode(CompetitionAudienceMode audienceMode) {
        this.setAudienceMode(audienceMode);
        return this;
    }

    public void setAudienceMode(CompetitionAudienceMode audienceMode) {
        this.audienceMode = audienceMode;
    }

    public CompetitionRegistrationMode getRegistrationMode() {
        return this.registrationMode;
    }

    public EventCompetitionSettings registrationMode(CompetitionRegistrationMode registrationMode) {
        this.setRegistrationMode(registrationMode);
        return this;
    }

    public void setRegistrationMode(CompetitionRegistrationMode registrationMode) {
        this.registrationMode = registrationMode;
    }

    public ZonedDateTime getRegistrationDeadline() {
        return this.registrationDeadline;
    }

    public EventCompetitionSettings registrationDeadline(ZonedDateTime registrationDeadline) {
        this.setRegistrationDeadline(registrationDeadline);
        return this;
    }

    public void setRegistrationDeadline(ZonedDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Boolean getRegistrationOpen() {
        return this.registrationOpen;
    }

    public EventCompetitionSettings registrationOpen(Boolean registrationOpen) {
        this.setRegistrationOpen(registrationOpen);
        return this;
    }

    public void setRegistrationOpen(Boolean registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public Boolean getAllowTicketSales() {
        return this.allowTicketSales;
    }

    public EventCompetitionSettings allowTicketSales(Boolean allowTicketSales) {
        this.setAllowTicketSales(allowTicketSales);
        return this;
    }

    public void setAllowTicketSales(Boolean allowTicketSales) {
        this.allowTicketSales = allowTicketSales;
    }

    public Integer getPointsFirst() {
        return this.pointsFirst;
    }

    public EventCompetitionSettings pointsFirst(Integer pointsFirst) {
        this.setPointsFirst(pointsFirst);
        return this;
    }

    public void setPointsFirst(Integer pointsFirst) {
        this.pointsFirst = pointsFirst;
    }

    public Integer getPointsSecond() {
        return this.pointsSecond;
    }

    public EventCompetitionSettings pointsSecond(Integer pointsSecond) {
        this.setPointsSecond(pointsSecond);
        return this;
    }

    public void setPointsSecond(Integer pointsSecond) {
        this.pointsSecond = pointsSecond;
    }

    public Integer getPointsThird() {
        return this.pointsThird;
    }

    public EventCompetitionSettings pointsThird(Integer pointsThird) {
        this.setPointsThird(pointsThird);
        return this;
    }

    public void setPointsThird(Integer pointsThird) {
        this.pointsThird = pointsThird;
    }

    public Integer getPointsFourth() {
        return this.pointsFourth;
    }

    public EventCompetitionSettings pointsFourth(Integer pointsFourth) {
        this.setPointsFourth(pointsFourth);
        return this;
    }

    public void setPointsFourth(Integer pointsFourth) {
        this.pointsFourth = pointsFourth;
    }

    public Integer getDefaultMaxPlacements() {
        return this.defaultMaxPlacements;
    }

    public EventCompetitionSettings defaultMaxPlacements(Integer defaultMaxPlacements) {
        this.setDefaultMaxPlacements(defaultMaxPlacements);
        return this;
    }

    public void setDefaultMaxPlacements(Integer defaultMaxPlacements) {
        this.defaultMaxPlacements = defaultMaxPlacements;
    }

    public Boolean getChampionEnabled() {
        return this.championEnabled;
    }

    public EventCompetitionSettings championEnabled(Boolean championEnabled) {
        this.setChampionEnabled(championEnabled);
        return this;
    }

    public void setChampionEnabled(Boolean championEnabled) {
        this.championEnabled = championEnabled;
    }

    public Boolean getChampionExcludeGroupPoints() {
        return this.championExcludeGroupPoints;
    }

    public EventCompetitionSettings championExcludeGroupPoints(Boolean championExcludeGroupPoints) {
        this.setChampionExcludeGroupPoints(championExcludeGroupPoints);
        return this;
    }

    public void setChampionExcludeGroupPoints(Boolean championExcludeGroupPoints) {
        this.championExcludeGroupPoints = championExcludeGroupPoints;
    }

    public Integer getChampionMaxCategory() {
        return this.championMaxCategory;
    }

    public EventCompetitionSettings championMaxCategory(Integer championMaxCategory) {
        this.setChampionMaxCategory(championMaxCategory);
        return this;
    }

    public void setChampionMaxCategory(Integer championMaxCategory) {
        this.championMaxCategory = championMaxCategory;
    }

    public CompetitionResultsDisplayMode getResultsDisplayMode() {
        return this.resultsDisplayMode;
    }

    public EventCompetitionSettings resultsDisplayMode(CompetitionResultsDisplayMode resultsDisplayMode) {
        this.setResultsDisplayMode(resultsDisplayMode);
        return this;
    }

    public void setResultsDisplayMode(CompetitionResultsDisplayMode resultsDisplayMode) {
        this.resultsDisplayMode = resultsDisplayMode;
    }

    public String getEligibilityText() {
        return this.eligibilityText;
    }

    public EventCompetitionSettings eligibilityText(String eligibilityText) {
        this.setEligibilityText(eligibilityText);
        return this;
    }

    public void setEligibilityText(String eligibilityText) {
        this.eligibilityText = eligibilityText;
    }

    public ZonedDateTime getWinnersPublishedEmailSentAt() {
        return this.winnersPublishedEmailSentAt;
    }

    public EventCompetitionSettings winnersPublishedEmailSentAt(ZonedDateTime winnersPublishedEmailSentAt) {
        this.setWinnersPublishedEmailSentAt(winnersPublishedEmailSentAt);
        return this;
    }

    public void setWinnersPublishedEmailSentAt(ZonedDateTime winnersPublishedEmailSentAt) {
        this.winnersPublishedEmailSentAt = winnersPublishedEmailSentAt;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionSettings createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetitionSettings updatedAt(ZonedDateTime updatedAt) {
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

    public EventCompetitionSettings event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionSettings id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionSettings)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionSettings) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
