/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2api.configuration;

/**
 *
 * @author sitaramu
 */
//import com.vm.Qsmart.utils.DBConstants;
import com.vm.qsmart2.utils.DateUtils;
import com.vm.qsmart2api.utils.DBConstants;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@ConditionalOnProperty(
//    value="module.enabled", 
//    havingValue = "true", 
//    matchIfMissing = true)
@PropertySources({
    @PropertySource("file:config/application.properties")})
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = DBConstants.JPA_UNIT_NAME_QSMART,transactionManagerRef = "mssqlTransactionManager"
        ,basePackages = "com.vm.qsmart2api.repository")
public class MssqlDataSource {

    @Autowired
    private Environment env; // Contains Properties Load by @PropertySources

    @Primary
    @Bean
    public DataSource mssqlDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.mssql.datasource.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.mssql.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.mssql.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.mssql.datasource.password"));
        return dataSource;
    }
    

    @Primary
    @Bean(name = DBConstants.JPA_UNIT_NAME_QSMART)
    LocalContainerEntityManagerFactoryBean mssqlEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mssqlDatasource());
        em.setPackagesToScan(new String[]{"com.vm.qsmart2.model"});
        em.setPersistenceUnitName(DBConstants.JPA_UNIT_NAME_QSMART);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", env.getProperty("hibernate.mssql.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("hibernate.mssql.format_sql"));
        //properties.put("hibernate.use_sql_comments", env.getProperty("hibernate.mssql.use_sql_comments"));
        em.setJpaPropertyMap(properties);
      //  em.afterPropertiesSet();
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
    public DateUtils dateUtils(){
        return new DateUtils();
    }

}
