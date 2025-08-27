package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.BulkOperationLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BulkOperationLogDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    private String operationType;

    @NotNull
    private Integer targetCount;

    private Integer successCount;

    private Integer errorCount;

    @Size(max = 16384)
    private String operationDetails;

    @NotNull
    private ZonedDateTime createdAt;

    private UserProfileDTO performedBy;

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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getOperationDetails() {
        return operationDetails;
    }

    public void setOperationDetails(String operationDetails) {
        this.operationDetails = operationDetails;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfileDTO getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(UserProfileDTO performedBy) {
        this.performedBy = performedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BulkOperationLogDTO)) {
            return false;
        }

        BulkOperationLogDTO bulkOperationLogDTO = (BulkOperationLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bulkOperationLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulkOperationLogDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", targetCount=" + getTargetCount() +
            ", successCount=" + getSuccessCount() +
            ", errorCount=" + getErrorCount() +
            ", operationDetails='" + getOperationDetails() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", performedBy=" + getPerformedBy() +
            "}";
    }
}
