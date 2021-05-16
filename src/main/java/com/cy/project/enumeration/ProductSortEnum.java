package com.cy.project.enumeration;

public enum ProductSortEnum {

    DEFAULT("productId","ASC"),
    PRICE_ASC("productPrice","ASC"),
    PRICE_DESC("productPrice","DESC"),
    DATE_ASC("productDate","ASC"),
    DATE_DESC("productDate","DESC"),
    SALES_ASC("productNumsOfSale","ASC"),
    SALES_DESC("productNumsOfSale","DESC");

    private String sortBy;
    private String sortType;

    private ProductSortEnum(String sortBy, String sortType) {
        this.sortBy = sortBy;
        this.sortType = sortType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortType() {
        return sortType;
    }
}
