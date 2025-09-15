# Communication Architecture: Email (SES) & WhatsApp (Twilio) for Multi-Tenant Event Management

## 1. Overview

This document outlines the requirements, analysis, and recommended architecture for integrating Amazon SES (email) and Twilio (WhatsApp) into the multi-tenant event management backend. It covers transactional and bulk messaging, per-tenant usage tracking, and reporting/billing needs.

---

## 2. Requirements

- **Transactional Email (SES):**
  - Send confirmation emails (e.g., ticket purchase) to individual users.
- **Bulk Email (SES):**
  - Tenants can send promotional/announcement emails to user groups.
  - Track number of emails sent per tenant per month (for billing/reporting).
- **Transactional WhatsApp (Twilio):**
  - Send confirmation messages (e.g., ticket purchase) to individual users.
- **Bulk WhatsApp (Twilio):**
  - Tenants can send group messages (promotional, transactional, event notifications).
  - Track number of WhatsApp messages sent per tenant per month.
- **Metadata Tracking:**
  - Log: tenant ID, recipient, timestamp, status, type (transactional/bulk), campaign (if any), and provider response.

---

## 3. Off-the-Shelf Solutions: Analysis

- **Email:**
  - _Amazon SES_ is enabled and suitable for transactional and bulk email. For advanced campaign management, consider AWS Pinpoint, but it is more complex to integrate.
  - _SendGrid/Mailgun_ offer analytics/campaigns but require migration and are not multi-tenant aware out-of-the-box.
- **WhatsApp:**
  - _Twilio_ is the industry standard for WhatsApp API. No off-the-shelf solution provides per-tenant analytics; custom tracking is required.
- **Unified Communication Platforms:**
  - _MessageBird, Vonage_ offer APIs for multiple channels but still require custom integration for multi-tenant usage tracking.

**Conclusion:**

- Use SES and Twilio for sending, but implement custom tracking/logging in your own database for analytics, reporting, and billing.

---

## 4. Current JDL/Schema Review

- No existing entities for communication/email/WhatsApp logging or campaign management.
- `TenantSettings` includes flags for enabling/disabling email/WhatsApp, but no tracking.
- No campaign or communication log entities exist.

---

## 5. Proposed Schema (JDL-style)

```jdl
entity EmailLog {
    id Long,
    tenantId String required maxlength(255),
    recipientEmail String required maxlength(255),
    subject String maxlength(255),
    body TextBlob,
    sentAt ZonedDateTime required,
    status String maxlength(50), // SENT, FAILED, etc.
    type String maxlength(50), // TRANSACTIONAL, BULK
    campaignId Long, // nullable, for bulk
    metadata TextBlob // JSON for provider response, etc.
}

entity WhatsAppLog {
    id Long,
    tenantId String required maxlength(255),
    recipientPhone String required maxlength(50),
    messageBody TextBlob,
    sentAt ZonedDateTime required,
    status String maxlength(50), // SENT, FAILED, etc.
    type String maxlength(50), // TRANSACTIONAL, BULK
    campaignId Long, // nullable, for bulk
    metadata TextBlob // JSON for provider response, etc.
}

entity CommunicationCampaign {
    id Long,
    tenantId String required maxlength(255),
    name String required maxlength(255),
    type String maxlength(50), // EMAIL, WHATSAPP
    description String maxlength(1000),
    createdById Long,
    createdAt ZonedDateTime required,
    scheduledAt ZonedDateTime,
    sentAt ZonedDateTime,
    status String maxlength(50) // DRAFT, SCHEDULED, SENT, FAILED
}
```

**Relationships:**

- `EmailLog{campaign} to CommunicationCampaign`
- `WhatsAppLog{campaign} to CommunicationCampaign`
- `CommunicationCampaign{createdBy} to UserProfile`

---

## 6. Implementation Tasks

1. **Integrate SES and Twilio using Java SDKs.**
2. **Create service classes** for sending and logging communications.
3. **Expose REST endpoints** for transactional and bulk messaging.
4. **Implement monthly aggregation queries** for reporting/billing per tenant.
5. **Add admin UI** for campaign management and usage analytics (optional).
6. **Ensure all sends are logged** with required metadata for analytics and billing.

---

## 7. Build vs. Buy Summary

| Feature                | Off-the-Shelf | Custom Build (Recommended)      |
| ---------------------- | ------------- | ------------------------------- |
| Transactional Email    | SES           | SES + EmailLog                  |
| Bulk Email             | Pinpoint/3rdP | SES + EmailLog + Campaign       |
| Transactional WhatsApp | Twilio        | Twilio + WhatsAppLog            |
| Bulk WhatsApp          | Twilio        | Twilio + WhatsAppLog + Campaign |
| Per-Tenant Analytics   | No            | Custom DB tables/queries        |
| Multi-Tenant Billing   | No            | Custom DB tables/queries        |

---

## 8. Recommendations

- **Proceed with SES and Twilio for sending.**
- **Implement EmailLog, WhatsAppLog, and CommunicationCampaign entities** for tracking and analytics.
- **No off-the-shelf solution** provides all requirements for multi-tenant analytics and billing; custom DB tracking is required.
- **No changes made yet** to code or JDL. This document is for planning and review.

---

_Prepared for: Multi-Tenant Event Management System_
_Date: [Insert Date]_
