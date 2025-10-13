package com.nextjstemplate.service;

/**
 * Service Interface for webhook signature verification.
 */
public interface WebhookSignatureService {
    /**
     * Verify Clerk webhook signature.
     *
     * @param payload   the raw webhook payload
     * @param signature the signature from X-Clerk-Signature header
     * @return true if signature is valid
     */
    boolean verifyWebhookSignature(String payload, String signature);

    /**
     * Compute HMAC SHA-256 signature.
     *
     * @param payload the data to sign
     * @param secret  the secret key
     * @return the computed signature
     */
    String computeHmacSha256(String payload, String secret);
}
