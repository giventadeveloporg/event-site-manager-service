package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextjstemplate.service.validation.GalleryAlbumCategoryValidator;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.GalleryAlbum} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GalleryAlbumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 2048)
    private String description;

    @Size(max = 2048)
    private String coverImageUrl;

    @NotNull
    private Boolean isPublic = true;

    @NotNull
    @Min(value = 0)
    private Integer displayOrder = 0;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @Min(GalleryAlbumCategoryValidator.MIN_ALBUM_YEAR)
    @Max(GalleryAlbumCategoryValidator.MAX_ALBUM_YEAR)
    private Integer albumYear;

    private Long galleryCategoryId;

    @JsonIgnore
    private boolean galleryCategoryIdSet;

    private LocalDate eventDateStart;

    private LocalDate eventDateEnd;

    @Size(max = GalleryAlbumCategoryValidator.MAX_EVENT_LOCATION_LENGTH)
    private String eventLocation;

    @JsonIgnore
    private boolean eventDateStartSet;

    @JsonIgnore
    private boolean eventDateEndSet;

    @JsonIgnore
    private boolean eventLocationSet;

    private GalleryCategoryDTO galleryCategory;

    private Long createdById;

    private UserProfileDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getAlbumYear() {
        return albumYear;
    }

    public void setAlbumYear(Integer albumYear) {
        this.albumYear = albumYear;
    }

    public Long getGalleryCategoryId() {
        return galleryCategoryId;
    }

    public void setGalleryCategoryId(Long galleryCategoryId) {
        this.galleryCategoryId = galleryCategoryId;
        this.galleryCategoryIdSet = true;
    }

    @JsonIgnore
    public boolean isGalleryCategoryIdSet() {
        return galleryCategoryIdSet;
    }

    public LocalDate getEventDateStart() {
        return eventDateStart;
    }

    public void setEventDateStart(LocalDate eventDateStart) {
        this.eventDateStart = eventDateStart;
        this.eventDateStartSet = true;
    }

    @JsonIgnore
    public boolean isEventDateStartSet() {
        return eventDateStartSet;
    }

    public LocalDate getEventDateEnd() {
        return eventDateEnd;
    }

    public void setEventDateEnd(LocalDate eventDateEnd) {
        this.eventDateEnd = eventDateEnd;
        this.eventDateEndSet = true;
    }

    @JsonIgnore
    public boolean isEventDateEndSet() {
        return eventDateEndSet;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
        this.eventLocationSet = true;
    }

    @JsonIgnore
    public boolean isEventLocationSet() {
        return eventLocationSet;
    }

    public GalleryCategoryDTO getGalleryCategory() {
        return galleryCategory;
    }

    public void setGalleryCategory(GalleryCategoryDTO galleryCategory) {
        this.galleryCategory = galleryCategory;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GalleryAlbumDTO)) {
            return false;
        }

        GalleryAlbumDTO galleryAlbumDTO = (GalleryAlbumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, galleryAlbumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GalleryAlbumDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", coverImageUrl='" + getCoverImageUrl() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", albumYear=" + getAlbumYear() +
            ", galleryCategoryId=" + getGalleryCategoryId() +
            ", eventDateStart='" + getEventDateStart() + "'" +
            ", eventDateEnd='" + getEventDateEnd() + "'" +
            ", eventLocation='" + getEventLocation() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdById=" + getCreatedById() +
            "}";
    }
}
