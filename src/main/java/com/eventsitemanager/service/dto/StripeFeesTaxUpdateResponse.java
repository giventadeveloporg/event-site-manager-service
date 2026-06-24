package com.eventsitemanager.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZonedDateTime;

/**
 * DTO for Stripe fees and tax update batch job response.
 */
public class StripeFeesTaxUpdateResponse {

    @JsonProperty("jobId")
    private String jobId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("startDate")
    @JsonDeserialize(using = LenientZonedDateTimeDeserializer.class)
    private ZonedDateTime startDate;

    @JsonProperty("endDate")
    @JsonDeserialize(using = LenientZonedDateTimeDeserializer.class)
    private ZonedDateTime endDate;

    @JsonProperty("forceUpdate")
    private Boolean forceUpdate;

    @JsonProperty("estimatedRecords")
    private Long estimatedRecords;

    @JsonProperty("estimatedCompletionTime")
    @JsonDeserialize(using = LenientZonedDateTimeDeserializer.class)
    private ZonedDateTime estimatedCompletionTime;

    @JsonProperty("message")
    private String message;

    public StripeFeesTaxUpdateResponse() {
        // Default constructor
    }

    public StripeFeesTaxUpdateResponse(
        String jobId,
        String status,
        String tenantId,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        Boolean forceUpdate,
        Long estimatedRecords,
        ZonedDateTime estimatedCompletionTime,
        String message
    ) {
        this.jobId = jobId;
        this.status = status;
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.forceUpdate = forceUpdate;
        this.estimatedRecords = estimatedRecords;
        this.estimatedCompletionTime = estimatedCompletionTime;
        this.message = message;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Long getEstimatedRecords() {
        return estimatedRecords;
    }

    public void setEstimatedRecords(Long estimatedRecords) {
        this.estimatedRecords = estimatedRecords;
    }

    public ZonedDateTime getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(ZonedDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (
            "StripeFeesTaxUpdateResponse{" +
            "jobId='" +
            jobId +
            '\'' +
            ", status='" +
            status +
            '\'' +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            ", forceUpdate=" +
            forceUpdate +
            ", estimatedRecords=" +
            estimatedRecords +
            ", estimatedCompletionTime=" +
            estimatedCompletionTime +
            ", message='" +
            message +
            '\'' +
            '}'
        );
    }
}
