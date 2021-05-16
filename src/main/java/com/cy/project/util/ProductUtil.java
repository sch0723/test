package com.cy.project.util;

import com.cy.project.enumeration.ProductSortEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductUtil {
    //每頁商品數目
    private static final int PAGESIZE = 12;

    public static Pageable getPageable(String sort, Integer pageIndex){

        ProductSortEnum sortEnum = ProductSortEnum.valueOf(sort);

        Sort mySort=null;
        if("DESC".equals(sortEnum.getSortType())){
            mySort = Sort.by(sortEnum.getSortBy()).descending();
        }else if("ASC".equals(sortEnum.getSortType())){
            mySort = Sort.by(sortEnum.getSortBy()).ascending();
        }


        return PageRequest.of(pageIndex, PAGESIZE, mySort);
    }
}
