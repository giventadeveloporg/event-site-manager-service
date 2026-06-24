package com.eventsitemanager.service;

import com.eventsitemanager.properties.BatchJobProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Obtains and caches a JWT for server-to-server calls to the batch-jobs microservice.
 * Uses the same {@code /api/authenticate} flow as {@code event-site-manager-batch-jobs}'s {@code BackendApiService}.
 */
@Component
public class BatchJobServiceAuthTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(BatchJobServiceAuthTokenProvider.class);

    private final BatchJobProperties batchJobProperties;
    private final ObjectMapper objectMapper;
    private final WebClient authWebClient;

    private volatile String cachedToken;
    private volatile Instant tokenExpiry;

    public BatchJobServiceAuthTokenProvider(BatchJobProperties batchJobProperties, ObjectMapper objectMapper) {
        this.batchJobProperties = batchJobProperties;
        this.objectMapper = objectMapper;
        this.authWebClient = WebClient.builder().build();
    }

    public boolean isConfigured() {
        BatchJobProperties.Auth auth = batchJobProperties.getAuth();
        return auth != null && StringUtils.hasText(auth.getUsername()) && StringUtils.hasText(auth.getPassword());
    }

    /**
     * Returns a valid JWT, refreshing from {@code /api/authenticate} when needed.
     */
    public String getTokenBlocking() {
        if (!isConfigured()) {
            return null;
        }
        BatchJobProperties.Auth auth = batchJobProperties.getAuth();
        Instant now = Instant.now();
        if (cachedToken != null && tokenExpiry != null && now.isBefore(tokenExpiry)) {
            return cachedToken;
        }
        synchronized (this) {
            now = Instant.now();
            if (cachedToken != null && tokenExpiry != null && now.isBefore(tokenExpiry)) {
                return cachedToken;
            }
            String base = auth.getBaseUrl() != null ? auth.getBaseUrl().trim() : "http://localhost:8080";
            if (base.endsWith("/")) {
                base = base.substring(0, base.length() - 1);
            }
            String url = base + "/api/authenticate";
            Map<String, Object> body = new HashMap<>();
            body.put("username", auth.getUsername().trim());
            body.put("password", auth.getPassword().trim());
            body.put("rememberMe", true);

            try {
                String responseJson = authWebClient
                    .post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

                if (responseJson == null || responseJson.isEmpty()) {
                    throw new IllegalStateException("Empty response from " + url);
                }
                JsonNode node = objectMapper.readTree(responseJson);
                String token = extractToken(node);
                if (token == null || token.isEmpty()) {
                    throw new IllegalStateException("No JWT in authenticate response");
                }
                cachedToken = token;
                tokenExpiry = Instant.now().plus(50, ChronoUnit.MINUTES);
                log.debug("Cached batch-job service JWT (50 min TTL)");
                return token;
            } catch (WebClientResponseException e) {
                log.error("Batch-job auth failed: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                throw new IllegalStateException("Failed to authenticate for batch jobs: HTTP " + e.getStatusCode(), e);
            } catch (Exception e) {
                log.error("Batch-job auth error calling {}", url, e);
                throw new IllegalStateException("Failed to authenticate for batch jobs: " + e.getMessage(), e);
            }
        }
    }

    public void clearCache() {
        synchronized (this) {
            cachedToken = null;
            tokenExpiry = null;
        }
    }

    private static String extractToken(JsonNode body) {
        if (body.has("id_token")) {
            return body.get("id_token").asText();
        }
        if (body.has("token")) {
            return body.get("token").asText();
        }
        if (body.has("jwt")) {
            return body.get("jwt").asText();
        }
        if (body.has("access_token")) {
            return body.get("access_token").asText();
        }
        return null;
    }
}
