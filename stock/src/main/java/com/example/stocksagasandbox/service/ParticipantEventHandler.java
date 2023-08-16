package com.example.stocksagasandbox.service;

import com.example.stocksagasandbox.entity.TransactionalOutboxEntity;
import com.example.stocksagasandbox.exception.BadRequestException;
import com.example.stocksagasandbox.exception.MerchandiseNotExistException;
import com.example.stocksagasandbox.model.DeliveryStatus;
import com.example.stocksagasandbox.model.OrderSagaCommand;
import com.example.stocksagasandbox.model.ParticipantReplyStatus;
import com.example.stocksagasandbox.model.SagaCommand;
import com.example.stocksagasandbox.repository.TransactionalOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ParticipantEventHandler {

    private final StockService stockService;
    private final TransactionalOutboxRepository transactionalOutboxRepository;

    public ParticipantEventHandler(StockService stockService, TransactionalOutboxRepository transactionalOutboxRepository) {
        this.stockService = stockService;
        this.transactionalOutboxRepository = transactionalOutboxRepository;
    }

    @Transactional
    public void handleOrderCommand(OrderSagaCommand command) {
        TransactionalOutboxEntity outboxEntity = buildTransactionalOutboxEntity(command);
        List<TransactionalOutboxEntity> outboxEntities = transactionalOutboxRepository.findAllByOrderId(command.getOrderId());
        if (outboxEntities.stream().anyMatch(c -> c.getCommand() == command.getCommand())) {
            log.info("Command duplicate {}, orderId={}.", command.getCommand(), command.getOrderId());
            return;
        }

        // порядок сообщений не влияет на исход
        if (outboxEntities.stream().anyMatch(c -> c.getReplyStatus() == ParticipantReplyStatus.ROLLED_BACK)) {
            log.info("Order is already rolled back, orderId={}.", command.getOrderId());
            return;
        }

        if (command.getCommand() == SagaCommand.CREATE_ORDER) {
            try {
                stockService.writeOffMerchandise(command.getOrder().getMerchandiseId(),
                        command.getOrder().getQuantity(), command.getOrder().getPrice());
                outboxEntity.setReplyStatus(ParticipantReplyStatus.APPROVED);
            } catch (MerchandiseNotExistException e) {
                log.info("Merchandise is absent on a stock: {}, orderId={}.", command.getOrder().getMerchandiseId(), command.getOrderId());
            } catch (BadRequestException e) {
                log.info("Incorrect price: merchandiseId={}, orderId={}.", command.getOrder().getMerchandiseId(), command.getOrderId());
            }
        } else {
            stockService.rollbackWriteOffMerchandise(command.getOrder().getMerchandiseId(), command.getOrder().getQuantity());
            outboxEntity.setReplyStatus(ParticipantReplyStatus.ROLLED_BACK);
        }
        transactionalOutboxRepository.save(outboxEntity);
    }

    private TransactionalOutboxEntity buildTransactionalOutboxEntity(OrderSagaCommand command) {
        TransactionalOutboxEntity outboxEntity = new TransactionalOutboxEntity();
        outboxEntity.setOrderId(command.getOrderId());
        outboxEntity.setDeliveryStatus(DeliveryStatus.PENDING);
        outboxEntity.setCommand(command.getCommand());
        outboxEntity.setReplyStatus(ParticipantReplyStatus.ROLLED_BACK);
        return outboxEntity;
    }
}
