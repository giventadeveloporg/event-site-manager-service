package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.dto.EventMediaDTO;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventCompetitionResult} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionResultDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 200)
    private String displayName;

    private Integer placement;

    @Size(max = 50)
    private String placementLabel;

    @Size(max = 255)
    private String prizeTitle;

    private String prizeDetails;

    @NotNull
    private Integer pointsAwarded;

    @Size(max = 1024)
    private String winnerPhotoUrl;

    private String notes;

    @NotNull
    private Boolean isPublished;

    private ZonedDateTime publishedAt;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private EventCompetitionDTO competition;

    private EventCompetitionParticipantDTO participantProfile;

    private EventCompetitionRegistrationDTO registration;

    private EventMediaDTO winnerMedia;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPlacement() {
        return placement;
    }

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public String getPlacementLabel() {
        return placementLabel;
    }

    public void setPlacementLabel(String placementLabel) {
        this.placementLabel = placementLabel;
    }

    public String getPrizeTitle() {
        return prizeTitle;
    }

    public void setPrizeTitle(String prizeTitle) {
        this.prizeTitle = prizeTitle;
    }

    public String getPrizeDetails() {
        return prizeDetails;
    }

    public void setPrizeDetails(String prizeDetails) {
        this.prizeDetails = prizeDetails;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public String getWinnerPhotoUrl() {
        return winnerPhotoUrl;
    }

    public void setWinnerPhotoUrl(String winnerPhotoUrl) {
        this.winnerPhotoUrl = winnerPhotoUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public ZonedDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(ZonedDateTime publishedAt) {
        this.publishedAt = publishedAt;
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

    public EventCompetitionDTO getCompetition() {
        return competition;
    }

    public void setCompetition(EventCompetitionDTO competition) {
        this.competition = competition;
    }

    public EventCompetitionParticipantDTO getParticipantProfile() {
        return participantProfile;
    }

    public void setParticipantProfile(EventCompetitionParticipantDTO participantProfile) {
        this.participantProfile = participantProfile;
    }

    public EventCompetitionRegistrationDTO getRegistration() {
        return registration;
    }

    public void setRegistration(EventCompetitionRegistrationDTO registration) {
        this.registration = registration;
    }

    public EventMediaDTO getWinnerMedia() {
        return winnerMedia;
    }

    public void setWinnerMedia(EventMediaDTO winnerMedia) {
        this.winnerMedia = winnerMedia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionResultDTO)) {
            return false;
        }
        EventCompetitionResultDTO other = (EventCompetitionResultDTO) o;
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
