package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventScoreCard;
import com.eventsitemanager.repository.EventScoreCardRepository;
import com.eventsitemanager.service.dto.EventScoreCardDTO;
import com.eventsitemanager.service.mapper.EventScoreCardMapper;
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
 * Integration tests for the {@link EventScoreCardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventScoreCardResourceIT {

    private static final String DEFAULT_TEAM_A_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_A_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_B_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_B_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEAM_A_SCORE = 1;
    private static final Integer UPDATED_TEAM_A_SCORE = 2;
    private static final Integer SMALLER_TEAM_A_SCORE = 1 - 1;

    private static final Integer DEFAULT_TEAM_B_SCORE = 1;
    private static final Integer UPDATED_TEAM_B_SCORE = 2;
    private static final Integer SMALLER_TEAM_B_SCORE = 1 - 1;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-score-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventScoreCardRepository eventScoreCardRepository;

    @Autowired
    private EventScoreCardMapper eventScoreCardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventScoreCardMockMvc;

    private EventScoreCard eventScoreCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventScoreCard createEntity(EntityManager em) {
        EventScoreCard eventScoreCard = new EventScoreCard()
            .teamAName(DEFAULT_TEAM_A_NAME)
            .teamBName(DEFAULT_TEAM_B_NAME)
            .teamAScore(DEFAULT_TEAM_A_SCORE)
            .teamBScore(DEFAULT_TEAM_B_SCORE)
            .remarks(DEFAULT_REMARKS)
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
        eventScoreCard.setEvent(eventDetails);
        return eventScoreCard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventScoreCard createUpdatedEntity(EntityManager em) {
        EventScoreCard eventScoreCard = new EventScoreCard()
            .teamAName(UPDATED_TEAM_A_NAME)
            .teamBName(UPDATED_TEAM_B_NAME)
            .teamAScore(UPDATED_TEAM_A_SCORE)
            .teamBScore(UPDATED_TEAM_B_SCORE)
            .remarks(UPDATED_REMARKS)
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
        eventScoreCard.setEvent(eventDetails);
        return eventScoreCard;
    }

    @BeforeEach
    public void initTest() {
        eventScoreCard = createEntity(em);
    }

    @Test
    @Transactional
    void createEventScoreCard() throws Exception {
        int databaseSizeBeforeCreate = eventScoreCardRepository.findAll().size();
        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);
        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeCreate + 1);
        EventScoreCard testEventScoreCard = eventScoreCardList.get(eventScoreCardList.size() - 1);
        assertThat(testEventScoreCard.getTeamAName()).isEqualTo(DEFAULT_TEAM_A_NAME);
        assertThat(testEventScoreCard.getTeamBName()).isEqualTo(DEFAULT_TEAM_B_NAME);
        assertThat(testEventScoreCard.getTeamAScore()).isEqualTo(DEFAULT_TEAM_A_SCORE);
        assertThat(testEventScoreCard.getTeamBScore()).isEqualTo(DEFAULT_TEAM_B_SCORE);
        assertThat(testEventScoreCard.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEventScoreCard.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventScoreCard.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventScoreCardWithExistingId() throws Exception {
        // Create the EventScoreCard with an existing ID
        eventScoreCard.setId(1L);
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        int databaseSizeBeforeCreate = eventScoreCardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTeamANameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardRepository.findAll().size();
        // set the field null
        eventScoreCard.setTeamAName(null);

        // Create the EventScoreCard, which fails.
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeamBNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardRepository.findAll().size();
        // set the field null
        eventScoreCard.setTeamBName(null);

        // Create the EventScoreCard, which fails.
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeamAScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardRepository.findAll().size();
        // set the field null
        eventScoreCard.setTeamAScore(null);

        // Create the EventScoreCard, which fails.
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTeamBScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardRepository.findAll().size();
        // set the field null
        eventScoreCard.setTeamBScore(null);

        // Create the EventScoreCard, which fails.
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardRepository.findAll().size();
        // set the field null
        eventScoreCard.setCreatedAt(null);

        // Create the EventScoreCard, which fails.
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardRepository.findAll().size();
        // set the field null
        eventScoreCard.setUpdatedAt(null);

        // Create the EventScoreCard, which fails.
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventScoreCards() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList
        restEventScoreCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventScoreCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamAName").value(hasItem(DEFAULT_TEAM_A_NAME)))
            .andExpect(jsonPath("$.[*].teamBName").value(hasItem(DEFAULT_TEAM_B_NAME)))
            .andExpect(jsonPath("$.[*].teamAScore").value(hasItem(DEFAULT_TEAM_A_SCORE)))
            .andExpect(jsonPath("$.[*].teamBScore").value(hasItem(DEFAULT_TEAM_B_SCORE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventScoreCard() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get the eventScoreCard
        restEventScoreCardMockMvc
            .perform(get(ENTITY_API_URL_ID, eventScoreCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventScoreCard.getId().intValue()))
            .andExpect(jsonPath("$.teamAName").value(DEFAULT_TEAM_A_NAME))
            .andExpect(jsonPath("$.teamBName").value(DEFAULT_TEAM_B_NAME))
            .andExpect(jsonPath("$.teamAScore").value(DEFAULT_TEAM_A_SCORE))
            .andExpect(jsonPath("$.teamBScore").value(DEFAULT_TEAM_B_SCORE))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventScoreCardsByIdFiltering() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        Long id = eventScoreCard.getId();

        defaultEventScoreCardShouldBeFound("id.equals=" + id);
        defaultEventScoreCardShouldNotBeFound("id.notEquals=" + id);

        defaultEventScoreCardShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventScoreCardShouldNotBeFound("id.greaterThan=" + id);

        defaultEventScoreCardShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventScoreCardShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamANameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAName equals to DEFAULT_TEAM_A_NAME
        defaultEventScoreCardShouldBeFound("teamAName.equals=" + DEFAULT_TEAM_A_NAME);

        // Get all the eventScoreCardList where teamAName equals to UPDATED_TEAM_A_NAME
        defaultEventScoreCardShouldNotBeFound("teamAName.equals=" + UPDATED_TEAM_A_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamANameIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAName in DEFAULT_TEAM_A_NAME or UPDATED_TEAM_A_NAME
        defaultEventScoreCardShouldBeFound("teamAName.in=" + DEFAULT_TEAM_A_NAME + "," + UPDATED_TEAM_A_NAME);

        // Get all the eventScoreCardList where teamAName equals to UPDATED_TEAM_A_NAME
        defaultEventScoreCardShouldNotBeFound("teamAName.in=" + UPDATED_TEAM_A_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamANameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAName is not null
        defaultEventScoreCardShouldBeFound("teamAName.specified=true");

        // Get all the eventScoreCardList where teamAName is null
        defaultEventScoreCardShouldNotBeFound("teamAName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamANameContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAName contains DEFAULT_TEAM_A_NAME
        defaultEventScoreCardShouldBeFound("teamAName.contains=" + DEFAULT_TEAM_A_NAME);

        // Get all the eventScoreCardList where teamAName contains UPDATED_TEAM_A_NAME
        defaultEventScoreCardShouldNotBeFound("teamAName.contains=" + UPDATED_TEAM_A_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamANameNotContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAName does not contain DEFAULT_TEAM_A_NAME
        defaultEventScoreCardShouldNotBeFound("teamAName.doesNotContain=" + DEFAULT_TEAM_A_NAME);

        // Get all the eventScoreCardList where teamAName does not contain UPDATED_TEAM_A_NAME
        defaultEventScoreCardShouldBeFound("teamAName.doesNotContain=" + UPDATED_TEAM_A_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBName equals to DEFAULT_TEAM_B_NAME
        defaultEventScoreCardShouldBeFound("teamBName.equals=" + DEFAULT_TEAM_B_NAME);

        // Get all the eventScoreCardList where teamBName equals to UPDATED_TEAM_B_NAME
        defaultEventScoreCardShouldNotBeFound("teamBName.equals=" + UPDATED_TEAM_B_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBName in DEFAULT_TEAM_B_NAME or UPDATED_TEAM_B_NAME
        defaultEventScoreCardShouldBeFound("teamBName.in=" + DEFAULT_TEAM_B_NAME + "," + UPDATED_TEAM_B_NAME);

        // Get all the eventScoreCardList where teamBName equals to UPDATED_TEAM_B_NAME
        defaultEventScoreCardShouldNotBeFound("teamBName.in=" + UPDATED_TEAM_B_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBName is not null
        defaultEventScoreCardShouldBeFound("teamBName.specified=true");

        // Get all the eventScoreCardList where teamBName is null
        defaultEventScoreCardShouldNotBeFound("teamBName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBNameContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBName contains DEFAULT_TEAM_B_NAME
        defaultEventScoreCardShouldBeFound("teamBName.contains=" + DEFAULT_TEAM_B_NAME);

        // Get all the eventScoreCardList where teamBName contains UPDATED_TEAM_B_NAME
        defaultEventScoreCardShouldNotBeFound("teamBName.contains=" + UPDATED_TEAM_B_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBName does not contain DEFAULT_TEAM_B_NAME
        defaultEventScoreCardShouldNotBeFound("teamBName.doesNotContain=" + DEFAULT_TEAM_B_NAME);

        // Get all the eventScoreCardList where teamBName does not contain UPDATED_TEAM_B_NAME
        defaultEventScoreCardShouldBeFound("teamBName.doesNotContain=" + UPDATED_TEAM_B_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore equals to DEFAULT_TEAM_A_SCORE
        defaultEventScoreCardShouldBeFound("teamAScore.equals=" + DEFAULT_TEAM_A_SCORE);

        // Get all the eventScoreCardList where teamAScore equals to UPDATED_TEAM_A_SCORE
        defaultEventScoreCardShouldNotBeFound("teamAScore.equals=" + UPDATED_TEAM_A_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore in DEFAULT_TEAM_A_SCORE or UPDATED_TEAM_A_SCORE
        defaultEventScoreCardShouldBeFound("teamAScore.in=" + DEFAULT_TEAM_A_SCORE + "," + UPDATED_TEAM_A_SCORE);

        // Get all the eventScoreCardList where teamAScore equals to UPDATED_TEAM_A_SCORE
        defaultEventScoreCardShouldNotBeFound("teamAScore.in=" + UPDATED_TEAM_A_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore is not null
        defaultEventScoreCardShouldBeFound("teamAScore.specified=true");

        // Get all the eventScoreCardList where teamAScore is null
        defaultEventScoreCardShouldNotBeFound("teamAScore.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore is greater than or equal to DEFAULT_TEAM_A_SCORE
        defaultEventScoreCardShouldBeFound("teamAScore.greaterThanOrEqual=" + DEFAULT_TEAM_A_SCORE);

        // Get all the eventScoreCardList where teamAScore is greater than or equal to UPDATED_TEAM_A_SCORE
        defaultEventScoreCardShouldNotBeFound("teamAScore.greaterThanOrEqual=" + UPDATED_TEAM_A_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore is less than or equal to DEFAULT_TEAM_A_SCORE
        defaultEventScoreCardShouldBeFound("teamAScore.lessThanOrEqual=" + DEFAULT_TEAM_A_SCORE);

        // Get all the eventScoreCardList where teamAScore is less than or equal to SMALLER_TEAM_A_SCORE
        defaultEventScoreCardShouldNotBeFound("teamAScore.lessThanOrEqual=" + SMALLER_TEAM_A_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore is less than DEFAULT_TEAM_A_SCORE
        defaultEventScoreCardShouldNotBeFound("teamAScore.lessThan=" + DEFAULT_TEAM_A_SCORE);

        // Get all the eventScoreCardList where teamAScore is less than UPDATED_TEAM_A_SCORE
        defaultEventScoreCardShouldBeFound("teamAScore.lessThan=" + UPDATED_TEAM_A_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamAScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamAScore is greater than DEFAULT_TEAM_A_SCORE
        defaultEventScoreCardShouldNotBeFound("teamAScore.greaterThan=" + DEFAULT_TEAM_A_SCORE);

        // Get all the eventScoreCardList where teamAScore is greater than SMALLER_TEAM_A_SCORE
        defaultEventScoreCardShouldBeFound("teamAScore.greaterThan=" + SMALLER_TEAM_A_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore equals to DEFAULT_TEAM_B_SCORE
        defaultEventScoreCardShouldBeFound("teamBScore.equals=" + DEFAULT_TEAM_B_SCORE);

        // Get all the eventScoreCardList where teamBScore equals to UPDATED_TEAM_B_SCORE
        defaultEventScoreCardShouldNotBeFound("teamBScore.equals=" + UPDATED_TEAM_B_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore in DEFAULT_TEAM_B_SCORE or UPDATED_TEAM_B_SCORE
        defaultEventScoreCardShouldBeFound("teamBScore.in=" + DEFAULT_TEAM_B_SCORE + "," + UPDATED_TEAM_B_SCORE);

        // Get all the eventScoreCardList where teamBScore equals to UPDATED_TEAM_B_SCORE
        defaultEventScoreCardShouldNotBeFound("teamBScore.in=" + UPDATED_TEAM_B_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore is not null
        defaultEventScoreCardShouldBeFound("teamBScore.specified=true");

        // Get all the eventScoreCardList where teamBScore is null
        defaultEventScoreCardShouldNotBeFound("teamBScore.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore is greater than or equal to DEFAULT_TEAM_B_SCORE
        defaultEventScoreCardShouldBeFound("teamBScore.greaterThanOrEqual=" + DEFAULT_TEAM_B_SCORE);

        // Get all the eventScoreCardList where teamBScore is greater than or equal to UPDATED_TEAM_B_SCORE
        defaultEventScoreCardShouldNotBeFound("teamBScore.greaterThanOrEqual=" + UPDATED_TEAM_B_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore is less than or equal to DEFAULT_TEAM_B_SCORE
        defaultEventScoreCardShouldBeFound("teamBScore.lessThanOrEqual=" + DEFAULT_TEAM_B_SCORE);

        // Get all the eventScoreCardList where teamBScore is less than or equal to SMALLER_TEAM_B_SCORE
        defaultEventScoreCardShouldNotBeFound("teamBScore.lessThanOrEqual=" + SMALLER_TEAM_B_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore is less than DEFAULT_TEAM_B_SCORE
        defaultEventScoreCardShouldNotBeFound("teamBScore.lessThan=" + DEFAULT_TEAM_B_SCORE);

        // Get all the eventScoreCardList where teamBScore is less than UPDATED_TEAM_B_SCORE
        defaultEventScoreCardShouldBeFound("teamBScore.lessThan=" + UPDATED_TEAM_B_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByTeamBScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where teamBScore is greater than DEFAULT_TEAM_B_SCORE
        defaultEventScoreCardShouldNotBeFound("teamBScore.greaterThan=" + DEFAULT_TEAM_B_SCORE);

        // Get all the eventScoreCardList where teamBScore is greater than SMALLER_TEAM_B_SCORE
        defaultEventScoreCardShouldBeFound("teamBScore.greaterThan=" + SMALLER_TEAM_B_SCORE);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where remarks equals to DEFAULT_REMARKS
        defaultEventScoreCardShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the eventScoreCardList where remarks equals to UPDATED_REMARKS
        defaultEventScoreCardShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultEventScoreCardShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the eventScoreCardList where remarks equals to UPDATED_REMARKS
        defaultEventScoreCardShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where remarks is not null
        defaultEventScoreCardShouldBeFound("remarks.specified=true");

        // Get all the eventScoreCardList where remarks is null
        defaultEventScoreCardShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where remarks contains DEFAULT_REMARKS
        defaultEventScoreCardShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the eventScoreCardList where remarks contains UPDATED_REMARKS
        defaultEventScoreCardShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where remarks does not contain DEFAULT_REMARKS
        defaultEventScoreCardShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the eventScoreCardList where remarks does not contain UPDATED_REMARKS
        defaultEventScoreCardShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventScoreCardShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardList where createdAt equals to UPDATED_CREATED_AT
        defaultEventScoreCardShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventScoreCardShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventScoreCardList where createdAt equals to UPDATED_CREATED_AT
        defaultEventScoreCardShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt is not null
        defaultEventScoreCardShouldBeFound("createdAt.specified=true");

        // Get all the eventScoreCardList where createdAt is null
        defaultEventScoreCardShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventScoreCardShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventScoreCardShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventScoreCardShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventScoreCardShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventScoreCardShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardList where createdAt is less than UPDATED_CREATED_AT
        defaultEventScoreCardShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventScoreCardShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventScoreCardShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventScoreCardShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventScoreCardShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventScoreCardShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventScoreCardList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventScoreCardShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt is not null
        defaultEventScoreCardShouldBeFound("updatedAt.specified=true");

        // Get all the eventScoreCardList where updatedAt is null
        defaultEventScoreCardShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventScoreCardShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventScoreCardShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventScoreCardShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventScoreCardShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventScoreCardShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventScoreCardShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        // Get all the eventScoreCardList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventScoreCardShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventScoreCardShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardsByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventScoreCardRepository.saveAndFlush(eventScoreCard);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventScoreCard.setEvent(event);
        eventScoreCardRepository.saveAndFlush(eventScoreCard);
        Long eventId = event.getId();
        // Get all the eventScoreCardList where event equals to eventId
        defaultEventScoreCardShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventScoreCardList where event equals to (eventId + 1)
        defaultEventScoreCardShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventScoreCardShouldBeFound(String filter) throws Exception {
        restEventScoreCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventScoreCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamAName").value(hasItem(DEFAULT_TEAM_A_NAME)))
            .andExpect(jsonPath("$.[*].teamBName").value(hasItem(DEFAULT_TEAM_B_NAME)))
            .andExpect(jsonPath("$.[*].teamAScore").value(hasItem(DEFAULT_TEAM_A_SCORE)))
            .andExpect(jsonPath("$.[*].teamBScore").value(hasItem(DEFAULT_TEAM_B_SCORE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventScoreCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventScoreCardShouldNotBeFound(String filter) throws Exception {
        restEventScoreCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventScoreCardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventScoreCard() throws Exception {
        // Get the eventScoreCard
        restEventScoreCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventScoreCard() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();

        // Update the eventScoreCard
        EventScoreCard updatedEventScoreCard = eventScoreCardRepository.findById(eventScoreCard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventScoreCard are not directly saved in db
        em.detach(updatedEventScoreCard);
        updatedEventScoreCard
            .teamAName(UPDATED_TEAM_A_NAME)
            .teamBName(UPDATED_TEAM_B_NAME)
            .teamAScore(UPDATED_TEAM_A_SCORE)
            .teamBScore(UPDATED_TEAM_B_SCORE)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(updatedEventScoreCard);

        restEventScoreCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventScoreCardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
        EventScoreCard testEventScoreCard = eventScoreCardList.get(eventScoreCardList.size() - 1);
        assertThat(testEventScoreCard.getTeamAName()).isEqualTo(UPDATED_TEAM_A_NAME);
        assertThat(testEventScoreCard.getTeamBName()).isEqualTo(UPDATED_TEAM_B_NAME);
        assertThat(testEventScoreCard.getTeamAScore()).isEqualTo(UPDATED_TEAM_A_SCORE);
        assertThat(testEventScoreCard.getTeamBScore()).isEqualTo(UPDATED_TEAM_B_SCORE);
        assertThat(testEventScoreCard.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEventScoreCard.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventScoreCard.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventScoreCard() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();
        eventScoreCard.setId(longCount.incrementAndGet());

        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventScoreCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventScoreCardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventScoreCard() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();
        eventScoreCard.setId(longCount.incrementAndGet());

        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventScoreCard() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();
        eventScoreCard.setId(longCount.incrementAndGet());

        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventScoreCardWithPatch() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();

        // Update the eventScoreCard using partial update
        EventScoreCard partialUpdatedEventScoreCard = new EventScoreCard();
        partialUpdatedEventScoreCard.setId(eventScoreCard.getId());

        partialUpdatedEventScoreCard
            .teamAName(UPDATED_TEAM_A_NAME)
            .teamBName(UPDATED_TEAM_B_NAME)
            .teamAScore(UPDATED_TEAM_A_SCORE)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventScoreCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventScoreCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventScoreCard))
            )
            .andExpect(status().isOk());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
        EventScoreCard testEventScoreCard = eventScoreCardList.get(eventScoreCardList.size() - 1);
        assertThat(testEventScoreCard.getTeamAName()).isEqualTo(UPDATED_TEAM_A_NAME);
        assertThat(testEventScoreCard.getTeamBName()).isEqualTo(UPDATED_TEAM_B_NAME);
        assertThat(testEventScoreCard.getTeamAScore()).isEqualTo(UPDATED_TEAM_A_SCORE);
        assertThat(testEventScoreCard.getTeamBScore()).isEqualTo(DEFAULT_TEAM_B_SCORE);
        assertThat(testEventScoreCard.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEventScoreCard.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventScoreCard.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventScoreCardWithPatch() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();

        // Update the eventScoreCard using partial update
        EventScoreCard partialUpdatedEventScoreCard = new EventScoreCard();
        partialUpdatedEventScoreCard.setId(eventScoreCard.getId());

        partialUpdatedEventScoreCard
            .teamAName(UPDATED_TEAM_A_NAME)
            .teamBName(UPDATED_TEAM_B_NAME)
            .teamAScore(UPDATED_TEAM_A_SCORE)
            .teamBScore(UPDATED_TEAM_B_SCORE)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventScoreCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventScoreCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventScoreCard))
            )
            .andExpect(status().isOk());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
        EventScoreCard testEventScoreCard = eventScoreCardList.get(eventScoreCardList.size() - 1);
        assertThat(testEventScoreCard.getTeamAName()).isEqualTo(UPDATED_TEAM_A_NAME);
        assertThat(testEventScoreCard.getTeamBName()).isEqualTo(UPDATED_TEAM_B_NAME);
        assertThat(testEventScoreCard.getTeamAScore()).isEqualTo(UPDATED_TEAM_A_SCORE);
        assertThat(testEventScoreCard.getTeamBScore()).isEqualTo(UPDATED_TEAM_B_SCORE);
        assertThat(testEventScoreCard.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEventScoreCard.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventScoreCard.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventScoreCard() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();
        eventScoreCard.setId(longCount.incrementAndGet());

        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventScoreCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventScoreCardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventScoreCard() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();
        eventScoreCard.setId(longCount.incrementAndGet());

        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventScoreCard() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardRepository.findAll().size();
        eventScoreCard.setId(longCount.incrementAndGet());

        // Create the EventScoreCard
        EventScoreCardDTO eventScoreCardDTO = eventScoreCardMapper.toDto(eventScoreCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventScoreCard in the database
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventScoreCard() throws Exception {
        // Initialize the database
        eventScoreCardRepository.saveAndFlush(eventScoreCard);

        int databaseSizeBeforeDelete = eventScoreCardRepository.findAll().size();

        // Delete the eventScoreCard
        restEventScoreCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventScoreCard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventScoreCard> eventScoreCardList = eventScoreCardRepository.findAll();
        assertThat(eventScoreCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
