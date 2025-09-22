package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * DTO for Twilio webhook payloads.
 */
public class WebhookPayloadDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String messageId;
  private String status;
  private String errorCode;
  private String errorMessage;
  private String recipientPhoneNumber;
  private String senderPhoneNumber;
  private ZonedDateTime timestamp;
  private String eventType;
  private Map<String, Object> metadata;
  private String tenantId;

  public WebhookPayloadDTO() {
  }

  public WebhookPayloadDTO(String messageId, String status, String eventType) {
    this.messageId = messageId;
    this.status = status;
    this.eventType = eventType;
    this.timestamp = ZonedDateTime.now();
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

  public String getRecipientPhoneNumber() {
    return recipientPhoneNumber;
  }

  public void setRecipientPhoneNumber(String recipientPhoneNumber) {
    this.recipientPhoneNumber = recipientPhoneNumber;
  }

  public String getSenderPhoneNumber() {
    return senderPhoneNumber;
  }

  public void setSenderPhoneNumber(String senderPhoneNumber) {
    this.senderPhoneNumber = senderPhoneNumber;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(ZonedDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public boolean isDeliveryEvent() {
    return "DELIVERED".equals(eventType) || "READ".equals(eventType);
  }

  public boolean isFailureEvent() {
    return "FAILED".equals(eventType) || "UNDELIVERED".equals(eventType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WebhookPayloadDTO that = (WebhookPayloadDTO) o;
    return Objects.equals(messageId, that.messageId) &&
        Objects.equals(status, that.status) &&
        Objects.equals(errorCode, that.errorCode) &&
        Objects.equals(errorMessage, that.errorMessage) &&
        Objects.equals(recipientPhoneNumber, that.recipientPhoneNumber) &&
        Objects.equals(senderPhoneNumber, that.senderPhoneNumber) &&
        Objects.equals(timestamp, that.timestamp) &&
        Objects.equals(eventType, that.eventType) &&
        Objects.equals(metadata, that.metadata) &&
        Objects.equals(tenantId, that.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, status, errorCode, errorMessage, recipientPhoneNumber, senderPhoneNumber, timestamp,
        eventType, metadata, tenantId);
  }

  @Override
  public String toString() {
    return "WebhookPayloadDTO{" +
        "messageId='" + messageId + '\'' +
        ", status='" + status + '\'' +
        ", errorCode='" + errorCode + '\'' +
        ", errorMessage='" + errorMessage + '\'' +
        ", recipientPhoneNumber='" + recipientPhoneNumber + '\'' +
        ", senderPhoneNumber='" + senderPhoneNumber + '\'' +
        ", timestamp=" + timestamp +
        ", eventType='" + eventType + '\'' +
        ", metadata=" + metadata +
        ", tenantId='" + tenantId + '\'' +
        '}';
  }
}
