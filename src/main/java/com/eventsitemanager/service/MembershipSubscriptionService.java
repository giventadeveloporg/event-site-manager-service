package com.eventsitemanager.service;

import com.eventsitemanager.domain.MembershipPlan;
import com.eventsitemanager.domain.MembershipSubscription;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.MembershipPlanRepository;
import com.eventsitemanager.repository.MembershipSubscriptionRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.security.TenantContext;
import com.eventsitemanager.service.dto.MembershipSubscriptionDTO;
import com.eventsitemanager.service.mapper.MembershipSubscriptionMapper;
import com.eventsitemanager.service.payment.PaymentException;
import com.eventsitemanager.service.payment.StripeSubscriptionService;
import com.stripe.model.Subscription;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing membership subscriptions.
 * Handles subscription lifecycle including creation, updates, cancellations, and status transitions.
 *
 * CRITICAL: This service implements race condition prevention patterns based on TicketGenerationService:
 * - findOrCreateSubscription() uses global duplicate check before creating
 * - DataIntegrityViolationException handling for unique constraint violations
 * - Propagation.REQUIRES_NEW for methods called from read-only contexts
 */
@Service
@Transactional
public class MembershipSubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(MembershipSubscriptionService.class);

    private static final List<String> ACTIVE_STATUSES = Arrays.asList("ACTIVE", "TRIAL", "PAST_DUE");

    private final MembershipSubscriptionRepository membershipSubscriptionRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final UserProfileRepository userProfileRepository;
    private final MembershipSubscriptionMapper membershipSubscriptionMapper;
    private final StripeSubscriptionService stripeSubscriptionService;

    public MembershipSubscriptionService(
        MembershipSubscriptionRepository membershipSubscriptionRepository,
        MembershipPlanRepository membershipPlanRepository,
        UserProfileRepository userProfileRepository,
        MembershipSubscriptionMapper membershipSubscriptionMapper,
        StripeSubscriptionService stripeSubscriptionService
    ) {
        this.membershipSubscriptionRepository = membershipSubscriptionRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.userProfileRepository = userProfileRepository;
        this.membershipSubscriptionMapper = membershipSubscriptionMapper;
        this.stripeSubscriptionService = stripeSubscriptionService;
    }

    // ==================== CORE CRUD OPERATIONS ====================

    /**
     * Save a membership subscription.
     *
     * @param membershipSubscriptionDTO the entity to save
     * @return the persisted entity
     */
    public MembershipSubscriptionDTO save(MembershipSubscriptionDTO membershipSubscriptionDTO) {
        log.debug("Request to save MembershipSubscription : {}", membershipSubscriptionDTO);

        MembershipSubscription subscription = membershipSubscriptionMapper.toEntity(membershipSubscriptionDTO);

        // Set timestamps
        ZonedDateTime now = ZonedDateTime.now();
        if (subscription.getId() == null) {
            subscription.setCreatedAt(now);
        }
        subscription.setUpdatedAt(now);

        subscription = membershipSubscriptionRepository.save(subscription);
        return membershipSubscriptionMapper.toDto(subscription);
    }

    /**
     * Partially update a membership subscription.
     *
     * @param membershipSubscriptionDTO the entity to update partially
     * @return the persisted entity
     */
    public Optional<MembershipSubscriptionDTO> partialUpdate(MembershipSubscriptionDTO membershipSubscriptionDTO) {
        log.debug("Request to partially update MembershipSubscription : {}", membershipSubscriptionDTO);

        return membershipSubscriptionRepository
            .findById(membershipSubscriptionDTO.getId())
            .map(existingSubscription -> {
                membershipSubscriptionMapper.partialUpdate(existingSubscription, membershipSubscriptionDTO);
                existingSubscription.setUpdatedAt(ZonedDateTime.now());
                return existingSubscription;
            })
            .map(membershipSubscriptionRepository::save)
            .map(membershipSubscriptionMapper::toDto);
    }

    /**
     * Get all membership subscriptions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MembershipSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipSubscriptions");
        return membershipSubscriptionRepository.findAll(pageable).map(membershipSubscriptionMapper::toDto);
    }

    /**
     * Get one membership subscription by ID.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MembershipSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get MembershipSubscription : {}", id);
        return membershipSubscriptionRepository.findById(id).map(membershipSubscriptionMapper::toDto);
    }

    /**
     * Delete a membership subscription.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MembershipSubscription : {}", id);
        membershipSubscriptionRepository.deleteById(id);
    }

    // ==================== SUBSCRIPTION LOOKUP ====================

    /**
     * Find user's active subscription.
     *
     * @param userProfileId the user profile ID
     * @param tenantId the tenant ID
     * @return the active subscription if found
     */
    @Transactional(readOnly = true)
    public Optional<MembershipSubscriptionDTO> findActiveByUserProfileIdAndTenantId(Long userProfileId, String tenantId) {
        log.debug("Request to get active MembershipSubscription for user {} in tenant {}", userProfileId, tenantId);
        return membershipSubscriptionRepository
            .findActiveSubscriptionByUserProfileIdAndTenantId(userProfileId, tenantId)
            .map(membershipSubscriptionMapper::toDto);
    }

    /**
     * Find subscription by Stripe subscription ID.
     *
     * @param stripeSubscriptionId the Stripe subscription ID
     * @return the subscription if found
     */
    @Transactional(readOnly = true)
    public Optional<MembershipSubscriptionDTO> findByStripeSubscriptionId(String stripeSubscriptionId) {
        log.debug("Request to get MembershipSubscription by Stripe ID: {}", stripeSubscriptionId);
        return membershipSubscriptionRepository.findByStripeSubscriptionId(stripeSubscriptionId).map(membershipSubscriptionMapper::toDto);
    }

    /**
     * Find subscriptions by tenant and status.
     *
     * @param tenantId the tenant ID
     * @param status the subscription status
     * @return list of subscriptions
     */
    @Transactional(readOnly = true)
    public List<MembershipSubscriptionDTO> findByTenantIdAndStatus(String tenantId, String status) {
        log.debug("Request to get MembershipSubscriptions for tenant {} with status {}", tenantId, status);
        return membershipSubscriptionRepository
            .findByTenantIdAndSubscriptionStatus(tenantId, status)
            .stream()
            .map(membershipSubscriptionMapper::toDto)
            .toList();
    }

    // ==================== RACE CONDITION PREVENTION ====================

    /**
     * Find existing subscription or create a new one.
     *
     * CRITICAL: This method implements GLOBAL duplicate prevention based on TicketGenerationService pattern.
     * Only ONE subscription can exist per stripe_subscription_id, regardless of tenant.
     *
     * Uses Propagation.REQUIRES_NEW to ensure a fresh read-write transaction.
     * This is necessary because this method may be called from:
     * 1. Webhook handlers
     * 2. Status polling endpoints with @Transactional(readOnly = true)
     *
     * @param stripeSubscriptionId Stripe subscription ID
     * @param stripeCustomerId Stripe customer ID
     * @param tenantId Tenant ID
     * @param userProfileId User Profile ID
     * @param membershipPlanId Membership Plan ID
     * @param stripeSubscription Stripe Subscription object (for dates)
     * @return MembershipSubscription (existing or newly created)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MembershipSubscription findOrCreateSubscription(
        String stripeSubscriptionId,
        String stripeCustomerId,
        String tenantId,
        Long userProfileId,
        Long membershipPlanId,
        Subscription stripeSubscription
    ) {
        // CRITICAL: First check if subscription exists for this stripe_subscription_id (GLOBAL check)
        if (stripeSubscriptionId != null && !stripeSubscriptionId.isEmpty()) {
            MembershipSubscription existingSubscription = membershipSubscriptionRepository
                .findByStripeSubscriptionId(stripeSubscriptionId)
                .map(existing -> {
                    if (tenantId.equals(existing.getTenantId())) {
                        log.info(
                            "Found existing subscription {} for stripe_subscription_id {} and tenant {}",
                            existing.getId(),
                            stripeSubscriptionId,
                            tenantId
                        );
                        return updateExistingSubscription(existing, stripeSubscription);
                    } else {
                        // DUPLICATE PREVENTION: Different tenant but same Stripe ID - return existing
                        log.warn(
                            "DUPLICATE PREVENTION: Subscription {} already exists for stripe_subscription_id {} " +
                            "with tenant '{}', but current request has tenant '{}'. Returning existing.",
                            existing.getId(),
                            stripeSubscriptionId,
                            existing.getTenantId(),
                            tenantId
                        );
                        return existing;
                    }
                })
                .orElse(null);

            if (existingSubscription != null) {
                return existingSubscription;
            }
        }

        // No existing subscription found - create new one
        log.info(
            "No existing subscription found for stripe_subscription_id {}, creating new for tenant {}",
            stripeSubscriptionId,
            tenantId
        );

        try {
            return createNewSubscription(
                stripeSubscriptionId,
                stripeCustomerId,
                tenantId,
                userProfileId,
                membershipPlanId,
                stripeSubscription
            );
        } catch (DataIntegrityViolationException e) {
            // CRITICAL: Handle unique constraint violation (race condition)
            // Another thread/webhook created a record between our check and insert
            log.warn(
                "Unique constraint violation for stripe_subscription_id {} - another thread created it. Fetching existing record.",
                stripeSubscriptionId
            );

            return membershipSubscriptionRepository
                .findByStripeSubscriptionId(stripeSubscriptionId)
                .orElseThrow(() -> {
                    log.error(
                        "CRITICAL: Unique constraint violated but no record found for stripe_subscription_id {}",
                        stripeSubscriptionId
                    );
                    return new IllegalStateException("Unique constraint violated but no record found: " + stripeSubscriptionId);
                });
        }
    }

    /**
     * Create a new subscription record.
     */
    private MembershipSubscription createNewSubscription(
        String stripeSubscriptionId,
        String stripeCustomerId,
        String tenantId,
        Long userProfileId,
        Long membershipPlanId,
        Subscription stripeSubscription
    ) {
        // Check if subscription already exists by stripeSubscriptionId and tenantId
        if (stripeSubscriptionId != null && !stripeSubscriptionId.isEmpty() && tenantId != null) {
            MembershipSubscription existing = membershipSubscriptionRepository
                .findByStripeSubscriptionIdAndTenantId(stripeSubscriptionId, tenantId)
                .orElse(null);
            if (existing != null) {
                // Return existing subscription instead of creating duplicate
                log.info(
                    "Subscription already exists for stripe_subscription_id {} and tenant {}. Returning existing subscription {}",
                    stripeSubscriptionId,
                    tenantId,
                    existing.getId()
                );
                return existing;
            }
        }

        MembershipSubscription subscription = new MembershipSubscription();
        subscription.setTenantId(tenantId);
        subscription.setStripeSubscriptionId(stripeSubscriptionId);
        subscription.setStripeCustomerId(stripeCustomerId);

        // Set user profile
        UserProfile userProfile = userProfileRepository
            .findById(userProfileId)
            .orElseThrow(() -> new IllegalArgumentException("User profile not found: " + userProfileId));
        subscription.setUserProfile(userProfile);

        // Set membership plan
        MembershipPlan membershipPlan = membershipPlanRepository
            .findById(membershipPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Membership plan not found: " + membershipPlanId));
        subscription.setMembershipPlan(membershipPlan);

        // Set dates from Stripe subscription
        if (stripeSubscription != null) {
            subscription.setCurrentPeriodStart(convertToLocalDate(stripeSubscription.getCurrentPeriodStart()));
            subscription.setCurrentPeriodEnd(convertToLocalDate(stripeSubscription.getCurrentPeriodEnd()));

            // Handle trial period
            if (stripeSubscription.getTrialStart() != null && stripeSubscription.getTrialEnd() != null) {
                subscription.setTrialStart(convertToLocalDate(stripeSubscription.getTrialStart()));
                subscription.setTrialEnd(convertToLocalDate(stripeSubscription.getTrialEnd()));
                subscription.setSubscriptionStatus("TRIAL");
            } else {
                subscription.setSubscriptionStatus(mapStripeStatus(stripeSubscription.getStatus()));
            }

            subscription.setCancelAtPeriodEnd(Boolean.TRUE.equals(stripeSubscription.getCancelAtPeriodEnd()));
        } else {
            // Default dates if no Stripe subscription provided
            LocalDate now = LocalDate.now();
            subscription.setCurrentPeriodStart(now);
            subscription.setCurrentPeriodEnd(calculatePeriodEnd(now, membershipPlan.getBillingInterval()));
            subscription.setSubscriptionStatus("ACTIVE");

            // Handle trial period from plan
            if (membershipPlan.getTrialDays() != null && membershipPlan.getTrialDays() > 0) {
                subscription.setTrialStart(now);
                subscription.setTrialEnd(now.plusDays(membershipPlan.getTrialDays()));
                subscription.setCurrentPeriodEnd(subscription.getTrialEnd());
                subscription.setSubscriptionStatus("TRIAL");
            }
        }

        subscription.setCancelAtPeriodEnd(false);
        subscription.setCreatedAt(ZonedDateTime.now());
        subscription.setUpdatedAt(ZonedDateTime.now());

        return membershipSubscriptionRepository.save(subscription);
    }

    /**
     * Update an existing subscription with fresh Stripe data.
     */
    private MembershipSubscription updateExistingSubscription(MembershipSubscription existing, Subscription stripeSubscription) {
        if (stripeSubscription != null) {
            existing.setCurrentPeriodStart(convertToLocalDate(stripeSubscription.getCurrentPeriodStart()));
            existing.setCurrentPeriodEnd(convertToLocalDate(stripeSubscription.getCurrentPeriodEnd()));
            existing.setSubscriptionStatus(mapStripeStatus(stripeSubscription.getStatus()));
            existing.setCancelAtPeriodEnd(Boolean.TRUE.equals(stripeSubscription.getCancelAtPeriodEnd()));

            if (stripeSubscription.getCanceledAt() != null) {
                existing.setCancelledAt(
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(stripeSubscription.getCanceledAt()), ZoneId.systemDefault())
                );
            }
        }
        existing.setUpdatedAt(ZonedDateTime.now());
        return membershipSubscriptionRepository.save(existing);
    }

    // ==================== STATUS TRANSITIONS ====================

    /**
     * Handle trial expiration - transition from TRIAL to ACTIVE.
     */
    public MembershipSubscriptionDTO handleTrialExpired(Long subscriptionId) {
        log.info("Handling trial expiration for subscription {}", subscriptionId);

        return membershipSubscriptionRepository
            .findById(subscriptionId)
            .map(subscription -> {
                if ("TRIAL".equals(subscription.getSubscriptionStatus())) {
                    subscription.setSubscriptionStatus("ACTIVE");
                    subscription.setUpdatedAt(ZonedDateTime.now());

                    // Update period dates
                    LocalDate now = LocalDate.now();
                    subscription.setCurrentPeriodStart(now);
                    subscription.setCurrentPeriodEnd(calculatePeriodEnd(now, subscription.getMembershipPlan().getBillingInterval()));

                    return membershipSubscriptionRepository.save(subscription);
                }
                return subscription;
            })
            .map(membershipSubscriptionMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
    }

    /**
     * Handle payment failure - transition to PAST_DUE.
     */
    public MembershipSubscriptionDTO handlePaymentFailed(String stripeSubscriptionId) {
        log.info("Handling payment failure for Stripe subscription {}", stripeSubscriptionId);

        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                subscription.setSubscriptionStatus("PAST_DUE");
                subscription.setUpdatedAt(ZonedDateTime.now());
                return membershipSubscriptionRepository.save(subscription);
            })
            .map(membershipSubscriptionMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found for Stripe ID: " + stripeSubscriptionId));
    }

    /**
     * Handle payment success - transition from PAST_DUE or TRIAL to ACTIVE.
     */
    public MembershipSubscriptionDTO handlePaymentSucceeded(String stripeSubscriptionId, LocalDate periodStart, LocalDate periodEnd) {
        log.info("Handling payment success for Stripe subscription {}", stripeSubscriptionId);

        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                String currentStatus = subscription.getSubscriptionStatus();
                if ("PAST_DUE".equals(currentStatus) || "TRIAL".equals(currentStatus)) {
                    subscription.setSubscriptionStatus("ACTIVE");
                }

                if (periodStart != null) {
                    subscription.setCurrentPeriodStart(periodStart);
                }
                if (periodEnd != null) {
                    subscription.setCurrentPeriodEnd(periodEnd);
                }

                subscription.setUpdatedAt(ZonedDateTime.now());
                return membershipSubscriptionRepository.save(subscription);
            })
            .map(membershipSubscriptionMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found for Stripe ID: " + stripeSubscriptionId));
    }

    // ==================== CANCELLATION ====================

    /**
     * Cancel subscription at period end.
     *
     * @param subscriptionId the subscription ID
     * @param cancellationReason the reason for cancellation
     * @return the updated subscription
     */
    public MembershipSubscriptionDTO cancelAtPeriodEnd(Long subscriptionId, String cancellationReason) {
        log.info("Cancelling subscription {} at period end", subscriptionId);

        return membershipSubscriptionRepository
            .findById(subscriptionId)
            .map(subscription -> {
                // Cancel in Stripe if applicable
                if (subscription.getStripeSubscriptionId() != null && !subscription.getStripeSubscriptionId().isEmpty()) {
                    try {
                        stripeSubscriptionService.cancelSubscription(
                            subscription.getTenantId(),
                            subscription.getStripeSubscriptionId(),
                            true // cancelAtPeriodEnd
                        );
                    } catch (PaymentException e) {
                        log.error("Failed to cancel Stripe subscription: {}", e.getMessage());
                        // Continue with local cancellation
                    }
                }

                subscription.setCancelAtPeriodEnd(true);
                subscription.setCancellationReason(cancellationReason);
                subscription.setUpdatedAt(ZonedDateTime.now());
                return membershipSubscriptionRepository.save(subscription);
            })
            .map(membershipSubscriptionMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
    }

    /**
     * Handle subscription deleted/cancelled (from Stripe webhook).
     */
    public MembershipSubscriptionDTO handleSubscriptionDeleted(String stripeSubscriptionId) {
        log.info("Handling subscription deletion for Stripe subscription {}", stripeSubscriptionId);

        return membershipSubscriptionRepository
            .findByStripeSubscriptionId(stripeSubscriptionId)
            .map(subscription -> {
                subscription.setSubscriptionStatus("CANCELLED");
                subscription.setCancelledAt(ZonedDateTime.now());
                subscription.setUpdatedAt(ZonedDateTime.now());
                return membershipSubscriptionRepository.save(subscription);
            })
            .map(membershipSubscriptionMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found for Stripe ID: " + stripeSubscriptionId));
    }

    /**
     * Resume a cancelled subscription (if cancel_at_period_end was true).
     */
    public MembershipSubscriptionDTO resumeSubscription(Long subscriptionId) {
        log.info("Resuming subscription {}", subscriptionId);

        return membershipSubscriptionRepository
            .findById(subscriptionId)
            .map(subscription -> {
                if (!subscription.getCancelAtPeriodEnd()) {
                    throw new IllegalStateException("Subscription is not marked for cancellation at period end");
                }

                // Resume in Stripe if applicable
                if (subscription.getStripeSubscriptionId() != null && !subscription.getStripeSubscriptionId().isEmpty()) {
                    try {
                        stripeSubscriptionService.resumeSubscription(subscription.getTenantId(), subscription.getStripeSubscriptionId());
                    } catch (PaymentException e) {
                        log.error("Failed to resume Stripe subscription: {}", e.getMessage());
                        throw new RuntimeException("Failed to resume subscription in Stripe", e);
                    }
                }

                subscription.setCancelAtPeriodEnd(false);
                subscription.setCancellationReason(null);
                subscription.setUpdatedAt(ZonedDateTime.now());
                return membershipSubscriptionRepository.save(subscription);
            })
            .map(membershipSubscriptionMapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
    }

    // ==================== UPGRADE/DOWNGRADE ====================

    /**
     * Change subscription plan (upgrade/downgrade).
     *
     * @param subscriptionId the subscription ID
     * @param newPlanId the new plan ID
     * @return the updated subscription
     */
    public MembershipSubscriptionDTO changePlan(Long subscriptionId, Long newPlanId) {
        log.info("Changing subscription {} to plan {}", subscriptionId, newPlanId);

        MembershipSubscription subscription = membershipSubscriptionRepository
            .findById(subscriptionId)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));

        MembershipPlan newPlan = membershipPlanRepository
            .findById(newPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + newPlanId));

        // Update in Stripe if applicable
        if (
            subscription.getStripeSubscriptionId() != null &&
            !subscription.getStripeSubscriptionId().isEmpty() &&
            newPlan.getStripePriceId() != null &&
            !newPlan.getStripePriceId().isEmpty()
        ) {
            try {
                stripeSubscriptionService.updateSubscriptionPrice(
                    subscription.getTenantId(),
                    subscription.getStripeSubscriptionId(),
                    newPlan.getStripePriceId(),
                    "create_prorations"
                );
            } catch (PaymentException e) {
                log.error("Failed to update Stripe subscription: {}", e.getMessage());
                throw new RuntimeException("Failed to update subscription in Stripe", e);
            }
        }

        subscription.setMembershipPlan(newPlan);
        subscription.setUpdatedAt(ZonedDateTime.now());
        return membershipSubscriptionMapper.toDto(membershipSubscriptionRepository.save(subscription));
    }

    // ==================== HELPER METHODS ====================

    private LocalDate convertToLocalDate(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }
        return Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate calculatePeriodEnd(LocalDate start, String billingInterval) {
        return switch (billingInterval.toUpperCase()) {
            case "MONTHLY" -> start.plusMonths(1);
            case "QUARTERLY" -> start.plusMonths(3);
            case "YEARLY" -> start.plusYears(1);
            case "ONE_TIME" -> start.plusYears(100); // Effectively forever
            default -> start.plusMonths(1);
        };
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
