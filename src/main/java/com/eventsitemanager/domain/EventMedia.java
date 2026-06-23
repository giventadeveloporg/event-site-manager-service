package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventMedia.
 */
@Entity
@Table(name = "event_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;

    @NotNull
    @Size(max = 255)
    @Column(name = "event_media_type", length = 255, nullable = false)
    private String eventMediaType;

    @NotNull
    @Size(max = 255)
    @Column(name = "storage_type", length = 255, nullable = false)
    private String storageType;

    @Size(max = 2048)
    @Column(name = "file_url", length = 2048)
    private String fileUrl;

    @Size(max = 255)
    @Column(name = "content_type", length = 255)
    private String contentType;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "event_flyer")
    private Boolean eventFlyer;

    @Column(name = "is_event_management_official_document")
    private Boolean isEventManagementOfficialDocument;

    @Column(name = "official_document_category_id")
    private Long officialDocumentCategoryId;

    @Column(name = "official_document_year")
    private Integer officialDocumentYear;

    @Column(name = "hierarchy_path", columnDefinition = "TEXT")
    private String hierarchyPath;

    @Column(name = "hierarchy_category_label", columnDefinition = "TEXT")
    private String hierarchyCategoryLabel;

    @Min(value = 0)
    @Column(name = "display_priority")
    private Integer displayPriority;

    @Size(max = 2048)
    @Column(name = "pre_signed_url", length = 2048)
    private String preSignedUrl;

    @Column(name = "pre_signed_url_expires_at")
    private ZonedDateTime preSignedUrlExpiresAt;

    @Size(max = 2048)
    @Column(name = "thumbnail_url", length = 2048)
    private String thumbnailUrl;

    @Size(max = 2048)
    @Column(name = "thumbnail_pre_signed_url", length = 2048)
    private String thumbnailPreSignedUrl;

    @Column(name = "thumbnail_pre_signed_url_expires_at")
    private ZonedDateTime thumbnailPreSignedUrlExpiresAt;

    @Size(max = 500)
    @Column(name = "alt_text", length = 500)
    private String altText;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "download_count")
    private Integer downloadCount;

    @Column(name = "is_featured_video")
    private Boolean isFeaturedVideo;

    @Size(max = 2048)
    @Column(name = "featured_video_url", length = 2048)
    private String featuredVideoUrl;

    @Column(name = "is_hero_image")
    private Boolean isHeroImage;

    @Column(name = "is_active_hero_image")
    private Boolean isActiveHeroImage;

    @NotNull
    @Column(name = "is_home_page_hero_image", nullable = false)
    private Boolean isHomePageHeroImage;

    @Column(name = "home_page_hero_display_duration_seconds")
    private Integer homePageHeroDisplayDurationSeconds;

    @NotNull
    @Column(name = "is_featured_event_image", nullable = false)
    private Boolean isFeaturedEventImage;

    @NotNull
    @Column(name = "is_live_event_image", nullable = false)
    private Boolean isLiveEventImage;

    @Column(name = "is_email_header_image")
    private Boolean isEmailHeaderImage;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "uploaded_by_id")
    private Long uploadedById;

    @Column(name = "start_displaying_from_date")
    private LocalDate startDisplayingFromDate;

    @Column(name = "sponsor_id")
    private Long sponsorId;

    @Column(name = "event_sponsors_join_id")
    private Long eventSponsorsJoinId;

    @Column(name = "performer_id")
    private Long performerId;

    @Column(name = "director_id")
    private Long directorId;

    /**
     * Priority ranking for media files (sponsor or event-sponsor).
     * Lower values indicate higher priority (0 = highest priority).
     * Used to determine which image to display when multiple files are available.
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "priority_ranking", nullable = false)
    private Integer priorityRanking = 0;

    @Column(name = "event_focus_group_id")
    private Long eventFocusGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @JsonIgnoreProperties(value = { "createdBy" }, allowSetters = true)
    private GalleryAlbum album;

    /*
     * @ManyToOne(fetch = FetchType.LAZY)
     *
     * @JsonIgnoreProperties(value = { "createdBy", "eventType", "discountCodes" },
     * allowSetters = true)
     * private EventDetails event;
     *
     * @ManyToOne(fetch = FetchType.LAZY)
     *
     * @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" },
     * allowSetters = true)
     * private UserProfile uploadedBy;
     */

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventMedia tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return this.title;
    }

    public EventMedia title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public EventMedia description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventMediaType() {
        return this.eventMediaType;
    }

    public EventMedia eventMediaType(String eventMediaType) {
        this.setEventMediaType(eventMediaType);
        return this;
    }

    public void setEventMediaType(String eventMediaType) {
        this.eventMediaType = eventMediaType;
    }

    public String getStorageType() {
        return this.storageType;
    }

    public EventMedia storageType(String storageType) {
        this.setStorageType(storageType);
        return this;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public EventMedia fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContentType() {
        return this.contentType;
    }

    public EventMedia contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public EventMedia fileSize(Integer fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public EventMedia isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getEventFlyer() {
        return this.eventFlyer;
    }

    public EventMedia eventFlyer(Boolean eventFlyer) {
        this.setEventFlyer(eventFlyer);
        return this;
    }

    public void setEventFlyer(Boolean eventFlyer) {
        this.eventFlyer = eventFlyer;
    }

    public Boolean getIsEventManagementOfficialDocument() {
        return this.isEventManagementOfficialDocument;
    }

    public EventMedia isEventManagementOfficialDocument(Boolean isEventManagementOfficialDocument) {
        this.setIsEventManagementOfficialDocument(isEventManagementOfficialDocument);
        return this;
    }

    public void setIsEventManagementOfficialDocument(Boolean isEventManagementOfficialDocument) {
        this.isEventManagementOfficialDocument = isEventManagementOfficialDocument;
    }

    public Long getOfficialDocumentCategoryId() {
        return officialDocumentCategoryId;
    }

    public void setOfficialDocumentCategoryId(Long officialDocumentCategoryId) {
        this.officialDocumentCategoryId = officialDocumentCategoryId;
    }

    public Integer getOfficialDocumentYear() {
        return officialDocumentYear;
    }

    public void setOfficialDocumentYear(Integer officialDocumentYear) {
        this.officialDocumentYear = officialDocumentYear;
    }

    public String getHierarchyPath() {
        return hierarchyPath;
    }

    public void setHierarchyPath(String hierarchyPath) {
        this.hierarchyPath = hierarchyPath;
    }

    public String getHierarchyCategoryLabel() {
        return hierarchyCategoryLabel;
    }

    public void setHierarchyCategoryLabel(String hierarchyCategoryLabel) {
        this.hierarchyCategoryLabel = hierarchyCategoryLabel;
    }

    public Integer getDisplayPriority() {
        return displayPriority;
    }

    public void setDisplayPriority(Integer displayPriority) {
        this.displayPriority = displayPriority;
    }

    public String getPreSignedUrl() {
        return this.preSignedUrl;
    }

    public EventMedia preSignedUrl(String preSignedUrl) {
        this.setPreSignedUrl(preSignedUrl);
        return this;
    }

    public void setPreSignedUrl(String preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public ZonedDateTime getPreSignedUrlExpiresAt() {
        return this.preSignedUrlExpiresAt;
    }

    public EventMedia preSignedUrlExpiresAt(ZonedDateTime preSignedUrlExpiresAt) {
        this.setPreSignedUrlExpiresAt(preSignedUrlExpiresAt);
        return this;
    }

    public void setPreSignedUrlExpiresAt(ZonedDateTime preSignedUrlExpiresAt) {
        this.preSignedUrlExpiresAt = preSignedUrlExpiresAt;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public EventMedia thumbnailUrl(String thumbnailUrl) {
        this.setThumbnailUrl(thumbnailUrl);
        return this;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailPreSignedUrl() {
        return this.thumbnailPreSignedUrl;
    }

    public EventMedia thumbnailPreSignedUrl(String thumbnailPreSignedUrl) {
        this.setThumbnailPreSignedUrl(thumbnailPreSignedUrl);
        return this;
    }

    public void setThumbnailPreSignedUrl(String thumbnailPreSignedUrl) {
        this.thumbnailPreSignedUrl = thumbnailPreSignedUrl;
    }

    public ZonedDateTime getThumbnailPreSignedUrlExpiresAt() {
        return this.thumbnailPreSignedUrlExpiresAt;
    }

    public EventMedia thumbnailPreSignedUrlExpiresAt(ZonedDateTime thumbnailPreSignedUrlExpiresAt) {
        this.setThumbnailPreSignedUrlExpiresAt(thumbnailPreSignedUrlExpiresAt);
        return this;
    }

    public void setThumbnailPreSignedUrlExpiresAt(ZonedDateTime thumbnailPreSignedUrlExpiresAt) {
        this.thumbnailPreSignedUrlExpiresAt = thumbnailPreSignedUrlExpiresAt;
    }

    public String getAltText() {
        return this.altText;
    }

    public EventMedia altText(String altText) {
        this.setAltText(altText);
        return this;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public EventMedia displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getDownloadCount() {
        return this.downloadCount;
    }

    public EventMedia downloadCount(Integer downloadCount) {
        this.setDownloadCount(downloadCount);
        return this;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Boolean getIsFeaturedVideo() {
        return this.isFeaturedVideo;
    }

    public EventMedia isFeaturedVideo(Boolean isFeaturedVideo) {
        this.setIsFeaturedVideo(isFeaturedVideo);
        return this;
    }

    public void setIsFeaturedVideo(Boolean isFeaturedVideo) {
        this.isFeaturedVideo = isFeaturedVideo;
    }

    public String getFeaturedVideoUrl() {
        return this.featuredVideoUrl;
    }

    public EventMedia featuredVideoUrl(String featuredVideoUrl) {
        this.setFeaturedVideoUrl(featuredVideoUrl);
        return this;
    }

    public void setFeaturedVideoUrl(String featuredVideoUrl) {
        this.featuredVideoUrl = featuredVideoUrl;
    }

    public Boolean getIsHeroImage() {
        return this.isHeroImage;
    }

    public EventMedia isHeroImage(Boolean isHeroImage) {
        this.setIsHeroImage(isHeroImage);
        return this;
    }

    public void setIsHeroImage(Boolean isHeroImage) {
        this.isHeroImage = isHeroImage;
    }

    public Boolean getIsActiveHeroImage() {
        return this.isActiveHeroImage;
    }

    public EventMedia isActiveHeroImage(Boolean isActiveHeroImage) {
        this.setIsActiveHeroImage(isActiveHeroImage);
        return this;
    }

    public void setIsActiveHeroImage(Boolean isActiveHeroImage) {
        this.isActiveHeroImage = isActiveHeroImage;
    }

    public Boolean getIsHomePageHeroImage() {
        return this.isHomePageHeroImage;
    }

    public EventMedia isHomePageHeroImage(Boolean isHomePageHeroImage) {
        this.setIsHomePageHeroImage(isHomePageHeroImage);
        return this;
    }

    public void setIsHomePageHeroImage(Boolean isHomePageHeroImage) {
        this.isHomePageHeroImage = isHomePageHeroImage;
    }

    public Integer getHomePageHeroDisplayDurationSeconds() {
        return this.homePageHeroDisplayDurationSeconds;
    }

    public EventMedia homePageHeroDisplayDurationSeconds(Integer homePageHeroDisplayDurationSeconds) {
        this.setHomePageHeroDisplayDurationSeconds(homePageHeroDisplayDurationSeconds);
        return this;
    }

    public void setHomePageHeroDisplayDurationSeconds(Integer homePageHeroDisplayDurationSeconds) {
        this.homePageHeroDisplayDurationSeconds = homePageHeroDisplayDurationSeconds;
    }

    public Boolean getIsFeaturedEventImage() {
        return this.isFeaturedEventImage;
    }

    public EventMedia isFeaturedEventImage(Boolean isFeaturedEventImage) {
        this.setIsFeaturedEventImage(isFeaturedEventImage);
        return this;
    }

    public void setIsFeaturedEventImage(Boolean isFeaturedEventImage) {
        this.isFeaturedEventImage = isFeaturedEventImage;
    }

    public Boolean getIsLiveEventImage() {
        return this.isLiveEventImage;
    }

    public EventMedia isLiveEventImage(Boolean isLiveEventImage) {
        this.setIsLiveEventImage(isLiveEventImage);
        return this;
    }

    public void setIsLiveEventImage(Boolean isLiveEventImage) {
        this.isLiveEventImage = isLiveEventImage;
    }

    public Boolean getIsEmailHeaderImage() {
        return this.isEmailHeaderImage;
    }

    public EventMedia isEmailHeaderImage(Boolean isEmailHeaderImage) {
        this.setIsEmailHeaderImage(isEmailHeaderImage);
        return this;
    }

    public void setIsEmailHeaderImage(Boolean isEmailHeaderImage) {
        this.isEmailHeaderImage = isEmailHeaderImage;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventMedia createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventMedia updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public EventMedia eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUploadedById() {
        return this.uploadedById;
    }

    public EventMedia uploadedById(Long uploadedById) {
        this.setUploadedById(uploadedById);
        return this;
    }

    public void setUploadedById(Long uploadedById) {
        this.uploadedById = uploadedById;
    }

    public LocalDate getStartDisplayingFromDate() {
        return this.startDisplayingFromDate;
    }

    public EventMedia startDisplayingFromDate(LocalDate startDisplayingFromDate) {
        this.setStartDisplayingFromDate(startDisplayingFromDate);
        return this;
    }

    public void setStartDisplayingFromDate(LocalDate startDisplayingFromDate) {
        this.startDisplayingFromDate = startDisplayingFromDate;
    }

    public Long getSponsorId() {
        return this.sponsorId;
    }

    public EventMedia sponsorId(Long sponsorId) {
        this.setSponsorId(sponsorId);
        return this;
    }

    public void setSponsorId(Long sponsorId) {
        this.sponsorId = sponsorId;
    }

    public Long getEventSponsorsJoinId() {
        return this.eventSponsorsJoinId;
    }

    public EventMedia eventSponsorsJoinId(Long eventSponsorsJoinId) {
        this.setEventSponsorsJoinId(eventSponsorsJoinId);
        return this;
    }

    public void setEventSponsorsJoinId(Long eventSponsorsJoinId) {
        this.eventSponsorsJoinId = eventSponsorsJoinId;
    }

    public Long getPerformerId() {
        return this.performerId;
    }

    public EventMedia performerId(Long performerId) {
        this.setPerformerId(performerId);
        return this;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Long getDirectorId() {
        return this.directorId;
    }

    public EventMedia directorId(Long directorId) {
        this.setDirectorId(directorId);
        return this;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Long getEventFocusGroupId() {
        return this.eventFocusGroupId;
    }

    public EventMedia eventFocusGroupId(Long eventFocusGroupId) {
        this.setEventFocusGroupId(eventFocusGroupId);
        return this;
    }

    public void setEventFocusGroupId(Long eventFocusGroupId) {
        this.eventFocusGroupId = eventFocusGroupId;
    }

    public Integer getPriorityRanking() {
        return this.priorityRanking;
    }

    public EventMedia priorityRanking(Integer priorityRanking) {
        this.setPriorityRanking(priorityRanking);
        return this;
    }

    public void setPriorityRanking(Integer priorityRanking) {
        this.priorityRanking = priorityRanking;
    }

    public GalleryAlbum getAlbum() {
        return this.album;
    }

    public void setAlbum(GalleryAlbum galleryAlbum) {
        this.album = galleryAlbum;
    }

    public EventMedia album(GalleryAlbum galleryAlbum) {
        this.setAlbum(galleryAlbum);
        return this;
    }

    /*
     * public EventDetails getEvent() {
     * return this.event;
     * }
     *
     * public void setEvent(EventDetails eventDetails) {
     * this.event = eventDetails;
     * }
     *
     * public EventMedia event(EventDetails eventDetails) {
     * this.setEvent(eventDetails);
     * return this;
     * }
     *
     * public UserProfile getUploadedBy() {
     * return this.uploadedBy;
     * }
     *
     * public void setUploadedBy(UserProfile userProfile) {
     * this.uploadedBy = userProfile;
     * }
     *
     * public EventMedia uploadedBy(UserProfile userProfile) {
     * this.setUploadedBy(userProfile);
     * return this;
     * }
     */

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((EventMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventMedia{" +
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
            ", homePageHeroDisplayDurationSeconds=" + getHomePageHeroDisplayDurationSeconds() +
            ", isFeaturedEventImage='" + getIsFeaturedEventImage() + "'" +
            ", isLiveEventImage='" + getIsLiveEventImage() + "'" +
            ", isEmailHeaderImage='" + getIsEmailHeaderImage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", eventId=" + getEventId() +
            ", uploadedById=" + getUploadedById() +
            ", startDisplayingFromDate='" + getStartDisplayingFromDate() + "'" +
            ", sponsorId=" + getSponsorId() +
            ", eventSponsorsJoinId=" + getEventSponsorsJoinId() +
            ", performerId=" + getPerformerId() +
            ", directorId=" + getDirectorId() +
            ", eventFocusGroupId=" + getEventFocusGroupId() +
            ", priorityRanking=" + getPriorityRanking() +
            ", album=" + (getAlbum() != null ? getAlbum().getId() : null) +
            "}";
    }
}
