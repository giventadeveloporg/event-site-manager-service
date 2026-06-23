package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.OAuthStateData;
import java.util.Optional;

/**
 * Service interface for managing OAuth state tokens (CSRF protection).
 */
public interface OAuthStateService {
    /**
     * Generate and store a new OAuth state token.
     *
     * @param provider the OAuth provider name
     * @param tenantId the tenant identifier
     * @param redirectUrl the URL to redirect to after authentication
     * @return the generated state data
     */
    OAuthStateData createStateToken(String provider, String tenantId, String redirectUrl);

    /**
     * Validate and retrieve OAuth state data.
     *
     * @param stateToken the state token to validate
     * @return Optional containing state data if valid, empty otherwise
     */
    Optional<OAuthStateData> validateAndConsumeStateToken(String stateToken);

    /**
     * Clean up expired state tokens.
     */
    void cleanupExpiredTokens();
}
