package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.DonationTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonationTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    private Long eventId;

    private Long paymentTransactionId;

    @NotNull
    @Size(max = 255)
    private String transactionReference;

    @Size(max = 255)
    private String givebutterDonationId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 50)
    private String phone;

    private String prayerIntention;

    @NotNull
    private Boolean isRecurring = false;

    @NotNull
    private Boolean isAnonymous = false;

    @NotNull
    @Size(max = 50)
    private String status;

    private String qrCodeUrl;

    private String qrCodeImageUrl;

    @NotNull
    private Boolean emailSent = false;

    private String metadata;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

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

    public Long getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(Long paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getGivebutterDonationId() {
        return givebutterDonationId;
    }

    public void setGivebutterDonationId(String givebutterDonationId) {
        this.givebutterDonationId = givebutterDonationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrayerIntention() {
        return prayerIntention;
    }

    public void setPrayerIntention(String prayerIntention) {
        this.prayerIntention = prayerIntention;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getQrCodeImageUrl() {
        return qrCodeImageUrl;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonationTransactionDTO)) {
            return false;
        }

        DonationTransactionDTO donationTransactionDTO = (DonationTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, donationTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonationTransactionDTO{" +
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
