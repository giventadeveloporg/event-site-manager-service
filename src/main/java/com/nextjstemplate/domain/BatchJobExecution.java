package com.nextjstemplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BatchJobExecution.
 * Tracks batch job execution history and statistics.
 */
@Entity
@Table(name = "batch_job_execution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BatchJobExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "job_name", length = 100, nullable = false)
    private String jobName;

    @NotNull
    @Size(max = 50)
    @Column(name = "job_type", length = 50, nullable = false)
    private String jobType;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private ZonedDateTime startedAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "processed_count")
    private Long processedCount = 0L;

    @Column(name = "success_count")
    private Long successCount = 0L;

    @Column(name = "failed_count")
    private Long failedCount = 0L;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Size(max = 100)
    @Column(name = "triggered_by", length = 100)
    private String triggeredBy;

    @Column(name = "parameters_json", columnDefinition = "text")
    private String parametersJson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BatchJobExecution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return this.jobName;
    }

    public BatchJobExecution jobName(String jobName) {
        this.setJobName(jobName);
        return this;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return this.jobType;
    }

    public BatchJobExecution jobType(String jobType) {
        this.setJobType(jobType);
        return this;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return this.status;
    }

    public BatchJobExecution status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public BatchJobExecution tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ZonedDateTime getStartedAt() {
        return this.startedAt;
    }

    public BatchJobExecution startedAt(ZonedDateTime startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public ZonedDateTime getCompletedAt() {
        return this.completedAt;
    }

    public BatchJobExecution completedAt(ZonedDateTime completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(ZonedDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getDurationMs() {
        return this.durationMs;
    }

    public BatchJobExecution durationMs(Long durationMs) {
        this.setDurationMs(durationMs);
        return this;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public Long getProcessedCount() {
        return this.processedCount;
    }

    public BatchJobExecution processedCount(Long processedCount) {
        this.setProcessedCount(processedCount);
        return this;
    }

    public void setProcessedCount(Long processedCount) {
        this.processedCount = processedCount;
    }

    public Long getSuccessCount() {
        return this.successCount;
    }

    public BatchJobExecution successCount(Long successCount) {
        this.setSuccessCount(successCount);
        return this;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailedCount() {
        return this.failedCount;
    }

    public BatchJobExecution failedCount(Long failedCount) {
        this.setFailedCount(failedCount);
        return this;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public BatchJobExecution errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTriggeredBy() {
        return this.triggeredBy;
    }

    public BatchJobExecution triggeredBy(String triggeredBy) {
        this.setTriggeredBy(triggeredBy);
        return this;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getParametersJson() {
        return this.parametersJson;
    }

    public BatchJobExecution parametersJson(String parametersJson) {
        this.setParametersJson(parametersJson);
        return this;
    }

    public void setParametersJson(String parametersJson) {
        this.parametersJson = parametersJson;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatchJobExecution)) {
            return false;
        }
        return getId() != null && getId().equals(((BatchJobExecution) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BatchJobExecution{" +
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
