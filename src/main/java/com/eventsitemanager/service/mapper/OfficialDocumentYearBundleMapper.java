package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.OfficialDocumentYearBundle;
import com.eventsitemanager.service.dto.OfficialDocumentYearBundleDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link OfficialDocumentYearBundle} and {@link OfficialDocumentYearBundleDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfficialDocumentYearBundleMapper extends EntityMapper<OfficialDocumentYearBundleDTO, OfficialDocumentYearBundle> {}
