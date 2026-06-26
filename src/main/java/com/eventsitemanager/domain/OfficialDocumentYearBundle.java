package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * Per-tenant, per official-document category, per calendar year; optional cover points to {@link EventMedia}.
 */
@Entity
@Table(
    name = "official_document_year_bundle",
    schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "ux_official_document_year_bundle_tenant_category_year",
            columnNames = { "tenant_id", "official_document_category_id", "document_year" }
        ),
    }
)
public class OfficialDocumentYearBundle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "officialDocumentYearBundleSeq")
    @SequenceGenerator(name = "officialDocumentYearBundleSeq", sequenceName = "public.event_sponsors_join_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "official_document_category_id", nullable = false)
    private Long officialDocumentCategoryId;

    @NotNull
    @Column(name = "document_year", nullable = false)
    private Integer documentYear;

    @Column(name = "cover_event_media_id")
    private Long coverEventMediaId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

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

    public Long getOfficialDocumentCategoryId() {
        return officialDocumentCategoryId;
    }

    public void setOfficialDocumentCategoryId(Long officialDocumentCategoryId) {
        this.officialDocumentCategoryId = officialDocumentCategoryId;
    }

    public Integer getDocumentYear() {
        return documentYear;
    }

    public void setDocumentYear(Integer documentYear) {
        this.documentYear = documentYear;
    }

    public Long getCoverEventMediaId() {
        return coverEventMediaId;
    }

    public void setCoverEventMediaId(Long coverEventMediaId) {
        this.coverEventMediaId = coverEventMediaId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
