-- Per-table application id sequences (replaces shared sequence_generator)
-- =====================================================

DROP SEQUENCE IF EXISTS public.sequence_generator CASCADE;

CREATE SEQUENCE IF NOT EXISTS public.user_profile_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.bulk_operation_log_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_type_details_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_details_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_recurrence_series_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.focus_group_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.focus_group_members_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_focus_groups_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_guest_pricing_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_admin_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_admin_audit_log_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_attendee_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_attendee_guest_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_attendee_attachment_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_calendar_entry_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_sponsors_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_sponsors_join_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.gallery_category_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.gallery_album_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.official_document_category_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_media_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.official_document_year_bundle_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_organizer_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_poll_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_poll_option_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_poll_response_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_ticket_transaction_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_ticket_type_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_ticket_transaction_item_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.qr_code_usage_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.rel_event_details__discount_codes_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.tenant_organization_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.tenant_settings_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.tenant_email_addresses_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.user_payment_transaction_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.user_subscription_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.user_task_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.executive_committee_team_members_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.team_groups_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.team_members_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.communication_campaign_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.email_log_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.whatsapp_log_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_featured_performers_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_contacts_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_emails_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_program_directors_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.clerk_user_tenant_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.clerk_organization_role_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.clerk_webhook_event_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.clerk_session_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.payment_provider_config_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.manual_payment_request_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.manual_payment_summary_report_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.platform_settlement_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.platform_invoice_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.membership_plan_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.membership_subscription_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.membership_subscription_reconciliation_log_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.promotion_email_template_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.promotion_email_sent_log_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.donation_transaction_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.donation_statistics_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.satellite_domain_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_category_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_article_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_section_display_config_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_sidebar_promotion_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_flash_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_live_stream_config_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.news_article_category_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_settings_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_day_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_participant_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_registration_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_result_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_content_block_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.event_competition_group_member_id_seq
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    START WITH 1
    CACHE 1;


--
-- TOC entry 230 (class 1259 OID 82796)
