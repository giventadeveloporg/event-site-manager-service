package com.eventsitemanager.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.domain.TenantOrganization;
import com.eventsitemanager.domain.TenantSettings;
import com.eventsitemanager.repository.TenantOrganizationRepository;
import com.eventsitemanager.repository.TenantSettingsRepository;
import com.eventsitemanager.service.dto.TenantOrganizationDTO;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import com.eventsitemanager.web.rest.TestUtil;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for tenant organization identity fields (v2.0) and tenant-settings write restrictions.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantOrganizationAddressDescriptionResourceIT {

    private static final String ORG_API_URL = "/api/tenant-organizations";
    private static final String SETTINGS_API_URL = "/api/tenant-settings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TenantOrganizationRepository tenantOrganizationRepository;

    @Autowired
    private TenantSettingsRepository tenantSettingsRepository;

    @Test
    @Transactional
    void createOrganizationWithFullAddressReturnsFieldsOnGet() throws Exception {
        TenantOrganizationDTO dto = buildOrganizationDto("tenant_org_addr_001", "Malayalee Federation");
        dto.setDescription("Serving the diaspora since 2010.");
        dto.setAddressLine1("123 Main Street");
        dto.setAddressLine2("Suite 200");
        dto.setCity("Dallas");
        dto.setStateProvince("TX");
        dto.setZipCode("75201");
        dto.setCountry("United States");
        dto.setWebsiteUrl("https://www.example.org");

        mockMvc
            .perform(post(ORG_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isCreated());

        TenantOrganization created = tenantOrganizationRepository
            .findAll()
            .stream()
            .filter(o -> "tenant_org_addr_001".equals(o.getTenantId()))
            .findFirst()
            .orElseThrow();

        mockMvc
            .perform(get(ORG_API_URL + "/{id}", created.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Serving the diaspora since 2010."))
            .andExpect(jsonPath("$.addressLine1").value("123 Main Street"))
            .andExpect(jsonPath("$.addressLine2").value("Suite 200"))
            .andExpect(jsonPath("$.city").value("Dallas"))
            .andExpect(jsonPath("$.stateProvince").value("TX"))
            .andExpect(jsonPath("$.zipCode").value("75201"))
            .andExpect(jsonPath("$.country").value("United States"))
            .andExpect(jsonPath("$.websiteUrl").value("https://www.example.org"));
    }

    @Test
    @Transactional
    void patchOrganizationUpdatesDescription() throws Exception {
        TenantOrganization organization = persistOrganization("tenant_org_patch_001", "Patch Org");

        TenantOrganizationDTO patch = new TenantOrganizationDTO();
        patch.setId(organization.getId());
        patch.setDescription("Updated organization description.");

        mockMvc
            .perform(
                patch(ORG_API_URL + "/{id}", organization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patch))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Updated organization description."));
    }

    @Test
    @Transactional
    void createOrganizationRejectsInvalidWebsiteUrl() throws Exception {
        TenantOrganizationDTO dto = buildOrganizationDto("tenant_org_invalid_url", "Invalid URL Org");
        dto.setWebsiteUrl("www.example.org");

        mockMvc
            .perform(post(ORG_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.websiteUrlInvalid"));
    }

    @Test
    @Transactional
    void patchTenantSettingsOperationalFieldsSucceeds() throws Exception {
        TenantSettings settings = persistTenantSettings("tenant_settings_operational_001");

        TenantSettingsDTO patch = new TenantSettingsDTO();
        patch.setId(settings.getId());
        patch.setEmail("info@example.org");
        patch.setPhoneNumber("+1-555-0100");
        patch.setShowEventsSectionInHomePage(true);

        mockMvc
            .perform(
                patch(SETTINGS_API_URL + "/{id}", settings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patch))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("info@example.org"))
            .andExpect(jsonPath("$.phoneNumber").value("+1-555-0100"))
            .andExpect(jsonPath("$.showEventsSectionInHomePage").value(true));
    }

    @Test
    @Transactional
    void patchTenantSettingsWithIdentityFieldsReturns400() throws Exception {
        TenantSettings settings = persistTenantSettings("tenant_settings_identity_reject_001");

        TenantSettingsDTO patch = new TenantSettingsDTO();
        patch.setId(settings.getId());
        patch.setAddressLine1("123 Main Street");

        mockMvc
            .perform(
                patch(SETTINGS_API_URL + "/{id}", settings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patch))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.identityFieldsMovedToTenantOrganization"));
    }

    @Test
    @Transactional
    void getTenantSettingsMayReturnLegacyIdentityFields() throws Exception {
        TenantSettings settings = persistTenantSettings("tenant_settings_legacy_get_001");
        settings.setAddressLine1("Legacy Address");
        settings.setDescription("Legacy description");
        tenantSettingsRepository.saveAndFlush(settings);

        mockMvc
            .perform(get(SETTINGS_API_URL + "/{id}", settings.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addressLine1").value("Legacy Address"))
            .andExpect(jsonPath("$.description").value("Legacy description"));
    }

    private TenantOrganizationDTO buildOrganizationDto(String tenantId, String organizationName) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        TenantOrganizationDTO dto = new TenantOrganizationDTO();
        dto.setTenantId(tenantId);
        dto.setOrganizationName(organizationName);
        dto.setContactEmail("contact@" + tenantId + ".example.com");
        dto.setIsActive(true);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return dto;
    }

    private TenantOrganization persistOrganization(String tenantId, String organizationName) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        TenantOrganization organization = new TenantOrganization()
            .tenantId(tenantId)
            .organizationName(organizationName)
            .contactEmail("contact@" + tenantId + ".example.com")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now);
        return tenantOrganizationRepository.saveAndFlush(organization);
    }

    private TenantSettings persistTenantSettings(String tenantId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        TenantSettings settings = new TenantSettings()
            .tenantId(tenantId)
            .allowUserRegistration(false)
            .requireAdminApproval(false)
            .enableWhatsappIntegration(false)
            .enableEmailMarketing(false)
            .whatsappApiKey("test-key")
            .emailProviderConfig("{}")
            .customCss("")
            .customJs("")
            .showEventsSectionInHomePage(true)
            .showTeamMembersSectionInHomePage(true)
            .showSponsorsSectionInHomePage(true)
            .createdAt(now)
            .updatedAt(now);
        return tenantSettingsRepository.saveAndFlush(settings);
    }
}
