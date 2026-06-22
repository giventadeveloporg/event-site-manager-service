package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventOrganizer;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.EventOrganizerRepository;
import com.eventsitemanager.service.dto.EventOrganizerDTO;
import com.eventsitemanager.service.mapper.EventOrganizerMapper;
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
 * Integration tests for the {@link EventOrganizerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventOrganizerResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-organizers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventOrganizerRepository eventOrganizerRepository;

    @Autowired
    private EventOrganizerMapper eventOrganizerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventOrganizerMockMvc;

    private EventOrganizer eventOrganizer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventOrganizer createEntity(EntityManager em) {
        EventOrganizer eventOrganizer = new EventOrganizer()
            .tenantId(DEFAULT_TENANT_ID)
            .title(DEFAULT_TITLE)
            .designation(DEFAULT_DESIGNATION)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .contactPhone(DEFAULT_CONTACT_PHONE)
            .isPrimary(DEFAULT_IS_PRIMARY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventOrganizer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventOrganizer createUpdatedEntity(EntityManager em) {
        EventOrganizer eventOrganizer = new EventOrganizer()
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventOrganizer;
    }

    @BeforeEach
    public void initTest() {
        eventOrganizer = createEntity(em);
    }

    @Test
    @Transactional
    void createEventOrganizer() throws Exception {
        int databaseSizeBeforeCreate = eventOrganizerRepository.findAll().size();
        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);
        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeCreate + 1);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(DEFAULT_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(DEFAULT_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventOrganizerWithExistingId() throws Exception {
        // Create the EventOrganizer with an existing ID
        eventOrganizer.setId(1L);
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        int databaseSizeBeforeCreate = eventOrganizerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOrganizerRepository.findAll().size();
        // set the field null
        eventOrganizer.setTitle(null);

        // Create the EventOrganizer, which fails.
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOrganizerRepository.findAll().size();
        // set the field null
        eventOrganizer.setCreatedAt(null);

        // Create the EventOrganizer, which fails.
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventOrganizerRepository.findAll().size();
        // set the field null
        eventOrganizer.setUpdatedAt(null);

        // Create the EventOrganizer, which fails.
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventOrganizers() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventOrganizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventOrganizer() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get the eventOrganizer
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL_ID, eventOrganizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventOrganizer.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventOrganizersByIdFiltering() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        Long id = eventOrganizer.getId();

        defaultEventOrganizerShouldBeFound("id.equals=" + id);
        defaultEventOrganizerShouldNotBeFound("id.notEquals=" + id);

        defaultEventOrganizerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventOrganizerShouldNotBeFound("id.greaterThan=" + id);

        defaultEventOrganizerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventOrganizerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventOrganizerShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventOrganizerList where tenantId equals to UPDATED_TENANT_ID
        defaultEventOrganizerShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventOrganizerShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventOrganizerList where tenantId equals to UPDATED_TENANT_ID
        defaultEventOrganizerShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where tenantId is not null
        defaultEventOrganizerShouldBeFound("tenantId.specified=true");

        // Get all the eventOrganizerList where tenantId is null
        defaultEventOrganizerShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where tenantId contains DEFAULT_TENANT_ID
        defaultEventOrganizerShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventOrganizerList where tenantId contains UPDATED_TENANT_ID
        defaultEventOrganizerShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventOrganizerShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventOrganizerList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventOrganizerShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where title equals to DEFAULT_TITLE
        defaultEventOrganizerShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventOrganizerList where title equals to UPDATED_TITLE
        defaultEventOrganizerShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventOrganizerShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventOrganizerList where title equals to UPDATED_TITLE
        defaultEventOrganizerShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where title is not null
        defaultEventOrganizerShouldBeFound("title.specified=true");

        // Get all the eventOrganizerList where title is null
        defaultEventOrganizerShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTitleContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where title contains DEFAULT_TITLE
        defaultEventOrganizerShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventOrganizerList where title contains UPDATED_TITLE
        defaultEventOrganizerShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where title does not contain DEFAULT_TITLE
        defaultEventOrganizerShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventOrganizerList where title does not contain UPDATED_TITLE
        defaultEventOrganizerShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where designation equals to DEFAULT_DESIGNATION
        defaultEventOrganizerShouldBeFound("designation.equals=" + DEFAULT_DESIGNATION);

        // Get all the eventOrganizerList where designation equals to UPDATED_DESIGNATION
        defaultEventOrganizerShouldNotBeFound("designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where designation in DEFAULT_DESIGNATION or UPDATED_DESIGNATION
        defaultEventOrganizerShouldBeFound("designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION);

        // Get all the eventOrganizerList where designation equals to UPDATED_DESIGNATION
        defaultEventOrganizerShouldNotBeFound("designation.in=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where designation is not null
        defaultEventOrganizerShouldBeFound("designation.specified=true");

        // Get all the eventOrganizerList where designation is null
        defaultEventOrganizerShouldNotBeFound("designation.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByDesignationContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where designation contains DEFAULT_DESIGNATION
        defaultEventOrganizerShouldBeFound("designation.contains=" + DEFAULT_DESIGNATION);

        // Get all the eventOrganizerList where designation contains UPDATED_DESIGNATION
        defaultEventOrganizerShouldNotBeFound("designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where designation does not contain DEFAULT_DESIGNATION
        defaultEventOrganizerShouldNotBeFound("designation.doesNotContain=" + DEFAULT_DESIGNATION);

        // Get all the eventOrganizerList where designation does not contain UPDATED_DESIGNATION
        defaultEventOrganizerShouldBeFound("designation.doesNotContain=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactEmail equals to DEFAULT_CONTACT_EMAIL
        defaultEventOrganizerShouldBeFound("contactEmail.equals=" + DEFAULT_CONTACT_EMAIL);

        // Get all the eventOrganizerList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultEventOrganizerShouldNotBeFound("contactEmail.equals=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactEmail in DEFAULT_CONTACT_EMAIL or UPDATED_CONTACT_EMAIL
        defaultEventOrganizerShouldBeFound("contactEmail.in=" + DEFAULT_CONTACT_EMAIL + "," + UPDATED_CONTACT_EMAIL);

        // Get all the eventOrganizerList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultEventOrganizerShouldNotBeFound("contactEmail.in=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactEmail is not null
        defaultEventOrganizerShouldBeFound("contactEmail.specified=true");

        // Get all the eventOrganizerList where contactEmail is null
        defaultEventOrganizerShouldNotBeFound("contactEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactEmailContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactEmail contains DEFAULT_CONTACT_EMAIL
        defaultEventOrganizerShouldBeFound("contactEmail.contains=" + DEFAULT_CONTACT_EMAIL);

        // Get all the eventOrganizerList where contactEmail contains UPDATED_CONTACT_EMAIL
        defaultEventOrganizerShouldNotBeFound("contactEmail.contains=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactEmailNotContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactEmail does not contain DEFAULT_CONTACT_EMAIL
        defaultEventOrganizerShouldNotBeFound("contactEmail.doesNotContain=" + DEFAULT_CONTACT_EMAIL);

        // Get all the eventOrganizerList where contactEmail does not contain UPDATED_CONTACT_EMAIL
        defaultEventOrganizerShouldBeFound("contactEmail.doesNotContain=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactPhone equals to DEFAULT_CONTACT_PHONE
        defaultEventOrganizerShouldBeFound("contactPhone.equals=" + DEFAULT_CONTACT_PHONE);

        // Get all the eventOrganizerList where contactPhone equals to UPDATED_CONTACT_PHONE
        defaultEventOrganizerShouldNotBeFound("contactPhone.equals=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactPhone in DEFAULT_CONTACT_PHONE or UPDATED_CONTACT_PHONE
        defaultEventOrganizerShouldBeFound("contactPhone.in=" + DEFAULT_CONTACT_PHONE + "," + UPDATED_CONTACT_PHONE);

        // Get all the eventOrganizerList where contactPhone equals to UPDATED_CONTACT_PHONE
        defaultEventOrganizerShouldNotBeFound("contactPhone.in=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactPhone is not null
        defaultEventOrganizerShouldBeFound("contactPhone.specified=true");

        // Get all the eventOrganizerList where contactPhone is null
        defaultEventOrganizerShouldNotBeFound("contactPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactPhoneContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactPhone contains DEFAULT_CONTACT_PHONE
        defaultEventOrganizerShouldBeFound("contactPhone.contains=" + DEFAULT_CONTACT_PHONE);

        // Get all the eventOrganizerList where contactPhone contains UPDATED_CONTACT_PHONE
        defaultEventOrganizerShouldNotBeFound("contactPhone.contains=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByContactPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where contactPhone does not contain DEFAULT_CONTACT_PHONE
        defaultEventOrganizerShouldNotBeFound("contactPhone.doesNotContain=" + DEFAULT_CONTACT_PHONE);

        // Get all the eventOrganizerList where contactPhone does not contain UPDATED_CONTACT_PHONE
        defaultEventOrganizerShouldBeFound("contactPhone.doesNotContain=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByIsPrimaryIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where isPrimary equals to DEFAULT_IS_PRIMARY
        defaultEventOrganizerShouldBeFound("isPrimary.equals=" + DEFAULT_IS_PRIMARY);

        // Get all the eventOrganizerList where isPrimary equals to UPDATED_IS_PRIMARY
        defaultEventOrganizerShouldNotBeFound("isPrimary.equals=" + UPDATED_IS_PRIMARY);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByIsPrimaryIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where isPrimary in DEFAULT_IS_PRIMARY or UPDATED_IS_PRIMARY
        defaultEventOrganizerShouldBeFound("isPrimary.in=" + DEFAULT_IS_PRIMARY + "," + UPDATED_IS_PRIMARY);

        // Get all the eventOrganizerList where isPrimary equals to UPDATED_IS_PRIMARY
        defaultEventOrganizerShouldNotBeFound("isPrimary.in=" + UPDATED_IS_PRIMARY);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByIsPrimaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where isPrimary is not null
        defaultEventOrganizerShouldBeFound("isPrimary.specified=true");

        // Get all the eventOrganizerList where isPrimary is null
        defaultEventOrganizerShouldNotBeFound("isPrimary.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventOrganizerShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventOrganizerList where createdAt equals to UPDATED_CREATED_AT
        defaultEventOrganizerShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventOrganizerShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventOrganizerList where createdAt equals to UPDATED_CREATED_AT
        defaultEventOrganizerShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt is not null
        defaultEventOrganizerShouldBeFound("createdAt.specified=true");

        // Get all the eventOrganizerList where createdAt is null
        defaultEventOrganizerShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventOrganizerShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventOrganizerList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventOrganizerShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventOrganizerShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventOrganizerList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventOrganizerShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventOrganizerShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventOrganizerList where createdAt is less than UPDATED_CREATED_AT
        defaultEventOrganizerShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventOrganizerShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventOrganizerList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventOrganizerShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventOrganizerShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventOrganizerList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventOrganizerShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventOrganizerShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventOrganizerList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventOrganizerShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt is not null
        defaultEventOrganizerShouldBeFound("updatedAt.specified=true");

        // Get all the eventOrganizerList where updatedAt is null
        defaultEventOrganizerShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventOrganizerShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventOrganizerList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventOrganizerShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventOrganizerShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventOrganizerList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventOrganizerShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventOrganizerShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventOrganizerList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventOrganizerShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        // Get all the eventOrganizerList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventOrganizerShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventOrganizerList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventOrganizerShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventOrganizersByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventOrganizerRepository.saveAndFlush(eventOrganizer);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventOrganizer.setEvent(event);
        eventOrganizerRepository.saveAndFlush(eventOrganizer);
        Long eventId = event.getId();
        // Get all the eventOrganizerList where event equals to eventId
        defaultEventOrganizerShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventOrganizerList where event equals to (eventId + 1)
        defaultEventOrganizerShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /*@Test
    @Transactional
    void getAllEventOrganizersByOrganizerIsEqualToSomething() throws Exception {
        UserProfile organizer;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventOrganizerRepository.saveAndFlush(eventOrganizer);
            organizer = UserProfileResourceIT.createEntity(em);
        } else {
            organizer = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(organizer);
        em.flush();
        eventOrganizer.setOrganizer(organizer);
        eventOrganizerRepository.saveAndFlush(eventOrganizer);
        Long organizerId = organizer.getId();
        // Get all the eventOrganizerList where organizer equals to organizerId
        defaultEventOrganizerShouldBeFound("organizerId.equals=" + organizerId);

        // Get all the eventOrganizerList where organizer equals to (organizerId + 1)
        defaultEventOrganizerShouldNotBeFound("organizerId.equals=" + (organizerId + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventOrganizerShouldBeFound(String filter) throws Exception {
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventOrganizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventOrganizerShouldNotBeFound(String filter) throws Exception {
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventOrganizerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventOrganizer() throws Exception {
        // Get the eventOrganizer
        restEventOrganizerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventOrganizer() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();

        // Update the eventOrganizer
        EventOrganizer updatedEventOrganizer = eventOrganizerRepository.findById(eventOrganizer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventOrganizer are not directly saved in db
        em.detach(updatedEventOrganizer);
        updatedEventOrganizer
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(updatedEventOrganizer);

        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventOrganizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventOrganizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventOrganizerWithPatch() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();

        // Update the eventOrganizer using partial update
        EventOrganizer partialUpdatedEventOrganizer = new EventOrganizer();
        partialUpdatedEventOrganizer.setId(eventOrganizer.getId());

        partialUpdatedEventOrganizer
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventOrganizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventOrganizer))
            )
            .andExpect(status().isOk());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventOrganizerWithPatch() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();

        // Update the eventOrganizer using partial update
        EventOrganizer partialUpdatedEventOrganizer = new EventOrganizer();
        partialUpdatedEventOrganizer.setId(eventOrganizer.getId());

        partialUpdatedEventOrganizer
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .designation(UPDATED_DESIGNATION)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventOrganizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventOrganizer))
            )
            .andExpect(status().isOk());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
        EventOrganizer testEventOrganizer = eventOrganizerList.get(eventOrganizerList.size() - 1);
        assertThat(testEventOrganizer.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventOrganizer.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testEventOrganizer.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testEventOrganizer.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testEventOrganizer.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testEventOrganizer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventOrganizer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventOrganizerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventOrganizer() throws Exception {
        int databaseSizeBeforeUpdate = eventOrganizerRepository.findAll().size();
        eventOrganizer.setId(longCount.incrementAndGet());

        // Create the EventOrganizer
        EventOrganizerDTO eventOrganizerDTO = eventOrganizerMapper.toDto(eventOrganizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventOrganizerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventOrganizerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventOrganizer in the database
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventOrganizer() throws Exception {
        // Initialize the database
        eventOrganizerRepository.saveAndFlush(eventOrganizer);

        int databaseSizeBeforeDelete = eventOrganizerRepository.findAll().size();

        // Delete the eventOrganizer
        restEventOrganizerMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventOrganizer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventOrganizer> eventOrganizerList = eventOrganizerRepository.findAll();
        assertThat(eventOrganizerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
