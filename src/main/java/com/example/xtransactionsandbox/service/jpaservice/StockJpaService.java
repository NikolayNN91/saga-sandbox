package com.example.xtransactionsandbox.service.jpaservice;

import com.example.xtransactionsandbox.entity.jpa.postgresql.MerchandiseJpaEntity;
import com.example.xtransactionsandbox.exception.MerchandiseNotExistException;
import com.example.xtransactionsandbox.repository.postgresql.GoodsRepository;
import org.springframework.stereotype.Service;

@Service
public class StockJpaService {
    private final GoodsRepository goodsRepository;

    public StockJpaService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public void writeOffMerchandise(Long merchandiseId, int amount) {
        MerchandiseJpaEntity merchandiseEntity = goodsRepository.findById(merchandiseId)
                .filter(m -> m.getQuantity() >= amount).map(m -> {
                    m.setQuantity(m.getQuantity() - amount);
                    return m;
                }).orElseThrow(MerchandiseNotExistException::new);
        goodsRepository.save(merchandiseEntity);
    }

    public MerchandiseJpaEntity createMerchandise(MerchandiseJpaEntity merchandise) {
        return goodsRepository.save(merchandise);
    }

    public MerchandiseJpaEntity getMerchandise(Long merchandiseId) {
        return goodsRepository.findById(merchandiseId).orElseThrow(MerchandiseNotExistException::new);
    }
}
