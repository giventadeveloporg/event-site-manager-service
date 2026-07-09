package com.eventsitemanager.web.rest;

import com.eventsitemanager.domain.enumeration.ProfileAudienceContactSource;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.ProfileAudienceContactRepository;
import com.eventsitemanager.security.TenantContext;
import com.eventsitemanager.service.ProfileAudienceContactQueryService;
import com.eventsitemanager.service.ProfileAudienceContactService;
import com.eventsitemanager.service.criteria.ProfileAudienceContactCriteria;
import com.eventsitemanager.service.dto.ProfileAudienceBulkImportResultDTO;
import com.eventsitemanager.service.dto.ProfileAudienceContactDTO;
import com.eventsitemanager.service.dto.ProfileAudienceSubscribeRequestDTO;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/profile-audience-contacts")
public class ProfileAudienceContactResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileAudienceContactResource.class);
    private static final String ENTITY_NAME = "profileAudienceContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileAudienceContactService profileAudienceContactService;
    private final ProfileAudienceContactRepository profileAudienceContactRepository;
    private final ProfileAudienceContactQueryService profileAudienceContactQueryService;

    public ProfileAudienceContactResource(
        ProfileAudienceContactService profileAudienceContactService,
        ProfileAudienceContactRepository profileAudienceContactRepository,
        ProfileAudienceContactQueryService profileAudienceContactQueryService
    ) {
        this.profileAudienceContactService = profileAudienceContactService;
        this.profileAudienceContactRepository = profileAudienceContactRepository;
        this.profileAudienceContactQueryService = profileAudienceContactQueryService;
    }

    @PostMapping("")
    public ResponseEntity<ProfileAudienceContactDTO> createProfileAudienceContact(
        @Valid @RequestBody ProfileAudienceContactDTO profileAudienceContactDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ProfileAudienceContact : {}", profileAudienceContactDTO);
        if (profileAudienceContactDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileAudienceContact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (profileAudienceContactDTO.getTenantId() == null || profileAudienceContactDTO.getTenantId().isBlank()) {
            throw new BadRequestAlertException("tenantId is required", ENTITY_NAME, "tenantidrequired");
        }
        ProfileAudienceContactDTO result = profileAudienceContactService.save(profileAudienceContactDTO);
        return ResponseEntity
            .created(new URI("/api/profile-audience-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileAudienceContactDTO> updateProfileAudienceContact(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileAudienceContactDTO profileAudienceContactDTO
    ) {
        LOG.debug("REST request to update ProfileAudienceContact : {}, {}", id, profileAudienceContactDTO);
        if (profileAudienceContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileAudienceContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileAudienceContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProfileAudienceContactDTO result = profileAudienceContactService.update(profileAudienceContactDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileAudienceContactDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileAudienceContactDTO> partialUpdateProfileAudienceContact(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileAudienceContactDTO profileAudienceContactDTO
    ) {
        LOG.debug("REST request to partial update ProfileAudienceContact : {}, {}", id, profileAudienceContactDTO);
        if (profileAudienceContactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileAudienceContactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!profileAudienceContactRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<ProfileAudienceContactDTO> result = profileAudienceContactService.partialUpdate(profileAudienceContactDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profileAudienceContactDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProfileAudienceContactDTO>> getAllProfileAudienceContacts(
        ProfileAudienceContactCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProfileAudienceContacts by criteria: {}", criteria);
        Page<ProfileAudienceContactDTO> page = profileAudienceContactQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProfileAudienceContacts(ProfileAudienceContactCriteria criteria) {
        LOG.debug("REST request to count ProfileAudienceContacts by criteria: {}", criteria);
        return ResponseEntity.ok().body(profileAudienceContactQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileAudienceContactDTO> getProfileAudienceContact(@PathVariable Long id) {
        LOG.debug("REST request to get ProfileAudienceContact : {}", id);
        Optional<ProfileAudienceContactDTO> profileAudienceContactDTO = profileAudienceContactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileAudienceContactDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileAudienceContact(@PathVariable Long id) {
        LOG.debug("REST request to delete ProfileAudienceContact : {}", id);
        profileAudienceContactService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/bulk-import")
    public ResponseEntity<ProfileAudienceBulkImportResultDTO> bulkImport(@Valid @RequestBody List<ProfileAudienceContactDTO> contacts) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant context is required", ENTITY_NAME, "tenantrequired");
        }
        ProfileAudienceBulkImportResultDTO result = profileAudienceContactService.bulkImport(tenantId, contacts);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/public/subscribe")
    public ResponseEntity<ProfileAudienceContactDTO> publicSubscribe(@Valid @RequestBody ProfileAudienceSubscribeRequestDTO request) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant context is required", ENTITY_NAME, "tenantrequired");
        }
        ProfileAudienceContactSource source = request.getMessage() != null && !request.getMessage().isBlank()
            ? ProfileAudienceContactSource.CONTACT_FORM
            : ProfileAudienceContactSource.SUBSCRIBE_FORM;
        ProfileAudienceContactDTO result = profileAudienceContactService.publicSubscribe(tenantId, request, source);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/unsubscribe")
    public ResponseEntity<Map<String, Object>> unsubscribe(@RequestParam String email, @RequestParam String token) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant context is required", ENTITY_NAME, "tenantrequired");
        }
        return ResponseEntity.ok(profileAudienceContactService.unsubscribe(tenantId, email, token));
    }
}
