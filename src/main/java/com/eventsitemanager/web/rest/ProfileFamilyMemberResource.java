package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileFamilyMemberRepository;
import com.eventsitemanager.service.ProfileFamilyMemberQueryService;
import com.eventsitemanager.service.ProfileFamilyMemberService;
import com.eventsitemanager.service.criteria.ProfileFamilyMemberCriteria;
import com.eventsitemanager.service.dto.ProfileFamilyMemberDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/profile-family-members")
public class ProfileFamilyMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileFamilyMemberResource.class);
    private static final String ENTITY_NAME = "profileFamilyMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileFamilyMemberService profileFamilyMemberService;
    private final ProfileFamilyMemberRepository profileFamilyMemberRepository;
    private final ProfileFamilyMemberQueryService profileFamilyMemberQueryService;

    public ProfileFamilyMemberResource(
        ProfileFamilyMemberService profileFamilyMemberService,
        ProfileFamilyMemberRepository profileFamilyMemberRepository,
        ProfileFamilyMemberQueryService profileFamilyMemberQueryService
    ) {
        this.profileFamilyMemberService = profileFamilyMemberService;
        this.profileFamilyMemberRepository = profileFamilyMemberRepository;
        this.profileFamilyMemberQueryService = profileFamilyMemberQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileFamilyMemberDTO> createProfileFamilyMember(
        @Valid @RequestBody ProfileFamilyMemberDTO profileFamilyMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfileFamilyMember : {}", profileFamilyMemberDTO);
        if (profileFamilyMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileFamilyMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileFamilyMemberDTO.getTenantId() == null || profileFamilyMemberDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileFamilyMember", ENTITY_NAME, "tenantidrequired");
        }
        ProfileFamilyMemberDTO result = profileFamilyMemberService.save(profileFamilyMemberDTO);
        return ResponseEntity
            .created(new URI("/api/profile-family-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileFamilyMemberDTO> updateProfileFamilyMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileFamilyMemberDTO profileFamilyMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileFamilyMember : {}, {}", id, profileFamilyMemberDTO);
        if (profileFamilyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileFamilyMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileFamilyMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileFamilyMemberDTO result = profileFamilyMemberService.update(profileFamilyMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileFamilyMemberDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileFamilyMemberDTO> partialUpdateProfileFamilyMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileFamilyMemberDTO profileFamilyMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileFamilyMember partially : {}, {}", id, profileFamilyMemberDTO);
        if (profileFamilyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileFamilyMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileFamilyMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileFamilyMemberDTO> result = profileFamilyMemberService.partialUpdate(profileFamilyMemberDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileFamilyMemberDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileFamilyMemberDTO>> getAllProfileFamilyMembers(
        ProfileFamilyMemberCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileFamilyMembers by criteria: {}", criteria);
        Page<ProfileFamilyMemberDTO> page = profileFamilyMemberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileFamilyMembers(ProfileFamilyMemberCriteria criteria) {
        LOG.debug("REST request to count ProfileFamilyMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileFamilyMemberQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileFamilyMemberDTO> getProfileFamilyMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileFamilyMember : {}", id);
        Optional<ProfileFamilyMemberDTO> profileFamilyMemberDTO = profileFamilyMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileFamilyMemberDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileFamilyMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileFamilyMember : {}", id);
        profileFamilyMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
