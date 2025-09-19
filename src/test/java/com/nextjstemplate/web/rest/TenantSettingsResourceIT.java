package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
import com.nextjstemplate.service.mapper.TenantSettingsMapper;
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
 * Integration tests for the {@link TenantSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantSettingsResourceIT {

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ALLOW_USER_REGISTRATION = false;
    private static final Boolean UPDATED_ALLOW_USER_REGISTRATION = true;

    private static final Boolean DEFAULT_REQUIRE_ADMIN_APPROVAL = false;
    private static final Boolean UPDATED_REQUIRE_ADMIN_APPROVAL = true;

    private static final Boolean DEFAULT_ENABLE_WHATSAPP_INTEGRATION = false;
    private static final Boolean UPDATED_ENABLE_WHATSAPP_INTEGRATION = true;

    private static final Boolean DEFAULT_ENABLE_EMAIL_MARKETING = false;
    private static final Boolean UPDATED_ENABLE_EMAIL_MARKETING = true;

    private static final String DEFAULT_WHATSAPP_API_KEY = "AAAAAAAAAA";
    private static final String UPDATED_WHATSAPP_API_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_PROVIDER_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_PROVIDER_CONFIG = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_CSS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_CSS = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_JS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_JS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_EVENTS_SECTION_IN_HOME_PAGE = false;
    private static final Boolean UPDATED_SHOW_EVENTS_SECTION_IN_HOME_PAGE = true;

    private static final Boolean DEFAULT_SHOW_TEAM_MEMBERS_SECTION_IN_HOME_PAGE = false;
    private static final Boolean UPDATED_SHOW_TEAM_MEMBERS_SECTION_IN_HOME_PAGE = true;

    private static final Boolean DEFAULT_SHOW_SPONSORS_SECTION_IN_HOME_PAGE = false;
    private static final Boolean UPDATED_SHOW_SPONSORS_SECTION_IN_HOME_PAGE = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/tenant-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantSettingsRepository tenantSettingsRepository;

    @Autowired
    private TenantSettingsMapper tenantSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantSettingsMockMvc;

    private TenantSettings tenantSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantSettings createEntity(EntityManager em) {
        TenantSettings tenantSettings = new TenantSettings()
            .tenantId(DEFAULT_TENANT_ID)
            .allowUserRegistration(DEFAULT_ALLOW_USER_REGISTRATION)
            .requireAdminApproval(DEFAULT_REQUIRE_ADMIN_APPROVAL)
            .enableWhatsappIntegration(DEFAULT_ENABLE_WHATSAPP_INTEGRATION)
            .enableEmailMarketing(DEFAULT_ENABLE_EMAIL_MARKETING)
            .whatsappApiKey(DEFAULT_WHATSAPP_API_KEY)
            .emailProviderConfig(DEFAULT_EMAIL_PROVIDER_CONFIG)
            .customCss(DEFAULT_CUSTOM_CSS)
            .customJs(DEFAULT_CUSTOM_JS)
            .showEventsSectionInHomePage(DEFAULT_SHOW_EVENTS_SECTION_IN_HOME_PAGE)
            .showTeamMembersSectionInHomePage(DEFAULT_SHOW_TEAM_MEMBERS_SECTION_IN_HOME_PAGE)
            .showSponsorsSectionInHomePage(DEFAULT_SHOW_SPONSORS_SECTION_IN_HOME_PAGE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return tenantSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantSettings createUpdatedEntity(EntityManager em) {
        TenantSettings tenantSettings = new TenantSettings()
            .tenantId(UPDATED_TENANT_ID)
            .allowUserRegistration(UPDATED_ALLOW_USER_REGISTRATION)
            .requireAdminApproval(UPDATED_REQUIRE_ADMIN_APPROVAL)
            .enableWhatsappIntegration(UPDATED_ENABLE_WHATSAPP_INTEGRATION)
            .enableEmailMarketing(UPDATED_ENABLE_EMAIL_MARKETING)
            .whatsappApiKey(UPDATED_WHATSAPP_API_KEY)
            .emailProviderConfig(UPDATED_EMAIL_PROVIDER_CONFIG)
            .customCss(UPDATED_CUSTOM_CSS)
            .customJs(UPDATED_CUSTOM_JS)
            .showEventsSectionInHomePage(UPDATED_SHOW_EVENTS_SECTION_IN_HOME_PAGE)
            .showTeamMembersSectionInHomePage(UPDATED_SHOW_TEAM_MEMBERS_SECTION_IN_HOME_PAGE)
            .showSponsorsSectionInHomePage(UPDATED_SHOW_SPONSORS_SECTION_IN_HOME_PAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return tenantSettings;
    }

    @BeforeEach
    public void initTest() {
        tenantSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createTenantSettings() throws Exception {
        int databaseSizeBeforeCreate = tenantSettingsRepository.findAll().size();
        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);
        restTenantSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        TenantSettings testTenantSettings = tenantSettingsList.get(tenantSettingsList.size() - 1);
        assertThat(testTenantSettings.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testTenantSettings.getAllowUserRegistration()).isEqualTo(DEFAULT_ALLOW_USER_REGISTRATION);
        assertThat(testTenantSettings.getRequireAdminApproval()).isEqualTo(DEFAULT_REQUIRE_ADMIN_APPROVAL);
        assertThat(testTenantSettings.getEnableWhatsappIntegration()).isEqualTo(DEFAULT_ENABLE_WHATSAPP_INTEGRATION);
        assertThat(testTenantSettings.getEnableEmailMarketing()).isEqualTo(DEFAULT_ENABLE_EMAIL_MARKETING);
        assertThat(testTenantSettings.getWhatsappApiKey()).isEqualTo(DEFAULT_WHATSAPP_API_KEY);
        assertThat(testTenantSettings.getEmailProviderConfig()).isEqualTo(DEFAULT_EMAIL_PROVIDER_CONFIG);
        assertThat(testTenantSettings.getCustomCss()).isEqualTo(DEFAULT_CUSTOM_CSS);
        assertThat(testTenantSettings.getCustomJs()).isEqualTo(DEFAULT_CUSTOM_JS);
        assertThat(testTenantSettings.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTenantSettings.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTenantSettingsWithExistingId() throws Exception {
        // Create the TenantSettings with an existing ID
        tenantSettings.setId(1L);
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        int databaseSizeBeforeCreate = tenantSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenantIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantSettingsRepository.findAll().size();
        // set the field null
        tenantSettings.setTenantId(null);

        // Create the TenantSettings, which fails.
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        restTenantSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantSettingsRepository.findAll().size();
        // set the field null
        tenantSettings.setCreatedAt(null);

        // Create the TenantSettings, which fails.
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        restTenantSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantSettingsRepository.findAll().size();
        // set the field null
        tenantSettings.setUpdatedAt(null);

        // Create the TenantSettings, which fails.
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        restTenantSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantSettings() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList
        restTenantSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].allowUserRegistration").value(hasItem(DEFAULT_ALLOW_USER_REGISTRATION.booleanValue())))
            .andExpect(jsonPath("$.[*].requireAdminApproval").value(hasItem(DEFAULT_REQUIRE_ADMIN_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].enableWhatsappIntegration").value(hasItem(DEFAULT_ENABLE_WHATSAPP_INTEGRATION.booleanValue())))
            .andExpect(jsonPath("$.[*].enableEmailMarketing").value(hasItem(DEFAULT_ENABLE_EMAIL_MARKETING.booleanValue())))
            .andExpect(jsonPath("$.[*].whatsappApiKey").value(hasItem(DEFAULT_WHATSAPP_API_KEY)))
            .andExpect(jsonPath("$.[*].emailProviderConfig").value(hasItem(DEFAULT_EMAIL_PROVIDER_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].customCss").value(hasItem(DEFAULT_CUSTOM_CSS.toString())))
            .andExpect(jsonPath("$.[*].customJs").value(hasItem(DEFAULT_CUSTOM_JS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getTenantSettings() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get the tenantSettings
        restTenantSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantSettings.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.allowUserRegistration").value(DEFAULT_ALLOW_USER_REGISTRATION.booleanValue()))
            .andExpect(jsonPath("$.requireAdminApproval").value(DEFAULT_REQUIRE_ADMIN_APPROVAL.booleanValue()))
            .andExpect(jsonPath("$.enableWhatsappIntegration").value(DEFAULT_ENABLE_WHATSAPP_INTEGRATION.booleanValue()))
            .andExpect(jsonPath("$.enableEmailMarketing").value(DEFAULT_ENABLE_EMAIL_MARKETING.booleanValue()))
            .andExpect(jsonPath("$.whatsappApiKey").value(DEFAULT_WHATSAPP_API_KEY))
            .andExpect(jsonPath("$.emailProviderConfig").value(DEFAULT_EMAIL_PROVIDER_CONFIG.toString()))
            .andExpect(jsonPath("$.customCss").value(DEFAULT_CUSTOM_CSS.toString()))
            .andExpect(jsonPath("$.customJs").value(DEFAULT_CUSTOM_JS.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getTenantSettingsByIdFiltering() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        Long id = tenantSettings.getId();

        defaultTenantSettingsShouldBeFound("id.equals=" + id);
        defaultTenantSettingsShouldNotBeFound("id.notEquals=" + id);

        defaultTenantSettingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTenantSettingsShouldNotBeFound("id.greaterThan=" + id);

        defaultTenantSettingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTenantSettingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where tenantId equals to DEFAULT_TENANT_ID
        defaultTenantSettingsShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the tenantSettingsList where tenantId equals to UPDATED_TENANT_ID
        defaultTenantSettingsShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultTenantSettingsShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the tenantSettingsList where tenantId equals to UPDATED_TENANT_ID
        defaultTenantSettingsShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where tenantId is not null
        defaultTenantSettingsShouldBeFound("tenantId.specified=true");

        // Get all the tenantSettingsList where tenantId is null
        defaultTenantSettingsShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where tenantId contains DEFAULT_TENANT_ID
        defaultTenantSettingsShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the tenantSettingsList where tenantId contains UPDATED_TENANT_ID
        defaultTenantSettingsShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where tenantId does not contain DEFAULT_TENANT_ID
        defaultTenantSettingsShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the tenantSettingsList where tenantId does not contain UPDATED_TENANT_ID
        defaultTenantSettingsShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByAllowUserRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where allowUserRegistration equals to DEFAULT_ALLOW_USER_REGISTRATION
        defaultTenantSettingsShouldBeFound("allowUserRegistration.equals=" + DEFAULT_ALLOW_USER_REGISTRATION);

        // Get all the tenantSettingsList where allowUserRegistration equals to UPDATED_ALLOW_USER_REGISTRATION
        defaultTenantSettingsShouldNotBeFound("allowUserRegistration.equals=" + UPDATED_ALLOW_USER_REGISTRATION);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByAllowUserRegistrationIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where allowUserRegistration in DEFAULT_ALLOW_USER_REGISTRATION or UPDATED_ALLOW_USER_REGISTRATION
        defaultTenantSettingsShouldBeFound(
            "allowUserRegistration.in=" + DEFAULT_ALLOW_USER_REGISTRATION + "," + UPDATED_ALLOW_USER_REGISTRATION
        );

        // Get all the tenantSettingsList where allowUserRegistration equals to UPDATED_ALLOW_USER_REGISTRATION
        defaultTenantSettingsShouldNotBeFound("allowUserRegistration.in=" + UPDATED_ALLOW_USER_REGISTRATION);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByAllowUserRegistrationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where allowUserRegistration is not null
        defaultTenantSettingsShouldBeFound("allowUserRegistration.specified=true");

        // Get all the tenantSettingsList where allowUserRegistration is null
        defaultTenantSettingsShouldNotBeFound("allowUserRegistration.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByRequireAdminApprovalIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where requireAdminApproval equals to DEFAULT_REQUIRE_ADMIN_APPROVAL
        defaultTenantSettingsShouldBeFound("requireAdminApproval.equals=" + DEFAULT_REQUIRE_ADMIN_APPROVAL);

        // Get all the tenantSettingsList where requireAdminApproval equals to UPDATED_REQUIRE_ADMIN_APPROVAL
        defaultTenantSettingsShouldNotBeFound("requireAdminApproval.equals=" + UPDATED_REQUIRE_ADMIN_APPROVAL);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByRequireAdminApprovalIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where requireAdminApproval in DEFAULT_REQUIRE_ADMIN_APPROVAL or UPDATED_REQUIRE_ADMIN_APPROVAL
        defaultTenantSettingsShouldBeFound(
            "requireAdminApproval.in=" + DEFAULT_REQUIRE_ADMIN_APPROVAL + "," + UPDATED_REQUIRE_ADMIN_APPROVAL
        );

        // Get all the tenantSettingsList where requireAdminApproval equals to UPDATED_REQUIRE_ADMIN_APPROVAL
        defaultTenantSettingsShouldNotBeFound("requireAdminApproval.in=" + UPDATED_REQUIRE_ADMIN_APPROVAL);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByRequireAdminApprovalIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where requireAdminApproval is not null
        defaultTenantSettingsShouldBeFound("requireAdminApproval.specified=true");

        // Get all the tenantSettingsList where requireAdminApproval is null
        defaultTenantSettingsShouldNotBeFound("requireAdminApproval.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByEnableWhatsappIntegrationIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where enableWhatsappIntegration equals to DEFAULT_ENABLE_WHATSAPP_INTEGRATION
        defaultTenantSettingsShouldBeFound("enableWhatsappIntegration.equals=" + DEFAULT_ENABLE_WHATSAPP_INTEGRATION);

        // Get all the tenantSettingsList where enableWhatsappIntegration equals to UPDATED_ENABLE_WHATSAPP_INTEGRATION
        defaultTenantSettingsShouldNotBeFound("enableWhatsappIntegration.equals=" + UPDATED_ENABLE_WHATSAPP_INTEGRATION);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByEnableWhatsappIntegrationIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where enableWhatsappIntegration in DEFAULT_ENABLE_WHATSAPP_INTEGRATION or UPDATED_ENABLE_WHATSAPP_INTEGRATION
        defaultTenantSettingsShouldBeFound(
            "enableWhatsappIntegration.in=" + DEFAULT_ENABLE_WHATSAPP_INTEGRATION + "," + UPDATED_ENABLE_WHATSAPP_INTEGRATION
        );

        // Get all the tenantSettingsList where enableWhatsappIntegration equals to UPDATED_ENABLE_WHATSAPP_INTEGRATION
        defaultTenantSettingsShouldNotBeFound("enableWhatsappIntegration.in=" + UPDATED_ENABLE_WHATSAPP_INTEGRATION);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByEnableWhatsappIntegrationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where enableWhatsappIntegration is not null
        defaultTenantSettingsShouldBeFound("enableWhatsappIntegration.specified=true");

        // Get all the tenantSettingsList where enableWhatsappIntegration is null
        defaultTenantSettingsShouldNotBeFound("enableWhatsappIntegration.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByEnableEmailMarketingIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where enableEmailMarketing equals to DEFAULT_ENABLE_EMAIL_MARKETING
        defaultTenantSettingsShouldBeFound("enableEmailMarketing.equals=" + DEFAULT_ENABLE_EMAIL_MARKETING);

        // Get all the tenantSettingsList where enableEmailMarketing equals to UPDATED_ENABLE_EMAIL_MARKETING
        defaultTenantSettingsShouldNotBeFound("enableEmailMarketing.equals=" + UPDATED_ENABLE_EMAIL_MARKETING);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByEnableEmailMarketingIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where enableEmailMarketing in DEFAULT_ENABLE_EMAIL_MARKETING or UPDATED_ENABLE_EMAIL_MARKETING
        defaultTenantSettingsShouldBeFound(
            "enableEmailMarketing.in=" + DEFAULT_ENABLE_EMAIL_MARKETING + "," + UPDATED_ENABLE_EMAIL_MARKETING
        );

        // Get all the tenantSettingsList where enableEmailMarketing equals to UPDATED_ENABLE_EMAIL_MARKETING
        defaultTenantSettingsShouldNotBeFound("enableEmailMarketing.in=" + UPDATED_ENABLE_EMAIL_MARKETING);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByEnableEmailMarketingIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where enableEmailMarketing is not null
        defaultTenantSettingsShouldBeFound("enableEmailMarketing.specified=true");

        // Get all the tenantSettingsList where enableEmailMarketing is null
        defaultTenantSettingsShouldNotBeFound("enableEmailMarketing.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByWhatsappApiKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where whatsappApiKey equals to DEFAULT_WHATSAPP_API_KEY
        defaultTenantSettingsShouldBeFound("whatsappApiKey.equals=" + DEFAULT_WHATSAPP_API_KEY);

        // Get all the tenantSettingsList where whatsappApiKey equals to UPDATED_WHATSAPP_API_KEY
        defaultTenantSettingsShouldNotBeFound("whatsappApiKey.equals=" + UPDATED_WHATSAPP_API_KEY);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByWhatsappApiKeyIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where whatsappApiKey in DEFAULT_WHATSAPP_API_KEY or UPDATED_WHATSAPP_API_KEY
        defaultTenantSettingsShouldBeFound("whatsappApiKey.in=" + DEFAULT_WHATSAPP_API_KEY + "," + UPDATED_WHATSAPP_API_KEY);

        // Get all the tenantSettingsList where whatsappApiKey equals to UPDATED_WHATSAPP_API_KEY
        defaultTenantSettingsShouldNotBeFound("whatsappApiKey.in=" + UPDATED_WHATSAPP_API_KEY);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByWhatsappApiKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where whatsappApiKey is not null
        defaultTenantSettingsShouldBeFound("whatsappApiKey.specified=true");

        // Get all the tenantSettingsList where whatsappApiKey is null
        defaultTenantSettingsShouldNotBeFound("whatsappApiKey.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByWhatsappApiKeyContainsSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where whatsappApiKey contains DEFAULT_WHATSAPP_API_KEY
        defaultTenantSettingsShouldBeFound("whatsappApiKey.contains=" + DEFAULT_WHATSAPP_API_KEY);

        // Get all the tenantSettingsList where whatsappApiKey contains UPDATED_WHATSAPP_API_KEY
        defaultTenantSettingsShouldNotBeFound("whatsappApiKey.contains=" + UPDATED_WHATSAPP_API_KEY);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByWhatsappApiKeyNotContainsSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where whatsappApiKey does not contain DEFAULT_WHATSAPP_API_KEY
        defaultTenantSettingsShouldNotBeFound("whatsappApiKey.doesNotContain=" + DEFAULT_WHATSAPP_API_KEY);

        // Get all the tenantSettingsList where whatsappApiKey does not contain UPDATED_WHATSAPP_API_KEY
        defaultTenantSettingsShouldBeFound("whatsappApiKey.doesNotContain=" + UPDATED_WHATSAPP_API_KEY);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt equals to DEFAULT_CREATED_AT
        defaultTenantSettingsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the tenantSettingsList where createdAt equals to UPDATED_CREATED_AT
        defaultTenantSettingsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTenantSettingsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the tenantSettingsList where createdAt equals to UPDATED_CREATED_AT
        defaultTenantSettingsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt is not null
        defaultTenantSettingsShouldBeFound("createdAt.specified=true");

        // Get all the tenantSettingsList where createdAt is null
        defaultTenantSettingsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultTenantSettingsShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the tenantSettingsList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultTenantSettingsShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultTenantSettingsShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the tenantSettingsList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultTenantSettingsShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt is less than DEFAULT_CREATED_AT
        defaultTenantSettingsShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the tenantSettingsList where createdAt is less than UPDATED_CREATED_AT
        defaultTenantSettingsShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where createdAt is greater than DEFAULT_CREATED_AT
        defaultTenantSettingsShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the tenantSettingsList where createdAt is greater than SMALLER_CREATED_AT
        defaultTenantSettingsShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTenantSettingsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the tenantSettingsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTenantSettingsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTenantSettingsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the tenantSettingsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTenantSettingsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt is not null
        defaultTenantSettingsShouldBeFound("updatedAt.specified=true");

        // Get all the tenantSettingsList where updatedAt is null
        defaultTenantSettingsShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultTenantSettingsShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the tenantSettingsList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultTenantSettingsShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultTenantSettingsShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the tenantSettingsList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultTenantSettingsShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultTenantSettingsShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the tenantSettingsList where updatedAt is less than UPDATED_UPDATED_AT
        defaultTenantSettingsShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTenantSettingsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        // Get all the tenantSettingsList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultTenantSettingsShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the tenantSettingsList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultTenantSettingsShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    //    @Test
    //    @Transactional
    //    void getAllTenantSettingsByTenantOrganizationIsEqualToSomething() throws Exception {
    //        TenantOrganization tenantOrganization;
    //        if (TestUtil.findAll(em, TenantOrganization.class).isEmpty()) {
    //            tenantSettingsRepository.saveAndFlush(tenantSettings);
    //            tenantOrganization = TenantOrganizationResourceIT.createEntity(em);
    //        } else {
    //            tenantOrganization = TestUtil.findAll(em, TenantOrganization.class).get(0);
    //        }
    //        em.persist(tenantOrganization);
    //        em.flush();
    //        tenantSettings.setTenantOrganization(tenantOrganization);
    //        tenantSettingsRepository.saveAndFlush(tenantSettings);
    //        Long tenantOrganizationId = tenantOrganization.getId();
    //        // Get all the tenantSettingsList where tenantOrganization equals to tenantOrganizationId
    //        defaultTenantSettingsShouldBeFound("tenantOrganizationId.equals=" + tenantOrganizationId);
    //
    //        // Get all the tenantSettingsList where tenantOrganization equals to (tenantOrganizationId + 1)
    //        defaultTenantSettingsShouldNotBeFound("tenantOrganizationId.equals=" + (tenantOrganizationId + 1));
    //    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantSettingsShouldBeFound(String filter) throws Exception {
        restTenantSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].allowUserRegistration").value(hasItem(DEFAULT_ALLOW_USER_REGISTRATION.booleanValue())))
            .andExpect(jsonPath("$.[*].requireAdminApproval").value(hasItem(DEFAULT_REQUIRE_ADMIN_APPROVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].enableWhatsappIntegration").value(hasItem(DEFAULT_ENABLE_WHATSAPP_INTEGRATION.booleanValue())))
            .andExpect(jsonPath("$.[*].enableEmailMarketing").value(hasItem(DEFAULT_ENABLE_EMAIL_MARKETING.booleanValue())))
            .andExpect(jsonPath("$.[*].whatsappApiKey").value(hasItem(DEFAULT_WHATSAPP_API_KEY)))
            .andExpect(jsonPath("$.[*].emailProviderConfig").value(hasItem(DEFAULT_EMAIL_PROVIDER_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].customCss").value(hasItem(DEFAULT_CUSTOM_CSS.toString())))
            .andExpect(jsonPath("$.[*].customJs").value(hasItem(DEFAULT_CUSTOM_JS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restTenantSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantSettingsShouldNotBeFound(String filter) throws Exception {
        restTenantSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTenantSettings() throws Exception {
        // Get the tenantSettings
        restTenantSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTenantSettings() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();

        // Update the tenantSettings
        TenantSettings updatedTenantSettings = tenantSettingsRepository.findById(tenantSettings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTenantSettings are not directly saved in db
        em.detach(updatedTenantSettings);
        updatedTenantSettings
            .tenantId(UPDATED_TENANT_ID)
            .allowUserRegistration(UPDATED_ALLOW_USER_REGISTRATION)
            .requireAdminApproval(UPDATED_REQUIRE_ADMIN_APPROVAL)
            .enableWhatsappIntegration(UPDATED_ENABLE_WHATSAPP_INTEGRATION)
            .enableEmailMarketing(UPDATED_ENABLE_EMAIL_MARKETING)
            .whatsappApiKey(UPDATED_WHATSAPP_API_KEY)
            .emailProviderConfig(UPDATED_EMAIL_PROVIDER_CONFIG)
            .customCss(UPDATED_CUSTOM_CSS)
            .customJs(UPDATED_CUSTOM_JS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(updatedTenantSettings);

        restTenantSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
        TenantSettings testTenantSettings = tenantSettingsList.get(tenantSettingsList.size() - 1);
        assertThat(testTenantSettings.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantSettings.getAllowUserRegistration()).isEqualTo(UPDATED_ALLOW_USER_REGISTRATION);
        assertThat(testTenantSettings.getRequireAdminApproval()).isEqualTo(UPDATED_REQUIRE_ADMIN_APPROVAL);
        assertThat(testTenantSettings.getEnableWhatsappIntegration()).isEqualTo(UPDATED_ENABLE_WHATSAPP_INTEGRATION);
        assertThat(testTenantSettings.getEnableEmailMarketing()).isEqualTo(UPDATED_ENABLE_EMAIL_MARKETING);
        assertThat(testTenantSettings.getWhatsappApiKey()).isEqualTo(UPDATED_WHATSAPP_API_KEY);
        assertThat(testTenantSettings.getEmailProviderConfig()).isEqualTo(UPDATED_EMAIL_PROVIDER_CONFIG);
        assertThat(testTenantSettings.getCustomCss()).isEqualTo(UPDATED_CUSTOM_CSS);
        assertThat(testTenantSettings.getCustomJs()).isEqualTo(UPDATED_CUSTOM_JS);
        assertThat(testTenantSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenantSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTenantSettings() throws Exception {
        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();
        tenantSettings.setId(longCount.incrementAndGet());

        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantSettings() throws Exception {
        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();
        tenantSettings.setId(longCount.incrementAndGet());

        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantSettings() throws Exception {
        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();
        tenantSettings.setId(longCount.incrementAndGet());

        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantSettingsWithPatch() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();

        // Update the tenantSettings using partial update
        TenantSettings partialUpdatedTenantSettings = new TenantSettings();
        partialUpdatedTenantSettings.setId(tenantSettings.getId());

        partialUpdatedTenantSettings
            .tenantId(UPDATED_TENANT_ID)
            .enableWhatsappIntegration(UPDATED_ENABLE_WHATSAPP_INTEGRATION)
            .whatsappApiKey(UPDATED_WHATSAPP_API_KEY)
            .emailProviderConfig(UPDATED_EMAIL_PROVIDER_CONFIG)
            .createdAt(UPDATED_CREATED_AT);

        restTenantSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantSettings))
            )
            .andExpect(status().isOk());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
        TenantSettings testTenantSettings = tenantSettingsList.get(tenantSettingsList.size() - 1);
        assertThat(testTenantSettings.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantSettings.getAllowUserRegistration()).isEqualTo(DEFAULT_ALLOW_USER_REGISTRATION);
        assertThat(testTenantSettings.getRequireAdminApproval()).isEqualTo(DEFAULT_REQUIRE_ADMIN_APPROVAL);
        assertThat(testTenantSettings.getEnableWhatsappIntegration()).isEqualTo(UPDATED_ENABLE_WHATSAPP_INTEGRATION);
        assertThat(testTenantSettings.getEnableEmailMarketing()).isEqualTo(DEFAULT_ENABLE_EMAIL_MARKETING);
        assertThat(testTenantSettings.getWhatsappApiKey()).isEqualTo(UPDATED_WHATSAPP_API_KEY);
        assertThat(testTenantSettings.getEmailProviderConfig()).isEqualTo(UPDATED_EMAIL_PROVIDER_CONFIG);
        assertThat(testTenantSettings.getCustomCss()).isEqualTo(DEFAULT_CUSTOM_CSS);
        assertThat(testTenantSettings.getCustomJs()).isEqualTo(DEFAULT_CUSTOM_JS);
        assertThat(testTenantSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenantSettings.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTenantSettingsWithPatch() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();

        // Update the tenantSettings using partial update
        TenantSettings partialUpdatedTenantSettings = new TenantSettings();
        partialUpdatedTenantSettings.setId(tenantSettings.getId());

        partialUpdatedTenantSettings
            .tenantId(UPDATED_TENANT_ID)
            .allowUserRegistration(UPDATED_ALLOW_USER_REGISTRATION)
            .requireAdminApproval(UPDATED_REQUIRE_ADMIN_APPROVAL)
            .enableWhatsappIntegration(UPDATED_ENABLE_WHATSAPP_INTEGRATION)
            .enableEmailMarketing(UPDATED_ENABLE_EMAIL_MARKETING)
            .whatsappApiKey(UPDATED_WHATSAPP_API_KEY)
            .emailProviderConfig(UPDATED_EMAIL_PROVIDER_CONFIG)
            .customCss(UPDATED_CUSTOM_CSS)
            .customJs(UPDATED_CUSTOM_JS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTenantSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantSettings))
            )
            .andExpect(status().isOk());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
        TenantSettings testTenantSettings = tenantSettingsList.get(tenantSettingsList.size() - 1);
        assertThat(testTenantSettings.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testTenantSettings.getAllowUserRegistration()).isEqualTo(UPDATED_ALLOW_USER_REGISTRATION);
        assertThat(testTenantSettings.getRequireAdminApproval()).isEqualTo(UPDATED_REQUIRE_ADMIN_APPROVAL);
        assertThat(testTenantSettings.getEnableWhatsappIntegration()).isEqualTo(UPDATED_ENABLE_WHATSAPP_INTEGRATION);
        assertThat(testTenantSettings.getEnableEmailMarketing()).isEqualTo(UPDATED_ENABLE_EMAIL_MARKETING);
        assertThat(testTenantSettings.getWhatsappApiKey()).isEqualTo(UPDATED_WHATSAPP_API_KEY);
        assertThat(testTenantSettings.getEmailProviderConfig()).isEqualTo(UPDATED_EMAIL_PROVIDER_CONFIG);
        assertThat(testTenantSettings.getCustomCss()).isEqualTo(UPDATED_CUSTOM_CSS);
        assertThat(testTenantSettings.getCustomJs()).isEqualTo(UPDATED_CUSTOM_JS);
        assertThat(testTenantSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenantSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTenantSettings() throws Exception {
        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();
        tenantSettings.setId(longCount.incrementAndGet());

        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantSettings() throws Exception {
        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();
        tenantSettings.setId(longCount.incrementAndGet());

        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantSettings() throws Exception {
        int databaseSizeBeforeUpdate = tenantSettingsRepository.findAll().size();
        tenantSettings.setId(longCount.incrementAndGet());

        // Create the TenantSettings
        TenantSettingsDTO tenantSettingsDTO = tenantSettingsMapper.toDto(tenantSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantSettings in the database
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantSettings() throws Exception {
        // Initialize the database
        tenantSettingsRepository.saveAndFlush(tenantSettings);

        int databaseSizeBeforeDelete = tenantSettingsRepository.findAll().size();

        // Delete the tenantSettings
        restTenantSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TenantSettings> tenantSettingsList = tenantSettingsRepository.findAll();
        assertThat(tenantSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
