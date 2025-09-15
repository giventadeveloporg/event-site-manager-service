package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.WhatsAppLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WhatsAppLogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    private String recipientPhone;

    @Size(max = 4096)
    private String messageBody;

    @NotNull
    private ZonedDateTime sentAt;

    @Size(max = 50)
    private String status;

    @Size(max = 50)
    private String type;

    private Long campaignId;

    @Size(max = 8192)
    private String metadata;

    private CommunicationCampaignDTO campaign;

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

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public CommunicationCampaignDTO getCampaign() {
        return campaign;
    }

    public void setCampaign(CommunicationCampaignDTO campaign) {
        this.campaign = campaign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WhatsAppLogDTO)) {
            return false;
        }

        WhatsAppLogDTO whatsAppLogDTO = (WhatsAppLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, whatsAppLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhatsAppLogDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", recipientPhone='" + getRecipientPhone() + "'" +
            ", messageBody='" + getMessageBody() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", campaignId=" + getCampaignId() +
            ", metadata='" + getMetadata() + "'" +
            ", campaign=" + getCampaign() +
            "}";
    }
}
