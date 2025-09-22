package com.nextjstemplate.service.exception;

/**
 * Exception thrown when there are issues with WhatsApp message templates.
 */
public class WhatsAppTemplateException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public WhatsAppTemplateException(String message) {
    super(message);
  }

  public WhatsAppTemplateException(String message, Throwable cause) {
    super(message, cause);
  }
}
