package com.eventsitemanager.config;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Central guard for inbound webhook and payment-cron feature flags.
 */
@Component
public class InboundWebhookGuard {

    private final ApplicationProperties applicationProperties;

    public InboundWebhookGuard(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public boolean isPaymentInboundEnabled() {
        return applicationProperties.getWebhooks().getPaymentInbound().isEnabled();
    }

    public boolean isClerkInboundEnabled() {
        return applicationProperties.getWebhooks().getClerkInbound().isEnabled();
    }

    public boolean isClerkScheduledTasksEnabled() {
        return applicationProperties.getWebhooks().getClerkScheduledTasks().isEnabled();
    }

    public boolean isPaymentCronEnabled() {
        return applicationProperties.getBatchJobs().getPaymentCron().isEnabled();
    }

    public ResponseEntity<Map<String, Object>> paymentInboundDisabledResponse() {
        return disabledResponse("Payment inbound webhooks");
    }

    public ResponseEntity<Map<String, Object>> clerkInboundDisabledResponse() {
        return disabledResponse("Clerk inbound webhooks");
    }

    public ResponseEntity<Map<String, Object>> paymentCronDisabledResponse() {
        return disabledResponse("Payment batch cron");
    }

    private ResponseEntity<Map<String, Object>> disabledResponse(String feature) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("error", feature + " are disabled", "enabled", "false"));
    }
}
