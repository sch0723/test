package com.cy.project.service;

import com.cy.project.entity.Orders;
import com.cy.project.entity.OrdersDetail;
import com.cy.project.entity.Users;
import com.cy.project.repository.OrdersDetailRepository;
import com.cy.project.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class OrdersTest {

    @Autowired
    private OrdersService os;

    @Autowired
    private UsersService us;

    @Autowired
    private OrdersRepository or;

    @Autowired
    private OrdersDetailRepository odr;

    @Test
    @Transactional
    @Rollback(value = false)
    public void test1(){
        Users users = us.findByUsersAccount("abc123");

//        Integer[] arrayid={1,2,3};
//
//        Orders orders = os.getOrdersDetailSet("abc123", arrayid);
//        orders.setUsers(users);
//        orders.setOrdersBuyDate(new Date());
//        orders.setOrdersPhone("12345");
//        orders.setOrdersContactName("12345");
//        or.save(orders);





    }
}
