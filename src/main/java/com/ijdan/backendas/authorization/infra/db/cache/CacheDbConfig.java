package com.ijdan.backendas.authorization.infra.db.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "cacheEntityManagerFactory",
        basePackages = { "com.ijdan.backendas.authorization.infra.db.cache.repo" },
        transactionManagerRef = "cacheTransactionManager"
)
public class CacheDbConfig {

    @Bean(name = "cacheDataSource")
    @ConfigurationProperties(prefix = "spring.h2")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "cacheEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("cacheDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.ijdan.backendas.authorization.infra.db.cache.domain")
                .persistenceUnit("cache")
                .build();
    }

    @Bean(name = "cacheTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("cacheEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

