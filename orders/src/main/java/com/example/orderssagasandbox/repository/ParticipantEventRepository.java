package com.example.orderssagasandbox.repository;

import com.example.orderssagasandbox.entity.ParticipantEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantEventRepository  extends JpaRepository<ParticipantEventEntity, Long> {
    Optional<ParticipantEventEntity> findByOrderId(Long orderId);
}
