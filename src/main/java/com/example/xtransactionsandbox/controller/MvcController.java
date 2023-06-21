package com.example.xtransactionsandbox.controller;

import com.example.xtransactionsandbox.dao.AccountDao;
import com.example.xtransactionsandbox.dao.OrderDao;
import com.example.xtransactionsandbox.dao.StockDao;
import com.example.xtransactionsandbox.entity.AccountEntity;
import com.example.xtransactionsandbox.entity.MerchandiseEntity;
import com.example.xtransactionsandbox.entity.OrderEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MvcController {

    private final AccountDao accountDao;
    private final OrderDao orderDao;
    private final StockDao stockDao;

    public MvcController(AccountDao accountDao, OrderDao orderDao, StockDao stockDao) {
        this.accountDao = accountDao;
        this.orderDao = orderDao;
        this.stockDao = stockDao;
    }

    @GetMapping("/dashboard")
    public ModelAndView getMerchandise() {
        List<AccountEntity> allAccounts = accountDao.getAll();
        List<OrderEntity> allOrders = orderDao.getAll();
        List<MerchandiseEntity> allGoods = stockDao.getAll();


        ModelAndView modelAndView = new ModelAndView("dashboard-view");
        modelAndView.getModelMap().addAttribute("accounts", allAccounts);
        modelAndView.getModelMap().addAttribute("orders", allOrders);
        modelAndView.getModelMap().addAttribute("goods", allGoods);

        return modelAndView;
    }
}
