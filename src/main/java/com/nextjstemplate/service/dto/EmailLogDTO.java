package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EmailLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailLogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String recipientEmail;

    @Size(max = 255)
    private String subject;

    @Size(max = 32768)
    private String body;

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

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
        if (!(o instanceof EmailLogDTO)) {
            return false;
        }

        EmailLogDTO emailLogDTO = (EmailLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailLogDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", recipientEmail='" + getRecipientEmail() + "'" +
            ", subject='" + getSubject() + "'" +
            ", body='" + getBody() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", campaignId=" + getCampaignId() +
            ", metadata='" + getMetadata() + "'" +
            ", campaign=" + getCampaign() +
            "}";
    }
}
