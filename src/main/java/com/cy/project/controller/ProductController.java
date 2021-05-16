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

    /**
     * 進入商品頁面
     * @param productId 商品id
     * @param model
     * @return
     */
    @GetMapping(value = "/product/{productId}")
    public String productById(@PathVariable Integer productId,Model model) {

        Product product = ps.findByProductId(productId).orElse(null);

        model.addAttribute("product",product);

        return "shop-details";
    }

    /**
     * 所有商品目錄預設排序第一頁
     * @param model
     * @return
     */
    @GetMapping(value = "/product")
    public String index(Model model) {

        Page<Product> page = ps.findBySortPage("DEFAULT", 0);

        model.addAttribute("pageData",page);
        model.addAttribute("search","all");

        return "shop-grid";
    }

    /**
     * 分類商品目錄預設排序第一頁
     * @param category 分類類型
     * @param model
     * @return
     */
    @GetMapping(value = "/category/{category}")
    public String category(@PathVariable String category,Model model) {

        Page<Product> page = ps.findByCategorySortPage(category, "DEFAULT", 0);

        model.addAttribute("pageData",page);
        model.addAttribute("search",category);

        return "shop-grid";
    }

    /**
     * 搜索攔商品目錄預設排序第一頁
     * @param sort 排序
     * @param pageIndex 頁碼
     * @param keywords 搜索的關鍵字
     * @param model
     * @return
     */
    @GetMapping(value = "/keys")
    public String productToKeysPage(@RequestParam(value="sort", required = false)String sort,
                                    @RequestParam(value="page", required = false)Integer pageIndex,
                                    @RequestParam(value="keywords")String keywords,
                                    Model model) {

        if ("".equals(keywords)){
            return "redirect:/";
        }

        String[] strArr=keywords.split(" ");
        if(pageIndex==null){
            pageIndex=1;
        }

        Page<Product> page = ps.findByKeywordsSortPage(strArr, sort, pageIndex-1);
        model.addAttribute("pageData",page);
        model.addAttribute("search","keys");
        model.addAttribute("getKeys",keywords);

        return "shop-grid";
    }

    /**
     * 所有和分類商品排序分頁返回商品資料
     * @param productType 頁面為所有商品(all)或分類(分類類型)
     * @param sort 排序
     * @param pageIndex 頁碼
     * @return
     */
    @GetMapping(value = "/grid/{productType}/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product> productPage(@PathVariable String productType, @PathVariable String sort, @PathVariable Integer pageIndex) {

        Page<Product> page;
        if ("all".equals(productType)) {
            page = ps.findBySortPage(sort, pageIndex - 1);
        } else {
            page = ps.findByCategorySortPage(productType, sort, pageIndex - 1);
        }

        return page;
    }

    /**
     * 搜索攔排序分頁返回商品資料
     * @param sort 排序
     * @param pageIndex 頁碼
     * @param keywords 搜索的關鍵字
     * @return
     */
    @GetMapping(value = "/keys/{sort}/{pageIndex}")
    @ResponseBody
    public Page<Product>  productKeysPage(@PathVariable String sort, @PathVariable Integer pageIndex,String keywords) {

        String[] strArr=keywords.split(" ");

        return ps.findByKeywordsSortPage(strArr, sort, pageIndex-1);
    }

    //5XX
    @GetMapping(value = "/getError")
    public String error() {
        return "不存在";
    }
}
