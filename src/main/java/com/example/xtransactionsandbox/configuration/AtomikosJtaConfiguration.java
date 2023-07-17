package com.example.xtransactionsandbox.configuration;


import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

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

    @Bean(name = "userTransactionManager", destroyMethod = "close")
    public TransactionManager userTransactionManager() throws SystemException {
        UserTransactionManager transactionManager = new UserTransactionManager();
        transactionManager.setForceShutdown(true);
        transactionManager.init();
        return transactionManager;
    }
}
