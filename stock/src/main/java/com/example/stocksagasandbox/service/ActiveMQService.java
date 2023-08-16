package com.example.stocksagasandbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActiveMQService {

    private final JmsTemplate jmsTemplate;

    @Value(value = "${app.activemq.topic.name.stock}")
    private String stockTopicName;

    public ActiveMQService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String msg) {
        jmsTemplate.convertAndSend(stockTopicName, msg);
        log.info("Message is published: {}", msg);
    }
}
