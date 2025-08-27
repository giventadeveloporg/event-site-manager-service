package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.ExecutiveCommitteeTeamMember;
import com.nextjstemplate.service.dto.ExecutiveCommitteeTeamMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExecutiveCommitteeTeamMember} and its DTO {@link ExecutiveCommitteeTeamMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExecutiveCommitteeTeamMemberMapper extends EntityMapper<ExecutiveCommitteeTeamMemberDTO, ExecutiveCommitteeTeamMember> {}
