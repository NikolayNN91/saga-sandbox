package com.example.orderssagasandbox.entity;

import com.example.orderssagasandbox.model.SagaCommand;
import lombok.Getter;
import lombok.Setter;
import com.example.orderssagasandbox.model.DeliveryStatus;

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
    private SagaCommand command;
}
