package com.example.orderssagasandbox.scheduler;

import com.example.orderssagasandbox.entity.OrderEntity;
import com.example.orderssagasandbox.exception.OrderNotFoundException;
import com.example.orderssagasandbox.model.Order;
import com.example.orderssagasandbox.model.OrderSagaCommand;
import com.example.orderssagasandbox.repository.OrderRepository;
import com.example.orderssagasandbox.repository.TransactionalOutboxRepository;
import com.example.orderssagasandbox.service.ActiveMQService;
import com.example.orderssagasandbox.model.DeliveryStatus;
import com.example.orderssagasandbox.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
public class TransactionalOutboxPollingScheduler {

    private final TransactionalOutboxRepository transactionalOutboxRepository;
    private final OrderRepository orderRepository;
    private final ActiveMQService activeMQService;

    public TransactionalOutboxPollingScheduler(TransactionalOutboxRepository transactionalOutboxRepository, OrderRepository orderRepository, ActiveMQService activeMQService) {
        this.transactionalOutboxRepository = transactionalOutboxRepository;
        this.orderRepository = orderRepository;
        this.activeMQService = activeMQService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void pollAndPublishEvents() {
        log.info("Polling...");
        transactionalOutboxRepository
                .findAllByDeliveryStatus("0")
                .forEach(outbox -> {
                    log.info("next outbox entity: {}",outbox);
                    OrderEntity orderEntity = orderRepository.findById(outbox.getOrderId()).orElseThrow(OrderNotFoundException::new);
                    Order order = Order.createFrom(orderEntity);
                    OrderSagaCommand orderSagaCommand = new OrderSagaCommand();
                    orderSagaCommand.setOrderId(outbox.getOrderId());
                    orderSagaCommand.setCommand(outbox.getCommand());
                    orderSagaCommand.setOrder(order);
                    String s = Utils.writeValueAsString(orderSagaCommand);
                    activeMQService.sendMessage(s);
//                    rabbitMQService.sendMessage2(s);
                    log.info("Message sent: {}", s);
                    outbox.setDeliveryStatus(DeliveryStatus.DELIVERED);
                });
    }
}
