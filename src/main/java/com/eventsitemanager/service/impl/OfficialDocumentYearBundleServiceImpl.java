package com.eventsitemanager.service.impl;

import com.eventsitemanager.domain.EventMedia;
import com.eventsitemanager.domain.OfficialDocumentCategory;
import com.eventsitemanager.domain.OfficialDocumentYearBundle;
import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.repository.EventMediaRepository;
import com.eventsitemanager.repository.OfficialDocumentCategoryRepository;
import com.eventsitemanager.repository.OfficialDocumentYearBundleRepository;
import com.eventsitemanager.service.OfficialDocumentYearBundleService;
import com.eventsitemanager.service.dto.OfficialDocumentYearBundleDTO;
import com.eventsitemanager.service.mapper.OfficialDocumentYearBundleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for {@link OfficialDocumentYearBundle}.
 */
@Service
@Transactional
public class OfficialDocumentYearBundleServiceImpl implements OfficialDocumentYearBundleService {

    private static final Logger LOG = LoggerFactory.getLogger(OfficialDocumentYearBundleServiceImpl.class);

    private static final String ENTITY_NAME = "officialDocumentYearBundle";

    private final OfficialDocumentYearBundleRepository officialDocumentYearBundleRepository;

    private final OfficialDocumentCategoryRepository officialDocumentCategoryRepository;

    private final EventMediaRepository eventMediaRepository;

    private final OfficialDocumentYearBundleMapper officialDocumentYearBundleMapper;

    public OfficialDocumentYearBundleServiceImpl(
        OfficialDocumentYearBundleRepository officialDocumentYearBundleRepository,
        OfficialDocumentCategoryRepository officialDocumentCategoryRepository,
        EventMediaRepository eventMediaRepository,
        OfficialDocumentYearBundleMapper officialDocumentYearBundleMapper
    ) {
        this.officialDocumentYearBundleRepository = officialDocumentYearBundleRepository;
        this.officialDocumentCategoryRepository = officialDocumentCategoryRepository;
        this.eventMediaRepository = eventMediaRepository;
        this.officialDocumentYearBundleMapper = officialDocumentYearBundleMapper;
    }

    @Override
    public void ensureBundleForUpload(String tenantId, Long officialDocumentCategoryId, Integer documentYear) {
        if (tenantId == null || tenantId.isBlank() || officialDocumentCategoryId == null || documentYear == null) {
            return;
        }
        if (
            officialDocumentYearBundleRepository.existsByTenantIdAndOfficialDocumentCategoryIdAndDocumentYear(
                tenantId,
                officialDocumentCategoryId,
                documentYear
            )
        ) {
            return;
        }
        OfficialDocumentYearBundle bundle = new OfficialDocumentYearBundle();
        bundle.setTenantId(tenantId);
        bundle.setOfficialDocumentCategoryId(officialDocumentCategoryId);
        bundle.setDocumentYear(documentYear);
        bundle.setCoverEventMediaId(null);
        try {
            officialDocumentYearBundleRepository.save(bundle);
        } catch (DataIntegrityViolationException ex) {
            // Concurrent insert winning the unique (tenant, category, year) — treat as success.
            LOG.debug("ensureBundleForUpload: row already exists or concurrent insert: {}", ex.getMessage());
        }
    }

    @Override
    public OfficialDocumentYearBundleDTO save(OfficialDocumentYearBundleDTO dto) {
        LOG.debug("Request to save OfficialDocumentYearBundle : {}", dto);
        validateYear(dto.getDocumentYear());
        validateTenantAndReferences(dto.getTenantId(), dto.getOfficialDocumentCategoryId(), dto.getCoverEventMediaId());

        OfficialDocumentYearBundle entity = officialDocumentYearBundleMapper.toEntity(dto);
        if (entity.getId() != null) {
            LOG.warn("OfficialDocumentYearBundle has ID {} set during create. Clearing ID to force sequence generation.", entity.getId());
            entity.setId(null);
        }
        entity = officialDocumentYearBundleRepository.save(entity);
        return officialDocumentYearBundleMapper.toDto(entity);
    }

    @Override
    public OfficialDocumentYearBundleDTO update(OfficialDocumentYearBundleDTO dto) {
        LOG.debug("Request to update OfficialDocumentYearBundle : {}", dto);
        validateYear(dto.getDocumentYear());
        validateTenantAndReferences(dto.getTenantId(), dto.getOfficialDocumentCategoryId(), dto.getCoverEventMediaId());

        OfficialDocumentYearBundle entity = officialDocumentYearBundleMapper.toEntity(dto);
        entity = officialDocumentYearBundleRepository.save(entity);
        return officialDocumentYearBundleMapper.toDto(entity);
    }

    @Override
    public Optional<OfficialDocumentYearBundleDTO> partialUpdate(OfficialDocumentYearBundleDTO dto) {
        LOG.debug("Request to partially update OfficialDocumentYearBundle : {}", dto);
        return officialDocumentYearBundleRepository
            .findById(dto.getId())
            .map(existing -> {
                officialDocumentYearBundleMapper.partialUpdate(existing, dto);
                validateYear(existing.getDocumentYear());
                validateTenantAndReferences(
                    existing.getTenantId(),
                    existing.getOfficialDocumentCategoryId(),
                    existing.getCoverEventMediaId()
                );
                return existing;
            })
            .map(officialDocumentYearBundleRepository::save)
            .map(officialDocumentYearBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfficialDocumentYearBundleDTO> findAll(Pageable pageable) {
        return officialDocumentYearBundleRepository.findAll(pageable).map(officialDocumentYearBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OfficialDocumentYearBundleDTO> findOne(Long id) {
        return officialDocumentYearBundleRepository.findById(id).map(officialDocumentYearBundleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        officialDocumentYearBundleRepository.deleteById(id);
    }

    private void validateYear(Integer documentYear) {
        if (documentYear == null) {
            throw new BadRequestAlertException("Document year is required", ENTITY_NAME, "yeardownrequired");
        }
        if (documentYear < 1900 || documentYear > 2100) {
            throw new BadRequestAlertException("Document year must be between 1900 and 2100", ENTITY_NAME, "invalidyear");
        }
    }

    private void validateTenantAndReferences(String tenantId, Long officialDocumentCategoryId, Long coverEventMediaId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestAlertException("Tenant ID is required", ENTITY_NAME, "tenantrequired");
        }
        OfficialDocumentCategory category = officialDocumentCategoryRepository
            .findById(officialDocumentCategoryId)
            .orElseThrow(() -> new BadRequestAlertException("Invalid official document category", ENTITY_NAME, "invalidcategory"));
        if (!tenantId.equals(category.getTenantId())) {
            throw new BadRequestAlertException("Tenant must match official document category", ENTITY_NAME, "tenantcategorymismatch");
        }
        if (coverEventMediaId != null) {
            EventMedia media = eventMediaRepository
                .findById(coverEventMediaId)
                .orElseThrow(() -> new BadRequestAlertException("Cover event media not found", ENTITY_NAME, "covermedianotfound"));
            if (media.getTenantId() != null && !tenantId.equals(media.getTenantId())) {
                throw new BadRequestAlertException("Cover media tenant must match bundle tenant", ENTITY_NAME, "covertenantmismatch");
            }
        }
    }
}
