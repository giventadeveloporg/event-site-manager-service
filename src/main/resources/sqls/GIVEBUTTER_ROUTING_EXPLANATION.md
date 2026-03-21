# Givebutter Routing Explanation

## Key Findings

### 1. Database Schema Corrections

**Table: `payment_provider_config`**

- Field name: `provider_name` (NOT `provider_type`)
- Field type: VARCHAR(50) with CHECK constraint
- Unique constraint: `(tenant_id, provider_name)` - **This means only ONE config per tenant+provider**
- `payment_use_case` can be NULL (applies to all use cases) or a specific enum value

**Table: `event_details`**

- Field name: `metadata`
- Field type: **TEXT** (NOT JSONB)
- Stores valid JSON as a string
- Java code parses it using `ObjectMapper.readValue()` in `getMetadataAsMap()`

### 2. Provider Lookup Flow

When a ticket purchase happens:

1. **Initial Provider List** (`PaymentOrchestrationService.initialize()` line 92-95):

   ```java
   List<Map<String, Object>> providerConfigs = configService.getActiveProviderConfigs(
       request.getTenantId(),
       request.getPaymentUseCase()  // PaymentUseCase.TICKET_SALE
   );
   ```

   - Calls: `findByTenantIdAndPaymentUseCaseAndIsActiveTrueOrderByFallbackOrderAsc(tenantId, TICKET_SALE)`
   - Returns configs where: `payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL`
   - **GIVEBUTTER won't be in this list if `payment_use_case = 'DONATION_ZERO_FEE'`**

2. **Event Metadata Check** (lines 122-209):

   ```java
   Map<String, Object> eventMetadata = event.getMetadataAsMap();
   Boolean isFundraiserEvent = (Boolean) eventMetadata.get("isFundraiserEvent");
   Map<String, Object> donationConfig = (Map<String, Object>) eventMetadata.get("donationConfig");
   ```

   - Checks for `isFundraiserEvent: true`
   - Checks for `donationConfig.useZeroFeeProvider: true`
   - Checks for `donationConfig.zeroFeeProvider: "GIVEBUTTER"`

3. **Givebutter Provider Lookup** (line 154):

   ```java
   Optional<Map<String, Object>> givebutterConfigOptional = configService
       .getProviderConfig(request.getTenantId(), PaymentProvider.GIVEBUTTER);
   ```

   - Calls: `findByTenantIdAndProviderName(tenantId, GIVEBUTTER)`
   - **This finds ANY Givebutter config, regardless of payment_use_case**
   - But it only adds to `providerConfigs` if the event metadata matches

4. **Provider Selection** (lines 219-244):
   - Tries each provider in `providerConfigs` order until one succeeds
   - If Givebutter was added to the front, it's tried first

### 3. Why Transaction 4407 Used Stripe

**Root Causes:**

1. ❌ Event metadata missing `donationConfig` section
2. ❌ Givebutter config has `payment_use_case = 'DONATION_ZERO_FEE'` (not NULL, not 'TICKET_SALE')
3. ❌ When `getActiveProviderConfigs(tenantId, TICKET_SALE)` was called, Givebutter wasn't returned
4. ❌ Routing logic at line 154 found Givebutter config, but it wasn't in `providerConfigs` list
5. ❌ So Stripe (which has `payment_use_case = NULL`) was used instead

## Solutions

### Solution 1: Update Event Metadata (Required)

Add `donationConfig` to event metadata:

```sql
UPDATE event_details
SET metadata = (
    SELECT jsonb_pretty(
        jsonb_build_object(
            'isFundraiserEvent', COALESCE((metadata::jsonb->>'isFundraiserEvent')::boolean, false),
            'isCharityEvent', COALESCE((metadata::jsonb->>'isCharityEvent')::boolean, false),
            'donationConfig', jsonb_build_object(
                'useZeroFeeProvider', true,
                'zeroFeeProvider', 'GIVEBUTTER',
                'givebutterCampaignId', 'YOUR_CAMPAIGN_ID_HERE'
            )
        )
    )::text
    FROM event_details e2
    WHERE e2.id = event_details.id
)
WHERE id = 4101;
```

### Solution 2: Fix Givebutter Provider Config (Required)

**Option A: Set payment_use_case to NULL** (Recommended)

- This makes Givebutter available for ALL use cases including TICKET_SALE

```sql
UPDATE payment_provider_config
SET payment_use_case = NULL,
    updated_at = NOW()
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER';
```

**Option B: Add separate config for TICKET_SALE**

- Only works if unique constraint allows it (check your DB)
- The constraint `unique_tenant_provider` is on `(tenant_id, provider_name)` only
- So you CAN have multiple configs with same tenant+provider but different payment_use_case
- But the code lookup might not work as expected

### Solution 3: Update Database Constraint (If Needed)

If you get an error inserting GIVEBUTTER:

```sql
ALTER TABLE payment_provider_config
DROP CONSTRAINT IF EXISTS check_provider_name;

ALTER TABLE payment_provider_config
ADD CONSTRAINT check_provider_name
CHECK (provider_name IN ('STRIPE', 'PAYPAL', 'ZEFFY', 'ZELLE_MANUAL', 'REVOLUT', 'CEFI_CHARITY', 'GIVEBUTTER'));
```

## Verification

After making changes, verify:

1. **Event Metadata:**

```sql
SELECT
    id,
    metadata,
    (metadata::jsonb->>'isFundraiserEvent')::boolean as is_fundraiser,
    metadata::jsonb->'donationConfig'->>'zeroFeeProvider' as zero_fee_provider
FROM event_details
WHERE id = 4101;
```

2. **Provider Config:**

```sql
SELECT
    provider_name,
    payment_use_case,
    is_active,
    fallback_order
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND provider_name = 'GIVEBUTTER';
```

3. **Provider Selection for TICKET_SALE:**

```sql
SELECT
    provider_name,
    payment_use_case,
    fallback_order,
    CASE
        WHEN payment_use_case = 'TICKET_SALE' THEN 'Direct match'
        WHEN payment_use_case IS NULL THEN 'Applies to all (including TICKET_SALE)'
        ELSE 'Does not apply'
    END as match_type
FROM payment_provider_config
WHERE tenant_id = 'tenant_demo_002'
  AND is_active = true
  AND (payment_use_case = 'TICKET_SALE' OR payment_use_case IS NULL)
ORDER BY
    CASE WHEN payment_use_case = 'TICKET_SALE' THEN 0 ELSE 1 END,
    fallback_order;
```

## Expected Behavior After Fixes

1. ✅ Event 4101 has `donationConfig` with `zeroFeeProvider: "GIVEBUTTER"`
2. ✅ Givebutter config has `payment_use_case = NULL` (applies to all use cases)
3. ✅ When `getActiveProviderConfigs(tenantId, TICKET_SALE)` is called, Givebutter is returned
4. ✅ Routing logic detects fundraiser event and prioritizes Givebutter
5. ✅ Givebutter is tried first, Stripe is fallback
6. ✅ Log shows: "Routing fundraiser ticket sale event 4101 to Givebutter"
