package com.cy.project.controller;


import com.cy.project.entity.Product;
import com.cy.project.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class ProductController {

    private final ProductService ps;
    public ProductController(ProductService productService){this.ps=productService;}

    @GetMapping(value = "/")
    public String index(){
        return "shop-grid";
    }


    @GetMapping(value ="/grid/all")
    @ResponseBody
    public Page<Product> productAllPage(){

        Page<Product> page = ps.findBySortPage("ASC", "productId", 0);

        for (Product p :page) {
            System.out.println(p);
        }

        return page;
    }

    @GetMapping(value ="/grid/{productType}/{sort}")
    @ResponseBody
    public Page<Product> productSort(@PathVariable String productType,@PathVariable String sort){

        String sortType="ASC";
        String sortBy="productId";
        if("PriceDESC".equals(sort)){
            sortType="DESC";
            sortBy="productPrice";

        }else if ("PriceASC".equals(sort)){
            sortType="ASC";
            sortBy="productPrice";
        }

        Page<Product> page;
        if("all".equals(productType)){
            page = ps.findBySortPage(sortType, sortBy, 0);
        }else {
            page = ps.findByCategorySortPage(productType,sortType,sortBy, 0);
        }

        for (Product p :page) {
            System.out.println(p);
        }

        return page;
    }

    @GetMapping(value ="/grid/{productType}/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product> productPage(@PathVariable String productType,@PathVariable String sort,@PathVariable Integer pageIndex){

        String sortType="ASC";
        String sortBy="productId";
        if("PriceDESC".equals(sort)){
            sortType="DESC";
            sortBy="productPrice";

        }else if ("PriceASC".equals(sort)){
            sortType="ASC";
            sortBy="productPrice";
        }

        Page<Product> page;
        if("all".equals(productType)){
            page = ps.findBySortPage(sortType, sortBy, pageIndex-1);
        }else {
            page = ps.findByCategorySortPage(productType,sortType,sortBy, pageIndex-1);
        }

        for (Product p :page) {
            System.out.println(p);
        }

        return page;
    }



    @GetMapping(value = "/grid/category/{category}")
    @ResponseBody
    public Page<Product> productCategoryPage(@PathVariable String category){

        Page<Product> page = ps.findByCategorySortPage(category,"","productId", 0);

        for (Product p :page) {
            System.out.println(p);
        }

        return page;
    }
}
