package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "gas_station_daily_metrics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationDailyMetrics implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gasStationDailyMetricsSeq")
    @SequenceGenerator(name = "gasStationDailyMetricsSeq", sequenceName = "public.gas_station_daily_metrics_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "station_id", nullable = false)
    private Long stationId;

    @NotNull
    @Column(name = "metric_date", nullable = false)
    private LocalDate metricDate;

    @Column(name = "fuel_gallons_sold", precision = 21, scale = 2)
    private BigDecimal fuelGallonsSold;

    @Column(name = "fuel_revenue_usd", precision = 21, scale = 2)
    private BigDecimal fuelRevenueUsd;

    @Column(name = "fuel_margin_cents_per_gallon", precision = 8, scale = 2)
    private BigDecimal fuelMarginCentsPerGallon;

    @Column(name = "in_store_sales_usd", precision = 21, scale = 2)
    private BigDecimal inStoreSalesUsd;

    @Column(name = "foodservice_sales_usd", precision = 21, scale = 2)
    private BigDecimal foodserviceSalesUsd;

    @Column(name = "lottery_sales_usd", precision = 21, scale = 2)
    private BigDecimal lotterySalesUsd;

    @Column(name = "transactions_count")
    private Integer transactionsCount;

    @Column(name = "labor_hours", precision = 10, scale = 2)
    private BigDecimal laborHours;

    @Column(name = "labor_cost_usd", precision = 21, scale = 2)
    private BigDecimal laborCostUsd;

    @Column(name = "waste_cost_usd", precision = 21, scale = 2)
    private BigDecimal wasteCostUsd;

    @Column(name = "shrink_cost_usd", precision = 21, scale = 2)
    private BigDecimal shrinkCostUsd;

    @Column(name = "expected_profit_usd", precision = 21, scale = 2)
    private BigDecimal expectedProfitUsd;

    @Column(name = "actual_profit_usd", precision = 21, scale = 2)
    private BigDecimal actualProfitUsd;

    @Lob
    @Column(name = "metrics_json")
    private String metricsJson;

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

    public LocalDate getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(LocalDate metricDate) {
        this.metricDate = metricDate;
    }

    public BigDecimal getFuelGallonsSold() {
        return fuelGallonsSold;
    }

    public void setFuelGallonsSold(BigDecimal fuelGallonsSold) {
        this.fuelGallonsSold = fuelGallonsSold;
    }

    public BigDecimal getFuelRevenueUsd() {
        return fuelRevenueUsd;
    }

    public void setFuelRevenueUsd(BigDecimal fuelRevenueUsd) {
        this.fuelRevenueUsd = fuelRevenueUsd;
    }

    public BigDecimal getFuelMarginCentsPerGallon() {
        return fuelMarginCentsPerGallon;
    }

    public void setFuelMarginCentsPerGallon(BigDecimal fuelMarginCentsPerGallon) {
        this.fuelMarginCentsPerGallon = fuelMarginCentsPerGallon;
    }

    public BigDecimal getInStoreSalesUsd() {
        return inStoreSalesUsd;
    }

    public void setInStoreSalesUsd(BigDecimal inStoreSalesUsd) {
        this.inStoreSalesUsd = inStoreSalesUsd;
    }

    public BigDecimal getFoodserviceSalesUsd() {
        return foodserviceSalesUsd;
    }

    public void setFoodserviceSalesUsd(BigDecimal foodserviceSalesUsd) {
        this.foodserviceSalesUsd = foodserviceSalesUsd;
    }

    public BigDecimal getLotterySalesUsd() {
        return lotterySalesUsd;
    }

    public void setLotterySalesUsd(BigDecimal lotterySalesUsd) {
        this.lotterySalesUsd = lotterySalesUsd;
    }

    public Integer getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(Integer transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public BigDecimal getLaborHours() {
        return laborHours;
    }

    public void setLaborHours(BigDecimal laborHours) {
        this.laborHours = laborHours;
    }

    public BigDecimal getLaborCostUsd() {
        return laborCostUsd;
    }

    public void setLaborCostUsd(BigDecimal laborCostUsd) {
        this.laborCostUsd = laborCostUsd;
    }

    public BigDecimal getWasteCostUsd() {
        return wasteCostUsd;
    }

    public void setWasteCostUsd(BigDecimal wasteCostUsd) {
        this.wasteCostUsd = wasteCostUsd;
    }

    public BigDecimal getShrinkCostUsd() {
        return shrinkCostUsd;
    }

    public void setShrinkCostUsd(BigDecimal shrinkCostUsd) {
        this.shrinkCostUsd = shrinkCostUsd;
    }

    public BigDecimal getExpectedProfitUsd() {
        return expectedProfitUsd;
    }

    public void setExpectedProfitUsd(BigDecimal expectedProfitUsd) {
        this.expectedProfitUsd = expectedProfitUsd;
    }

    public BigDecimal getActualProfitUsd() {
        return actualProfitUsd;
    }

    public void setActualProfitUsd(BigDecimal actualProfitUsd) {
        this.actualProfitUsd = actualProfitUsd;
    }

    public String getMetricsJson() {
        return metricsJson;
    }

    public void setMetricsJson(String metricsJson) {
        this.metricsJson = metricsJson;
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
        if (!(GasStationDailyMetrics.class.isInstance(o))) return false;
        return id != null && id.equals(((GasStationDailyMetrics) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
