package com.eventsitemanager.service.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import org.junit.jupiter.api.Test;

class TenantSettingsHeroFieldsValidatorTest {

    private static final String VALID_URL =
        "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/tenants/x/hero-defaults/slide-01.webp";

    @Test
    void applyCreateDefaults_setsSlideshowAndIncludeWithEventsWhenOmitted() {
        TenantSettingsDTO dto = new TenantSettingsDTO();

        TenantSettingsHeroFieldsValidator.applyCreateDefaults(dto);

        assertThat(dto.getDefaultHeroDisplayMode()).isEqualTo("slideshow");
        assertThat(dto.getDefaultHeroIncludeWithEvents()).isTrue();
        assertThat(dto.getHomepageCacheVersion()).isEqualTo(0L);
    }

    @Test
    void validateImageUrlsJson_acceptsEmptyArray() {
        TenantSettingsHeroFieldsValidator.validateImageUrlsJson("[]");
    }

    @Test
    void validateImageUrlsJson_rejectsNonHttpsUrl() {
        assertThatThrownBy(() -> TenantSettingsHeroFieldsValidator.validateImageUrlsJson("[\"http://example.com/a.webp\"]"))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("https://");
    }

    @Test
    void validateImageUrlsJson_rejectsMalformedJson() {
        assertThatThrownBy(() -> TenantSettingsHeroFieldsValidator.validateImageUrlsJson("not-json"))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("valid JSON array");
    }

    @Test
    void validateDisplayMode_rejectsInvalidValue() {
        assertThatThrownBy(() -> TenantSettingsHeroFieldsValidator.validateDisplayMode("carousel"))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("slideshow, random, single");
    }

    @Test
    void parseUrlList_returnsUrlsFromValidJson() {
        assertThat(TenantSettingsHeroFieldsValidator.parseUrlList("[\"" + VALID_URL + "\"]")).containsExactly(VALID_URL);
    }

    @Test
    void validateMaxDisplayCount_acceptsNullViaValidatePresentFields() {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setDefaultHeroMaxDisplayCount(null);
        TenantSettingsHeroFieldsValidator.validatePresentFields(dto);
    }

    @Test
    void validateMaxDisplayCount_rejectsZeroOrNegative() {
        assertThatThrownBy(() -> TenantSettingsHeroFieldsValidator.validateMaxDisplayCount(0))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("at least 1");
    }

    @Test
    void rejectDeprecatedIdentityFieldWrites_rejectsAddressLine1() {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setAddressLine1("123 Main Street");

        assertThatThrownBy(() -> TenantSettingsHeroFieldsValidator.rejectDeprecatedIdentityFieldWrites(dto))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("tenant-organization");
    }

    @Test
    void validateMaxDisplayCount_rejectsAboveCap() {
        assertThatThrownBy(() -> TenantSettingsHeroFieldsValidator.validateMaxDisplayCount(21))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("20");
    }
}
