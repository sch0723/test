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

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

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

    /**
     * 產生初始化訂單(設定訂單商品明細放入session供儲存定單使用)進入check
     * @param product 生成初始化訂單需要的商品
     * @param session
     * @return
     */
    @GetMapping(value = "/check")
    public String checkout(@RequestParam(value = "product[]") Integer[] product, HttpSession session) {

        session.setAttribute("orders", os.getInitOrders((String) session.getAttribute("users"), product));
        return "checkout";
    }

    /**
     * 進入訂單目錄頁面
     * @param session
     * @param model
     * @return
     */
    @GetMapping(value = "/myOrders")
    public String myOrders(HttpSession session, Model model){

        List<Orders> ordersList=os.findOrdersByUsers_UsersAccount((String) session.getAttribute("users"));

        model.addAttribute("ordersList",ordersList);
        return "orders-detail";
    }

    /**
     * 設定訂單剩餘屬性並儲存
     * @param name 收件人姓名
     * @param address 收件人地址
     * @param phone 收件人電話
     * @param email 收件人email
     * @param session
     * @return
     */
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

        //ordersId加入redis監聽訂單過期
        //os.orderStateExpiration(orders.getOrdersId());

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

    /**
     * 前往綠界結帳
     * @param orderId 結帳訂單的id
     * @return
     */
    @PostMapping(path = "/toPay")
    @ResponseBody
    public String toPay(@RequestParam("orderId") Integer orderId) {

        return os.genAioCheckOutALL(orderId);
    }

    /**
     * 接收綠界回傳交易結果,更新訂單狀態
     * @param RtnCode 交易結果
     * @param MerchantTradeNo 訂單編號
     * @param CheckMacValue
     * @return
     */
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
