package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.EventCompetitionContentBlock} entity.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCompetitionContentBlockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter blockType;

    private StringFilter title;

    private StringFilter bodyMarkdown;

    private IntegerFilter sortOrder;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter eventId;

    private Boolean distinct;

    public EventCompetitionContentBlockCriteria() {}

    public EventCompetitionContentBlockCriteria(EventCompetitionContentBlockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.blockType = other.blockType == null ? null : other.blockType.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.bodyMarkdown = other.bodyMarkdown == null ? null : other.bodyMarkdown.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCompetitionContentBlockCriteria copy() {
        return new EventCompetitionContentBlockCriteria(this);
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

    public StringFilter getBlockType() {
        return blockType;
    }

    public StringFilter blockType() {
        if (blockType == null) {
            blockType = new StringFilter();
        }
        return blockType;
    }

    public void setBlockType(StringFilter blockType) {
        this.blockType = blockType;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getBodyMarkdown() {
        return bodyMarkdown;
    }

    public StringFilter bodyMarkdown() {
        if (bodyMarkdown == null) {
            bodyMarkdown = new StringFilter();
        }
        return bodyMarkdown;
    }

    public void setBodyMarkdown(StringFilter bodyMarkdown) {
        this.bodyMarkdown = bodyMarkdown;
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

    public ZonedDateTimeFilter getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTimeFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new ZonedDateTimeFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTimeFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
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
        final EventCompetitionContentBlockCriteria that = (EventCompetitionContentBlockCriteria) o;
        return Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distinct);
    }
}
