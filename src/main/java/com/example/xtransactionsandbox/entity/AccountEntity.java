package com.example.xtransactionsandbox.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class AccountEntity {

    @Id
    private Long id;
    private Long userId;
    private double amount;
}
