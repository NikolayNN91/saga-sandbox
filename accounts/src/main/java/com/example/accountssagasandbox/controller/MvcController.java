package com.example.accountssagasandbox.controller;

import com.example.accountssagasandbox.entity.AccountEntity;
import com.example.accountssagasandbox.entity.TransactionalOutboxEntity;
import com.example.accountssagasandbox.repository.AccountRepository;
import com.example.accountssagasandbox.repository.TransactionalOutboxRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MvcController {

    private final AccountRepository accountRepository;
    private final TransactionalOutboxRepository transactionalOutboxRepository;

    public MvcController(AccountRepository accountRepository, TransactionalOutboxRepository transactionalOutboxRepository) {
        this.accountRepository = accountRepository;
        this.transactionalOutboxRepository = transactionalOutboxRepository;
    }

    @GetMapping("/dashboard/accounts")
    public ModelAndView getMerchandise() {
        List<AccountEntity> allAccounts = accountRepository.findAll();
        List<TransactionalOutboxEntity> allOutbox = transactionalOutboxRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("dashboard-view");
        modelAndView.getModelMap().addAttribute("accounts", allAccounts);
        modelAndView.getModelMap().addAttribute("outboxes", allOutbox);
        return modelAndView;
    }
}
