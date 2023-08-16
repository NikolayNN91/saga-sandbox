package com.example.orderssagasandbox.service;

import com.example.orderssagasandbox.entity.OrderEntity;
import com.example.orderssagasandbox.entity.ParticipantEventEntity;
import com.example.orderssagasandbox.model.*;
import com.example.orderssagasandbox.repository.ParticipantEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParticipantEventHandler {

    private final ParticipantEventRepository participantEventRepository;
    private final OrderService orderService;

    public ParticipantEventHandler(ParticipantEventRepository participantEventRepository, OrderService orderService) {
        this.participantEventRepository = participantEventRepository;
        this.orderService = orderService;
    }

    @Transactional
    public void handleEvent(ParticipantReply reply, String messageId) {
        OrderEntity order = orderService.getOrder(reply.getOrderId());
        if (order.getStatus() != OrderStatus.APPROVAL_PENDING) {
            log.info("Order status is already not APPROVAL_PENDING");
            return;
        }

        if (reply.getCommand() == SagaCommand.REJECT_ORDER && reply.getStatus() == ParticipantReplyStatus.ROLLED_BACK) {
            log.info("Participant is rolled back successfully");
            return;
        }

        // idempotency
        Optional<ParticipantEventEntity> byMessageId = participantEventRepository.findByOrderId(reply.getOrderId())
                .filter(event -> reply.getType() == event.getReplyType()
                        && reply.getCommand() == event.getCommand()
                        && reply.getStatus() == event.getReplyStatus());
        if (byMessageId.isPresent()) {
            log.info("Duplicate response from participant {} on {} command", reply.getType(), reply.getCommand());
            return;
        }

        ParticipantEventEntity participantEventEntity = buildParticipantEventEntity(reply, messageId);

        if (participantEventEntity.getReplyStatus() == ParticipantReplyStatus.APPROVED) {
            log.info("Participant reply status is approved: {}", reply);
            participantEventRepository.findByOrderId(reply.getOrderId())
                    .ifPresent(e -> {
                        log.info("Order approving: {}", reply);
                        List<ParticipantEventEntity> eventEntities = List.of(participantEventEntity, e);
                        if (isOrderCompletedSuccessfully(eventEntities)) {
                            order.setStatus(OrderStatus.APPROVED);
                        }
                    });
        } else {
            log.info("Order rejection: {}", reply);
            orderService.rejectOrder(reply.getOrderId());
        }

        participantEventRepository.save(participantEventEntity);
    }

    private ParticipantEventEntity buildParticipantEventEntity(ParticipantReply reply, String messageId) {
        ParticipantEventEntity participantEventEntity = new ParticipantEventEntity();
        participantEventEntity.setOrderId(reply.getOrderId());
        participantEventEntity.setReplyStatus(reply.getStatus());
        participantEventEntity.setReplyType(reply.getType());
        participantEventEntity.setCommand(reply.getCommand());
        participantEventEntity.setMessageId(messageId);
        return participantEventEntity;
    }

    private boolean isOrderCompletedSuccessfully(List<ParticipantEventEntity> entities) {
        List<ReplyType> expectedEvents = List.of(ReplyType.ACCOUNTS, ReplyType.STOCK);
        List<ReplyType> collect = entities.stream()
                .filter(e -> e.getReplyStatus() == ParticipantReplyStatus.APPROVED)
                .map(ParticipantEventEntity::getReplyType)
                .collect(Collectors.toList());
        return new HashSet<>(collect).containsAll(expectedEvents);
    }
}
