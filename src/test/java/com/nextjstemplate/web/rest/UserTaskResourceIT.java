package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
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
 * Integration tests for the {@link UserTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserTaskResourceIT {
    /*private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Long DEFAULT_EVENT_ID = 1L;
    private static final Long UPDATED_EVENT_ID = 2L;
    private static final Long SMALLER_EVENT_ID = 1L - 1L;

    private static final String DEFAULT_ASSIGNEE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNEE_CONTACT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_CONTACT_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNEE_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/user-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserTaskRepository userTaskRepository;

    @Autowired
    private UserTaskMapper userTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserTaskMockMvc;

    private UserTask userTask;

    *//**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static UserTask createEntity(EntityManager em) {
        UserTask userTask = new UserTask()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .dueDate(DEFAULT_DUE_DATE)
            .completed(DEFAULT_COMPLETED)

            .assigneeName(DEFAULT_ASSIGNEE_NAME)
            .assigneeContactPhone(DEFAULT_ASSIGNEE_CONTACT_PHONE)
            .assigneeContactEmail(DEFAULT_ASSIGNEE_CONTACT_EMAIL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return userTask;
    }

    *//**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*
    public static UserTask createUpdatedEntity(EntityManager em) {
        UserTask userTask = new UserTask()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueDate(UPDATED_DUE_DATE)
            .completed(UPDATED_COMPLETED)

            .assigneeName(UPDATED_ASSIGNEE_NAME)
            .assigneeContactPhone(UPDATED_ASSIGNEE_CONTACT_PHONE)
            .assigneeContactEmail(UPDATED_ASSIGNEE_CONTACT_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return userTask;
    }

    @BeforeEach
    public void initTest() {
        userTask = createEntity(em);
    }

    @Test
    @Transactional
    void createUserTask() throws Exception {
        int databaseSizeBeforeCreate = userTaskRepository.findAll().size();
        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);
        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeCreate + 1);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testUserTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserTask.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testUserTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testUserTask.getCompleted()).isEqualTo(DEFAULT_COMPLETED);

        assertThat(testUserTask.getAssigneeName()).isEqualTo(DEFAULT_ASSIGNEE_NAME);
        assertThat(testUserTask.getAssigneeContactPhone()).isEqualTo(DEFAULT_ASSIGNEE_CONTACT_PHONE);
        assertThat(testUserTask.getAssigneeContactEmail()).isEqualTo(DEFAULT_ASSIGNEE_CONTACT_EMAIL);
        assertThat(testUserTask.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserTask.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUserTaskWithExistingId() throws Exception {
        // Create the UserTask with an existing ID
        userTask.setId(1L);
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        int databaseSizeBeforeCreate = userTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null
        userTask.setTitle(null);

        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null
        userTask.setStatus(null);

        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null
        userTask.setPriority(null);

        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null
        userTask.setCompleted(null);

        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null


        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null
        userTask.setCreatedAt(null);

        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userTaskRepository.findAll().size();
        // set the field null
        userTask.setUpdatedAt(null);

        // Create the UserTask, which fails.
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        restUserTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isBadRequest());

        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserTasks() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].assigneeName").value(hasItem(DEFAULT_ASSIGNEE_NAME)))
            .andExpect(jsonPath("$.[*].assigneeContactPhone").value(hasItem(DEFAULT_ASSIGNEE_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].assigneeContactEmail").value(hasItem(DEFAULT_ASSIGNEE_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getUserTask() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get the userTask
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, userTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userTask.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.dueDate").value(sameInstant(DEFAULT_DUE_DATE)))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID.intValue()))
            .andExpect(jsonPath("$.assigneeName").value(DEFAULT_ASSIGNEE_NAME))
            .andExpect(jsonPath("$.assigneeContactPhone").value(DEFAULT_ASSIGNEE_CONTACT_PHONE))
            .andExpect(jsonPath("$.assigneeContactEmail").value(DEFAULT_ASSIGNEE_CONTACT_EMAIL))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getUserTasksByIdFiltering() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        Long id = userTask.getId();

        defaultUserTaskShouldBeFound("id.equals=" + id);
        defaultUserTaskShouldNotBeFound("id.notEquals=" + id);

        defaultUserTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultUserTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserTasksByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where title equals to DEFAULT_TITLE
        defaultUserTaskShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the userTaskList where title equals to UPDATED_TITLE
        defaultUserTaskShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserTasksByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultUserTaskShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the userTaskList where title equals to UPDATED_TITLE
        defaultUserTaskShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserTasksByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where title is not null
        defaultUserTaskShouldBeFound("title.specified=true");

        // Get all the userTaskList where title is null
        defaultUserTaskShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByTitleContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where title contains DEFAULT_TITLE
        defaultUserTaskShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the userTaskList where title contains UPDATED_TITLE
        defaultUserTaskShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserTasksByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where title does not contain DEFAULT_TITLE
        defaultUserTaskShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the userTaskList where title does not contain UPDATED_TITLE
        defaultUserTaskShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where status equals to DEFAULT_STATUS
        defaultUserTaskShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the userTaskList where status equals to UPDATED_STATUS
        defaultUserTaskShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUserTaskShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the userTaskList where status equals to UPDATED_STATUS
        defaultUserTaskShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where status is not null
        defaultUserTaskShouldBeFound("status.specified=true");

        // Get all the userTaskList where status is null
        defaultUserTaskShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByStatusContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where status contains DEFAULT_STATUS
        defaultUserTaskShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the userTaskList where status contains UPDATED_STATUS
        defaultUserTaskShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserTasksByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where status does not contain DEFAULT_STATUS
        defaultUserTaskShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the userTaskList where status does not contain UPDATED_STATUS
        defaultUserTaskShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserTasksByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where priority equals to DEFAULT_PRIORITY
        defaultUserTaskShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the userTaskList where priority equals to UPDATED_PRIORITY
        defaultUserTaskShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUserTasksByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultUserTaskShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the userTaskList where priority equals to UPDATED_PRIORITY
        defaultUserTaskShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUserTasksByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where priority is not null
        defaultUserTaskShouldBeFound("priority.specified=true");

        // Get all the userTaskList where priority is null
        defaultUserTaskShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByPriorityContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where priority contains DEFAULT_PRIORITY
        defaultUserTaskShouldBeFound("priority.contains=" + DEFAULT_PRIORITY);

        // Get all the userTaskList where priority contains UPDATED_PRIORITY
        defaultUserTaskShouldNotBeFound("priority.contains=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUserTasksByPriorityNotContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where priority does not contain DEFAULT_PRIORITY
        defaultUserTaskShouldNotBeFound("priority.doesNotContain=" + DEFAULT_PRIORITY);

        // Get all the userTaskList where priority does not contain UPDATED_PRIORITY
        defaultUserTaskShouldBeFound("priority.doesNotContain=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate equals to DEFAULT_DUE_DATE
        defaultUserTaskShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the userTaskList where dueDate equals to UPDATED_DUE_DATE
        defaultUserTaskShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultUserTaskShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the userTaskList where dueDate equals to UPDATED_DUE_DATE
        defaultUserTaskShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate is not null
        defaultUserTaskShouldBeFound("dueDate.specified=true");

        // Get all the userTaskList where dueDate is null
        defaultUserTaskShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultUserTaskShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the userTaskList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultUserTaskShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultUserTaskShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the userTaskList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultUserTaskShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate is less than DEFAULT_DUE_DATE
        defaultUserTaskShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the userTaskList where dueDate is less than UPDATED_DUE_DATE
        defaultUserTaskShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllUserTasksByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where dueDate is greater than DEFAULT_DUE_DATE
        defaultUserTaskShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the userTaskList where dueDate is greater than SMALLER_DUE_DATE
        defaultUserTaskShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllUserTasksByCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where completed equals to DEFAULT_COMPLETED
        defaultUserTaskShouldBeFound("completed.equals=" + DEFAULT_COMPLETED);

        // Get all the userTaskList where completed equals to UPDATED_COMPLETED
        defaultUserTaskShouldNotBeFound("completed.equals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllUserTasksByCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where completed in DEFAULT_COMPLETED or UPDATED_COMPLETED
        defaultUserTaskShouldBeFound("completed.in=" + DEFAULT_COMPLETED + "," + UPDATED_COMPLETED);

        // Get all the userTaskList where completed equals to UPDATED_COMPLETED
        defaultUserTaskShouldNotBeFound("completed.in=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllUserTasksByCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where completed is not null
        defaultUserTaskShouldBeFound("completed.specified=true");

        // Get all the userTaskList where completed is null
        defaultUserTaskShouldNotBeFound("completed.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId equals to DEFAULT_USER_ID
        defaultUserTaskShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userTaskList where userId equals to UPDATED_USER_ID
        defaultUserTaskShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserTaskShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userTaskList where userId equals to UPDATED_USER_ID
        defaultUserTaskShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId is not null
        defaultUserTaskShouldBeFound("userId.specified=true");

        // Get all the userTaskList where userId is null
        defaultUserTaskShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId is greater than or equal to DEFAULT_USER_ID
        defaultUserTaskShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userTaskList where userId is greater than or equal to UPDATED_USER_ID
        defaultUserTaskShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId is less than or equal to DEFAULT_USER_ID
        defaultUserTaskShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userTaskList where userId is less than or equal to SMALLER_USER_ID
        defaultUserTaskShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId is less than DEFAULT_USER_ID
        defaultUserTaskShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the userTaskList where userId is less than UPDATED_USER_ID
        defaultUserTaskShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where userId is greater than DEFAULT_USER_ID
        defaultUserTaskShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the userTaskList where userId is greater than SMALLER_USER_ID
        defaultUserTaskShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId equals to DEFAULT_EVENT_ID
        defaultUserTaskShouldBeFound("eventId.equals=" + DEFAULT_EVENT_ID);

        // Get all the userTaskList where eventId equals to UPDATED_EVENT_ID
        defaultUserTaskShouldNotBeFound("eventId.equals=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId in DEFAULT_EVENT_ID or UPDATED_EVENT_ID
        defaultUserTaskShouldBeFound("eventId.in=" + DEFAULT_EVENT_ID + "," + UPDATED_EVENT_ID);

        // Get all the userTaskList where eventId equals to UPDATED_EVENT_ID
        defaultUserTaskShouldNotBeFound("eventId.in=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId is not null
        defaultUserTaskShouldBeFound("eventId.specified=true");

        // Get all the userTaskList where eventId is null
        defaultUserTaskShouldNotBeFound("eventId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId is greater than or equal to DEFAULT_EVENT_ID
        defaultUserTaskShouldBeFound("eventId.greaterThanOrEqual=" + DEFAULT_EVENT_ID);

        // Get all the userTaskList where eventId is greater than or equal to UPDATED_EVENT_ID
        defaultUserTaskShouldNotBeFound("eventId.greaterThanOrEqual=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId is less than or equal to DEFAULT_EVENT_ID
        defaultUserTaskShouldBeFound("eventId.lessThanOrEqual=" + DEFAULT_EVENT_ID);

        // Get all the userTaskList where eventId is less than or equal to SMALLER_EVENT_ID
        defaultUserTaskShouldNotBeFound("eventId.lessThanOrEqual=" + SMALLER_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsLessThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId is less than DEFAULT_EVENT_ID
        defaultUserTaskShouldNotBeFound("eventId.lessThan=" + DEFAULT_EVENT_ID);

        // Get all the userTaskList where eventId is less than UPDATED_EVENT_ID
        defaultUserTaskShouldBeFound("eventId.lessThan=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where eventId is greater than DEFAULT_EVENT_ID
        defaultUserTaskShouldNotBeFound("eventId.greaterThan=" + DEFAULT_EVENT_ID);

        // Get all the userTaskList where eventId is greater than SMALLER_EVENT_ID
        defaultUserTaskShouldBeFound("eventId.greaterThan=" + SMALLER_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeName equals to DEFAULT_ASSIGNEE_NAME
        defaultUserTaskShouldBeFound("assigneeName.equals=" + DEFAULT_ASSIGNEE_NAME);

        // Get all the userTaskList where assigneeName equals to UPDATED_ASSIGNEE_NAME
        defaultUserTaskShouldNotBeFound("assigneeName.equals=" + UPDATED_ASSIGNEE_NAME);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeNameIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeName in DEFAULT_ASSIGNEE_NAME or UPDATED_ASSIGNEE_NAME
        defaultUserTaskShouldBeFound("assigneeName.in=" + DEFAULT_ASSIGNEE_NAME + "," + UPDATED_ASSIGNEE_NAME);

        // Get all the userTaskList where assigneeName equals to UPDATED_ASSIGNEE_NAME
        defaultUserTaskShouldNotBeFound("assigneeName.in=" + UPDATED_ASSIGNEE_NAME);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeName is not null
        defaultUserTaskShouldBeFound("assigneeName.specified=true");

        // Get all the userTaskList where assigneeName is null
        defaultUserTaskShouldNotBeFound("assigneeName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeNameContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeName contains DEFAULT_ASSIGNEE_NAME
        defaultUserTaskShouldBeFound("assigneeName.contains=" + DEFAULT_ASSIGNEE_NAME);

        // Get all the userTaskList where assigneeName contains UPDATED_ASSIGNEE_NAME
        defaultUserTaskShouldNotBeFound("assigneeName.contains=" + UPDATED_ASSIGNEE_NAME);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeNameNotContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeName does not contain DEFAULT_ASSIGNEE_NAME
        defaultUserTaskShouldNotBeFound("assigneeName.doesNotContain=" + DEFAULT_ASSIGNEE_NAME);

        // Get all the userTaskList where assigneeName does not contain UPDATED_ASSIGNEE_NAME
        defaultUserTaskShouldBeFound("assigneeName.doesNotContain=" + UPDATED_ASSIGNEE_NAME);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactPhone equals to DEFAULT_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldBeFound("assigneeContactPhone.equals=" + DEFAULT_ASSIGNEE_CONTACT_PHONE);

        // Get all the userTaskList where assigneeContactPhone equals to UPDATED_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldNotBeFound("assigneeContactPhone.equals=" + UPDATED_ASSIGNEE_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactPhone in DEFAULT_ASSIGNEE_CONTACT_PHONE or UPDATED_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldBeFound("assigneeContactPhone.in=" + DEFAULT_ASSIGNEE_CONTACT_PHONE + "," + UPDATED_ASSIGNEE_CONTACT_PHONE);

        // Get all the userTaskList where assigneeContactPhone equals to UPDATED_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldNotBeFound("assigneeContactPhone.in=" + UPDATED_ASSIGNEE_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactPhone is not null
        defaultUserTaskShouldBeFound("assigneeContactPhone.specified=true");

        // Get all the userTaskList where assigneeContactPhone is null
        defaultUserTaskShouldNotBeFound("assigneeContactPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactPhoneContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactPhone contains DEFAULT_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldBeFound("assigneeContactPhone.contains=" + DEFAULT_ASSIGNEE_CONTACT_PHONE);

        // Get all the userTaskList where assigneeContactPhone contains UPDATED_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldNotBeFound("assigneeContactPhone.contains=" + UPDATED_ASSIGNEE_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactPhone does not contain DEFAULT_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldNotBeFound("assigneeContactPhone.doesNotContain=" + DEFAULT_ASSIGNEE_CONTACT_PHONE);

        // Get all the userTaskList where assigneeContactPhone does not contain UPDATED_ASSIGNEE_CONTACT_PHONE
        defaultUserTaskShouldBeFound("assigneeContactPhone.doesNotContain=" + UPDATED_ASSIGNEE_CONTACT_PHONE);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactEmail equals to DEFAULT_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldBeFound("assigneeContactEmail.equals=" + DEFAULT_ASSIGNEE_CONTACT_EMAIL);

        // Get all the userTaskList where assigneeContactEmail equals to UPDATED_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldNotBeFound("assigneeContactEmail.equals=" + UPDATED_ASSIGNEE_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactEmailIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactEmail in DEFAULT_ASSIGNEE_CONTACT_EMAIL or UPDATED_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldBeFound("assigneeContactEmail.in=" + DEFAULT_ASSIGNEE_CONTACT_EMAIL + "," + UPDATED_ASSIGNEE_CONTACT_EMAIL);

        // Get all the userTaskList where assigneeContactEmail equals to UPDATED_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldNotBeFound("assigneeContactEmail.in=" + UPDATED_ASSIGNEE_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactEmail is not null
        defaultUserTaskShouldBeFound("assigneeContactEmail.specified=true");

        // Get all the userTaskList where assigneeContactEmail is null
        defaultUserTaskShouldNotBeFound("assigneeContactEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactEmailContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactEmail contains DEFAULT_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldBeFound("assigneeContactEmail.contains=" + DEFAULT_ASSIGNEE_CONTACT_EMAIL);

        // Get all the userTaskList where assigneeContactEmail contains UPDATED_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldNotBeFound("assigneeContactEmail.contains=" + UPDATED_ASSIGNEE_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserTasksByAssigneeContactEmailNotContainsSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where assigneeContactEmail does not contain DEFAULT_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldNotBeFound("assigneeContactEmail.doesNotContain=" + DEFAULT_ASSIGNEE_CONTACT_EMAIL);

        // Get all the userTaskList where assigneeContactEmail does not contain UPDATED_ASSIGNEE_CONTACT_EMAIL
        defaultUserTaskShouldBeFound("assigneeContactEmail.doesNotContain=" + UPDATED_ASSIGNEE_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserTaskShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userTaskList where createdAt equals to UPDATED_CREATED_AT
        defaultUserTaskShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserTaskShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userTaskList where createdAt equals to UPDATED_CREATED_AT
        defaultUserTaskShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt is not null
        defaultUserTaskShouldBeFound("createdAt.specified=true");

        // Get all the userTaskList where createdAt is null
        defaultUserTaskShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultUserTaskShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userTaskList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultUserTaskShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultUserTaskShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userTaskList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultUserTaskShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt is less than DEFAULT_CREATED_AT
        defaultUserTaskShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the userTaskList where createdAt is less than UPDATED_CREATED_AT
        defaultUserTaskShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where createdAt is greater than DEFAULT_CREATED_AT
        defaultUserTaskShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the userTaskList where createdAt is greater than SMALLER_CREATED_AT
        defaultUserTaskShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserTaskShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userTaskList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserTaskShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserTaskShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userTaskList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserTaskShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt is not null
        defaultUserTaskShouldBeFound("updatedAt.specified=true");

        // Get all the userTaskList where updatedAt is null
        defaultUserTaskShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultUserTaskShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userTaskList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultUserTaskShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultUserTaskShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userTaskList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultUserTaskShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultUserTaskShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the userTaskList where updatedAt is less than UPDATED_UPDATED_AT
        defaultUserTaskShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        // Get all the userTaskList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultUserTaskShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the userTaskList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultUserTaskShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserTasksByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userTaskRepository.saveAndFlush(userTask);
            user = UserProfileResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        userTask.setUser(user);
        userTaskRepository.saveAndFlush(userTask);
        Long userId = user.getId();
        // Get all the userTaskList where user equals to userId
        defaultUserTaskShouldBeFound("userId.equals=" + userId);

        // Get all the userTaskList where user equals to (userId + 1)
        defaultUserTaskShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUserTasksByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            userTaskRepository.saveAndFlush(userTask);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        userTask.setEvent(event);
        userTaskRepository.saveAndFlush(userTask);
        Long eventId = event.getId();
        // Get all the userTaskList where event equals to eventId
        defaultUserTaskShouldBeFound("eventId.equals=" + eventId);

        // Get all the userTaskList where event equals to (eventId + 1)
        defaultUserTaskShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    *//**
     * Executes the search, and checks that the default entity is returned.
     *//*
    private void defaultUserTaskShouldBeFound(String filter) throws Exception {
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].assigneeName").value(hasItem(DEFAULT_ASSIGNEE_NAME)))
            .andExpect(jsonPath("$.[*].assigneeContactPhone").value(hasItem(DEFAULT_ASSIGNEE_CONTACT_PHONE)))
            .andExpect(jsonPath("$.[*].assigneeContactEmail").value(hasItem(DEFAULT_ASSIGNEE_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    *//**
     * Executes the search, and checks that the default entity is not returned.
     *//*
    private void defaultUserTaskShouldNotBeFound(String filter) throws Exception {
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserTask() throws Exception {
        // Get the userTask
        restUserTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserTask() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();

        // Update the userTask
        UserTask updatedUserTask = userTaskRepository.findById(userTask.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserTask are not directly saved in db
        em.detach(updatedUserTask);
        updatedUserTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueDate(UPDATED_DUE_DATE)
            .completed(UPDATED_COMPLETED)

            .assigneeName(UPDATED_ASSIGNEE_NAME)
            .assigneeContactPhone(UPDATED_ASSIGNEE_CONTACT_PHONE)
            .assigneeContactEmail(UPDATED_ASSIGNEE_CONTACT_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(updatedUserTask);

        restUserTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserTask.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testUserTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testUserTask.getCompleted()).isEqualTo(UPDATED_COMPLETED);

        assertThat(testUserTask.getAssigneeName()).isEqualTo(UPDATED_ASSIGNEE_NAME);
        assertThat(testUserTask.getAssigneeContactPhone()).isEqualTo(UPDATED_ASSIGNEE_CONTACT_PHONE);
        assertThat(testUserTask.getAssigneeContactEmail()).isEqualTo(UPDATED_ASSIGNEE_CONTACT_EMAIL);
        assertThat(testUserTask.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserTask.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(longCount.incrementAndGet());

        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(longCount.incrementAndGet());

        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(longCount.incrementAndGet());

        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserTaskWithPatch() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();

        // Update the userTask using partial update
        UserTask partialUpdatedUserTask = new UserTask();
        partialUpdatedUserTask.setId(userTask.getId());

        partialUpdatedUserTask
            .title(UPDATED_TITLE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .completed(UPDATED_COMPLETED)
            .assigneeContactEmail(UPDATED_ASSIGNEE_CONTACT_EMAIL);

        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserTask))
            )
            .andExpect(status().isOk());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserTask.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testUserTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testUserTask.getCompleted()).isEqualTo(UPDATED_COMPLETED);

        assertThat(testUserTask.getAssigneeName()).isEqualTo(DEFAULT_ASSIGNEE_NAME);
        assertThat(testUserTask.getAssigneeContactPhone()).isEqualTo(DEFAULT_ASSIGNEE_CONTACT_PHONE);
        assertThat(testUserTask.getAssigneeContactEmail()).isEqualTo(UPDATED_ASSIGNEE_CONTACT_EMAIL);
        assertThat(testUserTask.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserTask.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserTaskWithPatch() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();

        // Update the userTask using partial update
        UserTask partialUpdatedUserTask = new UserTask();
        partialUpdatedUserTask.setId(userTask.getId());

        partialUpdatedUserTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .dueDate(UPDATED_DUE_DATE)
            .completed(UPDATED_COMPLETED)
            .assigneeName(UPDATED_ASSIGNEE_NAME)
            .assigneeContactPhone(UPDATED_ASSIGNEE_CONTACT_PHONE)
            .assigneeContactEmail(UPDATED_ASSIGNEE_CONTACT_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserTask))
            )
            .andExpect(status().isOk());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
        UserTask testUserTask = userTaskList.get(userTaskList.size() - 1);
        assertThat(testUserTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserTask.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testUserTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testUserTask.getCompleted()).isEqualTo(UPDATED_COMPLETED);

        assertThat(testUserTask.getAssigneeName()).isEqualTo(UPDATED_ASSIGNEE_NAME);
        assertThat(testUserTask.getAssigneeContactPhone()).isEqualTo(UPDATED_ASSIGNEE_CONTACT_PHONE);
        assertThat(testUserTask.getAssigneeContactEmail()).isEqualTo(UPDATED_ASSIGNEE_CONTACT_EMAIL);
        assertThat(testUserTask.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserTask.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(longCount.incrementAndGet());

        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(longCount.incrementAndGet());

        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserTask() throws Exception {
        int databaseSizeBeforeUpdate = userTaskRepository.findAll().size();
        userTask.setId(longCount.incrementAndGet());

        // Create the UserTask
        UserTaskDTO userTaskDTO = userTaskMapper.toDto(userTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserTaskMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserTask in the database
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserTask() throws Exception {
        // Initialize the database
        userTaskRepository.saveAndFlush(userTask);

        int databaseSizeBeforeDelete = userTaskRepository.findAll().size();

        // Delete the userTask
        restUserTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, userTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserTask> userTaskList = userTaskRepository.findAll();
        assertThat(userTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }*/
}
