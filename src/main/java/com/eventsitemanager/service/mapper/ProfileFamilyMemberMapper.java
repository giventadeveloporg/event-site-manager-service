package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileFamilyMember;
import com.eventsitemanager.service.dto.ProfileFamilyMemberDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileFamilyMemberMapper extends EntityMapper<ProfileFamilyMemberDTO, ProfileFamilyMember> {}
