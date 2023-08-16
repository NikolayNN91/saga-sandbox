package com.example.orderssagasandbox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSagaCommand {

    private Long orderId;
    private SagaCommand command;
    private Order order;
}
