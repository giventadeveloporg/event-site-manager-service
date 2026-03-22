package com.nextjstemplate.service.cache;

import com.nextjstemplate.domain.TenantSettings;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Invalidates Spring Cache and JPA second-level cache entries for {@link TenantSettings}
 * after mutations. Required because {@code findOne(id)} uses {@code @Cacheable} and the
 * entity is annotated with Hibernate {@code @Cache}; PATCH previously skipped eviction.
 * <p>
 * Placed under {@code com.nextjstemplate.service.cache} so ArchUnit layered architecture
 * allows referencing {@code domain} (see {@code TechnicalStructureTest} and project docs).
 */
@Component
public class TenantSettingsCacheInvalidation {

    public static final String TENANT_SETTINGS_CACHE_NAME = "tenantSettings";

    private final CacheManager cacheManager;
    private final EntityManagerFactory entityManagerFactory;

    public TenantSettingsCacheInvalidation(CacheManager cacheManager, EntityManagerFactory entityManagerFactory) {
        this.cacheManager = cacheManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Evicts the Spring cache entry keyed by entity id (see {@code TenantSettingsServiceImpl#findOne})
     * and the Hibernate second-level cache region for this entity id.
     *
     * @param tenantSettingsId primary key; no-op if null
     */
    public void evictForTenantSettingsId(Long tenantSettingsId) {
        if (tenantSettingsId == null) {
            return;
        }
        Cache springCache = cacheManager.getCache(TENANT_SETTINGS_CACHE_NAME);
        if (springCache != null) {
            springCache.evict(tenantSettingsId);
        }
        jakarta.persistence.Cache jpaCache = entityManagerFactory.getCache();
        if (jpaCache != null) {
            jpaCache.evict(TenantSettings.class, tenantSettingsId);
        }
    }
}
