package com.nextjstemplate.web.rest;

import com.nextjstemplate.service.TwilioWhatsAppService;
import com.nextjstemplate.service.dto.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Twilio WhatsApp operations.
 */
@RestController
@RequestMapping("/api/twilio-whatsapp")
public class TwilioWhatsAppResource {

  private static final Logger LOG = LoggerFactory.getLogger(TwilioWhatsAppResource.class);

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final TwilioWhatsAppService twilioWhatsAppService;

  public TwilioWhatsAppResource(TwilioWhatsAppService twilioWhatsAppService) {
    this.twilioWhatsAppService = twilioWhatsAppService;
  }

  /**
   * {@code POST /api/twilio-whatsapp/{tenantId}/send} : Send a WhatsApp message.
   *
   * @param tenantId The tenant ID
   * @param request  The message request
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         response in body
   */
  @PostMapping("/{tenantId}/send")
  public ResponseEntity<TwilioWhatsAppResponseDTO> sendMessage(
      @PathVariable String tenantId,
      @Valid @RequestBody TwilioWhatsAppRequestDTO request) {
    LOG.debug("REST request to send WhatsApp message for tenant: {}", tenantId);

    TwilioWhatsAppResponseDTO result = twilioWhatsAppService.sendMessage(tenantId, request);
    return ResponseEntity.ok(result);
  }

  /**
   * {@code POST /api/twilio-whatsapp/{tenantId}/send-bulk} : Send bulk WhatsApp
   * messages.
   *
   * @param tenantId The tenant ID
   * @param request  The bulk message request
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         response in body
   */
  @PostMapping("/{tenantId}/send-bulk")
  public ResponseEntity<TwilioWhatsAppBulkResponseDTO> sendBulkMessages(
      @PathVariable String tenantId,
      @Valid @RequestBody TwilioWhatsAppBulkRequestDTO request) {
    LOG.debug("REST request to send bulk WhatsApp messages for tenant: {}", tenantId);

    TwilioWhatsAppBulkResponseDTO result = twilioWhatsAppService.sendBulkMessages(tenantId, request);
    return ResponseEntity.ok(result);
  }

  /**
   * {@code GET /api/twilio-whatsapp/{tenantId}/status/{messageId}} : Check
   * message delivery status.
   *
   * @param tenantId  The tenant ID
   * @param messageId The Twilio message ID
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         status in body
   */
  @GetMapping("/{tenantId}/status/{messageId}")
  public ResponseEntity<TwilioWhatsAppResponseDTO> checkDeliveryStatus(
      @PathVariable String tenantId,
      @PathVariable String messageId) {
    LOG.debug("REST request to check delivery status for tenant: {}, messageId: {}", tenantId, messageId);

    TwilioWhatsAppResponseDTO result = twilioWhatsAppService.checkDeliveryStatus(tenantId, messageId);
    return ResponseEntity.ok(result);
  }

  /**
   * {@code GET /api/twilio-whatsapp/{tenantId}/templates} : Get approved
   * templates.
   *
   * @param tenantId The tenant ID
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         templates in body
   */
  @GetMapping("/{tenantId}/templates")
  public ResponseEntity<List<TwilioTemplateDTO>> getApprovedTemplates(@PathVariable String tenantId) {
    LOG.debug("REST request to get approved templates for tenant: {}", tenantId);

    List<TwilioTemplateDTO> result = twilioWhatsAppService.getApprovedTemplates(tenantId);
    return ResponseEntity.ok(result);
  }

  /**
   * {@code POST /api/twilio-whatsapp/{tenantId}/templates} : Create a new
   * template.
   *
   * @param tenantId The tenant ID
   * @param request  The template request
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and the
   *         template in body
   */
  @PostMapping("/{tenantId}/templates")
  public ResponseEntity<TwilioTemplateDTO> createTemplate(
      @PathVariable String tenantId,
      @Valid @RequestBody TwilioTemplateDTO request) throws URISyntaxException {
    LOG.debug("REST request to create template for tenant: {}", tenantId);

    TwilioTemplateDTO result = twilioWhatsAppService.createTemplate(tenantId, request);

    return ResponseEntity
        .created(new URI("/api/twilio-whatsapp/" + tenantId + "/templates/" + result.getId()))
        .body(result);
  }

  /**
   * {@code POST /api/twilio-whatsapp/{tenantId}/webhook} : Handle Twilio webhook.
   *
   * @param tenantId The tenant ID
   * @param payload  The webhook payload
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         processing result in body
   */
  @PostMapping("/{tenantId}/webhook")
  public ResponseEntity<Map<String, Object>> handleWebhook(
      @PathVariable String tenantId,
      @RequestBody Map<String, Object> payload) {
    LOG.debug("REST request to handle webhook for tenant: {}", tenantId);

    Map<String, Object> result = twilioWhatsAppService.handleWebhook(tenantId, payload);
    return ResponseEntity.ok(result);
  }

  /**
   * {@code GET /api/twilio-whatsapp/{tenantId}/credentials} : Get tenant
   * credentials.
   *
   * @param tenantId The tenant ID
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
   *         credentials in body
   */
  @GetMapping("/{tenantId}/credentials")
  public ResponseEntity<TwilioCredentialsDTO> getTenantCredentials(@PathVariable String tenantId) {
    LOG.debug("REST request to get credentials for tenant: {}", tenantId);

    TwilioCredentialsDTO result = twilioWhatsAppService.getTenantCredentials(tenantId);
    return ResponseEntity.ok(result);
  }

  /**
   * {@code PUT /api/twilio-whatsapp/{tenantId}/credentials} : Update tenant
   * credentials.
   *
   * @param tenantId    The tenant ID
   * @param credentials The credentials to store
   * @return the {@link ResponseEntity} with status {@code 200 (OK)}
   */
  @PutMapping("/{tenantId}/credentials")
  public ResponseEntity<Void> updateTenantCredentials(
      @PathVariable String tenantId,
      @Valid @RequestBody TwilioCredentialsDTO credentials) {
    LOG.debug("REST request to update credentials for tenant: {}", tenantId);

    twilioWhatsAppService.encryptAndStoreCredentials(tenantId, credentials);
    return ResponseEntity.ok().build();
  }
}
