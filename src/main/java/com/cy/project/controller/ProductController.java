package com.cy.project.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping(value = {"/","all"})
    public String productPage(){
        return "shop-grid";
    }
}
