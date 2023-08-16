package com.example.stocksagasandbox.controller;

import com.example.stocksagasandbox.entity.MerchandiseEntity;
import com.example.stocksagasandbox.entity.TransactionalOutboxEntity;
import com.example.stocksagasandbox.repository.GoodsRepository;
import com.example.stocksagasandbox.repository.TransactionalOutboxRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MvcController {

    private final GoodsRepository goodsRepository;
    private final TransactionalOutboxRepository transactionalOutboxRepository;

    public MvcController(GoodsRepository goodsRepository, TransactionalOutboxRepository transactionalOutboxRepository) {
        this.goodsRepository = goodsRepository;
        this.transactionalOutboxRepository = transactionalOutboxRepository;
    }

    @GetMapping("/dashboard/goods")
    public ModelAndView getMerchandise() {
        List<MerchandiseEntity> allGoods = goodsRepository.findAll();
        List<TransactionalOutboxEntity> allOutbox = transactionalOutboxRepository.findAll();

        ModelAndView modelAndView = new ModelAndView("dashboard-view");
        modelAndView.getModelMap().addAttribute("goods", allGoods);
        modelAndView.getModelMap().addAttribute("outboxes", allOutbox);

        return modelAndView;
    }
}
