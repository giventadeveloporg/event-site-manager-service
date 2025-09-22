package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.exception.*;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for WhatsApp-related errors.
 */
@ControllerAdvice
public class WhatsAppExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(WhatsAppExceptionHandler.class);

  /**
   * Handle WhatsAppNotEnabledException.
   */
  @ExceptionHandler(WhatsAppNotEnabledException.class)
  public ResponseEntity<Map<String, Object>> handleWhatsAppNotEnabledException(
      WhatsAppNotEnabledException ex, WebRequest request) {

    LOG.warn("WhatsApp not enabled: {}", ex.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_NOT_ENABLED",
        "WhatsApp integration is not enabled for this tenant",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handle WhatsAppConfigurationException.
   */
  @ExceptionHandler(WhatsAppConfigurationException.class)
  public ResponseEntity<Map<String, Object>> handleWhatsAppConfigurationException(
      WhatsAppConfigurationException ex, WebRequest request) {

    LOG.warn("WhatsApp configuration error: {}", ex.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_CONFIGURATION_ERROR",
        "WhatsApp configuration is invalid or incomplete",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handle WhatsAppRateLimitException.
   */
  @ExceptionHandler(WhatsAppRateLimitException.class)
  public ResponseEntity<Map<String, Object>> handleWhatsAppRateLimitException(
      WhatsAppRateLimitException ex, WebRequest request) {

    LOG.warn("WhatsApp rate limit exceeded: {}", ex.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_RATE_LIMIT_EXCEEDED",
        "WhatsApp rate limit has been exceeded",
        ex.getMessage(),
        HttpStatus.TOO_MANY_REQUESTS.value());

    // Add retry information
    errorResponse.put("retryAfterSeconds", ex.getRetryAfterSeconds());
    errorResponse.put("retryAfter", ZonedDateTime.now().plusSeconds(ex.getRetryAfterSeconds()));

    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
  }

  /**
   * Handle WhatsAppTemplateException.
   */
  @ExceptionHandler(WhatsAppTemplateException.class)
  public ResponseEntity<Map<String, Object>> handleWhatsAppTemplateException(
      WhatsAppTemplateException ex, WebRequest request) {

    LOG.warn("WhatsApp template error: {}", ex.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_TEMPLATE_ERROR",
        "WhatsApp template operation failed",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handle WhatsAppWebhookException.
   */
  @ExceptionHandler(WhatsAppWebhookException.class)
  public ResponseEntity<Map<String, Object>> handleWhatsAppWebhookException(
      WhatsAppWebhookException ex, WebRequest request) {

    LOG.warn("WhatsApp webhook error: {}", ex.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_WEBHOOK_ERROR",
        "WhatsApp webhook processing failed",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handle BadRequestAlertException for WhatsApp operations.
   */
  @ExceptionHandler(BadRequestAlertException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequestAlertException(
      BadRequestAlertException ex, WebRequest request) {

    // Only handle WhatsApp-related BadRequestAlertException
    if (!"whatsapp".equals(ex.getEntityName())) {
      return null; // Let other handlers deal with it
    }

    LOG.warn("WhatsApp bad request: {}", ex.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_BAD_REQUEST",
        "Invalid WhatsApp request",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  /**
   * Handle generic RuntimeException for WhatsApp operations.
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntimeException(
      RuntimeException ex, WebRequest request) {

    // Only handle WhatsApp-related exceptions by checking the stack trace
    if (!isWhatsAppRelatedException(ex)) {
      return null; // Let other handlers deal with it
    }

    LOG.error("WhatsApp runtime error: {}", ex.getMessage(), ex);

    Map<String, Object> errorResponse = createErrorResponse(
        "WHATSAPP_INTERNAL_ERROR",
        "An internal error occurred while processing WhatsApp request",
        "An unexpected error occurred. Please try again later.",
        HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  /**
   * Create a standardized error response.
   */
  private Map<String, Object> createErrorResponse(String errorCode, String title,
      String detail, int status) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("errorCode", errorCode);
    errorResponse.put("title", title);
    errorResponse.put("detail", detail);
    errorResponse.put("status", status);
    errorResponse.put("timestamp", ZonedDateTime.now());
    return errorResponse;
  }

  /**
   * Check if the exception is WhatsApp-related by examining the stack trace.
   */
  private boolean isWhatsAppRelatedException(RuntimeException ex) {
    StackTraceElement[] stackTrace = ex.getStackTrace();
    for (StackTraceElement element : stackTrace) {
      String className = element.getClassName();
      if (className.contains("whatsapp") ||
          className.contains("WhatsApp") ||
          className.contains("TwilioWhatsApp")) {
        return true;
      }
    }
    return false;
  }
}
