package com.nextjstemplate.service.criteria;

import com.nextjstemplate.domain.enumeration.EmailStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.PromotionEmailSentLog} entity.
 * This class is used in {@link com.nextjstemplate.web.rest.PromotionEmailSentLogResource} to receive
 * all the possible filtering options from the Http GET request parameters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailSentLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter templateId;

    private LongFilter eventId;

    private StringFilter recipientEmail;

    private StringFilter subject;

    private StringFilter promotionCode;

    private LongFilter discountCodeId;

    private ZonedDateTimeFilter sentAt;

    private BooleanFilter isTestEmail;

    private Filter<EmailStatus> emailStatus;

    private StringFilter errorMessage;

    private LongFilter sentById;

    private Boolean distinct;

    public PromotionEmailSentLogCriteria() {}

    public PromotionEmailSentLogCriteria(PromotionEmailSentLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.templateId = other.templateId == null ? null : other.templateId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.recipientEmail = other.recipientEmail == null ? null : other.recipientEmail.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.promotionCode = other.promotionCode == null ? null : other.promotionCode.copy();
        this.discountCodeId = other.discountCodeId == null ? null : other.discountCodeId.copy();
        this.sentAt = other.sentAt == null ? null : other.sentAt.copy();
        this.isTestEmail = other.isTestEmail == null ? null : other.isTestEmail.copy();
        this.emailStatus = other.emailStatus == null ? null : other.emailStatus.copy();
        this.errorMessage = other.errorMessage == null ? null : other.errorMessage.copy();
        this.sentById = other.sentById == null ? null : other.sentById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PromotionEmailSentLogCriteria copy() {
        return new PromotionEmailSentLogCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getTemplateId() {
        return templateId;
    }

    public LongFilter templateId() {
        if (templateId == null) {
            templateId = new LongFilter();
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public StringFilter getRecipientEmail() {
        return recipientEmail;
    }

    public StringFilter recipientEmail() {
        if (recipientEmail == null) {
            recipientEmail = new StringFilter();
        }
        return recipientEmail;
    }

    public void setRecipientEmail(StringFilter recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public StringFilter subject() {
        if (subject == null) {
            subject = new StringFilter();
        }
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public StringFilter getPromotionCode() {
        return promotionCode;
    }

    public StringFilter promotionCode() {
        if (promotionCode == null) {
            promotionCode = new StringFilter();
        }
        return promotionCode;
    }

    public void setPromotionCode(StringFilter promotionCode) {
        this.promotionCode = promotionCode;
    }

    public LongFilter getDiscountCodeId() {
        return discountCodeId;
    }

    public LongFilter discountCodeId() {
        if (discountCodeId == null) {
            discountCodeId = new LongFilter();
        }
        return discountCodeId;
    }

    public void setDiscountCodeId(LongFilter discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public ZonedDateTimeFilter getSentAt() {
        return sentAt;
    }

    public ZonedDateTimeFilter sentAt() {
        if (sentAt == null) {
            sentAt = new ZonedDateTimeFilter();
        }
        return sentAt;
    }

    public void setSentAt(ZonedDateTimeFilter sentAt) {
        this.sentAt = sentAt;
    }

    public BooleanFilter getIsTestEmail() {
        return isTestEmail;
    }

    public BooleanFilter isTestEmail() {
        if (isTestEmail == null) {
            isTestEmail = new BooleanFilter();
        }
        return isTestEmail;
    }

    public void setIsTestEmail(BooleanFilter isTestEmail) {
        this.isTestEmail = isTestEmail;
    }

    public Filter<EmailStatus> getEmailStatus() {
        return emailStatus;
    }

    public Filter<EmailStatus> emailStatus() {
        if (emailStatus == null) {
            emailStatus = new Filter<>();
        }
        return emailStatus;
    }

    public void setEmailStatus(Filter<EmailStatus> emailStatus) {
        this.emailStatus = emailStatus;
    }

    public StringFilter getErrorMessage() {
        return errorMessage;
    }

    public StringFilter errorMessage() {
        if (errorMessage == null) {
            errorMessage = new StringFilter();
        }
        return errorMessage;
    }

    public void setErrorMessage(StringFilter errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LongFilter getSentById() {
        return sentById;
    }

    public LongFilter sentById() {
        if (sentById == null) {
            sentById = new LongFilter();
        }
        return sentById;
    }

    public void setSentById(LongFilter sentById) {
        this.sentById = sentById;
    }

    public Boolean getDistinct() {
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
        final PromotionEmailSentLogCriteria that = (PromotionEmailSentLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(recipientEmail, that.recipientEmail) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(promotionCode, that.promotionCode) &&
            Objects.equals(discountCodeId, that.discountCodeId) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(isTestEmail, that.isTestEmail) &&
            Objects.equals(emailStatus, that.emailStatus) &&
            Objects.equals(errorMessage, that.errorMessage) &&
            Objects.equals(sentById, that.sentById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            templateId,
            eventId,
            recipientEmail,
            subject,
            promotionCode,
            discountCodeId,
            sentAt,
            isTestEmail,
            emailStatus,
            errorMessage,
            sentById,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailSentLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (templateId != null ? "templateId=" + templateId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (recipientEmail != null ? "recipientEmail=" + recipientEmail + ", " : "") +
            (subject != null ? "subject=" + subject + ", " : "") +
            (promotionCode != null ? "promotionCode=" + promotionCode + ", " : "") +
            (discountCodeId != null ? "discountCodeId=" + discountCodeId + ", " : "") +
            (sentAt != null ? "sentAt=" + sentAt + ", " : "") +
            (isTestEmail != null ? "isTestEmail=" + isTestEmail + ", " : "") +
            (emailStatus != null ? "emailStatus=" + emailStatus + ", " : "") +
            (errorMessage != null ? "errorMessage=" + errorMessage + ", " : "") +
            (sentById != null ? "sentById=" + sentById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}"
        );
    }
}
