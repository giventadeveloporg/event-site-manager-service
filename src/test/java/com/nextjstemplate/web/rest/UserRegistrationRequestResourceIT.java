package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserRegistrationRequest;
import com.nextjstemplate.repository.UserRegistrationRequestRepository;
import com.nextjstemplate.service.dto.UserRegistrationRequestDTO;
import com.nextjstemplate.service.mapper.UserRegistrationRequestMapper;
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
 * Integration tests for the {@link UserRegistrationRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRegistrationRequestResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_TOWN = "AAAAAAAAAA";
    private static final String UPDATED_CITY_TOWN = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_EDUCATIONAL_INSTITUTION = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATIONAL_INSTITUTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_ADMIN_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ADMIN_COMMENTS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SUBMITTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMITTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SUBMITTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_REVIEWED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REVIEWED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REVIEWED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_REJECTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REJECTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REJECTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/user-registration-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserRegistrationRequestRepository userRegistrationRequestRepository;

    @Autowired
    private UserRegistrationRequestMapper userRegistrationRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRegistrationRequestMockMvc;

    private UserRegistrationRequest userRegistrationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRegistrationRequest createEntity(EntityManager em) {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest()
            .tenantId(DEFAULT_TENANT_ID)
            .requestId(DEFAULT_REQUEST_ID)
            .userId(DEFAULT_USER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .familyName(DEFAULT_FAMILY_NAME)
            .cityTown(DEFAULT_CITY_TOWN)
            .district(DEFAULT_DISTRICT)
            .educationalInstitution(DEFAULT_EDUCATIONAL_INSTITUTION)
            .profileImageUrl(DEFAULT_PROFILE_IMAGE_URL)
            .requestReason(DEFAULT_REQUEST_REASON)
            .status(DEFAULT_STATUS)
            .adminComments(DEFAULT_ADMIN_COMMENTS)
            .submittedAt(DEFAULT_SUBMITTED_AT)
            .reviewedAt(DEFAULT_REVIEWED_AT)
            .approvedAt(DEFAULT_APPROVED_AT)
            .rejectedAt(DEFAULT_REJECTED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return userRegistrationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRegistrationRequest createUpdatedEntity(EntityManager em) {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest()
            .tenantId(UPDATED_TENANT_ID)
            .requestId(UPDATED_REQUEST_ID)
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .familyName(UPDATED_FAMILY_NAME)
            .cityTown(UPDATED_CITY_TOWN)
            .district(UPDATED_DISTRICT)
            .educationalInstitution(UPDATED_EDUCATIONAL_INSTITUTION)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .requestReason(UPDATED_REQUEST_REASON)
            .status(UPDATED_STATUS)
            .adminComments(UPDATED_ADMIN_COMMENTS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return userRegistrationRequest;
    }

    @BeforeEach
    public void initTest() {
        userRegistrationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeCreate = userRegistrationRequestRepository.findAll().size();
        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);
        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeCreate + 1);
        UserRegistrationRequest testUserRegistrationRequest = userRegistrationRequestList.get(userRegistrationRequestList.size() - 1);
        assertThat(testUserRegistrationRequest.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testUserRegistrationRequest.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testUserRegistrationRequest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserRegistrationRequest.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserRegistrationRequest.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserRegistrationRequest.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserRegistrationRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserRegistrationRequest.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testUserRegistrationRequest.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testUserRegistrationRequest.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserRegistrationRequest.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserRegistrationRequest.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testUserRegistrationRequest.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserRegistrationRequest.getFamilyName()).isEqualTo(DEFAULT_FAMILY_NAME);
        assertThat(testUserRegistrationRequest.getCityTown()).isEqualTo(DEFAULT_CITY_TOWN);
        assertThat(testUserRegistrationRequest.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testUserRegistrationRequest.getEducationalInstitution()).isEqualTo(DEFAULT_EDUCATIONAL_INSTITUTION);
        assertThat(testUserRegistrationRequest.getProfileImageUrl()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
        assertThat(testUserRegistrationRequest.getRequestReason()).isEqualTo(DEFAULT_REQUEST_REASON);
        assertThat(testUserRegistrationRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserRegistrationRequest.getAdminComments()).isEqualTo(DEFAULT_ADMIN_COMMENTS);
        assertThat(testUserRegistrationRequest.getSubmittedAt()).isEqualTo(DEFAULT_SUBMITTED_AT);
        assertThat(testUserRegistrationRequest.getReviewedAt()).isEqualTo(DEFAULT_REVIEWED_AT);
        assertThat(testUserRegistrationRequest.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testUserRegistrationRequest.getRejectedAt()).isEqualTo(DEFAULT_REJECTED_AT);
        assertThat(testUserRegistrationRequest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserRegistrationRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUserRegistrationRequestWithExistingId() throws Exception {
        // Create the UserRegistrationRequest with an existing ID
        userRegistrationRequest.setId(1L);
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        int databaseSizeBeforeCreate = userRegistrationRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenantIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setTenantId(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setRequestId(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setUserId(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setEmail(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setStatus(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmittedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setSubmittedAt(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setCreatedAt(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRegistrationRequestRepository.findAll().size();
        // set the field null
        userRegistrationRequest.setUpdatedAt(null);

        // Create the UserRegistrationRequest, which fails.
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequests() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList
        restUserRegistrationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRegistrationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].cityTown").value(hasItem(DEFAULT_CITY_TOWN)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].educationalInstitution").value(hasItem(DEFAULT_EDUCATIONAL_INSTITUTION)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].requestReason").value(hasItem(DEFAULT_REQUEST_REASON)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].adminComments").value(hasItem(DEFAULT_ADMIN_COMMENTS)))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(sameInstant(DEFAULT_SUBMITTED_AT))))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(sameInstant(DEFAULT_REVIEWED_AT))))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].rejectedAt").value(hasItem(sameInstant(DEFAULT_REJECTED_AT))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getUserRegistrationRequest() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get the userRegistrationRequest
        restUserRegistrationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, userRegistrationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRegistrationRequest.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.requestId").value(DEFAULT_REQUEST_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.familyName").value(DEFAULT_FAMILY_NAME))
            .andExpect(jsonPath("$.cityTown").value(DEFAULT_CITY_TOWN))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.educationalInstitution").value(DEFAULT_EDUCATIONAL_INSTITUTION))
            .andExpect(jsonPath("$.profileImageUrl").value(DEFAULT_PROFILE_IMAGE_URL))
            .andExpect(jsonPath("$.requestReason").value(DEFAULT_REQUEST_REASON))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.adminComments").value(DEFAULT_ADMIN_COMMENTS))
            .andExpect(jsonPath("$.submittedAt").value(sameInstant(DEFAULT_SUBMITTED_AT)))
            .andExpect(jsonPath("$.reviewedAt").value(sameInstant(DEFAULT_REVIEWED_AT)))
            .andExpect(jsonPath("$.approvedAt").value(sameInstant(DEFAULT_APPROVED_AT)))
            .andExpect(jsonPath("$.rejectedAt").value(sameInstant(DEFAULT_REJECTED_AT)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getUserRegistrationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        Long id = userRegistrationRequest.getId();

        defaultUserRegistrationRequestShouldBeFound("id.equals=" + id);
        defaultUserRegistrationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultUserRegistrationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserRegistrationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultUserRegistrationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserRegistrationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where tenantId equals to DEFAULT_TENANT_ID
        defaultUserRegistrationRequestShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the userRegistrationRequestList where tenantId equals to UPDATED_TENANT_ID
        defaultUserRegistrationRequestShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultUserRegistrationRequestShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the userRegistrationRequestList where tenantId equals to UPDATED_TENANT_ID
        defaultUserRegistrationRequestShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where tenantId is not null
        defaultUserRegistrationRequestShouldBeFound("tenantId.specified=true");

        // Get all the userRegistrationRequestList where tenantId is null
        defaultUserRegistrationRequestShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where tenantId contains DEFAULT_TENANT_ID
        defaultUserRegistrationRequestShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the userRegistrationRequestList where tenantId contains UPDATED_TENANT_ID
        defaultUserRegistrationRequestShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where tenantId does not contain DEFAULT_TENANT_ID
        defaultUserRegistrationRequestShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the userRegistrationRequestList where tenantId does not contain UPDATED_TENANT_ID
        defaultUserRegistrationRequestShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestId equals to DEFAULT_REQUEST_ID
        defaultUserRegistrationRequestShouldBeFound("requestId.equals=" + DEFAULT_REQUEST_ID);

        // Get all the userRegistrationRequestList where requestId equals to UPDATED_REQUEST_ID
        defaultUserRegistrationRequestShouldNotBeFound("requestId.equals=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestId in DEFAULT_REQUEST_ID or UPDATED_REQUEST_ID
        defaultUserRegistrationRequestShouldBeFound("requestId.in=" + DEFAULT_REQUEST_ID + "," + UPDATED_REQUEST_ID);

        // Get all the userRegistrationRequestList where requestId equals to UPDATED_REQUEST_ID
        defaultUserRegistrationRequestShouldNotBeFound("requestId.in=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestId is not null
        defaultUserRegistrationRequestShouldBeFound("requestId.specified=true");

        // Get all the userRegistrationRequestList where requestId is null
        defaultUserRegistrationRequestShouldNotBeFound("requestId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestIdContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestId contains DEFAULT_REQUEST_ID
        defaultUserRegistrationRequestShouldBeFound("requestId.contains=" + DEFAULT_REQUEST_ID);

        // Get all the userRegistrationRequestList where requestId contains UPDATED_REQUEST_ID
        defaultUserRegistrationRequestShouldNotBeFound("requestId.contains=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestIdNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestId does not contain DEFAULT_REQUEST_ID
        defaultUserRegistrationRequestShouldNotBeFound("requestId.doesNotContain=" + DEFAULT_REQUEST_ID);

        // Get all the userRegistrationRequestList where requestId does not contain UPDATED_REQUEST_ID
        defaultUserRegistrationRequestShouldBeFound("requestId.doesNotContain=" + UPDATED_REQUEST_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where userId equals to DEFAULT_USER_ID
        defaultUserRegistrationRequestShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userRegistrationRequestList where userId equals to UPDATED_USER_ID
        defaultUserRegistrationRequestShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserRegistrationRequestShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userRegistrationRequestList where userId equals to UPDATED_USER_ID
        defaultUserRegistrationRequestShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where userId is not null
        defaultUserRegistrationRequestShouldBeFound("userId.specified=true");

        // Get all the userRegistrationRequestList where userId is null
        defaultUserRegistrationRequestShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where userId contains DEFAULT_USER_ID
        defaultUserRegistrationRequestShouldBeFound("userId.contains=" + DEFAULT_USER_ID);

        // Get all the userRegistrationRequestList where userId contains UPDATED_USER_ID
        defaultUserRegistrationRequestShouldNotBeFound("userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where userId does not contain DEFAULT_USER_ID
        defaultUserRegistrationRequestShouldNotBeFound("userId.doesNotContain=" + DEFAULT_USER_ID);

        // Get all the userRegistrationRequestList where userId does not contain UPDATED_USER_ID
        defaultUserRegistrationRequestShouldBeFound("userId.doesNotContain=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where firstName equals to DEFAULT_FIRST_NAME
        defaultUserRegistrationRequestShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the userRegistrationRequestList where firstName equals to UPDATED_FIRST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUserRegistrationRequestShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the userRegistrationRequestList where firstName equals to UPDATED_FIRST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where firstName is not null
        defaultUserRegistrationRequestShouldBeFound("firstName.specified=true");

        // Get all the userRegistrationRequestList where firstName is null
        defaultUserRegistrationRequestShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where firstName contains DEFAULT_FIRST_NAME
        defaultUserRegistrationRequestShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the userRegistrationRequestList where firstName contains UPDATED_FIRST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the userRegistrationRequestList where firstName does not contain UPDATED_FIRST_NAME
        defaultUserRegistrationRequestShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where lastName equals to DEFAULT_LAST_NAME
        defaultUserRegistrationRequestShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the userRegistrationRequestList where lastName equals to UPDATED_LAST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUserRegistrationRequestShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the userRegistrationRequestList where lastName equals to UPDATED_LAST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where lastName is not null
        defaultUserRegistrationRequestShouldBeFound("lastName.specified=true");

        // Get all the userRegistrationRequestList where lastName is null
        defaultUserRegistrationRequestShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where lastName contains DEFAULT_LAST_NAME
        defaultUserRegistrationRequestShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the userRegistrationRequestList where lastName contains UPDATED_LAST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where lastName does not contain DEFAULT_LAST_NAME
        defaultUserRegistrationRequestShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the userRegistrationRequestList where lastName does not contain UPDATED_LAST_NAME
        defaultUserRegistrationRequestShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where email equals to DEFAULT_EMAIL
        defaultUserRegistrationRequestShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the userRegistrationRequestList where email equals to UPDATED_EMAIL
        defaultUserRegistrationRequestShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUserRegistrationRequestShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the userRegistrationRequestList where email equals to UPDATED_EMAIL
        defaultUserRegistrationRequestShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where email is not null
        defaultUserRegistrationRequestShouldBeFound("email.specified=true");

        // Get all the userRegistrationRequestList where email is null
        defaultUserRegistrationRequestShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEmailContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where email contains DEFAULT_EMAIL
        defaultUserRegistrationRequestShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the userRegistrationRequestList where email contains UPDATED_EMAIL
        defaultUserRegistrationRequestShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where email does not contain DEFAULT_EMAIL
        defaultUserRegistrationRequestShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the userRegistrationRequestList where email does not contain UPDATED_EMAIL
        defaultUserRegistrationRequestShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where phone equals to DEFAULT_PHONE
        defaultUserRegistrationRequestShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the userRegistrationRequestList where phone equals to UPDATED_PHONE
        defaultUserRegistrationRequestShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUserRegistrationRequestShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the userRegistrationRequestList where phone equals to UPDATED_PHONE
        defaultUserRegistrationRequestShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where phone is not null
        defaultUserRegistrationRequestShouldBeFound("phone.specified=true");

        // Get all the userRegistrationRequestList where phone is null
        defaultUserRegistrationRequestShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where phone contains DEFAULT_PHONE
        defaultUserRegistrationRequestShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the userRegistrationRequestList where phone contains UPDATED_PHONE
        defaultUserRegistrationRequestShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where phone does not contain DEFAULT_PHONE
        defaultUserRegistrationRequestShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the userRegistrationRequestList where phone does not contain UPDATED_PHONE
        defaultUserRegistrationRequestShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userRegistrationRequestList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the userRegistrationRequestList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine1 is not null
        defaultUserRegistrationRequestShouldBeFound("addressLine1.specified=true");

        // Get all the userRegistrationRequestList where addressLine1 is null
        defaultUserRegistrationRequestShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userRegistrationRequestList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userRegistrationRequestList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultUserRegistrationRequestShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userRegistrationRequestList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the userRegistrationRequestList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine2 is not null
        defaultUserRegistrationRequestShouldBeFound("addressLine2.specified=true");

        // Get all the userRegistrationRequestList where addressLine2 is null
        defaultUserRegistrationRequestShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userRegistrationRequestList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userRegistrationRequestList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultUserRegistrationRequestShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where city equals to DEFAULT_CITY
        defaultUserRegistrationRequestShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userRegistrationRequestList where city equals to UPDATED_CITY
        defaultUserRegistrationRequestShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserRegistrationRequestShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userRegistrationRequestList where city equals to UPDATED_CITY
        defaultUserRegistrationRequestShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where city is not null
        defaultUserRegistrationRequestShouldBeFound("city.specified=true");

        // Get all the userRegistrationRequestList where city is null
        defaultUserRegistrationRequestShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where city contains DEFAULT_CITY
        defaultUserRegistrationRequestShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the userRegistrationRequestList where city contains UPDATED_CITY
        defaultUserRegistrationRequestShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where city does not contain DEFAULT_CITY
        defaultUserRegistrationRequestShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the userRegistrationRequestList where city does not contain UPDATED_CITY
        defaultUserRegistrationRequestShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where state equals to DEFAULT_STATE
        defaultUserRegistrationRequestShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the userRegistrationRequestList where state equals to UPDATED_STATE
        defaultUserRegistrationRequestShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where state in DEFAULT_STATE or UPDATED_STATE
        defaultUserRegistrationRequestShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the userRegistrationRequestList where state equals to UPDATED_STATE
        defaultUserRegistrationRequestShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where state is not null
        defaultUserRegistrationRequestShouldBeFound("state.specified=true");

        // Get all the userRegistrationRequestList where state is null
        defaultUserRegistrationRequestShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStateContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where state contains DEFAULT_STATE
        defaultUserRegistrationRequestShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the userRegistrationRequestList where state contains UPDATED_STATE
        defaultUserRegistrationRequestShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where state does not contain DEFAULT_STATE
        defaultUserRegistrationRequestShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the userRegistrationRequestList where state does not contain UPDATED_STATE
        defaultUserRegistrationRequestShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where zipCode equals to DEFAULT_ZIP_CODE
        defaultUserRegistrationRequestShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the userRegistrationRequestList where zipCode equals to UPDATED_ZIP_CODE
        defaultUserRegistrationRequestShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultUserRegistrationRequestShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the userRegistrationRequestList where zipCode equals to UPDATED_ZIP_CODE
        defaultUserRegistrationRequestShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where zipCode is not null
        defaultUserRegistrationRequestShouldBeFound("zipCode.specified=true");

        // Get all the userRegistrationRequestList where zipCode is null
        defaultUserRegistrationRequestShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where zipCode contains DEFAULT_ZIP_CODE
        defaultUserRegistrationRequestShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the userRegistrationRequestList where zipCode contains UPDATED_ZIP_CODE
        defaultUserRegistrationRequestShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultUserRegistrationRequestShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the userRegistrationRequestList where zipCode does not contain UPDATED_ZIP_CODE
        defaultUserRegistrationRequestShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where country equals to DEFAULT_COUNTRY
        defaultUserRegistrationRequestShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the userRegistrationRequestList where country equals to UPDATED_COUNTRY
        defaultUserRegistrationRequestShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultUserRegistrationRequestShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the userRegistrationRequestList where country equals to UPDATED_COUNTRY
        defaultUserRegistrationRequestShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where country is not null
        defaultUserRegistrationRequestShouldBeFound("country.specified=true");

        // Get all the userRegistrationRequestList where country is null
        defaultUserRegistrationRequestShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCountryContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where country contains DEFAULT_COUNTRY
        defaultUserRegistrationRequestShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the userRegistrationRequestList where country contains UPDATED_COUNTRY
        defaultUserRegistrationRequestShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where country does not contain DEFAULT_COUNTRY
        defaultUserRegistrationRequestShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the userRegistrationRequestList where country does not contain UPDATED_COUNTRY
        defaultUserRegistrationRequestShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFamilyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where familyName equals to DEFAULT_FAMILY_NAME
        defaultUserRegistrationRequestShouldBeFound("familyName.equals=" + DEFAULT_FAMILY_NAME);

        // Get all the userRegistrationRequestList where familyName equals to UPDATED_FAMILY_NAME
        defaultUserRegistrationRequestShouldNotBeFound("familyName.equals=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFamilyNameIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where familyName in DEFAULT_FAMILY_NAME or UPDATED_FAMILY_NAME
        defaultUserRegistrationRequestShouldBeFound("familyName.in=" + DEFAULT_FAMILY_NAME + "," + UPDATED_FAMILY_NAME);

        // Get all the userRegistrationRequestList where familyName equals to UPDATED_FAMILY_NAME
        defaultUserRegistrationRequestShouldNotBeFound("familyName.in=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFamilyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where familyName is not null
        defaultUserRegistrationRequestShouldBeFound("familyName.specified=true");

        // Get all the userRegistrationRequestList where familyName is null
        defaultUserRegistrationRequestShouldNotBeFound("familyName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFamilyNameContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where familyName contains DEFAULT_FAMILY_NAME
        defaultUserRegistrationRequestShouldBeFound("familyName.contains=" + DEFAULT_FAMILY_NAME);

        // Get all the userRegistrationRequestList where familyName contains UPDATED_FAMILY_NAME
        defaultUserRegistrationRequestShouldNotBeFound("familyName.contains=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByFamilyNameNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where familyName does not contain DEFAULT_FAMILY_NAME
        defaultUserRegistrationRequestShouldNotBeFound("familyName.doesNotContain=" + DEFAULT_FAMILY_NAME);

        // Get all the userRegistrationRequestList where familyName does not contain UPDATED_FAMILY_NAME
        defaultUserRegistrationRequestShouldBeFound("familyName.doesNotContain=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityTownIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where cityTown equals to DEFAULT_CITY_TOWN
        defaultUserRegistrationRequestShouldBeFound("cityTown.equals=" + DEFAULT_CITY_TOWN);

        // Get all the userRegistrationRequestList where cityTown equals to UPDATED_CITY_TOWN
        defaultUserRegistrationRequestShouldNotBeFound("cityTown.equals=" + UPDATED_CITY_TOWN);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityTownIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where cityTown in DEFAULT_CITY_TOWN or UPDATED_CITY_TOWN
        defaultUserRegistrationRequestShouldBeFound("cityTown.in=" + DEFAULT_CITY_TOWN + "," + UPDATED_CITY_TOWN);

        // Get all the userRegistrationRequestList where cityTown equals to UPDATED_CITY_TOWN
        defaultUserRegistrationRequestShouldNotBeFound("cityTown.in=" + UPDATED_CITY_TOWN);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityTownIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where cityTown is not null
        defaultUserRegistrationRequestShouldBeFound("cityTown.specified=true");

        // Get all the userRegistrationRequestList where cityTown is null
        defaultUserRegistrationRequestShouldNotBeFound("cityTown.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityTownContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where cityTown contains DEFAULT_CITY_TOWN
        defaultUserRegistrationRequestShouldBeFound("cityTown.contains=" + DEFAULT_CITY_TOWN);

        // Get all the userRegistrationRequestList where cityTown contains UPDATED_CITY_TOWN
        defaultUserRegistrationRequestShouldNotBeFound("cityTown.contains=" + UPDATED_CITY_TOWN);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCityTownNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where cityTown does not contain DEFAULT_CITY_TOWN
        defaultUserRegistrationRequestShouldNotBeFound("cityTown.doesNotContain=" + DEFAULT_CITY_TOWN);

        // Get all the userRegistrationRequestList where cityTown does not contain UPDATED_CITY_TOWN
        defaultUserRegistrationRequestShouldBeFound("cityTown.doesNotContain=" + UPDATED_CITY_TOWN);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where district equals to DEFAULT_DISTRICT
        defaultUserRegistrationRequestShouldBeFound("district.equals=" + DEFAULT_DISTRICT);

        // Get all the userRegistrationRequestList where district equals to UPDATED_DISTRICT
        defaultUserRegistrationRequestShouldNotBeFound("district.equals=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByDistrictIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where district in DEFAULT_DISTRICT or UPDATED_DISTRICT
        defaultUserRegistrationRequestShouldBeFound("district.in=" + DEFAULT_DISTRICT + "," + UPDATED_DISTRICT);

        // Get all the userRegistrationRequestList where district equals to UPDATED_DISTRICT
        defaultUserRegistrationRequestShouldNotBeFound("district.in=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByDistrictIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where district is not null
        defaultUserRegistrationRequestShouldBeFound("district.specified=true");

        // Get all the userRegistrationRequestList where district is null
        defaultUserRegistrationRequestShouldNotBeFound("district.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByDistrictContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where district contains DEFAULT_DISTRICT
        defaultUserRegistrationRequestShouldBeFound("district.contains=" + DEFAULT_DISTRICT);

        // Get all the userRegistrationRequestList where district contains UPDATED_DISTRICT
        defaultUserRegistrationRequestShouldNotBeFound("district.contains=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByDistrictNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where district does not contain DEFAULT_DISTRICT
        defaultUserRegistrationRequestShouldNotBeFound("district.doesNotContain=" + DEFAULT_DISTRICT);

        // Get all the userRegistrationRequestList where district does not contain UPDATED_DISTRICT
        defaultUserRegistrationRequestShouldBeFound("district.doesNotContain=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEducationalInstitutionIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where educationalInstitution equals to DEFAULT_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldBeFound("educationalInstitution.equals=" + DEFAULT_EDUCATIONAL_INSTITUTION);

        // Get all the userRegistrationRequestList where educationalInstitution equals to UPDATED_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldNotBeFound("educationalInstitution.equals=" + UPDATED_EDUCATIONAL_INSTITUTION);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEducationalInstitutionIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where educationalInstitution in DEFAULT_EDUCATIONAL_INSTITUTION or UPDATED_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldBeFound(
            "educationalInstitution.in=" + DEFAULT_EDUCATIONAL_INSTITUTION + "," + UPDATED_EDUCATIONAL_INSTITUTION
        );

        // Get all the userRegistrationRequestList where educationalInstitution equals to UPDATED_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldNotBeFound("educationalInstitution.in=" + UPDATED_EDUCATIONAL_INSTITUTION);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEducationalInstitutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where educationalInstitution is not null
        defaultUserRegistrationRequestShouldBeFound("educationalInstitution.specified=true");

        // Get all the userRegistrationRequestList where educationalInstitution is null
        defaultUserRegistrationRequestShouldNotBeFound("educationalInstitution.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEducationalInstitutionContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where educationalInstitution contains DEFAULT_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldBeFound("educationalInstitution.contains=" + DEFAULT_EDUCATIONAL_INSTITUTION);

        // Get all the userRegistrationRequestList where educationalInstitution contains UPDATED_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldNotBeFound("educationalInstitution.contains=" + UPDATED_EDUCATIONAL_INSTITUTION);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByEducationalInstitutionNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where educationalInstitution does not contain DEFAULT_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldNotBeFound("educationalInstitution.doesNotContain=" + DEFAULT_EDUCATIONAL_INSTITUTION);

        // Get all the userRegistrationRequestList where educationalInstitution does not contain UPDATED_EDUCATIONAL_INSTITUTION
        defaultUserRegistrationRequestShouldBeFound("educationalInstitution.doesNotContain=" + UPDATED_EDUCATIONAL_INSTITUTION);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByProfileImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where profileImageUrl equals to DEFAULT_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldBeFound("profileImageUrl.equals=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the userRegistrationRequestList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldNotBeFound("profileImageUrl.equals=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByProfileImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where profileImageUrl in DEFAULT_PROFILE_IMAGE_URL or UPDATED_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldBeFound("profileImageUrl.in=" + DEFAULT_PROFILE_IMAGE_URL + "," + UPDATED_PROFILE_IMAGE_URL);

        // Get all the userRegistrationRequestList where profileImageUrl equals to UPDATED_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldNotBeFound("profileImageUrl.in=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByProfileImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where profileImageUrl is not null
        defaultUserRegistrationRequestShouldBeFound("profileImageUrl.specified=true");

        // Get all the userRegistrationRequestList where profileImageUrl is null
        defaultUserRegistrationRequestShouldNotBeFound("profileImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByProfileImageUrlContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where profileImageUrl contains DEFAULT_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldBeFound("profileImageUrl.contains=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the userRegistrationRequestList where profileImageUrl contains UPDATED_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldNotBeFound("profileImageUrl.contains=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByProfileImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where profileImageUrl does not contain DEFAULT_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldNotBeFound("profileImageUrl.doesNotContain=" + DEFAULT_PROFILE_IMAGE_URL);

        // Get all the userRegistrationRequestList where profileImageUrl does not contain UPDATED_PROFILE_IMAGE_URL
        defaultUserRegistrationRequestShouldBeFound("profileImageUrl.doesNotContain=" + UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestReason equals to DEFAULT_REQUEST_REASON
        defaultUserRegistrationRequestShouldBeFound("requestReason.equals=" + DEFAULT_REQUEST_REASON);

        // Get all the userRegistrationRequestList where requestReason equals to UPDATED_REQUEST_REASON
        defaultUserRegistrationRequestShouldNotBeFound("requestReason.equals=" + UPDATED_REQUEST_REASON);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestReasonIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestReason in DEFAULT_REQUEST_REASON or UPDATED_REQUEST_REASON
        defaultUserRegistrationRequestShouldBeFound("requestReason.in=" + DEFAULT_REQUEST_REASON + "," + UPDATED_REQUEST_REASON);

        // Get all the userRegistrationRequestList where requestReason equals to UPDATED_REQUEST_REASON
        defaultUserRegistrationRequestShouldNotBeFound("requestReason.in=" + UPDATED_REQUEST_REASON);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestReason is not null
        defaultUserRegistrationRequestShouldBeFound("requestReason.specified=true");

        // Get all the userRegistrationRequestList where requestReason is null
        defaultUserRegistrationRequestShouldNotBeFound("requestReason.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestReasonContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestReason contains DEFAULT_REQUEST_REASON
        defaultUserRegistrationRequestShouldBeFound("requestReason.contains=" + DEFAULT_REQUEST_REASON);

        // Get all the userRegistrationRequestList where requestReason contains UPDATED_REQUEST_REASON
        defaultUserRegistrationRequestShouldNotBeFound("requestReason.contains=" + UPDATED_REQUEST_REASON);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRequestReasonNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where requestReason does not contain DEFAULT_REQUEST_REASON
        defaultUserRegistrationRequestShouldNotBeFound("requestReason.doesNotContain=" + DEFAULT_REQUEST_REASON);

        // Get all the userRegistrationRequestList where requestReason does not contain UPDATED_REQUEST_REASON
        defaultUserRegistrationRequestShouldBeFound("requestReason.doesNotContain=" + UPDATED_REQUEST_REASON);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where status equals to DEFAULT_STATUS
        defaultUserRegistrationRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the userRegistrationRequestList where status equals to UPDATED_STATUS
        defaultUserRegistrationRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUserRegistrationRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the userRegistrationRequestList where status equals to UPDATED_STATUS
        defaultUserRegistrationRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where status is not null
        defaultUserRegistrationRequestShouldBeFound("status.specified=true");

        // Get all the userRegistrationRequestList where status is null
        defaultUserRegistrationRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStatusContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where status contains DEFAULT_STATUS
        defaultUserRegistrationRequestShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the userRegistrationRequestList where status contains UPDATED_STATUS
        defaultUserRegistrationRequestShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where status does not contain DEFAULT_STATUS
        defaultUserRegistrationRequestShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the userRegistrationRequestList where status does not contain UPDATED_STATUS
        defaultUserRegistrationRequestShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAdminCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where adminComments equals to DEFAULT_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldBeFound("adminComments.equals=" + DEFAULT_ADMIN_COMMENTS);

        // Get all the userRegistrationRequestList where adminComments equals to UPDATED_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldNotBeFound("adminComments.equals=" + UPDATED_ADMIN_COMMENTS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAdminCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where adminComments in DEFAULT_ADMIN_COMMENTS or UPDATED_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldBeFound("adminComments.in=" + DEFAULT_ADMIN_COMMENTS + "," + UPDATED_ADMIN_COMMENTS);

        // Get all the userRegistrationRequestList where adminComments equals to UPDATED_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldNotBeFound("adminComments.in=" + UPDATED_ADMIN_COMMENTS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAdminCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where adminComments is not null
        defaultUserRegistrationRequestShouldBeFound("adminComments.specified=true");

        // Get all the userRegistrationRequestList where adminComments is null
        defaultUserRegistrationRequestShouldNotBeFound("adminComments.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAdminCommentsContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where adminComments contains DEFAULT_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldBeFound("adminComments.contains=" + DEFAULT_ADMIN_COMMENTS);

        // Get all the userRegistrationRequestList where adminComments contains UPDATED_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldNotBeFound("adminComments.contains=" + UPDATED_ADMIN_COMMENTS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByAdminCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where adminComments does not contain DEFAULT_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldNotBeFound("adminComments.doesNotContain=" + DEFAULT_ADMIN_COMMENTS);

        // Get all the userRegistrationRequestList where adminComments does not contain UPDATED_ADMIN_COMMENTS
        defaultUserRegistrationRequestShouldBeFound("adminComments.doesNotContain=" + UPDATED_ADMIN_COMMENTS);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt equals to DEFAULT_SUBMITTED_AT
        defaultUserRegistrationRequestShouldBeFound("submittedAt.equals=" + DEFAULT_SUBMITTED_AT);

        // Get all the userRegistrationRequestList where submittedAt equals to UPDATED_SUBMITTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.equals=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt in DEFAULT_SUBMITTED_AT or UPDATED_SUBMITTED_AT
        defaultUserRegistrationRequestShouldBeFound("submittedAt.in=" + DEFAULT_SUBMITTED_AT + "," + UPDATED_SUBMITTED_AT);

        // Get all the userRegistrationRequestList where submittedAt equals to UPDATED_SUBMITTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.in=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt is not null
        defaultUserRegistrationRequestShouldBeFound("submittedAt.specified=true");

        // Get all the userRegistrationRequestList where submittedAt is null
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt is greater than or equal to DEFAULT_SUBMITTED_AT
        defaultUserRegistrationRequestShouldBeFound("submittedAt.greaterThanOrEqual=" + DEFAULT_SUBMITTED_AT);

        // Get all the userRegistrationRequestList where submittedAt is greater than or equal to UPDATED_SUBMITTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.greaterThanOrEqual=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt is less than or equal to DEFAULT_SUBMITTED_AT
        defaultUserRegistrationRequestShouldBeFound("submittedAt.lessThanOrEqual=" + DEFAULT_SUBMITTED_AT);

        // Get all the userRegistrationRequestList where submittedAt is less than or equal to SMALLER_SUBMITTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.lessThanOrEqual=" + SMALLER_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt is less than DEFAULT_SUBMITTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.lessThan=" + DEFAULT_SUBMITTED_AT);

        // Get all the userRegistrationRequestList where submittedAt is less than UPDATED_SUBMITTED_AT
        defaultUserRegistrationRequestShouldBeFound("submittedAt.lessThan=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsBySubmittedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where submittedAt is greater than DEFAULT_SUBMITTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("submittedAt.greaterThan=" + DEFAULT_SUBMITTED_AT);

        // Get all the userRegistrationRequestList where submittedAt is greater than SMALLER_SUBMITTED_AT
        defaultUserRegistrationRequestShouldBeFound("submittedAt.greaterThan=" + SMALLER_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt equals to DEFAULT_REVIEWED_AT
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.equals=" + DEFAULT_REVIEWED_AT);

        // Get all the userRegistrationRequestList where reviewedAt equals to UPDATED_REVIEWED_AT
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.equals=" + UPDATED_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt in DEFAULT_REVIEWED_AT or UPDATED_REVIEWED_AT
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.in=" + DEFAULT_REVIEWED_AT + "," + UPDATED_REVIEWED_AT);

        // Get all the userRegistrationRequestList where reviewedAt equals to UPDATED_REVIEWED_AT
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.in=" + UPDATED_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt is not null
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.specified=true");

        // Get all the userRegistrationRequestList where reviewedAt is null
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt is greater than or equal to DEFAULT_REVIEWED_AT
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.greaterThanOrEqual=" + DEFAULT_REVIEWED_AT);

        // Get all the userRegistrationRequestList where reviewedAt is greater than or equal to UPDATED_REVIEWED_AT
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.greaterThanOrEqual=" + UPDATED_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt is less than or equal to DEFAULT_REVIEWED_AT
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.lessThanOrEqual=" + DEFAULT_REVIEWED_AT);

        // Get all the userRegistrationRequestList where reviewedAt is less than or equal to SMALLER_REVIEWED_AT
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.lessThanOrEqual=" + SMALLER_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt is less than DEFAULT_REVIEWED_AT
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.lessThan=" + DEFAULT_REVIEWED_AT);

        // Get all the userRegistrationRequestList where reviewedAt is less than UPDATED_REVIEWED_AT
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.lessThan=" + UPDATED_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where reviewedAt is greater than DEFAULT_REVIEWED_AT
        defaultUserRegistrationRequestShouldNotBeFound("reviewedAt.greaterThan=" + DEFAULT_REVIEWED_AT);

        // Get all the userRegistrationRequestList where reviewedAt is greater than SMALLER_REVIEWED_AT
        defaultUserRegistrationRequestShouldBeFound("reviewedAt.greaterThan=" + SMALLER_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt equals to DEFAULT_APPROVED_AT
        defaultUserRegistrationRequestShouldBeFound("approvedAt.equals=" + DEFAULT_APPROVED_AT);

        // Get all the userRegistrationRequestList where approvedAt equals to UPDATED_APPROVED_AT
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.equals=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt in DEFAULT_APPROVED_AT or UPDATED_APPROVED_AT
        defaultUserRegistrationRequestShouldBeFound("approvedAt.in=" + DEFAULT_APPROVED_AT + "," + UPDATED_APPROVED_AT);

        // Get all the userRegistrationRequestList where approvedAt equals to UPDATED_APPROVED_AT
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.in=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt is not null
        defaultUserRegistrationRequestShouldBeFound("approvedAt.specified=true");

        // Get all the userRegistrationRequestList where approvedAt is null
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt is greater than or equal to DEFAULT_APPROVED_AT
        defaultUserRegistrationRequestShouldBeFound("approvedAt.greaterThanOrEqual=" + DEFAULT_APPROVED_AT);

        // Get all the userRegistrationRequestList where approvedAt is greater than or equal to UPDATED_APPROVED_AT
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.greaterThanOrEqual=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt is less than or equal to DEFAULT_APPROVED_AT
        defaultUserRegistrationRequestShouldBeFound("approvedAt.lessThanOrEqual=" + DEFAULT_APPROVED_AT);

        // Get all the userRegistrationRequestList where approvedAt is less than or equal to SMALLER_APPROVED_AT
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.lessThanOrEqual=" + SMALLER_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt is less than DEFAULT_APPROVED_AT
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.lessThan=" + DEFAULT_APPROVED_AT);

        // Get all the userRegistrationRequestList where approvedAt is less than UPDATED_APPROVED_AT
        defaultUserRegistrationRequestShouldBeFound("approvedAt.lessThan=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByApprovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where approvedAt is greater than DEFAULT_APPROVED_AT
        defaultUserRegistrationRequestShouldNotBeFound("approvedAt.greaterThan=" + DEFAULT_APPROVED_AT);

        // Get all the userRegistrationRequestList where approvedAt is greater than SMALLER_APPROVED_AT
        defaultUserRegistrationRequestShouldBeFound("approvedAt.greaterThan=" + SMALLER_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt equals to DEFAULT_REJECTED_AT
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.equals=" + DEFAULT_REJECTED_AT);

        // Get all the userRegistrationRequestList where rejectedAt equals to UPDATED_REJECTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.equals=" + UPDATED_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt in DEFAULT_REJECTED_AT or UPDATED_REJECTED_AT
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.in=" + DEFAULT_REJECTED_AT + "," + UPDATED_REJECTED_AT);

        // Get all the userRegistrationRequestList where rejectedAt equals to UPDATED_REJECTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.in=" + UPDATED_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt is not null
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.specified=true");

        // Get all the userRegistrationRequestList where rejectedAt is null
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt is greater than or equal to DEFAULT_REJECTED_AT
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.greaterThanOrEqual=" + DEFAULT_REJECTED_AT);

        // Get all the userRegistrationRequestList where rejectedAt is greater than or equal to UPDATED_REJECTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.greaterThanOrEqual=" + UPDATED_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt is less than or equal to DEFAULT_REJECTED_AT
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.lessThanOrEqual=" + DEFAULT_REJECTED_AT);

        // Get all the userRegistrationRequestList where rejectedAt is less than or equal to SMALLER_REJECTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.lessThanOrEqual=" + SMALLER_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt is less than DEFAULT_REJECTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.lessThan=" + DEFAULT_REJECTED_AT);

        // Get all the userRegistrationRequestList where rejectedAt is less than UPDATED_REJECTED_AT
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.lessThan=" + UPDATED_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByRejectedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where rejectedAt is greater than DEFAULT_REJECTED_AT
        defaultUserRegistrationRequestShouldNotBeFound("rejectedAt.greaterThan=" + DEFAULT_REJECTED_AT);

        // Get all the userRegistrationRequestList where rejectedAt is greater than SMALLER_REJECTED_AT
        defaultUserRegistrationRequestShouldBeFound("rejectedAt.greaterThan=" + SMALLER_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserRegistrationRequestShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userRegistrationRequestList where createdAt equals to UPDATED_CREATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserRegistrationRequestShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userRegistrationRequestList where createdAt equals to UPDATED_CREATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt is not null
        defaultUserRegistrationRequestShouldBeFound("createdAt.specified=true");

        // Get all the userRegistrationRequestList where createdAt is null
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultUserRegistrationRequestShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userRegistrationRequestList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultUserRegistrationRequestShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userRegistrationRequestList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt is less than DEFAULT_CREATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the userRegistrationRequestList where createdAt is less than UPDATED_CREATED_AT
        defaultUserRegistrationRequestShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where createdAt is greater than DEFAULT_CREATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the userRegistrationRequestList where createdAt is greater than SMALLER_CREATED_AT
        defaultUserRegistrationRequestShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserRegistrationRequestShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userRegistrationRequestList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserRegistrationRequestShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userRegistrationRequestList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt is not null
        defaultUserRegistrationRequestShouldBeFound("updatedAt.specified=true");

        // Get all the userRegistrationRequestList where updatedAt is null
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultUserRegistrationRequestShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userRegistrationRequestList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultUserRegistrationRequestShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userRegistrationRequestList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the userRegistrationRequestList where updatedAt is less than UPDATED_UPDATED_AT
        defaultUserRegistrationRequestShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserRegistrationRequestsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        // Get all the userRegistrationRequestList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultUserRegistrationRequestShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the userRegistrationRequestList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultUserRegistrationRequestShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    /* @Test
    @Transactional
    void getAllUserRegistrationRequestsByReviewedByIsEqualToSomething() throws Exception {
        UserProfile reviewedBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);
            reviewedBy = UserProfileResourceIT.createEntity(em);
        } else {
            reviewedBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(reviewedBy);
        em.flush();
        userRegistrationRequest.setReviewedBy(reviewedBy);
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);
        Long reviewedById = reviewedBy.getId();
        // Get all the userRegistrationRequestList where reviewedBy equals to reviewedById
        defaultUserRegistrationRequestShouldBeFound("reviewedById.equals=" + reviewedById);

        // Get all the userRegistrationRequestList where reviewedBy equals to (reviewedById + 1)
        defaultUserRegistrationRequestShouldNotBeFound("reviewedById.equals=" + (reviewedById + 1));
    }*/

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserRegistrationRequestShouldBeFound(String filter) throws Exception {
        restUserRegistrationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRegistrationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].requestId").value(hasItem(DEFAULT_REQUEST_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME)))
            .andExpect(jsonPath("$.[*].cityTown").value(hasItem(DEFAULT_CITY_TOWN)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].educationalInstitution").value(hasItem(DEFAULT_EDUCATIONAL_INSTITUTION)))
            .andExpect(jsonPath("$.[*].profileImageUrl").value(hasItem(DEFAULT_PROFILE_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].requestReason").value(hasItem(DEFAULT_REQUEST_REASON)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].adminComments").value(hasItem(DEFAULT_ADMIN_COMMENTS)))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(sameInstant(DEFAULT_SUBMITTED_AT))))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(sameInstant(DEFAULT_REVIEWED_AT))))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].rejectedAt").value(hasItem(sameInstant(DEFAULT_REJECTED_AT))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restUserRegistrationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserRegistrationRequestShouldNotBeFound(String filter) throws Exception {
        restUserRegistrationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserRegistrationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserRegistrationRequest() throws Exception {
        // Get the userRegistrationRequest
        restUserRegistrationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRegistrationRequest() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();

        // Update the userRegistrationRequest
        UserRegistrationRequest updatedUserRegistrationRequest = userRegistrationRequestRepository
            .findById(userRegistrationRequest.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserRegistrationRequest are not directly saved in db
        em.detach(updatedUserRegistrationRequest);
        updatedUserRegistrationRequest
            .tenantId(UPDATED_TENANT_ID)
            .requestId(UPDATED_REQUEST_ID)
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .familyName(UPDATED_FAMILY_NAME)
            .cityTown(UPDATED_CITY_TOWN)
            .district(UPDATED_DISTRICT)
            .educationalInstitution(UPDATED_EDUCATIONAL_INSTITUTION)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .requestReason(UPDATED_REQUEST_REASON)
            .status(UPDATED_STATUS)
            .adminComments(UPDATED_ADMIN_COMMENTS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(updatedUserRegistrationRequest);

        restUserRegistrationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRegistrationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
        UserRegistrationRequest testUserRegistrationRequest = userRegistrationRequestList.get(userRegistrationRequestList.size() - 1);
        assertThat(testUserRegistrationRequest.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testUserRegistrationRequest.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testUserRegistrationRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserRegistrationRequest.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserRegistrationRequest.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserRegistrationRequest.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserRegistrationRequest.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserRegistrationRequest.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testUserRegistrationRequest.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testUserRegistrationRequest.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserRegistrationRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserRegistrationRequest.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testUserRegistrationRequest.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserRegistrationRequest.getFamilyName()).isEqualTo(UPDATED_FAMILY_NAME);
        assertThat(testUserRegistrationRequest.getCityTown()).isEqualTo(UPDATED_CITY_TOWN);
        assertThat(testUserRegistrationRequest.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testUserRegistrationRequest.getEducationalInstitution()).isEqualTo(UPDATED_EDUCATIONAL_INSTITUTION);
        assertThat(testUserRegistrationRequest.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testUserRegistrationRequest.getRequestReason()).isEqualTo(UPDATED_REQUEST_REASON);
        assertThat(testUserRegistrationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserRegistrationRequest.getAdminComments()).isEqualTo(UPDATED_ADMIN_COMMENTS);
        assertThat(testUserRegistrationRequest.getSubmittedAt()).isEqualTo(UPDATED_SUBMITTED_AT);
        assertThat(testUserRegistrationRequest.getReviewedAt()).isEqualTo(UPDATED_REVIEWED_AT);
        assertThat(testUserRegistrationRequest.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testUserRegistrationRequest.getRejectedAt()).isEqualTo(UPDATED_REJECTED_AT);
        assertThat(testUserRegistrationRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserRegistrationRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();
        userRegistrationRequest.setId(longCount.incrementAndGet());

        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRegistrationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRegistrationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();
        userRegistrationRequest.setId(longCount.incrementAndGet());

        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRegistrationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();
        userRegistrationRequest.setId(longCount.incrementAndGet());

        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRegistrationRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRegistrationRequestWithPatch() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();

        // Update the userRegistrationRequest using partial update
        UserRegistrationRequest partialUpdatedUserRegistrationRequest = new UserRegistrationRequest();
        partialUpdatedUserRegistrationRequest.setId(userRegistrationRequest.getId());

        partialUpdatedUserRegistrationRequest
            .tenantId(UPDATED_TENANT_ID)
            .firstName(UPDATED_FIRST_NAME)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .familyName(UPDATED_FAMILY_NAME)
            .educationalInstitution(UPDATED_EDUCATIONAL_INSTITUTION)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .requestReason(UPDATED_REQUEST_REASON)
            .status(UPDATED_STATUS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT);

        restUserRegistrationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRegistrationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRegistrationRequest))
            )
            .andExpect(status().isOk());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
        UserRegistrationRequest testUserRegistrationRequest = userRegistrationRequestList.get(userRegistrationRequestList.size() - 1);
        assertThat(testUserRegistrationRequest.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testUserRegistrationRequest.getRequestId()).isEqualTo(DEFAULT_REQUEST_ID);
        assertThat(testUserRegistrationRequest.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserRegistrationRequest.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserRegistrationRequest.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserRegistrationRequest.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserRegistrationRequest.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserRegistrationRequest.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testUserRegistrationRequest.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testUserRegistrationRequest.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserRegistrationRequest.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserRegistrationRequest.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testUserRegistrationRequest.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserRegistrationRequest.getFamilyName()).isEqualTo(UPDATED_FAMILY_NAME);
        assertThat(testUserRegistrationRequest.getCityTown()).isEqualTo(DEFAULT_CITY_TOWN);
        assertThat(testUserRegistrationRequest.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testUserRegistrationRequest.getEducationalInstitution()).isEqualTo(UPDATED_EDUCATIONAL_INSTITUTION);
        assertThat(testUserRegistrationRequest.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testUserRegistrationRequest.getRequestReason()).isEqualTo(UPDATED_REQUEST_REASON);
        assertThat(testUserRegistrationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserRegistrationRequest.getAdminComments()).isEqualTo(DEFAULT_ADMIN_COMMENTS);
        assertThat(testUserRegistrationRequest.getSubmittedAt()).isEqualTo(UPDATED_SUBMITTED_AT);
        assertThat(testUserRegistrationRequest.getReviewedAt()).isEqualTo(DEFAULT_REVIEWED_AT);
        assertThat(testUserRegistrationRequest.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testUserRegistrationRequest.getRejectedAt()).isEqualTo(UPDATED_REJECTED_AT);
        assertThat(testUserRegistrationRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserRegistrationRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserRegistrationRequestWithPatch() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();

        // Update the userRegistrationRequest using partial update
        UserRegistrationRequest partialUpdatedUserRegistrationRequest = new UserRegistrationRequest();
        partialUpdatedUserRegistrationRequest.setId(userRegistrationRequest.getId());

        partialUpdatedUserRegistrationRequest
            .tenantId(UPDATED_TENANT_ID)
            .requestId(UPDATED_REQUEST_ID)
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .familyName(UPDATED_FAMILY_NAME)
            .cityTown(UPDATED_CITY_TOWN)
            .district(UPDATED_DISTRICT)
            .educationalInstitution(UPDATED_EDUCATIONAL_INSTITUTION)
            .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
            .requestReason(UPDATED_REQUEST_REASON)
            .status(UPDATED_STATUS)
            .adminComments(UPDATED_ADMIN_COMMENTS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserRegistrationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRegistrationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRegistrationRequest))
            )
            .andExpect(status().isOk());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
        UserRegistrationRequest testUserRegistrationRequest = userRegistrationRequestList.get(userRegistrationRequestList.size() - 1);
        assertThat(testUserRegistrationRequest.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testUserRegistrationRequest.getRequestId()).isEqualTo(UPDATED_REQUEST_ID);
        assertThat(testUserRegistrationRequest.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserRegistrationRequest.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserRegistrationRequest.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserRegistrationRequest.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserRegistrationRequest.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserRegistrationRequest.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testUserRegistrationRequest.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testUserRegistrationRequest.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserRegistrationRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserRegistrationRequest.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testUserRegistrationRequest.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserRegistrationRequest.getFamilyName()).isEqualTo(UPDATED_FAMILY_NAME);
        assertThat(testUserRegistrationRequest.getCityTown()).isEqualTo(UPDATED_CITY_TOWN);
        assertThat(testUserRegistrationRequest.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testUserRegistrationRequest.getEducationalInstitution()).isEqualTo(UPDATED_EDUCATIONAL_INSTITUTION);
        assertThat(testUserRegistrationRequest.getProfileImageUrl()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
        assertThat(testUserRegistrationRequest.getRequestReason()).isEqualTo(UPDATED_REQUEST_REASON);
        assertThat(testUserRegistrationRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserRegistrationRequest.getAdminComments()).isEqualTo(UPDATED_ADMIN_COMMENTS);
        assertThat(testUserRegistrationRequest.getSubmittedAt()).isEqualTo(UPDATED_SUBMITTED_AT);
        assertThat(testUserRegistrationRequest.getReviewedAt()).isEqualTo(UPDATED_REVIEWED_AT);
        assertThat(testUserRegistrationRequest.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testUserRegistrationRequest.getRejectedAt()).isEqualTo(UPDATED_REJECTED_AT);
        assertThat(testUserRegistrationRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserRegistrationRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();
        userRegistrationRequest.setId(longCount.incrementAndGet());

        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRegistrationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRegistrationRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();
        userRegistrationRequest.setId(longCount.incrementAndGet());

        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRegistrationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRegistrationRequest() throws Exception {
        int databaseSizeBeforeUpdate = userRegistrationRequestRepository.findAll().size();
        userRegistrationRequest.setId(longCount.incrementAndGet());

        // Create the UserRegistrationRequest
        UserRegistrationRequestDTO userRegistrationRequestDTO = userRegistrationRequestMapper.toDto(userRegistrationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRegistrationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRegistrationRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRegistrationRequest in the database
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRegistrationRequest() throws Exception {
        // Initialize the database
        userRegistrationRequestRepository.saveAndFlush(userRegistrationRequest);

        int databaseSizeBeforeDelete = userRegistrationRequestRepository.findAll().size();

        // Delete the userRegistrationRequest
        restUserRegistrationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRegistrationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserRegistrationRequest> userRegistrationRequestList = userRegistrationRequestRepository.findAll();
        assertThat(userRegistrationRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
