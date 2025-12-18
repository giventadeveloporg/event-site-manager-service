package com.nextjstemplate.service.criteria;

import com.nextjstemplate.domain.enumeration.TenantEmailType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.TenantEmailAddress} entity.
 * This class is used in {@link com.nextjstemplate.web.rest.TenantEmailAddressResource} to receive all the
 * possible filtering options from the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tenant-email-addresses?id.greaterThan=5&tenantId.equals=tenant1&emailType.equals=PRIMARY}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantEmailAddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter emailAddress;

    private StringFilter emailType;

    private StringFilter displayName;

    private StringFilter copyToEmailAddress;

    private BooleanFilter isActive;

    private BooleanFilter isDefault;

    private StringFilter description;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public TenantEmailAddressCriteria() {}

    public TenantEmailAddressCriteria(TenantEmailAddressCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.emailAddress = other.optionalEmailAddress().map(StringFilter::copy).orElse(null);
        this.emailType = other.optionalEmailType().map(StringFilter::copy).orElse(null);
        this.displayName = other.optionalDisplayName().map(StringFilter::copy).orElse(null);
        this.copyToEmailAddress = other.optionalCopyToEmailAddress().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.isDefault = other.optionalIsDefault().map(BooleanFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.updatedAt = other.optionalUpdatedAt().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TenantEmailAddressCriteria copy() {
        return new TenantEmailAddressCriteria(this);
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

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public Optional<StringFilter> optionalEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

    public StringFilter emailAddress() {
        if (emailAddress == null) {
            setEmailAddress(new StringFilter());
        }
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StringFilter getEmailType() {
        return emailType;
    }

    public Optional<StringFilter> optionalEmailType() {
        return Optional.ofNullable(emailType);
    }

    public StringFilter emailType() {
        if (emailType == null) {
            setEmailType(new StringFilter());
        }
        return emailType;
    }

    public void setEmailType(StringFilter emailType) {
        this.emailType = emailType;
    }

    public StringFilter getDisplayName() {
        return displayName;
    }

    public Optional<StringFilter> optionalDisplayName() {
        return Optional.ofNullable(displayName);
    }

    public StringFilter displayName() {
        if (displayName == null) {
            setDisplayName(new StringFilter());
        }
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }

    public StringFilter getCopyToEmailAddress() {
        return copyToEmailAddress;
    }

    public Optional<StringFilter> optionalCopyToEmailAddress() {
        return Optional.ofNullable(copyToEmailAddress);
    }

    public StringFilter copyToEmailAddress() {
        if (copyToEmailAddress == null) {
            setCopyToEmailAddress(new StringFilter());
        }
        return copyToEmailAddress;
    }

    public void setCopyToEmailAddress(StringFilter copyToEmailAddress) {
        this.copyToEmailAddress = copyToEmailAddress;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter getIsDefault() {
        return isDefault;
    }

    public Optional<BooleanFilter> optionalIsDefault() {
        return Optional.ofNullable(isDefault);
    }

    public BooleanFilter isDefault() {
        if (isDefault == null) {
            setIsDefault(new BooleanFilter());
        }
        return isDefault;
    }

    public void setIsDefault(BooleanFilter isDefault) {
        this.isDefault = isDefault;
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

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public Optional<InstantFilter> optionalUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            setUpdatedAt(new InstantFilter());
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
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
        final TenantEmailAddressCriteria that = (TenantEmailAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(emailType, that.emailType) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(copyToEmailAddress, that.copyToEmailAddress) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isDefault, that.isDefault) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            emailAddress,
            emailType,
            displayName,
            copyToEmailAddress,
            isActive,
            isDefault,
            description,
            createdAt,
            updatedAt,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantEmailAddressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalEmailAddress().map(f -> "emailAddress=" + f + ", ").orElse("") +
            optionalEmailType().map(f -> "emailType=" + f + ", ").orElse("") +
            optionalDisplayName().map(f -> "displayName=" + f + ", ").orElse("") +
            optionalCopyToEmailAddress().map(f -> "copyToEmailAddress=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalIsDefault().map(f -> "isDefault=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalUpdatedAt().map(f -> "updatedAt=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}";
    }
}
