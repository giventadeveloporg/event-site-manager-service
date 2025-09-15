package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class EventTicketTransactionStatisticsDTO implements Serializable {

    private Long eventId;
    private int totalTicketsSold;
    private BigDecimal totalAmount;
    private BigDecimal netAmount;
    private Map<String, Integer> ticketsByStatus;
    private Map<String, BigDecimal> amountByStatus;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public Map<String, Integer> getTicketsByStatus() {
        return ticketsByStatus;
    }

    public void setTicketsByStatus(Map<String, Integer> ticketsByStatus) {
        this.ticketsByStatus = ticketsByStatus;
    }

    public Map<String, BigDecimal> getAmountByStatus() {
        return amountByStatus;
    }

    public void setAmountByStatus(Map<String, BigDecimal> amountByStatus) {
        this.amountByStatus = amountByStatus;
    }
}
