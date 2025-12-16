package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Internal DTO matching the batch job project's BatchJobResponse structure.
 * Used for communication with the batch job service.
 */
public class BatchJobServiceResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("jobExecutionId")
    private Long jobExecutionId;

    @JsonProperty("processedCount")
    private Long processedCount;

    @JsonProperty("successCount")
    private Long successCount;

    @JsonProperty("failedCount")
    private Long failedCount;

    @JsonProperty("durationMs")
    private Long durationMs;

    public BatchJobServiceResponse() {}

    public BatchJobServiceResponse(
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
            "BatchJobServiceResponse{" +
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
