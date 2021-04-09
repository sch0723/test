package com.cy.project.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductUtil {

    private static final int PAGESIZE = 12;

    public static Pageable getPageable(String sortType, String sortBy, Integer pageIndex){
        Sort sort;
        if("DESC".equals(sortType)){
            sort = Sort.by(sortBy).descending();
        }else {
            sort = Sort.by(sortBy).ascending();
        }
        return PageRequest.of(pageIndex, PAGESIZE, sort);
    }
}
