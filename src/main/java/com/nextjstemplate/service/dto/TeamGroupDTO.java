package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.TeamGroup} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamGroupDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 32)
    private String teamType;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String slug;

    @Size(max = 64)
    private String sectionLabel;

    @Size(max = 255)
    private String headline;

    @Size(max = 2048)
    private String description;

    @Size(max = 128)
    private String ctaLabel;

    @Size(max = 500)
    private String ctaHref;

    private Integer displayOrder;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;

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

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCtaLabel() {
        return ctaLabel;
    }

    public void setCtaLabel(String ctaLabel) {
        this.ctaLabel = ctaLabel;
    }

    public String getCtaHref() {
        return ctaHref;
    }

    public void setCtaHref(String ctaHref) {
        this.ctaHref = ctaHref;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamGroupDTO)) {
            return false;
        }

        TeamGroupDTO teamGroupDTO = (TeamGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamGroupDTO{" +
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
