package com.nextjstemplate.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nextjstemplate.domain.BulkOperationLog} entity. This class is used
 * in {@link com.nextjstemplate.web.rest.BulkOperationLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bulk-operation-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BulkOperationLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tenantId;

    private StringFilter operationType;

    private IntegerFilter targetCount;

    private IntegerFilter successCount;

    private IntegerFilter errorCount;

    private StringFilter operationDetails;

    private ZonedDateTimeFilter createdAt;

    private LongFilter performedById;

    private Boolean distinct;

    public BulkOperationLogCriteria() {}

    public BulkOperationLogCriteria(BulkOperationLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tenantId = other.optionalTenantId().map(StringFilter::copy).orElse(null);
        this.operationType = other.optionalOperationType().map(StringFilter::copy).orElse(null);
        this.targetCount = other.optionalTargetCount().map(IntegerFilter::copy).orElse(null);
        this.successCount = other.optionalSuccessCount().map(IntegerFilter::copy).orElse(null);
        this.errorCount = other.optionalErrorCount().map(IntegerFilter::copy).orElse(null);
        this.operationDetails = other.optionalOperationDetails().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(ZonedDateTimeFilter::copy).orElse(null);
        this.performedById = other.optionalPerformedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BulkOperationLogCriteria copy() {
        return new BulkOperationLogCriteria(this);
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

    public StringFilter getOperationType() {
        return operationType;
    }

    public Optional<StringFilter> optionalOperationType() {
        return Optional.ofNullable(operationType);
    }

    public StringFilter operationType() {
        if (operationType == null) {
            setOperationType(new StringFilter());
        }
        return operationType;
    }

    public void setOperationType(StringFilter operationType) {
        this.operationType = operationType;
    }

    public IntegerFilter getTargetCount() {
        return targetCount;
    }

    public Optional<IntegerFilter> optionalTargetCount() {
        return Optional.ofNullable(targetCount);
    }

    public IntegerFilter targetCount() {
        if (targetCount == null) {
            setTargetCount(new IntegerFilter());
        }
        return targetCount;
    }

    public void setTargetCount(IntegerFilter targetCount) {
        this.targetCount = targetCount;
    }

    public IntegerFilter getSuccessCount() {
        return successCount;
    }

    public Optional<IntegerFilter> optionalSuccessCount() {
        return Optional.ofNullable(successCount);
    }

    public IntegerFilter successCount() {
        if (successCount == null) {
            setSuccessCount(new IntegerFilter());
        }
        return successCount;
    }

    public void setSuccessCount(IntegerFilter successCount) {
        this.successCount = successCount;
    }

    public IntegerFilter getErrorCount() {
        return errorCount;
    }

    public Optional<IntegerFilter> optionalErrorCount() {
        return Optional.ofNullable(errorCount);
    }

    public IntegerFilter errorCount() {
        if (errorCount == null) {
            setErrorCount(new IntegerFilter());
        }
        return errorCount;
    }

    public void setErrorCount(IntegerFilter errorCount) {
        this.errorCount = errorCount;
    }

    public StringFilter getOperationDetails() {
        return operationDetails;
    }

    public Optional<StringFilter> optionalOperationDetails() {
        return Optional.ofNullable(operationDetails);
    }

    public StringFilter operationDetails() {
        if (operationDetails == null) {
            setOperationDetails(new StringFilter());
        }
        return operationDetails;
    }

    public void setOperationDetails(StringFilter operationDetails) {
        this.operationDetails = operationDetails;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public ZonedDateTimeFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new ZonedDateTimeFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LongFilter getPerformedById() {
        return performedById;
    }

    public Optional<LongFilter> optionalPerformedById() {
        return Optional.ofNullable(performedById);
    }

    public LongFilter performedById() {
        if (performedById == null) {
            setPerformedById(new LongFilter());
        }
        return performedById;
    }

    public void setPerformedById(LongFilter performedById) {
        this.performedById = performedById;
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
        final BulkOperationLogCriteria that = (BulkOperationLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(operationType, that.operationType) &&
            Objects.equals(targetCount, that.targetCount) &&
            Objects.equals(successCount, that.successCount) &&
            Objects.equals(errorCount, that.errorCount) &&
            Objects.equals(operationDetails, that.operationDetails) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(performedById, that.performedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tenantId,
            operationType,
            targetCount,
            successCount,
            errorCount,
            operationDetails,
            createdAt,
            performedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BulkOperationLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTenantId().map(f -> "tenantId=" + f + ", ").orElse("") +
            optionalOperationType().map(f -> "operationType=" + f + ", ").orElse("") +
            optionalTargetCount().map(f -> "targetCount=" + f + ", ").orElse("") +
            optionalSuccessCount().map(f -> "successCount=" + f + ", ").orElse("") +
            optionalErrorCount().map(f -> "errorCount=" + f + ", ").orElse("") +
            optionalOperationDetails().map(f -> "operationDetails=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalPerformedById().map(f -> "performedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
