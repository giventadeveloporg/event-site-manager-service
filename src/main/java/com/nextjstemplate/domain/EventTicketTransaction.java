package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventTicketTransaction.
 */
@Entity
@Table(name = "event_ticket_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 255)
    @Column(name = "transaction_reference", insertable = false, updatable = false)
    private String transactionReference;

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

    @Size(max = 255)
    @Column(name = "phone", length = 255)
    private String phone;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price_per_unit", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerUnit;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "tax_amount", precision = 21, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "platform_fee_amount", precision = 21, scale = 2)
    private BigDecimal platformFeeAmount;

    @Column(name = "service_fee", precision = 21, scale = 2)
    private BigDecimal serviceFee;

    @Column(name = "discount_code_id")
    private Long discountCodeId;

    @Column(name = "discount_amount", precision = 21, scale = 2)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "final_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal finalAmount;

    @NotNull
    @Size(max = 255)
    @Column(name = "status", length = 255, nullable = false)
    private String status;

    @Size(max = 100)
    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Size(max = 255)
    @Column(name = "payment_reference", length = 255)
    private String paymentReference;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private ZonedDateTime purchaseDate;

    @Column(name = "confirmation_sent_at")
    private ZonedDateTime confirmationSentAt;

    @Column(name = "refund_amount", precision = 21, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "refund_date")
    private ZonedDateTime refundDate;

    @Size(max = 2048)
    @Column(name = "refund_reason", length = 2048)
    private String refundReason;

    @Size(max = 255)
    @Column(name = "stripe_checkout_session_id", length = 255)
    private String stripeCheckoutSessionId;

    @Size(max = 255)
    @Column(name = "stripe_payment_intent_id", length = 255)
    private String stripePaymentIntentId;

    @Size(max = 255)
    @Column(name = "stripe_customer_id", length = 255)
    private String stripeCustomerId;

    @Size(max = 50)
    @Column(name = "stripe_payment_status", length = 50)
    private String stripePaymentStatus;

    @Size(max = 255)
    @Column(name = "stripe_customer_email", length = 255)
    private String stripeCustomerEmail;

    @Size(max = 10)
    @Column(name = "stripe_payment_currency", length = 10)
    private String stripePaymentCurrency;

    @Column(name = "stripe_amount_discount", precision = 21, scale = 2)
    private BigDecimal stripeAmountDiscount;

    @Column(name = "stripe_amount_tax", precision = 21, scale = 2)
    private BigDecimal stripeAmountTax;

    @Column(name = "stripe_fee_amount", precision = 21, scale = 2)
    private BigDecimal stripeFeeAmount;

    @Size(max = 2048)
    @Column(name = "qr_code_image_url", length = 2048)
    private String qrCodeImageUrl;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Size(max = 50)
    @Column(name = "check_in_status", length = 50)
    private String checkInStatus;

    @Column(name = "number_of_guests_checked_in")
    private Integer numberOfGuestsCheckedIn;

    @Column(name = "check_in_time")
    private ZonedDateTime checkInTime;

    @Column(name = "check_out_time")
    private ZonedDateTime checkOutTime;

    /* @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile user;*/

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventTicketTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventTicketTransaction tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTransactionReference() {
        return this.transactionReference;
    }

    public EventTicketTransaction transactionReference(String transactionReference) {
        this.setTransactionReference(transactionReference);
        return this;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getEmail() {
        return this.email;
    }

    public EventTicketTransaction email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public EventTicketTransaction firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public EventTicketTransaction lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public EventTicketTransaction phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public EventTicketTransaction quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return this.pricePerUnit;
    }

    public EventTicketTransaction pricePerUnit(BigDecimal pricePerUnit) {
        this.setPricePerUnit(pricePerUnit);
        return this;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public EventTicketTransaction totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public EventTicketTransaction taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getPlatformFeeAmount() {
        return this.platformFeeAmount;
    }

    public EventTicketTransaction platformFeeAmount(BigDecimal platformFeeAmount) {
        this.setPlatformFeeAmount(platformFeeAmount);
        return this;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getServiceFee() {
        return this.serviceFee;
    }

    public EventTicketTransaction serviceFee(BigDecimal serviceFee) {
        this.setServiceFee(serviceFee);
        return this;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Long getDiscountCodeId() {
        return this.discountCodeId;
    }

    public EventTicketTransaction discountCodeId(Long discountCodeId) {
        this.setDiscountCodeId(discountCodeId);
        return this;
    }

    public void setDiscountCodeId(Long discountCodeId) {
        this.discountCodeId = discountCodeId;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public EventTicketTransaction discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return this.finalAmount;
    }

    public EventTicketTransaction finalAmount(BigDecimal finalAmount) {
        this.setFinalAmount(finalAmount);
        return this;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getStatus() {
        return this.status;
    }

    public EventTicketTransaction status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public EventTicketTransaction paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public EventTicketTransaction paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public ZonedDateTime getPurchaseDate() {
        return this.purchaseDate;
    }

    public EventTicketTransaction purchaseDate(ZonedDateTime purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(ZonedDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ZonedDateTime getConfirmationSentAt() {
        return this.confirmationSentAt;
    }

    public EventTicketTransaction confirmationSentAt(ZonedDateTime confirmationSentAt) {
        this.setConfirmationSentAt(confirmationSentAt);
        return this;
    }

    public void setConfirmationSentAt(ZonedDateTime confirmationSentAt) {
        this.confirmationSentAt = confirmationSentAt;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public EventTicketTransaction refundAmount(BigDecimal refundAmount) {
        this.setRefundAmount(refundAmount);
        return this;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public ZonedDateTime getRefundDate() {
        return this.refundDate;
    }

    public EventTicketTransaction refundDate(ZonedDateTime refundDate) {
        this.setRefundDate(refundDate);
        return this;
    }

    public void setRefundDate(ZonedDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundReason() {
        return this.refundReason;
    }

    public EventTicketTransaction refundReason(String refundReason) {
        this.setRefundReason(refundReason);
        return this;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getStripeCheckoutSessionId() {
        return this.stripeCheckoutSessionId;
    }

    public EventTicketTransaction stripeCheckoutSessionId(String stripeCheckoutSessionId) {
        this.setStripeCheckoutSessionId(stripeCheckoutSessionId);
        return this;
    }

    public void setStripeCheckoutSessionId(String stripeCheckoutSessionId) {
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
    }

    public String getStripePaymentIntentId() {
        return this.stripePaymentIntentId;
    }

    public EventTicketTransaction stripePaymentIntentId(String stripePaymentIntentId) {
        this.setStripePaymentIntentId(stripePaymentIntentId);
        return this;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripeCustomerId() {
        return this.stripeCustomerId;
    }

    public EventTicketTransaction stripeCustomerId(String stripeCustomerId) {
        this.setStripeCustomerId(stripeCustomerId);
        return this;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripePaymentStatus() {
        return this.stripePaymentStatus;
    }

    public EventTicketTransaction stripePaymentStatus(String stripePaymentStatus) {
        this.setStripePaymentStatus(stripePaymentStatus);
        return this;
    }

    public void setStripePaymentStatus(String stripePaymentStatus) {
        this.stripePaymentStatus = stripePaymentStatus;
    }

    public String getStripeCustomerEmail() {
        return this.stripeCustomerEmail;
    }

    public EventTicketTransaction stripeCustomerEmail(String stripeCustomerEmail) {
        this.setStripeCustomerEmail(stripeCustomerEmail);
        return this;
    }

    public void setStripeCustomerEmail(String stripeCustomerEmail) {
        this.stripeCustomerEmail = stripeCustomerEmail;
    }

    public String getStripePaymentCurrency() {
        return this.stripePaymentCurrency;
    }

    public EventTicketTransaction stripePaymentCurrency(String stripePaymentCurrency) {
        this.setStripePaymentCurrency(stripePaymentCurrency);
        return this;
    }

    public void setStripePaymentCurrency(String stripePaymentCurrency) {
        this.stripePaymentCurrency = stripePaymentCurrency;
    }

    public BigDecimal getStripeAmountDiscount() {
        return this.stripeAmountDiscount;
    }

    public EventTicketTransaction stripeAmountDiscount(BigDecimal stripeAmountDiscount) {
        this.setStripeAmountDiscount(stripeAmountDiscount);
        return this;
    }

    public void setStripeAmountDiscount(BigDecimal stripeAmountDiscount) {
        this.stripeAmountDiscount = stripeAmountDiscount;
    }

    public BigDecimal getStripeAmountTax() {
        return this.stripeAmountTax;
    }

    public EventTicketTransaction stripeAmountTax(BigDecimal stripeAmountTax) {
        this.setStripeAmountTax(stripeAmountTax);
        return this;
    }

    public void setStripeAmountTax(BigDecimal stripeAmountTax) {
        this.stripeAmountTax = stripeAmountTax;
    }

    public BigDecimal getStripeFeeAmount() {
        return this.stripeFeeAmount;
    }

    public EventTicketTransaction stripeFeeAmount(BigDecimal stripeFeeAmount) {
        this.setStripeFeeAmount(stripeFeeAmount);
        return this;
    }

    public void setStripeFeeAmount(BigDecimal stripeFeeAmount) {
        this.stripeFeeAmount = stripeFeeAmount;
    }

    public String getQrCodeImageUrl() {
        return this.qrCodeImageUrl;
    }

    public EventTicketTransaction qrCodeImageUrl(String qrCodeImageUrl) {
        this.setQrCodeImageUrl(qrCodeImageUrl);
        return this;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public EventTicketTransaction eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public EventTicketTransaction userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventTicketTransaction createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventTicketTransaction updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCheckInStatus() {
        return this.checkInStatus;
    }

    public EventTicketTransaction checkInStatus(String checkInStatus) {
        this.setCheckInStatus(checkInStatus);
        return this;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public Integer getNumberOfGuestsCheckedIn() {
        return this.numberOfGuestsCheckedIn;
    }

    public EventTicketTransaction numberOfGuestsCheckedIn(Integer numberOfGuestsCheckedIn) {
        this.setNumberOfGuestsCheckedIn(numberOfGuestsCheckedIn);
        return this;
    }

    public void setNumberOfGuestsCheckedIn(Integer numberOfGuestsCheckedIn) {
        this.numberOfGuestsCheckedIn = numberOfGuestsCheckedIn;
    }

    public ZonedDateTime getCheckInTime() {
        return this.checkInTime;
    }

    public EventTicketTransaction checkInTime(ZonedDateTime checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(ZonedDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public ZonedDateTime getCheckOutTime() {
        return this.checkOutTime;
    }

    public EventTicketTransaction checkOutTime(ZonedDateTime checkOutTime) {
        this.setCheckOutTime(checkOutTime);
        return this;
    }

    public void setCheckOutTime(ZonedDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    /*public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public EventTicketTransaction event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }*/

    /* public EventTicketTransaction user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }*/

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTicketTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((EventTicketTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransaction{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", transactionReference='" + getTransactionReference() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", platformFeeAmount=" + getPlatformFeeAmount() +
            ", serviceFee=" + getServiceFee() +
            ", discountCodeId=" + getDiscountCodeId() +
            ", discountAmount=" + getDiscountAmount() +
            ", finalAmount=" + getFinalAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", confirmationSentAt='" + getConfirmationSentAt() + "'" +
            ", refundAmount=" + getRefundAmount() +
            ", refundDate='" + getRefundDate() + "'" +
            ", refundReason='" + getRefundReason() + "'" +
            ", stripeCheckoutSessionId='" + getStripeCheckoutSessionId() + "'" +
            ", stripePaymentIntentId='" + getStripePaymentIntentId() + "'" +
   			", stripeCustomerId='" + getStripeCustomerId() + "'" +
            ", stripePaymentStatus='" + getStripePaymentStatus() + "'" +
            ", stripeCustomerEmail='" + getStripeCustomerEmail() + "'" +
            ", stripePaymentCurrency='" + getStripePaymentCurrency() + "'" +
            ", stripeAmountDiscount=" + getStripeAmountDiscount() +
            ", stripeAmountTax=" + getStripeAmountTax() +
            ", stripeFeeAmount=" + getStripeFeeAmount() +
            ", qrCodeImageUrl='" + getQrCodeImageUrl() + "'" +
            ", eventId=" + getEventId() +
            ", userId=" + getUserId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
 			", checkInStatus='" + getCheckInStatus() + "'" +
            ", numberOfGuestsCheckedIn=" + getNumberOfGuestsCheckedIn() +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            "}";
    }
}
