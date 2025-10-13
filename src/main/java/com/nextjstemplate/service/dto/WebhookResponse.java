package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * DTO for webhook response.
 */
@Schema(description = "Response after webhook processing")
public class WebhookResponse implements Serializable {

    @Schema(description = "Processing success status", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Webhook processed successfully")
    private String message;

    @Schema(description = "Event ID that was processed", example = "evt_2abc123def456")
    private String eventId;

    @Schema(description = "Processing time in milliseconds", example = "245")
    private Long processingTimeMs;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    @Override
    public String toString() {
        return (
            "WebhookResponse{" +
            "success=" +
            success +
            ", message='" +
            message +
            "', eventId='" +
            eventId +
            "', processingTimeMs=" +
            processingTimeMs +
            '}'
        );
    }
}
