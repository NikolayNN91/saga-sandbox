package com.example.accountssagasandbox.controller;

import com.example.accountssagasandbox.entity.AccountEntity;
import com.example.accountssagasandbox.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    private final AccountService accountService;

    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

//    @PostMapping("/buy/{orderId}")
//    public void buyGoods(@PathVariable Long orderId) {
//        purchaseOfGoodsService.checkout(orderId);
//    }

    @PutMapping("/account")
    public AccountEntity createAccount(@RequestBody AccountEntity account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/account")
    public AccountEntity updateAccount(@RequestBody AccountEntity account) {
        return accountService.updateAccount(account);
    }

    @GetMapping("/account/{accountId}")
    public AccountEntity getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

}
