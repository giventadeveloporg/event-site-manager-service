package com.nextjstemplate.service.impl;

import com.nextjstemplate.properties.ClerkProperties;
import com.nextjstemplate.service.WebhookSignatureService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of WebhookSignatureService for verifying Clerk webhook
 * signatures.
 */
@Service
public class WebhookSignatureServiceImpl implements WebhookSignatureService {

    private static final Logger log = LoggerFactory.getLogger(WebhookSignatureServiceImpl.class);
    private static final String HMAC_SHA256 = "HmacSHA256";

    private final ClerkProperties clerkProperties;

    public WebhookSignatureServiceImpl(ClerkProperties clerkProperties) {
        this.clerkProperties = clerkProperties;
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        if (payload == null || signature == null) {
            log.warn("Payload or signature is null");
            return false;
        }

        try {
            String webhookSecret = clerkProperties.getWebhookSecret();
            if (webhookSecret == null || webhookSecret.isEmpty()) {
                log.error("Webhook secret is not configured");
                return false;
            }

            // Clerk signature format: "v1,timestamp,signature"
            // We'll verify the signature part
            String[] parts = signature.split(",");
            if (parts.length < 3) {
                log.warn("Invalid signature format");
                return false;
            }

            String providedSignature = parts[2];
            String timestamp = parts[1];

            // Compute expected signature with timestamp
            String signedPayload = timestamp + "." + payload;
            String expectedSignature = computeHmacSha256(signedPayload, webhookSecret);

            // Compare signatures
            boolean isValid = MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                providedSignature.getBytes(StandardCharsets.UTF_8)
            );

            if (!isValid) {
                log.warn("Webhook signature verification failed");
            } else {
                log.debug("Webhook signature verified successfully");
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error verifying webhook signature", e);
            return false;
        }
    }

    @Override
    public String computeHmacSha256(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            log.error("Error computing HMAC SHA-256", e);
            throw new RuntimeException("Failed to compute signature", e);
        }
    }
}
