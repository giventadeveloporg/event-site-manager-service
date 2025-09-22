package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * DTO for WhatsApp analytics data.
 */
public class WhatsAppAnalyticsDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String tenantId;
  private ZonedDateTime fromDate;
  private ZonedDateTime toDate;
  private long totalMessages;
  private long sentMessages;
  private long deliveredMessages;
  private long failedMessages;
  private long readMessages;
  private double deliveryRate;
  private double readRate;
  private long totalRecipients;
  private long uniqueRecipients;

  public WhatsAppAnalyticsDTO() {
  }

  public WhatsAppAnalyticsDTO(String tenantId, ZonedDateTime fromDate, ZonedDateTime toDate) {
    this.tenantId = tenantId;
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ZonedDateTime getFromDate() {
    return fromDate;
  }

  public void setFromDate(ZonedDateTime fromDate) {
    this.fromDate = fromDate;
  }

  public ZonedDateTime getToDate() {
    return toDate;
  }

  public void setToDate(ZonedDateTime toDate) {
    this.toDate = toDate;
  }

  public long getTotalMessages() {
    return totalMessages;
  }

  public void setTotalMessages(long totalMessages) {
    this.totalMessages = totalMessages;
  }

  public long getSentMessages() {
    return sentMessages;
  }

  public void setSentMessages(long sentMessages) {
    this.sentMessages = sentMessages;
  }

  public long getDeliveredMessages() {
    return deliveredMessages;
  }

  public void setDeliveredMessages(long deliveredMessages) {
    this.deliveredMessages = deliveredMessages;
  }

  public long getFailedMessages() {
    return failedMessages;
  }

  public void setFailedMessages(long failedMessages) {
    this.failedMessages = failedMessages;
  }

  public long getReadMessages() {
    return readMessages;
  }

  public void setReadMessages(long readMessages) {
    this.readMessages = readMessages;
  }

  public double getDeliveryRate() {
    return deliveryRate;
  }

  public void setDeliveryRate(double deliveryRate) {
    this.deliveryRate = deliveryRate;
  }

  public double getReadRate() {
    return readRate;
  }

  public void setReadRate(double readRate) {
    this.readRate = readRate;
  }

  public long getTotalRecipients() {
    return totalRecipients;
  }

  public void setTotalRecipients(long totalRecipients) {
    this.totalRecipients = totalRecipients;
  }

  public long getUniqueRecipients() {
    return uniqueRecipients;
  }

  public void setUniqueRecipients(long uniqueRecipients) {
    this.uniqueRecipients = uniqueRecipients;
  }

  public void calculateRates() {
    if (totalMessages > 0) {
      this.deliveryRate = (double) deliveredMessages / totalMessages * 100;
      this.readRate = (double) readMessages / totalMessages * 100;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WhatsAppAnalyticsDTO that = (WhatsAppAnalyticsDTO) o;
    return totalMessages == that.totalMessages &&
        sentMessages == that.sentMessages &&
        deliveredMessages == that.deliveredMessages &&
        failedMessages == that.failedMessages &&
        readMessages == that.readMessages &&
        Double.compare(that.deliveryRate, deliveryRate) == 0 &&
        Double.compare(that.readRate, readRate) == 0 &&
        totalRecipients == that.totalRecipients &&
        uniqueRecipients == that.uniqueRecipients &&
        Objects.equals(tenantId, that.tenantId) &&
        Objects.equals(fromDate, that.fromDate) &&
        Objects.equals(toDate, that.toDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, fromDate, toDate, totalMessages, sentMessages, deliveredMessages, failedMessages,
        readMessages, deliveryRate, readRate, totalRecipients, uniqueRecipients);
  }

  @Override
  public String toString() {
    return "WhatsAppAnalyticsDTO{" +
        "tenantId='" + tenantId + '\'' +
        ", fromDate=" + fromDate +
        ", toDate=" + toDate +
        ", totalMessages=" + totalMessages +
        ", sentMessages=" + sentMessages +
        ", deliveredMessages=" + deliveredMessages +
        ", failedMessages=" + failedMessages +
        ", readMessages=" + readMessages +
        ", deliveryRate=" + deliveryRate +
        ", readRate=" + readRate +
        ", totalRecipients=" + totalRecipients +
        ", uniqueRecipients=" + uniqueRecipients +
        '}';
  }
}
