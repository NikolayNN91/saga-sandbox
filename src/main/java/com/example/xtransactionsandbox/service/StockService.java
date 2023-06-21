package com.example.xtransactionsandbox.service;

import com.example.xtransactionsandbox.dao.StockDao;
import com.example.xtransactionsandbox.entity.MerchandiseEntity;
import com.example.xtransactionsandbox.exception.MerchandiseNotExistException;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockDao stockDao;

    public StockService(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    public void writeOffMerchandise(Long merchandiseId, int amount) {
        MerchandiseEntity merchandiseEntity = stockDao.findById(merchandiseId)
                .filter(m -> m.getQuantity() >= amount).map(m -> {
                    m.setQuantity(m.getQuantity() - amount);
                    return m;
                }).orElseThrow(MerchandiseNotExistException::new);
        stockDao.update(merchandiseEntity);
    }

    public MerchandiseEntity createMerchandise(MerchandiseEntity merchandise) {
        return stockDao.insert(merchandise);
    }

    public MerchandiseEntity getMerchandise(Long merchandiseId) {
        return stockDao.findById(merchandiseId).orElseThrow(MerchandiseNotExistException::new);
    }
}
