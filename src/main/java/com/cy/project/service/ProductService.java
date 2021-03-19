package com.cy.project.service;


import com.cy.project.entity.Product;
import com.cy.project.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    //每頁商品數目
    private final int PAGESIZE=12;

    private final ProductRepository pr;

    public ProductService(ProductRepository productRepository){this.pr=productRepository;}

    //單一商品
    public Optional<Product> findByProductId(Integer id){
        return pr.findById(id);
    }


    //搜索全商品
    public Page<Product> findBySortPage(String sortType, String sortBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(sortType)){
            sort = Sort.by(sortBy).descending();
        }else {
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        return pr.findAll(pageable);
    }

    //搜索分類商品
    public Page<Product> findByCategorySortPage(String category,String sortType,String sortBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(sortType)){
            sort = Sort.by(sortBy).descending();
        }else {
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        return pr.findByProductCategory(category,pageable);
    }

    //like搜索單一條件
    public Page<Product> findByKeywordSortPage(String keyword, String sortType, String sortBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(sortType)){
            sort = Sort.by(sortBy).descending();
        }else {
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        Specification<Product> sp= (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"),"%"+keyword+"%");

        return pr.findAll(sp,pageable);
    }

    //like搜索多條件
    public Page<Product> findByKeywordsSortPage(String[] keywords, String sortType, String sortBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(sortType)){
            sort = Sort.by(sortBy).descending();
        }else {
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, PAGESIZE, sort);

        Specification<Product> sp= (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String keyword : keywords) {
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + keyword + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return pr.findAll(sp,pageable);
    }

}
