package com.eventsitemanager.service.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO for GiveButter donation status response.
 */
public class GiveButterDonationStatusDTO {

    private String donationId;
    private String status;
    private BigDecimal amount;
    private String email;
    private ZonedDateTime createdAt;

    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
