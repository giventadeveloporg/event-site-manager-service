package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.EmailLogRepository;
import com.nextjstemplate.service.EmailLogQueryService;
import com.nextjstemplate.service.EmailLogService;
import com.nextjstemplate.service.criteria.EmailLogCriteria;
import com.nextjstemplate.service.dto.EmailLogDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link com.nextjstemplate.domain.EmailLog}.
 */
@RestController
@RequestMapping("/api/email-logs")
public class EmailLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailLogResource.class);

    private static final String ENTITY_NAME = "emailLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailLogService emailLogService;

    private final EmailLogRepository emailLogRepository;

    private final EmailLogQueryService emailLogQueryService;

    public EmailLogResource(
        EmailLogService emailLogService,
        EmailLogRepository emailLogRepository,
        EmailLogQueryService emailLogQueryService
    ) {
        this.emailLogService = emailLogService;
        this.emailLogRepository = emailLogRepository;
        this.emailLogQueryService = emailLogQueryService;
    }

    /**
     * {@code POST  /email-logs} : Create a new emailLog.
     *
     * @param emailLogDTO the emailLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailLogDTO, or with status {@code 400 (Bad Request)} if the emailLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailLogDTO> createEmailLog(@Valid @RequestBody EmailLogDTO emailLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save EmailLog : {}", emailLogDTO);
        if (emailLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailLogDTO = emailLogService.save(emailLogDTO);
        return ResponseEntity.created(new URI("/api/email-logs/" + emailLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailLogDTO.getId().toString()))
            .body(emailLogDTO);
    }

    /**
     * {@code PUT  /email-logs/:id} : Updates an existing emailLog.
     *
     * @param id the id of the emailLogDTO to save.
     * @param emailLogDTO the emailLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailLogDTO,
     * or with status {@code 400 (Bad Request)} if the emailLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailLogDTO> updateEmailLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailLogDTO emailLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailLog : {}, {}", id, emailLogDTO);
        if (emailLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailLogDTO = emailLogService.update(emailLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailLogDTO.getId().toString()))
            .body(emailLogDTO);
    }

    /**
     * {@code PATCH  /email-logs/:id} : Partial updates given fields of an existing emailLog, field will ignore if it is null
     *
     * @param id the id of the emailLogDTO to save.
     * @param emailLogDTO the emailLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailLogDTO,
     * or with status {@code 400 (Bad Request)} if the emailLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailLogDTO> partialUpdateEmailLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailLogDTO emailLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailLog partially : {}, {}", id, emailLogDTO);
        if (emailLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailLogDTO> result = emailLogService.partialUpdate(emailLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-logs} : get all the emailLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailLogDTO>> getAllEmailLogs(
        EmailLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmailLogs by criteria: {}", criteria);

        Page<EmailLogDTO> page = emailLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-logs/count} : count all the emailLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmailLogs(EmailLogCriteria criteria) {
        LOG.debug("REST request to count EmailLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-logs/:id} : get the "id" emailLog.
     *
     * @param id the id of the emailLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailLogDTO> getEmailLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailLog : {}", id);
        Optional<EmailLogDTO> emailLogDTO = emailLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailLogDTO);
    }

    /**
     * {@code DELETE  /email-logs/:id} : delete the "id" emailLog.
     *
     * @param id the id of the emailLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailLog : {}", id);
        emailLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
