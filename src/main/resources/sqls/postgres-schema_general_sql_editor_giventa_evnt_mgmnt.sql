
CREATE ROLE giventa_event_management WITH LOGIN CREATEDB PASSWORD 'giventa_event_management';

ALTER DATABASE giventa_event_management OWNER to giventa_event_management ;

REASSIGN OWNED BY nextjs_template_boot TO giventa_event_management;

GRANT USAGE ON SCHEMA public TO giventa_event_management;

GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON ALL TABLES IN SCHEMA public TO giventa_event_management;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO giventa_event_management;

-- For the table
SELECT grantee, privilege_type
FROM information_schema.role_table_grants
WHERE table_name = 'event_details';



-- For the schema
SELECT nspname, usename, has_schema_privilege(usename, nspname, 'USAGE')
FROM pg_namespace, pg_user
WHERE nspname = 'public';

SELECT datname, usename AS owner
FROM pg_database
JOIN pg_user ON pg_database.datdba = pg_user.usesysid;


DELETE FROM public.user_task WHERE user_id NOT IN (SELECT id FROM public.user_profile);

ALTER TABLE EVENT
ALTER COLUMN start_time TYPE VARCHAR(100) USING start_time::VARCHAR,
ALTER COLUMN end_time TYPE VARCHAR(100) USING end_time::VARCHAR;

ALTER TABLE EVENT_MEDIA ADD COLUMN pre_signed_url VARCHAR(400);

INSERT INTO public.event_media
(

id, title, description, event_media_type, storage_type, file_url, file_data, file_data_content_type, content_type, file_size, is_public, event_flyer, is_event_management_official_document, created_at, updated_at, event_id, uploaded_by_id, pre_signed_url)
VALUES(1756, 'xxcx', 'xxxx', 'image/jpeg', 'S3', 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/event-id/1502/hanzh_1747836718150_f0603b68.jpg', NULL, NULL, 'image/jpeg', 51594, false, false, false, '2025-05-21 14:11:58.661', '2025-05-21 14:11:58.661', 1502, NULL, 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/event-id/1502/hanzh_1747836718150_f0603b68.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250521T141158Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIATIT5HARDKCWNLQMU%2F20250521%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=edaa7bb0b8fb5bc6f27370959fa184a86daf04542b7b601d7b12abcc9b9042c7');


ALTER TABLE public.event_media 
ALTER COLUMN file_url TYPE varchar(1200),
ALTER COLUMN pre_signed_url TYPE varchar(2048);

SELECT * FROM public.event_media
WHERE tenant_id = 'tenant_demo_001'
AND event_flyer = TRUE
AND event_id = 2251
LIMIT 20 OFFSET 0;


SELECT pid, datname, usename, application_name, client_addr 
FROM pg_stat_activity 
WHERE datname = 'giventa_event_management';

CREATE DATABASE giventa_event_management;

SELECT current_database();

SELECT rolname FROM pg_roles;

REASSIGN OWNED BY postgres TO giventa_event_management;


DROP TABLE IF EXISTS public.bulk_operation_log CASCADE;
DROP TABLE IF EXISTS public.qr_code_usage CASCADE;
DROP TABLE IF EXISTS public.user_registration_request CASCADE;
DROP TABLE IF EXISTS public.event_attendee_guest CASCADE;
DROP TABLE IF EXISTS public.event_guest_pricing CASCADE;
DROP TABLE IF EXISTS public.event_attendee CASCADE;
DROP TABLE IF EXISTS public.event_admin_audit_log CASCADE;
DROP TABLE IF EXISTS public.event_calendar_entry CASCADE;
DROP TABLE IF EXISTS public.event_media CASCADE;
DROP TABLE IF EXISTS public.event_poll_response CASCADE;
DROP TABLE IF EXISTS public.event_poll_option CASCADE;
DROP TABLE IF EXISTS public.event_poll CASCADE;
DROP TABLE IF EXISTS public.event_ticket_transaction CASCADE;
DROP TABLE IF EXISTS public.user_payment_transaction CASCADE;
DROP TABLE IF EXISTS public.event_ticket_type CASCADE;
DROP TABLE IF EXISTS public.event_organizer CASCADE;
DROP TABLE IF EXISTS public.event_details CASCADE;
DROP TABLE IF EXISTS public.event_admin CASCADE;
DROP TABLE IF EXISTS public.user_task CASCADE;
DROP TABLE IF EXISTS public.user_subscription CASCADE;
DROP TABLE IF EXISTS public.event_type_details CASCADE;
DROP TABLE IF EXISTS public.tenant_settings CASCADE;
DROP TABLE IF EXISTS public.user_profile CASCADE;
DROP TABLE IF EXISTS public.tenant_organization CASCADE;
DROP TABLE IF EXISTS public.databasechangeloglock CASCADE;
DROP TABLE IF EXISTS public.databasechangelog CASCADE;

-- same as above 

DROP TABLE public.event_details CASCADE;
DROP TABLE public.event_attendee CASCADE;

DROP TABLE public.event_poll  CASCADE;

DROP TABLE public.event_poll_option   CASCADE;

DROP TABLE public.event_ticket_transaction CASCADE;

DROP TABLE public.tenant_organization  CASCADE;

DROP TABLE public.user_profile   CASCADE;

 SELECT unnest(enum_range(NULL::user_role_type));
SELECT unnest(enum_range(NULL::user_status_type));

SELECT tablename, tableowner
FROM pg_tables
WHERE schemaname = 'public';


INSERT INTO public.event_details VALUES (2801, 'tenant_demo_001', 'Annual Tech Conference 2025', 'Join us for the biggest tech event of the year', 'A comprehensive conference featuring the latest in technology trends, networking opportunities, and expert speakers from around the globe.', '2025-07-15', '2025-07-17', '09:00', '17:00', 'Convention Center, Downtown', NULL, 500, 'TICKETED', true, 2, true, false, true, NULL, NULL, NULL, NULL, false, true, NULL, 2600, 2400, '2025-06-08 20:16:39.532111', '2025-06-08 20:16:39.532111', false, false, false);
--INSERT INTO public.event_details VALUES (2900, 'tenant_demo_002', 'Family Fun Day', 'Community event for all ages', 'A family-friendly event with activities for all age groups, food, games, and entertainment.', '2025-08-10', '2025-08-10', '10:00', '18:00', 'Community Park', NULL, 200, 'FREE', true, 4, true, true, true, NULL, NULL, NULL, NULL, false, true, NULL, 2700, 2450, '2025-06-08 20:16:39.532111', '2025-06-08 20:16:39.532111', false, false, false);
--INSERT INTO public.event_details VALUES (2850, 'tenant_demo_001', 'React Workshop for Beginners', 'Learn React from scratch in this hands-on workshop', 'A beginner-friendly workshop covering React fundamentals, component creation, state management, and building your first React application.', '2025-06-20', '2025-06-20', '10:00', '16:00', 'Tech Hub Building A', NULL, 30, 'FREE', true, 0, false, false, false, NULL, NULL, NULL, NULL, false, true, NULL, 2600, 2350, '2025-06-08 20:16:39.532111', '2025-06-08 20:46:43.612386', true, false, false);
--INSERT INTO public.event_details VALUES (3951, 'tenant_demo_001', 'xxxcxc', 'cxcx', 'cxcxcxc', '2025-06-22', '2025-06-22', '10:15 AM', '11:15 AM', 'xcxcx', 'xcxcxc', 2, 'free', true, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, false, true, NULL, NULL, 2350, '2025-06-09 01:16:21.63', '2025-06-08 21:17:06.742427', true, NULL, NULL);
I

INSERT INTO public.event_guest_pricing VALUES (3101, 'tenant_demo_001', 2800, 'ADULT', 150.00, true, '2025-06-01', '2025-07-14', 'Adult guest pricing for conference', NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-08 20:16:39.631864', '2025-06-08 20:16:39.631864');
--INSERT INTO public.event_guest_pricing VALUES (3151, 'tenant_demo_001', 2800, 'TEEN', 75.00, true, '2025-06-01', '2025-07-14', 'Teen guest pricing (13-17 years)', NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-08 20:16:39.631864', '2025-06-08 20:16:39.631864');
--INSERT INTO public.event_guest_pricing VALUES (3201, 'tenant_demo_001', 2800, 'CHILD', 25.00, true, '2025-06-01', '2025-07-14', 'Child guest pricing (5-12 years)', NULL, NULL, NULL, NULL, NULL, NULL, '2025-06-08 20:16:39.631864', '2025-06-08 20:16:39.631864');


	INSERT INTO public.event_ticket_type (id, tenant_id, event_id, code, name, description, price, available_quantity, sold_quantity, is_active, created_at, updated_at)
	VALUES
	(1, 'tenant_demo_001', 6151, 'STD', 'Standard', 'Standard ticket for Spring Gala', 50.00, 100, 10, true, now(), now()),
	(2, 'tenant_demo_001', 6151, 'VIP', 'VIP', 'VIP ticket for Tech Conference', 200.00, 50, 5, true, now(), now()),
	(3, 'tenant_demo_001', 6151, 'RUN', 'Runner', 'Runner ticket for Charity Run', 0.00, 300, 100, true, now(), now()),
	(4, 'tenant_demo_001', 6151, 'FAM', 'Family', 'Family ticket for Picnic', 20.00, 30, 10, true, now(), now()),
	(5, 'tenant_demo_001', 6151, 'DIN', 'Dinner', 'Dinner ticket for VIP Dinner', 100.00, 20, 8, true, now(), now()),
	(6, 'tenant_demo_001', 6151, 'FEST', 'Festival', 'Festival ticket for Summer Fest', 30.00, 200, 50, true, now(), now());
	
	UPDATE public.event_details
SET start_date = '2025-04-20';

   UPDATE public.event_details SET start_date = '2025-08-01', end_date = '2025-08-01' WHERE id = 1;
   UPDATE public.event_details SET start_date = '2025-08-15', end_date = '2025-08-15' WHERE id = 2;
   UPDATE public.event_details SET start_date = '2025-08-30', end_date = '2025-08-30' WHERE id = 3;
   UPDATE public.event_details SET start_date = '2025-09-10', end_date = '2025-09-10' WHERE id = 4;
   UPDATE public.event_details SET start_date = '2025-09-20', end_date = '2025-09-20' WHERE id = 5;
   UPDATE public.event_details SET start_date = '2025-09-30', end_date = '2025-09-30' WHERE id = 6;
   
   
  --  public.user_profile
     -- to enable email for users subscribed = true run/use all the three at once with setting token also--
   
    delete from public.user_profile
		WHERE id=4651;
   
   INSERT INTO public.user_profile
(id, tenant_id, user_id, first_name, last_name, email, phone, address_line_1, address_line_2, city, state, zip_code, country, notes, family_name, city_town, district, educational_institution, profile_image_url, is_email_subscribed, email_subscription_token, is_email_subscription_token_used, user_status, user_role, reviewed_by_admin_at, reviewed_by_admin_id, created_at, updated_at, request_id, request_reason, status, admin_comments,
submitted_at, reviewed_at, approved_at, rejected_at)
VALUES(4651, 'tenant_demo_001', 'guest_giventauser_gmail_com_1755451276831', '', '',
'giventauser@gmail.com', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
true, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaXZlbnRhdXNlckBnbWFpbC5jb20ifQ.gnZmiOIJmHJtrsx3OZtJRB6vcRvfhFUwiRdPU7dtgYrIdrRYsqPC7XiNKrLMSyDojGnnCt9R0Tp3efvtvNKmWQ', NULL, 'ACTIVE', 'MEMBER', NULL, NULL, '2025-08-17 17:21:15.246', '2025-08-17 17:21:15.246',
NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
   
     SELECT id, tenant_id, user_id, first_name, last_name, email, phone, address_line_1, address_line_2, city, state, zip_code, country, notes, family_name, city_town, district, educational_institution, profile_image_url, is_email_subscribed, email_subscription_token, is_email_subscription_token_used, user_status, user_role, reviewed_by_admin_at, reviewed_by_admin_id, created_at, updated_at, request_id, request_reason, status, admin_comments, submitted_at, reviewed_at, approved_at, rejected_at
FROM public.user_profile
WHERE id=4651;

 SELECT id, tenant_id, user_id, first_name, last_name, email, phone, address_line_1, address_line_2, city, state, zip_code, country, notes, family_name, city_town, district, educational_institution, profile_image_url, is_email_subscribed, email_subscription_token, is_email_subscription_token_used, user_status, user_role, reviewed_by_admin_at, reviewed_by_admin_id, created_at, updated_at, request_id, request_reason, status, admin_comments, submitted_at, reviewed_at, approved_at, rejected_at
FROM public.user_profile
WHERE email in ('giventauser@gmail.com','gain@hotmail.com');

--for prod   where WHERE id=10651;
   
     UPDATE public.user_profile
		SET is_email_subscribed = true, is_email_subscription_token_used = false
		WHERE email in ('giventauser@gmail.com','gain@hotmail.com');
     
       UPDATE public.user_profile
		SET is_email_subscribed = true, is_email_subscription_token_used = false
		WHERE email = 'giventauser@gmail.com';
       
       UPDATE public.user_profile
		SET email_subscription_token  = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnYWluQGhvdG1haWwuY29tIn0.hQFTfyU4gHgoKhVAjXajSqvdgCiN3qEYpdSZuhXr-4_NUO69m3VeTEAMzyXceTr8WWRWpGM6qahTk2ZkAaxLzA';
   
   
     -- to enable email for users unsubscribed = false --
   
     UPDATE public.user_profile
		SET is_email_subscribed = false, is_email_subscription_token_used = true
		WHERE email = 'giventauser@gmail.com';
   
   UPDATE public.user_profile
		SET email_subscription_token  = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnYWluQGhvdG1haWwuY29tIn0.hQFTfyU4gHgoKhVAjXajSqvdgCiN3qEYpdSZuhXr-4_NUO69m3VeTEAMzyXceTr8WWRWpGM6qahTk2ZkAaxLzA';
   
   commit;
   
   delete from public.event_media
		where description is null;
   
   
   -- Add sample media for events that don't have any media
-- This will give each event a unique image in the home page

INSERT INTO public.event_media (
    id, tenant_id, title, description, event_media_type, storage_type, 
    file_url, file_data, file_data_content_type, content_type, file_size, 
    is_public, event_flyer, is_event_management_official_document, 
    pre_signed_url, pre_signed_url_expires_at, alt_text, display_order, 
    download_count, is_featured_video, featured_video_url, is_featured_image, 
    is_hero_image, is_active_hero_image, created_at, updated_at, event_id, uploaded_by_id
) VALUES 
-- Event 3: Family Picnic
(7001, 'tenant_demo_001', 'family_picnic.jpg', 'Family picnic event flyer', 'image/jpeg', 'S3', 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/3/family_picnic_sample.jpg', 
NULL, 'image/jpeg', 'image/jpeg', 45000, true, true, false, 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/3/family_picnic_sample.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250101T000000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=SAMPLE%2F20250101%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=sample', 
NULL, NULL, NULL, NULL, NULL, false, NULL, false, false, false, 
'2025-01-01 00:00:00', '2025-01-01 00:00:00', 3, 4651),

-- Event 4: VIP Dinner  
(7002, 'tenant_demo_001', 'vip_dinner.jpg', 'VIP dinner event flyer', 'image/jpeg', 'S3', 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/4/vip_dinner_sample.jpg', 
NULL, 'image/jpeg', 'image/jpeg', 52000, true, true, false, 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/4/vip_dinner_sample.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250101T000000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=SAMPLE%2F20250101%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=sample', 
NULL, NULL, NULL, NULL, NULL, false, NULL, false, false, false, 
'2025-01-01 00:00:00', '2025-01-01 00:00:00', 4, 4651),

-- Event 5: Summer Fest
(7003, 'tenant_demo_001', 'summer_fest.jpg', 'Summer festival event flyer', 'image/jpeg', 'S3', 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/5/summer_fest_sample.jpg', 
NULL, 'image/jpeg', 'image/jpeg', 38000, true, true, false, 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/5/summer_fest_sample.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250101T000000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=SAMPLE%2F20250101%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=sample', 
NULL, NULL, NULL, NULL, NULL, false, NULL, false, false, false, 
'2025-01-01 00:00:00', '2025-01-01 00:00:00', 5, 4651),

-- Event 6: Spring Gala
(7004, 'tenant_demo_001', 'spring_gala.jpg', 'Spring gala event flyer', 'image/jpeg', 'S3', 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/6/spring_gala_sample.jpg', 
NULL, 'image/jpeg', 'image/jpeg', 41000, true, true, false, 
'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/6/spring_gala_sample.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250101T000000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3600&X-Amz-Credential=SAMPLE%2F20250101%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=sample', 
NULL, NULL, NULL, NULL, NULL, false, NULL, false, false, false, 
'2025-01-01 00:00:00', '2025-01-01 00:00:00', 6, 4651);

--select e1_0.id,e1_0.alt_text,e1_0.content_type,e1_0.created_at,e1_0.description,e1_0.display_order,e1_0.download_count,e1_0.event_flyer,e1_0.event_id,e1_0.event_media_type,e1_0.featured_video_url,e1_0.file_size,e1_0.file_url,e1_0.is_active_hero_image,e1_0.is_event_management_official_document,e1_0.is_featured_image,e1_0.is_featured_video,e1_0.is_hero_image,e1_0.is_public,e1_0.pre_signed_url,e1_0.pre_signed_url_expires_at,e1_0.storage_type,e1_0.tenant_id,e1_0.title,e1_0.updated_at,e1_0.uploaded_by_id from event_media e1_0 where e1_0.event_flyer=? and e1_0.event_id=? offset ? rows fetch first ? rows only 
--Hibernate: select e1_0.id,e1_0.alt_text,e1_0.content_type,e1_0.created_at,e1_0.description,e1_0.display_order,e1_0.download_count,e1_0.event_flyer,e1_0.event_id,e1_0.event_media_type,e1_0.featured_video_url,e1_0.file_size,e1_0.file_url,e1_0.is_active_hero_image,e1_0.is_event_management_official_document,e1_0.is_featured_image,e1_0.is_featured_video,e1_0.is_hero_image,e1_0.is_public,e1_0.pre_signed_url,e1_0.pre_signed_url_expires_at,e1_0.storage_type,e1_0.tenant_id,e1_0.title,e1_0.updated_at,e1_0.uploaded_by_id from event_media e1_0 where e1_0.event_flyer=? and e1_0.event_id=?

SELECT 
    e1_0.id,
    e1_0.alt_text,
    e1_0.content_type,
    e1_0.created_at,
    e1_0.description,
    e1_0.display_order,
    e1_0.download_count,
    e1_0.event_flyer,
    e1_0.event_id,
    e1_0.event_media_type,
    e1_0.featured_video_url,
    e1_0.file_size,
    e1_0.file_url,
    e1_0.is_active_hero_image,
    e1_0.is_event_management_official_document,
    e1_0.is_featured_image,
    e1_0.is_featured_video,
    e1_0.is_hero_image,
    e1_0.is_public,
    e1_0.pre_signed_url,
    e1_0.pre_signed_url_expires_at,
    e1_0.storage_type,
    e1_0.tenant_id,
    e1_0.title,
    e1_0.updated_at,
    e1_0.uploaded_by_id 
FROM event_media e1_0 
WHERE e1_0.event_flyer = true 
    AND e1_0.event_id in (1,2,3)
LIMIT 20 OFFSET 0;


UPDATE event_media 
SET 
    file_url = 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/1/kanj_cine_star_nite_2025_1750026380584_8b2bfa97.avif',
    file_data_content_type = 'image/avif'
WHERE event_id IN (2, 3);

delete from  event_media 
WHERE event_id not IN (1, 2, 3);

delete from  event_details 
WHERE id not IN (1, 2, 3); 

--  test media

-- Step 2: Delete event_attendee_guests records for events not in (1, 2, 3)
--DELETE FROM public.event_attendee_guest
--WHERE event_attendee_id IN (
--    SELECT id FROM event_attendee WHERE event_id NOT IN (1, 2, 3)
--);

DELETE FROM public.event_attendee 
WHERE event_id NOT IN (1, 2, 3);

-- Step 3: Delete event_media records for events not in (1, 2, 3)
DELETE FROM event_media 
WHERE event_id NOT IN (1, 2, 3);

-- Step 4: Delete event_ticket_transactions records for events not in (1, 2, 3)
DELETE FROM event_ticket_transaction 
WHERE event_id NOT IN (1, 2, 3);

-- Step 5: Delete event_ticket_types records for events not in (1, 2, 3)
DELETE FROM event_ticket_type 
WHERE event_id NOT IN (1, 2, 3);

-- Step 6: Finally, delete event_details records not in (1, 2, 3)



DELETE FROM event_attendee WHERE event_id IN (
    SELECT id FROM event_details 
    WHERE id NOT IN (1, 2, 3) AND tenant_id = 'tenant_demo_001'
);

DELETE FROM event_media WHERE event_id IN (
    SELECT id FROM event_details 
    WHERE id NOT IN (1, 2, 3) AND tenant_id = 'tenant_demo_001'
);

-- Then delete the main records
DELETE FROM event_details 
WHERE id NOT IN (1, 2, 3) AND tenant_id = 'tenant_demo_001';
   
  

UPDATE event_media
SET file_url = 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/1/kanj_cine_star_nite_2025_1750026380584_8b2bfa97.avif'
WHERE event_id = 2 ;

UPDATE event_media
SET file_url = 'https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/event-id/1/kanj_cine_star_nite_2025_1750026380584_8b2bfa97.avif'
WHERE event_id = 3 ;

-- Update Event 3 media records
UPDATE event_media
SET file_url = 'REPLACE(file_url, 'event-id/3/', 'event-id/1/'),
    pre_signed_url = REPLACE(pre_signed_url, 'event-id/3/', 'event-id/1/')'
WHERE event_id = 3
  AND file_url LIKE '%event-id/1/%';

-- Verify the fix
SELECT id, event_id, title, file_url
FROM event_media
WHERE event_id IN (2, 3)
  AND event_flyer = true
ORDER BY event_id, id;


UPDATE public.event_media
SET event_flyer = true where id =4601;
   
UPDATE public.event_media
SET event_flyer = false  where id = 4601;

UPDATE public.event_media
SET event_flyer = false  WHERE event_id IN (1, 2, 3);


-- To delete or edit event_ticket_type  run bebelow 2 stmnts
-- First, delete related transaction items
DELETE FROM event_ticket_transaction_item 
WHERE ticket_type_id = 4752;

-- Then delete the ticket type
DELETE FROM event_ticket_type 
WHERE id = 4752;
   
   
   SELECT id, tenant_id, transaction_reference, email, first_name, last_name, phone, quantity, price_per_unit, total_amount, tax_amount, platform_fee_amount, discount_code_id, discount_amount, final_amount, status, payment_method, payment_reference, stripe_checkout_session_id, stripe_payment_intent_id, purchase_date, confirmation_sent_at, refund_amount, refund_date, refund_reason, stripe_customer_id, stripe_payment_status, stripe_customer_email, stripe_payment_currency, stripe_amount_discount, stripe_amount_tax, stripe_fee_amount, qr_code_image_url, event_id, user_id, created_at, updated_at, number_of_guests_checked_in, check_in_status, check_in_time, check_out_time
FROM public.event_ticket_transaction
WHERE id=8551;	
	

SELECT id, tenant_id, transaction_id, ticket_type_id, quantity, price_per_unit, total_amount, created_at,
updated_at
FROM public.event_ticket_transaction_item
WHERE id=8551;

SELECT id, tenant_id, transaction_id, ticket_type_id, quantity, price_per_unit, total_amount, created_at,
updated_at
FROM public.event_ticket_transaction_item
WHERE transaction_id=8551;

	 --You already have ON DELETE CASCADE from items to transactions, so deleting the parent rows will automatically delete related items and fire the inventory trigger.
	 -- Use one of these:
	
	 -- Single event
	BEGIN;
	DELETE FROM public.event_ticket_transaction
	WHERE event_id = 3;
	COMMIT;

	 
	   --  ticket types quantity sold available..
	 SELECT id, tenant_id, "name", description, price, is_service_fee_included, service_fee, code, available_quantity, sold_quantity, is_active, sale_start_date, sale_end_date, min_quantity_per_order, max_quantity_per_order, requires_approval, sort_order, created_at, updated_at, event_id
FROM public.event_ticket_type
WHERE id IN (3,4801);


   -- Check if triggers exist
SELECT 
    trigger_name,
    event_manipulation,
    event_object_table,
    action_statement,
    action_timing
FROM information_schema.triggers 
WHERE trigger_name IN ('manage_ticket_inventory_trigger', 'update_ticket_sold_quantity_trigger');

-- Check trigger function exists
SELECT routine_name, routine_type 
FROM information_schema.routines 
WHERE routine_name IN ('manage_ticket_inventory', 'update_ticket_sold_quantity');

  -- Check current state of ticket types
SELECT 
    id,
    name,
    available_quantity,
    sold_quantity,
    (available_quantity - sold_quantity) as remaining_quantity
FROM event_ticket_type 
WHERE event_id = 3;

-- Check if there are any transaction items
SELECT 
    tti.id,
    tti.ticket_type_id,
    tti.quantity,
    ett.status,
    ett.id as transaction_id
FROM event_ticket_transaction_item tti
JOIN event_ticket_transaction ett ON tti.transaction_id = ett.id
WHERE ett.event_id = 3;


-- Test if triggers work by manually inserting a test record
INSERT INTO event_ticket_transaction_item (
    tenant_id, transaction_id, ticket_type_id, quantity, 
    price_per_unit, total_amount
) VALUES (
    'tenant_demo_001', 
    (SELECT id FROM event_ticket_transaction WHERE event_id = 3 LIMIT 1),
    3, -- ticket_type_id
    1, -- quantity
    0.74, -- price_per_unit
    0.74 -- total_amount
);

-- Check if sold_quantity was updated
SELECT id, name, available_quantity , sold_quantity , remaining_quantity
  FROM event_ticket_type WHERE id = 2;

SELECT id, name, available_quantity , sold_quantity , 
remaining_quantity FROM event_ticket_type WHERE event_id  = 3;


SELECT id, name, available_quantity, sold_quantity FROM event_ticket_type WHERE id = 2;

-- Calculate and update sold quantities based on actual transaction data
UPDATE event_ticket_type 
SET sold_quantity = (
    SELECT COALESCE(SUM(tti.quantity), 0)
    FROM event_ticket_transaction_item tti
    JOIN event_ticket_transaction ett ON tti.transaction_id = ett.id
    WHERE ett.status = 'COMPLETED' 
    AND tti.ticket_type_id = event_ticket_type.id
    AND ett.event_id = 3
),
updated_at = NOW()
WHERE event_id = 3;


  
  ALTER TABLE public.event_ticket_type
  ADD COLUMN remaining_quantity integer DEFAULT 0;

  UPDATE public.event_ticket_type
  SET remaining_quantity = COALESCE(available_quantity, 0) - COALESCE(sold_quantity, 0)
  WHERE remaining_quantity= 0;
