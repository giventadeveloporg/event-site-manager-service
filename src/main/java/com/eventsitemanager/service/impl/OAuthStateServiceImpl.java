package com.eventsitemanager.service.impl;

import com.eventsitemanager.service.OAuthStateService;
import com.eventsitemanager.service.dto.OAuthStateData;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing OAuth state tokens.
 * Uses in-memory storage with automatic cleanup.
 */
@Service
public class OAuthStateServiceImpl implements OAuthStateService {

    private final Logger log = LoggerFactory.getLogger(OAuthStateServiceImpl.class);

    private final Map<String, OAuthStateData> stateStore = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generate a cryptographically secure random state token.
     */
    private String generateStateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Override
    public OAuthStateData createStateToken(String provider, String tenantId, String redirectUrl) {
        log.debug("Creating OAuth state token for provider: {}, tenant: {}", provider, tenantId);

        String stateToken = generateStateToken();
        OAuthStateData stateData = new OAuthStateData(stateToken, provider, tenantId, redirectUrl);

        stateStore.put(stateToken, stateData);

        log.debug("Created OAuth state token: {}", stateToken);
        return stateData;
    }

    @Override
    public Optional<OAuthStateData> validateAndConsumeStateToken(String stateToken) {
        log.debug("Validating OAuth state token");

        if (stateToken == null || stateToken.isEmpty()) {
            log.warn("State token is null or empty");
            return Optional.empty();
        }

        // Retrieve and remove state data (consume token - one-time use)
        OAuthStateData stateData = stateStore.remove(stateToken);

        if (stateData == null) {
            log.warn("State token not found or already consumed: {}", stateToken);
            return Optional.empty();
        }

        // Check if expired
        if (stateData.isExpired()) {
            log.warn("State token expired: {}", stateToken);
            return Optional.empty();
        }

        log.debug("State token validated successfully for provider: {}, tenant: {}", stateData.getProvider(), stateData.getTenantId());
        return Optional.of(stateData);
    }

    @Override
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        int removedCount = 0;

        for (Map.Entry<String, OAuthStateData> entry : stateStore.entrySet()) {
            if (entry.getValue().getExpiresAt().isBefore(now)) {
                stateStore.remove(entry.getKey());
                removedCount++;
            }
        }

        if (removedCount > 0) {
            log.debug("Cleaned up {} expired OAuth state tokens", removedCount);
        }
    }
}
