package com.eventsitemanager.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Request DTO for manual payment summary aggregation job (batch-jobs microservice).
 */
public class ManualPaymentSummaryJobRequest {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("eventId")
    private Long eventId;

    @JsonProperty("snapshotDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate snapshotDate;

    public ManualPaymentSummaryJobRequest() {}

    public ManualPaymentSummaryJobRequest(String tenantId, Long eventId, LocalDate snapshotDate) {
        this.tenantId = tenantId;
        this.eventId = eventId;
        this.snapshotDate = snapshotDate;
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
            "ManualPaymentSummaryJobRequest{" +
            "tenantId='" +
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
