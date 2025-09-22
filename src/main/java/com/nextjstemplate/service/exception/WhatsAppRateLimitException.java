package com.nextjstemplate.service.exception;

/**
 * Exception thrown when WhatsApp rate limits are exceeded.
 */
public class WhatsAppRateLimitException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int retryAfterSeconds;

  public WhatsAppRateLimitException(String message) {
    super(message);
    this.retryAfterSeconds = 60; // Default retry after 60 seconds
  }

  public WhatsAppRateLimitException(String message, int retryAfterSeconds) {
    super(message);
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public WhatsAppRateLimitException(String message, Throwable cause) {
    super(message, cause);
    this.retryAfterSeconds = 60;
  }

  public WhatsAppRateLimitException(String message, int retryAfterSeconds, Throwable cause) {
    super(message, cause);
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public int getRetryAfterSeconds() {
    return retryAfterSeconds;
  }
}
