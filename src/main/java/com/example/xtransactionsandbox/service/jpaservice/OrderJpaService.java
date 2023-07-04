package com.example.xtransactionsandbox.service.jpaservice;

import com.example.xtransactionsandbox.entity.jpa.postgresql.OrderJpaEntity;
import com.example.xtransactionsandbox.exception.OrderNotFoundException;
import com.example.xtransactionsandbox.repository.postgresql.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderJpaService {

    private final OrderRepository orderRepository;

    public OrderJpaService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderJpaEntity getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public OrderJpaEntity createOrder(OrderJpaEntity order) {
        return orderRepository.save(order);
    }

    ;

    public OrderJpaEntity updateOrder(OrderJpaEntity order) {
        return orderRepository.save(order);
    }
}
