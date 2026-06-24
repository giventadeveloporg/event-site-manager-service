package com.eventsitemanager.service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for creating/updating PromotionEmailTemplate.
 * Form DTO that excludes audit fields and nested DTOs.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailTemplateFormDTO implements Serializable {

    private Long eventId;

    @NotNull
    @Size(max = 255)
    private String templateName;

    @NotNull
    @Size(max = 160)
    private String templateType;

    @NotNull
    @Size(max = 500)
    private String subject;

    @NotNull
    @Size(max = 255)
    private String fromEmail;

    @NotNull
    private String bodyHtml;

    private String footerHtml;

    @Size(max = 2048)
    private String headerImageUrl;

    @Size(max = 2048)
    private String footerImageUrl;

    private Long discountCodeId;

    private Boolean isActive;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getFooterHtml() {
        return footerHtml;
    }

    public void setFooterHtml(String footerHtml) {
        this.footerHtml = footerHtml;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public String getFooterImageUrl() {
        return footerImageUrl;
    }

    public void setFooterImageUrl(String footerImageUrl) {
        this.footerImageUrl = footerImageUrl;
    }

    public Long getDiscountCodeId() {
        return discountCodeId;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionEmailTemplateFormDTO)) {
            return false;
        }

        PromotionEmailTemplateFormDTO promotionEmailTemplateFormDTO = (PromotionEmailTemplateFormDTO) o;
        return (
            Objects.equals(this.eventId, promotionEmailTemplateFormDTO.eventId) &&
            Objects.equals(this.templateName, promotionEmailTemplateFormDTO.templateName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.eventId, this.templateName);
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailTemplateFormDTO{" +
            "eventId=" +
            getEventId() +
            ", templateName='" +
            getTemplateName() +
            "'" +
            ", subject='" +
            getSubject() +
            "'" +
            ", fromEmail='" +
            getFromEmail() +
            "'" +
            ", bodyHtml='" +
            getBodyHtml() +
            "'" +
            ", footerHtml='" +
            getFooterHtml() +
            "'" +
            ", headerImageUrl='" +
            getHeaderImageUrl() +
            "'" +
            ", footerImageUrl='" +
            getFooterImageUrl() +
            "'" +
            ", discountCodeId=" +
            getDiscountCodeId() +
            ", isActive='" +
            getIsActive() +
            "'" +
            "}"
        );
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}
