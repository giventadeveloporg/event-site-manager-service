package com.eventsitemanager.service;

import java.util.Locale;

/**
 * Normalisation helpers shared by onboarding submit / approve so duplicate checks compare like with like.
 */
public final class TenantOnboardingSupport {

    public static final String ENTITY_NAME = "tenantOnboardingRequest";

    private TenantOnboardingSupport() {}

    /**
     * Lower-cases and strips scheme, path, port and trailing dot: {@code "https://WWW.Example.com/x"} becomes
     * {@code "www.example.com"}.
     */
    public static String normalizeHostname(String raw) {
        if (raw == null) {
            return null;
        }
        String h = raw.trim().toLowerCase(Locale.ROOT);
        int scheme = h.indexOf("://");
        if (scheme >= 0) {
            h = h.substring(scheme + 3);
        }
        int slash = h.indexOf('/');
        if (slash >= 0) {
            h = h.substring(0, slash);
        }
        int colon = h.indexOf(':');
        if (colon >= 0) {
            h = h.substring(0, colon);
        }
        while (h.endsWith(".")) {
            h = h.substring(0, h.length() - 1);
        }
        return h.isEmpty() ? null : h;
    }

    /** {@code www.example.com} becomes {@code example.com}; other hosts are returned unchanged. */
    public static String stripWww(String hostname) {
        if (hostname == null) {
            return null;
        }
        return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
    }

    public static boolean isValidHostname(String hostname) {
        return (
            hostname != null &&
            hostname.length() <= 253 &&
            hostname.matches("^(?=.{1,253}$)([a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z]{2,63}$")
        );
    }

    public static String normalizeEmail(String raw) {
        if (raw == null) {
            return null;
        }
        String e = raw.trim().toLowerCase(Locale.ROOT);
        return e.isEmpty() ? null : e;
    }

    public static String trimToNull(String raw) {
        if (raw == null) {
            return null;
        }
        String t = raw.trim();
        return t.isEmpty() ? null : t;
    }
}
