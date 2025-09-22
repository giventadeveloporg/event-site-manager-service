# WhatsApp Integration API Documentation

## Overview

This document provides comprehensive documentation for the WhatsApp integration API endpoints. The integration allows sending WhatsApp messages through Twilio's WhatsApp Business API with multi-tenant support, rate limiting, retry logic, and comprehensive analytics.

## Table of Contents

1. [Authentication](#authentication)
2. [Base URL](#base-url)
3. [API Endpoints](#api-endpoints)
4. [Data Transfer Objects](#data-transfer-objects)
5. [Error Handling](#error-handling)
6. [Rate Limiting](#rate-limiting)
7. [Webhooks](#webhooks)
8. [Configuration](#configuration)
9. [Examples](#examples)

## Authentication

All API endpoints require tenant-based authentication. The tenant ID must be provided either as a request parameter or in the request body.

## Base URL

```
http://localhost:8080/api/whatsapp
```

## API Endpoints

### 1. Send Single Message

**Endpoint:** `POST /api/whatsapp/send-message`

**Description:** Send a single WhatsApp message to a recipient.

**Request Body:**
```json
{
  "tenantId": "string (required)",
  "to": "string (required)",
  "messageBody": "string (required)",
  "templateId": "string (optional)",
  "templateParameters": {
    "key": "value"
  },
  "mediaUrl": "string (optional)",
  "campaignId": "string (optional)"
}
```

**Response:**
```json
{
  "messageId": "string",
  "status": "string",
  "errorCode": "string (optional)",
  "errorMessage": "string (optional)",
  "sentAt": "datetime",
  "metadata": {
    "twilioResponse": "string",
    "messageType": "TEXT|TEMPLATE"
  }
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/whatsapp/send-message" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant_001",
    "to": "+1234567890",
    "messageBody": "Hello! This is a test message."
  }'
```

### 2. Send Bulk Messages

**Endpoint:** `POST /api/whatsapp/send-bulk`

**Description:** Send WhatsApp messages to multiple recipients.

**Request Body:**
```json
{
  "tenantId": "string (required)",
  "recipients": ["string (required)"],
  "messageBody": "string (required)",
  "templateId": "string (optional)",
  "templateParameters": {
    "key": "value"
  },
  "mediaUrl": "string (optional)",
  "campaignId": "string (optional)"
}
```

**Response:**
```json
{
  "totalSent": "number",
  "totalFailed": "number",
  "results": [
    {
      "messageId": "string",
      "status": "string",
      "errorCode": "string (optional)",
      "errorMessage": "string (optional)",
      "sentAt": "datetime"
    }
  ],
  "campaignId": "string",
  "sentAt": "datetime",
  "status": "SUCCESS|PARTIAL_SUCCESS|FAILED"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/whatsapp/send-bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant_001",
    "recipients": ["+1234567890", "+1234567891"],
    "messageBody": "Hello! This is a bulk message."
  }'
```

### 3. Check Delivery Status

**Endpoint:** `GET /api/whatsapp/delivery-status/{messageId}`

**Description:** Check the delivery status of a sent message.

**Parameters:**
- `messageId` (path): The Twilio message ID
- `tenantId` (query): The tenant ID

**Response:**
```json
{
  "messageId": "string",
  "status": "string",
  "errorCode": "string (optional)",
  "errorMessage": "string (optional)",
  "sentAt": "datetime",
  "metadata": {
    "twilioResponse": "string",
    "price": "string",
    "priceUnit": "string"
  }
}
```

**Example:**
```bash
curl -X GET "http://localhost:8080/api/whatsapp/delivery-status/SM1234567890abcdef?tenantId=tenant_001"
```

### 4. Test Connection

**Endpoint:** `GET /api/whatsapp/test-connection`

**Description:** Test Twilio credentials and connection.

**Parameters:**
- `tenantId` (query): The tenant ID

**Response:**
```json
{
  "success": "boolean",
  "message": "string",
  "errorCode": "string (optional)",
  "errorMessage": "string (optional)",
  "testedAt": "datetime",
  "tenantId": "string",
  "accountSid": "string",
  "whatsappFrom": "string"
}
```

**Example:**
```bash
curl -X GET "http://localhost:8080/api/whatsapp/test-connection?tenantId=tenant_001"
```

### 5. Get Templates

**Endpoint:** `GET /api/whatsapp/templates`

**Description:** Get approved WhatsApp message templates.

**Parameters:**
- `tenantId` (query): The tenant ID

**Response:**
```json
[
  {
    "id": "string",
    "name": "string",
    "content": "string",
    "category": "string",
    "status": "string",
    "language": "string",
    "parameters": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime",
    "tenantId": "string"
  }
]
```

**Example:**
```bash
curl -X GET "http://localhost:8080/api/whatsapp/templates?tenantId=tenant_001"
```

### 6. Create Template

**Endpoint:** `POST /api/whatsapp/templates`

**Description:** Create a new WhatsApp message template.

**Parameters:**
- `tenantId` (query): The tenant ID

**Request Body:**
```json
{
  "name": "string (required)",
  "content": "string (required)",
  "category": "string (optional)",
  "language": "string (optional)",
  "parameters": ["string (optional)"]
}
```

**Response:**
```json
{
  "id": "string",
  "name": "string",
  "content": "string",
  "category": "string",
  "status": "PENDING",
  "language": "string",
  "parameters": ["string"],
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "tenantId": "string"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/whatsapp/templates?tenantId=tenant_001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Welcome Message",
    "content": "Hello {{name}}, welcome to our service!",
    "category": "UTILITY",
    "language": "en",
    "parameters": ["name"]
  }'
```

### 7. Get Analytics

**Endpoint:** `GET /api/whatsapp/analytics`

**Description:** Get WhatsApp analytics for a tenant.

**Parameters:**
- `tenantId` (query): The tenant ID
- `fromDate` (query, optional): Start date (ISO 8601 format)
- `toDate` (query, optional): End date (ISO 8601 format)

**Response:**
```json
{
  "tenantId": "string",
  "fromDate": "datetime",
  "toDate": "datetime",
  "totalMessages": "number",
  "sentMessages": "number",
  "deliveredMessages": "number",
  "failedMessages": "number",
  "readMessages": "number",
  "deliveryRate": "number",
  "readRate": "number",
  "totalRecipients": "number",
  "uniqueRecipients": "number"
}
```

**Example:**
```bash
curl -X GET "http://localhost:8080/api/whatsapp/analytics?tenantId=tenant_001&fromDate=2024-01-01T00:00:00Z&toDate=2024-01-31T23:59:59Z"
```

## Webhook Endpoints

### 1. Delivery Status Webhook

**Endpoint:** `POST /api/whatsapp/webhook/delivery-status`

**Description:** Handle delivery status webhooks from Twilio.

**Parameters:**
- `tenantId` (query, optional): The tenant ID

**Headers:**
- `X-Twilio-Signature`: Twilio webhook signature

**Request Body:** Twilio webhook payload

**Response:**
```
Webhook processed successfully
```

### 2. Message Status Webhook

**Endpoint:** `POST /api/whatsapp/webhook/message-status`

**Description:** Handle message status webhooks from Twilio.

**Parameters:**
- `tenantId` (query, optional): The tenant ID

**Headers:**
- `X-Twilio-Signature`: Twilio webhook signature

**Request Body:** Twilio webhook payload

**Response:**
```
Webhook processed successfully
```

## Data Transfer Objects

### TwilioWhatsAppRequestDTO
```json
{
  "tenantId": "string",
  "to": "string",
  "messageBody": "string",
  "templateId": "string",
  "templateParameters": {
    "key": "value"
  },
  "mediaUrl": "string",
  "campaignId": "string"
}
```

### TwilioWhatsAppResponseDTO
```json
{
  "messageId": "string",
  "status": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "sentAt": "datetime",
  "metadata": {
    "key": "value"
  }
}
```

### TwilioWhatsAppBulkRequestDTO
```json
{
  "tenantId": "string",
  "recipients": ["string"],
  "messageBody": "string",
  "templateId": "string",
  "templateParameters": {
    "key": "value"
  },
  "mediaUrl": "string",
  "campaignId": "string"
}
```

### TwilioWhatsAppBulkResponseDTO
```json
{
  "totalSent": "number",
  "totalFailed": "number",
  "results": [
    {
      "messageId": "string",
      "status": "string",
      "errorCode": "string",
      "errorMessage": "string",
      "sentAt": "datetime"
    }
  ],
  "campaignId": "string",
  "sentAt": "datetime",
  "status": "string"
}
```

### ConnectionTestResultDTO
```json
{
  "success": "boolean",
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "testedAt": "datetime",
  "tenantId": "string",
  "accountSid": "string",
  "whatsappFrom": "string"
}
```

### MessageTemplateDTO
```json
{
  "templateId": "string",
  "name": "string",
  "category": "string",
  "language": "string",
  "status": "string",
  "body": "string",
  "components": [
    {
      "type": "string",
      "text": "string",
      "format": "string",
      "parameters": [
        {
          "type": "string",
          "text": "string",
          "example": "string"
        }
      ]
    }
  ],
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "tenantId": "string"
}
```

### WhatsAppAnalyticsDTO
```json
{
  "tenantId": "string",
  "fromDate": "datetime",
  "toDate": "datetime",
  "totalMessages": "number",
  "sentMessages": "number",
  "deliveredMessages": "number",
  "failedMessages": "number",
  "readMessages": "number",
  "deliveryRate": "number",
  "readRate": "number",
  "totalRecipients": "number",
  "uniqueRecipients": "number"
}
```

## Error Handling

The API uses standard HTTP status codes and returns error responses in the following format:

```json
{
  "errorCode": "string",
  "title": "string",
  "detail": "string",
  "status": "number",
  "timestamp": "datetime"
}
```

### Common Error Codes

- `WHATSAPP_NOT_ENABLED`: WhatsApp integration not enabled for tenant
- `WHATSAPP_CONFIGURATION_ERROR`: Invalid or missing configuration
- `WHATSAPP_RATE_LIMIT_EXCEEDED`: Rate limit exceeded
- `WHATSAPP_TEMPLATE_ERROR`: Template operation failed
- `WHATSAPP_WEBHOOK_ERROR`: Webhook processing failed
- `WHATSAPP_BAD_REQUEST`: Invalid request
- `WHATSAPP_INTERNAL_ERROR`: Internal server error

### HTTP Status Codes

- `200 OK`: Request successful
- `400 Bad Request`: Invalid request
- `429 Too Many Requests`: Rate limit exceeded
- `500 Internal Server Error`: Server error

## Rate Limiting

The API implements rate limiting to prevent abuse and ensure fair usage:

- **Single Messages**: 20 messages per minute per tenant
- **Bulk Messages**: 100 messages per hour per tenant
- **Retry Attempts**: 3 attempts with exponential backoff
- **Retry Delay**: 60 seconds base delay

Rate limit information is included in error responses:

```json
{
  "errorCode": "WHATSAPP_RATE_LIMIT_EXCEEDED",
  "title": "Rate limit exceeded",
  "detail": "Rate limit has been exceeded",
  "status": 429,
  "retryAfterSeconds": 60,
  "retryAfter": "2024-01-01T12:00:00Z"
}
```

## Webhooks

### Webhook Security

All webhooks are secured using Twilio's signature validation. The webhook token is stored in the tenant settings and used to validate incoming webhooks.

### Webhook Payload

Twilio sends webhook payloads with the following structure:

```
MessageSid: The message ID
MessageStatus: The message status (sent, delivered, failed, etc.)
ErrorCode: Error code if applicable
ErrorMessage: Error message if applicable
To: Recipient phone number
From: Sender phone number
EventType: The event type
```

### Webhook Processing

The webhook handler:
1. Validates the webhook signature
2. Extracts the tenant ID from the request
3. Updates the message status in the database
4. Logs the webhook event

## Configuration

### Application Properties

The WhatsApp integration can be configured using the following properties:

```yaml
whatsapp:
  encryption:
    key: ${WHATSAPP_ENCRYPTION_KEY:your-32-character-encryption-key-here}
    algorithm: AES/GCM/NoPadding

  webhook:
    base-url: ${WHATSAPP_WEBHOOK_BASE_URL:http://localhost:8080}
    encryption-key: ${WHATSAPP_WEBHOOK_ENCRYPTION_KEY:your-webhook-encryption-key-here}

  rate-limit:
    messages-per-minute: ${WHATSAPP_RATE_LIMIT_MESSAGES_PER_MINUTE:20}
    bulk-messages-per-hour: ${WHATSAPP_RATE_LIMIT_BULK_MESSAGES_PER_HOUR:100}
    retry-attempts: ${WHATSAPP_RATE_LIMIT_RETRY_ATTEMPTS:3}
    retry-delay-seconds: ${WHATSAPP_RATE_LIMIT_RETRY_DELAY_SECONDS:60}

  message:
    max-recipients-per-bulk: ${WHATSAPP_MAX_RECIPIENTS_PER_BULK:1000}
    template-validation-enabled: ${WHATSAPP_TEMPLATE_VALIDATION_ENABLED:true}
    delivery-status-check-enabled: ${WHATSAPP_DELIVERY_STATUS_CHECK_ENABLED:true}

  logging:
    enabled: ${WHATSAPP_LOGGING_ENABLED:true}
    log-level: ${WHATSAPP_LOGGING_LEVEL:INFO}
    log-message-content: ${WHATSAPP_LOG_MESSAGE_CONTENT:false}
    log-personal-data: ${WHATSAPP_LOG_PERSONAL_DATA:false}
```

### Environment Variables

- `WHATSAPP_ENCRYPTION_KEY`: Encryption key for sensitive data
- `WHATSAPP_WEBHOOK_BASE_URL`: Base URL for webhooks
- `WHATSAPP_WEBHOOK_ENCRYPTION_KEY`: Webhook encryption key
- `WHATSAPP_RATE_LIMIT_MESSAGES_PER_MINUTE`: Rate limit for single messages
- `WHATSAPP_RATE_LIMIT_BULK_MESSAGES_PER_HOUR`: Rate limit for bulk messages
- `WHATSAPP_MAX_RECIPIENTS_PER_BULK`: Maximum recipients per bulk message

## Examples

### Complete Integration Example

```bash
# 1. Test connection
curl -X GET "http://localhost:8080/api/whatsapp/test-connection?tenantId=tenant_001"

# 2. Send a single message
curl -X POST "http://localhost:8080/api/whatsapp/send-message" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant_001",
    "to": "+1234567890",
    "messageBody": "Hello! This is a test message."
  }'

# 3. Check delivery status
curl -X GET "http://localhost:8080/api/whatsapp/delivery-status/SM1234567890abcdef?tenantId=tenant_001"

# 4. Send bulk messages
curl -X POST "http://localhost:8080/api/whatsapp/send-bulk" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant_001",
    "recipients": ["+1234567890", "+1234567891"],
    "messageBody": "Hello! This is a bulk message."
  }'

# 5. Get analytics
curl -X GET "http://localhost:8080/api/whatsapp/analytics?tenantId=tenant_001"
```

### Template Usage Example

```bash
# 1. Create a template
curl -X POST "http://localhost:8080/api/whatsapp/templates?tenantId=tenant_001" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Event Reminder",
    "content": "Hello {{name}}, don'\''t forget about {{eventName}} tomorrow at {{time}}!",
    "category": "UTILITY",
    "language": "en",
    "parameters": ["name", "eventName", "time"]
  }'

# 2. Send message using template
curl -X POST "http://localhost:8080/api/whatsapp/send-message" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant_001",
    "to": "+1234567890",
    "templateId": "event_reminder",
    "templateParameters": {
      "name": "John",
      "eventName": "Team Meeting",
      "time": "2:00 PM"
    }
  }'
```

## Support

For support and questions about the WhatsApp integration API, please contact the development team or refer to the project documentation.

## Changelog

### Version 1.0.0
- Initial release
- Single message sending
- Bulk message sending
- Delivery status checking
- Connection testing
- Template management
- Analytics
- Webhook handling
- Rate limiting and retry logic
- Multi-tenant support
