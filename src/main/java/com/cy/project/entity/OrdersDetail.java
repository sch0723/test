package com.cy.project.entity;

import lombok.Data;
import lombok.ToString;

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

    @ManyToOne(targetEntity = Product.class ,cascade = CascadeType.DETACH)
    @JoinColumn(name="productId",referencedColumnName = "productId")
    private Product product;

    @ManyToOne(targetEntity = Orders.class)
    @JoinColumn(name="ordersId",referencedColumnName = "ordersId")
    private Orders orders;

}
