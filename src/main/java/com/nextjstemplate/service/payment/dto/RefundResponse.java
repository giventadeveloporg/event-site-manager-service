package com.nextjstemplate.service.payment.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Response DTO for refund operations.
 */
public class RefundResponse {

    private String refundId;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String reason;
    private ZonedDateTime processedAt;
    private Map<String, Object> providerMetadata;

    // Getters and Setters

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public Map<String, Object> getProviderMetadata() {
        return providerMetadata;
    }

    public void setProviderMetadata(Map<String, Object> providerMetadata) {
        this.providerMetadata = providerMetadata;
    }
}
