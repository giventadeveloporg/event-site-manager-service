package com.nextjstemplate.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import com.nextjstemplate.web.rest.TestUtil;
import java.time.LocalDate;
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
 * Integration tests for gallery album event date and location enhancements.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GalleryAlbumEventDateLocationResourceIT {

    private static final String DEFAULT_TENANT_ID = "tenant_gallery_event_date_001";
    private static final String ALBUM_API_URL = "/api/gallery-albums";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GalleryAlbumRepository galleryAlbumRepository;

    @Test
    @Transactional
    void createAlbumWithEventDatesAndLocationReturnsIsoDatesOnGet() throws Exception {
        GalleryAlbumDTO albumDTO = buildAlbumDto("Conference album");
        albumDTO.setAlbumYear(2015);
        albumDTO.setEventDateStart(LocalDate.of(2015, 10, 24));
        albumDTO.setEventDateEnd(LocalDate.of(2015, 10, 26));
        albumDTO.setEventLocation("Indore");

        mockMvc
            .perform(
                post(ALBUM_API_URL)
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isCreated());

        GalleryAlbum createdAlbum = galleryAlbumRepository
            .findAll()
            .stream()
            .filter(a -> "Conference album".equals(a.getTitle()))
            .findFirst()
            .orElseThrow();

        mockMvc
            .perform(get(ALBUM_API_URL + "/{id}", createdAlbum.getId()).header("X-Tenant-ID", DEFAULT_TENANT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventDateStart").value("2015-10-24"))
            .andExpect(jsonPath("$.eventDateEnd").value("2015-10-26"))
            .andExpect(jsonPath("$.eventLocation").value("Indore"));
    }

    @Test
    @Transactional
    void patchAlbumClearsEventDateEndWithMergePatchNull() throws Exception {
        GalleryAlbum album = persistAlbumWithEventDates(
            "Multi-day event",
            2015,
            LocalDate.of(2015, 10, 24),
            LocalDate.of(2015, 10, 26),
            "Indore"
        );

        String patchJson =
            "{\"id\":" + album.getId() + ",\"eventDateStart\":\"2015-10-24\",\"eventDateEnd\":null,\"eventLocation\":\"Indore\"}";

        mockMvc
            .perform(
                patch(ALBUM_API_URL + "/{id}", album.getId())
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .contentType("application/merge-patch+json")
                    .content(patchJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventDateStart").value("2015-10-24"))
            .andExpect(jsonPath("$.eventDateEnd").doesNotExist())
            .andExpect(jsonPath("$.eventLocation").value("Indore"));
    }

    @Test
    @Transactional
    void createAlbumRejectsEndDateBeforeStartDate() throws Exception {
        GalleryAlbumDTO albumDTO = buildAlbumDto("Invalid date order");
        albumDTO.setAlbumYear(2015);
        albumDTO.setEventDateStart(LocalDate.of(2015, 10, 26));
        albumDTO.setEventDateEnd(LocalDate.of(2015, 10, 24));

        mockMvc
            .perform(
                post(ALBUM_API_URL)
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(albumDTO))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("error.eventDateOrderInvalid"));
    }

    @Test
    @Transactional
    void getLegacyAlbumWithoutEventFieldsReturnsOk() throws Exception {
        GalleryAlbum album = persistAlbumWithoutEventFields("Legacy album", 2019);

        mockMvc
            .perform(get(ALBUM_API_URL + "/{id}", album.getId()).header("X-Tenant-ID", DEFAULT_TENANT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Legacy album"))
            .andExpect(jsonPath("$.albumYear").value(2019))
            .andExpect(jsonPath("$.eventDateStart").doesNotExist())
            .andExpect(jsonPath("$.eventDateEnd").doesNotExist())
            .andExpect(jsonPath("$.eventLocation").doesNotExist());
    }

    private GalleryAlbumDTO buildAlbumDto(String title) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryAlbumDTO dto = new GalleryAlbumDTO();
        dto.setTenantId(DEFAULT_TENANT_ID);
        dto.setTitle(title);
        dto.setIsPublic(true);
        dto.setDisplayOrder(0);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return dto;
    }

    private GalleryAlbum persistAlbumWithEventDates(
        String title,
        Integer albumYear,
        LocalDate eventDateStart,
        LocalDate eventDateEnd,
        String eventLocation
    ) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryAlbum album = new GalleryAlbum()
            .tenantId(DEFAULT_TENANT_ID)
            .title(title)
            .isPublic(true)
            .displayOrder(0)
            .albumYear(albumYear)
            .eventDateStart(eventDateStart)
            .eventDateEnd(eventDateEnd)
            .eventLocation(eventLocation)
            .createdAt(now)
            .updatedAt(now);
        return galleryAlbumRepository.saveAndFlush(album);
    }

    private GalleryAlbum persistAlbumWithoutEventFields(String title, Integer albumYear) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryAlbum album = new GalleryAlbum()
            .tenantId(DEFAULT_TENANT_ID)
            .title(title)
            .isPublic(true)
            .displayOrder(0)
            .albumYear(albumYear)
            .createdAt(now)
            .updatedAt(now);
        return galleryAlbumRepository.saveAndFlush(album);
    }
}
