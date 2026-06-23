package com.eventsitemanager.service;

import java.util.Map;
import java.util.Optional;

/**
 * Service for direct Google OAuth integration (bypassing Clerk).
 * Handles OAuth flow directly with Google's OAuth 2.0 API.
 */
public interface GoogleOAuthService {
    /**
     * Generate Google OAuth authorization URL.
     *
     * @param redirectUri the callback URL after Google authentication
     * @param state the state parameter for CSRF protection
     * @return the Google OAuth authorization URL
     */
    String generateAuthorizationUrl(String redirectUri, String state);

    /**
     * Exchange authorization code for access token.
     *
     * @param code the authorization code from Google
     * @param redirectUri the redirect URI used in authorization (must match)
     * @return Optional containing token response with access_token, id_token, etc.
     */
    Optional<Map<String, Object>> exchangeCodeForToken(String code, String redirectUri);

    /**
     * Get user information from Google using access token.
     *
     * @param accessToken the Google access token
     * @return Optional containing user information (email, name, picture, etc.)
     */
    Optional<Map<String, Object>> getUserInfo(String accessToken);

    /**
     * Verify Google ID token and extract user information.
     *
     * @param idToken the Google ID token from token response
     * @return Optional containing user information from ID token
     */
    Optional<Map<String, Object>> verifyIdToken(String idToken);
}
