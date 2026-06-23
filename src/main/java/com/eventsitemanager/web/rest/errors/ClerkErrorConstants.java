package com.eventsitemanager.web.rest.errors;

import java.net.URI;

/**
 * Error constants for Clerk authentication.
 */
public final class ClerkErrorConstants {

    public static final String ERR_CLERK_AUTH = "error.clerk.authentication";
    public static final String ERR_INVALID_CREDENTIALS = "error.clerk.invalidCredentials";
    public static final String ERR_TOKEN_EXPIRED = "error.clerk.tokenExpired";
    public static final String ERR_TOKEN_INVALID = "error.clerk.tokenInvalid";
    public static final String ERR_USER_NOT_FOUND = "error.clerk.userNotFound";
    public static final String ERR_EMAIL_EXISTS = "error.clerk.emailExists";
    public static final String ERR_INVALID_TENANT = "error.clerk.invalidTenant";
    public static final String ERR_UNAUTHORIZED = "error.clerk.unauthorized";
    public static final String ERR_RATE_LIMIT = "error.clerk.rateLimit";
    public static final String ERR_WEBHOOK_SIGNATURE = "error.clerk.webhookSignature";
    public static final String ERR_SOCIAL_PROVIDER = "error.clerk.socialProvider";

    public static final URI CLERK_ERROR_TYPE = URI.create("https://api.clerk.com/problem/authentication-error");

    // Error codes from PRD
    public static final String AUTH_001 = "AUTH_001"; // Invalid credentials
    public static final String AUTH_002 = "AUTH_002"; // Token expired
    public static final String AUTH_003 = "AUTH_003"; // Token invalid
    public static final String AUTH_004 = "AUTH_004"; // User not found
    public static final String AUTH_005 = "AUTH_005"; // Email already exists
    public static final String AUTH_006 = "AUTH_006"; // Invalid tenant
    public static final String AUTH_007 = "AUTH_007"; // Unauthorized access
    public static final String AUTH_008 = "AUTH_008"; // Rate limit exceeded
    public static final String AUTH_009 = "AUTH_009"; // Webhook signature invalid
    public static final String AUTH_010 = "AUTH_010"; // Social provider error

    private ClerkErrorConstants() {
        // Utility class
    }
}
