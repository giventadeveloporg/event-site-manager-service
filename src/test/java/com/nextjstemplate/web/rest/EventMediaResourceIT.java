package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.EventMedia;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.EventMediaRepository;
import com.nextjstemplate.service.dto.EventMediaDTO;
import com.nextjstemplate.service.mapper.EventMediaMapper;
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
import org.springframework.util.Base64Utils;

//import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link EventMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventMediaResourceIT {
    /*
    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_MEDIA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_MEDIA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STORAGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_DATA_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;
    private static final Integer SMALLER_FILE_SIZE = 1 - 1;

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Boolean DEFAULT_EVENT_FLYER = false;
    private static final Boolean UPDATED_EVENT_FLYER = true;

    private static final Boolean DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT = false;
    private static final Boolean UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PRE_SIGNED_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRE_SIGNED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/event-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventMediaRepository eventMediaRepository;

    @Autowired
    private EventMediaMapper eventMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMediaMockMvc;

    private EventMedia eventMedia;

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventMedia createEntity(EntityManager em) {
        EventMedia eventMedia = new EventMedia()
            .tenantId(DEFAULT_TENANT_ID)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .eventMediaType(DEFAULT_EVENT_MEDIA_TYPE)
            .storageType(DEFAULT_STORAGE_TYPE)
            .fileUrl(DEFAULT_FILE_URL)
            .fileData(DEFAULT_FILE_DATA)
            .fileDataContentType(DEFAULT_FILE_DATA_CONTENT_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .isPublic(DEFAULT_IS_PUBLIC)
            .eventFlyer(DEFAULT_EVENT_FLYER)
            .isEventManagementOfficialDocument(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .preSignedUrl(DEFAULT_PRE_SIGNED_URL);
        return eventMedia;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static EventMedia createUpdatedEntity(EntityManager em) {
        EventMedia eventMedia = new EventMedia()
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .isEventManagementOfficialDocument(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL);
        return eventMedia;
    }

    @BeforeEach
    public void initTest() {
        eventMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createEventMedia() throws Exception {
        int databaseSizeBeforeCreate = eventMediaRepository.findAll().size();
        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);
        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isCreated());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeCreate + 1);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testEventMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(DEFAULT_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(DEFAULT_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(DEFAULT_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(DEFAULT_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(DEFAULT_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEventMedia.getPreSignedUrl()).isEqualTo(DEFAULT_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void createEventMediaWithExistingId() throws Exception {
        // Create the EventMedia with an existing ID
        eventMedia.setId(1L);
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        int databaseSizeBeforeCreate = eventMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setTitle(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventMediaTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setEventMediaType(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStorageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setStorageType(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setCreatedAt(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventMediaRepository.findAll().size();
        // set the field null
        eventMedia.setUpdatedAt(null);

        // Create the EventMedia, which fails.
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        restEventMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isBadRequest());

        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventMedias() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventMediaType").value(hasItem(DEFAULT_EVENT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].storageType").value(hasItem(DEFAULT_STORAGE_TYPE)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].eventFlyer").value(hasItem(DEFAULT_EVENT_FLYER.booleanValue())))
            .andExpect(
                jsonPath("$.[*].isEventManagementOfficialDocument")
                    .value(hasItem(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].preSignedUrl").value(hasItem(DEFAULT_PRE_SIGNED_URL)));
    }

    @Test
    @Transactional
    void getEventMedia() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get the eventMedia
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, eventMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventMedia.getId().intValue()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.eventMediaType").value(DEFAULT_EVENT_MEDIA_TYPE))
            .andExpect(jsonPath("$.storageType").value(DEFAULT_STORAGE_TYPE))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.fileDataContentType").value(DEFAULT_FILE_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileData").value(Base64Utils.encodeToString(DEFAULT_FILE_DATA)))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC.booleanValue()))
            .andExpect(jsonPath("$.eventFlyer").value(DEFAULT_EVENT_FLYER.booleanValue()))
            .andExpect(jsonPath("$.isEventManagementOfficialDocument").value(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.preSignedUrl").value(DEFAULT_PRE_SIGNED_URL));
    }

    @Test
    @Transactional
    void getEventMediasByIdFiltering() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        Long id = eventMedia.getId();

        defaultEventMediaShouldBeFound("id.equals=" + id);
        defaultEventMediaShouldNotBeFound("id.notEquals=" + id);

        defaultEventMediaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventMediaShouldNotBeFound("id.greaterThan=" + id);

        defaultEventMediaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventMediaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventMediasByTenantIdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where tenantId equals to DEFAULT_TENANT_ID
        defaultEventMediaShouldBeFound("tenantId.equals=" + DEFAULT_TENANT_ID);

        // Get all the eventMediaList where tenantId equals to UPDATED_TENANT_ID
        defaultEventMediaShouldNotBeFound("tenantId.equals=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventMediasByTenantIdIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where tenantId in DEFAULT_TENANT_ID or UPDATED_TENANT_ID
        defaultEventMediaShouldBeFound("tenantId.in=" + DEFAULT_TENANT_ID + "," + UPDATED_TENANT_ID);

        // Get all the eventMediaList where tenantId equals to UPDATED_TENANT_ID
        defaultEventMediaShouldNotBeFound("tenantId.in=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventMediasByTenantIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where tenantId is not null
        defaultEventMediaShouldBeFound("tenantId.specified=true");

        // Get all the eventMediaList where tenantId is null
        defaultEventMediaShouldNotBeFound("tenantId.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByTenantIdContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where tenantId contains DEFAULT_TENANT_ID
        defaultEventMediaShouldBeFound("tenantId.contains=" + DEFAULT_TENANT_ID);

        // Get all the eventMediaList where tenantId contains UPDATED_TENANT_ID
        defaultEventMediaShouldNotBeFound("tenantId.contains=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventMediasByTenantIdNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where tenantId does not contain DEFAULT_TENANT_ID
        defaultEventMediaShouldNotBeFound("tenantId.doesNotContain=" + DEFAULT_TENANT_ID);

        // Get all the eventMediaList where tenantId does not contain UPDATED_TENANT_ID
        defaultEventMediaShouldBeFound("tenantId.doesNotContain=" + UPDATED_TENANT_ID);
    }

    @Test
    @Transactional
    void getAllEventMediasByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where title equals to DEFAULT_TITLE
        defaultEventMediaShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventMediaList where title equals to UPDATED_TITLE
        defaultEventMediaShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventMediasByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventMediaShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventMediaList where title equals to UPDATED_TITLE
        defaultEventMediaShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventMediasByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where title is not null
        defaultEventMediaShouldBeFound("title.specified=true");

        // Get all the eventMediaList where title is null
        defaultEventMediaShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByTitleContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where title contains DEFAULT_TITLE
        defaultEventMediaShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventMediaList where title contains UPDATED_TITLE
        defaultEventMediaShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventMediasByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where title does not contain DEFAULT_TITLE
        defaultEventMediaShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventMediaList where title does not contain UPDATED_TITLE
        defaultEventMediaShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllEventMediasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where description equals to DEFAULT_DESCRIPTION
        defaultEventMediaShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventMediaList where description equals to UPDATED_DESCRIPTION
        defaultEventMediaShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventMediasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventMediaShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventMediaList where description equals to UPDATED_DESCRIPTION
        defaultEventMediaShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventMediasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where description is not null
        defaultEventMediaShouldBeFound("description.specified=true");

        // Get all the eventMediaList where description is null
        defaultEventMediaShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where description contains DEFAULT_DESCRIPTION
        defaultEventMediaShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventMediaList where description contains UPDATED_DESCRIPTION
        defaultEventMediaShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventMediasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where description does not contain DEFAULT_DESCRIPTION
        defaultEventMediaShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventMediaList where description does not contain UPDATED_DESCRIPTION
        defaultEventMediaShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEventMediasByEventMediaTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventMediaType equals to DEFAULT_EVENT_MEDIA_TYPE
        defaultEventMediaShouldBeFound("eventMediaType.equals=" + DEFAULT_EVENT_MEDIA_TYPE);

        // Get all the eventMediaList where eventMediaType equals to UPDATED_EVENT_MEDIA_TYPE
        defaultEventMediaShouldNotBeFound("eventMediaType.equals=" + UPDATED_EVENT_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByEventMediaTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventMediaType in DEFAULT_EVENT_MEDIA_TYPE or UPDATED_EVENT_MEDIA_TYPE
        defaultEventMediaShouldBeFound("eventMediaType.in=" + DEFAULT_EVENT_MEDIA_TYPE + "," + UPDATED_EVENT_MEDIA_TYPE);

        // Get all the eventMediaList where eventMediaType equals to UPDATED_EVENT_MEDIA_TYPE
        defaultEventMediaShouldNotBeFound("eventMediaType.in=" + UPDATED_EVENT_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByEventMediaTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventMediaType is not null
        defaultEventMediaShouldBeFound("eventMediaType.specified=true");

        // Get all the eventMediaList where eventMediaType is null
        defaultEventMediaShouldNotBeFound("eventMediaType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByEventMediaTypeContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventMediaType contains DEFAULT_EVENT_MEDIA_TYPE
        defaultEventMediaShouldBeFound("eventMediaType.contains=" + DEFAULT_EVENT_MEDIA_TYPE);

        // Get all the eventMediaList where eventMediaType contains UPDATED_EVENT_MEDIA_TYPE
        defaultEventMediaShouldNotBeFound("eventMediaType.contains=" + UPDATED_EVENT_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByEventMediaTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventMediaType does not contain DEFAULT_EVENT_MEDIA_TYPE
        defaultEventMediaShouldNotBeFound("eventMediaType.doesNotContain=" + DEFAULT_EVENT_MEDIA_TYPE);

        // Get all the eventMediaList where eventMediaType does not contain UPDATED_EVENT_MEDIA_TYPE
        defaultEventMediaShouldBeFound("eventMediaType.doesNotContain=" + UPDATED_EVENT_MEDIA_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByStorageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where storageType equals to DEFAULT_STORAGE_TYPE
        defaultEventMediaShouldBeFound("storageType.equals=" + DEFAULT_STORAGE_TYPE);

        // Get all the eventMediaList where storageType equals to UPDATED_STORAGE_TYPE
        defaultEventMediaShouldNotBeFound("storageType.equals=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByStorageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where storageType in DEFAULT_STORAGE_TYPE or UPDATED_STORAGE_TYPE
        defaultEventMediaShouldBeFound("storageType.in=" + DEFAULT_STORAGE_TYPE + "," + UPDATED_STORAGE_TYPE);

        // Get all the eventMediaList where storageType equals to UPDATED_STORAGE_TYPE
        defaultEventMediaShouldNotBeFound("storageType.in=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByStorageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where storageType is not null
        defaultEventMediaShouldBeFound("storageType.specified=true");

        // Get all the eventMediaList where storageType is null
        defaultEventMediaShouldNotBeFound("storageType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByStorageTypeContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where storageType contains DEFAULT_STORAGE_TYPE
        defaultEventMediaShouldBeFound("storageType.contains=" + DEFAULT_STORAGE_TYPE);

        // Get all the eventMediaList where storageType contains UPDATED_STORAGE_TYPE
        defaultEventMediaShouldNotBeFound("storageType.contains=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByStorageTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where storageType does not contain DEFAULT_STORAGE_TYPE
        defaultEventMediaShouldNotBeFound("storageType.doesNotContain=" + DEFAULT_STORAGE_TYPE);

        // Get all the eventMediaList where storageType does not contain UPDATED_STORAGE_TYPE
        defaultEventMediaShouldBeFound("storageType.doesNotContain=" + UPDATED_STORAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileUrl equals to DEFAULT_FILE_URL
        defaultEventMediaShouldBeFound("fileUrl.equals=" + DEFAULT_FILE_URL);

        // Get all the eventMediaList where fileUrl equals to UPDATED_FILE_URL
        defaultEventMediaShouldNotBeFound("fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileUrl in DEFAULT_FILE_URL or UPDATED_FILE_URL
        defaultEventMediaShouldBeFound("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL);

        // Get all the eventMediaList where fileUrl equals to UPDATED_FILE_URL
        defaultEventMediaShouldNotBeFound("fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileUrl is not null
        defaultEventMediaShouldBeFound("fileUrl.specified=true");

        // Get all the eventMediaList where fileUrl is null
        defaultEventMediaShouldNotBeFound("fileUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileUrl contains DEFAULT_FILE_URL
        defaultEventMediaShouldBeFound("fileUrl.contains=" + DEFAULT_FILE_URL);

        // Get all the eventMediaList where fileUrl contains UPDATED_FILE_URL
        defaultEventMediaShouldNotBeFound("fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileUrl does not contain DEFAULT_FILE_URL
        defaultEventMediaShouldNotBeFound("fileUrl.doesNotContain=" + DEFAULT_FILE_URL);

        // Get all the eventMediaList where fileUrl does not contain UPDATED_FILE_URL
        defaultEventMediaShouldBeFound("fileUrl.doesNotContain=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize equals to DEFAULT_FILE_SIZE
        defaultEventMediaShouldBeFound("fileSize.equals=" + DEFAULT_FILE_SIZE);

        // Get all the eventMediaList where fileSize equals to UPDATED_FILE_SIZE
        defaultEventMediaShouldNotBeFound("fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize in DEFAULT_FILE_SIZE or UPDATED_FILE_SIZE
        defaultEventMediaShouldBeFound("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE);

        // Get all the eventMediaList where fileSize equals to UPDATED_FILE_SIZE
        defaultEventMediaShouldNotBeFound("fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize is not null
        defaultEventMediaShouldBeFound("fileSize.specified=true");

        // Get all the eventMediaList where fileSize is null
        defaultEventMediaShouldNotBeFound("fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize is greater than or equal to DEFAULT_FILE_SIZE
        defaultEventMediaShouldBeFound("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the eventMediaList where fileSize is greater than or equal to UPDATED_FILE_SIZE
        defaultEventMediaShouldNotBeFound("fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize is less than or equal to DEFAULT_FILE_SIZE
        defaultEventMediaShouldBeFound("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE);

        // Get all the eventMediaList where fileSize is less than or equal to SMALLER_FILE_SIZE
        defaultEventMediaShouldNotBeFound("fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize is less than DEFAULT_FILE_SIZE
        defaultEventMediaShouldNotBeFound("fileSize.lessThan=" + DEFAULT_FILE_SIZE);

        // Get all the eventMediaList where fileSize is less than UPDATED_FILE_SIZE
        defaultEventMediaShouldBeFound("fileSize.lessThan=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEventMediasByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where fileSize is greater than DEFAULT_FILE_SIZE
        defaultEventMediaShouldNotBeFound("fileSize.greaterThan=" + DEFAULT_FILE_SIZE);

        // Get all the eventMediaList where fileSize is greater than SMALLER_FILE_SIZE
        defaultEventMediaShouldBeFound("fileSize.greaterThan=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEventMediasByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where isPublic equals to DEFAULT_IS_PUBLIC
        defaultEventMediaShouldBeFound("isPublic.equals=" + DEFAULT_IS_PUBLIC);

        // Get all the eventMediaList where isPublic equals to UPDATED_IS_PUBLIC
        defaultEventMediaShouldNotBeFound("isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllEventMediasByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where isPublic in DEFAULT_IS_PUBLIC or UPDATED_IS_PUBLIC
        defaultEventMediaShouldBeFound("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC);

        // Get all the eventMediaList where isPublic equals to UPDATED_IS_PUBLIC
        defaultEventMediaShouldNotBeFound("isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllEventMediasByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where isPublic is not null
        defaultEventMediaShouldBeFound("isPublic.specified=true");

        // Get all the eventMediaList where isPublic is null
        defaultEventMediaShouldNotBeFound("isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByEventFlyerIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventFlyer equals to DEFAULT_EVENT_FLYER
        defaultEventMediaShouldBeFound("eventFlyer.equals=" + DEFAULT_EVENT_FLYER);

        // Get all the eventMediaList where eventFlyer equals to UPDATED_EVENT_FLYER
        defaultEventMediaShouldNotBeFound("eventFlyer.equals=" + UPDATED_EVENT_FLYER);
    }

    @Test
    @Transactional
    void getAllEventMediasByEventFlyerIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventFlyer in DEFAULT_EVENT_FLYER or UPDATED_EVENT_FLYER
        defaultEventMediaShouldBeFound("eventFlyer.in=" + DEFAULT_EVENT_FLYER + "," + UPDATED_EVENT_FLYER);

        // Get all the eventMediaList where eventFlyer equals to UPDATED_EVENT_FLYER
        defaultEventMediaShouldNotBeFound("eventFlyer.in=" + UPDATED_EVENT_FLYER);
    }

    @Test
    @Transactional
    void getAllEventMediasByEventFlyerIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where eventFlyer is not null
        defaultEventMediaShouldBeFound("eventFlyer.specified=true");

        // Get all the eventMediaList where eventFlyer is null
        defaultEventMediaShouldNotBeFound("eventFlyer.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByIsEventManagementOfficialDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where isEventManagementOfficialDocument equals to DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT
        defaultEventMediaShouldBeFound("isEventManagementOfficialDocument.equals=" + DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);

        // Get all the eventMediaList where isEventManagementOfficialDocument equals to UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT
        defaultEventMediaShouldNotBeFound("isEventManagementOfficialDocument.equals=" + UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllEventMediasByIsEventManagementOfficialDocumentIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where isEventManagementOfficialDocument in DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT or UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT
        defaultEventMediaShouldBeFound(
            "isEventManagementOfficialDocument.in=" +
            DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT +
            "," +
            UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT
        );

        // Get all the eventMediaList where isEventManagementOfficialDocument equals to UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT
        defaultEventMediaShouldNotBeFound("isEventManagementOfficialDocument.in=" + UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllEventMediasByIsEventManagementOfficialDocumentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where isEventManagementOfficialDocument is not null
        defaultEventMediaShouldBeFound("isEventManagementOfficialDocument.specified=true");

        // Get all the eventMediaList where isEventManagementOfficialDocument is null
        defaultEventMediaShouldNotBeFound("isEventManagementOfficialDocument.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventMediaShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventMediaList where createdAt equals to UPDATED_CREATED_AT
        defaultEventMediaShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventMediaShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventMediaList where createdAt equals to UPDATED_CREATED_AT
        defaultEventMediaShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt is not null
        defaultEventMediaShouldBeFound("createdAt.specified=true");

        // Get all the eventMediaList where createdAt is null
        defaultEventMediaShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventMediaShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventMediaList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventMediaShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventMediaShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventMediaList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventMediaShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventMediaShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventMediaList where createdAt is less than UPDATED_CREATED_AT
        defaultEventMediaShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventMediaShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventMediaList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventMediaShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventMediaShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventMediaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventMediaShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventMediaShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventMediaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventMediaShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt is not null
        defaultEventMediaShouldBeFound("updatedAt.specified=true");

        // Get all the eventMediaList where updatedAt is null
        defaultEventMediaShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventMediaShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventMediaList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventMediaShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventMediaShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventMediaList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventMediaShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventMediaShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventMediaList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventMediaShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventMediaShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventMediaList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventMediaShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventMediasByPreSignedUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where preSignedUrl equals to DEFAULT_PRE_SIGNED_URL
        defaultEventMediaShouldBeFound("preSignedUrl.equals=" + DEFAULT_PRE_SIGNED_URL);

        // Get all the eventMediaList where preSignedUrl equals to UPDATED_PRE_SIGNED_URL
        defaultEventMediaShouldNotBeFound("preSignedUrl.equals=" + UPDATED_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByPreSignedUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where preSignedUrl in DEFAULT_PRE_SIGNED_URL or UPDATED_PRE_SIGNED_URL
        defaultEventMediaShouldBeFound("preSignedUrl.in=" + DEFAULT_PRE_SIGNED_URL + "," + UPDATED_PRE_SIGNED_URL);

        // Get all the eventMediaList where preSignedUrl equals to UPDATED_PRE_SIGNED_URL
        defaultEventMediaShouldNotBeFound("preSignedUrl.in=" + UPDATED_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByPreSignedUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where preSignedUrl is not null
        defaultEventMediaShouldBeFound("preSignedUrl.specified=true");

        // Get all the eventMediaList where preSignedUrl is null
        defaultEventMediaShouldNotBeFound("preSignedUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEventMediasByPreSignedUrlContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where preSignedUrl contains DEFAULT_PRE_SIGNED_URL
        defaultEventMediaShouldBeFound("preSignedUrl.contains=" + DEFAULT_PRE_SIGNED_URL);

        // Get all the eventMediaList where preSignedUrl contains UPDATED_PRE_SIGNED_URL
        defaultEventMediaShouldNotBeFound("preSignedUrl.contains=" + UPDATED_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void getAllEventMediasByPreSignedUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        // Get all the eventMediaList where preSignedUrl does not contain DEFAULT_PRE_SIGNED_URL
        defaultEventMediaShouldNotBeFound("preSignedUrl.doesNotContain=" + DEFAULT_PRE_SIGNED_URL);

        // Get all the eventMediaList where preSignedUrl does not contain UPDATED_PRE_SIGNED_URL
        defaultEventMediaShouldBeFound("preSignedUrl.doesNotContain=" + UPDATED_PRE_SIGNED_URL);
    }

    *//*@Test
    @Transactional
    void getAllEventMediasByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventMediaRepository.saveAndFlush(eventMedia);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventMedia.setEvent(event);
        eventMediaRepository.saveAndFlush(eventMedia);
        Long eventId = event.getId();
        // Get all the eventMediaList where event equals to eventId
        defaultEventMediaShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventMediaList where event equals to (eventId + 1)
        defaultEventMediaShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }*//*

   *//* @Test
    @Transactional
    void getAllEventMediasByUploadedByIsEqualToSomething() throws Exception {
        UserProfile uploadedBy;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            eventMediaRepository.saveAndFlush(eventMedia);
            uploadedBy = UserProfileResourceIT.createEntity(em);
        } else {
            uploadedBy = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(uploadedBy);
        em.flush();
        eventMedia.setUploadedBy(uploadedBy);
        eventMediaRepository.saveAndFlush(eventMedia);
        Long uploadedById = uploadedBy.getId();
        // Get all the eventMediaList where uploadedBy equals to uploadedById
        defaultEventMediaShouldBeFound("uploadedById.equals=" + uploadedById);

        // Get all the eventMediaList where uploadedBy equals to (uploadedById + 1)
        defaultEventMediaShouldNotBeFound("uploadedById.equals=" + (uploadedById + 1));
    }*//*

    *//**
     * Executes the search, and checks that the default entity is returned.
     *//*
    private void defaultEventMediaShouldBeFound(String filter) throws Exception {
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventMediaType").value(hasItem(DEFAULT_EVENT_MEDIA_TYPE)))
            .andExpect(jsonPath("$.[*].storageType").value(hasItem(DEFAULT_STORAGE_TYPE)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].eventFlyer").value(hasItem(DEFAULT_EVENT_FLYER.booleanValue())))
            .andExpect(
                jsonPath("$.[*].isEventManagementOfficialDocument")
                    .value(hasItem(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].preSignedUrl").value(hasItem(DEFAULT_PRE_SIGNED_URL)));

        // Check, that the count call also returns 1
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    *//**
     * Executes the search, and checks that the default entity is not returned.
     *//*
    private void defaultEventMediaShouldNotBeFound(String filter) throws Exception {
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventMedia() throws Exception {
        // Get the eventMedia
        restEventMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventMedia() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();

        // Update the eventMedia
        EventMedia updatedEventMedia = eventMediaRepository.findById(eventMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventMedia are not directly saved in db
        em.detach(updatedEventMedia);
        updatedEventMedia
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .isEventManagementOfficialDocument(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL);
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(updatedEventMedia);

        restEventMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(UPDATED_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(UPDATED_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEventMedia.getPreSignedUrl()).isEqualTo(UPDATED_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void putNonExistingEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventMediaWithPatch() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();

        // Update the eventMedia using partial update
        EventMedia partialUpdatedEventMedia = new EventMedia();
        partialUpdatedEventMedia.setId(eventMedia.getId());

        partialUpdatedEventMedia
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .updatedAt(UPDATED_UPDATED_AT)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL);

        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventMedia))
            )
            .andExpect(status().isOk());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(UPDATED_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(DEFAULT_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(UPDATED_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(DEFAULT_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEventMedia.getPreSignedUrl()).isEqualTo(UPDATED_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void fullUpdateEventMediaWithPatch() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();

        // Update the eventMedia using partial update
        EventMedia partialUpdatedEventMedia = new EventMedia();
        partialUpdatedEventMedia.setId(eventMedia.getId());

        partialUpdatedEventMedia
            .tenantId(UPDATED_TENANT_ID)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .eventMediaType(UPDATED_EVENT_MEDIA_TYPE)
            .storageType(UPDATED_STORAGE_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileData(UPDATED_FILE_DATA)
            .fileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .isPublic(UPDATED_IS_PUBLIC)
            .eventFlyer(UPDATED_EVENT_FLYER)
            .isEventManagementOfficialDocument(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL);

        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventMedia))
            )
            .andExpect(status().isOk());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
        EventMedia testEventMedia = eventMediaList.get(eventMediaList.size() - 1);
        assertThat(testEventMedia.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testEventMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventMedia.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEventMedia.getEventMediaType()).isEqualTo(UPDATED_EVENT_MEDIA_TYPE);
        assertThat(testEventMedia.getStorageType()).isEqualTo(UPDATED_STORAGE_TYPE);
        assertThat(testEventMedia.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testEventMedia.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testEventMedia.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testEventMedia.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testEventMedia.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
        assertThat(testEventMedia.getEventFlyer()).isEqualTo(UPDATED_EVENT_FLYER);
        assertThat(testEventMedia.getIsEventManagementOfficialDocument()).isEqualTo(UPDATED_IS_EVENT_MANAGEMENT_OFFICIAL_DOCUMENT);
        assertThat(testEventMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEventMedia.getPreSignedUrl()).isEqualTo(UPDATED_PRE_SIGNED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventMedia() throws Exception {
        int databaseSizeBeforeUpdate = eventMediaRepository.findAll().size();
        eventMedia.setId(longCount.incrementAndGet());

        // Create the EventMedia
        EventMediaDTO eventMediaDTO = eventMediaMapper.toDto(eventMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMediaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventMedia in the database
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventMedia() throws Exception {
        // Initialize the database
        eventMediaRepository.saveAndFlush(eventMedia);

        int databaseSizeBeforeDelete = eventMediaRepository.findAll().size();

        // Delete the eventMedia
        restEventMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventMedia> eventMediaList = eventMediaRepository.findAll();
        assertThat(eventMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
