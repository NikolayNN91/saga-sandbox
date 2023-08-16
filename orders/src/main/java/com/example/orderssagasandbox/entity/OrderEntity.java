package com.example.orderssagasandbox.entity;

import lombok.Getter;
import lombok.Setter;
import com.example.orderssagasandbox.model.OrderStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "seller_id")
    private Long sellerId;
    @Column(name = "merchandise_id")
    private Long merchandiseId;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "status")
    private OrderStatus status;
    private Double price;
}
