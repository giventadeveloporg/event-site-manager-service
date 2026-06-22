package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.BulkOperationLog;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.BulkOperationLogDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BulkOperationLog} and its DTO {@link BulkOperationLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface BulkOperationLogMapper extends EntityMapper<BulkOperationLogDTO, BulkOperationLog> {
    @Mapping(target = "performedBy", source = "performedBy", qualifiedByName = "userProfileId")
    BulkOperationLogDTO toDto(BulkOperationLog s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
