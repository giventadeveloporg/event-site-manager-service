-- SQL Script to Fix Givebutter Routing for Event 4101
--
-- This script fixes the configuration issues preventing Givebutter from being used for ticket sales.
--
-- Issues Found:
-- 1. Event 4101 metadata is missing donationConfig section
-- 2. Givebutter provider is not configured for TICKET_SALE use case
-- 3. Database constraint may need updating to allow GIVEBUTTER in provider_name
--
-- IMPORTANT NOTES:
-- - The metadata field in event_details is TEXT (not JSONB), but stores valid JSON
-- - The provider_name field is used (not provider_type)
-- - The unique constraint is on (tenant_id, provider_name) - this means you can only have ONE config per tenant+provider
--   If you need different configs for different use cases, you'll need to handle this differently
--
-- Run these queries to fix the configuration:

-- ============================================================================
-- STEP 0: Verify Current State
-- ============================================================================
-- Check current event metadata
SELECT id, title, metadata FROM event_details WHERE id = 4101;

-- Check current provider configs
SELECT
    id,
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
ORDER BY provider_name, payment_use_case;

-- ============================================================================
-- STEP 1: Update Database Constraint to Allow GIVEBUTTER (if needed)
-- ============================================================================
-- Check if constraint allows GIVEBUTTER
-- If you get an error when inserting GIVEBUTTER, run this:
--
-- ALTER TABLE payment_provider_config
-- DROP CONSTRAINT IF EXISTS check_provider_name;
--
-- ALTER TABLE payment_provider_config
-- ADD CONSTRAINT check_provider_name
-- CHECK (provider_name IN ('STRIPE', 'PAYPAL', 'ZEFFY', 'ZELLE_MANUAL', 'REVOLUT', 'CEFI_CHARITY', 'GIVEBUTTER'));

-- ============================================================================
-- FIX 1: Update Event 4101 Metadata to Include donationConfig
-- ============================================================================
-- This adds the donationConfig section required for Givebutter routing
-- IMPORTANT: Replace "your_campaign_id_here" with your actual Givebutter campaign ID

-- Since metadata is TEXT (not JSONB), we need to use string manipulation or cast to JSONB
-- Option 1: Using JSONB cast (PostgreSQL will handle TEXT->JSONB conversion)
UPDATE event_details
SET metadata = (
    SELECT jsonb_pretty(
        jsonb_build_object(
            'isFundraiserEvent', COALESCE((metadata::jsonb->>'isFundraiserEvent')::boolean, false),
            'isCharityEvent', COALESCE((metadata::jsonb->>'isCharityEvent')::boolean, false),
            'donationConfig', jsonb_build_object(
                'useZeroFeeProvider', true,
                'zeroFeeProvider', 'GIVEBUTTER',
                'givebutterCampaignId', 'your_campaign_id_here'  -- REPLACE WITH ACTUAL CAMPAIGN ID
            )
        )
    )::text
    FROM event_details e2
    WHERE e2.id = event_details.id
)
WHERE id = 4101;

-- Option 2: Simple string replacement (if metadata is simple)
-- UPDATE event_details
-- SET metadata = '{"isFundraiserEvent":true,"isCharityEvent":false,"donationConfig":{"useZeroFeeProvider":true,"zeroFeeProvider":"GIVEBUTTER","givebutterCampaignId":"your_campaign_id_here"}}'
-- WHERE id = 4101;

-- Verify the update:
SELECT
    id,
    title,
    metadata,
    -- Extract values using JSONB cast
    (metadata::jsonb->>'isFundraiserEvent')::boolean as is_fundraiser,
    (metadata::jsonb->>'isCharityEvent')::boolean as is_charity,
    metadata::jsonb->'donationConfig'->>'useZeroFeeProvider' as use_zero_fee_provider,
    metadata::jsonb->'donationConfig'->>'zeroFeeProvider' as zero_fee_provider,
    metadata::jsonb->'donationConfig'->>'givebutterCampaignId' as campaign_id
FROM event_details
WHERE id = 4101;

-- ============================================================================
-- FIX 2: Add Givebutter Provider Configuration for TICKET_SALE Use Case
-- ============================================================================
-- IMPORTANT: The unique constraint is on (tenant_id, provider_name), NOT including payment_use_case
-- This means you can only have ONE Givebutter config per tenant
-- If you already have a Givebutter config for DONATION_ZERO_FEE, you have two options:
--   1. Update the existing config to have payment_use_case = NULL (applies to all use cases)
--   2. Delete the existing config and create a new one with payment_use_case = NULL
--   3. Or create separate configs if the unique constraint allows it (check your DB)

-- First, check if a Givebutter configuration already exists:
SELECT
    id,
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    configuration_json IS NOT NULL as has_config_json
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER';

-- Option A: If Givebutter config exists but payment_use_case is set to DONATION_ZERO_FEE
-- Update it to NULL so it applies to all use cases (including TICKET_SALE)
UPDATE payment_provider_config
SET payment_use_case = NULL,
    updated_at = NOW()
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER'
  AND payment_use_case = 'DONATION_ZERO_FEE';

-- Option B: If no Givebutter config exists, insert one
-- This copies the config from an existing Givebutter config (if you have one)
-- Replace 4100 with your actual Givebutter config ID, or manually insert values
INSERT INTO payment_provider_config (
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    provider_api_key_encrypted,
    webhook_secret_encrypted,
    configuration_json,
    created_at,
    updated_at
)
SELECT
    tenant_id,
    provider_name,
    NULL,  -- NULL means applies to all use cases
    is_active,
    0,  -- Set fallback_order to 0 to prioritize Givebutter
    provider_api_key_encrypted,  -- Copy encrypted credentials
    webhook_secret_encrypted,
    configuration_json,
    NOW(),
    NOW()
FROM payment_provider_config
WHERE id = 4100  -- Replace with your existing GIVEBUTTER config ID
  AND provider_name = 'GIVEBUTTER'
  AND NOT EXISTS (
      SELECT 1
      FROM payment_provider_config
      WHERE tenant_id = 'tenant_demo_002'
        AND provider_name = 'GIVEBUTTER'
  );

-- Option C: If you need to manually insert (replace encrypted values):
/*
INSERT INTO payment_provider_config (
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    provider_api_key_encrypted,
    webhook_secret_encrypted,
    configuration_json,
    created_at,
    updated_at
)
VALUES (
    'tenant_demo_002',
    'GIVEBUTTER',
    NULL,  -- NULL means applies to all use cases
    true,
    0,  -- Prioritize Givebutter
    'ENCRYPTED_API_KEY_HERE',  -- Replace with encrypted API key
    'ENCRYPTED_WEBHOOK_SECRET_HERE',  -- Replace with encrypted webhook secret
    '{}'::jsonb,  -- Or your config JSON
    NOW(),
    NOW()
);
*/

-- Verify the configuration:
SELECT
    id,
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    CASE
        WHEN payment_use_case IS NULL THEN 'Applies to all use cases'
        ELSE 'Specific use case: ' || payment_use_case::text
    END as scope,
    created_at
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER'
ORDER BY payment_use_case NULLS FIRST, fallback_order;

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================

-- 1. Verify Event Metadata (using JSONB cast for TEXT field)
SELECT
    id,
    title,
    metadata,
    (metadata::jsonb->>'isFundraiserEvent')::boolean as is_fundraiser,
    (metadata::jsonb->>'isCharityEvent')::boolean as is_charity,
    metadata::jsonb->'donationConfig'->>'useZeroFeeProvider' as use_zero_fee,
    metadata::jsonb->'donationConfig'->>'zeroFeeProvider' as zero_fee_provider,
    metadata::jsonb->'donationConfig'->>'givebutterCampaignId' as campaign_id
FROM event_details
WHERE id = 4101;

-- 2. Verify Givebutter Provider Configurations
SELECT
    id,
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    CASE
        WHEN payment_use_case IS NULL THEN 'Applies to all use cases (including TICKET_SALE)'
        WHEN payment_use_case = 'TICKET_SALE' THEN 'Specific: TICKET_SALE'
        WHEN payment_use_case = 'DONATION_ZERO_FEE' THEN 'Specific: DONATION_ZERO_FEE'
        ELSE 'Specific: ' || payment_use_case::text
    END as scope
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER'
ORDER BY
    CASE WHEN payment_use_case IS NULL THEN 0 ELSE 1 END,
    fallback_order;

-- 3. Check Provider Selection Order for TICKET_SALE
-- This simulates what the backend would see when initializing a TICKET_SALE payment
-- The backend calls: getActiveProviderConfigs(tenantId, PaymentUseCase.TICKET_SALE)
-- This returns configs where payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL
SELECT
    provider_name,
    payment_use_case,
    fallback_order,
    is_active,
    CASE
        WHEN payment_use_case = 'TICKET_SALE' THEN 'Direct match for TICKET_SALE'
        WHEN payment_use_case IS NULL THEN 'General purpose (applies to all use cases including TICKET_SALE)'
        ELSE 'Does not apply to TICKET_SALE'
    END as match_type
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND is_active = true
  AND (payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL)
ORDER BY
    CASE WHEN payment_use_case = 'TICKET_SALE' THEN 0 ELSE 1 END,
    fallback_order;

-- ============================================================================
-- EXPECTED RESULTS AFTER FIXES
-- ============================================================================
--
-- After running the fixes:
--
-- 1. Event 4101 metadata should contain:
--    {
--      "isFundraiserEvent": true,
--      "isCharityEvent": false,
--      "donationConfig": {
--        "useZeroFeeProvider": true,
--        "zeroFeeProvider": "GIVEBUTTER",
--        "givebutterCampaignId": "your_campaign_id_here"
--      }
--    }
--
-- 2. Payment provider config should have:
--    - GIVEBUTTER with payment_use_case = NULL (applies to all use cases including TICKET_SALE)
--    - OR GIVEBUTTER with payment_use_case = 'TICKET_SALE' (if unique constraint allows)
--    - STRIPE for general use (already exists)
--
-- 3. When initializing a TICKET_SALE payment for event 4101:
--    - Backend calls: getActiveProviderConfigs('tenant_demo_002', PaymentUseCase.TICKET_SALE)
--    - This returns configs where payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL
--    - Backend should detect fundraiser event with donationConfig
--    - Backend should find Givebutter in the providerConfigs list
--    - Backend should route to Givebutter instead of Stripe
--
-- ============================================================================
-- CODE FLOW EXPLANATION
-- ============================================================================
--
-- When a ticket purchase happens:
-- 1. PaymentOrchestrationService.initialize() is called with PaymentUseCase.TICKET_SALE
-- 2. Line 92-95: Calls configService.getActiveProviderConfigs(tenantId, PaymentUseCase.TICKET_SALE)
--    - This queries: findByTenantIdAndPaymentUseCaseAndIsActiveTrueOrderByFallbackOrderAsc(tenantId, TICKET_SALE)
--    - Returns configs where payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL
-- 3. Line 122-209: Checks event metadata for fundraiser/charity event configuration
--    - Line 127: Gets event metadata using event.getMetadataAsMap()
--    - Line 138-139: Checks isFundraiserEvent and isCharityEvent
--    - Line 142: Gets donationConfig from metadata
--    - Line 145-186: If TICKET_SALE and fundraiser event and donationConfig.useZeroFeeProvider = true
--      and donationConfig.zeroFeeProvider = "GIVEBUTTER", then:
--      - Line 154: Calls configService.getProviderConfig(tenantId, PaymentProvider.GIVEBUTTER)
--      - This queries: findByTenantIdAndProviderName(tenantId, GIVEBUTTER)
--      - Line 156-178: Adds Givebutter to front of providerConfigs list if not already present
-- 4. Line 219-244: Tries each provider in order until one succeeds
--
-- ============================================================================
-- IMPORTANT NOTES
-- ============================================================================
--
-- 1. Campaign ID: Replace "your_campaign_id_here" with your actual Givebutter campaign ID
-- 2. Encrypted Credentials: When inserting the new provider config, you need to use
--    encrypted API key and webhook secret. Copy these from the existing Givebutter config.
-- 3. Unique Constraint: The unique constraint on (tenant_id, provider_name) means you can
--    only have ONE Givebutter config per tenant. Set payment_use_case to NULL to apply to all use cases.
-- 4. Testing: After making these changes, test a new ticket purchase for event 4101
--    to verify Givebutter is used. Check logs for "Routing fundraiser ticket sale event 4101 to Givebutter"
