package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tenant-scoped gallery album category (e.g. Ecumenical Visits).
 */
@Entity
@Table(
    name = "gallery_category",
    uniqueConstraints = { @UniqueConstraint(name = "ux_gallery_category_tenant_slug", columnNames = { "tenant_id", "slug" }) }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GalleryCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 64)
    @Column(name = "slug", length = 64, nullable = false)
    private String slug;

    @NotNull
    @Size(max = 128)
    @Column(name = "display_name", length = 128, nullable = false)
    private String displayName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Min(0)
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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

    public GalleryCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public GalleryCategory tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public GalleryCategory slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public GalleryCategory displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GalleryCategory description(String description) {
        this.setDescription(description);
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public GalleryCategory sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public GalleryCategory isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public GalleryCategory createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GalleryCategory updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GalleryCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((GalleryCategory) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "GalleryCategory{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", slug='" +
            getSlug() +
            "'" +
            ", displayName='" +
            getDisplayName() +
            "'" +
            ", sortOrder=" +
            getSortOrder() +
            ", isActive='" +
            getIsActive() +
            "'" +
            "}"
        );
    }
}
