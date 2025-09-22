package com.nextjstemplate.service.dto;

/**
 * Enum for WhatsApp message types.
 */
public enum MessageType {
  TEXT("text"),
  TEMPLATE("template"),
  MEDIA("media"),
  LOCATION("location"),
  CONTACT("contact");

  private final String value;

  MessageType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
