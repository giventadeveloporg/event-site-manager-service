-- ===================================================================
-- Test Data for Local Development and Testing
-- Multi-Tenant Spring Boot Application
-- ===================================================================

-- Create test tenants
INSERT INTO tenant_organization (id, tenant_id, organization_name, domain_name, is_active, created_at, updated_at)
VALUES 
    (1, 'tenant_demo_001', 'Demo Organization 1', 'demo1.local', true, NOW(), NOW()),
    (2, 'tenant_demo_002', 'Demo Organization 2', 'demo2.local', true, NOW(), NOW()),
    (3, 'tenant_demo_003', 'Demo Organization 3', 'demo3.local', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test users for each tenant
INSERT INTO user_profile (id, tenant_id, email, first_name, last_name, is_active, created_at, updated_at)
VALUES 
    -- Tenant 1 users
    (1, 'tenant_demo_001', 'admin@demo1.local', 'John', 'Doe', true, NOW(), NOW()),
    (2, 'tenant_demo_001', 'user1@demo1.local', 'Alice', 'Smith', true, NOW(), NOW()),
    (3, 'tenant_demo_001', 'user2@demo1.local', 'Bob', 'Johnson', true, NOW(), NOW()),
    
    -- Tenant 2 users
    (4, 'tenant_demo_002', 'admin@demo2.local', 'Jane', 'Wilson', true, NOW(), NOW()),
    (5, 'tenant_demo_002', 'user1@demo2.local', 'Charlie', 'Brown', true, NOW(), NOW()),
    (6, 'tenant_demo_002', 'user2@demo2.local', 'Diana', 'Davis', true, NOW(), NOW()),
    
    -- Tenant 3 users
    (7, 'tenant_demo_003', 'admin@demo3.local', 'Eve', 'Miller', true, NOW(), NOW()),
    (8, 'tenant_demo_003', 'user1@demo3.local', 'Frank', 'Garcia', true, NOW(), NOW()),
    (9, 'tenant_demo_003', 'user2@demo3.local', 'Grace', 'Martinez', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test events for each tenant
INSERT INTO event_details (id, tenant_id, title, description, event_date, location, created_by_id, created_at, updated_at)
VALUES 
    -- Tenant 1 events
    (1, 'tenant_demo_001', 'Spring Festival 2025', 'Annual spring celebration with food and music', NOW() + INTERVAL '7 days', 'Central Park', 1, NOW(), NOW()),
    (2, 'tenant_demo_001', 'Tech Meetup', 'Monthly technology discussion and networking', NOW() + INTERVAL '14 days', 'Tech Hub', 2, NOW(), NOW()),
    
    -- Tenant 2 events
    (3, 'tenant_demo_002', 'Cultural Night', 'Traditional music and dance performance', NOW() + INTERVAL '10 days', 'Community Center', 4, NOW(), NOW()),
    (4, 'tenant_demo_002', 'Business Conference', 'Annual business networking event', NOW() + INTERVAL '21 days', 'Convention Center', 5, NOW(), NOW()),
    
    -- Tenant 3 events
    (5, 'tenant_demo_003', 'Food Festival', 'Local cuisine showcase and tasting', NOW() + INTERVAL '5 days', 'Market Square', 7, NOW(), NOW()),
    (6, 'tenant_demo_003', 'Sports Tournament', 'Annual cricket championship', NOW() + INTERVAL '30 days', 'Sports Complex', 8, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test polls for each tenant
INSERT INTO event_poll (id, tenant_id, title, description, is_active, is_anonymous, allow_multiple_choices, max_responses_per_user, results_visible_to, start_date, end_date, event_id, created_by_id, created_at, updated_at)
VALUES 
    -- Tenant 1 polls
    (1, 'tenant_demo_001', 'Favorite Food', 'What is your favorite food at the festival?', true, false, false, 1, 'ALL', NOW(), NOW() + INTERVAL '6 days', 1, 1, NOW(), NOW()),
    (2, 'tenant_demo_001', 'Event Timing', 'What time works best for the tech meetup?', true, false, true, 2, 'ALL', NOW(), NOW() + INTERVAL '13 days', 2, 2, NOW(), NOW()),
    
    -- Tenant 2 polls
    (3, 'tenant_demo_002', 'Music Preference', 'Which type of music do you prefer?', true, false, false, 1, 'ALL', NOW(), NOW() + INTERVAL '9 days', 3, 4, NOW(), NOW()),
    (4, 'tenant_demo_002', 'Conference Topics', 'What topics interest you most?', true, false, true, 3, 'ALL', NOW(), NOW() + INTERVAL '20 days', 4, 5, NOW(), NOW()),
    
    -- Tenant 3 polls
    (5, 'tenant_demo_003', 'Dish Rating', 'Rate the dishes you tried', true, false, true, 5, 'ALL', NOW(), NOW() + INTERVAL '4 days', 5, 7, NOW(), NOW()),
    (6, 'tenant_demo_003', 'Team Preference', 'Which team do you support?', true, true, false, 1, 'ALL', NOW(), NOW() + INTERVAL '29 days', 6, 8, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test poll options
INSERT INTO event_poll_option (id, tenant_id, option_text, poll_id, created_at, updated_at)
VALUES 
    -- Poll 1 options (Favorite Food)
    (1, 'tenant_demo_001', 'Biryani', 1, NOW(), NOW()),
    (2, 'tenant_demo_001', 'Dosa', 1, NOW(), NOW()),
    (3, 'tenant_demo_001', 'Curry', 1, NOW(), NOW()),
    (4, 'tenant_demo_001', 'Samosa', 1, NOW(), NOW()),
    
    -- Poll 2 options (Event Timing)
    (5, 'tenant_demo_001', 'Morning (9 AM)', 2, NOW(), NOW()),
    (6, 'tenant_demo_001', 'Afternoon (2 PM)', 2, NOW(), NOW()),
    (7, 'tenant_demo_001', 'Evening (6 PM)', 2, NOW(), NOW()),
    
    -- Poll 3 options (Music Preference)
    (8, 'tenant_demo_002', 'Classical', 3, NOW(), NOW()),
    (9, 'tenant_demo_002', 'Bollywood', 3, NOW(), NOW()),
    (10, 'tenant_demo_002', 'Folk', 3, NOW(), NOW()),
    (11, 'tenant_demo_002', 'Modern', 3, NOW(), NOW()),
    
    -- Poll 4 options (Conference Topics)
    (12, 'tenant_demo_002', 'Technology', 4, NOW(), NOW()),
    (13, 'tenant_demo_002', 'Business Strategy', 4, NOW(), NOW()),
    (14, 'tenant_demo_002', 'Marketing', 4, NOW(), NOW()),
    (15, 'tenant_demo_002', 'Finance', 4, NOW(), NOW()),
    
    -- Poll 5 options (Dish Rating)
    (16, 'tenant_demo_003', 'Excellent', 5, NOW(), NOW()),
    (17, 'tenant_demo_003', 'Good', 5, NOW(), NOW()),
    (18, 'tenant_demo_003', 'Average', 5, NOW(), NOW()),
    (19, 'tenant_demo_003', 'Poor', 5, NOW(), NOW()),
    
    -- Poll 6 options (Team Preference)
    (20, 'tenant_demo_003', 'Team A', 6, NOW(), NOW()),
    (21, 'tenant_demo_003', 'Team B', 6, NOW(), NOW()),
    (22, 'tenant_demo_003', 'Team C', 6, NOW(), NOW()),
    (23, 'tenant_demo_003', 'No Preference', 6, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test poll responses
INSERT INTO event_poll_response (id, tenant_id, response_value, is_anonymous, comment, poll_id, poll_option_id, user_id, created_at, updated_at)
VALUES 
    -- Poll 1 responses (Favorite Food)
    (1, 'tenant_demo_001', 'Biryani', false, 'Love the flavor!', 1, 1, 2, NOW(), NOW()),
    (2, 'tenant_demo_001', 'Dosa', false, 'Perfect breakfast option', 1, 2, 3, NOW(), NOW()),
    
    -- Poll 2 responses (Event Timing)
    (3, 'tenant_demo_001', 'Morning,Afternoon', false, 'Both times work for me', 2, 5, 2, NOW(), NOW()),
    (4, 'tenant_demo_001', 'Evening', false, 'Evening is perfect', 2, 7, 3, NOW(), NOW()),
    
    -- Poll 3 responses (Music Preference)
    (5, 'tenant_demo_002', 'Classical', false, 'Traditional music is beautiful', 3, 8, 5, NOW(), NOW()),
    (6, 'tenant_demo_002', 'Bollywood', false, 'Love the energy!', 3, 9, 6, NOW(), NOW()),
    
    -- Poll 4 responses (Conference Topics)
    (7, 'tenant_demo_002', 'Technology,Marketing', false, 'These two are my interests', 4, 12, 5, NOW(), NOW()),
    (8, 'tenant_demo_002', 'Business Strategy,Finance', false, 'Important for growth', 4, 13, 6, NOW(), NOW()),
    
    -- Poll 5 responses (Dish Rating)
    (9, 'tenant_demo_003', 'Excellent,Good', false, 'Most dishes were great', 5, 16, 8, NOW(), NOW()),
    (10, 'tenant_demo_003', 'Good,Average', false, 'Mixed experience', 5, 17, 9, NOW(), NOW()),
    
    -- Poll 6 responses (Team Preference) - Anonymous
    (11, 'tenant_demo_003', 'Team A', true, '', 6, 20, 8, NOW(), NOW()),
    (12, 'tenant_demo_003', 'Team B', true, '', 6, 21, 9, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test event attendees
INSERT INTO event_attendee (id, tenant_id, event_id, user_id, registration_date, status, created_at, updated_at)
VALUES 
    -- Event 1 attendees
    (1, 'tenant_demo_001', 1, 2, NOW(), 'REGISTERED', NOW(), NOW()),
    (2, 'tenant_demo_001', 1, 3, NOW(), 'REGISTERED', NOW(), NOW()),
    
    -- Event 2 attendees
    (3, 'tenant_demo_001', 2, 2, NOW(), 'REGISTERED', NOW(), NOW()),
    (4, 'tenant_demo_001', 2, 3, NOW(), 'PENDING', NOW(), NOW()),
    
    -- Event 3 attendees
    (5, 'tenant_demo_002', 3, 5, NOW(), 'REGISTERED', NOW(), NOW()),
    (6, 'tenant_demo_002', 3, 6, NOW(), 'REGISTERED', NOW(), NOW()),
    
    -- Event 4 attendees
    (7, 'tenant_demo_002', 4, 5, NOW(), 'REGISTERED', NOW(), NOW()),
    (8, 'tenant_demo_002', 4, 6, NOW(), 'CANCELLED', NOW(), NOW()),
    
    -- Event 5 attendees
    (9, 'tenant_demo_003', 5, 8, NOW(), 'REGISTERED', NOW(), NOW()),
    (10, 'tenant_demo_003', 5, 9, NOW(), 'REGISTERED', NOW(), NOW()),
    
    -- Event 6 attendees
    (11, 'tenant_demo_003', 6, 8, NOW(), 'REGISTERED', NOW(), NOW()),
    (12, 'tenant_demo_003', 6, 9, NOW(), 'PENDING', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test tenant settings
INSERT INTO tenant_settings (id, tenant_id, organization_name, website_url, contact_email, contact_phone, timezone, currency, language, show_events_section_in_home_page, show_team_members_section_in_home_page, show_sponsors_section_in_home_page, created_at, updated_at)
VALUES 
    (1, 'tenant_demo_001', 'Demo Organization 1', 'https://demo1.local', 'contact@demo1.local', '+1-555-0101', 'America/New_York', 'USD', 'en', true, true, false, NOW(), NOW()),
    (2, 'tenant_demo_002', 'Demo Organization 2', 'https://demo2.local', 'contact@demo2.local', '+1-555-0102', 'America/Los_Angeles', 'USD', 'en', true, false, true, NOW(), NOW()),
    (3, 'tenant_demo_003', 'Demo Organization 3', 'https://demo3.local', 'contact@demo3.local', '+1-555-0103', 'Europe/London', 'GBP', 'en', false, true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Create test email logs
INSERT INTO email_log (id, tenant_id, recipient_email, subject, content, status, sent_at, created_at, updated_at)
VALUES 
    (1, 'tenant_demo_001', 'user1@demo1.local', 'Welcome to Demo Organization 1', 'Welcome to our platform!', 'SENT', NOW(), NOW(), NOW()),
    (2, 'tenant_demo_002', 'user1@demo2.local', 'Event Registration Confirmation', 'Your registration is confirmed.', 'SENT', NOW(), NOW(), NOW()),
    (3, 'tenant_demo_003', 'user1@demo3.local', 'Poll Response Received', 'Thank you for your poll response.', 'SENT', NOW(), NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Update sequences to avoid conflicts
SELECT setval('sequence_generator', (SELECT MAX(id) FROM tenant_organization));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM user_profile));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM event_details));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM event_poll));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM event_poll_option));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM event_poll_response));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM event_attendee));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM tenant_settings));
SELECT setval('sequence_generator', (SELECT MAX(id) FROM email_log));

-- Create indexes for better performance in testing
CREATE INDEX IF NOT EXISTS idx_user_profile_tenant_id ON user_profile(tenant_id);
CREATE INDEX IF NOT EXISTS idx_event_details_tenant_id ON event_details(tenant_id);
CREATE INDEX IF NOT EXISTS idx_event_poll_tenant_id ON event_poll(tenant_id);
CREATE INDEX IF NOT EXISTS idx_event_poll_response_tenant_id ON event_poll_response(tenant_id);
CREATE INDEX IF NOT EXISTS idx_event_attendee_tenant_id ON event_attendee(tenant_id);

-- Display summary
DO $$
BEGIN
    RAISE NOTICE 'Test data created successfully!';
    RAISE NOTICE 'Tenants: 3 (tenant_demo_001, tenant_demo_002, tenant_demo_003)';
    RAISE NOTICE 'Users: 9 (3 per tenant)';
    RAISE NOTICE 'Events: 6 (2 per tenant)';
    RAISE NOTICE 'Polls: 6 (2 per tenant)';
    RAISE NOTICE 'Poll Options: 23';
    RAISE NOTICE 'Poll Responses: 12';
    RAISE NOTICE 'Event Attendees: 12';
    RAISE NOTICE 'Tenant Settings: 3';
    RAISE NOTICE 'Email Logs: 3';
END $$;
