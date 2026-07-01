package com.eventsitemanager.service.validation;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * Validates tenant default hero image settings on {@link TenantSettingsDTO}.
 */
public final class TenantSettingsHeroFieldsValidator {

    public static final String ENTITY_NAME = "tenantSettings";

    public static final String DEFAULT_DISPLAY_MODE = "slideshow";

    private static final Set<String> ALLOWED_DISPLAY_MODES = Set.of("slideshow", "random", "single");

    private static final int MAX_JSON_LENGTH = 16 * 1024;

    /** Aligns with frontend {@code MAX_TENANT_HERO_SLIDES} in defaultHeroImages.ts */
    private static final int MAX_HERO_DISPLAY_COUNT = 20;

    /** Max slides stored in {@code defaultHeroImageUrlsJson} (same as frontend library cap). */
    private static final int MAX_HERO_SLIDES = 20;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TenantSettingsHeroFieldsValidator() {}

    /**
     * Apply create-time defaults and validate all hero fields present on the DTO.
     */
    public static final String IDENTITY_FIELDS_MOVED_MESSAGE =
        "Identity fields moved to tenant-organization; use PATCH /api/tenant-organizations/{id}";

    public static void applyCreateDefaultsAndValidate(TenantSettingsDTO dto) {
        rejectDeprecatedIdentityFieldWrites(dto);
        applyCreateDefaults(dto);
        validatePresentFields(dto);
    }

    /**
     * Validate only non-null hero fields (PATCH / partial update).
     */
    public static void validatePresentFields(TenantSettingsDTO dto) {
        if (dto == null) {
            return;
        }
        rejectDeprecatedIdentityFieldWrites(dto);
        if (dto.getDefaultHeroImageUrlsJson() != null) {
            validateImageUrlsJson(dto.getDefaultHeroImageUrlsJson());
        }
        if (dto.getDefaultHeroDisplayMode() != null) {
            validateDisplayMode(dto.getDefaultHeroDisplayMode());
        }
        if (dto.getDefaultHeroMaxDisplayCount() != null) {
            validateMaxDisplayCount(dto.getDefaultHeroMaxDisplayCount());
        }
    }

    public static void applyCreateDefaults(TenantSettingsDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getDefaultHeroDisplayMode() == null) {
            dto.setDefaultHeroDisplayMode(DEFAULT_DISPLAY_MODE);
        }
        if (dto.getDefaultHeroIncludeWithEvents() == null) {
            dto.setDefaultHeroIncludeWithEvents(Boolean.TRUE);
        }
        if (dto.getHomepageCacheVersion() == null) {
            dto.setHomepageCacheVersion(0L);
        }
    }

    public static List<String> parseUrlList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);
            if (!root.isArray()) {
                return Collections.emptyList();
            }
            List<String> urls = new ArrayList<>();
            for (JsonNode item : root) {
                String url = extractHttpsUrl(item);
                if (StringUtils.hasText(url)) {
                    urls.add(url);
                }
            }
            return urls;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static void validateImageUrlsJson(String json) {
        if (json.length() > MAX_JSON_LENGTH) {
            throw badRequest("defaultHeroImageUrlsJson must not exceed 16KB", "defaultHeroImageUrlsJsonTooLong");
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);
            if (!root.isArray()) {
                throw badRequest("defaultHeroImageUrlsJson must be a JSON array", "invalidDefaultHeroImageUrlsJson");
            }
            if (root.size() > MAX_HERO_SLIDES) {
                throw badRequest(
                    "defaultHeroImageUrlsJson must not contain more than " + MAX_HERO_SLIDES + " slides",
                    "invalidDefaultHeroImageUrlsJson"
                );
            }
            for (int i = 0; i < root.size(); i++) {
                validateSlideElement(root.get(i), i);
            }
        } catch (BadRequestAlertException e) {
            throw e;
        } catch (Exception e) {
            throw badRequest(
                "defaultHeroImageUrlsJson must be a valid JSON array of HTTPS URLs or slide objects",
                "invalidDefaultHeroImageUrlsJson"
            );
        }
    }

    /**
     * Legacy plain URL string, or enriched slide {@code { url, active?, fileName? }} from the admin UI.
     */
    private static void validateSlideElement(JsonNode item, int index) {
        if (item == null || item.isNull()) {
            throw badRequest(
                "defaultHeroImageUrlsJson array element at index " + index + " must be a URL string or slide object",
                "invalidDefaultHeroImageUrlsJson"
            );
        }
        if (item.isTextual()) {
            validateHttpsUrl(item.asText(), index);
            return;
        }
        if (item.isObject()) {
            JsonNode urlNode = item.get("url");
            if (urlNode == null || !urlNode.isTextual()) {
                throw badRequest(
                    "defaultHeroImageUrlsJson slide at index " + index + " must include a string url field",
                    "invalidDefaultHeroImageUrlsJson"
                );
            }
            validateHttpsUrl(urlNode.asText(), index);
            JsonNode activeNode = item.get("active");
            if (activeNode != null && !activeNode.isBoolean()) {
                throw badRequest(
                    "defaultHeroImageUrlsJson slide at index " + index + " active must be a boolean when set",
                    "invalidDefaultHeroImageUrlsJson"
                );
            }
            JsonNode fileNameNode = item.get("fileName");
            if (fileNameNode != null && !fileNameNode.isTextual()) {
                throw badRequest(
                    "defaultHeroImageUrlsJson slide at index " + index + " fileName must be a string when set",
                    "invalidDefaultHeroImageUrlsJson"
                );
            }
            return;
        }
        throw badRequest(
            "defaultHeroImageUrlsJson array element at index " + index + " must be a URL string or slide object",
            "invalidDefaultHeroImageUrlsJson"
        );
    }

    private static String extractHttpsUrl(JsonNode item) {
        if (item == null || item.isNull()) {
            return null;
        }
        if (item.isTextual()) {
            return item.asText().trim();
        }
        if (item.isObject()) {
            JsonNode urlNode = item.get("url");
            if (urlNode != null && urlNode.isTextual()) {
                return urlNode.asText().trim();
            }
        }
        return null;
    }

    private static void validateHttpsUrl(String url, int index) {
        if (!StringUtils.hasText(url)) {
            throw badRequest(
                "defaultHeroImageUrlsJson array element at index " + index + " must be a non-empty string",
                "invalidDefaultHeroImageUrlsJson"
            );
        }
        if (!url.trim().startsWith("https://")) {
            throw badRequest(
                "defaultHeroImageUrlsJson array element at index " + index + " must start with https://",
                "invalidDefaultHeroImageUrlsJson"
            );
        }
    }

    public static void validateDisplayMode(String displayMode) {
        if (!ALLOWED_DISPLAY_MODES.contains(displayMode)) {
            throw badRequest("defaultHeroDisplayMode must be one of: slideshow, random, single", "invalidDefaultHeroDisplayMode");
        }
    }

    public static void validateMaxDisplayCount(Integer maxDisplayCount) {
        if (maxDisplayCount < 1) {
            throw badRequest("defaultHeroMaxDisplayCount must be at least 1 when set", "invalidDefaultHeroMaxDisplayCount");
        }
        if (maxDisplayCount > MAX_HERO_DISPLAY_COUNT) {
            throw badRequest("defaultHeroMaxDisplayCount must not exceed " + MAX_HERO_DISPLAY_COUNT, "invalidDefaultHeroMaxDisplayCount");
        }
    }

    /**
     * Reject writes of deprecated identity fields on tenant-settings (v2.0 — canonical on tenant-organization).
     */
    public static void rejectDeprecatedIdentityFieldWrites(TenantSettingsDTO dto) {
        if (dto == null) {
            return;
        }
        if (
            dto.isDescriptionSet() ||
            dto.isCitySet() ||
            dto.isAddressLine1Set() ||
            dto.isAddressLine2Set() ||
            dto.isStateProvinceSet() ||
            dto.isZipCodeSet() ||
            dto.isCountrySet()
        ) {
            throw badRequest(IDENTITY_FIELDS_MOVED_MESSAGE, "identityFieldsMovedToTenantOrganization");
        }
    }

    private static BadRequestAlertException badRequest(String message, String errorKey) {
        return new BadRequestAlertException(message, ENTITY_NAME, errorKey);
    }
}
