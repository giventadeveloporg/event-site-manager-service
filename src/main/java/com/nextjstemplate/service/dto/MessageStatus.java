package com.nextjstemplate.service.dto;

/**
 * Enum for WhatsApp message statuses.
 */
public enum MessageStatus {
  PENDING("pending"),
  SENT("sent"),
  DELIVERED("delivered"),
  READ("read"),
  FAILED("failed"),
  UNDELIVERED("undelivered"),
  REJECTED("rejected");

  private final String value;

  MessageStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  public boolean isSuccess() {
    return this == SENT || this == DELIVERED || this == READ;
  }

  public boolean isFailure() {
    return this == FAILED || this == UNDELIVERED || this == REJECTED;
  }
}
