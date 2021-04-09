package com.cy.project.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductUtil {
    //每頁商品數目
    private static final int PAGESIZE = 12;

    public static Pageable getPageable(String sort, Integer pageIndex){

        String sortType = "ASC";
        String sortBy = "productId";
        if ("PriceDESC".equals(sort)) {
            sortType = "DESC";
            sortBy = "productPrice";

        } else if ("PriceASC".equals(sort)) {
            sortType = "ASC";
            sortBy = "productPrice";
        }

        Sort mySort;
        if("DESC".equals(sortType)){
            mySort = Sort.by(sortBy).descending();
        }else {
            mySort = Sort.by(sortBy).ascending();
        }
        return PageRequest.of(pageIndex, PAGESIZE, mySort);
    }
}
