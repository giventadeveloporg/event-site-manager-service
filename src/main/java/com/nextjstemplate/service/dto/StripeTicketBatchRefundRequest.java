package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Request DTO for Stripe ticket batch refund job.
 */
public class StripeTicketBatchRefundRequest {

    /**
     * Event ID to process refunds for (required).
     */
    @JsonProperty("eventId")
    @NotNull(message = "eventId is required")
    private Long eventId;

    /**
     * Tenant ID for data isolation (optional: defaults to JWT tenant context).
     */
    @JsonProperty("tenantId")
    private String tenantId;

    /**
     * Unique job identifier (optional: auto-generated if not provided).
     */
    @JsonProperty("jobId")
    private String jobId;

    /**
     * Optional start date filter for purchase date (ISO 8601 format).
     * Process transactions with purchase date on or after this date.
     */
    @JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime startDate;

    /**
     * Optional end date filter for purchase date (ISO 8601 format).
     * Process transactions with purchase date on or before this date.
     */
    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime endDate;

    public StripeTicketBatchRefundRequest() {
        // Default constructor
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    @Override
    public String toString() {
        return (
            "StripeTicketBatchRefundRequest{" +
            "eventId=" +
            eventId +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", jobId='" +
            jobId +
            '\'' +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            '}'
        );
    }
}
