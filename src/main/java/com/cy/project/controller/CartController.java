package com.cy.project.controller;


import com.cy.project.entity.Product;
import com.cy.project.service.CartService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    private final CartService cs;

    public CartController(CartService cartService){
        this.cs=cartService;
    }

    @GetMapping(value = "/cart")
    public String cart() {


        return "shopping-cart";
    }

    @GetMapping(value = "/check")
    public String checkout() {


        return "checkout";
    }
}
