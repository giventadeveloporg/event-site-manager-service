package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.domain.GalleryCategory;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import com.nextjstemplate.service.dto.GalleryCategoryDTO;
import com.nextjstemplate.service.dto.UserProfileDTO;
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
