package com.nextjstemplate.service.exception;

/**
 * Exception thrown when WhatsApp integration is not enabled for a tenant.
 */
public class WhatsAppNotEnabledException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public WhatsAppNotEnabledException(String message) {
    super(message);
  }

  public WhatsAppNotEnabledException(String message, Throwable cause) {
    super(message, cause);
  }
}
