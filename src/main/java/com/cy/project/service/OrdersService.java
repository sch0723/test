package com.cy.project.service;

import com.cy.project.entity.CartItem;
import com.cy.project.entity.Orders;
import com.cy.project.entity.OrdersDetail;
import com.cy.project.entity.Product;
import com.cy.project.repository.OrdersRepository;
import com.cy.project.repository.ProductRepository;


import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
//@Transactional(readOnly = true)
public class OrdersService {

    @Value("${ecpay.returnURL}")
    private String returnURL;
    @Value("${ecpay.clientBackURL}")
    private String clientBackURL;

    @Resource
    private RedisTemplate<String, CartItem> redisTemplate;

    private final OrdersRepository or;

    private final ProductRepository pr;

    public OrdersService(OrdersRepository ordersRepository, ProductRepository productRepository) {
        this.or = ordersRepository;
        this.pr = productRepository;
    }

    //儲存訂單
//    @Transactional(readOnly = false)
    public Orders save(Orders orders){
        return or.save(orders);
    }

    //使用者所有訂單
    public List<Orders> findOrdersByUsers_UsersAccount(String usersAccount){
        return or.findOrdersByUsers_UsersAccount(usersAccount);
    }

    //初始化訂單(設定訂單商品明細)
    public Orders getInitOrders(String usersAccount,Integer[] arrayId){

        Orders orders=new Orders();
        Set<OrdersDetail> ordersDetailSet = new HashSet<>();


        int SumPrice=0;
        for (Integer id : arrayId) {
            Product product=pr.findById(id).orElse(null);
            HashOperations<String, String, CartItem> opsForHash = redisTemplate.opsForHash();

            int nums=opsForHash.get(usersAccount,String.valueOf(id)).getCount();
            int Price=product.getProductPrice()*nums;

            OrdersDetail detail = new OrdersDetail();
            detail.setOrders(orders);
            detail.setProduct(product);
            detail.setOrdersDetailProductNums(nums);
            detail.setOrdersDetailTotalPrice(Price);
            ordersDetailSet.add(detail);
            SumPrice+=Price;
        }
        orders.setOrdersState(0);
        orders.setOrdersOrdersDetail(ordersDetailSet);
        orders.setOrdersSumPrice(SumPrice);

        return orders;
    }

    //生成綠界表單
    @Transactional
    public String genAioCheckOutALL(Integer id){
        AllInOne all = new AllInOne("");

        AioCheckOutALL obj = new AioCheckOutALL();

        Orders order = or.findById(id).orElse(null);

        if (order.getOrdersState()!=0){
            return "";
        }

        obj.setMerchantTradeNo("t"+new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + order.getOrdersId());
        obj.setMerchantTradeDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(order.getOrdersBuyDate()));
        obj.setTotalAmount("" + order.getOrdersSumPrice());
        obj.setTradeDesc("test Description");

        int itemNums=0;
        Set<OrdersDetail> ordersOrdersDetail = order.getOrdersOrdersDetail();
        for (OrdersDetail detailed : ordersOrdersDetail) {
            itemNums+=detailed.getOrdersDetailProductNums();
        }
        System.out.println(itemNums);

        obj.setItemName("玩具X"+itemNums);
        obj.setReturnURL(returnURL);
        obj.setClientBackURL(clientBackURL);
        obj.setNeedExtraPaidInfo("N");
        String form = all.aioCheckOut(obj, null);
        return form;
    }

    //更改訂單狀態為已結帳
    @Transactional
    public Orders confirmPay(int id) {

        Orders order = or.findById(id).orElse(null);
        order.setOrdersState(1);
//        order = or.save(order);

        return order;
    }

    //檢查CheckMacValue
    public boolean compareCheckMacValue(String checkMacValue) {
        System.out.println(checkMacValue);
        AllInOne all = new AllInOne("");
        Hashtable<String, String> dict = new Hashtable<String, String>();
        dict.put("MerchantID", "2000132");
        dict.put("CheckMacValue", checkMacValue);

        return all.compareCheckMacValue(dict);
    }
}
