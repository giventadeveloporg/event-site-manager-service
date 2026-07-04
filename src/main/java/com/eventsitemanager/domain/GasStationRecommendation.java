package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.GasStationRecommendationCategory;
import com.eventsitemanager.domain.enumeration.GasStationRecommendationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "gas_station_recommendation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationRecommendation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gasStationRecommendationSeq")
    @SequenceGenerator(name = "gasStationRecommendationSeq", sequenceName = "public.gas_station_recommendation_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @Column(name = "station_id")
    private Long stationId;

    @NotNull
    @Column(name = "recommendation_date", nullable = false)
    private LocalDate recommendationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 32, nullable = false)
    private GasStationRecommendationCategory category;

    @NotNull
    @Size(max = 500)
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Lob
    @Column(name = "detail")
    private String detail;

    @Column(name = "estimated_impact_usd", precision = 21, scale = 2)
    private BigDecimal estimatedImpactUsd;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "confidence_pct", precision = 5, scale = 2)
    private BigDecimal confidencePct;

    @Lob
    @Column(name = "explanation")
    private String explanation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 32, nullable = false)
    private GasStationRecommendationStatus status = GasStationRecommendationStatus.NEW;

    @Size(max = 2000)
    @Column(name = "owner_feedback", length = 2000)
    private String ownerFeedback;

    @Size(max = 128)
    @Column(name = "source_model_run_id", length = 128)
    private String sourceModelRunId;

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

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public LocalDate getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(LocalDate recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public GasStationRecommendationCategory getCategory() {
        return category;
    }

    public void setCategory(GasStationRecommendationCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getEstimatedImpactUsd() {
        return estimatedImpactUsd;
    }

    public void setEstimatedImpactUsd(BigDecimal estimatedImpactUsd) {
        this.estimatedImpactUsd = estimatedImpactUsd;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getConfidencePct() {
        return confidencePct;
    }

    public void setConfidencePct(BigDecimal confidencePct) {
        this.confidencePct = confidencePct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public GasStationRecommendationStatus getStatus() {
        return status;
    }

    public void setStatus(GasStationRecommendationStatus status) {
        this.status = status;
    }

    public String getOwnerFeedback() {
        return ownerFeedback;
    }

    public void setOwnerFeedback(String ownerFeedback) {
        this.ownerFeedback = ownerFeedback;
    }

    public String getSourceModelRunId() {
        return sourceModelRunId;
    }

    public void setSourceModelRunId(String sourceModelRunId) {
        this.sourceModelRunId = sourceModelRunId;
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
        if (!(GasStationRecommendation.class.isInstance(o))) return false;
        return id != null && id.equals(((GasStationRecommendation) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
