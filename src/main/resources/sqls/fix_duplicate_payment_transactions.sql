-- SQL Script to Identify and Fix Duplicate Primary Keys in user_payment_transaction table
--
-- This script helps identify and fix duplicate primary key violations in the user_payment_transaction table.
-- Duplicate primary keys should not exist and indicate a database integrity issue.
--
-- Run this script to:
-- 1. Identify duplicate IDs
-- 2. See which rows are duplicates
-- 3. Get recommendations for fixing them

-- Step 1: Find all duplicate IDs
SELECT
    id,
    COUNT(*) as duplicate_count
FROM user_payment_transaction
GROUP BY id
HAVING COUNT(*) > 1
ORDER BY id;

-- Step 2: View all duplicate rows for a specific ID (replace 4351 with the actual duplicate ID)
-- This shows all rows with the same ID so you can decide which one to keep
SELECT
    id,
    tenant_id,
    transaction_type,
    amount,
    currency,
    status,
    stripe_payment_intent_id,
    external_transaction_id,
    event_id,
    created_at,
    updated_at
FROM user_payment_transaction
WHERE id = 4351  -- Replace with the duplicate ID found in Step 1
ORDER BY created_at ASC;

-- Step 3: Fix duplicates by keeping the oldest record and updating newer duplicates with new IDs
-- WARNING: This will modify data. Review carefully before running.
--
-- For each duplicate ID found, you need to:
-- 1. Keep the oldest record (lowest created_at)
-- 2. Update other duplicates to use new IDs from the sequence
--
-- Example fix for ID 4351 (replace with actual duplicate ID):
--
-- BEGIN;
--
-- -- Get the oldest record (this one we'll keep)
-- SELECT id, created_at
-- FROM user_payment_transaction
-- WHERE id = 4351
-- ORDER BY created_at ASC
-- LIMIT 1;
--
-- -- For other duplicates, assign new IDs
-- -- Option A: Delete duplicates (if they're truly duplicates)
-- DELETE FROM user_payment_transaction
-- WHERE id = 4351
-- AND created_at > (SELECT MIN(created_at) FROM user_payment_transaction WHERE id = 4351);
--
-- -- Option B: Update duplicates to use new sequence IDs (if they're different transactions)
-- -- First, get the next sequence value
-- SELECT nextval('sequence_generator');
--
-- -- Then update the duplicate (replace NEW_ID with the sequence value)
-- UPDATE user_payment_transaction
-- SET id = NEW_ID
-- WHERE id = 4351
-- AND created_at > (SELECT MIN(created_at) FROM user_payment_transaction WHERE id = 4351)
-- LIMIT 1;
--
-- COMMIT;

-- Step 4: Verify no duplicates remain
SELECT
    id,
    COUNT(*) as count
FROM user_payment_transaction
GROUP BY id
HAVING COUNT(*) > 1;

-- Step 5: Check sequence generator is ahead of max ID
SELECT
    (SELECT MAX(id) FROM user_payment_transaction) as max_id,
    (SELECT last_value FROM sequence_generator) as sequence_last_value,
    CASE
        WHEN (SELECT MAX(id) FROM user_payment_transaction) > (SELECT last_value FROM sequence_generator)
        THEN 'WARNING: Max ID exceeds sequence value - sequence may need reset'
        ELSE 'OK: Sequence is ahead of max ID'
    END as status;

-- Step 6: If sequence needs reset (only if max_id > sequence_last_value):
-- SELECT setval('sequence_generator', (SELECT MAX(id) FROM user_payment_transaction), true);

