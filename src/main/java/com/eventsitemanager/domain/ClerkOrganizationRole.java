package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Maps Clerk Organization roles to application roles and permissions.
 */
@Entity
@Table(
    name = "clerk_organization_role",
    uniqueConstraints = { @UniqueConstraint(name = "uq_clerk_org_role", columnNames = { "clerk_org_id", "clerk_role_name" }) }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClerkOrganizationRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "clerk_org_id", length = 255, nullable = false)
    private String clerkOrgId;

    @NotNull
    @Size(max = 100)
    @Column(name = "clerk_role_name", length = 100, nullable = false)
    private String clerkRoleName;

    @NotNull
    @Size(max = 100)
    @Column(name = "application_role", length = 100, nullable = false)
    private String applicationRole;

    @Column(name = "permissions", columnDefinition = "text")
    private String permissions;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClerkOrganizationRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClerkOrgId() {
        return this.clerkOrgId;
    }

    public ClerkOrganizationRole clerkOrgId(String clerkOrgId) {
        this.setClerkOrgId(clerkOrgId);
        return this;
    }

    public void setClerkOrgId(String clerkOrgId) {
        this.clerkOrgId = clerkOrgId;
    }

    public String getClerkRoleName() {
        return this.clerkRoleName;
    }

    public ClerkOrganizationRole clerkRoleName(String clerkRoleName) {
        this.setClerkRoleName(clerkRoleName);
        return this;
    }

    public void setClerkRoleName(String clerkRoleName) {
        this.clerkRoleName = clerkRoleName;
    }

    public String getApplicationRole() {
        return this.applicationRole;
    }

    public ClerkOrganizationRole applicationRole(String applicationRole) {
        this.setApplicationRole(applicationRole);
        return this;
    }

    public void setApplicationRole(String applicationRole) {
        this.applicationRole = applicationRole;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public ClerkOrganizationRole permissions(String permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ClerkOrganizationRole createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public ClerkOrganizationRole updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClerkOrganizationRole)) {
            return false;
        }
        return getId() != null && getId().equals(((ClerkOrganizationRole) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClerkOrganizationRole{" +
            "id=" + getId() +
            ", clerkOrgId='" + getClerkOrgId() + "'" +
            ", clerkRoleName='" + getClerkRoleName() + "'" +
            ", applicationRole='" + getApplicationRole() + "'" +
            ", permissions='" + getPermissions() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
