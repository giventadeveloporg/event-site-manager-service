package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

/**
 * DTO for Stripe fees and tax update batch job request.
 */
public class StripeFeesTaxUpdateRequest {

    /**
     * Optional tenant ID to filter by. If not provided, processes all tenants.
     */
    @JsonProperty("tenantId")
    private String tenantId;

    /**
     * Optional start date (ISO 8601 format). Process transactions created on or after this date.
     */
    @JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime startDate;

    /**
     * Optional end date (ISO 8601 format). Process transactions created on or before this date.
     */
    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime endDate;

    /**
     * If true, update even if stripe_fee_amount is already populated. Default: false.
     */
    @JsonProperty("forceUpdate")
    private Boolean forceUpdate = false;

    public StripeFeesTaxUpdateRequest() {
        // Default constructor
    }

    public StripeFeesTaxUpdateRequest(String tenantId, ZonedDateTime startDate, ZonedDateTime endDate, Boolean forceUpdate) {
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.forceUpdate = forceUpdate != null ? forceUpdate : false;
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
        this.forceUpdate = forceUpdate != null ? forceUpdate : false;
    }

    @Override
    public String toString() {
        return (
            "StripeFeesTaxUpdateRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", startDate=" +
            startDate +
            ", endDate=" +
            endDate +
            ", forceUpdate=" +
            forceUpdate +
            '}'
        );
    }
}
