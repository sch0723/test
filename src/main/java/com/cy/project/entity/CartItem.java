package com.cy.project.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class CartItem implements Serializable {

    private static final long serialVersionUID = 687598572774375205L;

    private Product product;

    private Integer count;

    private Integer subTotalPrice;

}
