package com.zl.middleware.core.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * JPA以及数据源配置
 *
 * @author zl
 * @since 2021/11/18 9:57
 */
@Configuration
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        basePackages = "com.zl.middleware.core.repository")
public class JpaDataSourceConfiguration {
    /**
     * 数据库配置文件
     */
    @Resource
    private DataSourceProperties properties;

    /**
     * 业务数据库，用于查询人员信息
     *
     * @return 业务数据源配置
     */
    @Primary
    @Bean("jpaDataSource")
    public DataSource jpaDataSource() {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * 实体管理器工厂类
     *
     * @param builder {@link EntityManagerFactoryBuilder}
     * @return {@link LocalContainerEntityManagerFactoryBean}
     */
    @Primary
    @Bean("jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(jpaDataSource())
                .packages("com.zl.middleware.core.entity")
                .persistenceUnit("jpaPersistenceUnit")
                .build();
    }

    /**
     * 实体管理器
     *
     * @param builder {@link EntityManagerFactoryBuilder}
     * @return {@link EntityManager}
     */
    @Primary
    @Bean("jpaEntityManager")
    public EntityManager jpaEntityManager(EntityManagerFactoryBuilder builder) {
        return jpaEntityManagerFactory(builder).getObject().createEntityManager();
    }

    /**
     * 事务管理器
     *
     * @param entityManagerFactory 实体管理器工厂
     * @return 事务管理器
     */
    @Primary
    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("jpaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
