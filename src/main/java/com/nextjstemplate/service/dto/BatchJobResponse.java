package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for batch job response.
 */
public class BatchJobResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("jobId")
    private String jobId;

    @JsonProperty("jobName")
    private String jobName;

    @JsonProperty("message")
    private String message;

    @JsonProperty("estimatedDuration")
    private String estimatedDuration;

    @JsonProperty("request")
    private BatchJobRequest request;

    public BatchJobResponse() {
        // Default constructor
    }

    public BatchJobResponse(
        String status,
        String jobId,
        String jobName,
        String message,
        String estimatedDuration,
        BatchJobRequest request
    ) {
        this.status = status;
        this.jobId = jobId;
        this.jobName = jobName;
        this.message = message;
        this.estimatedDuration = estimatedDuration;
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public BatchJobRequest getRequest() {
        return request;
    }

    public void setRequest(BatchJobRequest request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return (
            "BatchJobResponse{" +
            "status='" +
            status +
            '\'' +
            ", jobId='" +
            jobId +
            '\'' +
            ", jobName='" +
            jobName +
            '\'' +
            ", message='" +
            message +
            '\'' +
            ", estimatedDuration='" +
            estimatedDuration +
            '\'' +
            ", request=" +
            request +
            '}'
        );
    }
}
