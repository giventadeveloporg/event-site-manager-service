package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BulkOperationLog.
 */
@Entity
@Table(name = "bulk_operation_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BulkOperationLog implements Serializable {

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
    @Size(max = 50)
    @Column(name = "operation_type", length = 50, nullable = false)
    private String operationType;

    @NotNull
    @Column(name = "target_count", nullable = false)
    private Integer targetCount;

    @Column(name = "success_count")
    private Integer successCount;

    @Column(name = "error_count")
    private Integer errorCount;

    @Size(max = 16384)
    @Column(name = "operation_details", length = 16384)
    private String operationDetails;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile performedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BulkOperationLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public BulkOperationLog tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOperationType() {
        return this.operationType;
    }

    public BulkOperationLog operationType(String operationType) {
        this.setOperationType(operationType);
        return this;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getTargetCount() {
        return this.targetCount;
    }

    public BulkOperationLog targetCount(Integer targetCount) {
        this.setTargetCount(targetCount);
        return this;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getSuccessCount() {
        return this.successCount;
    }

    public BulkOperationLog successCount(Integer successCount) {
        this.setSuccessCount(successCount);
        return this;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getErrorCount() {
        return this.errorCount;
    }

    public BulkOperationLog errorCount(Integer errorCount) {
        this.setErrorCount(errorCount);
        return this;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getOperationDetails() {
        return this.operationDetails;
    }

    public BulkOperationLog operationDetails(String operationDetails) {
        this.setOperationDetails(operationDetails);
        return this;
    }

    public void setOperationDetails(String operationDetails) {
        this.operationDetails = operationDetails;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public BulkOperationLog createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfile getPerformedBy() {
        return this.performedBy;
    }

    public void setPerformedBy(UserProfile userProfile) {
        this.performedBy = userProfile;
    }

    public BulkOperationLog performedBy(UserProfile userProfile) {
        this.setPerformedBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BulkOperationLog)) {
            return false;
        }
        return getId() != null && getId().equals(((BulkOperationLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulkOperationLog{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", targetCount=" + getTargetCount() +
            ", successCount=" + getSuccessCount() +
            ", errorCount=" + getErrorCount() +
            ", operationDetails='" + getOperationDetails() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
