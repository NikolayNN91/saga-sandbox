package com.example.xtransactionsandbox.service.jpaservice;

import com.example.xtransactionsandbox.entity.jpa.mysql.AccountJpaEntity;
import com.example.xtransactionsandbox.exception.AccountNotFoundException;
import com.example.xtransactionsandbox.exception.NotEnoughMoneyException;
import com.example.xtransactionsandbox.repository.mysql.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountJpaService {

    private final AccountRepository accountRepository;

    public AccountJpaService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void writeOffMoney(Long accountId, double amount) {
        AccountJpaEntity account = accountRepository.findById(accountId).filter(a -> a.getAmount() >= amount).map(a -> {
            a.setAmount(a.getAmount() - amount);
            return a;
        }).orElseThrow(NotEnoughMoneyException::new);
        accountRepository.save(account);
    }

    public AccountJpaEntity getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public List<AccountJpaEntity> getAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    public AccountJpaEntity createAccount(AccountJpaEntity account) {
        return accountRepository.save(account);
    }

    public AccountJpaEntity updateAccount(AccountJpaEntity account) {
        return accountRepository.save(account);
    }
}
