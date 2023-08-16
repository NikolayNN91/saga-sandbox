package com.example.orderssagasandbox.entity;

import com.example.orderssagasandbox.model.ParticipantReplyStatus;
import com.example.orderssagasandbox.model.ReplyType;
import com.example.orderssagasandbox.model.SagaCommand;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "participant_events")
public class ParticipantEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "reply_type")
    private ReplyType replyType;
    @Column(name = "reply_status")
    private ParticipantReplyStatus replyStatus;
    private SagaCommand command;
    @Column(name = "message_id")
    private String messageId;

}
