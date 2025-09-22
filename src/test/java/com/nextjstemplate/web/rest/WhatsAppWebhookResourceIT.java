package com.nextjstemplate.web.rest;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the WhatsApp Webhook REST controller.
 */
@AutoConfigureMockMvc
@IntegrationTest
class WhatsAppWebhookResourceIT {

  @Autowired
  private MockMvc restWhatsAppWebhookMockMvc;

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
    // For now, we'll skip this as it's not essential for the WhatsApp webhook test
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
  void testHandleDeliveryStatusWebhook() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/delivery-status")
            .param("tenantId", "test-tenant-001")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "delivered")
            .param("To", "+1234567890")
            .param("From", "+1234567890")
            .param("EventType", "delivered")
            .header("X-Twilio-Signature", "test-signature"))
        .andExpect(status().isOk())
        .andExpect(content().string("Webhook processed successfully"));
  }

  @Test
  @Transactional
  void testHandleMessageStatusWebhook() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/message-status")
            .param("tenantId", "test-tenant-001")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "sent")
            .param("To", "+1234567890")
            .param("From", "+1234567890")
            .param("EventType", "sent")
            .header("X-Twilio-Signature", "test-signature"))
        .andExpect(status().isOk())
        .andExpect(content().string("Webhook processed successfully"));
  }

  @Test
  @Transactional
  void testHandleWebhookWithMissingTenantId() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/delivery-status")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "delivered")
            .header("X-Twilio-Signature", "test-signature"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Tenant ID is required"));
  }

  @Test
  @Transactional
  void testHandleWebhookWithInvalidTenantId() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/delivery-status")
            .param("tenantId", "invalid-tenant")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "delivered")
            .header("X-Twilio-Signature", "test-signature"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Webhook not configured for tenant"));
  }

  @Test
  @Transactional
  void testHandleWebhookWithMissingSignature() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/delivery-status")
            .param("tenantId", "test-tenant-001")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "delivered"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid webhook signature"));
  }

  @Test
  @Transactional
  void testHandleWebhookWithFailedMessage() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/delivery-status")
            .param("tenantId", "test-tenant-001")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "failed")
            .param("ErrorCode", "30008")
            .param("ErrorMessage", "Unknown error")
            .param("To", "+1234567890")
            .param("From", "+1234567890")
            .param("EventType", "failed")
            .header("X-Twilio-Signature", "test-signature"))
        .andExpect(status().isOk())
        .andExpect(content().string("Webhook processed successfully"));
  }

  @Test
  @Transactional
  void testHandleWebhookWithReadStatus() throws Exception {
    restWhatsAppWebhookMockMvc
        .perform(post("/api/whatsapp/webhook/delivery-status")
            .param("tenantId", "test-tenant-001")
            .param("MessageSid", "test-message-id")
            .param("MessageStatus", "read")
            .param("To", "+1234567890")
            .param("From", "+1234567890")
            .param("EventType", "read")
            .header("X-Twilio-Signature", "test-signature"))
        .andExpect(status().isOk())
        .andExpect(content().string("Webhook processed successfully"));
  }
}
