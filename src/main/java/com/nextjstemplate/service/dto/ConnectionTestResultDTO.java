package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * DTO for Twilio connection test results.
 */
public class ConnectionTestResultDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private boolean success;
  private String message;
  private String errorCode;
  private String errorMessage;
  private ZonedDateTime testedAt;
  private String tenantId;
  private String accountSid;
  private String whatsappFrom;

  public ConnectionTestResultDTO() {
  }

  public ConnectionTestResultDTO(boolean success, String message) {
    this.success = success;
    this.message = message;
    this.testedAt = ZonedDateTime.now();
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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

  public ZonedDateTime getTestedAt() {
    return testedAt;
  }

  public void setTestedAt(ZonedDateTime testedAt) {
    this.testedAt = testedAt;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getAccountSid() {
    return accountSid;
  }

  public void setAccountSid(String accountSid) {
    this.accountSid = accountSid;
  }

  public String getWhatsappFrom() {
    return whatsappFrom;
  }

  public void setWhatsappFrom(String whatsappFrom) {
    this.whatsappFrom = whatsappFrom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ConnectionTestResultDTO that = (ConnectionTestResultDTO) o;
    return success == that.success &&
        Objects.equals(message, that.message) &&
        Objects.equals(errorCode, that.errorCode) &&
        Objects.equals(errorMessage, that.errorMessage) &&
        Objects.equals(testedAt, that.testedAt) &&
        Objects.equals(tenantId, that.tenantId) &&
        Objects.equals(accountSid, that.accountSid) &&
        Objects.equals(whatsappFrom, that.whatsappFrom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message, errorCode, errorMessage, testedAt, tenantId, accountSid, whatsappFrom);
  }

  @Override
  public String toString() {
    return "ConnectionTestResultDTO{" +
        "success=" + success +
        ", message='" + message + '\'' +
        ", errorCode='" + errorCode + '\'' +
        ", errorMessage='" + errorMessage + '\'' +
        ", testedAt=" + testedAt +
        ", tenantId='" + tenantId + '\'' +
        ", accountSid='" + accountSid + '\'' +
        ", whatsappFrom='" + whatsappFrom + '\'' +
        '}';
  }
}
