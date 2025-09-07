package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.UserProfileQueryService;
import com.nextjstemplate.service.UserProfileService;
import com.nextjstemplate.service.criteria.UserProfileCriteria;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.UserProfile}.
 */
@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    private static final String ENTITY_NAME = "userProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserProfileService userProfileService;

    private final UserProfileRepository userProfileRepository;

    private final UserProfileQueryService userProfileQueryService;

    private final JwtDecoder jwtDecoder;

    private final JwtEncoder jwtEncoder;

    public UserProfileResource(
        UserProfileService userProfileService,
        UserProfileRepository userProfileRepository,
        UserProfileQueryService userProfileQueryService,
        JwtDecoder jwtDecoder,
        JwtEncoder jwtEncoder
    ) {
        this.userProfileService = userProfileService;
        this.userProfileRepository = userProfileRepository;
        this.userProfileQueryService = userProfileQueryService;
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * {@code POST  /user-profiles} : Create a new userProfile.
     *
     * @param userProfileDTO the userProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new userProfileDTO, or with status {@code 400 (Bad Request)}
     *         if the userProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserProfileDTO> createUserProfile(@Valid @RequestBody UserProfileDTO userProfileDTO) throws URISyntaxException {
        log.debug("REST request to save UserProfile : {}", userProfileDTO);
        if (userProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new userProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JwtClaimsSet claims = JwtClaimsSet.builder().subject(userProfileDTO.getEmail()).build();
        JwsHeader jwsHeader = JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS512).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        userProfileDTO.setEmailSubscriptionToken(token);
        userProfileDTO.setIsEmailSubscribed(true);
        UserProfileDTO result = userProfileService.save(userProfileDTO);
        return ResponseEntity
            .created(new URI("/api/user-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /user-profiles} : Create a new userProfile.
     * Used by the excel bulk upload template to insert multiple users at the same time in the user profile table
     * @param users the userProfileDTOs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new userProfileDTO, or with status {@code 400 (Bad Request)}
     *         if the userProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<UserProfileDTO>> createBulk(@RequestBody List<UserProfileDTO> users) {
        List<UserProfileDTO> created = userProfileService.saveBulkUploadUsers(users);
        return ResponseEntity.ok(created);
    }

    /**
     * {@code PUT  /user-profiles/:id} : Updates an existing userProfile.
     *
     * @param id             the id of the userProfileDTO to save.
     * @param userProfileDTO the userProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated userProfileDTO,
     *         or with status {@code 400 (Bad Request)} if the userProfileDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         userProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserProfileDTO userProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserProfile : {}, {}", id, userProfileDTO);
        if (userProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserProfileDTO result = userProfileService.update(userProfileDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-profiles/:id} : Partial updates given fields of an
     * existing userProfile, field will ignore if it is null
     *
     * @param id             the id of the userProfileDTO to save.
     * @param userProfileDTO the userProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated userProfileDTO,
     *         or with status {@code 400 (Bad Request)} if the userProfileDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the userProfileDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         userProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserProfileDTO> partialUpdateUserProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserProfileDTO userProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserProfile partially : {}, {}", id, userProfileDTO);
        if (userProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserProfileDTO> result = userProfileService.partialUpdate(userProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-profiles} : get all the userProfiles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of userProfiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserProfileDTO>> getAllUserProfiles(
        UserProfileCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserProfiles by criteria: {}", criteria);

        Page<UserProfileDTO> page = userProfileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-profiles/by-user/:userId} : get the userProfile by user ID.
     *
     * @param userId the user ID to search for
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the userProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfileByUserId(@PathVariable String userId) {
        log.debug("REST request to get UserProfile by user ID : {}", userId);
        Optional<UserProfileDTO> userProfileDTO = userProfileService.findByUserId(userId);
        return ResponseUtil.wrapOrNotFound(userProfileDTO);
    }

    /**
     * {@code GET  /user-profiles/:id} : get the "id" userProfile.
     *
     * @param id the id of the userProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the userProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        Optional<UserProfileDTO> userProfileDTO = userProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userProfileDTO);
    }

    /**
     * {@code DELETE  /user-profiles/:id} : delete the "id" userProfile.
     *
     * @param id the id of the userProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /user-profiles/count} : count all the userProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserProfiles(UserProfileCriteria criteria) {
        log.debug("REST request to count UserProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(userProfileQueryService.countByCriteria(criteria));
    }

    /**
     * Unsubscribe endpoint: /api/unsubscribe-email?email=...&token=...
     */
    @GetMapping("/unsubscribe-email")
    public ResponseEntity<Map<String, Object>> unsubscribeEmail(@RequestParam String email, @RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserProfileDTO> userOpt = userProfileService.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Email not found.");
            return ResponseEntity.badRequest().body(response);
        }
        UserProfileDTO user = userOpt.orElseThrow();
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String subject = jwt.getSubject();
            if (!email.equalsIgnoreCase(subject)) {
                response.put("success", false);
                response.put("message", "Invalid token for this email.");
                return ResponseEntity.badRequest().body(response);
            }
            if (Boolean.TRUE.equals(user.getIsEmailSubscriptionTokenUsed())) {
                response.put("success", true);
                response.put("message", "You have already unsubscribed.");
                return ResponseEntity.ok(response);
            }
            user.setIsEmailSubscribed(false);
            user.setIsEmailSubscriptionTokenUsed(true);
            userProfileService.save(user);
            response.put("success", true);
            response.put("message", "You have been unsubscribed from emails.");
            return ResponseEntity.ok(response);
        } catch (JwtException e) {
            response.put("success", false);
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Resubscribe endpoint: /api/resubscribe-email?email=...&token=...
     */
    @GetMapping("/resubscribe-email")
    public ResponseEntity<Map<String, Object>> resubscribeEmail(@RequestParam String email, @RequestParam String tenantId) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserProfileDTO> userProfileDTO = userProfileService.findByEmailAndTenantId(email, tenantId);
        if (userProfileDTO.isEmpty()) {
            response.put("success", false);
            response.put("message", "Email not found.");
            return ResponseEntity.badRequest().body(response);
        }
        UserProfileDTO user = userProfileDTO.orElseThrow();
        try {
            JwtClaimsSet claims = JwtClaimsSet.builder().subject(user.getEmail()).build();
            JwsHeader jwsHeader = JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS512).build();
            String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
            user.setEmailSubscriptionToken(token);
            user.setIsEmailSubscribed(true);
            user.setIsEmailSubscriptionTokenUsed(false);
            userProfileService.save(user);
            response.put("success", true);
            response.put("message", "You have been re-subscribed to emails.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
