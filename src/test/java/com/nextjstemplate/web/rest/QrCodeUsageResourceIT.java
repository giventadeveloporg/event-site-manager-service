package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventAttendee;
import com.nextjstemplate.domain.QrCodeUsage;
import com.nextjstemplate.repository.QrCodeUsageRepository;
import com.nextjstemplate.service.dto.QrCodeUsageDTO;
import com.nextjstemplate.service.mapper.QrCodeUsageMapper;
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
 * Integration tests for the {@link QrCodeUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QrCodeUsageResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE_DATA = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE_DATA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_GENERATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_GENERATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_GENERATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_USED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_USED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_USED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_USAGE_COUNT = 1;
    private static final Integer UPDATED_USAGE_COUNT = 2;
    private static final Integer SMALLER_USAGE_COUNT = 1 - 1;

    private static final String DEFAULT_LAST_SCANNED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_SCANNED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/qr-code-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QrCodeUsageRepository qrCodeUsageRepository;

    @Autowired
    private QrCodeUsageMapper qrCodeUsageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQrCodeUsageMockMvc;

    private QrCodeUsage qrCodeUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCodeUsage createEntity(EntityManager em) {
        QrCodeUsage qrCodeUsage = new QrCodeUsage()
            .tenantId(DEFAULT_TENANT_ID)
            .qrCodeData(DEFAULT_QR_CODE_DATA)
            .generatedAt(DEFAULT_GENERATED_AT)
            .usedAt(DEFAULT_USED_AT)
            .usageCount(DEFAULT_USAGE_COUNT)
            .lastScannedBy(DEFAULT_LAST_SCANNED_BY)
            .createdAt(DEFAULT_CREATED_AT);
        return qrCodeUsage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrCodeUsage createUpdatedEntity(EntityManager em) {
        QrCodeUsage qrCodeUsage = new QrCodeUsage()
            .tenantId(UPDATED_TENANT_ID)
            .qrCodeData(UPDATED_QR_CODE_DATA)
            .generatedAt(UPDATED_GENERATED_AT)
            .usedAt(UPDATED_USED_AT)
            .usageCount(UPDATED_USAGE_COUNT)
            .lastScannedBy(UPDATED_LAST_SCANNED_BY)
            .createdAt(UPDATED_CREATED_AT);
        return qrCodeUsage;
    }

    @BeforeEach
    public void initTest() {
        qrCodeUsage = createEntity(em);
    }

    @Test
    @Transactional
    void createQrCodeUsage() throws Exception {
        int databaseSizeBeforeCreate = qrCodeUsageRepository.findAll().size();
        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);
        restQrCodeUsageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeCreate + 1);
        QrCodeUsage testQrCodeUsage = qrCodeUsageList.get(qrCodeUsageList.size() - 1);
        assertThat(testQrCodeUsage.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testQrCodeUsage.getQrCodeData()).isEqualTo(DEFAULT_QR_CODE_DATA);
        assertThat(testQrCodeUsage.getGeneratedAt()).isEqualTo(DEFAULT_GENERATED_AT);
        assertThat(testQrCodeUsage.getUsedAt()).isEqualTo(DEFAULT_USED_AT);
        assertThat(testQrCodeUsage.getUsageCount()).isEqualTo(DEFAULT_USAGE_COUNT);
        assertThat(testQrCodeUsage.getLastScannedBy()).isEqualTo(DEFAULT_LAST_SCANNED_BY);
        assertThat(testQrCodeUsage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createQrCodeUsageWithExistingId() throws Exception {
        // Create the QrCodeUsage with an existing ID
        qrCodeUsage.setId(1L);
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        int databaseSizeBeforeCreate = qrCodeUsageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQrCodeUsageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQrCodeDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrCodeUsageRepository.findAll().size();
        // set the field null
        qrCodeUsage.setQrCodeData(null);

        // Create the QrCodeUsage, which fails.
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        restQrCodeUsageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneratedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrCodeUsageRepository.findAll().size();
        // set the field null
        qrCodeUsage.setGeneratedAt(null);

        // Create the QrCodeUsage, which fails.
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        restQrCodeUsageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrCodeUsageRepository.findAll().size();
        // set the field null
        qrCodeUsage.setCreatedAt(null);

        // Create the QrCodeUsage, which fails.
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        restQrCodeUsageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQrCodeUsages() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList
        restQrCodeUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrCodeUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].qrCodeData").value(hasItem(DEFAULT_QR_CODE_DATA)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(sameInstant(DEFAULT_GENERATED_AT))))
            .andExpect(jsonPath("$.[*].usedAt").value(hasItem(sameInstant(DEFAULT_USED_AT))))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].lastScannedBy").value(hasItem(DEFAULT_LAST_SCANNED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    void getQrCodeUsage() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get the qrCodeUsage
        restQrCodeUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, qrCodeUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qrCodeUsage.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.qrCodeData").value(DEFAULT_QR_CODE_DATA))
            .andExpect(jsonPath("$.generatedAt").value(sameInstant(DEFAULT_GENERATED_AT)))
            .andExpect(jsonPath("$.usedAt").value(sameInstant(DEFAULT_USED_AT)))
            .andExpect(jsonPath("$.usageCount").value(DEFAULT_USAGE_COUNT))
            .andExpect(jsonPath("$.lastScannedBy").value(DEFAULT_LAST_SCANNED_BY))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    void getQrCodeUsagesByIdFiltering() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        Long id = qrCodeUsage.getId();

        defaultQrCodeUsageShouldBeFound("id.equals=" + id);
        defaultQrCodeUsageShouldNotBeFound("id.notEquals=" + id);

        defaultQrCodeUsageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQrCodeUsageShouldNotBeFound("id.greaterThan=" + id);

        defaultQrCodeUsageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQrCodeUsageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where tenantId equals to DEFAULT_TENANT_ID
        defaultQrCodeUsageShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the qrCodeUsageList where tenantId equals to UPDATED_TENANT_ID
        defaultQrCodeUsageShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultQrCodeUsageShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the qrCodeUsageList where tenantId equals to UPDATED_TENANT_ID
        defaultQrCodeUsageShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where tenantId is not null
        defaultQrCodeUsageShouldBeFound("tenantId.specified=true");

        // Get all the qrCodeUsageList where tenantId is null
        defaultQrCodeUsageShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where tenantId contains DEFAULT_TENANT_ID
        defaultQrCodeUsageShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the qrCodeUsageList where tenantId contains UPDATED_TENANT_ID
        defaultQrCodeUsageShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where tenantId does not contain DEFAULT_TENANT_ID
        defaultQrCodeUsageShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the qrCodeUsageList where tenantId does not contain UPDATED_TENANT_ID
        defaultQrCodeUsageShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByQrCodeDataIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where qrCodeData equals to DEFAULT_QR_CODE_DATA
        defaultQrCodeUsageShouldBeFound("qrCodeData.equals=" + DEFAULT_QR_CODE_DATA);

        // Get all the qrCodeUsageList where qrCodeData equals to UPDATED_QR_CODE_DATA
        defaultQrCodeUsageShouldNotBeFound("qrCodeData.equals=" + UPDATED_QR_CODE_DATA);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByQrCodeDataIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where qrCodeData in DEFAULT_QR_CODE_DATA or UPDATED_QR_CODE_DATA
        defaultQrCodeUsageShouldBeFound("qrCodeData.in=" + DEFAULT_QR_CODE_DATA + "," + UPDATED_QR_CODE_DATA);

        // Get all the qrCodeUsageList where qrCodeData equals to UPDATED_QR_CODE_DATA
        defaultQrCodeUsageShouldNotBeFound("qrCodeData.in=" + UPDATED_QR_CODE_DATA);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByQrCodeDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where qrCodeData is not null
        defaultQrCodeUsageShouldBeFound("qrCodeData.specified=true");

        // Get all the qrCodeUsageList where qrCodeData is null
        defaultQrCodeUsageShouldNotBeFound("qrCodeData.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByQrCodeDataContainsSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where qrCodeData contains DEFAULT_QR_CODE_DATA
        defaultQrCodeUsageShouldBeFound("qrCodeData.contains=" + DEFAULT_QR_CODE_DATA);

        // Get all the qrCodeUsageList where qrCodeData contains UPDATED_QR_CODE_DATA
        defaultQrCodeUsageShouldNotBeFound("qrCodeData.contains=" + UPDATED_QR_CODE_DATA);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByQrCodeDataNotContainsSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where qrCodeData does not contain DEFAULT_QR_CODE_DATA
        defaultQrCodeUsageShouldNotBeFound("qrCodeData.doesNotContain=" + DEFAULT_QR_CODE_DATA);

        // Get all the qrCodeUsageList where qrCodeData does not contain UPDATED_QR_CODE_DATA
        defaultQrCodeUsageShouldBeFound("qrCodeData.doesNotContain=" + UPDATED_QR_CODE_DATA);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt equals to DEFAULT_GENERATED_AT
        defaultQrCodeUsageShouldBeFound("generatedAt.equals=" + DEFAULT_GENERATED_AT);

        // Get all the qrCodeUsageList where generatedAt equals to UPDATED_GENERATED_AT
        defaultQrCodeUsageShouldNotBeFound("generatedAt.equals=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt in DEFAULT_GENERATED_AT or UPDATED_GENERATED_AT
        defaultQrCodeUsageShouldBeFound("generatedAt.in=" + DEFAULT_GENERATED_AT + "," + UPDATED_GENERATED_AT);

        // Get all the qrCodeUsageList where generatedAt equals to UPDATED_GENERATED_AT
        defaultQrCodeUsageShouldNotBeFound("generatedAt.in=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt is not null
        defaultQrCodeUsageShouldBeFound("generatedAt.specified=true");

        // Get all the qrCodeUsageList where generatedAt is null
        defaultQrCodeUsageShouldNotBeFound("generatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt is greater than or equal to DEFAULT_GENERATED_AT
        defaultQrCodeUsageShouldBeFound("generatedAt.greaterThanOrEqual=" + DEFAULT_GENERATED_AT);

        // Get all the qrCodeUsageList where generatedAt is greater than or equal to UPDATED_GENERATED_AT
        defaultQrCodeUsageShouldNotBeFound("generatedAt.greaterThanOrEqual=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt is less than or equal to DEFAULT_GENERATED_AT
        defaultQrCodeUsageShouldBeFound("generatedAt.lessThanOrEqual=" + DEFAULT_GENERATED_AT);

        // Get all the qrCodeUsageList where generatedAt is less than or equal to SMALLER_GENERATED_AT
        defaultQrCodeUsageShouldNotBeFound("generatedAt.lessThanOrEqual=" + SMALLER_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt is less than DEFAULT_GENERATED_AT
        defaultQrCodeUsageShouldNotBeFound("generatedAt.lessThan=" + DEFAULT_GENERATED_AT);

        // Get all the qrCodeUsageList where generatedAt is less than UPDATED_GENERATED_AT
        defaultQrCodeUsageShouldBeFound("generatedAt.lessThan=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByGeneratedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where generatedAt is greater than DEFAULT_GENERATED_AT
        defaultQrCodeUsageShouldNotBeFound("generatedAt.greaterThan=" + DEFAULT_GENERATED_AT);

        // Get all the qrCodeUsageList where generatedAt is greater than SMALLER_GENERATED_AT
        defaultQrCodeUsageShouldBeFound("generatedAt.greaterThan=" + SMALLER_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt equals to DEFAULT_USED_AT
        defaultQrCodeUsageShouldBeFound("usedAt.equals=" + DEFAULT_USED_AT);

        // Get all the qrCodeUsageList where usedAt equals to UPDATED_USED_AT
        defaultQrCodeUsageShouldNotBeFound("usedAt.equals=" + UPDATED_USED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt in DEFAULT_USED_AT or UPDATED_USED_AT
        defaultQrCodeUsageShouldBeFound("usedAt.in=" + DEFAULT_USED_AT + "," + UPDATED_USED_AT);

        // Get all the qrCodeUsageList where usedAt equals to UPDATED_USED_AT
        defaultQrCodeUsageShouldNotBeFound("usedAt.in=" + UPDATED_USED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt is not null
        defaultQrCodeUsageShouldBeFound("usedAt.specified=true");

        // Get all the qrCodeUsageList where usedAt is null
        defaultQrCodeUsageShouldNotBeFound("usedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt is greater than or equal to DEFAULT_USED_AT
        defaultQrCodeUsageShouldBeFound("usedAt.greaterThanOrEqual=" + DEFAULT_USED_AT);

        // Get all the qrCodeUsageList where usedAt is greater than or equal to UPDATED_USED_AT
        defaultQrCodeUsageShouldNotBeFound("usedAt.greaterThanOrEqual=" + UPDATED_USED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt is less than or equal to DEFAULT_USED_AT
        defaultQrCodeUsageShouldBeFound("usedAt.lessThanOrEqual=" + DEFAULT_USED_AT);

        // Get all the qrCodeUsageList where usedAt is less than or equal to SMALLER_USED_AT
        defaultQrCodeUsageShouldNotBeFound("usedAt.lessThanOrEqual=" + SMALLER_USED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt is less than DEFAULT_USED_AT
        defaultQrCodeUsageShouldNotBeFound("usedAt.lessThan=" + DEFAULT_USED_AT);

        // Get all the qrCodeUsageList where usedAt is less than UPDATED_USED_AT
        defaultQrCodeUsageShouldBeFound("usedAt.lessThan=" + UPDATED_USED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usedAt is greater than DEFAULT_USED_AT
        defaultQrCodeUsageShouldNotBeFound("usedAt.greaterThan=" + DEFAULT_USED_AT);

        // Get all the qrCodeUsageList where usedAt is greater than SMALLER_USED_AT
        defaultQrCodeUsageShouldBeFound("usedAt.greaterThan=" + SMALLER_USED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount equals to DEFAULT_USAGE_COUNT
        defaultQrCodeUsageShouldBeFound("usageCount.equals=" + DEFAULT_USAGE_COUNT);

        // Get all the qrCodeUsageList where usageCount equals to UPDATED_USAGE_COUNT
        defaultQrCodeUsageShouldNotBeFound("usageCount.equals=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount in DEFAULT_USAGE_COUNT or UPDATED_USAGE_COUNT
        defaultQrCodeUsageShouldBeFound("usageCount.in=" + DEFAULT_USAGE_COUNT + "," + UPDATED_USAGE_COUNT);

        // Get all the qrCodeUsageList where usageCount equals to UPDATED_USAGE_COUNT
        defaultQrCodeUsageShouldNotBeFound("usageCount.in=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount is not null
        defaultQrCodeUsageShouldBeFound("usageCount.specified=true");

        // Get all the qrCodeUsageList where usageCount is null
        defaultQrCodeUsageShouldNotBeFound("usageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount is greater than or equal to DEFAULT_USAGE_COUNT
        defaultQrCodeUsageShouldBeFound("usageCount.greaterThanOrEqual=" + DEFAULT_USAGE_COUNT);

        // Get all the qrCodeUsageList where usageCount is greater than or equal to UPDATED_USAGE_COUNT
        defaultQrCodeUsageShouldNotBeFound("usageCount.greaterThanOrEqual=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount is less than or equal to DEFAULT_USAGE_COUNT
        defaultQrCodeUsageShouldBeFound("usageCount.lessThanOrEqual=" + DEFAULT_USAGE_COUNT);

        // Get all the qrCodeUsageList where usageCount is less than or equal to SMALLER_USAGE_COUNT
        defaultQrCodeUsageShouldNotBeFound("usageCount.lessThanOrEqual=" + SMALLER_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount is less than DEFAULT_USAGE_COUNT
        defaultQrCodeUsageShouldNotBeFound("usageCount.lessThan=" + DEFAULT_USAGE_COUNT);

        // Get all the qrCodeUsageList where usageCount is less than UPDATED_USAGE_COUNT
        defaultQrCodeUsageShouldBeFound("usageCount.lessThan=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByUsageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where usageCount is greater than DEFAULT_USAGE_COUNT
        defaultQrCodeUsageShouldNotBeFound("usageCount.greaterThan=" + DEFAULT_USAGE_COUNT);

        // Get all the qrCodeUsageList where usageCount is greater than SMALLER_USAGE_COUNT
        defaultQrCodeUsageShouldBeFound("usageCount.greaterThan=" + SMALLER_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByLastScannedByIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where lastScannedBy equals to DEFAULT_LAST_SCANNED_BY
        defaultQrCodeUsageShouldBeFound("lastScannedBy.equals=" + DEFAULT_LAST_SCANNED_BY);

        // Get all the qrCodeUsageList where lastScannedBy equals to UPDATED_LAST_SCANNED_BY
        defaultQrCodeUsageShouldNotBeFound("lastScannedBy.equals=" + UPDATED_LAST_SCANNED_BY);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByLastScannedByIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where lastScannedBy in DEFAULT_LAST_SCANNED_BY or UPDATED_LAST_SCANNED_BY
        defaultQrCodeUsageShouldBeFound("lastScannedBy.in=" + DEFAULT_LAST_SCANNED_BY + "," + UPDATED_LAST_SCANNED_BY);

        // Get all the qrCodeUsageList where lastScannedBy equals to UPDATED_LAST_SCANNED_BY
        defaultQrCodeUsageShouldNotBeFound("lastScannedBy.in=" + UPDATED_LAST_SCANNED_BY);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByLastScannedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where lastScannedBy is not null
        defaultQrCodeUsageShouldBeFound("lastScannedBy.specified=true");

        // Get all the qrCodeUsageList where lastScannedBy is null
        defaultQrCodeUsageShouldNotBeFound("lastScannedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByLastScannedByContainsSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where lastScannedBy contains DEFAULT_LAST_SCANNED_BY
        defaultQrCodeUsageShouldBeFound("lastScannedBy.contains=" + DEFAULT_LAST_SCANNED_BY);

        // Get all the qrCodeUsageList where lastScannedBy contains UPDATED_LAST_SCANNED_BY
        defaultQrCodeUsageShouldNotBeFound("lastScannedBy.contains=" + UPDATED_LAST_SCANNED_BY);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByLastScannedByNotContainsSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where lastScannedBy does not contain DEFAULT_LAST_SCANNED_BY
        defaultQrCodeUsageShouldNotBeFound("lastScannedBy.doesNotContain=" + DEFAULT_LAST_SCANNED_BY);

        // Get all the qrCodeUsageList where lastScannedBy does not contain UPDATED_LAST_SCANNED_BY
        defaultQrCodeUsageShouldBeFound("lastScannedBy.doesNotContain=" + UPDATED_LAST_SCANNED_BY);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt equals to DEFAULT_CREATED_AT
        defaultQrCodeUsageShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the qrCodeUsageList where createdAt equals to UPDATED_CREATED_AT
        defaultQrCodeUsageShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultQrCodeUsageShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the qrCodeUsageList where createdAt equals to UPDATED_CREATED_AT
        defaultQrCodeUsageShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt is not null
        defaultQrCodeUsageShouldBeFound("createdAt.specified=true");

        // Get all the qrCodeUsageList where createdAt is null
        defaultQrCodeUsageShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultQrCodeUsageShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the qrCodeUsageList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultQrCodeUsageShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultQrCodeUsageShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the qrCodeUsageList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultQrCodeUsageShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt is less than DEFAULT_CREATED_AT
        defaultQrCodeUsageShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the qrCodeUsageList where createdAt is less than UPDATED_CREATED_AT
        defaultQrCodeUsageShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQrCodeUsagesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        // Get all the qrCodeUsageList where createdAt is greater than DEFAULT_CREATED_AT
        defaultQrCodeUsageShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the qrCodeUsageList where createdAt is greater than SMALLER_CREATED_AT
        defaultQrCodeUsageShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    /* @Test
    @Transactional
    void getAllQrCodeUsagesByAttendeeIsEqualToSomething() throws Exception {
        EventAttendee attendee;
        if (TestUtil.findAll(em, EventAttendee.class).isEmpty()) {
            qrCodeUsageRepository.saveAndFlush(qrCodeUsage);
            attendee = EventAttendeeResourceIT.createEntity(em);
        } else {
            attendee = TestUtil.findAll(em, EventAttendee.class).get(0);
        }
        em.persist(attendee);
        em.flush();
        qrCodeUsage.setAttendee(attendee);
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);
        Long attendeeId = attendee.getId();
        // Get all the qrCodeUsageList where attendee equals to attendeeId
        defaultQrCodeUsageShouldBeFound("attendeeId.equals=" + attendeeId);

        // Get all the qrCodeUsageList where attendee equals to (attendeeId + 1)
        defaultQrCodeUsageShouldNotBeFound("attendeeId.equals=" + (attendeeId + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQrCodeUsageShouldBeFound(String filter) throws Exception {
        restQrCodeUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrCodeUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].qrCodeData").value(hasItem(DEFAULT_QR_CODE_DATA)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(sameInstant(DEFAULT_GENERATED_AT))))
            .andExpect(jsonPath("$.[*].usedAt").value(hasItem(sameInstant(DEFAULT_USED_AT))))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].lastScannedBy").value(hasItem(DEFAULT_LAST_SCANNED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));

        // Check, that the count call also returns 1
        restQrCodeUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQrCodeUsageShouldNotBeFound(String filter) throws Exception {
        restQrCodeUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQrCodeUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQrCodeUsage() throws Exception {
        // Get the qrCodeUsage
        restQrCodeUsageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQrCodeUsage() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();

        // Update the qrCodeUsage
        QrCodeUsage updatedQrCodeUsage = qrCodeUsageRepository.findById(qrCodeUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQrCodeUsage are not directly saved in db
        em.detach(updatedQrCodeUsage);
        updatedQrCodeUsage
            .tenantId(UPDATED_TENANT_ID)
            .qrCodeData(UPDATED_QR_CODE_DATA)
            .generatedAt(UPDATED_GENERATED_AT)
            .usedAt(UPDATED_USED_AT)
            .usageCount(UPDATED_USAGE_COUNT)
            .lastScannedBy(UPDATED_LAST_SCANNED_BY)
            .createdAt(UPDATED_CREATED_AT);
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(updatedQrCodeUsage);

        restQrCodeUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qrCodeUsageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isOk());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
        QrCodeUsage testQrCodeUsage = qrCodeUsageList.get(qrCodeUsageList.size() - 1);
        assertThat(testQrCodeUsage.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testQrCodeUsage.getQrCodeData()).isEqualTo(UPDATED_QR_CODE_DATA);
        assertThat(testQrCodeUsage.getGeneratedAt()).isEqualTo(UPDATED_GENERATED_AT);
        assertThat(testQrCodeUsage.getUsedAt()).isEqualTo(UPDATED_USED_AT);
        assertThat(testQrCodeUsage.getUsageCount()).isEqualTo(UPDATED_USAGE_COUNT);
        assertThat(testQrCodeUsage.getLastScannedBy()).isEqualTo(UPDATED_LAST_SCANNED_BY);
        assertThat(testQrCodeUsage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingQrCodeUsage() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();
        qrCodeUsage.setId(longCount.incrementAndGet());

        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQrCodeUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qrCodeUsageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQrCodeUsage() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();
        qrCodeUsage.setId(longCount.incrementAndGet());

        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQrCodeUsage() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();
        qrCodeUsage.setId(longCount.incrementAndGet());

        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeUsageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQrCodeUsageWithPatch() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();

        // Update the qrCodeUsage using partial update
        QrCodeUsage partialUpdatedQrCodeUsage = new QrCodeUsage();
        partialUpdatedQrCodeUsage.setId(qrCodeUsage.getId());

        partialUpdatedQrCodeUsage
            .tenantId(UPDATED_TENANT_ID)
            .qrCodeData(UPDATED_QR_CODE_DATA)
            .usedAt(UPDATED_USED_AT)
            .usageCount(UPDATED_USAGE_COUNT);

        restQrCodeUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQrCodeUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQrCodeUsage))
            )
            .andExpect(status().isOk());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
        QrCodeUsage testQrCodeUsage = qrCodeUsageList.get(qrCodeUsageList.size() - 1);
        assertThat(testQrCodeUsage.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testQrCodeUsage.getQrCodeData()).isEqualTo(UPDATED_QR_CODE_DATA);
        assertThat(testQrCodeUsage.getGeneratedAt()).isEqualTo(DEFAULT_GENERATED_AT);
        assertThat(testQrCodeUsage.getUsedAt()).isEqualTo(UPDATED_USED_AT);
        assertThat(testQrCodeUsage.getUsageCount()).isEqualTo(UPDATED_USAGE_COUNT);
        assertThat(testQrCodeUsage.getLastScannedBy()).isEqualTo(DEFAULT_LAST_SCANNED_BY);
        assertThat(testQrCodeUsage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateQrCodeUsageWithPatch() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();

        // Update the qrCodeUsage using partial update
        QrCodeUsage partialUpdatedQrCodeUsage = new QrCodeUsage();
        partialUpdatedQrCodeUsage.setId(qrCodeUsage.getId());

        partialUpdatedQrCodeUsage
            .tenantId(UPDATED_TENANT_ID)
            .qrCodeData(UPDATED_QR_CODE_DATA)
            .generatedAt(UPDATED_GENERATED_AT)
            .usedAt(UPDATED_USED_AT)
            .usageCount(UPDATED_USAGE_COUNT)
            .lastScannedBy(UPDATED_LAST_SCANNED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restQrCodeUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQrCodeUsage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQrCodeUsage))
            )
            .andExpect(status().isOk());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
        QrCodeUsage testQrCodeUsage = qrCodeUsageList.get(qrCodeUsageList.size() - 1);
        assertThat(testQrCodeUsage.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testQrCodeUsage.getQrCodeData()).isEqualTo(UPDATED_QR_CODE_DATA);
        assertThat(testQrCodeUsage.getGeneratedAt()).isEqualTo(UPDATED_GENERATED_AT);
        assertThat(testQrCodeUsage.getUsedAt()).isEqualTo(UPDATED_USED_AT);
        assertThat(testQrCodeUsage.getUsageCount()).isEqualTo(UPDATED_USAGE_COUNT);
        assertThat(testQrCodeUsage.getLastScannedBy()).isEqualTo(UPDATED_LAST_SCANNED_BY);
        assertThat(testQrCodeUsage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingQrCodeUsage() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();
        qrCodeUsage.setId(longCount.incrementAndGet());

        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQrCodeUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, qrCodeUsageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQrCodeUsage() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();
        qrCodeUsage.setId(longCount.incrementAndGet());

        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQrCodeUsage() throws Exception {
        int databaseSizeBeforeUpdate = qrCodeUsageRepository.findAll().size();
        qrCodeUsage.setId(longCount.incrementAndGet());

        // Create the QrCodeUsage
        QrCodeUsageDTO qrCodeUsageDTO = qrCodeUsageMapper.toDto(qrCodeUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQrCodeUsageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(qrCodeUsageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QrCodeUsage in the database
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQrCodeUsage() throws Exception {
        // Initialize the database
        qrCodeUsageRepository.saveAndFlush(qrCodeUsage);

        int databaseSizeBeforeDelete = qrCodeUsageRepository.findAll().size();

        // Delete the qrCodeUsage
        restQrCodeUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, qrCodeUsage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QrCodeUsage> qrCodeUsageList = qrCodeUsageRepository.findAll();
        assertThat(qrCodeUsageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
