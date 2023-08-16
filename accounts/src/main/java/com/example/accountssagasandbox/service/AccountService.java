package com.example.accountssagasandbox.service;

import com.example.accountssagasandbox.entity.AccountEntity;
import com.example.accountssagasandbox.exception.AccountNotFoundException;
import com.example.accountssagasandbox.exception.NotEnoughMoneyException;
import com.example.accountssagasandbox.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void writeOffMoney(Long accountId, double amount) {
        AccountEntity account = accountRepository.findById(accountId).filter(a -> a.getAmount() >= amount).map(a -> {
            a.setAmount(a.getAmount() - amount);
            return a;
        }).orElseThrow(NotEnoughMoneyException::new);
        accountRepository.save(account);
    }

    public void rollbackWriteOffMoney(Long accountId, double amount) {
        AccountEntity account = accountRepository.findById(accountId)
                .map(a -> {
                    a.setAmount(a.getAmount() + amount);
                    return a;
                }).orElseThrow(NotEnoughMoneyException::new);
        accountRepository.save(account);
    }

    public AccountEntity getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public AccountEntity createAccount(AccountEntity account) {
        return accountRepository.save(account);
    }

    public AccountEntity updateAccount(AccountEntity account) {
        return accountRepository.save(account);
    }
}
