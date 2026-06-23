package com.eventsitemanager.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for synchronizing the sequence_generator sequence with the maximum IDs
 * across all tables that use it. This prevents duplicate key constraint violations
 * when the sequence gets out of sync with manually inserted data.
 */
@Service
public class SequenceSynchronizationService {

    private static final Logger log = LoggerFactory.getLogger(SequenceSynchronizationService.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Synchronizes the sequence_generator sequence to be at least as high as
     * the maximum ID across all tables that use it.
     * This is a runtime fix for when the sequence gets out of sync.
     *
     * @return the new sequence value that was set
     */
    @Transactional
    public Long synchronizeSequence() {
        log.info("Synchronizing sequence_generator with maximum IDs across all tables...");

        // Build the SQL query to find the maximum ID across all tables using sequence_generator
        // This matches the logic in Latest_Schema_Post__Blob_Claude_12.sql
        String sql =
            """
            SELECT pg_catalog.setval(
                'public.sequence_generator',
                GREATEST(
                    COALESCE((SELECT MAX(id) FROM public.user_profile), 0),
                    COALESCE((SELECT MAX(id) FROM public.bulk_operation_log), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_type_details), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_details), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_recurrence_series), 0),
                    COALESCE((SELECT MAX(id) FROM public.focus_group), 0),
                    COALESCE((SELECT MAX(id) FROM public.focus_group_members), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_focus_groups), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_guest_pricing), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_admin), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_admin_audit_log), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_attendee), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_attendee_guest), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_calendar_entry), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_sponsors), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_sponsors_join), 0),
                    COALESCE((SELECT MAX(id) FROM public.gallery_album), 0),
                    COALESCE((SELECT MAX(id) FROM public.gallery_category), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_media), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_organizer), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_poll), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_poll_option), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_poll_response), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_ticket_transaction), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_ticket_type), 0),
                    COALESCE((SELECT MAX(id) FROM public.qr_code_usage), 0),
                    COALESCE((SELECT MAX(id) FROM public.tenant_organization), 0),
                    COALESCE((SELECT MAX(id) FROM public.tenant_settings), 0),
                    COALESCE((SELECT MAX(id) FROM public.tenant_email_addresses), 0),
                    COALESCE((SELECT MAX(id) FROM public.user_payment_transaction), 0),
                    COALESCE((SELECT MAX(id) FROM public.user_subscription), 0),
                    COALESCE((SELECT MAX(id) FROM public.user_task), 0),
                    COALESCE((SELECT MAX(id) FROM public.executive_committee_team_members), 0),
                    COALESCE((SELECT MAX(id) FROM public.communication_campaign), 0),
                    COALESCE((SELECT MAX(id) FROM public.email_log), 0),
                    COALESCE((SELECT MAX(id) FROM public.whatsapp_log), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_featured_performers), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_contacts), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_emails), 0),
                    COALESCE((SELECT MAX(id) FROM public.event_program_directors), 0),
                    COALESCE((SELECT MAX(id) FROM public.payment_provider_config), 0),
                    COALESCE((SELECT MAX(id) FROM public.manual_payment_request), 0),
                    COALESCE((SELECT MAX(id) FROM public.manual_payment_summary_report), 0),
                    COALESCE((SELECT MAX(id) FROM public.platform_settlement), 0),
                    COALESCE((SELECT MAX(id) FROM public.platform_invoice), 0),
                    COALESCE((SELECT MAX(id) FROM public.membership_plan), 0),
                    COALESCE((SELECT MAX(id) FROM public.membership_subscription), 0),
                    COALESCE((SELECT MAX(id) FROM public.membership_subscription_reconciliation_log), 0),
                    COALESCE((SELECT MAX(id) FROM public.promotion_email_template), 0),
                    COALESCE((SELECT MAX(id) FROM public.promotion_email_sent_log), 0),
                    COALESCE((SELECT MAX(id) FROM public.clerk_user_tenant), 0),
                    COALESCE((SELECT MAX(id) FROM public.clerk_organization_role), 0),
                    COALESCE((SELECT MAX(id) FROM public.clerk_webhook_event), 0),
                    COALESCE((SELECT MAX(id) FROM public.clerk_session), 0),
                    1
                ),
                true
            )
            """;

        try {
            Query query = entityManager.createNativeQuery(sql);
            Object result = query.getSingleResult();
            Long newSequenceValue = result != null ? ((Number) result).longValue() : null;

            log.info("Sequence synchronized successfully. New sequence value: {}", newSequenceValue);

            // Verify the synchronization
            Query verifyQuery = entityManager.createNativeQuery("SELECT last_value FROM public.sequence_generator");
            Object verifyResult = verifyQuery.getSingleResult();
            Long actualSequenceValue = verifyResult != null ? ((Number) verifyResult).longValue() : null;

            log.info("Verified sequence value: {}", actualSequenceValue);

            return actualSequenceValue;
        } catch (Exception e) {
            log.error("Failed to synchronize sequence_generator", e);
            throw new RuntimeException("Failed to synchronize sequence_generator: " + e.getMessage(), e);
        }
    }
}
