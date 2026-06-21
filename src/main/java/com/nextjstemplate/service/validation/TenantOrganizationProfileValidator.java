package com.nextjstemplate.service.validation;

import com.nextjstemplate.domain.TenantOrganization;
import com.nextjstemplate.errors.BadRequestAlertException;
import com.nextjstemplate.service.dto.TenantOrganizationDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Validates tenant organization description, address, and website fields.
 */
@Component
public class TenantOrganizationProfileValidator {

    public static final String ENTITY_NAME = "tenantOrganization";

    public static final int MAX_DESCRIPTION_LENGTH = 1000;

    public void validateAndNormalize(TenantOrganizationDTO dto) {
        if (dto == null) {
            return;
        }
        normalizeOptionalFields(dto);
        validateFields(dto);
    }

    public void validatePresentFields(TenantOrganizationDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.isDescriptionSet()) {
            dto.setDescription(normalizeOptionalString(dto.getDescription()));
            validateDescription(dto.getDescription());
        }
        if (dto.isAddressLine1Set()) {
            dto.setAddressLine1(normalizeOptionalString(dto.getAddressLine1()));
        }
        if (dto.isAddressLine2Set()) {
            dto.setAddressLine2(normalizeOptionalString(dto.getAddressLine2()));
        }
        if (dto.isCitySet()) {
            dto.setCity(normalizeOptionalString(dto.getCity()));
        }
        if (dto.isStateProvinceSet()) {
            dto.setStateProvince(normalizeOptionalString(dto.getStateProvince()));
        }
        if (dto.isZipCodeSet()) {
            dto.setZipCode(normalizeOptionalString(dto.getZipCode()));
        }
        if (dto.isCountrySet()) {
            dto.setCountry(normalizeOptionalString(dto.getCountry()));
        }
        if (dto.isWebsiteUrlSet()) {
            dto.setWebsiteUrl(normalizeOptionalString(dto.getWebsiteUrl()));
            validateWebsiteUrl(dto.getWebsiteUrl());
        }
    }

    public void applyPartialNullableFields(TenantOrganization entity, TenantOrganizationDTO dto) {
        if (dto.isDescriptionSet()) {
            entity.setDescription(normalizeOptionalString(dto.getDescription()));
        }
        if (dto.isAddressLine1Set()) {
            entity.setAddressLine1(normalizeOptionalString(dto.getAddressLine1()));
        }
        if (dto.isAddressLine2Set()) {
            entity.setAddressLine2(normalizeOptionalString(dto.getAddressLine2()));
        }
        if (dto.isCitySet()) {
            entity.setCity(normalizeOptionalString(dto.getCity()));
        }
        if (dto.isStateProvinceSet()) {
            entity.setStateProvince(normalizeOptionalString(dto.getStateProvince()));
        }
        if (dto.isZipCodeSet()) {
            entity.setZipCode(normalizeOptionalString(dto.getZipCode()));
        }
        if (dto.isCountrySet()) {
            entity.setCountry(normalizeOptionalString(dto.getCountry()));
        }
        if (dto.isWebsiteUrlSet()) {
            entity.setWebsiteUrl(normalizeOptionalString(dto.getWebsiteUrl()));
        }
    }

    public void validateEntityFields(TenantOrganization entity) {
        validateDescription(entity.getDescription());
        validateWebsiteUrl(entity.getWebsiteUrl());
    }

    private void normalizeOptionalFields(TenantOrganizationDTO dto) {
        dto.setDescription(normalizeOptionalString(dto.getDescription()));
        dto.setAddressLine1(normalizeOptionalString(dto.getAddressLine1()));
        dto.setAddressLine2(normalizeOptionalString(dto.getAddressLine2()));
        dto.setCity(normalizeOptionalString(dto.getCity()));
        dto.setStateProvince(normalizeOptionalString(dto.getStateProvince()));
        dto.setZipCode(normalizeOptionalString(dto.getZipCode()));
        dto.setCountry(normalizeOptionalString(dto.getCountry()));
        dto.setWebsiteUrl(normalizeOptionalString(dto.getWebsiteUrl()));
    }

    private void validateFields(TenantOrganizationDTO dto) {
        validateDescription(dto.getDescription());
        validateWebsiteUrl(dto.getWebsiteUrl());
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new BadRequestAlertException("Description must not exceed 1000 characters", ENTITY_NAME, "descriptionTooLong");
        }
    }

    public void validateWebsiteUrl(String websiteUrl) {
        if (!StringUtils.hasText(websiteUrl)) {
            return;
        }
        String trimmed = websiteUrl.trim();
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            throw new BadRequestAlertException("Website URL must start with http:// or https://", ENTITY_NAME, "websiteUrlInvalid");
        }
    }

    private String normalizeOptionalString(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
