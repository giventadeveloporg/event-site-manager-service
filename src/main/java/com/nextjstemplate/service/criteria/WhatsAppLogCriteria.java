package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.WhatsAppLog} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.WhatsAppLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /whats-app-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WhatsAppLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter recipientPhone;

    private StringFilter messageBody;

    private ZonedDateTimeFilter sentAt;

    private StringFilter status;

    private StringFilter type;

    private LongFilter campaignId;

    private StringFilter metadata;

    private Boolean distinct;

    public WhatsAppLogCriteria() {}

    public WhatsAppLogCriteria(WhatsAppLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.recipientPhone = other.optionalRecipientPhone().map(StringFilter::copy).orElse(null);
        this.messageBody = other.optionalMessageBody().map(StringFilter::copy).orElse(null);
        this.sentAt = other.optionalSentAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.campaignId = other.optionalCampaignId().map(LongFilter::copy).orElse(null);
        this.metadata = other.optionalMetadata().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WhatsAppLogCriteria copy() {
        return new WhatsAppLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public Optional<StringFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getRecipientPhone() {
        return recipientPhone;
    }

    public Optional<StringFilter> optionalRecipientPhone() {
        return Optional.ofNullable(recipientPhone);
    }

    public StringFilter recipientPhone() {
        if (recipientPhone == null) {
            setRecipientPhone(new StringFilter());
        }
        return recipientPhone;
    }

    public void setRecipientPhone(StringFilter recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public StringFilter getMessageBody() {
        return messageBody;
    }

    public Optional<StringFilter> optionalMessageBody() {
        return Optional.ofNullable(messageBody);
    }

    public StringFilter messageBody() {
        if (messageBody == null) {
            setMessageBody(new StringFilter());
        }
        return messageBody;
    }

    public void setMessageBody(StringFilter messageBody) {
        this.messageBody = messageBody;
    }

    public ZonedDateTimeFilter getSentAt() {
        return sentAt;
    }

    public Optional<ZonedDateTimeFilter> optionalSentAt() {
        return Optional.ofNullable(sentAt);
    }

    public ZonedDateTimeFilter sentAt() {
        if (sentAt == null) {
            setSentAt(new ZonedDateTimeFilter());
        }
        return sentAt;
    }

    public void setSentAt(ZonedDateTimeFilter sentAt) {
        this.sentAt = sentAt;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getCampaignId() {
        return campaignId;
    }

    public Optional<LongFilter> optionalCampaignId() {
        return Optional.ofNullable(campaignId);
    }

    public LongFilter campaignId() {
        if (campaignId == null) {
            setCampaignId(new LongFilter());
        }
        return campaignId;
    }

    public void setCampaignId(LongFilter campaignId) {
        this.campaignId = campaignId;
    }

    public StringFilter getMetadata() {
        return metadata;
    }

    public Optional<StringFilter> optionalMetadata() {
        return Optional.ofNullable(metadata);
    }

    public StringFilter metadata() {
        if (metadata == null) {
            setMetadata(new StringFilter());
        }
        return metadata;
    }

    public void setMetadata(StringFilter metadata) {
        this.metadata = metadata;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WhatsAppLogCriteria that = (WhatsAppLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(recipientPhone, that.recipientPhone) &&
            Objects.equals(messageBody, that.messageBody) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(type, that.type) &&
            Objects.equals(campaignId, that.campaignId) &&
            Objects.equals(metadata, that.metadata) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, recipientPhone, messageBody, sentAt, status, type, campaignId, metadata, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WhatsAppLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalRecipientPhone().map(f -> "recipientPhone=" + f + ", ").orElse("") +
            optionalMessageBody().map(f -> "messageBody=" + f + ", ").orElse("") +
            optionalSentAt().map(f -> "sentAt=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalCampaignId().map(f -> "campaignId=" + f + ", ").orElse("") +
            optionalMetadata().map(f -> "metadata=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
