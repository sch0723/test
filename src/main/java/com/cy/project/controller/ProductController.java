package com.cy.project.controller;


import com.cy.project.entity.Product;
import com.cy.project.service.ProductService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Transactional
public class ProductController {

    private final ProductService ps;
    public ProductController(ProductService productService){this.ps=productService;}

    @GetMapping(value = {"/","all"})
    public String productAllPage(){

        Page<Product> page = ps.findBySortPage("DESC", "productPrice", 2);

        System.out.println("本頁頁碼:"+page.getNumber());
        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        System.out.println(page.get());
        System.out.println(page.getPageable());
        System.out.println(page.getSort());
        for (Product p :page) {
            System.out.println(p);
        }

        return "shop-grid";
    }

    @GetMapping(value = "/category")
    public String productCategoryPage(){

        Page<Product> page = ps.findByCategorySortPage("七龍珠","","productPrice", 1);


        System.out.println("本頁頁碼:"+page.getNumber());
        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        System.out.println(page.get());
        System.out.println(page.getPageable());
        System.out.println(page.getSort());


        for (Product p :page) {
            System.out.println(p);
        }

        return "shop-grid";
    }
}
