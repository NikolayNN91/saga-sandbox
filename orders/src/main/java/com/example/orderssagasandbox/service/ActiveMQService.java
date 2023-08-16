package com.example.orderssagasandbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActiveMQService {

    private final JmsTemplate activeTemplate;

    @Value(value = "${app.activemq.topic.name.order}")
    private String orderTopicName;

    public ActiveMQService(JmsTemplate activeTemplate) {
        this.activeTemplate = activeTemplate;
    }

    public void sendMessage(String msg) {
        activeTemplate.convertAndSend(orderTopicName, msg);
        log.info("Message is published: {}", msg);
    }
}
