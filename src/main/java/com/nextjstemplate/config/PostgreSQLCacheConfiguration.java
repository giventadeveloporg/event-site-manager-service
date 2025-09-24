package com.nextjstemplate.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

/**
 * PostgreSQL Cache Configuration for local-postgres-cache profile.
 * Provides a simple in-memory cache manager when PostgreSQL caching is used.
 */
@Configuration
@EnableCaching
@Profile("local-postgres-cache")
public class PostgreSQLCacheConfiguration {

  @Autowired(required = false)
  private GitProperties gitProperties;

  @Autowired(required = false)
  private BuildProperties buildProperties;

  /**
   * Simple in-memory cache manager for PostgreSQL caching profile.
   * This provides basic caching functionality without external cache
   * dependencies.
   */
  @Bean
  @Primary
  public CacheManager cacheManager() {
    org.springframework.cache.concurrent.ConcurrentMapCacheManager cacheManager = new org.springframework.cache.concurrent.ConcurrentMapCacheManager();

    // Enable dynamic cache creation
    cacheManager.setAllowNullValues(false);
    cacheManager.setCacheNames(java.util.Arrays.asList(
        "com.nextjstemplate.domain.UserProfile",
        "com.nextjstemplate.domain.UserSubscription",
        "com.nextjstemplate.domain.UserTask",
        "com.nextjstemplate.domain.EventMedia",
        "com.nextjstemplate.domain.TenantOrganization",
        "com.nextjstemplate.domain.EventTypeDetails",
        "com.nextjstemplate.domain.EventDetails",
        "com.nextjstemplate.domain.EventTicketTransaction",
        "com.nextjstemplate.domain.EventTicketType",
        "com.nextjstemplate.domain.EventPoll",
        "com.nextjstemplate.domain.EventPollOption",
        "com.nextjstemplate.domain.EventPollResponse",
        "com.nextjstemplate.domain.EventAdmin",
        "com.nextjstemplate.domain.EventAdminAuditLog",
        "com.nextjstemplate.domain.EventCalendarEntry",
        "com.nextjstemplate.domain.UserPaymentTransaction",
        "com.nextjstemplate.domain.TenantSettings",
        "com.nextjstemplate.domain.EventAttendee",
        "com.nextjstemplate.domain.UserRegistrationRequest",
        "com.nextjstemplate.domain.QrCodeUsage",
        "com.nextjstemplate.domain.BulkOperationLog",
        "com.nextjstemplate.domain.EventAttendeeGuest",
        "com.nextjstemplate.domain.EventGuestPricing",
        "com.nextjstemplate.domain.EventOrganizer",
        "com.nextjstemplate.domain.DiscountCode",
        "com.nextjstemplate.domain.EventScoreCard",
        "com.nextjstemplate.domain.EventScoreCardDetail",
        "com.nextjstemplate.domain.EventLiveUpdate",
        "com.nextjstemplate.domain.EventLiveUpdateAttachment",
        "com.nextjstemplate.domain.EmailLog",
        "com.nextjstemplate.domain.WhatsAppLog",
        "com.nextjstemplate.domain.CommunicationCampaign",
        "com.nextjstemplate.domain.EventTicketTransactionItem",
        "com.nextjstemplate.domain.ExecutiveCommitteeTeamMember"));

    return cacheManager;
  }

  /**
   * Key generator for cache keys.
   */
  @Bean
  public KeyGenerator keyGenerator() {
    return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
  }

  /**
   * PostgreSQL cache table initializer.
   * Creates the cache table if it doesn't exist.
   */
  @Bean
  @ConditionalOnProperty(name = "postgresql-cache.enabled", havingValue = "true", matchIfMissing = true)
  public PostgreSQLCacheInitializer postgreSQLCacheInitializer(DataSource dataSource) {
    return new PostgreSQLCacheInitializer(dataSource);
  }

  /**
   * PostgreSQL Cache Initializer - creates cache tables in PostgreSQL.
   */
  public static class PostgreSQLCacheInitializer {

    private final JdbcTemplate jdbcTemplate;

    public PostgreSQLCacheInitializer(DataSource dataSource) {
      this.jdbcTemplate = new JdbcTemplate(dataSource);
      initializeCacheTables();
    }

    private void initializeCacheTables() {
      try {
        // Create cache table
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS app_cache (
                cache_key VARCHAR(255) PRIMARY KEY,
                cache_value TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                expires_at TIMESTAMP,
                tenant_id VARCHAR(100) DEFAULT 'default'
            )
            """);

        // Create session store table
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS session_store (
                session_id VARCHAR(255) PRIMARY KEY,
                session_data TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_accessed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                expires_at TIMESTAMP,
                tenant_id VARCHAR(100) DEFAULT 'default'
            )
            """);

        // Create indexes for performance
        jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS idx_app_cache_tenant_expires
            ON app_cache(tenant_id, expires_at)
            """);

        jdbcTemplate.execute("""
            CREATE INDEX IF NOT EXISTS idx_session_store_tenant_expires
            ON session_store(tenant_id, expires_at)
            """);

        System.out.println("PostgreSQL cache tables initialized successfully");

      } catch (Exception e) {
        System.err.println("Failed to initialize PostgreSQL cache tables: " + e.getMessage());
        // Don't fail startup if cache tables can't be created
      }
    }
  }
}
