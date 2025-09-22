package com.nextjstemplate.service.exception;

/**
 * Exception thrown when WhatsApp configuration is invalid or missing.
 */
public class WhatsAppConfigurationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public WhatsAppConfigurationException(String message) {
    super(message);
  }

  public WhatsAppConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
