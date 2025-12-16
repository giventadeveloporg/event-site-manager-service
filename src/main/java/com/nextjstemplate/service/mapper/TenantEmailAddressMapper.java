package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TenantEmailAddress;
import com.nextjstemplate.domain.enumeration.TenantEmailType;
import com.nextjstemplate.service.dto.TenantEmailAddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantEmailAddress} and its DTO {@link TenantEmailAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantEmailAddressMapper extends EntityMapper<TenantEmailAddressDTO, TenantEmailAddress> {
    @Mapping(target = "emailType", source = "emailType", qualifiedByName = "emailTypeToString")
    TenantEmailAddressDTO toDto(TenantEmailAddress entity);

    @Mapping(target = "emailType", source = "emailType", qualifiedByName = "stringToEmailType")
    TenantEmailAddress toEntity(TenantEmailAddressDTO dto);

    @Named("emailTypeToString")
    default String emailTypeToString(TenantEmailType emailType) {
        return emailType != null ? emailType.name() : null;
    }

    @Named("stringToEmailType")
    default TenantEmailType stringToEmailType(String emailType) {
        return emailType != null ? TenantEmailType.valueOf(emailType.toUpperCase()) : null;
    }
}
