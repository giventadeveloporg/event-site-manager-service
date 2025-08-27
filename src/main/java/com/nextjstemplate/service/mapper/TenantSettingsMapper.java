package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.service.dto.TenantOrganizationDTO;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
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
