package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.GalleryAlbum} entity.
 * This class is used in {@link com.nextjstemplate.web.rest.GalleryAlbumResource} to receive
 * all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gallery-albums?id.greaterThan=5&isPublic.equals=true&displayOrder.lessThan=10}
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GalleryAlbumCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter title;

    private StringFilter description;

    private StringFilter coverImageUrl;

    private BooleanFilter isPublic;

    private IntegerFilter displayOrder;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter updatedAt;

    private LongFilter createdById;

    private Boolean distinct;

    public GalleryAlbumCriteria() {}

    public GalleryAlbumCriteria(GalleryAlbumCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tenantId = other.tenantId == null ? null : other.tenantId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.coverImageUrl = other.coverImageUrl == null ? null : other.coverImageUrl.copy();
        this.isPublic = other.isPublic == null ? null : other.isPublic.copy();
        this.displayOrder = other.displayOrder == null ? null : other.displayOrder.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GalleryAlbumCriteria copy() {
        return new GalleryAlbumCriteria(this);
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

    public StringFilter getCoverImageUrl() {
        return coverImageUrl;
    }

    public StringFilter coverImageUrl() {
        if (coverImageUrl == null) {
            coverImageUrl = new StringFilter();
        }
        return coverImageUrl;
    }

    public void setCoverImageUrl(StringFilter coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            isPublic = new BooleanFilter();
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public IntegerFilter getDisplayOrder() {
        return displayOrder;
    }

    public IntegerFilter displayOrder() {
        if (displayOrder == null) {
            displayOrder = new IntegerFilter();
        }
        return displayOrder;
    }

    public void setDisplayOrder(IntegerFilter displayOrder) {
        this.displayOrder = displayOrder;
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
        final GalleryAlbumCriteria that = (GalleryAlbumCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(coverImageUrl, that.coverImageUrl) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(displayOrder, that.displayOrder) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            title,
            description,
            coverImageUrl,
            isPublic,
            displayOrder,
            createdAt,
            updatedAt,
            createdById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GalleryAlbumCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (coverImageUrl != null ? "coverImageUrl=" + coverImageUrl + ", " : "") +
            (isPublic != null ? "isPublic=" + isPublic + ", " : "") +
            (displayOrder != null ? "displayOrder=" + displayOrder + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
