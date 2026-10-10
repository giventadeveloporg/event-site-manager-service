package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.TenantOnboardingRequest;
import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantOnboardingRequest} and its DTO {@link TenantOnboardingRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantOnboardingRequestMapper extends EntityMapper<TenantOnboardingRequestDTO, TenantOnboardingRequest> {
    /**
     * PATCH may only edit request content; lifecycle, review and audit columns are owned by submit / approve / reject.
     */
    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "source", ignore = true)
    @Mapping(target = "reviewedByClerkUserId", ignore = true)
    @Mapping(target = "reviewedByEmail", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "provisioningResult", ignore = true)
    @Mapping(target = "submitterIp", ignore = true)
    @Mapping(target = "userAgent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void partialUpdate(@MappingTarget TenantOnboardingRequest entity, TenantOnboardingRequestDTO dto);
}
