package com.eventsitemanager.web.rest;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileAchievementRepository;
import com.eventsitemanager.service.ProfileAchievementQueryService;
import com.eventsitemanager.service.ProfileAchievementService;
import com.eventsitemanager.service.criteria.ProfileAchievementCriteria;
import com.eventsitemanager.service.dto.ProfileAchievementDTO;
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
@RequestMapping("/api/profile-achievements")
public class ProfileAchievementResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAchievementResource.class);
    private static final String ENTITY_NAME = "profileAchievement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileAchievementService profileAchievementService;
    private final ProfileAchievementRepository profileAchievementRepository;
    private final ProfileAchievementQueryService profileAchievementQueryService;

    public ProfileAchievementResource(
        ProfileAchievementService profileAchievementService,
        ProfileAchievementRepository profileAchievementRepository,
        ProfileAchievementQueryService profileAchievementQueryService
    ) {
        this.profileAchievementService = profileAchievementService;
        this.profileAchievementRepository = profileAchievementRepository;
        this.profileAchievementQueryService = profileAchievementQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileAchievementDTO> createProfileAchievement(@Valid @RequestBody ProfileAchievementDTO profileAchievementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileAchievement : {}", profileAchievementDTO);
        if (profileAchievementDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileAchievement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileAchievementDTO.getTenantId() == null || profileAchievementDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required when creating profileAchievement", ENTITY_NAME, "tenantidrequired");
        }
        ProfileAchievementDTO result = profileAchievementService.save(profileAchievementDTO);
        return ResponseEntity
            .created(new URI("/api/profile-achievements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileAchievementDTO> updateProfileAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileAchievementDTO profileAchievementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileAchievement : {}, {}", id, profileAchievementDTO);
        if (profileAchievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileAchievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileAchievementDTO result = profileAchievementService.update(profileAchievementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileAchievementDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileAchievementDTO> partialUpdateProfileAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileAchievementDTO profileAchievementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileAchievement partially : {}, {}", id, profileAchievementDTO);
        if (profileAchievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileAchievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileAchievementDTO> result = profileAchievementService.partialUpdate(profileAchievementDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileAchievementDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileAchievementDTO>> getAllProfileAchievements(
        ProfileAchievementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileAchievements by criteria: {}", criteria);
        Page<ProfileAchievementDTO> page = profileAchievementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileAchievements(ProfileAchievementCriteria criteria) {
        LOG.debug("REST request to count ProfileAchievements by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileAchievementQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileAchievementDTO> getProfileAchievement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileAchievement : {}", id);
        Optional<ProfileAchievementDTO> profileAchievementDTO = profileAchievementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileAchievementDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileAchievement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileAchievement : {}", id);
        profileAchievementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
