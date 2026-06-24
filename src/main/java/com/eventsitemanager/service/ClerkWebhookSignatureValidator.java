package com.eventsitemanager.service;

import com.eventsitemanager.properties.ClerkProperties;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for validating Clerk webhook signatures.
 */
@Service
public class ClerkWebhookSignatureValidator {

    private static final Logger log = LoggerFactory.getLogger(ClerkWebhookSignatureValidator.class);
    private static final String HMAC_SHA256 = "HmacSHA256";

    private final ClerkProperties clerkProperties;

    public ClerkWebhookSignatureValidator(ClerkProperties clerkProperties) {
        this.clerkProperties = clerkProperties;
    }

    /**
     * Verify Clerk webhook signature.
     * Clerk sends signature in svix-signature header format:
     * v1,signature1 v1,signature2
     *
     * @param payload   the raw request body
     * @param signature the signature from X-Clerk-Signature or svix-signature
     *                  header
     * @return true if signature is valid
     */
    public boolean verifySignature(String payload, String signature) {
        if (payload == null || signature == null) {
            log.warn("Payload or signature is null");
            return false;
        }

        try {
            String webhookSecret = clerkProperties.getWebhookSecret();
            if (webhookSecret == null || webhookSecret.isEmpty()) {
                log.error("Clerk webhook secret is not configured");
                return false;
            }

            // Remove "whsec_" prefix from webhook secret if present
            String secret = webhookSecret.startsWith("whsec_") ? webhookSecret.substring(6) : webhookSecret;

            // Clerk/Svix uses base64-encoded secret
            byte[] secretBytes = Base64.getDecoder().decode(secret);

            // Compute HMAC-SHA256
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretBytes, HMAC_SHA256);
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getEncoder().encodeToString(hash);

            // Extract signature(s) from header - format: "v1,sig1 v1,sig2"
            String[] signatureParts = signature.split(" ");
            for (String part : signatureParts) {
                String[] versionAndSig = part.split(",");
                if (versionAndSig.length == 2 && "v1".equals(versionAndSig[0])) {
                    String providedSig = versionAndSig[1];
                    if (MessageDigest.isEqual(expectedSignature.getBytes(), providedSig.getBytes())) {
                        log.debug("Webhook signature verified successfully");
                        return true;
                    }
                }
            }

            log.warn("Webhook signature verification failed");
            return false;
        } catch (Exception e) {
            log.error("Error verifying webhook signature", e);
            return false;
        }
    }

    /**
     * Alternative verification method using simple HMAC comparison.
     *
     * @param payload   the raw request body
     * @param signature the signature to verify
     * @return true if signature is valid
     */
    public boolean verifySimpleSignature(String payload, String signature) {
        try {
            String webhookSecret = clerkProperties.getWebhookSecret();
            if (webhookSecret == null) {
                log.error("Webhook secret not configured");
                return false;
            }

            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = bytesToHex(hash);

            boolean isValid = MessageDigest.isEqual(expectedSignature.getBytes(), signature.getBytes());
            log.debug("Simple signature verification: {}", isValid ? "SUCCESS" : "FAILED");
            return isValid;
        } catch (Exception e) {
            log.error("Error in simple signature verification", e);
            return false;
        }
    }

    /**
     * Convert byte array to hex string.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
