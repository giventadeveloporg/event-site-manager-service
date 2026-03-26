package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.OfficialDocumentCategory;
import com.nextjstemplate.service.dto.OfficialDocumentCategoryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for {@link OfficialDocumentCategory} and {@link OfficialDocumentCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface OfficialDocumentCategoryMapper extends EntityMapper<OfficialDocumentCategoryDTO, OfficialDocumentCategory> {}
