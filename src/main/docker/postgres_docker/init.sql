-- PostgreSQL initialization script for Event Site Manager
-- This script ensures proper user and database setup

-- Create the database if it doesn't exist
SELECT 'CREATE DATABASE event_site_manager_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'event_site_manager_db')\gexec

-- Connect to the new database
\c event_site_manager_db;

-- Create the application user if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'event_site_app') THEN
        CREATE ROLE event_site_app WITH LOGIN PASSWORD 'event_site_app!';
    END IF;
END
$$;

-- Grant necessary privileges to the application user
GRANT ALL PRIVILEGES ON DATABASE event_site_manager_db TO event_site_app;
GRANT ALL PRIVILEGES ON SCHEMA public TO event_site_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO event_site_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO event_site_app;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO event_site_app;

-- Grant default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO event_site_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO event_site_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO event_site_app;

-- Set the search path for the user
ALTER ROLE event_site_app SET search_path = public;

-- Create a superuser role for admin operations (optional)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'event_site_admin') THEN
        CREATE ROLE event_site_admin WITH LOGIN SUPERUSER PASSWORD 'event_site_admin!';
    END IF;
END
$$;

-- Grant database privileges to admin user
GRANT ALL PRIVILEGES ON DATABASE event_site_manager_db TO event_site_admin;

-- Output success message
SELECT 'PostgreSQL initialization completed successfully' AS status;

