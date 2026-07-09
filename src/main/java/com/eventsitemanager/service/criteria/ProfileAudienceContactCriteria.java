package com.eventsitemanager.service.criteria;

import com.eventsitemanager.domain.enumeration.ProfileAudienceContactOptInStatus;
import com.eventsitemanager.domain.enumeration.ProfileAudienceContactSource;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileAudienceContactCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter tenantId;
    private LongFilter publicProfileId;
    private StringFilter email;
    private ProfileAudienceContactSourceFilter source;
    private ProfileAudienceContactOptInStatusFilter optInStatus;
    private ZonedDateTimeFilter createdAt;
    private ZonedDateTimeFilter updatedAt;
    private Boolean distinct;

    public ProfileAudienceContactCriteria() {}

    public ProfileAudienceContactCriteria(ProfileAudienceContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.publicProfileId = other.publicProfileId == null ? null : other.publicProfileId.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.source = other.source == null ? null : other.source.copy();
        this.optInStatus = other.optInStatus == null ? null : other.optInStatus.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProfileAudienceContactCriteria copy() {
        return new ProfileAudienceContactCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getPublicProfileId() {
        return publicProfileId;
    }

    public void setPublicProfileId(LongFilter publicProfileId) {
        this.publicProfileId = publicProfileId;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public ProfileAudienceContactSourceFilter getSource() {
        return source;
    }

    public void setSource(ProfileAudienceContactSourceFilter source) {
        this.source = source;
    }

    public ProfileAudienceContactOptInStatusFilter getOptInStatus() {
        return optInStatus;
    }

    public void setOptInStatus(ProfileAudienceContactOptInStatusFilter optInStatus) {
        this.optInStatus = optInStatus;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileAudienceContactCriteria that = (ProfileAudienceContactCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }

    public static class ProfileAudienceContactSourceFilter extends Filter<ProfileAudienceContactSource> {

        public ProfileAudienceContactSourceFilter() {}

        public ProfileAudienceContactSourceFilter(ProfileAudienceContactSourceFilter filter) {
            super(filter);
        }

        @Override
        public ProfileAudienceContactSourceFilter copy() {
            return new ProfileAudienceContactSourceFilter(this);
        }
    }

    public static class ProfileAudienceContactOptInStatusFilter extends Filter<ProfileAudienceContactOptInStatus> {

        public ProfileAudienceContactOptInStatusFilter() {}

        public ProfileAudienceContactOptInStatusFilter(ProfileAudienceContactOptInStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProfileAudienceContactOptInStatusFilter copy() {
            return new ProfileAudienceContactOptInStatusFilter(this);
        }
    }
}
