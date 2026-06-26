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
 * A GalleryAlbum.
 */
@Entity
@Table(name = "gallery_album")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GalleryAlbum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "galleryAlbumSeq")
    @SequenceGenerator(name = "galleryAlbumSeq", sequenceName = "public.gallery_album_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;

    @Size(max = 2048)
    @Column(name = "cover_image_url", length = 2048)
    private String coverImageUrl;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    @NotNull
    @Min(value = 0)
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "album_year")
    private Integer albumYear;

    @Column(name = "event_date_start")
    private LocalDate eventDateStart;

    @Column(name = "event_date_end")
    private LocalDate eventDateEnd;

    @Size(max = 256)
    @Column(name = "event_location", length = 256)
    private String eventLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_category_id")
    @JsonIgnoreProperties(value = { "tenantId", "description", "sortOrder", "isActive", "createdAt", "updatedAt" }, allowSetters = true)
    private GalleryCategory galleryCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GalleryAlbum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public GalleryAlbum tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return this.title;
    }

    public GalleryAlbum title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public GalleryAlbum description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return this.coverImageUrl;
    }

    public GalleryAlbum coverImageUrl(String coverImageUrl) {
        this.setCoverImageUrl(coverImageUrl);
        return this;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public GalleryAlbum isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public GalleryAlbum displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public GalleryAlbum createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public GalleryAlbum updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfile getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserProfile createdBy) {
        this.createdBy = createdBy;
    }

    public GalleryAlbum createdBy(UserProfile createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public Integer getAlbumYear() {
        return albumYear;
    }

    public void setAlbumYear(Integer albumYear) {
        this.albumYear = albumYear;
    }

    public GalleryAlbum albumYear(Integer albumYear) {
        this.setAlbumYear(albumYear);
        return this;
    }

    public GalleryCategory getGalleryCategory() {
        return galleryCategory;
    }

    public void setGalleryCategory(GalleryCategory galleryCategory) {
        this.galleryCategory = galleryCategory;
    }

    public GalleryAlbum galleryCategory(GalleryCategory galleryCategory) {
        this.setGalleryCategory(galleryCategory);
        return this;
    }

    public LocalDate getEventDateStart() {
        return eventDateStart;
    }

    public void setEventDateStart(LocalDate eventDateStart) {
        this.eventDateStart = eventDateStart;
    }

    public GalleryAlbum eventDateStart(LocalDate eventDateStart) {
        this.setEventDateStart(eventDateStart);
        return this;
    }

    public LocalDate getEventDateEnd() {
        return eventDateEnd;
    }

    public void setEventDateEnd(LocalDate eventDateEnd) {
        this.eventDateEnd = eventDateEnd;
    }

    public GalleryAlbum eventDateEnd(LocalDate eventDateEnd) {
        this.setEventDateEnd(eventDateEnd);
        return this;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public GalleryAlbum eventLocation(String eventLocation) {
        this.setEventLocation(eventLocation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GalleryAlbum)) {
            return false;
        }
        return getId() != null && getId().equals(((GalleryAlbum) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GalleryAlbum{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", coverImageUrl='" + getCoverImageUrl() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", albumYear=" + getAlbumYear() +
            ", eventDateStart='" + getEventDateStart() + "'" +
            ", eventDateEnd='" + getEventDateEnd() + "'" +
            ", eventLocation='" + getEventLocation() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
