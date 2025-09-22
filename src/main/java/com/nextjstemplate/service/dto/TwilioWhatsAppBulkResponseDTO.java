package com.nextjstemplate.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * DTO for Twilio WhatsApp bulk message responses.
 */
public class TwilioWhatsAppBulkResponseDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private int totalSent;
  private int totalFailed;
  private List<TwilioWhatsAppResponseDTO> results;
  private String campaignId;
  private ZonedDateTime sentAt;
  private String status;

  public TwilioWhatsAppBulkResponseDTO() {
  }

  public TwilioWhatsAppBulkResponseDTO(int totalSent, int totalFailed, List<TwilioWhatsAppResponseDTO> results) {
    this.totalSent = totalSent;
    this.totalFailed = totalFailed;
    this.results = results;
    this.sentAt = ZonedDateTime.now();
    this.status = totalFailed > 0 ? "PARTIAL_SUCCESS" : "SUCCESS";
  }

  public int getTotalSent() {
    return totalSent;
  }

  public void setTotalSent(int totalSent) {
    this.totalSent = totalSent;
  }

  public int getTotalFailed() {
    return totalFailed;
  }

  public void setTotalFailed(int totalFailed) {
    this.totalFailed = totalFailed;
  }

  public List<TwilioWhatsAppResponseDTO> getResults() {
    return results;
  }

  public void setResults(List<TwilioWhatsAppResponseDTO> results) {
    this.results = results;
  }

  public String getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(String campaignId) {
    this.campaignId = campaignId;
  }

  public ZonedDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(ZonedDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public boolean isSuccess() {
    return "SUCCESS".equals(status);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TwilioWhatsAppBulkResponseDTO that = (TwilioWhatsAppBulkResponseDTO) o;
    return totalSent == that.totalSent &&
        totalFailed == that.totalFailed &&
        Objects.equals(results, that.results) &&
        Objects.equals(campaignId, that.campaignId) &&
        Objects.equals(sentAt, that.sentAt) &&
        Objects.equals(status, that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalSent, totalFailed, results, campaignId, sentAt, status);
  }

  @Override
  public String toString() {
    return "TwilioWhatsAppBulkResponseDTO{" +
        "totalSent=" + totalSent +
        ", totalFailed=" + totalFailed +
        ", results=" + results +
        ", campaignId='" + campaignId + '\'' +
        ", sentAt=" + sentAt +
        ", status='" + status + '\'' +
        '}';
  }
}

