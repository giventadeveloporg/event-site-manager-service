package com.nextjstemplate.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO representing a promotion test email job request for the batch jobs microservice.
 */
public class PromotionTestEmailJobRequest {

    @JsonProperty("tenantId")
    @NotBlank
    @Size(max = 255)
    private String tenantId;

    @JsonProperty("templateId")
    @NotNull
    private Long templateId;

    @JsonProperty("recipientEmail")
    @NotBlank
    @Email
    @Size(max = 255)
    private String recipientEmail;

    @JsonProperty("submittedAtEpochMillis")
    private Long submittedAtEpochMillis;

    @JsonProperty("userId")
    private Long userId;

    public PromotionTestEmailJobRequest() {}

    public PromotionTestEmailJobRequest(String tenantId, Long templateId, String recipientEmail, Long submittedAtEpochMillis, Long userId) {
        this.tenantId = tenantId;
        this.templateId = templateId;
        this.recipientEmail = recipientEmail;
        this.submittedAtEpochMillis = submittedAtEpochMillis;
        this.userId = userId;
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

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public Long getSubmittedAtEpochMillis() {
        return submittedAtEpochMillis;
    }

    public void setSubmittedAtEpochMillis(Long submittedAtEpochMillis) {
        this.submittedAtEpochMillis = submittedAtEpochMillis;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return (
            "PromotionTestEmailJobRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", templateId=" +
            templateId +
            ", recipientEmail='" +
            recipientEmail +
            '\'' +
            ", submittedAtEpochMillis=" +
            submittedAtEpochMillis +
            ", userId=" +
            userId +
            '}'
        );
    }
}
