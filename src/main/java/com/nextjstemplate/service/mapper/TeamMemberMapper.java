package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TeamMember;
import com.nextjstemplate.service.dto.TeamMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamMember} and its DTO {@link TeamMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeamMemberMapper extends EntityMapper<TeamMemberDTO, TeamMember> {}
