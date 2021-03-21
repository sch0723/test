package com.cy.project.controller;


import com.cy.project.entity.CartItem;
import com.cy.project.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class CartController {

    private final CartService cs;

    public CartController(CartService cartService){
        this.cs=cartService;
    }

    //購物車頁面
    @GetMapping(value = "/cart")
    public String cart(HttpSession session, Model model) {

        String usersAccount=(String) session.getAttribute("users");
        List<CartItem> cartItemList = cs.getCart(usersAccount);
        Map<String, Integer> map=cs.getNumsAndPrice(usersAccount);

        model.addAttribute("cart",cartItemList);
        model.addAttribute("totalPrice",map.get("totalPrice"));

        return "shopping-cart";
    }

    //計算購物車商品總數和總金額(登入未登入皆可)
    @GetMapping(value = "/getCartNumsAndPrice")
    @ResponseBody
    public Map<String,Integer> getNumsAndPrice(HttpSession session, @CookieValue(value = "myUUID",required = false)String myUUID){

        String usersAccount = (String)session.getAttribute("users");

        Map<String, Integer> map=null;

        if(usersAccount!=null){
            map = cs.getNumsAndPrice(usersAccount);
        }else if (myUUID!=null){
            String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
            if (!myUUID.matches(regex)) {
                return null;
            }
            map = cs.getNumsAndPrice(myUUID);
        }
        return map;
    }

    //添加某數量商品進購物車(登入未登入皆可)返回購物車商品總數和總金額供頁面更新
    @PostMapping(value = "/cart/{id}/{nums}")
    @ResponseBody
    public Map<String,Integer> addToCart(HttpSession session, HttpServletResponse response,
                                         @PathVariable Integer id, @PathVariable Integer nums,
                                         @CookieValue(value = "myUUID",required = false)String myUUID){

        String usersAccount = (String)session.getAttribute("users");

        Map<String, Integer> map;

        //登入
        if(usersAccount!=null){
            map = cs.addToCartForUsersAccount(usersAccount, id, nums);

        //未登入有UUID
        }else if (myUUID!=null){
            //檢查cookie中UUID格式
            String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
            if (!myUUID.matches(regex)) {
                return null;
            }
            map = cs.addToCartForUUID(myUUID, id, nums);

        //未登入也沒有UUID,產生UUID存cookie
        }else {
            myUUID= UUID.randomUUID().toString();
            Cookie cookie = new Cookie("myUUID", myUUID);
            cookie.setPath("/");
            response.addCookie(cookie);

            map = cs.addToCartForUUID(myUUID, id, nums);
        }

        return map;
    }

    //更新數量(登入only)返回購物車商品總數和總金額供頁面更新
    @PutMapping(value = "/cart/{id}/{nums}")
    @ResponseBody
    public Map<String,Integer> updateNums(HttpSession session, @PathVariable Integer id, @PathVariable Integer nums){

        String usersAccount = (String)session.getAttribute("users");

        return cs.updateNumsToCart(usersAccount, id, nums);
    }

    //刪除(登入only)返回購物車商品總數和總金額供頁面更新
    @DeleteMapping(value = "/cart/{id}")
    @ResponseBody
    public Map<String,Integer> deleteProduct(HttpSession session, @PathVariable Integer id){

        String usersAccount = (String)session.getAttribute("users");

        return cs.deleteProductToCart(usersAccount, id);
    }
}
