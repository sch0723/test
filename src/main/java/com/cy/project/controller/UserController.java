package com.cy.project.controller;

import com.cy.project.service.UsersService;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

    @PostMapping (value = "/verifyUsers")
    public String productById(@RequestParam(value="usersAccount")String usersAccount, @RequestParam(value="usersPwd")String usersPwd, Model model) {

        System.out.println(usersAccount);
        System.out.println(usersPwd);

        if(us.findByUsersAccount(usersAccount)==null){
            model.addAttribute("msg","帳號不存在");
            return "login";
        }
        if(!us.findByUsersAccount(usersAccount).getUsersPwd().equals(usersPwd)){
            model.addAttribute("msg","密碼錯誤");
            return "login";
        }
        model.addAttribute("msg","ss");

        return "redirect:/";
    }

}
