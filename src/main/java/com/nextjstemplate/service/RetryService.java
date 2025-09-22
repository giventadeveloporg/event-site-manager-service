package com.nextjstemplate.service;

import com.nextjstemplate.config.WhatsAppProperties;
import com.nextjstemplate.service.exception.WhatsAppRateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * Service for handling retry logic for WhatsApp API calls.
 */
@Service
public class RetryService {

  private static final Logger LOG = LoggerFactory.getLogger(RetryService.class);

  private final WhatsAppProperties whatsAppProperties;

  public RetryService(WhatsAppProperties whatsAppProperties) {
    this.whatsAppProperties = whatsAppProperties;
  }

  /**
   * Execute a supplier with retry logic.
   *
   * @param supplier      The supplier to execute
   * @param operationName The name of the operation for logging
   * @param <T>           The return type
   * @return The result of the supplier
   * @throws Exception if all retry attempts fail
   */
  public <T> T executeWithRetry(Supplier<T> supplier, String operationName) throws Exception {
    int maxAttempts = whatsAppProperties.getRateLimit().getRetryAttempts();
    int retryDelaySeconds = whatsAppProperties.getRateLimit().getRetryDelaySeconds();

    Exception lastException = null;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        LOG.debug("Executing {} (attempt {}/{})", operationName, attempt, maxAttempts);
        return supplier.get();

      } catch (WhatsAppRateLimitException e) {
        lastException = e;
        LOG.warn("Rate limit hit for {} (attempt {}/{}): {}",
            operationName, attempt, maxAttempts, e.getMessage());

        if (attempt < maxAttempts) {
          int delaySeconds = e.getRetryAfterSeconds() > 0 ? e.getRetryAfterSeconds() : retryDelaySeconds;
          LOG.info("Waiting {} seconds before retry for {}", delaySeconds, operationName);
          sleep(delaySeconds * 1000);
        }

      } catch (Exception e) {
        lastException = e;
        LOG.error("Error executing {} (attempt {}/{}): {}",
            operationName, attempt, maxAttempts, e.getMessage(), e);

        if (attempt < maxAttempts) {
          // For non-rate-limit errors, use exponential backoff
          int delaySeconds = retryDelaySeconds * (int) Math.pow(2, attempt - 1);
          LOG.info("Waiting {} seconds before retry for {}", delaySeconds, operationName);
          sleep(delaySeconds * 1000);
        }
      }
    }

    LOG.error("All retry attempts failed for {}", operationName);
    throw lastException;
  }

  /**
   * Execute a supplier with retry logic for bulk operations.
   *
   * @param supplier      The supplier to execute
   * @param operationName The name of the operation for logging
   * @param <T>           The return type
   * @return The result of the supplier
   * @throws Exception if all retry attempts fail
   */
  public <T> T executeBulkWithRetry(Supplier<T> supplier, String operationName) throws Exception {
    int maxAttempts = whatsAppProperties.getRateLimit().getRetryAttempts();
    int retryDelaySeconds = whatsAppProperties.getRateLimit().getRetryDelaySeconds();

    Exception lastException = null;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        LOG.debug("Executing bulk {} (attempt {}/{})", operationName, attempt, maxAttempts);
        return supplier.get();

      } catch (WhatsAppRateLimitException e) {
        lastException = e;
        LOG.warn("Rate limit hit for bulk {} (attempt {}/{}): {}",
            operationName, attempt, maxAttempts, e.getMessage());

        if (attempt < maxAttempts) {
          // For bulk operations, use longer delays
          int delaySeconds = e.getRetryAfterSeconds() > 0 ? e.getRetryAfterSeconds() * 2 : retryDelaySeconds * 2;
          LOG.info("Waiting {} seconds before retry for bulk {}", delaySeconds, operationName);
          sleep(delaySeconds * 1000);
        }

      } catch (Exception e) {
        lastException = e;
        LOG.error("Error executing bulk {} (attempt {}/{}): {}",
            operationName, attempt, maxAttempts, e.getMessage(), e);

        if (attempt < maxAttempts) {
          // For bulk operations, use longer exponential backoff
          int delaySeconds = retryDelaySeconds * 2 * (int) Math.pow(2, attempt - 1);
          LOG.info("Waiting {} seconds before retry for bulk {}", delaySeconds, operationName);
          sleep(delaySeconds * 1000);
        }
      }
    }

    LOG.error("All retry attempts failed for bulk {}", operationName);
    throw lastException;
  }

  /**
   * Sleep for the specified number of milliseconds.
   *
   * @param milliseconds The number of milliseconds to sleep
   */
  private void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOG.warn("Sleep interrupted for retry delay");
    }
  }
}
