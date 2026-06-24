package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.TenantOrganization;
import com.eventsitemanager.service.dto.TenantOrganizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantOrganization} and its DTO {@link TenantOrganizationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantOrganizationMapper extends EntityMapper<TenantOrganizationDTO, TenantOrganization> {}
