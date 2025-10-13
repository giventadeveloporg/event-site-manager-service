package com.nextjstemplate.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for social provider errors (AUTH_010).
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class SocialProviderException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public SocialProviderException(String message) {
        super(ClerkErrorConstants.AUTH_010, message != null ? message : "Social provider error", HttpStatus.BAD_GATEWAY);
    }

    public SocialProviderException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_010, message != null ? message : "Social provider error", HttpStatus.BAD_GATEWAY, cause);
    }
}
