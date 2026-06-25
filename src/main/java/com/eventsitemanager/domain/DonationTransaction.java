package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DonationTransaction.
 * Stores standalone donation transactions (non-ticketed donations) for GiveButter integration.
 */
@Entity
@Table(name = "donation_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonationTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "payment_transaction_id")
    private Long paymentTransactionId;

    @NotNull
    @Size(max = 255)
    @Column(name = "transaction_reference", length = 255, nullable = false, unique = true)
    private String transactionReference;

    @Size(max = 255)
    @Column(name = "givebutter_donation_id", length = 255)
    private String givebutterDonationId;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "prayer_intention", columnDefinition = "TEXT")
    private String prayerIntention;

    @NotNull
    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring = false;

    @NotNull
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;

    @NotNull
    @Size(max = 50)
    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "qr_code_url", columnDefinition = "TEXT")
    private String qrCodeUrl;

    @Column(name = "qr_code_image_url", columnDefinition = "TEXT")
    private String qrCodeImageUrl;

    @NotNull
    @Column(name = "email_sent", nullable = false)
    private Boolean emailSent = false;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DonationTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public DonationTransaction tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public DonationTransaction eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getPaymentTransactionId() {
        return this.paymentTransactionId;
    }

    public DonationTransaction paymentTransactionId(Long paymentTransactionId) {
        this.setPaymentTransactionId(paymentTransactionId);
        return this;
    }

    public void setPaymentTransactionId(Long paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getTransactionReference() {
        return this.transactionReference;
    }

    public DonationTransaction transactionReference(String transactionReference) {
        this.setTransactionReference(transactionReference);
        return this;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getGivebutterDonationId() {
        return this.givebutterDonationId;
    }

    public DonationTransaction givebutterDonationId(String givebutterDonationId) {
        this.setGivebutterDonationId(givebutterDonationId);
        return this;
    }

    public void setGivebutterDonationId(String givebutterDonationId) {
        this.givebutterDonationId = givebutterDonationId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public DonationTransaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return this.email;
    }

    public DonationTransaction email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public DonationTransaction firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public DonationTransaction lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public DonationTransaction phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrayerIntention() {
        return this.prayerIntention;
    }

    public DonationTransaction prayerIntention(String prayerIntention) {
        this.setPrayerIntention(prayerIntention);
        return this;
    }

    public void setPrayerIntention(String prayerIntention) {
        this.prayerIntention = prayerIntention;
    }

    public Boolean getIsRecurring() {
        return this.isRecurring;
    }

    public DonationTransaction isRecurring(Boolean isRecurring) {
        this.setIsRecurring(isRecurring);
        return this;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean getIsAnonymous() {
        return this.isAnonymous;
    }

    public DonationTransaction isAnonymous(Boolean isAnonymous) {
        this.setIsAnonymous(isAnonymous);
        return this;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getStatus() {
        return this.status;
    }

    public DonationTransaction status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCodeUrl() {
        return this.qrCodeUrl;
    }

    public DonationTransaction qrCodeUrl(String qrCodeUrl) {
        this.setQrCodeUrl(qrCodeUrl);
        return this;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getQrCodeImageUrl() {
        return this.qrCodeImageUrl;
    }

    public DonationTransaction qrCodeImageUrl(String qrCodeImageUrl) {
        this.setQrCodeImageUrl(qrCodeImageUrl);
        return this;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }

    public Boolean getEmailSent() {
        return this.emailSent;
    }

    public DonationTransaction emailSent(Boolean emailSent) {
        this.setEmailSent(emailSent);
        return this;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public DonationTransaction metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public DonationTransaction createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public DonationTransaction updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = ZonedDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonationTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((DonationTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationTransaction{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", eventId=" + getEventId() +
            ", paymentTransactionId=" + getPaymentTransactionId() +
            ", transactionReference='" + getTransactionReference() + "'" +
            ", givebutterDonationId='" + getGivebutterDonationId() + "'" +
            ", amount=" + getAmount() +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", prayerIntention='" + getPrayerIntention() + "'" +
            ", isRecurring=" + getIsRecurring() +
            ", isAnonymous=" + getIsAnonymous() +
            ", status='" + getStatus() + "'" +
            ", qrCodeUrl='" + getQrCodeUrl() + "'" +
            ", qrCodeImageUrl='" + getQrCodeImageUrl() + "'" +
            ", emailSent=" + getEmailSent() +
            ", metadata='" + getMetadata() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
