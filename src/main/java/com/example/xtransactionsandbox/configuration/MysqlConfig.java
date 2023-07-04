package com.example.xtransactionsandbox.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
@ComponentScan("com.example.xtransactionsandbox.entity.jpa.mysql")
@EnableJpaRepositories(basePackages = "com.example.xtransactionsandbox.repository.mysql",
        entityManagerFactoryRef = "mysqlEntityManager", transactionManagerRef = "transactionManager")
public class MysqlConfig {

    @Bean
    @Qualifier("mysqlDataSource")
    public DataSource mysqlDataSource(@Value("${spring.datasource.mysql.url}") String url,
                                      @Value("${spring.datasource.mysql.username}") String username,
                                      @Value("${spring.datasource.mysql.password}") String password,
                                      @Value("${spring.datasource.mysql.root-password}") String rootPassword) throws SQLException {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(url);
        mysqlXADataSource.setUser("root");
        mysqlXADataSource.setPassword(rootPassword);
        mysqlXADataSource.setUseHostsInPrivileges(true);

        try {
            mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        } catch (SQLException e) {
            throw new RuntimeException("Крындец!!!", e);
        }

        AtomikosDataSourceBean atomikosXADataSource = new AtomikosDataSourceBean();
        atomikosXADataSource.setXaDataSource(mysqlXADataSource);
        atomikosXADataSource.setUniqueResourceName("xads");
        atomikosXADataSource.init();
        return mysqlXADataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mysqlEntityManager(DataSource mysqlDataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.hbm2ddl.auto", "none");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJtaDataSource(mysqlDataSource);
        factory.setJpaPropertyMap(properties);
        factory.setPackagesToScan("com.example.xtransactionsandbox.entity.jpa.mysql");
        // Configure the entity manager factory
        return factory;
    }
}
