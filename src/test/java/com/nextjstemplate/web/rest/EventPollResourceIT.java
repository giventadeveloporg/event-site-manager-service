package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventPoll;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventPollRepository;
import com.nextjstemplate.service.dto.EventPollDTO;
import com.nextjstemplate.service.mapper.EventPollMapper;
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
 * Integration tests for the {@link EventPollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventPollResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-polls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventPollRepository eventPollRepository;

    @Autowired
    private EventPollMapper eventPollMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventPollMockMvc;

    private EventPoll eventPoll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPoll createEntity(EntityManager em) {
        EventPoll eventPoll = new EventPoll()
            .tenantId(DEFAULT_TENANT_ID)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventPoll;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPoll createUpdatedEntity(EntityManager em) {
        EventPoll eventPoll = new EventPoll()
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventPoll;
    }

    @BeforeEach
    public void initTest() {
        eventPoll = createEntity(em);
    }

    @Test
    @Transactional
    void createEventPoll() throws Exception {
        int databaseSizeBeforeCreate = eventPollRepository.findAll().size();
        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);
        restEventPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isCreated());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeCreate + 1);
        EventPoll testEventPoll = eventPollList.get(eventPollList.size() - 1);
        assertThat(testEventPoll.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventPoll.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventPoll.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventPoll.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEventPoll.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEventPoll.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEventPoll.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventPoll.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventPollWithExistingId() throws Exception {
        // Create the EventPoll with an existing ID
        eventPoll.setId(1L);
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        int databaseSizeBeforeCreate = eventPollRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollRepository.findAll().size();
        // set the field null
        eventPoll.setTitle(null);

        // Create the EventPoll, which fails.
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        restEventPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isBadRequest());

        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollRepository.findAll().size();
        // set the field null
        eventPoll.setStartDate(null);

        // Create the EventPoll, which fails.
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        restEventPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isBadRequest());

        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollRepository.findAll().size();
        // set the field null
        eventPoll.setCreatedAt(null);

        // Create the EventPoll, which fails.
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        restEventPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isBadRequest());

        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollRepository.findAll().size();
        // set the field null
        eventPoll.setUpdatedAt(null);

        // Create the EventPoll, which fails.
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        restEventPollMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isBadRequest());

        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventPolls() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList
        restEventPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventPoll() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get the eventPoll
        restEventPollMockMvc
            .perform(get(ENTITY_API_URL_ID, eventPoll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventPoll.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventPollsByIdFiltering() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        Long id = eventPoll.getId();

        defaultEventPollShouldBeFound("id.equals=" + id);
        defaultEventPollShouldNotBeFound("id.notEquals=" + id);

        defaultEventPollShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventPollShouldNotBeFound("id.greaterThan=" + id);

        defaultEventPollShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventPollShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventPollsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventPollShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventPollList where tenantId equals to UPDATED_TENANT_ID
        defaultEventPollShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventPollShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventPollList where tenantId equals to UPDATED_TENANT_ID
        defaultEventPollShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where tenantId is not null
        defaultEventPollShouldBeFound("tenantId.specified=true");

        // Get all the eventPollList where tenantId is null
        defaultEventPollShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where tenantId contains DEFAULT_TENANT_ID
        defaultEventPollShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventPollList where tenantId contains UPDATED_TENANT_ID
        defaultEventPollShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventPollShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventPollList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventPollShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where title equals to DEFAULT_TITLE
        defaultEventPollShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventPollList where title equals to UPDATED_TITLE
        defaultEventPollShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventPollsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventPollShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventPollList where title equals to UPDATED_TITLE
        defaultEventPollShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventPollsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where title is not null
        defaultEventPollShouldBeFound("title.specified=true");

        // Get all the eventPollList where title is null
        defaultEventPollShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByTitleContainsSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where title contains DEFAULT_TITLE
        defaultEventPollShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventPollList where title contains UPDATED_TITLE
        defaultEventPollShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventPollsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where title does not contain DEFAULT_TITLE
        defaultEventPollShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventPollList where title does not contain UPDATED_TITLE
        defaultEventPollShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventPollsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where description equals to DEFAULT_DESCRIPTION
        defaultEventPollShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventPollList where description equals to UPDATED_DESCRIPTION
        defaultEventPollShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventPollsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventPollShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventPollList where description equals to UPDATED_DESCRIPTION
        defaultEventPollShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventPollsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where description is not null
        defaultEventPollShouldBeFound("description.specified=true");

        // Get all the eventPollList where description is null
        defaultEventPollShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where description contains DEFAULT_DESCRIPTION
        defaultEventPollShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventPollList where description contains UPDATED_DESCRIPTION
        defaultEventPollShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventPollsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where description does not contain DEFAULT_DESCRIPTION
        defaultEventPollShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventPollList where description does not contain UPDATED_DESCRIPTION
        defaultEventPollShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventPollsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEventPollShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the eventPollList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventPollShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventPollsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEventPollShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the eventPollList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventPollShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventPollsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where isActive is not null
        defaultEventPollShouldBeFound("isActive.specified=true");

        // Get all the eventPollList where isActive is null
        defaultEventPollShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate equals to DEFAULT_START_DATE
        defaultEventPollShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the eventPollList where startDate equals to UPDATED_START_DATE
        defaultEventPollShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultEventPollShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the eventPollList where startDate equals to UPDATED_START_DATE
        defaultEventPollShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate is not null
        defaultEventPollShouldBeFound("startDate.specified=true");

        // Get all the eventPollList where startDate is null
        defaultEventPollShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultEventPollShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the eventPollList where startDate is greater than or equal to UPDATED_START_DATE
        defaultEventPollShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate is less than or equal to DEFAULT_START_DATE
        defaultEventPollShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the eventPollList where startDate is less than or equal to SMALLER_START_DATE
        defaultEventPollShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate is less than DEFAULT_START_DATE
        defaultEventPollShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the eventPollList where startDate is less than UPDATED_START_DATE
        defaultEventPollShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where startDate is greater than DEFAULT_START_DATE
        defaultEventPollShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the eventPollList where startDate is greater than SMALLER_START_DATE
        defaultEventPollShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate equals to DEFAULT_END_DATE
        defaultEventPollShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the eventPollList where endDate equals to UPDATED_END_DATE
        defaultEventPollShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultEventPollShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the eventPollList where endDate equals to UPDATED_END_DATE
        defaultEventPollShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate is not null
        defaultEventPollShouldBeFound("endDate.specified=true");

        // Get all the eventPollList where endDate is null
        defaultEventPollShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultEventPollShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventPollList where endDate is greater than or equal to UPDATED_END_DATE
        defaultEventPollShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate is less than or equal to DEFAULT_END_DATE
        defaultEventPollShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventPollList where endDate is less than or equal to SMALLER_END_DATE
        defaultEventPollShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate is less than DEFAULT_END_DATE
        defaultEventPollShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the eventPollList where endDate is less than UPDATED_END_DATE
        defaultEventPollShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where endDate is greater than DEFAULT_END_DATE
        defaultEventPollShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the eventPollList where endDate is greater than SMALLER_END_DATE
        defaultEventPollShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventPollShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventPollList where createdAt equals to UPDATED_CREATED_AT
        defaultEventPollShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventPollShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventPollList where createdAt equals to UPDATED_CREATED_AT
        defaultEventPollShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt is not null
        defaultEventPollShouldBeFound("createdAt.specified=true");

        // Get all the eventPollList where createdAt is null
        defaultEventPollShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventPollShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventPollList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventPollShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventPollShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventPollList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventPollShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventPollShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventPollList where createdAt is less than UPDATED_CREATED_AT
        defaultEventPollShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventPollShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventPollList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventPollShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventPollShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventPollShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventPollShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventPollList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventPollShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt is not null
        defaultEventPollShouldBeFound("updatedAt.specified=true");

        // Get all the eventPollList where updatedAt is null
        defaultEventPollShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventPollShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventPollShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventPollShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventPollShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventPollShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventPollShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        // Get all the eventPollList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventPollShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventPollShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollsByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventPollRepository.saveAndFlush(eventPoll);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventPoll.setEvent(event);
        eventPollRepository.saveAndFlush(eventPoll);
        Long eventId = event.getId();
        // Get all the eventPollList where event equals to eventId
        defaultEventPollShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventPollList where event equals to (eventId + 1)
        defaultEventPollShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /* @Test
    @Transactional
    void getAllEventPollsByCreatedByIsEqualToSomething() throws Exception {
        UserProfile createdBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventPollRepository.saveAndFlush(eventPoll);
            createdBy = UserProfileResourceIT.createEntity(em);
        } else {
            createdBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        eventPoll.setCreatedBy(createdBy);
        eventPollRepository.saveAndFlush(eventPoll);
        Long createdById = createdBy.getId();
        // Get all the eventPollList where createdBy equals to createdById
        defaultEventPollShouldBeFound("createdById.equals=" + createdById);

        // Get all the eventPollList where createdBy equals to (createdById + 1)
        defaultEventPollShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventPollShouldBeFound(String filter) throws Exception {
        restEventPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventPollMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventPollShouldNotBeFound(String filter) throws Exception {
        restEventPollMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventPollMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventPoll() throws Exception {
        // Get the eventPoll
        restEventPollMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventPoll() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();

        // Update the eventPoll
        EventPoll updatedEventPoll = eventPollRepository.findById(eventPoll.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventPoll are not directly saved in db
        em.detach(updatedEventPoll);
        updatedEventPoll
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventPollDTO eventPollDTO = eventPollMapper.toDto(updatedEventPoll);

        restEventPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
        EventPoll testEventPoll = eventPollList.get(eventPollList.size() - 1);
        assertThat(testEventPoll.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPoll.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventPoll.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventPoll.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventPoll.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEventPoll.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPoll.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventPoll() throws Exception {
        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();
        eventPoll.setId(longCount.incrementAndGet());

        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPollDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventPoll() throws Exception {
        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();
        eventPoll.setId(longCount.incrementAndGet());

        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventPoll() throws Exception {
        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();
        eventPoll.setId(longCount.incrementAndGet());

        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventPollWithPatch() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();

        // Update the eventPoll using partial update
        EventPoll partialUpdatedEventPoll = new EventPoll();
        partialUpdatedEventPoll.setId(eventPoll.getId());

        partialUpdatedEventPoll
            .tenantId(UPDATED_TENANT_ID)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPoll))
            )
            .andExpect(status().isOk());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
        EventPoll testEventPoll = eventPollList.get(eventPollList.size() - 1);
        assertThat(testEventPoll.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPoll.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventPoll.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventPoll.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventPoll.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEventPoll.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventPoll.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventPollWithPatch() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();

        // Update the eventPoll using partial update
        EventPoll partialUpdatedEventPoll = new EventPoll();
        partialUpdatedEventPoll.setId(eventPoll.getId());

        partialUpdatedEventPoll
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPoll.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPoll))
            )
            .andExpect(status().isOk());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
        EventPoll testEventPoll = eventPollList.get(eventPollList.size() - 1);
        assertThat(testEventPoll.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPoll.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventPoll.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventPoll.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventPoll.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEventPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEventPoll.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPoll.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventPoll() throws Exception {
        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();
        eventPoll.setId(longCount.incrementAndGet());

        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventPollDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventPoll() throws Exception {
        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();
        eventPoll.setId(longCount.incrementAndGet());

        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventPoll() throws Exception {
        int databaseSizeBeforeUpdate = eventPollRepository.findAll().size();
        eventPoll.setId(longCount.incrementAndGet());

        // Create the EventPoll
        EventPollDTO eventPollDTO = eventPollMapper.toDto(eventPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventPollDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPoll in the database
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventPoll() throws Exception {
        // Initialize the database
        eventPollRepository.saveAndFlush(eventPoll);

        int databaseSizeBeforeDelete = eventPollRepository.findAll().size();

        // Delete the eventPoll
        restEventPollMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventPoll.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventPoll> eventPollList = eventPollRepository.findAll();
        assertThat(eventPollList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
