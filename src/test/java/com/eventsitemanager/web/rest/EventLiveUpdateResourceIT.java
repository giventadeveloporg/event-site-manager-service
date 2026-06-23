package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventDetails;
import com.eventsitemanager.domain.EventLiveUpdate;
import com.eventsitemanager.repository.EventLiveUpdateRepository;
import com.eventsitemanager.service.dto.EventLiveUpdateDTO;
import com.eventsitemanager.service.mapper.EventLiveUpdateMapper;
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
 * Integration tests for the {@link EventLiveUpdateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventLiveUpdateResourceIT {

    private static final String DEFAULT_UPDATE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_VIDEO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_LINK_URL = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_LINK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;
    private static final Integer SMALLER_DISPLAY_ORDER = 1 - 1;

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-live-updates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventLiveUpdateRepository eventLiveUpdateRepository;

    @Autowired
    private EventLiveUpdateMapper eventLiveUpdateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventLiveUpdateMockMvc;

    private EventLiveUpdate eventLiveUpdate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLiveUpdate createEntity(EntityManager em) {
        EventLiveUpdate eventLiveUpdate = new EventLiveUpdate()
            .updateType(DEFAULT_UPDATE_TYPE)
            .contentText(DEFAULT_CONTENT_TEXT)
            .contentImageUrl(DEFAULT_CONTENT_IMAGE_URL)
            .contentVideoUrl(DEFAULT_CONTENT_VIDEO_URL)
            .contentLinkUrl(DEFAULT_CONTENT_LINK_URL)
            .metadata(DEFAULT_METADATA)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
            .isDefault(DEFAULT_IS_DEFAULT)
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
        eventLiveUpdate.setEvent(eventDetails);
        return eventLiveUpdate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLiveUpdate createUpdatedEntity(EntityManager em) {
        EventLiveUpdate eventLiveUpdate = new EventLiveUpdate()
            .updateType(UPDATED_UPDATE_TYPE)
            .contentText(UPDATED_CONTENT_TEXT)
            .contentImageUrl(UPDATED_CONTENT_IMAGE_URL)
            .contentVideoUrl(UPDATED_CONTENT_VIDEO_URL)
            .contentLinkUrl(UPDATED_CONTENT_LINK_URL)
            .metadata(UPDATED_METADATA)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isDefault(UPDATED_IS_DEFAULT)
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
        eventLiveUpdate.setEvent(eventDetails);
        return eventLiveUpdate;
    }

    @BeforeEach
    public void initTest() {
        eventLiveUpdate = createEntity(em);
    }

    @Test
    @Transactional
    void createEventLiveUpdate() throws Exception {
        int databaseSizeBeforeCreate = eventLiveUpdateRepository.findAll().size();
        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);
        restEventLiveUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeCreate + 1);
        EventLiveUpdate testEventLiveUpdate = eventLiveUpdateList.get(eventLiveUpdateList.size() - 1);
        assertThat(testEventLiveUpdate.getUpdateType()).isEqualTo(DEFAULT_UPDATE_TYPE);
        assertThat(testEventLiveUpdate.getContentText()).isEqualTo(DEFAULT_CONTENT_TEXT);
        assertThat(testEventLiveUpdate.getContentImageUrl()).isEqualTo(DEFAULT_CONTENT_IMAGE_URL);
        assertThat(testEventLiveUpdate.getContentVideoUrl()).isEqualTo(DEFAULT_CONTENT_VIDEO_URL);
        assertThat(testEventLiveUpdate.getContentLinkUrl()).isEqualTo(DEFAULT_CONTENT_LINK_URL);
        assertThat(testEventLiveUpdate.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testEventLiveUpdate.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
        assertThat(testEventLiveUpdate.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testEventLiveUpdate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventLiveUpdate.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventLiveUpdateWithExistingId() throws Exception {
        // Create the EventLiveUpdate with an existing ID
        eventLiveUpdate.setId(1L);
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        int databaseSizeBeforeCreate = eventLiveUpdateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventLiveUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUpdateTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLiveUpdateRepository.findAll().size();
        // set the field null
        eventLiveUpdate.setUpdateType(null);

        // Create the EventLiveUpdate, which fails.
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        restEventLiveUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLiveUpdateRepository.findAll().size();
        // set the field null
        eventLiveUpdate.setCreatedAt(null);

        // Create the EventLiveUpdate, which fails.
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        restEventLiveUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLiveUpdateRepository.findAll().size();
        // set the field null
        eventLiveUpdate.setUpdatedAt(null);

        // Create the EventLiveUpdate, which fails.
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        restEventLiveUpdateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdates() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList
        restEventLiveUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLiveUpdate.getId().intValue())))
            .andExpect(jsonPath("$.[*].updateType").value(hasItem(DEFAULT_UPDATE_TYPE)))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].contentImageUrl").value(hasItem(DEFAULT_CONTENT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].contentVideoUrl").value(hasItem(DEFAULT_CONTENT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].contentLinkUrl").value(hasItem(DEFAULT_CONTENT_LINK_URL)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventLiveUpdate() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get the eventLiveUpdate
        restEventLiveUpdateMockMvc
            .perform(get(ENTITY_API_URL_ID, eventLiveUpdate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventLiveUpdate.getId().intValue()))
            .andExpect(jsonPath("$.updateType").value(DEFAULT_UPDATE_TYPE))
            .andExpect(jsonPath("$.contentText").value(DEFAULT_CONTENT_TEXT))
            .andExpect(jsonPath("$.contentImageUrl").value(DEFAULT_CONTENT_IMAGE_URL))
            .andExpect(jsonPath("$.contentVideoUrl").value(DEFAULT_CONTENT_VIDEO_URL))
            .andExpect(jsonPath("$.contentLinkUrl").value(DEFAULT_CONTENT_LINK_URL))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA.toString()))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventLiveUpdatesByIdFiltering() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        Long id = eventLiveUpdate.getId();

        defaultEventLiveUpdateShouldBeFound("id.equals=" + id);
        defaultEventLiveUpdateShouldNotBeFound("id.notEquals=" + id);

        defaultEventLiveUpdateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventLiveUpdateShouldNotBeFound("id.greaterThan=" + id);

        defaultEventLiveUpdateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventLiveUpdateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updateType equals to DEFAULT_UPDATE_TYPE
        defaultEventLiveUpdateShouldBeFound("updateType.equals=" + DEFAULT_UPDATE_TYPE);

        // Get all the eventLiveUpdateList where updateType equals to UPDATED_UPDATE_TYPE
        defaultEventLiveUpdateShouldNotBeFound("updateType.equals=" + UPDATED_UPDATE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updateType in DEFAULT_UPDATE_TYPE or UPDATED_UPDATE_TYPE
        defaultEventLiveUpdateShouldBeFound("updateType.in=" + DEFAULT_UPDATE_TYPE + "," + UPDATED_UPDATE_TYPE);

        // Get all the eventLiveUpdateList where updateType equals to UPDATED_UPDATE_TYPE
        defaultEventLiveUpdateShouldNotBeFound("updateType.in=" + UPDATED_UPDATE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updateType is not null
        defaultEventLiveUpdateShouldBeFound("updateType.specified=true");

        // Get all the eventLiveUpdateList where updateType is null
        defaultEventLiveUpdateShouldNotBeFound("updateType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdateTypeContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updateType contains DEFAULT_UPDATE_TYPE
        defaultEventLiveUpdateShouldBeFound("updateType.contains=" + DEFAULT_UPDATE_TYPE);

        // Get all the eventLiveUpdateList where updateType contains UPDATED_UPDATE_TYPE
        defaultEventLiveUpdateShouldNotBeFound("updateType.contains=" + UPDATED_UPDATE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdateTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updateType does not contain DEFAULT_UPDATE_TYPE
        defaultEventLiveUpdateShouldNotBeFound("updateType.doesNotContain=" + DEFAULT_UPDATE_TYPE);

        // Get all the eventLiveUpdateList where updateType does not contain UPDATED_UPDATE_TYPE
        defaultEventLiveUpdateShouldBeFound("updateType.doesNotContain=" + UPDATED_UPDATE_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentTextIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentText equals to DEFAULT_CONTENT_TEXT
        defaultEventLiveUpdateShouldBeFound("contentText.equals=" + DEFAULT_CONTENT_TEXT);

        // Get all the eventLiveUpdateList where contentText equals to UPDATED_CONTENT_TEXT
        defaultEventLiveUpdateShouldNotBeFound("contentText.equals=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentTextIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentText in DEFAULT_CONTENT_TEXT or UPDATED_CONTENT_TEXT
        defaultEventLiveUpdateShouldBeFound("contentText.in=" + DEFAULT_CONTENT_TEXT + "," + UPDATED_CONTENT_TEXT);

        // Get all the eventLiveUpdateList where contentText equals to UPDATED_CONTENT_TEXT
        defaultEventLiveUpdateShouldNotBeFound("contentText.in=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentText is not null
        defaultEventLiveUpdateShouldBeFound("contentText.specified=true");

        // Get all the eventLiveUpdateList where contentText is null
        defaultEventLiveUpdateShouldNotBeFound("contentText.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentTextContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentText contains DEFAULT_CONTENT_TEXT
        defaultEventLiveUpdateShouldBeFound("contentText.contains=" + DEFAULT_CONTENT_TEXT);

        // Get all the eventLiveUpdateList where contentText contains UPDATED_CONTENT_TEXT
        defaultEventLiveUpdateShouldNotBeFound("contentText.contains=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentTextNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentText does not contain DEFAULT_CONTENT_TEXT
        defaultEventLiveUpdateShouldNotBeFound("contentText.doesNotContain=" + DEFAULT_CONTENT_TEXT);

        // Get all the eventLiveUpdateList where contentText does not contain UPDATED_CONTENT_TEXT
        defaultEventLiveUpdateShouldBeFound("contentText.doesNotContain=" + UPDATED_CONTENT_TEXT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentImageUrl equals to DEFAULT_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldBeFound("contentImageUrl.equals=" + DEFAULT_CONTENT_IMAGE_URL);

        // Get all the eventLiveUpdateList where contentImageUrl equals to UPDATED_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldNotBeFound("contentImageUrl.equals=" + UPDATED_CONTENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentImageUrl in DEFAULT_CONTENT_IMAGE_URL or UPDATED_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldBeFound("contentImageUrl.in=" + DEFAULT_CONTENT_IMAGE_URL + "," + UPDATED_CONTENT_IMAGE_URL);

        // Get all the eventLiveUpdateList where contentImageUrl equals to UPDATED_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldNotBeFound("contentImageUrl.in=" + UPDATED_CONTENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentImageUrl is not null
        defaultEventLiveUpdateShouldBeFound("contentImageUrl.specified=true");

        // Get all the eventLiveUpdateList where contentImageUrl is null
        defaultEventLiveUpdateShouldNotBeFound("contentImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentImageUrlContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentImageUrl contains DEFAULT_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldBeFound("contentImageUrl.contains=" + DEFAULT_CONTENT_IMAGE_URL);

        // Get all the eventLiveUpdateList where contentImageUrl contains UPDATED_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldNotBeFound("contentImageUrl.contains=" + UPDATED_CONTENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentImageUrl does not contain DEFAULT_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldNotBeFound("contentImageUrl.doesNotContain=" + DEFAULT_CONTENT_IMAGE_URL);

        // Get all the eventLiveUpdateList where contentImageUrl does not contain UPDATED_CONTENT_IMAGE_URL
        defaultEventLiveUpdateShouldBeFound("contentImageUrl.doesNotContain=" + UPDATED_CONTENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentVideoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentVideoUrl equals to DEFAULT_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldBeFound("contentVideoUrl.equals=" + DEFAULT_CONTENT_VIDEO_URL);

        // Get all the eventLiveUpdateList where contentVideoUrl equals to UPDATED_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldNotBeFound("contentVideoUrl.equals=" + UPDATED_CONTENT_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentVideoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentVideoUrl in DEFAULT_CONTENT_VIDEO_URL or UPDATED_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldBeFound("contentVideoUrl.in=" + DEFAULT_CONTENT_VIDEO_URL + "," + UPDATED_CONTENT_VIDEO_URL);

        // Get all the eventLiveUpdateList where contentVideoUrl equals to UPDATED_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldNotBeFound("contentVideoUrl.in=" + UPDATED_CONTENT_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentVideoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentVideoUrl is not null
        defaultEventLiveUpdateShouldBeFound("contentVideoUrl.specified=true");

        // Get all the eventLiveUpdateList where contentVideoUrl is null
        defaultEventLiveUpdateShouldNotBeFound("contentVideoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentVideoUrlContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentVideoUrl contains DEFAULT_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldBeFound("contentVideoUrl.contains=" + DEFAULT_CONTENT_VIDEO_URL);

        // Get all the eventLiveUpdateList where contentVideoUrl contains UPDATED_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldNotBeFound("contentVideoUrl.contains=" + UPDATED_CONTENT_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentVideoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentVideoUrl does not contain DEFAULT_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldNotBeFound("contentVideoUrl.doesNotContain=" + DEFAULT_CONTENT_VIDEO_URL);

        // Get all the eventLiveUpdateList where contentVideoUrl does not contain UPDATED_CONTENT_VIDEO_URL
        defaultEventLiveUpdateShouldBeFound("contentVideoUrl.doesNotContain=" + UPDATED_CONTENT_VIDEO_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentLinkUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentLinkUrl equals to DEFAULT_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldBeFound("contentLinkUrl.equals=" + DEFAULT_CONTENT_LINK_URL);

        // Get all the eventLiveUpdateList where contentLinkUrl equals to UPDATED_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldNotBeFound("contentLinkUrl.equals=" + UPDATED_CONTENT_LINK_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentLinkUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentLinkUrl in DEFAULT_CONTENT_LINK_URL or UPDATED_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldBeFound("contentLinkUrl.in=" + DEFAULT_CONTENT_LINK_URL + "," + UPDATED_CONTENT_LINK_URL);

        // Get all the eventLiveUpdateList where contentLinkUrl equals to UPDATED_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldNotBeFound("contentLinkUrl.in=" + UPDATED_CONTENT_LINK_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentLinkUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentLinkUrl is not null
        defaultEventLiveUpdateShouldBeFound("contentLinkUrl.specified=true");

        // Get all the eventLiveUpdateList where contentLinkUrl is null
        defaultEventLiveUpdateShouldNotBeFound("contentLinkUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentLinkUrlContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentLinkUrl contains DEFAULT_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldBeFound("contentLinkUrl.contains=" + DEFAULT_CONTENT_LINK_URL);

        // Get all the eventLiveUpdateList where contentLinkUrl contains UPDATED_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldNotBeFound("contentLinkUrl.contains=" + UPDATED_CONTENT_LINK_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByContentLinkUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where contentLinkUrl does not contain DEFAULT_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldNotBeFound("contentLinkUrl.doesNotContain=" + DEFAULT_CONTENT_LINK_URL);

        // Get all the eventLiveUpdateList where contentLinkUrl does not contain UPDATED_CONTENT_LINK_URL
        defaultEventLiveUpdateShouldBeFound("contentLinkUrl.doesNotContain=" + UPDATED_CONTENT_LINK_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder equals to DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateShouldBeFound("displayOrder.equals=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateList where displayOrder equals to UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.equals=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder in DEFAULT_DISPLAY_ORDER or UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateShouldBeFound("displayOrder.in=" + DEFAULT_DISPLAY_ORDER + "," + UPDATED_DISPLAY_ORDER);

        // Get all the eventLiveUpdateList where displayOrder equals to UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.in=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder is not null
        defaultEventLiveUpdateShouldBeFound("displayOrder.specified=true");

        // Get all the eventLiveUpdateList where displayOrder is null
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder is greater than or equal to DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateShouldBeFound("displayOrder.greaterThanOrEqual=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateList where displayOrder is greater than or equal to UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.greaterThanOrEqual=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder is less than or equal to DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateShouldBeFound("displayOrder.lessThanOrEqual=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateList where displayOrder is less than or equal to SMALLER_DISPLAY_ORDER
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.lessThanOrEqual=" + SMALLER_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder is less than DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.lessThan=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateList where displayOrder is less than UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateShouldBeFound("displayOrder.lessThan=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByDisplayOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where displayOrder is greater than DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateShouldNotBeFound("displayOrder.greaterThan=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateList where displayOrder is greater than SMALLER_DISPLAY_ORDER
        defaultEventLiveUpdateShouldBeFound("displayOrder.greaterThan=" + SMALLER_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where isDefault equals to DEFAULT_IS_DEFAULT
        defaultEventLiveUpdateShouldBeFound("isDefault.equals=" + DEFAULT_IS_DEFAULT);

        // Get all the eventLiveUpdateList where isDefault equals to UPDATED_IS_DEFAULT
        defaultEventLiveUpdateShouldNotBeFound("isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where isDefault in DEFAULT_IS_DEFAULT or UPDATED_IS_DEFAULT
        defaultEventLiveUpdateShouldBeFound("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT);

        // Get all the eventLiveUpdateList where isDefault equals to UPDATED_IS_DEFAULT
        defaultEventLiveUpdateShouldNotBeFound("isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where isDefault is not null
        defaultEventLiveUpdateShouldBeFound("isDefault.specified=true");

        // Get all the eventLiveUpdateList where isDefault is null
        defaultEventLiveUpdateShouldNotBeFound("isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventLiveUpdateShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateList where createdAt equals to UPDATED_CREATED_AT
        defaultEventLiveUpdateShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventLiveUpdateShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventLiveUpdateList where createdAt equals to UPDATED_CREATED_AT
        defaultEventLiveUpdateShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt is not null
        defaultEventLiveUpdateShouldBeFound("createdAt.specified=true");

        // Get all the eventLiveUpdateList where createdAt is null
        defaultEventLiveUpdateShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventLiveUpdateShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventLiveUpdateShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventLiveUpdateShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventLiveUpdateShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventLiveUpdateShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateList where createdAt is less than UPDATED_CREATED_AT
        defaultEventLiveUpdateShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventLiveUpdateShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventLiveUpdateShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventLiveUpdateShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventLiveUpdateShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventLiveUpdateList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt is not null
        defaultEventLiveUpdateShouldBeFound("updatedAt.specified=true");

        // Get all the eventLiveUpdateList where updatedAt is null
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventLiveUpdateShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventLiveUpdateShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventLiveUpdateShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        // Get all the eventLiveUpdateList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventLiveUpdateShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventLiveUpdateShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdatesByEventIsEqualToSomething() throws Exception {
        EventDetails event;
        if (TestUtil.findAll(em, EventDetails.class).isEmpty()) {
            eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);
            event = EventDetailsResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, EventDetails.class).get(0);
        }
        em.persist(event);
        em.flush();
        eventLiveUpdate.setEvent(event);
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);
        Long eventId = event.getId();
        // Get all the eventLiveUpdateList where event equals to eventId
        defaultEventLiveUpdateShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventLiveUpdateList where event equals to (eventId + 1)
        defaultEventLiveUpdateShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventLiveUpdateShouldBeFound(String filter) throws Exception {
        restEventLiveUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLiveUpdate.getId().intValue())))
            .andExpect(jsonPath("$.[*].updateType").value(hasItem(DEFAULT_UPDATE_TYPE)))
            .andExpect(jsonPath("$.[*].contentText").value(hasItem(DEFAULT_CONTENT_TEXT)))
            .andExpect(jsonPath("$.[*].contentImageUrl").value(hasItem(DEFAULT_CONTENT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].contentVideoUrl").value(hasItem(DEFAULT_CONTENT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].contentLinkUrl").value(hasItem(DEFAULT_CONTENT_LINK_URL)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventLiveUpdateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventLiveUpdateShouldNotBeFound(String filter) throws Exception {
        restEventLiveUpdateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventLiveUpdateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventLiveUpdate() throws Exception {
        // Get the eventLiveUpdate
        restEventLiveUpdateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventLiveUpdate() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();

        // Update the eventLiveUpdate
        EventLiveUpdate updatedEventLiveUpdate = eventLiveUpdateRepository.findById(eventLiveUpdate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventLiveUpdate are not directly saved in db
        em.detach(updatedEventLiveUpdate);
        updatedEventLiveUpdate
            .updateType(UPDATED_UPDATE_TYPE)
            .contentText(UPDATED_CONTENT_TEXT)
            .contentImageUrl(UPDATED_CONTENT_IMAGE_URL)
            .contentVideoUrl(UPDATED_CONTENT_VIDEO_URL)
            .contentLinkUrl(UPDATED_CONTENT_LINK_URL)
            .metadata(UPDATED_METADATA)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(updatedEventLiveUpdate);

        restEventLiveUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLiveUpdateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
        EventLiveUpdate testEventLiveUpdate = eventLiveUpdateList.get(eventLiveUpdateList.size() - 1);
        assertThat(testEventLiveUpdate.getUpdateType()).isEqualTo(UPDATED_UPDATE_TYPE);
        assertThat(testEventLiveUpdate.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testEventLiveUpdate.getContentImageUrl()).isEqualTo(UPDATED_CONTENT_IMAGE_URL);
        assertThat(testEventLiveUpdate.getContentVideoUrl()).isEqualTo(UPDATED_CONTENT_VIDEO_URL);
        assertThat(testEventLiveUpdate.getContentLinkUrl()).isEqualTo(UPDATED_CONTENT_LINK_URL);
        assertThat(testEventLiveUpdate.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testEventLiveUpdate.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testEventLiveUpdate.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testEventLiveUpdate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventLiveUpdate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventLiveUpdate() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();
        eventLiveUpdate.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLiveUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLiveUpdateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventLiveUpdate() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();
        eventLiveUpdate.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventLiveUpdate() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();
        eventLiveUpdate.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventLiveUpdateWithPatch() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();

        // Update the eventLiveUpdate using partial update
        EventLiveUpdate partialUpdatedEventLiveUpdate = new EventLiveUpdate();
        partialUpdatedEventLiveUpdate.setId(eventLiveUpdate.getId());

        partialUpdatedEventLiveUpdate
            .updateType(UPDATED_UPDATE_TYPE)
            .contentImageUrl(UPDATED_CONTENT_IMAGE_URL)
            .contentVideoUrl(UPDATED_CONTENT_VIDEO_URL)
            .contentLinkUrl(UPDATED_CONTENT_LINK_URL)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .createdAt(UPDATED_CREATED_AT);

        restEventLiveUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLiveUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLiveUpdate))
            )
            .andExpect(status().isOk());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
        EventLiveUpdate testEventLiveUpdate = eventLiveUpdateList.get(eventLiveUpdateList.size() - 1);
        assertThat(testEventLiveUpdate.getUpdateType()).isEqualTo(UPDATED_UPDATE_TYPE);
        assertThat(testEventLiveUpdate.getContentText()).isEqualTo(DEFAULT_CONTENT_TEXT);
        assertThat(testEventLiveUpdate.getContentImageUrl()).isEqualTo(UPDATED_CONTENT_IMAGE_URL);
        assertThat(testEventLiveUpdate.getContentVideoUrl()).isEqualTo(UPDATED_CONTENT_VIDEO_URL);
        assertThat(testEventLiveUpdate.getContentLinkUrl()).isEqualTo(UPDATED_CONTENT_LINK_URL);
        assertThat(testEventLiveUpdate.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testEventLiveUpdate.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testEventLiveUpdate.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testEventLiveUpdate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventLiveUpdate.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventLiveUpdateWithPatch() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();

        // Update the eventLiveUpdate using partial update
        EventLiveUpdate partialUpdatedEventLiveUpdate = new EventLiveUpdate();
        partialUpdatedEventLiveUpdate.setId(eventLiveUpdate.getId());

        partialUpdatedEventLiveUpdate
            .updateType(UPDATED_UPDATE_TYPE)
            .contentText(UPDATED_CONTENT_TEXT)
            .contentImageUrl(UPDATED_CONTENT_IMAGE_URL)
            .contentVideoUrl(UPDATED_CONTENT_VIDEO_URL)
            .contentLinkUrl(UPDATED_CONTENT_LINK_URL)
            .metadata(UPDATED_METADATA)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventLiveUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLiveUpdate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLiveUpdate))
            )
            .andExpect(status().isOk());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
        EventLiveUpdate testEventLiveUpdate = eventLiveUpdateList.get(eventLiveUpdateList.size() - 1);
        assertThat(testEventLiveUpdate.getUpdateType()).isEqualTo(UPDATED_UPDATE_TYPE);
        assertThat(testEventLiveUpdate.getContentText()).isEqualTo(UPDATED_CONTENT_TEXT);
        assertThat(testEventLiveUpdate.getContentImageUrl()).isEqualTo(UPDATED_CONTENT_IMAGE_URL);
        assertThat(testEventLiveUpdate.getContentVideoUrl()).isEqualTo(UPDATED_CONTENT_VIDEO_URL);
        assertThat(testEventLiveUpdate.getContentLinkUrl()).isEqualTo(UPDATED_CONTENT_LINK_URL);
        assertThat(testEventLiveUpdate.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testEventLiveUpdate.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testEventLiveUpdate.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testEventLiveUpdate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventLiveUpdate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventLiveUpdate() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();
        eventLiveUpdate.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLiveUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventLiveUpdateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventLiveUpdate() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();
        eventLiveUpdate.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventLiveUpdate() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateRepository.findAll().size();
        eventLiveUpdate.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdate
        EventLiveUpdateDTO eventLiveUpdateDTO = eventLiveUpdateMapper.toDto(eventLiveUpdate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLiveUpdate in the database
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventLiveUpdate() throws Exception {
        // Initialize the database
        eventLiveUpdateRepository.saveAndFlush(eventLiveUpdate);

        int databaseSizeBeforeDelete = eventLiveUpdateRepository.findAll().size();

        // Delete the eventLiveUpdate
        restEventLiveUpdateMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventLiveUpdate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventLiveUpdate> eventLiveUpdateList = eventLiveUpdateRepository.findAll();
        assertThat(eventLiveUpdateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
