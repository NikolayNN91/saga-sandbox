package com.example.xtransactionsandbox.service;

import com.example.xtransactionsandbox.dao.OrderDao;
import com.example.xtransactionsandbox.exception.OrderNotFoundException;
import com.example.xtransactionsandbox.entity.OrderEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderEntity getOrder(Long orderId) {
        return orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public OrderEntity createOrder(OrderEntity order) {
        return orderDao.insert(order);
    };

    public OrderEntity updateOrder(OrderEntity order) {
        return orderDao.updateById(order);
    }
}
