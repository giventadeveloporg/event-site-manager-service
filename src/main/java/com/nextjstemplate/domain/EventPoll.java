package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventPoll.
 */
@Entity
@Table(name = "event_poll")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventPoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    @Column(name = "allow_multiple_choices")
    private Boolean allowMultipleChoices = false;

    @Column(name = "max_responses_per_user")
    private Integer maxResponsesPerUser = 1;

    @Size(max = 50)
    @Column(name = "results_visible_to", length = 50)
    private String resultsVisibleTo = "ALL";

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventPoll id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventPoll tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return this.title;
    }

    public EventPoll title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public EventPoll description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public EventPoll isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public EventPoll startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return this.endDate;
    }

    public EventPoll endDate(ZonedDateTime endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventPoll createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventPoll updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public EventPoll event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    public UserProfile getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UserProfile userProfile) {
        this.createdBy = userProfile;
    }

    public EventPoll createdBy(UserProfile userProfile) {
        this.setCreatedBy(userProfile);
        return this;
    }

    public Boolean getIsAnonymous() {
        return this.isAnonymous;
    }

    public EventPoll isAnonymous(Boolean isAnonymous) {
        this.setIsAnonymous(isAnonymous);
        return this;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Boolean getAllowMultipleChoices() {
        return this.allowMultipleChoices;
    }

    public EventPoll allowMultipleChoices(Boolean allowMultipleChoices) {
        this.setAllowMultipleChoices(allowMultipleChoices);
        return this;
    }

    public void setAllowMultipleChoices(Boolean allowMultipleChoices) {
        this.allowMultipleChoices = allowMultipleChoices;
    }

    public Integer getMaxResponsesPerUser() {
        return this.maxResponsesPerUser;
    }

    public EventPoll maxResponsesPerUser(Integer maxResponsesPerUser) {
        this.setMaxResponsesPerUser(maxResponsesPerUser);
        return this;
    }

    public void setMaxResponsesPerUser(Integer maxResponsesPerUser) {
        this.maxResponsesPerUser = maxResponsesPerUser;
    }

    public String getResultsVisibleTo() {
        return this.resultsVisibleTo;
    }

    public EventPoll resultsVisibleTo(String resultsVisibleTo) {
        this.setResultsVisibleTo(resultsVisibleTo);
        return this;
    }

    public void setResultsVisibleTo(String resultsVisibleTo) {
        this.resultsVisibleTo = resultsVisibleTo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventPoll)) {
            return false;
        }
        return getId() != null && getId().equals(((EventPoll) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPoll{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isAnonymous='" + getIsAnonymous() + "'" +
            ", allowMultipleChoices='" + getAllowMultipleChoices() + "'" +
            ", maxResponsesPerUser='" + getMaxResponsesPerUser() + "'" +
            ", resultsVisibleTo='" + getResultsVisibleTo() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
