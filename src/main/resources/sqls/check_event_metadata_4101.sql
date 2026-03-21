-- SQL Query to Check Event 4101 Metadata
-- Run this to verify the metadata structure

SELECT
    id,
    title,
    metadata,
    -- Extract individual fields
    (metadata::jsonb->>'isFundraiserEvent')::boolean as is_fundraiser_event,
    (metadata::jsonb->>'isCharityEvent')::boolean as is_charity_event,
    -- Check if donationConfig exists
    CASE
        WHEN metadata::jsonb ? 'donationConfig' THEN 'YES'
        ELSE 'NO'
    END as has_donation_config,
    -- Extract donationConfig fields
    metadata::jsonb->'donationConfig'->>'useZeroFeeProvider' as use_zero_fee_provider,
    metadata::jsonb->'donationConfig'->>'zeroFeeProvider' as zero_fee_provider,
    metadata::jsonb->'donationConfig'->>'givebutterCampaignId' as campaign_id,
    -- Show full donationConfig
    metadata::jsonb->'donationConfig' as donation_config_json
FROM event_details
WHERE id = 4101;

-- Expected result after fix:
-- is_fundraiser_event: true
-- has_donation_config: YES
-- use_zero_fee_provider: true
-- zero_fee_provider: GIVEBUTTER
-- campaign_id: (your campaign ID)

