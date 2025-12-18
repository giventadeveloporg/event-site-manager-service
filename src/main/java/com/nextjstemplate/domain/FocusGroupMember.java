package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FocusGroupMember entity representing membership in a focus group.
 */
@Entity
@Table(
    name = "focus_group_member",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_focus_group_member", columnNames = { "tenant_id", "focus_group_id", "user_profile_id" }),
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FocusGroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    @NotNull
    @Column(name = "user_profile_id", nullable = false)
    private Long userProfileId;

    @NotNull
    @Size(max = 50)
    @Column(name = "role", length = 50, nullable = false)
    private String role;

    @NotNull
    @Size(max = 50)
    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "focus_group_id", nullable = false)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private FocusGroup focusGroup;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FocusGroupMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public FocusGroupMember tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserProfileId() {
        return this.userProfileId;
    }

    public FocusGroupMember userProfileId(Long userProfileId) {
        this.setUserProfileId(userProfileId);
        return this;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getRole() {
        return this.role;
    }

    public FocusGroupMember role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return this.status;
    }

    public FocusGroupMember status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public FocusGroupMember createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public FocusGroupMember updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public FocusGroup getFocusGroup() {
        return this.focusGroup;
    }

    public void setFocusGroup(FocusGroup focusGroup) {
        this.focusGroup = focusGroup;
    }

    public FocusGroupMember focusGroup(FocusGroup focusGroup) {
        this.setFocusGroup(focusGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FocusGroupMember)) {
            return false;
        }
        return getId() != null && getId().equals(((FocusGroupMember) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "FocusGroupMember{" +
        "id=" + getId() +
        ", tenantId='" + getTenantId() + "'" +
        ", userProfileId=" + getUserProfileId() +
        ", role='" + getRole() + "'" +
        ", status='" + getStatus() + "'" +
        ", createdAt='" + getCreatedAt() + "'" +
        ", updatedAt='" + getUpdatedAt() + "'" +
        "}";
  }
}
