package com.example.xtransactionsandbox.service;

import com.example.xtransactionsandbox.dao.AccountDao;
import com.example.xtransactionsandbox.entity.AccountEntity;
import com.example.xtransactionsandbox.exception.AccountNotFoundException;
import com.example.xtransactionsandbox.exception.NotEnoughMoneyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void writeOffMoney(Long accountId, double amount) {
        AccountEntity account = accountDao.findById(accountId).filter(a -> a.getAmount() >= amount).map(a -> {
            a.setAmount(a.getAmount() - amount);
            return a;
        }).orElseThrow(NotEnoughMoneyException::new);
        accountDao.update(account);
    }

    public AccountEntity getAccount(Long accountId) {
        return accountDao.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public List<AccountEntity> getAccounts(Long userId) {
        return accountDao.findAllByUserId(userId);
    }

    public AccountEntity createAccount(AccountEntity account) {
        return accountDao.insert(account);
    }

    public AccountEntity updateAccount(AccountEntity account) {
        return accountDao.update(account);
    }
}
