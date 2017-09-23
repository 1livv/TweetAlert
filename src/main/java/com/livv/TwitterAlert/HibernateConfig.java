package com.livv.TwitterAlert;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by gheorghe on 23/09/2017.
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private Config config;

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }

    @Bean
    public DataSource restDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(config.getProperty("talert.jdbc.driverClassName"));
        basicDataSource.setUrl(config.getProperty("talert.jdbc.url"));
        basicDataSource.setUsername(config.getProperty("talert.jdbc.username"));
        basicDataSource.setPassword(config.getProperty("talert.jdbc.password"));

        return basicDataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean  = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(restDataSource());
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan("com.livv.TwitterAlert");

        return sessionFactoryBean;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager (LocalSessionFactoryBean sessionFactoryBean) {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactoryBean.getObject());

        return hibernateTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto",
                config.getProperty("talert.thibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect",
                config.getProperty("talert.hibernate.dialect"));
        properties.setProperty("hibernate.globally_quoted_identifiers", "true");

        return properties;
    }


}
