package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventAdminAuditLog;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventAdminAuditLogRepository;
import com.nextjstemplate.service.dto.EventAdminAuditLogDTO;
import com.nextjstemplate.service.mapper.EventAdminAuditLogMapper;
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
 * Integration tests for the {@link EventAdminAuditLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventAdminAuditLogResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHANGES = "AAAAAAAAAA";
    private static final String UPDATED_CHANGES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-admin-audit-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventAdminAuditLogRepository eventAdminAuditLogRepository;

    @Autowired
    private EventAdminAuditLogMapper eventAdminAuditLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventAdminAuditLogMockMvc;

    private EventAdminAuditLog eventAdminAuditLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventAdminAuditLog createEntity(EntityManager em) {
        EventAdminAuditLog eventAdminAuditLog = new EventAdminAuditLog()
            .tenantId(DEFAULT_TENANT_ID)
            .action(DEFAULT_ACTION)
            .tableName(DEFAULT_TABLE_NAME)
            .recordId(DEFAULT_RECORD_ID)
            .changes(DEFAULT_CHANGES)
            .createdAt(DEFAULT_CREATED_AT);
        return eventAdminAuditLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventAdminAuditLog createUpdatedEntity(EntityManager em) {
        EventAdminAuditLog eventAdminAuditLog = new EventAdminAuditLog()
            .tenantId(UPDATED_TENANT_ID)
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);
        return eventAdminAuditLog;
    }

    @BeforeEach
    public void initTest() {
        eventAdminAuditLog = createEntity(em);
    }

    @Test
    @Transactional
    void createEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeCreate = eventAdminAuditLogRepository.findAll().size();
        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);
        restEventAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeCreate + 1);
        EventAdminAuditLog testEventAdminAuditLog = eventAdminAuditLogList.get(eventAdminAuditLogList.size() - 1);
        assertThat(testEventAdminAuditLog.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventAdminAuditLog.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testEventAdminAuditLog.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testEventAdminAuditLog.getRecordId()).isEqualTo(DEFAULT_RECORD_ID);
        assertThat(testEventAdminAuditLog.getChanges()).isEqualTo(DEFAULT_CHANGES);
        assertThat(testEventAdminAuditLog.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createEventAdminAuditLogWithExistingId() throws Exception {
        // Create the EventAdminAuditLog with an existing ID
        eventAdminAuditLog.setId(1L);
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        int databaseSizeBeforeCreate = eventAdminAuditLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminAuditLogRepository.findAll().size();
        // set the field null
        eventAdminAuditLog.setAction(null);

        // Create the EventAdminAuditLog, which fails.
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        restEventAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminAuditLogRepository.findAll().size();
        // set the field null
        eventAdminAuditLog.setTableName(null);

        // Create the EventAdminAuditLog, which fails.
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        restEventAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminAuditLogRepository.findAll().size();
        // set the field null
        eventAdminAuditLog.setRecordId(null);

        // Create the EventAdminAuditLog, which fails.
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        restEventAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventAdminAuditLogRepository.findAll().size();
        // set the field null
        eventAdminAuditLog.setCreatedAt(null);

        // Create the EventAdminAuditLog, which fails.
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        restEventAdminAuditLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogs() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList
        restEventAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAdminAuditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID)))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    void getEventAdminAuditLog() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get the eventAdminAuditLog
        restEventAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL_ID, eventAdminAuditLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventAdminAuditLog.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.recordId").value(DEFAULT_RECORD_ID))
            .andExpect(jsonPath("$.changes").value(DEFAULT_CHANGES.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    void getEventAdminAuditLogsByIdFiltering() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        Long id = eventAdminAuditLog.getId();

        defaultEventAdminAuditLogShouldBeFound("id.equals=" + id);
        defaultEventAdminAuditLogShouldNotBeFound("id.notEquals=" + id);

        defaultEventAdminAuditLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventAdminAuditLogShouldNotBeFound("id.greaterThan=" + id);

        defaultEventAdminAuditLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventAdminAuditLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventAdminAuditLogShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventAdminAuditLogList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAdminAuditLogShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventAdminAuditLogShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventAdminAuditLogList where tenantId equals to UPDATED_TENANT_ID
        defaultEventAdminAuditLogShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tenantId is not null
        defaultEventAdminAuditLogShouldBeFound("tenantId.specified=true");

        // Get all the eventAdminAuditLogList where tenantId is null
        defaultEventAdminAuditLogShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tenantId contains DEFAULT_TENANT_ID
        defaultEventAdminAuditLogShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventAdminAuditLogList where tenantId contains UPDATED_TENANT_ID
        defaultEventAdminAuditLogShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventAdminAuditLogShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventAdminAuditLogList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventAdminAuditLogShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where action equals to DEFAULT_ACTION
        defaultEventAdminAuditLogShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the eventAdminAuditLogList where action equals to UPDATED_ACTION
        defaultEventAdminAuditLogShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultEventAdminAuditLogShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the eventAdminAuditLogList where action equals to UPDATED_ACTION
        defaultEventAdminAuditLogShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where action is not null
        defaultEventAdminAuditLogShouldBeFound("action.specified=true");

        // Get all the eventAdminAuditLogList where action is null
        defaultEventAdminAuditLogShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByActionContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where action contains DEFAULT_ACTION
        defaultEventAdminAuditLogShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the eventAdminAuditLogList where action contains UPDATED_ACTION
        defaultEventAdminAuditLogShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where action does not contain DEFAULT_ACTION
        defaultEventAdminAuditLogShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the eventAdminAuditLogList where action does not contain UPDATED_ACTION
        defaultEventAdminAuditLogShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTableNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tableName equals to DEFAULT_TABLE_NAME
        defaultEventAdminAuditLogShouldBeFound("tableName.equals=" + DEFAULT_TABLE_NAME);

        // Get all the eventAdminAuditLogList where tableName equals to UPDATED_TABLE_NAME
        defaultEventAdminAuditLogShouldNotBeFound("tableName.equals=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTableNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tableName in DEFAULT_TABLE_NAME or UPDATED_TABLE_NAME
        defaultEventAdminAuditLogShouldBeFound("tableName.in=" + DEFAULT_TABLE_NAME + "," + UPDATED_TABLE_NAME);

        // Get all the eventAdminAuditLogList where tableName equals to UPDATED_TABLE_NAME
        defaultEventAdminAuditLogShouldNotBeFound("tableName.in=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTableNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tableName is not null
        defaultEventAdminAuditLogShouldBeFound("tableName.specified=true");

        // Get all the eventAdminAuditLogList where tableName is null
        defaultEventAdminAuditLogShouldNotBeFound("tableName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTableNameContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tableName contains DEFAULT_TABLE_NAME
        defaultEventAdminAuditLogShouldBeFound("tableName.contains=" + DEFAULT_TABLE_NAME);

        // Get all the eventAdminAuditLogList where tableName contains UPDATED_TABLE_NAME
        defaultEventAdminAuditLogShouldNotBeFound("tableName.contains=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByTableNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where tableName does not contain DEFAULT_TABLE_NAME
        defaultEventAdminAuditLogShouldNotBeFound("tableName.doesNotContain=" + DEFAULT_TABLE_NAME);

        // Get all the eventAdminAuditLogList where tableName does not contain UPDATED_TABLE_NAME
        defaultEventAdminAuditLogShouldBeFound("tableName.doesNotContain=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByRecordIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where recordId equals to DEFAULT_RECORD_ID
        defaultEventAdminAuditLogShouldBeFound("recordId.equals=" + DEFAULT_RECORD_ID);

        // Get all the eventAdminAuditLogList where recordId equals to UPDATED_RECORD_ID
        defaultEventAdminAuditLogShouldNotBeFound("recordId.equals=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByRecordIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where recordId in DEFAULT_RECORD_ID or UPDATED_RECORD_ID
        defaultEventAdminAuditLogShouldBeFound("recordId.in=" + DEFAULT_RECORD_ID + "," + UPDATED_RECORD_ID);

        // Get all the eventAdminAuditLogList where recordId equals to UPDATED_RECORD_ID
        defaultEventAdminAuditLogShouldNotBeFound("recordId.in=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByRecordIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where recordId is not null
        defaultEventAdminAuditLogShouldBeFound("recordId.specified=true");

        // Get all the eventAdminAuditLogList where recordId is null
        defaultEventAdminAuditLogShouldNotBeFound("recordId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByRecordIdContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where recordId contains DEFAULT_RECORD_ID
        defaultEventAdminAuditLogShouldBeFound("recordId.contains=" + DEFAULT_RECORD_ID);

        // Get all the eventAdminAuditLogList where recordId contains UPDATED_RECORD_ID
        defaultEventAdminAuditLogShouldNotBeFound("recordId.contains=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByRecordIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where recordId does not contain DEFAULT_RECORD_ID
        defaultEventAdminAuditLogShouldNotBeFound("recordId.doesNotContain=" + DEFAULT_RECORD_ID);

        // Get all the eventAdminAuditLogList where recordId does not contain UPDATED_RECORD_ID
        defaultEventAdminAuditLogShouldBeFound("recordId.doesNotContain=" + UPDATED_RECORD_ID);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventAdminAuditLogShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminAuditLogList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventAdminAuditLogShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventAdminAuditLogList where createdAt equals to UPDATED_CREATED_AT
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt is not null
        defaultEventAdminAuditLogShouldBeFound("createdAt.specified=true");

        // Get all the eventAdminAuditLogList where createdAt is null
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventAdminAuditLogShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminAuditLogList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventAdminAuditLogShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminAuditLogList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminAuditLogList where createdAt is less than UPDATED_CREATED_AT
        defaultEventAdminAuditLogShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventAdminAuditLogsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        // Get all the eventAdminAuditLogList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventAdminAuditLogShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventAdminAuditLogList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventAdminAuditLogShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    /* @Test
    @Transactional
    void getAllEventAdminAuditLogsByAdminIsEqualToSomething() throws Exception {
        UserProfile admin;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);
            admin = UserProfileResourceIT.createEntity(em);
        } else {
            admin = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(admin);
        em.flush();
        eventAdminAuditLog.setAdmin(admin);
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);
        Long adminId = admin.getId();
        // Get all the eventAdminAuditLogList where admin equals to adminId
        defaultEventAdminAuditLogShouldBeFound("adminId.equals=" + adminId);

        // Get all the eventAdminAuditLogList where admin equals to (adminId + 1)
        defaultEventAdminAuditLogShouldNotBeFound("adminId.equals=" + (adminId + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventAdminAuditLogShouldBeFound(String filter) throws Exception {
        restEventAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventAdminAuditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].recordId").value(hasItem(DEFAULT_RECORD_ID)))
            .andExpect(jsonPath("$.[*].changes").value(hasItem(DEFAULT_CHANGES.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));

        // Check, that the count call also returns 1
        restEventAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventAdminAuditLogShouldNotBeFound(String filter) throws Exception {
        restEventAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventAdminAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventAdminAuditLog() throws Exception {
        // Get the eventAdminAuditLog
        restEventAdminAuditLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventAdminAuditLog() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();

        // Update the eventAdminAuditLog
        EventAdminAuditLog updatedEventAdminAuditLog = eventAdminAuditLogRepository.findById(eventAdminAuditLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventAdminAuditLog are not directly saved in db
        em.detach(updatedEventAdminAuditLog);
        updatedEventAdminAuditLog
            .tenantId(UPDATED_TENANT_ID)
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(updatedEventAdminAuditLog);

        restEventAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAdminAuditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
        EventAdminAuditLog testEventAdminAuditLog = eventAdminAuditLogList.get(eventAdminAuditLogList.size() - 1);
        assertThat(testEventAdminAuditLog.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAdminAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testEventAdminAuditLog.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testEventAdminAuditLog.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testEventAdminAuditLog.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testEventAdminAuditLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();
        eventAdminAuditLog.setId(longCount.incrementAndGet());

        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventAdminAuditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();
        eventAdminAuditLog.setId(longCount.incrementAndGet());

        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();
        eventAdminAuditLog.setId(longCount.incrementAndGet());

        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventAdminAuditLogWithPatch() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();

        // Update the eventAdminAuditLog using partial update
        EventAdminAuditLog partialUpdatedEventAdminAuditLog = new EventAdminAuditLog();
        partialUpdatedEventAdminAuditLog.setId(eventAdminAuditLog.getId());

        partialUpdatedEventAdminAuditLog
            .tenantId(UPDATED_TENANT_ID)
            .action(UPDATED_ACTION)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);

        restEventAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAdminAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAdminAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
        EventAdminAuditLog testEventAdminAuditLog = eventAdminAuditLogList.get(eventAdminAuditLogList.size() - 1);
        assertThat(testEventAdminAuditLog.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAdminAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testEventAdminAuditLog.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testEventAdminAuditLog.getRecordId()).isEqualTo(DEFAULT_RECORD_ID);
        assertThat(testEventAdminAuditLog.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testEventAdminAuditLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventAdminAuditLogWithPatch() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();

        // Update the eventAdminAuditLog using partial update
        EventAdminAuditLog partialUpdatedEventAdminAuditLog = new EventAdminAuditLog();
        partialUpdatedEventAdminAuditLog.setId(eventAdminAuditLog.getId());

        partialUpdatedEventAdminAuditLog
            .tenantId(UPDATED_TENANT_ID)
            .action(UPDATED_ACTION)
            .tableName(UPDATED_TABLE_NAME)
            .recordId(UPDATED_RECORD_ID)
            .changes(UPDATED_CHANGES)
            .createdAt(UPDATED_CREATED_AT);

        restEventAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventAdminAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventAdminAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
        EventAdminAuditLog testEventAdminAuditLog = eventAdminAuditLogList.get(eventAdminAuditLogList.size() - 1);
        assertThat(testEventAdminAuditLog.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventAdminAuditLog.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testEventAdminAuditLog.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testEventAdminAuditLog.getRecordId()).isEqualTo(UPDATED_RECORD_ID);
        assertThat(testEventAdminAuditLog.getChanges()).isEqualTo(UPDATED_CHANGES);
        assertThat(testEventAdminAuditLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();
        eventAdminAuditLog.setId(longCount.incrementAndGet());

        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventAdminAuditLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();
        eventAdminAuditLog.setId(longCount.incrementAndGet());

        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventAdminAuditLog() throws Exception {
        int databaseSizeBeforeUpdate = eventAdminAuditLogRepository.findAll().size();
        eventAdminAuditLog.setId(longCount.incrementAndGet());

        // Create the EventAdminAuditLog
        EventAdminAuditLogDTO eventAdminAuditLogDTO = eventAdminAuditLogMapper.toDto(eventAdminAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventAdminAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventAdminAuditLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventAdminAuditLog in the database
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventAdminAuditLog() throws Exception {
        // Initialize the database
        eventAdminAuditLogRepository.saveAndFlush(eventAdminAuditLog);

        int databaseSizeBeforeDelete = eventAdminAuditLogRepository.findAll().size();

        // Delete the eventAdminAuditLog
        restEventAdminAuditLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventAdminAuditLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventAdminAuditLog> eventAdminAuditLogList = eventAdminAuditLogRepository.findAll();
        assertThat(eventAdminAuditLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
