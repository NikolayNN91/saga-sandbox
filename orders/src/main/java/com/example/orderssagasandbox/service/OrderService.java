package com.example.orderssagasandbox.service;

import com.example.orderssagasandbox.entity.OrderEntity;
import com.example.orderssagasandbox.entity.TransactionalOutboxEntity;
import com.example.orderssagasandbox.exception.OrderNotFoundException;
import com.example.orderssagasandbox.model.SagaCommand;
import com.example.orderssagasandbox.repository.OrderRepository;
import com.example.orderssagasandbox.model.DeliveryStatus;
import com.example.orderssagasandbox.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TransactionalOutboxService transactionalOutboxService;

    public OrderService(OrderRepository orderRepository, TransactionalOutboxService transactionalOutboxService) {
        this.orderRepository = orderRepository;
        this.transactionalOutboxService = transactionalOutboxService;
    }

    public OrderEntity getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    public OrderEntity createOrder(OrderEntity order) {
        order.setStatus(OrderStatus.APPROVAL_PENDING);
        OrderEntity saved = orderRepository.save(order);
        TransactionalOutboxEntity newOutboxEntity = buildTransactionalOutboxEntity(order.getId(), SagaCommand.CREATE_ORDER);
        transactionalOutboxService.save(newOutboxEntity);
        return saved;
    }

    public OrderEntity updateOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    private TransactionalOutboxEntity buildTransactionalOutboxEntity(Long orderId, SagaCommand command) {
        TransactionalOutboxEntity newEntity = new TransactionalOutboxEntity();
        newEntity.setOrderId(orderId);
        newEntity.setDeliveryStatus(DeliveryStatus.PENDING);
        newEntity.setCommand(command);
        return newEntity;
    }

    @Transactional
    public void rejectOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(o -> {
            o.setStatus(OrderStatus.REJECTED);
            log.info("Order status changed to REJECTED: id={}", orderId);
        });
        TransactionalOutboxEntity newOutboxEntity = buildTransactionalOutboxEntity(orderId, SagaCommand.REJECT_ORDER);
        transactionalOutboxService.save(newOutboxEntity);
    }
}
