package com.nextjstemplate.service;

import com.nextjstemplate.config.AwsProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
public class EmailSenderService {

    private final SesClient sesClient;
    private final String fromAddress;

    @Autowired
    public EmailSenderService(AwsProperties awsProperties, @Value("${jhipster.mail.from}") String fromAddress) {
        this.sesClient =
            SesClient
                .builder()
                .region(Region.of(awsProperties.getS3().getRegion()))
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsProperties.getS3().getAccessKey(), awsProperties.getS3().getSecretKey())
                    )
                )
                .build();
        this.fromAddress = fromAddress;
    }

    public void sendEmail(String to, String subject, String body, boolean isHtml, Map<String, String> headers) {
        try {
            Body emailBody;
            if (isHtml) {
                emailBody = Body.builder().html(Content.builder().data(body).build()).build();
            } else {
                emailBody = Body.builder().text(Content.builder().data(body).build()).build();
            }
            SendEmailRequest.Builder emailRequestBuilder = SendEmailRequest
                .builder()
                .destination(Destination.builder().toAddresses(to).build())
                .message(Message.builder().subject(Content.builder().data(subject).build()).body(emailBody).build())
                .source(fromAddress);
            if (headers != null && !headers.isEmpty()) {
                emailRequestBuilder =
                    emailRequestBuilder.overrideConfiguration(cfg -> {
                        headers.forEach(cfg::putHeader);
                    });
            }
            SendEmailRequest emailRequest = emailRequestBuilder.build();
            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            // Optionally log response.messageId()
        } catch (SesException e) {
            // Log error
            throw new RuntimeException("Failed to send email via SES", e);
        }
    }

    // For backward compatibility
    public void sendEmail(String to, String subject, String body, boolean isHtml) {
        sendEmail(to, subject, body, isHtml, new HashMap<>());
    }

    public void sendEmail(String to, String subject, String body) {
        sendEmail(to, subject, body, false, new HashMap<>());
    }

    // Helper to build List-Unsubscribe header value
    public static String buildListUnsubscribeHeader(String email, String link) {
        return String.format("<mailto:unsubscribe@yourdomain.com>, <%s>", link);
    }

    // Batch email sending for better performance
    public void sendBatchEmails(List<String> toAddresses, String subject, String body, boolean isHtml, Map<String, String> headers) {
        try {
            Body emailBody;
            if (isHtml) {
                emailBody = Body.builder().html(Content.builder().data(body).build()).build();
            } else {
                emailBody = Body.builder().text(Content.builder().data(body).build()).build();
            }

            // Send emails in batches of 50 (SES recommended batch size)
            int batchSize = 50;
            for (int i = 0; i < toAddresses.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, toAddresses.size());
                List<String> batch = toAddresses.subList(i, endIndex);

                SendEmailRequest.Builder emailRequestBuilder = SendEmailRequest
                    .builder()
                    .destination(Destination.builder().toAddresses(batch).build())
                    .message(Message.builder().subject(Content.builder().data(subject).build()).body(emailBody).build())
                    .source(fromAddress);

                if (headers != null && !headers.isEmpty()) {
                    emailRequestBuilder =
                        emailRequestBuilder.overrideConfiguration(cfg -> {
                            headers.forEach(cfg::putHeader);
                        });
                }

                SendEmailRequest emailRequest = emailRequestBuilder.build();
                SendEmailResponse response = sesClient.sendEmail(emailRequest);

                // Log batch success
                System.out.println("Sent batch " + (i / batchSize + 1) + " to " + batch.size() + " recipients");
            }
        } catch (SesException e) {
            throw new RuntimeException("Failed to send batch emails via SES", e);
        }
    }
}
