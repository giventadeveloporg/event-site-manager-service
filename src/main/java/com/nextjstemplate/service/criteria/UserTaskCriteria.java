package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.UserTask} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.UserTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter description;

    private StringFilter status;

    private StringFilter priority;

    private ZonedDateTimeFilter dueDate;

    private BooleanFilter completed;

    private StringFilter assigneeName;

    private StringFilter assigneeContactPhone;

    private StringFilter assigneeContactEmail;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter userId;

    private LongFilter eventId;

    private Boolean distinct;

    public UserTaskCriteria() {}

    public UserTaskCriteria(UserTaskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(StringFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.completed = other.optionalCompleted().map(BooleanFilter::copy).orElse(null);
        this.assigneeName = other.optionalAssigneeName().map(StringFilter::copy).orElse(null);
        this.assigneeContactPhone = other.optionalAssigneeContactPhone().map(StringFilter::copy).orElse(null);
        this.assigneeContactEmail = other.optionalAssigneeContactEmail().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.eventId = other.optionalEventId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserTaskCriteria copy() {
        return new UserTaskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public Optional<StringFilter> optionalTenantId() {
        return Optional.ofNullable(tenantId);
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            setTenantId(new StringFilter());
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getPriority() {
        return priority;
    }

    public Optional<StringFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public StringFilter priority() {
        if (priority == null) {
            setPriority(new StringFilter());
        }
        return priority;
    }

    public void setPriority(StringFilter priority) {
        this.priority = priority;
    }

    public ZonedDateTimeFilter getDueDate() {
        return dueDate;
    }

    public Optional<ZonedDateTimeFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public ZonedDateTimeFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new ZonedDateTimeFilter());
        }
        return dueDate;
    }

    public void setDueDate(ZonedDateTimeFilter dueDate) {
        this.dueDate = dueDate;
    }

    public BooleanFilter getCompleted() {
        return completed;
    }

    public Optional<BooleanFilter> optionalCompleted() {
        return Optional.ofNullable(completed);
    }

    public BooleanFilter completed() {
        if (completed == null) {
            setCompleted(new BooleanFilter());
        }
        return completed;
    }

    public void setCompleted(BooleanFilter completed) {
        this.completed = completed;
    }

    public StringFilter getAssigneeName() {
        return assigneeName;
    }

    public Optional<StringFilter> optionalAssigneeName() {
        return Optional.ofNullable(assigneeName);
    }

    public StringFilter assigneeName() {
        if (assigneeName == null) {
            setAssigneeName(new StringFilter());
        }
        return assigneeName;
    }

    public void setAssigneeName(StringFilter assigneeName) {
        this.assigneeName = assigneeName;
    }

    public StringFilter getAssigneeContactPhone() {
        return assigneeContactPhone;
    }

    public Optional<StringFilter> optionalAssigneeContactPhone() {
        return Optional.ofNullable(assigneeContactPhone);
    }

    public StringFilter assigneeContactPhone() {
        if (assigneeContactPhone == null) {
            setAssigneeContactPhone(new StringFilter());
        }
        return assigneeContactPhone;
    }

    public void setAssigneeContactPhone(StringFilter assigneeContactPhone) {
        this.assigneeContactPhone = assigneeContactPhone;
    }

    public StringFilter getAssigneeContactEmail() {
        return assigneeContactEmail;
    }

    public Optional<StringFilter> optionalAssigneeContactEmail() {
        return Optional.ofNullable(assigneeContactEmail);
    }

    public StringFilter assigneeContactEmail() {
        if (assigneeContactEmail == null) {
            setAssigneeContactEmail(new StringFilter());
        }
        return assigneeContactEmail;
    }

    public void setAssigneeContactEmail(StringFilter assigneeContactEmail) {
        this.assigneeContactEmail = assigneeContactEmail;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ZonedDateTimeFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new ZonedDateTimeFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public Optional<LongFilter> optionalEventId() {
        return Optional.ofNullable(eventId);
    }

    public LongFilter eventId() {
        if (eventId == null) {
            setEventId(new LongFilter());
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserTaskCriteria that = (UserTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(completed, that.completed) &&
            Objects.equals(assigneeName, that.assigneeName) &&
            Objects.equals(assigneeContactPhone, that.assigneeContactPhone) &&
            Objects.equals(assigneeContactEmail, that.assigneeContactEmail) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            description,
            status,
            priority,
            dueDate,
            completed,
            assigneeName,
            assigneeContactPhone,
            assigneeContactEmail,
            createdAt,
            updatedAt,
            userId,
            eventId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTaskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalCompleted().map(f -> "completed=" + f + ", ").orElse("") +
            optionalAssigneeName().map(f -> "assigneeName=" + f + ", ").orElse("") +
            optionalAssigneeContactPhone().map(f -> "assigneeContactPhone=" + f + ", ").orElse("") +
            optionalAssigneeContactEmail().map(f -> "assigneeContactEmail=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalEventId().map(f -> "eventId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
