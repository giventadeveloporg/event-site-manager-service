package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for sending promotion emails.
 * Request DTO for test and bulk email sending.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SendPromotionEmailDTO implements Serializable {

    @NotNull
    private Long templateId;

    /**
     * Optional recipient email for test emails.
     * If not provided for test email, uses admin email.
     */
    @Size(max = 255)
    private String recipientEmail;

    /**
     * Indicates if this is a test email (sent to admin/recipientEmail)
     * or bulk email (sent to event registrants).
     */
    @NotNull
    private Boolean isTestEmail;

    /**
     * Optional subject override for the email.
     * If not provided, uses template subject.
     */
    @Size(max = 500)
    private String subjectOverride;

    /**
     * Optional body HTML override for the email.
     * If not provided, uses template bodyHtml.
     */
    private String bodyHtmlOverride;

    /**
     * Optional list of recipient emails for bulk sending.
     * If not provided, retrieves from event registrants.
     */
    private List<String> recipientEmails;

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

    public Boolean getIsTestEmail() {
        return isTestEmail;
    }

    public void setIsTestEmail(Boolean isTestEmail) {
        this.isTestEmail = isTestEmail;
    }

    public String getSubjectOverride() {
        return subjectOverride;
    }

    public void setSubjectOverride(String subjectOverride) {
        this.subjectOverride = subjectOverride;
    }

    public String getBodyHtmlOverride() {
        return bodyHtmlOverride;
    }

    public void setBodyHtmlOverride(String bodyHtmlOverride) {
        this.bodyHtmlOverride = bodyHtmlOverride;
    }

    public List<String> getRecipientEmails() {
        return recipientEmails;
    }

    public void setRecipientEmails(List<String> recipientEmails) {
        this.recipientEmails = recipientEmails != null ? recipientEmails : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendPromotionEmailDTO)) {
            return false;
        }

        SendPromotionEmailDTO sendPromotionEmailDTO = (SendPromotionEmailDTO) o;
        return Objects.equals(this.templateId, sendPromotionEmailDTO.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.templateId);
    }

    @Override
    public String toString() {
        return (
            "SendPromotionEmailDTO{" +
            "templateId=" +
            getTemplateId() +
            ", recipientEmail='" +
            getRecipientEmail() +
            "'" +
            ", isTestEmail='" +
            getIsTestEmail() +
            "'" +
            ", subjectOverride='" +
            getSubjectOverride() +
            "'" +
            ", bodyHtmlOverride='" +
            getBodyHtmlOverride() +
            "'" +
            ", recipientEmails=" +
            getRecipientEmails() +
            "}"
        );
    }
}
