package com.cy.project.service;


import com.cy.project.entity.Product;
import com.cy.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class ProductTest {

    private final int PAGESIZE=12;

    @Autowired
    private ProductRepository pr;
    @Autowired
    private ProductService ps;

    @Test
    void test1(){

        int pageIndex=0;
        String order="DESC";
        String orderType="productNumsOfSale";
        String pros="鋼彈";

        Sort sort;
        if("DESC".equals(order)){
            sort = Sort.by(orderType).descending();
        }else {
            sort = Sort.by(orderType).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        Specification<Product> sp1= new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(criteriaBuilder.like(root.get("productName"),"%"+pros+"%"));
                predicates.add(criteriaBuilder.like(root.get("productName"),"%"+"不挑盒況"+"%"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Specification<Product> sp2= (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"),"%"+pros+"%");

        Page<Product> page=pr.findAll(sp1,pageable);

        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        for (Product p :page) {
            System.out.println(p);
        }


    }


    @org.junit.jupiter.api.Test
    void test2(){

        int pageIndex=0;
        String order="ASC";
        String orderType="productNumsOfSale";

        Sort sort;
        if("DESC".equals(order)){
            sort = Sort.by(orderType).descending();
        }else {
            sort = Sort.by(orderType).ascending();
        }

        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        Page<Product> page=pr.findAll(pageable);

        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        System.out.println("內容:"+page.getContent());
    }
//    @org.junit.jupiter.api.Test
//    void test3(){
//        Page<Product> page = ps.findByKeywordSortPage("海", "","productPrice", 2);
//
//        System.out.println("本頁筆數:"+page.getNumberOfElements());
//        System.out.println("每頁筆數:"+page.getSize());
//        System.out.println("全部筆數:"+page.getTotalElements());
//        System.out.println("全部頁數:"+page.getTotalPages());
//        System.out.println("內容:"+page.getContent());
//
//    }

    @org.junit.jupiter.api.Test
    void test4(){
        Page<Product> page = ps.findBySortPage("","productPrice", 1);

        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        System.out.println("內容:"+page.getContent());

    }

    @org.junit.jupiter.api.Test
    void test5(){
        Page<Product> page = ps.findByCategorySortPage("七龍珠","","productPrice", 1);

        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        System.out.println("內容:"+page.getContent());

    }

    @org.junit.jupiter.api.Test
    void test6(){
        String[] keywords={};
        Page<Product> page = ps.findByKeywordsSortPage(keywords,"","productPrice", 17);

        System.out.println("本頁筆數:"+page.getNumberOfElements());
        System.out.println("每頁筆數:"+page.getSize());
        System.out.println("全部筆數:"+page.getTotalElements());
        System.out.println("全部頁數:"+page.getTotalPages());
        System.out.println("內容:"+page.getContent());



    }

}
