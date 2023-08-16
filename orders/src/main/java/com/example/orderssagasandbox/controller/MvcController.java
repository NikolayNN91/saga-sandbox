package com.example.orderssagasandbox.controller;

import com.example.orderssagasandbox.entity.OrderEntity;
import com.example.orderssagasandbox.entity.ParticipantEventEntity;
import com.example.orderssagasandbox.entity.TransactionalOutboxEntity;
import com.example.orderssagasandbox.repository.OrderRepository;
import com.example.orderssagasandbox.repository.ParticipantEventRepository;
import com.example.orderssagasandbox.repository.TransactionalOutboxRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MvcController {

    private final OrderRepository orderRepository;
    private final TransactionalOutboxRepository transactionalOutboxRepository;
    private final ParticipantEventRepository participantEventRepository;


    public MvcController(OrderRepository orderRepository, TransactionalOutboxRepository transactionalOutboxRepository, ParticipantEventRepository participantEventRepository) {
        this.orderRepository = orderRepository;
        this.transactionalOutboxRepository = transactionalOutboxRepository;
        this.participantEventRepository = participantEventRepository;
    }

    @GetMapping("/dashboard")
    public ModelAndView getMerchandise() {
        List<OrderEntity> allOrders = orderRepository.findAll();
        List<TransactionalOutboxEntity> allOutbox = transactionalOutboxRepository.findAll();
        List<ParticipantEventEntity> participantEvents = participantEventRepository.findAll();

        ModelAndView modelAndView = new ModelAndView("dashboard-view");
        modelAndView.getModelMap().addAttribute("orders", allOrders);
        modelAndView.getModelMap().addAttribute("outboxes", allOutbox);
        modelAndView.getModelMap().addAttribute("events", participantEvents);

        return modelAndView;
    }
}
