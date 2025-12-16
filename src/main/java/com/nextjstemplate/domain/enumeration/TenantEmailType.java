package com.nextjstemplate.domain.enumeration;

/**
 * The TenantEmailType enumeration.
 * Must match PostgreSQL enum: tenant_email_type
 */
public enum TenantEmailType {
    INFO,
    SALES,
    TICKETS,
    CONTACT,
    SUPPORT,
    MARKETING,
    NOREPLY,
    ADMIN,
}
