package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationDailyMetricsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long stationId;

    @NotNull
    private LocalDate metricDate;

    private BigDecimal fuelGallonsSold;

    private BigDecimal fuelRevenueUsd;

    private BigDecimal fuelMarginCentsPerGallon;

    private BigDecimal inStoreSalesUsd;

    private BigDecimal foodserviceSalesUsd;

    private BigDecimal lotterySalesUsd;

    private Integer transactionsCount;

    private BigDecimal laborHours;

    private BigDecimal laborCostUsd;

    private BigDecimal wasteCostUsd;

    private BigDecimal shrinkCostUsd;

    private BigDecimal expectedProfitUsd;

    private BigDecimal actualProfitUsd;

    private String metricsJson;

    @Size(max = 128)
    private String sourceModelRunId;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

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
        if (!(GasStationDailyMetricsDTO.class.isInstance(o))) return false;
        GasStationDailyMetricsDTO other = (GasStationDailyMetricsDTO) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
