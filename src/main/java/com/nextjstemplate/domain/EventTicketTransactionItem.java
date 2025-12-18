package com.nextjstemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A EventTicketTransactionItem.
 */
@Entity
@Table(name = "event_ticket_transaction_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventTicketTransactionItem implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(EventTicketTransactionItem.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "public.sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "tenant_id", length = 255)
    private String tenantId;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @NotNull
    @Column(name = "ticket_type_id", nullable = false)
    private Long ticketTypeId;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price_per_unit", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerUnit;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    /**
     * PrePersist callback to ensure tenantId is set before persisting.
     * This is a safety net - tenantId should already be set from the DTO in the service layer.
     * If tenantId is null here, it indicates the DTO didn't have tenantId, which is an error.
     */
    @PrePersist
    protected void onPrePersist() {
        if (tenantId == null || tenantId.isEmpty()) {
            log.error(
                "CRITICAL: EventTicketTransactionItem entity is being persisted with NULL or EMPTY tenantId! " +
                "transactionId={}, ticketTypeId={}, quantity={}. " +
                "This indicates tenantId was not set from the DTO in the service layer.",
                transactionId,
                ticketTypeId,
                quantity
            );
            // DO NOT set a default tenantId here - this is an error condition that should fail
            // The service layer should have already validated and set tenantId from the DTO
        } else {
            log.debug("EventTicketTransactionItem PrePersist: tenantId='{}', transactionId={}", tenantId, transactionId);
        }
    }

    /*@ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "event", "user" }, allowSetters = true)
    private EventTicketTransaction transaction;*/

    /* @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private EventTicketType ticketType;*/

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventTicketTransactionItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public EventTicketTransactionItem tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public EventTicketTransactionItem transactionId(Long transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTicketTypeId() {
        return this.ticketTypeId;
    }

    public EventTicketTransactionItem ticketTypeId(Long ticketTypeId) {
        this.setTicketTypeId(ticketTypeId);
        return this;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public EventTicketTransactionItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return this.pricePerUnit;
    }

    public EventTicketTransactionItem pricePerUnit(BigDecimal pricePerUnit) {
        this.setPricePerUnit(pricePerUnit);
        return this;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public EventTicketTransactionItem totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public EventTicketTransactionItem createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventTicketTransactionItem updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /* public EventTicketTransaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(EventTicketTransaction eventTicketTransaction) {
        this.transaction = eventTicketTransaction;
    }*/

    /* public EventTicketTransactionItem transaction(EventTicketTransaction eventTicketTransaction) {
        this.setTransaction(eventTicketTransaction);
        return this;
    }

    public EventTicketType getTicketType() {
        return this.ticketType;
    }

    public void setTicketType(EventTicketType eventTicketType) {
        this.ticketType = eventTicketType;
    }*/

    /* public EventTicketTransactionItem ticketType(EventTicketType eventTicketType) {
        this.setTicketType(eventTicketType);
        return this;
    }*/

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTicketTransactionItem)) {
            return false;
        }
        return getId() != null && getId().equals(((EventTicketTransactionItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventTicketTransactionItem{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", transactionId=" + getTransactionId() +
            ", ticketTypeId=" + getTicketTypeId() +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", totalAmount=" + getTotalAmount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
