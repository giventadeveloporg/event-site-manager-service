package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventCalendarEntry;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.repository.EventCalendarEntryRepository;
import com.eventsitemanager.service.dto.EventCalendarEntryDTO;
import com.eventsitemanager.service.mapper.EventCalendarEntryMapper;
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
 * Integration tests for the {@link EventCalendarEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventCalendarEntryResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CALENDAR_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_CALENDAR_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CALENDAR_LINK = "AAAAAAAAAA";
    private static final String UPDATED_CALENDAR_LINK = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-calendar-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventCalendarEntryRepository eventCalendarEntryRepository;

    @Autowired
    private EventCalendarEntryMapper eventCalendarEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventCalendarEntryMockMvc;

    private EventCalendarEntry eventCalendarEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCalendarEntry createEntity(EntityManager em) {
        EventCalendarEntry eventCalendarEntry = new EventCalendarEntry()
            .tenantId(DEFAULT_TENANT_ID)
            .calendarProvider(DEFAULT_CALENDAR_PROVIDER)
            .externalEventId(DEFAULT_EXTERNAL_EVENT_ID)
            .calendarLink(DEFAULT_CALENDAR_LINK)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventCalendarEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCalendarEntry createUpdatedEntity(EntityManager em) {
        EventCalendarEntry eventCalendarEntry = new EventCalendarEntry()
            .tenantId(UPDATED_TENANT_ID)
            .calendarProvider(UPDATED_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID)
            .calendarLink(UPDATED_CALENDAR_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventCalendarEntry;
    }

    @BeforeEach
    public void initTest() {
        eventCalendarEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createEventCalendarEntry() throws Exception {
        int databaseSizeBeforeCreate = eventCalendarEntryRepository.findAll().size();
        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);
        restEventCalendarEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeCreate + 1);
        EventCalendarEntry testEventCalendarEntry = eventCalendarEntryList.get(eventCalendarEntryList.size() - 1);
        assertThat(testEventCalendarEntry.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventCalendarEntry.getCalendarProvider()).isEqualTo(DEFAULT_CALENDAR_PROVIDER);
        assertThat(testEventCalendarEntry.getExternalEventId()).isEqualTo(DEFAULT_EXTERNAL_EVENT_ID);
        assertThat(testEventCalendarEntry.getCalendarLink()).isEqualTo(DEFAULT_CALENDAR_LINK);
        assertThat(testEventCalendarEntry.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventCalendarEntry.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventCalendarEntryWithExistingId() throws Exception {
        // Create the EventCalendarEntry with an existing ID
        eventCalendarEntry.setId(1L);
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        int databaseSizeBeforeCreate = eventCalendarEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventCalendarEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCalendarProviderIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventCalendarEntryRepository.findAll().size();
        // set the field null
        eventCalendarEntry.setCalendarProvider(null);

        // Create the EventCalendarEntry, which fails.
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        restEventCalendarEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCalendarLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventCalendarEntryRepository.findAll().size();
        // set the field null
        eventCalendarEntry.setCalendarLink(null);

        // Create the EventCalendarEntry, which fails.
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        restEventCalendarEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventCalendarEntryRepository.findAll().size();
        // set the field null
        eventCalendarEntry.setCreatedAt(null);

        // Create the EventCalendarEntry, which fails.
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        restEventCalendarEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventCalendarEntryRepository.findAll().size();
        // set the field null
        eventCalendarEntry.setUpdatedAt(null);

        // Create the EventCalendarEntry, which fails.
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        restEventCalendarEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntries() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList
        restEventCalendarEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCalendarEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].calendarProvider").value(hasItem(DEFAULT_CALENDAR_PROVIDER)))
            .andExpect(jsonPath("$.[*].externalEventId").value(hasItem(DEFAULT_EXTERNAL_EVENT_ID)))
            .andExpect(jsonPath("$.[*].calendarLink").value(hasItem(DEFAULT_CALENDAR_LINK)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventCalendarEntry() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get the eventCalendarEntry
        restEventCalendarEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, eventCalendarEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventCalendarEntry.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.calendarProvider").value(DEFAULT_CALENDAR_PROVIDER))
            .andExpect(jsonPath("$.externalEventId").value(DEFAULT_EXTERNAL_EVENT_ID))
            .andExpect(jsonPath("$.calendarLink").value(DEFAULT_CALENDAR_LINK))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventCalendarEntriesByIdFiltering() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        Long id = eventCalendarEntry.getId();

        defaultEventCalendarEntryShouldBeFound("id.equals=" + id);
        defaultEventCalendarEntryShouldNotBeFound("id.notEquals=" + id);

        defaultEventCalendarEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventCalendarEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultEventCalendarEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventCalendarEntryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventCalendarEntryShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventCalendarEntryList where tenantId equals to UPDATED_TENANT_ID
        defaultEventCalendarEntryShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventCalendarEntryShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventCalendarEntryList where tenantId equals to UPDATED_TENANT_ID
        defaultEventCalendarEntryShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where tenantId is not null
        defaultEventCalendarEntryShouldBeFound("tenantId.specified=true");

        // Get all the eventCalendarEntryList where tenantId is null
        defaultEventCalendarEntryShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where tenantId contains DEFAULT_TENANT_ID
        defaultEventCalendarEntryShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventCalendarEntryList where tenantId contains UPDATED_TENANT_ID
        defaultEventCalendarEntryShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventCalendarEntryShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventCalendarEntryList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventCalendarEntryShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarProvider equals to DEFAULT_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldBeFound("calendarProvider.equals=" + DEFAULT_CALENDAR_PROVIDER);

        // Get all the eventCalendarEntryList where calendarProvider equals to UPDATED_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldNotBeFound("calendarProvider.equals=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarProviderIsInShouldWork() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarProvider in DEFAULT_CALENDAR_PROVIDER or UPDATED_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldBeFound("calendarProvider.in=" + DEFAULT_CALENDAR_PROVIDER + "," + UPDATED_CALENDAR_PROVIDER);

        // Get all the eventCalendarEntryList where calendarProvider equals to UPDATED_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldNotBeFound("calendarProvider.in=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarProvider is not null
        defaultEventCalendarEntryShouldBeFound("calendarProvider.specified=true");

        // Get all the eventCalendarEntryList where calendarProvider is null
        defaultEventCalendarEntryShouldNotBeFound("calendarProvider.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarProviderContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarProvider contains DEFAULT_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldBeFound("calendarProvider.contains=" + DEFAULT_CALENDAR_PROVIDER);

        // Get all the eventCalendarEntryList where calendarProvider contains UPDATED_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldNotBeFound("calendarProvider.contains=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarProviderNotContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarProvider does not contain DEFAULT_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldNotBeFound("calendarProvider.doesNotContain=" + DEFAULT_CALENDAR_PROVIDER);

        // Get all the eventCalendarEntryList where calendarProvider does not contain UPDATED_CALENDAR_PROVIDER
        defaultEventCalendarEntryShouldBeFound("calendarProvider.doesNotContain=" + UPDATED_CALENDAR_PROVIDER);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByExternalEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where externalEventId equals to DEFAULT_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldBeFound("externalEventId.equals=" + DEFAULT_EXTERNAL_EVENT_ID);

        // Get all the eventCalendarEntryList where externalEventId equals to UPDATED_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldNotBeFound("externalEventId.equals=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByExternalEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where externalEventId in DEFAULT_EXTERNAL_EVENT_ID or UPDATED_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldBeFound("externalEventId.in=" + DEFAULT_EXTERNAL_EVENT_ID + "," + UPDATED_EXTERNAL_EVENT_ID);

        // Get all the eventCalendarEntryList where externalEventId equals to UPDATED_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldNotBeFound("externalEventId.in=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByExternalEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where externalEventId is not null
        defaultEventCalendarEntryShouldBeFound("externalEventId.specified=true");

        // Get all the eventCalendarEntryList where externalEventId is null
        defaultEventCalendarEntryShouldNotBeFound("externalEventId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByExternalEventIdContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where externalEventId contains DEFAULT_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldBeFound("externalEventId.contains=" + DEFAULT_EXTERNAL_EVENT_ID);

        // Get all the eventCalendarEntryList where externalEventId contains UPDATED_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldNotBeFound("externalEventId.contains=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByExternalEventIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where externalEventId does not contain DEFAULT_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldNotBeFound("externalEventId.doesNotContain=" + DEFAULT_EXTERNAL_EVENT_ID);

        // Get all the eventCalendarEntryList where externalEventId does not contain UPDATED_EXTERNAL_EVENT_ID
        defaultEventCalendarEntryShouldBeFound("externalEventId.doesNotContain=" + UPDATED_EXTERNAL_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarLink equals to DEFAULT_CALENDAR_LINK
        defaultEventCalendarEntryShouldBeFound("calendarLink.equals=" + DEFAULT_CALENDAR_LINK);

        // Get all the eventCalendarEntryList where calendarLink equals to UPDATED_CALENDAR_LINK
        defaultEventCalendarEntryShouldNotBeFound("calendarLink.equals=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarLinkIsInShouldWork() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarLink in DEFAULT_CALENDAR_LINK or UPDATED_CALENDAR_LINK
        defaultEventCalendarEntryShouldBeFound("calendarLink.in=" + DEFAULT_CALENDAR_LINK + "," + UPDATED_CALENDAR_LINK);

        // Get all the eventCalendarEntryList where calendarLink equals to UPDATED_CALENDAR_LINK
        defaultEventCalendarEntryShouldNotBeFound("calendarLink.in=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarLink is not null
        defaultEventCalendarEntryShouldBeFound("calendarLink.specified=true");

        // Get all the eventCalendarEntryList where calendarLink is null
        defaultEventCalendarEntryShouldNotBeFound("calendarLink.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarLinkContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarLink contains DEFAULT_CALENDAR_LINK
        defaultEventCalendarEntryShouldBeFound("calendarLink.contains=" + DEFAULT_CALENDAR_LINK);

        // Get all the eventCalendarEntryList where calendarLink contains UPDATED_CALENDAR_LINK
        defaultEventCalendarEntryShouldNotBeFound("calendarLink.contains=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCalendarLinkNotContainsSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where calendarLink does not contain DEFAULT_CALENDAR_LINK
        defaultEventCalendarEntryShouldNotBeFound("calendarLink.doesNotContain=" + DEFAULT_CALENDAR_LINK);

        // Get all the eventCalendarEntryList where calendarLink does not contain UPDATED_CALENDAR_LINK
        defaultEventCalendarEntryShouldBeFound("calendarLink.doesNotContain=" + UPDATED_CALENDAR_LINK);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventCalendarEntryShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventCalendarEntryList where createdAt equals to UPDATED_CREATED_AT
        defaultEventCalendarEntryShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventCalendarEntryShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventCalendarEntryList where createdAt equals to UPDATED_CREATED_AT
        defaultEventCalendarEntryShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt is not null
        defaultEventCalendarEntryShouldBeFound("createdAt.specified=true");

        // Get all the eventCalendarEntryList where createdAt is null
        defaultEventCalendarEntryShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventCalendarEntryShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventCalendarEntryList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventCalendarEntryShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventCalendarEntryShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventCalendarEntryList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventCalendarEntryShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventCalendarEntryShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventCalendarEntryList where createdAt is less than UPDATED_CREATED_AT
        defaultEventCalendarEntryShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventCalendarEntryShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventCalendarEntryList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventCalendarEntryShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventCalendarEntryShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventCalendarEntryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventCalendarEntryShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventCalendarEntryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt is not null
        defaultEventCalendarEntryShouldBeFound("updatedAt.specified=true");

        // Get all the eventCalendarEntryList where updatedAt is null
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventCalendarEntryShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventCalendarEntryList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventCalendarEntryShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventCalendarEntryList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventCalendarEntryList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventCalendarEntryShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        // Get all the eventCalendarEntryList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventCalendarEntryShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventCalendarEntryList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventCalendarEntryShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventCalendarEntriesByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventCalendarEntry.setEvent(event);
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);
        Long eventId = event.getId();
        // Get all the eventCalendarEntryList where event equals to eventId
        defaultEventCalendarEntryShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventCalendarEntryList where event equals to (eventId + 1)
        defaultEventCalendarEntryShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /*@Test
    @Transactional
    void getAllEventCalendarEntriesByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        eventCalendarEntry.setCreatedBy(createdBy);
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);
        Long createdById = createdBy.getId();
        // Get all the eventCalendarEntryList where createdBy equals to createdById
        defaultEventCalendarEntryShouldBeFound("createdById.equals=" + createdById);

        // Get all the eventCalendarEntryList where createdBy equals to (createdById + 1)
        defaultEventCalendarEntryShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventCalendarEntryShouldBeFound(String filter) throws Exception {
        restEventCalendarEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCalendarEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].calendarProvider").value(hasItem(DEFAULT_CALENDAR_PROVIDER)))
            .andExpect(jsonPath("$.[*].externalEventId").value(hasItem(DEFAULT_EXTERNAL_EVENT_ID)))
            .andExpect(jsonPath("$.[*].calendarLink").value(hasItem(DEFAULT_CALENDAR_LINK)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventCalendarEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventCalendarEntryShouldNotBeFound(String filter) throws Exception {
        restEventCalendarEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventCalendarEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventCalendarEntry() throws Exception {
        // Get the eventCalendarEntry
        restEventCalendarEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventCalendarEntry() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();

        // Update the eventCalendarEntry
        EventCalendarEntry updatedEventCalendarEntry = eventCalendarEntryRepository.findById(eventCalendarEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventCalendarEntry are not directly saved in db
        em.detach(updatedEventCalendarEntry);
        updatedEventCalendarEntry
            .tenantId(UPDATED_TENANT_ID)
            .calendarProvider(UPDATED_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID)
            .calendarLink(UPDATED_CALENDAR_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(updatedEventCalendarEntry);

        restEventCalendarEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventCalendarEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
        EventCalendarEntry testEventCalendarEntry = eventCalendarEntryList.get(eventCalendarEntryList.size() - 1);
        assertThat(testEventCalendarEntry.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventCalendarEntry.getCalendarProvider()).isEqualTo(UPDATED_CALENDAR_PROVIDER);
        assertThat(testEventCalendarEntry.getExternalEventId()).isEqualTo(UPDATED_EXTERNAL_EVENT_ID);
        assertThat(testEventCalendarEntry.getCalendarLink()).isEqualTo(UPDATED_CALENDAR_LINK);
        assertThat(testEventCalendarEntry.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventCalendarEntry.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventCalendarEntry() throws Exception {
        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();
        eventCalendarEntry.setId(longCount.incrementAndGet());

        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCalendarEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventCalendarEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventCalendarEntry() throws Exception {
        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();
        eventCalendarEntry.setId(longCount.incrementAndGet());

        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCalendarEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventCalendarEntry() throws Exception {
        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();
        eventCalendarEntry.setId(longCount.incrementAndGet());

        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCalendarEntryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventCalendarEntryWithPatch() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();

        // Update the eventCalendarEntry using partial update
        EventCalendarEntry partialUpdatedEventCalendarEntry = new EventCalendarEntry();
        partialUpdatedEventCalendarEntry.setId(eventCalendarEntry.getId());

        partialUpdatedEventCalendarEntry.calendarProvider(UPDATED_CALENDAR_PROVIDER);

        restEventCalendarEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventCalendarEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventCalendarEntry))
            )
            .andExpect(status().isOk());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
        EventCalendarEntry testEventCalendarEntry = eventCalendarEntryList.get(eventCalendarEntryList.size() - 1);
        assertThat(testEventCalendarEntry.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventCalendarEntry.getCalendarProvider()).isEqualTo(UPDATED_CALENDAR_PROVIDER);
        assertThat(testEventCalendarEntry.getExternalEventId()).isEqualTo(DEFAULT_EXTERNAL_EVENT_ID);
        assertThat(testEventCalendarEntry.getCalendarLink()).isEqualTo(DEFAULT_CALENDAR_LINK);
        assertThat(testEventCalendarEntry.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventCalendarEntry.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventCalendarEntryWithPatch() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();

        // Update the eventCalendarEntry using partial update
        EventCalendarEntry partialUpdatedEventCalendarEntry = new EventCalendarEntry();
        partialUpdatedEventCalendarEntry.setId(eventCalendarEntry.getId());

        partialUpdatedEventCalendarEntry
            .tenantId(UPDATED_TENANT_ID)
            .calendarProvider(UPDATED_CALENDAR_PROVIDER)
            .externalEventId(UPDATED_EXTERNAL_EVENT_ID)
            .calendarLink(UPDATED_CALENDAR_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventCalendarEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventCalendarEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventCalendarEntry))
            )
            .andExpect(status().isOk());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
        EventCalendarEntry testEventCalendarEntry = eventCalendarEntryList.get(eventCalendarEntryList.size() - 1);
        assertThat(testEventCalendarEntry.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventCalendarEntry.getCalendarProvider()).isEqualTo(UPDATED_CALENDAR_PROVIDER);
        assertThat(testEventCalendarEntry.getExternalEventId()).isEqualTo(UPDATED_EXTERNAL_EVENT_ID);
        assertThat(testEventCalendarEntry.getCalendarLink()).isEqualTo(UPDATED_CALENDAR_LINK);
        assertThat(testEventCalendarEntry.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventCalendarEntry.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventCalendarEntry() throws Exception {
        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();
        eventCalendarEntry.setId(longCount.incrementAndGet());

        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCalendarEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventCalendarEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventCalendarEntry() throws Exception {
        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();
        eventCalendarEntry.setId(longCount.incrementAndGet());

        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCalendarEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventCalendarEntry() throws Exception {
        int databaseSizeBeforeUpdate = eventCalendarEntryRepository.findAll().size();
        eventCalendarEntry.setId(longCount.incrementAndGet());

        // Create the EventCalendarEntry
        EventCalendarEntryDTO eventCalendarEntryDTO = eventCalendarEntryMapper.toDto(eventCalendarEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCalendarEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCalendarEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventCalendarEntry in the database
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventCalendarEntry() throws Exception {
        // Initialize the database
        eventCalendarEntryRepository.saveAndFlush(eventCalendarEntry);

        int databaseSizeBeforeDelete = eventCalendarEntryRepository.findAll().size();

        // Delete the eventCalendarEntry
        restEventCalendarEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventCalendarEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventCalendarEntry> eventCalendarEntryList = eventCalendarEntryRepository.findAll();
        assertThat(eventCalendarEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
