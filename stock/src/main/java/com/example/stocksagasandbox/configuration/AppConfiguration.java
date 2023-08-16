package com.example.stocksagasandbox.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.Topic;
import javax.sql.DataSource;

@Configuration
@EnableScheduling
public class AppConfiguration {

    @Value("${app.activemq.topic.name.stock}")
    private String stockTopicName;
    @Value("${app.activemq.topic.name.order}")
    private String orderTopicName;

    @Bean
    public Topic orderTopic() {
        return new ActiveMQTopic(orderTopicName);
    }

    @Bean
    public Topic stockTopic() {
        return new ActiveMQTopic(stockTopicName);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:changelog/master.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
