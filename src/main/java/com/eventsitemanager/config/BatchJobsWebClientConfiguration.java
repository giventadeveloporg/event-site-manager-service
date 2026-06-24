package com.eventsitemanager.config;

import com.eventsitemanager.properties.BatchJobProperties;
import com.eventsitemanager.service.BatchJobServiceAuthTokenProvider;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient for the batch-jobs microservice. Adds Bearer JWT when {@code batch-jobs.service.auth.*} is set.
 */
@Configuration
public class BatchJobsWebClientConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BatchJobsWebClientConfiguration.class);

    private final BatchJobProperties batchJobProperties;
    private final BatchJobServiceAuthTokenProvider authTokenProvider;

    public BatchJobsWebClientConfiguration(BatchJobProperties batchJobProperties, BatchJobServiceAuthTokenProvider authTokenProvider) {
        this.batchJobProperties = batchJobProperties;
        this.authTokenProvider = authTokenProvider;
    }

    @PostConstruct
    public void logAuthStatus() {
        if (Boolean.FALSE.equals(batchJobProperties.getEnabled())) {
            return;
        }
        if (!authTokenProvider.isConfigured()) {
            log.warn(
                "Batch jobs service is enabled but batch-jobs.service.auth.username/password are not set. " +
                "Outbound calls to the batch-jobs API will get HTTP 401 unless that service permits anonymous access. " +
                "Set BATCH_JOBS_AUTH_USER/BATCH_JOBS_AUTH_PASS (or API_JWT_USER/API_JWT_PASS) and BATCH_JOBS_AUTH_BASE_URL if needed."
            );
        }
    }

    @Bean
    @Qualifier("batchJobsWebClient")
    public WebClient batchJobsWebClient() {
        WebClient.Builder builder = WebClient
            .builder()
            .baseUrl(batchJobProperties.getUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        if (authTokenProvider.isConfigured()) {
            builder.filter(bearerAuthFilter());
        }

        return builder.build();
    }

    private ExchangeFilterFunction bearerAuthFilter() {
        return (request, next) -> {
            String token = authTokenProvider.getTokenBlocking();
            if (token == null) {
                return next.exchange(request);
            }
            ClientRequest withAuth = ClientRequest.from(request).headers(headers -> headers.setBearerAuth(token)).build();
            return next.exchange(withAuth);
        };
    }
}
