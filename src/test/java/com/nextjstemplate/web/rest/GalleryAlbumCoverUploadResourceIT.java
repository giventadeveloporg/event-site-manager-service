package com.nextjstemplate.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.GalleryAlbum;
import com.nextjstemplate.repository.GalleryAlbumRepository;
import com.nextjstemplate.service.S3Service;
import jakarta.persistence.EntityManager;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GalleryAlbumCoverUploadResourceIT {

    private static final String UPLOAD_URL = "/api/event-medias/upload/gallery-album-cover-image";
    private static final String TENANT_ID = "tenant_gallery_cover_test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GalleryAlbumRepository galleryAlbumRepository;

    @Autowired
    private EntityManager em;

    @MockBean
    private S3Service s3Service;

    private GalleryAlbum album;

    @BeforeEach
    void setUp() {
        when(s3Service.generateGalleryAlbumCoverPath(anyString(), any(), anyString()))
            .thenAnswer(invocation ->
                "dev/media/tenantId/" +
                invocation.getArgument(0) +
                "/gallery-album/album-id/" +
                invocation.getArgument(1) +
                "/cover_test.jpg"
            );
        when(s3Service.uploadFile(anyString(), any())).thenReturn("https://example-bucket.s3.amazonaws.com/gallery-cover.jpg");
    }

    @Test
    @Transactional
    void uploadGalleryAlbumCoverImageUpdatesAlbumCoverUrl() throws Exception {
        album = persistAlbum("Cover Test Album");

        MockMultipartFile file = new MockMultipartFile("file", "cover.jpg", "image/jpeg", "fake-image-data".getBytes());

        mockMvc
            .perform(
                multipart(UPLOAD_URL)
                    .file(file)
                    .param("albumId", album.getId().toString())
                    .param("tenantId", TENANT_ID)
                    .param("isPublic", "true")
                    .header("X-Tenant-ID", TENANT_ID)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.fileUrl").value("https://example-bucket.s3.amazonaws.com/gallery-cover.jpg"))
            .andExpect(jsonPath("$.albumId").value(album.getId().intValue()))
            .andExpect(jsonPath("$.eventMediaType").value("GALLERY_COVER"));

        GalleryAlbum reloaded = galleryAlbumRepository.findById(album.getId()).orElseThrow();
        assertThat(reloaded.getCoverImageUrl()).isEqualTo("https://example-bucket.s3.amazonaws.com/gallery-cover.jpg");
    }

    @Test
    @Transactional
    void uploadGalleryAlbumCoverImageTenantMismatchReturnsForbidden() throws Exception {
        album = persistAlbum("Tenant Mismatch Album");

        MockMultipartFile file = new MockMultipartFile("file", "cover.jpg", "image/jpeg", "fake-image-data".getBytes());

        mockMvc
            .perform(
                multipart(UPLOAD_URL)
                    .file(file)
                    .param("albumId", album.getId().toString())
                    .param("tenantId", "other_tenant")
                    .header("X-Tenant-ID", "other_tenant")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void uploadGalleryAlbumCoverImageInvalidFileTypeReturnsBadRequest() throws Exception {
        album = persistAlbum("Invalid Type Album");

        MockMultipartFile file = new MockMultipartFile("file", "cover.pdf", "application/pdf", "fake-pdf".getBytes());

        mockMvc
            .perform(
                multipart(UPLOAD_URL)
                    .file(file)
                    .param("albumId", album.getId().toString())
                    .param("tenantId", TENANT_ID)
                    .header("X-Tenant-ID", TENANT_ID)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    private GalleryAlbum persistAlbum(String title) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        GalleryAlbum entity = new GalleryAlbum()
            .tenantId(TENANT_ID)
            .title(title)
            .isPublic(true)
            .displayOrder(0)
            .createdAt(now)
            .updatedAt(now);
        return galleryAlbumRepository.saveAndFlush(entity);
    }
}
