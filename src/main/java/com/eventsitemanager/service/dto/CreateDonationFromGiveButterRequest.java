package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a donation transaction from GiveButter data.
 */
public class CreateDonationFromGiveButterRequest {

    @NotNull
    @Size(max = 255)
    private String tenantId;

    private Long eventId;

    @NotNull
    @Size(max = 255)
    private String givebutterDonationId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getGivebutterDonationId() {
        return givebutterDonationId;
    }

    public void setGivebutterDonationId(String givebutterDonationId) {
        this.givebutterDonationId = givebutterDonationId;
    }
}
