package com.eventsitemanager.service.dto;

import java.io.Serializable;

/**
 * DTO for contact form email batch job response from the batch jobs microservice.
 * This should mirror the ContactFormEmailJobResponse structure in the batch job project.
 */
public class ContactFormEmailJobResponse implements Serializable {

    private Boolean success;
    private String message;
    private Long jobExecutionId;
    private Long processedCount;
    private Long successCount;
    private Long failedCount;

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
            "ContactFormEmailJobResponse{" +
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
