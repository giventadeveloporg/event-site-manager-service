package com.nextjstemplate.service;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for the TwilioWhatsAppService.
 */
@IntegrationTest
class TwilioWhatsAppServiceIT {

  @Autowired
  private TwilioWhatsAppService twilioWhatsAppService;

  @Autowired
  private TenantSettingsRepository tenantSettingsRepository;

  private TenantSettings tenantSettings;

  @BeforeEach
  public void initTest() {
    tenantSettings = createTenantSettings();
    tenantSettingsRepository.saveAndFlush(tenantSettings);
  }

  private TenantSettings createTenantSettings() {
    TenantSettings settings = new TenantSettings();
    settings.setTenantId("test-tenant-001");
    // Note: tenantOrganization relationship would need to be set up properly in a
    // real test
    // For now, we'll skip this as it's not essential for the Twilio WhatsApp
    // service test
    settings.setEnableWhatsappIntegration(true);
    settings.setTwilioAccountSid("test-account-sid");
    settings.setTwilioAuthToken("test-auth-token");
    settings.setTwilioWhatsappFrom("+1234567890");
    settings.setWhatsappWebhookUrl("https://test.com/webhook");
    settings.setWhatsappWebhookToken("test-webhook-token");
    return settings;
  }

  @Test
  @Transactional
  void testGetTenantCredentials() {
    TwilioCredentialsDTO credentials = twilioWhatsAppService.getTenantCredentials("test-tenant-001");

    assertThat(credentials).isNotNull();
    assertThat(credentials.getAccountSid()).isEqualTo("test-account-sid");
    assertThat(credentials.getAuthToken()).isEqualTo("test-auth-token");
    assertThat(credentials.getWhatsappFrom()).isEqualTo("+1234567890");
    assertThat(credentials.getWebhookUrl()).isEqualTo("https://test.com/webhook");
    assertThat(credentials.getWebhookToken()).isEqualTo("test-webhook-token");
  }

  @Test
  @Transactional
  void testGetTenantCredentialsWithInvalidTenant() {
    assertThatThrownBy(() -> twilioWhatsAppService.getTenantCredentials("invalid-tenant"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Tenant not found");
  }

  @Test
  @Transactional
  void testGetTenantCredentialsWithWhatsAppDisabled() {
    tenantSettings.setEnableWhatsappIntegration(false);
    tenantSettingsRepository.saveAndFlush(tenantSettings);

    assertThatThrownBy(() -> twilioWhatsAppService.getTenantCredentials("test-tenant-001"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("WhatsApp integration not enabled");
  }

  @Test
  @Transactional
  void testGetTenantCredentialsWithMissingCredentials() {
    tenantSettings.setTwilioAccountSid(null);
    tenantSettingsRepository.saveAndFlush(tenantSettings);

    assertThatThrownBy(() -> twilioWhatsAppService.getTenantCredentials("test-tenant-001"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Twilio credentials not configured");
  }

  @Test
  @Transactional
  void testEncryptAndStoreCredentials() {
    TwilioCredentialsDTO credentials = new TwilioCredentialsDTO();
    credentials.setAccountSid("new-account-sid");
    credentials.setAuthToken("new-auth-token");
    credentials.setWhatsappFrom("+1987654321");
    credentials.setWebhookUrl("https://new-webhook.com");
    credentials.setWebhookToken("new-webhook-token");

    twilioWhatsAppService.encryptAndStoreCredentials("test-tenant-001", credentials);

    TenantSettings updatedSettings = tenantSettingsRepository.findByTenantId("test-tenant-001").orElse(null);
    assertThat(updatedSettings).isNotNull();
    assertThat(updatedSettings.getTwilioAccountSid()).isEqualTo("new-account-sid");
    assertThat(updatedSettings.getTwilioAuthToken()).isEqualTo("new-auth-token");
    assertThat(updatedSettings.getTwilioWhatsappFrom()).isEqualTo("+1987654321");
    assertThat(updatedSettings.getWhatsappWebhookUrl()).isEqualTo("https://new-webhook.com");
    assertThat(updatedSettings.getWhatsappWebhookToken()).isEqualTo("new-webhook-token");
    assertThat(updatedSettings.getEnableWhatsappIntegration()).isTrue();
  }

  @Test
  @Transactional
  void testGetApprovedTemplates() {
    List<TwilioTemplateDTO> templates = twilioWhatsAppService.getApprovedTemplates("test-tenant-001");

    assertThat(templates).isNotNull();
    assertThat(templates).hasSize(2);
    assertThat(templates.get(0).getName()).isEqualTo("Ticket Confirmation");
    assertThat(templates.get(1).getName()).isEqualTo("Event Reminder");
  }

  @Test
  @Transactional
  void testCreateTemplate() {
    TwilioTemplateDTO template = new TwilioTemplateDTO();
    template.setName("Test Template");
    template.setContent("Hello {{name}}, this is a test message!");
    template.setCategory("UTILITY");
    template.setLanguage("en");

    TwilioTemplateDTO createdTemplate = twilioWhatsAppService.createTemplate("test-tenant-001", template);

    assertThat(createdTemplate).isNotNull();
    assertThat(createdTemplate.getId()).isNotNull();
    assertThat(createdTemplate.getName()).isEqualTo("Test Template");
    assertThat(createdTemplate.getContent()).isEqualTo("Hello {{name}}, this is a test message!");
    assertThat(createdTemplate.getCategory()).isEqualTo("UTILITY");
    assertThat(createdTemplate.getLanguage()).isEqualTo("en");
    assertThat(createdTemplate.getStatus()).isEqualTo("PENDING");
    assertThat(createdTemplate.getCreatedAt()).isNotNull();
    assertThat(createdTemplate.getUpdatedAt()).isNotNull();
    assertThat(createdTemplate.getTenantId()).isEqualTo("test-tenant-001");
  }

  @Test
  @Transactional
  void testGetAnalytics() {
    WhatsAppAnalyticsDTO analytics = twilioWhatsAppService.getAnalytics("test-tenant-001", null, null);

    assertThat(analytics).isNotNull();
    assertThat(analytics.getTenantId()).isEqualTo("test-tenant-001");
    assertThat(analytics.getTotalMessages()).isEqualTo(150);
    assertThat(analytics.getSentMessages()).isEqualTo(145);
    assertThat(analytics.getDeliveredMessages()).isEqualTo(140);
    assertThat(analytics.getFailedMessages()).isEqualTo(5);
    assertThat(analytics.getReadMessages()).isEqualTo(120);
    assertThat(analytics.getTotalRecipients()).isEqualTo(100);
    assertThat(analytics.getUniqueRecipients()).isEqualTo(95);
    assertThat(analytics.getDeliveryRate()).isEqualTo(93.33, org.assertj.core.data.Offset.offset(0.01));
    assertThat(analytics.getReadRate()).isEqualTo(80.0, org.assertj.core.data.Offset.offset(0.01));
  }

  @Test
  @Transactional
  void testGetAnalyticsWithDateRange() {
    ZonedDateTime fromDate = ZonedDateTime.now().minusDays(7);
    ZonedDateTime toDate = ZonedDateTime.now();

    WhatsAppAnalyticsDTO analytics = twilioWhatsAppService.getAnalytics("test-tenant-001", fromDate, toDate);

    assertThat(analytics).isNotNull();
    assertThat(analytics.getTenantId()).isEqualTo("test-tenant-001");
    assertThat(analytics.getFromDate()).isEqualTo(fromDate);
    assertThat(analytics.getToDate()).isEqualTo(toDate);
  }

  @Test
  @Transactional
  void testHandleWebhook() {
    java.util.Map<String, Object> payload = new java.util.HashMap<>();
    payload.put("MessageSid", "test-message-id");
    payload.put("MessageStatus", "delivered");

    java.util.Map<String, Object> result = twilioWhatsAppService.handleWebhook("test-tenant-001", payload);

    assertThat(result).isNotNull();
    assertThat(result.get("status")).isEqualTo("processed");
    assertThat(result.get("messageId")).isEqualTo("test-message-id");
    assertThat(result.get("timestamp")).isNotNull();
  }

  @Test
  @Transactional
  void testHandleWebhookWithError() {
    java.util.Map<String, Object> payload = new java.util.HashMap<>();
    payload.put("MessageSid", "test-message-id");
    payload.put("MessageStatus", "failed");

    java.util.Map<String, Object> result = twilioWhatsAppService.handleWebhook("test-tenant-001", payload);

    assertThat(result).isNotNull();
    assertThat(result.get("status")).isEqualTo("processed");
    assertThat(result.get("messageId")).isEqualTo("test-message-id");
  }
}
