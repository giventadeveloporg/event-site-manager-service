package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eventsitemanager.domain.EventTicketType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTypeDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    private Boolean isServiceFeeIncluded;

    private BigDecimal serviceFee;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Size(max = 255)
    private String code;

    private Integer availableQuantity;

    private Integer soldQuantity;

    private Integer remainingQuantity;

    private Boolean isActive;

    private ZonedDateTime saleStartDate;

    private ZonedDateTime saleEndDate;

    private Integer minQuantityPerOrder;

    private Integer maxQuantityPerOrder;

    private Boolean requiresApproval;

    private Integer sortOrder;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    private EventDetailsDTO event;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsServiceFeeIncluded() {
        return isServiceFeeIncluded;
    }

    public void setIsServiceFeeIncluded(Boolean isServiceFeeIncluded) {
        this.isServiceFeeIncluded = isServiceFeeIncluded;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(ZonedDateTime saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public ZonedDateTime getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(ZonedDateTime saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public Integer getMinQuantityPerOrder() {
        return minQuantityPerOrder;
    }

    public void setMinQuantityPerOrder(Integer minQuantityPerOrder) {
        this.minQuantityPerOrder = minQuantityPerOrder;
    }

    public Integer getMaxQuantityPerOrder() {
        return maxQuantityPerOrder;
    }

    public void setMaxQuantityPerOrder(Integer maxQuantityPerOrder) {
        this.maxQuantityPerOrder = maxQuantityPerOrder;
    }

    public Boolean getRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EventDetailsDTO getEvent() {
        return event;
    }

    public void setEvent(EventDetailsDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTicketTypeDTO)) {
            return false;
        }

        EventTicketTypeDTO eventTicketTypeDTO = (EventTicketTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventTicketTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTypeDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isServiceFeeIncluded='" + getIsServiceFeeIncluded() + "'" +
            ", serviceFee=" + getServiceFee() +
            ", price=" + getPrice() +
            ", code='" + getCode() + "'" +
            ", availableQuantity=" + getAvailableQuantity() +
            ", soldQuantity=" + getSoldQuantity() +
            ", remainingQuantity=" + getRemainingQuantity() +
            ", isActive='" + getIsActive() + "'" +
            ", saleStartDate='" + getSaleStartDate() + "'" +
            ", saleEndDate='" + getSaleEndDate() + "'" +
            ", minQuantityPerOrder=" + getMinQuantityPerOrder() +
            ", maxQuantityPerOrder=" + getMaxQuantityPerOrder() +
            ", requiresApproval='" + getRequiresApproval() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", event=" + getEvent() +
            "}";
    }
}
