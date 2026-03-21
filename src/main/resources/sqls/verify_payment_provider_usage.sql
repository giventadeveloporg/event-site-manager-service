-- SQL Queries to Verify Payment Provider Used for Transactions
--
-- This script helps verify which payment provider was used for a specific transaction.
-- Use these queries to check if Givebutter or Stripe was used.

-- ============================================================================
-- QUERY 1: Check Payment Provider for Transaction ID 4407
-- ============================================================================
-- Replace 4407 with your actual transaction ID
SELECT
    id,
    tenant_id,
    transaction_type,
    amount,
    currency,
    status,
    payment_method,
    stripe_payment_intent_id,
    external_transaction_id,
    -- Extract provider from metadata JSON
    CASE
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"provider"%' THEN
            metadata::jsonb->>'provider'
        WHEN stripe_payment_intent_id LIKE 'pi_%' THEN
            'STRIPE'  -- Stripe payment intent IDs start with 'pi_'
        WHEN external_transaction_id IS NOT NULL AND stripe_payment_intent_id IS NULL THEN
            'GIVEBUTTER'  -- Givebutter uses external_transaction_id, stored in stripe_payment_intent_id for compatibility
        ELSE
            'UNKNOWN'
    END as detected_provider,
    -- Show full metadata for inspection
    metadata,
    created_at,
    updated_at
FROM user_payment_transaction
WHERE id = 4407;  -- Replace with your transaction ID

-- ============================================================================
-- QUERY 2: Check All Recent Ticket Purchases for Event 4101
-- ============================================================================
SELECT
    upt.id as payment_transaction_id,
    upt.tenant_id,
    upt.transaction_type,
    upt.amount,
    upt.status,
    -- Extract provider from metadata
    CASE
        WHEN upt.metadata IS NOT NULL AND upt.metadata::text LIKE '%"provider"%' THEN
            upt.metadata::jsonb->>'provider'
        WHEN upt.stripe_payment_intent_id LIKE 'pi_%' THEN
            'STRIPE'
        WHEN upt.external_transaction_id IS NOT NULL AND upt.stripe_payment_intent_id NOT LIKE 'pi_%' THEN
            'GIVEBUTTER'
        ELSE
            'UNKNOWN'
    END as detected_provider,
    upt.stripe_payment_intent_id,
    upt.external_transaction_id,
    upt.payment_method,
    upt.metadata,
    ett.id as ticket_transaction_id,
    ett.stripe_payment_intent_id as ticket_payment_reference,
    upt.created_at
FROM user_payment_transaction upt
LEFT JOIN event_ticket_transaction ett ON ett.transaction_reference = upt.id::text
WHERE upt.event_id = 4101
  AND upt.transaction_type = 'TICKET_SALE'
ORDER BY upt.created_at DESC
LIMIT 10;

-- ============================================================================
-- QUERY 3: Check Givebutter vs Stripe Usage for Event 4101
-- ============================================================================
SELECT
    CASE
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"provider":"GIVEBUTTER"%' THEN 'GIVEBUTTER'
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"provider":"STRIPE"%' THEN 'STRIPE'
        WHEN stripe_payment_intent_id LIKE 'pi_%' THEN 'STRIPE'
        WHEN external_transaction_id IS NOT NULL AND stripe_payment_intent_id NOT LIKE 'pi_%' THEN 'GIVEBUTTER'
        ELSE 'UNKNOWN'
    END as provider,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount
FROM user_payment_transaction
WHERE event_id = 4101
  AND transaction_type = 'TICKET_SALE'
GROUP BY
    CASE
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"provider":"GIVEBUTTER"%' THEN 'GIVEBUTTER'
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"provider":"STRIPE"%' THEN 'STRIPE'
        WHEN stripe_payment_intent_id LIKE 'pi_%' THEN 'STRIPE'
        WHEN external_transaction_id IS NOT NULL AND stripe_payment_intent_id NOT LIKE 'pi_%' THEN 'GIVEBUTTER'
        ELSE 'UNKNOWN'
    END
ORDER BY provider;

-- ============================================================================
-- QUERY 4: Detailed Check for Transaction 4407 (Latest Ticket Purchase)
-- ============================================================================
-- This query shows all relevant information for the latest transaction
SELECT
    'Payment Transaction' as record_type,
    upt.id,
    upt.tenant_id,
    upt.transaction_type,
    upt.amount,
    upt.currency,
    upt.status,
    upt.payment_method,
    upt.stripe_payment_intent_id,
    upt.external_transaction_id,
    CASE
        WHEN upt.metadata IS NOT NULL AND upt.metadata::text LIKE '%"provider":"GIVEBUTTER"%' THEN 'GIVEBUTTER'
        WHEN upt.metadata IS NOT NULL AND upt.metadata::text LIKE '%"provider":"STRIPE"%' THEN 'STRIPE'
        WHEN upt.stripe_payment_intent_id LIKE 'pi_%' THEN 'STRIPE'
        WHEN upt.external_transaction_id IS NOT NULL AND upt.stripe_payment_intent_id NOT LIKE 'pi_%' THEN 'GIVEBUTTER'
        ELSE 'UNKNOWN - Check metadata'
    END as provider,
    upt.metadata::text as metadata_json,
    upt.created_at
FROM user_payment_transaction upt
WHERE upt.id = 4407

UNION ALL

SELECT
    'Ticket Transaction' as record_type,
    ett.id,
    ett.tenant_id,
    'TICKET' as transaction_type,
    ett.total_amount as amount,
    'USD' as currency,
    ett.status,
    ett.payment_method,
    ett.stripe_payment_intent_id,
    NULL as external_transaction_id,
    CASE
        WHEN ett.stripe_payment_intent_id LIKE 'pi_%' THEN 'STRIPE'
        WHEN ett.stripe_payment_intent_id IS NOT NULL AND ett.stripe_payment_intent_id NOT LIKE 'pi_%' THEN 'GIVEBUTTER (donation ID)'
        ELSE 'UNKNOWN'
    END as provider,
    NULL as metadata_json,
    ett.created_at
FROM event_ticket_transaction ett
WHERE ett.transaction_reference = '4407'
   OR ett.stripe_payment_intent_id = (SELECT stripe_payment_intent_id FROM user_payment_transaction WHERE id = 4407)
ORDER BY created_at;

-- ============================================================================
-- QUERY 5: Check Event Metadata for Givebutter Configuration
-- ============================================================================
-- Verify that event 4101 is configured for Givebutter
SELECT
    id,
    title,
    tenant_id,
    metadata,
    -- Extract fundraiser flags
    CASE
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"isFundraiserEvent":true%' THEN 'YES'
        ELSE 'NO'
    END as is_fundraiser_event,
    CASE
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"isCharityEvent":true%' THEN 'YES'
        ELSE 'NO'
    END as is_charity_event,
    -- Extract Givebutter configuration
    CASE
        WHEN metadata IS NOT NULL AND metadata::text LIKE '%"zeroFeeProvider":"GIVEBUTTER"%' THEN 'YES'
        ELSE 'NO'
    END as givebutter_configured,
    -- Extract campaign ID if present
    CASE
        WHEN metadata IS NOT NULL THEN
            metadata::jsonb->'donationConfig'->>'givebutterCampaignId'
        ELSE NULL
    END as givebutter_campaign_id
FROM event_details
WHERE id = 4101;

-- ============================================================================
-- QUERY 6: Check Payment Provider Configuration for Tenant
-- ============================================================================
-- Verify Givebutter is configured and active for the tenant
SELECT
    id,
    tenant_id,
    provider_name,
    provider_type,
    payment_use_case,
    is_active,
    fallback_order,
    created_at
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND provider_type = 'GIVEBUTTER'
ORDER BY fallback_order;

-- ============================================================================
-- INTERPRETATION GUIDE
-- ============================================================================
--
-- To determine if Givebutter was used:
--
-- 1. Check metadata JSON for "provider":"GIVEBUTTER"
-- 2. Check stripe_payment_intent_id:
--    - If it starts with "pi_", it's STRIPE
--    - If it's a UUID or other format, it might be Givebutter donation ID
-- 3. Check external_transaction_id:
--    - If present and stripe_payment_intent_id doesn't start with "pi_", likely Givebutter
--
-- For Transaction 4407:
-- - If stripe_payment_intent_id = 'pi_3SUVyGK5BrggeAHM0Gb3zVav' → STRIPE was used
-- - If metadata contains "provider":"GIVEBUTTER" → Givebutter was used
-- - If external_transaction_id is set and not a Stripe ID → Givebutter was used
--
-- Expected Givebutter indicators:
-- - metadata JSON contains: "provider":"GIVEBUTTER"
-- - stripe_payment_intent_id contains Givebutter donation ID (not starting with "pi_")
-- - external_transaction_id contains Givebutter donation ID
-- - payment_method might be "GIVEBUTTER" or similar

