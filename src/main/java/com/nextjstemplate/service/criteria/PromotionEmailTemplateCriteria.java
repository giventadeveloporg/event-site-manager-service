package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.PromotionEmailTemplate} entity.
 * This class is used in {@link com.nextjstemplate.web.rest.PromotionEmailTemplateResource} to receive
 * all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /promotion-email-templates?id.greaterThan=5&eventId.equals=2&isActive.equals=true}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailTemplateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private LongFilter eventId;

    private StringFilter templateName;

    private StringFilter templateType;

    private StringFilter subject;

    private StringFilter promotionCode;

    private LongFilter discountCodeId;

    private BooleanFilter isActive;

    private LongFilter createdById;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private Boolean distinct;

    public PromotionEmailTemplateCriteria() {}

    public PromotionEmailTemplateCriteria(PromotionEmailTemplateCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.templateName = other.templateName == null ? null : other.templateName.copy();
        this.templateType = other.templateType == null ? null : other.templateType.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.promotionCode = other.promotionCode == null ? null : other.promotionCode.copy();
        this.discountCodeId = other.discountCodeId == null ? null : other.discountCodeId.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PromotionEmailTemplateCriteria copy() {
        return new PromotionEmailTemplateCriteria(this);
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

    public StringFilter getTemplateName() {
        return templateName;
    }

    public StringFilter templateName() {
        if (templateName == null) {
            templateName = new StringFilter();
        }
        return templateName;
    }

    public void setTemplateName(StringFilter templateName) {
        this.templateName = templateName;
    }

    public StringFilter getTemplateType() {
        return templateType;
    }

    public StringFilter templateType() {
        if (templateType == null) {
            templateType = new StringFilter();
        }
        return templateType;
    }

    public void setTemplateType(StringFilter templateType) {
        this.templateType = templateType;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
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
        final PromotionEmailTemplateCriteria that = (PromotionEmailTemplateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(templateName, that.templateName) &&
            Objects.equals(templateType, that.templateType) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(promotionCode, that.promotionCode) &&
            Objects.equals(discountCodeId, that.discountCodeId) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            eventId,
            templateName,
            templateType,
            subject,
            promotionCode,
            discountCodeId,
            isActive,
            createdById,
            createdAt,
            updatedAt,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailTemplateCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (templateName != null ? "templateName=" + templateName + ", " : "") +
            (templateType != null ? "templateType=" + templateType + ", " : "") +
            (subject != null ? "subject=" + subject + ", " : "") +
            (promotionCode != null ? "promotionCode=" + promotionCode + ", " : "") +
            (discountCodeId != null ? "discountCodeId=" + discountCodeId + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}"
        );
    }
}
