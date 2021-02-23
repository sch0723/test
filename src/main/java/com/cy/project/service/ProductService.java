package com.cy.project.service;


import com.cy.project.entity.Product;
import com.cy.project.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final int PAGESIZE=20;

    private final ProductRepository pr;

    public ProductService(ProductRepository productRepository){this.pr=productRepository;}


    //搜索全商品
    public List<Product> findByOrderPage(String orderType,String orderBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(orderType)){
            sort = Sort.by(orderBy).descending();
        }else {
            sort = Sort.by(orderBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex-1, PAGESIZE, sort);

        return pr.findAll(pageable).getContent();
    }

    //搜索分類商品
    public List<Product> findByCategoryOrderPage(String category,String orderType,String orderBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(orderType)){
            sort = Sort.by(orderBy).descending();
        }else {
            sort = Sort.by(orderBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex-1, PAGESIZE, sort);

        return pr.findByproductCategory(category,pageable);
    }

    //模糊搜索單一條件
    public List<Product> findByKeywordOrderPage(String keyword,String orderType,String orderBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(orderType)){
            sort = Sort.by(orderBy).descending();
        }else {
            sort = Sort.by(orderBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex-1, PAGESIZE, sort);

        Specification<Product> sp= (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"),"%"+keyword+"%");

        return pr.findAll(sp,pageable).getContent();
    }

    //模糊搜索多條件
    public List<Product> findByKeywordsOrderPage(String[] keywords,String orderType,String orderBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(orderType)){
            sort = Sort.by(orderBy).descending();
        }else {
            sort = Sort.by(orderBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex-1, PAGESIZE, sort);

        Specification<Product> sp= (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            for (String keyword : keywords) {
                predicates.add(criteriaBuilder.like(root.get("productName"),"%"+keyword+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return pr.findAll(sp,pageable).getContent();
    }

}
