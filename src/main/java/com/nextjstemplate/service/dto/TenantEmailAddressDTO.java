package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.TenantEmailAddress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantEmailAddressDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    @Email
    private String emailAddress;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String emailType;

    @Size(max = 255)
    private String displayName;

    @NotNull
    @Size(max = 255)
    private String copyToEmailAddress;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isDefault;

    private String description;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCopyToEmailAddress() {
        return copyToEmailAddress;
    }

    public void setCopyToEmailAddress(String copyToEmailAddress) {
        this.copyToEmailAddress = copyToEmailAddress;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantEmailAddressDTO)) {
            return false;
        }

        TenantEmailAddressDTO tenantEmailAddressDTO = (TenantEmailAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tenantEmailAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantEmailAddressDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", emailType='" + getEmailType() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", copyToEmailAddress='" + getCopyToEmailAddress() + "'" +
            ", isActive=" + getIsActive() +
            ", isDefault=" + getIsDefault() +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
