package com.eventsitemanager.service;

/**
 * Interface for sending ticket emails.
 * Abstracts the email sending to avoid service-layer dependency on web-layer resources.
 */
public interface TicketEmailSender {
    void sendTicketEmail(Long eventId, Long transactionId, String to, String emailHostUrlPrefix);
}
