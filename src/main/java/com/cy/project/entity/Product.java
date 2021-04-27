package com.cy.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Product implements Serializable {

	private static final long serialVersionUID = 2982227680155473789L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;

	private String productName;

	private Integer productPrice;

//	private String productImg;

	private Integer productNumsOfSale;

	private String productCategory;

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date productDate;

//	@OneToMany(mappedBy = "product")
//	private Set<Cart> productCart = new HashSet<>();
//
//	@OneToMany(mappedBy = "product")
//	private Set<OrdersDetail> productOrdersDetail = new HashSet<>();

}
