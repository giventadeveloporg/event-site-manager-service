package com.eventsitemanager.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class InboundWebhookGuardTest {

    @Test
    void flagsDefaultToDisabled() {
        ApplicationProperties properties = new ApplicationProperties();
        InboundWebhookGuard guard = new InboundWebhookGuard(properties);

        assertThat(guard.isPaymentInboundEnabled()).isFalse();
        assertThat(guard.isClerkInboundEnabled()).isFalse();
        assertThat(guard.isClerkScheduledTasksEnabled()).isFalse();
        assertThat(guard.isPaymentCronEnabled()).isFalse();
    }

    @Test
    void disabledResponseIs503WithMessage() {
        InboundWebhookGuard guard = new InboundWebhookGuard(new ApplicationProperties());

        var response = guard.paymentInboundDisabledResponse();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).containsEntry("enabled", "false");
        assertThat(response.getBody()).containsKey("error");
    }

    @Test
    void flagsCanBeEnabledViaProperties() {
        ApplicationProperties properties = new ApplicationProperties();
        properties.getWebhooks().getPaymentInbound().setEnabled(true);
        properties.getBatchJobs().getPaymentCron().setEnabled(true);

        InboundWebhookGuard guard = new InboundWebhookGuard(properties);

        assertThat(guard.isPaymentInboundEnabled()).isTrue();
        assertThat(guard.isPaymentCronEnabled()).isTrue();
        assertThat(guard.isClerkInboundEnabled()).isFalse();
    }
}
