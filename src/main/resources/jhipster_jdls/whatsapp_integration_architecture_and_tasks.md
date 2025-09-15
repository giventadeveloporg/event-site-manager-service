# WhatsApp Integration Architecture & Tasks

## Overview

Integrate Twilio WhatsApp API for transactional and bulk messaging in a multi-tenant event management backend.

---

## Requirements

- Send WhatsApp confirmation after ticket purchase (triggered from EventTicketTransactionResource).
- Allow tenants to send bulk WhatsApp messages for campaigns, announcements, and notices.
- Log all WhatsApp messages for analytics and billing.
- Store Twilio credentials in environment variables and map via application.yml.

---

## Example application.yml Configuration

```yaml
twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  whatsapp-from: ${TWILIO_WHATSAPP_FROM} # e.g., 'whatsapp:+14155238886'
```

---

## Proposed Schema (JDL-style)

```jdl
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

relationship ManyToOne {
    WhatsAppLog{campaign} to CommunicationCampaign
    CommunicationCampaign{createdBy} to UserProfile
}
```

---

## Implementation Steps

1. **Add Twilio Java SDK dependency to pom.xml:**
   ```xml
   <dependency>
       <groupId>com.twilio.sdk</groupId>
       <artifactId>twilio</artifactId>
       <version>9.15.3</version>
   </dependency>
   ```
2. **Add Twilio credentials to application.yml and environment variables.**
3. **Create a TwilioProperties config class** to map the Twilio config block.
4. **Implement WhatsAppSenderService** for sending and logging messages.
5. **Expose REST endpoints in WhatsAppLogResource** for transactional and bulk messaging.
6. **Implement template support for WhatsApp messages** (simple text with placeholders, e.g., `{{userName}}`, `{{eventName}}`).
7. **Log all sends in WhatsAppLog** for analytics and billing.
8. **Add admin UI for campaign management and analytics** (optional).

---

## Example WhatsApp Message Template

```
Hello {{userName}},
Your ticket for {{eventName}} on {{eventDate}} at {{eventVenue}} is confirmed!
Thank you for your purchase.
```

- Replace placeholders with actual values before sending.

---

## Recommendations

- Use templates for WhatsApp messages for consistency and personalization.
- Store templates as local files, in DB, or S3.
- Use environment variables for Twilio credentials.
- Log all WhatsApp sends for analytics and billing.

---

## Summary Table

| Feature                | Off-the-Shelf | Custom Build (Recommended)      |
| ---------------------- | ------------- | ------------------------------- |
| Transactional WhatsApp | Twilio        | Twilio + WhatsAppLog            |
| Bulk WhatsApp          | Twilio        | Twilio + WhatsAppLog + Campaign |
| Per-Tenant Analytics   | No            | Custom DB tables/queries        |
| Multi-Tenant Billing   | No            | Custom DB tables/queries        |

---

## Next Steps

- Implement the above steps in the codebase, following the modular structure used for email integration.
- Ensure all credentials are securely managed and not hardcoded.
- Test transactional and bulk WhatsApp messaging flows.
