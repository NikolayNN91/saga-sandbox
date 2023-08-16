package com.example.orderssagasandbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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
