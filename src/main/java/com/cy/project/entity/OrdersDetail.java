package com.cy.project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Data
public class  OrdersDetail implements Serializable {

    private static final long serialVersionUID = 250249825990717095L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordersDetailId;

    private Integer ordersDetailProductNums;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="orders_id")
    private Orders orders;

}
