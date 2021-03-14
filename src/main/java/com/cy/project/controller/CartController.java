package com.cy.project.controller;


import com.cy.project.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

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

    @GetMapping(value = "/getCartNumsAndPrice")
    @ResponseBody
    public Map<String,Integer> getNumsAndPrice(HttpSession session, @CookieValue(value = "myUUID",required = false)String myUUID){

        String usersAccount = (String)session.getAttribute("users");

        Map<String, Integer> map=null;

        if(usersAccount!=null){
            map = cs.getNumsAndPrice(usersAccount);
            System.out.println("usersAccount");
        }else if (myUUID!=null){
            map = cs.getNumsAndPrice(myUUID);
            System.out.println("old uuid:"+myUUID);
        }
        return map;
    }

    @GetMapping(value = "/addToCart/{id}/{nums}")
    @ResponseBody
    public Map<String,Integer> addToCart(HttpSession session, HttpServletResponse response,
                                         @PathVariable Integer id,
                                         @PathVariable Integer nums,
                                         @CookieValue(value = "myUUID",required = false)String myUUID){


        String usersAccount = (String)session.getAttribute("users");

        System.out.println(usersAccount+"??");

        Map<String, Integer> map;

        if(usersAccount!=null){
            map = cs.addToCart(usersAccount, id, nums);
            System.out.println("usersAccount");
        }else if (myUUID!=null){
            map = cs.addToCart(myUUID, id, nums);
            System.out.println("old uuid:"+myUUID);
        }else {
            myUUID= UUID.randomUUID().toString();
            Cookie cookie = new Cookie("myUUID", myUUID);
            cookie.setPath("/");
            response.addCookie(cookie);

            map = cs.addToCart(myUUID, id, nums);
            System.out.println("new uuid"+myUUID);
        }

        return map;
    }
}
