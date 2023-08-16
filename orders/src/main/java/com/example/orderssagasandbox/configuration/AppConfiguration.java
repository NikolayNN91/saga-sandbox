package com.example.orderssagasandbox.configuration;

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

    @Value(value = "${app.activemq.topic.name.order}")
    private String orderTopicName;
    @Value(value = "${app.activemq.topic.name.accounts}")
    private String accountsTopicName;
    @Value(value = "${app.activemq.topic.name.stock}")
    private String stockTopicName;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:changelog/master.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    @Bean
    public Topic topic() {
        return new ActiveMQTopic(orderTopicName);
    }

    @Bean
    public Topic accountsTopic() {
        return new ActiveMQTopic(accountsTopicName);
    }

    @Bean
    public Topic stockTopic() {
        return new ActiveMQTopic(stockTopicName);
    }

}
