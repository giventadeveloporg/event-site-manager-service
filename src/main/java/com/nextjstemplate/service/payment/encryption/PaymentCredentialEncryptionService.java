package com.nextjstemplate.service.payment.encryption;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for encrypting/decrypting payment provider credentials using AES-256-GCM.
 */
@Service
public class PaymentCredentialEncryptionService {

    private static final Logger log = LoggerFactory.getLogger(PaymentCredentialEncryptionService.class);
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    @Value("${application.payment.encryption.key:}")
    private String encryptionKeyBase64;

    private SecretKey getSecretKey() {
        try {
            if (encryptionKeyBase64 == null || encryptionKeyBase64.isEmpty()) {
                throw new IllegalStateException("Payment encryption key not configured. Set application.payment.encryption.key");
            }
            byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            log.error("Failed to initialize encryption key", e);
            throw new IllegalStateException("Failed to initialize payment encryption key", e);
        }
    }

    /**
     * Encrypts a credential value.
     *
     * @param plaintext The plaintext credential to encrypt
     * @return Base64-encoded encrypted value (IV + ciphertext)
     */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return null;
        }

        try {
            SecretKey key = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            // Generate IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Combine IV + ciphertext
            byte[] encrypted = new byte[GCM_IV_LENGTH + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, GCM_IV_LENGTH);
            System.arraycopy(ciphertext, 0, encrypted, GCM_IV_LENGTH, ciphertext.length);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Failed to encrypt credential", e);
            throw new RuntimeException("Failed to encrypt credential", e);
        }
    }

    /**
     * Decrypts a credential value.
     *
     * @param encryptedBase64 Base64-encoded encrypted value (IV + ciphertext)
     * @return Decrypted plaintext credential
     */
    public String decrypt(String encryptedBase64) {
        if (encryptedBase64 == null || encryptedBase64.isEmpty()) {
            return null;
        }

        try {
            SecretKey key = getSecretKey();
            byte[] encrypted = Base64.getDecoder().decode(encryptedBase64);

            // Extract IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encrypted, 0, iv, 0, GCM_IV_LENGTH);

            // Extract ciphertext
            byte[] ciphertext = new byte[encrypted.length - GCM_IV_LENGTH];
            System.arraycopy(encrypted, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to decrypt credential", e);
            throw new RuntimeException("Failed to decrypt credential", e);
        }
    }

    /**
     * Generates a new encryption key (for initial setup).
     * This should be run once and the key stored securely.
     *
     * @return Base64-encoded encryption key
     */
    public static String generateEncryptionKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(KEY_LENGTH);
            SecretKey key = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate encryption key", e);
        }
    }
}
