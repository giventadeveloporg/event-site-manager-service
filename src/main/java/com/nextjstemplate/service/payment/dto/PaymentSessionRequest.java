package com.nextjstemplate.service.payment.dto;

import com.nextjstemplate.domain.enumeration.PaymentUseCase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Request DTO for initializing a payment session.
 */
public class PaymentSessionRequest {

    @NotNull
    private String tenantId;

    @NotNull
    private PaymentUseCase paymentUseCase;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Size(min = 3, max = 3)
    private String currency;

    private String description;

    private String customerEmail;

    private String customerName;

    private Long eventId;

    private Long membershipPlanId;

    private Map<String, Object> metadata;

    private String returnUrl;

    private String cancelUrl;

    private Boolean requiresConfirmation;

    // Getters and Setters

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public PaymentUseCase getPaymentUseCase() {
        return paymentUseCase;
    }

    public void setPaymentUseCase(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getMembershipPlanId() {
        return membershipPlanId;
    }

    public void setMembershipPlanId(Long membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public Boolean getRequiresConfirmation() {
        return requiresConfirmation;
    }

    public void setRequiresConfirmation(Boolean requiresConfirmation) {
        this.requiresConfirmation = requiresConfirmation;
    }
}
