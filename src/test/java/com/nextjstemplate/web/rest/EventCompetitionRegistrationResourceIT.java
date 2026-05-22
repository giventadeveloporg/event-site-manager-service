package com.nextjstemplate.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.EventCompetition;
import com.nextjstemplate.domain.EventCompetitionParticipant;
import com.nextjstemplate.domain.EventCompetitionRegistration;
import com.nextjstemplate.domain.EventCompetitionSettings;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.enumeration.CompetitionAudienceMode;
import com.nextjstemplate.domain.enumeration.CompetitionEligibleAudience;
import com.nextjstemplate.domain.enumeration.CompetitionParticipantType;
import com.nextjstemplate.domain.enumeration.CompetitionRegistrationMode;
import com.nextjstemplate.domain.enumeration.CompetitionRegistrationStatus;
import com.nextjstemplate.domain.enumeration.CompetitionResultsDisplayMode;
import com.nextjstemplate.domain.enumeration.CompetitionType;
import com.nextjstemplate.repository.EventCompetitionRegistrationRepository;
import com.nextjstemplate.service.dto.EventCompetitionRegistrationDTO;
import com.nextjstemplate.service.mapper.EventCompetitionRegistrationMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link EventCompetitionRegistrationResource} validation rules.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventCompetitionRegistrationResourceIT {

    private static final String ENTITY_API_URL = "/api/event-competition-registrations";
    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";

    @Autowired
    private EventCompetitionRegistrationRepository eventCompetitionRegistrationRepository;

    @Autowired
    private EventCompetitionRegistrationMapper eventCompetitionRegistrationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventCompetitionRegistrationMockMvc;

    private record TestFixture(
        EventDetails event,
        EventCompetitionSettings settings,
        EventCompetition competition,
        UserProfile userProfile,
        EventCompetitionParticipant childParticipant,
        EventCompetitionParticipant adultParticipant
    ) {}

    private TestFixture createFixture(CompetitionEligibleAudience eligibleAudience, Integer maxParticipants) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

        EventDetails event = EventDetailsResourceIT.createEntity(em);
        event.setTenantId(DEFAULT_TENANT_ID);
        event.setIsCompetitionEvent(true);
        em.persist(event);

        EventCompetitionSettings settings = new EventCompetitionSettings()
            .tenantId(DEFAULT_TENANT_ID)
            .audienceMode(CompetitionAudienceMode.MIXED)
            .registrationMode(CompetitionRegistrationMode.SELF)
            .registrationDeadline(now.plusDays(7))
            .registrationOpen(true)
            .allowTicketSales(false)
            .pointsFirst(10)
            .pointsSecond(7)
            .pointsThird(5)
            .championEnabled(false)
            .championExcludeGroupPoints(false)
            .resultsDisplayMode(CompetitionResultsDisplayMode.FULL_NAME)
            .createdAt(now)
            .updatedAt(now);
        settings.setEvent(event);
        em.persist(settings);

        EventCompetition competition = new EventCompetition()
            .tenantId(DEFAULT_TENANT_ID)
            .name("Test Competition")
            .competitionType(CompetitionType.INDIVIDUAL)
            .eligibleAudience(eligibleAudience)
            .feeAmount(BigDecimal.ZERO)
            .requiresSoundtrack(false)
            .displayOrder(1)
            .isActive(true)
            .maxParticipants(maxParticipants)
            .createdAt(now)
            .updatedAt(now);
        competition.setEvent(event);
        em.persist(competition);

        ZonedDateTime profileNow = now;
        UserProfile userProfile = new UserProfile()
            .userId("user_comp_reg_test")
            .firstName("Test")
            .lastName("User")
            .email("comp-reg-test@example.com")
            .createdAt(profileNow)
            .updatedAt(profileNow);
        userProfile.setTenantId(DEFAULT_TENANT_ID);
        em.persist(userProfile);

        EventCompetitionParticipant childParticipant = new EventCompetitionParticipant()
            .tenantId(DEFAULT_TENANT_ID)
            .participantType(CompetitionParticipantType.CHILD)
            .clerkUserId("child_clerk_1")
            .firstName("Child")
            .lastName("One")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now);
        childParticipant.setUserProfile(userProfile);
        em.persist(childParticipant);

        EventCompetitionParticipant adultParticipant = new EventCompetitionParticipant()
            .tenantId(DEFAULT_TENANT_ID)
            .participantType(CompetitionParticipantType.ADULT)
            .clerkUserId("adult_clerk_1")
            .firstName("Adult")
            .lastName("One")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now);
        adultParticipant.setUserProfile(userProfile);
        em.persist(adultParticipant);

        em.flush();
        return new TestFixture(event, settings, competition, userProfile, childParticipant, adultParticipant);
    }

    private EventCompetitionRegistrationDTO buildRegistrationDto(
        TestFixture fixture,
        EventCompetitionParticipant participant,
        CompetitionRegistrationStatus status
    ) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
        EventCompetitionRegistration registration = new EventCompetitionRegistration()
            .tenantId(DEFAULT_TENANT_ID)
            .registrationStatus(status)
            .feeAmount(BigDecimal.ZERO)
            .createdAt(now)
            .updatedAt(now);
        registration.setEvent(fixture.event());
        registration.setCompetition(fixture.competition());
        registration.setParticipantProfile(participant);
        registration.setRegisteredByUserProfile(fixture.userProfile());
        return eventCompetitionRegistrationMapper.toDto(registration);
    }

    @Test
    @Transactional
    void createRegistrationFailsWhenRegistrationClosedByDeadline() throws Exception {
        TestFixture fixture = createFixture(CompetitionEligibleAudience.ALL, null);
        fixture.settings().setRegistrationDeadline(ZonedDateTime.now(ZoneId.systemDefault()).minusDays(1));
        em.merge(fixture.settings());
        em.flush();

        EventCompetitionRegistrationDTO dto = buildRegistrationDto(
            fixture,
            fixture.adultParticipant(),
            CompetitionRegistrationStatus.CONFIRMED
        );

        restEventCompetitionRegistrationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void createRegistrationFailsWhenCapacityExceeded() throws Exception {
        TestFixture fixture = createFixture(CompetitionEligibleAudience.ALL, 1);

        EventCompetitionRegistration existing = new EventCompetitionRegistration()
            .tenantId(DEFAULT_TENANT_ID)
            .registrationStatus(CompetitionRegistrationStatus.CONFIRMED)
            .feeAmount(BigDecimal.ZERO)
            .createdAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now());
        existing.setEvent(fixture.event());
        existing.setCompetition(fixture.competition());
        existing.setParticipantProfile(fixture.adultParticipant());
        existing.setRegisteredByUserProfile(fixture.userProfile());
        em.persist(existing);
        em.flush();

        EventCompetitionParticipant otherParticipant = new EventCompetitionParticipant()
            .tenantId(DEFAULT_TENANT_ID)
            .participantType(CompetitionParticipantType.ADULT)
            .clerkUserId("adult_clerk_2")
            .firstName("Adult")
            .lastName("Two")
            .isActive(true)
            .createdAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now());
        otherParticipant.setUserProfile(fixture.adultParticipant().getUserProfile());
        em.persist(otherParticipant);
        em.flush();

        EventCompetitionRegistrationDTO dto = buildRegistrationDto(fixture, otherParticipant, CompetitionRegistrationStatus.CONFIRMED);

        restEventCompetitionRegistrationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void createRegistrationFailsWhenDuplicateParticipant() throws Exception {
        TestFixture fixture = createFixture(CompetitionEligibleAudience.ALL, null);

        EventCompetitionRegistration existing = new EventCompetitionRegistration()
            .tenantId(DEFAULT_TENANT_ID)
            .registrationStatus(CompetitionRegistrationStatus.CONFIRMED)
            .feeAmount(BigDecimal.ZERO)
            .createdAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now());
        existing.setEvent(fixture.event());
        existing.setCompetition(fixture.competition());
        existing.setParticipantProfile(fixture.adultParticipant());
        existing.setRegisteredByUserProfile(fixture.userProfile());
        em.persist(existing);
        em.flush();

        EventCompetitionRegistrationDTO dto = buildRegistrationDto(
            fixture,
            fixture.adultParticipant(),
            CompetitionRegistrationStatus.CONFIRMED
        );

        restEventCompetitionRegistrationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isConflict());
    }

    @Test
    @Transactional
    void createRegistrationFailsWhenChildOnAdultOnlyCompetition() throws Exception {
        TestFixture fixture = createFixture(CompetitionEligibleAudience.ADULT_ONLY, null);

        EventCompetitionRegistrationDTO dto = buildRegistrationDto(
            fixture,
            fixture.childParticipant(),
            CompetitionRegistrationStatus.CONFIRMED
        );

        restEventCompetitionRegistrationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isBadRequest());
    }
}
