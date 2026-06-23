package com.eventsitemanager.config;

import com.eventsitemanager.cache.CompositeCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

/**
 * Unified Cache Configuration for all profiles
 * Uses CompositeCacheManager for L1/L2 cache pattern with configurable TTL
 */
@Configuration
@EnableCaching
public class UnifiedCacheConfiguration {

    @Autowired(required = false)
    private GitProperties gitProperties;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Autowired
    private CompositeCacheManager compositeCacheManager;

    /**
     * Primary cache manager - CompositeCacheManager with L1 (Caffeine) and L2 (PostgreSQL) support
     * Configured with per-cache TTL settings from application.yml
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        return compositeCacheManager;
    }

    /**
     * Key generator for cache keys
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
