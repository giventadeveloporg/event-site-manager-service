package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * DTO for Twilio WhatsApp bulk message requests.
 */
public class TwilioWhatsAppBulkRequestDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank(message = "Tenant ID is required")
  private String tenantId;

  @NotEmpty(message = "Recipients list cannot be empty")
  private List<String> recipients;

  @NotNull(message = "Message body is required")
  private String messageBody;

  private String templateId;

  private Map<String, String> templateParameters;

  private String mediaUrl;

  private String campaignId;

  public TwilioWhatsAppBulkRequestDTO() {
  }

  public TwilioWhatsAppBulkRequestDTO(List<String> recipients, String messageBody) {
    this.recipients = recipients;
    this.messageBody = messageBody;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public Map<String, String> getTemplateParameters() {
    return templateParameters;
  }

  public void setTemplateParameters(Map<String, String> templateParameters) {
    this.templateParameters = templateParameters;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TwilioWhatsAppBulkRequestDTO that = (TwilioWhatsAppBulkRequestDTO) o;
    return Objects.equals(tenantId, that.tenantId) &&
        Objects.equals(recipients, that.recipients) &&
        Objects.equals(messageBody, that.messageBody) &&
        Objects.equals(templateId, that.templateId) &&
        Objects.equals(templateParameters, that.templateParameters) &&
        Objects.equals(mediaUrl, that.mediaUrl) &&
        Objects.equals(campaignId, that.campaignId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, recipients, messageBody, templateId, templateParameters, mediaUrl, campaignId);
  }

  @Override
  public String toString() {
    return "TwilioWhatsAppBulkRequestDTO{" +
        "tenantId='" + tenantId + '\'' +
        ", recipients=" + recipients +
        ", messageBody='" + messageBody + '\'' +
        ", templateId='" + templateId + '\'' +
        ", templateParameters=" + templateParameters +
        ", mediaUrl='" + mediaUrl + '\'' +
        ", campaignId='" + campaignId + '\'' +
        '}';
  }
}
