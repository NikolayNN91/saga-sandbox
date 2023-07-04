package com.example.xtransactionsandbox.controller;

import com.example.xtransactionsandbox.entity.jpa.mysql.AccountJpaEntity;
import com.example.xtransactionsandbox.entity.jpa.postgresql.MerchandiseJpaEntity;
import com.example.xtransactionsandbox.entity.jpa.postgresql.OrderJpaEntity;
import com.example.xtransactionsandbox.service.jpaservice.AccountJpaService;
import com.example.xtransactionsandbox.service.jpaservice.OrderJpaService;
import com.example.xtransactionsandbox.service.jpaservice.PurchaseOfGoodsJpaService;
import com.example.xtransactionsandbox.service.jpaservice.StockJpaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    private final PurchaseOfGoodsJpaService purchaseOfGoodsService;
    private final OrderJpaService orderService;
    private final AccountJpaService accountService;
    private final StockJpaService stockService;

    public GoodsController(PurchaseOfGoodsJpaService purchaseOfGoodsService, OrderJpaService orderService, AccountJpaService accountService, StockJpaService stockService) {
        this.purchaseOfGoodsService = purchaseOfGoodsService;
        this.orderService = orderService;
        this.accountService = accountService;
        this.stockService = stockService;
    }

    @PostMapping("/buy/{orderId}")
    public void buyGoods(@PathVariable Long orderId) {
        purchaseOfGoodsService.checkout(orderId);
    }

    @PutMapping("/order")
    public OrderJpaEntity createOrder(@RequestBody OrderJpaEntity order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/order/{orderId}")
    public OrderJpaEntity getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/order/{orderId}")
    public OrderJpaEntity updateOrder(@RequestBody OrderJpaEntity order) {
        return orderService.updateOrder(order);
    }

    @PutMapping("/account")
    public AccountJpaEntity createAccount(@RequestBody AccountJpaEntity account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/account")
    public AccountJpaEntity updateAccount(@RequestBody AccountJpaEntity account) {
        return accountService.updateAccount(account);
    }

    @GetMapping("/account/{accountId}")
    public AccountJpaEntity getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

    @PutMapping("/merchandise")
    public MerchandiseJpaEntity createMerchandise(@RequestBody MerchandiseJpaEntity merchandise) {
        return stockService.createMerchandise(merchandise);
    }

    @GetMapping("/merchandise/{merchandiseId}")
    public MerchandiseJpaEntity getMerchandise(@PathVariable Long merchandiseId) {
        return stockService.getMerchandise(merchandiseId);
    }
}
