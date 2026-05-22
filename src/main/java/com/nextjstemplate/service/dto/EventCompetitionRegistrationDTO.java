package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.math.BigDecimal;
import com.nextjstemplate.domain.enumeration.*;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventCompetitionDTO;
import com.nextjstemplate.service.dto.EventCompetitionParticipantDTO;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventCompetitionRegistration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionRegistrationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private CompetitionRegistrationStatus registrationStatus;

    @NotNull
    private BigDecimal feeAmount;

    @Size(max = 20)
    private String effectiveCategory;

    @Size(max = 255)
    private String stripePaymentIntentId;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private EventCompetitionDTO competition;

    private EventCompetitionParticipantDTO participantProfile;

    private EventCompetitionRegistrationDTO groupLeaderRegistration;

    private UserProfileDTO registeredByUserProfile;

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

    public CompetitionRegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(CompetitionRegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getEffectiveCategory() {
        return effectiveCategory;
    }

    public void setEffectiveCategory(String effectiveCategory) {
        this.effectiveCategory = effectiveCategory;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
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

    public EventCompetitionRegistrationDTO getGroupLeaderRegistration() {
        return groupLeaderRegistration;
    }

    public void setGroupLeaderRegistration(EventCompetitionRegistrationDTO groupLeaderRegistration) {
        this.groupLeaderRegistration = groupLeaderRegistration;
    }

    public UserProfileDTO getRegisteredByUserProfile() {
        return registeredByUserProfile;
    }

    public void setRegisteredByUserProfile(UserProfileDTO registeredByUserProfile) {
        this.registeredByUserProfile = registeredByUserProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionRegistrationDTO)) {
            return false;
        }
        EventCompetitionRegistrationDTO other = (EventCompetitionRegistrationDTO) o;
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
