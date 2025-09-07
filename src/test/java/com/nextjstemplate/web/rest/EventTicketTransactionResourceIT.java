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
import com.nextjstemplate.domain.EventTicketType;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
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
 * Integration tests for the {@link EventTicketTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventTicketTransactionResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE_PER_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_UNIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE_PER_UNIT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PURCHASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PURCHASE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PURCHASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_DISCOUNT_CODE_ID = 1L;
    private static final Long UPDATED_DISCOUNT_CODE_ID = 2L;
    private static final Long SMALLER_DISCOUNT_CODE_ID = 1L - 1L;

    private static final BigDecimal DEFAULT_DISCOUNT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_AMOUNT = new BigDecimal(1 - 1);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-ticket-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventTicketTransactionRepository eventTicketTransactionRepository;

    @Autowired
    private EventTicketTransactionMapper eventTicketTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTicketTransactionMockMvc;

    private EventTicketTransaction eventTicketTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTicketTransaction createEntity(EntityManager em) {
        EventTicketTransaction eventTicketTransaction = new EventTicketTransaction()
            .tenantId(DEFAULT_TENANT_ID)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .quantity(DEFAULT_QUANTITY)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .status(DEFAULT_STATUS)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .discountCodeId(DEFAULT_DISCOUNT_CODE_ID)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventTicketTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTicketTransaction createUpdatedEntity(EntityManager em) {
        EventTicketTransaction eventTicketTransaction = new EventTicketTransaction()
            .tenantId(UPDATED_TENANT_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .discountCodeId(UPDATED_DISCOUNT_CODE_ID)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventTicketTransaction;
    }

    @BeforeEach
    public void initTest() {
        eventTicketTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createEventTicketTransaction() throws Exception {
        int databaseSizeBeforeCreate = eventTicketTransactionRepository.findAll().size();
        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);
        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        EventTicketTransaction testEventTicketTransaction = eventTicketTransactionList.get(eventTicketTransactionList.size() - 1);
        assertThat(testEventTicketTransaction.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventTicketTransaction.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEventTicketTransaction.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEventTicketTransaction.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEventTicketTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testEventTicketTransaction.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testEventTicketTransaction.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testEventTicketTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventTicketTransaction.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testEventTicketTransaction.getDiscountCodeId()).isEqualTo(DEFAULT_DISCOUNT_CODE_ID);
        assertThat(testEventTicketTransaction.getDiscountAmount()).isEqualByComparingTo(DEFAULT_DISCOUNT_AMOUNT);
        assertThat(testEventTicketTransaction.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventTicketTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventTicketTransactionWithExistingId() throws Exception {
        // Create the EventTicketTransaction with an existing ID
        eventTicketTransaction.setId(1L);
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        int databaseSizeBeforeCreate = eventTicketTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setEmail(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setQuantity(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setPricePerUnit(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setTotalAmount(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setStatus(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPurchaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setPurchaseDate(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setCreatedAt(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTransactionRepository.findAll().size();
        // set the field null
        eventTicketTransaction.setUpdatedAt(null);

        // Create the EventTicketTransaction, which fails.
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactions() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList
        restEventTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTicketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(sameInstant(DEFAULT_PURCHASE_DATE))))
            .andExpect(jsonPath("$.[*].discountCodeId").value(hasItem(DEFAULT_DISCOUNT_CODE_ID.intValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventTicketTransaction() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get the eventTicketTransaction
        restEventTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, eventTicketTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventTicketTransaction.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.pricePerUnit").value(sameNumber(DEFAULT_PRICE_PER_UNIT)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.purchaseDate").value(sameInstant(DEFAULT_PURCHASE_DATE)))
            .andExpect(jsonPath("$.discountCodeId").value(DEFAULT_DISCOUNT_CODE_ID.intValue()))
            .andExpect(jsonPath("$.discountAmount").value(sameNumber(DEFAULT_DISCOUNT_AMOUNT)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventTicketTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        Long id = eventTicketTransaction.getId();

        defaultEventTicketTransactionShouldBeFound("id.equals=" + id);
        defaultEventTicketTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultEventTicketTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventTicketTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultEventTicketTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventTicketTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventTicketTransactionShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventTicketTransactionList where tenantId equals to UPDATED_TENANT_ID
        defaultEventTicketTransactionShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventTicketTransactionShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventTicketTransactionList where tenantId equals to UPDATED_TENANT_ID
        defaultEventTicketTransactionShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where tenantId is not null
        defaultEventTicketTransactionShouldBeFound("tenantId.specified=true");

        // Get all the eventTicketTransactionList where tenantId is null
        defaultEventTicketTransactionShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where tenantId contains DEFAULT_TENANT_ID
        defaultEventTicketTransactionShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventTicketTransactionList where tenantId contains UPDATED_TENANT_ID
        defaultEventTicketTransactionShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventTicketTransactionShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventTicketTransactionList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventTicketTransactionShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where email equals to DEFAULT_EMAIL
        defaultEventTicketTransactionShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the eventTicketTransactionList where email equals to UPDATED_EMAIL
        defaultEventTicketTransactionShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEventTicketTransactionShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the eventTicketTransactionList where email equals to UPDATED_EMAIL
        defaultEventTicketTransactionShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where email is not null
        defaultEventTicketTransactionShouldBeFound("email.specified=true");

        // Get all the eventTicketTransactionList where email is null
        defaultEventTicketTransactionShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByEmailContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where email contains DEFAULT_EMAIL
        defaultEventTicketTransactionShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the eventTicketTransactionList where email contains UPDATED_EMAIL
        defaultEventTicketTransactionShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where email does not contain DEFAULT_EMAIL
        defaultEventTicketTransactionShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the eventTicketTransactionList where email does not contain UPDATED_EMAIL
        defaultEventTicketTransactionShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where firstName equals to DEFAULT_FIRST_NAME
        defaultEventTicketTransactionShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the eventTicketTransactionList where firstName equals to UPDATED_FIRST_NAME
        defaultEventTicketTransactionShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEventTicketTransactionShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the eventTicketTransactionList where firstName equals to UPDATED_FIRST_NAME
        defaultEventTicketTransactionShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where firstName is not null
        defaultEventTicketTransactionShouldBeFound("firstName.specified=true");

        // Get all the eventTicketTransactionList where firstName is null
        defaultEventTicketTransactionShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where firstName contains DEFAULT_FIRST_NAME
        defaultEventTicketTransactionShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the eventTicketTransactionList where firstName contains UPDATED_FIRST_NAME
        defaultEventTicketTransactionShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEventTicketTransactionShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the eventTicketTransactionList where firstName does not contain UPDATED_FIRST_NAME
        defaultEventTicketTransactionShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where lastName equals to DEFAULT_LAST_NAME
        defaultEventTicketTransactionShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the eventTicketTransactionList where lastName equals to UPDATED_LAST_NAME
        defaultEventTicketTransactionShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEventTicketTransactionShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the eventTicketTransactionList where lastName equals to UPDATED_LAST_NAME
        defaultEventTicketTransactionShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where lastName is not null
        defaultEventTicketTransactionShouldBeFound("lastName.specified=true");

        // Get all the eventTicketTransactionList where lastName is null
        defaultEventTicketTransactionShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where lastName contains DEFAULT_LAST_NAME
        defaultEventTicketTransactionShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the eventTicketTransactionList where lastName contains UPDATED_LAST_NAME
        defaultEventTicketTransactionShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where lastName does not contain DEFAULT_LAST_NAME
        defaultEventTicketTransactionShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the eventTicketTransactionList where lastName does not contain UPDATED_LAST_NAME
        defaultEventTicketTransactionShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity equals to DEFAULT_QUANTITY
        defaultEventTicketTransactionShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the eventTicketTransactionList where quantity equals to UPDATED_QUANTITY
        defaultEventTicketTransactionShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultEventTicketTransactionShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the eventTicketTransactionList where quantity equals to UPDATED_QUANTITY
        defaultEventTicketTransactionShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity is not null
        defaultEventTicketTransactionShouldBeFound("quantity.specified=true");

        // Get all the eventTicketTransactionList where quantity is null
        defaultEventTicketTransactionShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultEventTicketTransactionShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the eventTicketTransactionList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultEventTicketTransactionShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultEventTicketTransactionShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the eventTicketTransactionList where quantity is less than or equal to SMALLER_QUANTITY
        defaultEventTicketTransactionShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity is less than DEFAULT_QUANTITY
        defaultEventTicketTransactionShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the eventTicketTransactionList where quantity is less than UPDATED_QUANTITY
        defaultEventTicketTransactionShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where quantity is greater than DEFAULT_QUANTITY
        defaultEventTicketTransactionShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the eventTicketTransactionList where quantity is greater than SMALLER_QUANTITY
        defaultEventTicketTransactionShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit equals to DEFAULT_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the eventTicketTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit in DEFAULT_PRICE_PER_UNIT or UPDATED_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT);

        // Get all the eventTicketTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit is not null
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.specified=true");

        // Get all the eventTicketTransactionList where pricePerUnit is null
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit is greater than or equal to DEFAULT_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the eventTicketTransactionList where pricePerUnit is greater than or equal to UPDATED_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit is less than or equal to DEFAULT_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the eventTicketTransactionList where pricePerUnit is less than or equal to SMALLER_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit is less than DEFAULT_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the eventTicketTransactionList where pricePerUnit is less than UPDATED_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where pricePerUnit is greater than DEFAULT_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldNotBeFound("pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the eventTicketTransactionList where pricePerUnit is greater than SMALLER_PRICE_PER_UNIT
        defaultEventTicketTransactionShouldBeFound("pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the eventTicketTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the eventTicketTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount is not null
        defaultEventTicketTransactionShouldBeFound("totalAmount.specified=true");

        // Get all the eventTicketTransactionList where totalAmount is null
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the eventTicketTransactionList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the eventTicketTransactionList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the eventTicketTransactionList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the eventTicketTransactionList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultEventTicketTransactionShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where status equals to DEFAULT_STATUS
        defaultEventTicketTransactionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the eventTicketTransactionList where status equals to UPDATED_STATUS
        defaultEventTicketTransactionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEventTicketTransactionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the eventTicketTransactionList where status equals to UPDATED_STATUS
        defaultEventTicketTransactionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where status is not null
        defaultEventTicketTransactionShouldBeFound("status.specified=true");

        // Get all the eventTicketTransactionList where status is null
        defaultEventTicketTransactionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByStatusContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where status contains DEFAULT_STATUS
        defaultEventTicketTransactionShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the eventTicketTransactionList where status contains UPDATED_STATUS
        defaultEventTicketTransactionShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where status does not contain DEFAULT_STATUS
        defaultEventTicketTransactionShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the eventTicketTransactionList where status does not contain UPDATED_STATUS
        defaultEventTicketTransactionShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultEventTicketTransactionShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the eventTicketTransactionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultEventTicketTransactionShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the eventTicketTransactionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate is not null
        defaultEventTicketTransactionShouldBeFound("purchaseDate.specified=true");

        // Get all the eventTicketTransactionList where purchaseDate is null
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate is greater than or equal to DEFAULT_PURCHASE_DATE
        defaultEventTicketTransactionShouldBeFound("purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the eventTicketTransactionList where purchaseDate is greater than or equal to UPDATED_PURCHASE_DATE
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate is less than or equal to DEFAULT_PURCHASE_DATE
        defaultEventTicketTransactionShouldBeFound("purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the eventTicketTransactionList where purchaseDate is less than or equal to SMALLER_PURCHASE_DATE
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate is less than DEFAULT_PURCHASE_DATE
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the eventTicketTransactionList where purchaseDate is less than UPDATED_PURCHASE_DATE
        defaultEventTicketTransactionShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where purchaseDate is greater than DEFAULT_PURCHASE_DATE
        defaultEventTicketTransactionShouldNotBeFound("purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the eventTicketTransactionList where purchaseDate is greater than SMALLER_PURCHASE_DATE
        defaultEventTicketTransactionShouldBeFound("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId equals to DEFAULT_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldBeFound("discountCodeId.equals=" + DEFAULT_DISCOUNT_CODE_ID);

        // Get all the eventTicketTransactionList where discountCodeId equals to UPDATED_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.equals=" + UPDATED_DISCOUNT_CODE_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId in DEFAULT_DISCOUNT_CODE_ID or UPDATED_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldBeFound("discountCodeId.in=" + DEFAULT_DISCOUNT_CODE_ID + "," + UPDATED_DISCOUNT_CODE_ID);

        // Get all the eventTicketTransactionList where discountCodeId equals to UPDATED_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.in=" + UPDATED_DISCOUNT_CODE_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId is not null
        defaultEventTicketTransactionShouldBeFound("discountCodeId.specified=true");

        // Get all the eventTicketTransactionList where discountCodeId is null
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId is greater than or equal to DEFAULT_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldBeFound("discountCodeId.greaterThanOrEqual=" + DEFAULT_DISCOUNT_CODE_ID);

        // Get all the eventTicketTransactionList where discountCodeId is greater than or equal to UPDATED_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.greaterThanOrEqual=" + UPDATED_DISCOUNT_CODE_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId is less than or equal to DEFAULT_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldBeFound("discountCodeId.lessThanOrEqual=" + DEFAULT_DISCOUNT_CODE_ID);

        // Get all the eventTicketTransactionList where discountCodeId is less than or equal to SMALLER_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.lessThanOrEqual=" + SMALLER_DISCOUNT_CODE_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId is less than DEFAULT_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.lessThan=" + DEFAULT_DISCOUNT_CODE_ID);

        // Get all the eventTicketTransactionList where discountCodeId is less than UPDATED_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldBeFound("discountCodeId.lessThan=" + UPDATED_DISCOUNT_CODE_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountCodeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountCodeId is greater than DEFAULT_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldNotBeFound("discountCodeId.greaterThan=" + DEFAULT_DISCOUNT_CODE_ID);

        // Get all the eventTicketTransactionList where discountCodeId is greater than SMALLER_DISCOUNT_CODE_ID
        defaultEventTicketTransactionShouldBeFound("discountCodeId.greaterThan=" + SMALLER_DISCOUNT_CODE_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount equals to DEFAULT_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldBeFound("discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the eventTicketTransactionList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount in DEFAULT_DISCOUNT_AMOUNT or UPDATED_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldBeFound("discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT);

        // Get all the eventTicketTransactionList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount is not null
        defaultEventTicketTransactionShouldBeFound("discountAmount.specified=true");

        // Get all the eventTicketTransactionList where discountAmount is null
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount is greater than or equal to DEFAULT_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldBeFound("discountAmount.greaterThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the eventTicketTransactionList where discountAmount is greater than or equal to UPDATED_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.greaterThanOrEqual=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount is less than or equal to DEFAULT_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldBeFound("discountAmount.lessThanOrEqual=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the eventTicketTransactionList where discountAmount is less than or equal to SMALLER_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.lessThanOrEqual=" + SMALLER_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount is less than DEFAULT_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.lessThan=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the eventTicketTransactionList where discountAmount is less than UPDATED_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldBeFound("discountAmount.lessThan=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByDiscountAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where discountAmount is greater than DEFAULT_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldNotBeFound("discountAmount.greaterThan=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the eventTicketTransactionList where discountAmount is greater than SMALLER_DISCOUNT_AMOUNT
        defaultEventTicketTransactionShouldBeFound("discountAmount.greaterThan=" + SMALLER_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventTicketTransactionShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTransactionList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTicketTransactionShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventTicketTransactionShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventTicketTransactionList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTicketTransactionShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt is not null
        defaultEventTicketTransactionShouldBeFound("createdAt.specified=true");

        // Get all the eventTicketTransactionList where createdAt is null
        defaultEventTicketTransactionShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventTicketTransactionShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTransactionList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventTicketTransactionShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventTicketTransactionShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTransactionList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventTicketTransactionShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventTicketTransactionShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTransactionList where createdAt is less than UPDATED_CREATED_AT
        defaultEventTicketTransactionShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventTicketTransactionShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTransactionList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventTicketTransactionShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventTicketTransactionShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTransactionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventTicketTransactionShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventTicketTransactionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt is not null
        defaultEventTicketTransactionShouldBeFound("updatedAt.specified=true");

        // Get all the eventTicketTransactionList where updatedAt is null
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventTicketTransactionShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTransactionList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventTicketTransactionShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTransactionList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTransactionList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventTicketTransactionShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTransactionsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        // Get all the eventTicketTransactionList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventTicketTransactionShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTransactionList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventTicketTransactionShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /* @Test
    @Transactional
    void getAllEventTicketTransactionsByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventTicketTransaction.setEvent(event);
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);
        Long eventId = event.getId();
        // Get all the eventTicketTransactionList where event equals to eventId
        defaultEventTicketTransactionShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventTicketTransactionList where event equals to (eventId + 1)
        defaultEventTicketTransactionShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }*/

    //    @Test
    @Transactional
    void getAllEventTicketTransactionsByTicketTypeIsEqualToSomething() throws Exception {
        EventTicketType ticketType;
        if (TestUtil.findAll(em, EventTicketType.class).isEmpty()) {
            eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);
            ticketType = EventTicketTypeResourceIT.createEntity(em);
        } else {
            ticketType = TestUtil.findAll(em, EventTicketType.class).get(0);
        }
        em.persist(ticketType);
        em.flush();
        //        eventTicketTransaction.setTicketType(ticketType);
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);
        Long ticketTypeId = ticketType.getId();
        // Get all the eventTicketTransactionList where ticketType equals to ticketTypeId
        defaultEventTicketTransactionShouldBeFound("ticketTypeId.equals=" + ticketTypeId);

        // Get all the eventTicketTransactionList where ticketType equals to (ticketTypeId + 1)
        defaultEventTicketTransactionShouldNotBeFound("ticketTypeId.equals=" + (ticketTypeId + 1));
    }

    /* @Test
    @Transactional
    void getAllEventTicketTransactionsByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        eventTicketTransaction.setUser(user);
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);
        Long userId = user.getId();
        // Get all the eventTicketTransactionList where user equals to userId
        defaultEventTicketTransactionShouldBeFound("userId.equals=" + userId);

        // Get all the eventTicketTransactionList where user equals to (userId + 1)
        defaultEventTicketTransactionShouldNotBeFound("userId.equals=" + (userId + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTicketTransactionShouldBeFound(String filter) throws Exception {
        restEventTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTicketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(sameInstant(DEFAULT_PURCHASE_DATE))))
            .andExpect(jsonPath("$.[*].discountCodeId").value(hasItem(DEFAULT_DISCOUNT_CODE_ID.intValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(sameNumber(DEFAULT_DISCOUNT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTicketTransactionShouldNotBeFound(String filter) throws Exception {
        restEventTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventTicketTransaction() throws Exception {
        // Get the eventTicketTransaction
        restEventTicketTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventTicketTransaction() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();

        // Update the eventTicketTransaction
        EventTicketTransaction updatedEventTicketTransaction = eventTicketTransactionRepository
            .findById(eventTicketTransaction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEventTicketTransaction are not directly saved in db
        em.detach(updatedEventTicketTransaction);
        updatedEventTicketTransaction
            .tenantId(UPDATED_TENANT_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .discountCodeId(UPDATED_DISCOUNT_CODE_ID)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(updatedEventTicketTransaction);

        restEventTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTicketTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
        EventTicketTransaction testEventTicketTransaction = eventTicketTransactionList.get(eventTicketTransactionList.size() - 1);
        assertThat(testEventTicketTransaction.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEventTicketTransaction.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEventTicketTransaction.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEventTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testEventTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testEventTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testEventTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testEventTicketTransaction.getDiscountCodeId()).isEqualTo(UPDATED_DISCOUNT_CODE_ID);
        assertThat(testEventTicketTransaction.getDiscountAmount()).isEqualByComparingTo(UPDATED_DISCOUNT_AMOUNT);
        assertThat(testEventTicketTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTicketTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();
        eventTicketTransaction.setId(longCount.incrementAndGet());

        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTicketTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();
        eventTicketTransaction.setId(longCount.incrementAndGet());

        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();
        eventTicketTransaction.setId(longCount.incrementAndGet());

        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();

        // Update the eventTicketTransaction using partial update
        EventTicketTransaction partialUpdatedEventTicketTransaction = new EventTicketTransaction();
        partialUpdatedEventTicketTransaction.setId(eventTicketTransaction.getId());

        partialUpdatedEventTicketTransaction
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .createdAt(UPDATED_CREATED_AT);

        restEventTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
        EventTicketTransaction testEventTicketTransaction = eventTicketTransactionList.get(eventTicketTransactionList.size() - 1);
        assertThat(testEventTicketTransaction.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEventTicketTransaction.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEventTicketTransaction.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEventTicketTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testEventTicketTransaction.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testEventTicketTransaction.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testEventTicketTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testEventTicketTransaction.getDiscountCodeId()).isEqualTo(DEFAULT_DISCOUNT_CODE_ID);
        assertThat(testEventTicketTransaction.getDiscountAmount()).isEqualByComparingTo(DEFAULT_DISCOUNT_AMOUNT);
        assertThat(testEventTicketTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTicketTransaction.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();

        // Update the eventTicketTransaction using partial update
        EventTicketTransaction partialUpdatedEventTicketTransaction = new EventTicketTransaction();
        partialUpdatedEventTicketTransaction.setId(eventTicketTransaction.getId());

        partialUpdatedEventTicketTransaction
            .tenantId(UPDATED_TENANT_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .discountCodeId(UPDATED_DISCOUNT_CODE_ID)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
        EventTicketTransaction testEventTicketTransaction = eventTicketTransactionList.get(eventTicketTransactionList.size() - 1);
        assertThat(testEventTicketTransaction.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEventTicketTransaction.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEventTicketTransaction.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEventTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testEventTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testEventTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testEventTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testEventTicketTransaction.getDiscountCodeId()).isEqualTo(UPDATED_DISCOUNT_CODE_ID);
        assertThat(testEventTicketTransaction.getDiscountAmount()).isEqualByComparingTo(UPDATED_DISCOUNT_AMOUNT);
        assertThat(testEventTicketTransaction.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTicketTransaction.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();
        eventTicketTransaction.setId(longCount.incrementAndGet());

        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventTicketTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();
        eventTicketTransaction.setId(longCount.incrementAndGet());

        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTransactionRepository.findAll().size();
        eventTicketTransaction.setId(longCount.incrementAndGet());

        // Create the EventTicketTransaction
        EventTicketTransactionDTO eventTicketTransactionDTO = eventTicketTransactionMapper.toDto(eventTicketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTicketTransaction in the database
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventTicketTransaction() throws Exception {
        // Initialize the database
        eventTicketTransactionRepository.saveAndFlush(eventTicketTransaction);

        int databaseSizeBeforeDelete = eventTicketTransactionRepository.findAll().size();

        // Delete the eventTicketTransaction
        restEventTicketTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventTicketTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventTicketTransaction> eventTicketTransactionList = eventTicketTransactionRepository.findAll();
        assertThat(eventTicketTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
