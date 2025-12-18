package com.nextjstemplate.domain;

import com.nextjstemplate.domain.enumeration.TenantEmailType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TenantEmailAddress entity representing email addresses associated with tenants.
 */
@Entity
@Table(
    name = "tenant_email_addresses",
    uniqueConstraints = {
        @UniqueConstraint(name = "ux_tenant_email_addresses_tenant_type", columnNames = { "tenant_id", "email_type", "email_address" }),
    }
)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantEmailAddress implements Serializable {

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
    @Size(max = 255)
    @Email
    @Column(name = "email_address", length = 255, nullable = false)
    private String emailAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", length = 255, nullable = false)
    private TenantEmailType emailType;

    @Size(max = 255)
    @Column(name = "display_name", length = 255)
    private String displayName;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Note: tenant_id is a string identifier, not a foreign key to TenantOrganization
    // Removed @ManyToOne relationship to avoid type mismatch errors

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (isActive == null) {
            isActive = true;
        }
        if (isDefault == null) {
            isDefault = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TenantEmailAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public TenantEmailAddress tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public TenantEmailAddress emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public TenantEmailType getEmailType() {
        return this.emailType;
    }

    public TenantEmailAddress emailType(TenantEmailType emailType) {
        this.setEmailType(emailType);
        return this;
    }

    public void setEmailType(TenantEmailType emailType) {
        this.emailType = emailType;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public TenantEmailAddress displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TenantEmailAddress isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public TenantEmailAddress isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getDescription() {
        return this.description;
    }

    public TenantEmailAddress description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TenantEmailAddress createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TenantEmailAddress updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantEmailAddress)) {
            return false;
        }
        return getId() != null && getId().equals(((TenantEmailAddress) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantEmailAddress{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", emailType='" + getEmailType() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", isActive=" + getIsActive() +
            ", isDefault=" + getIsDefault() +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
