package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ExecutiveCommitteeTeamMember;
import com.eventsitemanager.service.dto.ExecutiveCommitteeTeamMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExecutiveCommitteeTeamMember} and its DTO {@link ExecutiveCommitteeTeamMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExecutiveCommitteeTeamMemberMapper extends EntityMapper<ExecutiveCommitteeTeamMemberDTO, ExecutiveCommitteeTeamMember> {}
