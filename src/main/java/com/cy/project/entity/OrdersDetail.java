package com.cy.project.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
//@EqualsAndHashCode
public class  OrdersDetail implements Serializable {

    private static final long serialVersionUID = 250249825990717095L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ordersDetailId;

    private Integer ordersDetailProductNums;

    private Integer ordersDetailTotalPrice;

    @ManyToOne(targetEntity = Product.class,fetch = FetchType.LAZY)
    @JoinColumn(name="productId",referencedColumnName = "productId")
    private Product product;

    @ManyToOne(targetEntity = Orders.class,fetch = FetchType.LAZY)
    @JoinColumn(name="ordersId",referencedColumnName = "ordersId")
    private Orders orders;

}
