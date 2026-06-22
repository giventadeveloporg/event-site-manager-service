package com.eventsitemanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QrCodeUsage.
 */
@Entity
@Table(name = "qr_code_usage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QrCodeUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 1000)
    @Column(name = "qr_code_data", length = 1000, nullable = false)
    private String qrCodeData;

    @NotNull
    @Column(name = "generated_at", nullable = false)
    private ZonedDateTime generatedAt;

    @Column(name = "used_at")
    private ZonedDateTime usedAt;

    @Column(name = "usage_count")
    private Integer usageCount;

    @Size(max = 255)
    @Column(name = "last_scanned_by", length = 255)
    private String lastScannedBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event", "attendee" }, allowSetters = true)
    private EventAttendee attendee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QrCodeUsage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public QrCodeUsage tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getQrCodeData() {
        return this.qrCodeData;
    }

    public QrCodeUsage qrCodeData(String qrCodeData) {
        this.setQrCodeData(qrCodeData);
        return this;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public ZonedDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    public QrCodeUsage generatedAt(ZonedDateTime generatedAt) {
        this.setGeneratedAt(generatedAt);
        return this;
    }

    public void setGeneratedAt(ZonedDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ZonedDateTime getUsedAt() {
        return this.usedAt;
    }

    public QrCodeUsage usedAt(ZonedDateTime usedAt) {
        this.setUsedAt(usedAt);
        return this;
    }

    public void setUsedAt(ZonedDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public Integer getUsageCount() {
        return this.usageCount;
    }

    public QrCodeUsage usageCount(Integer usageCount) {
        this.setUsageCount(usageCount);
        return this;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getLastScannedBy() {
        return this.lastScannedBy;
    }

    public QrCodeUsage lastScannedBy(String lastScannedBy) {
        this.setLastScannedBy(lastScannedBy);
        return this;
    }

    public void setLastScannedBy(String lastScannedBy) {
        this.lastScannedBy = lastScannedBy;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public QrCodeUsage createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EventAttendee getAttendee() {
        return this.attendee;
    }

    public void setAttendee(EventAttendee eventAttendee) {
        this.attendee = eventAttendee;
    }

    public QrCodeUsage attendee(EventAttendee eventAttendee) {
        this.setAttendee(eventAttendee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QrCodeUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((QrCodeUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QrCodeUsage{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", qrCodeData='" + getQrCodeData() + "'" +
            ", generatedAt='" + getGeneratedAt() + "'" +
            ", usedAt='" + getUsedAt() + "'" +
            ", usageCount=" + getUsageCount() +
            ", lastScannedBy='" + getLastScannedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
