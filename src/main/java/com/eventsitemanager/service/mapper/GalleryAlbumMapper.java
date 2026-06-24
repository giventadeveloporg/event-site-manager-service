package com.eventsitemanager.service.mapper;

import com.eventsitemanager.domain.GalleryAlbum;
import com.eventsitemanager.domain.GalleryCategory;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.service.dto.GalleryAlbumDTO;
import com.eventsitemanager.service.dto.GalleryCategoryDTO;
import com.eventsitemanager.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GalleryAlbum} and its DTO {@link GalleryAlbumDTO}.
 */
@Mapper(componentModel = "spring")
public interface GalleryAlbumMapper extends EntityMapper<GalleryAlbumDTO, GalleryAlbum> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userProfileId")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "galleryCategory", source = "galleryCategory", qualifiedByName = "galleryCategorySummary")
    @Mapping(target = "galleryCategoryId", source = "galleryCategory.id")
    GalleryAlbumDTO toDto(GalleryAlbum s);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "galleryCategory", ignore = true)
    GalleryAlbum toEntity(GalleryAlbumDTO dto);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("galleryCategorySummary")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "displayName", source = "displayName")
    GalleryCategoryDTO toDtoGalleryCategorySummary(GalleryCategory galleryCategory);
}
