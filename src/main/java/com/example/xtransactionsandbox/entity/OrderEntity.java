package com.example.xtransactionsandbox.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class OrderEntity {

    @Id
    private Long id;
    private Long customerId;
    private Long sellerId;
    private Long merchandiseId;
    private int quantity;
}
