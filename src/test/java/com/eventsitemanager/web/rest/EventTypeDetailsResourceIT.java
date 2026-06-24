package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventTypeDetails;
import com.eventsitemanager.repository.EventTypeDetailsRepository;
import com.eventsitemanager.service.dto.EventTypeDetailsDTO;
import com.eventsitemanager.service.mapper.EventTypeDetailsMapper;
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
 * Integration tests for the {@link EventTypeDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventTypeDetailsResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-type-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventTypeDetailsRepository eventTypeDetailsRepository;

    @Autowired
    private EventTypeDetailsMapper eventTypeDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTypeDetailsMockMvc;

    private EventTypeDetails eventTypeDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTypeDetails createEntity(EntityManager em) {
        EventTypeDetails eventTypeDetails = new EventTypeDetails()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventTypeDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTypeDetails createUpdatedEntity(EntityManager em) {
        EventTypeDetails eventTypeDetails = new EventTypeDetails()
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventTypeDetails;
    }

    @BeforeEach
    public void initTest() {
        eventTypeDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createEventTypeDetails() throws Exception {
        int databaseSizeBeforeCreate = eventTypeDetailsRepository.findAll().size();
        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);
        restEventTypeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        EventTypeDetails testEventTypeDetails = eventTypeDetailsList.get(eventTypeDetailsList.size() - 1);
        assertThat(testEventTypeDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventTypeDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventTypeDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventTypeDetails.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventTypeDetails.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventTypeDetailsWithExistingId() throws Exception {
        // Create the EventTypeDetails with an existing ID
        eventTypeDetails.setId(1L);
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        int databaseSizeBeforeCreate = eventTypeDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTypeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeDetailsRepository.findAll().size();
        // set the field null
        eventTypeDetails.setName(null);

        // Create the EventTypeDetails, which fails.
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        restEventTypeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeDetailsRepository.findAll().size();
        // set the field null
        eventTypeDetails.setCreatedAt(null);

        // Create the EventTypeDetails, which fails.
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        restEventTypeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTypeDetailsRepository.findAll().size();
        // set the field null
        eventTypeDetails.setUpdatedAt(null);

        // Create the EventTypeDetails, which fails.
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        restEventTypeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventTypeDetails() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList
        restEventTypeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTypeDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventTypeDetails() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get the eventTypeDetails
        restEventTypeDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, eventTypeDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventTypeDetails.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventTypeDetailsByIdFiltering() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        Long id = eventTypeDetails.getId();

        defaultEventTypeDetailsShouldBeFound("id.equals=" + id);
        defaultEventTypeDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultEventTypeDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventTypeDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultEventTypeDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventTypeDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventTypeDetailsShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventTypeDetailsList where tenantId equals to UPDATED_TENANT_ID
        defaultEventTypeDetailsShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventTypeDetailsShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventTypeDetailsList where tenantId equals to UPDATED_TENANT_ID
        defaultEventTypeDetailsShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where tenantId is not null
        defaultEventTypeDetailsShouldBeFound("tenantId.specified=true");

        // Get all the eventTypeDetailsList where tenantId is null
        defaultEventTypeDetailsShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where tenantId contains DEFAULT_TENANT_ID
        defaultEventTypeDetailsShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventTypeDetailsList where tenantId contains UPDATED_TENANT_ID
        defaultEventTypeDetailsShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventTypeDetailsShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventTypeDetailsList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventTypeDetailsShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where name equals to DEFAULT_NAME
        defaultEventTypeDetailsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventTypeDetailsList where name equals to UPDATED_NAME
        defaultEventTypeDetailsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventTypeDetailsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventTypeDetailsList where name equals to UPDATED_NAME
        defaultEventTypeDetailsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where name is not null
        defaultEventTypeDetailsShouldBeFound("name.specified=true");

        // Get all the eventTypeDetailsList where name is null
        defaultEventTypeDetailsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByNameContainsSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where name contains DEFAULT_NAME
        defaultEventTypeDetailsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventTypeDetailsList where name contains UPDATED_NAME
        defaultEventTypeDetailsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where name does not contain DEFAULT_NAME
        defaultEventTypeDetailsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventTypeDetailsList where name does not contain UPDATED_NAME
        defaultEventTypeDetailsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultEventTypeDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeDetailsList where description equals to UPDATED_DESCRIPTION
        defaultEventTypeDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventTypeDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventTypeDetailsList where description equals to UPDATED_DESCRIPTION
        defaultEventTypeDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where description is not null
        defaultEventTypeDetailsShouldBeFound("description.specified=true");

        // Get all the eventTypeDetailsList where description is null
        defaultEventTypeDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where description contains DEFAULT_DESCRIPTION
        defaultEventTypeDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeDetailsList where description contains UPDATED_DESCRIPTION
        defaultEventTypeDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultEventTypeDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventTypeDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultEventTypeDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventTypeDetailsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventTypeDetailsList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTypeDetailsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventTypeDetailsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventTypeDetailsList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTypeDetailsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt is not null
        defaultEventTypeDetailsShouldBeFound("createdAt.specified=true");

        // Get all the eventTypeDetailsList where createdAt is null
        defaultEventTypeDetailsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventTypeDetailsShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventTypeDetailsList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventTypeDetailsShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventTypeDetailsShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventTypeDetailsList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventTypeDetailsShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventTypeDetailsShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventTypeDetailsList where createdAt is less than UPDATED_CREATED_AT
        defaultEventTypeDetailsShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventTypeDetailsShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventTypeDetailsList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventTypeDetailsShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventTypeDetailsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventTypeDetailsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventTypeDetailsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventTypeDetailsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt is not null
        defaultEventTypeDetailsShouldBeFound("updatedAt.specified=true");

        // Get all the eventTypeDetailsList where updatedAt is null
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventTypeDetailsShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventTypeDetailsList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventTypeDetailsShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventTypeDetailsList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventTypeDetailsList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventTypeDetailsShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTypeDetailsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        // Get all the eventTypeDetailsList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventTypeDetailsShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventTypeDetailsList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventTypeDetailsShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTypeDetailsShouldBeFound(String filter) throws Exception {
        restEventTypeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTypeDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventTypeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTypeDetailsShouldNotBeFound(String filter) throws Exception {
        restEventTypeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTypeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventTypeDetails() throws Exception {
        // Get the eventTypeDetails
        restEventTypeDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventTypeDetails() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();

        // Update the eventTypeDetails
        EventTypeDetails updatedEventTypeDetails = eventTypeDetailsRepository.findById(eventTypeDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventTypeDetails are not directly saved in db
        em.detach(updatedEventTypeDetails);
        updatedEventTypeDetails
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(updatedEventTypeDetails);

        restEventTypeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTypeDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
        EventTypeDetails testEventTypeDetails = eventTypeDetailsList.get(eventTypeDetailsList.size() - 1);
        assertThat(testEventTypeDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTypeDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventTypeDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventTypeDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTypeDetails.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventTypeDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();
        eventTypeDetails.setId(longCount.incrementAndGet());

        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTypeDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventTypeDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();
        eventTypeDetails.setId(longCount.incrementAndGet());

        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventTypeDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();
        eventTypeDetails.setId(longCount.incrementAndGet());

        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventTypeDetailsWithPatch() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();

        // Update the eventTypeDetails using partial update
        EventTypeDetails partialUpdatedEventTypeDetails = new EventTypeDetails();
        partialUpdatedEventTypeDetails.setId(eventTypeDetails.getId());

        partialUpdatedEventTypeDetails
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restEventTypeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTypeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventTypeDetails))
            )
            .andExpect(status().isOk());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
        EventTypeDetails testEventTypeDetails = eventTypeDetailsList.get(eventTypeDetailsList.size() - 1);
        assertThat(testEventTypeDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTypeDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventTypeDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventTypeDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTypeDetails.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventTypeDetailsWithPatch() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();

        // Update the eventTypeDetails using partial update
        EventTypeDetails partialUpdatedEventTypeDetails = new EventTypeDetails();
        partialUpdatedEventTypeDetails.setId(eventTypeDetails.getId());

        partialUpdatedEventTypeDetails
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventTypeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTypeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventTypeDetails))
            )
            .andExpect(status().isOk());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
        EventTypeDetails testEventTypeDetails = eventTypeDetailsList.get(eventTypeDetailsList.size() - 1);
        assertThat(testEventTypeDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTypeDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventTypeDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventTypeDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTypeDetails.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventTypeDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();
        eventTypeDetails.setId(longCount.incrementAndGet());

        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventTypeDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventTypeDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();
        eventTypeDetails.setId(longCount.incrementAndGet());

        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventTypeDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeDetailsRepository.findAll().size();
        eventTypeDetails.setId(longCount.incrementAndGet());

        // Create the EventTypeDetails
        EventTypeDetailsDTO eventTypeDetailsDTO = eventTypeDetailsMapper.toDto(eventTypeDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTypeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTypeDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTypeDetails in the database
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventTypeDetails() throws Exception {
        // Initialize the database
        eventTypeDetailsRepository.saveAndFlush(eventTypeDetails);

        int databaseSizeBeforeDelete = eventTypeDetailsRepository.findAll().size();

        // Delete the eventTypeDetails
        restEventTypeDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventTypeDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventTypeDetails> eventTypeDetailsList = eventTypeDetailsRepository.findAll();
        assertThat(eventTypeDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
