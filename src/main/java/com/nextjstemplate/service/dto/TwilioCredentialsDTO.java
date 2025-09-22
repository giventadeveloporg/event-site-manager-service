package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for Twilio credentials.
 */
public class TwilioCredentialsDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank(message = "Account SID is required")
  private String accountSid;

  @NotBlank(message = "Auth Token is required")
  private String authToken;

  @NotBlank(message = "WhatsApp From number is required")
  private String whatsappFrom;

  private String webhookUrl;

  private String webhookToken;

  public TwilioCredentialsDTO() {
  }

  public TwilioCredentialsDTO(String accountSid, String authToken, String whatsappFrom) {
    this.accountSid = accountSid;
    this.authToken = authToken;
    this.whatsappFrom = whatsappFrom;
  }

  public String getAccountSid() {
    return accountSid;
  }

  public void setAccountSid(String accountSid) {
    this.accountSid = accountSid;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public String getWhatsappFrom() {
    return whatsappFrom;
  }

  public void setWhatsappFrom(String whatsappFrom) {
    this.whatsappFrom = whatsappFrom;
  }

  public String getWebhookUrl() {
    return webhookUrl;
  }

  public void setWebhookUrl(String webhookUrl) {
    this.webhookUrl = webhookUrl;
  }

  public String getWebhookToken() {
    return webhookToken;
  }

  public void setWebhookToken(String webhookToken) {
    this.webhookToken = webhookToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TwilioCredentialsDTO that = (TwilioCredentialsDTO) o;
    return Objects.equals(accountSid, that.accountSid) &&
        Objects.equals(authToken, that.authToken) &&
        Objects.equals(whatsappFrom, that.whatsappFrom) &&
        Objects.equals(webhookUrl, that.webhookUrl) &&
        Objects.equals(webhookToken, that.webhookToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountSid, authToken, whatsappFrom, webhookUrl, webhookToken);
  }

  @Override
  public String toString() {
    return "TwilioCredentialsDTO{" +
        "accountSid='" + accountSid + '\'' +
        ", authToken='[PROTECTED]'" +
        ", whatsappFrom='" + whatsappFrom + '\'' +
        ", webhookUrl='" + webhookUrl + '\'' +
        ", webhookToken='[PROTECTED]'" +
        '}';
  }
}
