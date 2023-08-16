package com.example.accountssagasandbox.scheduler;

import com.example.accountssagasandbox.model.DeliveryStatus;
import com.example.accountssagasandbox.model.ParticipantReply;
import com.example.accountssagasandbox.model.ReplyType;
import com.example.accountssagasandbox.repository.TransactionalOutboxRepository;
import com.example.accountssagasandbox.service.ActiveMQService;
import com.example.accountssagasandbox.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
public class TransactionalOutboxPollingScheduler {

    private final TransactionalOutboxRepository transactionalOutboxRepository;
    private final ActiveMQService activeMQService;

    public TransactionalOutboxPollingScheduler(TransactionalOutboxRepository transactionalOutboxRepository, ActiveMQService activeMQService) {
        this.transactionalOutboxRepository = transactionalOutboxRepository;
        this.activeMQService = activeMQService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void pollAndPublishEvents() {
        log.info("Polling ...");
        transactionalOutboxRepository
                .findAllByDeliveryStatus(String.valueOf(DeliveryStatus.PENDING.ordinal()))
                .forEach(outbox -> {
                    log.info("");
                    ParticipantReply participantReply = new ParticipantReply();
                    participantReply.setStatus(outbox.getReplyStatus());
                    participantReply.setType(ReplyType.ACCOUNTS);
                    participantReply.setCommand(outbox.getCommand());
                    participantReply.setOrderId(outbox.getOrderId());
//                    OrderSagaCommand orderSagaCommand = new OrderSagaCommand();
//                    orderSagaCommand.setOrderId(outbox.getOrderId());
//                    orderSagaCommand.setCommand(outbox.getCommand());
                    String s = Utils.writeValueAsString(participantReply);
                    activeMQService.sendMessage(s);
                    outbox.setDeliveryStatus(DeliveryStatus.DELIVERED);
                });
    }
}
