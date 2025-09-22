package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.TwilioWhatsAppService;
import com.nextjstemplate.service.dto.TwilioWhatsAppRequestDTO;
import com.nextjstemplate.service.dto.TwilioWhatsAppResponseDTO;
import com.nextjstemplate.service.dto.TwilioWhatsAppBulkRequestDTO;
import com.nextjstemplate.service.dto.TwilioWhatsAppBulkResponseDTO;
import com.nextjstemplate.service.dto.ConnectionTestResultDTO;
import com.nextjstemplate.service.dto.MessageTemplateDTO;
import com.nextjstemplate.service.dto.TwilioTemplateDTO;
import com.nextjstemplate.service.dto.WhatsAppAnalyticsDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * REST controller for managing WhatsApp message operations.
 */
@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppResource {

  private static final Logger LOG = LoggerFactory.getLogger(WhatsAppResource.class);
  private static final String ENTITY_NAME = "whatsapp";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final TwilioWhatsAppService twilioWhatsAppService;

  public WhatsAppResource(TwilioWhatsAppService twilioWhatsAppService) {
    this.twilioWhatsAppService = twilioWhatsAppService;
  }

  /**
   * {@code POST /api/whatsapp/send-message} : Send a WhatsApp message.
   *
   * @param request The message request containing tenantId and message details
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         response in body
   */
  @PostMapping("/send-message")
  public ResponseEntity<TwilioWhatsAppResponseDTO> sendMessage(
      @Valid @RequestBody TwilioWhatsAppRequestDTO request) {
    LOG.debug("REST request to send WhatsApp message: {}", request);

    try {
      // Validate tenant ID is present
      if (request.getTenantId() == null || request.getTenantId().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      // Validate recipient phone number
      if (request.getTo() == null || request.getTo().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Recipient phone number is required",
            ENTITY_NAME,
            "recipientrequired");
      }

      // Validate message content
      if (request.getMessageBody() == null || request.getMessageBody().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Message body is required",
            ENTITY_NAME,
            "messagebodyrequired");
      }

      TwilioWhatsAppResponseDTO result = twilioWhatsAppService.sendMessage(
          request.getTenantId(),
          request);

      LOG.info("Successfully sent WhatsApp message for tenant: {}", request.getTenantId());
      return ResponseEntity.ok(result);

    } catch (RuntimeException e) {
      LOG.error("Failed to send WhatsApp message: {}", e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to send WhatsApp message: " + e.getMessage(),
          ENTITY_NAME,
          "sendmessageerror");
    }
  }

  /**
   * {@code POST /api/whatsapp/send-bulk} : Send bulk WhatsApp messages.
   *
   * @param request The bulk message request containing tenantId and message
   *                details
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         response in body
   */
  @PostMapping("/send-bulk")
  public ResponseEntity<TwilioWhatsAppBulkResponseDTO> sendBulkMessages(
      @Valid @RequestBody TwilioWhatsAppBulkRequestDTO request) {
    LOG.debug("REST request to send bulk WhatsApp messages: {}", request);

    try {
      // Validate tenant ID is present
      if (request.getTenantId() == null || request.getTenantId().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      // Validate recipients list
      if (request.getRecipients() == null || request.getRecipients().isEmpty()) {
        throw new BadRequestAlertException(
            "Recipients list is required",
            ENTITY_NAME,
            "recipientsrequired");
      }

      // Validate message content
      if (request.getMessageBody() == null || request.getMessageBody().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Message body is required",
            ENTITY_NAME,
            "messagebodyrequired");
      }

      TwilioWhatsAppBulkResponseDTO result = twilioWhatsAppService.sendBulkMessages(
          request.getTenantId(),
          request);

      LOG.info("Successfully sent bulk WhatsApp messages for tenant: {}, sent: {}, failed: {}",
          request.getTenantId(), result.getTotalSent(), result.getTotalFailed());
      return ResponseEntity.ok(result);

    } catch (RuntimeException e) {
      LOG.error("Failed to send bulk WhatsApp messages: {}", e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to send bulk WhatsApp messages: " + e.getMessage(),
          ENTITY_NAME,
          "sendbulkmessageserror");
    }
  }

  /**
   * {@code GET /api/whatsapp/delivery-status/{messageId}} : Check message
   * delivery status.
   *
   * @param messageId The Twilio message ID
   * @param tenantId  The tenant ID (required for tenant isolation)
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         status in body
   */
  @GetMapping("/delivery-status/{messageId}")
  public ResponseEntity<TwilioWhatsAppResponseDTO> checkDeliveryStatus(
      @PathVariable String messageId,
      @RequestParam String tenantId) {
    LOG.debug("REST request to check delivery status for messageId: {}, tenantId: {}", messageId, tenantId);

    try {
      // Validate message ID
      if (messageId == null || messageId.trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Message ID is required",
            ENTITY_NAME,
            "messageidrequired");
      }

      // Validate tenant ID
      if (tenantId == null || tenantId.trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      TwilioWhatsAppResponseDTO result = twilioWhatsAppService.checkDeliveryStatus(
          tenantId,
          messageId);

      LOG.debug("Retrieved delivery status for messageId: {}, tenantId: {}, status: {}",
          messageId, tenantId, result.getStatus());
      return ResponseEntity.ok(result);

    } catch (RuntimeException e) {
      LOG.error("Failed to check delivery status for messageId: {}, tenantId: {}, error: {}",
          messageId, tenantId, e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to check delivery status: " + e.getMessage(),
          ENTITY_NAME,
          "checkdeliverystatuserror");
    }
  }

  /**
   * {@code GET /api/whatsapp/test-connection} : Test Twilio connection and
   * credentials.
   *
   * @param tenantId The tenant ID (required for tenant isolation)
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         connection test result in body
   */
  @GetMapping("/test-connection")
  public ResponseEntity<ConnectionTestResultDTO> testConnection(
      @RequestParam String tenantId) {
    LOG.debug("REST request to test Twilio connection for tenant: {}", tenantId);

    try {
      // Validate tenant ID
      if (tenantId == null || tenantId.trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      ConnectionTestResultDTO result = twilioWhatsAppService.testConnection(tenantId);

      LOG.info("Connection test completed for tenant: {}, success: {}",
          tenantId, result.isSuccess());
      return ResponseEntity.ok(result);

    } catch (RuntimeException e) {
      LOG.error("Failed to test connection for tenant: {}, error: {}",
          tenantId, e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to test connection: " + e.getMessage(),
          ENTITY_NAME,
          "testconnectionerror");
    }
  }

  /**
   * {@code GET /api/whatsapp/templates} : Get approved WhatsApp message
   * templates.
   *
   * @param tenantId The tenant ID (required for tenant isolation)
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         list of templates in body
   */
  @GetMapping("/templates")
  public ResponseEntity<List<TwilioTemplateDTO>> getTemplates(
      @RequestParam String tenantId) {
    LOG.debug("REST request to get WhatsApp templates for tenant: {}", tenantId);

    try {
      // Validate tenant ID
      if (tenantId == null || tenantId.trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      List<TwilioTemplateDTO> templates = twilioWhatsAppService.getApprovedTemplates(tenantId);

      LOG.info("Retrieved {} templates for tenant: {}", templates.size(), tenantId);
      return ResponseEntity.ok(templates);

    } catch (RuntimeException e) {
      LOG.error("Failed to get templates for tenant: {}, error: {}",
          tenantId, e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to get templates: " + e.getMessage(),
          ENTITY_NAME,
          "gettemplateserror");
    }
  }

  /**
   * {@code POST /api/whatsapp/templates} : Create a new WhatsApp message
   * template.
   *
   * @param tenantId The tenant ID (required for tenant isolation)
   * @param request  The template creation request
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         created template in body
   */
  @PostMapping("/templates")
  public ResponseEntity<TwilioTemplateDTO> createTemplate(
      @RequestParam String tenantId,
      @Valid @RequestBody TwilioTemplateDTO request) {
    LOG.debug("REST request to create WhatsApp template for tenant: {}", tenantId);

    try {
      // Validate tenant ID
      if (tenantId == null || tenantId.trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      // Validate template request
      if (request.getName() == null || request.getName().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Template name is required",
            ENTITY_NAME,
            "templatenamerequired");
      }

      if (request.getContent() == null || request.getContent().trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Template content is required",
            ENTITY_NAME,
            "templatecontentrequired");
      }

      TwilioTemplateDTO template = twilioWhatsAppService.createTemplate(tenantId, request);

      LOG.info("Created template for tenant: {}, templateId: {}",
          tenantId, template.getId());
      return ResponseEntity.ok(template);

    } catch (RuntimeException e) {
      LOG.error("Failed to create template for tenant: {}, error: {}",
          tenantId, e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to create template: " + e.getMessage(),
          ENTITY_NAME,
          "createtemplateerror");
    }
  }

  /**
   * {@code GET /api/whatsapp/analytics} : Get WhatsApp analytics for a tenant.
   *
   * @param tenantId The tenant ID (required for tenant isolation)
   * @param fromDate The start date for analytics (optional, defaults to 30 days
   *                 ago)
   * @param toDate   The end date for analytics (optional, defaults to now)
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         analytics data in body
   */
  @GetMapping("/analytics")
  public ResponseEntity<WhatsAppAnalyticsDTO> getAnalytics(
      @RequestParam String tenantId,
      @RequestParam(required = false) ZonedDateTime fromDate,
      @RequestParam(required = false) ZonedDateTime toDate) {
    LOG.debug("REST request to get WhatsApp analytics for tenant: {}, from: {}, to: {}",
        tenantId, fromDate, toDate);

    try {
      // Validate tenant ID
      if (tenantId == null || tenantId.trim().isEmpty()) {
        throw new BadRequestAlertException(
            "Tenant ID is required",
            ENTITY_NAME,
            "tenantidrequired");
      }

      WhatsAppAnalyticsDTO analytics = twilioWhatsAppService.getAnalytics(tenantId, fromDate, toDate);

      LOG.info("Retrieved analytics for tenant: {}, total messages: {}",
          tenantId, analytics.getTotalMessages());
      return ResponseEntity.ok(analytics);

    } catch (RuntimeException e) {
      LOG.error("Failed to get analytics for tenant: {}, error: {}",
          tenantId, e.getMessage(), e);
      throw new BadRequestAlertException(
          "Failed to get analytics: " + e.getMessage(),
          ENTITY_NAME,
          "getanalyticserror");
    }
  }
}
