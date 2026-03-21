-- =====================================================
-- Multi-Tenant Membership System Migration
-- Date: October 16, 2025
-- Purpose: Enable users to belong to multiple tenants
-- =====================================================

-- 1. Create tenant_memberships table
CREATE TABLE IF NOT EXISTS tenant_memberships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'member',
    permissions JSONB DEFAULT '[]'::jsonb,
    status VARCHAR(50) NOT NULL DEFAULT 'active',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    invited_by UUID,
    invited_at TIMESTAMP,
    last_accessed_at TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP,
    CONSTRAINT fk_tenant_membership_user FOREIGN KEY (user_id) REFERENCES jhi_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_tenant_membership_invited_by FOREIGN KEY (invited_by) REFERENCES jhi_user(id) ON DELETE SET NULL,
    CONSTRAINT unique_user_tenant UNIQUE(user_id, tenant_id)
);

-- 2. Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_tenant_memberships_user_id ON tenant_memberships(user_id);
CREATE INDEX IF NOT EXISTS idx_tenant_memberships_tenant_id ON tenant_memberships(tenant_id);
CREATE INDEX IF NOT EXISTS idx_tenant_memberships_status ON tenant_memberships(status);
CREATE INDEX IF NOT EXISTS idx_tenant_memberships_user_tenant ON tenant_memberships(user_id, tenant_id);

-- 3. Add comments for documentation
COMMENT ON TABLE tenant_memberships IS 'Maps users to tenants with role-based access control';
COMMENT ON COLUMN tenant_memberships.user_id IS 'Reference to the user';
COMMENT ON COLUMN tenant_memberships.tenant_id IS 'Tenant identifier (from X-Tenant-Id header)';
COMMENT ON COLUMN tenant_memberships.role IS 'User role within tenant: admin, member, viewer, etc.';
COMMENT ON COLUMN tenant_memberships.permissions IS 'Additional JSON permissions specific to this tenant';
COMMENT ON COLUMN tenant_memberships.status IS 'Membership status: active, suspended, pending_invitation';
COMMENT ON COLUMN tenant_memberships.joined_at IS 'When user joined this tenant';
COMMENT ON COLUMN tenant_memberships.invited_by IS 'User ID of who invited this member (NULL for self-registration)';
COMMENT ON COLUMN tenant_memberships.last_accessed_at IS 'Last time user accessed resources in this tenant';

-- 4. Migrate existing user tenant associations (if users table has tenant_id column)
-- Uncomment if you have existing users with a single tenant_id column
/*
INSERT INTO tenant_memberships (user_id, tenant_id, role, joined_at, created_date)
SELECT
    id as user_id,
    tenant_id,
    'admin' as role,  -- Default existing users to admin role
    created_date as joined_at,
    CURRENT_TIMESTAMP as created_date
FROM jhi_user
WHERE tenant_id IS NOT NULL
ON CONFLICT (user_id, tenant_id) DO NOTHING;
*/

-- 5. Optional: Remove tenant_id from users table (after migration)
-- Only run this after verifying tenant_memberships are created correctly
-- ALTER TABLE jhi_user DROP COLUMN IF EXISTS tenant_id;

-- 6. Create function to automatically update last_accessed_at
CREATE OR REPLACE FUNCTION update_tenant_membership_last_accessed()
RETURNS TRIGGER AS $$
BEGIN
    -- This function can be called from application triggers when user accesses tenant resources
    NEW.last_accessed_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 7. Create view for active tenant memberships (optional, for easier querying)
CREATE OR REPLACE VIEW active_tenant_memberships AS
SELECT
    tm.*,
    u.login as user_login,
    u.email as user_email,
    u.first_name,
    u.last_name
FROM tenant_memberships tm
JOIN jhi_user u ON tm.user_id = u.id
WHERE tm.status = 'active';

COMMENT ON VIEW active_tenant_memberships IS 'View of active tenant memberships with user details';

-- =====================================================
-- Verification Queries (Run these to verify migration)
-- =====================================================

-- Check if table was created
SELECT COUNT(*) as tenant_memberships_count FROM tenant_memberships;

-- Check indexes
SELECT indexname, tablename FROM pg_indexes WHERE tablename = 'tenant_memberships';

-- Check constraints
SELECT conname, contype FROM pg_constraint WHERE conrelid = 'tenant_memberships'::regclass;

-- =====================================================
-- Sample Data (Optional - for testing)
-- =====================================================

-- Example: Create a test membership (uncomment to use)
/*
INSERT INTO tenant_memberships (user_id, tenant_id, role, status)
VALUES (
    (SELECT id FROM jhi_user LIMIT 1),  -- First user in system
    'tenant_demo_001',
    'admin',
    'active'
);
*/

-- =====================================================
-- Rollback Script (Keep for emergency)
-- =====================================================

-- To rollback this migration (BE CAREFUL - DELETES DATA):
/*
DROP VIEW IF EXISTS active_tenant_memberships;
DROP FUNCTION IF EXISTS update_tenant_membership_last_accessed();
DROP TABLE IF EXISTS tenant_memberships CASCADE;
*/
