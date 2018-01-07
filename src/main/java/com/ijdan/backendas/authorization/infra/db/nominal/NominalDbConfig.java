package com.ijdan.backendas.authorization.infra.db.nominal;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "nominalEntityManagerFactory",
        basePackages = { "com.ijdan.backendas.authorization.infra.db.nominal.repo" },
        transactionManagerRef = "nominalTransactionManager"
)
public class NominalDbConfig {

    @Primary
    @Bean(name = "nominalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "nominalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("nominalDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.ijdan.backendas.authorization.infra.db.nominal.domain")
                .persistenceUnit("nominal")
                .build();
    }

    @Primary
    @Bean(name = "nominalTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("nominalEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

