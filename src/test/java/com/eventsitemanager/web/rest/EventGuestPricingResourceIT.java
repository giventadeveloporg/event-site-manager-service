package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static com.eventsitemanager.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventGuestPricing;
import com.eventsitemanager.repository.EventGuestPricingRepository;
import com.eventsitemanager.service.dto.EventGuestPricingDTO;
import com.eventsitemanager.service.mapper.EventGuestPricingMapper;
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
 * Integration tests for the {@link EventGuestPricingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventGuestPricingResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AGE_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_AGE_GROUP = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(0 - 1);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_VALID_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALID_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_VALID_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_TO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_VALID_TO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-guest-pricings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventGuestPricingRepository eventGuestPricingRepository;

    @Autowired
    private EventGuestPricingMapper eventGuestPricingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventGuestPricingMockMvc;

    private EventGuestPricing eventGuestPricing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventGuestPricing createEntity(EntityManager em) {
        EventGuestPricing eventGuestPricing = new EventGuestPricing()
            .tenantId(DEFAULT_TENANT_ID)
            .ageGroup(DEFAULT_AGE_GROUP)
            .price(DEFAULT_PRICE)
            .isActive(DEFAULT_IS_ACTIVE)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO)
            .description(DEFAULT_DESCRIPTION)
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
        eventGuestPricing.setEvent(eventDetails);
        return eventGuestPricing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventGuestPricing createUpdatedEntity(EntityManager em) {
        EventGuestPricing eventGuestPricing = new EventGuestPricing()
            .tenantId(UPDATED_TENANT_ID)
            .ageGroup(UPDATED_AGE_GROUP)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .description(UPDATED_DESCRIPTION)
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
        eventGuestPricing.setEvent(eventDetails);
        return eventGuestPricing;
    }

    @BeforeEach
    public void initTest() {
        eventGuestPricing = createEntity(em);
    }

    @Test
    @Transactional
    void createEventGuestPricing() throws Exception {
        int databaseSizeBeforeCreate = eventGuestPricingRepository.findAll().size();
        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);
        restEventGuestPricingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeCreate + 1);
        EventGuestPricing testEventGuestPricing = eventGuestPricingList.get(eventGuestPricingList.size() - 1);
        assertThat(testEventGuestPricing.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventGuestPricing.getAgeGroup()).isEqualTo(DEFAULT_AGE_GROUP);
        assertThat(testEventGuestPricing.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testEventGuestPricing.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEventGuestPricing.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testEventGuestPricing.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
        assertThat(testEventGuestPricing.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventGuestPricing.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventGuestPricing.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventGuestPricingWithExistingId() throws Exception {
        // Create the EventGuestPricing with an existing ID
        eventGuestPricing.setId(1L);
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        int databaseSizeBeforeCreate = eventGuestPricingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventGuestPricingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventGuestPricingRepository.findAll().size();
        // set the field null
        eventGuestPricing.setPrice(null);

        // Create the EventGuestPricing, which fails.
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        restEventGuestPricingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventGuestPricingRepository.findAll().size();
        // set the field null
        eventGuestPricing.setCreatedAt(null);

        // Create the EventGuestPricing, which fails.
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        restEventGuestPricingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventGuestPricingRepository.findAll().size();
        // set the field null
        eventGuestPricing.setUpdatedAt(null);

        // Create the EventGuestPricing, which fails.
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        restEventGuestPricingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventGuestPricings() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList
        restEventGuestPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventGuestPricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].ageGroup").value(hasItem(DEFAULT_AGE_GROUP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventGuestPricing() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get the eventGuestPricing
        restEventGuestPricingMockMvc
            .perform(get(ENTITY_API_URL_ID, eventGuestPricing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventGuestPricing.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.ageGroup").value(DEFAULT_AGE_GROUP))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventGuestPricingsByIdFiltering() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        Long id = eventGuestPricing.getId();

        defaultEventGuestPricingShouldBeFound("id.equals=" + id);
        defaultEventGuestPricingShouldNotBeFound("id.notEquals=" + id);

        defaultEventGuestPricingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventGuestPricingShouldNotBeFound("id.greaterThan=" + id);

        defaultEventGuestPricingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventGuestPricingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventGuestPricingShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventGuestPricingList where tenantId equals to UPDATED_TENANT_ID
        defaultEventGuestPricingShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventGuestPricingShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventGuestPricingList where tenantId equals to UPDATED_TENANT_ID
        defaultEventGuestPricingShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where tenantId is not null
        defaultEventGuestPricingShouldBeFound("tenantId.specified=true");

        // Get all the eventGuestPricingList where tenantId is null
        defaultEventGuestPricingShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where tenantId contains DEFAULT_TENANT_ID
        defaultEventGuestPricingShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventGuestPricingList where tenantId contains UPDATED_TENANT_ID
        defaultEventGuestPricingShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventGuestPricingShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventGuestPricingList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventGuestPricingShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByAgeGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where ageGroup equals to DEFAULT_AGE_GROUP
        defaultEventGuestPricingShouldBeFound("ageGroup.equals=" + DEFAULT_AGE_GROUP);

        // Get all the eventGuestPricingList where ageGroup equals to UPDATED_AGE_GROUP
        defaultEventGuestPricingShouldNotBeFound("ageGroup.equals=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByAgeGroupIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where ageGroup in DEFAULT_AGE_GROUP or UPDATED_AGE_GROUP
        defaultEventGuestPricingShouldBeFound("ageGroup.in=" + DEFAULT_AGE_GROUP + "," + UPDATED_AGE_GROUP);

        // Get all the eventGuestPricingList where ageGroup equals to UPDATED_AGE_GROUP
        defaultEventGuestPricingShouldNotBeFound("ageGroup.in=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByAgeGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where ageGroup is not null
        defaultEventGuestPricingShouldBeFound("ageGroup.specified=true");

        // Get all the eventGuestPricingList where ageGroup is null
        defaultEventGuestPricingShouldNotBeFound("ageGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByAgeGroupContainsSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where ageGroup contains DEFAULT_AGE_GROUP
        defaultEventGuestPricingShouldBeFound("ageGroup.contains=" + DEFAULT_AGE_GROUP);

        // Get all the eventGuestPricingList where ageGroup contains UPDATED_AGE_GROUP
        defaultEventGuestPricingShouldNotBeFound("ageGroup.contains=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByAgeGroupNotContainsSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where ageGroup does not contain DEFAULT_AGE_GROUP
        defaultEventGuestPricingShouldNotBeFound("ageGroup.doesNotContain=" + DEFAULT_AGE_GROUP);

        // Get all the eventGuestPricingList where ageGroup does not contain UPDATED_AGE_GROUP
        defaultEventGuestPricingShouldBeFound("ageGroup.doesNotContain=" + UPDATED_AGE_GROUP);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price equals to DEFAULT_PRICE
        defaultEventGuestPricingShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the eventGuestPricingList where price equals to UPDATED_PRICE
        defaultEventGuestPricingShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultEventGuestPricingShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the eventGuestPricingList where price equals to UPDATED_PRICE
        defaultEventGuestPricingShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price is not null
        defaultEventGuestPricingShouldBeFound("price.specified=true");

        // Get all the eventGuestPricingList where price is null
        defaultEventGuestPricingShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price is greater than or equal to DEFAULT_PRICE
        defaultEventGuestPricingShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the eventGuestPricingList where price is greater than or equal to UPDATED_PRICE
        defaultEventGuestPricingShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price is less than or equal to DEFAULT_PRICE
        defaultEventGuestPricingShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the eventGuestPricingList where price is less than or equal to SMALLER_PRICE
        defaultEventGuestPricingShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price is less than DEFAULT_PRICE
        defaultEventGuestPricingShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the eventGuestPricingList where price is less than UPDATED_PRICE
        defaultEventGuestPricingShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where price is greater than DEFAULT_PRICE
        defaultEventGuestPricingShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the eventGuestPricingList where price is greater than SMALLER_PRICE
        defaultEventGuestPricingShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEventGuestPricingShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the eventGuestPricingList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventGuestPricingShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEventGuestPricingShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the eventGuestPricingList where isActive equals to UPDATED_IS_ACTIVE
        defaultEventGuestPricingShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where isActive is not null
        defaultEventGuestPricingShouldBeFound("isActive.specified=true");

        // Get all the eventGuestPricingList where isActive is null
        defaultEventGuestPricingShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom equals to DEFAULT_VALID_FROM
        defaultEventGuestPricingShouldBeFound("validFrom.equals=" + DEFAULT_VALID_FROM);

        // Get all the eventGuestPricingList where validFrom equals to UPDATED_VALID_FROM
        defaultEventGuestPricingShouldNotBeFound("validFrom.equals=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom in DEFAULT_VALID_FROM or UPDATED_VALID_FROM
        defaultEventGuestPricingShouldBeFound("validFrom.in=" + DEFAULT_VALID_FROM + "," + UPDATED_VALID_FROM);

        // Get all the eventGuestPricingList where validFrom equals to UPDATED_VALID_FROM
        defaultEventGuestPricingShouldNotBeFound("validFrom.in=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom is not null
        defaultEventGuestPricingShouldBeFound("validFrom.specified=true");

        // Get all the eventGuestPricingList where validFrom is null
        defaultEventGuestPricingShouldNotBeFound("validFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom is greater than or equal to DEFAULT_VALID_FROM
        defaultEventGuestPricingShouldBeFound("validFrom.greaterThanOrEqual=" + DEFAULT_VALID_FROM);

        // Get all the eventGuestPricingList where validFrom is greater than or equal to UPDATED_VALID_FROM
        defaultEventGuestPricingShouldNotBeFound("validFrom.greaterThanOrEqual=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom is less than or equal to DEFAULT_VALID_FROM
        defaultEventGuestPricingShouldBeFound("validFrom.lessThanOrEqual=" + DEFAULT_VALID_FROM);

        // Get all the eventGuestPricingList where validFrom is less than or equal to SMALLER_VALID_FROM
        defaultEventGuestPricingShouldNotBeFound("validFrom.lessThanOrEqual=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsLessThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom is less than DEFAULT_VALID_FROM
        defaultEventGuestPricingShouldNotBeFound("validFrom.lessThan=" + DEFAULT_VALID_FROM);

        // Get all the eventGuestPricingList where validFrom is less than UPDATED_VALID_FROM
        defaultEventGuestPricingShouldBeFound("validFrom.lessThan=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validFrom is greater than DEFAULT_VALID_FROM
        defaultEventGuestPricingShouldNotBeFound("validFrom.greaterThan=" + DEFAULT_VALID_FROM);

        // Get all the eventGuestPricingList where validFrom is greater than SMALLER_VALID_FROM
        defaultEventGuestPricingShouldBeFound("validFrom.greaterThan=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo equals to DEFAULT_VALID_TO
        defaultEventGuestPricingShouldBeFound("validTo.equals=" + DEFAULT_VALID_TO);

        // Get all the eventGuestPricingList where validTo equals to UPDATED_VALID_TO
        defaultEventGuestPricingShouldNotBeFound("validTo.equals=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo in DEFAULT_VALID_TO or UPDATED_VALID_TO
        defaultEventGuestPricingShouldBeFound("validTo.in=" + DEFAULT_VALID_TO + "," + UPDATED_VALID_TO);

        // Get all the eventGuestPricingList where validTo equals to UPDATED_VALID_TO
        defaultEventGuestPricingShouldNotBeFound("validTo.in=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo is not null
        defaultEventGuestPricingShouldBeFound("validTo.specified=true");

        // Get all the eventGuestPricingList where validTo is null
        defaultEventGuestPricingShouldNotBeFound("validTo.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo is greater than or equal to DEFAULT_VALID_TO
        defaultEventGuestPricingShouldBeFound("validTo.greaterThanOrEqual=" + DEFAULT_VALID_TO);

        // Get all the eventGuestPricingList where validTo is greater than or equal to UPDATED_VALID_TO
        defaultEventGuestPricingShouldNotBeFound("validTo.greaterThanOrEqual=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo is less than or equal to DEFAULT_VALID_TO
        defaultEventGuestPricingShouldBeFound("validTo.lessThanOrEqual=" + DEFAULT_VALID_TO);

        // Get all the eventGuestPricingList where validTo is less than or equal to SMALLER_VALID_TO
        defaultEventGuestPricingShouldNotBeFound("validTo.lessThanOrEqual=" + SMALLER_VALID_TO);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsLessThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo is less than DEFAULT_VALID_TO
        defaultEventGuestPricingShouldNotBeFound("validTo.lessThan=" + DEFAULT_VALID_TO);

        // Get all the eventGuestPricingList where validTo is less than UPDATED_VALID_TO
        defaultEventGuestPricingShouldBeFound("validTo.lessThan=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByValidToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where validTo is greater than DEFAULT_VALID_TO
        defaultEventGuestPricingShouldNotBeFound("validTo.greaterThan=" + DEFAULT_VALID_TO);

        // Get all the eventGuestPricingList where validTo is greater than SMALLER_VALID_TO
        defaultEventGuestPricingShouldBeFound("validTo.greaterThan=" + SMALLER_VALID_TO);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where description equals to DEFAULT_DESCRIPTION
        defaultEventGuestPricingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventGuestPricingList where description equals to UPDATED_DESCRIPTION
        defaultEventGuestPricingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventGuestPricingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventGuestPricingList where description equals to UPDATED_DESCRIPTION
        defaultEventGuestPricingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where description is not null
        defaultEventGuestPricingShouldBeFound("description.specified=true");

        // Get all the eventGuestPricingList where description is null
        defaultEventGuestPricingShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where description contains DEFAULT_DESCRIPTION
        defaultEventGuestPricingShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventGuestPricingList where description contains UPDATED_DESCRIPTION
        defaultEventGuestPricingShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where description does not contain DEFAULT_DESCRIPTION
        defaultEventGuestPricingShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventGuestPricingList where description does not contain UPDATED_DESCRIPTION
        defaultEventGuestPricingShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventGuestPricingShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventGuestPricingList where createdAt equals to UPDATED_CREATED_AT
        defaultEventGuestPricingShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventGuestPricingShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventGuestPricingList where createdAt equals to UPDATED_CREATED_AT
        defaultEventGuestPricingShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt is not null
        defaultEventGuestPricingShouldBeFound("createdAt.specified=true");

        // Get all the eventGuestPricingList where createdAt is null
        defaultEventGuestPricingShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventGuestPricingShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventGuestPricingList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventGuestPricingShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventGuestPricingShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventGuestPricingList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventGuestPricingShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventGuestPricingShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventGuestPricingList where createdAt is less than UPDATED_CREATED_AT
        defaultEventGuestPricingShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventGuestPricingShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventGuestPricingList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventGuestPricingShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventGuestPricingShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventGuestPricingList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventGuestPricingShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventGuestPricingShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventGuestPricingList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventGuestPricingShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt is not null
        defaultEventGuestPricingShouldBeFound("updatedAt.specified=true");

        // Get all the eventGuestPricingList where updatedAt is null
        defaultEventGuestPricingShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventGuestPricingShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventGuestPricingList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventGuestPricingShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventGuestPricingShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventGuestPricingList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventGuestPricingShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventGuestPricingShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventGuestPricingList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventGuestPricingShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        // Get all the eventGuestPricingList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventGuestPricingShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventGuestPricingList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventGuestPricingShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventGuestPricingsByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventGuestPricingRepository.saveAndFlush(eventGuestPricing);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventGuestPricing.setEvent(event);
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);
        Long eventId = event.getId();
        // Get all the eventGuestPricingList where event equals to eventId
        defaultEventGuestPricingShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventGuestPricingList where event equals to (eventId + 1)
        defaultEventGuestPricingShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventGuestPricingShouldBeFound(String filter) throws Exception {
        restEventGuestPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventGuestPricing.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].ageGroup").value(hasItem(DEFAULT_AGE_GROUP)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventGuestPricingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventGuestPricingShouldNotBeFound(String filter) throws Exception {
        restEventGuestPricingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventGuestPricingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventGuestPricing() throws Exception {
        // Get the eventGuestPricing
        restEventGuestPricingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventGuestPricing() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();

        // Update the eventGuestPricing
        EventGuestPricing updatedEventGuestPricing = eventGuestPricingRepository.findById(eventGuestPricing.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventGuestPricing are not directly saved in db
        em.detach(updatedEventGuestPricing);
        updatedEventGuestPricing
            .tenantId(UPDATED_TENANT_ID)
            .ageGroup(UPDATED_AGE_GROUP)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(updatedEventGuestPricing);

        restEventGuestPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventGuestPricingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
        EventGuestPricing testEventGuestPricing = eventGuestPricingList.get(eventGuestPricingList.size() - 1);
        assertThat(testEventGuestPricing.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventGuestPricing.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
        assertThat(testEventGuestPricing.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testEventGuestPricing.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventGuestPricing.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testEventGuestPricing.getValidTo()).isEqualTo(UPDATED_VALID_TO);
        assertThat(testEventGuestPricing.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventGuestPricing.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventGuestPricing.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventGuestPricing() throws Exception {
        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();
        eventGuestPricing.setId(longCount.incrementAndGet());

        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventGuestPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventGuestPricingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventGuestPricing() throws Exception {
        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();
        eventGuestPricing.setId(longCount.incrementAndGet());

        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventGuestPricingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventGuestPricing() throws Exception {
        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();
        eventGuestPricing.setId(longCount.incrementAndGet());

        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventGuestPricingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventGuestPricingWithPatch() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();

        // Update the eventGuestPricing using partial update
        EventGuestPricing partialUpdatedEventGuestPricing = new EventGuestPricing();
        partialUpdatedEventGuestPricing.setId(eventGuestPricing.getId());

        partialUpdatedEventGuestPricing
            .ageGroup(UPDATED_AGE_GROUP)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .validFrom(UPDATED_VALID_FROM)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventGuestPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventGuestPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventGuestPricing))
            )
            .andExpect(status().isOk());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
        EventGuestPricing testEventGuestPricing = eventGuestPricingList.get(eventGuestPricingList.size() - 1);
        assertThat(testEventGuestPricing.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventGuestPricing.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
        assertThat(testEventGuestPricing.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testEventGuestPricing.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventGuestPricing.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testEventGuestPricing.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
        assertThat(testEventGuestPricing.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventGuestPricing.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventGuestPricing.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventGuestPricingWithPatch() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();

        // Update the eventGuestPricing using partial update
        EventGuestPricing partialUpdatedEventGuestPricing = new EventGuestPricing();
        partialUpdatedEventGuestPricing.setId(eventGuestPricing.getId());

        partialUpdatedEventGuestPricing
            .tenantId(UPDATED_TENANT_ID)
            .ageGroup(UPDATED_AGE_GROUP)
            .price(UPDATED_PRICE)
            .isActive(UPDATED_IS_ACTIVE)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventGuestPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventGuestPricing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventGuestPricing))
            )
            .andExpect(status().isOk());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
        EventGuestPricing testEventGuestPricing = eventGuestPricingList.get(eventGuestPricingList.size() - 1);
        assertThat(testEventGuestPricing.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventGuestPricing.getAgeGroup()).isEqualTo(UPDATED_AGE_GROUP);
        assertThat(testEventGuestPricing.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testEventGuestPricing.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEventGuestPricing.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testEventGuestPricing.getValidTo()).isEqualTo(UPDATED_VALID_TO);
        assertThat(testEventGuestPricing.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventGuestPricing.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventGuestPricing.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventGuestPricing() throws Exception {
        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();
        eventGuestPricing.setId(longCount.incrementAndGet());

        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventGuestPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventGuestPricingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventGuestPricing() throws Exception {
        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();
        eventGuestPricing.setId(longCount.incrementAndGet());

        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventGuestPricingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventGuestPricing() throws Exception {
        int databaseSizeBeforeUpdate = eventGuestPricingRepository.findAll().size();
        eventGuestPricing.setId(longCount.incrementAndGet());

        // Create the EventGuestPricing
        EventGuestPricingDTO eventGuestPricingDTO = eventGuestPricingMapper.toDto(eventGuestPricing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventGuestPricingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventGuestPricingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventGuestPricing in the database
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventGuestPricing() throws Exception {
        // Initialize the database
        eventGuestPricingRepository.saveAndFlush(eventGuestPricing);

        int databaseSizeBeforeDelete = eventGuestPricingRepository.findAll().size();

        // Delete the eventGuestPricing
        restEventGuestPricingMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventGuestPricing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventGuestPricing> eventGuestPricingList = eventGuestPricingRepository.findAll();
        assertThat(eventGuestPricingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
