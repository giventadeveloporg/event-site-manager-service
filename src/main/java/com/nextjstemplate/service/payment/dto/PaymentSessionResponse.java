package com.nextjstemplate.service.payment.dto;

import com.nextjstemplate.domain.enumeration.PaymentMethod;
import com.nextjstemplate.domain.enumeration.PaymentProvider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for payment session operations.
 */
public class PaymentSessionResponse {

    private String transactionId;
    private PaymentProvider provider;
    private String status;
    private String clientSecret;
    private String sessionUrl;
    private String publishableKey;
    private List<PaymentMethod> supportedMethods;
    private BigDecimal amount;
    private String currency;
    private Map<String, Object> providerMetadata;
    private Boolean requiresAction;
    private String actionType;
    private Map<String, Object> actionData;
    private String failureReason;
    private Map<String, Object> metadata;

    // CRITICAL: Stripe payment intent ID - REQUIRED for frontend ticket polling
    private String stripePaymentIntentId;

    // Ticket purchase fields (only populated when status=SUCCEEDED and it's a ticket purchase)
    private Long ticketTransactionId; // EventTicketTransaction ID
    private String qrCodeUrl; // QR code image URL - REQUIRED for frontend display
    private Boolean emailSent; // Whether ticket email was sent
    private Long eventId; // Event ID for ticket purchases

    // Getters and Setters

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public void setProvider(PaymentProvider provider) {
        this.provider = provider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public List<PaymentMethod> getSupportedMethods() {
        return supportedMethods;
    }

    public void setSupportedMethods(List<PaymentMethod> supportedMethods) {
        this.supportedMethods = supportedMethods;
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

    public Map<String, Object> getProviderMetadata() {
        return providerMetadata;
    }

    public void setProviderMetadata(Map<String, Object> providerMetadata) {
        this.providerMetadata = providerMetadata;
    }

    public Boolean getRequiresAction() {
        return requiresAction;
    }

    public void setRequiresAction(Boolean requiresAction) {
        this.requiresAction = requiresAction;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Map<String, Object> getActionData() {
        return actionData;
    }

    public void setActionData(Map<String, Object> actionData) {
        this.actionData = actionData;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Long getTicketTransactionId() {
        return ticketTransactionId;
    }

    public void setTicketTransactionId(Long ticketTransactionId) {
        this.ticketTransactionId = ticketTransactionId;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
}
