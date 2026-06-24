package com.eventsitemanager.service.payment.adapter.givebutter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO representing a Givebutter donation in webhook events.
 */
public class GivebutterDonation {

    @JsonProperty("id")
    private String id;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("campaign_id")
    private String campaignId;

    @JsonProperty("donor_email")
    private String donorEmail;

    @JsonProperty("donor_name")
    private String donorName;

    @JsonProperty("anonymous")
    private Boolean anonymous;

    @JsonProperty("custom_fields")
    private Map<String, String> customFields;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("status")
    private String status;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
