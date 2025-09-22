package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * DTO for Twilio WhatsApp API responses.
 */
public class TwilioWhatsAppResponseDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String messageId;
  private String status;
  private String errorCode;
  private String errorMessage;
  private ZonedDateTime sentAt;
  private Map<String, Object> metadata;

  public TwilioWhatsAppResponseDTO() {
  }

  public TwilioWhatsAppResponseDTO(String messageId, String status) {
    this.messageId = messageId;
    this.status = status;
    this.sentAt = ZonedDateTime.now();
  }

  public TwilioWhatsAppResponseDTO(String messageId, String status, String errorCode, String errorMessage) {
    this.messageId = messageId;
    this.status = status;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.sentAt = ZonedDateTime.now();
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

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  public boolean isSuccess() {
    return "SENT".equals(status) || "DELIVERED".equals(status);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TwilioWhatsAppResponseDTO that = (TwilioWhatsAppResponseDTO) o;
    return Objects.equals(messageId, that.messageId) &&
        Objects.equals(status, that.status) &&
        Objects.equals(errorCode, that.errorCode) &&
        Objects.equals(errorMessage, that.errorMessage) &&
        Objects.equals(sentAt, that.sentAt) &&
        Objects.equals(metadata, that.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, status, errorCode, errorMessage, sentAt, metadata);
  }

  @Override
  public String toString() {
    return "TwilioWhatsAppResponseDTO{" +
        "messageId='" + messageId + '\'' +
        ", status='" + status + '\'' +
        ", errorCode='" + errorCode + '\'' +
        ", errorMessage='" + errorMessage + '\'' +
        ", sentAt=" + sentAt +
        ", metadata=" + metadata +
        '}';
  }
}

