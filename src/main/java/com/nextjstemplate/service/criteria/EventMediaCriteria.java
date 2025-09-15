package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventMedia} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventMediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-medias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventMediaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter eventMediaType;

    private StringFilter storageType;

    private StringFilter fileUrl;

    private StringFilter contentType;

    private IntegerFilter fileSize;

    private BooleanFilter isPublic;

    private BooleanFilter eventFlyer;

    private BooleanFilter isEventManagementOfficialDocument;

    private StringFilter preSignedUrl;

    private ZonedDateTimeFilter preSignedUrlExpiresAt;

    private StringFilter altText;

    private IntegerFilter displayOrder;

    private IntegerFilter downloadCount;

    private BooleanFilter isFeaturedVideo;

    private StringFilter featuredVideoUrl;

    private BooleanFilter isHeroImage;

    private BooleanFilter isActiveHeroImage;

    private BooleanFilter isHomePageHeroImage;

    private BooleanFilter isFeaturedEventImage;

    private BooleanFilter isLiveEventImage;
    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private LongFilter uploadedById;

    private LocalDateFilter startDisplayingFromDate;

    private Boolean distinct;

    public EventMediaCriteria() {}

    public EventMediaCriteria(EventMediaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.eventMediaType = other.eventMediaType == null ? null : other.eventMediaType.copy();
        this.storageType = other.storageType == null ? null : other.storageType.copy();
        this.fileUrl = other.fileUrl == null ? null : other.fileUrl.copy();
        this.contentType = other.contentType == null ? null : other.contentType.copy();
        this.fileSize = other.fileSize == null ? null : other.fileSize.copy();
        this.isPublic = other.isPublic == null ? null : other.isPublic.copy();
        this.eventFlyer = other.eventFlyer == null ? null : other.eventFlyer.copy();
        this.isEventManagementOfficialDocument =
            other.isEventManagementOfficialDocument == null ? null : other.isEventManagementOfficialDocument.copy();
        this.preSignedUrl = other.preSignedUrl == null ? null : other.preSignedUrl.copy();
        this.preSignedUrlExpiresAt = other.preSignedUrlExpiresAt == null ? null : other.preSignedUrlExpiresAt.copy();
        this.altText = other.altText == null ? null : other.altText.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.downloadCount = other.downloadCount == null ? null : other.downloadCount.copy();
        this.isFeaturedVideo = other.isFeaturedVideo == null ? null : other.isFeaturedVideo.copy();
        this.featuredVideoUrl = other.featuredVideoUrl == null ? null : other.featuredVideoUrl.copy();
        this.isHeroImage = other.isHeroImage == null ? null : other.isHeroImage.copy();
        this.isActiveHeroImage = other.isActiveHeroImage == null ? null : other.isActiveHeroImage.copy();
        this.isHomePageHeroImage = other.optionalIsHomePageHeroImage().map(BooleanFilter::copy).orElse(null);
        this.isFeaturedEventImage = other.optionalIsFeaturedEventImage().map(BooleanFilter::copy).orElse(null);
        this.isLiveEventImage = other.optionalIsLiveEventImage().map(BooleanFilter::copy).orElse(null);

        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.uploadedById = other.uploadedById == null ? null : other.uploadedById.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.uploadedById = other.uploadedById == null ? null : other.uploadedById.copy();
        this.startDisplayingFromDate = other.startDisplayingFromDate == null ? null : other.startDisplayingFromDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventMediaCriteria copy() {
        return new EventMediaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getEventMediaType() {
        return eventMediaType;
    }

    public StringFilter eventMediaType() {
        if (eventMediaType == null) {
            eventMediaType = new StringFilter();
        }
        return eventMediaType;
    }

    public void setEventMediaType(StringFilter eventMediaType) {
        this.eventMediaType = eventMediaType;
    }

    public StringFilter getStorageType() {
        return storageType;
    }

    public StringFilter storageType() {
        if (storageType == null) {
            storageType = new StringFilter();
        }
        return storageType;
    }

    public void setStorageType(StringFilter storageType) {
        this.storageType = storageType;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public StringFilter fileUrl() {
        if (fileUrl == null) {
            fileUrl = new StringFilter();
        }
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public StringFilter contentType() {
        if (contentType == null) {
            contentType = new StringFilter();
        }
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public IntegerFilter getFileSize() {
        return fileSize;
    }

    public IntegerFilter fileSize() {
        if (fileSize == null) {
            fileSize = new IntegerFilter();
        }
        return fileSize;
    }

    public void setFileSize(IntegerFilter fileSize) {
        this.fileSize = fileSize;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            isPublic = new BooleanFilter();
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public BooleanFilter getEventFlyer() {
        return eventFlyer;
    }

    public BooleanFilter eventFlyer() {
        if (eventFlyer == null) {
            eventFlyer = new BooleanFilter();
        }
        return eventFlyer;
    }

    public void setEventFlyer(BooleanFilter eventFlyer) {
        this.eventFlyer = eventFlyer;
    }

    public BooleanFilter getIsEventManagementOfficialDocument() {
        return isEventManagementOfficialDocument;
    }

    public BooleanFilter isEventManagementOfficialDocument() {
        if (isEventManagementOfficialDocument == null) {
            isEventManagementOfficialDocument = new BooleanFilter();
        }
        return isEventManagementOfficialDocument;
    }

    public void setIsEventManagementOfficialDocument(BooleanFilter isEventManagementOfficialDocument) {
        this.isEventManagementOfficialDocument = isEventManagementOfficialDocument;
    }

    public StringFilter getPreSignedUrl() {
        return preSignedUrl;
    }

    public StringFilter preSignedUrl() {
        if (preSignedUrl == null) {
            preSignedUrl = new StringFilter();
        }
        return preSignedUrl;
    }

    public void setPreSignedUrl(StringFilter preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public ZonedDateTimeFilter getPreSignedUrlExpiresAt() {
        return preSignedUrlExpiresAt;
    }

    public ZonedDateTimeFilter preSignedUrlExpiresAt() {
        if (preSignedUrlExpiresAt == null) {
            preSignedUrlExpiresAt = new ZonedDateTimeFilter();
        }
        return preSignedUrlExpiresAt;
    }

    public void setPreSignedUrlExpiresAt(ZonedDateTimeFilter preSignedUrlExpiresAt) {
        this.preSignedUrlExpiresAt = preSignedUrlExpiresAt;
    }

    public StringFilter getAltText() {
        return altText;
    }

    public StringFilter altText() {
        if (altText == null) {
            altText = new StringFilter();
        }
        return altText;
    }

    public void setAltText(StringFilter altText) {
        this.altText = altText;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            displayOrder = new IntegerFilter();
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public IntegerFilter getDownloadCount() {
        return downloadCount;
    }

    public IntegerFilter downloadCount() {
        if (downloadCount == null) {
            downloadCount = new IntegerFilter();
        }
        return downloadCount;
    }

    public void setDownloadCount(IntegerFilter downloadCount) {
        this.downloadCount = downloadCount;
    }

    public BooleanFilter getIsFeaturedVideo() {
        return isFeaturedVideo;
    }

    public BooleanFilter isFeaturedVideo() {
        if (isFeaturedVideo == null) {
            isFeaturedVideo = new BooleanFilter();
        }
        return isFeaturedVideo;
    }

    public void setIsFeaturedVideo(BooleanFilter isFeaturedVideo) {
        this.isFeaturedVideo = isFeaturedVideo;
    }

    public StringFilter getFeaturedVideoUrl() {
        return featuredVideoUrl;
    }

    public StringFilter featuredVideoUrl() {
        if (featuredVideoUrl == null) {
            featuredVideoUrl = new StringFilter();
        }
        return featuredVideoUrl;
    }

    public void setFeaturedVideoUrl(StringFilter featuredVideoUrl) {
        this.featuredVideoUrl = featuredVideoUrl;
    }

    public BooleanFilter getIsHeroImage() {
        return isHeroImage;
    }

    public BooleanFilter isHeroImage() {
        if (isHeroImage == null) {
            isHeroImage = new BooleanFilter();
        }
        return isHeroImage;
    }

    public void setIsHeroImage(BooleanFilter isHeroImage) {
        this.isHeroImage = isHeroImage;
    }

    public BooleanFilter getIsActiveHeroImage() {
        return isActiveHeroImage;
    }

    public BooleanFilter isActiveHeroImage() {
        if (isActiveHeroImage == null) {
            isActiveHeroImage = new BooleanFilter();
        }
        return isActiveHeroImage;
    }

    public void setIsActiveHeroImage(BooleanFilter isActiveHeroImage) {
        this.isActiveHeroImage = isActiveHeroImage;
    }

    public BooleanFilter getIsHomePageHeroImage() {
        return isHomePageHeroImage;
    }

    public Optional<BooleanFilter> optionalIsHomePageHeroImage() {
        return Optional.ofNullable(isHomePageHeroImage);
    }

    public BooleanFilter isHomePageHeroImage() {
        if (isHomePageHeroImage == null) {
            setIsHomePageHeroImage(new BooleanFilter());
        }
        return isHomePageHeroImage;
    }

    public void setIsHomePageHeroImage(BooleanFilter isHomePageHeroImage) {
        this.isHomePageHeroImage = isHomePageHeroImage;
    }

    public BooleanFilter getIsFeaturedEventImage() {
        return isFeaturedEventImage;
    }

    public Optional<BooleanFilter> optionalIsFeaturedEventImage() {
        return Optional.ofNullable(isFeaturedEventImage);
    }

    public BooleanFilter isFeaturedEventImage() {
        if (isFeaturedEventImage == null) {
            setIsFeaturedEventImage(new BooleanFilter());
        }
        return isFeaturedEventImage;
    }

    public void setIsFeaturedEventImage(BooleanFilter isFeaturedEventImage) {
        this.isFeaturedEventImage = isFeaturedEventImage;
    }

    public BooleanFilter getIsLiveEventImage() {
        return isLiveEventImage;
    }

    public Optional<BooleanFilter> optionalIsLiveEventImage() {
        return Optional.ofNullable(isLiveEventImage);
    }

    public BooleanFilter isLiveEventImage() {
        if (isLiveEventImage == null) {
            setIsLiveEventImage(new BooleanFilter());
        }
        return isLiveEventImage;
    }

    public void setIsLiveEventImage(BooleanFilter isLiveEventImage) {
        this.isLiveEventImage = isLiveEventImage;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getUploadedById() {
        return uploadedById;
    }

    public LongFilter uploadedById() {
        if (uploadedById == null) {
            uploadedById = new LongFilter();
        }
        return uploadedById;
    }

    public void setUploadedById(LongFilter uploadedById) {
        this.uploadedById = uploadedById;
    }

    public LocalDateFilter getStartDisplayingFromDate() {
        return startDisplayingFromDate;
    }

    public LocalDateFilter startDisplayingFromDate() {
        if (startDisplayingFromDate == null) {
            startDisplayingFromDate = new LocalDateFilter();
        }
        return startDisplayingFromDate;
    }

    public void setStartDisplayingFromDate(LocalDateFilter startDisplayingFromDate) {
        this.startDisplayingFromDate = startDisplayingFromDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventMediaCriteria that = (EventMediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(eventMediaType, that.eventMediaType) &&
            Objects.equals(storageType, that.storageType) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(eventFlyer, that.eventFlyer) &&
            Objects.equals(isEventManagementOfficialDocument, that.isEventManagementOfficialDocument) &&
            Objects.equals(preSignedUrl, that.preSignedUrl) &&
            Objects.equals(preSignedUrlExpiresAt, that.preSignedUrlExpiresAt) &&
            Objects.equals(altText, that.altText) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(downloadCount, that.downloadCount) &&
            Objects.equals(isFeaturedVideo, that.isFeaturedVideo) &&
            Objects.equals(featuredVideoUrl, that.featuredVideoUrl) &&
            Objects.equals(isHeroImage, that.isHeroImage) &&
            Objects.equals(isActiveHeroImage, that.isActiveHeroImage) &&
            Objects.equals(isHomePageHeroImage, that.isHomePageHeroImage) &&
            Objects.equals(isFeaturedEventImage, that.isFeaturedEventImage) &&
            Objects.equals(isLiveEventImage, that.isLiveEventImage) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(uploadedById, that.uploadedById) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(uploadedById, that.uploadedById) &&
            Objects.equals(startDisplayingFromDate, that.startDisplayingFromDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            eventMediaType,
            storageType,
            fileUrl,
            contentType,
            fileSize,
            isPublic,
            eventFlyer,
            isEventManagementOfficialDocument,
            preSignedUrl,
            preSignedUrlExpiresAt,
            altText,
            displayOrder,
            downloadCount,
            isFeaturedVideo,
            featuredVideoUrl,
            isHeroImage,
            isActiveHeroImage,
            isHomePageHeroImage,
            isFeaturedEventImage,
            isLiveEventImage,
            createdAt,
            updatedAt,
            eventId,
            uploadedById,
            eventId,
            uploadedById,
            startDisplayingFromDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventMediaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (eventMediaType != null ? "eventMediaType=" + eventMediaType + ", " : "") +
            (storageType != null ? "storageType=" + storageType + ", " : "") +
            (fileUrl != null ? "fileUrl=" + fileUrl + ", " : "") +
            (contentType != null ? "contentType=" + contentType + ", " : "") +
            (fileSize != null ? "fileSize=" + fileSize + ", " : "") +
            (isPublic != null ? "isPublic=" + isPublic + ", " : "") +
            (eventFlyer != null ? "eventFlyer=" + eventFlyer + ", " : "") +
            (isEventManagementOfficialDocument != null ? "isEventManagementOfficialDocument=" + isEventManagementOfficialDocument + ", " : "") +
            (preSignedUrl != null ? "preSignedUrl=" + preSignedUrl + ", " : "") +
            (preSignedUrlExpiresAt != null ? "preSignedUrlExpiresAt=" + preSignedUrlExpiresAt + ", " : "") +
            (altText != null ? "altText=" + altText + ", " : "") +
            (displayOrder != null ? "displayOrder=" + displayOrder + ", " : "") +
            (downloadCount != null ? "downloadCount=" + downloadCount + ", " : "") +
            (isFeaturedVideo != null ? "isFeaturedVideo=" + isFeaturedVideo + ", " : "") +
            (featuredVideoUrl != null ? "featuredVideoUrl=" + featuredVideoUrl + ", " : "") +
            (isHeroImage != null ? "isHeroImage=" + isHeroImage + ", " : "") +
            (isActiveHeroImage != null ? "isActiveHeroImage=" + isActiveHeroImage + ", " : "") +
            optionalIsHomePageHeroImage().map(f -> "isHomePageHeroImage=" + f + ", ").orElse("") +
            optionalIsFeaturedEventImage().map(f -> "isFeaturedEventImage=" + f + ", ").orElse("") +
            optionalIsLiveEventImage().map(f -> "isLiveEventImage=" + f + ", ").orElse("") +

            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (uploadedById != null ? "uploadedById=" + uploadedById + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (uploadedById != null ? "uploadedById=" + uploadedById + ", " : "") +
            (startDisplayingFromDate != null ? "startDisplayingFromDate=" + startDisplayingFromDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
