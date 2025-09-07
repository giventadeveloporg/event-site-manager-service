package com.nextjstemplate.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppSenderService {

    private final String accountSid;
    private final String authToken;
    private final String whatsappFrom;

    public WhatsAppSenderService(
        @Value("${twilio.account-sid}") String accountSid,
        @Value("${twilio.auth-token}") String authToken,
        @Value("${twilio.whatsapp-from}") String whatsappFrom
    ) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.whatsappFrom = whatsappFrom;
        Twilio.init(accountSid, authToken);
    }

    public String sendMessage(String to, String messageBody) {
        try {
            Message message = Message.creator(new PhoneNumber("whatsapp:" + to), new PhoneNumber(whatsappFrom), messageBody).create();
            return message.getSid();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send WhatsApp message", e);
        }
    }

    public String sendMessageWithImage(String to, String messageBody, String imageUrl) {
        try {
            Message message = Message
                .creator(new PhoneNumber("whatsapp:" + to), new PhoneNumber(whatsappFrom), messageBody)
                .setMediaUrl(java.util.List.of(new java.net.URI(imageUrl)))
                .create();
            return message.getSid();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send WhatsApp message with image", e);
        }
    }
}
