package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.EventAdminAuditLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAdminAuditLogDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String action;

    @NotNull
    @Size(max = 255)
    private String tableName;

    @NotNull
    @Size(max = 255)
    private String recordId;

    @Size(max = 8192)
    private String changes;

    @NotNull
    private ZonedDateTime createdAt;

    private UserProfileDTO admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfileDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserProfileDTO admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAdminAuditLogDTO)) {
            return false;
        }

        EventAdminAuditLogDTO eventAdminAuditLogDTO = (EventAdminAuditLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventAdminAuditLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAdminAuditLogDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", action='" + getAction() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", recordId='" + getRecordId() + "'" +
            ", changes='" + getChanges() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", admin=" + getAdmin() +
            "}";
    }
}
