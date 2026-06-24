package com.eventsitemanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eventsitemanager.domain.CommunicationCampaign} entity. This class is used
 * in {@link com.eventsitemanager.web.rest.CommunicationCampaignResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /communication-campaigns?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunicationCampaignCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter name;

    private StringFilter type;

    private StringFilter description;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter scheduledAt;

    private ZonedDateTimeFilter sentAt;

    private StringFilter status;

    private LongFilter createdById;

    private Boolean distinct;

    public CommunicationCampaignCriteria() {}

    public CommunicationCampaignCriteria(CommunicationCampaignCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.scheduledAt = other.scheduledAt == null ? null : other.scheduledAt.copy();
        this.sentAt = other.sentAt == null ? null : other.sentAt.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommunicationCampaignCriteria copy() {
        return new CommunicationCampaignCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public ZonedDateTimeFilter getScheduledAt() {
        return scheduledAt;
    }

    public ZonedDateTimeFilter scheduledAt() {
        if (scheduledAt == null) {
            scheduledAt = new ZonedDateTimeFilter();
        }
        return scheduledAt;
    }

    public void setScheduledAt(ZonedDateTimeFilter scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public ZonedDateTimeFilter getSentAt() {
        return sentAt;
    }

    public ZonedDateTimeFilter sentAt() {
        if (sentAt == null) {
            sentAt = new ZonedDateTimeFilter();
        }
        return sentAt;
    }

    public void setSentAt(ZonedDateTimeFilter sentAt) {
        this.sentAt = sentAt;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
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
        final CommunicationCampaignCriteria that = (CommunicationCampaignCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(scheduledAt, that.scheduledAt) &&
            Objects.equals(sentAt, that.sentAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            name,
            type,
            description,
            createdById,
            createdAt,
            scheduledAt,
            sentAt,
            status,
            createdById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunicationCampaignCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (scheduledAt != null ? "scheduledAt=" + scheduledAt + ", " : "") +
            (sentAt != null ? "sentAt=" + sentAt + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
