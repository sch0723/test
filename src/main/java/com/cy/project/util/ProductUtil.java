package com.cy.project.util;

import com.cy.project.enumeration.ProductSortEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ProductUtil {
    //每頁商品數目
    private static int pageSize;
    @Value("${page.size}")
    public void setPageSize(int pageSize) {
        ProductUtil.pageSize = pageSize;
    }

    public static Pageable getPageable(String sort, Integer pageIndex){

        ProductSortEnum sortEnum = ProductSortEnum.valueOf(sort);

        Sort mySort=null;
        if("DESC".equals(sortEnum.getSortType())){
            mySort = Sort.by(sortEnum.getSortBy()).descending();
        }else if("ASC".equals(sortEnum.getSortType())){
            mySort = Sort.by(sortEnum.getSortBy()).ascending();
        }


        return PageRequest.of(pageIndex, pageSize, mySort);
    }
}
