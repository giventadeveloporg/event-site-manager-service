package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.ProfileAchievement;
import com.eventsitemanager.service.dto.ProfileAchievementDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileAchievementMapper extends EntityMapper<ProfileAchievementDTO, ProfileAchievement> {}
