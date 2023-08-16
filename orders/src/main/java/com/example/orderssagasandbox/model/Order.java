package com.example.orderssagasandbox.model;

import com.example.orderssagasandbox.entity.OrderEntity;
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

    public static Order createFrom(OrderEntity entity) {
        Order order = new Order();
        order.setOrderId(entity.getId());
        order.setPrice(entity.getPrice());
        order.setQuantity(entity.getQuantity());
        order.setCustomerId(entity.getCustomerId());
        order.setMerchandiseId(entity.getMerchandiseId());
        return order;
    }
}
