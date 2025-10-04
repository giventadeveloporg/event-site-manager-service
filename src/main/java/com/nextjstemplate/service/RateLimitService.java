package com.nextjstemplate.service;

import com.nextjstemplate.service.exception.WhatsAppRateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service for handling rate limiting for WhatsApp API calls.
 */
@Service
public class RateLimitService {

  private static final Logger LOG = LoggerFactory.getLogger(RateLimitService.class);

  private final int messagesPerMinute;
  private final int bulkMessagesPerHour;
  private final int retryDelaySeconds;
  private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

  public RateLimitService(
      @Value("${whatsapp.rate-limit.messages-per-minute:20}") int messagesPerMinute,
      @Value("${whatsapp.rate-limit.bulk-messages-per-hour:100}") int bulkMessagesPerHour,
      @Value("${whatsapp.rate-limit.retry-delay-seconds:60}") int retryDelaySeconds) {
    this.messagesPerMinute = messagesPerMinute;
    this.bulkMessagesPerHour = bulkMessagesPerHour;
    this.retryDelaySeconds = retryDelaySeconds;
  }

  /**
   * Check if a tenant can send a message based on rate limits.
   *
   * @param tenantId The tenant ID
   * @param isBulk   Whether this is a bulk message operation
   * @throws WhatsAppRateLimitException if rate limit is exceeded
   */
  public void checkRateLimit(String tenantId, boolean isBulk) {
    String key = tenantId + (isBulk ? "_bulk" : "_single");
    RateLimitInfo rateLimitInfo = rateLimitMap.computeIfAbsent(key, k -> new RateLimitInfo());

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime windowStart = now.minusMinutes(1);

    // Clean up old entries
    rateLimitInfo.cleanupOldEntries(windowStart);

    int limit = isBulk ? bulkMessagesPerHour : messagesPerMinute;

    if (rateLimitInfo.getMessageCount() >= limit) {
      LOG.warn("Rate limit exceeded for tenant: {}, isBulk: {}, limit: {}",
          tenantId, isBulk, limit);
      throw new WhatsAppRateLimitException(
          "Rate limit exceeded. Try again later.",
          retryDelaySeconds);
    }

    rateLimitInfo.addMessage(now);
    LOG.debug("Rate limit check passed for tenant: {}, isBulk: {}, count: {}/{}",
        tenantId, isBulk, rateLimitInfo.getMessageCount(), limit);
  }

  /**
   * Get the remaining message count for a tenant.
   *
   * @param tenantId The tenant ID
   * @param isBulk   Whether this is a bulk message operation
   * @return The remaining message count
   */
  public int getRemainingMessageCount(String tenantId, boolean isBulk) {
    String key = tenantId + (isBulk ? "_bulk" : "_single");
    RateLimitInfo rateLimitInfo = rateLimitMap.get(key);

    if (rateLimitInfo == null) {
      return isBulk ? bulkMessagesPerHour : messagesPerMinute;
    }

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime windowStart = now.minusMinutes(1);
    rateLimitInfo.cleanupOldEntries(windowStart);

    int limit = isBulk ? bulkMessagesPerHour : messagesPerMinute;

    return Math.max(0, limit - rateLimitInfo.getMessageCount());
  }

  /**
   * Reset rate limit for a tenant.
   *
   * @param tenantId The tenant ID
   */
  public void resetRateLimit(String tenantId) {
    rateLimitMap.remove(tenantId + "_single");
    rateLimitMap.remove(tenantId + "_bulk");
    LOG.info("Rate limit reset for tenant: {}", tenantId);
  }

  /**
   * Rate limit information for a tenant.
   */
  private static class RateLimitInfo {
    private final AtomicInteger messageCount = new AtomicInteger(0);
    private LocalDateTime lastReset = LocalDateTime.now();

    public void addMessage(LocalDateTime timestamp) {
      messageCount.incrementAndGet();
    }

    public int getMessageCount() {
      return messageCount.get();
    }

    public void cleanupOldEntries(LocalDateTime windowStart) {
      if (lastReset.isBefore(windowStart)) {
        messageCount.set(0);
        lastReset = LocalDateTime.now();
      }
    }
  }
}
