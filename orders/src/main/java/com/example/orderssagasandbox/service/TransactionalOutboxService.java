package com.example.orderssagasandbox.service;

import com.example.orderssagasandbox.entity.TransactionalOutboxEntity;
import com.example.orderssagasandbox.repository.TransactionalOutboxRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionalOutboxService {

    private final TransactionalOutboxRepository transactionalOutboxRepository;

    public TransactionalOutboxService(TransactionalOutboxRepository transactionalOutboxRepository) {
        this.transactionalOutboxRepository = transactionalOutboxRepository;
    }

    public void save(TransactionalOutboxEntity entity) {
        transactionalOutboxRepository.save(entity);
    }
}
