package com.example.accountssagasandbox.service;

import com.example.accountssagasandbox.entity.TransactionalOutboxEntity;
import com.example.accountssagasandbox.exception.NotEnoughMoneyException;
import com.example.accountssagasandbox.model.DeliveryStatus;
import com.example.accountssagasandbox.model.OrderSagaCommand;
import com.example.accountssagasandbox.model.ParticipantReplyStatus;
import com.example.accountssagasandbox.model.SagaCommand;
import com.example.accountssagasandbox.repository.TransactionalOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ParticipantEventHandler {

    private final AccountService accountService;
    private final TransactionalOutboxRepository transactionalOutboxRepository;

    public ParticipantEventHandler(AccountService accountService, TransactionalOutboxRepository transactionalOutboxRepository) {
        this.accountService = accountService;
        this.transactionalOutboxRepository = transactionalOutboxRepository;
    }

    @Transactional
    public void handleOrderCommand(OrderSagaCommand command) {
        TransactionalOutboxEntity outboxEntity = buildTransactionalOutboxEntity(command);
        List<TransactionalOutboxEntity> outboxEntities = transactionalOutboxRepository.findAllByOrderId(command.getOrderId());
        if (outboxEntities.stream().anyMatch(c -> c.getCommand() == command.getCommand() || c.getReplyStatus() == ParticipantReplyStatus.ROLLED_BACK)) {
            return;
        }
        double amount = command.getOrder().getPrice() * command.getOrder().getQuantity();
        if (command.getCommand() == SagaCommand.CREATE_ORDER) {
            try {
                accountService.writeOffMoney(command.getOrder().getCustomerId(), amount);
                outboxEntity.setReplyStatus(ParticipantReplyStatus.APPROVED);
            } catch (NotEnoughMoneyException e) {
                log.info("Merchandise is absent on a stock: {}, orderId={}.", command.getOrder().getMerchandiseId(), command.getOrderId());
            }
        } else {
            accountService.rollbackWriteOffMoney(command.getOrder().getCustomerId(), amount);
            outboxEntity.setReplyStatus(ParticipantReplyStatus.ROLLED_BACK);
            log.info("Account rolled back: {}, orderId={}", command.getOrder().getCustomerId(), command.getOrderId());
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
