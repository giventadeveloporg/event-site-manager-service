package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Request DTO for atomic team (group leader + members) registration.
 */
public class TeamRegistrationRequestDTO implements Serializable {

    @NotNull
    private EventCompetitionRegistrationDTO leaderRegistration;

    private List<Long> memberParticipantIds;

    private String teamName;

    private String teamDisplayName;

    private List<EventCompetitionGroupMemberDTO> groupMembers;

    public EventCompetitionRegistrationDTO getLeaderRegistration() {
        return leaderRegistration;
    }

    public void setLeaderRegistration(EventCompetitionRegistrationDTO leaderRegistration) {
        this.leaderRegistration = leaderRegistration;
    }

    public List<Long> getMemberParticipantIds() {
        return memberParticipantIds;
    }

    public void setMemberParticipantIds(List<Long> memberParticipantIds) {
        this.memberParticipantIds = memberParticipantIds;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDisplayName() {
        return teamDisplayName;
    }

    public void setTeamDisplayName(String teamDisplayName) {
        this.teamDisplayName = teamDisplayName;
    }

    public List<EventCompetitionGroupMemberDTO> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<EventCompetitionGroupMemberDTO> groupMembers) {
        this.groupMembers = groupMembers;
    }
}
