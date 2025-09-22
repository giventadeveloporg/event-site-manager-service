package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * DTO for message delivery status information.
 */
public class DeliveryStatusDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String messageId;
  private String status;
  private String errorCode;
  private String errorMessage;
  private ZonedDateTime sentAt;
  private ZonedDateTime deliveredAt;
  private ZonedDateTime readAt;
  private String recipientPhoneNumber;
  private String statusDescription;

  public DeliveryStatusDTO() {
  }

  public DeliveryStatusDTO(String messageId, String status) {
    this.messageId = messageId;
    this.status = status;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ZonedDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(ZonedDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public ZonedDateTime getDeliveredAt() {
    return deliveredAt;
  }

  public void setDeliveredAt(ZonedDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
  }

  public ZonedDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(ZonedDateTime readAt) {
    this.readAt = readAt;
  }

  public String getRecipientPhoneNumber() {
    return recipientPhoneNumber;
  }

  public void setRecipientPhoneNumber(String recipientPhoneNumber) {
    this.recipientPhoneNumber = recipientPhoneNumber;
  }

  public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public boolean isDelivered() {
    return "DELIVERED".equals(status) || "READ".equals(status);
  }

  public boolean isFailed() {
    return "FAILED".equals(status) || "UNDELIVERED".equals(status);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DeliveryStatusDTO that = (DeliveryStatusDTO) o;
    return Objects.equals(messageId, that.messageId) &&
        Objects.equals(status, that.status) &&
        Objects.equals(errorCode, that.errorCode) &&
        Objects.equals(errorMessage, that.errorMessage) &&
        Objects.equals(sentAt, that.sentAt) &&
        Objects.equals(deliveredAt, that.deliveredAt) &&
        Objects.equals(readAt, that.readAt) &&
        Objects.equals(recipientPhoneNumber, that.recipientPhoneNumber) &&
        Objects.equals(statusDescription, that.statusDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, status, errorCode, errorMessage, sentAt, deliveredAt, readAt, recipientPhoneNumber,
        statusDescription);
  }

  @Override
  public String toString() {
    return "DeliveryStatusDTO{" +
        "messageId='" + messageId + '\'' +
        ", status='" + status + '\'' +
        ", errorCode='" + errorCode + '\'' +
        ", errorMessage='" + errorMessage + '\'' +
        ", sentAt=" + sentAt +
        ", deliveredAt=" + deliveredAt +
        ", readAt=" + readAt +
        ", recipientPhoneNumber='" + recipientPhoneNumber + '\'' +
        ", statusDescription='" + statusDescription + '\'' +
        '}';
  }
}
