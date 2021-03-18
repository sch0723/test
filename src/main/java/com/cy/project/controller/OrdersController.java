package com.cy.project.controller;

import com.cy.project.entity.Orders;
import com.cy.project.entity.OrdersDetail;
import com.cy.project.service.CartService;
import com.cy.project.service.OrdersService;
import com.cy.project.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

@Controller
public class OrdersController {

    private final OrdersService os;

    private final UsersService us;

    private final CartService cs;

    public OrdersController(OrdersService ordersService, UsersService usersService, CartService cartService) {
        this.os = ordersService;
        this.us = usersService;
        this.cs = cartService;
    }

    @GetMapping(value = "/check")
    public String checkout(@RequestParam(value = "product[]") Integer[] product, HttpSession session) {

        session.setAttribute("orders", os.getInitOrders((String) session.getAttribute("users"), product));

        return "checkout";
    }

    @GetMapping(value = "/myOrders")
    public String myOrders(HttpSession session, Model model){
        List<Orders> ordersList=os.findOrdersByUsers_UsersAccount((String) session.getAttribute("users"));

        model.addAttribute("ordersList",ordersList);
        return "orders-detail";
    }

    @PostMapping(value = "/createOrders")
    public String createOrders(String name, String address, String phone, String email, HttpSession session) {
        String users = (String) session.getAttribute("users");

        Orders orders = (Orders)session.getAttribute("orders");

        orders.setUsers(us.findByUsersAccount(users));
        orders.setOrdersContactName(name);
        orders.setOrdersReceiveAddress(address);
        orders.setOrdersPhone(phone);
        orders.setOrdersEmail(email);
        orders.setOrdersBuyDate(new Date());

        os.save(orders);

        List<Integer> list = new ArrayList<>();
        for (OrdersDetail od : orders.getOrdersOrdersDetail()) {
            list.add(od.getProduct().getProductId());
        }
        cs.deleteProducts(users,list);

        session.removeAttribute("orders");

        return "redirect:/myOrders";
    }
}
