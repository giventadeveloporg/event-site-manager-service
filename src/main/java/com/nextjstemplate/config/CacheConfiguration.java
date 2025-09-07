package com.nextjstemplate.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.nextjstemplate.domain.UserProfile.class.getName());
            createCache(cm, com.nextjstemplate.domain.UserSubscription.class.getName());
            createCache(cm, com.nextjstemplate.domain.UserTask.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventMedia.class.getName());
            createCache(cm, com.nextjstemplate.domain.TenantOrganization.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventTypeDetails.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventDetails.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventTicketTransaction.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventTicketType.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventPoll.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventPollOption.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventPollResponse.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventAdmin.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventAdminAuditLog.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventCalendarEntry.class.getName());
            createCache(cm, com.nextjstemplate.domain.UserPaymentTransaction.class.getName());
            createCache(cm, com.nextjstemplate.domain.TenantSettings.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventAttendee.class.getName());
            createCache(cm, com.nextjstemplate.domain.UserRegistrationRequest.class.getName());
            createCache(cm, com.nextjstemplate.domain.QrCodeUsage.class.getName());
            createCache(cm, com.nextjstemplate.domain.BulkOperationLog.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventAttendeeGuest.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventGuestPricing.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventOrganizer.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventDetails.class.getName() + ".discountCodes");
            createCache(cm, com.nextjstemplate.domain.DiscountCode.class.getName());
            createCache(cm, com.nextjstemplate.domain.DiscountCode.class.getName() + ".events");
            createCache(cm, com.nextjstemplate.domain.EventScoreCard.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventScoreCardDetail.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventLiveUpdate.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventLiveUpdateAttachment.class.getName());
            createCache(cm, com.nextjstemplate.domain.EmailLog.class.getName());
            createCache(cm, com.nextjstemplate.domain.WhatsAppLog.class.getName());
            createCache(cm, com.nextjstemplate.domain.CommunicationCampaign.class.getName());
            createCache(cm, com.nextjstemplate.domain.EventTicketTransactionItem.class.getName());
            createCache(cm, com.nextjstemplate.domain.ExecutiveCommitteeTeamMember.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
