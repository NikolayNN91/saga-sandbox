package com.example.xtransactionsandbox.configuration;


import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.sql.SQLException;

@Configuration
public class AtomikosJtaConfiguration {

    @Bean
    public UserTransaction userTransaction() {
        UserTransactionImp userTransaction = new UserTransactionImp();
        try {
            userTransaction.setTransactionTimeout(1000);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
        return userTransaction;
    }

    @Bean(name="userTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager userTransactionManager() {
        UserTransactionManager transactionManager = new UserTransactionManager();
        transactionManager.setForceShutdown(false);
        return transactionManager;
    }

    @Bean
    @Qualifier("mysqlDataSource")
    public DataSource mysqlDataSource(@Value("${spring.datasource.mysql.url}") String url,
                                      @Value("${spring.datasource.mysql.username}") String username,
                                      @Value("${spring.datasource.mysql.password}") String password) {
        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(url);
        mysqlXADataSource.setUser(username);
        mysqlXADataSource.setPassword(password);

        try {
            mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        } catch (SQLException e) {
            throw new RuntimeException("Крындец!!!", e);
        }

        AtomikosDataSourceBean atomikosXADataSource = new AtomikosDataSourceBean();
        atomikosXADataSource.setXaDataSource(mysqlXADataSource);
        atomikosXADataSource.setUniqueResourceName("xads");
        return mysqlXADataSource;
    }

    @Bean
    @Qualifier("postgresqlDataSource")
    public DataSource postgresqlDataSource(@Value("${spring.datasource.postgresql.url}") String url,
                                      @Value("${spring.datasource.postgresql.username}") String username,
                                      @Value("${spring.datasource.postgresql.password}") String password) {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(url);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);
//        pgxaDataSource.setCurrentSchema("src/main/resources/schema-postgresql.sql");

        AtomikosDataSourceBean atomikosXADataSource = new AtomikosDataSourceBean();
        atomikosXADataSource.setXaDataSource(pgxaDataSource);
        atomikosXADataSource.setUniqueResourceName("xads2");
        atomikosXADataSource.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");

        return atomikosXADataSource;
    }
}
