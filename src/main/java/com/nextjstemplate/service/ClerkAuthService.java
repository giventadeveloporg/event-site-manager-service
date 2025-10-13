package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.RefreshTokenRequest;
import com.nextjstemplate.service.dto.RefreshTokenResponse;
import com.nextjstemplate.service.dto.SignInRequest;
import com.nextjstemplate.service.dto.SignInResponse;
import com.nextjstemplate.service.dto.SignOutRequest;
import com.nextjstemplate.service.dto.SignOutResponse;
import com.nextjstemplate.service.dto.SignUpRequest;
import com.nextjstemplate.service.dto.SignUpResponse;
import com.nextjstemplate.service.dto.SocialSignInRequest;
import com.nextjstemplate.service.dto.SocialSignInResponse;
import com.nextjstemplate.service.dto.TokenValidationRequest;
import com.nextjstemplate.service.dto.TokenValidationResponse;
import java.util.Optional;

/**
 * Service Interface for Clerk authentication operations.
 */
public interface ClerkAuthService {
    /**
     * Register a new user with Clerk and create a user profile in the database.
     *
     * @param request the sign-up request with user details
     * @return the sign-up response with JWT token
     */
    SignUpResponse signUp(SignUpRequest request);

    /**
     * Sign in a user and generate JWT token.
     *
     * @param request the sign-in request with credentials
     * @return the sign-in response with JWT token
     */
    Optional<SignInResponse> signIn(SignInRequest request);

    /**
     * Validate a JWT token.
     *
     * @param request the token validation request
     * @return the validation response with user details
     */
    Optional<TokenValidationResponse> validateToken(TokenValidationRequest request);

    /**
     * Get user by Clerk user ID.
     *
     * @param clerkUserId the Clerk user ID
     * @return user details if found
     */
    Optional<TokenValidationResponse> getUserByClerkId(String clerkUserId);

    /**
     * Generate JWT token for a user.
     *
     * @param clerkUserId the Clerk user ID
     * @param email       the user's email
     * @param tenantId    the tenant ID
     * @return the JWT token
     */
    String generateJwtToken(String clerkUserId, String email, String tenantId);

    /**
     * Refresh an access token.
     *
     * @param request the refresh token request
     * @return the new access token
     */
    Optional<RefreshTokenResponse> refreshToken(RefreshTokenRequest request);

    /**
     * Sign out a user and revoke their session.
     *
     * @param request the sign-out request with session ID
     * @return the sign-out response
     */
    SignOutResponse signOut(SignOutRequest request);

    /**
     * Sign in a user with social provider (Google, GitHub, Facebook, Apple).
     *
     * @param request the social sign-in request with Clerk session token
     * @return the social sign-in response with JWT token
     */
    Optional<SocialSignInResponse> socialSignIn(SocialSignInRequest request);
}
