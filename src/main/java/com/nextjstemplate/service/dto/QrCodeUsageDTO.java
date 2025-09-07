package com.nextjstemplate.service.dto;

import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.service.dto.EventTicketTypeDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.nextjstemplate.domain.QrCodeUsage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QrCodeUsageDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String tenantId;

    @NotNull
    @Size(max = 1000)
    private String qrCodeData;

    @NotNull
    private ZonedDateTime generatedAt;

    private ZonedDateTime usedAt;

    private Integer usageCount;

    @Size(max = 255)
    private String lastScannedBy;

    @NotNull
    private ZonedDateTime createdAt;

    private EventAttendeeDTO attendee;

    private EventTicketTransactionDTO transaction;
    private List<EventTicketTransactionItemDTO> items;

    private EventDetailsDTO eventDetails;

    private List<EventTicketTypeDTO> eventTicketTypes;

    public QrCodeUsageDTO() {}

    public QrCodeUsageDTO(
        EventTicketTransactionDTO transaction,
        List<EventTicketTransactionItemDTO> items,
        EventDetailsDTO eventDetails,
        List<EventTicketTypeDTO> eventTicketTypes
    ) {
        this.transaction = transaction;
        this.items = items;
        this.eventDetails = eventDetails;
        this.eventTicketTypes = eventTicketTypes;
    }

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

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public ZonedDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(ZonedDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ZonedDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(ZonedDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getLastScannedBy() {
        return lastScannedBy;
    }

    public void setLastScannedBy(String lastScannedBy) {
        this.lastScannedBy = lastScannedBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EventAttendeeDTO getAttendee() {
        return attendee;
    }

    public void setAttendee(EventAttendeeDTO attendee) {
        this.attendee = attendee;
    }

    public EventTicketTransactionDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(EventTicketTransactionDTO transaction) {
        this.transaction = transaction;
    }

    public List<EventTicketTransactionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<EventTicketTransactionItemDTO> items) {
        this.items = items;
    }

    public EventDetailsDTO getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetailsDTO eventDetails) {
        this.eventDetails = eventDetails;
    }

    public List<EventTicketTypeDTO> getEventTicketTypes() {
        return eventTicketTypes;
    }

    public void setEventTicketTypes(List<EventTicketTypeDTO> eventTicketTypes) {
        this.eventTicketTypes = eventTicketTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QrCodeUsageDTO)) {
            return false;
        }

        QrCodeUsageDTO qrCodeUsageDTO = (QrCodeUsageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, qrCodeUsageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QrCodeUsageDTO{" +
                "id=" + getId() +
                ", tenantId='" + getTenantId() + "'" +
                ", qrCodeData='" + getQrCodeData() + "'" +
                ", generatedAt='" + getGeneratedAt() + "'" +
                ", usedAt='" + getUsedAt() + "'" +
                ", usageCount=" + getUsageCount() +
                ", lastScannedBy='" + getLastScannedBy() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", attendee=" + getAttendee() +
                ", transaction=" + getTransaction() +
                ", items=" + getItems() +
                ", eventDetails=" + getEventDetails() +
                ", eventTicketTypes=" + getEventTicketTypes() +
                "}";
    }
}
