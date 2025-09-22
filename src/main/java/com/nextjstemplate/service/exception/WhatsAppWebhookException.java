package com.nextjstemplate.service.exception;

/**
 * Exception thrown when there are issues with WhatsApp webhook processing.
 */
public class WhatsAppWebhookException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public WhatsAppWebhookException(String message) {
    super(message);
  }

  public WhatsAppWebhookException(String message, Throwable cause) {
    super(message, cause);
  }
}
