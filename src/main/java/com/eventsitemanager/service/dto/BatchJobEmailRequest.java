package com.eventsitemanager.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for batch job email request.
 */
public class BatchJobEmailRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;
    private Long templateId;
    private Integer batchSize;
    private Integer maxEmails;
    private List<String> recipientEmails;
    private Long userId;
    private String recipientType; // "EVENT_ATTENDEES" or "SUBSCRIBED_MEMBERS"

    public BatchJobEmailRequest() {}

    public BatchJobEmailRequest(
        String tenantId,
        Long templateId,
        Integer batchSize,
        Integer maxEmails,
        List<String> recipientEmails,
        Long userId,
        String recipientType
    ) {
        this.tenantId = tenantId;
        this.templateId = templateId;
        this.batchSize = batchSize;
        this.maxEmails = maxEmails;
        this.recipientEmails = recipientEmails;
        this.userId = userId;
        this.recipientType = recipientType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getMaxEmails() {
        return maxEmails;
    }

    public void setMaxEmails(Integer maxEmails) {
        this.maxEmails = maxEmails;
    }

    public List<String> getRecipientEmails() {
        return recipientEmails;
    }

    public void setRecipientEmails(List<String> recipientEmails) {
        this.recipientEmails = recipientEmails;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    @Override
    public String toString() {
        return (
            "BatchJobEmailRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", templateId=" +
            templateId +
            ", batchSize=" +
            batchSize +
            ", maxEmails=" +
            maxEmails +
            ", recipientEmails=" +
            (recipientEmails != null ? recipientEmails.size() + " emails" : "null") +
            ", userId=" +
            userId +
            ", recipientType='" +
            recipientType +
            '\'' +
            '}'
        );
    }
}
