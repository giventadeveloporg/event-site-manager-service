package com.eventsitemanager.service.payment.webhook;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for handling rate limiting for Givebutter webhook endpoints.
 */
@Service
public class GivebutterWebhookRateLimitService {

    private static final Logger log = LoggerFactory.getLogger(GivebutterWebhookRateLimitService.class);

    private final int requestsPerMinute;
    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();

    public GivebutterWebhookRateLimitService(@Value("${givebutter.webhook.rate-limit.requests-per-minute:60}") int requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }

    /**
     * Check if a request from an IP address is within rate limits.
     *
     * @param ipAddress The IP address of the request
     * @throws RateLimitExceededException if rate limit is exceeded
     */
    public void checkRateLimit(String ipAddress) throws RateLimitExceededException {
        RateLimitInfo rateLimitInfo = rateLimitMap.computeIfAbsent(ipAddress, k -> new RateLimitInfo());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(1);

        // Clean up old entries
        rateLimitInfo.cleanupOldEntries(windowStart);

        if (rateLimitInfo.getRequestCount() >= requestsPerMinute) {
            log.warn("Rate limit exceeded for IP: {}, requests: {}/{}", ipAddress, rateLimitInfo.getRequestCount(), requestsPerMinute);
            throw new RateLimitExceededException("Rate limit exceeded. Too many requests from this IP.");
        }

        rateLimitInfo.addRequest(now);
        log.debug("Rate limit check passed for IP: {}, count: {}/{}", ipAddress, rateLimitInfo.getRequestCount(), requestsPerMinute);
    }

    /**
     * Get the remaining request count for an IP address.
     *
     * @param ipAddress The IP address
     * @return The remaining request count
     */
    public int getRemainingRequestCount(String ipAddress) {
        RateLimitInfo rateLimitInfo = rateLimitMap.get(ipAddress);

        if (rateLimitInfo == null) {
            return requestsPerMinute;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.minusMinutes(1);
        rateLimitInfo.cleanupOldEntries(windowStart);

        return Math.max(0, requestsPerMinute - rateLimitInfo.getRequestCount());
    }

    /**
     * Reset rate limit for an IP address.
     *
     * @param ipAddress The IP address
     */
    public void resetRateLimit(String ipAddress) {
        rateLimitMap.remove(ipAddress);
        log.debug("Rate limit reset for IP: {}", ipAddress);
    }

    /**
     * Inner class to track rate limit information.
     */
    private static class RateLimitInfo {

        private final AtomicInteger requestCount = new AtomicInteger(0);
        private LocalDateTime windowStart;

        public void addRequest(LocalDateTime timestamp) {
            if (windowStart == null || timestamp.isAfter(windowStart.plusMinutes(1))) {
                windowStart = timestamp;
                requestCount.set(1);
            } else {
                requestCount.incrementAndGet();
            }
        }

        public int getRequestCount() {
            return requestCount.get();
        }

        public void cleanupOldEntries(LocalDateTime windowStart) {
            if (this.windowStart != null && this.windowStart.isBefore(windowStart)) {
                this.windowStart = null;
                requestCount.set(0);
            }
        }
    }

    /**
     * Exception thrown when rate limit is exceeded.
     */
    public static class RateLimitExceededException extends Exception {

        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
