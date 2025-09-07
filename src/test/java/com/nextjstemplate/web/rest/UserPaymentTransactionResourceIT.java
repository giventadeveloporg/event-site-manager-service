package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.domain.UserPaymentTransaction;
import com.nextjstemplate.repository.UserPaymentTransactionRepository;
import com.nextjstemplate.service.dto.UserPaymentTransactionDTO;
import com.nextjstemplate.service.mapper.UserPaymentTransactionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link UserPaymentTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserPaymentTransactionResourceIT {/*

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final String DEFAULT_STRIPE_PAYMENT_INTENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STRIPE_PAYMENT_INTENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STRIPE_TRANSFER_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_STRIPE_TRANSFER_GROUP = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PLATFORM_FEE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLATFORM_FEE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PLATFORM_FEE_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TENANT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TENANT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TENANT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/user-payment-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserPaymentTransactionRepository userPaymentTransactionRepository;

    @Autowired
    private UserPaymentTransactionMapper userPaymentTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPaymentTransactionMockMvc;

    private UserPaymentTransaction userPaymentTransaction;

    */
    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static UserPaymentTransaction createEntity(EntityManager em) {
        UserPaymentTransaction userPaymentTransaction = new UserPaymentTransaction()
            .tenantId(DEFAULT_TENANT_ID)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .stripePaymentIntentId(DEFAULT_STRIPE_PAYMENT_INTENT_ID)
            .stripeTransferGroup(DEFAULT_STRIPE_TRANSFER_GROUP)
            .platformFeeAmount(DEFAULT_PLATFORM_FEE_AMOUNT)
            .tenantAmount(DEFAULT_TENANT_AMOUNT)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return userPaymentTransaction;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static UserPaymentTransaction createUpdatedEntity(EntityManager em) {
        UserPaymentTransaction userPaymentTransaction = new UserPaymentTransaction()
            .tenantId(UPDATED_TENANT_ID)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .stripePaymentIntentId(UPDATED_STRIPE_PAYMENT_INTENT_ID)
            .stripeTransferGroup(UPDATED_STRIPE_TRANSFER_GROUP)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .tenantAmount(UPDATED_TENANT_AMOUNT)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return userPaymentTransaction;
    }

    @BeforeEach
    public void initTest() {
        userPaymentTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeCreate = userPaymentTransactionRepository.findAll().size();
        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);
        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        UserPaymentTransaction testUserPaymentTransaction = userPaymentTransactionList.get(userPaymentTransactionList.size() - 1);
        assertThat(testUserPaymentTransaction.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testUserPaymentTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testUserPaymentTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testUserPaymentTransaction.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testUserPaymentTransaction.getStripePaymentIntentId()).isEqualTo(DEFAULT_STRIPE_PAYMENT_INTENT_ID);
        assertThat(testUserPaymentTransaction.getStripeTransferGroup()).isEqualTo(DEFAULT_STRIPE_TRANSFER_GROUP);
        assertThat(testUserPaymentTransaction.getPlatformFeeAmount()).isEqualByComparingTo(DEFAULT_PLATFORM_FEE_AMOUNT);
        assertThat(testUserPaymentTransaction.getTenantAmount()).isEqualByComparingTo(DEFAULT_TENANT_AMOUNT);
        assertThat(testUserPaymentTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserPaymentTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserPaymentTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUserPaymentTransactionWithExistingId() throws Exception {
        // Create the UserPaymentTransaction with an existing ID
        userPaymentTransaction.setId(1L);
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        int databaseSizeBeforeCreate = userPaymentTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenantIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setTenantId(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setTransactionType(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setAmount(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setCurrency(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setStatus(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setCreatedAt(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPaymentTransactionRepository.findAll().size();
        // set the field null
        userPaymentTransaction.setUpdatedAt(null);

        // Create the UserPaymentTransaction, which fails.
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactions() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList
        restUserPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPaymentTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].stripePaymentIntentId").value(hasItem(DEFAULT_STRIPE_PAYMENT_INTENT_ID)))
            .andExpect(jsonPath("$.[*].stripeTransferGroup").value(hasItem(DEFAULT_STRIPE_TRANSFER_GROUP)))
            .andExpect(jsonPath("$.[*].platformFeeAmount").value(hasItem(sameNumber(DEFAULT_PLATFORM_FEE_AMOUNT))))
            .andExpect(jsonPath("$.[*].tenantAmount").value(hasItem(sameNumber(DEFAULT_TENANT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getUserPaymentTransaction() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get the userPaymentTransaction
        restUserPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, userPaymentTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPaymentTransaction.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.stripePaymentIntentId").value(DEFAULT_STRIPE_PAYMENT_INTENT_ID))
            .andExpect(jsonPath("$.stripeTransferGroup").value(DEFAULT_STRIPE_TRANSFER_GROUP))
            .andExpect(jsonPath("$.platformFeeAmount").value(sameNumber(DEFAULT_PLATFORM_FEE_AMOUNT)))
            .andExpect(jsonPath("$.tenantAmount").value(sameNumber(DEFAULT_TENANT_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getUserPaymentTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        Long id = userPaymentTransaction.getId();

        defaultUserPaymentTransactionShouldBeFound("id.equals=" + id);
        defaultUserPaymentTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultUserPaymentTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserPaymentTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultUserPaymentTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserPaymentTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantId equals to DEFAULT_TENANT_ID
        defaultUserPaymentTransactionShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the userPaymentTransactionList where tenantId equals to UPDATED_TENANT_ID
        defaultUserPaymentTransactionShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultUserPaymentTransactionShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the userPaymentTransactionList where tenantId equals to UPDATED_TENANT_ID
        defaultUserPaymentTransactionShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantId is not null
        defaultUserPaymentTransactionShouldBeFound("tenantId.specified=true");

        // Get all the userPaymentTransactionList where tenantId is null
        defaultUserPaymentTransactionShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantId contains DEFAULT_TENANT_ID
        defaultUserPaymentTransactionShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the userPaymentTransactionList where tenantId contains UPDATED_TENANT_ID
        defaultUserPaymentTransactionShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantId does not contain DEFAULT_TENANT_ID
        defaultUserPaymentTransactionShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the userPaymentTransactionList where tenantId does not contain UPDATED_TENANT_ID
        defaultUserPaymentTransactionShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the userPaymentTransactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the userPaymentTransactionList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where transactionType is not null
        defaultUserPaymentTransactionShouldBeFound("transactionType.specified=true");

        // Get all the userPaymentTransactionList where transactionType is null
        defaultUserPaymentTransactionShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTransactionTypeContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where transactionType contains DEFAULT_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldBeFound("transactionType.contains=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the userPaymentTransactionList where transactionType contains UPDATED_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldNotBeFound("transactionType.contains=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTransactionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where transactionType does not contain DEFAULT_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldNotBeFound("transactionType.doesNotContain=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the userPaymentTransactionList where transactionType does not contain UPDATED_TRANSACTION_TYPE
        defaultUserPaymentTransactionShouldBeFound("transactionType.doesNotContain=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount equals to DEFAULT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the userPaymentTransactionList where amount equals to UPDATED_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the userPaymentTransactionList where amount equals to UPDATED_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount is not null
        defaultUserPaymentTransactionShouldBeFound("amount.specified=true");

        // Get all the userPaymentTransactionList where amount is null
        defaultUserPaymentTransactionShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the userPaymentTransactionList where amount is greater than or equal to UPDATED_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount is less than or equal to DEFAULT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the userPaymentTransactionList where amount is less than or equal to SMALLER_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount is less than DEFAULT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the userPaymentTransactionList where amount is less than UPDATED_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where amount is greater than DEFAULT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the userPaymentTransactionList where amount is greater than SMALLER_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where currency equals to DEFAULT_CURRENCY
        defaultUserPaymentTransactionShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the userPaymentTransactionList where currency equals to UPDATED_CURRENCY
        defaultUserPaymentTransactionShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultUserPaymentTransactionShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the userPaymentTransactionList where currency equals to UPDATED_CURRENCY
        defaultUserPaymentTransactionShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where currency is not null
        defaultUserPaymentTransactionShouldBeFound("currency.specified=true");

        // Get all the userPaymentTransactionList where currency is null
        defaultUserPaymentTransactionShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where currency contains DEFAULT_CURRENCY
        defaultUserPaymentTransactionShouldBeFound("currency.contains=" + DEFAULT_CURRENCY);

        // Get all the userPaymentTransactionList where currency contains UPDATED_CURRENCY
        defaultUserPaymentTransactionShouldNotBeFound("currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where currency does not contain DEFAULT_CURRENCY
        defaultUserPaymentTransactionShouldNotBeFound("currency.doesNotContain=" + DEFAULT_CURRENCY);

        // Get all the userPaymentTransactionList where currency does not contain UPDATED_CURRENCY
        defaultUserPaymentTransactionShouldBeFound("currency.doesNotContain=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripePaymentIntentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripePaymentIntentId equals to DEFAULT_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldBeFound("stripePaymentIntentId.equals=" + DEFAULT_STRIPE_PAYMENT_INTENT_ID);

        // Get all the userPaymentTransactionList where stripePaymentIntentId equals to UPDATED_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldNotBeFound("stripePaymentIntentId.equals=" + UPDATED_STRIPE_PAYMENT_INTENT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripePaymentIntentIdIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripePaymentIntentId in DEFAULT_STRIPE_PAYMENT_INTENT_ID or UPDATED_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldBeFound(
            "stripePaymentIntentId.in=" + DEFAULT_STRIPE_PAYMENT_INTENT_ID + "," + UPDATED_STRIPE_PAYMENT_INTENT_ID
        );

        // Get all the userPaymentTransactionList where stripePaymentIntentId equals to UPDATED_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldNotBeFound("stripePaymentIntentId.in=" + UPDATED_STRIPE_PAYMENT_INTENT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripePaymentIntentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripePaymentIntentId is not null
        defaultUserPaymentTransactionShouldBeFound("stripePaymentIntentId.specified=true");

        // Get all the userPaymentTransactionList where stripePaymentIntentId is null
        defaultUserPaymentTransactionShouldNotBeFound("stripePaymentIntentId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripePaymentIntentIdContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripePaymentIntentId contains DEFAULT_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldBeFound("stripePaymentIntentId.contains=" + DEFAULT_STRIPE_PAYMENT_INTENT_ID);

        // Get all the userPaymentTransactionList where stripePaymentIntentId contains UPDATED_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldNotBeFound("stripePaymentIntentId.contains=" + UPDATED_STRIPE_PAYMENT_INTENT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripePaymentIntentIdNotContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripePaymentIntentId does not contain DEFAULT_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldNotBeFound("stripePaymentIntentId.doesNotContain=" + DEFAULT_STRIPE_PAYMENT_INTENT_ID);

        // Get all the userPaymentTransactionList where stripePaymentIntentId does not contain UPDATED_STRIPE_PAYMENT_INTENT_ID
        defaultUserPaymentTransactionShouldBeFound("stripePaymentIntentId.doesNotContain=" + UPDATED_STRIPE_PAYMENT_INTENT_ID);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripeTransferGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripeTransferGroup equals to DEFAULT_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldBeFound("stripeTransferGroup.equals=" + DEFAULT_STRIPE_TRANSFER_GROUP);

        // Get all the userPaymentTransactionList where stripeTransferGroup equals to UPDATED_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldNotBeFound("stripeTransferGroup.equals=" + UPDATED_STRIPE_TRANSFER_GROUP);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripeTransferGroupIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripeTransferGroup in DEFAULT_STRIPE_TRANSFER_GROUP or UPDATED_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldBeFound(
            "stripeTransferGroup.in=" + DEFAULT_STRIPE_TRANSFER_GROUP + "," + UPDATED_STRIPE_TRANSFER_GROUP
        );

        // Get all the userPaymentTransactionList where stripeTransferGroup equals to UPDATED_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldNotBeFound("stripeTransferGroup.in=" + UPDATED_STRIPE_TRANSFER_GROUP);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripeTransferGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripeTransferGroup is not null
        defaultUserPaymentTransactionShouldBeFound("stripeTransferGroup.specified=true");

        // Get all the userPaymentTransactionList where stripeTransferGroup is null
        defaultUserPaymentTransactionShouldNotBeFound("stripeTransferGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripeTransferGroupContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripeTransferGroup contains DEFAULT_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldBeFound("stripeTransferGroup.contains=" + DEFAULT_STRIPE_TRANSFER_GROUP);

        // Get all the userPaymentTransactionList where stripeTransferGroup contains UPDATED_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldNotBeFound("stripeTransferGroup.contains=" + UPDATED_STRIPE_TRANSFER_GROUP);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStripeTransferGroupNotContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where stripeTransferGroup does not contain DEFAULT_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldNotBeFound("stripeTransferGroup.doesNotContain=" + DEFAULT_STRIPE_TRANSFER_GROUP);

        // Get all the userPaymentTransactionList where stripeTransferGroup does not contain UPDATED_STRIPE_TRANSFER_GROUP
        defaultUserPaymentTransactionShouldBeFound("stripeTransferGroup.doesNotContain=" + UPDATED_STRIPE_TRANSFER_GROUP);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount equals to DEFAULT_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("platformFeeAmount.equals=" + DEFAULT_PLATFORM_FEE_AMOUNT);

        // Get all the userPaymentTransactionList where platformFeeAmount equals to UPDATED_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.equals=" + UPDATED_PLATFORM_FEE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount in DEFAULT_PLATFORM_FEE_AMOUNT or UPDATED_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldBeFound(
            "platformFeeAmount.in=" + DEFAULT_PLATFORM_FEE_AMOUNT + "," + UPDATED_PLATFORM_FEE_AMOUNT
        );

        // Get all the userPaymentTransactionList where platformFeeAmount equals to UPDATED_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.in=" + UPDATED_PLATFORM_FEE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount is not null
        defaultUserPaymentTransactionShouldBeFound("platformFeeAmount.specified=true");

        // Get all the userPaymentTransactionList where platformFeeAmount is null
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount is greater than or equal to DEFAULT_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("platformFeeAmount.greaterThanOrEqual=" + DEFAULT_PLATFORM_FEE_AMOUNT);

        // Get all the userPaymentTransactionList where platformFeeAmount is greater than or equal to UPDATED_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.greaterThanOrEqual=" + UPDATED_PLATFORM_FEE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount is less than or equal to DEFAULT_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("platformFeeAmount.lessThanOrEqual=" + DEFAULT_PLATFORM_FEE_AMOUNT);

        // Get all the userPaymentTransactionList where platformFeeAmount is less than or equal to SMALLER_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.lessThanOrEqual=" + SMALLER_PLATFORM_FEE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount is less than DEFAULT_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.lessThan=" + DEFAULT_PLATFORM_FEE_AMOUNT);

        // Get all the userPaymentTransactionList where platformFeeAmount is less than UPDATED_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("platformFeeAmount.lessThan=" + UPDATED_PLATFORM_FEE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByPlatformFeeAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where platformFeeAmount is greater than DEFAULT_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("platformFeeAmount.greaterThan=" + DEFAULT_PLATFORM_FEE_AMOUNT);

        // Get all the userPaymentTransactionList where platformFeeAmount is greater than SMALLER_PLATFORM_FEE_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("platformFeeAmount.greaterThan=" + SMALLER_PLATFORM_FEE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount equals to DEFAULT_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.equals=" + DEFAULT_TENANT_AMOUNT);

        // Get all the userPaymentTransactionList where tenantAmount equals to UPDATED_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.equals=" + UPDATED_TENANT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount in DEFAULT_TENANT_AMOUNT or UPDATED_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.in=" + DEFAULT_TENANT_AMOUNT + "," + UPDATED_TENANT_AMOUNT);

        // Get all the userPaymentTransactionList where tenantAmount equals to UPDATED_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.in=" + UPDATED_TENANT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount is not null
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.specified=true");

        // Get all the userPaymentTransactionList where tenantAmount is null
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount is greater than or equal to DEFAULT_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.greaterThanOrEqual=" + DEFAULT_TENANT_AMOUNT);

        // Get all the userPaymentTransactionList where tenantAmount is greater than or equal to UPDATED_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.greaterThanOrEqual=" + UPDATED_TENANT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount is less than or equal to DEFAULT_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.lessThanOrEqual=" + DEFAULT_TENANT_AMOUNT);

        // Get all the userPaymentTransactionList where tenantAmount is less than or equal to SMALLER_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.lessThanOrEqual=" + SMALLER_TENANT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount is less than DEFAULT_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.lessThan=" + DEFAULT_TENANT_AMOUNT);

        // Get all the userPaymentTransactionList where tenantAmount is less than UPDATED_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.lessThan=" + UPDATED_TENANT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTenantAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where tenantAmount is greater than DEFAULT_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldNotBeFound("tenantAmount.greaterThan=" + DEFAULT_TENANT_AMOUNT);

        // Get all the userPaymentTransactionList where tenantAmount is greater than SMALLER_TENANT_AMOUNT
        defaultUserPaymentTransactionShouldBeFound("tenantAmount.greaterThan=" + SMALLER_TENANT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where status equals to DEFAULT_STATUS
        defaultUserPaymentTransactionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the userPaymentTransactionList where status equals to UPDATED_STATUS
        defaultUserPaymentTransactionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUserPaymentTransactionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the userPaymentTransactionList where status equals to UPDATED_STATUS
        defaultUserPaymentTransactionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where status is not null
        defaultUserPaymentTransactionShouldBeFound("status.specified=true");

        // Get all the userPaymentTransactionList where status is null
        defaultUserPaymentTransactionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStatusContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where status contains DEFAULT_STATUS
        defaultUserPaymentTransactionShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the userPaymentTransactionList where status contains UPDATED_STATUS
        defaultUserPaymentTransactionShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where status does not contain DEFAULT_STATUS
        defaultUserPaymentTransactionShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the userPaymentTransactionList where status does not contain UPDATED_STATUS
        defaultUserPaymentTransactionShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserPaymentTransactionShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userPaymentTransactionList where createdAt equals to UPDATED_CREATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserPaymentTransactionShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userPaymentTransactionList where createdAt equals to UPDATED_CREATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt is not null
        defaultUserPaymentTransactionShouldBeFound("createdAt.specified=true");

        // Get all the userPaymentTransactionList where createdAt is null
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultUserPaymentTransactionShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userPaymentTransactionList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultUserPaymentTransactionShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userPaymentTransactionList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt is less than DEFAULT_CREATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the userPaymentTransactionList where createdAt is less than UPDATED_CREATED_AT
        defaultUserPaymentTransactionShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where createdAt is greater than DEFAULT_CREATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the userPaymentTransactionList where createdAt is greater than SMALLER_CREATED_AT
        defaultUserPaymentTransactionShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserPaymentTransactionShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userPaymentTransactionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserPaymentTransactionShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userPaymentTransactionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt is not null
        defaultUserPaymentTransactionShouldBeFound("updatedAt.specified=true");

        // Get all the userPaymentTransactionList where updatedAt is null
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultUserPaymentTransactionShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userPaymentTransactionList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultUserPaymentTransactionShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userPaymentTransactionList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the userPaymentTransactionList where updatedAt is less than UPDATED_UPDATED_AT
        defaultUserPaymentTransactionShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        // Get all the userPaymentTransactionList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultUserPaymentTransactionShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the userPaymentTransactionList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultUserPaymentTransactionShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        userPaymentTransaction.setEvent(event);
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);
        Long eventId = event.getId();
        // Get all the userPaymentTransactionList where event equals to eventId
        defaultUserPaymentTransactionShouldBeFound("eventId.equals=" + eventId);

        // Get all the userPaymentTransactionList where event equals to (eventId + 1)
        defaultUserPaymentTransactionShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllUserPaymentTransactionsByTicketTransactionIsEqualToSomething() throws Exception {
        EventTicketTransaction ticketTransaction;
        if (TestUtil.findAll(em, EventTicketTransaction.class).isEmpty()) {
            userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);
            ticketTransaction = EventTicketTransactionResourceIT.createEntity(em);
        } else {
            ticketTransaction = TestUtil.findAll(em, EventTicketTransaction.class).get(0);
        }
        em.persist(ticketTransaction);
        em.flush();
        userPaymentTransaction.setTicketTransaction(ticketTransaction);
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);
        Long ticketTransactionId = ticketTransaction.getId();
        // Get all the userPaymentTransactionList where ticketTransaction equals to ticketTransactionId
        defaultUserPaymentTransactionShouldBeFound("ticketTransactionId.equals=" + ticketTransactionId);

        // Get all the userPaymentTransactionList where ticketTransaction equals to (ticketTransactionId + 1)
        defaultUserPaymentTransactionShouldNotBeFound("ticketTransactionId.equals=" + (ticketTransactionId + 1));
    }

    *//**
     * Executes the search, and checks that the default entity is returned.
     *//*
    private void defaultUserPaymentTransactionShouldBeFound(String filter) throws Exception {
        restUserPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPaymentTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].stripePaymentIntentId").value(hasItem(DEFAULT_STRIPE_PAYMENT_INTENT_ID)))
            .andExpect(jsonPath("$.[*].stripeTransferGroup").value(hasItem(DEFAULT_STRIPE_TRANSFER_GROUP)))
            .andExpect(jsonPath("$.[*].platformFeeAmount").value(hasItem(sameNumber(DEFAULT_PLATFORM_FEE_AMOUNT))))
            .andExpect(jsonPath("$.[*].tenantAmount").value(hasItem(sameNumber(DEFAULT_TENANT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restUserPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    *//**
     * Executes the search, and checks that the default entity is not returned.
     *//*
    private void defaultUserPaymentTransactionShouldNotBeFound(String filter) throws Exception {
        restUserPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserPaymentTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserPaymentTransaction() throws Exception {
        // Get the userPaymentTransaction
        restUserPaymentTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserPaymentTransaction() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();

        // Update the userPaymentTransaction
        UserPaymentTransaction updatedUserPaymentTransaction = userPaymentTransactionRepository
            .findById(userPaymentTransaction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserPaymentTransaction are not directly saved in db
        em.detach(updatedUserPaymentTransaction);
        updatedUserPaymentTransaction
            .tenantId(UPDATED_TENANT_ID)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .stripePaymentIntentId(UPDATED_STRIPE_PAYMENT_INTENT_ID)
            .stripeTransferGroup(UPDATED_STRIPE_TRANSFER_GROUP)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .tenantAmount(UPDATED_TENANT_AMOUNT)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(updatedUserPaymentTransaction);

        restUserPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPaymentTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        UserPaymentTransaction testUserPaymentTransaction = userPaymentTransactionList.get(userPaymentTransactionList.size() - 1);
        assertThat(testUserPaymentTransaction.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testUserPaymentTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testUserPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testUserPaymentTransaction.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testUserPaymentTransaction.getStripePaymentIntentId()).isEqualTo(UPDATED_STRIPE_PAYMENT_INTENT_ID);
        assertThat(testUserPaymentTransaction.getStripeTransferGroup()).isEqualTo(UPDATED_STRIPE_TRANSFER_GROUP);
        assertThat(testUserPaymentTransaction.getPlatformFeeAmount()).isEqualByComparingTo(UPDATED_PLATFORM_FEE_AMOUNT);
        assertThat(testUserPaymentTransaction.getTenantAmount()).isEqualByComparingTo(UPDATED_TENANT_AMOUNT);
        assertThat(testUserPaymentTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserPaymentTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserPaymentTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();
        userPaymentTransaction.setId(longCount.incrementAndGet());

        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPaymentTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();
        userPaymentTransaction.setId(longCount.incrementAndGet());

        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();
        userPaymentTransaction.setId(longCount.incrementAndGet());

        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPaymentTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPaymentTransactionWithPatch() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();

        // Update the userPaymentTransaction using partial update
        UserPaymentTransaction partialUpdatedUserPaymentTransaction = new UserPaymentTransaction();
        partialUpdatedUserPaymentTransaction.setId(userPaymentTransaction.getId());

        partialUpdatedUserPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .stripeTransferGroup(UPDATED_STRIPE_TRANSFER_GROUP)
            .tenantAmount(UPDATED_TENANT_AMOUNT)
            .status(UPDATED_STATUS);

        restUserPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPaymentTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPaymentTransaction))
            )
            .andExpect(status().isOk());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        UserPaymentTransaction testUserPaymentTransaction = userPaymentTransactionList.get(userPaymentTransactionList.size() - 1);
        assertThat(testUserPaymentTransaction.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testUserPaymentTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testUserPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testUserPaymentTransaction.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testUserPaymentTransaction.getStripePaymentIntentId()).isEqualTo(DEFAULT_STRIPE_PAYMENT_INTENT_ID);
        assertThat(testUserPaymentTransaction.getStripeTransferGroup()).isEqualTo(UPDATED_STRIPE_TRANSFER_GROUP);
        assertThat(testUserPaymentTransaction.getPlatformFeeAmount()).isEqualByComparingTo(DEFAULT_PLATFORM_FEE_AMOUNT);
        assertThat(testUserPaymentTransaction.getTenantAmount()).isEqualByComparingTo(UPDATED_TENANT_AMOUNT);
        assertThat(testUserPaymentTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserPaymentTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserPaymentTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserPaymentTransactionWithPatch() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();

        // Update the userPaymentTransaction using partial update
        UserPaymentTransaction partialUpdatedUserPaymentTransaction = new UserPaymentTransaction();
        partialUpdatedUserPaymentTransaction.setId(userPaymentTransaction.getId());

        partialUpdatedUserPaymentTransaction
            .tenantId(UPDATED_TENANT_ID)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .stripePaymentIntentId(UPDATED_STRIPE_PAYMENT_INTENT_ID)
            .stripeTransferGroup(UPDATED_STRIPE_TRANSFER_GROUP)
            .platformFeeAmount(UPDATED_PLATFORM_FEE_AMOUNT)
            .tenantAmount(UPDATED_TENANT_AMOUNT)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPaymentTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserPaymentTransaction))
            )
            .andExpect(status().isOk());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        UserPaymentTransaction testUserPaymentTransaction = userPaymentTransactionList.get(userPaymentTransactionList.size() - 1);
        assertThat(testUserPaymentTransaction.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testUserPaymentTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testUserPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testUserPaymentTransaction.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testUserPaymentTransaction.getStripePaymentIntentId()).isEqualTo(UPDATED_STRIPE_PAYMENT_INTENT_ID);
        assertThat(testUserPaymentTransaction.getStripeTransferGroup()).isEqualTo(UPDATED_STRIPE_TRANSFER_GROUP);
        assertThat(testUserPaymentTransaction.getPlatformFeeAmount()).isEqualByComparingTo(UPDATED_PLATFORM_FEE_AMOUNT);
        assertThat(testUserPaymentTransaction.getTenantAmount()).isEqualByComparingTo(UPDATED_TENANT_AMOUNT);
        assertThat(testUserPaymentTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserPaymentTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserPaymentTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();
        userPaymentTransaction.setId(longCount.incrementAndGet());

        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPaymentTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();
        userPaymentTransaction.setId(longCount.incrementAndGet());

        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = userPaymentTransactionRepository.findAll().size();
        userPaymentTransaction.setId(longCount.incrementAndGet());

        // Create the UserPaymentTransaction
        UserPaymentTransactionDTO userPaymentTransactionDTO = userPaymentTransactionMapper.toDto(userPaymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPaymentTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userPaymentTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPaymentTransaction in the database
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPaymentTransaction() throws Exception {
        // Initialize the database
        userPaymentTransactionRepository.saveAndFlush(userPaymentTransaction);

        int databaseSizeBeforeDelete = userPaymentTransactionRepository.findAll().size();

        // Delete the userPaymentTransaction
        restUserPaymentTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPaymentTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserPaymentTransaction> userPaymentTransactionList = userPaymentTransactionRepository.findAll();
        assertThat(userPaymentTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
