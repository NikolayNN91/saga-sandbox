package com.example.stocksagasandbox.repository;

import com.example.stocksagasandbox.entity.MerchandiseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<MerchandiseEntity, Long> {
}
