-- ===================================================
-- CLERK AUTHENTICATION INTEGRATION SCHEMA
-- Task ID: 2 - Database Schema Migration for Clerk
-- Date: October 13, 2025
-- STANDALONE MIGRATION SCRIPT (Safe to run independently)
-- ===================================================

-- ---------------------------------------------------
-- 1. ADD NEW CLERK COLUMNS TO EXISTING user_profile TABLE
-- ---------------------------------------------------
-- Adding new columns to support Clerk authentication integration
-- These columns store Clerk-specific data for user authentication

ALTER TABLE public.user_profile
    ADD COLUMN IF NOT EXISTS clerk_user_id VARCHAR(255),
    ADD COLUMN IF NOT EXISTS clerk_session_id VARCHAR(255),
    ADD COLUMN IF NOT EXISTS clerk_org_id VARCHAR(255),
    ADD COLUMN IF NOT EXISTS clerk_org_role VARCHAR(100),
    ADD COLUMN IF NOT EXISTS auth_provider VARCHAR(50),
    ADD COLUMN IF NOT EXISTS auth_provider_user_id VARCHAR(255),
    ADD COLUMN IF NOT EXISTS email_verified BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS profile_image_url_clerk VARCHAR(1024),
    ADD COLUMN IF NOT EXISTS last_sign_in_at TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN IF NOT EXISTS clerk_metadata JSONB;

-- Add column comments for documentation
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'clerk_user_id') THEN
        COMMENT ON COLUMN public.user_profile.clerk_user_id IS 'Clerk unique user identifier (e.g., user_2abc123def456)';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'clerk_session_id') THEN
        COMMENT ON COLUMN public.user_profile.clerk_session_id IS 'Current active Clerk session identifier';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'clerk_org_id') THEN
        COMMENT ON COLUMN public.user_profile.clerk_org_id IS 'Clerk Organization ID for role management (e.g., org_2abc123)';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'clerk_org_role') THEN
        COMMENT ON COLUMN public.user_profile.clerk_org_role IS 'Clerk Organization role (e.g., org:admin, org:member)';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'auth_provider') THEN
        COMMENT ON COLUMN public.user_profile.auth_provider IS 'Authentication provider: email, google, github, facebook, apple, etc.';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'auth_provider_user_id') THEN
        COMMENT ON COLUMN public.user_profile.auth_provider_user_id IS 'Provider-specific user ID (e.g., google_oauth2|123456789)';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'email_verified') THEN
        COMMENT ON COLUMN public.user_profile.email_verified IS 'Whether the user email has been verified via Clerk';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'profile_image_url_clerk') THEN
        COMMENT ON COLUMN public.user_profile.profile_image_url_clerk IS 'Clerk profile image URL (may differ from custom profile_image_url)';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'last_sign_in_at') THEN
        COMMENT ON COLUMN public.user_profile.last_sign_in_at IS 'Timestamp of last successful sign-in';
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_profile' AND column_name = 'clerk_metadata') THEN
        COMMENT ON COLUMN public.user_profile.clerk_metadata IS 'Additional Clerk user metadata stored as JSON (custom claims, attributes, etc.)';
    END IF;
END $$;

-- ---------------------------------------------------
-- 2. UPDATE UNIQUE CONSTRAINTS FOR MULTI-TENANT SUPPORT
-- ---------------------------------------------------
-- Drop old unique constraint on user_id (single tenant constraint)
-- Replace with composite unique constraint on (email, tenant_id) for multi-tenant support

ALTER TABLE public.user_profile DROP CONSTRAINT IF EXISTS ux_user_profile__user_id;

-- Add composite unique constraint for multi-tenant support
-- This allows same email across different tenants
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'uq_user_profile_email_tenant'
        AND table_name = 'user_profile'
    ) THEN
        ALTER TABLE public.user_profile
            ADD CONSTRAINT uq_user_profile_email_tenant UNIQUE (email, tenant_id);
    END IF;
END $$;

-- ---------------------------------------------------
-- 3. ADD INDEXES FOR CLERK COLUMNS ON user_profile
-- ---------------------------------------------------
-- Indexes to optimize Clerk-related queries

CREATE INDEX IF NOT EXISTS idx_user_profile_clerk_user_id
    ON public.user_profile(clerk_user_id);

CREATE INDEX IF NOT EXISTS idx_user_profile_tenant_email
    ON public.user_profile(tenant_id, email);

CREATE INDEX IF NOT EXISTS idx_user_profile_clerk_org_id
    ON public.user_profile(clerk_org_id);

CREATE INDEX IF NOT EXISTS idx_user_profile_auth_provider
    ON public.user_profile(auth_provider);

CREATE INDEX IF NOT EXISTS idx_user_profile_last_sign_in_at
    ON public.user_profile(last_sign_in_at DESC);

-- Add comments on indexes (safe - with exception handling)
DO $$
BEGIN
    BEGIN
        COMMENT ON INDEX idx_user_profile_clerk_user_id IS 'Index for fast Clerk user ID lookups';
    EXCEPTION WHEN undefined_table THEN
        RAISE NOTICE 'Index idx_user_profile_clerk_user_id does not exist, skipping comment';
    END;

    BEGIN
        COMMENT ON INDEX idx_user_profile_tenant_email IS 'Index for tenant-scoped email queries';
    EXCEPTION WHEN undefined_table THEN
        RAISE NOTICE 'Index idx_user_profile_tenant_email does not exist, skipping comment';
    END;

    BEGIN
        COMMENT ON INDEX idx_user_profile_clerk_org_id IS 'Index for organization-based queries';
    EXCEPTION WHEN undefined_table THEN
        RAISE NOTICE 'Index idx_user_profile_clerk_org_id does not exist, skipping comment';
    END;
END $$;

-- ---------------------------------------------------
-- 4. CREATE NEW TABLE: clerk_user_tenant
-- ---------------------------------------------------
-- Junction table to support users belonging to multiple tenants
-- A user can have different roles in different tenants

CREATE TABLE IF NOT EXISTS public.clerk_user_tenant (
    id BIGINT DEFAULT nextval('public.sequence_generator'::regclass) NOT NULL,
    user_profile_id BIGINT NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,
    role VARCHAR(100),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    joined_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT clerk_user_tenant_pkey PRIMARY KEY (id),
    CONSTRAINT uq_clerk_user_tenant UNIQUE (user_profile_id, tenant_id),
    CONSTRAINT fk_clerk_user_tenant_user_profile
        FOREIGN KEY (user_profile_id)
        REFERENCES public.user_profile(id)
        ON DELETE CASCADE
);

-- Indexes for clerk_user_tenant
CREATE INDEX IF NOT EXISTS idx_clerk_user_tenant_user
    ON public.clerk_user_tenant(user_profile_id);

CREATE INDEX IF NOT EXISTS idx_clerk_user_tenant_tenant
    ON public.clerk_user_tenant(tenant_id);

CREATE INDEX IF NOT EXISTS idx_clerk_user_tenant_status
    ON public.clerk_user_tenant(status);

-- Comments
COMMENT ON TABLE public.clerk_user_tenant IS 'Junction table for multi-tenant user relationships and tenant-specific roles';
COMMENT ON COLUMN public.clerk_user_tenant.user_profile_id IS 'Foreign key to user_profile table';
COMMENT ON COLUMN public.clerk_user_tenant.tenant_id IS 'Tenant identifier';
COMMENT ON COLUMN public.clerk_user_tenant.role IS 'Tenant-specific role (may differ from global user_role)';
COMMENT ON COLUMN public.clerk_user_tenant.status IS 'Status: ACTIVE, SUSPENDED, PENDING, INACTIVE';
COMMENT ON COLUMN public.clerk_user_tenant.joined_at IS 'When user joined this tenant';

-- ---------------------------------------------------
-- 5. CREATE NEW TABLE: clerk_organization_role
-- ---------------------------------------------------
-- Maps Clerk Organization roles to application roles
-- This table defines how Clerk org roles translate to app permissions

CREATE TABLE IF NOT EXISTS public.clerk_organization_role (
    id BIGINT DEFAULT nextval('public.sequence_generator'::regclass) NOT NULL,
    clerk_org_id VARCHAR(255) NOT NULL,
    clerk_role_name VARCHAR(100) NOT NULL,
    application_role VARCHAR(100) NOT NULL,
    permissions JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT clerk_organization_role_pkey PRIMARY KEY (id),
    CONSTRAINT uq_clerk_org_role UNIQUE (clerk_org_id, clerk_role_name)
);

-- Indexes for clerk_organization_role
CREATE INDEX IF NOT EXISTS idx_clerk_org_role_org
    ON public.clerk_organization_role(clerk_org_id);

CREATE INDEX IF NOT EXISTS idx_clerk_org_role_app_role
    ON public.clerk_organization_role(application_role);

-- Comments
COMMENT ON TABLE public.clerk_organization_role IS 'Maps Clerk Organization roles to application roles and permissions';
COMMENT ON COLUMN public.clerk_organization_role.clerk_org_id IS 'Clerk Organization ID (e.g., org_2abc123)';
COMMENT ON COLUMN public.clerk_organization_role.clerk_role_name IS 'Clerk role name (e.g., org:admin, org:member)';
COMMENT ON COLUMN public.clerk_organization_role.application_role IS 'Mapped application role (e.g., ROLE_ORG_ADMIN, ROLE_USER)';
COMMENT ON COLUMN public.clerk_organization_role.permissions IS 'JSON object containing role permissions';

-- Seed default role mappings
INSERT INTO public.clerk_organization_role (clerk_org_id, clerk_role_name, application_role, permissions)
VALUES
    ('org_default', 'org:admin', 'ROLE_ORG_ADMIN', '{"canManageUsers": true, "canManageEvents": true, "canViewReports": true, "canManageSettings": true}'::jsonb),
    ('org_default', 'org:member', 'ROLE_ORG_MEMBER', '{"canRegisterEvents": true, "canViewEvents": true, "canUpdateOwnProfile": true}'::jsonb),
    ('org_default', 'org:viewer', 'ROLE_VIEWER', '{"canViewEvents": true}'::jsonb)
ON CONFLICT (clerk_org_id, clerk_role_name) DO NOTHING;

-- ---------------------------------------------------
-- 6. CREATE NEW TABLE: clerk_webhook_event
-- ---------------------------------------------------
-- Audit trail and processing queue for all Clerk webhook events
-- Supports idempotency and retry logic

CREATE TABLE IF NOT EXISTS public.clerk_webhook_event (
    id BIGINT DEFAULT nextval('public.sequence_generator'::regclass) NOT NULL,
    event_id VARCHAR(255) UNIQUE NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    clerk_user_id VARCHAR(255),
    payload JSONB NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    processed_at TIMESTAMP WITHOUT TIME ZONE,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    received_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT clerk_webhook_event_pkey PRIMARY KEY (id)
);

-- Indexes for clerk_webhook_event
CREATE INDEX IF NOT EXISTS idx_clerk_webhook_event_type
    ON public.clerk_webhook_event(event_type);

CREATE INDEX IF NOT EXISTS idx_clerk_webhook_event_user
    ON public.clerk_webhook_event(clerk_user_id);

CREATE INDEX IF NOT EXISTS idx_clerk_webhook_processed
    ON public.clerk_webhook_event(processed);

CREATE INDEX IF NOT EXISTS idx_clerk_webhook_received_at
    ON public.clerk_webhook_event(received_at DESC);

CREATE INDEX IF NOT EXISTS idx_clerk_webhook_event_id
    ON public.clerk_webhook_event(event_id);

-- Comments
COMMENT ON TABLE public.clerk_webhook_event IS 'Audit trail and processing queue for Clerk webhooks';
COMMENT ON COLUMN public.clerk_webhook_event.event_id IS 'Unique Clerk event ID (for idempotency)';
COMMENT ON COLUMN public.clerk_webhook_event.event_type IS 'Event type: user.created, user.updated, user.deleted, session.created, etc.';
COMMENT ON COLUMN public.clerk_webhook_event.clerk_user_id IS 'Clerk user ID associated with the event';
COMMENT ON COLUMN public.clerk_webhook_event.payload IS 'Full webhook payload as JSON';
COMMENT ON COLUMN public.clerk_webhook_event.processed IS 'Whether the webhook has been processed';
COMMENT ON COLUMN public.clerk_webhook_event.error_message IS 'Error message if processing failed';
COMMENT ON COLUMN public.clerk_webhook_event.retry_count IS 'Number of retry attempts';

-- ---------------------------------------------------
-- 7. CREATE NEW TABLE: clerk_session
-- ---------------------------------------------------
-- Tracks active user sessions from Clerk
-- Used for session management and audit trail

CREATE TABLE IF NOT EXISTS public.clerk_session (
    id BIGINT DEFAULT nextval('public.sequence_generator'::regclass) NOT NULL,
    session_id VARCHAR(255) UNIQUE NOT NULL,
    clerk_user_id VARCHAR(255) NOT NULL,
    user_profile_id BIGINT,
    tenant_id VARCHAR(255),
    client_id VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent TEXT,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    last_active_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT clerk_session_pkey PRIMARY KEY (id),
    CONSTRAINT fk_clerk_session_user_profile
        FOREIGN KEY (user_profile_id)
        REFERENCES public.user_profile(id)
        ON DELETE CASCADE
);

-- Indexes for clerk_session
CREATE INDEX IF NOT EXISTS idx_clerk_session_user
    ON public.clerk_session(clerk_user_id);

CREATE INDEX IF NOT EXISTS idx_clerk_session_profile
    ON public.clerk_session(user_profile_id);

CREATE INDEX IF NOT EXISTS idx_clerk_session_tenant
    ON public.clerk_session(tenant_id);

CREATE INDEX IF NOT EXISTS idx_clerk_session_status
    ON public.clerk_session(status);

CREATE INDEX IF NOT EXISTS idx_clerk_session_expires_at
    ON public.clerk_session(expires_at);

CREATE INDEX IF NOT EXISTS idx_clerk_session_session_id
    ON public.clerk_session(session_id);

-- Comments
COMMENT ON TABLE public.clerk_session IS 'Tracks active user sessions from Clerk for session management and audit';
COMMENT ON COLUMN public.clerk_session.session_id IS 'Unique Clerk session ID (e.g., sess_2xyz789abc012)';
COMMENT ON COLUMN public.clerk_session.clerk_user_id IS 'Clerk user ID associated with this session';
COMMENT ON COLUMN public.clerk_session.user_profile_id IS 'Foreign key to user_profile table';
COMMENT ON COLUMN public.clerk_session.tenant_id IS 'Tenant context for this session';
COMMENT ON COLUMN public.clerk_session.client_id IS 'Device/client identifier';
COMMENT ON COLUMN public.clerk_session.ip_address IS 'IP address of the client';
COMMENT ON COLUMN public.clerk_session.user_agent IS 'Browser user agent string';
COMMENT ON COLUMN public.clerk_session.status IS 'Status: ACTIVE, EXPIRED, REVOKED';
COMMENT ON COLUMN public.clerk_session.expires_at IS 'When this session expires';
COMMENT ON COLUMN public.clerk_session.last_active_at IS 'Last activity timestamp for this session';

-- ---------------------------------------------------
-- 8. ADD TRIGGER FOR AUTOMATIC updated_at TIMESTAMPS
-- ---------------------------------------------------
-- Reuse existing update_updated_at_column function for new tables

DO $$
BEGIN
    -- Check if trigger already exists before creating
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_clerk_user_tenant_updated_at'
    ) THEN
        CREATE TRIGGER trg_clerk_user_tenant_updated_at
            BEFORE UPDATE ON public.clerk_user_tenant
            FOR EACH ROW
            EXECUTE FUNCTION public.update_updated_at_column();
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger
        WHERE tgname = 'trg_clerk_organization_role_updated_at'
    ) THEN
        CREATE TRIGGER trg_clerk_organization_role_updated_at
            BEFORE UPDATE ON public.clerk_organization_role
            FOR EACH ROW
            EXECUTE FUNCTION public.update_updated_at_column();
    END IF;
END $$;

-- ===================================================
-- VERIFICATION QUERIES (Optional - Run to verify)
-- ===================================================

-- Verify new columns exist
-- SELECT column_name, data_type, is_nullable
-- FROM information_schema.columns
-- WHERE table_name = 'user_profile'
--   AND (column_name LIKE 'clerk_%'
--    OR column_name IN ('auth_provider', 'email_verified', 'last_sign_in_at'));

-- Verify new tables exist
-- SELECT table_name
-- FROM information_schema.tables
-- WHERE table_name LIKE 'clerk_%';

-- Verify indexes
-- SELECT indexname
-- FROM pg_indexes
-- WHERE tablename IN ('user_profile', 'clerk_user_tenant', 'clerk_organization_role', 'clerk_webhook_event', 'clerk_session')
-- ORDER BY tablename, indexname;

-- Verify seeded role mappings
-- SELECT * FROM clerk_organization_role;

-- ===================================================
-- END OF CLERK AUTHENTICATION INTEGRATION SCHEMA
-- ===================================================

RAISE NOTICE 'Clerk migration completed successfully!';

