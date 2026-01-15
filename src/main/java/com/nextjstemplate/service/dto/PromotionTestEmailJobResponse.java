package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing a promotion test email job response from the batch jobs microservice.
 */
public class PromotionTestEmailJobResponse {

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

    public PromotionTestEmailJobResponse() {}

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

    @Override
    public String toString() {
        return (
            "PromotionTestEmailJobResponse{" +
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
            '}'
        );
    }
}
