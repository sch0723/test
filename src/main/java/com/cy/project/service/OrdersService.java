package com.cy.project.service;

import com.cy.project.entity.CartItem;
import com.cy.project.entity.Orders;
import com.cy.project.entity.OrdersDetail;
import com.cy.project.entity.Product;
import com.cy.project.repository.OrdersRepository;
import com.cy.project.repository.ProductRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrdersService {

    @Resource
    private RedisTemplate<String, CartItem> redisTemplate;

    private final OrdersRepository or;

    private final ProductRepository pr;

    public OrdersService(OrdersRepository ordersRepository, ProductRepository productRepository) {
        this.or = ordersRepository;
        this.pr = productRepository;
    }

    public Orders getOrdersDetailSet(String usersAccount,Integer[] arrayId){

        Orders orders=new Orders();
        Set<OrdersDetail> ordersDetailSet = new HashSet<>();


        int SumPrice=0;
        for (Integer id : arrayId) {
            Product product=pr.findById(id).orElse(null);
            HashOperations<String, Integer, CartItem> opsForHash = redisTemplate.opsForHash();

            int nums=opsForHash.get(usersAccount,id).getCount();

            OrdersDetail detail = new OrdersDetail();
            detail.setOrders(orders);
            detail.setProduct(product);
            detail.setOrdersDetailProductNums(nums);

            ordersDetailSet.add(detail);
            SumPrice+=product.getProductPrice()*nums;
        }
        orders.setOrdersState(0);
        orders.setOrdersOrdersDetail(ordersDetailSet);
        orders.setOrdersSumPrice(SumPrice);

        return orders;
    }
}
