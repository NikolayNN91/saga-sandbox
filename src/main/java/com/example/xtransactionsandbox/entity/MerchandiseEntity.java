package com.example.xtransactionsandbox.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class MerchandiseEntity {

    @Id
    private Long id;
    private String name;
    private Double price;
    private Long seller;
    private int quantity;
}
