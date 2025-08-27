package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserTask.
 */
@Entity
@Table(name = "user_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTask implements Serializable {

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

    @Size(max = 4096)
    @Column(name = "description", length = 4096)
    private String description;

    @NotNull
    @Size(max = 255)
    @Column(name = "status", length = 255, nullable = false)
    private String status;

    @NotNull
    @Size(max = 255)
    @Column(name = "priority", length = 255, nullable = false)
    private String priority;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Size(max = 255)
    @Column(name = "assignee_name", length = 255)
    private String assigneeName;

    @Size(max = 50)
    @Column(name = "assignee_contact_phone", length = 50)
    private String assigneeContactPhone;

    @Size(max = 255)
    @Column(name = "assignee_contact_email", length = 255)
    private String assigneeContactEmail;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "createdBy", "eventType" }, allowSetters = true)
    private EventDetails event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public UserTask tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return this.title;
    }

    public UserTask title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public UserTask description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return this.status;
    }

    public UserTask status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return this.priority;
    }

    public UserTask priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ZonedDateTime getDueDate() {
        return this.dueDate;
    }

    public UserTask dueDate(ZonedDateTime dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public UserTask completed(Boolean completed) {
        this.setCompleted(completed);
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getAssigneeName() {
        return this.assigneeName;
    }

    public UserTask assigneeName(String assigneeName) {
        this.setAssigneeName(assigneeName);
        return this;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeContactPhone() {
        return this.assigneeContactPhone;
    }

    public UserTask assigneeContactPhone(String assigneeContactPhone) {
        this.setAssigneeContactPhone(assigneeContactPhone);
        return this;
    }

    public void setAssigneeContactPhone(String assigneeContactPhone) {
        this.assigneeContactPhone = assigneeContactPhone;
    }

    public String getAssigneeContactEmail() {
        return this.assigneeContactEmail;
    }

    public UserTask assigneeContactEmail(String assigneeContactEmail) {
        this.setAssigneeContactEmail(assigneeContactEmail);
        return this;
    }

    public void setAssigneeContactEmail(String assigneeContactEmail) {
        this.assigneeContactEmail = assigneeContactEmail;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public UserTask createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public UserTask updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public UserTask user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public EventDetails getEvent() {
        return this.event;
    }

    public void setEvent(EventDetails eventDetails) {
        this.event = eventDetails;
    }

    public UserTask event(EventDetails eventDetails) {
        this.setEvent(eventDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserTask)) {
            return false;
        }
        return getId() != null && getId().equals(((UserTask) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTask{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", completed='" + getCompleted() + "'" +
            ", assigneeName='" + getAssigneeName() + "'" +
            ", assigneeContactPhone='" + getAssigneeContactPhone() + "'" +
            ", assigneeContactEmail='" + getAssigneeContactEmail() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
