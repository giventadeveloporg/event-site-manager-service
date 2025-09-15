package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventPoll;
import com.nextjstemplate.domain.EventPollOption;
import com.nextjstemplate.domain.EventPollResponse;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventPollResponseRepository;
import com.nextjstemplate.service.dto.EventPollResponseDTO;
import com.nextjstemplate.service.mapper.EventPollResponseMapper;
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
 * Integration tests for the {@link EventPollResponseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventPollResponseResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-poll-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventPollResponseRepository eventPollResponseRepository;

    @Autowired
    private EventPollResponseMapper eventPollResponseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventPollResponseMockMvc;

    private EventPollResponse eventPollResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPollResponse createEntity(EntityManager em) {
        EventPollResponse eventPollResponse = new EventPollResponse()
            .tenantId(DEFAULT_TENANT_ID)
            .comment(DEFAULT_COMMENT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventPollResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPollResponse createUpdatedEntity(EntityManager em) {
        EventPollResponse eventPollResponse = new EventPollResponse()
            .tenantId(UPDATED_TENANT_ID)
            .comment(UPDATED_COMMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventPollResponse;
    }

    @BeforeEach
    public void initTest() {
        eventPollResponse = createEntity(em);
    }

    @Test
    @Transactional
    void createEventPollResponse() throws Exception {
        int databaseSizeBeforeCreate = eventPollResponseRepository.findAll().size();
        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);
        restEventPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeCreate + 1);
        EventPollResponse testEventPollResponse = eventPollResponseList.get(eventPollResponseList.size() - 1);
        assertThat(testEventPollResponse.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventPollResponse.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testEventPollResponse.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventPollResponse.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventPollResponseWithExistingId() throws Exception {
        // Create the EventPollResponse with an existing ID
        eventPollResponse.setId(1L);
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        int databaseSizeBeforeCreate = eventPollResponseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollResponseRepository.findAll().size();
        // set the field null
        eventPollResponse.setCreatedAt(null);

        // Create the EventPollResponse, which fails.
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        restEventPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventPollResponseRepository.findAll().size();
        // set the field null
        eventPollResponse.setUpdatedAt(null);

        // Create the EventPollResponse, which fails.
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        restEventPollResponseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventPollResponses() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList
        restEventPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPollResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventPollResponse() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get the eventPollResponse
        restEventPollResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, eventPollResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventPollResponse.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventPollResponsesByIdFiltering() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        Long id = eventPollResponse.getId();

        defaultEventPollResponseShouldBeFound("id.equals=" + id);
        defaultEventPollResponseShouldNotBeFound("id.notEquals=" + id);

        defaultEventPollResponseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventPollResponseShouldNotBeFound("id.greaterThan=" + id);

        defaultEventPollResponseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventPollResponseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventPollResponseShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventPollResponseList where tenantId equals to UPDATED_TENANT_ID
        defaultEventPollResponseShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventPollResponseShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventPollResponseList where tenantId equals to UPDATED_TENANT_ID
        defaultEventPollResponseShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where tenantId is not null
        defaultEventPollResponseShouldBeFound("tenantId.specified=true");

        // Get all the eventPollResponseList where tenantId is null
        defaultEventPollResponseShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where tenantId contains DEFAULT_TENANT_ID
        defaultEventPollResponseShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventPollResponseList where tenantId contains UPDATED_TENANT_ID
        defaultEventPollResponseShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventPollResponseShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventPollResponseList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventPollResponseShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where comment equals to DEFAULT_COMMENT
        defaultEventPollResponseShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the eventPollResponseList where comment equals to UPDATED_COMMENT
        defaultEventPollResponseShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultEventPollResponseShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the eventPollResponseList where comment equals to UPDATED_COMMENT
        defaultEventPollResponseShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where comment is not null
        defaultEventPollResponseShouldBeFound("comment.specified=true");

        // Get all the eventPollResponseList where comment is null
        defaultEventPollResponseShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCommentContainsSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where comment contains DEFAULT_COMMENT
        defaultEventPollResponseShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the eventPollResponseList where comment contains UPDATED_COMMENT
        defaultEventPollResponseShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where comment does not contain DEFAULT_COMMENT
        defaultEventPollResponseShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the eventPollResponseList where comment does not contain UPDATED_COMMENT
        defaultEventPollResponseShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventPollResponseShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventPollResponseList where createdAt equals to UPDATED_CREATED_AT
        defaultEventPollResponseShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventPollResponseShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventPollResponseList where createdAt equals to UPDATED_CREATED_AT
        defaultEventPollResponseShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt is not null
        defaultEventPollResponseShouldBeFound("createdAt.specified=true");

        // Get all the eventPollResponseList where createdAt is null
        defaultEventPollResponseShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventPollResponseShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventPollResponseList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventPollResponseShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventPollResponseShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventPollResponseList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventPollResponseShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventPollResponseShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventPollResponseList where createdAt is less than UPDATED_CREATED_AT
        defaultEventPollResponseShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventPollResponseShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventPollResponseList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventPollResponseShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventPollResponseShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollResponseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventPollResponseShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventPollResponseShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventPollResponseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventPollResponseShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt is not null
        defaultEventPollResponseShouldBeFound("updatedAt.specified=true");

        // Get all the eventPollResponseList where updatedAt is null
        defaultEventPollResponseShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventPollResponseShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollResponseList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventPollResponseShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventPollResponseShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollResponseList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventPollResponseShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventPollResponseShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollResponseList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventPollResponseShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        // Get all the eventPollResponseList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventPollResponseShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventPollResponseList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventPollResponseShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByPollIsEqualToSomething() throws Exception {
        EventPoll poll;
        if (TestUtil.findAll(em, EventPoll.class).isEmpty()) {
            eventPollResponseRepository.saveAndFlush(eventPollResponse);
            poll = EventPollResourceIT.createEntity(em);
        } else {
            poll = TestUtil.findAll(em, EventPoll.class).get(0);
        }
        em.persist(poll);
        em.flush();
        eventPollResponse.setPoll(poll);
        eventPollResponseRepository.saveAndFlush(eventPollResponse);
        Long pollId = poll.getId();
        // Get all the eventPollResponseList where poll equals to pollId
        defaultEventPollResponseShouldBeFound("pollId.equals=" + pollId);

        // Get all the eventPollResponseList where poll equals to (pollId + 1)
        defaultEventPollResponseShouldNotBeFound("pollId.equals=" + (pollId + 1));
    }

    @Test
    @Transactional
    void getAllEventPollResponsesByPollOptionIsEqualToSomething() throws Exception {
        EventPollOption pollOption;
        if (TestUtil.findAll(em, EventPollOption.class).isEmpty()) {
            eventPollResponseRepository.saveAndFlush(eventPollResponse);
            pollOption = EventPollOptionResourceIT.createEntity(em);
        } else {
            pollOption = TestUtil.findAll(em, EventPollOption.class).get(0);
        }
        em.persist(pollOption);
        em.flush();
        eventPollResponse.setPollOption(pollOption);
        eventPollResponseRepository.saveAndFlush(eventPollResponse);
        Long pollOptionId = pollOption.getId();
        // Get all the eventPollResponseList where pollOption equals to pollOptionId
        defaultEventPollResponseShouldBeFound("pollOptionId.equals=" + pollOptionId);

        // Get all the eventPollResponseList where pollOption equals to (pollOptionId + 1)
        defaultEventPollResponseShouldNotBeFound("pollOptionId.equals=" + (pollOptionId + 1));
    }

    /* @Test
    @Transactional
    void getAllEventPollResponsesByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventPollResponseRepository.saveAndFlush(eventPollResponse);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        eventPollResponse.setUser(user);
        eventPollResponseRepository.saveAndFlush(eventPollResponse);
        Long userId = user.getId();
        // Get all the eventPollResponseList where user equals to userId
        defaultEventPollResponseShouldBeFound("userId.equals=" + userId);

        // Get all the eventPollResponseList where user equals to (userId + 1)
        defaultEventPollResponseShouldNotBeFound("userId.equals=" + (userId + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventPollResponseShouldBeFound(String filter) throws Exception {
        restEventPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPollResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventPollResponseShouldNotBeFound(String filter) throws Exception {
        restEventPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventPollResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventPollResponse() throws Exception {
        // Get the eventPollResponse
        restEventPollResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventPollResponse() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();

        // Update the eventPollResponse
        EventPollResponse updatedEventPollResponse = eventPollResponseRepository.findById(eventPollResponse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventPollResponse are not directly saved in db
        em.detach(updatedEventPollResponse);
        updatedEventPollResponse
            .tenantId(UPDATED_TENANT_ID)
            .comment(UPDATED_COMMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(updatedEventPollResponse);

        restEventPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPollResponseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
        EventPollResponse testEventPollResponse = eventPollResponseList.get(eventPollResponseList.size() - 1);
        assertThat(testEventPollResponse.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPollResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testEventPollResponse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPollResponse.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();
        eventPollResponse.setId(longCount.incrementAndGet());

        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPollResponseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();
        eventPollResponse.setId(longCount.incrementAndGet());

        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();
        eventPollResponse.setId(longCount.incrementAndGet());

        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollResponseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventPollResponseWithPatch() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();

        // Update the eventPollResponse using partial update
        EventPollResponse partialUpdatedEventPollResponse = new EventPollResponse();
        partialUpdatedEventPollResponse.setId(eventPollResponse.getId());

        partialUpdatedEventPollResponse.createdAt(UPDATED_CREATED_AT);

        restEventPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPollResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPollResponse))
            )
            .andExpect(status().isOk());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
        EventPollResponse testEventPollResponse = eventPollResponseList.get(eventPollResponseList.size() - 1);
        assertThat(testEventPollResponse.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventPollResponse.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testEventPollResponse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPollResponse.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventPollResponseWithPatch() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();

        // Update the eventPollResponse using partial update
        EventPollResponse partialUpdatedEventPollResponse = new EventPollResponse();
        partialUpdatedEventPollResponse.setId(eventPollResponse.getId());

        partialUpdatedEventPollResponse
            .tenantId(UPDATED_TENANT_ID)
            .comment(UPDATED_COMMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPollResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPollResponse))
            )
            .andExpect(status().isOk());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
        EventPollResponse testEventPollResponse = eventPollResponseList.get(eventPollResponseList.size() - 1);
        assertThat(testEventPollResponse.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventPollResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testEventPollResponse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventPollResponse.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();
        eventPollResponse.setId(longCount.incrementAndGet());

        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventPollResponseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();
        eventPollResponse.setId(longCount.incrementAndGet());

        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventPollResponse() throws Exception {
        int databaseSizeBeforeUpdate = eventPollResponseRepository.findAll().size();
        eventPollResponse.setId(longCount.incrementAndGet());

        // Create the EventPollResponse
        EventPollResponseDTO eventPollResponseDTO = eventPollResponseMapper.toDto(eventPollResponse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPollResponseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPollResponseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPollResponse in the database
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventPollResponse() throws Exception {
        // Initialize the database
        eventPollResponseRepository.saveAndFlush(eventPollResponse);

        int databaseSizeBeforeDelete = eventPollResponseRepository.findAll().size();

        // Delete the eventPollResponse
        restEventPollResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventPollResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventPollResponse> eventPollResponseList = eventPollResponseRepository.findAll();
        assertThat(eventPollResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
