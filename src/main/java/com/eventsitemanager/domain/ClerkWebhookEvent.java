package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Audit trail and processing queue for Clerk webhook events.
 * Supports idempotency and retry logic.
 */
@Entity
@Table(name = "clerk_webhook_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClerkWebhookEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clerkWebhookEventSeq")
    @SequenceGenerator(name = "clerkWebhookEventSeq", sequenceName = "public.clerk_webhook_event_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "event_id", length = 255, nullable = false, unique = true)
    private String eventId;

    @NotNull
    @Size(max = 100)
    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;

    @Size(max = 255)
    @Column(name = "clerk_user_id", length = 255)
    private String clerkUserId;

    @NotNull
    @Column(name = "payload", columnDefinition = "text", nullable = false)
    private String payload;

    @Column(name = "processed")
    private Boolean processed;

    @Column(name = "processed_at")
    private ZonedDateTime processedAt;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount;

    @NotNull
    @Column(name = "received_at", nullable = false)
    private ZonedDateTime receivedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClerkWebhookEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return this.eventId;
    }

    public ClerkWebhookEvent eventId(String eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public ClerkWebhookEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getClerkUserId() {
        return this.clerkUserId;
    }

    public ClerkWebhookEvent clerkUserId(String clerkUserId) {
        this.setClerkUserId(clerkUserId);
        return this;
    }

    public void setClerkUserId(String clerkUserId) {
        this.clerkUserId = clerkUserId;
    }

    public String getPayload() {
        return this.payload;
    }

    public ClerkWebhookEvent payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Boolean getProcessed() {
        return this.processed;
    }

    public ClerkWebhookEvent processed(Boolean processed) {
        this.setProcessed(processed);
        return this;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public ZonedDateTime getProcessedAt() {
        return this.processedAt;
    }

    public ClerkWebhookEvent processedAt(ZonedDateTime processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(ZonedDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public ClerkWebhookEvent errorMessage(String errorMessage) {
        this.setErrorMessage(errorMessage);
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRetryCount() {
        return this.retryCount;
    }

    public ClerkWebhookEvent retryCount(Integer retryCount) {
        this.setRetryCount(retryCount);
        return this;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public ZonedDateTime getReceivedAt() {
        return this.receivedAt;
    }

    public ClerkWebhookEvent receivedAt(ZonedDateTime receivedAt) {
        this.setReceivedAt(receivedAt);
        return this;
    }

    public void setReceivedAt(ZonedDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ClerkWebhookEvent createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClerkWebhookEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((ClerkWebhookEvent) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClerkWebhookEvent{" +
            "id=" + getId() +
            ", eventId='" + getEventId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", clerkUserId='" + getClerkUserId() + "'" +
            ", payload='" + getPayload() + "'" +
            ", processed='" + getProcessed() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", retryCount=" + getRetryCount() +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
