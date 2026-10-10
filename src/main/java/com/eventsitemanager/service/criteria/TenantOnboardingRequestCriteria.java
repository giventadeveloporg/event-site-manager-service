package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.OnboardingRequestSource;
import com.eventsitemanager.domain.enumeration.OnboardingRequestStatus;
import com.eventsitemanager.domain.enumeration.SiteType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.TenantOnboardingRequest} entity.
 * Example: {@code /api/tenant-onboarding-requests?status.equals=PENDING&organizationName.contains=club}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TenantOnboardingRequestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OnboardingRequestStatus
     */
    public static class OnboardingRequestStatusFilter extends Filter<OnboardingRequestStatus> {

        public OnboardingRequestStatusFilter() {}

        public OnboardingRequestStatusFilter(OnboardingRequestStatusFilter filter) {
            super(filter);
        }

        @Override
        public OnboardingRequestStatusFilter copy() {
            return new OnboardingRequestStatusFilter(this);
        }
    }

    /**
     * Class for filtering OnboardingRequestSource
     */
    public static class OnboardingRequestSourceFilter extends Filter<OnboardingRequestSource> {

        public OnboardingRequestSourceFilter() {}

        public OnboardingRequestSourceFilter(OnboardingRequestSourceFilter filter) {
            super(filter);
        }

        @Override
        public OnboardingRequestSourceFilter copy() {
            return new OnboardingRequestSourceFilter(this);
        }
    }

    /**
     * Class for filtering SiteType
     */
    public static class SiteTypeFilter extends Filter<SiteType> {

        public SiteTypeFilter() {}

        public SiteTypeFilter(SiteTypeFilter filter) {
            super(filter);
        }

        @Override
        public SiteTypeFilter copy() {
            return new SiteTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter requestCode;

    private OnboardingRequestStatusFilter status;

    private OnboardingRequestSourceFilter source;

    private StringFilter organizationName;

    private SiteTypeFilter siteType;

    private StringFilter contactEmail;

    private StringFilter requestedHostname;

    private StringFilter assignedTenantId;

    private ZonedDateTimeFilter createdAt;

    private Boolean distinct;

    public TenantOnboardingRequestCriteria() {}

    public TenantOnboardingRequestCriteria(TenantOnboardingRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestCode = other.requestCode == null ? null : other.requestCode.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.source = other.source == null ? null : other.source.copy();
        this.organizationName = other.organizationName == null ? null : other.organizationName.copy();
        this.siteType = other.siteType == null ? null : other.siteType.copy();
        this.contactEmail = other.contactEmail == null ? null : other.contactEmail.copy();
        this.requestedHostname = other.requestedHostname == null ? null : other.requestedHostname.copy();
        this.assignedTenantId = other.assignedTenantId == null ? null : other.assignedTenantId.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TenantOnboardingRequestCriteria copy() {
        return new TenantOnboardingRequestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(StringFilter requestCode) {
        this.requestCode = requestCode;
    }

    public OnboardingRequestStatusFilter getStatus() {
        return status;
    }

    public void setStatus(OnboardingRequestStatusFilter status) {
        this.status = status;
    }

    public OnboardingRequestSourceFilter getSource() {
        return source;
    }

    public void setSource(OnboardingRequestSourceFilter source) {
        this.source = source;
    }

    public StringFilter getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(StringFilter organizationName) {
        this.organizationName = organizationName;
    }

    public SiteTypeFilter getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteTypeFilter siteType) {
        this.siteType = siteType;
    }

    public StringFilter getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(StringFilter contactEmail) {
        this.contactEmail = contactEmail;
    }

    public StringFilter getRequestedHostname() {
        return requestedHostname;
    }

    public void setRequestedHostname(StringFilter requestedHostname) {
        this.requestedHostname = requestedHostname;
    }

    public StringFilter getAssignedTenantId() {
        return assignedTenantId;
    }

    public void setAssignedTenantId(StringFilter assignedTenantId) {
        this.assignedTenantId = assignedTenantId;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDistinct() {
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
        final TenantOnboardingRequestCriteria that = (TenantOnboardingRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestCode, that.requestCode) &&
            Objects.equals(status, that.status) &&
            Objects.equals(source, that.source) &&
            Objects.equals(organizationName, that.organizationName) &&
            Objects.equals(siteType, that.siteType) &&
            Objects.equals(contactEmail, that.contactEmail) &&
            Objects.equals(requestedHostname, that.requestedHostname) &&
            Objects.equals(assignedTenantId, that.assignedTenantId) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            requestCode,
            status,
            source,
            organizationName,
            siteType,
            contactEmail,
            requestedHostname,
            assignedTenantId,
            createdAt,
            distinct
        );
    }

    @Override
    public String toString() {
        return (
            "TenantOnboardingRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestCode != null ? "requestCode=" + requestCode + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (source != null ? "source=" + source + ", " : "") +
            (organizationName != null ? "organizationName=" + organizationName + ", " : "") +
            (siteType != null ? "siteType=" + siteType + ", " : "") +
            (contactEmail != null ? "contactEmail=" + contactEmail + ", " : "") +
            (requestedHostname != null ? "requestedHostname=" + requestedHostname + ", " : "") +
            (assignedTenantId != null ? "assignedTenantId=" + assignedTenantId + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}"
        );
    }
}
