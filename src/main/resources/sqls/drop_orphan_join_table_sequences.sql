-- ===================================================
-- Legacy cleanup: orphan sequences for join tables without id column
-- ===================================================
-- Canonical schema (mosc-temp/code_html_template/SQLS/Current_Sqls/
-- Event_Site_Manager_Latest_Schema.sql) never creates rel_*_id_seq sequences.
-- Run only on databases that previously applied an older Liquibase
-- create_per_table_id_sequences.sql which incorrectly created rel_*_id_seq.
-- Full canonical schema rebuild also drops this via DROP SEQUENCE at top of schema file.
-- Safe to run multiple times.
-- ===================================================

DROP SEQUENCE IF EXISTS public.rel_event_details__discount_codes_id_seq;
