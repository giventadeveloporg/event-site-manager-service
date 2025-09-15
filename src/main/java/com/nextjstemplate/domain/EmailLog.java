package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailLog.
 */
@Entity
@Table(name = "email_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailLog implements Serializable {

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
    @Size(max = 255)
    @Column(name = "recipient_email", length = 255, nullable = false)
    private String recipientEmail;

    @Size(max = 255)
    @Column(name = "subject", length = 255)
    private String subject;

    @Size(max = 32768)
    @Column(name = "body", length = 32768)
    private String body;

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

    public EmailLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EmailLog tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRecipientEmail() {
        return this.recipientEmail;
    }

    public EmailLog recipientEmail(String recipientEmail) {
        this.setRecipientEmail(recipientEmail);
        return this;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return this.subject;
    }

    public EmailLog subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public EmailLog body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getSentAt() {
        return this.sentAt;
    }

    public EmailLog sentAt(ZonedDateTime sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return this.status;
    }

    public EmailLog status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public EmailLog type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public EmailLog campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public EmailLog metadata(String metadata) {
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

    public EmailLog campaign(CommunicationCampaign communicationCampaign) {
        this.setCampaign(communicationCampaign);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailLog)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailLog{" +
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
            "}";
    }
}
