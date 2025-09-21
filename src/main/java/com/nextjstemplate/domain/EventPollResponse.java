package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventPollResponse.
 */
@Entity
@Table(name = "event_poll_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventPollResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @Size(max = 1000)
    @Column(name = "response_value", length = 1000)
    private String responseValue;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    @Size(max = 255)
    @Column(name = "comment", length = 255)
    private String comment;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "event", "createdBy" }, allowSetters = true)
    private EventPoll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "poll" }, allowSetters = true)
    private EventPollOption pollOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventPollResponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventPollResponse tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getComment() {
        return this.comment;
    }

    public EventPollResponse comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventPollResponse createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventPollResponse updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventPoll getPoll() {
        return this.poll;
    }

    public void setPoll(EventPoll eventPoll) {
        this.poll = eventPoll;
    }

    public EventPollResponse poll(EventPoll eventPoll) {
        this.setPoll(eventPoll);
        return this;
    }

    public EventPollOption getPollOption() {
        return this.pollOption;
    }

    public void setPollOption(EventPollOption eventPollOption) {
        this.pollOption = eventPollOption;
    }

    public EventPollResponse pollOption(EventPollOption eventPollOption) {
        this.setPollOption(eventPollOption);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public EventPollResponse user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public String getResponseValue() {
        return this.responseValue;
    }

    public EventPollResponse responseValue(String responseValue) {
        this.setResponseValue(responseValue);
        return this;
    }

    public void setResponseValue(String responseValue) {
        this.responseValue = responseValue;
    }

    public Boolean getIsAnonymous() {
        return this.isAnonymous;
    }

    public EventPollResponse isAnonymous(Boolean isAnonymous) {
        this.setIsAnonymous(isAnonymous);
        return this;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventPollResponse)) {
            return false;
        }
        return getId() != null && getId().equals(((EventPollResponse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPollResponse{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", responseValue='" + getResponseValue() + "'" +
            ", isAnonymous='" + getIsAnonymous() + "'" +
            ", comment='" + getComment() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
