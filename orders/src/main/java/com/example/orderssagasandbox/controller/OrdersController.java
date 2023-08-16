package com.example.orderssagasandbox.controller;

import com.example.orderssagasandbox.entity.OrderEntity;
import com.example.orderssagasandbox.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @PostMapping("/buy/{orderId}")
//    public void buyGoods(@PathVariable Long orderId) {
//        purchaseOfGoodsService.checkout(orderId);
//    }

    @PutMapping("/order")
    public OrderEntity createOrder(@RequestBody OrderEntity order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/order/{orderId}")
    public OrderEntity getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/order/{orderId}")
    public OrderEntity updateOrder(@RequestBody OrderEntity order) {
        return orderService.updateOrder(order);
    }
}
