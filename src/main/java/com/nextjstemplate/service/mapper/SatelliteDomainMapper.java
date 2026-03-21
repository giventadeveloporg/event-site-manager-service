package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.SatelliteDomain;
import com.nextjstemplate.service.dto.SatelliteDomainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SatelliteDomain} and its DTO {@link SatelliteDomainDTO}.
 */
@Mapper(componentModel = "spring")
public interface SatelliteDomainMapper extends EntityMapper<SatelliteDomainDTO, SatelliteDomain> {}
