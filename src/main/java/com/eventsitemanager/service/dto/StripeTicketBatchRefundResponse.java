package com.eventsitemanager.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Response DTO for Stripe ticket batch refund job.
 */
public class StripeTicketBatchRefundResponse {

    @JsonProperty("jobId")
    private String jobId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("eventId")
    private Long eventId;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("startDate")
    private ZonedDateTime startDate;

    @JsonProperty("endDate")
    private ZonedDateTime endDate;

    @JsonProperty("totalEligibleTickets")
    private Long totalEligibleTickets;

    @JsonProperty("processedCount")
    private Long processedCount;

    @JsonProperty("successCount")
    private Long successCount;

    @JsonProperty("failedCount")
    private Long failedCount;

    @JsonProperty("skippedCount")
    private Long skippedCount;

    @JsonProperty("totalRefundAmount")
    private BigDecimal totalRefundAmount;

    @JsonProperty("startTime")
    private ZonedDateTime startTime;

    @JsonProperty("estimatedCompletionTime")
    private ZonedDateTime estimatedCompletionTime;

    @JsonProperty("message")
    private String message;

    @JsonProperty("failedRefunds")
    private List<FailedRefund> failedRefunds;

    public StripeTicketBatchRefundResponse() {
        // Default constructor
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

    public Long getTotalEligibleTickets() {
        return totalEligibleTickets;
    }

    public void setTotalEligibleTickets(Long totalEligibleTickets) {
        this.totalEligibleTickets = totalEligibleTickets;
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

    public Long getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(Long skippedCount) {
        this.skippedCount = skippedCount;
    }

    public BigDecimal getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
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

    public List<FailedRefund> getFailedRefunds() {
        return failedRefunds;
    }

    public void setFailedRefunds(List<FailedRefund> failedRefunds) {
        this.failedRefunds = failedRefunds;
    }

    /**
     * Inner class for failed refund details.
     */
    public static class FailedRefund {

        @JsonProperty("ticketTransactionId")
        private Long ticketTransactionId;

        @JsonProperty("errorMessage")
        private String errorMessage;

        @JsonProperty("errorType")
        private String errorType;

        public FailedRefund() {
            // Default constructor
        }

        public Long getTicketTransactionId() {
            return ticketTransactionId;
        }

        public void setTicketTransactionId(Long ticketTransactionId) {
            this.ticketTransactionId = ticketTransactionId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }
    }
}
