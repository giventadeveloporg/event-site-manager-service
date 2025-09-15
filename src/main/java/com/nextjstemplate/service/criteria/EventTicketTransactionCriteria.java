package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventTicketTransaction} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventTicketTransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-ticket-transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter transactionReference;

    private StringFilter email;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phone;

    private IntegerFilter quantity;

    private BigDecimalFilter pricePerUnit;

    private BigDecimalFilter totalAmount;

    private BigDecimalFilter taxAmount;

    private BigDecimalFilter platformFeeAmount;

    private BigDecimalFilter serviceFee;
    private LongFilter discountCodeId;

    private BigDecimalFilter discountAmount;

    private BigDecimalFilter finalAmount;

    private StringFilter status;

    private StringFilter paymentMethod;

    private StringFilter paymentReference;

    private ZonedDateTimeFilter purchaseDate;

    private ZonedDateTimeFilter confirmationSentAt;

    private BigDecimalFilter refundAmount;

    private ZonedDateTimeFilter refundDate;

    private StringFilter refundReason;
    private StringFilter stripeCheckoutSessionId;

    private StringFilter stripePaymentIntentId;

    private StringFilter stripeCustomerId;

    private StringFilter stripePaymentStatus;

    private StringFilter stripeCustomerEmail;

    private StringFilter stripePaymentCurrency;

    private BigDecimalFilter stripeAmountDiscount;

    private BigDecimalFilter stripeAmountTax;

    private BigDecimalFilter stripeFeeAmount;

    private StringFilter qrCodeImageUrl;
    private LongFilter eventId;

    private LongFilter userId;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private StringFilter checkInStatus;

    private IntegerFilter numberOfGuestsCheckedIn;

    private ZonedDateTimeFilter checkInTime;

    private ZonedDateTimeFilter checkOutTime;
    private Boolean distinct;

    public EventTicketTransactionCriteria() {}

    public EventTicketTransactionCriteria(EventTicketTransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.transactionReference = other.transactionReference == null ? null : other.transactionReference.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.pricePerUnit = other.pricePerUnit == null ? null : other.pricePerUnit.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.taxAmount = other.taxAmount == null ? null : other.taxAmount.copy();
        this.platformFeeAmount = other.platformFeeAmount == null ? null : other.platformFeeAmount.copy();
        this.serviceFee = other.serviceFee == null ? null : other.serviceFee.copy();
        this.discountCodeId = other.discountCodeId == null ? null : other.discountCodeId.copy();
        this.discountAmount = other.discountAmount == null ? null : other.discountAmount.copy();
        this.finalAmount = other.finalAmount == null ? null : other.finalAmount.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.paymentMethod = other.paymentMethod == null ? null : other.paymentMethod.copy();
        this.paymentReference = other.paymentReference == null ? null : other.paymentReference.copy();
        this.purchaseDate = other.purchaseDate == null ? null : other.purchaseDate.copy();
        this.confirmationSentAt = other.confirmationSentAt == null ? null : other.confirmationSentAt.copy();
        this.refundAmount = other.refundAmount == null ? null : other.refundAmount.copy();
        this.refundDate = other.refundDate == null ? null : other.refundDate.copy();
        this.stripeCheckoutSessionId = other.stripeCheckoutSessionId == null ? null : other.stripeCheckoutSessionId.copy();
        this.stripePaymentIntentId = other.stripePaymentIntentId == null ? null : other.stripePaymentIntentId.copy();
        this.stripeCustomerId = other.stripeCustomerId == null ? null : other.stripeCustomerId.copy();
        this.stripePaymentStatus = other.stripePaymentStatus == null ? null : other.stripePaymentStatus.copy();
        this.stripeCustomerEmail = other.stripeCustomerEmail == null ? null : other.stripeCustomerEmail.copy();
        this.stripePaymentCurrency = other.stripePaymentCurrency == null ? null : other.stripePaymentCurrency.copy();
        this.stripeAmountDiscount = other.stripeAmountDiscount == null ? null : other.stripeAmountDiscount.copy();
        this.stripeAmountTax = other.stripeAmountTax == null ? null : other.stripeAmountTax.copy();
        this.stripeFeeAmount = other.stripeFeeAmount == null ? null : other.stripeFeeAmount.copy();
        this.qrCodeImageUrl = other.qrCodeImageUrl == null ? null : other.qrCodeImageUrl.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.checkInStatus = other.checkInStatus == null ? null : other.checkInStatus.copy();
        this.numberOfGuestsCheckedIn = other.numberOfGuestsCheckedIn == null ? null : other.numberOfGuestsCheckedIn.copy();
        this.checkInTime = other.checkInTime == null ? null : other.checkInTime.copy();
        this.checkOutTime = other.checkOutTime == null ? null : other.checkOutTime.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventTicketTransactionCriteria copy() {
        return new EventTicketTransactionCriteria(this);
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

    public StringFilter getTransactionReference() {
        return transactionReference;
    }

    public StringFilter transactionReference() {
        if (transactionReference == null) {
            transactionReference = new StringFilter();
        }
        return transactionReference;
    }

    public void setTransactionReference(StringFilter transactionReference) {
        this.transactionReference = transactionReference;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getPricePerUnit() {
        return pricePerUnit;
    }

    public BigDecimalFilter pricePerUnit() {
        if (pricePerUnit == null) {
            pricePerUnit = new BigDecimalFilter();
        }
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimalFilter pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimalFilter getTotalAmount() {
        return totalAmount;
    }

    public BigDecimalFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new BigDecimalFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(BigDecimalFilter totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimalFilter getTaxAmount() {
        return taxAmount;
    }

    public BigDecimalFilter taxAmount() {
        if (taxAmount == null) {
            taxAmount = new BigDecimalFilter();
        }
        return taxAmount;
    }

    public void setTaxAmount(BigDecimalFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimalFilter getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public BigDecimalFilter platformFeeAmount() {
        if (platformFeeAmount == null) {
            platformFeeAmount = new BigDecimalFilter();
        }
        return platformFeeAmount;
    }

    public void setPlatformFeeAmount(BigDecimalFilter platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
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

    public BigDecimalFilter getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimalFilter discountAmount() {
        if (discountAmount == null) {
            discountAmount = new BigDecimalFilter();
        }
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimalFilter discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimalFilter getFinalAmount() {
        return finalAmount;
    }

    public BigDecimalFilter finalAmount() {
        if (finalAmount == null) {
            finalAmount = new BigDecimalFilter();
        }
        return finalAmount;
    }

    public void setFinalAmount(BigDecimalFilter finalAmount) {
        this.finalAmount = finalAmount;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            paymentMethod = new StringFilter();
        }
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StringFilter getPaymentReference() {
        return paymentReference;
    }

    public StringFilter paymentReference() {
        if (paymentReference == null) {
            paymentReference = new StringFilter();
        }
        return paymentReference;
    }

    public void setPaymentReference(StringFilter paymentReference) {
        this.paymentReference = paymentReference;
    }

    public ZonedDateTimeFilter getPurchaseDate() {
        return purchaseDate;
    }

    public ZonedDateTimeFilter purchaseDate() {
        if (purchaseDate == null) {
            purchaseDate = new ZonedDateTimeFilter();
        }
        return purchaseDate;
    }

    public void setPurchaseDate(ZonedDateTimeFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public ZonedDateTimeFilter getConfirmationSentAt() {
        return confirmationSentAt;
    }

    public ZonedDateTimeFilter confirmationSentAt() {
        if (confirmationSentAt == null) {
            confirmationSentAt = new ZonedDateTimeFilter();
        }
        return confirmationSentAt;
    }

    public void setConfirmationSentAt(ZonedDateTimeFilter confirmationSentAt) {
        this.confirmationSentAt = confirmationSentAt;
    }

    public BigDecimalFilter getRefundAmount() {
        return refundAmount;
    }

    public BigDecimalFilter refundAmount() {
        if (refundAmount == null) {
            refundAmount = new BigDecimalFilter();
        }
        return refundAmount;
    }

    public void setRefundAmount(BigDecimalFilter refundAmount) {
        this.refundAmount = refundAmount;
    }

    public ZonedDateTimeFilter getRefundDate() {
        return refundDate;
    }

    public ZonedDateTimeFilter refundDate() {
        if (refundDate == null) {
            refundDate = new ZonedDateTimeFilter();
        }
        return refundDate;
    }

    public void setRefundDate(ZonedDateTimeFilter refundDate) {
        this.refundDate = refundDate;
    }

    public StringFilter getStripeCheckoutSessionId() {
        return stripeCheckoutSessionId;
    }

    public StringFilter stripeCheckoutSessionId() {
        if (stripeCheckoutSessionId == null) {
            stripeCheckoutSessionId = new StringFilter();
        }
        return stripeCheckoutSessionId;
    }

    public void setStripeCheckoutSessionId(StringFilter stripeCheckoutSessionId) {
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
    }

    public StringFilter getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public StringFilter stripePaymentIntentId() {
        if (stripePaymentIntentId == null) {
            stripePaymentIntentId = new StringFilter();
        }
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(StringFilter stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public StringFilter getStripeCustomerId() {
        return stripeCustomerId;
    }

    public StringFilter stripeCustomerId() {
        if (stripeCustomerId == null) {
            stripeCustomerId = new StringFilter();
        }
        return stripeCustomerId;
    }

    public void setStripeCustomerId(StringFilter stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public StringFilter getStripePaymentStatus() {
        return stripePaymentStatus;
    }

    public StringFilter stripePaymentStatus() {
        if (stripePaymentStatus == null) {
            stripePaymentStatus = new StringFilter();
        }
        return stripePaymentStatus;
    }

    public void setStripePaymentStatus(StringFilter stripePaymentStatus) {
        this.stripePaymentStatus = stripePaymentStatus;
    }

    public StringFilter getStripeCustomerEmail() {
        return stripeCustomerEmail;
    }

    public StringFilter stripeCustomerEmail() {
        if (stripeCustomerEmail == null) {
            stripeCustomerEmail = new StringFilter();
        }
        return stripeCustomerEmail;
    }

    public void setStripeCustomerEmail(StringFilter stripeCustomerEmail) {
        this.stripeCustomerEmail = stripeCustomerEmail;
    }

    public StringFilter getStripePaymentCurrency() {
        return stripePaymentCurrency;
    }

    public StringFilter stripePaymentCurrency() {
        if (stripePaymentCurrency == null) {
            stripePaymentCurrency = new StringFilter();
        }
        return stripePaymentCurrency;
    }

    public void setStripePaymentCurrency(StringFilter stripePaymentCurrency) {
        this.stripePaymentCurrency = stripePaymentCurrency;
    }

    public BigDecimalFilter getStripeAmountDiscount() {
        return stripeAmountDiscount;
    }

    public BigDecimalFilter stripeAmountDiscount() {
        if (stripeAmountDiscount == null) {
            stripeAmountDiscount = new BigDecimalFilter();
        }
        return stripeAmountDiscount;
    }

    public void setStripeAmountDiscount(BigDecimalFilter stripeAmountDiscount) {
        this.stripeAmountDiscount = stripeAmountDiscount;
    }

    public BigDecimalFilter getStripeAmountTax() {
        return stripeAmountTax;
    }

    public BigDecimalFilter stripeAmountTax() {
        if (stripeAmountTax == null) {
            stripeAmountTax = new BigDecimalFilter();
        }
        return stripeAmountTax;
    }

    public void setStripeAmountTax(BigDecimalFilter stripeAmountTax) {
        this.stripeAmountTax = stripeAmountTax;
    }

    public BigDecimalFilter getStripeFeeAmount() {
        return stripeFeeAmount;
    }

    public BigDecimalFilter stripeFeeAmount() {
        if (stripeFeeAmount == null) {
            stripeFeeAmount = new BigDecimalFilter();
        }
        return stripeFeeAmount;
    }

    public void setStripeFeeAmount(BigDecimalFilter stripeFeeAmount) {
        this.stripeFeeAmount = stripeFeeAmount;
    }

    public StringFilter getQrCodeImageUrl() {
        return qrCodeImageUrl;
    }

    public StringFilter qrCodeImageUrl() {
        if (qrCodeImageUrl == null) {
            qrCodeImageUrl = new StringFilter();
        }
        return qrCodeImageUrl;
    }

    public void setQrCodeImageUrl(StringFilter qrCodeImageUrl) {
        this.qrCodeImageUrl = qrCodeImageUrl;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public StringFilter getCheckInStatus() {
        return checkInStatus;
    }

    public StringFilter checkInStatus() {
        if (checkInStatus == null) {
            checkInStatus = new StringFilter();
        }
        return checkInStatus;
    }

    public void setCheckInStatus(StringFilter checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public IntegerFilter getNumberOfGuestsCheckedIn() {
        return numberOfGuestsCheckedIn;
    }

    public IntegerFilter numberOfGuestsCheckedIn() {
        if (numberOfGuestsCheckedIn == null) {
            numberOfGuestsCheckedIn = new IntegerFilter();
        }
        return numberOfGuestsCheckedIn;
    }

    public void setNumberOfGuestsCheckedIn(IntegerFilter numberOfGuestsCheckedIn) {
        this.numberOfGuestsCheckedIn = numberOfGuestsCheckedIn;
    }

    public ZonedDateTimeFilter getCheckInTime() {
        return checkInTime;
    }

    public ZonedDateTimeFilter checkInTime() {
        if (checkInTime == null) {
            checkInTime = new ZonedDateTimeFilter();
        }
        return checkInTime;
    }

    public void setCheckInTime(ZonedDateTimeFilter checkInTime) {
        this.checkInTime = checkInTime;
    }

    public ZonedDateTimeFilter getCheckOutTime() {
        return checkOutTime;
    }

    public ZonedDateTimeFilter checkOutTime() {
        if (checkOutTime == null) {
            checkOutTime = new ZonedDateTimeFilter();
        }
        return checkOutTime;
    }

    public void setCheckOutTime(ZonedDateTimeFilter checkOutTime) {
        this.checkOutTime = checkOutTime;
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
        final EventTicketTransactionCriteria that = (EventTicketTransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(transactionReference, that.transactionReference) &&
            Objects.equals(email, that.email) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(platformFeeAmount, that.platformFeeAmount) &&
            Objects.equals(serviceFee, that.serviceFee) &&
            Objects.equals(discountCodeId, that.discountCodeId) &&
            Objects.equals(discountAmount, that.discountAmount) &&
            Objects.equals(finalAmount, that.finalAmount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(paymentReference, that.paymentReference) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(confirmationSentAt, that.confirmationSentAt) &&
            Objects.equals(refundAmount, that.refundAmount) &&
            Objects.equals(refundDate, that.refundDate) &&
            Objects.equals(refundReason, that.refundReason) &&
            Objects.equals(stripeCheckoutSessionId, that.stripeCheckoutSessionId) &&
            Objects.equals(stripePaymentIntentId, that.stripePaymentIntentId) &&
            Objects.equals(stripeCustomerId, that.stripeCustomerId) &&
            Objects.equals(stripePaymentStatus, that.stripePaymentStatus) &&
            Objects.equals(stripeCustomerEmail, that.stripeCustomerEmail) &&
            Objects.equals(stripePaymentCurrency, that.stripePaymentCurrency) &&
            Objects.equals(stripeAmountDiscount, that.stripeAmountDiscount) &&
            Objects.equals(stripeAmountTax, that.stripeAmountTax) &&
            Objects.equals(stripeFeeAmount, that.stripeFeeAmount) &&
            Objects.equals(qrCodeImageUrl, that.qrCodeImageUrl) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(checkInStatus, that.checkInStatus) &&
            Objects.equals(numberOfGuestsCheckedIn, that.numberOfGuestsCheckedIn) &&
            Objects.equals(checkInTime, that.checkInTime) &&
            Objects.equals(checkOutTime, that.checkOutTime) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            transactionReference,
            email,
            firstName,
            lastName,
            phone,
            quantity,
            pricePerUnit,
            totalAmount,
            taxAmount,
            platformFeeAmount,
            serviceFee,
            discountCodeId,
            discountAmount,
            finalAmount,
            status,
            paymentMethod,
            paymentReference,
            purchaseDate,
            confirmationSentAt,
            refundAmount,
            refundDate,
            refundReason,
            stripeCheckoutSessionId,
            stripePaymentIntentId,
            stripeCustomerId,
            stripePaymentStatus,
            stripeCustomerEmail,
            stripePaymentCurrency,
            stripeAmountDiscount,
            stripeAmountTax,
            stripeFeeAmount,
            qrCodeImageUrl,
            eventId,
            userId,
            createdAt,
            updatedAt,
            checkInStatus,
            numberOfGuestsCheckedIn,
            checkInTime,
            checkOutTime,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (transactionReference != null ? "transactionReference=" + transactionReference + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (pricePerUnit != null ? "pricePerUnit=" + pricePerUnit + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (taxAmount != null ? "taxAmount=" + taxAmount + ", " : "") +
            (platformFeeAmount != null ? "platformFeeAmount=" + platformFeeAmount + ", " : "") +
            (serviceFee != null ? "serviceFee=" + serviceFee + ", " : "") +
            (discountCodeId != null ? "discountCodeId=" + discountCodeId + ", " : "") +
            (discountAmount != null ? "discountAmount=" + discountAmount + ", " : "") +
            (finalAmount != null ? "finalAmount=" + finalAmount + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
            (paymentReference != null ? "paymentReference=" + paymentReference + ", " : "") +
            (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
            (confirmationSentAt != null ? "confirmationSentAt=" + confirmationSentAt + ", " : "") +
            (refundAmount != null ? "refundAmount=" + refundAmount + ", " : "") +
            (refundDate != null ? "refundDate=" + refundDate + ", " : "") +
            (stripeCheckoutSessionId != null ? "stripeCheckoutSessionId=" + stripeCheckoutSessionId + ", " : "") +
            (stripePaymentIntentId != null ? "stripePaymentIntentId=" + stripePaymentIntentId + ", " : "") +
            (stripeCustomerId != null ? "stripeCustomerId=" + stripeCustomerId + ", " : "") +
            (stripePaymentStatus != null ? "stripePaymentStatus=" + stripePaymentStatus + ", " : "") +
            (stripeCustomerEmail != null ? "stripeCustomerEmail=" + stripeCustomerEmail + ", " : "") +
            (stripePaymentCurrency != null ? "stripePaymentCurrency=" + stripePaymentCurrency + ", " : "") +
            (stripeAmountDiscount != null ? "stripeAmountDiscount=" + stripeAmountDiscount + ", " : "") +
            (stripeAmountTax != null ? "stripeAmountTax=" + stripeAmountTax + ", " : "") +
            (stripeFeeAmount != null ? "stripeFeeAmount=" + stripeFeeAmount + ", " : "") +
			(qrCodeImageUrl != null ? "qrCodeImageUrl=" + qrCodeImageUrl + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (checkInStatus != null ? "checkInStatus=" + checkInStatus + ", " : "") +
            (numberOfGuestsCheckedIn != null ? "numberOfGuestsCheckedIn=" + numberOfGuestsCheckedIn + ", " : "") +
            (checkInTime != null ? "checkInTime=" + checkInTime + ", " : "") +
            (checkOutTime != null ? "checkOutTime=" + checkOutTime + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            
            "}";
    }
}
