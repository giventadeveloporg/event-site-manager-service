package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventAdminAuditLog.
 */
@Entity
@Table(name = "event_admin_audit_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventAdminAuditLog implements Serializable {

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
    @Column(name = "action", length = 255, nullable = false)
    private String action;

    @NotNull
    @Size(max = 255)
    @Column(name = "table_name", length = 255, nullable = false)
    private String tableName;

    @NotNull
    @Size(max = 255)
    @Column(name = "record_id", length = 255, nullable = false)
    private String recordId;

    @Size(max = 8192)
    @Column(name = "changes", length = 8192)
    private String changes;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reviewedByAdmin", "userSubscription" }, allowSetters = true)
    private UserProfile admin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventAdminAuditLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventAdminAuditLog tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAction() {
        return this.action;
    }

    public EventAdminAuditLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return this.tableName;
    }

    public EventAdminAuditLog tableName(String tableName) {
        this.setTableName(tableName);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public EventAdminAuditLog recordId(String recordId) {
        this.setRecordId(recordId);
        return this;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getChanges() {
        return this.changes;
    }

    public EventAdminAuditLog changes(String changes) {
        this.setChanges(changes);
        return this;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventAdminAuditLog createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserProfile getAdmin() {
        return this.admin;
    }

    public void setAdmin(UserProfile userProfile) {
        this.admin = userProfile;
    }

    public EventAdminAuditLog admin(UserProfile userProfile) {
        this.setAdmin(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventAdminAuditLog)) {
            return false;
        }
        return getId() != null && getId().equals(((EventAdminAuditLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventAdminAuditLog{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", action='" + getAction() + "'" +
            ", tableName='" + getTableName() + "'" +
            ", recordId='" + getRecordId() + "'" +
            ", changes='" + getChanges() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
