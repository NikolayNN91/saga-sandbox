package com.example.accountssagasandbox.entity;

import com.example.accountssagasandbox.model.DeliveryStatus;
import com.example.accountssagasandbox.model.ParticipantReplyStatus;
import com.example.accountssagasandbox.model.SagaCommand;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "outbox")
public class TransactionalOutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;
    @Column(name = "command")
    private SagaCommand command;
    @Column(name = "reply_status")
    private ParticipantReplyStatus replyStatus;
}
