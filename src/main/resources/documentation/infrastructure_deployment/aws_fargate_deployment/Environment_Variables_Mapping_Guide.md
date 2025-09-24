# Environment Variables Mapping Guide - Docker to AWS Fargate

## Overview

This guide shows how to map environment variables from your successful Docker local setup to AWS Fargate deployment, including how to securely manage secrets using AWS Secrets Manager.

---

## Environment Variables Mapping Table

| **Docker .env Variable** | **AWS Fargate Environment** | **AWS Secrets Manager** | **Purpose** |
|--------------------------|------------------------------|-------------------------|-------------|
| `SPRING_PROFILES_ACTIVE` | Direct Environment Variable | ❌ No | Spring Boot profile |
| `RDS_ENDPOINT` | Direct Environment Variable | ❌ No | Database host |
| `DB_NAME` | Direct Environment Variable | ❌ No | Database name |
| `DB_USERNAME` | Direct Environment Variable | ❌ No | Database username |
| `DB_PASSWORD` | Secret from AWS Secrets Manager | ✅ `malayalees/db-password` | Database password |
| `CACHE_TYPE` | Direct Environment Variable | ❌ No | Cache type (postgresql) |
| `POSTGRES_CACHE_TABLE` | Direct Environment Variable | ❌ No | Cache table name |
| `JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET` | Secret from AWS Secrets Manager | ✅ `malayalees/jwt-secret` | JWT signing secret |
| `JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET` | Secret from AWS Secrets Manager | ✅ `malayalees/jwt-base64-secret` | JWT base64 secret |
| `JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS` | Direct Environment Variable | ❌ No | JWT token validity |
| `JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME` | Direct Environment Variable | ❌ No | JWT remember-me validity |
| `AWS_ACCESS_KEY_ID` | Secret from AWS Secrets Manager | ✅ `malayalees/aws-access-key` | AWS access key |
| `AWS_SECRET_ACCESS_KEY` | Secret from AWS Secrets Manager | ✅ `malayalees/aws-secret-key` | AWS secret key |
| `TWILIO_ACCOUNT_SID` | Secret from AWS Secrets Manager | ✅ `malayalees/twilio-account-sid` | Twilio account SID |
| `TWILIO_AUTH_TOKEN` | Secret from AWS Secrets Manager | ✅ `malayalees/twilio-auth-token` | Twilio auth token |
| `TWILIO_WHATSAPP_FROM` | Direct Environment Variable | ❌ No | Twilio WhatsApp number |
| `QRCODE_SCAN_HOST_PREFIX` | Direct Environment Variable | ❌ No | QR code scan URL prefix |
| `EMAIL_HOST_URL_PREFIX` | Direct Environment Variable | ❌ No | Email host URL prefix |
| `JAVA_OPTS` | Direct Environment Variable | ❌ No | JVM options |

---

## Docker .env File Structure

### Current Docker .env File
```bash
# Spring Configuration
SPRING_PROFILES_ACTIVE=local-postgres-cache
JAVA_OPTS=-Xmx512m -Xms256m

# Database Configuration
RDS_ENDPOINT=host.docker.internal
DB_NAME=giventauser_db
DB_USERNAME=giventauser
DB_PASSWORD=giventauser

# PostgreSQL Caching Configuration
CACHE_TYPE=postgresql
POSTGRES_CACHE_TABLE=app_cache

# JHipster Security Configuration
JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET=mySecretKey12345678901234567890123456789012345678901234567890
JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=bXlTZWNyZXRLZXkxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==
JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS=86400
JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME=2592000

# AWS Configuration
AWS_ACCESS_KEY_ID=AKIATIT5HARDKCWNLQMU
AWS_SECRET_ACCESS_KEY=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

# Twilio Configuration
TWILIO_ACCOUNT_SID=AC48380299acc5e7e27aee75a3108c3058
TWILIO_AUTH_TOKEN=f460b6085002a38cf3c5cadd61c21057
TWILIO_WHATSAPP_FROM=whatsapp:+14155238886

# Application URLs
QRCODE_SCAN_HOST_PREFIX=http://localhost:3000/admin
EMAIL_HOST_URL_PREFIX=http://localhost:3000

# Liquibase Configuration
LIQUIBASE_ENABLED=false
```

---

## AWS Fargate Task Definition Structure

### Environment Variables (Non-Sensitive)
```json
{
  "environment": [
    {
      "name": "SPRING_PROFILES_ACTIVE",
      "value": "prod-aws-postgres-cache"
    },
    {
      "name": "RDS_ENDPOINT",
      "value": "your-rds-endpoint.rds.amazonaws.com"
    },
    {
      "name": "DB_NAME",
      "value": "malayalees_us_site"
    },
    {
      "name": "DB_USERNAME",
      "value": "malayalees_app"
    },
    {
      "name": "CACHE_TYPE",
      "value": "postgresql"
    },
    {
      "name": "POSTGRES_CACHE_TABLE",
      "value": "app_cache"
    },
    {
      "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS",
      "value": "86400"
    },
    {
      "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME",
      "value": "2592000"
    },
    {
      "name": "TWILIO_WHATSAPP_FROM",
      "value": "whatsapp:+14155238886"
    },
    {
      "name": "QRCODE_SCAN_HOST_PREFIX",
      "value": "https://your-domain.com/admin"
    },
    {
      "name": "EMAIL_HOST_URL_PREFIX",
      "value": "https://your-domain.com"
    },
    {
      "name": "JAVA_OPTS",
      "value": "-Xmx512m -Xms256m"
    }
  ]
}
```

### Secrets (Sensitive Data)
```json
{
  "secrets": [
    {
      "name": "DB_PASSWORD",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/db-password"
    },
    {
      "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/jwt-secret"
    },
    {
      "name": "JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/jwt-base64-secret"
    },
    {
      "name": "AWS_ACCESS_KEY_ID",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/aws-access-key"
    },
    {
      "name": "AWS_SECRET_ACCESS_KEY",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/aws-secret-key"
    },
    {
      "name": "TWILIO_ACCOUNT_SID",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/twilio-account-sid"
    },
    {
      "name": "TWILIO_AUTH_TOKEN",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:malayalees/twilio-auth-token"
    }
  ]
}
```

---

## AWS Secrets Manager Setup

### 1. Create Secrets Using AWS CLI

```bash
# Database password
aws secretsmanager create-secret \
    --name malayalees/db-password \
    --description "Database password for Malayalees application" \
    --secret-string "YourAppPassword123!" \
    --region us-east-1

# JWT secret
aws secretsmanager create-secret \
    --name malayalees/jwt-secret \
    --description "JWT secret for Malayalees application" \
    --secret-string "mySecretKey12345678901234567890123456789012345678901234567890" \
    --region us-east-1

# JWT base64 secret
aws secretsmanager create-secret \
    --name malayalees/jwt-base64-secret \
    --description "JWT base64 secret for Malayalees application" \
    --secret-string "bXlTZWNyZXRLZXkxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==" \
    --region us-east-1

# AWS credentials
aws secretsmanager create-secret \
    --name malayalees/aws-access-key \
    --description "AWS access key for Malayalees application" \
    --secret-string "AKIATIT5HARDKCWNLQMU" \
    --region us-east-1

aws secretsmanager create-secret \
    --name malayalees/aws-secret-key \
    --description "AWS secret key for Malayalees application" \
    --secret-string "9xyoyfvKjMJzRhDBZEkqM/qatrGUtV4IVO6CuIBo" \
    --region us-east-1

# Twilio credentials
aws secretsmanager create-secret \
    --name malayalees/twilio-account-sid \
    --description "Twilio account SID for Malayalees application" \
    --secret-string "AC48380299acc5e7e27aee75a3108c3058" \
    --region us-east-1

aws secretsmanager create-secret \
    --name malayalees/twilio-auth-token \
    --description "Twilio auth token for Malayalees application" \
    --secret-string "f460b6085002a38cf3c5cadd61c21057" \
    --region us-east-1
```

### 2. Update Secrets

```bash
# Update database password
aws secretsmanager update-secret \
    --secret-id malayalees/db-password \
    --secret-string "NewPassword123!" \
    --region us-east-1

# Update JWT secret
aws secretsmanager update-secret \
    --secret-id malayalees/jwt-secret \
    --secret-string "NewJWTSecret12345678901234567890123456789012345678901234567890" \
    --region us-east-1
```

### 3. Retrieve Secrets

```bash
# Get database password
aws secretsmanager get-secret-value \
    --secret-id malayalees/db-password \
    --region us-east-1 \
    --query SecretString --output text

# Get JWT secret
aws secretsmanager get-secret-value \
    --secret-id malayalees/jwt-secret \
    --region us-east-1 \
    --query SecretString --output text
```

---

## Spring Boot Configuration Mapping

### Docker Configuration (application-local-postgres-cache.yml)
```yaml
spring:
  profiles:
    active: local-postgres-cache

  datasource:
    url: jdbc:postgresql://${RDS_ENDPOINT:localhost}:5432/${DB_NAME:malayalees_us_site}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}

  cache:
    type: postgresql
    postgresql:
      cache-table: ${POSTGRES_CACHE_TABLE:app_cache}

  session:
    store-type: postgresql
    postgresql:
      table-name: "session_store"

jhipster:
  security:
    authentication:
      jwt:
        secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET}
        base64-secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET}
        token-validity-in-seconds: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS:86400}
        token-validity-in-seconds-for-remember-me: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME:2592000}

aws:
  access-key-id: ${AWS_ACCESS_KEY_ID}
  secret-access-key: ${AWS_SECRET_ACCESS_KEY}

twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  whatsapp-from: ${TWILIO_WHATSAPP_FROM:whatsapp:+14155238886}

application:
  urls:
    qrcode-scan-host-prefix: ${QRCODE_SCAN_HOST_PREFIX:http://localhost:3000/admin}
    email-host-url-prefix: ${EMAIL_HOST_URL_PREFIX:http://localhost:3000}
```

### AWS Configuration (application-prod-aws-postgres-cache.yml)
```yaml
spring:
  profiles:
    active: prod-aws-postgres-cache

  datasource:
    url: jdbc:postgresql://${RDS_ENDPOINT}:5432/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000

  cache:
    type: postgresql
    postgresql:
      cache-table: ${POSTGRES_CACHE_TABLE:app_cache}
      time-to-live: 600000
      cache-null-values: false
      enable-statistics: true

  session:
    store-type: postgresql
    postgresql:
      table-name: "session_store"
      cleanup-cron: "0 0 * * * *"

  jpa:
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          use_query_cache: true
          region:
            factory_class: org.hibernate.cache.jcache.JCacheRegionFactory
        connection:
          provider_disables_autocommit: true

jhipster:
  security:
    authentication:
      jwt:
        secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_SECRET}
        base64-secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET}
        token-validity-in-seconds: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS:86400}
        token-validity-in-seconds-for-remember-me: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME:2592000}

aws:
  access-key-id: ${AWS_ACCESS_KEY_ID}
  secret-access-key: ${AWS_SECRET_ACCESS_KEY}
  region: us-east-1

twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  whatsapp-from: ${TWILIO_WHATSAPP_FROM:whatsapp:+14155238886}

application:
  urls:
    qrcode-scan-host-prefix: ${QRCODE_SCAN_HOST_PREFIX:https://your-domain.com/admin}
    email-host-url-prefix: ${EMAIL_HOST_URL_PREFIX:https://your-domain.com}
```

---

## Environment-Specific Differences

### Local Development (Docker)
- **Profile**: `local-postgres-cache`
- **Database**: `host.docker.internal:5432`
- **URLs**: `http://localhost:3000`
- **Liquibase**: Enabled for schema updates
- **Logging**: DEBUG level for development

### AWS Production (Fargate)
- **Profile**: `prod-aws-postgres-cache`
- **Database**: RDS endpoint
- **URLs**: Production domain URLs
- **Liquibase**: Enabled for initial setup
- **Logging**: INFO level for production
- **Connection Pooling**: Optimized for production load
- **Caching**: Enhanced PostgreSQL caching configuration

---

## Security Best Practices

### 1. Secrets Management
- ✅ **Use AWS Secrets Manager** for sensitive data
- ✅ **Rotate secrets regularly**
- ✅ **Use least privilege access**
- ✅ **Enable encryption at rest**

### 2. Environment Variables
- ✅ **Use non-sensitive data** in environment variables
- ✅ **Use descriptive names**
- ✅ **Document all variables**
- ✅ **Validate input values**

### 3. Network Security
- ✅ **Use VPC with private subnets**
- ✅ **Configure security groups properly**
- ✅ **Use HTTPS for external communication**
- ✅ **Enable VPC Flow Logs**

---

## Troubleshooting Environment Variables

### 1. Check Environment Variables in Container
```bash
# Execute command in running container
aws ecs execute-command \
    --cluster malayalees-cluster \
    --task task-id \
    --container malayalees-app \
    --interactive \
    --command "/bin/bash"

# Inside container, check environment variables
env | grep -E "(SPRING|DB_|JHIPSTER|AWS|TWILIO)"
```

### 2. Check Secrets Manager Access
```bash
# Test secrets access from container
aws secretsmanager get-secret-value \
    --secret-id malayalees/db-password \
    --region us-east-1
```

### 3. Check Application Logs
```bash
# View ECS service logs
aws logs describe-log-streams \
    --log-group-name /ecs/malayalees-app \
    --region us-east-1

# Get log events
aws logs get-log-events \
    --log-group-name /ecs/malayalees-app \
    --log-stream-name ecs/malayalees-app/task-id \
    --region us-east-1
```

---

## Migration Checklist

### Pre-Migration
- [ ] Document all environment variables
- [ ] Identify sensitive vs non-sensitive variables
- [ ] Create AWS Secrets Manager secrets
- [ ] Test secrets access
- [ ] Update application configuration

### Migration
- [ ] Create Fargate task definition
- [ ] Map environment variables correctly
- [ ] Configure secrets from AWS Secrets Manager
- [ ] Test application startup
- [ ] Verify all functionality

### Post-Migration
- [ ] Monitor application logs
- [ ] Test all features
- [ ] Verify secrets rotation
- [ ] Update documentation
- [ ] Train team on new setup

---

*Last Updated: January 2025*
*Version: 1.0 - Environment Variables Mapping Guide*
