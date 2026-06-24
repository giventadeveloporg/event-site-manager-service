package com.eventsitemanager.web.rest;

import com.eventsitemanager.service.ClerkAuthService;
import com.eventsitemanager.service.dto.*;
import com.eventsitemanager.web.rest.errors.InvalidCredentialsException;
import com.eventsitemanager.web.rest.errors.TokenInvalidException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Clerk authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Clerk Authentication", description = "Endpoints for user authentication with Clerk")
public class ClerkAuthController {

    private final Logger log = LoggerFactory.getLogger(ClerkAuthController.class);

    private final ClerkAuthService clerkAuthService;

    public ClerkAuthController(ClerkAuthService clerkAuthService) {
        this.clerkAuthService = clerkAuthService;
    }

    /**
     * POST /api/auth/sign-up : Register a new user
     *
     * @param request the sign-up request with user details
     * @return the ResponseEntity with status 201 (Created) and the sign-up
     *         response, or with status 400 (Bad Request)
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user", description = "Create a new user account with Clerk and in the database")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "User registered successfully",
                content = @Content(schema = @Schema(implementation = SignUpResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
        }
    )
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        log.info("REST request to sign up user with email: {}", request.getEmail());

        try {
            SignUpResponse response = clerkAuthService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error during sign-up: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * POST /api/auth/sign-in : Authenticate a user
     *
     * @param request the sign-in request with credentials
     * @return the ResponseEntity with status 200 (OK) and the sign-in response, or
     *         with status 401 (Unauthorized)
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Authenticate a user", description = "Sign in with email and password to get a JWT token")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "User authenticated successfully",
                content = @Content(schema = @Schema(implementation = SignInResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        log.info("REST request to sign in user with email: {}", request.getEmail());

        SignInResponse response = clerkAuthService
            .signIn(request)
            .orElseThrow(() -> {
                log.warn("Sign-in failed for email: {}", request.getEmail());
                return new InvalidCredentialsException("Invalid credentials");
            });

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/verify-token : Validate a JWT token
     *
     * @param request the token validation request
     * @return the ResponseEntity with status 200 (OK) and the validation response
     */
    @PostMapping("/verify-token")
    @Operation(summary = "Validate a JWT token", description = "Verify the validity of a JWT token and get user details")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Token validated",
                content = @Content(schema = @Schema(implementation = TokenValidationResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<TokenValidationResponse> verifyToken(@Valid @RequestBody TokenValidationRequest request) {
        log.debug("REST request to verify token");

        TokenValidationResponse response = clerkAuthService
            .validateToken(request)
            .orElseThrow(() -> {
                log.warn("Token validation failed");
                return new TokenInvalidException("Token validation failed");
            });

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/refresh-token : Refresh an access token
     *
     * @param request the refresh token request
     * @return the ResponseEntity with status 200 (OK) and the new access token
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token", description = "Get a new access token using an existing valid token")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Token refreshed successfully",
                content = @Content(schema = @Schema(implementation = RefreshTokenResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.debug("REST request to refresh token");

        RefreshTokenResponse response = clerkAuthService
            .refreshToken(request)
            .orElseThrow(() -> {
                log.warn("Token refresh failed");
                return new TokenInvalidException("Token refresh failed");
            });

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/user : Get current authenticated user details
     *
     * @return the ResponseEntity with status 200 (OK) and user details
     */
    @GetMapping("/user")
    @Operation(summary = "Get current user", description = "Get details of the currently authenticated user")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "User details retrieved successfully",
                content = @Content(schema = @Schema(implementation = TokenValidationResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
        }
    )
    public ResponseEntity<TokenValidationResponse> getCurrentUser() {
        log.debug("REST request to get current user");

        // Get the authenticated user from Spring Security context
        // The ClerkJwtAuthenticationFilter has already validated the token
        TokenValidationResponse response = clerkAuthService
            .getCurrentAuthenticatedUser()
            .orElseThrow(() -> {
                log.warn("No authenticated user found in security context");
                return new TokenInvalidException("User not authenticated");
            });

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/sign-out : Sign out a user and revoke session
     *
     * @param request the sign-out request with session ID
     * @return the ResponseEntity with status 200 (OK) and sign-out response
     */
    @PostMapping("/sign-out")
    @Operation(summary = "Sign out user", description = "Revoke user session in Clerk and clear security context")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "User signed out successfully",
                content = @Content(schema = @Schema(implementation = SignOutResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<SignOutResponse> signOut(@Valid @RequestBody SignOutRequest request) {
        log.info("REST request to sign out session: {}", request.getSessionId());

        SignOutResponse response = clerkAuthService.signOut(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/sign-in/social : Authenticate with social provider
     *
     * @param request the social sign-in request with Clerk session token
     * @return the ResponseEntity with status 200 (OK) and social sign-in response
     */
    @PostMapping("/sign-in/social")
    @Operation(summary = "Authenticate with social provider", description = "Sign in using Google, GitHub, Facebook, Apple, etc.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "User authenticated successfully",
                content = @Content(schema = @Schema(implementation = SocialSignInResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid session token"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
        }
    )
    public ResponseEntity<SocialSignInResponse> socialSignIn(@Valid @RequestBody SocialSignInRequest request) {
        log.info("REST request for social sign-in with tenant: {} and provider: {}", request.getTenantId(), request.getProvider());

        SocialSignInResponse response = clerkAuthService
            .socialSignIn(request)
            .orElseThrow(() -> {
                log.warn("Social sign-in failed");
                return new InvalidCredentialsException("Social sign-in failed");
            });

        return ResponseEntity.ok(response);
    }
}
