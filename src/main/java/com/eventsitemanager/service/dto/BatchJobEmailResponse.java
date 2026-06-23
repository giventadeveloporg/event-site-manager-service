package com.eventsitemanager.service.dto;

import java.io.Serializable;

/**
 * DTO for batch job email response.
 */
public class BatchJobEmailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String message;
    private Long jobExecutionId;
    private Long processedCount;
    private Long successCount;
    private Long failedCount;
    private Long durationMs;

    public BatchJobEmailResponse() {}

    public BatchJobEmailResponse(
        Boolean success,
        String message,
        Long jobExecutionId,
        Long processedCount,
        Long successCount,
        Long failedCount,
        Long durationMs
    ) {
        this.success = success;
        this.message = message;
        this.jobExecutionId = jobExecutionId;
        this.processedCount = processedCount;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.durationMs = durationMs;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getJobExecutionId() {
        return jobExecutionId;
    }

    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
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

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    @Override
    public String toString() {
        return (
            "BatchJobEmailResponse{" +
            "success=" +
            success +
            ", message='" +
            message +
            '\'' +
            ", jobExecutionId=" +
            jobExecutionId +
            ", processedCount=" +
            processedCount +
            ", successCount=" +
            successCount +
            ", failedCount=" +
            failedCount +
            ", durationMs=" +
            durationMs +
            '}'
        );
    }
}
