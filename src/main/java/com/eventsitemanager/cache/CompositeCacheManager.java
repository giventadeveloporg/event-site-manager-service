package com.eventsitemanager.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

/**
 * Composite Cache Manager implementing L1/L2 cache pattern.
 *
 * L1 Cache (Caffeine): Fast in-memory cache for frequently accessed data
 * L2 Cache (PostgreSQL): Persistent cache for longer-term storage (future implementation)
 *
 * Current implementation: L1 (Caffeine) with configurable TTL per cache name
 * Future: Add PostgreSQL cache as L2 fallback
 */
@Component
public class CompositeCacheManager implements CacheManager {

    private static final Logger log = LoggerFactory.getLogger(CompositeCacheManager.class);

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    // Cache TTL configurations (in seconds)
    @Value("${cache.ttl.userProfiles:3600}")
    private long userProfilesTtl;

    @Value("${cache.ttl.userProfilesByUserId:1800}")
    private long userProfilesByUserIdTtl;

    @Value("${cache.ttl.userProfilesByEmail:1800}")
    private long userProfilesByEmailTtl;

    @Value("${cache.ttl.eventDetails:7200}")
    private long eventDetailsTtl;

    @Value("${cache.ttl.eventMedia:3600}")
    private long eventMediaTtl;

    @Value("${cache.ttl.eventTicketTypes:3600}")
    private long eventTicketTypesTtl;

    @Value("${cache.ttl.eventTicketTransactions:1800}")
    private long eventTicketTransactionsTtl;

    @Value("${cache.ttl.tenantSettings:7200}")
    private long tenantSettingsTtl;

    @Value("${cache.ttl.eventTypeDetails:7200}")
    private long eventTypeDetailsTtl;

    @Value("${cache.ttl.clerkUsers:300}")
    private long clerkUsersTtl; // Short TTL for external API responses

    @Value("${cache.ttl.tenantEmailAddresses:3600}")
    private long tenantEmailAddressesTtl;

    @Value("${cache.maxSize:1000}")
    private int maxCacheSize;

    @PostConstruct
    public void initializeCaches() {
        log.info("Initializing Composite Cache Manager with L1 (Caffeine) cache");

        // Initialize caches with specific TTL configurations
        Set<Cache> caches = Set.of(
            createCache("userProfiles", userProfilesTtl),
            createCache("userProfilesByUserId", userProfilesByUserIdTtl),
            createCache("userProfilesByEmail", userProfilesByEmailTtl),
            createCache("eventDetails", eventDetailsTtl),
            createCache("eventMedia", eventMediaTtl),
            createCache("eventTicketTypes", eventTicketTypesTtl),
            createCache("eventTicketTransactions", eventTicketTransactionsTtl),
            createCache("tenantSettings", tenantSettingsTtl),
            createCache("eventTypeDetails", eventTypeDetailsTtl),
            createCache("clerkUsers", clerkUsersTtl),
            createCache("tenantEmailAddresses", tenantEmailAddressesTtl)
        );

        // Populate cache map for quick lookup
        caches.forEach(cache -> cacheMap.put(cache.getName(), cache));

        log.info("Composite Cache Manager initialized with {} caches", caches.size());
    }

    /**
     * Create a Caffeine cache with specific TTL and size configuration
     */
    private CaffeineCache createCache(String name, long ttlSeconds) {
        Caffeine<Object, Object> caffeine = Caffeine
            .newBuilder()
            .maximumSize(maxCacheSize)
            .expireAfterWrite(Duration.ofSeconds(ttlSeconds))
            .recordStats(); // Enable statistics for monitoring

        log.debug("Created cache '{}' with TTL: {} seconds, maxSize: {}", name, ttlSeconds, maxCacheSize);
        return new CaffeineCache(name, caffeine.build());
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache == null) {
            // Create cache on-demand if not pre-configured
            log.debug("Creating on-demand cache: {}", name);
            cache = createCache(name, 3600); // Default 1 hour TTL
            cacheMap.put(name, cache);
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    /**
     * Get cache statistics for monitoring
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        cacheMap.forEach((name, cache) -> {
            if (cache instanceof CaffeineCache caffeineCache) {
                com.github.benmanes.caffeine.cache.stats.CacheStats cacheStats = caffeineCache.getNativeCache().stats();
                Map<String, Object> cacheStatsMap = new ConcurrentHashMap<>();
                cacheStatsMap.put("hitCount", cacheStats.hitCount());
                cacheStatsMap.put("missCount", cacheStats.missCount());
                cacheStatsMap.put("hitRate", cacheStats.hitRate());
                cacheStatsMap.put("evictionCount", cacheStats.evictionCount());
                cacheStatsMap.put("size", caffeineCache.getNativeCache().estimatedSize());
                stats.put(name, cacheStatsMap);
            }
        });
        return stats;
    }
}
