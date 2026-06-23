package com.eventsitemanager.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid webhook signature (AUTH_009).
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WebhookSignatureException extends ClerkAuthenticationException {

    private static final long serialVersionUID = 1L;

    public WebhookSignatureException(String message) {
        super(ClerkErrorConstants.AUTH_009, message != null ? message : "Webhook signature invalid", HttpStatus.UNAUTHORIZED);
    }

    public WebhookSignatureException(String message, Throwable cause) {
        super(ClerkErrorConstants.AUTH_009, message != null ? message : "Webhook signature invalid", HttpStatus.UNAUTHORIZED, cause);
    }
}
