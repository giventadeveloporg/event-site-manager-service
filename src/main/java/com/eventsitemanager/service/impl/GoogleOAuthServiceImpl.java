package com.eventsitemanager.service.impl;

import com.eventsitemanager.service.GoogleOAuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Implementation of GoogleOAuthService for direct Google OAuth integration.
 * Bypasses Clerk and communicates directly with Google's OAuth 2.0 API.
 *
 * NOTE: This service is DISABLED by default. We now use Clerk SDK for OAuth.
 * To enable, set: google.oauth.enabled=true in application.yml
 */
@Service
@ConditionalOnProperty(name = "google.oauth.enabled", havingValue = "true", matchIfMissing = false)
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuthServiceImpl.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    // Google OAuth endpoints
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${google.oauth.client-secret}")
    private String clientSecret;

    public GoogleOAuthServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder().build();
        log.info("GoogleOAuthService initialized for direct OAuth flow");
    }

    @Override
    public String generateAuthorizationUrl(String redirectUri, String state) {
        log.debug("Generating Google OAuth URL with redirect: {}", redirectUri);

        try {
            StringBuilder url = new StringBuilder(GOOGLE_AUTH_URL);
            url.append("?client_id=").append(URLEncoder.encode(clientId, StandardCharsets.UTF_8));
            url.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8));
            url.append("&response_type=code");
            url.append("&scope=").append(URLEncoder.encode("openid email profile", StandardCharsets.UTF_8));
            url.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8));
            url.append("&access_type=online");
            url.append("&prompt=consent"); // Force account selection

            String authUrl = url.toString();
            log.debug("Generated Google OAuth URL: {}", authUrl);
            return authUrl;
        } catch (Exception e) {
            log.error("Error generating Google OAuth URL", e);
            throw new RuntimeException("Failed to generate OAuth URL: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Map<String, Object>> exchangeCodeForToken(String code, String redirectUri) {
        log.debug("Exchanging authorization code for access token");

        try {
            // Build form data for token exchange
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("code", code);
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("redirect_uri", redirectUri);
            formData.add("grant_type", "authorization_code");

            String response = webClient
                .post()
                .uri(GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> tokenResponse = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});

            log.info("Successfully exchanged code for Google access token");
            log.debug("Token response keys: {}", tokenResponse.keySet());

            return Optional.of(tokenResponse);
        } catch (WebClientResponseException e) {
            log.error("Error exchanging code for token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error exchanging code for token", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> getUserInfo(String accessToken) {
        log.debug("Fetching user info from Google");

        try {
            String response = webClient
                .get()
                .uri(GOOGLE_USERINFO_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

            Map<String, Object> userInfo = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});

            log.info("Successfully fetched user info from Google: {}", userInfo.get("email"));
            log.debug("User info keys: {}", userInfo.keySet());

            return Optional.of(userInfo);
        } catch (WebClientResponseException e) {
            log.error("Error fetching user info: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error fetching user info", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> verifyIdToken(String idToken) {
        log.debug("Verifying and decoding ID token");

        try {
            // Decode ID token JWT (format: header.payload.signature)
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                log.warn("Invalid ID token format");
                return Optional.empty();
            }

            // Decode payload (Base64URL encoded)
            String payload = parts[1];
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            Map<String, Object> claims = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, Object>>() {});

            log.debug("ID token decoded successfully");
            log.debug("ID token claims: {}", claims.keySet());

            // Basic validation
            String iss = (String) claims.get("iss");
            if (!"https://accounts.google.com".equals(iss) && !"accounts.google.com".equals(iss)) {
                log.warn("Invalid issuer in ID token: {}", iss);
                return Optional.empty();
            }

            String aud = (String) claims.get("aud");
            if (!clientId.equals(aud)) {
                log.warn("Invalid audience in ID token: {}", aud);
                return Optional.empty();
            }

            // Check expiration
            Long exp = getLongValue(claims.get("exp"));
            if (exp != null && exp < System.currentTimeMillis() / 1000) {
                log.warn("ID token expired");
                return Optional.empty();
            }

            log.info("ID token verified successfully for email: {}", claims.get("email"));
            return Optional.of(claims);
        } catch (Exception e) {
            log.error("Error verifying ID token", e);
            return Optional.empty();
        }
    }

    /**
     * Helper method to safely convert Number to Long.
     */
    private Long getLongValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
