package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventScoreCard;
import com.eventsitemanager.domain.EventScoreCardDetail;
import com.eventsitemanager.repository.EventScoreCardDetailRepository;
import com.eventsitemanager.service.dto.EventScoreCardDetailDTO;
import com.eventsitemanager.service.mapper.EventScoreCardDetailMapper;
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
 * Integration tests for the {@link EventScoreCardDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventScoreCardDetailResourceIT {

    private static final String DEFAULT_TEAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAYER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-score-card-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventScoreCardDetailRepository eventScoreCardDetailRepository;

    @Autowired
    private EventScoreCardDetailMapper eventScoreCardDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventScoreCardDetailMockMvc;

    private EventScoreCardDetail eventScoreCardDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventScoreCardDetail createEntity(EntityManager em) {
        EventScoreCardDetail eventScoreCardDetail = new EventScoreCardDetail()
            .teamName(DEFAULT_TEAM_NAME)
            .playerName(DEFAULT_PLAYER_NAME)
            .points(DEFAULT_POINTS)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        EventScoreCard eventScoreCard;
        if (TestUtil.findAll(em, EventScoreCard.class).isEmpty()) {
            eventScoreCard = EventScoreCardResourceIT.createEntity(em);
            em.persist(eventScoreCard);
            em.flush();
        } else {
            eventScoreCard = TestUtil.findAll(em, EventScoreCard.class).get(0);
        }
        eventScoreCardDetail.setScoreCard(eventScoreCard);
        return eventScoreCardDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventScoreCardDetail createUpdatedEntity(EntityManager em) {
        EventScoreCardDetail eventScoreCardDetail = new EventScoreCardDetail()
            .teamName(UPDATED_TEAM_NAME)
            .playerName(UPDATED_PLAYER_NAME)
            .points(UPDATED_POINTS)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        EventScoreCard eventScoreCard;
        if (TestUtil.findAll(em, EventScoreCard.class).isEmpty()) {
            eventScoreCard = EventScoreCardResourceIT.createUpdatedEntity(em);
            em.persist(eventScoreCard);
            em.flush();
        } else {
            eventScoreCard = TestUtil.findAll(em, EventScoreCard.class).get(0);
        }
        eventScoreCardDetail.setScoreCard(eventScoreCard);
        return eventScoreCardDetail;
    }

    @BeforeEach
    public void initTest() {
        eventScoreCardDetail = createEntity(em);
    }

    @Test
    @Transactional
    void createEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeCreate = eventScoreCardDetailRepository.findAll().size();
        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);
        restEventScoreCardDetailMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeCreate + 1);
        EventScoreCardDetail testEventScoreCardDetail = eventScoreCardDetailList.get(eventScoreCardDetailList.size() - 1);
        assertThat(testEventScoreCardDetail.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testEventScoreCardDetail.getPlayerName()).isEqualTo(DEFAULT_PLAYER_NAME);
        assertThat(testEventScoreCardDetail.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testEventScoreCardDetail.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testEventScoreCardDetail.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventScoreCardDetail.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventScoreCardDetailWithExistingId() throws Exception {
        // Create the EventScoreCardDetail with an existing ID
        eventScoreCardDetail.setId(1L);
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        int databaseSizeBeforeCreate = eventScoreCardDetailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventScoreCardDetailMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTeamNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardDetailRepository.findAll().size();
        // set the field null
        eventScoreCardDetail.setTeamName(null);

        // Create the EventScoreCardDetail, which fails.
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        restEventScoreCardDetailMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardDetailRepository.findAll().size();
        // set the field null
        eventScoreCardDetail.setPoints(null);

        // Create the EventScoreCardDetail, which fails.
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        restEventScoreCardDetailMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardDetailRepository.findAll().size();
        // set the field null
        eventScoreCardDetail.setCreatedAt(null);

        // Create the EventScoreCardDetail, which fails.
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        restEventScoreCardDetailMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventScoreCardDetailRepository.findAll().size();
        // set the field null
        eventScoreCardDetail.setUpdatedAt(null);

        // Create the EventScoreCardDetail, which fails.
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        restEventScoreCardDetailMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetails() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList
        restEventScoreCardDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventScoreCardDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME)))
            .andExpect(jsonPath("$.[*].playerName").value(hasItem(DEFAULT_PLAYER_NAME)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventScoreCardDetail() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get the eventScoreCardDetail
        restEventScoreCardDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, eventScoreCardDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventScoreCardDetail.getId().intValue()))
            .andExpect(jsonPath("$.teamName").value(DEFAULT_TEAM_NAME))
            .andExpect(jsonPath("$.playerName").value(DEFAULT_PLAYER_NAME))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventScoreCardDetailsByIdFiltering() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        Long id = eventScoreCardDetail.getId();

        defaultEventScoreCardDetailShouldBeFound("id.equals=" + id);
        defaultEventScoreCardDetailShouldNotBeFound("id.notEquals=" + id);

        defaultEventScoreCardDetailShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventScoreCardDetailShouldNotBeFound("id.greaterThan=" + id);

        defaultEventScoreCardDetailShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventScoreCardDetailShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByTeamNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where teamName equals to DEFAULT_TEAM_NAME
        defaultEventScoreCardDetailShouldBeFound("teamName.equals=" + DEFAULT_TEAM_NAME);

        // Get all the eventScoreCardDetailList where teamName equals to UPDATED_TEAM_NAME
        defaultEventScoreCardDetailShouldNotBeFound("teamName.equals=" + UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByTeamNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where teamName in DEFAULT_TEAM_NAME or UPDATED_TEAM_NAME
        defaultEventScoreCardDetailShouldBeFound("teamName.in=" + DEFAULT_TEAM_NAME + "," + UPDATED_TEAM_NAME);

        // Get all the eventScoreCardDetailList where teamName equals to UPDATED_TEAM_NAME
        defaultEventScoreCardDetailShouldNotBeFound("teamName.in=" + UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByTeamNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where teamName is not null
        defaultEventScoreCardDetailShouldBeFound("teamName.specified=true");

        // Get all the eventScoreCardDetailList where teamName is null
        defaultEventScoreCardDetailShouldNotBeFound("teamName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByTeamNameContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where teamName contains DEFAULT_TEAM_NAME
        defaultEventScoreCardDetailShouldBeFound("teamName.contains=" + DEFAULT_TEAM_NAME);

        // Get all the eventScoreCardDetailList where teamName contains UPDATED_TEAM_NAME
        defaultEventScoreCardDetailShouldNotBeFound("teamName.contains=" + UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByTeamNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where teamName does not contain DEFAULT_TEAM_NAME
        defaultEventScoreCardDetailShouldNotBeFound("teamName.doesNotContain=" + DEFAULT_TEAM_NAME);

        // Get all the eventScoreCardDetailList where teamName does not contain UPDATED_TEAM_NAME
        defaultEventScoreCardDetailShouldBeFound("teamName.doesNotContain=" + UPDATED_TEAM_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPlayerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where playerName equals to DEFAULT_PLAYER_NAME
        defaultEventScoreCardDetailShouldBeFound("playerName.equals=" + DEFAULT_PLAYER_NAME);

        // Get all the eventScoreCardDetailList where playerName equals to UPDATED_PLAYER_NAME
        defaultEventScoreCardDetailShouldNotBeFound("playerName.equals=" + UPDATED_PLAYER_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPlayerNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where playerName in DEFAULT_PLAYER_NAME or UPDATED_PLAYER_NAME
        defaultEventScoreCardDetailShouldBeFound("playerName.in=" + DEFAULT_PLAYER_NAME + "," + UPDATED_PLAYER_NAME);

        // Get all the eventScoreCardDetailList where playerName equals to UPDATED_PLAYER_NAME
        defaultEventScoreCardDetailShouldNotBeFound("playerName.in=" + UPDATED_PLAYER_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPlayerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where playerName is not null
        defaultEventScoreCardDetailShouldBeFound("playerName.specified=true");

        // Get all the eventScoreCardDetailList where playerName is null
        defaultEventScoreCardDetailShouldNotBeFound("playerName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPlayerNameContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where playerName contains DEFAULT_PLAYER_NAME
        defaultEventScoreCardDetailShouldBeFound("playerName.contains=" + DEFAULT_PLAYER_NAME);

        // Get all the eventScoreCardDetailList where playerName contains UPDATED_PLAYER_NAME
        defaultEventScoreCardDetailShouldNotBeFound("playerName.contains=" + UPDATED_PLAYER_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPlayerNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where playerName does not contain DEFAULT_PLAYER_NAME
        defaultEventScoreCardDetailShouldNotBeFound("playerName.doesNotContain=" + DEFAULT_PLAYER_NAME);

        // Get all the eventScoreCardDetailList where playerName does not contain UPDATED_PLAYER_NAME
        defaultEventScoreCardDetailShouldBeFound("playerName.doesNotContain=" + UPDATED_PLAYER_NAME);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points equals to DEFAULT_POINTS
        defaultEventScoreCardDetailShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the eventScoreCardDetailList where points equals to UPDATED_POINTS
        defaultEventScoreCardDetailShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultEventScoreCardDetailShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the eventScoreCardDetailList where points equals to UPDATED_POINTS
        defaultEventScoreCardDetailShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points is not null
        defaultEventScoreCardDetailShouldBeFound("points.specified=true");

        // Get all the eventScoreCardDetailList where points is null
        defaultEventScoreCardDetailShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points is greater than or equal to DEFAULT_POINTS
        defaultEventScoreCardDetailShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the eventScoreCardDetailList where points is greater than or equal to UPDATED_POINTS
        defaultEventScoreCardDetailShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points is less than or equal to DEFAULT_POINTS
        defaultEventScoreCardDetailShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the eventScoreCardDetailList where points is less than or equal to SMALLER_POINTS
        defaultEventScoreCardDetailShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points is less than DEFAULT_POINTS
        defaultEventScoreCardDetailShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the eventScoreCardDetailList where points is less than UPDATED_POINTS
        defaultEventScoreCardDetailShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where points is greater than DEFAULT_POINTS
        defaultEventScoreCardDetailShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the eventScoreCardDetailList where points is greater than SMALLER_POINTS
        defaultEventScoreCardDetailShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where remarks equals to DEFAULT_REMARKS
        defaultEventScoreCardDetailShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the eventScoreCardDetailList where remarks equals to UPDATED_REMARKS
        defaultEventScoreCardDetailShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultEventScoreCardDetailShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the eventScoreCardDetailList where remarks equals to UPDATED_REMARKS
        defaultEventScoreCardDetailShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where remarks is not null
        defaultEventScoreCardDetailShouldBeFound("remarks.specified=true");

        // Get all the eventScoreCardDetailList where remarks is null
        defaultEventScoreCardDetailShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByRemarksContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where remarks contains DEFAULT_REMARKS
        defaultEventScoreCardDetailShouldBeFound("remarks.contains=" + DEFAULT_REMARKS);

        // Get all the eventScoreCardDetailList where remarks contains UPDATED_REMARKS
        defaultEventScoreCardDetailShouldNotBeFound("remarks.contains=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByRemarksNotContainsSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where remarks does not contain DEFAULT_REMARKS
        defaultEventScoreCardDetailShouldNotBeFound("remarks.doesNotContain=" + DEFAULT_REMARKS);

        // Get all the eventScoreCardDetailList where remarks does not contain UPDATED_REMARKS
        defaultEventScoreCardDetailShouldBeFound("remarks.doesNotContain=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventScoreCardDetailShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardDetailList where createdAt equals to UPDATED_CREATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventScoreCardDetailShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventScoreCardDetailList where createdAt equals to UPDATED_CREATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt is not null
        defaultEventScoreCardDetailShouldBeFound("createdAt.specified=true");

        // Get all the eventScoreCardDetailList where createdAt is null
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventScoreCardDetailShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardDetailList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventScoreCardDetailShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardDetailList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardDetailList where createdAt is less than UPDATED_CREATED_AT
        defaultEventScoreCardDetailShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventScoreCardDetailList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventScoreCardDetailShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventScoreCardDetailShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardDetailList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventScoreCardDetailShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventScoreCardDetailList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt is not null
        defaultEventScoreCardDetailShouldBeFound("updatedAt.specified=true");

        // Get all the eventScoreCardDetailList where updatedAt is null
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventScoreCardDetailShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardDetailList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventScoreCardDetailShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardDetailList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardDetailList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventScoreCardDetailShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        // Get all the eventScoreCardDetailList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventScoreCardDetailShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventScoreCardDetailList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventScoreCardDetailShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventScoreCardDetailsByScoreCardIsEqualToSomething() throws Exception {
        EventScoreCard scoreCard;
        if (TestUtil.findAll(em, EventScoreCard.class).isEmpty()) {
            eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);
            scoreCard = EventScoreCardResourceIT.createEntity(em);
        } else {
            scoreCard = TestUtil.findAll(em, EventScoreCard.class).get(0);
        }
        em.persist(scoreCard);
        em.flush();
        eventScoreCardDetail.setScoreCard(scoreCard);
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);
        Long scoreCardId = scoreCard.getId();
        // Get all the eventScoreCardDetailList where scoreCard equals to scoreCardId
        defaultEventScoreCardDetailShouldBeFound("scoreCardId.equals=" + scoreCardId);

        // Get all the eventScoreCardDetailList where scoreCard equals to (scoreCardId + 1)
        defaultEventScoreCardDetailShouldNotBeFound("scoreCardId.equals=" + (scoreCardId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventScoreCardDetailShouldBeFound(String filter) throws Exception {
        restEventScoreCardDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventScoreCardDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamName").value(hasItem(DEFAULT_TEAM_NAME)))
            .andExpect(jsonPath("$.[*].playerName").value(hasItem(DEFAULT_PLAYER_NAME)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventScoreCardDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventScoreCardDetailShouldNotBeFound(String filter) throws Exception {
        restEventScoreCardDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventScoreCardDetailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventScoreCardDetail() throws Exception {
        // Get the eventScoreCardDetail
        restEventScoreCardDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventScoreCardDetail() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();

        // Update the eventScoreCardDetail
        EventScoreCardDetail updatedEventScoreCardDetail = eventScoreCardDetailRepository
            .findById(eventScoreCardDetail.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEventScoreCardDetail are not directly saved in db
        em.detach(updatedEventScoreCardDetail);
        updatedEventScoreCardDetail
            .teamName(UPDATED_TEAM_NAME)
            .playerName(UPDATED_PLAYER_NAME)
            .points(UPDATED_POINTS)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(updatedEventScoreCardDetail);

        restEventScoreCardDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventScoreCardDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
        EventScoreCardDetail testEventScoreCardDetail = eventScoreCardDetailList.get(eventScoreCardDetailList.size() - 1);
        assertThat(testEventScoreCardDetail.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testEventScoreCardDetail.getPlayerName()).isEqualTo(UPDATED_PLAYER_NAME);
        assertThat(testEventScoreCardDetail.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testEventScoreCardDetail.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEventScoreCardDetail.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventScoreCardDetail.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();
        eventScoreCardDetail.setId(longCount.incrementAndGet());

        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventScoreCardDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventScoreCardDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();
        eventScoreCardDetail.setId(longCount.incrementAndGet());

        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();
        eventScoreCardDetail.setId(longCount.incrementAndGet());

        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardDetailMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventScoreCardDetailWithPatch() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();

        // Update the eventScoreCardDetail using partial update
        EventScoreCardDetail partialUpdatedEventScoreCardDetail = new EventScoreCardDetail();
        partialUpdatedEventScoreCardDetail.setId(eventScoreCardDetail.getId());

        partialUpdatedEventScoreCardDetail.remarks(UPDATED_REMARKS);

        restEventScoreCardDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventScoreCardDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventScoreCardDetail))
            )
            .andExpect(status().isOk());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
        EventScoreCardDetail testEventScoreCardDetail = eventScoreCardDetailList.get(eventScoreCardDetailList.size() - 1);
        assertThat(testEventScoreCardDetail.getTeamName()).isEqualTo(DEFAULT_TEAM_NAME);
        assertThat(testEventScoreCardDetail.getPlayerName()).isEqualTo(DEFAULT_PLAYER_NAME);
        assertThat(testEventScoreCardDetail.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testEventScoreCardDetail.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEventScoreCardDetail.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventScoreCardDetail.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventScoreCardDetailWithPatch() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();

        // Update the eventScoreCardDetail using partial update
        EventScoreCardDetail partialUpdatedEventScoreCardDetail = new EventScoreCardDetail();
        partialUpdatedEventScoreCardDetail.setId(eventScoreCardDetail.getId());

        partialUpdatedEventScoreCardDetail
            .teamName(UPDATED_TEAM_NAME)
            .playerName(UPDATED_PLAYER_NAME)
            .points(UPDATED_POINTS)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventScoreCardDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventScoreCardDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventScoreCardDetail))
            )
            .andExpect(status().isOk());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
        EventScoreCardDetail testEventScoreCardDetail = eventScoreCardDetailList.get(eventScoreCardDetailList.size() - 1);
        assertThat(testEventScoreCardDetail.getTeamName()).isEqualTo(UPDATED_TEAM_NAME);
        assertThat(testEventScoreCardDetail.getPlayerName()).isEqualTo(UPDATED_PLAYER_NAME);
        assertThat(testEventScoreCardDetail.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testEventScoreCardDetail.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testEventScoreCardDetail.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventScoreCardDetail.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();
        eventScoreCardDetail.setId(longCount.incrementAndGet());

        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventScoreCardDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventScoreCardDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();
        eventScoreCardDetail.setId(longCount.incrementAndGet());

        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventScoreCardDetail() throws Exception {
        int databaseSizeBeforeUpdate = eventScoreCardDetailRepository.findAll().size();
        eventScoreCardDetail.setId(longCount.incrementAndGet());

        // Create the EventScoreCardDetail
        EventScoreCardDetailDTO eventScoreCardDetailDTO = eventScoreCardDetailMapper.toDto(eventScoreCardDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventScoreCardDetailMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventScoreCardDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventScoreCardDetail in the database
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventScoreCardDetail() throws Exception {
        // Initialize the database
        eventScoreCardDetailRepository.saveAndFlush(eventScoreCardDetail);

        int databaseSizeBeforeDelete = eventScoreCardDetailRepository.findAll().size();

        // Delete the eventScoreCardDetail
        restEventScoreCardDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventScoreCardDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventScoreCardDetail> eventScoreCardDetailList = eventScoreCardDetailRepository.findAll();
        assertThat(eventScoreCardDetailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
