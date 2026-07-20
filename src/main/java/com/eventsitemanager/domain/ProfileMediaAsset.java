package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.ProfileMediaKind;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "profile_media_asset")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileMediaAsset implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileMediaAssetSeq")
    @SequenceGenerator(name = "profileMediaAssetSeq", sequenceName = "public.profile_media_asset_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 500)
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Size(max = 1024)
    @Column(name = "cover_image_url", length = 1024)
    private String coverImageUrl;

    @NotNull
    @Size(max = 1024)
    @Column(name = "file_url", length = 1024, nullable = false)
    private String fileUrl;

    @Size(max = 64)
    @Column(name = "file_type", length = 64)
    private String fileType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "media_kind", length = 32, nullable = false)
    private ProfileMediaKind mediaKind = ProfileMediaKind.DOCUMENT;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "display_order")
    private Integer displayOrder;

    @NotNull
    @Column(name = "is_downloadable", nullable = false)
    private Boolean isDownloadable = Boolean.TRUE;

    @NotNull
    @Column(name = "requires_email", nullable = false)
    private Boolean requiresEmail = Boolean.FALSE;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public ProfileMediaKind getMediaKind() {
        return mediaKind;
    }

    public void setMediaKind(ProfileMediaKind mediaKind) {
        this.mediaKind = mediaKind;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsDownloadable() {
        return isDownloadable;
    }

    public void setIsDownloadable(Boolean isDownloadable) {
        this.isDownloadable = isDownloadable;
    }

    public Boolean getRequiresEmail() {
        return requiresEmail;
    }

    public void setRequiresEmail(Boolean requiresEmail) {
        this.requiresEmail = requiresEmail;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(ProfileMediaAsset.class.isInstance(o))) return false;
        return id != null && id.equals(((ProfileMediaAsset) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
