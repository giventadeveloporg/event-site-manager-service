package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.GasStationConnectionMode;
import com.eventsitemanager.domain.enumeration.GasStationSystemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "gas_station_integration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GasStationIntegration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gasStationIntegrationSeq")
    @SequenceGenerator(name = "gasStationIntegrationSeq", sequenceName = "public.gas_station_integration_id_seq", allocationSize = 1)
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
    @Enumerated(EnumType.STRING)
    @Column(name = "system_type", length = 32, nullable = false)
    private GasStationSystemType systemType;

    @Size(max = 255)
    @Column(name = "provider_name", length = 255)
    private String providerName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "connection_mode", length = 32, nullable = false)
    private GasStationConnectionMode connectionMode = GasStationConnectionMode.MANUAL;

    @Lob
    @Column(name = "config_json")
    private String configJson;

    @Size(max = 512)
    @Column(name = "credentials_ref", length = 512)
    private String credentialsRef;

    @Size(max = 32)
    @Column(name = "sync_frequency", length = 32)
    private String syncFrequency;

    @Column(name = "last_sync_at")
    private ZonedDateTime lastSyncAt;

    @Size(max = 32)
    @Column(name = "last_sync_status", length = 32)
    private String lastSyncStatus;

    @NotNull
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = Boolean.TRUE;

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
        if (!(GasStationIntegration.class.isInstance(o))) return false;
        return id != null && id.equals(((GasStationIntegration) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
