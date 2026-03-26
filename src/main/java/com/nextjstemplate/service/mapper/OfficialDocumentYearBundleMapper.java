package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.OfficialDocumentYearBundle;
import com.nextjstemplate.service.dto.OfficialDocumentYearBundleDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link OfficialDocumentYearBundle} and {@link OfficialDocumentYearBundleDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfficialDocumentYearBundleMapper extends EntityMapper<OfficialDocumentYearBundleDTO, OfficialDocumentYearBundle> {}
