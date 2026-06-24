package com.eventsitemanager.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

/**
 * Task 9: Check-in analytics aggregation DTO.
 */
public class CheckInAnalyticsDTO implements Serializable {

    private Long eventId;
    private int totalTickets;
    private int checkedInCount;
    private int notCheckedInCount;
    private int noShowCount;
    private double checkInPercentage;
    private Map<String, Integer> checkInsByHour;
    private Map<String, Integer> checkInsByTicketType;
    private String peakCheckInTime;
    private String averageCheckInTime;

    // Date range support
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getCheckedInCount() {
        return checkedInCount;
    }

    public void setCheckedInCount(int checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

    public int getNotCheckedInCount() {
        return notCheckedInCount;
    }

    public void setNotCheckedInCount(int notCheckedInCount) {
        this.notCheckedInCount = notCheckedInCount;
    }

    public int getNoShowCount() {
        return noShowCount;
    }

    public void setNoShowCount(int noShowCount) {
        this.noShowCount = noShowCount;
    }

    public double getCheckInPercentage() {
        return checkInPercentage;
    }

    public void setCheckInPercentage(double checkInPercentage) {
        this.checkInPercentage = checkInPercentage;
    }

    public Map<String, Integer> getCheckInsByHour() {
        return checkInsByHour;
    }

    public void setCheckInsByHour(Map<String, Integer> checkInsByHour) {
        this.checkInsByHour = checkInsByHour;
    }

    public Map<String, Integer> getCheckInsByTicketType() {
        return checkInsByTicketType;
    }

    public void setCheckInsByTicketType(Map<String, Integer> checkInsByTicketType) {
        this.checkInsByTicketType = checkInsByTicketType;
    }

    public String getPeakCheckInTime() {
        return peakCheckInTime;
    }

    public void setPeakCheckInTime(String peakCheckInTime) {
        this.peakCheckInTime = peakCheckInTime;
    }

    public String getAverageCheckInTime() {
        return averageCheckInTime;
    }

    public void setAverageCheckInTime(String averageCheckInTime) {
        this.averageCheckInTime = averageCheckInTime;
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
}
