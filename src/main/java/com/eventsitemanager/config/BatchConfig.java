package com.eventsitemanager.config;

/**
 * Configuration for AWS Batch client.
 *
 * DEPRECATED: This configuration is no longer used. We now call the batch job microservice
 * via REST API instead of using AWS Batch SDK directly.
 *
 * The batch job service is configured via {@link BatchJobProperties} and accessed through
 * {@link com.eventsitemanager.service.BatchJobService} which uses WebClient to make REST calls.
 */
public class BatchConfig {
    // This class is kept for reference but is no longer used.
    // Batch job integration is now handled via REST API calls to the batch job microservice.
}
