package com.cy.project.controller;

import com.cy.project.entity.Users;
import com.cy.project.service.CartService;
import com.cy.project.service.UsersService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


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

        //驗證成功
        //合併購物車
        if (myUUID != null) {
            cs.mergeCart(usersAccount,myUUID);
        }

        //清除未登入下儲存購物車的UUID
        Cookie cookie = new Cookie("myUUID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        //儲存登入狀態
        session.setAttribute("users", users.getUsersAccount());

        //如果有前往攔截前請求
        String preURI = (String)session.getAttribute("preURI");
        if (preURI != null) {
            return "redirect:" + preURI;
        }

        return "redirect:/";
    }


    @PostMapping("/googleVerify")
    @ResponseBody
    public String main(String id_token) {
        System.out.println(id_token);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("265118023405-67ju2c654opfpfui0r4861gh5q7vs950.apps.googleusercontent.com")).build();
        GoogleIdToken idToken = null;

        try {
            idToken = verifier.verify(id_token);
        } catch (GeneralSecurityException e) {
            System.out.println("GeneralSecurityException");
        } catch (IOException e) {
            System.out.println("IOException");
        }

        if (idToken != null) {
            System.out.println("Y");
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            System.out.println(email);

        } else {
            System.out.println("Invalid ID token.");

        }
        return "Y";
    }
}
