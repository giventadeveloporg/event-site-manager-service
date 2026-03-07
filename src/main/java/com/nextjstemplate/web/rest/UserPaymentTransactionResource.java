package com.nextjstemplate.web.rest;

import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.repository.UserPaymentTransactionRepository;
import com.nextjstemplate.service.UserPaymentTransactionQueryService;
import com.nextjstemplate.service.UserPaymentTransactionService;
import com.nextjstemplate.service.criteria.UserPaymentTransactionCriteria;
import com.nextjstemplate.service.dto.UserPaymentTransactionDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.UserPaymentTransaction}.
 */
@RestController
@RequestMapping("/api/user-payment-transactions")
public class UserPaymentTransactionResource {

    private final Logger log = LoggerFactory.getLogger(UserPaymentTransactionResource.class);

    private static final String ENTITY_NAME = "userPaymentTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPaymentTransactionService userPaymentTransactionService;

    private final UserPaymentTransactionRepository userPaymentTransactionRepository;

    private final UserPaymentTransactionQueryService userPaymentTransactionQueryService;

    public UserPaymentTransactionResource(
        UserPaymentTransactionService userPaymentTransactionService,
        UserPaymentTransactionRepository userPaymentTransactionRepository,
        UserPaymentTransactionQueryService userPaymentTransactionQueryService
    ) {
        this.userPaymentTransactionService = userPaymentTransactionService;
        this.userPaymentTransactionRepository = userPaymentTransactionRepository;
        this.userPaymentTransactionQueryService = userPaymentTransactionQueryService;
    }

    /**
     * {@code POST  /user-payment-transactions} : Create a new userPaymentTransaction.
     *
     * @param userPaymentTransactionDTO the userPaymentTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPaymentTransactionDTO, or with status {@code 400 (Bad Request)} if the userPaymentTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserPaymentTransactionDTO> createUserPaymentTransaction(
        @Valid @RequestBody UserPaymentTransactionDTO userPaymentTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UserPaymentTransaction : {}", userPaymentTransactionDTO);
        if (userPaymentTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userPaymentTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserPaymentTransactionDTO result = userPaymentTransactionService.save(userPaymentTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/user-payment-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-payment-transactions/:id} : Updates an existing userPaymentTransaction.
     *
     * @param id the id of the userPaymentTransactionDTO to save.
     * @param userPaymentTransactionDTO the userPaymentTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPaymentTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the userPaymentTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPaymentTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserPaymentTransactionDTO> updateUserPaymentTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPaymentTransactionDTO userPaymentTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserPaymentTransaction : {}, {}", id, userPaymentTransactionDTO);
        if (userPaymentTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPaymentTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPaymentTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserPaymentTransactionDTO result = userPaymentTransactionService.update(userPaymentTransactionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPaymentTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-payment-transactions/:id} : Partial updates given fields of an existing userPaymentTransaction, field will ignore if it is null
     *
     * @param id the id of the userPaymentTransactionDTO to save.
     * @param userPaymentTransactionDTO the userPaymentTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPaymentTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the userPaymentTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userPaymentTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPaymentTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPaymentTransactionDTO> partialUpdateUserPaymentTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPaymentTransactionDTO userPaymentTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserPaymentTransaction partially : {}, {}", id, userPaymentTransactionDTO);
        if (userPaymentTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPaymentTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPaymentTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPaymentTransactionDTO> result = userPaymentTransactionService.partialUpdate(userPaymentTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPaymentTransactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-payment-transactions} : get all the userPaymentTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPaymentTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserPaymentTransactionDTO>> getAllUserPaymentTransactions(
        UserPaymentTransactionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserPaymentTransactions by criteria: {}", criteria);

        Page<UserPaymentTransactionDTO> page = userPaymentTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-payment-transactions/count} : count all the userPaymentTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserPaymentTransactions(UserPaymentTransactionCriteria criteria) {
        log.debug("REST request to count UserPaymentTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(userPaymentTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-payment-transactions/:id} : get the "id" userPaymentTransaction.
     *
     * @param id the id of the userPaymentTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPaymentTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPaymentTransactionDTO> getUserPaymentTransaction(@PathVariable Long id) {
        log.debug("REST request to get UserPaymentTransaction : {}", id);
        Optional<UserPaymentTransactionDTO> userPaymentTransactionDTO = userPaymentTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPaymentTransactionDTO);
    }

    /**
     * {@code DELETE  /user-payment-transactions/:id} : delete the "id" userPaymentTransaction.
     *
     * @param id the id of the userPaymentTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserPaymentTransaction(@PathVariable Long id) {
        log.debug("REST request to delete UserPaymentTransaction : {}", id);
        userPaymentTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
