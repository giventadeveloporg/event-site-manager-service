package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * DTO for Clerk webhook request payload.
 */
@Schema(description = "Clerk webhook event payload")
public class ClerkWebhookRequest implements Serializable {

    @Schema(description = "Webhook event ID", example = "evt_2abc123def456")
    private String id;

    @Schema(description = "Event type", example = "user.created")
    private String type;

    @Schema(description = "Event object (user, session, organization, etc.)")
    private String object;

    @Schema(description = "Event data payload")
    private JsonNode data;

    @Schema(description = "Event timestamp in milliseconds", example = "1697234567890")
    private Long timestamp;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ClerkWebhookRequest{" + "id='" + id + "', type='" + type + "', object='" + object + "', timestamp=" + timestamp + '}';
    }
}
