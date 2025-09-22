package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.*;
import java.util.List;
import java.util.Map;

/**
 * Service interface for Twilio WhatsApp API interactions.
 */
public interface TwilioWhatsAppService {

  /**
   * Send a single WhatsApp message.
   *
   * @param tenantId The tenant ID
   * @param request  The message request
   * @return The response containing message ID and status
   */
  TwilioWhatsAppResponseDTO sendMessage(String tenantId, TwilioWhatsAppRequestDTO request);

  /**
   * Send bulk WhatsApp messages.
   *
   * @param tenantId The tenant ID
   * @param request  The bulk message request
   * @return The response containing results for all messages
   */
  TwilioWhatsAppBulkResponseDTO sendBulkMessages(String tenantId, TwilioWhatsAppBulkRequestDTO request);

  /**
   * Check the delivery status of a message.
   *
   * @param tenantId  The tenant ID
   * @param messageId The Twilio message ID
   * @return The delivery status
   */
  TwilioWhatsAppResponseDTO checkDeliveryStatus(String tenantId, String messageId);

  /**
   * Get approved templates for a tenant.
   *
   * @param tenantId The tenant ID
   * @return List of approved templates
   */
  List<TwilioTemplateDTO> getApprovedTemplates(String tenantId);

  /**
   * Create a new template for a tenant.
   *
   * @param tenantId The tenant ID
   * @param request  The template request
   * @return The created template
   */
  TwilioTemplateDTO createTemplate(String tenantId, TwilioTemplateDTO request);

  /**
   * Handle incoming webhook from Twilio.
   *
   * @param tenantId The tenant ID
   * @param payload  The webhook payload
   * @return Processing result
   */
  Map<String, Object> handleWebhook(String tenantId, Map<String, Object> payload);

  /**
   * Get tenant credentials (private method).
   *
   * @param tenantId The tenant ID
   * @return The tenant's Twilio credentials
   */
  TwilioCredentialsDTO getTenantCredentials(String tenantId);

  /**
   * Encrypt and store credentials for a tenant (private method).
   *
   * @param tenantId    The tenant ID
   * @param credentials The credentials to store
   */
  void encryptAndStoreCredentials(String tenantId, TwilioCredentialsDTO credentials);

  /**
   * Test Twilio connection and credentials.
   *
   * @param tenantId The tenant ID
   * @return The connection test result
   */
  ConnectionTestResultDTO testConnection(String tenantId);

  /**
   * Get WhatsApp analytics for a tenant.
   *
   * @param tenantId The tenant ID
   * @param fromDate The start date for analytics (optional)
   * @param toDate   The end date for analytics (optional)
   * @return The analytics data
   */
  WhatsAppAnalyticsDTO getAnalytics(String tenantId, java.time.ZonedDateTime fromDate, java.time.ZonedDateTime toDate);

  /**
   * Log a message entry (private method).
   *
   * @param logEntry The log entry to store
   */
  void logMessage(com.nextjstemplate.service.dto.WhatsAppLogEntryDTO logEntry);
}
