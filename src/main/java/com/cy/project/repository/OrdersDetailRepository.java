package com.cy.project.repository;

import com.cy.project.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersDetailRepository  extends JpaRepository<OrdersDetail,Integer> {

    List<OrdersDetail> findOrdersDetailByOrders_OrdersId(Integer i);
    List<OrdersDetail> findOrdersDetailByOrders_OrdersIdAndProduct_ProductId(Integer i,Integer j);
}
