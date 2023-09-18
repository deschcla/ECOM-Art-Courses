package com.ecom.art_courses.config;

import com.ecom.art_courses.domain.*;
import com.ecom.art_courses.repository.UserRepository;
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
            createCache(cm, UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, User.class.getName());
            createCache(cm, Authority.class.getName());
            createCache(cm, User.class.getName() + ".authorities");
            createCache(cm, Acheteur.class.getName());
            createCache(cm, Acheteur.class.getName() + ".releveFactures");
            createCache(cm, Acheteur.class.getName() + ".commandes");
            createCache(cm, Produit.class.getName());
            createCache(cm, Produit.class.getName() + ".ligneCommandes");
            createCache(cm, Produit.class.getName() + ".commandes");
            createCache(cm, Categorie.class.getName());
            createCache(cm, Categorie.class.getName() + ".sousCategories");
            createCache(cm, SousCategorie.class.getName());
            createCache(cm, SousCategorie.class.getName() + ".produits");
            createCache(cm, Commande.class.getName());
            createCache(cm, Commande.class.getName() + ".carteBancaires");
            createCache(cm, Commande.class.getName() + ".produits");
            createCache(cm, Commande.class.getName() + ".ligneCommandes");
            createCache(cm, LigneCommande.class.getName());
            createCache(cm, CarteBancaire.class.getName());
            createCache(cm, ReleveFacture.class.getName());
            createCache(cm, ReleveFacture.class.getName() + ".commandes");
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
