package com.eventsitemanager.service.validation;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * Validates Google AdSense tenant settings on {@link TenantSettingsDTO}.
 */
public final class TenantSettingsAdsenseFieldsValidator {

    public static final String ENTITY_NAME = "tenantSettings";

    private static final String PUBLISHER_ID_PREFIX = "ca-pub-";

    private static final int MAX_PUBLISHER_ID_LENGTH = 32;

    private static final int MAX_JSON_LENGTH = 8 * 1024;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TenantSettingsAdsenseFieldsValidator() {}

    public static void applyCreateDefaultsAndValidate(TenantSettingsDTO dto) {
        applyCreateDefaults(dto);
        validatePresentFields(dto);
    }

    public static void applyCreateDefaults(TenantSettingsDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getEnableGoogleAdsense() == null) {
            dto.setEnableGoogleAdsense(Boolean.FALSE);
        }
    }

    public static void validatePresentFields(TenantSettingsDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getGoogleAdsensePublisherId() != null) {
            validatePublisherId(dto.getGoogleAdsensePublisherId());
        }
        if (dto.getGoogleAdsensePlacementsJson() != null) {
            validatePlacementsJson(dto.getGoogleAdsensePlacementsJson());
        }
        if (Boolean.TRUE.equals(dto.getEnableGoogleAdsense())) {
            if (!StringUtils.hasText(dto.getGoogleAdsensePublisherId())) {
                throw badRequest(
                    "googleAdsensePublisherId is required when enableGoogleAdsense is true",
                    "googleAdsensePublisherIdRequired"
                );
            }
            validatePublisherId(dto.getGoogleAdsensePublisherId());
        }
    }

    public static void validatePublisherId(String publisherId) {
        if (!StringUtils.hasText(publisherId)) {
            throw badRequest("googleAdsensePublisherId must not be empty when provided", "invalidGoogleAdsensePublisherId");
        }
        if (publisherId.length() > MAX_PUBLISHER_ID_LENGTH) {
            throw badRequest("googleAdsensePublisherId must not exceed 32 characters", "googleAdsensePublisherIdTooLong");
        }
        if (!publisherId.startsWith(PUBLISHER_ID_PREFIX)) {
            throw badRequest("googleAdsensePublisherId must start with ca-pub-", "invalidGoogleAdsensePublisherId");
        }
    }

    public static void validatePlacementsJson(String json) {
        if (json.length() > MAX_JSON_LENGTH) {
            throw badRequest("googleAdsensePlacementsJson must not exceed 8KB", "googleAdsensePlacementsJsonTooLong");
        }
        try {
            Map<String, Object> placements = OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
            if (placements == null) {
                throw badRequest("googleAdsensePlacementsJson must be a JSON object", "invalidGoogleAdsensePlacementsJson");
            }
            for (Map.Entry<String, Object> entry : placements.entrySet()) {
                if (!StringUtils.hasText(entry.getKey())) {
                    throw badRequest("googleAdsensePlacementsJson keys must be non-empty region ids", "invalidGoogleAdsensePlacementsJson");
                }
                Object value = entry.getValue();
                if (!(value instanceof String) || !StringUtils.hasText((String) value)) {
                    throw badRequest(
                        "googleAdsensePlacementsJson value for region '" + entry.getKey() + "' must be a non-empty ad slot id string",
                        "invalidGoogleAdsensePlacementsJson"
                    );
                }
            }
        } catch (BadRequestAlertException e) {
            throw e;
        } catch (Exception e) {
            throw badRequest("googleAdsensePlacementsJson must be valid JSON object", "invalidGoogleAdsensePlacementsJson");
        }
    }

    private static BadRequestAlertException badRequest(String message, String errorKey) {
        return new BadRequestAlertException(message, ENTITY_NAME, errorKey);
    }
}
