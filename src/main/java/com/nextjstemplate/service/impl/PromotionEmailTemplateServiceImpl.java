package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.DiscountCode;
import com.nextjstemplate.domain.EventDetails;
import com.nextjstemplate.domain.PromotionEmailTemplate;
import com.nextjstemplate.repository.DiscountCodeRepository;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.repository.PromotionEmailTemplateRepository;
import com.nextjstemplate.service.PromotionEmailTemplateService;
import com.nextjstemplate.service.dto.PromotionEmailTemplateDTO;
import com.nextjstemplate.service.dto.PromotionEmailTemplateFormDTO;
import com.nextjstemplate.service.mapper.PromotionEmailTemplateMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.PromotionEmailTemplate}.
 */
@Service
@Transactional
public class PromotionEmailTemplateServiceImpl implements PromotionEmailTemplateService {

    private final Logger log = LoggerFactory.getLogger(PromotionEmailTemplateServiceImpl.class);

    private final PromotionEmailTemplateRepository promotionEmailTemplateRepository;

    private final PromotionEmailTemplateMapper promotionEmailTemplateMapper;

    private final EventDetailsRepository eventRepository;

    private final DiscountCodeRepository discountCodeRepository;

    public PromotionEmailTemplateServiceImpl(
        PromotionEmailTemplateRepository promotionEmailTemplateRepository,
        PromotionEmailTemplateMapper promotionEmailTemplateMapper,
        EventDetailsRepository eventRepository,
        DiscountCodeRepository discountCodeRepository
    ) {
        this.promotionEmailTemplateRepository = promotionEmailTemplateRepository;
        this.promotionEmailTemplateMapper = promotionEmailTemplateMapper;
        this.eventRepository = eventRepository;
        this.discountCodeRepository = discountCodeRepository;
    }

    @Override
    public PromotionEmailTemplateDTO createTemplate(PromotionEmailTemplateFormDTO formDTO, String tenantId, Long userId) {
        log.debug("Request to create PromotionEmailTemplate: {}", formDTO);

        // Validate event exists and belongs to tenant
        EventDetails event = eventRepository
            .findById(formDTO.getEventId())
            .orElseThrow(() -> new EntityNotFoundException("Event not found: " + formDTO.getEventId()));

        if (!event.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Event does not belong to the specified tenant");
        }

        // Validate template name uniqueness
        if (!validateTemplateName(formDTO.getTemplateName(), formDTO.getEventId(), tenantId, null)) {
            throw new IllegalArgumentException("Template name '" + formDTO.getTemplateName() + "' already exists for this event");
        }

        // Validate discount code if provided
        if (formDTO.getDiscountCodeId() != null) {
            DiscountCode discountCode = discountCodeRepository
                .findById(formDTO.getDiscountCodeId())
                .orElseThrow(() -> new EntityNotFoundException("Discount code not found: " + formDTO.getDiscountCodeId()));

            if (!discountCode.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Discount code does not belong to the specified tenant");
            }
            if (!discountCode.getEventId().equals(formDTO.getEventId())) {
                throw new IllegalArgumentException("Discount code does not belong to the specified event");
            }
        }

        // Create entity from form DTO
        PromotionEmailTemplate template = new PromotionEmailTemplate();
        template.setTenantId(tenantId);
        template.setEventId(formDTO.getEventId());
        template.setTemplateName(formDTO.getTemplateName());
        template.setSubject(formDTO.getSubject());
        template.setFromEmail(formDTO.getFromEmail());
        template.setBodyHtml(formDTO.getBodyHtml());
        template.setFooterHtml(formDTO.getFooterHtml());
        template.setHeaderImageUrl(formDTO.getHeaderImageUrl());
        template.setFooterImageUrl(formDTO.getFooterImageUrl());
        template.setPromotionCode(formDTO.getDiscountCodeId() != null ? null : null); // Will be set from discount code if needed
        template.setDiscountCodeId(formDTO.getDiscountCodeId());
        template.setIsActive(formDTO.getIsActive() != null ? formDTO.getIsActive() : true);
        template.setCreatedById(userId);
        template.setCreatedAt(ZonedDateTime.now());
        template.setUpdatedAt(ZonedDateTime.now());

        // Set promotion code from discount code if discount code is provided
        if (formDTO.getDiscountCodeId() != null) {
            DiscountCode discountCode = discountCodeRepository.findById(formDTO.getDiscountCodeId()).orElse(null);
            if (discountCode != null) {
                template.setPromotionCode(discountCode.getCode());
            }
        }

        PromotionEmailTemplate saved = promotionEmailTemplateRepository.save(template);
        log.debug("Created PromotionEmailTemplate: {}", saved.getId());

        return promotionEmailTemplateMapper.toDto(saved);
    }

    @Override
    public PromotionEmailTemplateDTO updateTemplate(Long id, PromotionEmailTemplateFormDTO formDTO, String tenantId) {
        log.debug("Request to update PromotionEmailTemplate: id={}, formDTO={}", id, formDTO);

        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Promotion email template not found: " + id));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        // Validate event if changed
        if (formDTO.getEventId() != null && !formDTO.getEventId().equals(template.getEventId())) {
            EventDetails event = eventRepository
                .findById(formDTO.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + formDTO.getEventId()));

            if (!event.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Event does not belong to the specified tenant");
            }
        }

        // Validate template name uniqueness if changed
        if (
            formDTO.getTemplateName() != null &&
            !formDTO.getTemplateName().equals(template.getTemplateName()) &&
            !validateTemplateName(
                formDTO.getTemplateName(),
                formDTO.getEventId() != null ? formDTO.getEventId() : template.getEventId(),
                tenantId,
                id
            )
        ) {
            throw new IllegalArgumentException("Template name '" + formDTO.getTemplateName() + "' already exists for this event");
        }

        // Validate discount code if provided
        if (formDTO.getDiscountCodeId() != null) {
            DiscountCode discountCode = discountCodeRepository
                .findById(formDTO.getDiscountCodeId())
                .orElseThrow(() -> new EntityNotFoundException("Discount code not found: " + formDTO.getDiscountCodeId()));

            if (!discountCode.getTenantId().equals(tenantId)) {
                throw new IllegalArgumentException("Discount code does not belong to the specified tenant");
            }
            Long eventId = formDTO.getEventId() != null ? formDTO.getEventId() : template.getEventId();
            if (!discountCode.getEventId().equals(eventId)) {
                throw new IllegalArgumentException("Discount code does not belong to the specified event");
            }
        }

        // Update fields
        if (formDTO.getEventId() != null) {
            template.setEventId(formDTO.getEventId());
        }
        if (formDTO.getTemplateName() != null) {
            template.setTemplateName(formDTO.getTemplateName());
        }
        if (formDTO.getSubject() != null) {
            template.setSubject(formDTO.getSubject());
        }
        if (formDTO.getFromEmail() != null) {
            template.setFromEmail(formDTO.getFromEmail());
        }
        if (formDTO.getBodyHtml() != null) {
            template.setBodyHtml(formDTO.getBodyHtml());
        }
        if (formDTO.getFooterHtml() != null) {
            template.setFooterHtml(formDTO.getFooterHtml());
        }
        if (formDTO.getHeaderImageUrl() != null) {
            template.setHeaderImageUrl(formDTO.getHeaderImageUrl());
        }
        if (formDTO.getFooterImageUrl() != null) {
            template.setFooterImageUrl(formDTO.getFooterImageUrl());
        }
        if (formDTO.getDiscountCodeId() != null) {
            template.setDiscountCodeId(formDTO.getDiscountCodeId());
            // Update promotion code from discount code
            DiscountCode discountCode = discountCodeRepository.findById(formDTO.getDiscountCodeId()).orElse(null);
            if (discountCode != null) {
                template.setPromotionCode(discountCode.getCode());
            }
        }
        if (formDTO.getIsActive() != null) {
            template.setIsActive(formDTO.getIsActive());
        }
        template.setUpdatedAt(ZonedDateTime.now());

        PromotionEmailTemplate saved = promotionEmailTemplateRepository.save(template);
        log.debug("Updated PromotionEmailTemplate: {}", saved.getId());

        return promotionEmailTemplateMapper.toDto(saved);
    }

    @Override
    public Optional<PromotionEmailTemplateDTO> partialUpdate(Long id, PromotionEmailTemplateDTO templateDTO) {
        log.debug("Request to partially update PromotionEmailTemplate: id={}, templateDTO={}", id, templateDTO);

        return promotionEmailTemplateRepository
            .findById(id)
            .map(existingTemplate -> {
                // Handle empty strings for headerImageUrl and footerImageUrl - treat as null to prevent clearing
                PromotionEmailTemplateDTO dtoToUse = new PromotionEmailTemplateDTO();
                dtoToUse.setId(templateDTO.getId());
                dtoToUse.setTenantId(templateDTO.getTenantId());

                // Only copy non-null and non-empty values
                if (templateDTO.getEventId() != null) {
                    dtoToUse.setEventId(templateDTO.getEventId());
                }
                if (templateDTO.getTemplateName() != null && !templateDTO.getTemplateName().isEmpty()) {
                    dtoToUse.setTemplateName(templateDTO.getTemplateName());
                }
                if (templateDTO.getSubject() != null && !templateDTO.getSubject().isEmpty()) {
                    dtoToUse.setSubject(templateDTO.getSubject());
                }
                if (templateDTO.getFromEmail() != null && !templateDTO.getFromEmail().isEmpty()) {
                    dtoToUse.setFromEmail(templateDTO.getFromEmail());
                }
                if (templateDTO.getBodyHtml() != null && !templateDTO.getBodyHtml().isEmpty()) {
                    dtoToUse.setBodyHtml(templateDTO.getBodyHtml());
                }
                if (templateDTO.getFooterHtml() != null && !templateDTO.getFooterHtml().isEmpty()) {
                    dtoToUse.setFooterHtml(templateDTO.getFooterHtml());
                }
                // Only set headerImageUrl if it's not null AND not empty (empty string means don't update)
                if (templateDTO.getHeaderImageUrl() != null && !templateDTO.getHeaderImageUrl().isEmpty()) {
                    dtoToUse.setHeaderImageUrl(templateDTO.getHeaderImageUrl());
                }
                // Only set footerImageUrl if it's not null AND not empty (empty string means don't update)
                if (templateDTO.getFooterImageUrl() != null && !templateDTO.getFooterImageUrl().isEmpty()) {
                    dtoToUse.setFooterImageUrl(templateDTO.getFooterImageUrl());
                }
                if (templateDTO.getPromotionCode() != null && !templateDTO.getPromotionCode().isEmpty()) {
                    dtoToUse.setPromotionCode(templateDTO.getPromotionCode());
                }
                if (templateDTO.getDiscountCodeId() != null) {
                    dtoToUse.setDiscountCodeId(templateDTO.getDiscountCodeId());
                }
                if (templateDTO.getIsActive() != null) {
                    dtoToUse.setIsActive(templateDTO.getIsActive());
                }
                if (templateDTO.getCreatedById() != null) {
                    dtoToUse.setCreatedById(templateDTO.getCreatedById());
                }
                if (templateDTO.getUpdatedAt() != null) {
                    dtoToUse.setUpdatedAt(templateDTO.getUpdatedAt());
                }

                promotionEmailTemplateMapper.partialUpdate(existingTemplate, dtoToUse);
                existingTemplate.setUpdatedAt(ZonedDateTime.now());
                return existingTemplate;
            })
            .map(promotionEmailTemplateRepository::save)
            .map(promotionEmailTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionEmailTemplateDTO> findAll(Pageable pageable, String tenantId) {
        log.debug("Request to get all PromotionEmailTemplates for tenant: {}", tenantId);
        // Use repository method that filters by tenant
        List<PromotionEmailTemplateDTO> allTemplates = promotionEmailTemplateRepository
            .findByTenantIdAndIsActive(tenantId, true)
            .stream()
            .map(promotionEmailTemplateMapper::toDto)
            .collect(java.util.stream.Collectors.toList());

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allTemplates.size());
        List<PromotionEmailTemplateDTO> pageContent = start < allTemplates.size()
            ? allTemplates.subList(start, end)
            : java.util.Collections.emptyList();

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allTemplates.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PromotionEmailTemplateDTO> findOne(Long id, String tenantId) {
        log.debug("Request to get PromotionEmailTemplate: id={}, tenantId={}", id, tenantId);
        return promotionEmailTemplateRepository
            .findById(id)
            .filter(template -> template.getTenantId().equals(tenantId))
            .map(promotionEmailTemplateMapper::toDto);
    }

    @Override
    public void delete(Long id, String tenantId) {
        log.debug("Request to delete PromotionEmailTemplate: id={}, tenantId={}", id, tenantId);

        PromotionEmailTemplate template = promotionEmailTemplateRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Promotion email template not found: " + id));

        if (!template.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Template does not belong to the specified tenant");
        }

        // Hard delete: permanently remove the template from database
        // Note: PromotionEmailSentLog records will remain for audit purposes even after template deletion
        promotionEmailTemplateRepository.delete(template);
        log.info("Deleted PromotionEmailTemplate: id={}, templateName={}", id, template.getTemplateName());
    }

    @Override
    public boolean validateTemplateName(String templateName, Long eventId, String tenantId, Long excludeId) {
        log.debug("Validating template name: name={}, eventId={}, tenantId={}, excludeId={}", templateName, eventId, tenantId, excludeId);

        Optional<PromotionEmailTemplate> existing = promotionEmailTemplateRepository.findByTenantIdAndEventIdAndTemplateName(
            tenantId,
            eventId,
            templateName
        );

        if (existing.isEmpty()) {
            return true; // Name is unique
        }

        // If excludeId is provided and matches, it's the same template (update scenario)
        if (excludeId != null && existing.orElseThrow().getId().equals(excludeId)) {
            return true; // Name is unique for this template
        }

        return false; // Name already exists
    }
}
