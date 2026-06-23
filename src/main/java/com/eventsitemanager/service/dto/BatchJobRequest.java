package com.eventsitemanager.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for batch job request parameters.
 */
public class BatchJobRequest {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("stripeSubscriptionId")
    private String stripeSubscriptionId;

    @JsonProperty("batchSize")
    @Min(value = 1, message = "Batch size must be at least 1")
    private Integer batchSize;

    @JsonProperty("maxSubscriptions")
    @Min(value = 1, message = "Max subscriptions must be at least 1")
    private Integer maxSubscriptions;

    public BatchJobRequest() {
        // Default constructor
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getMaxSubscriptions() {
        return maxSubscriptions;
    }

    public void setMaxSubscriptions(Integer maxSubscriptions) {
        this.maxSubscriptions = maxSubscriptions;
    }

    @Override
    public String toString() {
        return (
            "BatchJobRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", stripeSubscriptionId='" +
            stripeSubscriptionId +
            '\'' +
            ", batchSize=" +
            batchSize +
            ", maxSubscriptions=" +
            maxSubscriptions +
            '}'
        );
    }
}
