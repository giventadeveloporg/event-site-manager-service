package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.LastMatch} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LastMatchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter leagueName;

    private StringFilter matchKind;

    private BooleanFilter isActive;

    private IntegerFilter priorityOrder;

    private Boolean distinct;

    public LastMatchCriteria() {}

    public LastMatchCriteria(LastMatchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.leagueName = other.optionalLeagueName().map(StringFilter::copy).orElse(null);
        this.matchKind = other.optionalMatchKind().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.priorityOrder = other.optionalPriorityOrder().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LastMatchCriteria copy() {
        return new LastMatchCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getLeagueName() {
        return leagueName;
    }

    public Optional<StringFilter> optionalLeagueName() {
        return Optional.ofNullable(leagueName);
    }

    public StringFilter leagueName() {
        if (leagueName == null) {
            setLeagueName(new StringFilter());
        }
        return leagueName;
    }

    public void setLeagueName(StringFilter leagueName) {
        this.leagueName = leagueName;
    }

    public StringFilter getMatchKind() {
        return matchKind;
    }

    public Optional<StringFilter> optionalMatchKind() {
        return Optional.ofNullable(matchKind);
    }

    public StringFilter matchKind() {
        if (matchKind == null) {
            setMatchKind(new StringFilter());
        }
        return matchKind;
    }

    public void setMatchKind(StringFilter matchKind) {
        this.matchKind = matchKind;
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

    public IntegerFilter getPriorityOrder() {
        return priorityOrder;
    }

    public Optional<IntegerFilter> optionalPriorityOrder() {
        return Optional.ofNullable(priorityOrder);
    }

    public IntegerFilter priorityOrder() {
        if (priorityOrder == null) {
            setPriorityOrder(new IntegerFilter());
        }
        return priorityOrder;
    }

    public void setPriorityOrder(IntegerFilter priorityOrder) {
        this.priorityOrder = priorityOrder;
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
        final LastMatchCriteria that = (LastMatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(leagueName, that.leagueName) &&
            Objects.equals(matchKind, that.matchKind) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(priorityOrder, that.priorityOrder) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, title, leagueName, matchKind, isActive, priorityOrder, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LastMatchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalLeagueName().map(f -> "leagueName=" + f + ", ").orElse("") +
            optionalMatchKind().map(f -> "matchKind=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalPriorityOrder().map(f -> "priorityOrder=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
