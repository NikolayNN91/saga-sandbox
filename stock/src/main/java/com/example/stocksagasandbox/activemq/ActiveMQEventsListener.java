package com.example.stocksagasandbox.activemq;

import com.example.stocksagasandbox.model.OrderSagaCommand;
import com.example.stocksagasandbox.service.ParticipantEventHandler;
import com.example.stocksagasandbox.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActiveMQEventsListener {

    private final ParticipantEventHandler participantEventHandler;

    public ActiveMQEventsListener(ParticipantEventHandler participantEventHandler) {
        this.participantEventHandler = participantEventHandler;
    }

    @JmsListener(destination = "${app.activemq.topic.name.order}", subscription = "stockService")
    public void receiver(String string) {
        log.info("Message received: {}", string);
        OrderSagaCommand command = Utils.readValue(string, OrderSagaCommand.class);
        participantEventHandler.handleOrderCommand(command);
    }
}
