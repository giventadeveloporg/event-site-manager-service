package com.nextjstemplate.service.payment.adapter.givebutter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing a Givebutter webhook event.
 */
public class GivebutterWebhookEvent {

    @JsonProperty("type")
    private String type; // "donation.completed", "donation.updated", "donation.refunded", etc.

    @JsonProperty("donation")
    private GivebutterDonation donation;

    @JsonProperty("campaign")
    private GivebutterCampaign campaign;

    // Getters and Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GivebutterDonation getDonation() {
        return donation;
    }

    public void setDonation(GivebutterDonation donation) {
        this.donation = donation;
    }

    public GivebutterCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(GivebutterCampaign campaign) {
        this.campaign = campaign;
    }
}
