package com.nextjstemplate.service.dto;

import com.nextjstemplate.domain.enumeration.EmailStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.PromotionEmailSentLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailSentLogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    private Long templateId;

    private Long eventId;

    @NotNull
    @Size(max = 255)
    private String recipientEmail;

    @NotNull
    @Size(max = 500)
    private String subject;

    @Size(max = 50)
    private String promotionCode;

    private Long discountCodeId;

    @NotNull
    private ZonedDateTime sentAt;

    private Boolean isTestEmail;

    @NotNull
    private EmailStatus emailStatus;

    private String errorMessage;

    private Long sentById;

    private PromotionEmailTemplateDTO template;

    private EventDetailsDTO event;

    private DiscountCodeDTO discountCode;

    private UserProfileDTO sentBy;

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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Long getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getIsTestEmail() {
        return isTestEmail;
    }

    public void setIsTestEmail(Boolean isTestEmail) {
        this.isTestEmail = isTestEmail;
    }

    public EmailStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(EmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getSentById() {
        return sentById;
    }

    public void setSentById(Long sentById) {
        this.sentById = sentById;
    }

    public PromotionEmailTemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(PromotionEmailTemplateDTO template) {
        this.template = template;
    }

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    public DiscountCodeDTO getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCodeDTO discountCode) {
        this.discountCode = discountCode;
    }

    public UserProfileDTO getSentBy() {
        return sentBy;
    }

    public void setSentBy(UserProfileDTO sentBy) {
        this.sentBy = sentBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionEmailSentLogDTO)) {
            return false;
        }

        PromotionEmailSentLogDTO promotionEmailSentLogDTO = (PromotionEmailSentLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promotionEmailSentLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailSentLogDTO{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", templateId=" +
            getTemplateId() +
            ", eventId=" +
            getEventId() +
            ", recipientEmail='" +
            getRecipientEmail() +
            "'" +
            ", subject='" +
            getSubject() +
            "'" +
            ", promotionCode='" +
            getPromotionCode() +
            "'" +
            ", discountCodeId=" +
            getDiscountCodeId() +
            ", sentAt='" +
            getSentAt() +
            "'" +
            ", isTestEmail='" +
            getIsTestEmail() +
            "'" +
            ", emailStatus='" +
            getEmailStatus() +
            "'" +
            ", errorMessage='" +
            getErrorMessage() +
            "'" +
            ", sentById=" +
            getSentById() +
            "}"
        );
    }
}
