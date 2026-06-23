package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.TeamGroup} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeamGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter teamType;

    private StringFilter name;

    private StringFilter slug;

    private BooleanFilter isActive;

    private IntegerFilter displayOrder;

    private Boolean distinct;

    public TeamGroupCriteria() {}

    public TeamGroupCriteria(TeamGroupCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.teamType = other.optionalTeamType().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.displayOrder = other.optionalDisplayOrder().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TeamGroupCriteria copy() {
        return new TeamGroupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public Optional<StringFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTeamType() {
        return teamType;
    }

    public Optional<StringFilter> optionalTeamType() {
        return Optional.ofNullable(teamType);
    }

    public StringFilter teamType() {
        if (teamType == null) {
            setTeamType(new StringFilter());
        }
        return teamType;
    }

    public void setTeamType(StringFilter teamType) {
        this.teamType = teamType;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public Optional<StringFilter> optionalSlug() {
        return Optional.ofNullable(slug);
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public Optional<IntegerFilter> optionalDisplayOrder() {
        return Optional.ofNullable(displayOrder);
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            setDisplayOrder(new IntegerFilter());
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
        final TeamGroupCriteria that = (TeamGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(teamType, that.teamType) &&
            Objects.equals(name, that.name) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, teamType, name, slug, isActive, displayOrder, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamGroupCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTeamType().map(f -> "teamType=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalDisplayOrder().map(f -> "displayOrder=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
