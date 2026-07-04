package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.ProfileWritingStatus;
import com.eventsitemanager.domain.enumeration.ProfileWritingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "profile_writing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileWriting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profileWritingSeq")
    @SequenceGenerator(name = "profileWritingSeq", sequenceName = "public.profile_writing_id_seq", allocationSize = 1)
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

    @Size(max = 150)
    @Column(name = "slug", length = 150)
    private String slug;

    @Size(max = 2000)
    @Column(name = "excerpt", length = 2000)
    private String excerpt;

    @Lob
    @Column(name = "body")
    private String body;

    @Size(max = 1024)
    @Column(name = "featured_image_url", length = 1024)
    private String featuredImageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "writing_type", length = 32, nullable = false)
    private ProfileWritingType writingType = ProfileWritingType.ORIGINAL;

    @Size(max = 1024)
    @Column(name = "external_url", length = 1024)
    private String externalUrl;

    @Size(max = 255)
    @Column(name = "publication_name", length = 255)
    private String publicationName;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    private ProfileWritingStatus status = ProfileWritingStatus.DRAFT;

    @Column(name = "display_order")
    private Integer displayOrder;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }

    public ProfileWritingType getWritingType() {
        return writingType;
    }

    public void setWritingType(ProfileWritingType writingType) {
        this.writingType = writingType;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }

    public ProfileWritingStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileWritingStatus status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(ProfileWriting.class.isInstance(o))) return false;
        return id != null && id.equals(((ProfileWriting) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
