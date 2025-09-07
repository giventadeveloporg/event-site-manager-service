package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.DiscountCode;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.repository.DiscountCodeRepository;
import com.nextjstemplate.service.dto.DiscountCodeDTO;
import com.nextjstemplate.service.mapper.DiscountCodeMapper;
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
 * Integration tests for the {@link DiscountCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiscountCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DISCOUNT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DISCOUNT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DISCOUNT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_VALUE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_MAX_USES = 1;
    private static final Integer UPDATED_MAX_USES = 2;
    private static final Integer SMALLER_MAX_USES = 1 - 1;

    private static final Integer DEFAULT_USES_COUNT = 1;
    private static final Integer UPDATED_USES_COUNT = 2;
    private static final Integer SMALLER_USES_COUNT = 1 - 1;

    private static final ZonedDateTime DEFAULT_VALID_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALID_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_VALID_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_VALID_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALID_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_VALID_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/discount-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Autowired
    private DiscountCodeMapper discountCodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiscountCodeMockMvc;

    private DiscountCode discountCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscountCode createEntity(EntityManager em) {
        DiscountCode discountCode = new DiscountCode()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discountValue(DEFAULT_DISCOUNT_VALUE)
            .maxUses(DEFAULT_MAX_USES)
            .usesCount(DEFAULT_USES_COUNT)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return discountCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscountCode createUpdatedEntity(EntityManager em) {
        DiscountCode discountCode = new DiscountCode()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .maxUses(UPDATED_MAX_USES)
            .usesCount(UPDATED_USES_COUNT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return discountCode;
    }

    @BeforeEach
    public void initTest() {
        discountCode = createEntity(em);
    }

    @Test
    @Transactional
    void createDiscountCode() throws Exception {
        int databaseSizeBeforeCreate = discountCodeRepository.findAll().size();
        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);
        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeCreate + 1);
        DiscountCode testDiscountCode = discountCodeList.get(discountCodeList.size() - 1);
        assertThat(testDiscountCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDiscountCode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDiscountCode.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testDiscountCode.getDiscountValue()).isEqualByComparingTo(DEFAULT_DISCOUNT_VALUE);
        assertThat(testDiscountCode.getMaxUses()).isEqualTo(DEFAULT_MAX_USES);
        assertThat(testDiscountCode.getUsesCount()).isEqualTo(DEFAULT_USES_COUNT);
        assertThat(testDiscountCode.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testDiscountCode.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
        assertThat(testDiscountCode.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testDiscountCode.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiscountCode.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createDiscountCodeWithExistingId() throws Exception {
        // Create the DiscountCode with an existing ID
        discountCode.setId(1L);
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        int databaseSizeBeforeCreate = discountCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountCodeRepository.findAll().size();
        // set the field null
        discountCode.setCode(null);

        // Create the DiscountCode, which fails.
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountCodeRepository.findAll().size();
        // set the field null
        discountCode.setDiscountType(null);

        // Create the DiscountCode, which fails.
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscountValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountCodeRepository.findAll().size();
        // set the field null
        discountCode.setDiscountValue(null);

        // Create the DiscountCode, which fails.
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountCodeRepository.findAll().size();
        // set the field null
        discountCode.setCreatedAt(null);

        // Create the DiscountCode, which fails.
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountCodeRepository.findAll().size();
        // set the field null
        discountCode.setUpdatedAt(null);

        // Create the DiscountCode, which fails.
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        restDiscountCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDiscountCodes() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList
        restDiscountCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE)))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].maxUses").value(hasItem(DEFAULT_MAX_USES)))
            .andExpect(jsonPath("$.[*].usesCount").value(hasItem(DEFAULT_USES_COUNT)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(sameInstant(DEFAULT_VALID_FROM))))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(sameInstant(DEFAULT_VALID_TO))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getDiscountCode() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get the discountCode
        restDiscountCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, discountCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(discountCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE))
            .andExpect(jsonPath("$.discountValue").value(sameNumber(DEFAULT_DISCOUNT_VALUE)))
            .andExpect(jsonPath("$.maxUses").value(DEFAULT_MAX_USES))
            .andExpect(jsonPath("$.usesCount").value(DEFAULT_USES_COUNT))
            .andExpect(jsonPath("$.validFrom").value(sameInstant(DEFAULT_VALID_FROM)))
            .andExpect(jsonPath("$.validTo").value(sameInstant(DEFAULT_VALID_TO)))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getDiscountCodesByIdFiltering() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        Long id = discountCode.getId();

        defaultDiscountCodeShouldBeFound("id.equals=" + id);
        defaultDiscountCodeShouldNotBeFound("id.notEquals=" + id);

        defaultDiscountCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDiscountCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultDiscountCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDiscountCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where code equals to DEFAULT_CODE
        defaultDiscountCodeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the discountCodeList where code equals to UPDATED_CODE
        defaultDiscountCodeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDiscountCodeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the discountCodeList where code equals to UPDATED_CODE
        defaultDiscountCodeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where code is not null
        defaultDiscountCodeShouldBeFound("code.specified=true");

        // Get all the discountCodeList where code is null
        defaultDiscountCodeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCodeContainsSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where code contains DEFAULT_CODE
        defaultDiscountCodeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the discountCodeList where code contains UPDATED_CODE
        defaultDiscountCodeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where code does not contain DEFAULT_CODE
        defaultDiscountCodeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the discountCodeList where code does not contain UPDATED_CODE
        defaultDiscountCodeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where description equals to DEFAULT_DESCRIPTION
        defaultDiscountCodeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the discountCodeList where description equals to UPDATED_DESCRIPTION
        defaultDiscountCodeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDiscountCodeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the discountCodeList where description equals to UPDATED_DESCRIPTION
        defaultDiscountCodeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where description is not null
        defaultDiscountCodeShouldBeFound("description.specified=true");

        // Get all the discountCodeList where description is null
        defaultDiscountCodeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where description contains DEFAULT_DESCRIPTION
        defaultDiscountCodeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the discountCodeList where description contains UPDATED_DESCRIPTION
        defaultDiscountCodeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where description does not contain DEFAULT_DESCRIPTION
        defaultDiscountCodeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the discountCodeList where description does not contain UPDATED_DESCRIPTION
        defaultDiscountCodeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountType equals to DEFAULT_DISCOUNT_TYPE
        defaultDiscountCodeShouldBeFound("discountType.equals=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the discountCodeList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultDiscountCodeShouldNotBeFound("discountType.equals=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountType in DEFAULT_DISCOUNT_TYPE or UPDATED_DISCOUNT_TYPE
        defaultDiscountCodeShouldBeFound("discountType.in=" + DEFAULT_DISCOUNT_TYPE + "," + UPDATED_DISCOUNT_TYPE);

        // Get all the discountCodeList where discountType equals to UPDATED_DISCOUNT_TYPE
        defaultDiscountCodeShouldNotBeFound("discountType.in=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountType is not null
        defaultDiscountCodeShouldBeFound("discountType.specified=true");

        // Get all the discountCodeList where discountType is null
        defaultDiscountCodeShouldNotBeFound("discountType.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountTypeContainsSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountType contains DEFAULT_DISCOUNT_TYPE
        defaultDiscountCodeShouldBeFound("discountType.contains=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the discountCodeList where discountType contains UPDATED_DISCOUNT_TYPE
        defaultDiscountCodeShouldNotBeFound("discountType.contains=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountTypeNotContainsSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountType does not contain DEFAULT_DISCOUNT_TYPE
        defaultDiscountCodeShouldNotBeFound("discountType.doesNotContain=" + DEFAULT_DISCOUNT_TYPE);

        // Get all the discountCodeList where discountType does not contain UPDATED_DISCOUNT_TYPE
        defaultDiscountCodeShouldBeFound("discountType.doesNotContain=" + UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue equals to DEFAULT_DISCOUNT_VALUE
        defaultDiscountCodeShouldBeFound("discountValue.equals=" + DEFAULT_DISCOUNT_VALUE);

        // Get all the discountCodeList where discountValue equals to UPDATED_DISCOUNT_VALUE
        defaultDiscountCodeShouldNotBeFound("discountValue.equals=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue in DEFAULT_DISCOUNT_VALUE or UPDATED_DISCOUNT_VALUE
        defaultDiscountCodeShouldBeFound("discountValue.in=" + DEFAULT_DISCOUNT_VALUE + "," + UPDATED_DISCOUNT_VALUE);

        // Get all the discountCodeList where discountValue equals to UPDATED_DISCOUNT_VALUE
        defaultDiscountCodeShouldNotBeFound("discountValue.in=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue is not null
        defaultDiscountCodeShouldBeFound("discountValue.specified=true");

        // Get all the discountCodeList where discountValue is null
        defaultDiscountCodeShouldNotBeFound("discountValue.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue is greater than or equal to DEFAULT_DISCOUNT_VALUE
        defaultDiscountCodeShouldBeFound("discountValue.greaterThanOrEqual=" + DEFAULT_DISCOUNT_VALUE);

        // Get all the discountCodeList where discountValue is greater than or equal to UPDATED_DISCOUNT_VALUE
        defaultDiscountCodeShouldNotBeFound("discountValue.greaterThanOrEqual=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue is less than or equal to DEFAULT_DISCOUNT_VALUE
        defaultDiscountCodeShouldBeFound("discountValue.lessThanOrEqual=" + DEFAULT_DISCOUNT_VALUE);

        // Get all the discountCodeList where discountValue is less than or equal to SMALLER_DISCOUNT_VALUE
        defaultDiscountCodeShouldNotBeFound("discountValue.lessThanOrEqual=" + SMALLER_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue is less than DEFAULT_DISCOUNT_VALUE
        defaultDiscountCodeShouldNotBeFound("discountValue.lessThan=" + DEFAULT_DISCOUNT_VALUE);

        // Get all the discountCodeList where discountValue is less than UPDATED_DISCOUNT_VALUE
        defaultDiscountCodeShouldBeFound("discountValue.lessThan=" + UPDATED_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByDiscountValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where discountValue is greater than DEFAULT_DISCOUNT_VALUE
        defaultDiscountCodeShouldNotBeFound("discountValue.greaterThan=" + DEFAULT_DISCOUNT_VALUE);

        // Get all the discountCodeList where discountValue is greater than SMALLER_DISCOUNT_VALUE
        defaultDiscountCodeShouldBeFound("discountValue.greaterThan=" + SMALLER_DISCOUNT_VALUE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses equals to DEFAULT_MAX_USES
        defaultDiscountCodeShouldBeFound("maxUses.equals=" + DEFAULT_MAX_USES);

        // Get all the discountCodeList where maxUses equals to UPDATED_MAX_USES
        defaultDiscountCodeShouldNotBeFound("maxUses.equals=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses in DEFAULT_MAX_USES or UPDATED_MAX_USES
        defaultDiscountCodeShouldBeFound("maxUses.in=" + DEFAULT_MAX_USES + "," + UPDATED_MAX_USES);

        // Get all the discountCodeList where maxUses equals to UPDATED_MAX_USES
        defaultDiscountCodeShouldNotBeFound("maxUses.in=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses is not null
        defaultDiscountCodeShouldBeFound("maxUses.specified=true");

        // Get all the discountCodeList where maxUses is null
        defaultDiscountCodeShouldNotBeFound("maxUses.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses is greater than or equal to DEFAULT_MAX_USES
        defaultDiscountCodeShouldBeFound("maxUses.greaterThanOrEqual=" + DEFAULT_MAX_USES);

        // Get all the discountCodeList where maxUses is greater than or equal to UPDATED_MAX_USES
        defaultDiscountCodeShouldNotBeFound("maxUses.greaterThanOrEqual=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses is less than or equal to DEFAULT_MAX_USES
        defaultDiscountCodeShouldBeFound("maxUses.lessThanOrEqual=" + DEFAULT_MAX_USES);

        // Get all the discountCodeList where maxUses is less than or equal to SMALLER_MAX_USES
        defaultDiscountCodeShouldNotBeFound("maxUses.lessThanOrEqual=" + SMALLER_MAX_USES);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses is less than DEFAULT_MAX_USES
        defaultDiscountCodeShouldNotBeFound("maxUses.lessThan=" + DEFAULT_MAX_USES);

        // Get all the discountCodeList where maxUses is less than UPDATED_MAX_USES
        defaultDiscountCodeShouldBeFound("maxUses.lessThan=" + UPDATED_MAX_USES);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByMaxUsesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where maxUses is greater than DEFAULT_MAX_USES
        defaultDiscountCodeShouldNotBeFound("maxUses.greaterThan=" + DEFAULT_MAX_USES);

        // Get all the discountCodeList where maxUses is greater than SMALLER_MAX_USES
        defaultDiscountCodeShouldBeFound("maxUses.greaterThan=" + SMALLER_MAX_USES);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount equals to DEFAULT_USES_COUNT
        defaultDiscountCodeShouldBeFound("usesCount.equals=" + DEFAULT_USES_COUNT);

        // Get all the discountCodeList where usesCount equals to UPDATED_USES_COUNT
        defaultDiscountCodeShouldNotBeFound("usesCount.equals=" + UPDATED_USES_COUNT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount in DEFAULT_USES_COUNT or UPDATED_USES_COUNT
        defaultDiscountCodeShouldBeFound("usesCount.in=" + DEFAULT_USES_COUNT + "," + UPDATED_USES_COUNT);

        // Get all the discountCodeList where usesCount equals to UPDATED_USES_COUNT
        defaultDiscountCodeShouldNotBeFound("usesCount.in=" + UPDATED_USES_COUNT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount is not null
        defaultDiscountCodeShouldBeFound("usesCount.specified=true");

        // Get all the discountCodeList where usesCount is null
        defaultDiscountCodeShouldNotBeFound("usesCount.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount is greater than or equal to DEFAULT_USES_COUNT
        defaultDiscountCodeShouldBeFound("usesCount.greaterThanOrEqual=" + DEFAULT_USES_COUNT);

        // Get all the discountCodeList where usesCount is greater than or equal to UPDATED_USES_COUNT
        defaultDiscountCodeShouldNotBeFound("usesCount.greaterThanOrEqual=" + UPDATED_USES_COUNT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount is less than or equal to DEFAULT_USES_COUNT
        defaultDiscountCodeShouldBeFound("usesCount.lessThanOrEqual=" + DEFAULT_USES_COUNT);

        // Get all the discountCodeList where usesCount is less than or equal to SMALLER_USES_COUNT
        defaultDiscountCodeShouldNotBeFound("usesCount.lessThanOrEqual=" + SMALLER_USES_COUNT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount is less than DEFAULT_USES_COUNT
        defaultDiscountCodeShouldNotBeFound("usesCount.lessThan=" + DEFAULT_USES_COUNT);

        // Get all the discountCodeList where usesCount is less than UPDATED_USES_COUNT
        defaultDiscountCodeShouldBeFound("usesCount.lessThan=" + UPDATED_USES_COUNT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUsesCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where usesCount is greater than DEFAULT_USES_COUNT
        defaultDiscountCodeShouldNotBeFound("usesCount.greaterThan=" + DEFAULT_USES_COUNT);

        // Get all the discountCodeList where usesCount is greater than SMALLER_USES_COUNT
        defaultDiscountCodeShouldBeFound("usesCount.greaterThan=" + SMALLER_USES_COUNT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom equals to DEFAULT_VALID_FROM
        defaultDiscountCodeShouldBeFound("validFrom.equals=" + DEFAULT_VALID_FROM);

        // Get all the discountCodeList where validFrom equals to UPDATED_VALID_FROM
        defaultDiscountCodeShouldNotBeFound("validFrom.equals=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom in DEFAULT_VALID_FROM or UPDATED_VALID_FROM
        defaultDiscountCodeShouldBeFound("validFrom.in=" + DEFAULT_VALID_FROM + "," + UPDATED_VALID_FROM);

        // Get all the discountCodeList where validFrom equals to UPDATED_VALID_FROM
        defaultDiscountCodeShouldNotBeFound("validFrom.in=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom is not null
        defaultDiscountCodeShouldBeFound("validFrom.specified=true");

        // Get all the discountCodeList where validFrom is null
        defaultDiscountCodeShouldNotBeFound("validFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom is greater than or equal to DEFAULT_VALID_FROM
        defaultDiscountCodeShouldBeFound("validFrom.greaterThanOrEqual=" + DEFAULT_VALID_FROM);

        // Get all the discountCodeList where validFrom is greater than or equal to UPDATED_VALID_FROM
        defaultDiscountCodeShouldNotBeFound("validFrom.greaterThanOrEqual=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom is less than or equal to DEFAULT_VALID_FROM
        defaultDiscountCodeShouldBeFound("validFrom.lessThanOrEqual=" + DEFAULT_VALID_FROM);

        // Get all the discountCodeList where validFrom is less than or equal to SMALLER_VALID_FROM
        defaultDiscountCodeShouldNotBeFound("validFrom.lessThanOrEqual=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom is less than DEFAULT_VALID_FROM
        defaultDiscountCodeShouldNotBeFound("validFrom.lessThan=" + DEFAULT_VALID_FROM);

        // Get all the discountCodeList where validFrom is less than UPDATED_VALID_FROM
        defaultDiscountCodeShouldBeFound("validFrom.lessThan=" + UPDATED_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validFrom is greater than DEFAULT_VALID_FROM
        defaultDiscountCodeShouldNotBeFound("validFrom.greaterThan=" + DEFAULT_VALID_FROM);

        // Get all the discountCodeList where validFrom is greater than SMALLER_VALID_FROM
        defaultDiscountCodeShouldBeFound("validFrom.greaterThan=" + SMALLER_VALID_FROM);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo equals to DEFAULT_VALID_TO
        defaultDiscountCodeShouldBeFound("validTo.equals=" + DEFAULT_VALID_TO);

        // Get all the discountCodeList where validTo equals to UPDATED_VALID_TO
        defaultDiscountCodeShouldNotBeFound("validTo.equals=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo in DEFAULT_VALID_TO or UPDATED_VALID_TO
        defaultDiscountCodeShouldBeFound("validTo.in=" + DEFAULT_VALID_TO + "," + UPDATED_VALID_TO);

        // Get all the discountCodeList where validTo equals to UPDATED_VALID_TO
        defaultDiscountCodeShouldNotBeFound("validTo.in=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo is not null
        defaultDiscountCodeShouldBeFound("validTo.specified=true");

        // Get all the discountCodeList where validTo is null
        defaultDiscountCodeShouldNotBeFound("validTo.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo is greater than or equal to DEFAULT_VALID_TO
        defaultDiscountCodeShouldBeFound("validTo.greaterThanOrEqual=" + DEFAULT_VALID_TO);

        // Get all the discountCodeList where validTo is greater than or equal to UPDATED_VALID_TO
        defaultDiscountCodeShouldNotBeFound("validTo.greaterThanOrEqual=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo is less than or equal to DEFAULT_VALID_TO
        defaultDiscountCodeShouldBeFound("validTo.lessThanOrEqual=" + DEFAULT_VALID_TO);

        // Get all the discountCodeList where validTo is less than or equal to SMALLER_VALID_TO
        defaultDiscountCodeShouldNotBeFound("validTo.lessThanOrEqual=" + SMALLER_VALID_TO);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo is less than DEFAULT_VALID_TO
        defaultDiscountCodeShouldNotBeFound("validTo.lessThan=" + DEFAULT_VALID_TO);

        // Get all the discountCodeList where validTo is less than UPDATED_VALID_TO
        defaultDiscountCodeShouldBeFound("validTo.lessThan=" + UPDATED_VALID_TO);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByValidToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where validTo is greater than DEFAULT_VALID_TO
        defaultDiscountCodeShouldNotBeFound("validTo.greaterThan=" + DEFAULT_VALID_TO);

        // Get all the discountCodeList where validTo is greater than SMALLER_VALID_TO
        defaultDiscountCodeShouldBeFound("validTo.greaterThan=" + SMALLER_VALID_TO);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultDiscountCodeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the discountCodeList where isActive equals to UPDATED_IS_ACTIVE
        defaultDiscountCodeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultDiscountCodeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the discountCodeList where isActive equals to UPDATED_IS_ACTIVE
        defaultDiscountCodeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where isActive is not null
        defaultDiscountCodeShouldBeFound("isActive.specified=true");

        // Get all the discountCodeList where isActive is null
        defaultDiscountCodeShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt equals to DEFAULT_CREATED_AT
        defaultDiscountCodeShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the discountCodeList where createdAt equals to UPDATED_CREATED_AT
        defaultDiscountCodeShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultDiscountCodeShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the discountCodeList where createdAt equals to UPDATED_CREATED_AT
        defaultDiscountCodeShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt is not null
        defaultDiscountCodeShouldBeFound("createdAt.specified=true");

        // Get all the discountCodeList where createdAt is null
        defaultDiscountCodeShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultDiscountCodeShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the discountCodeList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultDiscountCodeShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultDiscountCodeShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the discountCodeList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultDiscountCodeShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt is less than DEFAULT_CREATED_AT
        defaultDiscountCodeShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the discountCodeList where createdAt is less than UPDATED_CREATED_AT
        defaultDiscountCodeShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where createdAt is greater than DEFAULT_CREATED_AT
        defaultDiscountCodeShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the discountCodeList where createdAt is greater than SMALLER_CREATED_AT
        defaultDiscountCodeShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultDiscountCodeShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the discountCodeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultDiscountCodeShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultDiscountCodeShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the discountCodeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultDiscountCodeShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt is not null
        defaultDiscountCodeShouldBeFound("updatedAt.specified=true");

        // Get all the discountCodeList where updatedAt is null
        defaultDiscountCodeShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultDiscountCodeShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the discountCodeList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultDiscountCodeShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultDiscountCodeShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the discountCodeList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultDiscountCodeShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultDiscountCodeShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the discountCodeList where updatedAt is less than UPDATED_UPDATED_AT
        defaultDiscountCodeShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllDiscountCodesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        // Get all the discountCodeList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultDiscountCodeShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the discountCodeList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultDiscountCodeShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /*  @Test
    @Transactional
    void getAllDiscountCodesByEventsIsEqualToSomething() throws Exception {
        EventDetails events;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            discountCodeRepository.saveAndFlush(discountCode);
            events = EventDetailsResourceIT.createEntity(em);
        } else {
            events = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(events);
        em.flush();
        discountCode.addEvents(events);
        discountCodeRepository.saveAndFlush(discountCode);
        Long eventsId = events.getId();
        // Get all the discountCodeList where events equals to eventsId
        defaultDiscountCodeShouldBeFound("eventsId.equals=" + eventsId);

        // Get all the discountCodeList where events equals to (eventsId + 1)
        defaultDiscountCodeShouldNotBeFound("eventsId.equals=" + (eventsId + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiscountCodeShouldBeFound(String filter) throws Exception {
        restDiscountCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE)))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(sameNumber(DEFAULT_DISCOUNT_VALUE))))
            .andExpect(jsonPath("$.[*].maxUses").value(hasItem(DEFAULT_MAX_USES)))
            .andExpect(jsonPath("$.[*].usesCount").value(hasItem(DEFAULT_USES_COUNT)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(sameInstant(DEFAULT_VALID_FROM))))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(sameInstant(DEFAULT_VALID_TO))))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restDiscountCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiscountCodeShouldNotBeFound(String filter) throws Exception {
        restDiscountCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscountCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDiscountCode() throws Exception {
        // Get the discountCode
        restDiscountCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiscountCode() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();

        // Update the discountCode
        DiscountCode updatedDiscountCode = discountCodeRepository.findById(discountCode.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDiscountCode are not directly saved in db
        em.detach(updatedDiscountCode);
        updatedDiscountCode
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .maxUses(UPDATED_MAX_USES)
            .usesCount(UPDATED_USES_COUNT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(updatedDiscountCode);

        restDiscountCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, discountCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
        DiscountCode testDiscountCode = discountCodeList.get(discountCodeList.size() - 1);
        assertThat(testDiscountCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDiscountCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscountCode.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDiscountCode.getDiscountValue()).isEqualByComparingTo(UPDATED_DISCOUNT_VALUE);
        assertThat(testDiscountCode.getMaxUses()).isEqualTo(UPDATED_MAX_USES);
        assertThat(testDiscountCode.getUsesCount()).isEqualTo(UPDATED_USES_COUNT);
        assertThat(testDiscountCode.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testDiscountCode.getValidTo()).isEqualTo(UPDATED_VALID_TO);
        assertThat(testDiscountCode.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testDiscountCode.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDiscountCode.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();
        discountCode.setId(longCount.incrementAndGet());

        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, discountCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();
        discountCode.setId(longCount.incrementAndGet());

        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();
        discountCode.setId(longCount.incrementAndGet());

        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiscountCodeWithPatch() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();

        // Update the discountCode using partial update
        DiscountCode partialUpdatedDiscountCode = new DiscountCode();
        partialUpdatedDiscountCode.setId(discountCode.getId());

        partialUpdatedDiscountCode.description(UPDATED_DESCRIPTION).discountValue(UPDATED_DISCOUNT_VALUE).validFrom(UPDATED_VALID_FROM);

        restDiscountCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscountCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiscountCode))
            )
            .andExpect(status().isOk());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
        DiscountCode testDiscountCode = discountCodeList.get(discountCodeList.size() - 1);
        assertThat(testDiscountCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDiscountCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscountCode.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testDiscountCode.getDiscountValue()).isEqualByComparingTo(UPDATED_DISCOUNT_VALUE);
        assertThat(testDiscountCode.getMaxUses()).isEqualTo(DEFAULT_MAX_USES);
        assertThat(testDiscountCode.getUsesCount()).isEqualTo(DEFAULT_USES_COUNT);
        assertThat(testDiscountCode.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testDiscountCode.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
        assertThat(testDiscountCode.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testDiscountCode.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDiscountCode.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateDiscountCodeWithPatch() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();

        // Update the discountCode using partial update
        DiscountCode partialUpdatedDiscountCode = new DiscountCode();
        partialUpdatedDiscountCode.setId(discountCode.getId());

        partialUpdatedDiscountCode
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .maxUses(UPDATED_MAX_USES)
            .usesCount(UPDATED_USES_COUNT)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restDiscountCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscountCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiscountCode))
            )
            .andExpect(status().isOk());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
        DiscountCode testDiscountCode = discountCodeList.get(discountCodeList.size() - 1);
        assertThat(testDiscountCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDiscountCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscountCode.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testDiscountCode.getDiscountValue()).isEqualByComparingTo(UPDATED_DISCOUNT_VALUE);
        assertThat(testDiscountCode.getMaxUses()).isEqualTo(UPDATED_MAX_USES);
        assertThat(testDiscountCode.getUsesCount()).isEqualTo(UPDATED_USES_COUNT);
        assertThat(testDiscountCode.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testDiscountCode.getValidTo()).isEqualTo(UPDATED_VALID_TO);
        assertThat(testDiscountCode.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testDiscountCode.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDiscountCode.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();
        discountCode.setId(longCount.incrementAndGet());

        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, discountCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();
        discountCode.setId(longCount.incrementAndGet());

        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiscountCode() throws Exception {
        int databaseSizeBeforeUpdate = discountCodeRepository.findAll().size();
        discountCode.setId(longCount.incrementAndGet());

        // Create the DiscountCode
        DiscountCodeDTO discountCodeDTO = discountCodeMapper.toDto(discountCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discountCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DiscountCode in the database
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiscountCode() throws Exception {
        // Initialize the database
        discountCodeRepository.saveAndFlush(discountCode);

        int databaseSizeBeforeDelete = discountCodeRepository.findAll().size();

        // Delete the discountCode
        restDiscountCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, discountCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        assertThat(discountCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
