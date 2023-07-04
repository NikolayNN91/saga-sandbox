package com.example.xtransactionsandbox.repository.postgresql;

import com.example.xtransactionsandbox.entity.jpa.postgresql.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderJpaEntity, Long> {
}
