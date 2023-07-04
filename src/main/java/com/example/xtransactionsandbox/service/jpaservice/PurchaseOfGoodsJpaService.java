package com.example.xtransactionsandbox.service.jpaservice;

import com.example.xtransactionsandbox.entity.jpa.mysql.AccountJpaEntity;
import com.example.xtransactionsandbox.entity.jpa.postgresql.MerchandiseJpaEntity;
import com.example.xtransactionsandbox.entity.jpa.postgresql.OrderJpaEntity;
import com.example.xtransactionsandbox.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseOfGoodsJpaService {

    private final AccountJpaService accountService;
    private final StockJpaService stockService;
    private final OrderJpaService orderService;

    public PurchaseOfGoodsJpaService(AccountJpaService accountService, StockJpaService stockService, OrderJpaService orderService) {
        this.accountService = accountService;
        this.stockService = stockService;
        this.orderService = orderService;
    }

    @Transactional
    public void checkout(Long orderId) {
        OrderJpaEntity order = orderService.getOrder(orderId);
        AccountJpaEntity account = accountService.getAccounts(order.getCustomerId()).stream()
                .max((a, b) -> (int) (a.getAmount() - b.getAmount()))
                .orElseThrow(AccountNotFoundException::new);
        MerchandiseJpaEntity merchandise = stockService.getMerchandise(order.getMerchandiseId());
        double price = order.getQuantity() * merchandise.getPrice();
        accountService.writeOffMoney(account.getId(), price);
        stockService.writeOffMerchandise(order.getMerchandiseId(), order.getQuantity());
    }
}
