package com.example.stocksagasandbox.controller;

import com.example.stocksagasandbox.entity.MerchandiseEntity;
import com.example.stocksagasandbox.service.StockService;
import org.springframework.web.bind.annotation.*;

@RestController
public class GoodsController {

    private final StockService stockService;

    public GoodsController(StockService stockService) {
        this.stockService = stockService;
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
