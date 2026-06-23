package com.eventsitemanager.security;

/**
 * Thread-local storage for tenant context.
 * Stores the current tenant ID for the request thread.
 */
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    private TenantContext() {
        // Private constructor to prevent instantiation
    }

    /**
     * Set the current tenant ID for this thread.
     *
     * @param tenantId the tenant ID
     */
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    /**
     * Get the current tenant ID for this thread.
     *
     * @return the tenant ID, or null if not set
     */
    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    /**
     * Clear the tenant context for this thread.
     * Should be called after request processing is complete.
     */
    public static void clear() {
        currentTenant.remove();
    }

    /**
     * Check if a tenant context is set for this thread.
     *
     * @return true if tenant is set, false otherwise
     */
    public static boolean isSet() {
        return currentTenant.get() != null;
    }
}
