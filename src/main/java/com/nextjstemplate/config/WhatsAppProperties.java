package com.nextjstemplate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for WhatsApp integration.
 */
@Configuration
@ConfigurationProperties(prefix = "whatsapp")
public class WhatsAppProperties {

  private Encryption encryption = new Encryption();
  private Webhook webhook = new Webhook();
  private RateLimit rateLimit = new RateLimit();
  private Message message = new Message();
  private Logging logging = new Logging();

  public Encryption getEncryption() {
    return encryption;
  }

  public void setEncryption(Encryption encryption) {
    this.encryption = encryption;
  }

  public Webhook getWebhook() {
    return webhook;
  }

  public void setWebhook(Webhook webhook) {
    this.webhook = webhook;
  }

  public RateLimit getRateLimit() {
    return rateLimit;
  }

  public void setRateLimit(RateLimit rateLimit) {
    this.rateLimit = rateLimit;
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public Logging getLogging() {
    return logging;
  }

  public void setLogging(Logging logging) {
    this.logging = logging;
  }

  public static class Encryption {
    private String key;
    private String algorithm = "AES/GCM/NoPadding";

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getAlgorithm() {
      return algorithm;
    }

    public void setAlgorithm(String algorithm) {
      this.algorithm = algorithm;
    }
  }

  public static class Webhook {
    private String baseUrl;
    private String encryptionKey;

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    public String getEncryptionKey() {
      return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
      this.encryptionKey = encryptionKey;
    }
  }

  public static class RateLimit {
    private int messagesPerMinute = 20;
    private int bulkMessagesPerHour = 100;
    private int retryAttempts = 3;
    private int retryDelaySeconds = 60;

    public int getMessagesPerMinute() {
      return messagesPerMinute;
    }

    public void setMessagesPerMinute(int messagesPerMinute) {
      this.messagesPerMinute = messagesPerMinute;
    }

    public int getBulkMessagesPerHour() {
      return bulkMessagesPerHour;
    }

    public void setBulkMessagesPerHour(int bulkMessagesPerHour) {
      this.bulkMessagesPerHour = bulkMessagesPerHour;
    }

    public int getRetryAttempts() {
      return retryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
      this.retryAttempts = retryAttempts;
    }

    public int getRetryDelaySeconds() {
      return retryDelaySeconds;
    }

    public void setRetryDelaySeconds(int retryDelaySeconds) {
      this.retryDelaySeconds = retryDelaySeconds;
    }
  }

  public static class Message {
    private int maxRecipientsPerBulk = 1000;
    private boolean templateValidationEnabled = true;
    private boolean deliveryStatusCheckEnabled = true;

    public int getMaxRecipientsPerBulk() {
      return maxRecipientsPerBulk;
    }

    public void setMaxRecipientsPerBulk(int maxRecipientsPerBulk) {
      this.maxRecipientsPerBulk = maxRecipientsPerBulk;
    }

    public boolean isTemplateValidationEnabled() {
      return templateValidationEnabled;
    }

    public void setTemplateValidationEnabled(boolean templateValidationEnabled) {
      this.templateValidationEnabled = templateValidationEnabled;
    }

    public boolean isDeliveryStatusCheckEnabled() {
      return deliveryStatusCheckEnabled;
    }

    public void setDeliveryStatusCheckEnabled(boolean deliveryStatusCheckEnabled) {
      this.deliveryStatusCheckEnabled = deliveryStatusCheckEnabled;
    }
  }

  public static class Logging {
    private boolean enabled = true;
    private String logLevel = "INFO";
    private boolean logMessageContent = false;
    private boolean logPersonalData = false;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getLogLevel() {
      return logLevel;
    }

    public void setLogLevel(String logLevel) {
      this.logLevel = logLevel;
    }

    public boolean isLogMessageContent() {
      return logMessageContent;
    }

    public void setLogMessageContent(boolean logMessageContent) {
      this.logMessageContent = logMessageContent;
    }

    public boolean isLogPersonalData() {
      return logPersonalData;
    }

    public void setLogPersonalData(boolean logPersonalData) {
      this.logPersonalData = logPersonalData;
    }
  }
}
