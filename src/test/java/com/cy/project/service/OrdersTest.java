package com.cy.project.service;

import com.cy.project.entity.Orders;
import com.cy.project.entity.OrdersDetail;
import com.cy.project.entity.Users;
import com.cy.project.repository.OrdersDetailRepository;
import com.cy.project.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
//        Users users = us.findByUsersAccount("abc123");

//        Integer[] arrayid={1,2,3};
//
//        Orders orders = os.getOrdersDetailSet("abc123", arrayid);
//        orders.setUsers(users);
//        orders.setOrdersBuyDate(new Date());
//        orders.setOrdersPhone("12345");
//        orders.setOrdersContactName("12345");
//        or.save(orders);


////        users.getUsersOrders().remove(1);
        Users abc123 = us.findByUsersAccount("abc123");
        List<Orders> usersOrders = abc123.getUsersOrders();
        Iterator<Orders> ordersIterator = usersOrders.iterator();

        Orders orders = or.findById(41).orElse(null);
//        or.deleteById(24);
        Set<OrdersDetail> detail = orders.getOrdersOrdersDetail();
//
        or.save(orders);
//        List<OrdersDetail> ordersDetailByOrders_ordersId = odr.findOrdersDetailByOrders_OrdersId(29);

//        odr.deleteAll(ordersDetailByOrders_ordersId);


        Iterator<OrdersDetail> iterator = detail.iterator();
        System.out.println(detail.toString());
        while (iterator.hasNext()){
            OrdersDetail next = iterator.next();
            iterator.remove();
//            or.delete(next);
        }
//        for (OrdersDetail d: detail) {
//            detail.remove(d);
//            odr.delete(d);
////            odr.saveAndFlush(d);
//        }



    }

    @Test
    public void test2() {

        String s = os.genAioCheckOutALL(42);
        System.out.println(s);
//        os.confirmPay(42);

    }
}
