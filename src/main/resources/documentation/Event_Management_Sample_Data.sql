-- Event Management New Tables - Sample Insert Statements
-- Generated: January 2025
-- Purpose: Sample data for testing and development
-- Note: All backend development is complete - this is for reference only

-- =====================================================
-- DELETE STATEMENTS - CLEAR EXISTING DATA
-- =====================================================

-- Clear all data from new event management tables (in correct order due to foreign key constraints)
DELETE FROM public.event_sponsors_join;
DELETE FROM public.event_featured_performers;
DELETE FROM public.event_contacts;
DELETE FROM public.event_emails;
DELETE FROM public.event_program_directors;
DELETE FROM public.event_sponsors;

-- Note: Delete from join table first, then dependent tables, then parent tables
-- This ensures foreign key constraints are satisfied

-- =====================================================
-- SAMPLE DATA FOR EVENT FEATURED PERFORMERS
-- =====================================================

INSERT INTO public.event_featured_performers VALUES (
  1, 1, 'K.J. Yesudas', 'Yesudas', 'Vocalist',
  'Renowned Malayalam classical singer with over 50 years of experience in the music industry. Known for his melodious voice and classical renditions.',
  'Indian', '1940-01-10', 'yesudas@example.com', '+91-9847012345', 'https://yesudas.com',
  'https://s3.amazonaws.com/bucket/performers/1/portrait/yesudas_portrait.jpg',
  'https://s3.amazonaws.com/bucket/performers/1/performance/yesudas_performance.jpg',
  '["https://s3.amazonaws.com/bucket/performers/1/gallery/img1.jpg", "https://s3.amazonaws.com/bucket/performers/1/gallery/img2.jpg"]',
  45, 1, true, 'https://facebook.com/yesudas', 'https://twitter.com/yesudas', 'https://instagram.com/yesudas',
  'https://youtube.com/yesudas', 'https://linkedin.com/in/yesudas', 'https://tiktok.com/@yesudas',
  true, 100, '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_featured_performers VALUES (
  2, 1, 'Kalamandalam Gopi', 'Gopi Asan', 'Kathakali Performer',
  'Legendary Kathakali artist known for his exceptional performances and traditional Kerala dance form mastery.',
  'Indian', '1937-05-15', 'gopi@example.com', '+91-9847012346', 'https://gopiasan.com',
  'https://s3.amazonaws.com/bucket/performers/2/portrait/gopi_portrait.jpg',
  'https://s3.amazonaws.com/bucket/performers/2/performance/gopi_performance.jpg',
  '["https://s3.amazonaws.com/bucket/performers/2/gallery/img1.jpg"]',
  60, 2, false, 'https://facebook.com/gopiasan', NULL, 'https://instagram.com/gopiasan',
  'https://youtube.com/gopiasan', NULL, NULL,
  true, 90, '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_featured_performers VALUES (
  3, 2, 'Zakir Hussain', 'Zakir Hussain', 'Percussionist',
  'World-renowned tabla player and percussionist with international acclaim.',
  'Indian', '1951-03-09', 'zakir@example.com', '+91-9847012347', 'https://zakirhussain.com',
  'https://s3.amazonaws.com/bucket/performers/3/portrait/zakir_portrait.jpg',
  'https://s3.amazonaws.com/bucket/performers/3/performance/zakir_performance.jpg',
  '["https://s3.amazonaws.com/bucket/performers/3/gallery/img1.jpg", "https://s3.amazonaws.com/bucket/performers/3/gallery/img2.jpg", "https://s3.amazonaws.com/bucket/performers/3/gallery/img3.jpg"]',
  30, 3, false, 'https://facebook.com/zakirhussain', 'https://twitter.com/zakirhussain', 'https://instagram.com/zakirhussain',
  'https://youtube.com/zakirhussain', 'https://linkedin.com/in/zakirhussain', NULL,
  true, 80, '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

-- =====================================================
-- SAMPLE DATA FOR EVENT CONTACTS
-- =====================================================

INSERT INTO public.event_contacts VALUES (
  1, 1, 'Sarah Johnson', '+1-555-0101', 'sarah.johnson@example.com',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_contacts VALUES (
  2, 1, 'Michael Chen', '+1-555-0102', 'michael.chen@example.com',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_contacts VALUES (
  3, 2, 'Emily Rodriguez', '+1-555-0103', 'emily.rodriguez@example.com',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_contacts VALUES (
  4, 2, 'David Kim', '+1-555-0104', 'david.kim@example.com',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_contacts VALUES (
  5, 3, 'Lisa Wang', '+1-555-0105', 'lisa.wang@example.com',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

-- =====================================================
-- SAMPLE DATA FOR EVENT SPONSORS
-- =====================================================

INSERT INTO public.event_sponsors VALUES (
  1, 'Kerala Tourism Development Corporation', 'Title Sponsor', 'KTDC', 'Explore Gods Own Country',
  'Official tourism partner promoting Keralass rich cultural heritage and natural beauty.',
  'https://www.ktdc.com', 'contact@ktdc.com', '+91-471-2321132',
  'https://s3.amazonaws.com/bucket/sponsors/1/logo/ktdc_logo.png',
  'https://s3.amazonaws.com/bucket/sponsors/1/hero/ktdc_hero.jpg',
  'https://s3.amazonaws.com/bucket/sponsors/1/banner/ktdc_banner.jpg',
  true, 100, 'https://facebook.com/keralatourism', 'https://twitter.com/keralatourism',
  'https://linkedin.com/company/kerala-tourism', 'https://instagram.com/keralatourism',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors VALUES (
  2, 'Tata Consultancy Services', 'Platinum Sponsor', 'TCS', 'Building on Belief',
  'Leading IT services and consulting company supporting cultural events and community development.',
  'https://www.tcs.com', 'sponsorship@tcs.com', '+91-22-67789999',
  'https://s3.amazonaws.com/bucket/sponsors/2/logo/tcs_logo.png',
  'https://s3.amazonaws.com/bucket/sponsors/2/hero/tcs_hero.jpg',
  'https://s3.amazonaws.com/bucket/sponsors/2/banner/tcs_banner.jpg',
  true, 90, 'https://facebook.com/tcs', 'https://twitter.com/tcs',
  'https://linkedin.com/company/tcs', 'https://instagram.com/tcs',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors VALUES (
  3, 'Federal Bank', 'Gold Sponsor', 'Federal Bank', 'Your Perfect Banking Partner',
  'Premier private sector bank committed to supporting cultural and community events.',
  'https://www.federalbank.co.in', 'events@federalbank.co.in', '+91-484-2630606',
  'https://s3.amazonaws.com/bucket/sponsors/3/logo/federal_logo.png',
  'https://s3.amazonaws.com/bucket/sponsors/3/hero/federal_hero.jpg',
  'https://s3.amazonaws.com/bucket/sponsors/3/banner/federal_banner.jpg',
  true, 80, 'https://facebook.com/federalbank', 'https://twitter.com/federalbank',
  'https://linkedin.com/company/federal-bank', 'https://instagram.com/federalbank',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors VALUES (
  4, 'Kerala State Beverages Corporation', 'Silver Sponsor', 'KSBC', 'Quality in Every Drop',
  'Government corporation supporting cultural events and promoting responsible consumption.',
  'https://www.ksbc.kerala.gov.in', 'info@ksbc.kerala.gov.in', '+91-471-2321234',
  'https://s3.amazonaws.com/bucket/sponsors/4/logo/ksbc_logo.png',
  'https://s3.amazonaws.com/bucket/sponsors/4/hero/ksbc_hero.jpg',
  'https://s3.amazonaws.com/bucket/sponsors/4/banner/ksbc_banner.jpg',
  true, 70, 'https://facebook.com/ksbc', NULL,
  'https://linkedin.com/company/ksbc', 'https://instagram.com/ksbc',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors VALUES (
  5, 'Malayalam Manorama', 'Media Partner', 'Manorama', 'Truth Above All',
  'Leading Malayalam newspaper and media group promoting cultural events and community engagement.',
  'https://www.manoramaonline.com', 'events@manoramaonline.com', '+91-471-2518000',
  'https://s3.amazonaws.com/bucket/sponsors/5/logo/manorama_logo.png',
  'https://s3.amazonaws.com/bucket/sponsors/5/hero/manorama_hero.jpg',
  'https://s3.amazonaws.com/bucket/sponsors/5/banner/manorama_banner.jpg',
  true, 60, 'https://facebook.com/manoramaonline', 'https://twitter.com/manoramaonline',
  'https://linkedin.com/company/manorama', 'https://instagram.com/manoramaonline',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

-- =====================================================
-- SAMPLE DATA FOR EVENT SPONSORS JOIN (Many-to-Many)
-- =====================================================

INSERT INTO public.event_sponsors_join VALUES (
  1, 1, 1, '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors_join VALUES (
  2, 1, 2, '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors_join VALUES (
  3, 1, 3, '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors_join VALUES (
  4, 2, 2, '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors_join VALUES (
  5, 2, 4, '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors_join VALUES (
  6, 3, 1, '2025-01-10 10:00:00'
);

INSERT INTO public.event_sponsors_join VALUES (
  7, 3, 5, '2025-01-10 10:00:00'
);

-- =====================================================
-- SAMPLE DATA FOR EVENT EMAILS
-- =====================================================

INSERT INTO public.event_emails VALUES (
  1, 1, 'info@malayaleesfestival.com', '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_emails VALUES (
  2, 1, 'tickets@malayaleesfestival.com', '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_emails VALUES (
  3, 1, 'sponsorship@malayaleesfestival.com', '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_emails VALUES (
  4, 2, 'contact@culturalnight.com', '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_emails VALUES (
  5, 2, 'media@culturalnight.com', '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_emails VALUES (
  6, 3, 'admin@musicconcert.com', '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

-- =====================================================
-- SAMPLE DATA FOR EVENT PROGRAM DIRECTORS
-- =====================================================

INSERT INTO public.event_program_directors VALUES (
  1, 1, 'Dr. Rajesh Kumar', 'https://s3.amazonaws.com/bucket/directors/1/photo/rajesh_photo.jpg',
  'Dr. Rajesh Kumar is a distinguished cultural director with over 25 years of experience in organizing large-scale cultural events. He has been instrumental in promoting Malayalam arts and culture across the globe.',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_program_directors VALUES (
  2, 1, 'Ms. Priya Menon', 'https://s3.amazonaws.com/bucket/directors/2/photo/priya_photo.jpg',
  'Ms. Priya Menon is an accomplished event director specializing in traditional Kerala cultural programs. She has successfully organized numerous festivals and cultural events with a focus on authentic representation of Malayalam traditions.',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_program_directors VALUES (
  3, 2, 'Mr. Suresh Nair', 'https://s3.amazonaws.com/bucket/directors/3/photo/suresh_photo.jpg',
  'Mr. Suresh Nair brings extensive experience in cultural event management and has been associated with several prestigious cultural organizations. His expertise lies in coordinating diverse artistic performances and managing large audiences.',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

INSERT INTO public.event_program_directors VALUES (
  4, 3, 'Dr. Anitha Pillai', 'https://s3.amazonaws.com/bucket/directors/4/photo/anitha_photo.jpg',
  'Dr. Anitha Pillai is a renowned music director and cultural consultant with deep knowledge of classical and contemporary music. She has curated numerous musical events and has been recognized for her contributions to promoting Indian classical music.',
  '2025-01-10 10:00:00', '2025-01-10 10:00:00'
);

-- =====================================================
-- NOTES FOR FRONTEND DEVELOPERS
-- =====================================================

/*
IMPORTANT NOTES FOR FRONTEND INTEGRATION:

1. BACKEND STATUS: All backend development is complete and ready for frontend integration
2. DATABASE: All tables are created with proper relationships and constraints
3. API ENDPOINTS: 50+ REST endpoints are implemented and documented
4. AUTHENTICATION: JWT-based security is configured
5. IMAGE UPLOAD: AWS S3 integration with dynamic path construction is ready
6. VALIDATION: Comprehensive input validation and error handling is implemented

FRONTEND DEVELOPERS ONLY NEED TO:
- Create UI components for entity management
- Integrate with existing REST API endpoints
- Implement image upload UI using documented endpoints
- Handle frontend validation to complement backend validation
- Design user experience workflows

NO BACKEND DEVELOPMENT, DATABASE SETUP, OR INFRASTRUCTURE CONFIGURATION IS REQUIRED.

SAMPLE DATA REFERENCE:
- Event IDs 1, 2, 3 correspond to existing events in the event_details table
- All image URLs are placeholder S3 URLs - backend handles actual URL generation
- Phone numbers follow international format
- Email addresses are example domains
- All timestamps are in ISO format
- Priority rankings are used for sorting and display order
- Boolean flags control visibility and active status

RELATIONSHIPS:
- EventFeaturedPerformers → EventDetails (Many-to-One)
- EventContacts → EventDetails (Many-to-One)
- EventEmails → EventDetails (Many-to-One)
- EventProgramDirectors → EventDetails (Many-to-One)
- EventSponsorsJoin → EventDetails (Many-to-One)
- EventSponsorsJoin → EventSponsors (Many-to-One)
- EventDetails ↔ EventSponsors (Many-to-Many via EventSponsorsJoin)

API ENDPOINTS AVAILABLE:
- GET /api/event-featured-performers
- POST /api/event-featured-performers
- GET /api/event-contacts
- POST /api/event-contacts
- GET /api/event-sponsors
- POST /api/event-sponsors
- GET /api/event-sponsors-join
- POST /api/event-sponsors-join
- GET /api/event-emails
- POST /api/event-emails
- GET /api/event-program-directors
- POST /api/event-program-directors

IMAGE UPLOAD ENDPOINTS:
- POST /api/event-medias/upload/featured-performer/{entityId}/{imageType}
- POST /api/event-medias/upload/sponsor/{entityId}/{imageType}
- POST /api/event-medias/upload/contact/{entityId}/photo
- POST /api/event-medias/upload/program-director/{entityId}/photo
- POST /api/event-medias/upload (enhanced general endpoint)
*/
