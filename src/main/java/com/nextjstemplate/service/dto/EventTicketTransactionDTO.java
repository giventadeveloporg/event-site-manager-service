package com.nextjstemplate.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventTicketTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @Size(max = 255)
    private String transactionReference;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String phone;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    @NotNull
    private BigDecimal totalAmount;

    private BigDecimal taxAmount;

    private BigDecimal platformFeeAmount;

    private BigDecimal serviceFee;
    private Long discountCodeId;

    private BigDecimal discountAmount;

    @NotNull
    private BigDecimal finalAmount;

    @NotNull
    @Size(max = 255)
    private String status;

    @Size(max = 100)
    private String paymentMethod;

    @Size(max = 255)
    private String paymentReference;

    @NotNull
    private ZonedDateTime purchaseDate;

    private ZonedDateTime confirmationSentAt;

    private BigDecimal refundAmount;

    private ZonedDateTime refundDate;

    @Size(max = 2048)
    private String refundReason;

    @Size(max = 255)
    private String stripeCheckoutSessionId;

    @Size(max = 255)
    private String stripePaymentIntentId;

    @Size(max = 255)
    private String stripeCustomerId;

    @Size(max = 50)
    private String stripePaymentStatus;

    @Size(max = 255)
    private String stripeCustomerEmail;

    @Size(max = 10)
    private String stripePaymentCurrency;

    private BigDecimal stripeAmountDiscount;

    private BigDecimal stripeAmountTax;

    private BigDecimal stripeFeeAmount;

    @Size(max = 2048)
    private String qrCodeImageUrl;
    private Long eventId;

    private Long userId;
    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @Size(max = 50)
    private String checkInStatus;

    private Integer numberOfGuestsCheckedIn;

    private ZonedDateTime checkInTime;

    private ZonedDateTime checkOutTime;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Long getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public ZonedDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ZonedDateTime getConfirmationSentAt() {
        return confirmationSentAt;
    }

    public void setConfirmationSentAt(ZonedDateTime confirmationSentAt) {
        this.confirmationSentAt = confirmationSentAt;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public ZonedDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(ZonedDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getStripeCheckoutSessionId() {
        return stripeCheckoutSessionId;
    }

    public void setStripeCheckoutSessionId(String stripeCheckoutSessionId) {
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripePaymentStatus() {
        return stripePaymentStatus;
    }

    public void setStripePaymentStatus(String stripePaymentStatus) {
        this.stripePaymentStatus = stripePaymentStatus;
    }

    public String getStripeCustomerEmail() {
        return stripeCustomerEmail;
    }

    public void setStripeCustomerEmail(String stripeCustomerEmail) {
        this.stripeCustomerEmail = stripeCustomerEmail;
    }

    public String getStripePaymentCurrency() {
        return stripePaymentCurrency;
    }

    public void setStripePaymentCurrency(String stripePaymentCurrency) {
        this.stripePaymentCurrency = stripePaymentCurrency;
    }

    public BigDecimal getStripeAmountDiscount() {
        return stripeAmountDiscount;
    }

    public void setStripeAmountDiscount(BigDecimal stripeAmountDiscount) {
        this.stripeAmountDiscount = stripeAmountDiscount;
    }

    public BigDecimal getStripeAmountTax() {
        return stripeAmountTax;
    }

    public void setStripeAmountTax(BigDecimal stripeAmountTax) {
        this.stripeAmountTax = stripeAmountTax;
    }

    public BigDecimal getStripeFeeAmount() {
        return stripeFeeAmount;
    }

    public void setStripeFeeAmount(BigDecimal stripeFeeAmount) {
        this.stripeFeeAmount = stripeFeeAmount;
    }

    public String getQrCodeImageUrl() {
        return qrCodeImageUrl;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public Integer getNumberOfGuestsCheckedIn() {
        return numberOfGuestsCheckedIn;
    }

    public void setNumberOfGuestsCheckedIn(Integer numberOfGuestsCheckedIn) {
        this.numberOfGuestsCheckedIn = numberOfGuestsCheckedIn;
    }

    public ZonedDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public ZonedDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTicketTransactionDTO)) {
            return false;
        }

        EventTicketTransactionDTO eventTicketTransactionDTO = (EventTicketTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventTicketTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransactionDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", transactionReference='" + getTransactionReference() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", platformFeeAmount=" + getPlatformFeeAmount() +
            ", serviceFee=" + getServiceFee() +
            ", discountCodeId=" + getDiscountCodeId() +
            ", discountAmount=" + getDiscountAmount() +
            ", finalAmount=" + getFinalAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", confirmationSentAt='" + getConfirmationSentAt() + "'" +
            ", refundAmount=" + getRefundAmount() +
            ", refundDate='" + getRefundDate() + "'" +
            ", refundReason='" + getRefundReason() + "'" +
            ", stripeCheckoutSessionId='" + getStripeCheckoutSessionId() + "'" +
            ", stripePaymentIntentId='" + getStripePaymentIntentId() + "'" +
            ", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", stripePaymentStatus='" + getStripePaymentStatus() + "'" +
            ", stripeCustomerEmail='" + getStripeCustomerEmail() + "'" +
            ", stripePaymentCurrency='" + getStripePaymentCurrency() + "'" +
            ", stripeAmountDiscount=" + getStripeAmountDiscount() +
            ", stripeAmountTax=" + getStripeAmountTax() +
            ", stripeFeeAmount=" + getStripeFeeAmount() +
            ", qrCodeImageUrl='" + getQrCodeImageUrl() + "'" +
            ", eventId=" + getEventId() +
            ", userId=" + getUserId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", checkInStatus='" + getCheckInStatus() + "'" +
            ", numberOfGuestsCheckedIn=" + getNumberOfGuestsCheckedIn() +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            "}";
    }
}
