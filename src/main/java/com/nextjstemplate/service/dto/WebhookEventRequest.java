package com.nextjstemplate.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Map;

/**
 * DTO for Clerk webhook event request.
 */
@Schema(description = "Clerk webhook event payload")
public class WebhookEventRequest implements Serializable {

    @Schema(description = "Unique event identifier")
    private String id;

    @Schema(description = "Event type (e.g., user.created, session.ended)")
    private String type;

    @Schema(description = "Event object type")
    private String object;

    @Schema(description = "Event data payload")
    private Map<String, Object> data;

    @Schema(description = "Timestamp when event was created")
    private Long created;

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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "WebhookEventRequest{" + "id='" + id + "', type='" + type + "', object='" + object + "', created=" + created + '}';
    }
}
