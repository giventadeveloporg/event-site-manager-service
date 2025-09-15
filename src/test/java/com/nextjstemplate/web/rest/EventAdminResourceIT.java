package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventAdmin;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventAdminRepository;
import com.nextjstemplate.service.dto.EventAdminDTO;
import com.nextjstemplate.service.mapper.EventAdminMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventAdminResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventAdminResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-admins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventAdminRepository eventAdminRepository;

    @Autowired
    private EventAdminMapper eventAdminMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventAdminMockMvc;

    private EventAdmin eventAdmin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventAdmin createEntity(EntityManager em) {
        EventAdmin eventAdmin = new EventAdmin()
            .tenantId(DEFAULT_TENANT_ID)
            .role(DEFAULT_ROLE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventAdmin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventAdmin createUpdatedEntity(EntityManager em) {
        EventAdmin eventAdmin = new EventAdmin()
            .tenantId(UPDATED_TENANT_ID)
            .role(UPDATED_ROLE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventAdmin;
    }

    @BeforeEach
    public void initTest() {
        eventAdmin = createEntity(em);
    }

    @Test
    @Transactional
    void createEventAdmin() throws Exception {
        int databaseSizeBeforeCreate = eventAdminRepository.findAll().size();
        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);
        restEventAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAdminDTO)))
            .andExpect(status().isCreated());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeCreate + 1);
        EventAdmin testEventAdmin = eventAdminList.get(eventAdminList.size() - 1);
        assertThat(testEventAdmin.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAdmin.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testEventAdmin.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventAdmin.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventAdminWithExistingId() throws Exception {
        // Create the EventAdmin with an existing ID
        eventAdmin.setId(1L);
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        int databaseSizeBeforeCreate = eventAdminRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAdminDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminRepository.findAll().size();
        // set the field null
        eventAdmin.setRole(null);

        // Create the EventAdmin, which fails.
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        restEventAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAdminDTO)))
            .andExpect(status().isBadRequest());

        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminRepository.findAll().size();
        // set the field null
        eventAdmin.setCreatedAt(null);

        // Create the EventAdmin, which fails.
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        restEventAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAdminDTO)))
            .andExpect(status().isBadRequest());

        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminRepository.findAll().size();
        // set the field null
        eventAdmin.setUpdatedAt(null);

        // Create the EventAdmin, which fails.
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        restEventAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAdminDTO)))
            .andExpect(status().isBadRequest());

        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventAdmins() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList
        restEventAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAdmin.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventAdmin() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get the eventAdmin
        restEventAdminMockMvc
            .perform(get(ENTITY_API_URL_ID, eventAdmin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventAdmin.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventAdminsByIdFiltering() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        Long id = eventAdmin.getId();

        defaultEventAdminShouldBeFound("id.equals=" + id);
        defaultEventAdminShouldNotBeFound("id.notEquals=" + id);

        defaultEventAdminShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventAdminShouldNotBeFound("id.greaterThan=" + id);

        defaultEventAdminShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventAdminShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventAdminsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventAdminShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventAdminList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAdminShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventAdminShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventAdminList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAdminShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where tenantId is not null
        defaultEventAdminShouldBeFound("tenantId.specified=true");

        // Get all the eventAdminList where tenantId is null
        defaultEventAdminShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where tenantId contains DEFAULT_TENANT_ID
        defaultEventAdminShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventAdminList where tenantId contains UPDATED_TENANT_ID
        defaultEventAdminShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventAdminShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventAdminList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventAdminShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where role equals to DEFAULT_ROLE
        defaultEventAdminShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the eventAdminList where role equals to UPDATED_ROLE
        defaultEventAdminShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllEventAdminsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultEventAdminShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the eventAdminList where role equals to UPDATED_ROLE
        defaultEventAdminShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllEventAdminsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where role is not null
        defaultEventAdminShouldBeFound("role.specified=true");

        // Get all the eventAdminList where role is null
        defaultEventAdminShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminsByRoleContainsSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where role contains DEFAULT_ROLE
        defaultEventAdminShouldBeFound("role.contains=" + DEFAULT_ROLE);

        // Get all the eventAdminList where role contains UPDATED_ROLE
        defaultEventAdminShouldNotBeFound("role.contains=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllEventAdminsByRoleNotContainsSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where role does not contain DEFAULT_ROLE
        defaultEventAdminShouldNotBeFound("role.doesNotContain=" + DEFAULT_ROLE);

        // Get all the eventAdminList where role does not contain UPDATED_ROLE
        defaultEventAdminShouldBeFound("role.doesNotContain=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventAdminShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAdminShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventAdminShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventAdminList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAdminShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt is not null
        defaultEventAdminShouldBeFound("createdAt.specified=true");

        // Get all the eventAdminList where createdAt is null
        defaultEventAdminShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventAdminShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventAdminShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventAdminShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventAdminShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventAdminShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminList where createdAt is less than UPDATED_CREATED_AT
        defaultEventAdminShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventAdminShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventAdminShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventAdminShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventAdminList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventAdminShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventAdminShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventAdminList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventAdminShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt is not null
        defaultEventAdminShouldBeFound("updatedAt.specified=true");

        // Get all the eventAdminList where updatedAt is null
        defaultEventAdminShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventAdminShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventAdminList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventAdminShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventAdminShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventAdminList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventAdminShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventAdminShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventAdminList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventAdminShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        // Get all the eventAdminList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventAdminShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventAdminList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventAdminShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /*  @Test
    @Transactional
    void getAllEventAdminsByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventAdminRepository.saveAndFlush(eventAdmin);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        eventAdmin.setUser(user);
        eventAdminRepository.saveAndFlush(eventAdmin);
        Long userId = user.getId();
        // Get all the eventAdminList where user equals to userId
        defaultEventAdminShouldBeFound("userId.equals=" + userId);

        // Get all the eventAdminList where user equals to (userId + 1)
        defaultEventAdminShouldNotBeFound("userId.equals=" + (userId + 1));
    }*/

    /*@Test
    @Transactional
    void getAllEventAdminsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventAdminRepository.saveAndFlush(eventAdmin);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        eventAdmin.setCreatedBy(createdBy);
        eventAdminRepository.saveAndFlush(eventAdmin);
        Long createdById = createdBy.getId();
        // Get all the eventAdminList where createdBy equals to createdById
        defaultEventAdminShouldBeFound("createdById.equals=" + createdById);

        // Get all the eventAdminList where createdBy equals to (createdById + 1)
        defaultEventAdminShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventAdminShouldBeFound(String filter) throws Exception {
        restEventAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAdmin.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventAdminMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventAdminShouldNotBeFound(String filter) throws Exception {
        restEventAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventAdminMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventAdmin() throws Exception {
        // Get the eventAdmin
        restEventAdminMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventAdmin() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();

        // Update the eventAdmin
        EventAdmin updatedEventAdmin = eventAdminRepository.findById(eventAdmin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventAdmin are not directly saved in db
        em.detach(updatedEventAdmin);
        updatedEventAdmin.tenantId(UPDATED_TENANT_ID).role(UPDATED_ROLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(updatedEventAdmin);

        restEventAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAdminDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
        EventAdmin testEventAdmin = eventAdminList.get(eventAdminList.size() - 1);
        assertThat(testEventAdmin.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAdmin.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testEventAdmin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAdmin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventAdmin() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();
        eventAdmin.setId(longCount.incrementAndGet());

        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAdminDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventAdmin() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();
        eventAdmin.setId(longCount.incrementAndGet());

        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventAdmin() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();
        eventAdmin.setId(longCount.incrementAndGet());

        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAdminDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventAdminWithPatch() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();

        // Update the eventAdmin using partial update
        EventAdmin partialUpdatedEventAdmin = new EventAdmin();
        partialUpdatedEventAdmin.setId(eventAdmin.getId());

        partialUpdatedEventAdmin.role(UPDATED_ROLE);

        restEventAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAdmin))
            )
            .andExpect(status().isOk());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
        EventAdmin testEventAdmin = eventAdminList.get(eventAdminList.size() - 1);
        assertThat(testEventAdmin.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAdmin.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testEventAdmin.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventAdmin.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventAdminWithPatch() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();

        // Update the eventAdmin using partial update
        EventAdmin partialUpdatedEventAdmin = new EventAdmin();
        partialUpdatedEventAdmin.setId(eventAdmin.getId());

        partialUpdatedEventAdmin.tenantId(UPDATED_TENANT_ID).role(UPDATED_ROLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restEventAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAdmin))
            )
            .andExpect(status().isOk());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
        EventAdmin testEventAdmin = eventAdminList.get(eventAdminList.size() - 1);
        assertThat(testEventAdmin.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAdmin.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testEventAdmin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAdmin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventAdmin() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();
        eventAdmin.setId(longCount.incrementAndGet());

        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventAdminDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventAdmin() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();
        eventAdmin.setId(longCount.incrementAndGet());

        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventAdmin() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminRepository.findAll().size();
        eventAdmin.setId(longCount.incrementAndGet());

        // Create the EventAdmin
        EventAdminDTO eventAdminDTO = eventAdminMapper.toDto(eventAdmin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventAdminDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAdmin in the database
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventAdmin() throws Exception {
        // Initialize the database
        eventAdminRepository.saveAndFlush(eventAdmin);

        int databaseSizeBeforeDelete = eventAdminRepository.findAll().size();

        // Delete the eventAdmin
        restEventAdminMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventAdmin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventAdmin> eventAdminList = eventAdminRepository.findAll();
        assertThat(eventAdminList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
