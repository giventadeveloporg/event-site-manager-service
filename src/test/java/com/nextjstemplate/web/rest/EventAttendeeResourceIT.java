package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventAttendeeRepository;
import com.nextjstemplate.service.dto.EventAttendeeDTO;
import com.nextjstemplate.service.mapper.EventAttendeeMapper;
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
 * Integration tests for the {@link EventAttendeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventAttendeeResourceIT {
    /*  private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REGISTRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REGISTRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CONFIRMATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONFIRMATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CONFIRMATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CANCELLATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CANCELLATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CANCELLATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_CANCELLATION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CANCELLATION_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ATTENDEE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ATTENDEE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIAL_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUIREMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CHECK_IN_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_IN_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CHECK_IN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHECK_IN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CHECK_IN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-attendees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventAttendeeRepository eventAttendeeRepository;

    @Autowired
    private EventAttendeeMapper eventAttendeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventAttendeeMockMvc;

    private EventAttendee eventAttendee;

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventAttendee createEntity(EntityManager em) {
        EventAttendee eventAttendee = new EventAttendee()
            .tenantId(DEFAULT_TENANT_ID)
            .registrationStatus(DEFAULT_REGISTRATION_STATUS)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .confirmationDate(DEFAULT_CONFIRMATION_DATE)
            .cancellationDate(DEFAULT_CANCELLATION_DATE)
            .cancellationReason(DEFAULT_CANCELLATION_REASON)
            .attendeeType(DEFAULT_ATTENDEE_TYPE)
            .specialRequirements(DEFAULT_SPECIAL_REQUIREMENTS)
            .emergencyContactName(DEFAULT_EMERGENCY_CONTACT_NAME)
            .emergencyContactPhone(DEFAULT_EMERGENCY_CONTACT_PHONE)
            .checkInStatus(DEFAULT_CHECK_IN_STATUS)
            .checkInTime(DEFAULT_CHECK_IN_TIME)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        EventDetails eventDetails;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventDetails = EventDetailsResourceIT.createEntity(em);
            em.persist(eventDetails);
            em.flush();
        } else {
            eventDetails = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        eventAttendee.setEvent(eventDetails);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        eventAttendee.setAttendee(userProfile);
        return eventAttendee;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventAttendee createUpdatedEntity(EntityManager em) {
        EventAttendee eventAttendee = new EventAttendee()
            .tenantId(UPDATED_TENANT_ID)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .cancellationDate(UPDATED_CANCELLATION_DATE)
            .cancellationReason(UPDATED_CANCELLATION_REASON)
            .attendeeType(UPDATED_ATTENDEE_TYPE)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .emergencyContactName(UPDATED_EMERGENCY_CONTACT_NAME)
            .emergencyContactPhone(UPDATED_EMERGENCY_CONTACT_PHONE)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        EventDetails eventDetails;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventDetails = EventDetailsResourceIT.createUpdatedEntity(em);
            em.persist(eventDetails);
            em.flush();
        } else {
            eventDetails = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        eventAttendee.setEvent(eventDetails);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        eventAttendee.setAttendee(userProfile);
        return eventAttendee;
    }

    @BeforeEach
    public void initTest() {
        eventAttendee = createEntity(em);
    }

    @Test
    @Transactional
    void createEventAttendee() throws Exception {
        int databaseSizeBeforeCreate = eventAttendeeRepository.findAll().size();
        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);
        restEventAttendeeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeCreate + 1);
        EventAttendee testEventAttendee = eventAttendeeList.get(eventAttendeeList.size() - 1);
        assertThat(testEventAttendee.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAttendee.getRegistrationStatus()).isEqualTo(DEFAULT_REGISTRATION_STATUS);
        assertThat(testEventAttendee.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testEventAttendee.getConfirmationDate()).isEqualTo(DEFAULT_CONFIRMATION_DATE);
        assertThat(testEventAttendee.getCancellationDate()).isEqualTo(DEFAULT_CANCELLATION_DATE);
        assertThat(testEventAttendee.getCancellationReason()).isEqualTo(DEFAULT_CANCELLATION_REASON);
        assertThat(testEventAttendee.getAttendeeType()).isEqualTo(DEFAULT_ATTENDEE_TYPE);
        assertThat(testEventAttendee.getSpecialRequirements()).isEqualTo(DEFAULT_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendee.getEmergencyContactName()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_NAME);
        assertThat(testEventAttendee.getEmergencyContactPhone()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_PHONE);
        assertThat(testEventAttendee.getCheckInStatus()).isEqualTo(DEFAULT_CHECK_IN_STATUS);
        assertThat(testEventAttendee.getCheckInTime()).isEqualTo(DEFAULT_CHECK_IN_TIME);
        assertThat(testEventAttendee.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testEventAttendee.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventAttendee.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventAttendeeWithExistingId() throws Exception {
        // Create the EventAttendee with an existing ID
        eventAttendee.setId(1L);
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        int databaseSizeBeforeCreate = eventAttendeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventAttendeeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRegistrationStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeRepository.findAll().size();
        // set the field null
        eventAttendee.setRegistrationStatus(null);

        // Create the EventAttendee, which fails.
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        restEventAttendeeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegistrationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeRepository.findAll().size();
        // set the field null
        eventAttendee.setRegistrationDate(null);

        // Create the EventAttendee, which fails.
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        restEventAttendeeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeRepository.findAll().size();
        // set the field null
        eventAttendee.setCreatedAt(null);

        // Create the EventAttendee, which fails.
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        restEventAttendeeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAttendeeRepository.findAll().size();
        // set the field null
        eventAttendee.setUpdatedAt(null);

        // Create the EventAttendee, which fails.
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        restEventAttendeeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventAttendees() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList
        restEventAttendeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAttendee.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].registrationStatus").value(hasItem(DEFAULT_REGISTRATION_STATUS)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(sameInstant(DEFAULT_REGISTRATION_DATE))))
            .andExpect(jsonPath("$.[*].confirmationDate").value(hasItem(sameInstant(DEFAULT_CONFIRMATION_DATE))))
            .andExpect(jsonPath("$.[*].cancellationDate").value(hasItem(sameInstant(DEFAULT_CANCELLATION_DATE))))
            .andExpect(jsonPath("$.[*].cancellationReason").value(hasItem(DEFAULT_CANCELLATION_REASON)))
            .andExpect(jsonPath("$.[*].attendeeType").value(hasItem(DEFAULT_ATTENDEE_TYPE)))
            .andExpect(jsonPath("$.[*].specialRequirements").value(hasItem(DEFAULT_SPECIAL_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].emergencyContactName").value(hasItem(DEFAULT_EMERGENCY_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].emergencyContactPhone").value(hasItem(DEFAULT_EMERGENCY_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].checkInStatus").value(hasItem(DEFAULT_CHECK_IN_STATUS)))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(sameInstant(DEFAULT_CHECK_IN_TIME))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventAttendee() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get the eventAttendee
        restEventAttendeeMockMvc
            .perform(get(ENTITY_API_URL_ID, eventAttendee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventAttendee.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.registrationStatus").value(DEFAULT_REGISTRATION_STATUS))
            .andExpect(jsonPath("$.registrationDate").value(sameInstant(DEFAULT_REGISTRATION_DATE)))
            .andExpect(jsonPath("$.confirmationDate").value(sameInstant(DEFAULT_CONFIRMATION_DATE)))
            .andExpect(jsonPath("$.cancellationDate").value(sameInstant(DEFAULT_CANCELLATION_DATE)))
            .andExpect(jsonPath("$.cancellationReason").value(DEFAULT_CANCELLATION_REASON))
            .andExpect(jsonPath("$.attendeeType").value(DEFAULT_ATTENDEE_TYPE))
            .andExpect(jsonPath("$.specialRequirements").value(DEFAULT_SPECIAL_REQUIREMENTS))
            .andExpect(jsonPath("$.emergencyContactName").value(DEFAULT_EMERGENCY_CONTACT_NAME))
            .andExpect(jsonPath("$.emergencyContactPhone").value(DEFAULT_EMERGENCY_CONTACT_PHONE))
            .andExpect(jsonPath("$.checkInStatus").value(DEFAULT_CHECK_IN_STATUS))
            .andExpect(jsonPath("$.checkInTime").value(sameInstant(DEFAULT_CHECK_IN_TIME)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventAttendeesByIdFiltering() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        Long id = eventAttendee.getId();

        defaultEventAttendeeShouldBeFound("id.equals=" + id);
        defaultEventAttendeeShouldNotBeFound("id.notEquals=" + id);

        defaultEventAttendeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventAttendeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEventAttendeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventAttendeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventAttendeeShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventAttendeeList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAttendeeShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventAttendeeShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventAttendeeList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAttendeeShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where tenantId is not null
        defaultEventAttendeeShouldBeFound("tenantId.specified=true");

        // Get all the eventAttendeeList where tenantId is null
        defaultEventAttendeeShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where tenantId contains DEFAULT_TENANT_ID
        defaultEventAttendeeShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventAttendeeList where tenantId contains UPDATED_TENANT_ID
        defaultEventAttendeeShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventAttendeeShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventAttendeeList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventAttendeeShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationStatus equals to DEFAULT_REGISTRATION_STATUS
        defaultEventAttendeeShouldBeFound("registrationStatus.equals=" + DEFAULT_REGISTRATION_STATUS);

        // Get all the eventAttendeeList where registrationStatus equals to UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeShouldNotBeFound("registrationStatus.equals=" + UPDATED_REGISTRATION_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationStatus in DEFAULT_REGISTRATION_STATUS or UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeShouldBeFound("registrationStatus.in=" + DEFAULT_REGISTRATION_STATUS + "," + UPDATED_REGISTRATION_STATUS);

        // Get all the eventAttendeeList where registrationStatus equals to UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeShouldNotBeFound("registrationStatus.in=" + UPDATED_REGISTRATION_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationStatus is not null
        defaultEventAttendeeShouldBeFound("registrationStatus.specified=true");

        // Get all the eventAttendeeList where registrationStatus is null
        defaultEventAttendeeShouldNotBeFound("registrationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationStatusContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationStatus contains DEFAULT_REGISTRATION_STATUS
        defaultEventAttendeeShouldBeFound("registrationStatus.contains=" + DEFAULT_REGISTRATION_STATUS);

        // Get all the eventAttendeeList where registrationStatus contains UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeShouldNotBeFound("registrationStatus.contains=" + UPDATED_REGISTRATION_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationStatus does not contain DEFAULT_REGISTRATION_STATUS
        defaultEventAttendeeShouldNotBeFound("registrationStatus.doesNotContain=" + DEFAULT_REGISTRATION_STATUS);

        // Get all the eventAttendeeList where registrationStatus does not contain UPDATED_REGISTRATION_STATUS
        defaultEventAttendeeShouldBeFound("registrationStatus.doesNotContain=" + UPDATED_REGISTRATION_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultEventAttendeeShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the eventAttendeeList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultEventAttendeeShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultEventAttendeeShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the eventAttendeeList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultEventAttendeeShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate is not null
        defaultEventAttendeeShouldBeFound("registrationDate.specified=true");

        // Get all the eventAttendeeList where registrationDate is null
        defaultEventAttendeeShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultEventAttendeeShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the eventAttendeeList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultEventAttendeeShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultEventAttendeeShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the eventAttendeeList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultEventAttendeeShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultEventAttendeeShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the eventAttendeeList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultEventAttendeeShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultEventAttendeeShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the eventAttendeeList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultEventAttendeeShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate equals to DEFAULT_CONFIRMATION_DATE
        defaultEventAttendeeShouldBeFound("confirmationDate.equals=" + DEFAULT_CONFIRMATION_DATE);

        // Get all the eventAttendeeList where confirmationDate equals to UPDATED_CONFIRMATION_DATE
        defaultEventAttendeeShouldNotBeFound("confirmationDate.equals=" + UPDATED_CONFIRMATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate in DEFAULT_CONFIRMATION_DATE or UPDATED_CONFIRMATION_DATE
        defaultEventAttendeeShouldBeFound("confirmationDate.in=" + DEFAULT_CONFIRMATION_DATE + "," + UPDATED_CONFIRMATION_DATE);

        // Get all the eventAttendeeList where confirmationDate equals to UPDATED_CONFIRMATION_DATE
        defaultEventAttendeeShouldNotBeFound("confirmationDate.in=" + UPDATED_CONFIRMATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate is not null
        defaultEventAttendeeShouldBeFound("confirmationDate.specified=true");

        // Get all the eventAttendeeList where confirmationDate is null
        defaultEventAttendeeShouldNotBeFound("confirmationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate is greater than or equal to DEFAULT_CONFIRMATION_DATE
        defaultEventAttendeeShouldBeFound("confirmationDate.greaterThanOrEqual=" + DEFAULT_CONFIRMATION_DATE);

        // Get all the eventAttendeeList where confirmationDate is greater than or equal to UPDATED_CONFIRMATION_DATE
        defaultEventAttendeeShouldNotBeFound("confirmationDate.greaterThanOrEqual=" + UPDATED_CONFIRMATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate is less than or equal to DEFAULT_CONFIRMATION_DATE
        defaultEventAttendeeShouldBeFound("confirmationDate.lessThanOrEqual=" + DEFAULT_CONFIRMATION_DATE);

        // Get all the eventAttendeeList where confirmationDate is less than or equal to SMALLER_CONFIRMATION_DATE
        defaultEventAttendeeShouldNotBeFound("confirmationDate.lessThanOrEqual=" + SMALLER_CONFIRMATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate is less than DEFAULT_CONFIRMATION_DATE
        defaultEventAttendeeShouldNotBeFound("confirmationDate.lessThan=" + DEFAULT_CONFIRMATION_DATE);

        // Get all the eventAttendeeList where confirmationDate is less than UPDATED_CONFIRMATION_DATE
        defaultEventAttendeeShouldBeFound("confirmationDate.lessThan=" + UPDATED_CONFIRMATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByConfirmationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where confirmationDate is greater than DEFAULT_CONFIRMATION_DATE
        defaultEventAttendeeShouldNotBeFound("confirmationDate.greaterThan=" + DEFAULT_CONFIRMATION_DATE);

        // Get all the eventAttendeeList where confirmationDate is greater than SMALLER_CONFIRMATION_DATE
        defaultEventAttendeeShouldBeFound("confirmationDate.greaterThan=" + SMALLER_CONFIRMATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate equals to DEFAULT_CANCELLATION_DATE
        defaultEventAttendeeShouldBeFound("cancellationDate.equals=" + DEFAULT_CANCELLATION_DATE);

        // Get all the eventAttendeeList where cancellationDate equals to UPDATED_CANCELLATION_DATE
        defaultEventAttendeeShouldNotBeFound("cancellationDate.equals=" + UPDATED_CANCELLATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate in DEFAULT_CANCELLATION_DATE or UPDATED_CANCELLATION_DATE
        defaultEventAttendeeShouldBeFound("cancellationDate.in=" + DEFAULT_CANCELLATION_DATE + "," + UPDATED_CANCELLATION_DATE);

        // Get all the eventAttendeeList where cancellationDate equals to UPDATED_CANCELLATION_DATE
        defaultEventAttendeeShouldNotBeFound("cancellationDate.in=" + UPDATED_CANCELLATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate is not null
        defaultEventAttendeeShouldBeFound("cancellationDate.specified=true");

        // Get all the eventAttendeeList where cancellationDate is null
        defaultEventAttendeeShouldNotBeFound("cancellationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate is greater than or equal to DEFAULT_CANCELLATION_DATE
        defaultEventAttendeeShouldBeFound("cancellationDate.greaterThanOrEqual=" + DEFAULT_CANCELLATION_DATE);

        // Get all the eventAttendeeList where cancellationDate is greater than or equal to UPDATED_CANCELLATION_DATE
        defaultEventAttendeeShouldNotBeFound("cancellationDate.greaterThanOrEqual=" + UPDATED_CANCELLATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate is less than or equal to DEFAULT_CANCELLATION_DATE
        defaultEventAttendeeShouldBeFound("cancellationDate.lessThanOrEqual=" + DEFAULT_CANCELLATION_DATE);

        // Get all the eventAttendeeList where cancellationDate is less than or equal to SMALLER_CANCELLATION_DATE
        defaultEventAttendeeShouldNotBeFound("cancellationDate.lessThanOrEqual=" + SMALLER_CANCELLATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate is less than DEFAULT_CANCELLATION_DATE
        defaultEventAttendeeShouldNotBeFound("cancellationDate.lessThan=" + DEFAULT_CANCELLATION_DATE);

        // Get all the eventAttendeeList where cancellationDate is less than UPDATED_CANCELLATION_DATE
        defaultEventAttendeeShouldBeFound("cancellationDate.lessThan=" + UPDATED_CANCELLATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationDate is greater than DEFAULT_CANCELLATION_DATE
        defaultEventAttendeeShouldNotBeFound("cancellationDate.greaterThan=" + DEFAULT_CANCELLATION_DATE);

        // Get all the eventAttendeeList where cancellationDate is greater than SMALLER_CANCELLATION_DATE
        defaultEventAttendeeShouldBeFound("cancellationDate.greaterThan=" + SMALLER_CANCELLATION_DATE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationReason equals to DEFAULT_CANCELLATION_REASON
        defaultEventAttendeeShouldBeFound("cancellationReason.equals=" + DEFAULT_CANCELLATION_REASON);

        // Get all the eventAttendeeList where cancellationReason equals to UPDATED_CANCELLATION_REASON
        defaultEventAttendeeShouldNotBeFound("cancellationReason.equals=" + UPDATED_CANCELLATION_REASON);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationReasonIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationReason in DEFAULT_CANCELLATION_REASON or UPDATED_CANCELLATION_REASON
        defaultEventAttendeeShouldBeFound("cancellationReason.in=" + DEFAULT_CANCELLATION_REASON + "," + UPDATED_CANCELLATION_REASON);

        // Get all the eventAttendeeList where cancellationReason equals to UPDATED_CANCELLATION_REASON
        defaultEventAttendeeShouldNotBeFound("cancellationReason.in=" + UPDATED_CANCELLATION_REASON);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationReason is not null
        defaultEventAttendeeShouldBeFound("cancellationReason.specified=true");

        // Get all the eventAttendeeList where cancellationReason is null
        defaultEventAttendeeShouldNotBeFound("cancellationReason.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationReasonContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationReason contains DEFAULT_CANCELLATION_REASON
        defaultEventAttendeeShouldBeFound("cancellationReason.contains=" + DEFAULT_CANCELLATION_REASON);

        // Get all the eventAttendeeList where cancellationReason contains UPDATED_CANCELLATION_REASON
        defaultEventAttendeeShouldNotBeFound("cancellationReason.contains=" + UPDATED_CANCELLATION_REASON);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCancellationReasonNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where cancellationReason does not contain DEFAULT_CANCELLATION_REASON
        defaultEventAttendeeShouldNotBeFound("cancellationReason.doesNotContain=" + DEFAULT_CANCELLATION_REASON);

        // Get all the eventAttendeeList where cancellationReason does not contain UPDATED_CANCELLATION_REASON
        defaultEventAttendeeShouldBeFound("cancellationReason.doesNotContain=" + UPDATED_CANCELLATION_REASON);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByAttendeeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where attendeeType equals to DEFAULT_ATTENDEE_TYPE
        defaultEventAttendeeShouldBeFound("attendeeType.equals=" + DEFAULT_ATTENDEE_TYPE);

        // Get all the eventAttendeeList where attendeeType equals to UPDATED_ATTENDEE_TYPE
        defaultEventAttendeeShouldNotBeFound("attendeeType.equals=" + UPDATED_ATTENDEE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByAttendeeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where attendeeType in DEFAULT_ATTENDEE_TYPE or UPDATED_ATTENDEE_TYPE
        defaultEventAttendeeShouldBeFound("attendeeType.in=" + DEFAULT_ATTENDEE_TYPE + "," + UPDATED_ATTENDEE_TYPE);

        // Get all the eventAttendeeList where attendeeType equals to UPDATED_ATTENDEE_TYPE
        defaultEventAttendeeShouldNotBeFound("attendeeType.in=" + UPDATED_ATTENDEE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByAttendeeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where attendeeType is not null
        defaultEventAttendeeShouldBeFound("attendeeType.specified=true");

        // Get all the eventAttendeeList where attendeeType is null
        defaultEventAttendeeShouldNotBeFound("attendeeType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByAttendeeTypeContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where attendeeType contains DEFAULT_ATTENDEE_TYPE
        defaultEventAttendeeShouldBeFound("attendeeType.contains=" + DEFAULT_ATTENDEE_TYPE);

        // Get all the eventAttendeeList where attendeeType contains UPDATED_ATTENDEE_TYPE
        defaultEventAttendeeShouldNotBeFound("attendeeType.contains=" + UPDATED_ATTENDEE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByAttendeeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where attendeeType does not contain DEFAULT_ATTENDEE_TYPE
        defaultEventAttendeeShouldNotBeFound("attendeeType.doesNotContain=" + DEFAULT_ATTENDEE_TYPE);

        // Get all the eventAttendeeList where attendeeType does not contain UPDATED_ATTENDEE_TYPE
        defaultEventAttendeeShouldBeFound("attendeeType.doesNotContain=" + UPDATED_ATTENDEE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesBySpecialRequirementsIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where specialRequirements equals to DEFAULT_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldBeFound("specialRequirements.equals=" + DEFAULT_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeList where specialRequirements equals to UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldNotBeFound("specialRequirements.equals=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesBySpecialRequirementsIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where specialRequirements in DEFAULT_SPECIAL_REQUIREMENTS or UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldBeFound("specialRequirements.in=" + DEFAULT_SPECIAL_REQUIREMENTS + "," + UPDATED_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeList where specialRequirements equals to UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldNotBeFound("specialRequirements.in=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesBySpecialRequirementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where specialRequirements is not null
        defaultEventAttendeeShouldBeFound("specialRequirements.specified=true");

        // Get all the eventAttendeeList where specialRequirements is null
        defaultEventAttendeeShouldNotBeFound("specialRequirements.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesBySpecialRequirementsContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where specialRequirements contains DEFAULT_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldBeFound("specialRequirements.contains=" + DEFAULT_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeList where specialRequirements contains UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldNotBeFound("specialRequirements.contains=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesBySpecialRequirementsNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where specialRequirements does not contain DEFAULT_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldNotBeFound("specialRequirements.doesNotContain=" + DEFAULT_SPECIAL_REQUIREMENTS);

        // Get all the eventAttendeeList where specialRequirements does not contain UPDATED_SPECIAL_REQUIREMENTS
        defaultEventAttendeeShouldBeFound("specialRequirements.doesNotContain=" + UPDATED_SPECIAL_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactName equals to DEFAULT_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldBeFound("emergencyContactName.equals=" + DEFAULT_EMERGENCY_CONTACT_NAME);

        // Get all the eventAttendeeList where emergencyContactName equals to UPDATED_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldNotBeFound("emergencyContactName.equals=" + UPDATED_EMERGENCY_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactName in DEFAULT_EMERGENCY_CONTACT_NAME or UPDATED_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldBeFound(
            "emergencyContactName.in=" + DEFAULT_EMERGENCY_CONTACT_NAME + "," + UPDATED_EMERGENCY_CONTACT_NAME
        );

        // Get all the eventAttendeeList where emergencyContactName equals to UPDATED_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldNotBeFound("emergencyContactName.in=" + UPDATED_EMERGENCY_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactName is not null
        defaultEventAttendeeShouldBeFound("emergencyContactName.specified=true");

        // Get all the eventAttendeeList where emergencyContactName is null
        defaultEventAttendeeShouldNotBeFound("emergencyContactName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactNameContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactName contains DEFAULT_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldBeFound("emergencyContactName.contains=" + DEFAULT_EMERGENCY_CONTACT_NAME);

        // Get all the eventAttendeeList where emergencyContactName contains UPDATED_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldNotBeFound("emergencyContactName.contains=" + UPDATED_EMERGENCY_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactName does not contain DEFAULT_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldNotBeFound("emergencyContactName.doesNotContain=" + DEFAULT_EMERGENCY_CONTACT_NAME);

        // Get all the eventAttendeeList where emergencyContactName does not contain UPDATED_EMERGENCY_CONTACT_NAME
        defaultEventAttendeeShouldBeFound("emergencyContactName.doesNotContain=" + UPDATED_EMERGENCY_CONTACT_NAME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactPhone equals to DEFAULT_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldBeFound("emergencyContactPhone.equals=" + DEFAULT_EMERGENCY_CONTACT_PHONE);

        // Get all the eventAttendeeList where emergencyContactPhone equals to UPDATED_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldNotBeFound("emergencyContactPhone.equals=" + UPDATED_EMERGENCY_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactPhone in DEFAULT_EMERGENCY_CONTACT_PHONE or UPDATED_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldBeFound(
            "emergencyContactPhone.in=" + DEFAULT_EMERGENCY_CONTACT_PHONE + "," + UPDATED_EMERGENCY_CONTACT_PHONE
        );

        // Get all the eventAttendeeList where emergencyContactPhone equals to UPDATED_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldNotBeFound("emergencyContactPhone.in=" + UPDATED_EMERGENCY_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactPhone is not null
        defaultEventAttendeeShouldBeFound("emergencyContactPhone.specified=true");

        // Get all the eventAttendeeList where emergencyContactPhone is null
        defaultEventAttendeeShouldNotBeFound("emergencyContactPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactPhoneContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactPhone contains DEFAULT_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldBeFound("emergencyContactPhone.contains=" + DEFAULT_EMERGENCY_CONTACT_PHONE);

        // Get all the eventAttendeeList where emergencyContactPhone contains UPDATED_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldNotBeFound("emergencyContactPhone.contains=" + UPDATED_EMERGENCY_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEmergencyContactPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where emergencyContactPhone does not contain DEFAULT_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldNotBeFound("emergencyContactPhone.doesNotContain=" + DEFAULT_EMERGENCY_CONTACT_PHONE);

        // Get all the eventAttendeeList where emergencyContactPhone does not contain UPDATED_EMERGENCY_CONTACT_PHONE
        defaultEventAttendeeShouldBeFound("emergencyContactPhone.doesNotContain=" + UPDATED_EMERGENCY_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInStatus equals to DEFAULT_CHECK_IN_STATUS
        defaultEventAttendeeShouldBeFound("checkInStatus.equals=" + DEFAULT_CHECK_IN_STATUS);

        // Get all the eventAttendeeList where checkInStatus equals to UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeShouldNotBeFound("checkInStatus.equals=" + UPDATED_CHECK_IN_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInStatus in DEFAULT_CHECK_IN_STATUS or UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeShouldBeFound("checkInStatus.in=" + DEFAULT_CHECK_IN_STATUS + "," + UPDATED_CHECK_IN_STATUS);

        // Get all the eventAttendeeList where checkInStatus equals to UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeShouldNotBeFound("checkInStatus.in=" + UPDATED_CHECK_IN_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInStatus is not null
        defaultEventAttendeeShouldBeFound("checkInStatus.specified=true");

        // Get all the eventAttendeeList where checkInStatus is null
        defaultEventAttendeeShouldNotBeFound("checkInStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInStatusContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInStatus contains DEFAULT_CHECK_IN_STATUS
        defaultEventAttendeeShouldBeFound("checkInStatus.contains=" + DEFAULT_CHECK_IN_STATUS);

        // Get all the eventAttendeeList where checkInStatus contains UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeShouldNotBeFound("checkInStatus.contains=" + UPDATED_CHECK_IN_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInStatus does not contain DEFAULT_CHECK_IN_STATUS
        defaultEventAttendeeShouldNotBeFound("checkInStatus.doesNotContain=" + DEFAULT_CHECK_IN_STATUS);

        // Get all the eventAttendeeList where checkInStatus does not contain UPDATED_CHECK_IN_STATUS
        defaultEventAttendeeShouldBeFound("checkInStatus.doesNotContain=" + UPDATED_CHECK_IN_STATUS);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime equals to DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeShouldBeFound("checkInTime.equals=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeList where checkInTime equals to UPDATED_CHECK_IN_TIME
        defaultEventAttendeeShouldNotBeFound("checkInTime.equals=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime in DEFAULT_CHECK_IN_TIME or UPDATED_CHECK_IN_TIME
        defaultEventAttendeeShouldBeFound("checkInTime.in=" + DEFAULT_CHECK_IN_TIME + "," + UPDATED_CHECK_IN_TIME);

        // Get all the eventAttendeeList where checkInTime equals to UPDATED_CHECK_IN_TIME
        defaultEventAttendeeShouldNotBeFound("checkInTime.in=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime is not null
        defaultEventAttendeeShouldBeFound("checkInTime.specified=true");

        // Get all the eventAttendeeList where checkInTime is null
        defaultEventAttendeeShouldNotBeFound("checkInTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime is greater than or equal to DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeShouldBeFound("checkInTime.greaterThanOrEqual=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeList where checkInTime is greater than or equal to UPDATED_CHECK_IN_TIME
        defaultEventAttendeeShouldNotBeFound("checkInTime.greaterThanOrEqual=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime is less than or equal to DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeShouldBeFound("checkInTime.lessThanOrEqual=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeList where checkInTime is less than or equal to SMALLER_CHECK_IN_TIME
        defaultEventAttendeeShouldNotBeFound("checkInTime.lessThanOrEqual=" + SMALLER_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime is less than DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeShouldNotBeFound("checkInTime.lessThan=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeList where checkInTime is less than UPDATED_CHECK_IN_TIME
        defaultEventAttendeeShouldBeFound("checkInTime.lessThan=" + UPDATED_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCheckInTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where checkInTime is greater than DEFAULT_CHECK_IN_TIME
        defaultEventAttendeeShouldNotBeFound("checkInTime.greaterThan=" + DEFAULT_CHECK_IN_TIME);

        // Get all the eventAttendeeList where checkInTime is greater than SMALLER_CHECK_IN_TIME
        defaultEventAttendeeShouldBeFound("checkInTime.greaterThan=" + SMALLER_CHECK_IN_TIME);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where notes equals to DEFAULT_NOTES
        defaultEventAttendeeShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the eventAttendeeList where notes equals to UPDATED_NOTES
        defaultEventAttendeeShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultEventAttendeeShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the eventAttendeeList where notes equals to UPDATED_NOTES
        defaultEventAttendeeShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where notes is not null
        defaultEventAttendeeShouldBeFound("notes.specified=true");

        // Get all the eventAttendeeList where notes is null
        defaultEventAttendeeShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByNotesContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where notes contains DEFAULT_NOTES
        defaultEventAttendeeShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the eventAttendeeList where notes contains UPDATED_NOTES
        defaultEventAttendeeShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where notes does not contain DEFAULT_NOTES
        defaultEventAttendeeShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the eventAttendeeList where notes does not contain UPDATED_NOTES
        defaultEventAttendeeShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventAttendeeShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAttendeeShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventAttendeeShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventAttendeeList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAttendeeShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt is not null
        defaultEventAttendeeShouldBeFound("createdAt.specified=true");

        // Get all the eventAttendeeList where createdAt is null
        defaultEventAttendeeShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventAttendeeShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventAttendeeShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventAttendeeShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventAttendeeShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventAttendeeShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeList where createdAt is less than UPDATED_CREATED_AT
        defaultEventAttendeeShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventAttendeeShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAttendeeList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventAttendeeShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventAttendeeShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventAttendeeShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventAttendeeShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventAttendeeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventAttendeeShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt is not null
        defaultEventAttendeeShouldBeFound("updatedAt.specified=true");

        // Get all the eventAttendeeList where updatedAt is null
        defaultEventAttendeeShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventAttendeeShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventAttendeeShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventAttendeeShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventAttendeeShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventAttendeeShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventAttendeeShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        // Get all the eventAttendeeList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventAttendeeShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventAttendeeList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventAttendeeShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAttendeesByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventAttendeeRepository.saveAndFlush(eventAttendee);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventAttendee.setEvent(event);
        eventAttendeeRepository.saveAndFlush(eventAttendee);
        Long eventId = event.getId();
        // Get all the eventAttendeeList where event equals to eventId
        defaultEventAttendeeShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventAttendeeList where event equals to (eventId + 1)
        defaultEventAttendeeShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    *//*@Test
    @Transactional
    void getAllEventAttendeesByAttendeeIsEqualToSomething() throws Exception {
        UserProfile attendee;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventAttendeeRepository.saveAndFlush(eventAttendee);
            attendee = UserProfileResourceIT.createEntity(em);
        } else {
            attendee = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(attendee);
        em.flush();
        eventAttendee.setAttendee(attendee);
        eventAttendeeRepository.saveAndFlush(eventAttendee);
        Long attendeeId = attendee.getId();
        // Get all the eventAttendeeList where attendee equals to attendeeId
        defaultEventAttendeeShouldBeFound("attendeeId.equals=" + attendeeId);

        // Get all the eventAttendeeList where attendee equals to (attendeeId + 1)
        defaultEventAttendeeShouldNotBeFound("attendeeId.equals=" + (attendeeId + 1));
    }*//*

    *//**
     * Executes the search, and checks that the default entity is returned.
     *//*
    private void defaultEventAttendeeShouldBeFound(String filter) throws Exception {
        restEventAttendeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAttendee.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].registrationStatus").value(hasItem(DEFAULT_REGISTRATION_STATUS)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(sameInstant(DEFAULT_REGISTRATION_DATE))))
            .andExpect(jsonPath("$.[*].confirmationDate").value(hasItem(sameInstant(DEFAULT_CONFIRMATION_DATE))))
            .andExpect(jsonPath("$.[*].cancellationDate").value(hasItem(sameInstant(DEFAULT_CANCELLATION_DATE))))
            .andExpect(jsonPath("$.[*].cancellationReason").value(hasItem(DEFAULT_CANCELLATION_REASON)))
            .andExpect(jsonPath("$.[*].attendeeType").value(hasItem(DEFAULT_ATTENDEE_TYPE)))
            .andExpect(jsonPath("$.[*].specialRequirements").value(hasItem(DEFAULT_SPECIAL_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].emergencyContactName").value(hasItem(DEFAULT_EMERGENCY_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].emergencyContactPhone").value(hasItem(DEFAULT_EMERGENCY_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].checkInStatus").value(hasItem(DEFAULT_CHECK_IN_STATUS)))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(sameInstant(DEFAULT_CHECK_IN_TIME))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventAttendeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    *//**
     * Executes the search, and checks that the default entity is not returned.
     *//*
    private void defaultEventAttendeeShouldNotBeFound(String filter) throws Exception {
        restEventAttendeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventAttendeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventAttendee() throws Exception {
        // Get the eventAttendee
        restEventAttendeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventAttendee() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();

        // Update the eventAttendee
        EventAttendee updatedEventAttendee = eventAttendeeRepository.findById(eventAttendee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventAttendee are not directly saved in db
        em.detach(updatedEventAttendee);
        updatedEventAttendee
            .tenantId(UPDATED_TENANT_ID)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .cancellationDate(UPDATED_CANCELLATION_DATE)
            .cancellationReason(UPDATED_CANCELLATION_REASON)
            .attendeeType(UPDATED_ATTENDEE_TYPE)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .emergencyContactName(UPDATED_EMERGENCY_CONTACT_NAME)
            .emergencyContactPhone(UPDATED_EMERGENCY_CONTACT_PHONE)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(updatedEventAttendee);

        restEventAttendeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAttendeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
        EventAttendee testEventAttendee = eventAttendeeList.get(eventAttendeeList.size() - 1);
        assertThat(testEventAttendee.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAttendee.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testEventAttendee.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testEventAttendee.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testEventAttendee.getCancellationDate()).isEqualTo(UPDATED_CANCELLATION_DATE);
        assertThat(testEventAttendee.getCancellationReason()).isEqualTo(UPDATED_CANCELLATION_REASON);
        assertThat(testEventAttendee.getAttendeeType()).isEqualTo(UPDATED_ATTENDEE_TYPE);
        assertThat(testEventAttendee.getSpecialRequirements()).isEqualTo(UPDATED_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendee.getEmergencyContactName()).isEqualTo(UPDATED_EMERGENCY_CONTACT_NAME);
        assertThat(testEventAttendee.getEmergencyContactPhone()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PHONE);
        assertThat(testEventAttendee.getCheckInStatus()).isEqualTo(UPDATED_CHECK_IN_STATUS);
        assertThat(testEventAttendee.getCheckInTime()).isEqualTo(UPDATED_CHECK_IN_TIME);
        assertThat(testEventAttendee.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEventAttendee.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAttendee.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventAttendee() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();
        eventAttendee.setId(longCount.incrementAndGet());

        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAttendeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAttendeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventAttendee() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();
        eventAttendee.setId(longCount.incrementAndGet());

        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventAttendee() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();
        eventAttendee.setId(longCount.incrementAndGet());

        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventAttendeeWithPatch() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();

        // Update the eventAttendee using partial update
        EventAttendee partialUpdatedEventAttendee = new EventAttendee();
        partialUpdatedEventAttendee.setId(eventAttendee.getId());

        partialUpdatedEventAttendee
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .emergencyContactPhone(UPDATED_EMERGENCY_CONTACT_PHONE)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .notes(UPDATED_NOTES);

        restEventAttendeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAttendee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAttendee))
            )
            .andExpect(status().isOk());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
        EventAttendee testEventAttendee = eventAttendeeList.get(eventAttendeeList.size() - 1);
        assertThat(testEventAttendee.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAttendee.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testEventAttendee.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testEventAttendee.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testEventAttendee.getCancellationDate()).isEqualTo(DEFAULT_CANCELLATION_DATE);
        assertThat(testEventAttendee.getCancellationReason()).isEqualTo(DEFAULT_CANCELLATION_REASON);
        assertThat(testEventAttendee.getAttendeeType()).isEqualTo(DEFAULT_ATTENDEE_TYPE);
        assertThat(testEventAttendee.getSpecialRequirements()).isEqualTo(DEFAULT_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendee.getEmergencyContactName()).isEqualTo(DEFAULT_EMERGENCY_CONTACT_NAME);
        assertThat(testEventAttendee.getEmergencyContactPhone()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PHONE);
        assertThat(testEventAttendee.getCheckInStatus()).isEqualTo(UPDATED_CHECK_IN_STATUS);
        assertThat(testEventAttendee.getCheckInTime()).isEqualTo(UPDATED_CHECK_IN_TIME);
        assertThat(testEventAttendee.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEventAttendee.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventAttendee.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventAttendeeWithPatch() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();

        // Update the eventAttendee using partial update
        EventAttendee partialUpdatedEventAttendee = new EventAttendee();
        partialUpdatedEventAttendee.setId(eventAttendee.getId());

        partialUpdatedEventAttendee
            .tenantId(UPDATED_TENANT_ID)
            .registrationStatus(UPDATED_REGISTRATION_STATUS)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .cancellationDate(UPDATED_CANCELLATION_DATE)
            .cancellationReason(UPDATED_CANCELLATION_REASON)
            .attendeeType(UPDATED_ATTENDEE_TYPE)
            .specialRequirements(UPDATED_SPECIAL_REQUIREMENTS)
            .emergencyContactName(UPDATED_EMERGENCY_CONTACT_NAME)
            .emergencyContactPhone(UPDATED_EMERGENCY_CONTACT_PHONE)
            .checkInStatus(UPDATED_CHECK_IN_STATUS)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventAttendeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAttendee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAttendee))
            )
            .andExpect(status().isOk());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
        EventAttendee testEventAttendee = eventAttendeeList.get(eventAttendeeList.size() - 1);
        assertThat(testEventAttendee.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAttendee.getRegistrationStatus()).isEqualTo(UPDATED_REGISTRATION_STATUS);
        assertThat(testEventAttendee.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testEventAttendee.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testEventAttendee.getCancellationDate()).isEqualTo(UPDATED_CANCELLATION_DATE);
        assertThat(testEventAttendee.getCancellationReason()).isEqualTo(UPDATED_CANCELLATION_REASON);
        assertThat(testEventAttendee.getAttendeeType()).isEqualTo(UPDATED_ATTENDEE_TYPE);
        assertThat(testEventAttendee.getSpecialRequirements()).isEqualTo(UPDATED_SPECIAL_REQUIREMENTS);
        assertThat(testEventAttendee.getEmergencyContactName()).isEqualTo(UPDATED_EMERGENCY_CONTACT_NAME);
        assertThat(testEventAttendee.getEmergencyContactPhone()).isEqualTo(UPDATED_EMERGENCY_CONTACT_PHONE);
        assertThat(testEventAttendee.getCheckInStatus()).isEqualTo(UPDATED_CHECK_IN_STATUS);
        assertThat(testEventAttendee.getCheckInTime()).isEqualTo(UPDATED_CHECK_IN_TIME);
        assertThat(testEventAttendee.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testEventAttendee.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventAttendee.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventAttendee() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();
        eventAttendee.setId(longCount.incrementAndGet());

        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAttendeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventAttendeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventAttendee() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();
        eventAttendee.setId(longCount.incrementAndGet());

        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventAttendee() throws Exception {
        int databaseSizeBeforeUpdate = eventAttendeeRepository.findAll().size();
        eventAttendee.setId(longCount.incrementAndGet());

        // Create the EventAttendee
        EventAttendeeDTO eventAttendeeDTO = eventAttendeeMapper.toDto(eventAttendee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAttendeeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAttendeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAttendee in the database
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventAttendee() throws Exception {
        // Initialize the database
        eventAttendeeRepository.saveAndFlush(eventAttendee);

        int databaseSizeBeforeDelete = eventAttendeeRepository.findAll().size();

        // Delete the eventAttendee
        restEventAttendeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventAttendee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAll();
        assertThat(eventAttendeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
*/
}
