package com.cy.project.service;


import com.cy.project.entity.Product;
import com.cy.project.enumeration.ProductSortEnum;
import com.cy.project.repository.ProductRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SpringBootTest
public class ProductTest {

    private final int PAGESIZE = 12;

    @Autowired
    private ProductRepository pr;
    @Autowired
    private ProductService ps;
    @Autowired
    private EntityManager entityManager;

    @Test
    void test1() {

        int pageIndex = 0;
        String order = "DESC";
        String orderType = "productNumsOfSale";
        String pros = "鋼彈";

        Sort sort;
        if ("DESC".equals(order)) {
            sort = Sort.by(orderType).descending();
        } else {
            sort = Sort.by(orderType).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        Specification<Product> sp1 = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + pros + "%"));
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + "不挑盒況" + "%"));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

//        Specification<Product> sp2 = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + pros + "%");

        Page<Product> page = pr.findAll(sp1, pageable);

        System.out.println("本頁筆數:" + page.getNumberOfElements());
        System.out.println("每頁筆數:" + page.getSize());
        System.out.println("全部筆數:" + page.getTotalElements());
        System.out.println("全部頁數:" + page.getTotalPages());
        for (Product p : page) {
            System.out.println(p);
        }


    }


    @Test
    void test2() {

        int pageIndex = 0;
        String order = "ASC";
        String orderType = "productNumsOfSale";

        Sort sort;
        if ("DESC".equals(order)) {
            sort = Sort.by(orderType).descending();
        } else {
            sort = Sort.by(orderType).ascending();
        }

        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        Page<Product> page = pr.findAll(pageable);

        System.out.println("本頁筆數:" + page.getNumberOfElements());
        System.out.println("每頁筆數:" + page.getSize());
        System.out.println("全部筆數:" + page.getTotalElements());
        System.out.println("全部頁數:" + page.getTotalPages());
        System.out.println("內容:" + page.getContent());
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
    void test4() {
        Page<Product> page = ps.findBySortPage( "productPrice", 1);

        System.out.println("本頁筆數:" + page.getNumberOfElements());
        System.out.println("每頁筆數:" + page.getSize());
        System.out.println("全部筆數:" + page.getTotalElements());
        System.out.println("全部頁數:" + page.getTotalPages());
        System.out.println("內容:" + page.getContent());

    }

    @org.junit.jupiter.api.Test
    void test5() {
        Page<Product> page = ps.findByCategorySortPage("七龍珠", "productPrice", 1);

        System.out.println("本頁筆數:" + page.getNumberOfElements());
        System.out.println("每頁筆數:" + page.getSize());
        System.out.println("全部筆數:" + page.getTotalElements());
        System.out.println("全部頁數:" + page.getTotalPages());
        System.out.println("內容:" + page.getContent());

    }

    @org.junit.jupiter.api.Test
    void test6() {
        String[] keywords = {};
        Page<Product> page = ps.findByKeywordsSortPage(keywords, "productPrice", 17);

        System.out.println("本頁筆數:" + page.getNumberOfElements());
        System.out.println("每頁筆數:" + page.getSize());
        System.out.println("全部筆數:" + page.getTotalElements());
        System.out.println("全部頁數:" + page.getTotalPages());
        System.out.println("內容:" + page.getContent());


    }

    @Test
    @Transactional
    @Rollback(value = false)
    void test7() {
        String pros = "鬼滅之刃";


        Specification<Product> sp1= (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            predicates.add(criteriaBuilder.like(root.get("productName"),"%"+pros+"%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Specification<Product> sp2 = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"), "%" + pros + "%");

        List<Product> page = pr.findAll(sp2);

        for (Product p : page) {
            p.setProductCategory(pros);
            System.out.println(p);
        }

        pr.saveAll(page);



    }

    @Test
    void test8(){

        String[] strs=new String[]{"海","賊","王"};
        int pageSize=12;
        int pageIndex=3;
        String sort="DEFAULT";


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

        Root<Product> root = criteriaQuery.from(Product.class);

        ArrayList<Predicate> predicateArrayList = new ArrayList<>();

        for (String str : strs) {
            predicateArrayList.add(criteriaBuilder.like(root.get("productName"),"%"+str+"%"));
        }
        new HashMap<>();
        Path<Long> productName = root.get( "productName" );
        Path<String> productPrice = root.get( "productPrice");

        criteriaQuery.select(criteriaBuilder.construct(Product.class,productName,productPrice));
        criteriaQuery.where(predicateArrayList.toArray(new Predicate[0]));

        ProductSortEnum productSortEnum = ProductSortEnum.valueOf(sort);
        if (productSortEnum.getSortType().equals("ASC")){
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(productSortEnum.getSortBy())));
        }else if(productSortEnum.getSortType().equals("DESC")){
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(productSortEnum.getSortBy())));
        }


        List<Product> resultList = entityManager.createQuery(criteriaQuery).setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize).getResultList();

        for (Product product : resultList) {
            System.out.println(product.getProductId()+":"+product.getProductName()+":"+product.getProductPrice()+":"+product.getProductNumsOfSale());
        }
    }

    @Test
    void test9(){

        String[] strs=new String[]{"海","賊","王"};
        int pageSize=12;
        int pageIndex=3;
        String sort="DEFAULT";

        StringBuffer buffer = new StringBuffer("SELECT p FROM Product p ");
        for (int i = 0 ; i<strs.length ; i++){
            if(i==0){
                buffer.append("WHERE productName like "+"'%"+strs[i]+"%' ");
            }else {
                buffer.append("AND productName like "+"'%"+strs[i]+"%' ");
            }
        }

        ProductSortEnum productSortEnum = ProductSortEnum.valueOf(sort);
        buffer.append("ORDER BY "+productSortEnum.getSortBy()+" "+productSortEnum.getSortType());

        System.out.println(buffer.toString());

        List<Product> resultList = entityManager.createQuery(buffer.toString()).setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize).getResultList();

        for (Product product : resultList) {
            System.out.println(product.getProductId()+":"+product.getProductName()+":"+product.getProductPrice()+":"+product.getProductNumsOfSale());
        }
    }

    @Test
    void test10(){

        String[] strs=new String[]{"海","賊","王"};
        int pageSize=12;
        int pageIndex=3;
        String sort="DEFAULT";

        StringBuffer buffer = new StringBuffer("SELECT p FROM Product p WHERE 1=1 ");
        for (int i = 0 ; i<strs.length ; i++){
            buffer.append("AND p.productName like :str"+(i+1)+" ");
        }
        buffer.append("ORDER BY :sort");

        System.out.println(buffer.toString());

        Query query = entityManager.createQuery(buffer.toString());
        for (int i = 0; i<strs.length;i++){
            query.setParameter("str"+(i+1),"%"+strs[i]+"%");
        }
        ProductSortEnum productSortEnum = ProductSortEnum.valueOf(sort);
        query.setParameter("sort",productSortEnum.getSortBy()+" "+productSortEnum.getSortType());


        List<Product> resultList = query.setFirstResult((pageIndex-1)*pageSize).setMaxResults(pageSize).getResultList();

        for (Product product : resultList) {
            System.out.println(product.getProductId()+":"+product.getProductName()+":"+product.getProductPrice()+":"+product.getProductNumsOfSale());
        }
    }
}
