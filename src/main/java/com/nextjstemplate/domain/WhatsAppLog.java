package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WhatsAppLog.
 */
@Entity
@Table(name = "whats_app_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WhatsAppLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Size(max = 50)
    @Column(name = "recipient_phone", length = 50, nullable = false)
    private String recipientPhone;

    @Size(max = 4096)
    @Column(name = "message_body", length = 4096)
    private String messageBody;

    @NotNull
    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Size(max = 50)
    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Size(max = 8192)
    @Column(name = "metadata", length = 8192)
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "createdBy" }, allowSetters = true)
    private CommunicationCampaign campaign;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WhatsAppLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public WhatsAppLog tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRecipientPhone() {
        return this.recipientPhone;
    }

    public WhatsAppLog recipientPhone(String recipientPhone) {
        this.setRecipientPhone(recipientPhone);
        return this;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getMessageBody() {
        return this.messageBody;
    }

    public WhatsAppLog messageBody(String messageBody) {
        this.setMessageBody(messageBody);
        return this;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public ZonedDateTime getSentAt() {
        return this.sentAt;
    }

    public WhatsAppLog sentAt(ZonedDateTime sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return this.status;
    }

    public WhatsAppLog status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public WhatsAppLog type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public WhatsAppLog campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public WhatsAppLog metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public CommunicationCampaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(CommunicationCampaign communicationCampaign) {
        this.campaign = communicationCampaign;
    }

    public WhatsAppLog campaign(CommunicationCampaign communicationCampaign) {
        this.setCampaign(communicationCampaign);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WhatsAppLog)) {
            return false;
        }
        return getId() != null && getId().equals(((WhatsAppLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhatsAppLog{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", recipientPhone='" + getRecipientPhone() + "'" +
            ", messageBody='" + getMessageBody() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", campaignId=" + getCampaignId() +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
