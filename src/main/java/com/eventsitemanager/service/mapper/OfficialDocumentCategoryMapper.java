package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.OfficialDocumentCategory;
import com.eventsitemanager.service.dto.OfficialDocumentCategoryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link OfficialDocumentCategory} and {@link OfficialDocumentCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfficialDocumentCategoryMapper extends EntityMapper<OfficialDocumentCategoryDTO, OfficialDocumentCategory> {}
