package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.BatchJobExecution} entity.
 * Tracks batch job execution history and statistics.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BatchJobExecutionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String jobName;

    @NotNull
    @Size(max = 50)
    private String jobType;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "RUNNING|COMPLETED|FAILED|CANCELLED")
    private String status;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    private ZonedDateTime startedAt;

    private ZonedDateTime completedAt;

    private Long durationMs;

    private Long processedCount = 0L;

    private Long successCount = 0L;

    private Long failedCount = 0L;

    private String errorMessage;

    @Size(max = 100)
    private String triggeredBy;

    private String parametersJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ZonedDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public ZonedDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(ZonedDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public Long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(Long processedCount) {
        this.processedCount = processedCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getParametersJson() {
        return parametersJson;
    }

    public void setParametersJson(String parametersJson) {
        this.parametersJson = parametersJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatchJobExecutionDTO)) {
            return false;
        }

        BatchJobExecutionDTO batchJobExecutionDTO = (BatchJobExecutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, batchJobExecutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BatchJobExecutionDTO{" +
            "id=" + getId() +
            ", jobName='" + getJobName() + "'" +
            ", jobType='" + getJobType() + "'" +
            ", status='" + getStatus() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", durationMs=" + getDurationMs() +
            ", processedCount=" + getProcessedCount() +
            ", successCount=" + getSuccessCount() +
            ", failedCount=" + getFailedCount() +
            ", triggeredBy='" + getTriggeredBy() + "'" +
            "}";
    }
}
