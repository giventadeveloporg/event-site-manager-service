package com.eventsitemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.TenantOnboardingRequest;
import com.eventsitemanager.domain.UserProfile;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import com.eventsitemanager.repository.SatelliteDomainRepository;
import com.eventsitemanager.repository.TenantEmailAddressRepository;
import com.eventsitemanager.repository.TenantOnboardingRequestRepository;
import com.eventsitemanager.repository.TenantOrganizationRepository;
import com.eventsitemanager.repository.TenantSettingsRepository;
import com.eventsitemanager.repository.UserProfileRepository;
import com.eventsitemanager.service.TenantOnboardingNotificationService;
import com.eventsitemanager.service.dto.TenantOnboardingApproveDTO;
import com.eventsitemanager.service.dto.TenantOnboardingRejectDTO;
import com.eventsitemanager.service.dto.TenantOnboardingSubmitDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link TenantOnboardingRequestResource}.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantOnboardingRequestResourceIT {

    private static final String API = "/api/tenant-onboarding-requests";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TenantOnboardingRequestRepository requestRepository;

    @Autowired
    private TenantOrganizationRepository tenantOrganizationRepository;

    @Autowired
    private TenantSettingsRepository tenantSettingsRepository;

    @Autowired
    private TenantEmailAddressRepository tenantEmailAddressRepository;

    @Autowired
    private SatelliteDomainRepository satelliteDomainRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @MockBean
    private TenantOnboardingNotificationService notificationService;

    private String suffix;

    @BeforeEach
    void initSuffix() {
        suffix = Long.toString(ThreadLocalRandom.current().nextLong(1_000_000L, 9_999_999L));
    }

    private TenantOnboardingSubmitDTO submitDto(String host, String email) {
        TenantOnboardingSubmitDTO dto = new TenantOnboardingSubmitDTO();
        dto.setOrganizationName("Onboarding Test Org " + suffix);
        dto.setContactEmail(email);
        dto.setRequestedHostname(host);
        dto.setContactFirstName("Test");
        dto.setContactLastName("Customer");
        dto.setPrimaryColor("#1E40AF");
        dto.setSecondaryColor("#F59E0B");
        return dto;
    }

    private Long submit(TenantOnboardingSubmitDTO dto) throws Exception {
        String body = mvc
            .perform(post(API + "/submit").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.requestCode").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();
        JsonNode node = om.readTree(body);
        return node.get("id").asLong();
    }

    private TenantOnboardingApproveDTO approveDto(String tenantId, String satelliteKey, String sourceTenant) {
        TenantOnboardingApproveDTO dto = new TenantOnboardingApproveDTO();
        dto.setTenantId(tenantId);
        dto.setSatelliteKey(satelliteKey);
        dto.setAdminSourceTenantId(sourceTenant);
        dto.setCloneAdmins(true);
        dto.setReviewedByEmail("reviewer@example.com");
        return dto;
    }

    @Test
    @Transactional
    void submitNormalizesHostAndEmail() throws Exception {
        Long id = submit(submitDto("HTTPS://WWW.Onb" + suffix + ".Example.com/path", "  Owner" + suffix + "@Example.com "));

        TenantOnboardingRequest saved = requestRepository.findById(id).orElseThrow();
        assertThat(saved.getRequestedHostname()).isEqualTo("www.onb" + suffix + ".example.com");
        assertThat(saved.getContactEmail()).isEqualTo("owner" + suffix + "@example.com");
        assertThat(saved.getStatus()).isEqualTo(OnboardingRequestStatus.PENDING);
        assertThat(saved.getRequestCode()).startsWith("ONB-");
    }

    @Test
    @Transactional
    void submitRejectsDuplicatePendingHostname() throws Exception {
        String host = "www.dup" + suffix + ".example.com";
        submit(submitDto(host, "a" + suffix + "@example.com"));

        mvc
            .perform(
                post(API + "/submit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submitDto(host, "b" + suffix + "@example.com")))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.hostnamepending"));
    }

    @Test
    @Transactional
    void submitRequiresMandatoryFields() throws Exception {
        TenantOnboardingSubmitDTO dto = new TenantOnboardingSubmitDTO();
        dto.setOrganizationName("Missing host");
        dto.setContactEmail("x" + suffix + "@example.com");
        mvc
            .perform(post(API + "/submit").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void approveCreatesTenantInOneTransaction() throws Exception {
        String sourceTenant = "onb_src_" + suffix;
        UserProfile admin = new UserProfile();
        admin.setTenantId(sourceTenant);
        admin.setUserId("user_onb_" + suffix);
        admin.setEmail("admin" + suffix + "@example.com");
        admin.setFirstName("Platform");
        admin.setLastName("Admin");
        admin.setUserRole("ADMIN");
        admin.setUserStatus("APPROVED");
        admin.setCreatedAt(ZonedDateTime.now());
        admin.setUpdatedAt(ZonedDateTime.now());
        userProfileRepository.saveAndFlush(admin);

        String host = "www.appr" + suffix + ".example.com";
        Long id = submit(submitDto(host, "owner" + suffix + "@example.com"));
        String tenantId = "onb_test_" + suffix;
        String satelliteKey = "onb-test-" + suffix;

        mvc
            .perform(
                post(API + "/{id}/approve", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approveDto(tenantId, satelliteKey, sourceTenant)))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"))
            .andExpect(jsonPath("$.assignedTenantId").value(tenantId));

        assertThat(tenantOrganizationRepository.existsByTenantId(tenantId)).isTrue();
        assertThat(tenantSettingsRepository.findByTenantId(tenantId)).isPresent();
        assertThat(satelliteDomainRepository.findBySatelliteKey(satelliteKey))
            .get()
            .satisfies(s -> {
                assertThat(s.getHostname()).isEqualTo(host);
                assertThat(s.getDomain()).isEqualTo("https://" + host);
                assertThat(s.getTenantId()).isEqualTo(tenantId);
            });
        assertThat(tenantEmailAddressRepository.findAll().stream().filter(e -> tenantId.equals(e.getTenantId())).count()).isEqualTo(3);
        assertThat(userProfileRepository.findByUserIdAndTenantId("user_onb_" + suffix, tenantId))
            .get()
            .satisfies(p -> assertThat(p.getUserRole()).isEqualTo("ADMIN"));

        TenantOnboardingRequest saved = requestRepository.findById(id).orElseThrow();
        assertThat(saved.getProvisioningResult()).contains(tenantId).contains("satelliteDomainId");
        assertThat(saved.getApprovedAt()).isNotNull();
    }

    @Test
    @Transactional
    void approveWithTakenTenantIdCreatesNothing() throws Exception {
        String tenantId = "onb_dup_" + suffix;
        Long first = submit(submitDto("www.first" + suffix + ".example.com", "first" + suffix + "@example.com"));
        mvc
            .perform(
                post(API + "/{id}/approve", first)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approveDto(tenantId, "onb-first-" + suffix, "none_" + suffix)))
            )
            .andExpect(status().isOk());

        Long second = submit(submitDto("www.second" + suffix + ".example.com", "second" + suffix + "@example.com"));
        mvc
            .perform(
                post(API + "/{id}/approve", second)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approveDto(tenantId, "onb-second-" + suffix, "none_" + suffix)))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.tenantidtaken"));

        assertThat(satelliteDomainRepository.findBySatelliteKey("onb-second-" + suffix)).isEmpty();
        assertThat(requestRepository.findById(second).orElseThrow().getStatus()).isEqualTo(OnboardingRequestStatus.PENDING);
    }

    @Test
    @Transactional
    void rejectMovesPendingToRejectedAndBlocksApprove() throws Exception {
        Long id = submit(submitDto("www.rej" + suffix + ".example.com", "rej" + suffix + "@example.com"));
        TenantOnboardingRejectDTO reject = new TenantOnboardingRejectDTO();
        reject.setAdminComments("Duplicate of an existing customer");

        mvc
            .perform(post(API + "/{id}/reject", id).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reject)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("REJECTED"));

        mvc
            .perform(
                post(API + "/{id}/approve", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approveDto("onb_rej_" + suffix, "onb-rej-" + suffix, "none_" + suffix)))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.invalidTransition"));
    }
}
