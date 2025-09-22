package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * DTO for WhatsApp log entries.
 */
public class WhatsAppLogEntryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String tenantId;
  private String recipientPhone;
  private String messageBody;
  private ZonedDateTime sentAt;
  private String status;
  private String type;
  private String campaignId;
  private String messageId;
  private Map<String, Object> metadata;
  private String errorMessage;

  public WhatsAppLogEntryDTO() {
  }

  public WhatsAppLogEntryDTO(String tenantId, String recipientPhone, String messageBody, String status, String type) {
    this.tenantId = tenantId;
    this.recipientPhone = recipientPhone;
    this.messageBody = messageBody;
    this.status = status;
    this.type = type;
    this.sentAt = ZonedDateTime.now();
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getRecipientPhone() {
    return recipientPhone;
  }

  public void setRecipientPhone(String recipientPhone) {
    this.recipientPhone = recipientPhone;
  }

  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }

  public ZonedDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(ZonedDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WhatsAppLogEntryDTO that = (WhatsAppLogEntryDTO) o;
    return Objects.equals(tenantId, that.tenantId) &&
        Objects.equals(recipientPhone, that.recipientPhone) &&
        Objects.equals(messageBody, that.messageBody) &&
        Objects.equals(sentAt, that.sentAt) &&
        Objects.equals(status, that.status) &&
        Objects.equals(type, that.type) &&
        Objects.equals(campaignId, that.campaignId) &&
        Objects.equals(messageId, that.messageId) &&
        Objects.equals(metadata, that.metadata) &&
        Objects.equals(errorMessage, that.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, recipientPhone, messageBody, sentAt, status, type, campaignId, messageId, metadata,
        errorMessage);
  }

  @Override
  public String toString() {
    return "WhatsAppLogEntryDTO{" +
        "tenantId='" + tenantId + '\'' +
        ", recipientPhone='" + recipientPhone + '\'' +
        ", messageBody='" + messageBody + '\'' +
        ", sentAt=" + sentAt +
        ", status='" + status + '\'' +
        ", type='" + type + '\'' +
        ", campaignId='" + campaignId + '\'' +
        ", messageId='" + messageId + '\'' +
        ", metadata=" + metadata +
        ", errorMessage='" + errorMessage + '\'' +
        '}';
  }
}

