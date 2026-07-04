package com.eventsitemanager.service.dto;

import com.eventsitemanager.domain.enumeration.GasStationConnectionMode;
import com.eventsitemanager.domain.enumeration.GasStationSystemType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationIntegrationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long stationId;

    @NotNull
    private GasStationSystemType systemType;

    @Size(max = 255)
    private String providerName;

    @NotNull
    private GasStationConnectionMode connectionMode;

    private String configJson;

    @Size(max = 512)
    private String credentialsRef;

    @Size(max = 32)
    private String syncFrequency;

    private ZonedDateTime lastSyncAt;

    @Size(max = 32)
    private String lastSyncStatus;

    private Boolean isEnabled;

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

    public GasStationSystemType getSystemType() {
        return systemType;
    }

    public void setSystemType(GasStationSystemType systemType) {
        this.systemType = systemType;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public GasStationConnectionMode getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(GasStationConnectionMode connectionMode) {
        this.connectionMode = connectionMode;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public String getCredentialsRef() {
        return credentialsRef;
    }

    public void setCredentialsRef(String credentialsRef) {
        this.credentialsRef = credentialsRef;
    }

    public String getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(String syncFrequency) {
        this.syncFrequency = syncFrequency;
    }

    public ZonedDateTime getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(ZonedDateTime lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }

    public String getLastSyncStatus() {
        return lastSyncStatus;
    }

    public void setLastSyncStatus(String lastSyncStatus) {
        this.lastSyncStatus = lastSyncStatus;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
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
        if (!(GasStationIntegrationDTO.class.isInstance(o))) return false;
        GasStationIntegrationDTO other = (GasStationIntegrationDTO) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
