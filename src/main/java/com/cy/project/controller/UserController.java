package com.cy.project.controller;

import com.cy.project.entity.Users;
import com.cy.project.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String productById(@RequestParam(value="usersAccount")String usersAccount, @RequestParam(value="usersPwd")String usersPwd,
                              Model model, HttpSession session) {

        System.out.println(usersAccount);
        System.out.println(usersPwd);

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

        System.out.println("111");
        return "redirect:/";
    }

}
