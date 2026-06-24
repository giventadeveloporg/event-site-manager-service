package com.eventsitemanager.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Standalone utility to encrypt Stripe credentials for database insertion.
 * This can be run without Spring Boot context.
 *
 * Usage:
 *   java StripeCredentialEncryptor <encryption-key-base64> <plaintext>
 */
public class StripeCredentialEncryptor {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java StripeCredentialEncryptor <encryption-key-base64> <plaintext>");
            System.exit(1);
        }

        String encryptionKeyBase64 = args[0];
        String plaintext = args[1];

        try {
            String encrypted = encrypt(encryptionKeyBase64, plaintext);
            System.out.println(encrypted);
        } catch (Exception e) {
            System.err.println("Error encrypting: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Encrypts a credential value using AES-256-GCM.
     *
     * @param encryptionKeyBase64 Base64-encoded AES-256 encryption key
     * @param plaintext The plaintext credential to encrypt
     * @return Base64-encoded encrypted value (IV + ciphertext)
     */
    public static String encrypt(String encryptionKeyBase64, String plaintext) throws Exception {
        if (plaintext == null || plaintext.isEmpty()) {
            return null;
        }

        byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
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
    }
}
