package com.example.xtransactionsandbox.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ComponentScan("com.example.xtransactionsandbox.entity.jpa.postgresql")
@EnableJpaRepositories(basePackages = "com.example.xtransactionsandbox.repository.postgresql", entityManagerFactoryRef = "postgresqlEntityManager", transactionManagerRef = "transactionManager")
public class PGConfig {

    @Bean(destroyMethod = "close")
    @Qualifier("postgresqlDataSource")
    public DataSource postgresqlDataSource(@Value("${spring.datasource.postgresql.url}") String url,
                                           @Value("${spring.datasource.postgresql.username}") String username,
                                           @Value("${spring.datasource.postgresql.password}") String password) throws AtomikosSQLException {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(url);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);

        AtomikosDataSourceBean atomikosXADataSource = new AtomikosDataSourceBean();
        atomikosXADataSource.setXaDataSource(pgxaDataSource);
        atomikosXADataSource.setUniqueResourceName("xads2");
        atomikosXADataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        atomikosXADataSource.init();

        return atomikosXADataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean postgresqlEntityManager(DataSource postgresqlDataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.show_sql", "true");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJtaDataSource(postgresqlDataSource);
        factory.setJpaPropertyMap(properties);
        factory.setPackagesToScan("com.example.xtransactionsandbox.entity.jpa.postgresql");
        // Configure the entity manager factory
        return factory;
    }
}
