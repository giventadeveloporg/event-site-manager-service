package com.eventsitemanager.domain;

import com.eventsitemanager.domain.enumeration.EmailStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PromotionEmailSentLog.
 * Audit log of all sent promotion emails for compliance, analytics, and debugging.
 */
@Entity
@Table(name = "promotion_email_sent_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PromotionEmailSentLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @Column(name = "template_id", nullable = true)
    private Long templateId;

    @Column(name = "event_id", nullable = true)
    private Long eventId;

    @NotNull
    @Size(max = 255)
    @Column(name = "recipient_email", length = 255, nullable = false)
    private String recipientEmail;

    @NotNull
    @Size(max = 500)
    @Column(name = "subject", length = 500, nullable = false)
    private String subject;

    @Size(max = 50)
    @Column(name = "promotion_code", length = 50)
    private String promotionCode;

    @Column(name = "discount_code_id")
    private Long discountCodeId;

    @NotNull
    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt;

    @Column(name = "is_test_email")
    private Boolean isTestEmail;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "email_status", length = 50, nullable = false)
    private EmailStatus emailStatus;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "sent_by_id")
    private Long sentById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "event", "discountCode", "createdBy" }, allowSetters = true)
    private PromotionEmailTemplate template;

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
    @JoinColumn(name = "sent_by_id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "userSubscription", "reviewedByAdmin" }, allowSetters = true)
    private UserProfile sentBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PromotionEmailSentLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public PromotionEmailSentLog tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTemplateId() {
        return this.templateId;
    }

    public PromotionEmailSentLog templateId(Long templateId) {
        this.setTemplateId(templateId);
        return this;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public PromotionEmailSentLog eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getRecipientEmail() {
        return this.recipientEmail;
    }

    public PromotionEmailSentLog recipientEmail(String recipientEmail) {
        this.setRecipientEmail(recipientEmail);
        return this;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return this.subject;
    }

    public PromotionEmailSentLog subject(String subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPromotionCode() {
        return this.promotionCode;
    }

    public PromotionEmailSentLog promotionCode(String promotionCode) {
        this.setPromotionCode(promotionCode);
        return this;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public Long getDiscountCodeId() {
        return this.discountCodeId;
    }

    public PromotionEmailSentLog discountCodeId(Long discountCodeId) {
        this.setDiscountCodeId(discountCodeId);
        return this;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public ZonedDateTime getSentAt() {
        return this.sentAt;
    }

    public PromotionEmailSentLog sentAt(ZonedDateTime sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getIsTestEmail() {
        return this.isTestEmail;
    }

    public PromotionEmailSentLog isTestEmail(Boolean isTestEmail) {
        this.setIsTestEmail(isTestEmail);
        return this;
    }

    public void setIsTestEmail(Boolean isTestEmail) {
        this.isTestEmail = isTestEmail;
    }

    public EmailStatus getEmailStatus() {
        return this.emailStatus;
    }

    public PromotionEmailSentLog emailStatus(EmailStatus emailStatus) {
        this.setEmailStatus(emailStatus);
        return this;
    }

    public void setEmailStatus(EmailStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public PromotionEmailSentLog errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getSentById() {
        return this.sentById;
    }

    public PromotionEmailSentLog sentById(Long sentById) {
        this.setSentById(sentById);
        return this;
    }

    public void setSentById(Long sentById) {
        this.sentById = sentById;
    }

    public PromotionEmailTemplate getTemplate() {
        return this.template;
    }

    public void setTemplate(PromotionEmailTemplate promotionEmailTemplate) {
        this.template = promotionEmailTemplate;
    }

    public PromotionEmailSentLog template(PromotionEmailTemplate promotionEmailTemplate) {
        this.setTemplate(promotionEmailTemplate);
        return this;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public PromotionEmailSentLog event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public DiscountCode getDiscountCode() {
        return this.discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public PromotionEmailSentLog discountCode(DiscountCode discountCode) {
        this.setDiscountCode(discountCode);
        return this;
    }

    public UserProfile getSentBy() {
        return this.sentBy;
    }

    public void setSentBy(UserProfile userProfile) {
        this.sentBy = userProfile;
    }

    public PromotionEmailSentLog sentBy(UserProfile userProfile) {
        this.setSentBy(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionEmailSentLog)) {
            return false;
        }
        return getId() != null && getId().equals(((PromotionEmailSentLog) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "PromotionEmailSentLog{" +
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
