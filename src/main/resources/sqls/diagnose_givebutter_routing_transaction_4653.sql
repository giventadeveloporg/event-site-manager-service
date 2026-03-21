-- Diagnostic SQL to check why transaction 4653 used Stripe instead of Givebutter
-- Run this to verify the configuration and metadata

-- 1. Check transaction 4653 details and provider stored in metadata
SELECT
    id,
    tenant_id,
    event_id,
    transaction_type,
    amount,
    status,
    stripe_payment_intent_id,
    external_transaction_id,
    -- Extract provider from metadata JSON
    CASE
        WHEN metadata IS NOT NULL AND metadata LIKE '%"provider":"GIVEBUTTER"%' THEN 'GIVEBUTTER'
        WHEN metadata IS NOT NULL AND metadata LIKE '%"provider":"STRIPE"%' THEN 'STRIPE'
        WHEN stripe_payment_intent_id LIKE 'pi_%' THEN 'STRIPE (legacy)'
        ELSE 'UNKNOWN'
    END as detected_provider,
    -- Show metadata JSON
    metadata
FROM user_payment_transaction
WHERE id = 4653;

-- 2. Check event 4101 metadata for Givebutter routing configuration
SELECT
    id,
    title,
    tenant_id,
    metadata,
    -- Extract individual fields
    CASE
        WHEN metadata IS NOT NULL AND metadata LIKE '%"isFundraiserEvent":true%' THEN true
        ELSE false
    END as is_fundraiser_event,
    CASE
        WHEN metadata IS NOT NULL AND metadata LIKE '%"isCharityEvent":true%' THEN true
        ELSE false
    END as is_charity_event,
    -- Check if donationConfig exists
    CASE
        WHEN metadata IS NOT NULL AND metadata LIKE '%"donationConfig"%' THEN 'YES'
        ELSE 'NO'
    END as has_donation_config,
    -- Extract donationConfig fields (if metadata is valid JSON)
    CASE
        WHEN metadata IS NOT NULL AND metadata LIKE '%"useZeroFeeProvider":true%' THEN true
        ELSE false
    END as use_zero_fee_provider,
    CASE
        WHEN metadata LIKE '%"zeroFeeProvider":"GIVEBUTTER"%' THEN 'GIVEBUTTER'
        WHEN metadata LIKE '%"zeroFeeProvider":"STRIPE"%' THEN 'STRIPE'
        ELSE NULL
    END as zero_fee_provider
FROM event_details
WHERE id = 4101;

-- 3. Check Givebutter provider configuration for tenant_demo_002
SELECT
    id,
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    CASE
        WHEN payment_use_case IS NULL THEN 'ALL_USE_CASES'
        ELSE payment_use_case
    END as applies_to_use_case
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER'
  AND is_active = true;

-- 4. Check all active payment providers for tenant_demo_002 and TICKET_SALE use case
SELECT
    id,
    tenant_id,
    provider_name,
    payment_use_case,
    is_active,
    fallback_order,
    CASE
        WHEN payment_use_case IS NULL THEN 'ALL_USE_CASES'
        ELSE payment_use_case
    END as applies_to_use_case
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND is_active = true
  AND (payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL)
ORDER BY fallback_order;

-- Expected Results:
-- 1. Transaction 4653 should show provider="STRIPE" in metadata (confirming Stripe was used)
-- 2. Event 4101 should have:
--    - isFundraiserEvent: true
--    - has_donationConfig: YES
--    - useZeroFeeProvider: true
--    - zeroFeeProvider: GIVEBUTTER
-- 3. Givebutter provider should exist for tenant_demo_002 with payment_use_case = NULL (for all use cases)
-- 4. Both STRIPE and GIVEBUTTER should be in the list, with GIVEBUTTER having lower fallback_order (higher priority)

