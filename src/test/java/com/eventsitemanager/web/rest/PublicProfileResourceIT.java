package com.eventsitemanager.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eventsitemanager.IntegrationTest;
import com.eventsitemanager.service.dto.PublicProfileDTO;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublicProfileResourceIT {

    private static final String ENTITY_API_URL = "/api/public-profiles";
    private static final String TENANT_ID = "tenant_public_profile_test_001";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void duplicateTenantIdReturnsConflict() throws Exception {
        PublicProfileDTO first = buildDto("First Profile");
        PublicProfileDTO duplicate = buildDto("Duplicate Profile");

        mockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(first)))
            .andExpect(status().isCreated());

        mockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(duplicate)))
            .andExpect(status().isConflict());
    }

    private PublicProfileDTO buildDto(String displayName) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        PublicProfileDTO dto = new PublicProfileDTO();
        dto.setTenantId(TENANT_ID);
        dto.setDisplayName(displayName);
        dto.setContactFormEnabled(Boolean.FALSE);
        dto.setIsPublished(Boolean.FALSE);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return dto;
    }
}
