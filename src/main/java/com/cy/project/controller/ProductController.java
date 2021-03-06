package com.cy.project.controller;


import com.cy.project.entity.Product;
import com.cy.project.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class ProductController {

    private final ProductService ps;

    public ProductController(ProductService productService) {
        this.ps = productService;
    }

    @GetMapping(value = "/")
    public String index() {
        return "shop-grid";
    }

    @GetMapping(value = "/grid/{productType}/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product> productPage(@PathVariable String productType, @PathVariable String sort, @PathVariable Integer pageIndex) {

        String sortType = "ASC";
        String sortBy = "productId";
        if ("PriceDESC".equals(sort)) {
            sortType = "DESC";
            sortBy = "productPrice";

        } else if ("PriceASC".equals(sort)) {
            sortType = "ASC";
            sortBy = "productPrice";
        }

        Page<Product> page;
        if ("all".equals(productType)) {
            page = ps.findBySortPage(sortType, sortBy, pageIndex - 1);
        } else {
            page = ps.findByCategorySortPage(productType, sortType, sortBy, pageIndex - 1);
        }

        for (Product p : page) {
            System.out.println(p);
        }
        System.out.println("都走這裡");

        return page;
    }

    @GetMapping(value = "/keys/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product>  productKeysPage(@PathVariable String sort, @PathVariable Integer pageIndex, @RequestAttribute String[] arr) {


        System.out.println(arr);

        String sortType = "ASC";
        String sortBy = "productId";
        if ("PriceDESC".equals(sort)) {
            sortType = "DESC";
            sortBy = "productPrice";

        } else if ("PriceASC".equals(sort)) {
            sortType = "ASC";
            sortBy = "productPrice";
        }

        Page<Product> page;
        page = ps.findByKeywordsSortPage(arr, sortType, sortBy, pageIndex-1);


        for (Product p : page) {
            System.out.println(p);
        }
        System.out.println("??");

        return page;
    }


    @GetMapping(value = "/getError")
    public String error(@PathVariable String category) {
        return "不存在";
    }
}
