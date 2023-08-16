package com.example.orderssagasandbox.activemq;

import com.example.orderssagasandbox.model.ParticipantReply;
import com.example.orderssagasandbox.service.ParticipantEventHandler;
import com.example.orderssagasandbox.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Component
public class ActiveMQEventsListener {

    private final ParticipantEventHandler participantEventHandler;

    public ActiveMQEventsListener(ParticipantEventHandler participantEventHandler) {
        this.participantEventHandler = participantEventHandler;
    }

    @JmsListeners({@JmsListener(destination = "${app.activemq.topic.name.accounts}"), @JmsListener(destination = "${app.activemq.topic.name.stock}")})
    public void receiver(String string, Message message) {
        log.info("Message received: {}", string);
        ParticipantReply reply = Utils.readValue(string, ParticipantReply.class);
        String jmsMessageID;
        try {
            jmsMessageID = message.getJMSMessageID();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        participantEventHandler.handleEvent(reply, jmsMessageID);
    }
}
