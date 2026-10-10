package com.eventsitemanager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Body for {@code POST /api/tenant-onboarding-requests/{id}/reject}.
 */
public class TenantOnboardingRejectDTO implements Serializable {

    @NotBlank
    @Size(max = 4000)
    private String adminComments;

    @Size(max = 255)
    private String reviewedByClerkUserId;

    @Size(max = 255)
    private String reviewedByEmail;

    private Boolean notifySubmitter;

    public String getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public String getReviewedByClerkUserId() {
        return reviewedByClerkUserId;
    }

    public void setReviewedByClerkUserId(String reviewedByClerkUserId) {
        this.reviewedByClerkUserId = reviewedByClerkUserId;
    }

    public String getReviewedByEmail() {
        return reviewedByEmail;
    }

    public void setReviewedByEmail(String reviewedByEmail) {
        this.reviewedByEmail = reviewedByEmail;
    }

    public Boolean getNotifySubmitter() {
        return notifySubmitter;
    }

    public void setNotifySubmitter(Boolean notifySubmitter) {
        this.notifySubmitter = notifySubmitter;
    }
}
