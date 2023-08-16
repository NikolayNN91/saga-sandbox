package com.example.accountssagasandbox.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantReply {

    private Long orderId;
    private ReplyType type;
    private ParticipantReplyStatus status;
    private SagaCommand command;

    @Override
    public String toString() {
        return "ParticipantReply{" +
                "orderId=" + orderId +
                ", type=" + type +
                ", status=" + status +
                ", command=" + command +
                '}';
    }

}
