package com.example.stocksagasandbox.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private Long orderId;
    private Long customerId;
    private Long merchandiseId;
    private Integer quantity;
    private Double price;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", merchandiseId=" + merchandiseId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
