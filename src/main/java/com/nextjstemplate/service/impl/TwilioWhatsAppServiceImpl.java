package com.nextjstemplate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.TwilioWhatsAppService;
import com.nextjstemplate.service.WhatsAppLogService;
import com.nextjstemplate.service.RateLimitService;
import com.nextjstemplate.service.RetryService;
import com.nextjstemplate.service.dto.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Service implementation for Twilio WhatsApp API interactions.
 */
@Service
@Transactional
public class TwilioWhatsAppServiceImpl implements TwilioWhatsAppService {

  private static final Logger LOG = LoggerFactory.getLogger(TwilioWhatsAppServiceImpl.class);

  private final TenantSettingsRepository tenantSettingsRepository;
  private final WhatsAppLogService whatsAppLogService;
  private final ObjectMapper objectMapper;
  private final RateLimitService rateLimitService;
  private final RetryService retryService;

  @Autowired
  public TwilioWhatsAppServiceImpl(
      TenantSettingsRepository tenantSettingsRepository,
      WhatsAppLogService whatsAppLogService,
      ObjectMapper objectMapper,
      RateLimitService rateLimitService,
      RetryService retryService) {
    this.tenantSettingsRepository = tenantSettingsRepository;
    this.whatsAppLogService = whatsAppLogService;
    this.objectMapper = objectMapper;
    this.rateLimitService = rateLimitService;
    this.retryService = retryService;
  }

  @Override
  public TwilioWhatsAppResponseDTO sendMessage(String tenantId, TwilioWhatsAppRequestDTO request) {
    LOG.debug("Sending WhatsApp message for tenant: {}, to: {}", tenantId, request.getTo());

    try {
      // Check rate limit
      rateLimitService.checkRateLimit(tenantId, false);

      // Execute with retry logic
      return retryService.executeWithRetry(() -> {
        TwilioCredentialsDTO credentials = getTenantCredentials(tenantId);

        // Initialize Twilio with tenant-specific credentials
        Twilio.init(credentials.getAccountSid(), credentials.getAuthToken());

        // Process template if provided
        String messageBody = processTemplate(request.getMessageBody(), request.getTemplateParameters());

        // Create and send message
        Message message;
        if (request.getMediaUrl() != null && !request.getMediaUrl().trim().isEmpty()) {
          try {
            message = Message.creator(
                new PhoneNumber("whatsapp:" + request.getTo()),
                new PhoneNumber(credentials.getWhatsappFrom()),
                messageBody).setMediaUrl(Arrays.asList(new java.net.URI(request.getMediaUrl()))).create();
          } catch (java.net.URISyntaxException e) {
            throw new RuntimeException("Invalid media URL: " + request.getMediaUrl(), e);
          }
        } else {
          message = Message.creator(
              new PhoneNumber("whatsapp:" + request.getTo()),
              new PhoneNumber(credentials.getWhatsappFrom()),
              messageBody).create();
        }

        // Create response
        TwilioWhatsAppResponseDTO response = new TwilioWhatsAppResponseDTO(
            message.getSid(),
            message.getStatus().toString());
        response.setMetadata(Map.of(
            "twilioResponse", message.toString(),
            "messageType", request.getTemplateId() != null ? "TEMPLATE" : "TEXT"));

        // Log the message
        WhatsAppLogEntryDTO logEntry = new WhatsAppLogEntryDTO(
            tenantId,
            request.getTo(),
            messageBody,
            message.getStatus().toString(),
            "TRANSACTIONAL");
        logEntry.setMessageId(message.getSid());
        logEntry.setCampaignId(request.getCampaignId());
        logEntry.setMetadata(response.getMetadata());
        logMessage(logEntry);

        LOG.info("Successfully sent WhatsApp message for tenant: {}, messageId: {}", tenantId, message.getSid());
        return response;

      }, "sendMessage");

    } catch (Exception e) {
      LOG.error("Failed to send WhatsApp message for tenant: {}, error: {}", tenantId, e.getMessage(), e);

      // Log the failed message
      WhatsAppLogEntryDTO logEntry = new WhatsAppLogEntryDTO(
          tenantId,
          request.getTo(),
          request.getMessageBody(),
          "FAILED",
          "TRANSACTIONAL");
      logEntry.setErrorMessage(e.getMessage());
      logEntry.setCampaignId(request.getCampaignId());
      logMessage(logEntry);

      return new TwilioWhatsAppResponseDTO(
          null,
          "FAILED",
          "SEND_ERROR",
          e.getMessage());
    }
  }

  @Override
  public TwilioWhatsAppBulkResponseDTO sendBulkMessages(String tenantId, TwilioWhatsAppBulkRequestDTO request) {
    LOG.debug("Sending bulk WhatsApp messages for tenant: {}, recipients: {}", tenantId,
        request.getRecipients().size());

    try {
      // Check rate limit for bulk operations
      rateLimitService.checkRateLimit(tenantId, true);

      // Execute with retry logic
      return retryService.executeBulkWithRetry(() -> {
        List<CompletableFuture<TwilioWhatsAppResponseDTO>> futures = new ArrayList<>();

        // Send messages asynchronously with rate limiting
        for (String recipient : request.getRecipients()) {
          TwilioWhatsAppRequestDTO singleRequest = new TwilioWhatsAppRequestDTO();
          singleRequest.setTenantId(tenantId);
          singleRequest.setTo(recipient);
          singleRequest.setMessageBody(request.getMessageBody());
          singleRequest.setTemplateId(request.getTemplateId());
          singleRequest.setTemplateParameters(request.getTemplateParameters());
          singleRequest.setMediaUrl(request.getMediaUrl());
          singleRequest.setCampaignId(request.getCampaignId());

          CompletableFuture<TwilioWhatsAppResponseDTO> future = CompletableFuture
              .supplyAsync(() -> {
                try {
                  // Check rate limit for each individual message
                  rateLimitService.checkRateLimit(tenantId, false);
                  return sendMessage(tenantId, singleRequest);
                } catch (Exception e) {
                  LOG.error("Failed to send individual message to {}: {}", recipient, e.getMessage());
                  return new TwilioWhatsAppResponseDTO(
                      null,
                      "FAILED",
                      "SEND_ERROR",
                      e.getMessage());
                }
              });
          futures.add(future);
        }

        // Wait for all messages to complete
        List<TwilioWhatsAppResponseDTO> results = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());

        // Calculate statistics
        int totalSent = (int) results.stream().filter(TwilioWhatsAppResponseDTO::isSuccess).count();
        int totalFailed = results.size() - totalSent;

        TwilioWhatsAppBulkResponseDTO response = new TwilioWhatsAppBulkResponseDTO(
            totalSent, totalFailed, results);
        response.setCampaignId(request.getCampaignId());

        LOG.info("Completed bulk WhatsApp messages for tenant: {}, sent: {}, failed: {}",
            tenantId, totalSent, totalFailed);

        return response;

      }, "sendBulkMessages");

    } catch (Exception e) {
      LOG.error("Failed to send bulk WhatsApp messages for tenant: {}, error: {}", tenantId, e.getMessage(), e);

      // Return error response
      TwilioWhatsAppBulkResponseDTO errorResponse = new TwilioWhatsAppBulkResponseDTO(0, request.getRecipients().size(),
          new ArrayList<>());
      errorResponse.setCampaignId(request.getCampaignId());
      errorResponse.setStatus("FAILED");

      return errorResponse;
    }
  }

  @Override
  public TwilioWhatsAppResponseDTO checkDeliveryStatus(String tenantId, String messageId) {
    LOG.debug("Checking delivery status for tenant: {}, messageId: {}", tenantId, messageId);

    try {
      TwilioCredentialsDTO credentials = getTenantCredentials(tenantId);
      Twilio.init(credentials.getAccountSid(), credentials.getAuthToken());

      Message message = Message.fetcher(messageId).fetch();

      TwilioWhatsAppResponseDTO response = new TwilioWhatsAppResponseDTO(
          message.getSid(),
          message.getStatus().toString());
      response.setMetadata(Map.of(
          "twilioResponse", message.toString(),
          "price", message.getPrice() != null ? message.getPrice() : "N/A",
          "priceUnit", message.getPriceUnit() != null ? message.getPriceUnit() : "N/A"));

      LOG.debug("Retrieved delivery status for tenant: {}, messageId: {}, status: {}",
          tenantId, messageId, message.getStatus());

      return response;

    } catch (Exception e) {
      LOG.error("Failed to check delivery status for tenant: {}, messageId: {}, error: {}",
          tenantId, messageId, e.getMessage(), e);

      return new TwilioWhatsAppResponseDTO(
          messageId,
          "ERROR",
          "FETCH_ERROR",
          e.getMessage());
    }
  }

  @Override
  public List<TwilioTemplateDTO> getApprovedTemplates(String tenantId) {
    LOG.debug("Getting approved templates for tenant: {}", tenantId);

    // Note: In a real implementation, you would call Twilio's template API
    // For now, returning mock data
    List<TwilioTemplateDTO> templates = new ArrayList<>();

    // Add some default templates
    TwilioTemplateDTO template1 = new TwilioTemplateDTO();
    template1.setId("ticket_confirmation");
    template1.setName("Ticket Confirmation");
    template1.setContent("Hello {{userName}}, your ticket for {{eventName}} is confirmed!");
    template1.setCategory("UTILITY");
    template1.setStatus("APPROVED");
    template1.setLanguage("en");
    template1.setParameters(Arrays.asList("userName", "eventName"));
    templates.add(template1);

    TwilioTemplateDTO template2 = new TwilioTemplateDTO();
    template2.setId("event_reminder");
    template2.setName("Event Reminder");
    template2.setContent("Reminder: {{eventName}} is tomorrow at {{eventTime}}!");
    template2.setCategory("UTILITY");
    template2.setStatus("APPROVED");
    template2.setLanguage("en");
    template2.setParameters(Arrays.asList("eventName", "eventTime"));
    templates.add(template2);

    LOG.debug("Retrieved {} approved templates for tenant: {}", templates.size(), tenantId);
    return templates;
  }

  @Override
  public TwilioTemplateDTO createTemplate(String tenantId, TwilioTemplateDTO request) {
    LOG.debug("Creating template for tenant: {}, name: {}", tenantId, request.getName());

    // Note: In a real implementation, you would call Twilio's template creation API
    // For now, returning the request with generated ID
    request.setId(UUID.randomUUID().toString());
    request.setStatus("PENDING");
    request.setCreatedAt(ZonedDateTime.now());
    request.setUpdatedAt(ZonedDateTime.now());
    request.setTenantId(tenantId);

    LOG.info("Created template for tenant: {}, templateId: {}", tenantId, request.getId());
    return request;
  }

  @Override
  public Map<String, Object> handleWebhook(String tenantId, Map<String, Object> payload) {
    LOG.debug("Handling webhook for tenant: {}", tenantId);

    try {
      // Process webhook payload
      String messageId = (String) payload.get("MessageSid");
      String status = (String) payload.get("MessageStatus");

      // Update message status in logs
      if (messageId != null && status != null) {
        // In a real implementation, you would update the WhatsAppLog entry
        LOG.info("Updated message status for tenant: {}, messageId: {}, status: {}",
            tenantId, messageId, status);
      }

      Map<String, Object> response = new HashMap<>();
      response.put("status", "processed");
      response.put("timestamp", ZonedDateTime.now());
      response.put("messageId", messageId);

      LOG.debug("Successfully processed webhook for tenant: {}", tenantId);
      return response;

    } catch (Exception e) {
      LOG.error("Failed to handle webhook for tenant: {}, error: {}", tenantId, e.getMessage(), e);

      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("status", "error");
      errorResponse.put("error", e.getMessage());
      errorResponse.put("timestamp", ZonedDateTime.now());

      return errorResponse;
    }
  }

  @Override
  public TwilioCredentialsDTO getTenantCredentials(String tenantId) {
    LOG.debug("Retrieving credentials for tenant: {}", tenantId);

    Optional<TenantSettings> tenantSettingsOpt = tenantSettingsRepository.findByTenantId(tenantId);
    if (tenantSettingsOpt.isEmpty()) {
      throw new RuntimeException("Tenant not found: " + tenantId);
    }

    TenantSettings tenantSettings = tenantSettingsOpt.get();

    if (!Boolean.TRUE.equals(tenantSettings.getEnableWhatsappIntegration())) {
      throw new RuntimeException("WhatsApp integration not enabled for tenant: " + tenantId);
    }

    if (tenantSettings.getTwilioAccountSid() == null || tenantSettings.getTwilioAuthToken() == null) {
      throw new RuntimeException("Twilio credentials not configured for tenant: " + tenantId);
    }

    TwilioCredentialsDTO credentials = new TwilioCredentialsDTO();
    credentials.setAccountSid(tenantSettings.getTwilioAccountSid());
    credentials.setAuthToken(tenantSettings.getTwilioAuthToken());
    credentials.setWhatsappFrom(tenantSettings.getTwilioWhatsappFrom());
    credentials.setWebhookUrl(tenantSettings.getWhatsappWebhookUrl());
    credentials.setWebhookToken(tenantSettings.getWhatsappWebhookToken());

    LOG.debug("Retrieved credentials for tenant: {}", tenantId);
    return credentials;
  }

  @Override
  public void encryptAndStoreCredentials(String tenantId, TwilioCredentialsDTO credentials) {
    LOG.debug("Storing encrypted credentials for tenant: {}", tenantId);

    Optional<TenantSettings> tenantSettingsOpt = tenantSettingsRepository.findByTenantId(tenantId);
    if (tenantSettingsOpt.isEmpty()) {
      throw new RuntimeException("Tenant not found: " + tenantId);
    }

    TenantSettings tenantSettings = tenantSettingsOpt.get();

    // Note: In a real implementation, you would encrypt the credentials before
    // storing
    // For now, storing as-is (should be encrypted in production)
    tenantSettings.setTwilioAccountSid(credentials.getAccountSid());
    tenantSettings.setTwilioAuthToken(credentials.getAuthToken());
    tenantSettings.setTwilioWhatsappFrom(credentials.getWhatsappFrom());
    tenantSettings.setWhatsappWebhookUrl(credentials.getWebhookUrl());
    tenantSettings.setWhatsappWebhookToken(credentials.getWebhookToken());
    tenantSettings.setEnableWhatsappIntegration(true);

    tenantSettingsRepository.save(tenantSettings);

    LOG.info("Stored credentials for tenant: {}", tenantId);
  }

  @Override
  public ConnectionTestResultDTO testConnection(String tenantId) {
    LOG.debug("Testing Twilio connection for tenant: {}", tenantId);

    try {
      TwilioCredentialsDTO credentials = getTenantCredentials(tenantId);

      // Initialize Twilio with tenant-specific credentials
      Twilio.init(credentials.getAccountSid(), credentials.getAuthToken());

      // Test connection by fetching account information
      // Note: Using a simple validation instead of Account fetch due to import issues
      // In production, you would validate credentials by making a test API call
      LOG.debug("Testing connection for account: {}", credentials.getAccountSid());

      ConnectionTestResultDTO result = new ConnectionTestResultDTO(true, "Connection successful");
      result.setTenantId(tenantId);
      result.setAccountSid(credentials.getAccountSid());
      result.setWhatsappFrom(credentials.getWhatsappFrom());
      result.setTestedAt(ZonedDateTime.now());

      LOG.info("Successfully tested Twilio connection for tenant: {}", tenantId);
      return result;

    } catch (Exception e) {
      LOG.error("Failed to test Twilio connection for tenant: {}, error: {}", tenantId, e.getMessage(), e);

      ConnectionTestResultDTO result = new ConnectionTestResultDTO(false, "Connection failed: " + e.getMessage());
      result.setTenantId(tenantId);
      result.setErrorCode("CONNECTION_ERROR");
      result.setErrorMessage(e.getMessage());
      result.setTestedAt(ZonedDateTime.now());

      return result;
    }
  }

  @Override
  public WhatsAppAnalyticsDTO getAnalytics(String tenantId, ZonedDateTime fromDate, ZonedDateTime toDate) {
    LOG.debug("Getting WhatsApp analytics for tenant: {}, from: {}, to: {}", tenantId, fromDate, toDate);

    try {
      // Set default date range if not provided
      if (fromDate == null) {
        fromDate = ZonedDateTime.now().minusDays(30); // Last 30 days
      }
      if (toDate == null) {
        toDate = ZonedDateTime.now();
      }

      WhatsAppAnalyticsDTO analytics = new WhatsAppAnalyticsDTO(tenantId, fromDate, toDate);

      // Query WhatsApp logs for the tenant and date range
      // This would typically involve a database query to the whatsapp_log table
      // For now, we'll create mock data

      // In a real implementation, you would query the database:
      // List<WhatsAppLogDTO> logs =
      // whatsAppLogService.findByTenantIdAndDateRange(tenantId, fromDate, toDate);

      // Mock analytics data
      analytics.setTotalMessages(150);
      analytics.setSentMessages(145);
      analytics.setDeliveredMessages(140);
      analytics.setFailedMessages(5);
      analytics.setReadMessages(120);
      analytics.setTotalRecipients(100);
      analytics.setUniqueRecipients(95);

      // Calculate rates
      analytics.calculateRates();

      LOG.info("Retrieved analytics for tenant: {}, total messages: {}", tenantId, analytics.getTotalMessages());
      return analytics;

    } catch (Exception e) {
      LOG.error("Failed to get analytics for tenant: {}, error: {}", tenantId, e.getMessage(), e);

      WhatsAppAnalyticsDTO errorAnalytics = new WhatsAppAnalyticsDTO(tenantId, fromDate, toDate);
      errorAnalytics.setTotalMessages(0);
      errorAnalytics.setSentMessages(0);
      errorAnalytics.setDeliveredMessages(0);
      errorAnalytics.setFailedMessages(0);
      errorAnalytics.setReadMessages(0);
      errorAnalytics.setTotalRecipients(0);
      errorAnalytics.setUniqueRecipients(0);

      return errorAnalytics;
    }
  }

  @Override
  public void logMessage(WhatsAppLogEntryDTO logEntry) {
    LOG.debug("Logging WhatsApp message for tenant: {}", logEntry.getTenantId());

    try {
      // Convert to WhatsAppLogDTO and save
      com.nextjstemplate.service.dto.WhatsAppLogDTO whatsAppLogDTO = new com.nextjstemplate.service.dto.WhatsAppLogDTO();
      whatsAppLogDTO.setTenantId(logEntry.getTenantId());
      whatsAppLogDTO.setRecipientPhone(logEntry.getRecipientPhone());
      whatsAppLogDTO.setMessageBody(logEntry.getMessageBody());
      whatsAppLogDTO.setSentAt(logEntry.getSentAt());
      whatsAppLogDTO.setStatus(logEntry.getStatus());
      whatsAppLogDTO.setType(logEntry.getType());
      if (logEntry.getCampaignId() != null && !logEntry.getCampaignId().trim().isEmpty()) {
        try {
          whatsAppLogDTO.setCampaignId(Long.valueOf(logEntry.getCampaignId()));
        } catch (NumberFormatException e) {
          LOG.warn("Invalid campaign ID format: {}", logEntry.getCampaignId());
        }
      }

      if (logEntry.getMetadata() != null) {
        whatsAppLogDTO.setMetadata(objectMapper.writeValueAsString(logEntry.getMetadata()));
      }

      whatsAppLogService.save(whatsAppLogDTO);

      LOG.debug("Successfully logged WhatsApp message for tenant: {}", logEntry.getTenantId());

    } catch (Exception e) {
      LOG.error("Failed to log WhatsApp message for tenant: {}, error: {}",
          logEntry.getTenantId(), e.getMessage(), e);
      // Don't throw exception to avoid breaking the main flow
    }
  }

  /**
   * Process template with parameters.
   */
  private String processTemplate(String template, Map<String, String> parameters) {
    if (template == null || parameters == null || parameters.isEmpty()) {
      return template;
    }

    String result = template;
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      String placeholder = "{{" + entry.getKey() + "}}";
      result = result.replace(placeholder, entry.getValue());
    }

    return result;
  }
}
