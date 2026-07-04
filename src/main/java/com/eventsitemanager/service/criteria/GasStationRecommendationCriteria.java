package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.GasStationRecommendationCategory;
import com.eventsitemanager.domain.enumeration.GasStationRecommendationStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationRecommendationCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter stationId;
    private LocalDateFilter recommendationDate;
    private GasStationRecommendationCategoryFilter category;
    private GasStationRecommendationStatusFilter status;
    private IntegerFilter priority;

    private Boolean distinct;

    public GasStationRecommendationCriteria() {}

    public GasStationRecommendationCriteria(GasStationRecommendationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.stationId = other.stationId == null ? null : other.stationId.copy();
        this.recommendationDate = other.recommendationDate == null ? null : other.recommendationDate.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GasStationRecommendationCriteria copy() {
        return new GasStationRecommendationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getStationId() {
        return stationId;
    }

    public void setStationId(LongFilter stationId) {
        this.stationId = stationId;
    }

    public LocalDateFilter getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(LocalDateFilter recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public GasStationRecommendationCategoryFilter getCategory() {
        return category;
    }

    public void setCategory(GasStationRecommendationCategoryFilter category) {
        this.category = category;
    }

    public GasStationRecommendationStatusFilter getStatus() {
        return status;
    }

    public void setStatus(GasStationRecommendationStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GasStationRecommendationCriteria that = (GasStationRecommendationCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }

    public static class GasStationRecommendationCategoryFilter extends Filter<GasStationRecommendationCategory> {

        public GasStationRecommendationCategoryFilter() {}

        public GasStationRecommendationCategoryFilter(GasStationRecommendationCategoryFilter filter) {
            super(filter);
        }

        @Override
        public GasStationRecommendationCategoryFilter copy() {
            return new GasStationRecommendationCategoryFilter(this);
        }
    }

    public static class GasStationRecommendationStatusFilter extends Filter<GasStationRecommendationStatus> {

        public GasStationRecommendationStatusFilter() {}

        public GasStationRecommendationStatusFilter(GasStationRecommendationStatusFilter filter) {
            super(filter);
        }

        @Override
        public GasStationRecommendationStatusFilter copy() {
            return new GasStationRecommendationStatusFilter(this);
        }
    }
}
