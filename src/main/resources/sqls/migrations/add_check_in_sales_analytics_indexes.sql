-- Task 12: Migration script to add performance indexes for check-in and sales analytics
-- This script adds recommended indexes to improve query performance for check-in and sales analytics endpoints
-- Created: January 2026
-- Purpose: Optimize queries for QR scanner check-in history and sales analytics reporting

-- =====================================================
-- INDEXES FOR CHECK-IN QUERIES
-- =====================================================

-- Index for check-in status filtering (Task 1)
-- Supports: GET /api/event-ticket-transactions?eventId.equals=X&checkInStatus.equals=CHECKED_IN
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_check_in_status
    ON public.event_ticket_transaction(event_id, check_in_status)
    WHERE check_in_status IS NOT NULL;

-- Index for check-in time filtering (Task 2)
-- Supports: GET /api/event-ticket-transactions?eventId.equals=X&checkInTime.greaterThanOrEqual=...&checkInTime.lessThanOrEqual=...
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_check_in_time
    ON public.event_ticket_transaction(event_id, check_in_time)
    WHERE check_in_time IS NOT NULL;

-- Composite index for check-in queries (Task 3)
-- Supports: Combined filters for eventId, checkInStatus, and checkInTime
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_check_in_composite
    ON public.event_ticket_transaction(event_id, check_in_status, check_in_time)
    WHERE check_in_status = 'CHECKED_IN' AND check_in_time IS NOT NULL;

-- =====================================================
-- INDEXES FOR SALES ANALYTICS QUERIES
-- =====================================================

-- Index for sales analytics (purchase date filtering) (Task 11, 10)
-- Supports: GET /api/event-ticket-transactions/statistics/{eventId}?startDate=...&endDate=...
-- Supports: GET /api/event-ticket-transactions/sales-analytics/{eventId}?startDate=...&endDate=...
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_purchase_date
    ON public.event_ticket_transaction(event_id, purchase_date)
    WHERE purchase_date IS NOT NULL AND status = 'COMPLETED';

-- Composite index for sales analytics with status filter
-- Excludes REFUNDED and CANCELLED transactions from analytics
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_sales_analytics
    ON public.event_ticket_transaction(event_id, purchase_date, status, final_amount)
    WHERE purchase_date IS NOT NULL
      AND status IN ('COMPLETED', 'PENDING', 'FAILED')
      AND final_amount IS NOT NULL;

-- =====================================================
-- INDEXES FOR TENANT ISOLATION (Task 8)
-- =====================================================

-- Composite index for tenant-scoped check-in queries
-- Ensures efficient tenant filtering combined with check-in filters
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_tenant_check_in
    ON public.event_ticket_transaction(tenant_id, event_id, check_in_status, check_in_time)
    WHERE tenant_id IS NOT NULL AND check_in_status IS NOT NULL;

-- Composite index for tenant-scoped sales analytics
-- Ensures efficient tenant filtering combined with purchase date filters
CREATE INDEX IF NOT EXISTS idx_event_ticket_transaction_tenant_sales
    ON public.event_ticket_transaction(tenant_id, event_id, purchase_date, status)
    WHERE tenant_id IS NOT NULL
      AND purchase_date IS NOT NULL
      AND status IN ('COMPLETED', 'PENDING', 'FAILED');

-- =====================================================
-- COMMENTS ON INDEXES
-- =====================================================

COMMENT ON INDEX idx_event_ticket_transaction_check_in_status IS 'Index for fast check-in status filtering by event';
COMMENT ON INDEX idx_event_ticket_transaction_check_in_time IS 'Index for fast check-in time range filtering by event';
COMMENT ON INDEX idx_event_ticket_transaction_check_in_composite IS 'Composite index for optimized check-in history queries';
COMMENT ON INDEX idx_event_ticket_transaction_purchase_date IS 'Index for fast purchase date filtering for sales analytics';
COMMENT ON INDEX idx_event_ticket_transaction_sales_analytics IS 'Composite index for optimized sales analytics queries';
COMMENT ON INDEX idx_event_ticket_transaction_tenant_check_in IS 'Composite index for tenant-scoped check-in queries';
COMMENT ON INDEX idx_event_ticket_transaction_tenant_sales IS 'Composite index for tenant-scoped sales analytics queries';

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Verify indexes were created successfully
-- SELECT indexname, indexdef
-- FROM pg_indexes
-- WHERE tablename = 'event_ticket_transaction'
--   AND indexname LIKE '%check_in%' OR indexname LIKE '%sales%' OR indexname LIKE '%tenant%'
-- ORDER BY indexname;
