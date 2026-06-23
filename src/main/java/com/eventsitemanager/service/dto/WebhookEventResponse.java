package com.eventsitemanager.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * DTO for webhook event processing response.
 */
@Schema(description = "Response after processing webhook event")
public class WebhookEventResponse implements Serializable {

    @Schema(description = "Processing success status", example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "Webhook processed successfully")
    private String message;

    @Schema(description = "Event ID that was processed")
    private String eventId;

    @Schema(description = "Event type that was processed")
    private String eventType;

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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return (
            "WebhookEventResponse{" +
            "success=" +
            success +
            ", message='" +
            message +
            "', eventId='" +
            eventId +
            "', eventType='" +
            eventType +
            "'}"
        );
    }
}
