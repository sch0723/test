package com.cy.project.repository;

import com.cy.project.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    List<Orders> findOrdersByUsers_UsersAccount(String usersAccount);

    @Modifying
    @Query("UPDATE Orders o SET o.ordersState =:ordersState WHERE o.ordersId=:ordersId")
    int setOrdersState(int ordersState ,int ordersId);
}
