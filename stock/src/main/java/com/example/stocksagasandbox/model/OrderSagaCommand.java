package com.example.stocksagasandbox.model;

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

    @Override
    public String toString() {
        return "OrderSagaCommand{" +
                "orderId=" + orderId +
                ", orderCommand=" + command +
                ", order=" + order +
                '}';
    }
}
