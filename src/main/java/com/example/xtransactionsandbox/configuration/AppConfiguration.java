package com.example.xtransactionsandbox.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
public class AppConfiguration {

    // mysql datasource
//    @Bean
//    @ConfigurationProperties("spring.datasource.mysql")
//    public DataSourceProperties mysqlDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    // postgresql datasource
//    @Bean
//    @ConfigurationProperties("spring.datasource.postgresql")
//    public DataSourceProperties postgresqlDataSourceProperties() {
//        return new DataSourceProperties();
//    }

    @Bean
    @DependsOn("userTransactionManager")
    public PlatformTransactionManager transactionManager(UserTransaction userTransaction,
                                                         @Qualifier("userTransactionManager") TransactionManager userTransactionManager) {
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

    @Bean
    public SpringLiquibase mysqlLiquibase(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/mysql/changelog/master.xml");
        liquibase.setDataSource(mysqlDataSource);
        return liquibase;
    }

    @Bean
    public SpringLiquibase postgresqlLiquibase(@Qualifier("postgresqlDataSource") DataSource postgresqlDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/postgresql/changelog/master.xml");
        liquibase.setDataSource(postgresqlDataSource);
        return liquibase;
    }
}
