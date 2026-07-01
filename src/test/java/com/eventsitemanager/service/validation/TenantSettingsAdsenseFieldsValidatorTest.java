package com.eventsitemanager.service.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eventsitemanager.errors.BadRequestAlertException;
import com.eventsitemanager.service.dto.TenantSettingsDTO;
import org.junit.jupiter.api.Test;

class TenantSettingsAdsenseFieldsValidatorTest {

    private static final String VALID_PUBLISHER = "ca-pub-1234567890123456";

    @Test
    void applyCreateDefaults_setsEnableFalseWhenOmitted() {
        TenantSettingsDTO dto = new TenantSettingsDTO();

        TenantSettingsAdsenseFieldsValidator.applyCreateDefaults(dto);

        assertThat(dto.getEnableGoogleAdsense()).isFalse();
    }

    @Test
    void validatePresentFields_requiresPublisherIdWhenEnabled() {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setEnableGoogleAdsense(true);

        assertThatThrownBy(() -> TenantSettingsAdsenseFieldsValidator.validatePresentFields(dto))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("googleAdsensePublisherId is required");
    }

    @Test
    void validatePublisherId_rejectsInvalidPrefix() {
        assertThatThrownBy(() -> TenantSettingsAdsenseFieldsValidator.validatePublisherId("pub-123"))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("ca-pub-");
    }

    @Test
    void validatePlacementsJson_acceptsRegionMap() {
        TenantSettingsAdsenseFieldsValidator.validatePlacementsJson(
            "{\"sidebar\":\"111\",\"between_sections\":\"222\",\"footer_strip\":\"333\"}"
        );
    }

    @Test
    void validatePlacementsJson_rejectsNonObject() {
        assertThatThrownBy(() -> TenantSettingsAdsenseFieldsValidator.validatePlacementsJson("[\"123\"]"))
            .isInstanceOf(BadRequestAlertException.class)
            .hasMessageContaining("JSON object");
    }

    @Test
    void applyCreateDefaultsAndValidate_acceptsEnabledWithPublisherAndPlacements() {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setEnableGoogleAdsense(true);
        dto.setGoogleAdsensePublisherId(VALID_PUBLISHER);
        dto.setGoogleAdsensePlacementsJson("{\"footer_strip\":\"999\"}");

        TenantSettingsAdsenseFieldsValidator.applyCreateDefaultsAndValidate(dto);

        assertThat(dto.getEnableGoogleAdsense()).isTrue();
    }

    @Test
    void validatePresentFields_allowsDisabledWithEmptyPublisherAndPlacements() {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setEnableGoogleAdsense(false);
        dto.setGoogleAdsensePublisherId("");
        dto.setGoogleAdsensePlacementsJson("");

        TenantSettingsAdsenseFieldsValidator.validatePresentFields(dto);
    }

    @Test
    void validatePresentFields_allowsDisabledWithNullPublisherAndPlacements() {
        TenantSettingsDTO dto = new TenantSettingsDTO();
        dto.setEnableGoogleAdsense(false);

        TenantSettingsAdsenseFieldsValidator.validatePresentFields(dto);
    }
}
