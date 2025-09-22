package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.WebhookSignatureValidator;
import com.nextjstemplate.service.WhatsAppLogService;
import com.nextjstemplate.service.dto.WebhookPayloadDTO;
import com.nextjstemplate.service.dto.WhatsAppLogDTO;
import com.nextjstemplate.service.dto.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

/**
 * REST controller for handling WhatsApp webhooks from Twilio.
 */
@RestController
@RequestMapping("/api/whatsapp/webhook")
public class WhatsAppWebhookResource {

  private static final Logger LOG = LoggerFactory.getLogger(WhatsAppWebhookResource.class);

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final WebhookSignatureValidator webhookSignatureValidator;
  private final WhatsAppLogService whatsAppLogService;

  public WhatsAppWebhookResource(WebhookSignatureValidator webhookSignatureValidator,
      WhatsAppLogService whatsAppLogService) {
    this.webhookSignatureValidator = webhookSignatureValidator;
    this.whatsAppLogService = whatsAppLogService;
  }

  /**
   * {@code POST /api/whatsapp/webhook/delivery-status} : Handle delivery status
   * webhooks from Twilio.
   *
   * @param request  The HTTP request containing the webhook payload
   * @param tenantId The tenant ID extracted from the webhook URL or header
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}
   */
  @PostMapping("/delivery-status")
  public ResponseEntity<String> handleDeliveryStatus(
      HttpServletRequest request,
      @RequestParam(required = false) String tenantId) {

    LOG.debug("REST request to handle WhatsApp delivery status webhook for tenant: {}", tenantId);

    try {
      // Extract tenant ID from various sources
      String extractedTenantId = extractTenantId(request, tenantId);
      if (extractedTenantId == null) {
        LOG.warn("No tenant ID found in webhook request");
        return ResponseEntity.badRequest().body("Tenant ID is required");
      }

      // Get webhook token for signature validation
      String webhookToken = getWebhookToken(extractedTenantId);
      if (webhookToken == null) {
        LOG.warn("No webhook token found for tenant: {}", extractedTenantId);
        return ResponseEntity.badRequest().body("Webhook not configured for tenant");
      }

      // Validate webhook signature
      if (!webhookSignatureValidator.validateSignature(request, webhookToken)) {
        LOG.warn("Invalid webhook signature for tenant: {}", extractedTenantId);
        return ResponseEntity.badRequest().body("Invalid webhook signature");
      }

      // Parse webhook payload
      WebhookPayloadDTO payload = parseWebhookPayload(request, extractedTenantId);

      // Update message status in database
      updateMessageStatus(payload);

      LOG.info("Successfully processed delivery status webhook for message: {}, tenant: {}",
          payload.getMessageId(), extractedTenantId);

      return ResponseEntity.ok("Webhook processed successfully");

    } catch (Exception e) {
      LOG.error("Error processing delivery status webhook: {}", e.getMessage(), e);
      return ResponseEntity.internalServerError().body("Error processing webhook");
    }
  }

  /**
   * {@code POST /api/whatsapp/webhook/message-status} : Handle message status
   * webhooks from Twilio.
   *
   * @param request  The HTTP request containing the webhook payload
   * @param tenantId The tenant ID extracted from the webhook URL or header
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}
   */
  @PostMapping("/message-status")
  public ResponseEntity<String> handleMessageStatus(
      HttpServletRequest request,
      @RequestParam(required = false) String tenantId) {

    LOG.debug("REST request to handle WhatsApp message status webhook for tenant: {}", tenantId);

    try {
      // Extract tenant ID from various sources
      String extractedTenantId = extractTenantId(request, tenantId);
      if (extractedTenantId == null) {
        LOG.warn("No tenant ID found in webhook request");
        return ResponseEntity.badRequest().body("Tenant ID is required");
      }

      // Get webhook token for signature validation
      String webhookToken = getWebhookToken(extractedTenantId);
      if (webhookToken == null) {
        LOG.warn("No webhook token found for tenant: {}", extractedTenantId);
        return ResponseEntity.badRequest().body("Webhook not configured for tenant");
      }

      // Validate webhook signature
      if (!webhookSignatureValidator.validateSignature(request, webhookToken)) {
        LOG.warn("Invalid webhook signature for tenant: {}", extractedTenantId);
        return ResponseEntity.badRequest().body("Invalid webhook signature");
      }

      // Parse webhook payload
      WebhookPayloadDTO payload = parseWebhookPayload(request, extractedTenantId);

      // Update message status in database
      updateMessageStatus(payload);

      LOG.info("Successfully processed message status webhook for message: {}, tenant: {}",
          payload.getMessageId(), extractedTenantId);

      return ResponseEntity.ok("Webhook processed successfully");

    } catch (Exception e) {
      LOG.error("Error processing message status webhook: {}", e.getMessage(), e);
      return ResponseEntity.internalServerError().body("Error processing webhook");
    }
  }

  /**
   * Extracts tenant ID from various sources in the request.
   */
  private String extractTenantId(HttpServletRequest request, String tenantId) {
    // First check the parameter
    if (tenantId != null && !tenantId.trim().isEmpty()) {
      return tenantId;
    }

    // Check X-Tenant-ID header
    String headerTenantId = request.getHeader("X-Tenant-ID");
    if (headerTenantId != null && !headerTenantId.trim().isEmpty()) {
      return headerTenantId;
    }

    // Check the webhook URL path for tenant ID pattern
    String requestURI = request.getRequestURI();
    if (requestURI.contains("/tenant/")) {
      String[] parts = requestURI.split("/tenant/");
      if (parts.length > 1) {
        String[] tenantParts = parts[1].split("/");
        if (tenantParts.length > 0) {
          return tenantParts[0];
        }
      }
    }

    return null;
  }

  /**
   * Gets the webhook token for the given tenant.
   * This should be retrieved from the tenant settings.
   */
  private String getWebhookToken(String tenantId) {
    // TODO: Implement retrieval from tenant settings
    // For now, return a placeholder
    LOG.debug("Retrieving webhook token for tenant: {}", tenantId);
    return "placeholder-webhook-token";
  }

  /**
   * Parses the webhook payload from the request.
   */
  private WebhookPayloadDTO parseWebhookPayload(HttpServletRequest request, String tenantId) {
    WebhookPayloadDTO payload = new WebhookPayloadDTO();
    payload.setTenantId(tenantId);

    // Parse Twilio webhook parameters
    String messageId = request.getParameter("MessageSid");
    String status = request.getParameter("MessageStatus");
    String errorCode = request.getParameter("ErrorCode");
    String errorMessage = request.getParameter("ErrorMessage");
    String recipientPhone = request.getParameter("To");
    String senderPhone = request.getParameter("From");
    String eventType = request.getParameter("EventType");

    payload.setMessageId(messageId);
    payload.setStatus(status);
    payload.setErrorCode(errorCode);
    payload.setErrorMessage(errorMessage);
    payload.setRecipientPhoneNumber(recipientPhone);
    payload.setSenderPhoneNumber(senderPhone);
    payload.setEventType(eventType);
    payload.setTimestamp(ZonedDateTime.now());

    LOG.debug("Parsed webhook payload: {}", payload);
    return payload;
  }

  /**
   * Updates the message status in the database.
   */
  private void updateMessageStatus(WebhookPayloadDTO payload) {
    try {
      // Find the existing log entry by message ID
      // This would typically involve a database query
      // For now, we'll create a new log entry

      WhatsAppLogDTO logEntry = new WhatsAppLogDTO();
      // Note: WhatsAppLogDTO doesn't have setMessageId method, storing in metadata
      // instead
      logEntry.setRecipientPhone(payload.getRecipientPhoneNumber());
      logEntry.setMessageBody(""); // Not available in webhook
      logEntry.setStatus(mapTwilioStatusToMessageStatus(payload.getStatus()).toString());
      logEntry.setSentAt(payload.getTimestamp());
      logEntry.setType("WEBHOOK_UPDATE");
      logEntry.setCampaignId(null); // Not available in webhook
      logEntry.setMetadata(String.format(
          "{\"messageId\":\"%s\",\"eventType\":\"%s\",\"errorCode\":\"%s\",\"errorMessage\":\"%s\"}",
          payload.getMessageId() != null ? payload.getMessageId() : "",
          payload.getEventType() != null ? payload.getEventType() : "",
          payload.getErrorCode() != null ? payload.getErrorCode() : "",
          payload.getErrorMessage() != null ? payload.getErrorMessage() : ""));

      // Save the log entry
      whatsAppLogService.save(logEntry);

      LOG.debug("Updated message status for message ID: {} to status: {}",
          payload.getMessageId(), payload.getStatus());

    } catch (Exception e) {
      LOG.error("Error updating message status for message ID: {}: {}",
          payload.getMessageId(), e.getMessage(), e);
    }
  }

  /**
   * Maps Twilio status to our MessageStatus enum.
   */
  private MessageStatus mapTwilioStatusToMessageStatus(String twilioStatus) {
    if (twilioStatus == null) {
      return MessageStatus.PENDING;
    }

    switch (twilioStatus.toUpperCase()) {
      case "SENT":
        return MessageStatus.SENT;
      case "DELIVERED":
        return MessageStatus.DELIVERED;
      case "READ":
        return MessageStatus.READ;
      case "FAILED":
        return MessageStatus.FAILED;
      case "UNDELIVERED":
        return MessageStatus.UNDELIVERED;
      case "REJECTED":
        return MessageStatus.REJECTED;
      default:
        return MessageStatus.PENDING;
    }
  }
}
