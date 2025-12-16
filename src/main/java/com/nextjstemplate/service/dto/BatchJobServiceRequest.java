package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Internal DTO matching the batch job project's BatchJobRequest structure.
 * Used for communication with the batch job service.
 */
public class BatchJobServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("batchSize")
    private Integer batchSize;

    @JsonProperty("maxSubscriptions")
    private Integer maxSubscriptions;

    @JsonProperty("stripeSubscriptionId")
    private String stripeSubscriptionId;

    public BatchJobServiceRequest() {}

    public BatchJobServiceRequest(String tenantId, Integer batchSize, Integer maxSubscriptions) {
        this.tenantId = tenantId;
        this.batchSize = batchSize;
        this.maxSubscriptions = maxSubscriptions;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    @Override
    public String toString() {
        return (
            "BatchJobServiceRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", batchSize=" +
            batchSize +
            ", maxSubscriptions=" +
            maxSubscriptions +
            ", stripeSubscriptionId='" +
            stripeSubscriptionId +
            '\'' +
            '}'
        );
    }
}
