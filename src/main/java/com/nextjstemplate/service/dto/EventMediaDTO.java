package com.nextjstemplate.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventMediaDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 2048)
    private String description;

    @NotNull
    @Size(max = 255)
    private String eventMediaType;

    @NotNull
    @Size(max = 255)
    private String storageType;

    @Size(max = 2048)
    private String fileUrl;

    @Size(max = 255)
    private String contentType;

    private Integer fileSize;

    private Boolean isPublic;

    private Boolean eventFlyer;

    private Boolean isEventManagementOfficialDocument;

    @Size(max = 2048)
    private String preSignedUrl;

    private ZonedDateTime preSignedUrlExpiresAt;

    @Size(max = 500)
    private String altText;

    private Integer displayOrder;

    private Integer downloadCount;

    private Boolean isFeaturedVideo;

    @Size(max = 2048)
    private String featuredVideoUrl;

    private Boolean isHeroImage;

    private Boolean isActiveHeroImage;

    @NotNull
    private Boolean isHomePageHeroImage;

    @NotNull
    private Boolean isFeaturedEventImage;

    @NotNull
    private Boolean isLiveEventImage;

    private Long eventId;

    private Long uploadedById;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private LocalDate startDisplayingFromDate;

    /* private EventDetailsDTO event;

    private UserProfileDTO uploadedBy;*/

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

    public String getEventMediaType() {
        return eventMediaType;
    }

    public void setEventMediaType(String eventMediaType) {
        this.eventMediaType = eventMediaType;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getEventFlyer() {
        return eventFlyer;
    }

    public void setEventFlyer(Boolean eventFlyer) {
        this.eventFlyer = eventFlyer;
    }

    public Boolean getIsEventManagementOfficialDocument() {
        return isEventManagementOfficialDocument;
    }

    public void setIsEventManagementOfficialDocument(Boolean isEventManagementOfficialDocument) {
        this.isEventManagementOfficialDocument = isEventManagementOfficialDocument;
    }

    public String getPreSignedUrl() {
        return preSignedUrl;
    }

    public void setPreSignedUrl(String preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public ZonedDateTime getPreSignedUrlExpiresAt() {
        return preSignedUrlExpiresAt;
    }

    public void setPreSignedUrlExpiresAt(ZonedDateTime preSignedUrlExpiresAt) {
        this.preSignedUrlExpiresAt = preSignedUrlExpiresAt;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Boolean getIsFeaturedVideo() {
        return isFeaturedVideo;
    }

    public void setIsFeaturedVideo(Boolean isFeaturedVideo) {
        this.isFeaturedVideo = isFeaturedVideo;
    }

    public String getFeaturedVideoUrl() {
        return featuredVideoUrl;
    }

    public void setFeaturedVideoUrl(String featuredVideoUrl) {
        this.featuredVideoUrl = featuredVideoUrl;
    }

    public Boolean getIsHeroImage() {
        return isHeroImage;
    }

    public void setIsHeroImage(Boolean isHeroImage) {
        this.isHeroImage = isHeroImage;
    }

    public Boolean getIsActiveHeroImage() {
        return isActiveHeroImage;
    }

    public void setIsActiveHeroImage(Boolean isActiveHeroImage) {
        this.isActiveHeroImage = isActiveHeroImage;
    }

    public Boolean getIsHomePageHeroImage() {
        return isHomePageHeroImage;
    }

    public void setIsHomePageHeroImage(Boolean isHomePageHeroImage) {
        this.isHomePageHeroImage = isHomePageHeroImage;
    }

    public Boolean getIsFeaturedEventImage() {
        return isFeaturedEventImage;
    }

    public void setIsFeaturedEventImage(Boolean isFeaturedEventImage) {
        this.isFeaturedEventImage = isFeaturedEventImage;
    }

    public Boolean getIsLiveEventImage() {
        return isLiveEventImage;
    }

    public void setIsLiveEventImage(Boolean isLiveEventImage) {
        this.isLiveEventImage = isLiveEventImage;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(Long uploadedById) {
        this.uploadedById = uploadedById;
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

    public LocalDate getStartDisplayingFromDate() {
        return startDisplayingFromDate;
    }

    public void setStartDisplayingFromDate(LocalDate startDisplayingFromDate) {
        this.startDisplayingFromDate = startDisplayingFromDate;
    }

    /*public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    public UserProfileDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UserProfileDTO uploadedBy) {
        this.uploadedBy = uploadedBy;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventMediaDTO)) {
            return false;
        }

        EventMediaDTO eventMediaDTO = (EventMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventMediaDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", eventMediaType='" + getEventMediaType() + "'" +
            ", storageType='" + getStorageType() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", fileSize=" + getFileSize() +
            ", isPublic='" + getIsPublic() + "'" +
            ", eventFlyer='" + getEventFlyer() + "'" +
            ", isEventManagementOfficialDocument='" + getIsEventManagementOfficialDocument() + "'" +
            ", preSignedUrl='" + getPreSignedUrl() + "'" +
            ", preSignedUrlExpiresAt='" + getPreSignedUrlExpiresAt() + "'" +
            ", altText='" + getAltText() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", downloadCount=" + getDownloadCount() +
            ", isFeaturedVideo='" + getIsFeaturedVideo() + "'" +
            ", featuredVideoUrl='" + getFeaturedVideoUrl() + "'" +
            ", isHeroImage='" + getIsHeroImage() + "'" +
            ", isActiveHeroImage='" + getIsActiveHeroImage() + "'" +
			", isHomePageHeroImage='" + getIsHomePageHeroImage() + "'" +
            ", isFeaturedEventImage='" + getIsFeaturedEventImage() + "'" +
            ", isLiveEventImage='" + getIsLiveEventImage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", startDisplayingFromDate='" + getStartDisplayingFromDate() + "'" +
           /* ", event=" + getEvent() +
            ", uploadedBy=" + getUploadedBy() +*/
            "}";
    }
}
