package com.eventsitemanager.web.rest;

import com.eventsitemanager.cache.CompositeCacheManager;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for cache statistics and monitoring.
 */
@RestController
@RequestMapping("/api/cache")
public class CacheStatisticsResource {

    private static final Logger log = LoggerFactory.getLogger(CacheStatisticsResource.class);

    @Autowired
    private CompositeCacheManager compositeCacheManager;

    /**
     * Get cache statistics for all caches
     *
     * @return Map of cache names to their statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        log.debug("REST request to get cache statistics");
        Map<String, Object> statistics = compositeCacheManager.getCacheStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get cache names
     *
     * @return List of cache names
     */
    @GetMapping("/names")
    public ResponseEntity<java.util.Collection<String>> getCacheNames() {
        log.debug("REST request to get cache names");
        return ResponseEntity.ok(compositeCacheManager.getCacheNames());
    }
}
