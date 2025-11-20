package com.nextjstemplate.service.payment.adapter.givebutter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing a Givebutter campaign in webhook events.
 */
public class GivebutterCampaign {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("metadata")
    private java.util.Map<String, Object> metadata;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(java.util.Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
