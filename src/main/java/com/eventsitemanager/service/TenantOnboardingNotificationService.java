package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.TenantOnboardingRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * Best-effort emails for the onboarding workflow. Every method swallows failures: an email outage must never
 * fail a submit, approve or reject that has already been committed.
 */
@Service
public class TenantOnboardingNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantOnboardingNotificationService.class);

    private final EmailSenderService emailSenderService;

    @Value("${onboarding.notify-email:}")
    private String platformNotifyEmail;

    public TenantOnboardingNotificationService(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    public void onSubmitted(TenantOnboardingRequestDTO r) {
        if (platformNotifyEmail != null && !platformNotifyEmail.isBlank()) {
            send(
                platformNotifyEmail.trim(),
                "New onboarding request " + r.getRequestCode() + ": " + r.getOrganizationName(),
                paragraph("A new customer onboarding request is waiting for review.") +
                details(r) +
                paragraph("Review it in the admin hub under Admin &rarr; Onboarding Requests.")
            );
        }
        send(
            r.getContactEmail(),
            "We received your request (" + r.getRequestCode() + ")",
            paragraph("Hello,") +
            paragraph(
                "Thank you for requesting a site for <strong>" +
                esc(r.getOrganizationName()) +
                "</strong>. Your request code is <strong>" +
                esc(r.getRequestCode()) +
                "</strong>."
            ) +
            paragraph("Our team will review it and contact you about domain setup and next steps.")
        );
    }

    public void onApproved(TenantOnboardingRequestDTO r) {
        send(
            r.getContactEmail(),
            "Your site request " + r.getRequestCode() + " was approved",
            paragraph("Hello,") +
            paragraph(
                "Your site for <strong>" +
                esc(r.getOrganizationName()) +
                "</strong> has been set up for <strong>" +
                esc(r.getRequestedHostname()) +
                "</strong>."
            ) +
            paragraph("We will contact you to finish the domain (DNS) configuration before the site goes live.") +
            commentsBlock(r)
        );
    }

    public void onRejected(TenantOnboardingRequestDTO r) {
        send(
            r.getContactEmail(),
            "Update on your site request " + r.getRequestCode(),
            paragraph("Hello,") +
            paragraph(
                "We are unable to proceed with your request for <strong>" + esc(r.getOrganizationName()) + "</strong> at this time."
            ) +
            commentsBlock(r)
        );
    }

    private void send(String to, String subject, String htmlBody) {
        if (to == null || to.isBlank()) {
            return;
        }
        try {
            emailSenderService.sendEmail(to.trim(), subject, htmlBody, true);
        } catch (RuntimeException e) {
            LOG.warn("[ONBOARDING] Notification email to {} failed: {}", to, e.getMessage());
        }
    }

    private static String details(TenantOnboardingRequestDTO r) {
        return (
            "<ul>" +
            item("Request code", r.getRequestCode()) +
            item("Organization", r.getOrganizationName()) +
            item("Site type", r.getSiteType() != null ? r.getSiteType().name() : null) +
            item("Requested hostname", r.getRequestedHostname()) +
            item("Contact", joinName(r.getContactFirstName(), r.getContactLastName())) +
            item("Contact email", r.getContactEmail()) +
            item("Contact phone", r.getContactPhone()) +
            item("Notes", r.getCustomerNotes()) +
            "</ul>"
        );
    }

    private static String commentsBlock(TenantOnboardingRequestDTO r) {
        String c = r.getAdminComments();
        return c == null || c.isBlank() ? "" : paragraph("Notes from our team: " + esc(c));
    }

    private static String item(String label, String value) {
        return value == null || value.isBlank() ? "" : "<li><strong>" + label + ":</strong> " + esc(value) + "</li>";
    }

    private static String joinName(String first, String last) {
        String joined = ((first == null ? "" : first.trim()) + " " + (last == null ? "" : last.trim())).trim();
        return joined.isEmpty() ? null : joined;
    }

    private static String paragraph(String html) {
        return "<p>" + html + "</p>";
    }

    private static String esc(String s) {
        return s == null ? "" : HtmlUtils.htmlEscape(s);
    }
}
