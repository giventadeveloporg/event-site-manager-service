package com.nextjstemplate.service;

import com.google.common.util.concurrent.RateLimiter;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    private final SesClient sesClient;
    private final String fromAddress;

    // AWS SES rate limiting: 200 emails/second (adjust based on your SES account limits)
    private final RateLimiter sesRateLimiter;

    // Circuit breaker for SES
    private final CircuitBreaker sesCircuitBreaker;

    // Metrics
    private final Counter emailSentCounter;
    private final Counter emailFailedCounter;
    private final Counter emailRateLimitedCounter;
    private final Counter circuitBreakerOpenCounter;
    private final Timer emailSendTimer;

    public EmailSenderService(
        @Value("${aws.s3.access-key}") String accessKey,
        @Value("${aws.s3.secret-key}") String secretKey,
        @Value("${aws.s3.region}") String region,
        @Value("${jhipster.mail.from}") String fromAddress,
        @Value("${aws.ses.rate-limit-per-second:200}") double sesRateLimitPerSecond,
        MeterRegistry meterRegistry
    ) {
        this.sesClient =
            SesClient
                .builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
        this.fromAddress = fromAddress;

        // Initialize SES rate limiter
        this.sesRateLimiter = RateLimiter.create(sesRateLimitPerSecond);
        log.info("Initialized SES rate limiter with {} emails/second", sesRateLimitPerSecond);

        // Initialize circuit breaker for SES
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
            .custom()
            .failureRateThreshold(50) // Open circuit if 50% of calls fail
            .waitDurationInOpenState(Duration.ofSeconds(30)) // Wait 30 seconds before trying again
            .slidingWindowSize(100) // Last 100 calls
            .minimumNumberOfCalls(10) // Need at least 10 calls before calculating failure rate
            .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        this.sesCircuitBreaker = circuitBreakerRegistry.circuitBreaker("sesEmailSender");
        log.info("Initialized SES circuit breaker");

        // Initialize metrics
        this.emailSentCounter =
            Counter.builder("email.sent.total").description("Total number of emails sent successfully").register(meterRegistry);
        this.emailFailedCounter =
            Counter.builder("email.failed.total").description("Total number of failed email sends").register(meterRegistry);
        this.emailRateLimitedCounter =
            Counter.builder("email.rate_limited.total").description("Total number of emails rate limited").register(meterRegistry);
        this.circuitBreakerOpenCounter =
            Counter
                .builder("email.circuit_breaker.open.total")
                .description("Total number of times circuit breaker opened")
                .register(meterRegistry);
        this.emailSendTimer = Timer.builder("email.send.duration").description("Time taken to send email").register(meterRegistry);
    }

    public void sendEmail(String to, String subject, String body, boolean isHtml, Map<String, String> headers) {
        // Check circuit breaker
        if (sesCircuitBreaker.getState() == io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN) {
            circuitBreakerOpenCounter.increment();
            log.warn("SES circuit breaker is OPEN, rejecting email send request");
            throw new RuntimeException("Email service is temporarily unavailable (circuit breaker open)");
        }

        // Apply rate limiting
        if (!sesRateLimiter.tryAcquire()) {
            emailRateLimitedCounter.increment();
            log.warn("SES rate limit exceeded, email send request rejected");
            throw new RuntimeException("Email rate limit exceeded, please try again later");
        }

        Timer.Sample sample = Timer.start();
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

            // Execute with circuit breaker protection
            SendEmailResponse response = sesCircuitBreaker.executeSupplier(() -> {
                return sesClient.sendEmail(emailRequest);
            });

            emailSentCounter.increment();
            sample.stop(emailSendTimer);
            log.debug("Email sent successfully to {}: {}", to, response.messageId());
        } catch (SesException e) {
            emailFailedCounter.increment();
            sample.stop(emailSendTimer);
            log.error("Failed to send email via SES to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email via SES: " + e.getMessage(), e);
        } catch (Exception e) {
            emailFailedCounter.increment();
            sample.stop(emailSendTimer);
            log.error("Unexpected error sending email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    // For backward compatibility
    public void sendEmail(String to, String subject, String body, boolean isHtml) {
        sendEmail(to, subject, body, isHtml, new HashMap<>());
    }

    public void sendEmail(String to, String subject, String body) {
        sendEmail(to, subject, body, false, new HashMap<>());
    }

    /**
     * Send email with custom from address.
     *
     * @param from the sender email address
     * @param to the recipient email address
     * @param subject the email subject
     * @param body the email body
     * @param isHtml whether the body is HTML
     * @param headers optional email headers
     */
    public void sendEmail(String from, String to, String subject, String body, boolean isHtml, Map<String, String> headers) {
        // Check circuit breaker
        if (sesCircuitBreaker.getState() == io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN) {
            circuitBreakerOpenCounter.increment();
            log.warn("SES circuit breaker is OPEN, rejecting email send request");
            throw new RuntimeException("Email service is temporarily unavailable (circuit breaker open)");
        }

        // Apply rate limiting
        if (!sesRateLimiter.tryAcquire()) {
            emailRateLimitedCounter.increment();
            log.warn("SES rate limit exceeded, email send request rejected");
            throw new RuntimeException("Email rate limit exceeded, please try again later");
        }

        Timer.Sample sample = Timer.start();
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
                .source(from);
            if (headers != null && !headers.isEmpty()) {
                emailRequestBuilder =
                    emailRequestBuilder.overrideConfiguration(cfg -> {
                        headers.forEach(cfg::putHeader);
                    });
            }
            SendEmailRequest emailRequest = emailRequestBuilder.build();

            // Execute with circuit breaker protection
            SendEmailResponse response = sesCircuitBreaker.executeSupplier(() -> {
                return sesClient.sendEmail(emailRequest);
            });

            emailSentCounter.increment();
            sample.stop(emailSendTimer);
            log.debug("Email sent successfully from {} to {}: {}", from, to, response.messageId());
        } catch (SesException e) {
            emailFailedCounter.increment();
            sample.stop(emailSendTimer);
            log.error("Failed to send email via SES from {} to {}: {}", from, to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email via SES: " + e.getMessage(), e);
        } catch (Exception e) {
            emailFailedCounter.increment();
            sample.stop(emailSendTimer);
            log.error("Unexpected error sending email from {} to {}: {}", from, to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    /**
     * Send batch emails with custom from address.
     *
     * @param from the sender email address
     * @param toAddresses list of recipient email addresses
     * @param subject the email subject
     * @param body the email body
     * @param isHtml whether the body is HTML
     * @param headers optional email headers
     */
    public void sendBatchEmails(
        String from,
        List<String> toAddresses,
        String subject,
        String body,
        boolean isHtml,
        Map<String, String> headers
    ) {
        // Check circuit breaker
        if (sesCircuitBreaker.getState() == io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN) {
            circuitBreakerOpenCounter.increment();
            log.warn("SES circuit breaker is OPEN, rejecting batch email send request");
            throw new RuntimeException("Email service is temporarily unavailable (circuit breaker open)");
        }

        try {
            Body emailBody;
            if (isHtml) {
                emailBody = Body.builder().html(Content.builder().data(body).build()).build();
            } else {
                emailBody = Body.builder().text(Content.builder().data(body).build()).build();
            }

            // Send emails in batches of 50 (SES recommended batch size)
            int batchSize = 50;
            int totalBatches = (int) Math.ceil((double) toAddresses.size() / batchSize);
            int successfulBatches = 0;
            int failedBatches = 0;

            for (int i = 0; i < toAddresses.size(); i += batchSize) {
                // Apply rate limiting for each batch
                if (!sesRateLimiter.tryAcquire()) {
                    emailRateLimitedCounter.increment(batchSize);
                    log.warn("SES rate limit exceeded for batch {}/{}", (i / batchSize + 1), totalBatches);
                    failedBatches++;
                    continue;
                }

                int endIndex = Math.min(i + batchSize, toAddresses.size());
                List<String> batch = toAddresses.subList(i, endIndex);

                Timer.Sample sample = Timer.start();
                try {
                    SendEmailRequest.Builder emailRequestBuilder = SendEmailRequest
                        .builder()
                        .destination(Destination.builder().toAddresses(batch).build())
                        .message(Message.builder().subject(Content.builder().data(subject).build()).body(emailBody).build())
                        .source(from);

                    if (headers != null && !headers.isEmpty()) {
                        emailRequestBuilder =
                            emailRequestBuilder.overrideConfiguration(cfg -> {
                                headers.forEach(cfg::putHeader);
                            });
                    }

                    SendEmailRequest emailRequest = emailRequestBuilder.build();

                    // Execute with circuit breaker protection
                    SendEmailResponse response = sesCircuitBreaker.executeSupplier(() -> {
                        return sesClient.sendEmail(emailRequest);
                    });

                    emailSentCounter.increment(batch.size());
                    sample.stop(emailSendTimer);
                    successfulBatches++;
                    log.debug(
                        "Sent batch {}/{} from {} to {} recipients: {}",
                        (i / batchSize + 1),
                        totalBatches,
                        from,
                        batch.size(),
                        response.messageId()
                    );
                } catch (SesException e) {
                    emailFailedCounter.increment(batch.size());
                    sample.stop(emailSendTimer);
                    failedBatches++;
                    log.error("Failed to send batch {}/{} via SES: {}", (i / batchSize + 1), totalBatches, e.getMessage(), e);
                    // Continue with next batch instead of failing completely
                } catch (Exception e) {
                    emailFailedCounter.increment(batch.size());
                    sample.stop(emailSendTimer);
                    failedBatches++;
                    log.error("Unexpected error sending batch {}/{}: {}", (i / batchSize + 1), totalBatches, e.getMessage(), e);
                }
            }

            log.info(
                "Batch email sending completed from {}: {} successful batches, {} failed batches, {} total recipients",
                from,
                successfulBatches,
                failedBatches,
                toAddresses.size()
            );
        } catch (Exception e) {
            log.error("Failed to send batch emails via SES from {}: {}", from, e.getMessage(), e);
            throw new RuntimeException("Failed to send batch emails via SES: " + e.getMessage(), e);
        }
    }

    // Helper to build List-Unsubscribe header value
    public static String buildListUnsubscribeHeader(String email, String link) {
        return String.format("<mailto:unsubscribe@yourdomain.com>, <%s>", link);
    }

    // Batch email sending for better performance with rate limiting and circuit breaker
    public void sendBatchEmails(List<String> toAddresses, String subject, String body, boolean isHtml, Map<String, String> headers) {
        // Check circuit breaker
        if (sesCircuitBreaker.getState() == io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN) {
            circuitBreakerOpenCounter.increment();
            log.warn("SES circuit breaker is OPEN, rejecting batch email send request");
            throw new RuntimeException("Email service is temporarily unavailable (circuit breaker open)");
        }

        try {
            Body emailBody;
            if (isHtml) {
                emailBody = Body.builder().html(Content.builder().data(body).build()).build();
            } else {
                emailBody = Body.builder().text(Content.builder().data(body).build()).build();
            }

            // Send emails in batches of 50 (SES recommended batch size)
            int batchSize = 50;
            int totalBatches = (int) Math.ceil((double) toAddresses.size() / batchSize);
            int successfulBatches = 0;
            int failedBatches = 0;

            for (int i = 0; i < toAddresses.size(); i += batchSize) {
                // Apply rate limiting for each batch
                if (!sesRateLimiter.tryAcquire()) {
                    emailRateLimitedCounter.increment(batchSize);
                    log.warn("SES rate limit exceeded for batch {}/{}", (i / batchSize + 1), totalBatches);
                    failedBatches++;
                    continue;
                }

                int endIndex = Math.min(i + batchSize, toAddresses.size());
                List<String> batch = toAddresses.subList(i, endIndex);

                Timer.Sample sample = Timer.start();
                try {
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

                    // Execute with circuit breaker protection
                    SendEmailResponse response = sesCircuitBreaker.executeSupplier(() -> {
                        return sesClient.sendEmail(emailRequest);
                    });

                    emailSentCounter.increment(batch.size());
                    sample.stop(emailSendTimer);
                    successfulBatches++;
                    log.debug(
                        "Sent batch {}/{} to {} recipients: {}",
                        (i / batchSize + 1),
                        totalBatches,
                        batch.size(),
                        response.messageId()
                    );
                } catch (SesException e) {
                    emailFailedCounter.increment(batch.size());
                    sample.stop(emailSendTimer);
                    failedBatches++;
                    log.error("Failed to send batch {}/{} via SES: {}", (i / batchSize + 1), totalBatches, e.getMessage(), e);
                    // Continue with next batch instead of failing completely
                } catch (Exception e) {
                    emailFailedCounter.increment(batch.size());
                    sample.stop(emailSendTimer);
                    failedBatches++;
                    log.error("Unexpected error sending batch {}/{}: {}", (i / batchSize + 1), totalBatches, e.getMessage(), e);
                }
            }

            log.info(
                "Batch email sending completed: {} successful batches, {} failed batches, {} total recipients",
                successfulBatches,
                failedBatches,
                toAddresses.size()
            );
        } catch (Exception e) {
            log.error("Failed to send batch emails via SES: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send batch emails via SES: " + e.getMessage(), e);
        }
    }
}
