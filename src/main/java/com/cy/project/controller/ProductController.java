package com.cy.project.controller;


import com.cy.project.entity.Product;
import com.cy.project.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


        return page;
    }

    @GetMapping(value = "/keys/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product>  productKeysPage(@PathVariable String sort, @PathVariable Integer pageIndex, @RequestParam(value="keywords[]")String[] keywords) {

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
        page = ps.findByKeywordsSortPage(keywords, sortType, sortBy, pageIndex-1);

        return page;
    }


    @GetMapping(value = "/getError")
    public String error(@PathVariable String category) {
        return "不存在";
    }
}
