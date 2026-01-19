package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for cart item in manual payment request.
 */
public class CartItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long ticketTypeId;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    public CartItemDTO() {}

    public CartItemDTO(Long ticketTypeId, Integer quantity) {
        this.ticketTypeId = ticketTypeId;
        this.quantity = quantity;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItemDTO)) {
            return false;
        }
        CartItemDTO that = (CartItemDTO) o;
        return Objects.equals(ticketTypeId, that.ticketTypeId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketTypeId, quantity);
    }

    @Override
    public String toString() {
        return "CartItemDTO{" + "ticketTypeId=" + ticketTypeId + ", quantity=" + quantity + '}';
    }
}
