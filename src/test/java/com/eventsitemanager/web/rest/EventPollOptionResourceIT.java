package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventPoll;
import com.eventsitemanager.domain.EventPollOption;
import com.eventsitemanager.repository.EventPollOptionRepository;
import com.eventsitemanager.service.dto.EventPollOptionDTO;
import com.eventsitemanager.service.mapper.EventPollOptionMapper;
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
 * Integration tests for the {@link EventPollOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventPollOptionResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OPTION_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-poll-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventPollOptionRepository eventPollOptionRepository;

    @Autowired
    private EventPollOptionMapper eventPollOptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventPollOptionMockMvc;

    private EventPollOption eventPollOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPollOption createEntity(EntityManager em) {
        EventPollOption eventPollOption = new EventPollOption()
            .tenantId(DEFAULT_TENANT_ID)
            .optionText(DEFAULT_OPTION_TEXT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventPollOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPollOption createUpdatedEntity(EntityManager em) {
        EventPollOption eventPollOption = new EventPollOption()
            .tenantId(UPDATED_TENANT_ID)
            .optionText(UPDATED_OPTION_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventPollOption;
    }

    @BeforeEach
    public void initTest() {
        eventPollOption = createEntity(em);
    }

    @Test
    @Transactional
    void createEventPollOption() throws Exception {
        int databaseSizeBeforeCreate = eventPollOptionRepository.findAll().size();
        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);
        restEventPollOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeCreate + 1);
        EventPollOption testEventPollOption = eventPollOptionList.get(eventPollOptionList.size() - 1);
        assertThat(testEventPollOption.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventPollOption.getOptionText()).isEqualTo(DEFAULT_OPTION_TEXT);
        assertThat(testEventPollOption.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventPollOption.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventPollOptionWithExistingId() throws Exception {
        // Create the EventPollOption with an existing ID
        eventPollOption.setId(1L);
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        int databaseSizeBeforeCreate = eventPollOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventPollOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOptionTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollOptionRepository.findAll().size();
        // set the field null
        eventPollOption.setOptionText(null);

        // Create the EventPollOption, which fails.
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        restEventPollOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollOptionRepository.findAll().size();
        // set the field null
        eventPollOption.setCreatedAt(null);

        // Create the EventPollOption, which fails.
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        restEventPollOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollOptionRepository.findAll().size();
        // set the field null
        eventPollOption.setUpdatedAt(null);

        // Create the EventPollOption, which fails.
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        restEventPollOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventPollOptions() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList
        restEventPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPollOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].optionText").value(hasItem(DEFAULT_OPTION_TEXT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventPollOption() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get the eventPollOption
        restEventPollOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, eventPollOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventPollOption.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.optionText").value(DEFAULT_OPTION_TEXT))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventPollOptionsByIdFiltering() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        Long id = eventPollOption.getId();

        defaultEventPollOptionShouldBeFound("id.equals=" + id);
        defaultEventPollOptionShouldNotBeFound("id.notEquals=" + id);

        defaultEventPollOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventPollOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultEventPollOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventPollOptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventPollOptionShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventPollOptionList where tenantId equals to UPDATED_TENANT_ID
        defaultEventPollOptionShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventPollOptionShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventPollOptionList where tenantId equals to UPDATED_TENANT_ID
        defaultEventPollOptionShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where tenantId is not null
        defaultEventPollOptionShouldBeFound("tenantId.specified=true");

        // Get all the eventPollOptionList where tenantId is null
        defaultEventPollOptionShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where tenantId contains DEFAULT_TENANT_ID
        defaultEventPollOptionShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventPollOptionList where tenantId contains UPDATED_TENANT_ID
        defaultEventPollOptionShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventPollOptionShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventPollOptionList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventPollOptionShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByOptionTextIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where optionText equals to DEFAULT_OPTION_TEXT
        defaultEventPollOptionShouldBeFound("optionText.equals=" + DEFAULT_OPTION_TEXT);

        // Get all the eventPollOptionList where optionText equals to UPDATED_OPTION_TEXT
        defaultEventPollOptionShouldNotBeFound("optionText.equals=" + UPDATED_OPTION_TEXT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByOptionTextIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where optionText in DEFAULT_OPTION_TEXT or UPDATED_OPTION_TEXT
        defaultEventPollOptionShouldBeFound("optionText.in=" + DEFAULT_OPTION_TEXT + "," + UPDATED_OPTION_TEXT);

        // Get all the eventPollOptionList where optionText equals to UPDATED_OPTION_TEXT
        defaultEventPollOptionShouldNotBeFound("optionText.in=" + UPDATED_OPTION_TEXT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByOptionTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where optionText is not null
        defaultEventPollOptionShouldBeFound("optionText.specified=true");

        // Get all the eventPollOptionList where optionText is null
        defaultEventPollOptionShouldNotBeFound("optionText.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByOptionTextContainsSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where optionText contains DEFAULT_OPTION_TEXT
        defaultEventPollOptionShouldBeFound("optionText.contains=" + DEFAULT_OPTION_TEXT);

        // Get all the eventPollOptionList where optionText contains UPDATED_OPTION_TEXT
        defaultEventPollOptionShouldNotBeFound("optionText.contains=" + UPDATED_OPTION_TEXT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByOptionTextNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where optionText does not contain DEFAULT_OPTION_TEXT
        defaultEventPollOptionShouldNotBeFound("optionText.doesNotContain=" + DEFAULT_OPTION_TEXT);

        // Get all the eventPollOptionList where optionText does not contain UPDATED_OPTION_TEXT
        defaultEventPollOptionShouldBeFound("optionText.doesNotContain=" + UPDATED_OPTION_TEXT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventPollOptionShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventPollOptionList where createdAt equals to UPDATED_CREATED_AT
        defaultEventPollOptionShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventPollOptionShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventPollOptionList where createdAt equals to UPDATED_CREATED_AT
        defaultEventPollOptionShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt is not null
        defaultEventPollOptionShouldBeFound("createdAt.specified=true");

        // Get all the eventPollOptionList where createdAt is null
        defaultEventPollOptionShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventPollOptionShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventPollOptionList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventPollOptionShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventPollOptionShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventPollOptionList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventPollOptionShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventPollOptionShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventPollOptionList where createdAt is less than UPDATED_CREATED_AT
        defaultEventPollOptionShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventPollOptionShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventPollOptionList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventPollOptionShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventPollOptionShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollOptionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventPollOptionShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventPollOptionShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventPollOptionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventPollOptionShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt is not null
        defaultEventPollOptionShouldBeFound("updatedAt.specified=true");

        // Get all the eventPollOptionList where updatedAt is null
        defaultEventPollOptionShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventPollOptionShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollOptionList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventPollOptionShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventPollOptionShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollOptionList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventPollOptionShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventPollOptionShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollOptionList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventPollOptionShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        // Get all the eventPollOptionList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventPollOptionShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollOptionList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventPollOptionShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollOptionsByPollIsEqualToSomething() throws Exception {
        EventPoll poll;
        if (TestUtil.findAll(em, EventPoll.class).isEmpty()) {
            eventPollOptionRepository.saveAndFlush(eventPollOption);
            poll = EventPollResourceIT.createEntity(em);
        } else {
            poll = TestUtil.findAll(em, EventPoll.class).get(0);
        }
        em.persist(poll);
        em.flush();
        eventPollOption.setPoll(poll);
        eventPollOptionRepository.saveAndFlush(eventPollOption);
        Long pollId = poll.getId();
        // Get all the eventPollOptionList where poll equals to pollId
        defaultEventPollOptionShouldBeFound("pollId.equals=" + pollId);

        // Get all the eventPollOptionList where poll equals to (pollId + 1)
        defaultEventPollOptionShouldNotBeFound("pollId.equals=" + (pollId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventPollOptionShouldBeFound(String filter) throws Exception {
        restEventPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPollOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].optionText").value(hasItem(DEFAULT_OPTION_TEXT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventPollOptionShouldNotBeFound(String filter) throws Exception {
        restEventPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventPollOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventPollOption() throws Exception {
        // Get the eventPollOption
        restEventPollOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventPollOption() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();

        // Update the eventPollOption
        EventPollOption updatedEventPollOption = eventPollOptionRepository.findById(eventPollOption.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventPollOption are not directly saved in db
        em.detach(updatedEventPollOption);
        updatedEventPollOption
            .tenantId(UPDATED_TENANT_ID)
            .optionText(UPDATED_OPTION_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(updatedEventPollOption);

        restEventPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPollOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
        EventPollOption testEventPollOption = eventPollOptionList.get(eventPollOptionList.size() - 1);
        assertThat(testEventPollOption.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPollOption.getOptionText()).isEqualTo(UPDATED_OPTION_TEXT);
        assertThat(testEventPollOption.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPollOption.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventPollOption() throws Exception {
        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();
        eventPollOption.setId(longCount.incrementAndGet());

        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPollOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventPollOption() throws Exception {
        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();
        eventPollOption.setId(longCount.incrementAndGet());

        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventPollOption() throws Exception {
        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();
        eventPollOption.setId(longCount.incrementAndGet());

        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollOptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventPollOptionWithPatch() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();

        // Update the eventPollOption using partial update
        EventPollOption partialUpdatedEventPollOption = new EventPollOption();
        partialUpdatedEventPollOption.setId(eventPollOption.getId());

        partialUpdatedEventPollOption.tenantId(UPDATED_TENANT_ID).optionText(UPDATED_OPTION_TEXT).createdAt(UPDATED_CREATED_AT);

        restEventPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPollOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPollOption))
            )
            .andExpect(status().isOk());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
        EventPollOption testEventPollOption = eventPollOptionList.get(eventPollOptionList.size() - 1);
        assertThat(testEventPollOption.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPollOption.getOptionText()).isEqualTo(UPDATED_OPTION_TEXT);
        assertThat(testEventPollOption.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPollOption.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventPollOptionWithPatch() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();

        // Update the eventPollOption using partial update
        EventPollOption partialUpdatedEventPollOption = new EventPollOption();
        partialUpdatedEventPollOption.setId(eventPollOption.getId());

        partialUpdatedEventPollOption
            .tenantId(UPDATED_TENANT_ID)
            .optionText(UPDATED_OPTION_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPollOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPollOption))
            )
            .andExpect(status().isOk());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
        EventPollOption testEventPollOption = eventPollOptionList.get(eventPollOptionList.size() - 1);
        assertThat(testEventPollOption.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPollOption.getOptionText()).isEqualTo(UPDATED_OPTION_TEXT);
        assertThat(testEventPollOption.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPollOption.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventPollOption() throws Exception {
        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();
        eventPollOption.setId(longCount.incrementAndGet());

        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventPollOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventPollOption() throws Exception {
        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();
        eventPollOption.setId(longCount.incrementAndGet());

        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventPollOption() throws Exception {
        int databaseSizeBeforeUpdate = eventPollOptionRepository.findAll().size();
        eventPollOption.setId(longCount.incrementAndGet());

        // Create the EventPollOption
        EventPollOptionDTO eventPollOptionDTO = eventPollOptionMapper.toDto(eventPollOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollOptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPollOption in the database
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventPollOption() throws Exception {
        // Initialize the database
        eventPollOptionRepository.saveAndFlush(eventPollOption);

        int databaseSizeBeforeDelete = eventPollOptionRepository.findAll().size();

        // Delete the eventPollOption
        restEventPollOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventPollOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventPollOption> eventPollOptionList = eventPollOptionRepository.findAll();
        assertThat(eventPollOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
