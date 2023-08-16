package com.example.stocksagasandbox.scheduler;

import com.example.stocksagasandbox.model.DeliveryStatus;
import com.example.stocksagasandbox.model.ParticipantReply;
import com.example.stocksagasandbox.model.ReplyType;
import com.example.stocksagasandbox.repository.TransactionalOutboxRepository;
import com.example.stocksagasandbox.service.ActiveMQService;
import com.example.stocksagasandbox.utils.Utils;
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
                    ParticipantReply reply = new ParticipantReply();
                    reply.setOrderId(outbox.getOrderId());
                    reply.setType(ReplyType.STOCK);
                    reply.setStatus(outbox.getReplyStatus());
                    reply.setCommand(outbox.getCommand());

                    String s = Utils.writeValueAsString(reply);
                    activeMQService.sendMessage(s);
                    log.info("Message sent: {}", s);
                    outbox.setDeliveryStatus(DeliveryStatus.DELIVERED);
                });
    }
}
