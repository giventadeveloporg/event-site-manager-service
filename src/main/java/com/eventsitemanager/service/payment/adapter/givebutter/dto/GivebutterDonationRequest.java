package com.eventsitemanager.service.payment.adapter.givebutter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Request DTO for creating a Givebutter donation.
 */
public class GivebutterDonationRequest {

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("campaignId")
    private String campaignId;

    @JsonProperty("donorEmail")
    private String donorEmail;

    @JsonProperty("donorName")
    private String donorName;

    @JsonProperty("anonymous")
    private Boolean anonymous;

    @JsonProperty("recurring")
    private Boolean recurring;

    @JsonProperty("recurringFrequency")
    private String recurringFrequency; // "monthly", "weekly", "yearly", etc.

    @JsonProperty("customFields")
    private Map<String, String> customFields; // For prayer intentions

    @JsonProperty("metadata")
    private Map<String, Object> metadata; // tenant_id, event_id, etc.

    // Getters and Setters

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
