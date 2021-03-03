package com.cy.project.service;

import com.cy.project.repository.OrdersRepository;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    private final OrdersRepository or;

    public OrdersService(OrdersRepository ordersRepository) {
        this.or = ordersRepository;
    }
}
