package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for rate limit exceeded (AUTH_008).
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class ClerkRateLimitException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public ClerkRateLimitException(String message) {
        super(ClerkErrorConstants.AUTH_008, message != null ? message : "Rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS);
    }

    public ClerkRateLimitException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_008, message != null ? message : "Rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS, cause);
    }
}
