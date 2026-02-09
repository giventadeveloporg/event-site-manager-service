package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.MembershipSubscriptionRepository;
import com.nextjstemplate.service.MembershipSubscriptionQueryService;
import com.nextjstemplate.service.MembershipSubscriptionService;
import com.nextjstemplate.service.criteria.MembershipSubscriptionCriteria;
import com.nextjstemplate.service.dto.MembershipSubscriptionDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.MembershipSubscription}.
 */
@RestController
@RequestMapping("/api/membership-subscriptions")
public class MembershipSubscriptionResource {

    private static final Logger log = LoggerFactory.getLogger(MembershipSubscriptionResource.class);

    private static final String ENTITY_NAME = "membershipSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipSubscriptionService membershipSubscriptionService;
    private final MembershipSubscriptionRepository membershipSubscriptionRepository;
    private final MembershipSubscriptionQueryService membershipSubscriptionQueryService;

    public MembershipSubscriptionResource(
        MembershipSubscriptionService membershipSubscriptionService,
        MembershipSubscriptionRepository membershipSubscriptionRepository,
        MembershipSubscriptionQueryService membershipSubscriptionQueryService
    ) {
        this.membershipSubscriptionService = membershipSubscriptionService;
        this.membershipSubscriptionRepository = membershipSubscriptionRepository;
        this.membershipSubscriptionQueryService = membershipSubscriptionQueryService;
    }

    /**
     * {@code POST  /membership-subscriptions} : Create a new membershipSubscription.
     *
     * @param membershipSubscriptionDTO the membershipSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipSubscriptionDTO, or with status {@code 400 (Bad Request)} if the membershipSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MembershipSubscriptionDTO> createMembershipSubscription(
        @Valid @RequestBody MembershipSubscriptionDTO membershipSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MembershipSubscription : {}", membershipSubscriptionDTO);
        if (membershipSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Check for existing active subscription for this user
        Optional<MembershipSubscriptionDTO> existingActive = membershipSubscriptionService.findActiveByUserProfileIdAndTenantId(
            membershipSubscriptionDTO.getUserProfileId(),
            membershipSubscriptionDTO.getTenantId()
        );
        if (existingActive.isPresent()) {
            throw new BadRequestAlertException("User already has an active subscription", ENTITY_NAME, "activesubscriptionexists");
        }

        MembershipSubscriptionDTO result = membershipSubscriptionService.save(membershipSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/membership-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membership-subscriptions/:id} : Partial updates given fields of an existing membershipSubscription, field will ignore if it is null
     *
     * @param id the id of the membershipSubscriptionDTO to save.
     * @param membershipSubscriptionDTO the membershipSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the membershipSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MembershipSubscriptionDTO> partialUpdateMembershipSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MembershipSubscriptionDTO membershipSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MembershipSubscription partially : {}, {}", id, membershipSubscriptionDTO);
        if (membershipSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MembershipSubscriptionDTO> result = membershipSubscriptionService.partialUpdate(membershipSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-subscriptions} : get all the membershipSubscriptions.
     *
     * @param pageable the pagination information.
     * @param tenantId optional tenant ID filter.
     * @param subscriptionStatus optional status filter.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MembershipSubscriptionDTO>> getAllMembershipSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "tenantId.equals", required = false) String tenantId,
        @RequestParam(value = "subscriptionStatus.equals", required = false) String subscriptionStatus
    ) {
        log.debug("REST request to get a page of MembershipSubscriptions, tenantId={}, status={}", tenantId, subscriptionStatus);

        MembershipSubscriptionCriteria criteria = new MembershipSubscriptionCriteria();
        if (tenantId != null && !tenantId.isEmpty()) {
            criteria.setTenantId(new StringFilter());
            criteria.getTenantId().setEquals(tenantId);
        }
        if (subscriptionStatus != null && !subscriptionStatus.isEmpty()) {
            criteria.setSubscriptionStatus(new StringFilter());
            criteria.getSubscriptionStatus().setEquals(subscriptionStatus);
        }
        Page<MembershipSubscriptionDTO> page = membershipSubscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /membership-subscriptions/:id} : get the "id" membershipSubscription.
     *
     * @param id the id of the membershipSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MembershipSubscriptionDTO> getMembershipSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to get MembershipSubscription : {}", id);
        Optional<MembershipSubscriptionDTO> membershipSubscriptionDTO = membershipSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipSubscriptionDTO);
    }

    /**
     * {@code GET  /membership-subscriptions/by-user/:userId} : get the user's active subscription.
     *
     * @param userId the user profile ID.
     * @param tenantId the tenant ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<MembershipSubscriptionDTO> getActiveSubscriptionByUser(
        @PathVariable("userId") Long userId,
        @RequestParam("tenantId") String tenantId
    ) {
        log.debug("REST request to get active MembershipSubscription for user {} in tenant {}", userId, tenantId);
        Optional<MembershipSubscriptionDTO> membershipSubscriptionDTO = membershipSubscriptionService.findActiveByUserProfileIdAndTenantId(
            userId,
            tenantId
        );
        return ResponseUtil.wrapOrNotFound(membershipSubscriptionDTO);
    }

    /**
     * {@code GET  /membership-subscriptions/by-stripe/:stripeSubscriptionId} : get subscription by Stripe ID.
     *
     * @param stripeSubscriptionId the Stripe subscription ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/by-stripe/{stripeSubscriptionId}")
    public ResponseEntity<MembershipSubscriptionDTO> getSubscriptionByStripeId(
        @PathVariable("stripeSubscriptionId") String stripeSubscriptionId
    ) {
        log.debug("REST request to get MembershipSubscription by Stripe ID {}", stripeSubscriptionId);
        Optional<MembershipSubscriptionDTO> membershipSubscriptionDTO = membershipSubscriptionService.findByStripeSubscriptionId(
            stripeSubscriptionId
        );
        return ResponseUtil.wrapOrNotFound(membershipSubscriptionDTO);
    }

    /**
     * {@code POST  /membership-subscriptions/:id/cancel} : Cancel subscription at period end.
     *
     * @param id the subscription ID.
     * @param body the request body containing cancellation reason.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipSubscriptionDTO.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<MembershipSubscriptionDTO> cancelSubscription(
        @PathVariable("id") Long id,
        @RequestBody(required = false) Map<String, String> body
    ) {
        log.debug("REST request to cancel MembershipSubscription : {}", id);
        String cancellationReason = body != null ? body.get("cancellationReason") : null;
        MembershipSubscriptionDTO result = membershipSubscriptionService.cancelAtPeriodEnd(id, cancellationReason);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code POST  /membership-subscriptions/:id/resume} : Resume a cancelled subscription.
     *
     * @param id the subscription ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipSubscriptionDTO.
     */
    @PostMapping("/{id}/resume")
    public ResponseEntity<MembershipSubscriptionDTO> resumeSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to resume MembershipSubscription : {}", id);
        MembershipSubscriptionDTO result = membershipSubscriptionService.resumeSubscription(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code POST  /membership-subscriptions/:id/change-plan} : Change subscription plan.
     *
     * @param id the subscription ID.
     * @param body the request body containing new plan ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipSubscriptionDTO.
     */
    @PostMapping("/{id}/change-plan")
    public ResponseEntity<MembershipSubscriptionDTO> changePlan(@PathVariable("id") Long id, @RequestBody Map<String, Long> body) {
        log.debug("REST request to change plan for MembershipSubscription : {}", id);
        Long newPlanId = body.get("newPlanId");
        if (newPlanId == null) {
            throw new BadRequestAlertException("newPlanId is required", ENTITY_NAME, "newplanrequired");
        }
        MembershipSubscriptionDTO result = membershipSubscriptionService.changePlan(id, newPlanId);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code DELETE  /membership-subscriptions/:id} : delete the "id" membershipSubscription.
     *
     * @param id the id of the membershipSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to delete MembershipSubscription : {}", id);
        membershipSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
