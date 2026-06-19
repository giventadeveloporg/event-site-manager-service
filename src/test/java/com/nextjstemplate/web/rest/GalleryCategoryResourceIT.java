package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.domain.GalleryCategory;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.repository.GalleryCategoryRepository;
import com.nextjstemplate.service.dto.GalleryAlbumDTO;
import com.nextjstemplate.service.dto.GalleryCategoryDTO;
import com.nextjstemplate.web.rest.TestUtil;
import jakarta.persistence.EntityManager;
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
 * Integration tests for gallery categories and album category/year enhancements.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GalleryCategoryResourceIT {

    private static final String DEFAULT_TENANT_ID = "tenant_gallery_test_001";
    private static final String CATEGORY_API_URL = "/api/gallery-categories";
    private static final String ALBUM_API_URL = "/api/gallery-albums";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GalleryCategoryRepository galleryCategoryRepository;

    @Autowired
    private GalleryAlbumRepository galleryAlbumRepository;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    void createCategoryThenAlbumWithYearAndCategoryReturnsNestedCategoryOnGet() throws Exception {
        GalleryCategoryDTO categoryDTO = buildCategoryDto("ecumenical-visits", "Ecumenical Visits");

        mockMvc
            .perform(
                post(CATEGORY_API_URL)
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isCreated());

        GalleryCategory createdCategory = galleryCategoryRepository
            .findByTenantIdAndSlug(DEFAULT_TENANT_ID, "ecumenical-visits")
            .orElseThrow();

        GalleryAlbumDTO albumDTO = buildAlbumDto("Russia visit", 2019, createdCategory.getId());

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
            .filter(a -> "Russia visit".equals(a.getTitle()))
            .findFirst()
            .orElseThrow();

        mockMvc
            .perform(get(ALBUM_API_URL + "/{id}", createdAlbum.getId()).header("X-Tenant-ID", DEFAULT_TENANT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.albumYear").value(2019))
            .andExpect(jsonPath("$.galleryCategoryId").value(createdCategory.getId().intValue()))
            .andExpect(jsonPath("$.galleryCategory.slug").value("ecumenical-visits"))
            .andExpect(jsonPath("$.galleryCategory.displayName").value("Ecumenical Visits"));
    }

    @Test
    @Transactional
    void patchAlbumUpdatesYearAndCategory() throws Exception {
        GalleryCategory categoryA = persistCategory("major-events", "Major Events");
        GalleryCategory categoryB = persistCategory("receptions", "Receptions");
        GalleryAlbum album = persistAlbum("Old title", 2018, categoryA);

        GalleryAlbumDTO patch = new GalleryAlbumDTO();
        patch.setId(album.getId());
        patch.setAlbumYear(2020);
        patch.setGalleryCategoryId(categoryB.getId());

        mockMvc
            .perform(
                patch(ALBUM_API_URL + "/{id}", album.getId())
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patch))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.albumYear").value(2020))
            .andExpect(jsonPath("$.galleryCategoryId").value(categoryB.getId().intValue()));
    }

    @Test
    @Transactional
    void filterAlbumsByAlbumYearEquals() throws Exception {
        persistAlbum("Album 2019", 2019, null);
        persistAlbum("Album 2020", 2020, null);

        mockMvc
            .perform(
                get(ALBUM_API_URL)
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .param("tenantId.equals", DEFAULT_TENANT_ID)
                    .param("albumYear.equals", "2019")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].albumYear").value(2019))
            .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    @Transactional
    void deleteCategorySetsAlbumForeignKeyToNull() throws Exception {
        GalleryCategory category = persistCategory("liturgical-events", "Liturgical Events");
        GalleryAlbum album = persistAlbum("Liturgy album", 2021, category);
        assertThat(album.getGalleryCategory()).isNotNull();

        mockMvc
            .perform(delete(CATEGORY_API_URL + "/{id}", category.getId()).header("X-Tenant-ID", DEFAULT_TENANT_ID))
            .andExpect(status().isNoContent());

        em.flush();
        em.clear();

        GalleryAlbum reloaded = galleryAlbumRepository.findById(album.getId()).orElseThrow();
        assertThat(reloaded.getGalleryCategory()).isNull();
    }

    @Test
    @Transactional
    void duplicateCategorySlugReturnsConflict() throws Exception {
        persistCategory("ecumenical-visits", "Ecumenical Visits");

        GalleryCategoryDTO duplicate = buildCategoryDto("ecumenical-visits", "Duplicate");

        mockMvc
            .perform(
                post(CATEGORY_API_URL)
                    .header("X-Tenant-ID", DEFAULT_TENANT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(duplicate))
            )
            .andExpect(status().isConflict());
    }

    private GalleryCategoryDTO buildCategoryDto(String slug, String displayName) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryCategoryDTO dto = new GalleryCategoryDTO();
        dto.setTenantId(DEFAULT_TENANT_ID);
        dto.setSlug(slug);
        dto.setDisplayName(displayName);
        dto.setSortOrder(10);
        dto.setIsActive(true);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return dto;
    }

    private GalleryAlbumDTO buildAlbumDto(String title, Integer albumYear, Long categoryId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryAlbumDTO dto = new GalleryAlbumDTO();
        dto.setTenantId(DEFAULT_TENANT_ID);
        dto.setTitle(title);
        dto.setIsPublic(true);
        dto.setDisplayOrder(0);
        dto.setAlbumYear(albumYear);
        dto.setGalleryCategoryId(categoryId);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        return dto;
    }

    private GalleryCategory persistCategory(String slug, String displayName) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryCategory category = new GalleryCategory()
            .tenantId(DEFAULT_TENANT_ID)
            .slug(slug)
            .displayName(displayName)
            .sortOrder(10)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now);
        return galleryCategoryRepository.saveAndFlush(category);
    }

    private GalleryAlbum persistAlbum(String title, Integer albumYear, GalleryCategory category) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryAlbum album = new GalleryAlbum()
            .tenantId(DEFAULT_TENANT_ID)
            .title(title)
            .isPublic(true)
            .displayOrder(0)
            .albumYear(albumYear)
            .createdAt(now)
            .updatedAt(now);
        if (category != null) {
            album.setGalleryCategory(category);
        }
        return galleryAlbumRepository.save(album);
    }
}
