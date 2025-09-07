package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.BulkOperationLog;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.BulkOperationLogRepository;
import com.nextjstemplate.service.dto.BulkOperationLogDTO;
import com.nextjstemplate.service.mapper.BulkOperationLogMapper;
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
 * Integration tests for the {@link BulkOperationLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BulkOperationLogResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TARGET_COUNT = 1;
    private static final Integer UPDATED_TARGET_COUNT = 2;
    private static final Integer SMALLER_TARGET_COUNT = 1 - 1;

    private static final Integer DEFAULT_SUCCESS_COUNT = 1;
    private static final Integer UPDATED_SUCCESS_COUNT = 2;
    private static final Integer SMALLER_SUCCESS_COUNT = 1 - 1;

    private static final Integer DEFAULT_ERROR_COUNT = 1;
    private static final Integer UPDATED_ERROR_COUNT = 2;
    private static final Integer SMALLER_ERROR_COUNT = 1 - 1;

    private static final String DEFAULT_OPERATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_DETAILS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/bulk-operation-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BulkOperationLogRepository bulkOperationLogRepository;

    @Autowired
    private BulkOperationLogMapper bulkOperationLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBulkOperationLogMockMvc;

    private BulkOperationLog bulkOperationLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BulkOperationLog createEntity(EntityManager em) {
        BulkOperationLog bulkOperationLog = new BulkOperationLog()
            .tenantId(DEFAULT_TENANT_ID)
            .operationType(DEFAULT_OPERATION_TYPE)
            .targetCount(DEFAULT_TARGET_COUNT)
            .successCount(DEFAULT_SUCCESS_COUNT)
            .errorCount(DEFAULT_ERROR_COUNT)
            .operationDetails(DEFAULT_OPERATION_DETAILS)
            .createdAt(DEFAULT_CREATED_AT);
        return bulkOperationLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BulkOperationLog createUpdatedEntity(EntityManager em) {
        BulkOperationLog bulkOperationLog = new BulkOperationLog()
            .tenantId(UPDATED_TENANT_ID)
            .operationType(UPDATED_OPERATION_TYPE)
            .targetCount(UPDATED_TARGET_COUNT)
            .successCount(UPDATED_SUCCESS_COUNT)
            .errorCount(UPDATED_ERROR_COUNT)
            .operationDetails(UPDATED_OPERATION_DETAILS)
            .createdAt(UPDATED_CREATED_AT);
        return bulkOperationLog;
    }

    @BeforeEach
    public void initTest() {
        bulkOperationLog = createEntity(em);
    }

    @Test
    @Transactional
    void createBulkOperationLog() throws Exception {
        int databaseSizeBeforeCreate = bulkOperationLogRepository.findAll().size();
        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);
        restBulkOperationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeCreate + 1);
        BulkOperationLog testBulkOperationLog = bulkOperationLogList.get(bulkOperationLogList.size() - 1);
        assertThat(testBulkOperationLog.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testBulkOperationLog.getOperationType()).isEqualTo(DEFAULT_OPERATION_TYPE);
        assertThat(testBulkOperationLog.getTargetCount()).isEqualTo(DEFAULT_TARGET_COUNT);
        assertThat(testBulkOperationLog.getSuccessCount()).isEqualTo(DEFAULT_SUCCESS_COUNT);
        assertThat(testBulkOperationLog.getErrorCount()).isEqualTo(DEFAULT_ERROR_COUNT);
        assertThat(testBulkOperationLog.getOperationDetails()).isEqualTo(DEFAULT_OPERATION_DETAILS);
        assertThat(testBulkOperationLog.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createBulkOperationLogWithExistingId() throws Exception {
        // Create the BulkOperationLog with an existing ID
        bulkOperationLog.setId(1L);
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        int databaseSizeBeforeCreate = bulkOperationLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBulkOperationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOperationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulkOperationLogRepository.findAll().size();
        // set the field null
        bulkOperationLog.setOperationType(null);

        // Create the BulkOperationLog, which fails.
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        restBulkOperationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTargetCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulkOperationLogRepository.findAll().size();
        // set the field null
        bulkOperationLog.setTargetCount(null);

        // Create the BulkOperationLog, which fails.
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        restBulkOperationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = bulkOperationLogRepository.findAll().size();
        // set the field null
        bulkOperationLog.setCreatedAt(null);

        // Create the BulkOperationLog, which fails.
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        restBulkOperationLogMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogs() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList
        restBulkOperationLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bulkOperationLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE)))
            .andExpect(jsonPath("$.[*].targetCount").value(hasItem(DEFAULT_TARGET_COUNT)))
            .andExpect(jsonPath("$.[*].successCount").value(hasItem(DEFAULT_SUCCESS_COUNT)))
            .andExpect(jsonPath("$.[*].errorCount").value(hasItem(DEFAULT_ERROR_COUNT)))
            .andExpect(jsonPath("$.[*].operationDetails").value(hasItem(DEFAULT_OPERATION_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    void getBulkOperationLog() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get the bulkOperationLog
        restBulkOperationLogMockMvc
            .perform(get(ENTITY_API_URL_ID, bulkOperationLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bulkOperationLog.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.operationType").value(DEFAULT_OPERATION_TYPE))
            .andExpect(jsonPath("$.targetCount").value(DEFAULT_TARGET_COUNT))
            .andExpect(jsonPath("$.successCount").value(DEFAULT_SUCCESS_COUNT))
            .andExpect(jsonPath("$.errorCount").value(DEFAULT_ERROR_COUNT))
            .andExpect(jsonPath("$.operationDetails").value(DEFAULT_OPERATION_DETAILS.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    void getBulkOperationLogsByIdFiltering() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        Long id = bulkOperationLog.getId();

        defaultBulkOperationLogShouldBeFound("id.equals=" + id);
        defaultBulkOperationLogShouldNotBeFound("id.notEquals=" + id);

        defaultBulkOperationLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBulkOperationLogShouldNotBeFound("id.greaterThan=" + id);

        defaultBulkOperationLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBulkOperationLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where tenantId equals to DEFAULT_TENANT_ID
        defaultBulkOperationLogShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the bulkOperationLogList where tenantId equals to UPDATED_TENANT_ID
        defaultBulkOperationLogShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultBulkOperationLogShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the bulkOperationLogList where tenantId equals to UPDATED_TENANT_ID
        defaultBulkOperationLogShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where tenantId is not null
        defaultBulkOperationLogShouldBeFound("tenantId.specified=true");

        // Get all the bulkOperationLogList where tenantId is null
        defaultBulkOperationLogShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where tenantId contains DEFAULT_TENANT_ID
        defaultBulkOperationLogShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the bulkOperationLogList where tenantId contains UPDATED_TENANT_ID
        defaultBulkOperationLogShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where tenantId does not contain DEFAULT_TENANT_ID
        defaultBulkOperationLogShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the bulkOperationLogList where tenantId does not contain UPDATED_TENANT_ID
        defaultBulkOperationLogShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByOperationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where operationType equals to DEFAULT_OPERATION_TYPE
        defaultBulkOperationLogShouldBeFound("operationType.equals=" + DEFAULT_OPERATION_TYPE);

        // Get all the bulkOperationLogList where operationType equals to UPDATED_OPERATION_TYPE
        defaultBulkOperationLogShouldNotBeFound("operationType.equals=" + UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByOperationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where operationType in DEFAULT_OPERATION_TYPE or UPDATED_OPERATION_TYPE
        defaultBulkOperationLogShouldBeFound("operationType.in=" + DEFAULT_OPERATION_TYPE + "," + UPDATED_OPERATION_TYPE);

        // Get all the bulkOperationLogList where operationType equals to UPDATED_OPERATION_TYPE
        defaultBulkOperationLogShouldNotBeFound("operationType.in=" + UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByOperationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where operationType is not null
        defaultBulkOperationLogShouldBeFound("operationType.specified=true");

        // Get all the bulkOperationLogList where operationType is null
        defaultBulkOperationLogShouldNotBeFound("operationType.specified=false");
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByOperationTypeContainsSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where operationType contains DEFAULT_OPERATION_TYPE
        defaultBulkOperationLogShouldBeFound("operationType.contains=" + DEFAULT_OPERATION_TYPE);

        // Get all the bulkOperationLogList where operationType contains UPDATED_OPERATION_TYPE
        defaultBulkOperationLogShouldNotBeFound("operationType.contains=" + UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByOperationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where operationType does not contain DEFAULT_OPERATION_TYPE
        defaultBulkOperationLogShouldNotBeFound("operationType.doesNotContain=" + DEFAULT_OPERATION_TYPE);

        // Get all the bulkOperationLogList where operationType does not contain UPDATED_OPERATION_TYPE
        defaultBulkOperationLogShouldBeFound("operationType.doesNotContain=" + UPDATED_OPERATION_TYPE);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount equals to DEFAULT_TARGET_COUNT
        defaultBulkOperationLogShouldBeFound("targetCount.equals=" + DEFAULT_TARGET_COUNT);

        // Get all the bulkOperationLogList where targetCount equals to UPDATED_TARGET_COUNT
        defaultBulkOperationLogShouldNotBeFound("targetCount.equals=" + UPDATED_TARGET_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsInShouldWork() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount in DEFAULT_TARGET_COUNT or UPDATED_TARGET_COUNT
        defaultBulkOperationLogShouldBeFound("targetCount.in=" + DEFAULT_TARGET_COUNT + "," + UPDATED_TARGET_COUNT);

        // Get all the bulkOperationLogList where targetCount equals to UPDATED_TARGET_COUNT
        defaultBulkOperationLogShouldNotBeFound("targetCount.in=" + UPDATED_TARGET_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount is not null
        defaultBulkOperationLogShouldBeFound("targetCount.specified=true");

        // Get all the bulkOperationLogList where targetCount is null
        defaultBulkOperationLogShouldNotBeFound("targetCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount is greater than or equal to DEFAULT_TARGET_COUNT
        defaultBulkOperationLogShouldBeFound("targetCount.greaterThanOrEqual=" + DEFAULT_TARGET_COUNT);

        // Get all the bulkOperationLogList where targetCount is greater than or equal to UPDATED_TARGET_COUNT
        defaultBulkOperationLogShouldNotBeFound("targetCount.greaterThanOrEqual=" + UPDATED_TARGET_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount is less than or equal to DEFAULT_TARGET_COUNT
        defaultBulkOperationLogShouldBeFound("targetCount.lessThanOrEqual=" + DEFAULT_TARGET_COUNT);

        // Get all the bulkOperationLogList where targetCount is less than or equal to SMALLER_TARGET_COUNT
        defaultBulkOperationLogShouldNotBeFound("targetCount.lessThanOrEqual=" + SMALLER_TARGET_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsLessThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount is less than DEFAULT_TARGET_COUNT
        defaultBulkOperationLogShouldNotBeFound("targetCount.lessThan=" + DEFAULT_TARGET_COUNT);

        // Get all the bulkOperationLogList where targetCount is less than UPDATED_TARGET_COUNT
        defaultBulkOperationLogShouldBeFound("targetCount.lessThan=" + UPDATED_TARGET_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByTargetCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where targetCount is greater than DEFAULT_TARGET_COUNT
        defaultBulkOperationLogShouldNotBeFound("targetCount.greaterThan=" + DEFAULT_TARGET_COUNT);

        // Get all the bulkOperationLogList where targetCount is greater than SMALLER_TARGET_COUNT
        defaultBulkOperationLogShouldBeFound("targetCount.greaterThan=" + SMALLER_TARGET_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount equals to DEFAULT_SUCCESS_COUNT
        defaultBulkOperationLogShouldBeFound("successCount.equals=" + DEFAULT_SUCCESS_COUNT);

        // Get all the bulkOperationLogList where successCount equals to UPDATED_SUCCESS_COUNT
        defaultBulkOperationLogShouldNotBeFound("successCount.equals=" + UPDATED_SUCCESS_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsInShouldWork() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount in DEFAULT_SUCCESS_COUNT or UPDATED_SUCCESS_COUNT
        defaultBulkOperationLogShouldBeFound("successCount.in=" + DEFAULT_SUCCESS_COUNT + "," + UPDATED_SUCCESS_COUNT);

        // Get all the bulkOperationLogList where successCount equals to UPDATED_SUCCESS_COUNT
        defaultBulkOperationLogShouldNotBeFound("successCount.in=" + UPDATED_SUCCESS_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount is not null
        defaultBulkOperationLogShouldBeFound("successCount.specified=true");

        // Get all the bulkOperationLogList where successCount is null
        defaultBulkOperationLogShouldNotBeFound("successCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount is greater than or equal to DEFAULT_SUCCESS_COUNT
        defaultBulkOperationLogShouldBeFound("successCount.greaterThanOrEqual=" + DEFAULT_SUCCESS_COUNT);

        // Get all the bulkOperationLogList where successCount is greater than or equal to UPDATED_SUCCESS_COUNT
        defaultBulkOperationLogShouldNotBeFound("successCount.greaterThanOrEqual=" + UPDATED_SUCCESS_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount is less than or equal to DEFAULT_SUCCESS_COUNT
        defaultBulkOperationLogShouldBeFound("successCount.lessThanOrEqual=" + DEFAULT_SUCCESS_COUNT);

        // Get all the bulkOperationLogList where successCount is less than or equal to SMALLER_SUCCESS_COUNT
        defaultBulkOperationLogShouldNotBeFound("successCount.lessThanOrEqual=" + SMALLER_SUCCESS_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsLessThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount is less than DEFAULT_SUCCESS_COUNT
        defaultBulkOperationLogShouldNotBeFound("successCount.lessThan=" + DEFAULT_SUCCESS_COUNT);

        // Get all the bulkOperationLogList where successCount is less than UPDATED_SUCCESS_COUNT
        defaultBulkOperationLogShouldBeFound("successCount.lessThan=" + UPDATED_SUCCESS_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsBySuccessCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where successCount is greater than DEFAULT_SUCCESS_COUNT
        defaultBulkOperationLogShouldNotBeFound("successCount.greaterThan=" + DEFAULT_SUCCESS_COUNT);

        // Get all the bulkOperationLogList where successCount is greater than SMALLER_SUCCESS_COUNT
        defaultBulkOperationLogShouldBeFound("successCount.greaterThan=" + SMALLER_SUCCESS_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount equals to DEFAULT_ERROR_COUNT
        defaultBulkOperationLogShouldBeFound("errorCount.equals=" + DEFAULT_ERROR_COUNT);

        // Get all the bulkOperationLogList where errorCount equals to UPDATED_ERROR_COUNT
        defaultBulkOperationLogShouldNotBeFound("errorCount.equals=" + UPDATED_ERROR_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsInShouldWork() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount in DEFAULT_ERROR_COUNT or UPDATED_ERROR_COUNT
        defaultBulkOperationLogShouldBeFound("errorCount.in=" + DEFAULT_ERROR_COUNT + "," + UPDATED_ERROR_COUNT);

        // Get all the bulkOperationLogList where errorCount equals to UPDATED_ERROR_COUNT
        defaultBulkOperationLogShouldNotBeFound("errorCount.in=" + UPDATED_ERROR_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount is not null
        defaultBulkOperationLogShouldBeFound("errorCount.specified=true");

        // Get all the bulkOperationLogList where errorCount is null
        defaultBulkOperationLogShouldNotBeFound("errorCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount is greater than or equal to DEFAULT_ERROR_COUNT
        defaultBulkOperationLogShouldBeFound("errorCount.greaterThanOrEqual=" + DEFAULT_ERROR_COUNT);

        // Get all the bulkOperationLogList where errorCount is greater than or equal to UPDATED_ERROR_COUNT
        defaultBulkOperationLogShouldNotBeFound("errorCount.greaterThanOrEqual=" + UPDATED_ERROR_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount is less than or equal to DEFAULT_ERROR_COUNT
        defaultBulkOperationLogShouldBeFound("errorCount.lessThanOrEqual=" + DEFAULT_ERROR_COUNT);

        // Get all the bulkOperationLogList where errorCount is less than or equal to SMALLER_ERROR_COUNT
        defaultBulkOperationLogShouldNotBeFound("errorCount.lessThanOrEqual=" + SMALLER_ERROR_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsLessThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount is less than DEFAULT_ERROR_COUNT
        defaultBulkOperationLogShouldNotBeFound("errorCount.lessThan=" + DEFAULT_ERROR_COUNT);

        // Get all the bulkOperationLogList where errorCount is less than UPDATED_ERROR_COUNT
        defaultBulkOperationLogShouldBeFound("errorCount.lessThan=" + UPDATED_ERROR_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByErrorCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where errorCount is greater than DEFAULT_ERROR_COUNT
        defaultBulkOperationLogShouldNotBeFound("errorCount.greaterThan=" + DEFAULT_ERROR_COUNT);

        // Get all the bulkOperationLogList where errorCount is greater than SMALLER_ERROR_COUNT
        defaultBulkOperationLogShouldBeFound("errorCount.greaterThan=" + SMALLER_ERROR_COUNT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt equals to DEFAULT_CREATED_AT
        defaultBulkOperationLogShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the bulkOperationLogList where createdAt equals to UPDATED_CREATED_AT
        defaultBulkOperationLogShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultBulkOperationLogShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the bulkOperationLogList where createdAt equals to UPDATED_CREATED_AT
        defaultBulkOperationLogShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt is not null
        defaultBulkOperationLogShouldBeFound("createdAt.specified=true");

        // Get all the bulkOperationLogList where createdAt is null
        defaultBulkOperationLogShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultBulkOperationLogShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the bulkOperationLogList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultBulkOperationLogShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultBulkOperationLogShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the bulkOperationLogList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultBulkOperationLogShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt is less than DEFAULT_CREATED_AT
        defaultBulkOperationLogShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the bulkOperationLogList where createdAt is less than UPDATED_CREATED_AT
        defaultBulkOperationLogShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBulkOperationLogsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        // Get all the bulkOperationLogList where createdAt is greater than DEFAULT_CREATED_AT
        defaultBulkOperationLogShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the bulkOperationLogList where createdAt is greater than SMALLER_CREATED_AT
        defaultBulkOperationLogShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    /*  @Test
    @Transactional
    void getAllBulkOperationLogsByPerformedByIsEqualToSomething() throws Exception {
        UserProfile performedBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            bulkOperationLogRepository.saveAndFlush(bulkOperationLog);
            performedBy = UserProfileResourceIT.createEntity(em);
        } else {
            performedBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(performedBy);
        em.flush();
        bulkOperationLog.setPerformedBy(performedBy);
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);
        Long performedById = performedBy.getId();
        // Get all the bulkOperationLogList where performedBy equals to performedById
        defaultBulkOperationLogShouldBeFound("performedById.equals=" + performedById);

        // Get all the bulkOperationLogList where performedBy equals to (performedById + 1)
        defaultBulkOperationLogShouldNotBeFound("performedById.equals=" + (performedById + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBulkOperationLogShouldBeFound(String filter) throws Exception {
        restBulkOperationLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bulkOperationLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE)))
            .andExpect(jsonPath("$.[*].targetCount").value(hasItem(DEFAULT_TARGET_COUNT)))
            .andExpect(jsonPath("$.[*].successCount").value(hasItem(DEFAULT_SUCCESS_COUNT)))
            .andExpect(jsonPath("$.[*].errorCount").value(hasItem(DEFAULT_ERROR_COUNT)))
            .andExpect(jsonPath("$.[*].operationDetails").value(hasItem(DEFAULT_OPERATION_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));

        // Check, that the count call also returns 1
        restBulkOperationLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBulkOperationLogShouldNotBeFound(String filter) throws Exception {
        restBulkOperationLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBulkOperationLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBulkOperationLog() throws Exception {
        // Get the bulkOperationLog
        restBulkOperationLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBulkOperationLog() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();

        // Update the bulkOperationLog
        BulkOperationLog updatedBulkOperationLog = bulkOperationLogRepository.findById(bulkOperationLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBulkOperationLog are not directly saved in db
        em.detach(updatedBulkOperationLog);
        updatedBulkOperationLog
            .tenantId(UPDATED_TENANT_ID)
            .operationType(UPDATED_OPERATION_TYPE)
            .targetCount(UPDATED_TARGET_COUNT)
            .successCount(UPDATED_SUCCESS_COUNT)
            .errorCount(UPDATED_ERROR_COUNT)
            .operationDetails(UPDATED_OPERATION_DETAILS)
            .createdAt(UPDATED_CREATED_AT);
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(updatedBulkOperationLog);

        restBulkOperationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bulkOperationLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
        BulkOperationLog testBulkOperationLog = bulkOperationLogList.get(bulkOperationLogList.size() - 1);
        assertThat(testBulkOperationLog.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testBulkOperationLog.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
        assertThat(testBulkOperationLog.getTargetCount()).isEqualTo(UPDATED_TARGET_COUNT);
        assertThat(testBulkOperationLog.getSuccessCount()).isEqualTo(UPDATED_SUCCESS_COUNT);
        assertThat(testBulkOperationLog.getErrorCount()).isEqualTo(UPDATED_ERROR_COUNT);
        assertThat(testBulkOperationLog.getOperationDetails()).isEqualTo(UPDATED_OPERATION_DETAILS);
        assertThat(testBulkOperationLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingBulkOperationLog() throws Exception {
        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();
        bulkOperationLog.setId(longCount.incrementAndGet());

        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulkOperationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bulkOperationLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBulkOperationLog() throws Exception {
        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();
        bulkOperationLog.setId(longCount.incrementAndGet());

        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulkOperationLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBulkOperationLog() throws Exception {
        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();
        bulkOperationLog.setId(longCount.incrementAndGet());

        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulkOperationLogMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBulkOperationLogWithPatch() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();

        // Update the bulkOperationLog using partial update
        BulkOperationLog partialUpdatedBulkOperationLog = new BulkOperationLog();
        partialUpdatedBulkOperationLog.setId(bulkOperationLog.getId());

        partialUpdatedBulkOperationLog
            .tenantId(UPDATED_TENANT_ID)
            .operationType(UPDATED_OPERATION_TYPE)
            .targetCount(UPDATED_TARGET_COUNT)
            .successCount(UPDATED_SUCCESS_COUNT);

        restBulkOperationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBulkOperationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBulkOperationLog))
            )
            .andExpect(status().isOk());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
        BulkOperationLog testBulkOperationLog = bulkOperationLogList.get(bulkOperationLogList.size() - 1);
        assertThat(testBulkOperationLog.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testBulkOperationLog.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
        assertThat(testBulkOperationLog.getTargetCount()).isEqualTo(UPDATED_TARGET_COUNT);
        assertThat(testBulkOperationLog.getSuccessCount()).isEqualTo(UPDATED_SUCCESS_COUNT);
        assertThat(testBulkOperationLog.getErrorCount()).isEqualTo(DEFAULT_ERROR_COUNT);
        assertThat(testBulkOperationLog.getOperationDetails()).isEqualTo(DEFAULT_OPERATION_DETAILS);
        assertThat(testBulkOperationLog.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateBulkOperationLogWithPatch() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();

        // Update the bulkOperationLog using partial update
        BulkOperationLog partialUpdatedBulkOperationLog = new BulkOperationLog();
        partialUpdatedBulkOperationLog.setId(bulkOperationLog.getId());

        partialUpdatedBulkOperationLog
            .tenantId(UPDATED_TENANT_ID)
            .operationType(UPDATED_OPERATION_TYPE)
            .targetCount(UPDATED_TARGET_COUNT)
            .successCount(UPDATED_SUCCESS_COUNT)
            .errorCount(UPDATED_ERROR_COUNT)
            .operationDetails(UPDATED_OPERATION_DETAILS)
            .createdAt(UPDATED_CREATED_AT);

        restBulkOperationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBulkOperationLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBulkOperationLog))
            )
            .andExpect(status().isOk());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
        BulkOperationLog testBulkOperationLog = bulkOperationLogList.get(bulkOperationLogList.size() - 1);
        assertThat(testBulkOperationLog.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testBulkOperationLog.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
        assertThat(testBulkOperationLog.getTargetCount()).isEqualTo(UPDATED_TARGET_COUNT);
        assertThat(testBulkOperationLog.getSuccessCount()).isEqualTo(UPDATED_SUCCESS_COUNT);
        assertThat(testBulkOperationLog.getErrorCount()).isEqualTo(UPDATED_ERROR_COUNT);
        assertThat(testBulkOperationLog.getOperationDetails()).isEqualTo(UPDATED_OPERATION_DETAILS);
        assertThat(testBulkOperationLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBulkOperationLog() throws Exception {
        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();
        bulkOperationLog.setId(longCount.incrementAndGet());

        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBulkOperationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bulkOperationLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBulkOperationLog() throws Exception {
        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();
        bulkOperationLog.setId(longCount.incrementAndGet());

        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulkOperationLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBulkOperationLog() throws Exception {
        int databaseSizeBeforeUpdate = bulkOperationLogRepository.findAll().size();
        bulkOperationLog.setId(longCount.incrementAndGet());

        // Create the BulkOperationLog
        BulkOperationLogDTO bulkOperationLogDTO = bulkOperationLogMapper.toDto(bulkOperationLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBulkOperationLogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bulkOperationLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BulkOperationLog in the database
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBulkOperationLog() throws Exception {
        // Initialize the database
        bulkOperationLogRepository.saveAndFlush(bulkOperationLog);

        int databaseSizeBeforeDelete = bulkOperationLogRepository.findAll().size();

        // Delete the bulkOperationLog
        restBulkOperationLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, bulkOperationLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BulkOperationLog> bulkOperationLogList = bulkOperationLogRepository.findAll();
        assertThat(bulkOperationLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
