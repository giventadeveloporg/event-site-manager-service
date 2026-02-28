package com.nextjstemplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "event_attendee_attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAttendeeAttachment implements Serializable {

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
    @Column(name = "attendee_id", nullable = false)
    private Long attendeeId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Size(max = 255)
    @Column(name = "title", length = 255)
    private String title;

    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;

    @NotNull
    @Size(max = 2048)
    @Column(name = "file_url", length = 2048, nullable = false)
    private String fileUrl;

    @Size(max = 255)
    @Column(name = "content_type", length = 255)
    private String contentType;

    @Column(name = "file_size")
    private Integer fileSize;

    @Size(max = 255)
    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @NotNull
    @Size(max = 255)
    @Column(name = "storage_type", length = 255, nullable = false)
    private String storageType;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @NotNull
    @Size(max = 255)
    @Column(name = "event_media_type", length = 255, nullable = false)
    private String eventMediaType;

    @Column(name = "uploaded_by_id")
    private Long uploadedById;

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

    public Long getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(Long attendeeId) {
        this.attendeeId = attendeeId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getEventMediaType() {
        return eventMediaType;
    }

    public void setEventMediaType(String eventMediaType) {
        this.eventMediaType = eventMediaType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventAttendeeAttachment)) return false;
        return getId() != null && getId().equals(((EventAttendeeAttachment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
