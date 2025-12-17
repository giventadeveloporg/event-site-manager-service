package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.PromotionEmailTemplate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
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

    @Size(max = 50)
    private String promotionCode;

    private Long discountCodeId;

    private Boolean isActive;

    private Long createdById;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

    private DiscountCodeDTO discountCode;

    private UserProfileDTO createdBy;

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

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public UserProfileDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfileDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionEmailTemplateDTO)) {
            return false;
        }

        PromotionEmailTemplateDTO promotionEmailTemplateDTO = (PromotionEmailTemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, promotionEmailTemplateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailTemplateDTO{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", eventId=" +
            getEventId() +
            ", templateName='" +
            getTemplateName() +
            "'" +
            ", templateType='" +
            getTemplateType() +
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
            ", promotionCode='" +
            getPromotionCode() +
            "'" +
            ", discountCodeId=" +
            getDiscountCodeId() +
            ", isActive='" +
            getIsActive() +
            "'" +
            ", createdById=" +
            getCreatedById() +
            ", createdAt='" +
            getCreatedAt() +
            "'" +
            ", updatedAt='" +
            getUpdatedAt() +
            "'" +
            "}"
        );
    }
}
