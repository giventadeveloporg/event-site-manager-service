package com.nextjstemplate.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EmailMetricsService {

  private final AtomicLong totalEmailsSent = new AtomicLong(0);
  private final AtomicLong totalEmailsFailed = new AtomicLong(0);
  private final ConcurrentHashMap<String, Long> tenantEmailCounts = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, LocalDateTime> lastEmailSent = new ConcurrentHashMap<>();

  public void recordEmailSent(String tenantId) {
    totalEmailsSent.incrementAndGet();
    tenantEmailCounts.merge(tenantId, 1L, Long::sum);
    lastEmailSent.put(tenantId, LocalDateTime.now());
  }

  public void recordEmailFailed(String tenantId) {
    totalEmailsFailed.incrementAndGet();
  }

  public Map<String, Object> getMetrics() {
    Map<String, Object> metrics = new ConcurrentHashMap<>();
    metrics.put("totalEmailsSent", totalEmailsSent.get());
    metrics.put("totalEmailsFailed", totalEmailsFailed.get());
    metrics.put("successRate", calculateSuccessRate());
    metrics.put("tenantEmailCounts", new ConcurrentHashMap<>(tenantEmailCounts));
    metrics.put("lastEmailSent", new ConcurrentHashMap<>(lastEmailSent));
    return metrics;
  }

  private double calculateSuccessRate() {
    long total = totalEmailsSent.get() + totalEmailsFailed.get();
    return total > 0 ? (double) totalEmailsSent.get() / total : 0.0;
  }

  public void resetMetrics() {
    totalEmailsSent.set(0);
    totalEmailsFailed.set(0);
    tenantEmailCounts.clear();
    lastEmailSent.clear();
  }
}