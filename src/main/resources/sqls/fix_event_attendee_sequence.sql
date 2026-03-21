-- =====================================================
-- One-off: Fix event_attendee_pkey duplicate key (id=4)
-- =====================================================
-- Run this when you see: duplicate key value violates unique constraint "event_attendee_pkey"
-- Detail: Key (id)=(4) already exists.
--
-- Cause: The shared sequence_generator is behind the max(id) in event_attendee (e.g. sequence
-- returns 4 but row 4 already exists). ID clearing in EventAttendeeServiceImpl is correct;
-- the DB sequence must be synced.
--
-- Usage (replace with your DB name):
--   psql -d your_database_name -f fix_event_attendee_sequence.sql
-- =====================================================

-- Set sequence to at least max(id) from event_attendee so next nextval() is safe.
SELECT setval(
    'public.sequence_generator',
    GREATEST(
        COALESCE((SELECT last_value FROM public.sequence_generator), 0),
        COALESCE((SELECT MAX(id) FROM public.event_attendee), 0),
        COALESCE((SELECT MAX(id) FROM public.event_attendee_guest), 0)
    ),
    true
);

-- Verify: next value should be > max(id)
SELECT
    (SELECT last_value FROM public.sequence_generator) AS sequence_last_value,
    (SELECT MAX(id) FROM public.event_attendee) AS event_attendee_max_id,
    CASE
        WHEN (SELECT last_value FROM public.sequence_generator) >= (SELECT COALESCE(MAX(id), 0) FROM public.event_attendee)
        THEN 'OK: sequence >= max(id)'
        ELSE 'WARNING: run full sync_sequence_after_inserts.sql'
    END AS status;
