package com.cy.project.repository;

import com.cy.project.entity.Cart;
import com.cy.project.entity.Product;
import com.cy.project.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUsersAndProduct(Users users, Product product);
}
