package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.SatelliteDomain}
 * entity. This class is used
 * in {@link com.nextjstemplate.web.rest.SatelliteDomainResource} to receive all
 * the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /satellite-domains?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SatelliteDomainCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter satelliteKey;

    private StringFilter domain;

    private StringFilter hostname;

    private StringFilter displayName;

    private StringFilter tenantId;

    private BooleanFilter enabled;

    private StringFilter orgName;

    private StringFilter fullName;

    private StringFilter contactEmail;

    private BooleanFilter showOnAuthHeader;

    private BooleanFilter showOnAuthFooter;

    private Boolean distinct;

    public SatelliteDomainCriteria() {}

    public SatelliteDomainCriteria(SatelliteDomainCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.satelliteKey = other.optionalSatelliteKey().map(StringFilter::copy).orElse(null);
        this.domain = other.optionalDomain().map(StringFilter::copy).orElse(null);
        this.hostname = other.optionalHostname().map(StringFilter::copy).orElse(null);
        this.displayName = other.optionalDisplayName().map(StringFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.enabled = other.optionalEnabled().map(BooleanFilter::copy).orElse(null);
        this.orgName = other.optionalOrgName().map(StringFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.contactEmail = other.optionalContactEmail().map(StringFilter::copy).orElse(null);
        this.showOnAuthHeader = other.optionalShowOnAuthHeader().map(BooleanFilter::copy).orElse(null);
        this.showOnAuthFooter = other.optionalShowOnAuthFooter().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SatelliteDomainCriteria copy() {
        return new SatelliteDomainCriteria(this);
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

    public StringFilter getSatelliteKey() {
        return satelliteKey;
    }

    public Optional<StringFilter> optionalSatelliteKey() {
        return Optional.ofNullable(satelliteKey);
    }

    public StringFilter satelliteKey() {
        if (satelliteKey == null) {
            setSatelliteKey(new StringFilter());
        }
        return satelliteKey;
    }

    public void setSatelliteKey(StringFilter satelliteKey) {
        this.satelliteKey = satelliteKey;
    }

    public StringFilter getDomain() {
        return domain;
    }

    public Optional<StringFilter> optionalDomain() {
        return Optional.ofNullable(domain);
    }

    public StringFilter domain() {
        if (domain == null) {
            setDomain(new StringFilter());
        }
        return domain;
    }

    public void setDomain(StringFilter domain) {
        this.domain = domain;
    }

    public StringFilter getHostname() {
        return hostname;
    }

    public Optional<StringFilter> optionalHostname() {
        return Optional.ofNullable(hostname);
    }

    public StringFilter hostname() {
        if (hostname == null) {
            setHostname(new StringFilter());
        }
        return hostname;
    }

    public void setHostname(StringFilter hostname) {
        this.hostname = hostname;
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

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public Optional<BooleanFilter> optionalEnabled() {
        return Optional.ofNullable(enabled);
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            setEnabled(new BooleanFilter());
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public StringFilter getOrgName() {
        return orgName;
    }

    public Optional<StringFilter> optionalOrgName() {
        return Optional.ofNullable(orgName);
    }

    public StringFilter orgName() {
        if (orgName == null) {
            setOrgName(new StringFilter());
        }
        return orgName;
    }

    public void setOrgName(StringFilter orgName) {
        this.orgName = orgName;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public Optional<StringFilter> optionalContactEmail() {
        return Optional.ofNullable(contactEmail);
    }

    public StringFilter contactEmail() {
        if (contactEmail == null) {
            setContactEmail(new StringFilter());
        }
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public BooleanFilter getShowOnAuthHeader() {
        return showOnAuthHeader;
    }

    public Optional<BooleanFilter> optionalShowOnAuthHeader() {
        return Optional.ofNullable(showOnAuthHeader);
    }

    public BooleanFilter showOnAuthHeader() {
        if (showOnAuthHeader == null) {
            setShowOnAuthHeader(new BooleanFilter());
        }
        return showOnAuthHeader;
    }

    public void setShowOnAuthHeader(BooleanFilter showOnAuthHeader) {
        this.showOnAuthHeader = showOnAuthHeader;
    }

    public BooleanFilter getShowOnAuthFooter() {
        return showOnAuthFooter;
    }

    public Optional<BooleanFilter> optionalShowOnAuthFooter() {
        return Optional.ofNullable(showOnAuthFooter);
    }

    public BooleanFilter showOnAuthFooter() {
        if (showOnAuthFooter == null) {
            setShowOnAuthFooter(new BooleanFilter());
        }
        return showOnAuthFooter;
    }

    public void setShowOnAuthFooter(BooleanFilter showOnAuthFooter) {
        this.showOnAuthFooter = showOnAuthFooter;
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
        final SatelliteDomainCriteria that = (SatelliteDomainCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(satelliteKey, that.satelliteKey) &&
            Objects.equals(domain, that.domain) &&
            Objects.equals(hostname, that.hostname) &&
            Objects.equals(displayName, that.displayName) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(orgName, that.orgName) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(showOnAuthHeader, that.showOnAuthHeader) &&
            Objects.equals(showOnAuthFooter, that.showOnAuthFooter) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            satelliteKey,
            domain,
            hostname,
            displayName,
            tenantId,
            enabled,
            orgName,
            fullName,
            contactEmail,
            showOnAuthHeader,
            showOnAuthFooter,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SatelliteDomainCriteria{" +
                optionalId().map(f -> "id=" + f + ", ").orElse("") +
                optionalSatelliteKey().map(f -> "satelliteKey=" + f + ", ").orElse("") +
                optionalDomain().map(f -> "domain=" + f + ", ").orElse("") +
                optionalHostname().map(f -> "hostname=" + f + ", ").orElse("") +
                optionalDisplayName().map(f -> "displayName=" + f + ", ").orElse("") +
                optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
                optionalEnabled().map(f -> "enabled=" + f + ", ").orElse("") +
                optionalOrgName().map(f -> "orgName=" + f + ", ").orElse("") +
                optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
                optionalContactEmail().map(f -> "contactEmail=" + f + ", ").orElse("") +
                optionalShowOnAuthHeader().map(f -> "showOnAuthHeader=" + f + ", ").orElse("") +
                optionalShowOnAuthFooter().map(f -> "showOnAuthFooter=" + f + ", ").orElse("") +
                optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
                "}";
    }
}
