package com.eventsitemanager.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for submitting a manual payment ticket email job to the batch jobs microservice.
 * This should mirror the ManualPaymentTicketEmailJobRequest structure expected by the
 * Event Site Manager Batch Jobs project.
 */
public class ManualPaymentTicketEmailJobRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;
    private Long paymentRequestId;
    private Long eventId;
    private Long ticketTransactionId;
    private String recipientEmail;
    private String recipientName;
    private String eventTitle;
    private String eventDate;
    private String eventLocation;
    private String qrCodeImageUrl;
    private List<TicketItemDTO> ticketItems;
    private BigDecimal totalAmount;
    private String transactionReference;
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

    public Long getTicketTransactionId() {
        return ticketTransactionId;
    }

    public void setTicketTransactionId(Long ticketTransactionId) {
        this.ticketTransactionId = ticketTransactionId;
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

    public String getQrCodeImageUrl() {
        return qrCodeImageUrl;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }

    public List<TicketItemDTO> getTicketItems() {
        return ticketItems;
    }

    public void setTicketItems(List<TicketItemDTO> ticketItems) {
        this.ticketItems = ticketItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Inner DTO for ticket items in the ticket email.
     */
    public static class TicketItemDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private String ticketTypeName;
        private Integer quantity;
        private BigDecimal pricePerUnit;
        private BigDecimal totalAmount;

        public String getTicketTypeName() {
            return ticketTypeName;
        }

        public void setTicketTypeName(String ticketTypeName) {
            this.ticketTypeName = ticketTypeName;
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
    }

    @Override
    public String toString() {
        return (
            "ManualPaymentTicketEmailJobRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", paymentRequestId=" +
            paymentRequestId +
            ", eventId=" +
            eventId +
            ", ticketTransactionId=" +
            ticketTransactionId +
            ", recipientEmail='" +
            recipientEmail +
            '\'' +
            ", recipientName='" +
            recipientName +
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
            ", qrCodeImageUrl='" +
            qrCodeImageUrl +
            '\'' +
            ", ticketItems=" +
            (ticketItems != null ? ticketItems.size() + " items" : "null") +
            ", totalAmount=" +
            totalAmount +
            ", transactionReference='" +
            transactionReference +
            '\'' +
            ", userId=" +
            userId +
            '}'
        );
    }
}
