package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.CompetitionGroupMemberRole;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventCompetitionGroupMember} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionGroupMemberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    private CompetitionGroupMemberRole memberRole;

    private Integer sortOrder;

    @NotNull
    private ZonedDateTime createdAt;

    private EventCompetitionRegistrationDTO registration;

    private EventCompetitionParticipantDTO participantProfile;

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

    public CompetitionGroupMemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(CompetitionGroupMemberRole memberRole) {
        this.memberRole = memberRole;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EventCompetitionRegistrationDTO getRegistration() {
        return registration;
    }

    public void setRegistration(EventCompetitionRegistrationDTO registration) {
        this.registration = registration;
    }

    public EventCompetitionParticipantDTO getParticipantProfile() {
        return participantProfile;
    }

    public void setParticipantProfile(EventCompetitionParticipantDTO participantProfile) {
        this.participantProfile = participantProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCompetitionGroupMemberDTO)) {
            return false;
        }
        EventCompetitionGroupMemberDTO other = (EventCompetitionGroupMemberDTO) o;
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
