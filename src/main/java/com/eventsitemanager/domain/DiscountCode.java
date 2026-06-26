package com.eventsitemanager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DiscountCode.
 */
@Entity
@Table(name = "discount_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiscountCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discountCodeSeq")
    @SequenceGenerator(name = "discountCodeSeq", sequenceName = "public.discount_code_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Size(max = 20)
    @Column(name = "discount_type", length = 20, nullable = false)
    private String discountType;

    @NotNull
    @Column(name = "discount_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Column(name = "max_uses")
    private Integer maxUses;

    @Column(name = "uses_count")
    private Integer usesCount;

    @Column(name = "valid_from")
    private ZonedDateTime validFrom;

    @Column(name = "valid_to")
    private ZonedDateTime validTo;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Size(max = 255)
    @Column(name = "tenant_id", length = 255, nullable = false)
    private String tenantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DiscountCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public DiscountCode code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public DiscountCode description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public DiscountCode discountType(String discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public DiscountCode discountValue(BigDecimal discountValue) {
        this.setDiscountValue(discountValue);
        return this;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Integer getMaxUses() {
        return this.maxUses;
    }

    public DiscountCode maxUses(Integer maxUses) {
        this.setMaxUses(maxUses);
        return this;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getUsesCount() {
        return this.usesCount;
    }

    public DiscountCode usesCount(Integer usesCount) {
        this.setUsesCount(usesCount);
        return this;
    }

    public void setUsesCount(Integer usesCount) {
        this.usesCount = usesCount;
    }

    public ZonedDateTime getValidFrom() {
        return this.validFrom;
    }

    public DiscountCode validFrom(ZonedDateTime validFrom) {
        this.setValidFrom(validFrom);
        return this;
    }

    public void setValidFrom(ZonedDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public ZonedDateTime getValidTo() {
        return this.validTo;
    }

    public DiscountCode validTo(ZonedDateTime validTo) {
        this.setValidTo(validTo);
        return this;
    }

    public void setValidTo(ZonedDateTime validTo) {
        this.validTo = validTo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public DiscountCode isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public DiscountCode createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public DiscountCode updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public DiscountCode eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public DiscountCode tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscountCode)) {
            return false;
        }
        return getId() != null && getId().equals(((DiscountCode) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiscountCode{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", discountType='" + getDiscountType() + "'" +
            ", discountValue=" + getDiscountValue() +
            ", maxUses=" + getMaxUses() +
            ", usesCount=" + getUsesCount() +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", eventId=" + getEventId() +
            ", tenantId='" + getTenantId() + "'" +
            "}";
    }
}
