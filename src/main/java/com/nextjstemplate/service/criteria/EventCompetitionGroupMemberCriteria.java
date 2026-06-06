package com.nextjstemplate.service.criteria;

import com.nextjstemplate.domain.enumeration.CompetitionGroupMemberRole;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventCompetitionGroupMember} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionGroupMemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private CompetitionGroupMemberRoleFilter memberRole;

    private IntegerFilter sortOrder;

    private ZonedDateTimeFilter createdAt;

    private LongFilter registrationId;

    private LongFilter participantProfileId;

    private Boolean distinct;

    public EventCompetitionGroupMemberCriteria() {}

    public EventCompetitionGroupMemberCriteria(EventCompetitionGroupMemberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.memberRole = other.memberRole == null ? null : other.memberRole.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.registrationId = other.registrationId == null ? null : other.registrationId.copy();
        this.participantProfileId = other.participantProfileId == null ? null : other.participantProfileId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionGroupMemberCriteria copy() {
        return new EventCompetitionGroupMemberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTenantId() {
        return tenantId;
    }

    public StringFilter tenantId() {
        if (tenantId == null) {
            tenantId = new StringFilter();
        }
        return tenantId;
    }

    public void setTenantId(StringFilter tenantId) {
        this.tenantId = tenantId;
    }

    public CompetitionGroupMemberRoleFilter getMemberRole() {
        return memberRole;
    }

    public CompetitionGroupMemberRoleFilter memberRole() {
        if (memberRole == null) {
            memberRole = new CompetitionGroupMemberRoleFilter();
        }
        return memberRole;
    }

    public void setMemberRole(CompetitionGroupMemberRoleFilter memberRole) {
        this.memberRole = memberRole;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            sortOrder = new IntegerFilter();
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            createdAt = new ZonedDateTimeFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LongFilter getRegistrationId() {
        return registrationId;
    }

    public LongFilter registrationId() {
        if (registrationId == null) {
            registrationId = new LongFilter();
        }
        return registrationId;
    }

    public void setRegistrationId(LongFilter registrationId) {
        this.registrationId = registrationId;
    }

    public LongFilter getParticipantProfileId() {
        return participantProfileId;
    }

    public LongFilter participantProfileId() {
        if (participantProfileId == null) {
            participantProfileId = new LongFilter();
        }
        return participantProfileId;
    }

    public void setParticipantProfileId(LongFilter participantProfileId) {
        this.participantProfileId = participantProfileId;
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
        final EventCompetitionGroupMemberCriteria that = (EventCompetitionGroupMemberCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }

    public static class CompetitionGroupMemberRoleFilter extends Filter<CompetitionGroupMemberRole> {

        public CompetitionGroupMemberRoleFilter() {}

        public CompetitionGroupMemberRoleFilter(CompetitionGroupMemberRoleFilter other) {
            super(other);
        }

        @Override
        public CompetitionGroupMemberRoleFilter copy() {
            return new CompetitionGroupMemberRoleFilter(this);
        }
    }
}
