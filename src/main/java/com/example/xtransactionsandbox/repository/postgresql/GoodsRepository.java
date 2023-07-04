package com.example.xtransactionsandbox.repository.postgresql;

import com.example.xtransactionsandbox.entity.jpa.postgresql.MerchandiseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<MerchandiseJpaEntity, Long> {
}
