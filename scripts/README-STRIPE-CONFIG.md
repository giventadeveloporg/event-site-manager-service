# Stripe Payment Provider Configuration Scripts

This directory contains scripts to configure Stripe payment provider credentials in the database.

## Overview

The Stripe payment provider configuration is stored in the `payment_provider_config` table with encrypted credentials. These scripts automate the process of:

1. Generating an encryption key (if needed)
2. Encrypting Stripe credentials
3. Storing them in the database

## Prerequisites

1. **Java 17+** installed and in PATH
2. **Maven** (Maven wrapper included in project)
3. **PostgreSQL database** running and accessible
4. **Stripe API credentials** from your Stripe dashboard:
   - Secret Key (starts with `sk_test_` or `sk_live_`)
   - Webhook Secret (starts with `whsec_`)
   - Publishable Key (starts with `pk_test_` or `pk_live_`)

## Step 1: Generate Encryption Key

First, generate an encryption key that will be used to encrypt/decrypt payment credentials:

```powershell
.\scripts\generate-payment-encryption-key.ps1
```

This will output a Base64-encoded encryption key. **Save this key securely** - you'll need it every time you run the application.

### Set the Encryption Key

**Option A: Environment Variable (Recommended for local development)**

```powershell
$env:PAYMENT_ENCRYPTION_KEY="<your-generated-key-here>"
```

**Option B: Application Configuration File**

Add to `src/main/resources/config/application-local.yml` or `application-dev.yml`:

```yaml
application:
  payment:
    encryption:
      key: <your-generated-key-here>
```

⚠️ **IMPORTANT**: Never commit the encryption key to version control! Add it to `.gitignore` or use environment variables.

## Step 2: Set Stripe Credentials

Set the following environment variables with your Stripe credentials:

```powershell
# Stripe Secret Key (from https://dashboard.stripe.com/apikeys)
$env:STRIPE_SECRET_KEY="sk_test_YOUR_SECRET_KEY_HERE"

# Stripe Webhook Secret (from https://dashboard.stripe.com/webhooks)
$env:STRIPE_WEBHOOK_SECRET="whsec_YOUR_WEBHOOK_SECRET_HERE"

# Stripe Publishable Key (from https://dashboard.stripe.com/apikeys)
$env:NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY="pk_test_YOUR_PUBLISHABLE_KEY_HERE"
```

## Step 3: Configure Stripe in Database

Run the configuration script:

```powershell
.\scripts\configure-stripe-payment.ps1
```

Or with custom parameters:

```powershell
# For a specific tenant
.\scripts\configure-stripe-payment.ps1 -TenantId "tenant_demo_001" -Profile "local"

# For dev environment
.\scripts\configure-stripe-payment.ps1 -TenantId "tenant_demo_001" -Profile "dev"
```

The script will:

- ✅ Validate that all required environment variables are set
- ✅ Encrypt the Stripe credentials using the encryption key
- ✅ Create or update the `payment_provider_config` record in the database
- ✅ Exit after successful configuration

## Verification

After running the script, verify the configuration was saved:

```sql
SELECT
    id,
    tenant_id,
    provider_name,
    is_active,
    publishable_key,
    created_at,
    updated_at
FROM payment_provider_config
WHERE provider_name = 'STRIPE'
  AND tenant_id = 'tenant_demo_001';
```

The `provider_secret_key_encrypted` and `webhook_secret_encrypted` columns should contain encrypted values (not plain text).

## Troubleshooting

### Error: "Payment encryption key not configured"

**Solution**: Make sure you've set the `PAYMENT_ENCRYPTION_KEY` environment variable or added it to your application configuration file.

```powershell
# Check if it's set
echo $env:PAYMENT_ENCRYPTION_KEY

# Set it if missing
$env:PAYMENT_ENCRYPTION_KEY="<your-key>"
```

### Error: "STRIPE_SECRET_KEY environment variable is required"

**Solution**: Make sure all three Stripe environment variables are set:

```powershell
$env:STRIPE_SECRET_KEY="sk_test_..."
$env:STRIPE_WEBHOOK_SECRET="whsec_..."
$env:NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY="pk_test_..."
```

### Error: Database connection failed

**Solution**: Ensure your database is running and the connection settings in `application-local.yml` or `application-dev.yml` are correct.

### Error: "Failed to encrypt credential"

**Solution**: Verify the encryption key is valid Base64-encoded AES-256 key. Regenerate it if needed:

```powershell
.\scripts\generate-payment-encryption-key.ps1
```

## Environment-Specific Configuration

### Local Development (`local` profile)

```powershell
$env:PAYMENT_ENCRYPTION_KEY="<local-encryption-key>"
$env:STRIPE_SECRET_KEY="sk_test_..."  # Test mode key
$env:STRIPE_WEBHOOK_SECRET="whsec_..."
$env:NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY="pk_test_..."

.\scripts\configure-stripe-payment.ps1 -Profile "local"
```

### Development Environment (`dev` profile)

```powershell
$env:PAYMENT_ENCRYPTION_KEY="<dev-encryption-key>"
$env:STRIPE_SECRET_KEY="sk_test_..."  # Test mode key
$env:STRIPE_WEBHOOK_SECRET="whsec_..."
$env:NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY="pk_test_..."

.\scripts\configure-stripe-payment.ps1 -Profile "dev"
```

### Production Environment

For production, use **live mode** Stripe keys and ensure the encryption key is stored securely (e.g., AWS Secrets Manager, Azure Key Vault, etc.).

## Security Best Practices

1. ✅ **Never commit encryption keys or Stripe credentials to version control**
2. ✅ **Use different encryption keys for each environment** (dev, staging, prod)
3. ✅ **Rotate encryption keys periodically** (requires re-encrypting all credentials)
4. ✅ **Use environment variables** for sensitive configuration in production
5. ✅ **Restrict database access** to authorized personnel only
6. ✅ **Monitor Stripe API usage** in the Stripe dashboard

## Additional Notes

- The Stripe Price ID (`NEXT_PUBLIC_STRIPE_PRO_PRICE_ID`) is **NOT** stored in `payment_provider_config`. It's stored in the `membership_plan` table per plan.
- The configuration script supports multiple tenants - run it for each tenant that needs Stripe configured.
- The script will update existing configurations if they already exist for the tenant.

## Related Files

- `src/main/java/com/eventsitemanager/service/payment/config/StripeConfigSetupRunner.java` - Java configuration runner
- `src/main/java/com/eventsitemanager/service/payment/encryption/PaymentCredentialEncryptionService.java` - Encryption service
- `src/main/java/com/eventsitemanager/domain/PaymentProviderConfig.java` - Database entity
- `src/main/java/com/eventsitemanager/service/payment/adapter/StripePaymentAdapter.java` - Stripe payment adapter
