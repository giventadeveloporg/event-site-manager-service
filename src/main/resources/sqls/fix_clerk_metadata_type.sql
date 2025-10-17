-- ===================================================
-- FIX CLERK_METADATA COLUMN TYPE
-- Change from JSONB to TEXT to avoid Hibernate type issues
-- Date: October 14, 2025
-- ===================================================

-- Change the column type from JSONB to TEXT
ALTER TABLE public.user_profile
ALTER COLUMN clerk_metadata TYPE TEXT;

-- Verify the change
-- SELECT column_name, data_type
-- FROM information_schema.columns
-- WHERE table_name = 'user_profile' AND column_name = 'clerk_metadata';
