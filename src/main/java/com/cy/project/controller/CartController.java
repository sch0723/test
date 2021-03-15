package com.cy.project.controller;


import com.cy.project.entity.CartItem;
import com.cy.project.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CartController {

    private final CartService cs;

    public CartController(CartService cartService){
        this.cs=cartService;
    }

    @GetMapping(value = "/cart")
    public String cart(HttpSession session, Model model) {

        String usersAccount=(String) session.getAttribute("users");
        List<CartItem> cartItemList = cs.getCart(usersAccount);
        Map<String, Integer> map=cs.getNumsAndPrice(usersAccount);

        model.addAttribute("cart",cartItemList);
        model.addAttribute("totalPrice",map.get("totalPrice"));

        return "shopping-cart";
    }



    @GetMapping(value = "/getCartNumsAndPrice")
    @ResponseBody
    public Map<String,Integer> getNumsAndPrice(HttpSession session, @CookieValue(value = "myUUID",required = false)String myUUID){

        String usersAccount = (String)session.getAttribute("users");

        Map<String, Integer> map=null;

        if(usersAccount!=null){
            map = cs.getNumsAndPrice(usersAccount);
        }else if (myUUID!=null){
            map = cs.getNumsAndPrice(myUUID);
        }
        return map;
    }

    @PostMapping(value = "/cart/{id}/{nums}")
    @ResponseBody
    public Map<String,Integer> addToCart(HttpSession session, HttpServletResponse response,
                                         @PathVariable Integer id, @PathVariable Integer nums,
                                         @CookieValue(value = "myUUID",required = false)String myUUID){

        String usersAccount = (String)session.getAttribute("users");

        Map<String, Integer> map;

        if(usersAccount!=null){
            map = cs.addToCart(usersAccount, id, nums);
        }else if (myUUID!=null){
            map = cs.addToCart(myUUID, id, nums);
        }else {
            myUUID= UUID.randomUUID().toString();
            Cookie cookie = new Cookie("myUUID", myUUID);
            cookie.setPath("/");
            response.addCookie(cookie);

            map = cs.addToCart(myUUID, id, nums);
        }

        return map;
    }

    @PutMapping(value = "/cart/{id}/{nums}")
    @ResponseBody
    public Map<String,Integer> updateNums(HttpSession session, @PathVariable Integer id, @PathVariable Integer nums){

        String usersAccount = (String)session.getAttribute("users");

        return cs.updateNumsToCart(usersAccount, id, nums);
    }

    @DeleteMapping(value = "/cart/{id}")
    @ResponseBody
    public Map<String,Integer> deleteProduct(HttpSession session, @PathVariable Integer id){

        String usersAccount = (String)session.getAttribute("users");

        return cs.deleteProductToCart(usersAccount, id);
    }
}
