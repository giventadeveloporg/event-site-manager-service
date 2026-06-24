package com.eventsitemanager.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Task 10: Sales analytics aggregation DTO.
 */
public class SalesAnalyticsDTO implements Serializable {

    private Long eventId;
    private int totalSales;
    private BigDecimal totalRevenue;
    private BigDecimal netRevenue;
    private BigDecimal netRevenueBeforeTax;
    private BigDecimal totalDiscounts;
    private BigDecimal totalRefunds;
    private BigDecimal averageTicketPrice;
    private List<SalesByDateDTO> salesByDate;
    private Map<String, SalesByTicketTypeDTO> salesByTicketType;
    private Map<String, SalesByPaymentMethodDTO> salesByPaymentMethod;

    // Date range support
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(BigDecimal netRevenue) {
        this.netRevenue = netRevenue;
    }

    public BigDecimal getNetRevenueBeforeTax() {
        return netRevenueBeforeTax;
    }

    public void setNetRevenueBeforeTax(BigDecimal netRevenueBeforeTax) {
        this.netRevenueBeforeTax = netRevenueBeforeTax;
    }

    public BigDecimal getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(BigDecimal totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public BigDecimal getTotalRefunds() {
        return totalRefunds;
    }

    public void setTotalRefunds(BigDecimal totalRefunds) {
        this.totalRefunds = totalRefunds;
    }

    public BigDecimal getAverageTicketPrice() {
        return averageTicketPrice;
    }

    public void setAverageTicketPrice(BigDecimal averageTicketPrice) {
        this.averageTicketPrice = averageTicketPrice;
    }

    public List<SalesByDateDTO> getSalesByDate() {
        return salesByDate;
    }

    public void setSalesByDate(List<SalesByDateDTO> salesByDate) {
        this.salesByDate = salesByDate;
    }

    public Map<String, SalesByTicketTypeDTO> getSalesByTicketType() {
        return salesByTicketType;
    }

    public void setSalesByTicketType(Map<String, SalesByTicketTypeDTO> salesByTicketType) {
        this.salesByTicketType = salesByTicketType;
    }

    public Map<String, SalesByPaymentMethodDTO> getSalesByPaymentMethod() {
        return salesByPaymentMethod;
    }

    public void setSalesByPaymentMethod(Map<String, SalesByPaymentMethodDTO> salesByPaymentMethod) {
        this.salesByPaymentMethod = salesByPaymentMethod;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Nested DTOs for aggregated data
    public static class SalesByDateDTO implements Serializable {

        private LocalDate date;
        private int count;
        private BigDecimal revenue;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    public static class SalesByTicketTypeDTO implements Serializable {

        private int count;
        private BigDecimal revenue;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    public static class SalesByPaymentMethodDTO implements Serializable {

        private int count;
        private BigDecimal revenue;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }
}
