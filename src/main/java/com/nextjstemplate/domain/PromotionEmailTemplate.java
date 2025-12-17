package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PromotionEmailTemplate.
 * Stores reusable promotion email templates associated with events and discount codes.
 */
@Entity
@Table(
    name = "promotion_email_template",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_template_name_per_event", columnNames = { "tenant_id", "event_id", "template_name" }),
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailTemplate implements Serializable {

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
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Size(max = 255)
    @Column(name = "template_name", length = 255, nullable = false)
    private String templateName;

    @NotNull
    @Size(max = 160)
    @Column(name = "template_type", length = 160, nullable = false)
    private String templateType;

    @NotNull
    @Size(max = 500)
    @Column(name = "subject", length = 500, nullable = false)
    private String subject;

    @NotNull
    @Size(max = 255)
    @Column(name = "from_email", length = 255, nullable = false)
    private String fromEmail;

    @NotNull
    @Column(name = "body_html", nullable = false, columnDefinition = "TEXT")
    private String bodyHtml;

    @Column(name = "footer_html", nullable = true, columnDefinition = "TEXT")
    private String footerHtml;

    @Size(max = 2048)
    @Column(name = "header_image_url", length = 2048)
    private String headerImageUrl;

    @Size(max = 2048)
    @Column(name = "footer_image_url", length = 2048)
    private String footerImageUrl;

    @Size(max = 50)
    @Column(name = "promotion_code", length = 50)
    private String promotionCode;

    @Column(name = "discount_code_id")
    private Long discountCodeId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by_id")
    private Long createdById;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(
        value = {
            "parentEvent",
            "childEvents",
            "createdBy",
            "eventType",
            "discountCodes",
            "eventFeaturedPerformers",
            "eventContacts",
            "eventEmails",
            "eventProgramDirectors",
            "eventSponsorsJoins",
        },
        allowSetters = true
    )
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_code_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "events" }, allowSetters = true)
    private DiscountCode discountCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "userSubscription", "reviewedByAdmin" }, allowSetters = true)
    private UserProfile createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PromotionEmailTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public PromotionEmailTemplate tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public PromotionEmailTemplate eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public PromotionEmailTemplate templateName(String templateName) {
        this.setTemplateName(templateName);
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return this.templateType;
    }

    public PromotionEmailTemplate templateType(String templateType) {
        this.setTemplateType(templateType);
        return this;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSubject() {
        return this.subject;
    }

    public PromotionEmailTemplate subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return this.fromEmail;
    }

    public PromotionEmailTemplate fromEmail(String fromEmail) {
        this.setFromEmail(fromEmail);
        return this;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getBodyHtml() {
        return this.bodyHtml;
    }

    public PromotionEmailTemplate bodyHtml(String bodyHtml) {
        this.setBodyHtml(bodyHtml);
        return this;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getFooterHtml() {
        return this.footerHtml;
    }

    public PromotionEmailTemplate footerHtml(String footerHtml) {
        this.setFooterHtml(footerHtml);
        return this;
    }

    public void setFooterHtml(String footerHtml) {
        this.footerHtml = footerHtml;
    }

    public String getHeaderImageUrl() {
        return this.headerImageUrl;
    }

    public PromotionEmailTemplate headerImageUrl(String headerImageUrl) {
        this.setHeaderImageUrl(headerImageUrl);
        return this;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public String getFooterImageUrl() {
        return this.footerImageUrl;
    }

    public PromotionEmailTemplate footerImageUrl(String footerImageUrl) {
        this.setFooterImageUrl(footerImageUrl);
        return this;
    }

    public void setFooterImageUrl(String footerImageUrl) {
        this.footerImageUrl = footerImageUrl;
    }

    public String getPromotionCode() {
        return this.promotionCode;
    }

    public PromotionEmailTemplate promotionCode(String promotionCode) {
        this.setPromotionCode(promotionCode);
        return this;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Long getDiscountCodeId() {
        return this.discountCodeId;
    }

    public PromotionEmailTemplate discountCodeId(Long discountCodeId) {
        this.setDiscountCodeId(discountCodeId);
        return this;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PromotionEmailTemplate isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCreatedById() {
        return this.createdById;
    }

    public PromotionEmailTemplate createdById(Long createdById) {
        this.setCreatedById(createdById);
        return this;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public PromotionEmailTemplate createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public PromotionEmailTemplate updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public PromotionEmailTemplate event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public DiscountCode getDiscountCode() {
        return this.discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public PromotionEmailTemplate discountCode(DiscountCode discountCode) {
        this.setDiscountCode(discountCode);
        return this;
    }

    public UserProfile getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserProfile userProfile) {
        this.createdBy = userProfile;
    }

    public PromotionEmailTemplate createdBy(UserProfile userProfile) {
        this.setCreatedBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionEmailTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((PromotionEmailTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailTemplate{" +
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
