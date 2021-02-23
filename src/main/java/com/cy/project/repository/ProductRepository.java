package com.cy.project.repository;

import com.cy.project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> , JpaSpecificationExecutor<Product> {

    Product findByProductId(Integer id);

    Page<Product> findByproductCategory(String productCategory, Pageable pageable);

    Product findByproductDate(Integer id);



}
