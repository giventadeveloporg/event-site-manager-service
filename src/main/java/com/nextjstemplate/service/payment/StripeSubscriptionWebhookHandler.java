package com.nextjstemplate.service.payment;

import com.nextjstemplate.domain.MembershipPlan;
import com.nextjstemplate.domain.MembershipSubscription;
import com.nextjstemplate.repository.MembershipPlanRepository;
import com.nextjstemplate.repository.MembershipSubscriptionRepository;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.security.TenantContext;
import com.nextjstemplate.service.MembershipSubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling Stripe subscription webhook events.
 *
 * CRITICAL: This service implements race condition prevention patterns:
 * - Uses findOrCreateSubscription with unique constraint handling
 * - Uses Propagation.REQUIRES_NEW to ensure fresh read-write transactions
 * - Explicitly sets TenantContext for proper multi-tenant handling
 */
@Service
public class StripeSubscriptionWebhookHandler {

    private static final Logger log = LoggerFactory.getLogger(StripeSubscriptionWebhookHandler.class);

    private final MembershipSubscriptionService membershipSubscriptionService;
    private final MembershipSubscriptionRepository membershipSubscriptionRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final UserProfileRepository userProfileRepository;

    public StripeSubscriptionWebhookHandler(
        MembershipSubscriptionService membershipSubscriptionService,
        MembershipSubscriptionRepository membershipSubscriptionRepository,
        MembershipPlanRepository membershipPlanRepository,
        UserProfileRepository userProfileRepository
    ) {
        this.membershipSubscriptionService = membershipSubscriptionService;
        this.membershipSubscriptionRepository = membershipSubscriptionRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Handle subscription-related webhook events.
     *
     * @param event Stripe webhook event
     * @param result Map to populate with results
     * @return true if event was handled, false if not a subscription event
     */
    public boolean handleSubscriptionEvent(Event event, Map<String, Object> result) {
        String eventType = event.getType();

        return switch (eventType) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted(event, result);
            case "customer.subscription.created" -> handleSubscriptionCreated(event, result);
            case "customer.subscription.updated" -> handleSubscriptionUpdated(event, result);
            case "customer.subscription.deleted" -> handleSubscriptionDeleted(event, result);
            case "invoice.payment_succeeded" -> handleInvoicePaymentSucceeded(event, result);
            case "invoice.payment_failed" -> handleInvoicePaymentFailed(event, result);
            default -> false;
        };
    }

    /**
     * Handle checkout.session.completed event.
     * Creates subscription after successful checkout payment.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean handleCheckoutSessionCompleted(Event event, Map<String, Object> result) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

        if (session == null) {
            log.warn("Checkout session is null in checkout.session.completed webhook event");
            return false;
        }

        // Only process subscription mode sessions
        if (!"subscription".equals(session.getMode())) {
            log.debug("Checkout session {} is not a subscription, skipping", session.getId());
            return false;
        }

        String stripeSubscriptionId = session.getSubscription();
        String stripeCustomerId = session.getCustomer();

        if (stripeSubscriptionId == null || stripeSubscriptionId.isEmpty()) {
            log.warn("Checkout session {} has no subscription ID", session.getId());
            return false;
        }

        log.info("[StripeSubscriptionWebhookHandler] Processing checkout.session.completed for subscription {}", stripeSubscriptionId);

        // Extract metadata
        Map<String, String> metadata = session.getMetadata();
        String tenantId = metadata != null ? metadata.get("tenantId") : null;
        String userProfileIdStr = metadata != null ? metadata.get("userProfileId") : null;
        String membershipPlanIdStr = metadata != null ? metadata.get("membershipPlanId") : null;

        if (tenantId == null || userProfileIdStr == null || membershipPlanIdStr == null) {
            log.error(
                "Missing required metadata in checkout session {}. tenantId={}, userProfileId={}, membershipPlanId={}",
                session.getId(),
                tenantId,
                userProfileIdStr,
                membershipPlanIdStr
            );
            result.put("error", "Missing required metadata");
            return true;
        }

        // Set tenant context
        TenantContext.setCurrentTenant(tenantId);
        try {
            Long userProfileId = Long.parseLong(userProfileIdStr);
            Long membershipPlanId = Long.parseLong(membershipPlanIdStr);

            // Find or create subscription using race-condition-safe method
            MembershipSubscription subscription = membershipSubscriptionService.findOrCreateSubscription(
                stripeSubscriptionId,
                stripeCustomerId,
                tenantId,
                userProfileId,
                membershipPlanId,
                null // Stripe Subscription object not available, will use defaults
            );

            log.info(
                "[StripeSubscriptionWebhookHandler] Processed checkout.session.completed - subscription {} created/found",
                subscription.getId()
            );

            result.put("subscriptionId", subscription.getId());
            result.put("stripeSubscriptionId", stripeSubscriptionId);
            result.put("status", subscription.getSubscriptionStatus());
            return true;
        } catch (Exception e) {
            log.error(
                "[StripeSubscriptionWebhookHandler] Error processing checkout.session.completed for session {}: {}",
                session.getId(),
                e.getMessage(),
                e
            );
            result.put("error", e.getMessage());
            return true;
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Handle customer.subscription.created event.
     * Creates or updates subscription record when Stripe subscription is created.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean handleSubscriptionCreated(Event event, Map<String, Object> result) {
        Subscription stripeSubscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);

        if (stripeSubscription == null) {
            log.warn("Subscription is null in customer.subscription.created webhook event");
            return false;
        }

        String stripeSubscriptionId = stripeSubscription.getId();
        String stripeCustomerId = stripeSubscription.getCustomer();

        log.info("[StripeSubscriptionWebhookHandler] Processing customer.subscription.created for subscription {}", stripeSubscriptionId);

        // Extract metadata
        Map<String, String> metadata = stripeSubscription.getMetadata();
        String tenantId = metadata != null ? metadata.get("tenantId") : null;
        String userProfileIdStr = metadata != null ? metadata.get("userProfileId") : null;
        String membershipPlanIdStr = metadata != null ? metadata.get("membershipPlanId") : null;

        // If metadata is missing, try to find existing subscription
        if (tenantId == null || userProfileIdStr == null || membershipPlanIdStr == null) {
            // Check if subscription already exists (might have been created by checkout.session.completed)
            return membershipSubscriptionRepository
                .findByStripeSubscriptionId(stripeSubscriptionId)
                .map(subscription -> {
                    log.info("Subscription {} already exists, updating with Stripe data", stripeSubscriptionId);
                    updateSubscriptionFromStripe(subscription, stripeSubscription);
                    membershipSubscriptionRepository.save(subscription);
                    result.put("subscriptionId", subscription.getId());
                    result.put("status", subscription.getSubscriptionStatus());
                    return true;
                })
                .orElseGet(() -> {
                    log.warn(
                        "Missing metadata in customer.subscription.created and no existing subscription found. " +
                        "stripeSubscriptionId={}, tenantId={}, userProfileId={}, membershipPlanId={}",
                        stripeSubscriptionId,
                        tenantId,
                        userProfileIdStr,
                        membershipPlanIdStr
                    );
                    result.put("warning", "Missing metadata and no existing subscription");
                    return true;
                });
        }

        // Set tenant context
        TenantContext.setCurrentTenant(tenantId);
        try {
            Long userProfileId = Long.parseLong(userProfileIdStr);
            Long membershipPlanId = Long.parseLong(membershipPlanIdStr);

            // Find or create subscription using race-condition-safe method
            MembershipSubscription subscription = membershipSubscriptionService.findOrCreateSubscription(
                stripeSubscriptionId,
                stripeCustomerId,
                tenantId,
                userProfileId,
                membershipPlanId,
                stripeSubscription
            );

            log.info(
                "[StripeSubscriptionWebhookHandler] Processed customer.subscription.created - subscription {} created/updated",
                subscription.getId()
            );

            result.put("subscriptionId", subscription.getId());
            result.put("stripeSubscriptionId", stripeSubscriptionId);
            result.put("status", subscription.getSubscriptionStatus());
            return true;
        } catch (Exception e) {
            log.error(
                "[StripeSubscriptionWebhookHandler] Error processing customer.subscription.created for {}: {}",
                stripeSubscriptionId,
                e.getMessage(),
                e
            );
            result.put("error", e.getMessage());
            return true;
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Handle customer.subscription.updated event.
     * Updates subscription record when Stripe subscription is updated.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean handleSubscriptionUpdated(Event event, Map<String, Object> result) {
        Subscription stripeSubscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);

        if (stripeSubscription == null) {
            log.warn("Subscription is null in customer.subscription.updated webhook event");
            return false;
        }

        String stripeSubscriptionId = stripeSubscription.getId();

        log.info("[StripeSubscriptionWebhookHandler] Processing customer.subscription.updated for subscription {}", stripeSubscriptionId);

        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                // Set tenant context
                TenantContext.setCurrentTenant(subscription.getTenantId());
                try {
                    updateSubscriptionFromStripe(subscription, stripeSubscription);
                    membershipSubscriptionRepository.save(subscription);

                    log.info(
                        "[StripeSubscriptionWebhookHandler] Updated subscription {} from Stripe. Status: {}",
                        subscription.getId(),
                        subscription.getSubscriptionStatus()
                    );

                    result.put("subscriptionId", subscription.getId());
                    result.put("stripeSubscriptionId", stripeSubscriptionId);
                    result.put("status", subscription.getSubscriptionStatus());
                    return true;
                } finally {
                    TenantContext.clear();
                }
            })
            .orElseGet(() -> {
                log.warn("No local subscription found for Stripe subscription {}", stripeSubscriptionId);
                result.put("warning", "Subscription not found locally");
                return true;
            });
    }

    /**
     * Handle customer.subscription.deleted event.
     * Marks subscription as cancelled when Stripe subscription is deleted.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean handleSubscriptionDeleted(Event event, Map<String, Object> result) {
        Subscription stripeSubscription = (Subscription) event.getDataObjectDeserializer().getObject().orElse(null);

        if (stripeSubscription == null) {
            log.warn("Subscription is null in customer.subscription.deleted webhook event");
            return false;
        }

        String stripeSubscriptionId = stripeSubscription.getId();

        log.info("[StripeSubscriptionWebhookHandler] Processing customer.subscription.deleted for subscription {}", stripeSubscriptionId);

        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                // Set tenant context
                TenantContext.setCurrentTenant(subscription.getTenantId());
                try {
                    subscription.setSubscriptionStatus("CANCELLED");
                    subscription.setCancelledAt(ZonedDateTime.now());
                    subscription.setUpdatedAt(ZonedDateTime.now());
                    membershipSubscriptionRepository.save(subscription);

                    log.info(
                        "[StripeSubscriptionWebhookHandler] Marked subscription {} as CANCELLED due to Stripe deletion",
                        subscription.getId()
                    );

                    result.put("subscriptionId", subscription.getId());
                    result.put("stripeSubscriptionId", stripeSubscriptionId);
                    result.put("status", "CANCELLED");
                    return true;
                } finally {
                    TenantContext.clear();
                }
            })
            .orElseGet(() -> {
                log.warn("No local subscription found for deleted Stripe subscription {}", stripeSubscriptionId);
                result.put("warning", "Subscription not found locally");
                return true;
            });
    }

    /**
     * Handle invoice.payment_succeeded event.
     * Updates subscription status to ACTIVE if was PAST_DUE or TRIAL.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean handleInvoicePaymentSucceeded(Event event, Map<String, Object> result) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElse(null);

        if (invoice == null) {
            log.warn("Invoice is null in invoice.payment_succeeded webhook event");
            return false;
        }

        // Only process subscription invoices
        String stripeSubscriptionId = invoice.getSubscription();
        if (stripeSubscriptionId == null || stripeSubscriptionId.isEmpty()) {
            log.debug("Invoice {} is not a subscription invoice, skipping", invoice.getId());
            return false;
        }

        log.info("[StripeSubscriptionWebhookHandler] Processing invoice.payment_succeeded for subscription {}", stripeSubscriptionId);

        final Invoice invoiceFinal = invoice;
        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                // Set tenant context
                TenantContext.setCurrentTenant(subscription.getTenantId());
                try {
                    String previousStatus = subscription.getSubscriptionStatus();

                    // Transition from PAST_DUE or TRIAL to ACTIVE
                    if ("PAST_DUE".equals(previousStatus) || "TRIAL".equals(previousStatus)) {
                        subscription.setSubscriptionStatus("ACTIVE");
                    }

                    // Update period dates from invoice
                    if (invoiceFinal.getPeriodStart() != null) {
                        subscription.setCurrentPeriodStart(convertToLocalDate(invoiceFinal.getPeriodStart()));
                    }
                    if (invoiceFinal.getPeriodEnd() != null) {
                        subscription.setCurrentPeriodEnd(convertToLocalDate(invoiceFinal.getPeriodEnd()));
                    }

                    subscription.setUpdatedAt(ZonedDateTime.now());
                    membershipSubscriptionRepository.save(subscription);

                    log.info(
                        "[StripeSubscriptionWebhookHandler] Processed invoice.payment_succeeded - subscription {} status: {} -> {}",
                        subscription.getId(),
                        previousStatus,
                        subscription.getSubscriptionStatus()
                    );

                    result.put("subscriptionId", subscription.getId());
                    result.put("stripeSubscriptionId", stripeSubscriptionId);
                    result.put("status", subscription.getSubscriptionStatus());
                    result.put("invoiceId", invoiceFinal.getId());
                    return true;
                } finally {
                    TenantContext.clear();
                }
            })
            .orElseGet(() -> {
                log.warn("No local subscription found for Stripe subscription {} (from invoice)", stripeSubscriptionId);
                result.put("warning", "Subscription not found locally");
                return true;
            });
    }

    /**
     * Handle invoice.payment_failed event.
     * Updates subscription status to PAST_DUE.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private boolean handleInvoicePaymentFailed(Event event, Map<String, Object> result) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElse(null);

        if (invoice == null) {
            log.warn("Invoice is null in invoice.payment_failed webhook event");
            return false;
        }

        // Only process subscription invoices
        String stripeSubscriptionId = invoice.getSubscription();
        if (stripeSubscriptionId == null || stripeSubscriptionId.isEmpty()) {
            log.debug("Invoice {} is not a subscription invoice, skipping", invoice.getId());
            return false;
        }

        log.info("[StripeSubscriptionWebhookHandler] Processing invoice.payment_failed for subscription {}", stripeSubscriptionId);

        final Invoice invoiceFinal = invoice;
        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                // Set tenant context
                TenantContext.setCurrentTenant(subscription.getTenantId());
                try {
                    String previousStatus = subscription.getSubscriptionStatus();
                    subscription.setSubscriptionStatus("PAST_DUE");
                    subscription.setUpdatedAt(ZonedDateTime.now());
                    membershipSubscriptionRepository.save(subscription);

                    log.info(
                        "[StripeSubscriptionWebhookHandler] Processed invoice.payment_failed - subscription {} status: {} -> PAST_DUE",
                        subscription.getId(),
                        previousStatus
                    );

                    result.put("subscriptionId", subscription.getId());
                    result.put("stripeSubscriptionId", stripeSubscriptionId);
                    result.put("status", "PAST_DUE");
                    result.put("invoiceId", invoiceFinal.getId());
                    return true;
                } finally {
                    TenantContext.clear();
                }
            })
            .orElseGet(() -> {
                log.warn("No local subscription found for Stripe subscription {} (from failed invoice)", stripeSubscriptionId);
                result.put("warning", "Subscription not found locally");
                return true;
            });
    }

    // ==================== HELPER METHODS ====================

    private void updateSubscriptionFromStripe(MembershipSubscription subscription, Subscription stripeSubscription) {
        // Update period dates
        if (stripeSubscription.getCurrentPeriodStart() != null) {
            subscription.setCurrentPeriodStart(convertToLocalDate(stripeSubscription.getCurrentPeriodStart()));
        }
        if (stripeSubscription.getCurrentPeriodEnd() != null) {
            subscription.setCurrentPeriodEnd(convertToLocalDate(stripeSubscription.getCurrentPeriodEnd()));
        }

        // Update trial dates
        if (stripeSubscription.getTrialStart() != null) {
            subscription.setTrialStart(convertToLocalDate(stripeSubscription.getTrialStart()));
        }
        if (stripeSubscription.getTrialEnd() != null) {
            subscription.setTrialEnd(convertToLocalDate(stripeSubscription.getTrialEnd()));
        }

        // Update status
        subscription.setSubscriptionStatus(mapStripeStatus(stripeSubscription.getStatus()));

        // Update cancellation info
        subscription.setCancelAtPeriodEnd(Boolean.TRUE.equals(stripeSubscription.getCancelAtPeriodEnd()));
        if (stripeSubscription.getCanceledAt() != null) {
            subscription.setCancelledAt(
                ZonedDateTime.ofInstant(Instant.ofEpochSecond(stripeSubscription.getCanceledAt()), ZoneId.systemDefault())
            );
        }

        // Update customer ID if changed
        if (stripeSubscription.getCustomer() != null) {
            subscription.setStripeCustomerId(stripeSubscription.getCustomer());
        }

        subscription.setUpdatedAt(ZonedDateTime.now());
    }

    private LocalDate convertToLocalDate(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }
        return Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String mapStripeStatus(String stripeStatus) {
        if (stripeStatus == null || stripeStatus.isEmpty()) {
            return "ACTIVE";
        }

        return switch (stripeStatus.toLowerCase()) {
            case "active" -> "ACTIVE";
            case "trialing" -> "TRIAL";
            case "past_due" -> "PAST_DUE";
            case "canceled", "cancelled" -> "CANCELLED";
            case "unpaid" -> "SUSPENDED";
            case "incomplete", "incomplete_expired" -> "EXPIRED";
            default -> "ACTIVE";
        };
    }
}
