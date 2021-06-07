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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OrdersService {

    @Value("${ecpay.returnURL}")
    private String returnURL;
    @Value("${ecpay.clientBackURL}")
    private String clientBackURL;

    @Resource
    private RedisTemplate<String, CartItem> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    private final OrdersRepository or;

    private final ProductRepository pr;

    public OrdersService(StringRedisTemplate stringRedisTemplate, OrdersRepository ordersRepository, ProductRepository productRepository) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.or = ordersRepository;
        this.pr = productRepository;
    }

    /**
     * 儲存訂單
     * @param orders 訂單
     * @return
     */
    @Transactional
    public Orders save(Orders orders){
        orders = or.save(orders);
        //ordersId加入redis監聽訂單過期
        orderStateExpiration(orders.getOrdersId());
        return orders;
    }

    /**
     * 使用者所有訂單
     * @param usersAccount 帳號
     * @return
     */
    public List<Orders> findOrdersByUsers_UsersAccount(String usersAccount){
        return or.findOrdersByUsers_UsersAccount(usersAccount);
    }

    /**
     * 初始化訂單(設定訂單商品明細)
     * @param usersAccount 帳號
     * @param productIdArray 初始化訂單需要的商品id
     * @return
     */
    public Orders getInitOrders(String usersAccount,Integer[] productIdArray){

        Orders orders=new Orders();
        Set<OrdersDetail> ordersDetailSet = new HashSet<>();


        int SumPrice=0;
        for (Integer id : productIdArray) {
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

    /**
     * 生成綠界表單
     * @param ordersId 訂單id
     * @return
     */
    @Transactional
    public String genAioCheckOutALL(Integer ordersId){
        AllInOne all = new AllInOne("");

        AioCheckOutALL obj = new AioCheckOutALL();

        Orders order = or.findById(ordersId).orElse(null);

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

    /**
     * 更改訂單狀態為已結帳
     * @param ordersId 訂單id
     * @return
     */
    @Transactional
    public Orders confirmPay(int ordersId) {

        Orders order = or.findById(ordersId).orElse(null);
        order.setOrdersState(1);
//        order = or.save(order);

        return order;
    }

    /**
     * 檢查CheckMacValue
     * @param checkMacValue
     * @return
     */
    public boolean compareCheckMacValue(String checkMacValue) {
        System.out.println(checkMacValue);
        AllInOne all = new AllInOne("");
        Hashtable<String, String> dict = new Hashtable<String, String>();
        dict.put("MerchantID", "2000132");
        dict.put("CheckMacValue", checkMacValue);

        return all.compareCheckMacValue(dict);
    }

    /**
     * 檢查訂單狀態未繳費設定為過期
     * @param ordersId 訂單id
     * @return
     */
    @Transactional
    public void checkOrdersExpiration(Integer ordersId){

        Orders orders = or.findById(ordersId).orElse(null);

        if (orders.getOrdersState()==0){
            or.setOrdersState(1, ordersId);
        }
    }

    /**
     * orderId儲存redis用以監聽過期
     * @param ordersId 訂單id
     */
    public void orderStateExpiration(Integer ordersId){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String message="orders:"+ordersId;
        stringValueOperations.append(message,"");
        stringRedisTemplate.expire(message,5, TimeUnit.MINUTES);
    }
}
