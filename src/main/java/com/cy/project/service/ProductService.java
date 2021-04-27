package com.cy.project.service;


import com.cy.project.entity.Product;
import com.cy.project.repository.ProductRepository;
import com.cy.project.util.ProductUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional(readOnly = true)
@Cacheable("product")
public class ProductService {

    private final ProductRepository pr;

    public ProductService(ProductRepository productRepository){this.pr=productRepository;}

    //單一商品
    public Optional<Product> findByProductId(Integer id){
        return pr.findById(id);
    }


    //搜索全商品
    public Page<Product> findBySortPage(String sort, Integer pageIndex){

        return pr.findAll(ProductUtil.getPageable(sort,pageIndex));
    }

    //搜索分類商品
    public Page<Product> findByCategorySortPage(String category,String sort, Integer pageIndex){

        return pr.findByProductCategory(category,ProductUtil.getPageable(sort,pageIndex));
    }

    //like搜索單一條件
    public Page<Product> findByKeywordSortPage(String keyword, String sort, Integer pageIndex){

        Specification<Product> sp= (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("productName"),"%"+keyword+"%");

        return pr.findAll(sp,ProductUtil.getPageable(sort,pageIndex));
    }

    //like搜索多條件
    public Page<Product> findByKeywordsSortPage(String[] keywords, String sort, Integer pageIndex){

        Specification<Product> sp= (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String keyword : keywords) {
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + keyword + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return pr.findAll(sp,ProductUtil.getPageable(sort,pageIndex));
    }

}
