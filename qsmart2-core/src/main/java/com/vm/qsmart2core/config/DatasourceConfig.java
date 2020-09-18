/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.config;

import com.vm.qsmart2.utils.DBConstants;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Phani
 */
@Configuration
@PropertySources({
    @PropertySource("file:config/application.properties")})
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = DBConstants.JPA_UNIT_NAME_QSMART,transactionManagerRef = "mssqlTransactionManager"
        ,basePackages = "com.vm.qsmart2core.repositary")
public class DatasourceConfig {
    
    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource masterDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(env.getProperty("spring.db.url"));
        dataSourceBuilder.username(env.getProperty("spring.db.username"));
        dataSourceBuilder.password(env.getProperty("spring.db.password"));
        dataSourceBuilder.driverClassName(env.getProperty("spring.db.driverClassName"));
        return dataSourceBuilder.build();
    }
    
    
    @Primary
    @Bean(name = DBConstants.JPA_UNIT_NAME_QSMART)
    LocalContainerEntityManagerFactoryBean mssqlEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(masterDataSource());
        em.setPackagesToScan(new String[]{"com.vm.qsmart2.model"});
        em.setPersistenceUnitName(DBConstants.JPA_UNIT_NAME_QSMART);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", env.getProperty("hibernate.db.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.db.format_sql"));
        //properties.put("hibernate.use_sql_comments", env.getProperty("hibernate.mssql.use_sql_comments"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean(name = "mssqlTransactionManager")
    public PlatformTransactionManager mssqlTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mssqlEntityManager().getObject());
        return transactionManager;
    }
    
    @Bean
    public JdbcTemplate intializeJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource());
        return jdbcTemplate;
    }

}
