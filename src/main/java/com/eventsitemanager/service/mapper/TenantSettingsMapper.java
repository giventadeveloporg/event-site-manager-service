package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.TenantOrganization;
import com.eventsitemanager.domain.TenantSettings;
import com.eventsitemanager.service.dto.TenantOrganizationDTO;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantSettings} and its DTO {@link TenantSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantSettingsMapper extends EntityMapper<TenantSettingsDTO, TenantSettings> {
    @Mapping(target = "tenantOrganization", source = "tenantOrganization", qualifiedByName = "tenantOrganizationId")
    TenantSettingsDTO toDto(TenantSettings s);

    @Named("tenantOrganizationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantOrganizationDTO toDtoTenantOrganizationId(TenantOrganization tenantOrganization);
}
