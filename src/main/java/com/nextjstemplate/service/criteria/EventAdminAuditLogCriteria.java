package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventAdminAuditLog} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.EventAdminAuditLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-admin-audit-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAdminAuditLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter action;

    private StringFilter tableName;

    private StringFilter recordId;

    private StringFilter changes;

    private ZonedDateTimeFilter createdAt;

    private LongFilter adminId;

    private Boolean distinct;

    public EventAdminAuditLogCriteria() {}

    public EventAdminAuditLogCriteria(EventAdminAuditLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.action = other.optionalAction().map(StringFilter::copy).orElse(null);
        this.tableName = other.optionalTableName().map(StringFilter::copy).orElse(null);
        this.recordId = other.optionalRecordId().map(StringFilter::copy).orElse(null);
        this.changes = other.optionalChanges().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.adminId = other.optionalAdminId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventAdminAuditLogCriteria copy() {
        return new EventAdminAuditLogCriteria(this);
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

    public StringFilter getAction() {
        return action;
    }

    public Optional<StringFilter> optionalAction() {
        return Optional.ofNullable(action);
    }

    public StringFilter action() {
        if (action == null) {
            setAction(new StringFilter());
        }
        return action;
    }

    public void setAction(StringFilter action) {
        this.action = action;
    }

    public StringFilter getTableName() {
        return tableName;
    }

    public Optional<StringFilter> optionalTableName() {
        return Optional.ofNullable(tableName);
    }

    public StringFilter tableName() {
        if (tableName == null) {
            setTableName(new StringFilter());
        }
        return tableName;
    }

    public void setTableName(StringFilter tableName) {
        this.tableName = tableName;
    }

    public StringFilter getRecordId() {
        return recordId;
    }

    public Optional<StringFilter> optionalRecordId() {
        return Optional.ofNullable(recordId);
    }

    public StringFilter recordId() {
        if (recordId == null) {
            setRecordId(new StringFilter());
        }
        return recordId;
    }

    public void setRecordId(StringFilter recordId) {
        this.recordId = recordId;
    }

    public StringFilter getChanges() {
        return changes;
    }

    public Optional<StringFilter> optionalChanges() {
        return Optional.ofNullable(changes);
    }

    public StringFilter changes() {
        if (changes == null) {
            setChanges(new StringFilter());
        }
        return changes;
    }

    public void setChanges(StringFilter changes) {
        this.changes = changes;
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

    public LongFilter getAdminId() {
        return adminId;
    }

    public Optional<LongFilter> optionalAdminId() {
        return Optional.ofNullable(adminId);
    }

    public LongFilter adminId() {
        if (adminId == null) {
            setAdminId(new LongFilter());
        }
        return adminId;
    }

    public void setAdminId(LongFilter adminId) {
        this.adminId = adminId;
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
        final EventAdminAuditLogCriteria that = (EventAdminAuditLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(action, that.action) &&
            Objects.equals(tableName, that.tableName) &&
            Objects.equals(recordId, that.recordId) &&
            Objects.equals(changes, that.changes) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(adminId, that.adminId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, action, tableName, recordId, changes, createdAt, adminId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAdminAuditLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalAction().map(f -> "action=" + f + ", ").orElse("") +
            optionalTableName().map(f -> "tableName=" + f + ", ").orElse("") +
            optionalRecordId().map(f -> "recordId=" + f + ", ").orElse("") +
            optionalChanges().map(f -> "changes=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalAdminId().map(f -> "adminId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
