-- Migration to change email_type from custom enum to varchar
-- This allows more flexibility and simpler ORM mapping

-- Drop the NOT NULL constraint temporarily if needed
ALTER TABLE tenant_email_addresses
ALTER COLUMN email_type DROP NOT NULL;

-- Change the column type from tenant_email_type enum to varchar(255)
-- PostgreSQL will automatically cast the enum values to strings
ALTER TABLE tenant_email_addresses
ALTER COLUMN email_type TYPE character varying(255) USING email_type::text;

-- Restore the NOT NULL constraint
ALTER TABLE tenant_email_addresses
ALTER COLUMN email_type SET NOT NULL;

-- Optional: Drop the enum type if no longer needed by other tables
-- DROP TYPE IF EXISTS public.tenant_email_type;








