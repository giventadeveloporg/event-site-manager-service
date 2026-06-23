package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static com.eventsitemanager.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventTicketType;
import com.eventsitemanager.repository.EventTicketTypeRepository;
import com.eventsitemanager.service.dto.EventTicketTypeDTO;
import com.eventsitemanager.service.mapper.EventTicketTypeMapper;
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
 * Integration tests for the {@link EventTicketTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventTicketTypeResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE_QUANTITY = 1;
    private static final Integer UPDATED_AVAILABLE_QUANTITY = 2;
    private static final Integer SMALLER_AVAILABLE_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_SOLD_QUANTITY = 1;
    private static final Integer UPDATED_SOLD_QUANTITY = 2;
    private static final Integer SMALLER_SOLD_QUANTITY = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-ticket-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventTicketTypeRepository eventTicketTypeRepository;

    @Autowired
    private EventTicketTypeMapper eventTicketTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventTicketTypeMockMvc;

    private EventTicketType eventTicketType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTicketType createEntity(EntityManager em) {
        EventTicketType eventTicketType = new EventTicketType()
            .tenantId(DEFAULT_TENANT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .code(DEFAULT_CODE)
            .availableQuantity(DEFAULT_AVAILABLE_QUANTITY)
            .soldQuantity(DEFAULT_SOLD_QUANTITY)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return eventTicketType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventTicketType createUpdatedEntity(EntityManager em) {
        EventTicketType eventTicketType = new EventTicketType()
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .code(UPDATED_CODE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return eventTicketType;
    }

    @BeforeEach
    public void initTest() {
        eventTicketType = createEntity(em);
    }

    @Test
    @Transactional
    void createEventTicketType() throws Exception {
        int databaseSizeBeforeCreate = eventTicketTypeRepository.findAll().size();
        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);
        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EventTicketType testEventTicketType = eventTicketTypeList.get(eventTicketTypeList.size() - 1);
        assertThat(testEventTicketType.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventTicketType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventTicketType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventTicketType.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testEventTicketType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEventTicketType.getAvailableQuantity()).isEqualTo(DEFAULT_AVAILABLE_QUANTITY);
        assertThat(testEventTicketType.getSoldQuantity()).isEqualTo(DEFAULT_SOLD_QUANTITY);
        assertThat(testEventTicketType.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEventTicketType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventTicketType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventTicketTypeWithExistingId() throws Exception {
        // Create the EventTicketType with an existing ID
        eventTicketType.setId(1L);
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        int databaseSizeBeforeCreate = eventTicketTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTypeRepository.findAll().size();
        // set the field null
        eventTicketType.setName(null);

        // Create the EventTicketType, which fails.
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTypeRepository.findAll().size();
        // set the field null
        eventTicketType.setPrice(null);

        // Create the EventTicketType, which fails.
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTypeRepository.findAll().size();
        // set the field null
        eventTicketType.setCode(null);

        // Create the EventTicketType, which fails.
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTypeRepository.findAll().size();
        // set the field null
        eventTicketType.setCreatedAt(null);

        // Create the EventTicketType, which fails.
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventTicketTypeRepository.findAll().size();
        // set the field null
        eventTicketType.setUpdatedAt(null);

        // Create the EventTicketType, which fails.
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        restEventTicketTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventTicketTypes() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList
        restEventTicketTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTicketType.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].availableQuantity").value(hasItem(DEFAULT_AVAILABLE_QUANTITY)))
            .andExpect(jsonPath("$.[*].soldQuantity").value(hasItem(DEFAULT_SOLD_QUANTITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventTicketType() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get the eventTicketType
        restEventTicketTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, eventTicketType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventTicketType.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.availableQuantity").value(DEFAULT_AVAILABLE_QUANTITY))
            .andExpect(jsonPath("$.soldQuantity").value(DEFAULT_SOLD_QUANTITY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventTicketTypesByIdFiltering() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        Long id = eventTicketType.getId();

        defaultEventTicketTypeShouldBeFound("id.equals=" + id);
        defaultEventTicketTypeShouldNotBeFound("id.notEquals=" + id);

        defaultEventTicketTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventTicketTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultEventTicketTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventTicketTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventTicketTypeShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventTicketTypeList where tenantId equals to UPDATED_TENANT_ID
        defaultEventTicketTypeShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventTicketTypeShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventTicketTypeList where tenantId equals to UPDATED_TENANT_ID
        defaultEventTicketTypeShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where tenantId is not null
        defaultEventTicketTypeShouldBeFound("tenantId.specified=true");

        // Get all the eventTicketTypeList where tenantId is null
        defaultEventTicketTypeShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where tenantId contains DEFAULT_TENANT_ID
        defaultEventTicketTypeShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventTicketTypeList where tenantId contains UPDATED_TENANT_ID
        defaultEventTicketTypeShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventTicketTypeShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventTicketTypeList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventTicketTypeShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where name equals to DEFAULT_NAME
        defaultEventTicketTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventTicketTypeList where name equals to UPDATED_NAME
        defaultEventTicketTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventTicketTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventTicketTypeList where name equals to UPDATED_NAME
        defaultEventTicketTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where name is not null
        defaultEventTicketTypeShouldBeFound("name.specified=true");

        // Get all the eventTicketTypeList where name is null
        defaultEventTicketTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where name contains DEFAULT_NAME
        defaultEventTicketTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventTicketTypeList where name contains UPDATED_NAME
        defaultEventTicketTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where name does not contain DEFAULT_NAME
        defaultEventTicketTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventTicketTypeList where name does not contain UPDATED_NAME
        defaultEventTicketTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where description equals to DEFAULT_DESCRIPTION
        defaultEventTicketTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventTicketTypeList where description equals to UPDATED_DESCRIPTION
        defaultEventTicketTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventTicketTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventTicketTypeList where description equals to UPDATED_DESCRIPTION
        defaultEventTicketTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where description is not null
        defaultEventTicketTypeShouldBeFound("description.specified=true");

        // Get all the eventTicketTypeList where description is null
        defaultEventTicketTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where description contains DEFAULT_DESCRIPTION
        defaultEventTicketTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventTicketTypeList where description contains UPDATED_DESCRIPTION
        defaultEventTicketTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultEventTicketTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventTicketTypeList where description does not contain UPDATED_DESCRIPTION
        defaultEventTicketTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price equals to DEFAULT_PRICE
        defaultEventTicketTypeShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the eventTicketTypeList where price equals to UPDATED_PRICE
        defaultEventTicketTypeShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultEventTicketTypeShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the eventTicketTypeList where price equals to UPDATED_PRICE
        defaultEventTicketTypeShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price is not null
        defaultEventTicketTypeShouldBeFound("price.specified=true");

        // Get all the eventTicketTypeList where price is null
        defaultEventTicketTypeShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price is greater than or equal to DEFAULT_PRICE
        defaultEventTicketTypeShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the eventTicketTypeList where price is greater than or equal to UPDATED_PRICE
        defaultEventTicketTypeShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price is less than or equal to DEFAULT_PRICE
        defaultEventTicketTypeShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the eventTicketTypeList where price is less than or equal to SMALLER_PRICE
        defaultEventTicketTypeShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price is less than DEFAULT_PRICE
        defaultEventTicketTypeShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the eventTicketTypeList where price is less than UPDATED_PRICE
        defaultEventTicketTypeShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where price is greater than DEFAULT_PRICE
        defaultEventTicketTypeShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the eventTicketTypeList where price is greater than SMALLER_PRICE
        defaultEventTicketTypeShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where code equals to DEFAULT_CODE
        defaultEventTicketTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the eventTicketTypeList where code equals to UPDATED_CODE
        defaultEventTicketTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultEventTicketTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the eventTicketTypeList where code equals to UPDATED_CODE
        defaultEventTicketTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where code is not null
        defaultEventTicketTypeShouldBeFound("code.specified=true");

        // Get all the eventTicketTypeList where code is null
        defaultEventTicketTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where code contains DEFAULT_CODE
        defaultEventTicketTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the eventTicketTypeList where code contains UPDATED_CODE
        defaultEventTicketTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where code does not contain DEFAULT_CODE
        defaultEventTicketTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the eventTicketTypeList where code does not contain UPDATED_CODE
        defaultEventTicketTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity equals to DEFAULT_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldBeFound("availableQuantity.equals=" + DEFAULT_AVAILABLE_QUANTITY);

        // Get all the eventTicketTypeList where availableQuantity equals to UPDATED_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.equals=" + UPDATED_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity in DEFAULT_AVAILABLE_QUANTITY or UPDATED_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldBeFound("availableQuantity.in=" + DEFAULT_AVAILABLE_QUANTITY + "," + UPDATED_AVAILABLE_QUANTITY);

        // Get all the eventTicketTypeList where availableQuantity equals to UPDATED_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.in=" + UPDATED_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity is not null
        defaultEventTicketTypeShouldBeFound("availableQuantity.specified=true");

        // Get all the eventTicketTypeList where availableQuantity is null
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity is greater than or equal to DEFAULT_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldBeFound("availableQuantity.greaterThanOrEqual=" + DEFAULT_AVAILABLE_QUANTITY);

        // Get all the eventTicketTypeList where availableQuantity is greater than or equal to UPDATED_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.greaterThanOrEqual=" + UPDATED_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity is less than or equal to DEFAULT_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldBeFound("availableQuantity.lessThanOrEqual=" + DEFAULT_AVAILABLE_QUANTITY);

        // Get all the eventTicketTypeList where availableQuantity is less than or equal to SMALLER_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.lessThanOrEqual=" + SMALLER_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity is less than DEFAULT_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.lessThan=" + DEFAULT_AVAILABLE_QUANTITY);

        // Get all the eventTicketTypeList where availableQuantity is less than UPDATED_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldBeFound("availableQuantity.lessThan=" + UPDATED_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByAvailableQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where availableQuantity is greater than DEFAULT_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("availableQuantity.greaterThan=" + DEFAULT_AVAILABLE_QUANTITY);

        // Get all the eventTicketTypeList where availableQuantity is greater than SMALLER_AVAILABLE_QUANTITY
        defaultEventTicketTypeShouldBeFound("availableQuantity.greaterThan=" + SMALLER_AVAILABLE_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity equals to DEFAULT_SOLD_QUANTITY
        defaultEventTicketTypeShouldBeFound("soldQuantity.equals=" + DEFAULT_SOLD_QUANTITY);

        // Get all the eventTicketTypeList where soldQuantity equals to UPDATED_SOLD_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.equals=" + UPDATED_SOLD_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity in DEFAULT_SOLD_QUANTITY or UPDATED_SOLD_QUANTITY
        defaultEventTicketTypeShouldBeFound("soldQuantity.in=" + DEFAULT_SOLD_QUANTITY + "," + UPDATED_SOLD_QUANTITY);

        // Get all the eventTicketTypeList where soldQuantity equals to UPDATED_SOLD_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.in=" + UPDATED_SOLD_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity is not null
        defaultEventTicketTypeShouldBeFound("soldQuantity.specified=true");

        // Get all the eventTicketTypeList where soldQuantity is null
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity is greater than or equal to DEFAULT_SOLD_QUANTITY
        defaultEventTicketTypeShouldBeFound("soldQuantity.greaterThanOrEqual=" + DEFAULT_SOLD_QUANTITY);

        // Get all the eventTicketTypeList where soldQuantity is greater than or equal to UPDATED_SOLD_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.greaterThanOrEqual=" + UPDATED_SOLD_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity is less than or equal to DEFAULT_SOLD_QUANTITY
        defaultEventTicketTypeShouldBeFound("soldQuantity.lessThanOrEqual=" + DEFAULT_SOLD_QUANTITY);

        // Get all the eventTicketTypeList where soldQuantity is less than or equal to SMALLER_SOLD_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.lessThanOrEqual=" + SMALLER_SOLD_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity is less than DEFAULT_SOLD_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.lessThan=" + DEFAULT_SOLD_QUANTITY);

        // Get all the eventTicketTypeList where soldQuantity is less than UPDATED_SOLD_QUANTITY
        defaultEventTicketTypeShouldBeFound("soldQuantity.lessThan=" + UPDATED_SOLD_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesBySoldQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where soldQuantity is greater than DEFAULT_SOLD_QUANTITY
        defaultEventTicketTypeShouldNotBeFound("soldQuantity.greaterThan=" + DEFAULT_SOLD_QUANTITY);

        // Get all the eventTicketTypeList where soldQuantity is greater than SMALLER_SOLD_QUANTITY
        defaultEventTicketTypeShouldBeFound("soldQuantity.greaterThan=" + SMALLER_SOLD_QUANTITY);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEventTicketTypeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the eventTicketTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventTicketTypeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEventTicketTypeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the eventTicketTypeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventTicketTypeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where isActive is not null
        defaultEventTicketTypeShouldBeFound("isActive.specified=true");

        // Get all the eventTicketTypeList where isActive is null
        defaultEventTicketTypeShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventTicketTypeShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTypeList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTicketTypeShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventTicketTypeShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventTicketTypeList where createdAt equals to UPDATED_CREATED_AT
        defaultEventTicketTypeShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt is not null
        defaultEventTicketTypeShouldBeFound("createdAt.specified=true");

        // Get all the eventTicketTypeList where createdAt is null
        defaultEventTicketTypeShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventTicketTypeShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTypeList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventTicketTypeShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventTicketTypeShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTypeList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventTicketTypeShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventTicketTypeShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTypeList where createdAt is less than UPDATED_CREATED_AT
        defaultEventTicketTypeShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventTicketTypeShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventTicketTypeList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventTicketTypeShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventTicketTypeShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTypeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTicketTypeShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventTicketTypeShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventTicketTypeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventTicketTypeShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt is not null
        defaultEventTicketTypeShouldBeFound("updatedAt.specified=true");

        // Get all the eventTicketTypeList where updatedAt is null
        defaultEventTicketTypeShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventTicketTypeShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTypeList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventTicketTypeShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventTicketTypeShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTypeList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventTicketTypeShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventTicketTypeShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTypeList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventTicketTypeShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        // Get all the eventTicketTypeList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventTicketTypeShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventTicketTypeList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventTicketTypeShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventTicketTypesByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventTicketTypeRepository.saveAndFlush(eventTicketType);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventTicketType.setEvent(event);
        eventTicketTypeRepository.saveAndFlush(eventTicketType);
        Long eventId = event.getId();
        // Get all the eventTicketTypeList where event equals to eventId
        defaultEventTicketTypeShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventTicketTypeList where event equals to (eventId + 1)
        defaultEventTicketTypeShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventTicketTypeShouldBeFound(String filter) throws Exception {
        restEventTicketTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventTicketType.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].availableQuantity").value(hasItem(DEFAULT_AVAILABLE_QUANTITY)))
            .andExpect(jsonPath("$.[*].soldQuantity").value(hasItem(DEFAULT_SOLD_QUANTITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventTicketTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventTicketTypeShouldNotBeFound(String filter) throws Exception {
        restEventTicketTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventTicketTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventTicketType() throws Exception {
        // Get the eventTicketType
        restEventTicketTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventTicketType() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();

        // Update the eventTicketType
        EventTicketType updatedEventTicketType = eventTicketTypeRepository.findById(eventTicketType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventTicketType are not directly saved in db
        em.detach(updatedEventTicketType);
        updatedEventTicketType
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .code(UPDATED_CODE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(updatedEventTicketType);

        restEventTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTicketTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
        EventTicketType testEventTicketType = eventTicketTypeList.get(eventTicketTypeList.size() - 1);
        assertThat(testEventTicketType.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventTicketType.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testEventTicketType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEventTicketType.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testEventTicketType.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testEventTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventTicketType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTicketType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventTicketType() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();
        eventTicketType.setId(longCount.incrementAndGet());

        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventTicketTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventTicketType() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();
        eventTicketType.setId(longCount.incrementAndGet());

        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventTicketType() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();
        eventTicketType.setId(longCount.incrementAndGet());

        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventTicketTypeWithPatch() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();

        // Update the eventTicketType using partial update
        EventTicketType partialUpdatedEventTicketType = new EventTicketType();
        partialUpdatedEventTicketType.setId(eventTicketType.getId());

        partialUpdatedEventTicketType.tenantId(UPDATED_TENANT_ID).name(UPDATED_NAME).price(UPDATED_PRICE).isActive(UPDATED_IS_ACTIVE);

        restEventTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTicketType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventTicketType))
            )
            .andExpect(status().isOk());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
        EventTicketType testEventTicketType = eventTicketTypeList.get(eventTicketTypeList.size() - 1);
        assertThat(testEventTicketType.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventTicketType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventTicketType.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testEventTicketType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEventTicketType.getAvailableQuantity()).isEqualTo(DEFAULT_AVAILABLE_QUANTITY);
        assertThat(testEventTicketType.getSoldQuantity()).isEqualTo(DEFAULT_SOLD_QUANTITY);
        assertThat(testEventTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventTicketType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventTicketType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventTicketTypeWithPatch() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();

        // Update the eventTicketType using partial update
        EventTicketType partialUpdatedEventTicketType = new EventTicketType();
        partialUpdatedEventTicketType.setId(eventTicketType.getId());

        partialUpdatedEventTicketType
            .tenantId(UPDATED_TENANT_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .code(UPDATED_CODE)
            .availableQuantity(UPDATED_AVAILABLE_QUANTITY)
            .soldQuantity(UPDATED_SOLD_QUANTITY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventTicketType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventTicketType))
            )
            .andExpect(status().isOk());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
        EventTicketType testEventTicketType = eventTicketTypeList.get(eventTicketTypeList.size() - 1);
        assertThat(testEventTicketType.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventTicketType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventTicketType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventTicketType.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testEventTicketType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEventTicketType.getAvailableQuantity()).isEqualTo(UPDATED_AVAILABLE_QUANTITY);
        assertThat(testEventTicketType.getSoldQuantity()).isEqualTo(UPDATED_SOLD_QUANTITY);
        assertThat(testEventTicketType.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventTicketType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventTicketType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventTicketType() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();
        eventTicketType.setId(longCount.incrementAndGet());

        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventTicketTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventTicketType() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();
        eventTicketType.setId(longCount.incrementAndGet());

        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventTicketType() throws Exception {
        int databaseSizeBeforeUpdate = eventTicketTypeRepository.findAll().size();
        eventTicketType.setId(longCount.incrementAndGet());

        // Create the EventTicketType
        EventTicketTypeDTO eventTicketTypeDTO = eventTicketTypeMapper.toDto(eventTicketType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventTicketTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventTicketTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventTicketType in the database
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventTicketType() throws Exception {
        // Initialize the database
        eventTicketTypeRepository.saveAndFlush(eventTicketType);

        int databaseSizeBeforeDelete = eventTicketTypeRepository.findAll().size();

        // Delete the eventTicketType
        restEventTicketTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventTicketType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventTicketType> eventTicketTypeList = eventTicketTypeRepository.findAll();
        assertThat(eventTicketTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
