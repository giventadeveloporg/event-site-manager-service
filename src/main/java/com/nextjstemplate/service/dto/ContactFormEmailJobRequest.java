package com.nextjstemplate.service.dto;

import java.io.Serializable;

/**
 * DTO for submitting a contact form email job to the batch jobs microservice.
 * This should mirror the ContactFormEmailJobRequest structure expected by the
 * Event Site Manager Batch Jobs project.
 */
public class ContactFormEmailJobRequest implements Serializable {

    private String tenantId;
    private String firstName;
    private String lastName;
    private String messageBody;
    private String fromEmail;
    private String toEmail;
    private Long submittedAtEpochMillis;
    private Long userId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
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
            "ContactFormEmailJobRequest{" +
            "tenantId='" +
            tenantId +
            '\'' +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", messageBody='" +
            (messageBody != null ? "[length=" + messageBody.length() + "]" : null) +
            '\'' +
            ", fromEmail='" +
            fromEmail +
            '\'' +
            ", toEmail='" +
            toEmail +
            '\'' +
            ", submittedAtEpochMillis=" +
            submittedAtEpochMillis +
            ", userId=" +
            userId +
            '}'
        );
    }
}
