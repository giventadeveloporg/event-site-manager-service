package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TeamGroup.
 */
@Entity
@Table(name = "team_groups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamGroup implements Serializable {

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
    @Size(max = 32)
    @Column(name = "team_type", length = 32, nullable = false)
    private String teamType;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "slug", length = 100)
    private String slug;

    @Size(max = 64)
    @Column(name = "section_label", length = 64)
    private String sectionLabel;

    @Size(max = 255)
    @Column(name = "headline", length = 255)
    private String headline;

    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;

    @Size(max = 128)
    @Column(name = "cta_label", length = 128)
    private String ctaLabel;

    @Size(max = 500)
    @Column(name = "cta_href", length = 500)
    private String ctaHref;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TeamGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TeamGroup tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTeamType() {
        return this.teamType;
    }

    public TeamGroup teamType(String teamType) {
        this.setTeamType(teamType);
        return this;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public String getName() {
        return this.name;
    }

    public TeamGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return this.slug;
    }

    public TeamGroup slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSectionLabel() {
        return this.sectionLabel;
    }

    public TeamGroup sectionLabel(String sectionLabel) {
        this.setSectionLabel(sectionLabel);
        return this;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public String getHeadline() {
        return this.headline;
    }

    public TeamGroup headline(String headline) {
        this.setHeadline(headline);
        return this;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return this.description;
    }

    public TeamGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCtaLabel() {
        return this.ctaLabel;
    }

    public TeamGroup ctaLabel(String ctaLabel) {
        this.setCtaLabel(ctaLabel);
        return this;
    }

    public void setCtaLabel(String ctaLabel) {
        this.ctaLabel = ctaLabel;
    }

    public String getCtaHref() {
        return this.ctaHref;
    }

    public TeamGroup ctaHref(String ctaHref) {
        this.setCtaHref(ctaHref);
        return this;
    }

    public void setCtaHref(String ctaHref) {
        this.ctaHref = ctaHref;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public TeamGroup displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TeamGroup isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TeamGroup createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TeamGroup updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((TeamGroup) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamGroup{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", teamType='" + getTeamType() + "'" +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            ", sectionLabel='" + getSectionLabel() + "'" +
            ", headline='" + getHeadline() + "'" +
            ", description='" + getDescription() + "'" +
            ", ctaLabel='" + getCtaLabel() + "'" +
            ", ctaHref='" + getCtaHref() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
