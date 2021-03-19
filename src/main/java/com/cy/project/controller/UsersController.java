package com.cy.project.controller;

import com.cy.project.entity.Users;
import com.cy.project.service.CartService;
import com.cy.project.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class UsersController {

    private final UsersService us;
    private final CartService cs;

    public UsersController(UsersService usersService, CartService cartService) {
        this.us = usersService;
        this.cs = cartService;
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        return "login";
    }

    @GetMapping(value = "/logout")
    public String toLogout(HttpSession session) {
        session.removeAttribute("users");
        return "login";
    }


    //登入驗證
    @PostMapping(value = "/verifyUsers")
    public String verifyUsers(String usersAccount, String usersPwd, Model model,
                              HttpSession session, HttpServletResponse response,
                              @CookieValue(value = "myUUID", required = false) String myUUID) {


        Users users = us.findByUsersAccount(usersAccount);
        if (users == null) {
            model.addAttribute("msg", "帳號不存在");
            return "login";
        }
        if (!users.getUsersPwd().equals(usersPwd)) {
            model.addAttribute("msg", "密碼錯誤");
            return "login";
        }

        //合併購物車
        if (myUUID != null) {
            cs.mergeCart(usersAccount,myUUID);
        }

        //清除UUID
        Cookie cookie = new Cookie("myUUID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        //儲存登入狀態
        session.setAttribute("users", users.getUsersAccount());

        //前往攔截前請求
        String preURI = (String)session.getAttribute("preURI");
        if (preURI != null) {
            return "redirect:" + preURI;
        }

        return "redirect:/";
    }
}
