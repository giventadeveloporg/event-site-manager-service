package com.eventsitemanager.domain;

import com.eventsitemanager.domain.EventCompetition;
import com.eventsitemanager.domain.EventCompetitionParticipant;
import com.eventsitemanager.domain.EventCompetitionRegistration;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventMedia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCompetitionResult.
 */
@Entity
@Table(name = "event_competition_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventCompetitionResultSeq")
    @SequenceGenerator(name = "eventCompetitionResultSeq", sequenceName = "public.event_competition_result_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id")
    private String tenantId;

    @NotNull
    @Size(max = 200)
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "placement")
    private Integer placement;

    @Size(max = 50)
    @Column(name = "placement_label")
    private String placementLabel;

    @Size(max = 255)
    @Column(name = "prize_title")
    private String prizeTitle;

    @Lob
    @Column(name = "prize_details")
    private String prizeDetails;

    @NotNull
    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @Size(max = 1024)
    @Column(name = "winner_photo_url")
    private String winnerPhotoUrl;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "published_at")
    private ZonedDateTime publishedAt;

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
    @JsonIgnoreProperties(value = { "event", "competitionDay" }, allowSetters = true)
    private EventCompetition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userProfile", "guardianUserProfile" }, allowSetters = true)
    private EventCompetitionParticipant participantProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "event", "competition", "participantProfile", "registeredByUserProfile", "groupLeaderRegistration" },
        allowSetters = true
    )
    private EventCompetitionRegistration registration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private EventMedia winnerMedia;

    public String getTenantId() {
        return this.tenantId;
    }

    public EventCompetitionResult tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public EventCompetitionResult displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPlacement() {
        return this.placement;
    }

    public EventCompetitionResult placement(Integer placement) {
        this.setPlacement(placement);
        return this;
    }

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public String getPlacementLabel() {
        return this.placementLabel;
    }

    public EventCompetitionResult placementLabel(String placementLabel) {
        this.setPlacementLabel(placementLabel);
        return this;
    }

    public void setPlacementLabel(String placementLabel) {
        this.placementLabel = placementLabel;
    }

    public String getPrizeTitle() {
        return this.prizeTitle;
    }

    public EventCompetitionResult prizeTitle(String prizeTitle) {
        this.setPrizeTitle(prizeTitle);
        return this;
    }

    public void setPrizeTitle(String prizeTitle) {
        this.prizeTitle = prizeTitle;
    }

    public String getPrizeDetails() {
        return this.prizeDetails;
    }

    public EventCompetitionResult prizeDetails(String prizeDetails) {
        this.setPrizeDetails(prizeDetails);
        return this;
    }

    public void setPrizeDetails(String prizeDetails) {
        this.prizeDetails = prizeDetails;
    }

    public Integer getPointsAwarded() {
        return this.pointsAwarded;
    }

    public EventCompetitionResult pointsAwarded(Integer pointsAwarded) {
        this.setPointsAwarded(pointsAwarded);
        return this;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public String getWinnerPhotoUrl() {
        return this.winnerPhotoUrl;
    }

    public EventCompetitionResult winnerPhotoUrl(String winnerPhotoUrl) {
        this.setWinnerPhotoUrl(winnerPhotoUrl);
        return this;
    }

    public void setWinnerPhotoUrl(String winnerPhotoUrl) {
        this.winnerPhotoUrl = winnerPhotoUrl;
    }

    public String getNotes() {
        return this.notes;
    }

    public EventCompetitionResult notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsPublished() {
        return this.isPublished;
    }

    public EventCompetitionResult isPublished(Boolean isPublished) {
        this.setIsPublished(isPublished);
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public ZonedDateTime getPublishedAt() {
        return this.publishedAt;
    }

    public EventCompetitionResult publishedAt(ZonedDateTime publishedAt) {
        this.setPublishedAt(publishedAt);
        return this;
    }

    public void setPublishedAt(ZonedDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventCompetitionResult createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventCompetitionResult updatedAt(ZonedDateTime updatedAt) {
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

    public EventCompetitionResult event(EventDetails event) {
        this.setEvent(event);
        return this;
    }

    public EventCompetition getCompetition() {
        return this.competition;
    }

    public void setCompetition(EventCompetition competition) {
        this.competition = competition;
    }

    public EventCompetitionResult competition(EventCompetition competition) {
        this.setCompetition(competition);
        return this;
    }

    public EventCompetitionParticipant getParticipantProfile() {
        return this.participantProfile;
    }

    public void setParticipantProfile(EventCompetitionParticipant participantProfile) {
        this.participantProfile = participantProfile;
    }

    public EventCompetitionResult participantProfile(EventCompetitionParticipant participantProfile) {
        this.setParticipantProfile(participantProfile);
        return this;
    }

    public EventCompetitionRegistration getRegistration() {
        return this.registration;
    }

    public void setRegistration(EventCompetitionRegistration registration) {
        this.registration = registration;
    }

    public EventCompetitionResult registration(EventCompetitionRegistration registration) {
        this.setRegistration(registration);
        return this;
    }

    public EventMedia getWinnerMedia() {
        return this.winnerMedia;
    }

    public void setWinnerMedia(EventMedia winnerMedia) {
        this.winnerMedia = winnerMedia;
    }

    public EventCompetitionResult winnerMedia(EventMedia winnerMedia) {
        this.setWinnerMedia(winnerMedia);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCompetitionResult id(Long id) {
        this.setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionResult)) {
            return false;
        }
        return getId() != null && getId().equals(((EventCompetitionResult) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
