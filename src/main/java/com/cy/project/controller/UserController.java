package com.cy.project.controller;

import com.cy.project.entity.Users;
import com.cy.project.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {
    private final UsersService us;

    public UserController(UsersService usersService){
        this.us=usersService;
    }

    @GetMapping(value = "/login")
    public String toLogin(){
        return "login";
    }

    @GetMapping(value = "/logout")
    public String toLogout(HttpSession session){
        session.removeAttribute("users");
        return "login";
    }


    @PostMapping (value = "/verifyUsers")
    public String productById(String usersAccount, String usersPwd, Model model, HttpSession session, @CookieValue(value = "preURI",required = false)String preURI) {


        Users users = us.findByUsersAccount(usersAccount);
        if(users==null){
            model.addAttribute("msg","帳號不存在");
            return "login";
        }
        if(!users.getUsersPwd().equals(usersPwd)){
            model.addAttribute("msg","密碼錯誤");
            return "login";
        }
        session.setAttribute("users",users);

        if ("/cart".equals(preURI)){
            return "redirect:/cart";
        }

        return "redirect:/";
    }

}
