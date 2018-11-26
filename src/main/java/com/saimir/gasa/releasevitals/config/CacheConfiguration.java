package com.saimir.gasa.releasevitals.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.saimir.gasa.releasevitals.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Release.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Release.class.getName() + ".projects", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Sprint.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Sprint.class.getName() + ".issues", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Project.class.getName() + ".versions", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Project.class.getName() + ".epics", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Project.class.getName() + ".issues", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Version.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Epic.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Epic.class.getName() + ".unestimatedIssues", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Issue.class.getName(), jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Issue.class.getName() + ".statuses", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Issue.class.getName() + ".fixVersions", jcacheConfiguration);
            cm.createCache(com.saimir.gasa.releasevitals.domain.Status.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
