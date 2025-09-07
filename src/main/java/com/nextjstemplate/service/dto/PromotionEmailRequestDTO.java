package com.nextjstemplate.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PromotionEmailRequestDTO {

    @NotBlank
    private String tenantId;

    @Email
    private String to;

    private boolean isTestEmail;

    private String subject;

    private String promoCode;

    private String emailHostUrlPrefix;

    @NotBlank
    private String bodyHtml; // HTML content

    private String headerImagePath; // S3 path with {tenantId} placeholder
    private String footerPath; // S3 path with {tenantId} placeholder

    // Getters and setters
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getHeaderImagePath() {
        return headerImagePath;
    }

    public void setHeaderImagePath(String headerImagePath) {
        this.headerImagePath = headerImagePath;
    }

    public String getFooterPath() {
        return footerPath;
    }

    public void setFooterPath(String footerPath) {
        this.footerPath = footerPath;
    }

    public boolean isTestEmail() {
        return isTestEmail;
    }

    public void setTestEmail(boolean testEmail) {
        isTestEmail = testEmail;
    }

    public String getEmailHostUrlPrefix() {
        return emailHostUrlPrefix;
    }

    public void setEmailHostUrlPrefix(String emailHostUrlPrefix) {
        this.emailHostUrlPrefix = emailHostUrlPrefix;
    }
}
