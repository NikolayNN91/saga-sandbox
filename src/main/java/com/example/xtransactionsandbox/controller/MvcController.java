package com.example.xtransactionsandbox.controller;

import com.example.xtransactionsandbox.entity.jpa.mysql.AccountJpaEntity;
import com.example.xtransactionsandbox.entity.jpa.postgresql.MerchandiseJpaEntity;
import com.example.xtransactionsandbox.entity.jpa.postgresql.OrderJpaEntity;
import com.example.xtransactionsandbox.repository.mysql.AccountRepository;
import com.example.xtransactionsandbox.repository.postgresql.GoodsRepository;
import com.example.xtransactionsandbox.repository.postgresql.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MvcController {

    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;

    public MvcController(AccountRepository accountRepository, OrderRepository orderRepository, GoodsRepository goodsRepository) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.goodsRepository = goodsRepository;
    }

    @GetMapping("/dashboard")
    public ModelAndView getMerchandise() {
        List<AccountJpaEntity> allAccounts = accountRepository.findAll();
        List<OrderJpaEntity> allOrders = orderRepository.findAll();
        List<MerchandiseJpaEntity> allGoods = goodsRepository.findAll();


        ModelAndView modelAndView = new ModelAndView("dashboard-view");
        modelAndView.getModelMap().addAttribute("accounts", allAccounts);
        modelAndView.getModelMap().addAttribute("orders", allOrders);
        modelAndView.getModelMap().addAttribute("goods", allGoods);

        return modelAndView;
    }
}
