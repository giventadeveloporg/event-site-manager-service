package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GalleryCategory;
import com.eventsitemanager.service.dto.GalleryCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GalleryCategory} and its DTO {@link GalleryCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface GalleryCategoryMapper extends EntityMapper<GalleryCategoryDTO, GalleryCategory> {}
