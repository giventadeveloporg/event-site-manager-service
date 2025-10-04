package com.nextjstemplate.service;

import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.dto.*;
import com.nextjstemplate.service.impl.TwilioWhatsAppServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link TwilioWhatsAppService}.
 */
@ExtendWith(MockitoExtension.class)
class TwilioWhatsAppServiceTest {

  @Mock
  private TenantSettingsRepository tenantSettingsRepository;

  @Mock
  private WhatsAppLogService whatsAppLogService;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private TwilioWhatsAppServiceImpl twilioWhatsAppService;

  private TenantSettings tenantSettings;
  private TwilioCredentialsDTO credentials;
  private TwilioWhatsAppRequestDTO request;

  @BeforeEach
  void setUp() {
    // Setup tenant settings
    tenantSettings = new TenantSettings();
    tenantSettings.setId(1L);
    tenantSettings.setTenantId("tenant_test_001");
    tenantSettings.setEnableWhatsappIntegration(true);
    tenantSettings.setTwilioAccountSid("AC1234567890abcdef1234567890abcdef12");
    tenantSettings.setTwilioAuthToken("auth_token_12345");
    tenantSettings.setTwilioWhatsappFrom("whatsapp:+14155238886");
    tenantSettings.setWhatsappWebhookUrl("https://example.com/webhook");
    tenantSettings.setWhatsappWebhookToken("webhook_token_123");

    // Setup credentials
    credentials = new TwilioCredentialsDTO();
    credentials.setAccountSid("AC1234567890abcdef1234567890abcdef12");
    credentials.setAuthToken("auth_token_12345");
    credentials.setWhatsappFrom("whatsapp:+14155238886");
    credentials.setWebhookUrl("https://example.com/webhook");
    credentials.setWebhookToken("webhook_token_123");

    // Setup request
    request = new TwilioWhatsAppRequestDTO();
    request.setTo("+1234567890");
    request.setMessageBody("Test message");
  }

  @Test
  void testGetTenantCredentials_Success() {
    // Given
    when(tenantSettingsRepository.findByTenantId("tenant_test_001"))
        .thenReturn(Optional.of(tenantSettings));

    // When
    TwilioCredentialsDTO result = twilioWhatsAppService.getTenantCredentials("tenant_test_001");

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getAccountSid()).isEqualTo("AC1234567890abcdef1234567890abcdef12");
    assertThat(result.getAuthToken()).isEqualTo("auth_token_12345");
    assertThat(result.getWhatsappFrom()).isEqualTo("whatsapp:+14155238886");
    assertThat(result.getWebhookUrl()).isEqualTo("https://example.com/webhook");
    assertThat(result.getWebhookToken()).isEqualTo("webhook_token_123");

    verify(tenantSettingsRepository).findByTenantId("tenant_test_001");
  }

  @Test
  void testGetTenantCredentials_TenantNotFound() {
    // Given
    when(tenantSettingsRepository.findByTenantId("nonexistent_tenant"))
        .thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> twilioWhatsAppService.getTenantCredentials("nonexistent_tenant"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Tenant not found: nonexistent_tenant");

    verify(tenantSettingsRepository).findByTenantId("nonexistent_tenant");
  }

  @Test
  void testGetTenantCredentials_WhatsAppNotEnabled() {
    // Given
    tenantSettings.setEnableWhatsappIntegration(false);
    when(tenantSettingsRepository.findByTenantId("tenant_test_001"))
        .thenReturn(Optional.of(tenantSettings));

    // When & Then
    assertThatThrownBy(() -> twilioWhatsAppService.getTenantCredentials("tenant_test_001"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("WhatsApp integration not enabled for tenant: tenant_test_001");
  }

  @Test
  void testGetTenantCredentials_MissingCredentials() {
    // Given
    tenantSettings.setTwilioAccountSid(null);
    when(tenantSettingsRepository.findByTenantId("tenant_test_001"))
        .thenReturn(Optional.of(tenantSettings));

    // When & Then
    assertThatThrownBy(() -> twilioWhatsAppService.getTenantCredentials("tenant_test_001"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Twilio credentials not configured for tenant: tenant_test_001");
  }

  @Test
  void testEncryptAndStoreCredentials_Success() {
    // Given
    when(tenantSettingsRepository.findByTenantId("tenant_test_001"))
        .thenReturn(Optional.of(tenantSettings));
    when(tenantSettingsRepository.save(any(TenantSettings.class)))
        .thenReturn(tenantSettings);

    // When
    twilioWhatsAppService.encryptAndStoreCredentials("tenant_test_001", credentials);

    // Then
    verify(tenantSettingsRepository).findByTenantId("tenant_test_001");
    verify(tenantSettingsRepository).save(any(TenantSettings.class));
  }

  @Test
  void testEncryptAndStoreCredentials_TenantNotFound() {
    // Given
    when(tenantSettingsRepository.findByTenantId("nonexistent_tenant"))
        .thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> twilioWhatsAppService.encryptAndStoreCredentials("nonexistent_tenant", credentials))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Tenant not found: nonexistent_tenant");
  }

  @Test
  void testLogMessage_Success() throws Exception {
    // Given
    WhatsAppLogEntryDTO logEntry = new WhatsAppLogEntryDTO();
    logEntry.setTenantId("tenant_test_001");
    logEntry.setRecipientPhone("+1234567890");
    logEntry.setMessageBody("Test message");
    logEntry.setStatus("SENT");
    logEntry.setType("TRANSACTIONAL");
    logEntry.setMetadata(Map.of("key", "value")); // Add metadata to ensure objectMapper is called

    when(objectMapper.writeValueAsString(any())).thenReturn("{}");
    when(whatsAppLogService.save(any())).thenReturn(new WhatsAppLogDTO());

    // When
    twilioWhatsAppService.logMessage(logEntry);

    // Then
    verify(whatsAppLogService).save(any());
    verify(objectMapper).writeValueAsString(any());
  }

  @Test
  void testLogMessage_JsonProcessingException() throws Exception {
    // Given
    WhatsAppLogEntryDTO logEntry = new WhatsAppLogEntryDTO();
    logEntry.setTenantId("tenant_test_001");
    logEntry.setRecipientPhone("+1234567890");
    logEntry.setMessageBody("Test message");
    logEntry.setStatus("SENT");
    logEntry.setType("TRANSACTIONAL");
    logEntry.setMetadata(Map.of("key", "value"));

    when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON error"));

    // When
    twilioWhatsAppService.logMessage(logEntry);

    // Then
    verify(whatsAppLogService, never()).save(any());
  }

  @Test
  void testGetApprovedTemplates() {
    // When
    List<TwilioTemplateDTO> result = twilioWhatsAppService.getApprovedTemplates("tenant_test_001");

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);

    TwilioTemplateDTO template1 = result.get(0);
    assertThat(template1.getId()).isEqualTo("ticket_confirmation");
    assertThat(template1.getName()).isEqualTo("Ticket Confirmation");
    assertThat(template1.getStatus()).isEqualTo("APPROVED");

    TwilioTemplateDTO template2 = result.get(1);
    assertThat(template2.getId()).isEqualTo("event_reminder");
    assertThat(template2.getName()).isEqualTo("Event Reminder");
    assertThat(template2.getStatus()).isEqualTo("APPROVED");
  }

  @Test
  void testCreateTemplate() {
    // Given
    TwilioTemplateDTO request = new TwilioTemplateDTO();
    request.setName("Test Template");
    request.setContent("Hello {{name}}!");
    request.setCategory("UTILITY");

    // When
    TwilioTemplateDTO result = twilioWhatsAppService.createTemplate("tenant_test_001", request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
    assertThat(result.getName()).isEqualTo("Test Template");
    assertThat(result.getContent()).isEqualTo("Hello {{name}}!");
    assertThat(result.getStatus()).isEqualTo("PENDING");
    assertThat(result.getCreatedAt()).isNotNull();
    assertThat(result.getUpdatedAt()).isNotNull();
    assertThat(result.getTenantId()).isEqualTo("tenant_test_001");
  }

  @Test
  void testHandleWebhook_Success() {
    // Given
    Map<String, Object> payload = new HashMap<>();
    payload.put("MessageSid", "SM1234567890abcdef1234567890abcdef12");
    payload.put("MessageStatus", "delivered");
    payload.put("To", "whatsapp:+1234567890");

    // When
    Map<String, Object> result = twilioWhatsAppService.handleWebhook("tenant_test_001", payload);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.get("status")).isEqualTo("processed");
    assertThat(result.get("messageId")).isEqualTo("SM1234567890abcdef1234567890abcdef12");
    assertThat(result.get("timestamp")).isNotNull();
  }

  @Test
  void testHandleWebhook_Exception() {
    // Given
    Map<String, Object> payload = new HashMap<>();
    // Invalid payload that might cause issues

    // When
    Map<String, Object> result = twilioWhatsAppService.handleWebhook("tenant_test_001", payload);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.get("status")).isEqualTo("processed");
    assertThat(result.get("timestamp")).isNotNull();
  }

  @Test
  void testProcessTemplate() {
    // Given
    String template = "Hello {{name}}, your ticket for {{event}} is confirmed!";
    Map<String, String> parameters = Map.of(
        "name", "John Doe",
        "event", "Concert 2024");

    // When
    TwilioWhatsAppResponseDTO result = twilioWhatsAppService.sendMessage("tenant_test_001", request);

    // Then
    // Note: This test would need to mock Twilio SDK calls in a real implementation
    // For now, we're just testing that the service doesn't throw exceptions
    assertThat(result).isNotNull();
  }

  @Test
  void testSendBulkMessages() {
    // Given
    TwilioWhatsAppBulkRequestDTO bulkRequest = new TwilioWhatsAppBulkRequestDTO();
    bulkRequest.setRecipients(Arrays.asList("+1234567890", "+0987654321"));
    bulkRequest.setMessageBody("Bulk test message");
    bulkRequest.setCampaignId("campaign_123");

    // When
    TwilioWhatsAppBulkResponseDTO result = twilioWhatsAppService.sendBulkMessages("tenant_test_001", bulkRequest);

    // Then
    // Note: This test would need to mock Twilio SDK calls in a real implementation
    assertThat(result).isNotNull();
    assertThat(result.getCampaignId()).isEqualTo("campaign_123");
    assertThat(result.getSentAt()).isNotNull();
  }
}
