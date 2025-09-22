package com.nextjstemplate.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the WhatsApp REST controller.
 */
@AutoConfigureMockMvc
@IntegrationTest
class WhatsAppResourceIT {

  @Autowired
  private MockMvc restWhatsAppMockMvc;

  @Autowired
  private TenantSettingsRepository tenantSettingsRepository;

  @Autowired
  private ObjectMapper objectMapper;

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
    // For now, we'll skip this as it's not essential for the WhatsApp resource test
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
  void testSendMessage() throws Exception {
    TwilioWhatsAppRequestDTO request = new TwilioWhatsAppRequestDTO();
    request.setTenantId("test-tenant-001");
    request.setTo("+1234567890");
    request.setMessageBody("Test message");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.messageId").exists())
        .andExpect(jsonPath("$.status").exists());
  }

  @Test
  @Transactional
  void testSendMessageWithInvalidTenant() throws Exception {
    TwilioWhatsAppRequestDTO request = new TwilioWhatsAppRequestDTO();
    request.setTenantId("invalid-tenant");
    request.setTo("+1234567890");
    request.setMessageBody("Test message");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void testSendBulkMessages() throws Exception {
    TwilioWhatsAppBulkRequestDTO request = new TwilioWhatsAppBulkRequestDTO();
    request.setTenantId("test-tenant-001");
    request.setRecipients(Arrays.asList("+1234567890", "+1234567891"));
    request.setMessageBody("Bulk test message");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-bulk")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.totalSent").exists())
        .andExpect(jsonPath("$.totalFailed").exists())
        .andExpect(jsonPath("$.results").isArray());
  }

  @Test
  @Transactional
  void testCheckDeliveryStatus() throws Exception {
    restWhatsAppMockMvc
        .perform(get("/api/whatsapp/delivery-status/test-message-id")
            .param("tenantId", "test-tenant-001"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.messageId").value("test-message-id"))
        .andExpect(jsonPath("$.status").exists());
  }

  @Test
  @Transactional
  void testTestConnection() throws Exception {
    restWhatsAppMockMvc
        .perform(get("/api/whatsapp/test-connection")
            .param("tenantId", "test-tenant-001"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.success").exists())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.tenantId").value("test-tenant-001"));
  }

  @Test
  @Transactional
  void testGetTemplates() throws Exception {
    restWhatsAppMockMvc
        .perform(get("/api/whatsapp/templates")
            .param("tenantId", "test-tenant-001"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  @Transactional
  void testCreateTemplate() throws Exception {
    TwilioTemplateDTO template = new TwilioTemplateDTO();
    template.setName("Test Template");
    template.setContent("Hello {{name}}, this is a test message!");
    template.setCategory("UTILITY");
    template.setLanguage("en");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/templates")
            .param("tenantId", "test-tenant-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(template)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("Test Template"))
        .andExpect(jsonPath("$.status").value("PENDING"));
  }

  @Test
  @Transactional
  void testGetAnalytics() throws Exception {
    restWhatsAppMockMvc
        .perform(get("/api/whatsapp/analytics")
            .param("tenantId", "test-tenant-001"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.tenantId").value("test-tenant-001"))
        .andExpect(jsonPath("$.totalMessages").exists())
        .andExpect(jsonPath("$.deliveryRate").exists())
        .andExpect(jsonPath("$.readRate").exists());
  }

  @Test
  @Transactional
  void testGetAnalyticsWithDateRange() throws Exception {
    ZonedDateTime fromDate = ZonedDateTime.now().minusDays(7);
    ZonedDateTime toDate = ZonedDateTime.now();

    restWhatsAppMockMvc
        .perform(get("/api/whatsapp/analytics")
            .param("tenantId", "test-tenant-001")
            .param("fromDate", fromDate.toString())
            .param("toDate", toDate.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.tenantId").value("test-tenant-001"))
        .andExpect(jsonPath("$.fromDate").exists())
        .andExpect(jsonPath("$.toDate").exists());
  }

  @Test
  @Transactional
  void testSendMessageValidation() throws Exception {
    // Test missing tenant ID
    TwilioWhatsAppRequestDTO request = new TwilioWhatsAppRequestDTO();
    request.setTo("+1234567890");
    request.setMessageBody("Test message");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    // Test missing recipient
    request.setTenantId("test-tenant-001");
    request.setTo(null);

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    // Test missing message body
    request.setTo("+1234567890");
    request.setMessageBody(null);

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void testSendBulkMessageValidation() throws Exception {
    // Test missing recipients
    TwilioWhatsAppBulkRequestDTO request = new TwilioWhatsAppBulkRequestDTO();
    request.setTenantId("test-tenant-001");
    request.setMessageBody("Bulk test message");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-bulk")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    // Test empty recipients list
    request.setRecipients(Arrays.asList());

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/send-bulk")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void testCreateTemplateValidation() throws Exception {
    // Test missing template name
    TwilioTemplateDTO template = new TwilioTemplateDTO();
    template.setContent("Hello {{name}}, this is a test message!");
    template.setCategory("UTILITY");
    template.setLanguage("en");

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/templates")
            .param("tenantId", "test-tenant-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(template)))
        .andExpect(status().isBadRequest());

    // Test missing template content
    template.setName("Test Template");
    template.setContent(null);

    restWhatsAppMockMvc
        .perform(post("/api/whatsapp/templates")
            .param("tenantId", "test-tenant-001")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(template)))
        .andExpect(status().isBadRequest());
  }
}
