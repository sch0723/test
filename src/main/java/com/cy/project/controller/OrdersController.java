package com.cy.project.controller;

import com.cy.project.entity.Orders;
import com.cy.project.entity.OrdersDetail;
import com.cy.project.service.CartService;
import com.cy.project.service.OrdersService;
import com.cy.project.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class OrdersController {

    private final OrdersService os;

    private final UsersService us;

    private final CartService cs;

    public OrdersController(OrdersService ordersService, UsersService usersService, CartService cartService) {
        this.os = ordersService;
        this.us = usersService;
        this.cs = cartService;
    }

    //產生初始化訂單(設定訂單商品明細放入session供儲存定單使用)進入check
    @GetMapping(value = "/check")
    public String checkout(@RequestParam(value = "product[]") Integer[] product, HttpSession session) {

        session.setAttribute("orders", os.getInitOrders((String) session.getAttribute("users"), product));

        return "checkout";
    }

    //使用者訂單目錄
    @GetMapping(value = "/myOrders")
    public String myOrders(HttpSession session, Model model){
        List<Orders> ordersList=os.findOrdersByUsers_UsersAccount((String) session.getAttribute("users"));

        model.addAttribute("ordersList",ordersList);
        return "orders-detail";
    }

    //設定訂單剩餘屬性並儲存
    @PostMapping(value = "/createOrders")
    public String createOrders(String name, String address, String phone, String email, HttpSession session) {
        String users = (String) session.getAttribute("users");

        //取出session中初始化定單
        Orders orders = (Orders)session.getAttribute("orders");
        orders.setUsers(us.findByUsersAccount(users));
        orders.setOrdersContactName(name);
        orders.setOrdersReceiveAddress(address);
        orders.setOrdersPhone(phone);
        orders.setOrdersEmail(email);
        orders.setOrdersBuyDate(new Date());

        orders = os.save(orders);

        //orderid加入redis監聽訂單過期
        os.orderStateExpiration("orders:"+orders.getOrdersId());

        //從購物車刪除已結帳商品
        List<Integer> list = new ArrayList<>();
        for (OrdersDetail od : orders.getOrdersOrdersDetail()) {
            list.add(od.getProduct().getProductId());
        }
        cs.deleteProducts(users,list);
        //訂單儲存後刪除session中初始化定單
        session.removeAttribute("orders");

        return "redirect:/myOrders";
    }

    //前往綠界結帳
    @PostMapping(path = "/toPay")
    @ResponseBody
    public String toPay(@RequestParam("orderId") Integer orderId) {

        return os.genAioCheckOutALL(orderId);
    }

    //接收綠界回傳交易結果,更新訂單狀態
    @PostMapping(path = "/confirmPay")
    @ResponseBody
    public String confirmPay(@RequestParam("RtnCode") Integer RtnCode,
                             @RequestParam("MerchantTradeNo") String MerchantTradeNo,
                             @RequestParam("CheckMacValue") String CheckMacValue) {

        boolean check = os.compareCheckMacValue(CheckMacValue);

        if (RtnCode == 1) {
            os.confirmPay(Integer.parseInt(MerchantTradeNo.substring(13)));
            return "1|OK";
        }
        return "";
    }
}
