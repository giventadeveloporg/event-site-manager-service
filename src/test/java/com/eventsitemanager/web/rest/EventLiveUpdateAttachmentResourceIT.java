package com.eventsitemanager.web.rest;

import static com.eventsitemanager.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.EventLiveUpdate;
import com.eventsitemanager.domain.EventLiveUpdateAttachment;
import com.eventsitemanager.repository.EventLiveUpdateAttachmentRepository;
import com.eventsitemanager.service.dto.EventLiveUpdateAttachmentDTO;
import com.eventsitemanager.service.mapper.EventLiveUpdateAttachmentMapper;
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
 * Integration tests for the {@link EventLiveUpdateAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventLiveUpdateAttachmentResourceIT {

    private static final String DEFAULT_ATTACHMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTACHMENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;
    private static final Integer SMALLER_DISPLAY_ORDER = 1 - 1;

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/event-live-update-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventLiveUpdateAttachmentRepository eventLiveUpdateAttachmentRepository;

    @Autowired
    private EventLiveUpdateAttachmentMapper eventLiveUpdateAttachmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventLiveUpdateAttachmentMockMvc;

    private EventLiveUpdateAttachment eventLiveUpdateAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLiveUpdateAttachment createEntity(EntityManager em) {
        EventLiveUpdateAttachment eventLiveUpdateAttachment = new EventLiveUpdateAttachment()
            .attachmentType(DEFAULT_ATTACHMENT_TYPE)
            .attachmentUrl(DEFAULT_ATTACHMENT_URL)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
            .metadata(DEFAULT_METADATA)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        EventLiveUpdate eventLiveUpdate;
        if (TestUtil.findAll(em, EventLiveUpdate.class).isEmpty()) {
            eventLiveUpdate = EventLiveUpdateResourceIT.createEntity(em);
            em.persist(eventLiveUpdate);
            em.flush();
        } else {
            eventLiveUpdate = TestUtil.findAll(em, EventLiveUpdate.class).get(0);
        }
        eventLiveUpdateAttachment.setLiveUpdate(eventLiveUpdate);
        return eventLiveUpdateAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventLiveUpdateAttachment createUpdatedEntity(EntityManager em) {
        EventLiveUpdateAttachment eventLiveUpdateAttachment = new EventLiveUpdateAttachment()
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .metadata(UPDATED_METADATA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        EventLiveUpdate eventLiveUpdate;
        if (TestUtil.findAll(em, EventLiveUpdate.class).isEmpty()) {
            eventLiveUpdate = EventLiveUpdateResourceIT.createUpdatedEntity(em);
            em.persist(eventLiveUpdate);
            em.flush();
        } else {
            eventLiveUpdate = TestUtil.findAll(em, EventLiveUpdate.class).get(0);
        }
        eventLiveUpdateAttachment.setLiveUpdate(eventLiveUpdate);
        return eventLiveUpdateAttachment;
    }

    @BeforeEach
    public void initTest() {
        eventLiveUpdateAttachment = createEntity(em);
    }

    @Test
    @Transactional
    void createEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeCreate = eventLiveUpdateAttachmentRepository.findAll().size();
        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeCreate + 1);
        EventLiveUpdateAttachment testEventLiveUpdateAttachment = eventLiveUpdateAttachmentList.get(
            eventLiveUpdateAttachmentList.size() - 1
        );
        assertThat(testEventLiveUpdateAttachment.getAttachmentType()).isEqualTo(DEFAULT_ATTACHMENT_TYPE);
        assertThat(testEventLiveUpdateAttachment.getAttachmentUrl()).isEqualTo(DEFAULT_ATTACHMENT_URL);
        assertThat(testEventLiveUpdateAttachment.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
        assertThat(testEventLiveUpdateAttachment.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testEventLiveUpdateAttachment.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEventLiveUpdateAttachment.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEventLiveUpdateAttachmentWithExistingId() throws Exception {
        // Create the EventLiveUpdateAttachment with an existing ID
        eventLiveUpdateAttachment.setId(1L);
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        int databaseSizeBeforeCreate = eventLiveUpdateAttachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLiveUpdateAttachmentRepository.findAll().size();
        // set the field null
        eventLiveUpdateAttachment.setCreatedAt(null);

        // Create the EventLiveUpdateAttachment, which fails.
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        restEventLiveUpdateAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventLiveUpdateAttachmentRepository.findAll().size();
        // set the field null
        eventLiveUpdateAttachment.setUpdatedAt(null);

        // Create the EventLiveUpdateAttachment, which fails.
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        restEventLiveUpdateAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachments() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList
        restEventLiveUpdateAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLiveUpdateAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentType").value(hasItem(DEFAULT_ATTACHMENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachmentUrl").value(hasItem(DEFAULT_ATTACHMENT_URL)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEventLiveUpdateAttachment() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get the eventLiveUpdateAttachment
        restEventLiveUpdateAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, eventLiveUpdateAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventLiveUpdateAttachment.getId().intValue()))
            .andExpect(jsonPath("$.attachmentType").value(DEFAULT_ATTACHMENT_TYPE))
            .andExpect(jsonPath("$.attachmentUrl").value(DEFAULT_ATTACHMENT_URL))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getEventLiveUpdateAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        Long id = eventLiveUpdateAttachment.getId();

        defaultEventLiveUpdateAttachmentShouldBeFound("id.equals=" + id);
        defaultEventLiveUpdateAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultEventLiveUpdateAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventLiveUpdateAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultEventLiveUpdateAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventLiveUpdateAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentType equals to DEFAULT_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentType.equals=" + DEFAULT_ATTACHMENT_TYPE);

        // Get all the eventLiveUpdateAttachmentList where attachmentType equals to UPDATED_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentType.equals=" + UPDATED_ATTACHMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentType in DEFAULT_ATTACHMENT_TYPE or UPDATED_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentType.in=" + DEFAULT_ATTACHMENT_TYPE + "," + UPDATED_ATTACHMENT_TYPE);

        // Get all the eventLiveUpdateAttachmentList where attachmentType equals to UPDATED_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentType.in=" + UPDATED_ATTACHMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentType is not null
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentType.specified=true");

        // Get all the eventLiveUpdateAttachmentList where attachmentType is null
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentTypeContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentType contains DEFAULT_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentType.contains=" + DEFAULT_ATTACHMENT_TYPE);

        // Get all the eventLiveUpdateAttachmentList where attachmentType contains UPDATED_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentType.contains=" + UPDATED_ATTACHMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentType does not contain DEFAULT_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentType.doesNotContain=" + DEFAULT_ATTACHMENT_TYPE);

        // Get all the eventLiveUpdateAttachmentList where attachmentType does not contain UPDATED_ATTACHMENT_TYPE
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentType.doesNotContain=" + UPDATED_ATTACHMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl equals to DEFAULT_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentUrl.equals=" + DEFAULT_ATTACHMENT_URL);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl equals to UPDATED_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentUrl.equals=" + UPDATED_ATTACHMENT_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentUrlIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl in DEFAULT_ATTACHMENT_URL or UPDATED_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentUrl.in=" + DEFAULT_ATTACHMENT_URL + "," + UPDATED_ATTACHMENT_URL);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl equals to UPDATED_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentUrl.in=" + UPDATED_ATTACHMENT_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl is not null
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentUrl.specified=true");

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl is null
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentUrlContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl contains DEFAULT_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentUrl.contains=" + DEFAULT_ATTACHMENT_URL);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl contains UPDATED_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentUrl.contains=" + UPDATED_ATTACHMENT_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByAttachmentUrlNotContainsSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl does not contain DEFAULT_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldNotBeFound("attachmentUrl.doesNotContain=" + DEFAULT_ATTACHMENT_URL);

        // Get all the eventLiveUpdateAttachmentList where attachmentUrl does not contain UPDATED_ATTACHMENT_URL
        defaultEventLiveUpdateAttachmentShouldBeFound("attachmentUrl.doesNotContain=" + UPDATED_ATTACHMENT_URL);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder equals to DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.equals=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateAttachmentList where displayOrder equals to UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.equals=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder in DEFAULT_DISPLAY_ORDER or UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.in=" + DEFAULT_DISPLAY_ORDER + "," + UPDATED_DISPLAY_ORDER);

        // Get all the eventLiveUpdateAttachmentList where displayOrder equals to UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.in=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is not null
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.specified=true");

        // Get all the eventLiveUpdateAttachmentList where displayOrder is null
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is greater than or equal to DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.greaterThanOrEqual=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is greater than or equal to UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.greaterThanOrEqual=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is less than or equal to DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.lessThanOrEqual=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is less than or equal to SMALLER_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.lessThanOrEqual=" + SMALLER_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is less than DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.lessThan=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is less than UPDATED_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.lessThan=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByDisplayOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is greater than DEFAULT_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldNotBeFound("displayOrder.greaterThan=" + DEFAULT_DISPLAY_ORDER);

        // Get all the eventLiveUpdateAttachmentList where displayOrder is greater than SMALLER_DISPLAY_ORDER
        defaultEventLiveUpdateAttachmentShouldBeFound("displayOrder.greaterThan=" + SMALLER_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt equals to DEFAULT_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateAttachmentList where createdAt equals to UPDATED_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the eventLiveUpdateAttachmentList where createdAt equals to UPDATED_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt is not null
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.specified=true");

        // Get all the eventLiveUpdateAttachmentList where createdAt is null
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateAttachmentList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateAttachmentList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt is less than DEFAULT_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateAttachmentList where createdAt is less than UPDATED_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where createdAt is greater than DEFAULT_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the eventLiveUpdateAttachmentList where createdAt is greater than SMALLER_CREATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateAttachmentList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the eventLiveUpdateAttachmentList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is not null
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.specified=true");

        // Get all the eventLiveUpdateAttachmentList where updatedAt is null
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is less than UPDATED_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the eventLiveUpdateAttachmentList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultEventLiveUpdateAttachmentShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllEventLiveUpdateAttachmentsByLiveUpdateIsEqualToSomething() throws Exception {
        EventLiveUpdate liveUpdate;
        if (TestUtil.findAll(em, EventLiveUpdate.class).isEmpty()) {
            eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);
            liveUpdate = EventLiveUpdateResourceIT.createEntity(em);
        } else {
            liveUpdate = TestUtil.findAll(em, EventLiveUpdate.class).get(0);
        }
        em.persist(liveUpdate);
        em.flush();
        eventLiveUpdateAttachment.setLiveUpdate(liveUpdate);
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);
        Long liveUpdateId = liveUpdate.getId();
        // Get all the eventLiveUpdateAttachmentList where liveUpdate equals to liveUpdateId
        defaultEventLiveUpdateAttachmentShouldBeFound("liveUpdateId.equals=" + liveUpdateId);

        // Get all the eventLiveUpdateAttachmentList where liveUpdate equals to (liveUpdateId + 1)
        defaultEventLiveUpdateAttachmentShouldNotBeFound("liveUpdateId.equals=" + (liveUpdateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventLiveUpdateAttachmentShouldBeFound(String filter) throws Exception {
        restEventLiveUpdateAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventLiveUpdateAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].attachmentType").value(hasItem(DEFAULT_ATTACHMENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachmentUrl").value(hasItem(DEFAULT_ATTACHMENT_URL)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restEventLiveUpdateAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventLiveUpdateAttachmentShouldNotBeFound(String filter) throws Exception {
        restEventLiveUpdateAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventLiveUpdateAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventLiveUpdateAttachment() throws Exception {
        // Get the eventLiveUpdateAttachment
        restEventLiveUpdateAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventLiveUpdateAttachment() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();

        // Update the eventLiveUpdateAttachment
        EventLiveUpdateAttachment updatedEventLiveUpdateAttachment = eventLiveUpdateAttachmentRepository
            .findById(eventLiveUpdateAttachment.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEventLiveUpdateAttachment are not directly saved in db
        em.detach(updatedEventLiveUpdateAttachment);
        updatedEventLiveUpdateAttachment
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .metadata(UPDATED_METADATA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(updatedEventLiveUpdateAttachment);

        restEventLiveUpdateAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLiveUpdateAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
        EventLiveUpdateAttachment testEventLiveUpdateAttachment = eventLiveUpdateAttachmentList.get(
            eventLiveUpdateAttachmentList.size() - 1
        );
        assertThat(testEventLiveUpdateAttachment.getAttachmentType()).isEqualTo(UPDATED_ATTACHMENT_TYPE);
        assertThat(testEventLiveUpdateAttachment.getAttachmentUrl()).isEqualTo(UPDATED_ATTACHMENT_URL);
        assertThat(testEventLiveUpdateAttachment.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testEventLiveUpdateAttachment.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testEventLiveUpdateAttachment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventLiveUpdateAttachment.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();
        eventLiveUpdateAttachment.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventLiveUpdateAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();
        eventLiveUpdateAttachment.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();
        eventLiveUpdateAttachment.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventLiveUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();

        // Update the eventLiveUpdateAttachment using partial update
        EventLiveUpdateAttachment partialUpdatedEventLiveUpdateAttachment = new EventLiveUpdateAttachment();
        partialUpdatedEventLiveUpdateAttachment.setId(eventLiveUpdateAttachment.getId());

        partialUpdatedEventLiveUpdateAttachment
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventLiveUpdateAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLiveUpdateAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLiveUpdateAttachment))
            )
            .andExpect(status().isOk());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
        EventLiveUpdateAttachment testEventLiveUpdateAttachment = eventLiveUpdateAttachmentList.get(
            eventLiveUpdateAttachmentList.size() - 1
        );
        assertThat(testEventLiveUpdateAttachment.getAttachmentType()).isEqualTo(UPDATED_ATTACHMENT_TYPE);
        assertThat(testEventLiveUpdateAttachment.getAttachmentUrl()).isEqualTo(UPDATED_ATTACHMENT_URL);
        assertThat(testEventLiveUpdateAttachment.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
        assertThat(testEventLiveUpdateAttachment.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testEventLiveUpdateAttachment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventLiveUpdateAttachment.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEventLiveUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();

        // Update the eventLiveUpdateAttachment using partial update
        EventLiveUpdateAttachment partialUpdatedEventLiveUpdateAttachment = new EventLiveUpdateAttachment();
        partialUpdatedEventLiveUpdateAttachment.setId(eventLiveUpdateAttachment.getId());

        partialUpdatedEventLiveUpdateAttachment
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .attachmentUrl(UPDATED_ATTACHMENT_URL)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .metadata(UPDATED_METADATA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEventLiveUpdateAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventLiveUpdateAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventLiveUpdateAttachment))
            )
            .andExpect(status().isOk());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
        EventLiveUpdateAttachment testEventLiveUpdateAttachment = eventLiveUpdateAttachmentList.get(
            eventLiveUpdateAttachmentList.size() - 1
        );
        assertThat(testEventLiveUpdateAttachment.getAttachmentType()).isEqualTo(UPDATED_ATTACHMENT_TYPE);
        assertThat(testEventLiveUpdateAttachment.getAttachmentUrl()).isEqualTo(UPDATED_ATTACHMENT_URL);
        assertThat(testEventLiveUpdateAttachment.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testEventLiveUpdateAttachment.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testEventLiveUpdateAttachment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEventLiveUpdateAttachment.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();
        eventLiveUpdateAttachment.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventLiveUpdateAttachmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();
        eventLiveUpdateAttachment.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventLiveUpdateAttachment() throws Exception {
        int databaseSizeBeforeUpdate = eventLiveUpdateAttachmentRepository.findAll().size();
        eventLiveUpdateAttachment.setId(longCount.incrementAndGet());

        // Create the EventLiveUpdateAttachment
        EventLiveUpdateAttachmentDTO eventLiveUpdateAttachmentDTO = eventLiveUpdateAttachmentMapper.toDto(eventLiveUpdateAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventLiveUpdateAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventLiveUpdateAttachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventLiveUpdateAttachment in the database
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventLiveUpdateAttachment() throws Exception {
        // Initialize the database
        eventLiveUpdateAttachmentRepository.saveAndFlush(eventLiveUpdateAttachment);

        int databaseSizeBeforeDelete = eventLiveUpdateAttachmentRepository.findAll().size();

        // Delete the eventLiveUpdateAttachment
        restEventLiveUpdateAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventLiveUpdateAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventLiveUpdateAttachment> eventLiveUpdateAttachmentList = eventLiveUpdateAttachmentRepository.findAll();
        assertThat(eventLiveUpdateAttachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
