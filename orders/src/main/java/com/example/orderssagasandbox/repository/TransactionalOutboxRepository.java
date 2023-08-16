package com.example.orderssagasandbox.repository;

import com.example.orderssagasandbox.entity.TransactionalOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionalOutboxRepository extends JpaRepository<TransactionalOutboxEntity, Long> {

    @Query(value = "select * from outbox out where out.delivery_status=?1", nativeQuery = true)
    List<TransactionalOutboxEntity> findAllByDeliveryStatus(String deliveryStatus);

}
