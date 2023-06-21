package com.example.xtransactionsandbox.controller;

import com.example.xtransactionsandbox.entity.AccountEntity;
import com.example.xtransactionsandbox.entity.MerchandiseEntity;
import com.example.xtransactionsandbox.entity.OrderEntity;
import com.example.xtransactionsandbox.service.AccountService;
import com.example.xtransactionsandbox.service.OrderService;
import com.example.xtransactionsandbox.service.PurchaseOfGoodsService;
import com.example.xtransactionsandbox.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    private final PurchaseOfGoodsService purchaseOfGoodsService;
    private final OrderService orderService;
    private final AccountService accountService;
    private final StockService stockService;

    public GoodsController(PurchaseOfGoodsService purchaseOfGoodsService, OrderService orderService, AccountService accountService, StockService stockService) {
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

    @PutMapping("/account")
    public AccountEntity createAccount(@RequestBody AccountEntity account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/account")
    public AccountEntity updateAccount(@RequestBody AccountEntity account) {
        return accountService.updateAccount(account);
    }

    @GetMapping("/account/{accountId}")
    public AccountEntity getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

    @PutMapping("/merchandise")
    public MerchandiseEntity createMerchandise(@RequestBody MerchandiseEntity merchandise) {
        return stockService.createMerchandise(merchandise);
    }

    @GetMapping("/merchandise/{merchandiseId}")
    public MerchandiseEntity getMerchandise(@PathVariable Long merchandiseId) {
        return stockService.getMerchandise(merchandiseId);
    }
}
