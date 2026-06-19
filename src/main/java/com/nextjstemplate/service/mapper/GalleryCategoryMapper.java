package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.GalleryCategory;
import com.nextjstemplate.service.dto.GalleryCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GalleryCategory} and its DTO {@link GalleryCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface GalleryCategoryMapper extends EntityMapper<GalleryCategoryDTO, GalleryCategory> {}
