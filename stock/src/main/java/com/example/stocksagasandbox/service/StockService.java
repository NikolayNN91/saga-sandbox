package com.example.stocksagasandbox.service;

import com.example.stocksagasandbox.entity.MerchandiseEntity;
import com.example.stocksagasandbox.exception.BadRequestException;
import com.example.stocksagasandbox.exception.MerchandiseNotExistException;
import com.example.stocksagasandbox.repository.GoodsRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final GoodsRepository goodsRepository;

    public StockService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public void writeOffMerchandise(Long merchandiseId, int amount, Double price) {
        MerchandiseEntity merchandiseEntity = goodsRepository.findById(merchandiseId)
                .filter(m -> m.getQuantity() >= amount)
                .orElseThrow(MerchandiseNotExistException::new);
        if (!merchandiseEntity.getPrice().equals(price)) {
            throw new BadRequestException("Incorrect price");
        }
        merchandiseEntity.setQuantity(merchandiseEntity.getQuantity() - amount);
        goodsRepository.save(merchandiseEntity);
    }

    public void rollbackWriteOffMerchandise(Long merchandiseId, int amount) {
        MerchandiseEntity merchandiseEntity = goodsRepository.findById(merchandiseId)
                .map(m -> {
                    m.setQuantity(m.getQuantity() + amount);
                    return m;
                }).orElseThrow(MerchandiseNotExistException::new);
        goodsRepository.save(merchandiseEntity);
    }

    public MerchandiseEntity createMerchandise(MerchandiseEntity merchandise) {
        return goodsRepository.save(merchandise);
    }

    public MerchandiseEntity getMerchandise(Long merchandiseId) {
        return goodsRepository.findById(merchandiseId).orElseThrow(MerchandiseNotExistException::new);
    }
}
