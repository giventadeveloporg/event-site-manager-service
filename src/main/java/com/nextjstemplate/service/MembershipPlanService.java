package com.nextjstemplate.service;

import com.nextjstemplate.domain.MembershipPlan;
import com.nextjstemplate.repository.MembershipPlanRepository;
import com.nextjstemplate.service.dto.MembershipPlanDTO;
import com.nextjstemplate.service.mapper.MembershipPlanMapper;
import com.nextjstemplate.service.payment.PaymentException;
import com.nextjstemplate.service.payment.StripeSubscriptionService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing membership plans.
 * Handles CRUD operations and Stripe product/price synchronization.
 */
@Service
@Transactional
public class MembershipPlanService {

    private static final Logger log = LoggerFactory.getLogger(MembershipPlanService.class);

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanMapper membershipPlanMapper;
    private final StripeSubscriptionService stripeSubscriptionService;

    public MembershipPlanService(
        MembershipPlanRepository membershipPlanRepository,
        MembershipPlanMapper membershipPlanMapper,
        StripeSubscriptionService stripeSubscriptionService
    ) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.membershipPlanMapper = membershipPlanMapper;
        this.stripeSubscriptionService = stripeSubscriptionService;
    }

    /**
     * Save a membership plan.
     * Creates Stripe Product and Price if not already set.
     *
     * @param membershipPlanDTO the entity to save
     * @return the persisted entity
     */
    public MembershipPlanDTO save(MembershipPlanDTO membershipPlanDTO) {
        log.debug("Request to save MembershipPlan : {}", membershipPlanDTO);

        MembershipPlan membershipPlan = membershipPlanMapper.toEntity(membershipPlanDTO);

        // Set timestamps
        ZonedDateTime now = ZonedDateTime.now();
        boolean isNewPlan = membershipPlan.getId() == null;
        if (isNewPlan) {
            membershipPlan.setCreatedAt(now);
        }
        membershipPlan.setUpdatedAt(now);

        // Validate plan code uniqueness for new plans
        if (isNewPlan) {
            if (membershipPlanRepository.existsByTenantIdAndPlanCode(membershipPlan.getTenantId(), membershipPlan.getPlanCode())) {
                throw new IllegalArgumentException(
                    "Plan code '" + membershipPlan.getPlanCode() + "' already exists for tenant " + membershipPlan.getTenantId()
                );
            }
        }

        // Check if we need to create Stripe Product and Price
        boolean needsStripeSync =
            membershipPlan.getStripeProductId() == null ||
            membershipPlan.getStripeProductId().isEmpty() ||
            membershipPlan.getStripePriceId() == null ||
            membershipPlan.getStripePriceId().isEmpty();

        // Save entity first to ensure we have an ID for Stripe metadata
        membershipPlan = membershipPlanRepository.save(membershipPlan);

        // Create Stripe Product and Price if not already set
        if (needsStripeSync) {
            try {
                Map<String, String> stripeIds = stripeSubscriptionService.createProductAndPrice(
                    membershipPlan.getTenantId(),
                    membershipPlan
                );
                membershipPlan.setStripeProductId(stripeIds.get("stripeProductId"));
                membershipPlan.setStripePriceId(stripeIds.get("stripePriceId"));
                log.info(
                    "Created Stripe product {} and price {} for plan {}",
                    membershipPlan.getStripeProductId(),
                    membershipPlan.getStripePriceId(),
                    membershipPlan.getPlanCode()
                );
                // Save again with Stripe IDs
                membershipPlan = membershipPlanRepository.save(membershipPlan);
            } catch (PaymentException e) {
                log.error("Failed to create Stripe product/price for plan {}: {}", membershipPlan.getPlanCode(), e.getMessage());
                // Continue without Stripe IDs - they can be created later
            }
        }

        return membershipPlanMapper.toDto(membershipPlan);
    }

    /**
     * Update a membership plan.
     *
     * @param membershipPlanDTO the entity to save
     * @return the persisted entity
     */
    public MembershipPlanDTO update(MembershipPlanDTO membershipPlanDTO) {
        log.debug("Request to update MembershipPlan : {}", membershipPlanDTO);

        MembershipPlan membershipPlan = membershipPlanMapper.toEntity(membershipPlanDTO);
        membershipPlan.setUpdatedAt(ZonedDateTime.now());

        // Check if plan has active subscriptions
        if (membershipPlanRepository.hasActiveSubscriptions(membershipPlan.getId())) {
            log.warn(
                "Plan {} has active subscriptions - some changes may not take effect for existing subscribers",
                membershipPlan.getId()
            );
        }

        membershipPlan = membershipPlanRepository.save(membershipPlan);
        return membershipPlanMapper.toDto(membershipPlan);
    }

    /**
     * Partially update a membership plan.
     *
     * @param membershipPlanDTO the entity to update partially
     * @return the persisted entity
     */
    public Optional<MembershipPlanDTO> partialUpdate(MembershipPlanDTO membershipPlanDTO) {
        log.debug("Request to partially update MembershipPlan : {}", membershipPlanDTO);

        return membershipPlanRepository
            .findById(membershipPlanDTO.getId())
            .map(existingPlan -> {
                membershipPlanMapper.partialUpdate(existingPlan, membershipPlanDTO);
                existingPlan.setUpdatedAt(ZonedDateTime.now());
                return existingPlan;
            })
            .map(membershipPlanRepository::save)
            .map(membershipPlanMapper::toDto);
    }

    /**
     * Get all membership plans.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MembershipPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MembershipPlans");
        return membershipPlanRepository.findAll(pageable).map(membershipPlanMapper::toDto);
    }

    /**
     * Get all membership plans for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MembershipPlanDTO> findByTenantId(String tenantId) {
        log.debug("Request to get all MembershipPlans for tenant: {}", tenantId);
        return membershipPlanRepository.findByTenantId(tenantId).stream().map(membershipPlanMapper::toDto).toList();
    }

    /**
     * Get all active membership plans for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MembershipPlanDTO> findActiveByTenantId(String tenantId) {
        log.debug("Request to get active MembershipPlans for tenant: {}", tenantId);
        return membershipPlanRepository.findByTenantIdAndIsActiveTrue(tenantId).stream().map(membershipPlanMapper::toDto).toList();
    }

    /**
     * Get one membership plan by ID.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MembershipPlanDTO> findOne(Long id) {
        log.debug("Request to get MembershipPlan : {}", id);
        return membershipPlanRepository.findById(id).map(membershipPlanMapper::toDto);
    }

    /**
     * Get one membership plan by tenant ID and plan code.
     *
     * @param tenantId the tenant ID
     * @param planCode the plan code
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MembershipPlanDTO> findByTenantIdAndPlanCode(String tenantId, String planCode) {
        log.debug("Request to get MembershipPlan by tenant {} and code {}", tenantId, planCode);
        return membershipPlanRepository.findByTenantIdAndPlanCode(tenantId, planCode).map(membershipPlanMapper::toDto);
    }

    /**
     * Delete a membership plan.
     * Soft delete by setting isActive=false if plan has any subscriptions.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MembershipPlan : {}", id);

        membershipPlanRepository
            .findById(id)
            .ifPresent(plan -> {
                if (membershipPlanRepository.hasActiveSubscriptions(id)) {
                    // Soft delete - deactivate instead of deleting
                    plan.setIsActive(false);
                    plan.setUpdatedAt(ZonedDateTime.now());
                    membershipPlanRepository.save(plan);
                    log.info("Soft deleted (deactivated) MembershipPlan {} because it has active subscriptions", id);
                } else {
                    // Hard delete - no active subscriptions
                    membershipPlanRepository.deleteById(id);
                    log.info("Hard deleted MembershipPlan {}", id);
                }
            });
    }

    /**
     * Check if plan code exists for tenant.
     */
    @Transactional(readOnly = true)
    public boolean existsByTenantIdAndPlanCode(String tenantId, String planCode) {
        return membershipPlanRepository.existsByTenantIdAndPlanCode(tenantId, planCode);
    }
}
