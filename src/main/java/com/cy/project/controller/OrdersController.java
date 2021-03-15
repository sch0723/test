package com.cy.project.controller;

import com.cy.project.service.OrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrdersController {

    private final OrdersService os;

    public OrdersController(OrdersService ordersService) {
        this.os = ordersService;
    }

    @GetMapping(value = "/check")
    public String checkout(@RequestParam(value="product[]")Integer[] product) {

        for (Integer i :product) {
            System.out.println(i);
        }

        return "checkout";
    }
}
