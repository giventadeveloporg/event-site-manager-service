package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.DiscountCode;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventTypeDetails;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.EventDetailsRepository;
import com.eventsitemanager.service.EventDetailsService;
import com.eventsitemanager.service.dto.EventDetailsDTO;
import com.eventsitemanager.service.mapper.EventDetailsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EventDetailsResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTIONS_TO_VENUE = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTIONS_TO_VENUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;
    private static final Integer SMALLER_CAPACITY = 1 - 1;

    private static final String DEFAULT_ADMISSION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ADMISSION_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_MAX_GUESTS_PER_ATTENDEE = 0;
    private static final Integer UPDATED_MAX_GUESTS_PER_ATTENDEE = 1;
    private static final Integer SMALLER_MAX_GUESTS_PER_ATTENDEE = 0 - 1;

    private static final Boolean DEFAULT_ALLOW_GUESTS = false;
    private static final Boolean UPDATED_ALLOW_GUESTS = true;

    private static final Boolean DEFAULT_REQUIRE_GUEST_APPROVAL = false;
    private static final Boolean UPDATED_REQUIRE_GUEST_APPROVAL = true;

    private static final Boolean DEFAULT_ENABLE_GUEST_PRICING = false;
    private static final Boolean UPDATED_ENABLE_GUEST_PRICING = true;

    private static final Boolean DEFAULT_IS_REGISTRATION_REQUIRED = false;
    private static final Boolean UPDATED_IS_REGISTRATION_REQUIRED = true;

    private static final Boolean DEFAULT_IS_SPORTS_EVENT = false;
    private static final Boolean UPDATED_IS_SPORTS_EVENT = true;

    private static final Boolean DEFAULT_IS_LIVE = false;
    private static final Boolean UPDATED_IS_LIVE = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventDetailsRepository eventDetailsRepository;

    @Mock
    private EventDetailsRepository eventDetailsRepositoryMock;

    @Autowired
    private EventDetailsMapper eventDetailsMapper;

    @Mock
    private EventDetailsService eventDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventDetailsMockMvc;

    private EventDetails eventDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventDetails createEntity(EntityManager em) {
        EventDetails eventDetails = new EventDetails()
            .tenantId(DEFAULT_TENANT_ID)
            .title(DEFAULT_TITLE)
            .caption(DEFAULT_CAPTION)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .location(DEFAULT_LOCATION)
            .directionsToVenue(DEFAULT_DIRECTIONS_TO_VENUE)
            .capacity(DEFAULT_CAPACITY)
            .admissionType(DEFAULT_ADMISSION_TYPE)
            .isActive(DEFAULT_IS_ACTIVE)
            .maxGuestsPerAttendee(DEFAULT_MAX_GUESTS_PER_ATTENDEE)
            .allowGuests(DEFAULT_ALLOW_GUESTS)
            .requireGuestApproval(DEFAULT_REQUIRE_GUEST_APPROVAL)
            .enableGuestPricing(DEFAULT_ENABLE_GUEST_PRICING)
            .isRegistrationRequired(DEFAULT_IS_REGISTRATION_REQUIRED)
            .isSportsEvent(DEFAULT_IS_SPORTS_EVENT)
            .isLive(DEFAULT_IS_LIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventDetails createUpdatedEntity(EntityManager em) {
        EventDetails eventDetails = new EventDetails()
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .location(UPDATED_LOCATION)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .maxGuestsPerAttendee(UPDATED_MAX_GUESTS_PER_ATTENDEE)
            .allowGuests(UPDATED_ALLOW_GUESTS)
            .requireGuestApproval(UPDATED_REQUIRE_GUEST_APPROVAL)
            .enableGuestPricing(UPDATED_ENABLE_GUEST_PRICING)
            .isRegistrationRequired(UPDATED_IS_REGISTRATION_REQUIRED)
            .isSportsEvent(UPDATED_IS_SPORTS_EVENT)
            .isLive(UPDATED_IS_LIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventDetails;
    }

    @BeforeEach
    public void initTest() {
        eventDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createEventDetails() throws Exception {
        int databaseSizeBeforeCreate = eventDetailsRepository.findAll().size();
        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);
        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        EventDetails testEventDetails = eventDetailsList.get(eventDetailsList.size() - 1);
        assertThat(testEventDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventDetails.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventDetails.getCaption()).isEqualTo(DEFAULT_CAPTION);
        assertThat(testEventDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventDetails.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEventDetails.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEventDetails.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testEventDetails.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testEventDetails.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEventDetails.getDirectionsToVenue()).isEqualTo(DEFAULT_DIRECTIONS_TO_VENUE);
        assertThat(testEventDetails.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testEventDetails.getAdmissionType()).isEqualTo(DEFAULT_ADMISSION_TYPE);
        assertThat(testEventDetails.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEventDetails.getMaxGuestsPerAttendee()).isEqualTo(DEFAULT_MAX_GUESTS_PER_ATTENDEE);
        assertThat(testEventDetails.getAllowGuests()).isEqualTo(DEFAULT_ALLOW_GUESTS);
        assertThat(testEventDetails.getRequireGuestApproval()).isEqualTo(DEFAULT_REQUIRE_GUEST_APPROVAL);
        assertThat(testEventDetails.getEnableGuestPricing()).isEqualTo(DEFAULT_ENABLE_GUEST_PRICING);
        assertThat(testEventDetails.getIsRegistrationRequired()).isEqualTo(DEFAULT_IS_REGISTRATION_REQUIRED);
        assertThat(testEventDetails.getIsSportsEvent()).isEqualTo(DEFAULT_IS_SPORTS_EVENT);
        assertThat(testEventDetails.getIsLive()).isEqualTo(DEFAULT_IS_LIVE);
        assertThat(testEventDetails.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventDetails.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventDetailsWithExistingId() throws Exception {
        // Create the EventDetails with an existing ID
        eventDetails.setId(1L);
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        int databaseSizeBeforeCreate = eventDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setTitle(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setStartDate(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setEndDate(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setStartTime(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setEndTime(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setCreatedAt(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventDetailsRepository.findAll().size();
        // set the field null
        eventDetails.setUpdatedAt(null);

        // Create the EventDetails, which fails.
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        restEventDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventDetails() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList
        restEventDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].directionsToVenue").value(hasItem(DEFAULT_DIRECTIONS_TO_VENUE)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].admissionType").value(hasItem(DEFAULT_ADMISSION_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].maxGuestsPerAttendee").value(hasItem(DEFAULT_MAX_GUESTS_PER_ATTENDEE)))
            .andExpect(jsonPath("$.[*].allowGuests").value(hasItem(DEFAULT_ALLOW_GUESTS.booleanValue())))
            .andExpect(jsonPath("$.[*].requireGuestApproval").value(hasItem(DEFAULT_REQUIRE_GUEST_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].enableGuestPricing").value(hasItem(DEFAULT_ENABLE_GUEST_PRICING.booleanValue())))
            .andExpect(jsonPath("$.[*].isRegistrationRequired").value(hasItem(DEFAULT_IS_REGISTRATION_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].isSportsEvent").value(hasItem(DEFAULT_IS_SPORTS_EVENT.booleanValue())))
            .andExpect(jsonPath("$.[*].isLive").value(hasItem(DEFAULT_IS_LIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(eventDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eventDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEventDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eventDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(eventDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEventDetails() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get the eventDetails
        restEventDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, eventDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventDetails.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.directionsToVenue").value(DEFAULT_DIRECTIONS_TO_VENUE))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.admissionType").value(DEFAULT_ADMISSION_TYPE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.maxGuestsPerAttendee").value(DEFAULT_MAX_GUESTS_PER_ATTENDEE))
            .andExpect(jsonPath("$.allowGuests").value(DEFAULT_ALLOW_GUESTS.booleanValue()))
            .andExpect(jsonPath("$.requireGuestApproval").value(DEFAULT_REQUIRE_GUEST_APPROVAL.booleanValue()))
            .andExpect(jsonPath("$.enableGuestPricing").value(DEFAULT_ENABLE_GUEST_PRICING.booleanValue()))
            .andExpect(jsonPath("$.isRegistrationRequired").value(DEFAULT_IS_REGISTRATION_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.isSportsEvent").value(DEFAULT_IS_SPORTS_EVENT.booleanValue()))
            .andExpect(jsonPath("$.isLive").value(DEFAULT_IS_LIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventDetailsByIdFiltering() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        Long id = eventDetails.getId();

        defaultEventDetailsShouldBeFound("id.equals=" + id);
        defaultEventDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultEventDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultEventDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventDetailsShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventDetailsList where tenantId equals to UPDATED_TENANT_ID
        defaultEventDetailsShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventDetailsShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventDetailsList where tenantId equals to UPDATED_TENANT_ID
        defaultEventDetailsShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where tenantId is not null
        defaultEventDetailsShouldBeFound("tenantId.specified=true");

        // Get all the eventDetailsList where tenantId is null
        defaultEventDetailsShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where tenantId contains DEFAULT_TENANT_ID
        defaultEventDetailsShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventDetailsList where tenantId contains UPDATED_TENANT_ID
        defaultEventDetailsShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventDetailsShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventDetailsList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventDetailsShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where title equals to DEFAULT_TITLE
        defaultEventDetailsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventDetailsList where title equals to UPDATED_TITLE
        defaultEventDetailsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventDetailsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventDetailsList where title equals to UPDATED_TITLE
        defaultEventDetailsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where title is not null
        defaultEventDetailsShouldBeFound("title.specified=true");

        // Get all the eventDetailsList where title is null
        defaultEventDetailsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByTitleContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where title contains DEFAULT_TITLE
        defaultEventDetailsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventDetailsList where title contains UPDATED_TITLE
        defaultEventDetailsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where title does not contain DEFAULT_TITLE
        defaultEventDetailsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventDetailsList where title does not contain UPDATED_TITLE
        defaultEventDetailsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCaptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where caption equals to DEFAULT_CAPTION
        defaultEventDetailsShouldBeFound("caption.equals=" + DEFAULT_CAPTION);

        // Get all the eventDetailsList where caption equals to UPDATED_CAPTION
        defaultEventDetailsShouldNotBeFound("caption.equals=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCaptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where caption in DEFAULT_CAPTION or UPDATED_CAPTION
        defaultEventDetailsShouldBeFound("caption.in=" + DEFAULT_CAPTION + "," + UPDATED_CAPTION);

        // Get all the eventDetailsList where caption equals to UPDATED_CAPTION
        defaultEventDetailsShouldNotBeFound("caption.in=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCaptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where caption is not null
        defaultEventDetailsShouldBeFound("caption.specified=true");

        // Get all the eventDetailsList where caption is null
        defaultEventDetailsShouldNotBeFound("caption.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByCaptionContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where caption contains DEFAULT_CAPTION
        defaultEventDetailsShouldBeFound("caption.contains=" + DEFAULT_CAPTION);

        // Get all the eventDetailsList where caption contains UPDATED_CAPTION
        defaultEventDetailsShouldNotBeFound("caption.contains=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCaptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where caption does not contain DEFAULT_CAPTION
        defaultEventDetailsShouldNotBeFound("caption.doesNotContain=" + DEFAULT_CAPTION);

        // Get all the eventDetailsList where caption does not contain UPDATED_CAPTION
        defaultEventDetailsShouldBeFound("caption.doesNotContain=" + UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultEventDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventDetailsList where description equals to UPDATED_DESCRIPTION
        defaultEventDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventDetailsList where description equals to UPDATED_DESCRIPTION
        defaultEventDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where description is not null
        defaultEventDetailsShouldBeFound("description.specified=true");

        // Get all the eventDetailsList where description is null
        defaultEventDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where description contains DEFAULT_DESCRIPTION
        defaultEventDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventDetailsList where description contains UPDATED_DESCRIPTION
        defaultEventDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultEventDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultEventDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate equals to DEFAULT_START_DATE
        defaultEventDetailsShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the eventDetailsList where startDate equals to UPDATED_START_DATE
        defaultEventDetailsShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultEventDetailsShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the eventDetailsList where startDate equals to UPDATED_START_DATE
        defaultEventDetailsShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate is not null
        defaultEventDetailsShouldBeFound("startDate.specified=true");

        // Get all the eventDetailsList where startDate is null
        defaultEventDetailsShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultEventDetailsShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the eventDetailsList where startDate is greater than or equal to UPDATED_START_DATE
        defaultEventDetailsShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate is less than or equal to DEFAULT_START_DATE
        defaultEventDetailsShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the eventDetailsList where startDate is less than or equal to SMALLER_START_DATE
        defaultEventDetailsShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate is less than DEFAULT_START_DATE
        defaultEventDetailsShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the eventDetailsList where startDate is less than UPDATED_START_DATE
        defaultEventDetailsShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startDate is greater than DEFAULT_START_DATE
        defaultEventDetailsShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the eventDetailsList where startDate is greater than SMALLER_START_DATE
        defaultEventDetailsShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate equals to DEFAULT_END_DATE
        defaultEventDetailsShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the eventDetailsList where endDate equals to UPDATED_END_DATE
        defaultEventDetailsShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultEventDetailsShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the eventDetailsList where endDate equals to UPDATED_END_DATE
        defaultEventDetailsShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate is not null
        defaultEventDetailsShouldBeFound("endDate.specified=true");

        // Get all the eventDetailsList where endDate is null
        defaultEventDetailsShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultEventDetailsShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventDetailsList where endDate is greater than or equal to UPDATED_END_DATE
        defaultEventDetailsShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate is less than or equal to DEFAULT_END_DATE
        defaultEventDetailsShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventDetailsList where endDate is less than or equal to SMALLER_END_DATE
        defaultEventDetailsShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate is less than DEFAULT_END_DATE
        defaultEventDetailsShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the eventDetailsList where endDate is less than UPDATED_END_DATE
        defaultEventDetailsShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endDate is greater than DEFAULT_END_DATE
        defaultEventDetailsShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the eventDetailsList where endDate is greater than SMALLER_END_DATE
        defaultEventDetailsShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startTime equals to DEFAULT_START_TIME
        defaultEventDetailsShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the eventDetailsList where startTime equals to UPDATED_START_TIME
        defaultEventDetailsShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultEventDetailsShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the eventDetailsList where startTime equals to UPDATED_START_TIME
        defaultEventDetailsShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startTime is not null
        defaultEventDetailsShouldBeFound("startTime.specified=true");

        // Get all the eventDetailsList where startTime is null
        defaultEventDetailsShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartTimeContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startTime contains DEFAULT_START_TIME
        defaultEventDetailsShouldBeFound("startTime.contains=" + DEFAULT_START_TIME);

        // Get all the eventDetailsList where startTime contains UPDATED_START_TIME
        defaultEventDetailsShouldNotBeFound("startTime.contains=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByStartTimeNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where startTime does not contain DEFAULT_START_TIME
        defaultEventDetailsShouldNotBeFound("startTime.doesNotContain=" + DEFAULT_START_TIME);

        // Get all the eventDetailsList where startTime does not contain UPDATED_START_TIME
        defaultEventDetailsShouldBeFound("startTime.doesNotContain=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endTime equals to DEFAULT_END_TIME
        defaultEventDetailsShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the eventDetailsList where endTime equals to UPDATED_END_TIME
        defaultEventDetailsShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultEventDetailsShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the eventDetailsList where endTime equals to UPDATED_END_TIME
        defaultEventDetailsShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endTime is not null
        defaultEventDetailsShouldBeFound("endTime.specified=true");

        // Get all the eventDetailsList where endTime is null
        defaultEventDetailsShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndTimeContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endTime contains DEFAULT_END_TIME
        defaultEventDetailsShouldBeFound("endTime.contains=" + DEFAULT_END_TIME);

        // Get all the eventDetailsList where endTime contains UPDATED_END_TIME
        defaultEventDetailsShouldNotBeFound("endTime.contains=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEndTimeNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where endTime does not contain DEFAULT_END_TIME
        defaultEventDetailsShouldNotBeFound("endTime.doesNotContain=" + DEFAULT_END_TIME);

        // Get all the eventDetailsList where endTime does not contain UPDATED_END_TIME
        defaultEventDetailsShouldBeFound("endTime.doesNotContain=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllEventDetailsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where location equals to DEFAULT_LOCATION
        defaultEventDetailsShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the eventDetailsList where location equals to UPDATED_LOCATION
        defaultEventDetailsShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultEventDetailsShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the eventDetailsList where location equals to UPDATED_LOCATION
        defaultEventDetailsShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where location is not null
        defaultEventDetailsShouldBeFound("location.specified=true");

        // Get all the eventDetailsList where location is null
        defaultEventDetailsShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByLocationContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where location contains DEFAULT_LOCATION
        defaultEventDetailsShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the eventDetailsList where location contains UPDATED_LOCATION
        defaultEventDetailsShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where location does not contain DEFAULT_LOCATION
        defaultEventDetailsShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the eventDetailsList where location does not contain UPDATED_LOCATION
        defaultEventDetailsShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDirectionsToVenueIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where directionsToVenue equals to DEFAULT_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldBeFound("directionsToVenue.equals=" + DEFAULT_DIRECTIONS_TO_VENUE);

        // Get all the eventDetailsList where directionsToVenue equals to UPDATED_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldNotBeFound("directionsToVenue.equals=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDirectionsToVenueIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where directionsToVenue in DEFAULT_DIRECTIONS_TO_VENUE or UPDATED_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldBeFound("directionsToVenue.in=" + DEFAULT_DIRECTIONS_TO_VENUE + "," + UPDATED_DIRECTIONS_TO_VENUE);

        // Get all the eventDetailsList where directionsToVenue equals to UPDATED_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldNotBeFound("directionsToVenue.in=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDirectionsToVenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where directionsToVenue is not null
        defaultEventDetailsShouldBeFound("directionsToVenue.specified=true");

        // Get all the eventDetailsList where directionsToVenue is null
        defaultEventDetailsShouldNotBeFound("directionsToVenue.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByDirectionsToVenueContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where directionsToVenue contains DEFAULT_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldBeFound("directionsToVenue.contains=" + DEFAULT_DIRECTIONS_TO_VENUE);

        // Get all the eventDetailsList where directionsToVenue contains UPDATED_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldNotBeFound("directionsToVenue.contains=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByDirectionsToVenueNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where directionsToVenue does not contain DEFAULT_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldNotBeFound("directionsToVenue.doesNotContain=" + DEFAULT_DIRECTIONS_TO_VENUE);

        // Get all the eventDetailsList where directionsToVenue does not contain UPDATED_DIRECTIONS_TO_VENUE
        defaultEventDetailsShouldBeFound("directionsToVenue.doesNotContain=" + UPDATED_DIRECTIONS_TO_VENUE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity equals to DEFAULT_CAPACITY
        defaultEventDetailsShouldBeFound("capacity.equals=" + DEFAULT_CAPACITY);

        // Get all the eventDetailsList where capacity equals to UPDATED_CAPACITY
        defaultEventDetailsShouldNotBeFound("capacity.equals=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity in DEFAULT_CAPACITY or UPDATED_CAPACITY
        defaultEventDetailsShouldBeFound("capacity.in=" + DEFAULT_CAPACITY + "," + UPDATED_CAPACITY);

        // Get all the eventDetailsList where capacity equals to UPDATED_CAPACITY
        defaultEventDetailsShouldNotBeFound("capacity.in=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity is not null
        defaultEventDetailsShouldBeFound("capacity.specified=true");

        // Get all the eventDetailsList where capacity is null
        defaultEventDetailsShouldNotBeFound("capacity.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity is greater than or equal to DEFAULT_CAPACITY
        defaultEventDetailsShouldBeFound("capacity.greaterThanOrEqual=" + DEFAULT_CAPACITY);

        // Get all the eventDetailsList where capacity is greater than or equal to UPDATED_CAPACITY
        defaultEventDetailsShouldNotBeFound("capacity.greaterThanOrEqual=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity is less than or equal to DEFAULT_CAPACITY
        defaultEventDetailsShouldBeFound("capacity.lessThanOrEqual=" + DEFAULT_CAPACITY);

        // Get all the eventDetailsList where capacity is less than or equal to SMALLER_CAPACITY
        defaultEventDetailsShouldNotBeFound("capacity.lessThanOrEqual=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity is less than DEFAULT_CAPACITY
        defaultEventDetailsShouldNotBeFound("capacity.lessThan=" + DEFAULT_CAPACITY);

        // Get all the eventDetailsList where capacity is less than UPDATED_CAPACITY
        defaultEventDetailsShouldBeFound("capacity.lessThan=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where capacity is greater than DEFAULT_CAPACITY
        defaultEventDetailsShouldNotBeFound("capacity.greaterThan=" + DEFAULT_CAPACITY);

        // Get all the eventDetailsList where capacity is greater than SMALLER_CAPACITY
        defaultEventDetailsShouldBeFound("capacity.greaterThan=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAdmissionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where admissionType equals to DEFAULT_ADMISSION_TYPE
        defaultEventDetailsShouldBeFound("admissionType.equals=" + DEFAULT_ADMISSION_TYPE);

        // Get all the eventDetailsList where admissionType equals to UPDATED_ADMISSION_TYPE
        defaultEventDetailsShouldNotBeFound("admissionType.equals=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAdmissionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where admissionType in DEFAULT_ADMISSION_TYPE or UPDATED_ADMISSION_TYPE
        defaultEventDetailsShouldBeFound("admissionType.in=" + DEFAULT_ADMISSION_TYPE + "," + UPDATED_ADMISSION_TYPE);

        // Get all the eventDetailsList where admissionType equals to UPDATED_ADMISSION_TYPE
        defaultEventDetailsShouldNotBeFound("admissionType.in=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAdmissionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where admissionType is not null
        defaultEventDetailsShouldBeFound("admissionType.specified=true");

        // Get all the eventDetailsList where admissionType is null
        defaultEventDetailsShouldNotBeFound("admissionType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByAdmissionTypeContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where admissionType contains DEFAULT_ADMISSION_TYPE
        defaultEventDetailsShouldBeFound("admissionType.contains=" + DEFAULT_ADMISSION_TYPE);

        // Get all the eventDetailsList where admissionType contains UPDATED_ADMISSION_TYPE
        defaultEventDetailsShouldNotBeFound("admissionType.contains=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAdmissionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where admissionType does not contain DEFAULT_ADMISSION_TYPE
        defaultEventDetailsShouldNotBeFound("admissionType.doesNotContain=" + DEFAULT_ADMISSION_TYPE);

        // Get all the eventDetailsList where admissionType does not contain UPDATED_ADMISSION_TYPE
        defaultEventDetailsShouldBeFound("admissionType.doesNotContain=" + UPDATED_ADMISSION_TYPE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEventDetailsShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the eventDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventDetailsShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEventDetailsShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the eventDetailsList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventDetailsShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isActive is not null
        defaultEventDetailsShouldBeFound("isActive.specified=true");

        // Get all the eventDetailsList where isActive is null
        defaultEventDetailsShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee equals to DEFAULT_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldBeFound("maxGuestsPerAttendee.equals=" + DEFAULT_MAX_GUESTS_PER_ATTENDEE);

        // Get all the eventDetailsList where maxGuestsPerAttendee equals to UPDATED_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.equals=" + UPDATED_MAX_GUESTS_PER_ATTENDEE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee in DEFAULT_MAX_GUESTS_PER_ATTENDEE or UPDATED_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldBeFound(
            "maxGuestsPerAttendee.in=" + DEFAULT_MAX_GUESTS_PER_ATTENDEE + "," + UPDATED_MAX_GUESTS_PER_ATTENDEE
        );

        // Get all the eventDetailsList where maxGuestsPerAttendee equals to UPDATED_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.in=" + UPDATED_MAX_GUESTS_PER_ATTENDEE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee is not null
        defaultEventDetailsShouldBeFound("maxGuestsPerAttendee.specified=true");

        // Get all the eventDetailsList where maxGuestsPerAttendee is null
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee is greater than or equal to DEFAULT_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldBeFound("maxGuestsPerAttendee.greaterThanOrEqual=" + DEFAULT_MAX_GUESTS_PER_ATTENDEE);

        // Get all the eventDetailsList where maxGuestsPerAttendee is greater than or equal to UPDATED_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.greaterThanOrEqual=" + UPDATED_MAX_GUESTS_PER_ATTENDEE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee is less than or equal to DEFAULT_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldBeFound("maxGuestsPerAttendee.lessThanOrEqual=" + DEFAULT_MAX_GUESTS_PER_ATTENDEE);

        // Get all the eventDetailsList where maxGuestsPerAttendee is less than or equal to SMALLER_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.lessThanOrEqual=" + SMALLER_MAX_GUESTS_PER_ATTENDEE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee is less than DEFAULT_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.lessThan=" + DEFAULT_MAX_GUESTS_PER_ATTENDEE);

        // Get all the eventDetailsList where maxGuestsPerAttendee is less than UPDATED_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldBeFound("maxGuestsPerAttendee.lessThan=" + UPDATED_MAX_GUESTS_PER_ATTENDEE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByMaxGuestsPerAttendeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where maxGuestsPerAttendee is greater than DEFAULT_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldNotBeFound("maxGuestsPerAttendee.greaterThan=" + DEFAULT_MAX_GUESTS_PER_ATTENDEE);

        // Get all the eventDetailsList where maxGuestsPerAttendee is greater than SMALLER_MAX_GUESTS_PER_ATTENDEE
        defaultEventDetailsShouldBeFound("maxGuestsPerAttendee.greaterThan=" + SMALLER_MAX_GUESTS_PER_ATTENDEE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAllowGuestsIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where allowGuests equals to DEFAULT_ALLOW_GUESTS
        defaultEventDetailsShouldBeFound("allowGuests.equals=" + DEFAULT_ALLOW_GUESTS);

        // Get all the eventDetailsList where allowGuests equals to UPDATED_ALLOW_GUESTS
        defaultEventDetailsShouldNotBeFound("allowGuests.equals=" + UPDATED_ALLOW_GUESTS);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAllowGuestsIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where allowGuests in DEFAULT_ALLOW_GUESTS or UPDATED_ALLOW_GUESTS
        defaultEventDetailsShouldBeFound("allowGuests.in=" + DEFAULT_ALLOW_GUESTS + "," + UPDATED_ALLOW_GUESTS);

        // Get all the eventDetailsList where allowGuests equals to UPDATED_ALLOW_GUESTS
        defaultEventDetailsShouldNotBeFound("allowGuests.in=" + UPDATED_ALLOW_GUESTS);
    }

    @Test
    @Transactional
    void getAllEventDetailsByAllowGuestsIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where allowGuests is not null
        defaultEventDetailsShouldBeFound("allowGuests.specified=true");

        // Get all the eventDetailsList where allowGuests is null
        defaultEventDetailsShouldNotBeFound("allowGuests.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByRequireGuestApprovalIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where requireGuestApproval equals to DEFAULT_REQUIRE_GUEST_APPROVAL
        defaultEventDetailsShouldBeFound("requireGuestApproval.equals=" + DEFAULT_REQUIRE_GUEST_APPROVAL);

        // Get all the eventDetailsList where requireGuestApproval equals to UPDATED_REQUIRE_GUEST_APPROVAL
        defaultEventDetailsShouldNotBeFound("requireGuestApproval.equals=" + UPDATED_REQUIRE_GUEST_APPROVAL);
    }

    @Test
    @Transactional
    void getAllEventDetailsByRequireGuestApprovalIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where requireGuestApproval in DEFAULT_REQUIRE_GUEST_APPROVAL or UPDATED_REQUIRE_GUEST_APPROVAL
        defaultEventDetailsShouldBeFound(
            "requireGuestApproval.in=" + DEFAULT_REQUIRE_GUEST_APPROVAL + "," + UPDATED_REQUIRE_GUEST_APPROVAL
        );

        // Get all the eventDetailsList where requireGuestApproval equals to UPDATED_REQUIRE_GUEST_APPROVAL
        defaultEventDetailsShouldNotBeFound("requireGuestApproval.in=" + UPDATED_REQUIRE_GUEST_APPROVAL);
    }

    @Test
    @Transactional
    void getAllEventDetailsByRequireGuestApprovalIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where requireGuestApproval is not null
        defaultEventDetailsShouldBeFound("requireGuestApproval.specified=true");

        // Get all the eventDetailsList where requireGuestApproval is null
        defaultEventDetailsShouldNotBeFound("requireGuestApproval.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByEnableGuestPricingIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where enableGuestPricing equals to DEFAULT_ENABLE_GUEST_PRICING
        defaultEventDetailsShouldBeFound("enableGuestPricing.equals=" + DEFAULT_ENABLE_GUEST_PRICING);

        // Get all the eventDetailsList where enableGuestPricing equals to UPDATED_ENABLE_GUEST_PRICING
        defaultEventDetailsShouldNotBeFound("enableGuestPricing.equals=" + UPDATED_ENABLE_GUEST_PRICING);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEnableGuestPricingIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where enableGuestPricing in DEFAULT_ENABLE_GUEST_PRICING or UPDATED_ENABLE_GUEST_PRICING
        defaultEventDetailsShouldBeFound("enableGuestPricing.in=" + DEFAULT_ENABLE_GUEST_PRICING + "," + UPDATED_ENABLE_GUEST_PRICING);

        // Get all the eventDetailsList where enableGuestPricing equals to UPDATED_ENABLE_GUEST_PRICING
        defaultEventDetailsShouldNotBeFound("enableGuestPricing.in=" + UPDATED_ENABLE_GUEST_PRICING);
    }

    @Test
    @Transactional
    void getAllEventDetailsByEnableGuestPricingIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where enableGuestPricing is not null
        defaultEventDetailsShouldBeFound("enableGuestPricing.specified=true");

        // Get all the eventDetailsList where enableGuestPricing is null
        defaultEventDetailsShouldNotBeFound("enableGuestPricing.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsRegistrationRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isRegistrationRequired equals to DEFAULT_IS_REGISTRATION_REQUIRED
        defaultEventDetailsShouldBeFound("isRegistrationRequired.equals=" + DEFAULT_IS_REGISTRATION_REQUIRED);

        // Get all the eventDetailsList where isRegistrationRequired equals to UPDATED_IS_REGISTRATION_REQUIRED
        defaultEventDetailsShouldNotBeFound("isRegistrationRequired.equals=" + UPDATED_IS_REGISTRATION_REQUIRED);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsRegistrationRequiredIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isRegistrationRequired in DEFAULT_IS_REGISTRATION_REQUIRED or UPDATED_IS_REGISTRATION_REQUIRED
        defaultEventDetailsShouldBeFound(
            "isRegistrationRequired.in=" + DEFAULT_IS_REGISTRATION_REQUIRED + "," + UPDATED_IS_REGISTRATION_REQUIRED
        );

        // Get all the eventDetailsList where isRegistrationRequired equals to UPDATED_IS_REGISTRATION_REQUIRED
        defaultEventDetailsShouldNotBeFound("isRegistrationRequired.in=" + UPDATED_IS_REGISTRATION_REQUIRED);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsRegistrationRequiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isRegistrationRequired is not null
        defaultEventDetailsShouldBeFound("isRegistrationRequired.specified=true");

        // Get all the eventDetailsList where isRegistrationRequired is null
        defaultEventDetailsShouldNotBeFound("isRegistrationRequired.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsSportsEventIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isSportsEvent equals to DEFAULT_IS_SPORTS_EVENT
        defaultEventDetailsShouldBeFound("isSportsEvent.equals=" + DEFAULT_IS_SPORTS_EVENT);

        // Get all the eventDetailsList where isSportsEvent equals to UPDATED_IS_SPORTS_EVENT
        defaultEventDetailsShouldNotBeFound("isSportsEvent.equals=" + UPDATED_IS_SPORTS_EVENT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsSportsEventIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isSportsEvent in DEFAULT_IS_SPORTS_EVENT or UPDATED_IS_SPORTS_EVENT
        defaultEventDetailsShouldBeFound("isSportsEvent.in=" + DEFAULT_IS_SPORTS_EVENT + "," + UPDATED_IS_SPORTS_EVENT);

        // Get all the eventDetailsList where isSportsEvent equals to UPDATED_IS_SPORTS_EVENT
        defaultEventDetailsShouldNotBeFound("isSportsEvent.in=" + UPDATED_IS_SPORTS_EVENT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsSportsEventIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isSportsEvent is not null
        defaultEventDetailsShouldBeFound("isSportsEvent.specified=true");

        // Get all the eventDetailsList where isSportsEvent is null
        defaultEventDetailsShouldNotBeFound("isSportsEvent.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsLiveIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isLive equals to DEFAULT_IS_LIVE
        defaultEventDetailsShouldBeFound("isLive.equals=" + DEFAULT_IS_LIVE);

        // Get all the eventDetailsList where isLive equals to UPDATED_IS_LIVE
        defaultEventDetailsShouldNotBeFound("isLive.equals=" + UPDATED_IS_LIVE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsLiveIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isLive in DEFAULT_IS_LIVE or UPDATED_IS_LIVE
        defaultEventDetailsShouldBeFound("isLive.in=" + DEFAULT_IS_LIVE + "," + UPDATED_IS_LIVE);

        // Get all the eventDetailsList where isLive equals to UPDATED_IS_LIVE
        defaultEventDetailsShouldNotBeFound("isLive.in=" + UPDATED_IS_LIVE);
    }

    @Test
    @Transactional
    void getAllEventDetailsByIsLiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where isLive is not null
        defaultEventDetailsShouldBeFound("isLive.specified=true");

        // Get all the eventDetailsList where isLive is null
        defaultEventDetailsShouldNotBeFound("isLive.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventDetailsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventDetailsList where createdAt equals to UPDATED_CREATED_AT
        defaultEventDetailsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventDetailsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventDetailsList where createdAt equals to UPDATED_CREATED_AT
        defaultEventDetailsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt is not null
        defaultEventDetailsShouldBeFound("createdAt.specified=true");

        // Get all the eventDetailsList where createdAt is null
        defaultEventDetailsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventDetailsShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventDetailsList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventDetailsShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventDetailsShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventDetailsList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventDetailsShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventDetailsShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventDetailsList where createdAt is less than UPDATED_CREATED_AT
        defaultEventDetailsShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventDetailsShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventDetailsList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventDetailsShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventDetailsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventDetailsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventDetailsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventDetailsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventDetailsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventDetailsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt is not null
        defaultEventDetailsShouldBeFound("updatedAt.specified=true");

        // Get all the eventDetailsList where updatedAt is null
        defaultEventDetailsShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventDetailsShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventDetailsList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventDetailsShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventDetailsShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventDetailsList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventDetailsShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventDetailsShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventDetailsList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventDetailsShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventDetailsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        // Get all the eventDetailsList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventDetailsShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventDetailsList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventDetailsShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /*@Test
    @Transactional
    void getAllEventDetailsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventDetailsRepository.saveAndFlush(eventDetails);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        eventDetails.setCreatedBy(createdBy);
        eventDetailsRepository.saveAndFlush(eventDetails);
        Long createdById = createdBy.getId();
        // Get all the eventDetailsList where createdBy equals to createdById
        defaultEventDetailsShouldBeFound("createdById.equals=" + createdById);

        // Get all the eventDetailsList where createdBy equals to (createdById + 1)
        defaultEventDetailsShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }*/

    @Test
    @Transactional
    void getAllEventDetailsByEventTypeIsEqualToSomething() throws Exception {
        EventTypeDetails eventType;
        if (TestUtil.findAll(em, EventTypeDetails.class).isEmpty()) {
            eventDetailsRepository.saveAndFlush(eventDetails);
            eventType = EventTypeDetailsResourceIT.createEntity(em);
        } else {
            eventType = TestUtil.findAll(em, EventTypeDetails.class).get(0);
        }
        em.persist(eventType);
        em.flush();
        eventDetails.setEventType(eventType);
        eventDetailsRepository.saveAndFlush(eventDetails);
        Long eventTypeId = eventType.getId();
        // Get all the eventDetailsList where eventType equals to eventTypeId
        defaultEventDetailsShouldBeFound("eventTypeId.equals=" + eventTypeId);

        // Get all the eventDetailsList where eventType equals to (eventTypeId + 1)
        defaultEventDetailsShouldNotBeFound("eventTypeId.equals=" + (eventTypeId + 1));
    }

    @Test
    @Transactional
    void getAllEventDetailsByDiscountCodesIsEqualToSomething() throws Exception {
        DiscountCode discountCodes;
        if (TestUtil.findAll(em, DiscountCode.class).isEmpty()) {
            eventDetailsRepository.saveAndFlush(eventDetails);
            discountCodes = DiscountCodeResourceIT.createEntity(em);
        } else {
            discountCodes = TestUtil.findAll(em, DiscountCode.class).get(0);
        }
        em.persist(discountCodes);
        em.flush();
        eventDetails.addDiscountCodes(discountCodes);
        eventDetailsRepository.saveAndFlush(eventDetails);
        Long discountCodesId = discountCodes.getId();
        // Get all the eventDetailsList where discountCodes equals to discountCodesId
        defaultEventDetailsShouldBeFound("discountCodesId.equals=" + discountCodesId);

        // Get all the eventDetailsList where discountCodes equals to (discountCodesId + 1)
        defaultEventDetailsShouldNotBeFound("discountCodesId.equals=" + (discountCodesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventDetailsShouldBeFound(String filter) throws Exception {
        restEventDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].directionsToVenue").value(hasItem(DEFAULT_DIRECTIONS_TO_VENUE)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].admissionType").value(hasItem(DEFAULT_ADMISSION_TYPE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].maxGuestsPerAttendee").value(hasItem(DEFAULT_MAX_GUESTS_PER_ATTENDEE)))
            .andExpect(jsonPath("$.[*].allowGuests").value(hasItem(DEFAULT_ALLOW_GUESTS.booleanValue())))
            .andExpect(jsonPath("$.[*].requireGuestApproval").value(hasItem(DEFAULT_REQUIRE_GUEST_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].enableGuestPricing").value(hasItem(DEFAULT_ENABLE_GUEST_PRICING.booleanValue())))
            .andExpect(jsonPath("$.[*].isRegistrationRequired").value(hasItem(DEFAULT_IS_REGISTRATION_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].isSportsEvent").value(hasItem(DEFAULT_IS_SPORTS_EVENT.booleanValue())))
            .andExpect(jsonPath("$.[*].isLive").value(hasItem(DEFAULT_IS_LIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventDetailsShouldNotBeFound(String filter) throws Exception {
        restEventDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventDetails() throws Exception {
        // Get the eventDetails
        restEventDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventDetails() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();

        // Update the eventDetails
        EventDetails updatedEventDetails = eventDetailsRepository.findById(eventDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventDetails are not directly saved in db
        em.detach(updatedEventDetails);
        updatedEventDetails
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .location(UPDATED_LOCATION)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .maxGuestsPerAttendee(UPDATED_MAX_GUESTS_PER_ATTENDEE)
            .allowGuests(UPDATED_ALLOW_GUESTS)
            .requireGuestApproval(UPDATED_REQUIRE_GUEST_APPROVAL)
            .enableGuestPricing(UPDATED_ENABLE_GUEST_PRICING)
            .isRegistrationRequired(UPDATED_IS_REGISTRATION_REQUIRED)
            .isSportsEvent(UPDATED_IS_SPORTS_EVENT)
            .isLive(UPDATED_IS_LIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(updatedEventDetails);

        restEventDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
        EventDetails testEventDetails = eventDetailsList.get(eventDetailsList.size() - 1);
        assertThat(testEventDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventDetails.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventDetails.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testEventDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventDetails.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventDetails.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEventDetails.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEventDetails.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEventDetails.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEventDetails.getDirectionsToVenue()).isEqualTo(UPDATED_DIRECTIONS_TO_VENUE);
        assertThat(testEventDetails.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testEventDetails.getAdmissionType()).isEqualTo(UPDATED_ADMISSION_TYPE);
        assertThat(testEventDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventDetails.getMaxGuestsPerAttendee()).isEqualTo(UPDATED_MAX_GUESTS_PER_ATTENDEE);
        assertThat(testEventDetails.getAllowGuests()).isEqualTo(UPDATED_ALLOW_GUESTS);
        assertThat(testEventDetails.getRequireGuestApproval()).isEqualTo(UPDATED_REQUIRE_GUEST_APPROVAL);
        assertThat(testEventDetails.getEnableGuestPricing()).isEqualTo(UPDATED_ENABLE_GUEST_PRICING);
        assertThat(testEventDetails.getIsRegistrationRequired()).isEqualTo(UPDATED_IS_REGISTRATION_REQUIRED);
        assertThat(testEventDetails.getIsSportsEvent()).isEqualTo(UPDATED_IS_SPORTS_EVENT);
        assertThat(testEventDetails.getIsLive()).isEqualTo(UPDATED_IS_LIVE);
        assertThat(testEventDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventDetails.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();
        eventDetails.setId(longCount.incrementAndGet());

        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();
        eventDetails.setId(longCount.incrementAndGet());

        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();
        eventDetails.setId(longCount.incrementAndGet());

        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventDetailsWithPatch() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();

        // Update the eventDetails using partial update
        EventDetails partialUpdatedEventDetails = new EventDetails();
        partialUpdatedEventDetails.setId(eventDetails.getId());

        partialUpdatedEventDetails
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .allowGuests(UPDATED_ALLOW_GUESTS)
            .isSportsEvent(UPDATED_IS_SPORTS_EVENT)
            .isLive(UPDATED_IS_LIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventDetails))
            )
            .andExpect(status().isOk());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
        EventDetails testEventDetails = eventDetailsList.get(eventDetailsList.size() - 1);
        assertThat(testEventDetails.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventDetails.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventDetails.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testEventDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventDetails.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventDetails.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEventDetails.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEventDetails.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testEventDetails.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testEventDetails.getDirectionsToVenue()).isEqualTo(DEFAULT_DIRECTIONS_TO_VENUE);
        assertThat(testEventDetails.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testEventDetails.getAdmissionType()).isEqualTo(DEFAULT_ADMISSION_TYPE);
        assertThat(testEventDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventDetails.getMaxGuestsPerAttendee()).isEqualTo(DEFAULT_MAX_GUESTS_PER_ATTENDEE);
        assertThat(testEventDetails.getAllowGuests()).isEqualTo(UPDATED_ALLOW_GUESTS);
        assertThat(testEventDetails.getRequireGuestApproval()).isEqualTo(DEFAULT_REQUIRE_GUEST_APPROVAL);
        assertThat(testEventDetails.getEnableGuestPricing()).isEqualTo(DEFAULT_ENABLE_GUEST_PRICING);
        assertThat(testEventDetails.getIsRegistrationRequired()).isEqualTo(DEFAULT_IS_REGISTRATION_REQUIRED);
        assertThat(testEventDetails.getIsSportsEvent()).isEqualTo(UPDATED_IS_SPORTS_EVENT);
        assertThat(testEventDetails.getIsLive()).isEqualTo(UPDATED_IS_LIVE);
        assertThat(testEventDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventDetails.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventDetailsWithPatch() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();

        // Update the eventDetails using partial update
        EventDetails partialUpdatedEventDetails = new EventDetails();
        partialUpdatedEventDetails.setId(eventDetails.getId());

        partialUpdatedEventDetails
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .caption(UPDATED_CAPTION)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .location(UPDATED_LOCATION)
            .directionsToVenue(UPDATED_DIRECTIONS_TO_VENUE)
            .capacity(UPDATED_CAPACITY)
            .admissionType(UPDATED_ADMISSION_TYPE)
            .isActive(UPDATED_IS_ACTIVE)
            .maxGuestsPerAttendee(UPDATED_MAX_GUESTS_PER_ATTENDEE)
            .allowGuests(UPDATED_ALLOW_GUESTS)
            .requireGuestApproval(UPDATED_REQUIRE_GUEST_APPROVAL)
            .enableGuestPricing(UPDATED_ENABLE_GUEST_PRICING)
            .isRegistrationRequired(UPDATED_IS_REGISTRATION_REQUIRED)
            .isSportsEvent(UPDATED_IS_SPORTS_EVENT)
            .isLive(UPDATED_IS_LIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventDetails))
            )
            .andExpect(status().isOk());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
        EventDetails testEventDetails = eventDetailsList.get(eventDetailsList.size() - 1);
        assertThat(testEventDetails.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventDetails.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventDetails.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testEventDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventDetails.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventDetails.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEventDetails.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testEventDetails.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testEventDetails.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testEventDetails.getDirectionsToVenue()).isEqualTo(UPDATED_DIRECTIONS_TO_VENUE);
        assertThat(testEventDetails.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testEventDetails.getAdmissionType()).isEqualTo(UPDATED_ADMISSION_TYPE);
        assertThat(testEventDetails.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventDetails.getMaxGuestsPerAttendee()).isEqualTo(UPDATED_MAX_GUESTS_PER_ATTENDEE);
        assertThat(testEventDetails.getAllowGuests()).isEqualTo(UPDATED_ALLOW_GUESTS);
        assertThat(testEventDetails.getRequireGuestApproval()).isEqualTo(UPDATED_REQUIRE_GUEST_APPROVAL);
        assertThat(testEventDetails.getEnableGuestPricing()).isEqualTo(UPDATED_ENABLE_GUEST_PRICING);
        assertThat(testEventDetails.getIsRegistrationRequired()).isEqualTo(UPDATED_IS_REGISTRATION_REQUIRED);
        assertThat(testEventDetails.getIsSportsEvent()).isEqualTo(UPDATED_IS_SPORTS_EVENT);
        assertThat(testEventDetails.getIsLive()).isEqualTo(UPDATED_IS_LIVE);
        assertThat(testEventDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventDetails.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();
        eventDetails.setId(longCount.incrementAndGet());

        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();
        eventDetails.setId(longCount.incrementAndGet());

        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventDetails() throws Exception {
        int databaseSizeBeforeUpdate = eventDetailsRepository.findAll().size();
        eventDetails.setId(longCount.incrementAndGet());

        // Create the EventDetails
        EventDetailsDTO eventDetailsDTO = eventDetailsMapper.toDto(eventDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventDetails in the database
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventDetails() throws Exception {
        // Initialize the database
        eventDetailsRepository.saveAndFlush(eventDetails);

        int databaseSizeBeforeDelete = eventDetailsRepository.findAll().size();

        // Delete the eventDetails
        restEventDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventDetails> eventDetailsList = eventDetailsRepository.findAll();
        assertThat(eventDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
