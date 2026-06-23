package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.SatelliteDomain;
import com.eventsitemanager.service.dto.SatelliteDomainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SatelliteDomain} and its DTO {@link SatelliteDomainDTO}.
 */
@Mapper(componentModel = "spring")
public interface SatelliteDomainMapper extends EntityMapper<SatelliteDomainDTO, SatelliteDomain> {}
