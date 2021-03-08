package com.cy.project.controller;


import com.cy.project.entity.Product;
import com.cy.project.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ProductController {

    private final ProductService ps;

    public ProductController(ProductService productService) {
        this.ps = productService;
    }

    //商品頁面
    @GetMapping(value = "/product/{id}")
    public String productById(@PathVariable Integer id,Model model) {

        Product product = ps.findByProductId(id).orElse(null);

        model.addAttribute("product",product);

        return "shop-details";
    }

    //所有商品目錄
    @GetMapping(value = "/")
    public String index(Model model) {
        Page<Product> page = ps.findBySortPage("ASC", "productId", 0);

        model.addAttribute("pageData",page);
        model.addAttribute("search","all");

        return "shop-grid";
    }

    //分類商品目錄
    @GetMapping(value = "/category/{category}")
    public String category(@PathVariable String category,Model model) {
        Page<Product> page = ps.findByCategorySortPage(category, "ASC", "productId", 0);

        model.addAttribute("pageData",page);
        model.addAttribute("search",category);

        return "shop-grid";
    }

    //搜索攔商品目錄
    @GetMapping(value = "/keys")
    public String productToKeysPage(@RequestParam(value="keywords")String keywords,Model model) {

        if ("".equals(keywords)){
            return "redirect:/";
        }

        String[] strArr=keywords.split(" ");

        Page<Product> page = ps.findByKeywordsSortPage(strArr, "ASC", "productId", 0);

        model.addAttribute("pageData",page);
        model.addAttribute("search","keys");
        model.addAttribute("getKeys",keywords);

        return "shop-grid";
    }

    //所有和分類商品排序分頁
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

    //搜索攔排序分頁
    @GetMapping(value = "/keys/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product>  productKeysPage(@PathVariable String sort, @PathVariable Integer pageIndex,String keywords) {

        String[] strArr=keywords.split(" ");

        String sortType = "ASC";
        String sortBy = "productId";
        if ("PriceDESC".equals(sort)) {
            sortType = "DESC";
            sortBy = "productPrice";

        } else if ("PriceASC".equals(sort)) {
            sortType = "ASC";
            sortBy = "productPrice";
        }

        Page<Product> page = ps.findByKeywordsSortPage(strArr, sortType, sortBy, pageIndex-1);

        return page;
    }

    //5XX
    @GetMapping(value = "/getError")
    public String error() {
        return "不存在";
    }
}
