package com.nextjstemplate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

/**
 * Unified Cache Configuration for all profiles
 * Uses Spring Boot's auto-configured Caffeine cache manager
 */
@Configuration
@EnableCaching
public class UnifiedCacheConfiguration {

    @Autowired(required = false)
    private GitProperties gitProperties;

    @Autowired(required = false)
    private BuildProperties buildProperties;

    /**
     * Primary cache manager using Caffeine
     * This will work with spring.cache.type=caffeine configuration
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // Let Spring Boot auto-configure the Caffeine specs from application.yml
        return cacheManager;
    }

    /**
     * Key generator for cache keys
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
