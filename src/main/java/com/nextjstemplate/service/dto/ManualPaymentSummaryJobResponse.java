package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Response DTO for manual payment summary aggregation job (batch-jobs microservice).
 */
public class ManualPaymentSummaryJobResponse {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("jobExecutionId")
    private Long jobExecutionId;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("eventId")
    private Long eventId;

    @JsonProperty("snapshotDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate snapshotDate;

    public ManualPaymentSummaryJobResponse() {}

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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentSummaryJobResponse{" +
            "success=" +
            success +
            ", message='" +
            message +
            '\'' +
            ", jobExecutionId=" +
            jobExecutionId +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", eventId=" +
            eventId +
            ", snapshotDate=" +
            snapshotDate +
            '}'
        );
    }
}
