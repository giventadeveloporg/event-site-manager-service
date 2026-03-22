package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.TenantSettings;
import com.nextjstemplate.repository.TenantSettingsRepository;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.TenantSettingsService;
import com.nextjstemplate.service.cache.TenantSettingsCacheInvalidation;
import com.nextjstemplate.service.dto.TenantSettingsDTO;
import com.nextjstemplate.service.mapper.TenantSettingsMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.TenantSettings}.
 */
@Service
@Transactional
public class TenantSettingsServiceImpl implements TenantSettingsService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSettingsServiceImpl.class);

    private final TenantSettingsRepository tenantSettingsRepository;

    private final TenantSettingsMapper tenantSettingsMapper;

    private final S3Service s3Service;

    private final TenantSettingsCacheInvalidation tenantSettingsCacheInvalidation;

    public TenantSettingsServiceImpl(
        TenantSettingsRepository tenantSettingsRepository,
        TenantSettingsMapper tenantSettingsMapper,
        S3Service s3Service,
        TenantSettingsCacheInvalidation tenantSettingsCacheInvalidation
    ) {
        this.tenantSettingsRepository = tenantSettingsRepository;
        this.tenantSettingsMapper = tenantSettingsMapper;
        this.s3Service = s3Service;
        this.tenantSettingsCacheInvalidation = tenantSettingsCacheInvalidation;
    }

    @Override
    public TenantSettingsDTO save(TenantSettingsDTO tenantSettingsDTO) {
        LOG.debug("Request to save TenantSettings : {}", tenantSettingsDTO);
        TenantSettings tenantSettings = tenantSettingsMapper.toEntity(tenantSettingsDTO);
        tenantSettings = tenantSettingsRepository.save(tenantSettings);
        tenantSettingsCacheInvalidation.evictForTenantSettingsId(tenantSettings.getId());
        return tenantSettingsMapper.toDto(tenantSettings);
    }

    @Override
    public TenantSettingsDTO update(TenantSettingsDTO tenantSettingsDTO) {
        LOG.debug("Request to update TenantSettings : {}", tenantSettingsDTO);
        // Use merge semantics like PATCH: clients often omit server-managed fields (e.g. homepageCacheVersion).
        // Full toEntity() would map null onto @NotNull fields and fail Bean Validation on commit.
        TenantSettings tenantSettings = tenantSettingsRepository
            .findById(tenantSettingsDTO.getId())
            .orElseThrow(() -> new EntityNotFoundException("TenantSettings not found with id " + tenantSettingsDTO.getId()));
        tenantSettingsMapper.partialUpdate(tenantSettings, tenantSettingsDTO);
        tenantSettings = tenantSettingsRepository.save(tenantSettings);
        tenantSettingsCacheInvalidation.evictForTenantSettingsId(tenantSettings.getId());
        return tenantSettingsMapper.toDto(tenantSettings);
    }

    @Override
    public Optional<TenantSettingsDTO> partialUpdate(TenantSettingsDTO tenantSettingsDTO) {
        LOG.debug("Request to partially update TenantSettings : {}", tenantSettingsDTO);

        Optional<TenantSettingsDTO> result = tenantSettingsRepository
            .findById(tenantSettingsDTO.getId())
            .map(existingTenantSettings -> {
                tenantSettingsMapper.partialUpdate(existingTenantSettings, tenantSettingsDTO);

                return existingTenantSettings;
            })
            .map(tenantSettingsRepository::save)
            .map(tenantSettingsMapper::toDto);
        result.ifPresent(dto -> tenantSettingsCacheInvalidation.evictForTenantSettingsId(dto.getId()));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
        value = "tenantSettings",
        key = "#id",
        // Optional return: Spring may expose the inner DTO as #result in unless — do not call isEmpty() on DTO
        unless = "#result == null || (#result instanceof T(java.util.Optional) && !#result.isPresent())"
    )
    public Optional<TenantSettingsDTO> findOne(Long id) {
        LOG.debug("Request to get TenantSettings : {}", id);
        return tenantSettingsRepository.findById(id).map(tenantSettingsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TenantSettings : {}", id);
        tenantSettingsRepository.deleteById(id);
        tenantSettingsCacheInvalidation.evictForTenantSettingsId(id);
    }

    @Override
    public TenantSettingsDTO uploadEmailFooterHtml(String tenantId, MultipartFile file) {
        LOG.debug("Request to upload email footer HTML for tenant: {}", tenantId);

        // 1. Validate tenant settings exists
        TenantSettings tenantSettings = tenantSettingsRepository
            .findByTenantId(tenantId)
            .orElseThrow(() -> new EntityNotFoundException("Tenant settings not found for tenant: " + tenantId));

        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (HTML files)
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("text/html") && !file.getOriginalFilename().endsWith(".html"))) {
            throw new IllegalArgumentException("File must be an HTML file");
        }

        // Validate file size (max 1MB for HTML)
        if (file.getSize() > 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 1MB");
        }

        // 3. Generate S3 path
        String s3Path = s3Service.generateTenantEmailFooterHtmlPath(tenantId);

        // 4. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        LOG.debug("Uploaded email footer HTML to S3: {}", s3Url);

        // 5. Update tenant_settings table with email footer HTML URL
        try {
            TenantSettingsDTO settingsDTO = tenantSettingsMapper.toDto(tenantSettings);
            settingsDTO.setEmailFooterHtmlUrl(s3Url);
            settingsDTO.setUpdatedAt(ZonedDateTime.now());
            return partialUpdate(settingsDTO)
                .orElseThrow(() -> new RuntimeException("Failed to update tenant settings with email footer HTML URL"));
        } catch (Exception e) {
            LOG.error("Failed to update TenantSettingsDTO with email footer HTML URL for tenant {}: {}", tenantId, e.getMessage(), e);
            // Fallback to direct entity update if service update fails
            try {
                tenantSettings.setEmailFooterHtmlUrl(s3Url);
                tenantSettings.setUpdatedAt(ZonedDateTime.now());
                TenantSettings saved = tenantSettingsRepository.save(tenantSettings);
                tenantSettingsCacheInvalidation.evictForTenantSettingsId(saved.getId());
                LOG.warn("Updated tenant settings {} with email footer HTML URL via direct entity update (fallback)", tenantId);
                return tenantSettingsMapper.toDto(saved);
            } catch (Exception fallbackException) {
                LOG.error(
                    "Failed to update TenantSettings with email footer HTML URL via fallback for tenant {}: {}",
                    tenantId,
                    fallbackException.getMessage(),
                    fallbackException
                );
                throw new RuntimeException("Failed to update tenant settings with email footer HTML URL", fallbackException);
            }
        }
    }

    @Override
    public TenantSettingsDTO uploadTenantLogo(String tenantId, MultipartFile file) {
        LOG.debug("Request to upload tenant logo for tenant: {}", tenantId);

        // 1. Validate tenant settings exists
        TenantSettings tenantSettings = tenantSettingsRepository
            .findByTenantId(tenantId)
            .orElseThrow(() -> new EntityNotFoundException("Tenant settings not found for tenant: " + tenantId));

        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (images only)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate file size (max 5MB for logo)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // 3. Generate S3 path
        String s3Path = s3Service.generateTenantLogoImagePath(tenantId, file.getOriginalFilename());

        // 4. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        LOG.debug("Uploaded tenant logo to S3: {}", s3Url);

        // 5. Update tenant_settings table with logo image URL
        try {
            TenantSettingsDTO settingsDTO = tenantSettingsMapper.toDto(tenantSettings);
            settingsDTO.setLogoImageUrl(s3Url);
            settingsDTO.setUpdatedAt(ZonedDateTime.now());
            return partialUpdate(settingsDTO)
                .orElseThrow(() -> new RuntimeException("Failed to update tenant settings with logo image URL"));
        } catch (Exception e) {
            LOG.error("Failed to update TenantSettingsDTO with logo image URL for tenant {}: {}", tenantId, e.getMessage(), e);
            // Fallback to direct entity update if service update fails
            try {
                tenantSettings.setLogoImageUrl(s3Url);
                tenantSettings.setUpdatedAt(ZonedDateTime.now());
                TenantSettings saved = tenantSettingsRepository.save(tenantSettings);
                tenantSettingsCacheInvalidation.evictForTenantSettingsId(saved.getId());
                LOG.warn("Updated tenant settings {} with logo image URL via direct entity update (fallback)", tenantId);
                return tenantSettingsMapper.toDto(saved);
            } catch (Exception fallbackException) {
                LOG.error(
                    "Failed to update TenantSettings with logo image URL via fallback for tenant {}: {}",
                    tenantId,
                    fallbackException.getMessage(),
                    fallbackException
                );
                throw new RuntimeException("Failed to update tenant settings with logo image URL", fallbackException);
            }
        }
    }

    @Override
    public TenantSettingsDTO uploadEmailHeaderImage(String tenantId, MultipartFile file) {
        LOG.debug("Request to upload email header image for tenant: {}", tenantId);

        // 1. Validate tenant settings exists
        TenantSettings tenantSettings = tenantSettingsRepository
            .findByTenantId(tenantId)
            .orElseThrow(() -> new EntityNotFoundException("Tenant settings not found for tenant: " + tenantId));

        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate file type (images only: PNG, JPG, JPEG, GIF, WEBP)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Validate specific image types
        String filename = file.getOriginalFilename();
        if (filename != null) {
            String lowerFilename = filename.toLowerCase();
            boolean isValidImageType =
                lowerFilename.endsWith(".png") ||
                lowerFilename.endsWith(".jpg") ||
                lowerFilename.endsWith(".jpeg") ||
                lowerFilename.endsWith(".gif") ||
                lowerFilename.endsWith(".webp");
            if (!isValidImageType) {
                throw new IllegalArgumentException("File must be an image (PNG, JPG, JPEG, GIF, or WEBP)");
            }
        }

        // Validate file size (max 5MB for email header image)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // 3. Generate S3 path
        String s3Path = s3Service.generateTenantEmailHeaderImagePath(tenantId, file.getOriginalFilename());

        // 4. Upload to S3
        String s3Url = s3Service.uploadFile(s3Path, file);
        LOG.debug("Uploaded email header image to S3: {}", s3Url);

        // 5. Update tenant_settings table with email header image URL
        try {
            TenantSettingsDTO settingsDTO = tenantSettingsMapper.toDto(tenantSettings);
            settingsDTO.setEmailHeaderImageUrl(s3Url);
            settingsDTO.setUpdatedAt(ZonedDateTime.now());
            return partialUpdate(settingsDTO)
                .orElseThrow(() -> new RuntimeException("Failed to update tenant settings with email header image URL"));
        } catch (Exception e) {
            LOG.error("Failed to update TenantSettingsDTO with email header image URL for tenant {}: {}", tenantId, e.getMessage(), e);
            // Fallback to direct entity update if service update fails
            try {
                tenantSettings.setEmailHeaderImageUrl(s3Url);
                tenantSettings.setUpdatedAt(ZonedDateTime.now());
                TenantSettings saved = tenantSettingsRepository.save(tenantSettings);
                tenantSettingsCacheInvalidation.evictForTenantSettingsId(saved.getId());
                LOG.warn("Updated tenant settings {} with email header image URL via direct entity update (fallback)", tenantId);
                return tenantSettingsMapper.toDto(saved);
            } catch (Exception fallbackException) {
                LOG.error(
                    "Failed to update TenantSettings with email header image URL via fallback for tenant {}: {}",
                    tenantId,
                    fallbackException.getMessage(),
                    fallbackException
                );
                throw new RuntimeException("Failed to update tenant settings with email header image URL", fallbackException);
            }
        }
    }
}
