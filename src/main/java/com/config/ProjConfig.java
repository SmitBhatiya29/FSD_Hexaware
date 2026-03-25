package com.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.*")
public class ProjConfig {
    @Bean
    public DataSource dataSource(){
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/flight_sys_db");
        dataSource.setUsername("root");
        dataSource.setPassword("Smit@001");
        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        // Spring ORM connecting to JPA and give it DS and model class package
        // Ye actual object hai jo
        // DB connection Entities
        // Hibernate config
        // sabko connect karta hai
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();


        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.model");


        // Connect JPA --> Hibernate
        // “JPA ka engine kaun use karega? → Hibernate”
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter =
                new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);



        // Hibernate needs to be told if you want it to create tables
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto","update");

        localContainerEntityManagerFactoryBean.setJpaProperties(properties);

        return localContainerEntityManagerFactoryBean;
    }
    /*
Spring-ORM (LocalContainerEntityManagerFactoryBean)
   --> JPA (jakarta.persistence.EntityManagerFactory) -- Set Property hbm2ddl.auto
        --> Hibernate (HibernateJpaVendorAdapter)
* */

    @Bean
    public PlatformTransactionManager getTransactionManager(EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
    }

}


