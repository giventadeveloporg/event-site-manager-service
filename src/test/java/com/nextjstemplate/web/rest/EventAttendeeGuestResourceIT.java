package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.EventAttendeeGuest;
import com.nextjstemplate.domain.enumeration.GuestAgeGroup;
import com.nextjstemplate.domain.enumeration.UserEventCheckInStatus;
import com.nextjstemplate.domain.enumeration.UserEventRegistrationStatus;
import com.nextjstemplate.domain.enumeration.UserToGuestRelationship;
import com.nextjstemplate.repository.EventAttendeeGuestRepository;
import com.nextjstemplate.service.dto.EventAttendeeGuestDTO;
import com.nextjstemplate.service.mapper.EventAttendeeGuestMapper;
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
 * Integration tests for the {@link EventAttendeeGuestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventAttendeeGuestResourceIT {
    /* private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GUEST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUEST_NAME = "BBBBBBBBBB";

    private static final GuestAgeGroup DEFAULT_AGE_GROUP = GuestAgeGroup.ADULT;
    private static final GuestAgeGroup UPDATED_AGE_GROUP = GuestAgeGroup.TEEN;

    private static final UserToGuestRelationship DEFAULT_RELATIONSHIP = UserToGuestRelationship.SPOUSE;
    private static final UserToGuestRelationship UPDATED_RELATIONSHIP = UserToGuestRelationship.CHILD;

    private static final String DEFAULT_SPECIAL_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUIREMENTS = "BBBBBBBBBB";

    private static final UserEventRegistrationStatus DEFAULT_REGISTRATION_STATUS = UserEventRegistrationStatus.PENDING;
    private static final UserEventRegistrationStatus UPDATED_REGISTRATION_STATUS = UserEventRegistrationStatus.CONFIRMED;

    private static final UserEventCheckInStatus DEFAULT_CHECK_IN_STATUS = UserEventCheckInStatus.NOT_CHECKED_IN;
    private static final UserEventCheckInStatus UPDATED_CHECK_IN_STATUS = UserEventCheckInStatus.CHECKED_IN;

    private static final ZonedDateTime DEFAULT_CHECK_IN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHECK_IN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CHECK_IN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-attendee-guests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventAttendeeGuestRepository eventAttendeeGuestRepository;

    @Autowired
    private EventAttendeeGuestMapper eventAttendeeGuestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventAttendeeGuestMockMvc;

    private EventAttendeeGuest eventAttendeeGuest;

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventAttendeeGuest createEntity(EntityManager em) {
        EventAttendeeGuest eventAttendeeGuest = new EventAttendeeGuest()
            .tenantId(DEFAULT_TENANT_ID)
            .guestName(DEFAULT_GUEST_NAME)
            .ageGroup(DEFAULT_AGE_GROUP)
            .relationship(DEFAULT_RELATIONSHIP)
            .specialRequirements(DEFAULT_SPECIAL_REQUIREMENTS)
            .registrationStatus(DEFAULT_REGISTRATION_STATUS)
            .checkInStatus(DEFAULT_CHECK_IN_STATUS)
            .checkInTime(DEFAULT_CHECK_IN_TIME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        EventAttendee eventAttendee;
        if (TestUtil.findAll(em, EventAttendee.class).isEmpty()) {
            eventAttendee = EventAttendeeResourceIT.createEntity(em);
            em.persist(eventAttendee);
            em.flush();
        } else {
            eventAttendee = TestUtil.findAll(em, EventAttendee.class).get(0);
        }
        eventAttendeeGuest.setPrimaryAttendee(eventAttendee);
        return eventAttendeeGuest;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventAttendeeGuest createUpdatedEntity(EntityManager em) {
        EventAttendeeGuest eventAttendeeGuest = new EventAttendeeGuest()
            .tenantId(UPDATED_TENANT_ID)
            .guestName(UPDATED_GUEST_NAME)
            .ageGroup(UPDATED_AGE_GROUP)
            .relationship(UPDATED_RELATIONSHIP)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        EventAttendee eventAttendee;
        if (TestUtil.findAll(em, EventAttendee.class).isEmpty()) {
            eventAttendee = EventAttendeeResourceIT.createUpdatedEntity(em);
            em.persist(eventAttendee);
            em.flush();
        } else {
            eventAttendee = TestUtil.findAll(em, EventAttendee.class).get(0);
        }
        eventAttendeeGuest.setPrimaryAttendee(eventAttendee);
        return eventAttendeeGuest;
    }

    @BeforeEach
    public void initTest() {
        eventAttendeeGuest = createEntity(em);
    }

    @Test
    @Transactional
    void createEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeCreate = eventAttendeeGuestRepository.findAll().size();
        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);
        restEventAttendeeGuestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeCreate + 1);
        EventAttendeeGuest testEventAttendeeGuest = eventAttendeeGuestList.get(eventAttendeeGuestList.size() - 1);
        assertThat(testEventAttendeeGuest.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAttendeeGuest.getGuestName()).isEqualTo(DEFAULT_GUEST_NAME);
        assertThat(testEventAttendeeGuest.getAgeGroup()).isEqualTo(DEFAULT_AGE_GROUP);
        assertThat(testEventAttendeeGuest.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
        assertThat(testEventAttendeeGuest.getSpecialRequirements()).isEqualTo(DEFAULT_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendeeGuest.getRegistrationStatus()).isEqualTo(DEFAULT_REGISTRATION_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInStatus()).isEqualTo(DEFAULT_CHECK_IN_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInTime()).isEqualTo(DEFAULT_CHECK_IN_TIME);
        assertThat(testEventAttendeeGuest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventAttendeeGuest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventAttendeeGuestWithExistingId() throws Exception {
        // Create the EventAttendeeGuest with an existing ID
        eventAttendeeGuest.setId(1L);
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        int databaseSizeBeforeCreate = eventAttendeeGuestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventAttendeeGuestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGuestNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeGuestRepository.findAll().size();
        // set the field null
        eventAttendeeGuest.setGuestName(null);

        // Create the EventAttendeeGuest, which fails.
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        restEventAttendeeGuestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgeGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeGuestRepository.findAll().size();
        // set the field null
        eventAttendeeGuest.setAgeGroup(null);

        // Create the EventAttendeeGuest, which fails.
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        restEventAttendeeGuestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeGuestRepository.findAll().size();
        // set the field null
        eventAttendeeGuest.setCreatedAt(null);

        // Create the EventAttendeeGuest, which fails.
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        restEventAttendeeGuestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeGuestRepository.findAll().size();
        // set the field null
        eventAttendeeGuest.setUpdatedAt(null);

        // Create the EventAttendeeGuest, which fails.
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        restEventAttendeeGuestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuests() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList
        restEventAttendeeGuestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAttendeeGuest.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].guestName").value(hasItem(DEFAULT_GUEST_NAME)))
            .andExpect(jsonPath("$.[*].ageGroup").value(hasItem(DEFAULT_AGE_GROUP.toString())))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP.toString())))
            .andExpect(jsonPath("$.[*].specialRequirements").value(hasItem(DEFAULT_SPECIAL_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].registrationStatus").value(hasItem(DEFAULT_REGISTRATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].checkInStatus").value(hasItem(DEFAULT_CHECK_IN_STATUS.toString())))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(sameInstant(DEFAULT_CHECK_IN_TIME))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventAttendeeGuest() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get the eventAttendeeGuest
        restEventAttendeeGuestMockMvc
            .perform(get(ENTITY_API_URL_ID, eventAttendeeGuest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventAttendeeGuest.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.guestName").value(DEFAULT_GUEST_NAME))
            .andExpect(jsonPath("$.ageGroup").value(DEFAULT_AGE_GROUP.toString()))
            .andExpect(jsonPath("$.relationship").value(DEFAULT_RELATIONSHIP.toString()))
            .andExpect(jsonPath("$.specialRequirements").value(DEFAULT_SPECIAL_REQUIREMENTS))
            .andExpect(jsonPath("$.registrationStatus").value(DEFAULT_REGISTRATION_STATUS.toString()))
            .andExpect(jsonPath("$.checkInStatus").value(DEFAULT_CHECK_IN_STATUS.toString()))
            .andExpect(jsonPath("$.checkInTime").value(sameInstant(DEFAULT_CHECK_IN_TIME)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventAttendeeGuestsByIdFiltering() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        Long id = eventAttendeeGuest.getId();

        defaultEventAttendeeGuestShouldBeFound("id.equals=" + id);
        defaultEventAttendeeGuestShouldNotBeFound("id.notEquals=" + id);

        defaultEventAttendeeGuestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventAttendeeGuestShouldNotBeFound("id.greaterThan=" + id);

        defaultEventAttendeeGuestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventAttendeeGuestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventAttendeeGuestShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventAttendeeGuestList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAttendeeGuestShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventAttendeeGuestShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventAttendeeGuestList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAttendeeGuestShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where tenantId is not null
        defaultEventAttendeeGuestShouldBeFound("tenantId.specified=true");

        // Get all the eventAttendeeGuestList where tenantId is null
        defaultEventAttendeeGuestShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where tenantId contains DEFAULT_TENANT_ID
        defaultEventAttendeeGuestShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventAttendeeGuestList where tenantId contains UPDATED_TENANT_ID
        defaultEventAttendeeGuestShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventAttendeeGuestShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventAttendeeGuestList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventAttendeeGuestShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByGuestNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where guestName equals to DEFAULT_GUEST_NAME
        defaultEventAttendeeGuestShouldBeFound("guestName.equals=" + DEFAULT_GUEST_NAME);

        // Get all the eventAttendeeGuestList where guestName equals to UPDATED_GUEST_NAME
        defaultEventAttendeeGuestShouldNotBeFound("guestName.equals=" + UPDATED_GUEST_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByGuestNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where guestName in DEFAULT_GUEST_NAME or UPDATED_GUEST_NAME
        defaultEventAttendeeGuestShouldBeFound("guestName.in=" + DEFAULT_GUEST_NAME + "," + UPDATED_GUEST_NAME);

        // Get all the eventAttendeeGuestList where guestName equals to UPDATED_GUEST_NAME
        defaultEventAttendeeGuestShouldNotBeFound("guestName.in=" + UPDATED_GUEST_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByGuestNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where guestName is not null
        defaultEventAttendeeGuestShouldBeFound("guestName.specified=true");

        // Get all the eventAttendeeGuestList where guestName is null
        defaultEventAttendeeGuestShouldNotBeFound("guestName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByGuestNameContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where guestName contains DEFAULT_GUEST_NAME
        defaultEventAttendeeGuestShouldBeFound("guestName.contains=" + DEFAULT_GUEST_NAME);

        // Get all the eventAttendeeGuestList where guestName contains UPDATED_GUEST_NAME
        defaultEventAttendeeGuestShouldNotBeFound("guestName.contains=" + UPDATED_GUEST_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByGuestNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where guestName does not contain DEFAULT_GUEST_NAME
        defaultEventAttendeeGuestShouldNotBeFound("guestName.doesNotContain=" + DEFAULT_GUEST_NAME);

        // Get all the eventAttendeeGuestList where guestName does not contain UPDATED_GUEST_NAME
        defaultEventAttendeeGuestShouldBeFound("guestName.doesNotContain=" + UPDATED_GUEST_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByAgeGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where ageGroup equals to DEFAULT_AGE_GROUP
        defaultEventAttendeeGuestShouldBeFound("ageGroup.equals=" + DEFAULT_AGE_GROUP);

        // Get all the eventAttendeeGuestList where ageGroup equals to UPDATED_AGE_GROUP
        defaultEventAttendeeGuestShouldNotBeFound("ageGroup.equals=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByAgeGroupIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where ageGroup in DEFAULT_AGE_GROUP or UPDATED_AGE_GROUP
        defaultEventAttendeeGuestShouldBeFound("ageGroup.in=" + DEFAULT_AGE_GROUP + "," + UPDATED_AGE_GROUP);

        // Get all the eventAttendeeGuestList where ageGroup equals to UPDATED_AGE_GROUP
        defaultEventAttendeeGuestShouldNotBeFound("ageGroup.in=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByAgeGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where ageGroup is not null
        defaultEventAttendeeGuestShouldBeFound("ageGroup.specified=true");

        // Get all the eventAttendeeGuestList where ageGroup is null
        defaultEventAttendeeGuestShouldNotBeFound("ageGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByRelationshipIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where relationship equals to DEFAULT_RELATIONSHIP
        defaultEventAttendeeGuestShouldBeFound("relationship.equals=" + DEFAULT_RELATIONSHIP);

        // Get all the eventAttendeeGuestList where relationship equals to UPDATED_RELATIONSHIP
        defaultEventAttendeeGuestShouldNotBeFound("relationship.equals=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByRelationshipIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where relationship in DEFAULT_RELATIONSHIP or UPDATED_RELATIONSHIP
        defaultEventAttendeeGuestShouldBeFound("relationship.in=" + DEFAULT_RELATIONSHIP + "," + UPDATED_RELATIONSHIP);

        // Get all the eventAttendeeGuestList where relationship equals to UPDATED_RELATIONSHIP
        defaultEventAttendeeGuestShouldNotBeFound("relationship.in=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByRelationshipIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where relationship is not null
        defaultEventAttendeeGuestShouldBeFound("relationship.specified=true");

        // Get all the eventAttendeeGuestList where relationship is null
        defaultEventAttendeeGuestShouldNotBeFound("relationship.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsBySpecialRequirementsIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where specialRequirements equals to DEFAULT_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldBeFound("specialRequirements.equals=" + DEFAULT_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeGuestList where specialRequirements equals to UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldNotBeFound("specialRequirements.equals=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsBySpecialRequirementsIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where specialRequirements in DEFAULT_SPECIAL_REQUIREMENTS or UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldBeFound(
            "specialRequirements.in=" + DEFAULT_SPECIAL_REQUIREMENTS + "," + UPDATED_SPECIAL_REQUIREMENTS
        );

        // Get all the eventAttendeeGuestList where specialRequirements equals to UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldNotBeFound("specialRequirements.in=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsBySpecialRequirementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where specialRequirements is not null
        defaultEventAttendeeGuestShouldBeFound("specialRequirements.specified=true");

        // Get all the eventAttendeeGuestList where specialRequirements is null
        defaultEventAttendeeGuestShouldNotBeFound("specialRequirements.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsBySpecialRequirementsContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where specialRequirements contains DEFAULT_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldBeFound("specialRequirements.contains=" + DEFAULT_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeGuestList where specialRequirements contains UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldNotBeFound("specialRequirements.contains=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsBySpecialRequirementsNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where specialRequirements does not contain DEFAULT_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldNotBeFound("specialRequirements.doesNotContain=" + DEFAULT_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeGuestList where specialRequirements does not contain UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeGuestShouldBeFound("specialRequirements.doesNotContain=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByRegistrationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where registrationStatus equals to DEFAULT_REGISTRATION_STATUS
        defaultEventAttendeeGuestShouldBeFound("registrationStatus.equals=" + DEFAULT_REGISTRATION_STATUS);

        // Get all the eventAttendeeGuestList where registrationStatus equals to UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeGuestShouldNotBeFound("registrationStatus.equals=" + UPDATED_REGISTRATION_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByRegistrationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where registrationStatus in DEFAULT_REGISTRATION_STATUS or UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeGuestShouldBeFound("registrationStatus.in=" + DEFAULT_REGISTRATION_STATUS + "," + UPDATED_REGISTRATION_STATUS);

        // Get all the eventAttendeeGuestList where registrationStatus equals to UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeGuestShouldNotBeFound("registrationStatus.in=" + UPDATED_REGISTRATION_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByRegistrationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where registrationStatus is not null
        defaultEventAttendeeGuestShouldBeFound("registrationStatus.specified=true");

        // Get all the eventAttendeeGuestList where registrationStatus is null
        defaultEventAttendeeGuestShouldNotBeFound("registrationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInStatus equals to DEFAULT_CHECK_IN_STATUS
        defaultEventAttendeeGuestShouldBeFound("checkInStatus.equals=" + DEFAULT_CHECK_IN_STATUS);

        // Get all the eventAttendeeGuestList where checkInStatus equals to UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeGuestShouldNotBeFound("checkInStatus.equals=" + UPDATED_CHECK_IN_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInStatus in DEFAULT_CHECK_IN_STATUS or UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeGuestShouldBeFound("checkInStatus.in=" + DEFAULT_CHECK_IN_STATUS + "," + UPDATED_CHECK_IN_STATUS);

        // Get all the eventAttendeeGuestList where checkInStatus equals to UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeGuestShouldNotBeFound("checkInStatus.in=" + UPDATED_CHECK_IN_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInStatus is not null
        defaultEventAttendeeGuestShouldBeFound("checkInStatus.specified=true");

        // Get all the eventAttendeeGuestList where checkInStatus is null
        defaultEventAttendeeGuestShouldNotBeFound("checkInStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime equals to DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldBeFound("checkInTime.equals=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeGuestList where checkInTime equals to UPDATED_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.equals=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime in DEFAULT_CHECK_IN_TIME or UPDATED_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldBeFound("checkInTime.in=" + DEFAULT_CHECK_IN_TIME + "," + UPDATED_CHECK_IN_TIME);

        // Get all the eventAttendeeGuestList where checkInTime equals to UPDATED_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.in=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime is not null
        defaultEventAttendeeGuestShouldBeFound("checkInTime.specified=true");

        // Get all the eventAttendeeGuestList where checkInTime is null
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime is greater than or equal to DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldBeFound("checkInTime.greaterThanOrEqual=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeGuestList where checkInTime is greater than or equal to UPDATED_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.greaterThanOrEqual=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime is less than or equal to DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldBeFound("checkInTime.lessThanOrEqual=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeGuestList where checkInTime is less than or equal to SMALLER_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.lessThanOrEqual=" + SMALLER_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime is less than DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.lessThan=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeGuestList where checkInTime is less than UPDATED_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldBeFound("checkInTime.lessThan=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCheckInTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where checkInTime is greater than DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldNotBeFound("checkInTime.greaterThan=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeGuestList where checkInTime is greater than SMALLER_CHECK_IN_TIME
        defaultEventAttendeeGuestShouldBeFound("checkInTime.greaterThan=" + SMALLER_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventAttendeeGuestShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeGuestList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventAttendeeGuestShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventAttendeeGuestList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt is not null
        defaultEventAttendeeGuestShouldBeFound("createdAt.specified=true");

        // Get all the eventAttendeeGuestList where createdAt is null
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventAttendeeGuestShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeGuestList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventAttendeeGuestShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeGuestList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeGuestList where createdAt is less than UPDATED_CREATED_AT
        defaultEventAttendeeGuestShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeGuestList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventAttendeeGuestShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventAttendeeGuestShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeGuestList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventAttendeeGuestShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventAttendeeGuestList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt is not null
        defaultEventAttendeeGuestShouldBeFound("updatedAt.specified=true");

        // Get all the eventAttendeeGuestList where updatedAt is null
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventAttendeeGuestShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeGuestList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventAttendeeGuestShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeGuestList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeGuestList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventAttendeeGuestShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        // Get all the eventAttendeeGuestList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventAttendeeGuestShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeGuestList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventAttendeeGuestShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeeGuestsByPrimaryAttendeeIsEqualToSomething() throws Exception {
        EventAttendee primaryAttendee;
        if (TestUtil.findAll(em, EventAttendee.class).isEmpty()) {
            eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);
            primaryAttendee = EventAttendeeResourceIT.createEntity(em);
        } else {
            primaryAttendee = TestUtil.findAll(em, EventAttendee.class).get(0);
        }
        em.persist(primaryAttendee);
        em.flush();
        eventAttendeeGuest.setPrimaryAttendee(primaryAttendee);
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);
        Long primaryAttendeeId = primaryAttendee.getId();
        // Get all the eventAttendeeGuestList where primaryAttendee equals to primaryAttendeeId
        defaultEventAttendeeGuestShouldBeFound("primaryAttendeeId.equals=" + primaryAttendeeId);

        // Get all the eventAttendeeGuestList where primaryAttendee equals to (primaryAttendeeId + 1)
        defaultEventAttendeeGuestShouldNotBeFound("primaryAttendeeId.equals=" + (primaryAttendeeId + 1));
    }

    *//**
     * Executes the search, and checks that the default entity is returned.
     *//*
    private void defaultEventAttendeeGuestShouldBeFound(String filter) throws Exception {
        restEventAttendeeGuestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAttendeeGuest.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].guestName").value(hasItem(DEFAULT_GUEST_NAME)))
            .andExpect(jsonPath("$.[*].ageGroup").value(hasItem(DEFAULT_AGE_GROUP.toString())))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP.toString())))
            .andExpect(jsonPath("$.[*].specialRequirements").value(hasItem(DEFAULT_SPECIAL_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].registrationStatus").value(hasItem(DEFAULT_REGISTRATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].checkInStatus").value(hasItem(DEFAULT_CHECK_IN_STATUS.toString())))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(sameInstant(DEFAULT_CHECK_IN_TIME))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventAttendeeGuestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    *//**
     * Executes the search, and checks that the default entity is not returned.
     *//*
    private void defaultEventAttendeeGuestShouldNotBeFound(String filter) throws Exception {
        restEventAttendeeGuestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventAttendeeGuestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventAttendeeGuest() throws Exception {
        // Get the eventAttendeeGuest
        restEventAttendeeGuestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventAttendeeGuest() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();

        // Update the eventAttendeeGuest
        EventAttendeeGuest updatedEventAttendeeGuest = eventAttendeeGuestRepository.findById(eventAttendeeGuest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventAttendeeGuest are not directly saved in db
        em.detach(updatedEventAttendeeGuest);
        updatedEventAttendeeGuest
            .tenantId(UPDATED_TENANT_ID)
            .guestName(UPDATED_GUEST_NAME)
            .ageGroup(UPDATED_AGE_GROUP)
            .relationship(UPDATED_RELATIONSHIP)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(updatedEventAttendeeGuest);

        restEventAttendeeGuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAttendeeGuestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
        EventAttendeeGuest testEventAttendeeGuest = eventAttendeeGuestList.get(eventAttendeeGuestList.size() - 1);
        assertThat(testEventAttendeeGuest.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAttendeeGuest.getGuestName()).isEqualTo(UPDATED_GUEST_NAME);
        assertThat(testEventAttendeeGuest.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
        assertThat(testEventAttendeeGuest.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
        assertThat(testEventAttendeeGuest.getSpecialRequirements()).isEqualTo(UPDATED_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendeeGuest.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInStatus()).isEqualTo(UPDATED_CHECK_IN_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInTime()).isEqualTo(UPDATED_CHECK_IN_TIME);
        assertThat(testEventAttendeeGuest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAttendeeGuest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();
        eventAttendeeGuest.setId(longCount.incrementAndGet());

        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAttendeeGuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAttendeeGuestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();
        eventAttendeeGuest.setId(longCount.incrementAndGet());

        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeGuestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();
        eventAttendeeGuest.setId(longCount.incrementAndGet());

        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeGuestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventAttendeeGuestWithPatch() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();

        // Update the eventAttendeeGuest using partial update
        EventAttendeeGuest partialUpdatedEventAttendeeGuest = new EventAttendeeGuest();
        partialUpdatedEventAttendeeGuest.setId(eventAttendeeGuest.getId());

        partialUpdatedEventAttendeeGuest
            .guestName(UPDATED_GUEST_NAME)
            .relationship(UPDATED_RELATIONSHIP)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventAttendeeGuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAttendeeGuest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAttendeeGuest))
            )
            .andExpect(status().isOk());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
        EventAttendeeGuest testEventAttendeeGuest = eventAttendeeGuestList.get(eventAttendeeGuestList.size() - 1);
        assertThat(testEventAttendeeGuest.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAttendeeGuest.getGuestName()).isEqualTo(UPDATED_GUEST_NAME);
        assertThat(testEventAttendeeGuest.getAgeGroup()).isEqualTo(DEFAULT_AGE_GROUP);
        assertThat(testEventAttendeeGuest.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
        assertThat(testEventAttendeeGuest.getSpecialRequirements()).isEqualTo(UPDATED_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendeeGuest.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInStatus()).isEqualTo(DEFAULT_CHECK_IN_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInTime()).isEqualTo(DEFAULT_CHECK_IN_TIME);
        assertThat(testEventAttendeeGuest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAttendeeGuest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventAttendeeGuestWithPatch() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();

        // Update the eventAttendeeGuest using partial update
        EventAttendeeGuest partialUpdatedEventAttendeeGuest = new EventAttendeeGuest();
        partialUpdatedEventAttendeeGuest.setId(eventAttendeeGuest.getId());

        partialUpdatedEventAttendeeGuest
            .tenantId(UPDATED_TENANT_ID)
            .guestName(UPDATED_GUEST_NAME)
            .ageGroup(UPDATED_AGE_GROUP)
            .relationship(UPDATED_RELATIONSHIP)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventAttendeeGuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAttendeeGuest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAttendeeGuest))
            )
            .andExpect(status().isOk());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
        EventAttendeeGuest testEventAttendeeGuest = eventAttendeeGuestList.get(eventAttendeeGuestList.size() - 1);
        assertThat(testEventAttendeeGuest.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAttendeeGuest.getGuestName()).isEqualTo(UPDATED_GUEST_NAME);
        assertThat(testEventAttendeeGuest.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
        assertThat(testEventAttendeeGuest.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
        assertThat(testEventAttendeeGuest.getSpecialRequirements()).isEqualTo(UPDATED_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendeeGuest.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInStatus()).isEqualTo(UPDATED_CHECK_IN_STATUS);
        assertThat(testEventAttendeeGuest.getCheckInTime()).isEqualTo(UPDATED_CHECK_IN_TIME);
        assertThat(testEventAttendeeGuest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAttendeeGuest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();
        eventAttendeeGuest.setId(longCount.incrementAndGet());

        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAttendeeGuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventAttendeeGuestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();
        eventAttendeeGuest.setId(longCount.incrementAndGet());

        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeGuestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventAttendeeGuest() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeGuestRepository.findAll().size();
        eventAttendeeGuest.setId(longCount.incrementAndGet());

        // Create the EventAttendeeGuest
        EventAttendeeGuestDTO eventAttendeeGuestDTO = eventAttendeeGuestMapper.toDto(eventAttendeeGuest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeGuestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeGuestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAttendeeGuest in the database
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventAttendeeGuest() throws Exception {
        // Initialize the database
        eventAttendeeGuestRepository.saveAndFlush(eventAttendeeGuest);

        int databaseSizeBeforeDelete = eventAttendeeGuestRepository.findAll().size();

        // Delete the eventAttendeeGuest
        restEventAttendeeGuestMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventAttendeeGuest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventAttendeeGuest> eventAttendeeGuestList = eventAttendeeGuestRepository.findAll();
        assertThat(eventAttendeeGuestList).hasSize(databaseSizeBeforeDelete - 1);
    }
*/
}
