package com.example.xtransactionsandbox.service;

import com.example.xtransactionsandbox.entity.AccountEntity;
import com.example.xtransactionsandbox.entity.MerchandiseEntity;
import com.example.xtransactionsandbox.entity.OrderEntity;
import com.example.xtransactionsandbox.exception.AccountNotFoundException;
import com.example.xtransactionsandbox.exception.NotEnoughMoneyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseOfGoodsService {

    private final AccountService accountService;
    private final StockService stockService;
    private final OrderService orderService;

    public PurchaseOfGoodsService(AccountService accountService, StockService stockService, OrderService orderService) {
        this.accountService = accountService;
        this.stockService = stockService;
        this.orderService = orderService;
    }

    @Transactional
    public void checkout(Long orderId) {
        OrderEntity order = orderService.getOrder(orderId);
        AccountEntity account = accountService.getAccounts(order.getCustomerId()).stream()
                .max((a, b) -> (int) (a.getAmount() - b.getAmount()))
                .orElseThrow(AccountNotFoundException::new);
        MerchandiseEntity merchandise = stockService.getMerchandise(order.getMerchandiseId());
        double price = order.getQuantity() * merchandise.getPrice();
        accountService.writeOffMoney(account.getId(), price);
        stockService.writeOffMerchandise(order.getMerchandiseId(), order.getQuantity());
    }
}
