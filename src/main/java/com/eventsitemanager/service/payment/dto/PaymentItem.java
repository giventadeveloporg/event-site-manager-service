package com.eventsitemanager.service.payment.dto;

import java.math.BigDecimal;

/**
 * DTO representing a payment item (e.g., ticket, donation item).
 */
public class PaymentItem {

    private String itemType; // e.g., "TICKET", "OFFERING", "DONATION"
    private Long itemId; // e.g., ticketTypeId for tickets
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;

    // Getters and Setters

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
