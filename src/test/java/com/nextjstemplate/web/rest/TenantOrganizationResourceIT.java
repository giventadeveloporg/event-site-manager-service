package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantOrganizationRepository;
import com.nextjstemplate.service.dto.TenantOrganizationDTO;
import com.nextjstemplate.service.mapper.TenantOrganizationMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link TenantOrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantOrganizationResourceIT {/*

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_COLOR = "AAAAAAA";
    private static final String UPDATED_PRIMARY_COLOR = "BBBBBBB";

    private static final String DEFAULT_SECONDARY_COLOR = "AAAAAAA";
    private static final String UPDATED_SECONDARY_COLOR = "BBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_PLAN = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_PLAN = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SUBSCRIPTION_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBSCRIPTION_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SUBSCRIPTION_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_SUBSCRIPTION_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBSCRIPTION_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SUBSCRIPTION_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_MONTHLY_FEE_USD = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_FEE_USD = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_FEE_USD = new BigDecimal(1 - 1);

    private static final String DEFAULT_STRIPE_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_STRIPE_CUSTOMER_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/tenant-organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantOrganizationRepository tenantOrganizationRepository;

    @Autowired
    private TenantOrganizationMapper tenantOrganizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantOrganizationMockMvc;

    private TenantOrganization tenantOrganization;

    */
    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static TenantOrganization createEntity(EntityManager em) {
        TenantOrganization tenantOrganization = new TenantOrganization()
            .tenantId(DEFAULT_TENANT_ID)
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .domain(DEFAULT_DOMAIN)
            .primaryColor(DEFAULT_PRIMARY_COLOR)
            .secondaryColor(DEFAULT_SECONDARY_COLOR)
            .logoUrl(DEFAULT_LOGO_URL)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .contactPhone(DEFAULT_CONTACT_PHONE)
            .subscriptionPlan(DEFAULT_SUBSCRIPTION_PLAN)
            .subscriptionStatus(DEFAULT_SUBSCRIPTION_STATUS)
            .subscriptionStartDate(DEFAULT_SUBSCRIPTION_START_DATE)
            .subscriptionEndDate(DEFAULT_SUBSCRIPTION_END_DATE)
            .monthlyFeeUsd(DEFAULT_MONTHLY_FEE_USD)
            .stripeCustomerId(DEFAULT_STRIPE_CUSTOMER_ID)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return tenantOrganization;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static TenantOrganization createUpdatedEntity(EntityManager em) {
        TenantOrganization tenantOrganization = new TenantOrganization()
            .tenantId(UPDATED_TENANT_ID)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .domain(UPDATED_DOMAIN)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .logoUrl(UPDATED_LOGO_URL)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .subscriptionPlan(UPDATED_SUBSCRIPTION_PLAN)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionEndDate(UPDATED_SUBSCRIPTION_END_DATE)
            .monthlyFeeUsd(UPDATED_MONTHLY_FEE_USD)
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return tenantOrganization;
    }

    @BeforeEach
    public void initTest() {
        tenantOrganization = createEntity(em);
    }

    @Test
    @Transactional
    void createTenantOrganization() throws Exception {
        int databaseSizeBeforeCreate = tenantOrganizationRepository.findAll().size();
        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);
        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeCreate + 1);
        TenantOrganization testTenantOrganization = tenantOrganizationList.get(tenantOrganizationList.size() - 1);
        assertThat(testTenantOrganization.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testTenantOrganization.getOrganizationName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
        assertThat(testTenantOrganization.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testTenantOrganization.getPrimaryColor()).isEqualTo(DEFAULT_PRIMARY_COLOR);
        assertThat(testTenantOrganization.getSecondaryColor()).isEqualTo(DEFAULT_SECONDARY_COLOR);
        assertThat(testTenantOrganization.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testTenantOrganization.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testTenantOrganization.getContactPhone()).isEqualTo(DEFAULT_CONTACT_PHONE);
        assertThat(testTenantOrganization.getSubscriptionPlan()).isEqualTo(DEFAULT_SUBSCRIPTION_PLAN);
        assertThat(testTenantOrganization.getSubscriptionStatus()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS);
        assertThat(testTenantOrganization.getSubscriptionStartDate()).isEqualTo(DEFAULT_SUBSCRIPTION_START_DATE);
        assertThat(testTenantOrganization.getSubscriptionEndDate()).isEqualTo(DEFAULT_SUBSCRIPTION_END_DATE);
        assertThat(testTenantOrganization.getMonthlyFeeUsd()).isEqualByComparingTo(DEFAULT_MONTHLY_FEE_USD);
        assertThat(testTenantOrganization.getStripeCustomerId()).isEqualTo(DEFAULT_STRIPE_CUSTOMER_ID);
        assertThat(testTenantOrganization.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testTenantOrganization.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTenantOrganization.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTenantOrganizationWithExistingId() throws Exception {
        // Create the TenantOrganization with an existing ID
        tenantOrganization.setId(1L);
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        int databaseSizeBeforeCreate = tenantOrganizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenantIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setTenantId(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrganizationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setOrganizationName(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setContactEmail(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionPlanIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setSubscriptionPlan(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setSubscriptionStatus(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setCreatedAt(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantOrganizationRepository.findAll().size();
        // set the field null
        tenantOrganization.setUpdatedAt(null);

        // Create the TenantOrganization, which fails.
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantOrganizations() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList
        restTenantOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].secondaryColor").value(hasItem(DEFAULT_SECONDARY_COLOR)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].subscriptionPlan").value(hasItem(DEFAULT_SUBSCRIPTION_PLAN)))
            .andExpect(jsonPath("$.[*].subscriptionStatus").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS)))
            .andExpect(jsonPath("$.[*].subscriptionStartDate").value(hasItem(DEFAULT_SUBSCRIPTION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionEndDate").value(hasItem(DEFAULT_SUBSCRIPTION_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].monthlyFeeUsd").value(hasItem(sameNumber(DEFAULT_MONTHLY_FEE_USD))))
            .andExpect(jsonPath("$.[*].stripeCustomerId").value(hasItem(DEFAULT_STRIPE_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getTenantOrganization() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get the tenantOrganization
        restTenantOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantOrganization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantOrganization.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN))
            .andExpect(jsonPath("$.primaryColor").value(DEFAULT_PRIMARY_COLOR))
            .andExpect(jsonPath("$.secondaryColor").value(DEFAULT_SECONDARY_COLOR))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.contactPhone").value(DEFAULT_CONTACT_PHONE))
            .andExpect(jsonPath("$.subscriptionPlan").value(DEFAULT_SUBSCRIPTION_PLAN))
            .andExpect(jsonPath("$.subscriptionStatus").value(DEFAULT_SUBSCRIPTION_STATUS))
            .andExpect(jsonPath("$.subscriptionStartDate").value(DEFAULT_SUBSCRIPTION_START_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionEndDate").value(DEFAULT_SUBSCRIPTION_END_DATE.toString()))
            .andExpect(jsonPath("$.monthlyFeeUsd").value(sameNumber(DEFAULT_MONTHLY_FEE_USD)))
            .andExpect(jsonPath("$.stripeCustomerId").value(DEFAULT_STRIPE_CUSTOMER_ID))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getTenantOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        Long id = tenantOrganization.getId();

        defaultTenantOrganizationShouldBeFound("id.equals=" + id);
        defaultTenantOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultTenantOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTenantOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultTenantOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTenantOrganizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where tenantId equals to DEFAULT_TENANT_ID
        defaultTenantOrganizationShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the tenantOrganizationList where tenantId equals to UPDATED_TENANT_ID
        defaultTenantOrganizationShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultTenantOrganizationShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the tenantOrganizationList where tenantId equals to UPDATED_TENANT_ID
        defaultTenantOrganizationShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where tenantId is not null
        defaultTenantOrganizationShouldBeFound("tenantId.specified=true");

        // Get all the tenantOrganizationList where tenantId is null
        defaultTenantOrganizationShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where tenantId contains DEFAULT_TENANT_ID
        defaultTenantOrganizationShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the tenantOrganizationList where tenantId contains UPDATED_TENANT_ID
        defaultTenantOrganizationShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where tenantId does not contain DEFAULT_TENANT_ID
        defaultTenantOrganizationShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the tenantOrganizationList where tenantId does not contain UPDATED_TENANT_ID
        defaultTenantOrganizationShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByOrganizationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where organizationName equals to DEFAULT_ORGANIZATION_NAME
        defaultTenantOrganizationShouldBeFound("organizationName.equals=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the tenantOrganizationList where organizationName equals to UPDATED_ORGANIZATION_NAME
        defaultTenantOrganizationShouldNotBeFound("organizationName.equals=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByOrganizationNameIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where organizationName in DEFAULT_ORGANIZATION_NAME or UPDATED_ORGANIZATION_NAME
        defaultTenantOrganizationShouldBeFound("organizationName.in=" + DEFAULT_ORGANIZATION_NAME + "," + UPDATED_ORGANIZATION_NAME);

        // Get all the tenantOrganizationList where organizationName equals to UPDATED_ORGANIZATION_NAME
        defaultTenantOrganizationShouldNotBeFound("organizationName.in=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByOrganizationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where organizationName is not null
        defaultTenantOrganizationShouldBeFound("organizationName.specified=true");

        // Get all the tenantOrganizationList where organizationName is null
        defaultTenantOrganizationShouldNotBeFound("organizationName.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByOrganizationNameContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where organizationName contains DEFAULT_ORGANIZATION_NAME
        defaultTenantOrganizationShouldBeFound("organizationName.contains=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the tenantOrganizationList where organizationName contains UPDATED_ORGANIZATION_NAME
        defaultTenantOrganizationShouldNotBeFound("organizationName.contains=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByOrganizationNameNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where organizationName does not contain DEFAULT_ORGANIZATION_NAME
        defaultTenantOrganizationShouldNotBeFound("organizationName.doesNotContain=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the tenantOrganizationList where organizationName does not contain UPDATED_ORGANIZATION_NAME
        defaultTenantOrganizationShouldBeFound("organizationName.doesNotContain=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByDomainIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where domain equals to DEFAULT_DOMAIN
        defaultTenantOrganizationShouldBeFound("domain.equals=" + DEFAULT_DOMAIN);

        // Get all the tenantOrganizationList where domain equals to UPDATED_DOMAIN
        defaultTenantOrganizationShouldNotBeFound("domain.equals=" + UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByDomainIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where domain in DEFAULT_DOMAIN or UPDATED_DOMAIN
        defaultTenantOrganizationShouldBeFound("domain.in=" + DEFAULT_DOMAIN + "," + UPDATED_DOMAIN);

        // Get all the tenantOrganizationList where domain equals to UPDATED_DOMAIN
        defaultTenantOrganizationShouldNotBeFound("domain.in=" + UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByDomainIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where domain is not null
        defaultTenantOrganizationShouldBeFound("domain.specified=true");

        // Get all the tenantOrganizationList where domain is null
        defaultTenantOrganizationShouldNotBeFound("domain.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByDomainContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where domain contains DEFAULT_DOMAIN
        defaultTenantOrganizationShouldBeFound("domain.contains=" + DEFAULT_DOMAIN);

        // Get all the tenantOrganizationList where domain contains UPDATED_DOMAIN
        defaultTenantOrganizationShouldNotBeFound("domain.contains=" + UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByDomainNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where domain does not contain DEFAULT_DOMAIN
        defaultTenantOrganizationShouldNotBeFound("domain.doesNotContain=" + DEFAULT_DOMAIN);

        // Get all the tenantOrganizationList where domain does not contain UPDATED_DOMAIN
        defaultTenantOrganizationShouldBeFound("domain.doesNotContain=" + UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByPrimaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where primaryColor equals to DEFAULT_PRIMARY_COLOR
        defaultTenantOrganizationShouldBeFound("primaryColor.equals=" + DEFAULT_PRIMARY_COLOR);

        // Get all the tenantOrganizationList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("primaryColor.equals=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByPrimaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where primaryColor in DEFAULT_PRIMARY_COLOR or UPDATED_PRIMARY_COLOR
        defaultTenantOrganizationShouldBeFound("primaryColor.in=" + DEFAULT_PRIMARY_COLOR + "," + UPDATED_PRIMARY_COLOR);

        // Get all the tenantOrganizationList where primaryColor equals to UPDATED_PRIMARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("primaryColor.in=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByPrimaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where primaryColor is not null
        defaultTenantOrganizationShouldBeFound("primaryColor.specified=true");

        // Get all the tenantOrganizationList where primaryColor is null
        defaultTenantOrganizationShouldNotBeFound("primaryColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByPrimaryColorContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where primaryColor contains DEFAULT_PRIMARY_COLOR
        defaultTenantOrganizationShouldBeFound("primaryColor.contains=" + DEFAULT_PRIMARY_COLOR);

        // Get all the tenantOrganizationList where primaryColor contains UPDATED_PRIMARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("primaryColor.contains=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByPrimaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where primaryColor does not contain DEFAULT_PRIMARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("primaryColor.doesNotContain=" + DEFAULT_PRIMARY_COLOR);

        // Get all the tenantOrganizationList where primaryColor does not contain UPDATED_PRIMARY_COLOR
        defaultTenantOrganizationShouldBeFound("primaryColor.doesNotContain=" + UPDATED_PRIMARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySecondaryColorIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where secondaryColor equals to DEFAULT_SECONDARY_COLOR
        defaultTenantOrganizationShouldBeFound("secondaryColor.equals=" + DEFAULT_SECONDARY_COLOR);

        // Get all the tenantOrganizationList where secondaryColor equals to UPDATED_SECONDARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("secondaryColor.equals=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySecondaryColorIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where secondaryColor in DEFAULT_SECONDARY_COLOR or UPDATED_SECONDARY_COLOR
        defaultTenantOrganizationShouldBeFound("secondaryColor.in=" + DEFAULT_SECONDARY_COLOR + "," + UPDATED_SECONDARY_COLOR);

        // Get all the tenantOrganizationList where secondaryColor equals to UPDATED_SECONDARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("secondaryColor.in=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySecondaryColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where secondaryColor is not null
        defaultTenantOrganizationShouldBeFound("secondaryColor.specified=true");

        // Get all the tenantOrganizationList where secondaryColor is null
        defaultTenantOrganizationShouldNotBeFound("secondaryColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySecondaryColorContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where secondaryColor contains DEFAULT_SECONDARY_COLOR
        defaultTenantOrganizationShouldBeFound("secondaryColor.contains=" + DEFAULT_SECONDARY_COLOR);

        // Get all the tenantOrganizationList where secondaryColor contains UPDATED_SECONDARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("secondaryColor.contains=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySecondaryColorNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where secondaryColor does not contain DEFAULT_SECONDARY_COLOR
        defaultTenantOrganizationShouldNotBeFound("secondaryColor.doesNotContain=" + DEFAULT_SECONDARY_COLOR);

        // Get all the tenantOrganizationList where secondaryColor does not contain UPDATED_SECONDARY_COLOR
        defaultTenantOrganizationShouldBeFound("secondaryColor.doesNotContain=" + UPDATED_SECONDARY_COLOR);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where logoUrl equals to DEFAULT_LOGO_URL
        defaultTenantOrganizationShouldBeFound("logoUrl.equals=" + DEFAULT_LOGO_URL);

        // Get all the tenantOrganizationList where logoUrl equals to UPDATED_LOGO_URL
        defaultTenantOrganizationShouldNotBeFound("logoUrl.equals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where logoUrl in DEFAULT_LOGO_URL or UPDATED_LOGO_URL
        defaultTenantOrganizationShouldBeFound("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL);

        // Get all the tenantOrganizationList where logoUrl equals to UPDATED_LOGO_URL
        defaultTenantOrganizationShouldNotBeFound("logoUrl.in=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where logoUrl is not null
        defaultTenantOrganizationShouldBeFound("logoUrl.specified=true");

        // Get all the tenantOrganizationList where logoUrl is null
        defaultTenantOrganizationShouldNotBeFound("logoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByLogoUrlContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where logoUrl contains DEFAULT_LOGO_URL
        defaultTenantOrganizationShouldBeFound("logoUrl.contains=" + DEFAULT_LOGO_URL);

        // Get all the tenantOrganizationList where logoUrl contains UPDATED_LOGO_URL
        defaultTenantOrganizationShouldNotBeFound("logoUrl.contains=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByLogoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where logoUrl does not contain DEFAULT_LOGO_URL
        defaultTenantOrganizationShouldNotBeFound("logoUrl.doesNotContain=" + DEFAULT_LOGO_URL);

        // Get all the tenantOrganizationList where logoUrl does not contain UPDATED_LOGO_URL
        defaultTenantOrganizationShouldBeFound("logoUrl.doesNotContain=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactEmail equals to DEFAULT_CONTACT_EMAIL
        defaultTenantOrganizationShouldBeFound("contactEmail.equals=" + DEFAULT_CONTACT_EMAIL);

        // Get all the tenantOrganizationList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultTenantOrganizationShouldNotBeFound("contactEmail.equals=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactEmail in DEFAULT_CONTACT_EMAIL or UPDATED_CONTACT_EMAIL
        defaultTenantOrganizationShouldBeFound("contactEmail.in=" + DEFAULT_CONTACT_EMAIL + "," + UPDATED_CONTACT_EMAIL);

        // Get all the tenantOrganizationList where contactEmail equals to UPDATED_CONTACT_EMAIL
        defaultTenantOrganizationShouldNotBeFound("contactEmail.in=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactEmail is not null
        defaultTenantOrganizationShouldBeFound("contactEmail.specified=true");

        // Get all the tenantOrganizationList where contactEmail is null
        defaultTenantOrganizationShouldNotBeFound("contactEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactEmailContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactEmail contains DEFAULT_CONTACT_EMAIL
        defaultTenantOrganizationShouldBeFound("contactEmail.contains=" + DEFAULT_CONTACT_EMAIL);

        // Get all the tenantOrganizationList where contactEmail contains UPDATED_CONTACT_EMAIL
        defaultTenantOrganizationShouldNotBeFound("contactEmail.contains=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactEmailNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactEmail does not contain DEFAULT_CONTACT_EMAIL
        defaultTenantOrganizationShouldNotBeFound("contactEmail.doesNotContain=" + DEFAULT_CONTACT_EMAIL);

        // Get all the tenantOrganizationList where contactEmail does not contain UPDATED_CONTACT_EMAIL
        defaultTenantOrganizationShouldBeFound("contactEmail.doesNotContain=" + UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactPhone equals to DEFAULT_CONTACT_PHONE
        defaultTenantOrganizationShouldBeFound("contactPhone.equals=" + DEFAULT_CONTACT_PHONE);

        // Get all the tenantOrganizationList where contactPhone equals to UPDATED_CONTACT_PHONE
        defaultTenantOrganizationShouldNotBeFound("contactPhone.equals=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactPhone in DEFAULT_CONTACT_PHONE or UPDATED_CONTACT_PHONE
        defaultTenantOrganizationShouldBeFound("contactPhone.in=" + DEFAULT_CONTACT_PHONE + "," + UPDATED_CONTACT_PHONE);

        // Get all the tenantOrganizationList where contactPhone equals to UPDATED_CONTACT_PHONE
        defaultTenantOrganizationShouldNotBeFound("contactPhone.in=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactPhone is not null
        defaultTenantOrganizationShouldBeFound("contactPhone.specified=true");

        // Get all the tenantOrganizationList where contactPhone is null
        defaultTenantOrganizationShouldNotBeFound("contactPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactPhoneContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactPhone contains DEFAULT_CONTACT_PHONE
        defaultTenantOrganizationShouldBeFound("contactPhone.contains=" + DEFAULT_CONTACT_PHONE);

        // Get all the tenantOrganizationList where contactPhone contains UPDATED_CONTACT_PHONE
        defaultTenantOrganizationShouldNotBeFound("contactPhone.contains=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByContactPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where contactPhone does not contain DEFAULT_CONTACT_PHONE
        defaultTenantOrganizationShouldNotBeFound("contactPhone.doesNotContain=" + DEFAULT_CONTACT_PHONE);

        // Get all the tenantOrganizationList where contactPhone does not contain UPDATED_CONTACT_PHONE
        defaultTenantOrganizationShouldBeFound("contactPhone.doesNotContain=" + UPDATED_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionPlan equals to DEFAULT_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldBeFound("subscriptionPlan.equals=" + DEFAULT_SUBSCRIPTION_PLAN);

        // Get all the tenantOrganizationList where subscriptionPlan equals to UPDATED_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldNotBeFound("subscriptionPlan.equals=" + UPDATED_SUBSCRIPTION_PLAN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionPlanIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionPlan in DEFAULT_SUBSCRIPTION_PLAN or UPDATED_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldBeFound("subscriptionPlan.in=" + DEFAULT_SUBSCRIPTION_PLAN + "," + UPDATED_SUBSCRIPTION_PLAN);

        // Get all the tenantOrganizationList where subscriptionPlan equals to UPDATED_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldNotBeFound("subscriptionPlan.in=" + UPDATED_SUBSCRIPTION_PLAN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionPlanIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionPlan is not null
        defaultTenantOrganizationShouldBeFound("subscriptionPlan.specified=true");

        // Get all the tenantOrganizationList where subscriptionPlan is null
        defaultTenantOrganizationShouldNotBeFound("subscriptionPlan.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionPlanContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionPlan contains DEFAULT_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldBeFound("subscriptionPlan.contains=" + DEFAULT_SUBSCRIPTION_PLAN);

        // Get all the tenantOrganizationList where subscriptionPlan contains UPDATED_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldNotBeFound("subscriptionPlan.contains=" + UPDATED_SUBSCRIPTION_PLAN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionPlanNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionPlan does not contain DEFAULT_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldNotBeFound("subscriptionPlan.doesNotContain=" + DEFAULT_SUBSCRIPTION_PLAN);

        // Get all the tenantOrganizationList where subscriptionPlan does not contain UPDATED_SUBSCRIPTION_PLAN
        defaultTenantOrganizationShouldBeFound("subscriptionPlan.doesNotContain=" + UPDATED_SUBSCRIPTION_PLAN);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStatus equals to DEFAULT_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldBeFound("subscriptionStatus.equals=" + DEFAULT_SUBSCRIPTION_STATUS);

        // Get all the tenantOrganizationList where subscriptionStatus equals to UPDATED_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldNotBeFound("subscriptionStatus.equals=" + UPDATED_SUBSCRIPTION_STATUS);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStatus in DEFAULT_SUBSCRIPTION_STATUS or UPDATED_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldBeFound("subscriptionStatus.in=" + DEFAULT_SUBSCRIPTION_STATUS + "," + UPDATED_SUBSCRIPTION_STATUS);

        // Get all the tenantOrganizationList where subscriptionStatus equals to UPDATED_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldNotBeFound("subscriptionStatus.in=" + UPDATED_SUBSCRIPTION_STATUS);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStatus is not null
        defaultTenantOrganizationShouldBeFound("subscriptionStatus.specified=true");

        // Get all the tenantOrganizationList where subscriptionStatus is null
        defaultTenantOrganizationShouldNotBeFound("subscriptionStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStatusContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStatus contains DEFAULT_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldBeFound("subscriptionStatus.contains=" + DEFAULT_SUBSCRIPTION_STATUS);

        // Get all the tenantOrganizationList where subscriptionStatus contains UPDATED_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldNotBeFound("subscriptionStatus.contains=" + UPDATED_SUBSCRIPTION_STATUS);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStatusNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStatus does not contain DEFAULT_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldNotBeFound("subscriptionStatus.doesNotContain=" + DEFAULT_SUBSCRIPTION_STATUS);

        // Get all the tenantOrganizationList where subscriptionStatus does not contain UPDATED_SUBSCRIPTION_STATUS
        defaultTenantOrganizationShouldBeFound("subscriptionStatus.doesNotContain=" + UPDATED_SUBSCRIPTION_STATUS);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate equals to DEFAULT_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionStartDate.equals=" + DEFAULT_SUBSCRIPTION_START_DATE);

        // Get all the tenantOrganizationList where subscriptionStartDate equals to UPDATED_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.equals=" + UPDATED_SUBSCRIPTION_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate in DEFAULT_SUBSCRIPTION_START_DATE or UPDATED_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldBeFound(
            "subscriptionStartDate.in=" + DEFAULT_SUBSCRIPTION_START_DATE + "," + UPDATED_SUBSCRIPTION_START_DATE
        );

        // Get all the tenantOrganizationList where subscriptionStartDate equals to UPDATED_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.in=" + UPDATED_SUBSCRIPTION_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate is not null
        defaultTenantOrganizationShouldBeFound("subscriptionStartDate.specified=true");

        // Get all the tenantOrganizationList where subscriptionStartDate is null
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate is greater than or equal to DEFAULT_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionStartDate.greaterThanOrEqual=" + DEFAULT_SUBSCRIPTION_START_DATE);

        // Get all the tenantOrganizationList where subscriptionStartDate is greater than or equal to UPDATED_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.greaterThanOrEqual=" + UPDATED_SUBSCRIPTION_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate is less than or equal to DEFAULT_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionStartDate.lessThanOrEqual=" + DEFAULT_SUBSCRIPTION_START_DATE);

        // Get all the tenantOrganizationList where subscriptionStartDate is less than or equal to SMALLER_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.lessThanOrEqual=" + SMALLER_SUBSCRIPTION_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate is less than DEFAULT_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.lessThan=" + DEFAULT_SUBSCRIPTION_START_DATE);

        // Get all the tenantOrganizationList where subscriptionStartDate is less than UPDATED_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionStartDate.lessThan=" + UPDATED_SUBSCRIPTION_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionStartDate is greater than DEFAULT_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionStartDate.greaterThan=" + DEFAULT_SUBSCRIPTION_START_DATE);

        // Get all the tenantOrganizationList where subscriptionStartDate is greater than SMALLER_SUBSCRIPTION_START_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionStartDate.greaterThan=" + SMALLER_SUBSCRIPTION_START_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate equals to DEFAULT_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionEndDate.equals=" + DEFAULT_SUBSCRIPTION_END_DATE);

        // Get all the tenantOrganizationList where subscriptionEndDate equals to UPDATED_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.equals=" + UPDATED_SUBSCRIPTION_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate in DEFAULT_SUBSCRIPTION_END_DATE or UPDATED_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldBeFound(
            "subscriptionEndDate.in=" + DEFAULT_SUBSCRIPTION_END_DATE + "," + UPDATED_SUBSCRIPTION_END_DATE
        );

        // Get all the tenantOrganizationList where subscriptionEndDate equals to UPDATED_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.in=" + UPDATED_SUBSCRIPTION_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate is not null
        defaultTenantOrganizationShouldBeFound("subscriptionEndDate.specified=true");

        // Get all the tenantOrganizationList where subscriptionEndDate is null
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate is greater than or equal to DEFAULT_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionEndDate.greaterThanOrEqual=" + DEFAULT_SUBSCRIPTION_END_DATE);

        // Get all the tenantOrganizationList where subscriptionEndDate is greater than or equal to UPDATED_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.greaterThanOrEqual=" + UPDATED_SUBSCRIPTION_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate is less than or equal to DEFAULT_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionEndDate.lessThanOrEqual=" + DEFAULT_SUBSCRIPTION_END_DATE);

        // Get all the tenantOrganizationList where subscriptionEndDate is less than or equal to SMALLER_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.lessThanOrEqual=" + SMALLER_SUBSCRIPTION_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate is less than DEFAULT_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.lessThan=" + DEFAULT_SUBSCRIPTION_END_DATE);

        // Get all the tenantOrganizationList where subscriptionEndDate is less than UPDATED_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionEndDate.lessThan=" + UPDATED_SUBSCRIPTION_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsBySubscriptionEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where subscriptionEndDate is greater than DEFAULT_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldNotBeFound("subscriptionEndDate.greaterThan=" + DEFAULT_SUBSCRIPTION_END_DATE);

        // Get all the tenantOrganizationList where subscriptionEndDate is greater than SMALLER_SUBSCRIPTION_END_DATE
        defaultTenantOrganizationShouldBeFound("subscriptionEndDate.greaterThan=" + SMALLER_SUBSCRIPTION_END_DATE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd equals to DEFAULT_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.equals=" + DEFAULT_MONTHLY_FEE_USD);

        // Get all the tenantOrganizationList where monthlyFeeUsd equals to UPDATED_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.equals=" + UPDATED_MONTHLY_FEE_USD);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd in DEFAULT_MONTHLY_FEE_USD or UPDATED_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.in=" + DEFAULT_MONTHLY_FEE_USD + "," + UPDATED_MONTHLY_FEE_USD);

        // Get all the tenantOrganizationList where monthlyFeeUsd equals to UPDATED_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.in=" + UPDATED_MONTHLY_FEE_USD);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd is not null
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.specified=true");

        // Get all the tenantOrganizationList where monthlyFeeUsd is null
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd is greater than or equal to DEFAULT_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.greaterThanOrEqual=" + DEFAULT_MONTHLY_FEE_USD);

        // Get all the tenantOrganizationList where monthlyFeeUsd is greater than or equal to UPDATED_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.greaterThanOrEqual=" + UPDATED_MONTHLY_FEE_USD);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd is less than or equal to DEFAULT_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.lessThanOrEqual=" + DEFAULT_MONTHLY_FEE_USD);

        // Get all the tenantOrganizationList where monthlyFeeUsd is less than or equal to SMALLER_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.lessThanOrEqual=" + SMALLER_MONTHLY_FEE_USD);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd is less than DEFAULT_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.lessThan=" + DEFAULT_MONTHLY_FEE_USD);

        // Get all the tenantOrganizationList where monthlyFeeUsd is less than UPDATED_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.lessThan=" + UPDATED_MONTHLY_FEE_USD);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByMonthlyFeeUsdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where monthlyFeeUsd is greater than DEFAULT_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldNotBeFound("monthlyFeeUsd.greaterThan=" + DEFAULT_MONTHLY_FEE_USD);

        // Get all the tenantOrganizationList where monthlyFeeUsd is greater than SMALLER_MONTHLY_FEE_USD
        defaultTenantOrganizationShouldBeFound("monthlyFeeUsd.greaterThan=" + SMALLER_MONTHLY_FEE_USD);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByStripeCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where stripeCustomerId equals to DEFAULT_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldBeFound("stripeCustomerId.equals=" + DEFAULT_STRIPE_CUSTOMER_ID);

        // Get all the tenantOrganizationList where stripeCustomerId equals to UPDATED_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldNotBeFound("stripeCustomerId.equals=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByStripeCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where stripeCustomerId in DEFAULT_STRIPE_CUSTOMER_ID or UPDATED_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldBeFound("stripeCustomerId.in=" + DEFAULT_STRIPE_CUSTOMER_ID + "," + UPDATED_STRIPE_CUSTOMER_ID);

        // Get all the tenantOrganizationList where stripeCustomerId equals to UPDATED_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldNotBeFound("stripeCustomerId.in=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByStripeCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where stripeCustomerId is not null
        defaultTenantOrganizationShouldBeFound("stripeCustomerId.specified=true");

        // Get all the tenantOrganizationList where stripeCustomerId is null
        defaultTenantOrganizationShouldNotBeFound("stripeCustomerId.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByStripeCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where stripeCustomerId contains DEFAULT_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldBeFound("stripeCustomerId.contains=" + DEFAULT_STRIPE_CUSTOMER_ID);

        // Get all the tenantOrganizationList where stripeCustomerId contains UPDATED_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldNotBeFound("stripeCustomerId.contains=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByStripeCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where stripeCustomerId does not contain DEFAULT_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldNotBeFound("stripeCustomerId.doesNotContain=" + DEFAULT_STRIPE_CUSTOMER_ID);

        // Get all the tenantOrganizationList where stripeCustomerId does not contain UPDATED_STRIPE_CUSTOMER_ID
        defaultTenantOrganizationShouldBeFound("stripeCustomerId.doesNotContain=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where isActive equals to DEFAULT_IS_ACTIVE
        defaultTenantOrganizationShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the tenantOrganizationList where isActive equals to UPDATED_IS_ACTIVE
        defaultTenantOrganizationShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultTenantOrganizationShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the tenantOrganizationList where isActive equals to UPDATED_IS_ACTIVE
        defaultTenantOrganizationShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where isActive is not null
        defaultTenantOrganizationShouldBeFound("isActive.specified=true");

        // Get all the tenantOrganizationList where isActive is null
        defaultTenantOrganizationShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt equals to DEFAULT_CREATED_AT
        defaultTenantOrganizationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the tenantOrganizationList where createdAt equals to UPDATED_CREATED_AT
        defaultTenantOrganizationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTenantOrganizationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the tenantOrganizationList where createdAt equals to UPDATED_CREATED_AT
        defaultTenantOrganizationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt is not null
        defaultTenantOrganizationShouldBeFound("createdAt.specified=true");

        // Get all the tenantOrganizationList where createdAt is null
        defaultTenantOrganizationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultTenantOrganizationShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the tenantOrganizationList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultTenantOrganizationShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultTenantOrganizationShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the tenantOrganizationList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultTenantOrganizationShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt is less than DEFAULT_CREATED_AT
        defaultTenantOrganizationShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the tenantOrganizationList where createdAt is less than UPDATED_CREATED_AT
        defaultTenantOrganizationShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where createdAt is greater than DEFAULT_CREATED_AT
        defaultTenantOrganizationShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the tenantOrganizationList where createdAt is greater than SMALLER_CREATED_AT
        defaultTenantOrganizationShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTenantOrganizationShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the tenantOrganizationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTenantOrganizationShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTenantOrganizationShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the tenantOrganizationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTenantOrganizationShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt is not null
        defaultTenantOrganizationShouldBeFound("updatedAt.specified=true");

        // Get all the tenantOrganizationList where updatedAt is null
        defaultTenantOrganizationShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultTenantOrganizationShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the tenantOrganizationList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultTenantOrganizationShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultTenantOrganizationShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the tenantOrganizationList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultTenantOrganizationShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultTenantOrganizationShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the tenantOrganizationList where updatedAt is less than UPDATED_UPDATED_AT
        defaultTenantOrganizationShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantOrganizationsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        // Get all the tenantOrganizationList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultTenantOrganizationShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the tenantOrganizationList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultTenantOrganizationShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

   *//* @Test
    @Transactional
    void getAllTenantOrganizationsByTenantSettingsIsEqualToSomething() throws Exception {
        TenantSettings tenantSettings;
        if (TestUtil.findAll(em, TenantSettings.class).isEmpty()) {
            tenantOrganizationRepository.saveAndFlush(tenantOrganization);
            tenantSettings = TenantSettingsResourceIT.createEntity(em);
        } else {
            tenantSettings = TestUtil.findAll(em, TenantSettings.class).get(0);
        }
        em.persist(tenantSettings);
        em.flush();
        tenantOrganization.setTenantSettings(tenantSettings);
        tenantSettings.setTenantOrganization(tenantOrganization);
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);
        Long tenantSettingsId = tenantSettings.getId();
        // Get all the tenantOrganizationList where tenantSettings equals to tenantSettingsId
        defaultTenantOrganizationShouldBeFound("tenantSettingsId.equals=" + tenantSettingsId);

        // Get all the tenantOrganizationList where tenantSettings equals to (tenantSettingsId + 1)
        defaultTenantOrganizationShouldNotBeFound("tenantSettingsId.equals=" + (tenantSettingsId + 1));
    }*//*

    *//**
     * Executes the search, and checks that the default entity is returned.
     *//*
    private void defaultTenantOrganizationShouldBeFound(String filter) throws Exception {
        restTenantOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantOrganization.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN)))
            .andExpect(jsonPath("$.[*].primaryColor").value(hasItem(DEFAULT_PRIMARY_COLOR)))
            .andExpect(jsonPath("$.[*].secondaryColor").value(hasItem(DEFAULT_SECONDARY_COLOR)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactPhone").value(hasItem(DEFAULT_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].subscriptionPlan").value(hasItem(DEFAULT_SUBSCRIPTION_PLAN)))
            .andExpect(jsonPath("$.[*].subscriptionStatus").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS)))
            .andExpect(jsonPath("$.[*].subscriptionStartDate").value(hasItem(DEFAULT_SUBSCRIPTION_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionEndDate").value(hasItem(DEFAULT_SUBSCRIPTION_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].monthlyFeeUsd").value(hasItem(sameNumber(DEFAULT_MONTHLY_FEE_USD))))
            .andExpect(jsonPath("$.[*].stripeCustomerId").value(hasItem(DEFAULT_STRIPE_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restTenantOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    *//**
     * Executes the search, and checks that the default entity is not returned.
     *//*
    private void defaultTenantOrganizationShouldNotBeFound(String filter) throws Exception {
        restTenantOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenantOrganization() throws Exception {
        // Get the tenantOrganization
        restTenantOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenantOrganization() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();

        // Update the tenantOrganization
        TenantOrganization updatedTenantOrganization = tenantOrganizationRepository.findById(tenantOrganization.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenantOrganization are not directly saved in db
        em.detach(updatedTenantOrganization);
        updatedTenantOrganization
            .tenantId(UPDATED_TENANT_ID)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .domain(UPDATED_DOMAIN)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .logoUrl(UPDATED_LOGO_URL)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .subscriptionPlan(UPDATED_SUBSCRIPTION_PLAN)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionEndDate(UPDATED_SUBSCRIPTION_END_DATE)
            .monthlyFeeUsd(UPDATED_MONTHLY_FEE_USD)
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(updatedTenantOrganization);

        restTenantOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
        TenantOrganization testTenantOrganization = tenantOrganizationList.get(tenantOrganizationList.size() - 1);
        assertThat(testTenantOrganization.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantOrganization.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testTenantOrganization.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testTenantOrganization.getPrimaryColor()).isEqualTo(UPDATED_PRIMARY_COLOR);
        assertThat(testTenantOrganization.getSecondaryColor()).isEqualTo(UPDATED_SECONDARY_COLOR);
        assertThat(testTenantOrganization.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testTenantOrganization.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testTenantOrganization.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testTenantOrganization.getSubscriptionPlan()).isEqualTo(UPDATED_SUBSCRIPTION_PLAN);
        assertThat(testTenantOrganization.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testTenantOrganization.getSubscriptionStartDate()).isEqualTo(UPDATED_SUBSCRIPTION_START_DATE);
        assertThat(testTenantOrganization.getSubscriptionEndDate()).isEqualTo(UPDATED_SUBSCRIPTION_END_DATE);
        assertThat(testTenantOrganization.getMonthlyFeeUsd()).isEqualByComparingTo(UPDATED_MONTHLY_FEE_USD);
        assertThat(testTenantOrganization.getStripeCustomerId()).isEqualTo(UPDATED_STRIPE_CUSTOMER_ID);
        assertThat(testTenantOrganization.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTenantOrganization.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenantOrganization.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTenantOrganization() throws Exception {
        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();
        tenantOrganization.setId(longCount.incrementAndGet());

        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantOrganizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantOrganization() throws Exception {
        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();
        tenantOrganization.setId(longCount.incrementAndGet());

        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantOrganization() throws Exception {
        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();
        tenantOrganization.setId(longCount.incrementAndGet());

        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantOrganizationWithPatch() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();

        // Update the tenantOrganization using partial update
        TenantOrganization partialUpdatedTenantOrganization = new TenantOrganization();
        partialUpdatedTenantOrganization.setId(tenantOrganization.getId());

        partialUpdatedTenantOrganization
            .tenantId(UPDATED_TENANT_ID)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .domain(UPDATED_DOMAIN)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .logoUrl(UPDATED_LOGO_URL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .subscriptionPlan(UPDATED_SUBSCRIPTION_PLAN)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionEndDate(UPDATED_SUBSCRIPTION_END_DATE)
            .monthlyFeeUsd(UPDATED_MONTHLY_FEE_USD)
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT);

        restTenantOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantOrganization))
            )
            .andExpect(status().isOk());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
        TenantOrganization testTenantOrganization = tenantOrganizationList.get(tenantOrganizationList.size() - 1);
        assertThat(testTenantOrganization.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantOrganization.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testTenantOrganization.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testTenantOrganization.getPrimaryColor()).isEqualTo(DEFAULT_PRIMARY_COLOR);
        assertThat(testTenantOrganization.getSecondaryColor()).isEqualTo(UPDATED_SECONDARY_COLOR);
        assertThat(testTenantOrganization.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testTenantOrganization.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testTenantOrganization.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testTenantOrganization.getSubscriptionPlan()).isEqualTo(UPDATED_SUBSCRIPTION_PLAN);
        assertThat(testTenantOrganization.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testTenantOrganization.getSubscriptionStartDate()).isEqualTo(UPDATED_SUBSCRIPTION_START_DATE);
        assertThat(testTenantOrganization.getSubscriptionEndDate()).isEqualTo(UPDATED_SUBSCRIPTION_END_DATE);
        assertThat(testTenantOrganization.getMonthlyFeeUsd()).isEqualByComparingTo(UPDATED_MONTHLY_FEE_USD);
        assertThat(testTenantOrganization.getStripeCustomerId()).isEqualTo(UPDATED_STRIPE_CUSTOMER_ID);
        assertThat(testTenantOrganization.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTenantOrganization.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenantOrganization.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTenantOrganizationWithPatch() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();

        // Update the tenantOrganization using partial update
        TenantOrganization partialUpdatedTenantOrganization = new TenantOrganization();
        partialUpdatedTenantOrganization.setId(tenantOrganization.getId());

        partialUpdatedTenantOrganization
            .tenantId(UPDATED_TENANT_ID)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .domain(UPDATED_DOMAIN)
            .primaryColor(UPDATED_PRIMARY_COLOR)
            .secondaryColor(UPDATED_SECONDARY_COLOR)
            .logoUrl(UPDATED_LOGO_URL)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .contactPhone(UPDATED_CONTACT_PHONE)
            .subscriptionPlan(UPDATED_SUBSCRIPTION_PLAN)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .subscriptionStartDate(UPDATED_SUBSCRIPTION_START_DATE)
            .subscriptionEndDate(UPDATED_SUBSCRIPTION_END_DATE)
            .monthlyFeeUsd(UPDATED_MONTHLY_FEE_USD)
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTenantOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantOrganization))
            )
            .andExpect(status().isOk());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
        TenantOrganization testTenantOrganization = tenantOrganizationList.get(tenantOrganizationList.size() - 1);
        assertThat(testTenantOrganization.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantOrganization.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testTenantOrganization.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testTenantOrganization.getPrimaryColor()).isEqualTo(UPDATED_PRIMARY_COLOR);
        assertThat(testTenantOrganization.getSecondaryColor()).isEqualTo(UPDATED_SECONDARY_COLOR);
        assertThat(testTenantOrganization.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testTenantOrganization.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testTenantOrganization.getContactPhone()).isEqualTo(UPDATED_CONTACT_PHONE);
        assertThat(testTenantOrganization.getSubscriptionPlan()).isEqualTo(UPDATED_SUBSCRIPTION_PLAN);
        assertThat(testTenantOrganization.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testTenantOrganization.getSubscriptionStartDate()).isEqualTo(UPDATED_SUBSCRIPTION_START_DATE);
        assertThat(testTenantOrganization.getSubscriptionEndDate()).isEqualTo(UPDATED_SUBSCRIPTION_END_DATE);
        assertThat(testTenantOrganization.getMonthlyFeeUsd()).isEqualByComparingTo(UPDATED_MONTHLY_FEE_USD);
        assertThat(testTenantOrganization.getStripeCustomerId()).isEqualTo(UPDATED_STRIPE_CUSTOMER_ID);
        assertThat(testTenantOrganization.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testTenantOrganization.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenantOrganization.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTenantOrganization() throws Exception {
        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();
        tenantOrganization.setId(longCount.incrementAndGet());

        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantOrganizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantOrganization() throws Exception {
        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();
        tenantOrganization.setId(longCount.incrementAndGet());

        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantOrganization() throws Exception {
        int databaseSizeBeforeUpdate = tenantOrganizationRepository.findAll().size();
        tenantOrganization.setId(longCount.incrementAndGet());

        // Create the TenantOrganization
        TenantOrganizationDTO tenantOrganizationDTO = tenantOrganizationMapper.toDto(tenantOrganization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantOrganizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantOrganization in the database
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantOrganization() throws Exception {
        // Initialize the database
        tenantOrganizationRepository.saveAndFlush(tenantOrganization);

        int databaseSizeBeforeDelete = tenantOrganizationRepository.findAll().size();

        // Delete the tenantOrganization
        restTenantOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantOrganization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TenantOrganization> tenantOrganizationList = tenantOrganizationRepository.findAll();
        assertThat(tenantOrganizationList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
