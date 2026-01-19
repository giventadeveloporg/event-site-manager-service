package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for submitting a manual payment confirmation email job to the batch jobs microservice.
 * This should mirror the ManualPaymentConfirmationEmailJobRequest structure expected by the
 * Event Site Manager Batch Jobs project.
 */
public class ManualPaymentConfirmationEmailJobRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;
    private Long paymentRequestId;
    private Long eventId;
    private String recipientEmail;
    private String recipientName;
    private String paymentMethod;
    private BigDecimal amount;
    private String paymentInstructions;
    private String eventTitle;
    private String eventDate;
    private String eventLocation;
    private Long userId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(Long paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentInstructions() {
        return paymentInstructions;
    }

    public void setPaymentInstructions(String paymentInstructions) {
        this.paymentInstructions = paymentInstructions;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentConfirmationEmailJobRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", paymentRequestId=" +
            paymentRequestId +
            ", eventId=" +
            eventId +
            ", recipientEmail='" +
            recipientEmail +
            '\'' +
            ", recipientName='" +
            recipientName +
            '\'' +
            ", paymentMethod='" +
            paymentMethod +
            '\'' +
            ", amount=" +
            amount +
            ", paymentInstructions='" +
            (paymentInstructions != null ? "[length=" + paymentInstructions.length() + "]" : null) +
            '\'' +
            ", eventTitle='" +
            eventTitle +
            '\'' +
            ", eventDate='" +
            eventDate +
            '\'' +
            ", eventLocation='" +
            eventLocation +
            '\'' +
            ", userId=" +
            userId +
            '}'
        );
    }
}
